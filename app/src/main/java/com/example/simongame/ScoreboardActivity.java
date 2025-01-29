package com.example.simongame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
        Button tryAgainButton = findViewById(R.id.tryAgainButton);

        SharedPreferences preferences = getSharedPreferences("SimonGame", MODE_PRIVATE);
        Set<String> scoresSet = preferences.getStringSet("scores", new HashSet<>());

        // Parse scores and handle updates
        ArrayList<String> scoresList = new ArrayList<>();
        String username = getIntent().getStringExtra("username");
        int latestScore = getIntent().getIntExtra("score", 0);
        boolean scoreUpdated = false;

        // Convert Set to ArrayList and update the score for existing usernames
        for (String entry : scoresSet) {
            String[] parts = entry.split(": ");
            String existingUsername = parts[0];
            int score = Integer.parseInt(parts[1]);

            if (existingUsername.equals(username)) {
                // Update score if the new score is higher
                if (latestScore > score) {
                    scoresList.add(username + ": " + latestScore);
                    scoreUpdated = true;
                } else {
                    scoresList.add(entry); // Keep the original score
                }
            } else {
                scoresList.add(entry); // Add other users' scores unchanged
            }
        }

        // If username is new or updated, add the latest score
        if (!scoreUpdated && username != null && !username.isEmpty() && latestScore > 0) {
            scoresList.add(username + ": " + latestScore);
        }

        // Update scoresSet to match scoresList and save to SharedPreferences
        scoresSet = new HashSet<>(scoresList);
        preferences.edit().putStringSet("scores", scoresSet).apply();

        // Sort scores in descending order based on score value
        Collections.sort(scoresList, (s1, s2) -> {
            int score1 = Integer.parseInt(s1.split(": ")[1]);
            int score2 = Integer.parseInt(s2.split(": ")[1]);
            return Integer.compare(score2, score1);
        });

        // Prepare display list
        ArrayList<String> displayList = new ArrayList<>();
        if (!scoresList.isEmpty()) {
            displayList.add("🏆 High Score: " + scoresList.get(0)); // Highlight high score
            displayList.add("🎮 Latest Scores:");
            displayList.addAll(scoresList);
        } else {
            displayList.add("⚬ No scores available!"); // Fallback message
        }

        // Set adapter for ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.scoreboard_list_item, displayList);
        scoreboardListView.setAdapter(adapter);

        // Try Again Button Click Listener
        tryAgainButton.setOnClickListener(view -> {
            Intent intent = new Intent(ScoreboardActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Close scoreboard activity
        });
    }
}