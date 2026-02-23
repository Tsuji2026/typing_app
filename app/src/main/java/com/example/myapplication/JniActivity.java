package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class JniActivity extends AppCompatActivity {
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

    // タイピング練習開始ボタン押下
    public void click_get_jni(View v) {
        Log.i("MainActivity", "call click_get_jni()");
    }
}

