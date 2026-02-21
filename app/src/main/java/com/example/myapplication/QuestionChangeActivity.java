package com.example.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;

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
    public void click_add_csv(View view) {
        Log.i("QuestionAddActivity", "call click_add()");
        TextView add_text;
        String add_question_text;
        int add_question_text_len;
        ListData add_data = new ListData();
        long newRowId;
        int i;
        int tmp_max_index;

        add_text = findViewById(R.id.add_text);
        add_question_text = String.valueOf(add_text.getText());
        add_question_text_len = Integer.parseInt(String.valueOf(add_text.length()));
        add_data.setId(MainActivity.typingList_max_index + 1);
        add_data.setTextData_str(add_question_text);
        add_data.setTextData_len(add_question_text_len);

        i = 0;
        tmp_max_index = MainActivity.typingList.get(i).getId();
        for(i = 1; i < MainActivity.typingList_max_num; i++) {
            if(tmp_max_index < MainActivity.typingList.get(i).getId()){
                tmp_max_index = MainActivity.typingList.get(i).getId();
            }
        }

        MainActivity.typingList_max_index = tmp_max_index + 1;

        newRowId = mDatabaseHelper.setTyping(MainActivity.typingList_max_index, add_question_text, add_question_text_len);

        if(newRowId != -1){
            MainActivity.typingList_max_num = mDatabaseHelper.getAllTyping(MainActivity.typingList);

            add_text.setText("");
            question_change_dialog_text = String.valueOf(getResources().getText(R.string.str_dialog_add));

            change_answer.setText(String.valueOf(getResources().getText(R.string.str_change_answer_add)));
            mHandler.postDelayed(update_change_answer, 3000);

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
            MainActivity.typingList_max_num = mDatabaseHelper.getAllTyping(MainActivity.typingList);

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
            question_change_dialog_text = String.valueOf(getResources().getText(R.string.str_dialog_del));

            change_answer.setText(String.valueOf(getResources().getText(R.string.str_change_answer_del)));
            mHandler.postDelayed(update_change_answer, 3000);

//            DialogFragment dialogFragment = new QuestionChangeDialog();
//            dialogFragment.show(getSupportFragmentManager(), "question_change_dialog");

            show_typing_database();
        }
    }

    public void click_go_home(View v) {
        Log.i("QuestionAddActivity", "call click_go_home()");
        this.finish();
    }
}