package com.example.finalopg1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * LoginActivity protects access to AdminActivity by requiring a password.
 */
public class LoginActivity extends AppCompatActivity {

    private static final String ADMIN_PASSWORD = "password"; // Hardcoded password

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Find views by ID
        EditText editPassword = findViewById(R.id.edit_password);
        Button buttonLogin = findViewById(R.id.button_login);

        // Set onClickListener for the login button
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredPassword = editPassword.getText().toString().trim();

                // Check if the entered password matches the hardcoded password
                if (enteredPassword.equals(ADMIN_PASSWORD)) {
                    // Password is correct, navigate to AdminActivity
                    Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                    startActivity(intent);
                    finish(); // Close LoginActivity
                } else {
                    // Password is incorrect, show a toast message
                    Toast.makeText(LoginActivity.this, "Forkert adgangskode!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
