package com.example.androidtictactoe;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class OfflineWinDialog extends Dialog {

    private final String message;
    private final OfflinePlay offlinePlay;

    public OfflineWinDialog(@NonNull Context context, String message, OfflinePlay offlinePlay) {
        super(context);
        this.message = message;
        this.offlinePlay = offlinePlay;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.offline_win_dialog_layout);

        final TextView messageTxt = findViewById(R.id.messageTxt);
        final Button starAgainBtn = findViewById(R.id.startAgainBtn);

        messageTxt.setText(message);

        starAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                offlinePlay.restartMatch();
                dismiss();
            }
        });
    }
}
