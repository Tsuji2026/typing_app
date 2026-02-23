package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button mSound_button;
    public static List<ListData> typingList;
    public static int typingList_max_num;
    public static int typingList_max_index;
    private Intent intent_PlaySoundService;
    private DatabaseHelper databaseHelper;
    private Boolean not_stop_sound;
    private TextView mSound_change;
    private String mSound_change_text_str;
    private Integer mSound_now;
    private AudioManager mAudioManager;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("MainActivity", "call onCreate()");
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // お題データを取得
        databaseHelper = new DatabaseHelper(this);
        typingList = new ArrayList<ListData>();
        long newRowId = 0;

        typingList_max_num = databaseHelper.getAllTyping(typingList);
        if (typingList_max_num == 0) {
            int id = 0;
            int i;
            typingList_max_num = read_csv(typingList, getApplicationContext());
            typingList_max_index = typingList_max_num;

            for(i = 0; i < typingList_max_num; i++) {
                newRowId = databaseHelper.setTyping(typingList.get(id).getId(), typingList.get(id).getTextData_str(), typingList.get(id).getTextData_len());
                if(newRowId != -1){
                    id++;
                }
            }
        }

        // 音楽再生／停止ボタン
        mSound_button = findViewById(R.id.sound_button);
        mSound_button.setBackgroundColor(getResources().getColor(R.color.color_sound_on, getTheme()));
        mSound_button.setText(getResources().getText(R.string.str_sound_on_button_name));
        intent_PlaySoundService = new Intent(this, PlaySoundService.class);
        not_stop_sound = false;

        // 音量変更
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        mSound_now = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mSound_change_text_str = "音量：";
        mSound_change = findViewById(R.id.sound_volume_change_name);
        String tmp_str = mSound_change_text_str + String.valueOf(mSound_now);
        mSound_change.setText(tmp_str);
    }

    @Override
    protected void onStart() {
        Log.i("MainActivity", "call onStart()");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.i("MainActivity", "call onResume()");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.i("MainActivity", "call onPause()");
//        check_PlaySoundService();
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i("MainActivity", "call onStop()");
        super.onStop();
    }

    @Override
    protected void onRestart() {
        Log.i("MainActivity", "call onRestart()");
        not_stop_sound = false;
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        Log.i("MainActivity", "call onDestroy()");
//        check_PlaySoundService();
        databaseHelper.close();
        super.onDestroy();
    }

    // PlaySoundService起動チェック
    private void check_PlaySoundService() {
        if(!not_stop_sound) {
            ActivityManager manager = (ActivityManager) getSystemService((Context.ACTIVITY_SERVICE));
            for (ActivityManager.RunningServiceInfo serviceInfo : manager.getRunningServices((Integer.MAX_VALUE))) {
                if (PlaySoundService.class.getName().equals(serviceInfo.service.getClassName())) {
                    stopService(intent_PlaySoundService);
                }
            }
        }
    }

    // タイピング練習開始ボタン押下
    public void click_start_typing(View v) {
        Log.i("MainActivity", "call click_typing_start()");
        not_stop_sound = true;
        Intent intent = new Intent(this, TypingActivity.class);
        startActivity(intent);
    }

    // 問題追加ボタン押下
    public void click_add_question(View view) {
        Log.i("MainActivity", "call click_add_question()");
        not_stop_sound = true;
        Intent intent = new Intent(this, QuestionChangeActivity.class);
        startActivity(intent);
    }

    // jniボタン押下
    public void click_typing_jni(View view) {
        Log.i("MainActivity", "call click_typing_jni()");
        not_stop_sound = true;
        Intent intent = new Intent(this, JniActivity.class);
        startActivity(intent);
    }

    // タイピング練習終了ボタン押下
    public void click_typing_end(View v) {
        Log.i("MainActivity", "call click_typing_end()");
        check_PlaySoundService();
        this.finish();
    }

    // 音楽再生／停止ボタン押下
    public void click_change_sound(View v) {
        Log.i("MainActivity", "call click_sound_change()");
        if (String.valueOf(mSound_button.getText()).equals(getString(R.string.str_sound_off_button_name))) {
            Log.i("MainActivity", "call click_sound_change() sound_off");
            mSound_button.setBackgroundColor(getResources().getColor(R.color.color_sound_on, getTheme()));
            mSound_button.setText(getResources().getText(R.string.str_sound_on_button_name));
            check_PlaySoundService();
        } else {
            Log.i("MainActivity", "call click_sound_change() sound_on");
            mSound_button.setBackgroundColor(getResources().getColor(R.color.color_sound_off, getTheme()));
            mSound_button.setText(getResources().getText(R.string.str_sound_off_button_name));
            startService(intent_PlaySoundService);
        }
    }

    // 問題が記載されているCSVファイルを読み込み
    public int read_csv(List<ListData> list, Context context) {
        int ret_max = 0;
        AssetManager assetManager = context.getAssets();

        Log.i("MainActivity", "call read_csv()");
        try {
            // CSVファイルの読み込み
            InputStream inputStream = assetManager.open("text1.csv");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferReader = new BufferedReader(inputStreamReader);
            String line;

            while ((line = bufferReader.readLine()) != null) {
                //カンマ区切りで１つづつ配列に入れる
                ListData data = new ListData();
                String[] RowData = line.split(",");

                //CSVの左([0]番目)から順番にセット
                data.setId(Integer.parseInt(RowData[0]));
                data.setTextData_str(RowData[1]);
                data.setTextData_len(Integer.parseInt(RowData[2]));
                list.add(data);
                ret_max++;
            }
            bufferReader.close();
        } catch (IOException e){
            typingList_max_num = 0;
            Log.e("CsvRead", "IOException:" + e.getMessage());
        }
        return ret_max;
    }

    // 音量変更：音量UP
    public void click_change_sound_volume_up(View view) {
        Log.i("MainActivity", "call click_change_sound_volume_up()");
        mSound_now++;
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mSound_now, 0);
        String tmp_str = mSound_change_text_str + String.valueOf(mSound_now);
        mSound_change.setText(tmp_str);
    }
    // 音量変更：音量DOWN
    public void click_change_sound_volume_down(View view) {
        Log.i("MainActivity", "call click_change_sound_volume_down()");
        mSound_now--;
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mSound_now, 0);
        String tmp_str = mSound_change_text_str + String.valueOf(mSound_now);
        mSound_change.setText(tmp_str);
    }
}
