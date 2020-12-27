package com.example.hitthemole;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static int score;
    private static String score_prefix = "Score: ";
    private static String between_score_time = "   ";
    private static String time_prefix = "Time (sec): ";
    private static String maxed_out = "MAX TIME!!!";
    private static int width = 0;
    private static int height = 0;
    private static long seconds = 0;
    private static long whacked = 0;
    private static boolean max_time = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        ImageButton mole = findViewById(R.id.moleButton);

        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        mole.setOnClickListener(v -> MoleMove(true));

        MoleMove(false);

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    TextView score_t = findViewById(R.id.scoreView);
                    if (!max_time && seconds < Long.MAX_VALUE - 1)
                        score_t.setText(score_prefix + score + between_score_time + time_prefix + seconds);
                    else {
                        max_time = true;
                        score_t.setText(score_prefix + score + between_score_time + time_prefix + maxed_out);
                    }

                    if (seconds - whacked > 1 || (whacked == 0 && seconds == 0)) {
                        MoleMove(false);
                    }
                });

                if (seconds >= Long.MAX_VALUE - 1 || whacked >= Long.MAX_VALUE - 1) {
                    seconds = 0;
                    whacked = 0;
                }
                seconds++;
            }
        }, 0, 1000);
    }
    
    public void MoleMove(boolean clicked) {
        runOnUiThread(() -> {
            ImageButton mole = findViewById(R.id.moleButton);
            Random rand = new Random();

            int x = rand.nextInt(width - mole.getWidth());
            int y = rand.nextInt(height - mole.getHeight());

            if (Math.abs(mole.getX() - x) <= 30 || Math.abs(mole.getY() - y) <= 30) {
                MoleMove(clicked);
                return;
            }

            if (clicked) {
                score++;
                whacked = seconds;
            }

            mole.setX(x);
            mole.setY(y);
        });
    }
}