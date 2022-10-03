package com.example.tictactoeekg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final ImageButton[][] buttons = new ImageButton[3][3];
    private boolean player1Turn = true;
    private int roundCount;
    private int player1Points, player2Points;
    private TextView textViewPlayer1, textViewPlayer2;
    private MediaPlayer impostor_win_sound, crewmate_win_sound, draw_sound;
    private String impostor_id = String.valueOf(R.drawable.impostor);
    private String crewmate_id = String.valueOf(R.drawable.crewmate);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewPlayer1 = findViewById(R.id.text_view_p1);
        textViewPlayer2 = findViewById(R.id.text_view_p2);
        impostor_win_sound = MediaPlayer.create(this, R.raw.amongus_impostor);
        crewmate_win_sound = MediaPlayer.create(this, R.raw.amongus_crewmate);
        draw_sound = MediaPlayer.create(this, R.raw.amongus_draw);

        for (int i = 0; i < 3; i++) {
             for (int j = 0; j < 3; j++) {
                 String buttonID = "button_" + i + j;
                 int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                 buttons[i][j] = findViewById(resID);
                 buttons[i][j].setOnClickListener(this);
             }
        }

        Button buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(view -> resetGame());
    }

    @Override
    public void onClick(View view) {
        if (((ImageButton) view).getDrawable() != null) {
            return;
        }

        if (player1Turn) {
            ((ImageButton) view).setImageResource(R.drawable.impostor);
        } else {
            ((ImageButton) view).setImageResource(R.drawable.crewmate);
        }

        roundCount++;

        if (checkForWin()) {
            if (player1Turn) {
                player1Wins();
            } else {
                player2Wins();
            }
        } else if (roundCount == 9) {
            draw();
        } else {
            player1Turn = !player1Turn;
        }
    }

    private boolean checkForWin() {
        String[][] field = new String[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = String.valueOf(buttons[i][j].getId());
            }
        }

        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals(field[i][1]) && field[i][0].equals(field[i][2]) && !field[i][0].equals("null")) {
                return true;
            }
        }

        for (int i = 0; i < 3; i++) {
            if (field[0][i].equals(field[1][i]) && field[0][i].equals(field[2][i]) && !field[0][i].equals("null")) {
                return true;
            }
        }

        if (field[0][0].equals(field[1][1]) && field[0][0].equals(field[2][2]) && !field[0][0].equals("null")) {
            return true;
        }

        return field[0][2].equals(field[1][1]) && field[0][2].equals(field[2][0]) && !field[0][2].equals("null");
    }

    private void player1Wins() {
        player1Points++;
        Toast.makeText(this, "Impostors win!", Toast.LENGTH_SHORT).show();
        impostor_win_sound.start();
        updatePointsText();
        delayedReset();
    }

    private void player2Wins() {
        player2Points++;
        Toast.makeText(this, "Crewmates win!", Toast.LENGTH_SHORT).show();
        crewmate_win_sound.start();
        updatePointsText();
        delayedReset();
    }

    private void draw() {
        Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show();
        draw_sound.start();
        delayedReset();
    }

    private void updatePointsText() {
        textViewPlayer1.setText("Impostors: " + player1Points);
        textViewPlayer2.setText("Crewmates: " + player2Points);
    }

    private void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setImageDrawable(null);
            }
        }

        roundCount = 0;
        player1Turn = true;
    }

    private void resetGame() {
        player1Points = 0;
        player2Points = 0;
        updatePointsText();
        resetBoard();
    }

    private void delayedReset() {
        // delay in ms
        int DELAY = 1000;

        Handler handler = new Handler();
        handler.postDelayed(this::resetBoard, DELAY);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("roundCount", roundCount);
        outState.putInt("player1Points", player1Points);
        outState.putInt("player2Points", player2Points);
        outState.putBoolean("player1Turn", player1Turn);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        roundCount = savedInstanceState.getInt("roundCount");
        player1Points = savedInstanceState.getInt("player1Points");
        player2Points = savedInstanceState.getInt("player2Points");
        player1Turn = savedInstanceState.getBoolean("player1Turn");
    }
}