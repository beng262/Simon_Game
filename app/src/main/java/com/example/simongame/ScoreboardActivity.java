package com.example.simongame;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ScoreboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        ListView scoreboardListView = findViewById(R.id.scoreboardListView);
        SharedPreferences preferences = getSharedPreferences("SimonGame", MODE_PRIVATE);
        Set<String> scoresSet = preferences.getStringSet("scores", new HashSet<>());

        // Safeguard: Parse scores and handle non-numeric data
        ArrayList<Integer> scoresList = new ArrayList<>();
        for (String s : scoresSet) {
            try {
                scoresList.add(Integer.parseInt(s));
            } catch (NumberFormatException e) {
                // Log or ignore invalid score entries
                e.printStackTrace();
            }
        }

        // Add the latest score (only if valid)
        int latestScore = getIntent().getIntExtra("score", 0);
        if (latestScore > 0) {
            scoresList.add(latestScore);
            scoresSet.add(String.valueOf(latestScore));
            preferences.edit().putStringSet("scores", scoresSet).apply();
        }

        // Sort scores in descending order
        Collections.sort(scoresList, Collections.reverseOrder());

        // Prepare display list with high score and latest scores
        ArrayList<String> displayList = new ArrayList<>();
        if (!scoresList.isEmpty()) {
            displayList.add("High Score: " + scoresList.get(0)); // Add high score
            displayList.add("Latest Scores:"); // Header for latest scores
            for (int i = 0; i < scoresList.size(); i++) { // Add all scores
                displayList.add("Score: " + scoresList.get(i));
            }
        } else {
            displayList.add("No scores available!"); // Fallback message
        }

        // Set adapter for ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayList);
        scoreboardListView.setAdapter(adapter);
    }
}