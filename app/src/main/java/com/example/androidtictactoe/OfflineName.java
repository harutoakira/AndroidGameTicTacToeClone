package com.example.androidtictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class OfflineName extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_name);

        final EditText playerOne = findViewById(R.id.playerOneName);
        final EditText playerTwo = findViewById(R.id.playerTwoName);
        final Button startGameBtn = findViewById(R.id.startGameBtn);

        startGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String getPlayerOneName = playerOne.getText().toString();
                final String getPlayerTwoName = playerTwo.getText().toString();

                if (getPlayerOneName.isEmpty() || getPlayerTwoName.isEmpty()) {
                    Toast.makeText(OfflineName.this, "Vui lòng nhập tên của bạn", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(OfflineName.this, OfflinePlay.class);
                    intent.putExtra("Người chơi 1", getPlayerOneName);
                    intent.putExtra("Người chơi 2", getPlayerTwoName);
                    startActivity(intent);
                }
            }
        });
    }
}