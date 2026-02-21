package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TypingActivity extends AppCompatActivity implements TextWatcher {
    private TextView mQuestion_text;
    private String mQuestion_text_str;
    private EditText mInput_text;
    private TextView mExplanation_text;
    private TextView mNumber_of_correct_answers;
    private int mCorrect_answer_num_str;
    private String mCorrect_answer_text_str;
    private int mCsvList_index;
    private ListData mCsvList_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("TypingActivity", "call onCreate()");
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_typing);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 説明
        mExplanation_text = findViewById(R.id.explanation_text);
        mExplanation_text.setText(getString(R.string.str_explanation_text));

        // お題設定
        mQuestion_text = findViewById(R.id.question_text);
        mCsvList_index = 0;
        mQuestion_text = findViewById(R.id.question_text);
        mCsvList_data = MainActivity.typingList.get(mCsvList_index);
        mQuestion_text_str = mCsvList_data.getTextData_str();
        mQuestion_text.setText(mQuestion_text_str);
        mCsvList_index++;

        // 入力文字
        mInput_text = findViewById(R.id.input_text);
        // リスナーを登録
//        mInput_text.addTextChangedListener(this); // 文字１つを入力した時に呼び出される
        // Enterを押した時に呼び出される
        mInput_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.i("TypingActivity", "call onStart()");
                if(event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if(event.getAction() == KeyEvent.ACTION_DOWN) {
                        String tmp1 = String.valueOf(mQuestion_text.getText());
                        String tmp2 = String.valueOf(mInput_text.getText());
                        if(String.valueOf(mQuestion_text.getText()).equals(String.valueOf(mInput_text.getText()))) {
                            if(mCsvList_index != MainActivity.typingList_max_num) {
                                // お題設定
                                set_next_question();
                            } else {
                                // 最後まで行ったので終了表示
                                call_TypingFinishActivity();
                            }
                        }
                        return true;
                    }
                }
                return false;
            }
        });

        // 正解数
        mCorrect_answer_text_str = getString(R.string.str_correct_answer_text);
        mCorrect_answer_num_str = 0;
        mNumber_of_correct_answers = findViewById(R.id.number_of_correct_answers);
        mNumber_of_correct_answers.setText(mCorrect_answer_num_str + mCorrect_answer_text_str);
    }

    private void set_next_question() {
        mCsvList_data = MainActivity.typingList.get(mCsvList_index);
        mQuestion_text_str = mCsvList_data.getTextData_str();
        mQuestion_text.setText(mQuestion_text_str);
        mCsvList_index++;

        mInput_text.setText("");

        mCorrect_answer_num_str++;
        mNumber_of_correct_answers.setText(mCorrect_answer_num_str + mCorrect_answer_text_str);
    }

    private void call_TypingFinishActivity(){
        Intent intent = new Intent(this, TypingFinishActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    protected void onStart() {
        Log.i("TypingActivity", "call onStart()");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.i("TypingActivity", "call onResume()");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.i("TypingActivity", "call onPause()");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i("TypingActivity", "call onStop()");
        super.onStop();
    }

    @Override
    protected void onRestart() {
        Log.i("TypingActivity", "call onRestart()");
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        Log.i("TypingActivity", "call onDestroy()");
        super.onDestroy();
    }

    // 文字列が修正される直前に呼び出されるメソッド
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        Log.i("TypingActivity", "call beforeTextChanged() :" + s.toString());
    }

    // 文字列が修正される直前に呼び出されるメソッド
    @Override
    public void afterTextChanged(Editable s) {
        Log.i("TypingActivity", "call afterTextChanged() :" + s.toString());
    }

    // 文字１つを入力した時に呼び出される
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String str_input= s.toString();

        Log.i("TypingActivity", "call onTextChanged() str_input:" + str_input);

        if(str_input.equals(mQuestion_text_str)) {
            if (mCsvList_index != MainActivity.typingList_max_num) {
                Log.i("TypingActivity", "call onTextChanged() typing next)");
                // お題設定
                set_next_question();
            } else {
                // 最後まで行ったので終了表示
                call_TypingFinishActivity();
            }
        }
    }

    public void click_go_home(View v) {
        Log.i("TypingActivity", "call click_go_home()");
        this.finish();
    }
}