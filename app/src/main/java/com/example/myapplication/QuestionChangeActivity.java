package com.example.myapplication;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;
import android.Manifest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionChangeActivity extends AppCompatActivity {
    public static String question_change_dialog_text;
    private DatabaseHelper mDatabaseHelper;
    private Handler mHandler = new Handler();
    private Runnable update_change_answer;
    private TextView change_answer;

    private TextView mMike_status;
    private TextView mMike_input_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("QuestionAddActivity", "call onCreate()");

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_question_change);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mMike_status = findViewById(R.id.mike_status);
        mMike_input_text = findViewById(R.id.add_voice_text_id);

        change_answer = findViewById(R.id.change_answer);
        mDatabaseHelper = new DatabaseHelper(getApplicationContext());

        update_change_answer = new Runnable() {
            public void run() {
                change_answer.setText("");
            }
        };

        show_typing_database();
    }

    @Override
    protected void onStart() {
        Log.i("QuestionAddActivity", "call onStart()");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.i("QuestionAddActivity", "call onResume()");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.i("QuestionAddActivity", "call onPause()");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i("QuestionAddActivity", "call onStop()");
        super.onStop();
    }

    @Override
    protected void onRestart() {
        Log.i("QuestionAddActivity", "call onRestart()");
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        Log.i("QuestionAddActivity", "call onDestroy()");
        super.onDestroy();
    }

    public void show_typing_database() {
        List<ListData> tmp_typingList = new ArrayList<ListData>();
        int tmp_typingList_max;

        tmp_typingList_max = mDatabaseHelper.getAllTyping(tmp_typingList);
        // リストビューにデータ表示
        ArrayList<Map<String, String>> listData_show = new ArrayList<>();
        for(ListData list_tmp :tmp_typingList)
        {
            Map<String, String> item = new HashMap<>();
            item.put("text_data_id", "id:" + String.valueOf(list_tmp.getId()));
            item.put("text_data_str", list_tmp.getTextData_str());
            listData_show.add(item);
        }

        ListView userListView = findViewById(R.id.userListView);
        userListView.setAdapter(new SimpleAdapter(this,
                                                  listData_show,
                                                  android.R.layout.simple_list_item_2,
                                                  new String[] {"text_data_id", "text_data_str"},
                                                  new int[] {android.R.id.text1, android.R.id.text2}
                                                 )
                               );
    }

    // 問題追加
    public void click_add_csv_text(View view) {
        Log.i("QuestionAddActivity", "call click_add_csv_text()");

        TextView add_text = findViewById(R.id.add_text_id);
        String tmp_text = String.valueOf(add_text.getText());
        int tmp_text_len = Integer.parseInt(String.valueOf(add_text.length()));

        add_csv(tmp_text, tmp_text_len);
        add_text.setText("");
    }

    public void click_add_csv_voice(View view) {
        Log.i("QuestionAddActivity", "call click_add_csv_voice()");

        TextView add_text = findViewById(R.id.add_voice_text_id);
        String tmp_text = String.valueOf(add_text.getText());
        int tmp_text_len = Integer.parseInt(String.valueOf(add_text.length()));

        add_csv(tmp_text, tmp_text_len);
        add_text.setText("");
    }

    private void add_csv(String text, int len) {
        Log.i("QuestionAddActivity", "call click_add()");

        ListData add_data = new ListData();
        long newRowId;
        int i;
        int tmp_max_index;

        add_data.setId(MainActivity.typingList_max_index + 1);
        add_data.setTextData_str(text);
        add_data.setTextData_len(len);

        i = 0;
        tmp_max_index = MainActivity.typingList.get(i).getId();
        for(i = 1; i < MainActivity.typingList_max_num; i++) {
            if(tmp_max_index < MainActivity.typingList.get(i).getId()){
                tmp_max_index = MainActivity.typingList.get(i).getId();
            }
        }

        MainActivity.typingList_max_index = tmp_max_index + 1;

        newRowId = mDatabaseHelper.setTyping(MainActivity.typingList_max_index, text, len);

        if(newRowId != -1){
            MainActivity.typingList_max_num = mDatabaseHelper.getAllTyping(MainActivity.typingList);

            change_answer.setText(String.valueOf(getResources().getText(R.string.str_change_answer_add)));
            mHandler.postDelayed(update_change_answer, 3000);

//            question_change_dialog_text = String.valueOf(getResources().getText(R.string.str_dialog_add));
//            DialogFragment dialogFragment = new QuestionChangeDialog();
//            dialogFragment.show(getSupportFragmentManager(), "question_change_dialog");

            show_typing_database();
        }
    }

    public void click_delete_csv(View view) {
        Log.i("QuestionAddActivity", "call click_delete_csv()");
        int del_id;
        TextView del_text_id;
        int del_row = 0;

        del_text_id = findViewById(R.id.del_text_id);
        del_id = Integer.parseInt(String.valueOf(del_text_id.getText()));

        del_row = mDatabaseHelper.deleteTyping(del_id);
        if(del_row > 0) {
            int i = 0;
            int tmp_max_index;
            MainActivity.typingList_max_num = mDatabaseHelper.getAllTyping(MainActivity.typingList);

            tmp_max_index = MainActivity.typingList.get(i).getId();
            for (i = 1; i < MainActivity.typingList_max_num; i++) {
                if (tmp_max_index < MainActivity.typingList.get(i).getId()) {
                    tmp_max_index = MainActivity.typingList.get(i).getId();
                }
            }
            MainActivity.typingList_max_index = tmp_max_index;

            del_text_id.setText("");
            change_answer.setText(String.valueOf(getResources().getText(R.string.str_change_answer_del)));
            mHandler.postDelayed(update_change_answer, 3000);

//            question_change_dialog_text = String.valueOf(getResources().getText(R.string.str_dialog_del));
//            DialogFragment dialogFragment = new QuestionChangeDialog();
//            dialogFragment.show(getSupportFragmentManager(), "question_change_dialog");

            show_typing_database();
        }
    }

    public void click_karaoke_mike(View view) {
        Log.i("QuestionAddActivity", "call click_karaoke_mike()");
        String tmp;

        // 状態が録音中の場合は、何もしない。
        if(mMike_status.getText().toString() .equals(getString(R.string.str_mike_status_start_text))){
            return;
        }

        // 権限チェック
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
            mMike_status.setText("再実行してください。");
            return;
        }

        // 音声認識セットアップ
        SpeechRecognizer recognizer = SpeechRecognizer.createSpeechRecognizer(this);
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");

        recognizer.setRecognitionListener(
                new RecognitionListener() {
                    @Override
                    // 音声入力終了、結果が準備OK
                    public void onResults(Bundle results) {
                        Log.i("QuestionAddActivity", "call onResults()");
                        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                        if (matches != null && matches.size() > 0) {
                            String tmp = matches.get(0);
                            mMike_input_text.setText(tmp);
                        }
                        mMike_status.setText("停止中");
                    }

                    @Override
                    // ネットワークエラー、音声入力に関するエラーが発生
                    public void onError(int error) {
                        Log.i("QuestionAddActivity", "call onError()");
                        mMike_status.setText("再実行してください。");
                    }

                    @Override
                    // 準備が整い発話OK状態
                    public void onReadyForSpeech(Bundle params) {
                        Log.i("QuestionAddActivity", "call onReadyForSpeech()");
                    }

                    @Override
                    // 発話開始
                    public void onBeginningOfSpeech() {
                        Log.i("QuestionAddActivity", "call onBeginningOfSpeech()");
                    }

                    @Override
                    // 音声のレベルが変化
                    public void onRmsChanged(float rmsdB) {
                        Log.i("QuestionAddActivity", "call onRmsChanged()");
                    }

                    @Override
                    // 音声受信
                    public void onBufferReceived(byte[] buffer) {
                        Log.i("QuestionAddActivity", "call onBufferReceived()");
                    }

                    @Override
                    // 発話終了
                    public void onEndOfSpeech() {
                        Log.i("QuestionAddActivity", "call onEndOfSpeech()");
                    }

                    @Override
                    // 部分的な認識結果が利用可能
                    public void onPartialResults(Bundle partialResults) {
                        Log.i("QuestionAddActivity", "call onPartialResults()");
                    }

                    @Override
                    // 追加イベントを受信
                    public void onEvent(int eventType, Bundle params) {
                        Log.i("QuestionAddActivity", "call onEvent()");
                    }
                }
        );

        // 録音開始
        recognizer.startListening(intent);
        mMike_status.setText("録音中...");
    }

    public void click_go_home(View v) {
        Log.i("QuestionAddActivity", "call click_go_home()");
        this.finish();
    }
}