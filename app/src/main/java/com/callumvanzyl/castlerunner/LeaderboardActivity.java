package com.callumvanzyl.castlerunner;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.v4.math.MathUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class LeaderboardActivity extends AppCompatActivity {

    LeaderboardManager leaderboardManager;

    ArrayList<Pair<String, Integer>> scores = null;

    private void hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }

    private void exit() {
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }

    private void updateAllScores() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView scoreOneName = findViewById(R.id.scoreOneName);
                TextView scoreOneValue = findViewById(R.id.scoreOneValue);

                TextView scoreTwoName = findViewById(R.id.scoreTwoName);
                TextView scoreTwoValue = findViewById(R.id.scoreTwoValue);

                TextView scoreThreeName = findViewById(R.id.scoreThreeName);
                TextView scoreThreeValue = findViewById(R.id.scoreThreeValue);

                Collections.sort(scores, new Comparator<Pair<String, Integer>>() {
                    @Override
                    public int compare(final Pair<String, Integer> o1, final Pair<String, Integer> o2) {
                        if (o1.second > o2.second) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                });

                int i = scores.size();
                i = MathUtils.clamp(i, 0, 3);

                for (int j = 0; j < i; j++) {
                    if (j == 0) {
                        Pair a = scores.get(0);
                        scoreOneName.setText((String) a.first);
                        scoreOneValue.setText(Integer.toString((Integer) a.second));
                    }
                    if (j == 1) {
                        Pair a = scores.get(1);
                        scoreTwoName.setText((String) a.first);
                        scoreTwoValue.setText(Integer.toString((Integer) a.second));
                    }
                    if (j == 2) {
                        Pair a = scores.get(2);
                        scoreThreeName.setText((String) a.first);
                        scoreThreeValue.setText(Integer.toString((Integer) a.second));
                    }
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        hideSystemUI();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        final ImageView exitButton = findViewById(R.id.exitButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });

        leaderboardManager = new LeaderboardManager(this);

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                scores = leaderboardManager.getScores();
                if (scores != null) {
                    updateAllScores();
                    this.cancel();
                }
            }
        }, 0, 1);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

}
