package com.example.androidtictactoe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class OnlineName extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_name);

        final EditText playerNameEt = findViewById(R.id.playerNameEt);
        final AppCompatButton startGameBtn = findViewById(R.id.startGameBtn);

        startGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String getPlayerName = playerNameEt.getText().toString();

                if (getPlayerName.isEmpty()) {
                    Toast.makeText(OnlineName.this, "Vui lòng nhập tên của bạn", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(OnlineName.this, OnlinePlay.class);
                    intent.putExtra("playerName", getPlayerName);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}