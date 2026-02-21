package com.example.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class QuestionChangeDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Log.i("QuestionAddDialog", "call onCreateDialog()");
        return new MaterialAlertDialogBuilder(requireActivity())
            .setMessage(QuestionChangeActivity.question_change_dialog_text)
            .setPositiveButton("OK", (dialog, id) -> {
                rewrite();
                })
            .create();
    }

    private void rewrite(){
        Log.i("QuestionAddDialog", "call rewrite()");
//        QuestionChangeActivity activity = new QuestionChangeActivity();
//        activity.show_typing_database();
    }
}