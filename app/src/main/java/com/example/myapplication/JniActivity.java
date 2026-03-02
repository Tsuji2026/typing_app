package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class JniActivity extends AppCompatActivity {
    private TextView mJni_text;
    private TextView mJni_text_2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("JniActivity", "call onCreate()");
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_jni);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.jni_activity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mJni_text = findViewById(R.id.get_jni_text_id);
        mJni_text_2 = findViewById(R.id.get_jni_text_id_2);
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
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        Log.i("MainActivity", "call onDestroy()");
        super.onDestroy();
    }

    // ネイティブメソッド定義
    public native String getMessageFromJNI_no_input();    // タイピング練習開始ボタン押下
    public native String getMessageFromJNI_input(int num, String str);    // タイピング練習開始ボタン押下

    // ネイティブメソッド読み込み
    static {
        System.loadLibrary("mylib");
    }

    public void click_get_jni(View v) {
        Log.i("MainActivity", "call click_get_jni()");
        // ネイティブメソッド実行
        String tmp_msg = getMessageFromJNI_no_input();
        mJni_text.setText(tmp_msg);
    }

    public void click_get_jni_2(View v) {
        Log.i("MainActivity", "call click_get_jni()");
        // ネイティブメソッド実行
        String tmp_msg = getMessageFromJNI_input(123, "test");
        mJni_text_2.setText(tmp_msg);
    }


}

