package com.example.androidtictactoe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OnlinePlay extends AppCompatActivity {

    private LinearLayout player1Layout, player2Layout;
    private ImageView image1, image2, image3, image4, image5, image6, image7, image8, image9;
    private TextView player2TV;

    //combo để thắng
    private final List<int[]> combinationsList = new ArrayList<>();
    //vị trí đã được nhấn bởi người chơi
    private final List<String> doneBoxes = new ArrayList<>();
    //tạo id người chơi
    private String playerUniqueId = "0";

    //firebase database reference từ URL
    DatabaseReference myref = FirebaseDatabase.getInstance().getReferenceFromUrl("https://androidtictactoe-a8317-default-rtdb.firebaseio.com/");

    //tìm đối thủ
    private boolean opponentFound = false;
    private String opponentUniqueId = "0";
    //tìm trận
    private String status = "matching";
    //lượt chơi của player
    private String playerTurn = "";
    //id kết nối
    private String connectionId = "";

    //tạo eventListener cho firebase
    ValueEventListener turnsEventListener, wonEvenListener;

    //box được chọn từ người chơi (khoảng trống sẽ được thay thế = id người chơi)
    private final String[] boxesSelectedBy = {"", "", "", "", "", "", "", "", "",};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_play);

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
        player2TV = findViewById(R.id.player2TV);

        final String getPlayerName = getIntent().getStringExtra("playerName");

        combinationsList.add(new int[]{0, 1, 2});
        combinationsList.add(new int[]{3, 4, 5});
        combinationsList.add(new int[]{6, 7, 8});
        combinationsList.add(new int[]{0, 4, 8});
        combinationsList.add(new int[]{2, 4, 6});
        combinationsList.add(new int[]{0, 3, 6});
        combinationsList.add(new int[]{1, 4, 7});
        combinationsList.add(new int[]{2, 5, 8});

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Chờ Đối Thủ");
        progressDialog.show();

        //tạo id cho player. Player sẽ được chỉ định bởi id này
        playerUniqueId = String.valueOf(System.currentTimeMillis());

        //cài đặt tên cho player
        player1TV.setText(getPlayerName);

        myref.child("connections").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //kiếm tra có kiếm được đối thủ không.Không thì bắt đầu kiếm đối thủ
                if (!opponentFound) {
                    //kiểm tra trong database realtime có đối thủ ko
                    if (snapshot.hasChildren()) {
                        //kiểm tra có đối thủ nào đang chờ trận
                        for (DataSnapshot connections : snapshot.getChildren()) {
                            String conId = connections.getKey();
                            int getPlayersCount = (int) connections.getChildrenCount();
                            if (status.equals("waiting")) {
                                if (getPlayersCount == 2) {
                                    playerTurn = playerUniqueId;
                                    applyPlayerTurn(playerTurn);
                                    boolean playerFound = false;
                                    for (DataSnapshot players : connections.getChildren()) {
                                        String getplayerUniqueId = players.getKey();
                                        if (getplayerUniqueId.equals(playerUniqueId)) {
                                            playerFound = true;
                                        } else if (playerFound) {
                                            String getOpponentPlayerName = players.child("player_name").getValue(String.class);
                                            opponentUniqueId = players.getKey();
                                            player2TV.setText(getOpponentPlayerName);
                                            //tạo id kết nối
                                            connectionId = conId;
                                            opponentFound = true;
                                            //tạo turn và số lần thắng cho database
                                            myref.child("turns").child(connectionId).addValueEventListener(turnsEventListener);
                                            myref.child("won").child(connectionId).addValueEventListener(wonEvenListener);
                                            if (progressDialog.isShowing()) {
                                                progressDialog.dismiss();
                                            }
                                            myref.child("connections").removeEventListener(this);
                                        }
                                    }
                                }
                            } else {
                                if (getPlayersCount == 1) {
                                    connections.child(playerUniqueId).child("player_name").getRef().setValue(getPlayerName);
                                    for (DataSnapshot players : connections.getChildren()) {
                                        String getOpponentName = players.child("player_name").getValue(String.class);
                                        opponentUniqueId = players.getKey();
                                        playerTurn = opponentUniqueId;
                                        applyPlayerTurn(playerTurn);
                                        player2TV.setText(getOpponentName);
                                        connectionId = conId;
                                        opponentFound = true;
                                        //tạo turn và số lần thắng cho database
                                        myref.child("turns").child(connectionId).addValueEventListener(turnsEventListener);
                                        myref.child("won").child(connectionId).addValueEventListener(wonEvenListener);
                                        if (progressDialog.isShowing()) {
                                            progressDialog.dismiss();
                                        }
                                        myref.child("connections").removeEventListener(this);
                                        break;
                                    }
                                }
                            }
                        }
                        //kiểm tra coi có còn người chơi trong phòng hay không
                        if (!opponentFound && !status.equals("waiting")) {
                            String connectionUniqueId = String.valueOf(System.currentTimeMillis());
                            snapshot.child(connectionUniqueId).child(playerUniqueId).child("player_name").getRef().setValue(getPlayerName);
                            status = "waiting";
                        }
                    } else {
                        String connectionUniqueId = String.valueOf(System.currentTimeMillis());
                        snapshot.child(connectionUniqueId).child(playerUniqueId).child("player_name").getRef().setValue(getPlayerName);
                        status = "waiting";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        turnsEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.getChildrenCount() == 2) {
                        //chọn địa điểm trên bàn cờ bởi người dùng
                        final int getBoxPosition = Integer.parseInt(dataSnapshot.child("box_position").getValue(String.class));
                        //chọn người chơi đã nhấn vào box
                        final String getPlayerId = dataSnapshot.child("player_id").getValue(String.class);

                        if (!doneBoxes.contains(String.valueOf(getBoxPosition))) {
                            //chọn vị trí
                            doneBoxes.add(String.valueOf(getBoxPosition));

                            if (getBoxPosition == 1) {
                                selectBox(image1, getBoxPosition, getPlayerId);

                            } else if (getBoxPosition == 2) {
                                selectBox(image2, getBoxPosition, getPlayerId);
                            } else if (getBoxPosition == 3) {
                                selectBox(image3, getBoxPosition, getPlayerId);
                            } else if (getBoxPosition == 4) {
                                selectBox(image4, getBoxPosition, getPlayerId);
                            } else if (getBoxPosition == 5) {
                                selectBox(image5, getBoxPosition, getPlayerId);
                            } else if (getBoxPosition == 6) {
                                selectBox(image6, getBoxPosition, getPlayerId);
                            } else if (getBoxPosition == 7) {
                                selectBox(image7, getBoxPosition, getPlayerId);
                            } else if (getBoxPosition == 8) {
                                selectBox(image8, getBoxPosition, getPlayerId);
                            } else if (getBoxPosition == 9) {
                                selectBox(image9, getBoxPosition, getPlayerId);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        wonEvenListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //kiếm tra có người chơi thắng chưa
                if (snapshot.hasChild("player_id")) {
                    String getWinPlayerId = snapshot.child("player_id").getValue(String.class);
                    final OnlineWinDialog winDialog;
                    if (getWinPlayerId.equals(playerUniqueId)) {
                        winDialog = new OnlineWinDialog(OnlinePlay.this, "Bạn đã thắng !!");
                    } else {
                        winDialog = new OnlineWinDialog(OnlinePlay.this, "Đối phương đã thắng !!");
                    }
                    winDialog.setCancelable(false);
                    winDialog.show();
                    //remove Listener khỏi database
                    myref.child("turns").child(connectionId).removeEventListener(turnsEventListener);
                    myref.child("won").child(connectionId).removeEventListener(wonEvenListener);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //kiếm tra ô có được chọn trước đó chưa và người chơi hiện tại
                if (!doneBoxes.contains("1") && playerTurn.equals(playerUniqueId)) {
                    ((ImageView) v).setImageResource(R.drawable.cross_icon);
                    //gửi thông tin vị trí ô và id người chơi tới database
                    myref.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("box_position").setValue("1");
                    myref.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("player_id").setValue(playerUniqueId);
                    //đổi lượt chơi
                    playerTurn = opponentUniqueId;
                }
            }
        });

        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //kiếm tra ô có được chọn trước đó chưa và người chơi hiện tại
                if (!doneBoxes.contains("2") && playerTurn.equals(playerUniqueId)) {
                    ((ImageView) v).setImageResource(R.drawable.cross_icon);
                    //gửi thông tin vị trí ô và id người chơi tới database
                    myref.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("box_position").setValue("2");
                    myref.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("player_id").setValue(playerUniqueId);
                    //đổi lượt chơi
                    playerTurn = opponentUniqueId;
                }
            }
        });

        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //kiếm tra ô có được chọn trước đó chưa và người chơi hiện tại
                if (!doneBoxes.contains("3") && playerTurn.equals(playerUniqueId)) {
                    ((ImageView) v).setImageResource(R.drawable.cross_icon);
                    //gửi thông tin vị trí ô và id người chơi tới database
                    myref.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("box_position").setValue("3");
                    myref.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("player_id").setValue(playerUniqueId);
                    //đổi lượt chơi
                    playerTurn = opponentUniqueId;
                }
            }
        });

        image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //kiếm tra ô có được chọn trước đó chưa và người chơi hiện tại
                if (!doneBoxes.contains("4") && playerTurn.equals(playerUniqueId)) {
                    ((ImageView) v).setImageResource(R.drawable.cross_icon);
                    //gửi thông tin vị trí ô và id người chơi tới database
                    myref.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("box_position").setValue("4");
                    myref.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("player_id").setValue(playerUniqueId);
                    //đổi lượt chơi
                    playerTurn = opponentUniqueId;
                }
            }
        });

        image5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //kiếm tra ô có được chọn trước đó chưa và người chơi hiện tại
                if (!doneBoxes.contains("5") && playerTurn.equals(playerUniqueId)) {
                    ((ImageView) v).setImageResource(R.drawable.cross_icon);
                    //gửi thông tin vị trí ô và id người chơi tới database
                    myref.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("box_position").setValue("5");
                    myref.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("player_id").setValue(playerUniqueId);
                    //đổi lượt chơi
                    playerTurn = opponentUniqueId;
                }
            }
        });

        image6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //kiếm tra ô có được chọn trước đó chưa và người chơi hiện tại
                if (!doneBoxes.contains("6") && playerTurn.equals(playerUniqueId)) {
                    ((ImageView) v).setImageResource(R.drawable.cross_icon);
                    //gửi thông tin vị trí ô và id người chơi tới database
                    myref.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("box_position").setValue("6");
                    myref.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("player_id").setValue(playerUniqueId);
                    //đổi lượt chơi
                    playerTurn = opponentUniqueId;
                }
            }
        });

        image7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //kiếm tra ô có được chọn trước đó chưa và người chơi hiện tại
                if (!doneBoxes.contains("7") && playerTurn.equals(playerUniqueId)) {
                    ((ImageView) v).setImageResource(R.drawable.cross_icon);
                    //gửi thông tin vị trí ô và id người chơi tới database
                    myref.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("box_position").setValue("7");
                    myref.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("player_id").setValue(playerUniqueId);
                    //đổi lượt chơi
                    playerTurn = opponentUniqueId;
                }
            }
        });

        image8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //kiếm tra ô có được chọn trước đó chưa và người chơi hiện tại
                if (!doneBoxes.contains("8") && playerTurn.equals(playerUniqueId)) {
                    ((ImageView) v).setImageResource(R.drawable.cross_icon);
                    //gửi thông tin vị trí ô và id người chơi tới database
                    myref.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("box_position").setValue("8");
                    myref.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("player_id").setValue(playerUniqueId);
                    //đổi lượt chơi
                    playerTurn = opponentUniqueId;
                }
            }
        });

        image9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //kiếm tra ô có được chọn trước đó chưa và người chơi hiện tại
                if (!doneBoxes.contains("9") && playerTurn.equals(playerUniqueId)) {
                    ((ImageView) v).setImageResource(R.drawable.cross_icon);
                    //gửi thông tin vị trí ô và id người chơi tới database
                    myref.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("box_position").setValue("9");
                    myref.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("player_id").setValue(playerUniqueId);
                    //đổi lượt chơi
                    playerTurn = opponentUniqueId;
                }
            }
        });
    }

    private void applyPlayerTurn(String playerUniqueId2) {
        if (playerUniqueId2.equals(playerUniqueId)) {
            player1Layout.setBackgroundResource(R.drawable.round_back_dark_blue_stroke);

            player2Layout.setBackgroundResource(R.drawable.round_back_dark_blue_20);
        } else {
            player2Layout.setBackgroundResource(R.drawable.round_back_dark_blue_stroke);

            player1Layout.setBackgroundResource(R.drawable.round_back_dark_blue_20);
        }
    }

    private void selectBox(ImageView imageView, int selectedBoxPosition, String selectedByPlayer) {

        boxesSelectedBy[selectedBoxPosition - 1] = selectedByPlayer;

        if (selectedByPlayer.equals(playerUniqueId)) {
            imageView.setImageResource(R.drawable.cross_icon);
            playerTurn = opponentUniqueId;
        } else {
            imageView.setImageResource(R.drawable.zero_icon);
            playerTurn = playerUniqueId;
        }
        applyPlayerTurn(playerTurn);

        //kiểm tra người chơi nào đã thắng trận
        if (checkPlayerWin(selectedByPlayer)) {
            //gửi id người thắng đến database, khiến cho đối thủ có thể thấy
            myref.child("won").child(connectionId).child("player_id").setValue(selectedByPlayer);
        }

        //game kết thúc khi ô không còn chỗ trống
        if (doneBoxes.size() == 9) {
            final OnlineWinDialog winDialog = new OnlineWinDialog(OnlinePlay.this, "Hòa");
            winDialog.setCancelable(false);
            winDialog.show();

        }
    }

    private boolean checkPlayerWin(String playerId) {
        boolean isPlayerWon = false;
        for (int i = 0; i < combinationsList.size(); i++) {
            final int[] combination = combinationsList.get(i);
            if (boxesSelectedBy[combination[0]].equals(playerId) &&
                    boxesSelectedBy[combination[1]].equals(playerId) &&
                    boxesSelectedBy[combination[2]].equals(playerId)) {
                isPlayerWon = true;
            }
        }
        return isPlayerWon;
    }
}