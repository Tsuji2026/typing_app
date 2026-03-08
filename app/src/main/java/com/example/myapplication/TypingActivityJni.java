package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TypingActivityJni extends AppCompatActivity implements TextWatcher {
    private TextView mQuestion_text;
    private String mQuestion_text_str;
    private int mQuestion_text_len;
    private EditText mInput_text;
    private TextView mExplanation_text;
    private TextView mNumber_of_correct_answers;
    private int mCorrect_answer_num_str;
    private String mCorrect_answer_text_str;
    private int mCsvList_index;
    private ListData mCsvList_data;
    private Boolean all_match;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("TypingActivityJni", "call onCreate()");
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
        mQuestion_text_len = mCsvList_data.getTextData_len();

        // 入力文字
        mInput_text = findViewById(R.id.input_text);
        // リスナーを登録
        mInput_text.addTextChangedListener(this); // 文字１つを入力した時に呼び出される
        // 不一致文字数カウント
        all_match = true;

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
        mQuestion_text_len = mCsvList_data.getTextData_len();

        mInput_text.removeTextChangedListener(this);
        mInput_text.setText("");
        mInput_text.addTextChangedListener(this);

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
        Log.i("TypingActivityJni", "call onStart()");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.i("TypingActivityJni", "call onResume()");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.i("TypingActivityJni", "call onPause()");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i("TypingActivityJni", "call onStop()");
        super.onStop();
    }

    @Override
    protected void onRestart() {
        Log.i("TypingActivityJni", "call onRestart()");
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        Log.i("TypingActivityJni", "call onDestroy()");
        super.onDestroy();
    }

    // 文字列が修正される直前に呼び出されるメソッド
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        Log.i("TypingActivityJni", "call beforeTextChanged() :" + s.toString());
    }

    // 文字列が修正される直前に呼び出されるメソッド
    @Override
    public void afterTextChanged(Editable s) {
        Log.i("TypingActivityJni", "call afterTextChanged() :" + s.toString());
    }

    // 文字１つを入力した時に呼び出されるs
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // 空文字、開始位置0、開始位置が文字列を超えている場合（削除キー押下）
        if(s.length() == 0 || start < 0 || start >= s.length()){
            return;
        }
        String textbox_str = s.toString();
        char input_char = textbox_str.charAt(start);     // 入力文字（1文字）
        int input_char_pos = start + 1;     // 入力文字（1文字）が何文字目か

        Log.i("TypingActivityJni", "call onTextChanged() textbox_str:" + textbox_str
                + ", input_char:" + input_char + ", input_char_pos:" + input_char_pos);

        // 入力文字判定：不一致の場合、入力文字を赤文字に、不一致カウントをカウントアップ
        if(false == checkInputStrRed(input_char, input_char_pos, mQuestion_text_str)) {
            Log.i("TypingActivityJni", "call onTextChanged() input char no match input_char:"
                    + input_char + "input_char_pos:" + input_char_pos);
            //入力した文字を赤文字
            SpannableStringBuilder ssb = new SpannableStringBuilder(mInput_text.getText());
            ssb.setSpan(
                    new ForegroundColorSpan(Color.RED),
                    input_char_pos - 1,  // 開始
                    input_char_pos,  // 終了
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );

            mInput_text.removeTextChangedListener(this);
            mInput_text.setText(ssb);
            mInput_text.setSelection(ssb.length());
            mInput_text.addTextChangedListener(this);
        }

        // 入力文字列判定
        all_match = checkMatch(textbox_str, mQuestion_text_str);

        // 最後まで入力＆入力文字に不一致がない場合、次のお題or終了表示
        if((s.length() == mQuestion_text_len) && (all_match == true)) {
            // 最終問題までいっていない
            if(mCsvList_index != MainActivity.typingList_max_num){
                // お題設定
                Log.i("TypingActivityJni", "call onTextChanged() typing next");
                set_next_question();
                all_match = false;
            } else {
                // 最後まで行ったので終了表示
                Log.i("TypingActivityJni", "call onTextChanged() typing finish");
                call_TypingFinishActivity();
            }
        }
    }

    // ネイティブメソッド定義
    public native boolean checkInputStrRed(char input, int input_pos, String question_str);
    public native boolean checkMatch(String input_str, String question_str);

    // ネイティブメソッド読み込み
    static {
        System.loadLibrary("mylib");
    }

    public void click_go_home(View v) {
        Log.i("TypingActivityJni", "call click_go_home()");
        this.finish();
    }
}