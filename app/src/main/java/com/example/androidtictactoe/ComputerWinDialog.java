package com.example.androidtictactoe;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

import androidx.annotation.NonNull;

public class ComputerWinDialog extends Dialog {

    private final Activity activity;
    private final String message;

    public ComputerWinDialog(@NonNull Context context, String message2) {
        super(context);
        message = message2;
        activity = (Activity) context;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.computer_win_dialog_layout);
        ((TextView) findViewById(R.id.messageTV)).setText(message);
        AppCompatButton startNew = findViewById(R.id.startNewBtn);
        startNew.setOnClickListener(v -> {
            dismiss();
            getContext().startActivity(new Intent(activity, activity.getClass()));
            activity.finish();
        });
    }
}
