package com.example.simongame;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        EditText usernameInput = findViewById(R.id.usernameInput);
        Button startGameButton = findViewById(R.id.startGameButton);

        startGameButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString();
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            intent.putExtra("username", username); // Pass the username to MainActivity
            startActivity(intent);
        });
    }
}