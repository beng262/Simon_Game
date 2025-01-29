package com.example.simongame;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private int score = 0;
    private String username; // Declare username variable
    private TextView scoreText;
    private ArrayList<Integer> simonSequence = new ArrayList<>();
    private int currentIndex = 0;
    private Random random = new Random();
    private boolean isUserTurn = false; // Prevent user input during sequence playback

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Retrieve the username from the Intent
        username = getIntent().getStringExtra("username");

        scoreText = findViewById(R.id.scoreText);
        Button buttonRed = findViewById(R.id.buttonRed);
        Button buttonGreen = findViewById(R.id.buttonGreen);
        Button buttonBlue = findViewById(R.id.buttonBlue);
        Button buttonYellow = findViewById(R.id.buttonYellow);

        // Generate the first sequence
        generateNextInSequence();

        // Button listeners
        buttonRed.setOnClickListener(v -> handleUserInput(0));
        buttonGreen.setOnClickListener(v -> handleUserInput(1));
        buttonBlue.setOnClickListener(v -> handleUserInput(2));
        buttonYellow.setOnClickListener(v -> handleUserInput(3));
    }

    private void generateNextInSequence() {
        simonSequence.add(random.nextInt(4)); // Add a random button (0-3)
        currentIndex = 0;
        playSimonSequence();
    }

    private void playSimonSequence() {
        isUserTurn = false; // Lock user input during playback
        Handler handler = new Handler();

        for (int i = 0; i < simonSequence.size(); i++) {
            int index = i;
            handler.postDelayed(() -> highlightButton(simonSequence.get(index)), i * 1000);
        }

        // Unlock user input after the sequence finishes
        handler.postDelayed(() -> isUserTurn = true, simonSequence.size() * 1000);
    }

    private void highlightButton(int buttonId) {
        Button button = getButtonById(buttonId);
        if (button != null) {
            button.setAlpha(0.5f); // Highlight
            button.postDelayed(() -> button.setAlpha(1.0f), 500);
        }
    }

    private Button getButtonById(int id) {
        switch (id) {
            case 0: return findViewById(R.id.buttonRed);
            case 1: return findViewById(R.id.buttonGreen);
            case 2: return findViewById(R.id.buttonBlue);
            case 3: return findViewById(R.id.buttonYellow);
            default: return null;
        }
    }

    private void handleUserInput(int buttonId) {
        if (!isUserTurn) return; // Ignore input during playback

        if (buttonId == simonSequence.get(currentIndex)) {
            currentIndex++;
            if (currentIndex == simonSequence.size()) {
                score++;
                scoreText.setText("Score: " + score);
                generateNextInSequence();
            }
        } else {
            // Send the username and score to ScoreboardActivity
            Intent intent = new Intent(this, ScoreboardActivity.class);
            intent.putExtra("username", username); // Pass the username to ScoreboardActivity
            intent.putExtra("score", score);       // Pass the score to ScoreboardActivity
            startActivity(intent);
            finish();
        }
    }
}