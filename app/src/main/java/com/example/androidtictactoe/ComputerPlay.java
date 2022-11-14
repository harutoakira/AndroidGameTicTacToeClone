package com.example.androidtictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ComputerPlay extends AppCompatActivity {

    private ImageView image1, image2, image3, image4, image5, image6, image7, image8, image9;
    private LinearLayout player1Layout, player2Layout;

    private static final int[] computerTurns = {1, 3, 2, 9, 5, 7, 4, 6, 8};
    private static final int[] computerTurns2 = {3, 7, 5, 1, 4, 9, 2, 6, 8};
    private static final int[] computerTurns3 = {1, 5, 9, 7, 5, 3, 2, 4, 6};
    private static final int[] computerTurns4 = {1, 7, 4, 3, 5, 9, 8, 6, 2};
    private final String[] boxesSelectedBy = {"", "", "", "", "", "", "", "", ""};
    private final List<int[]> combinationsList = new ArrayList();
    private int[] data = null;

    private final List<String> doneBoxes = new ArrayList();

    public int playerTurn = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_computer_play);

        player1Layout = findViewById(R.id.player1Layout);
        player2Layout = findViewById(R.id.player2Layout);

        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);
        image4 = findViewById(R.id.image4);
        image5 = findViewById(R.id.image5);
        image6 = findViewById(R.id.image6);
        image7 = findViewById(R.id.image7);
        image8 = findViewById(R.id.image8);
        image9 = findViewById(R.id.image9);

        TextView player1TV = findViewById(R.id.player1TV);
        TextView player2TV = findViewById(R.id.player2TV);

        int position = new Random().nextInt(4);

        if (position == 0) {
            this.data = computerTurns;
        } else if (position == 1) {
            this.data = computerTurns2;
        } else if (position == 2) {
            this.data = computerTurns3;
        } else {
            this.data = computerTurns4;
        }
        Log.e("klajsfkljafssaf", "asd" + Arrays.toString(this.data));

        combinationsList.add(new int[]{0, 1, 2});
        combinationsList.add(new int[]{3, 4, 5});
        combinationsList.add(new int[]{6, 7, 8});
        combinationsList.add(new int[]{0, 3, 6});
        combinationsList.add(new int[]{1, 4, 7});
        combinationsList.add(new int[]{2, 5, 8});
        combinationsList.add(new int[]{2, 4, 6});
        combinationsList.add(new int[]{0, 4, 8});

        final String getPlayerName = getIntent().getStringExtra("playerName");

        player1TV.setText(getPlayerName);
        player2TV.setText("Computer");

        image1.setOnClickListener(v -> {
            if (playerTurn == 1 && !doneBoxes.contains("1")) {
                ComputerPlay computerPlay = ComputerPlay.this;
                computerPlay.selectBox(computerPlay.image1, 1, playerTurn);
            }
        });

        image2.setOnClickListener(v -> {
            if (playerTurn == 1 && !doneBoxes.contains("2")) {
                ComputerPlay computerPlay = ComputerPlay.this;
                computerPlay.selectBox(computerPlay.image2, 2, playerTurn);
            }
        });

        image3.setOnClickListener(v -> {
            if (playerTurn == 1 && !doneBoxes.contains("3")) {
                ComputerPlay computerPlay = ComputerPlay.this;
                computerPlay.selectBox(computerPlay.image3, 3, playerTurn);
            }
        });

        image4.setOnClickListener(v -> {
            if (playerTurn == 1 && !doneBoxes.contains("4")) {
                ComputerPlay computerPlay = ComputerPlay.this;
                computerPlay.selectBox(computerPlay.image4, 4, playerTurn);
            }
        });

        image5.setOnClickListener(v -> {
            if (playerTurn == 1 && !doneBoxes.contains("5")) {
                ComputerPlay computerPlay = ComputerPlay.this;
                computerPlay.selectBox(computerPlay.image5, 5, playerTurn);
            }
        });

        image6.setOnClickListener(v -> {
            if (playerTurn == 1 && !doneBoxes.contains("6")) {
                ComputerPlay computerPlay = ComputerPlay.this;
                computerPlay.selectBox(computerPlay.image6, 6, playerTurn);
            }
        });

        image7.setOnClickListener(v -> {
            if (playerTurn == 1 && !doneBoxes.contains("7")) {
                ComputerPlay computerPlay = ComputerPlay.this;
                computerPlay.selectBox(computerPlay.image7, 7, playerTurn);
            }
        });

        image8.setOnClickListener(v -> {
            if (playerTurn == 1 && !doneBoxes.contains("8")) {
                ComputerPlay computerPlay = ComputerPlay.this;
                computerPlay.selectBox(computerPlay.image8, 8, playerTurn);
            }
        });

        image9.setOnClickListener(v -> {
            if (playerTurn == 1 && !doneBoxes.contains("9")) {
                ComputerPlay computerPlay = ComputerPlay.this;
                computerPlay.selectBox(computerPlay.image9, 9, playerTurn);
            }
        });
    }

    private void applyPlayerTurn() {
        if (playerTurn == 1) {
            player1Layout.setBackgroundResource(R.drawable.round_back_dark_blue_stroke);
            player2Layout.setBackgroundResource(R.drawable.round_back_dark_blue_20);
            return;
        }
        player2Layout.setBackgroundResource(R.drawable.round_back_dark_blue_stroke);
        player1Layout.setBackgroundResource(R.drawable.round_back_dark_blue_20);
        if (doneBoxes.size() < 9) {
            performComputer();
        }
    }

    private void selectBox(ImageView imageView, int selectedBoxPosition, int playerTurn2) {
        ComputerWinDialog computerWinDialog;
        boxesSelectedBy[selectedBoxPosition - 1] = String.valueOf(playerTurn2);
        if (playerTurn2 == 1) {
            imageView.setImageResource(R.drawable.cross_icon);
            playerTurn = 0;
        } else {
            imageView.setImageResource(R.drawable.zero_icon);
            playerTurn = 1;
        }
        doneBoxes.add(String.valueOf(selectedBoxPosition));
        if (checkPlayerWin(playerTurn2)) {
            if (playerTurn2 == 1) {
                computerWinDialog = new ComputerWinDialog(this, "Bạn chiến thắng");
            } else {
                computerWinDialog = new ComputerWinDialog(this, "Computer chiến thắng");
            }
            computerWinDialog.setCancelable(false);
            computerWinDialog.show();
        } else {
            applyPlayerTurn();
        }
        if (doneBoxes.size() == 9) {
            ComputerWinDialog winDialog2 = new ComputerWinDialog(this, "Hòa nhau");
            winDialog2.setCancelable(false);
            winDialog2.show();
        }
    }

    private void performComputer() {
        int[] combination = new int[0];
        int selectedToWin = 0;
        for (int i = 0; i < combinationsList.size(); i++) {
            combination = combinationsList.get(i);
            selectedToWin = 0;
            if (boxesSelectedBy[combination[0]].equals(String.valueOf(2)) && !boxesSelectedBy[combination[1]].equals(String.valueOf(1)) && !boxesSelectedBy[combination[2]].equals(String.valueOf(1))) {
                selectedToWin = 1;
            }
            if (boxesSelectedBy[combination[1]].equals(String.valueOf(2)) && !boxesSelectedBy[combination[0]].equals(String.valueOf(1)) && !boxesSelectedBy[combination[2]].equals(String.valueOf(1))) {
                selectedToWin++;
            }
            if (boxesSelectedBy[combination[2]].equals(String.valueOf(2)) && !boxesSelectedBy[combination[1]].equals(String.valueOf(1)) && !boxesSelectedBy[combination[0]].equals(String.valueOf(1))) {
                selectedToWin++;
            }
            if (selectedToWin == 2) {
                break;
            }
        }
        if (selectedToWin != 2) {
            for (int i2 = 0; i2 < combinationsList.size(); i2++) {
                combination = combinationsList.get(i2);
                int selectedToWin2 = 0;

                if (boxesSelectedBy[combination[0]].equals(String.valueOf(1)) && !boxesSelectedBy[combination[1]].equals(String.valueOf(2)) && !boxesSelectedBy[combination[2]].equals(String.valueOf(2))) {
                    selectedToWin2 = 1;
                }
                if (boxesSelectedBy[combination[1]].equals(String.valueOf(1)) && !boxesSelectedBy[combination[0]].equals(String.valueOf(2)) && !boxesSelectedBy[combination[2]].equals(String.valueOf(2))) {
                    selectedToWin2++;
                }
                if (boxesSelectedBy[combination[2]].equals(String.valueOf(1)) && !boxesSelectedBy[combination[1]].equals(String.valueOf(2)) && !boxesSelectedBy[combination[0]].equals(String.valueOf(2))) {
                    selectedToWin2++;
                }
                if (selectedToWin == 2) {
                    break;
                }
            }
        }
        if (selectedToWin != 2) {
            int l = 1;
            while (true) {
                int[] iArr = data;
                if (l > iArr.length) {
                    return;
                }
                if (!this.doneBoxes.contains(String.valueOf(iArr[l]))) {
                    selectBoxOfComputer(data[l]);
                    return;
                }
                l++;
            }
        } else {
            for (int i3 : combination) {
                if (!doneBoxes.contains(String.valueOf(i3 + 1))) {
                    selectBoxOfComputer(i3 + 1);
                    return;
                }
            }
        }
    }

    private void selectBoxOfComputer(final int l) {
        new Handler().postDelayed(() -> {
            if (l == 1) {
                ComputerPlay singleMode = ComputerPlay.this;
                singleMode.selectBox(singleMode.image1, l, 2);
            } else if (l == 2) {
                ComputerPlay singleMode2 = ComputerPlay.this;
                singleMode2.selectBox(singleMode2.image2, l, 2);
            } else if (l == 3) {
                ComputerPlay singleMode3 = ComputerPlay.this;
                singleMode3.selectBox(singleMode3.image3, l, 2);
            } else if (l == 4) {
                ComputerPlay singleMode4 = ComputerPlay.this;
                singleMode4.selectBox(singleMode4.image4, l, 2);
            } else if (l == 5) {
                ComputerPlay singleMode5 = ComputerPlay.this;
                singleMode5.selectBox(singleMode5.image5, l, 2);
            } else if (l == 6) {
                ComputerPlay singleMode6 = ComputerPlay.this;
                singleMode6.selectBox(singleMode6.image6, l, 2);
            } else if (l == 7) {
                ComputerPlay singleMode7 = ComputerPlay.this;
                singleMode7.selectBox(singleMode7.image7, l, 2);
            } else if (l == 8) {
                ComputerPlay singleMode8 = ComputerPlay.this;
                singleMode8.selectBox(singleMode8.image8, l, 2);
            } else {
                ComputerPlay singleMode9 = ComputerPlay.this;
                singleMode9.selectBox(singleMode9.image9, l, 2);
            }
        }, 2000);
    }

    private boolean checkPlayerWin(int playerTurn2) {
        boolean isPlayerWon = false;
        for (int i = 0; i < combinationsList.size(); i++) {
            int[] combination = combinationsList.get(i);
            if (boxesSelectedBy[combination[0]].equals(String.valueOf(playerTurn2)) && boxesSelectedBy[combination[1]].equals(String.valueOf(playerTurn2)) && boxesSelectedBy[combination[2]].equals(String.valueOf(playerTurn2))) {
                isPlayerWon = true;
            }
        }
        return isPlayerWon;
    }
}