package com.example.androidtictactoe;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class OnlineWinDialog extends Dialog{

    private final String message;
    private final   OnlinePlay onlinePlay;

    public OnlineWinDialog(@NonNull Context context, String message) {
        super(context);
        this.message = message;
        this.onlinePlay = ((OnlinePlay)context);
    }

    @Override
    protected void  onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.online_win_dialog_layout);

        final TextView messageTV = findViewById(R.id.messageTV);
        final Button startBtn = findViewById(R.id.startNewBtn);

        messageTV.setText(message);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                getContext().startActivity(new Intent(getContext(), OnlineName.class));
                onlinePlay.finish();
            }
        });
    }
}
