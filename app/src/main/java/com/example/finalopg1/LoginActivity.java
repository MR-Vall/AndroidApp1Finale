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

    // Hardcoded password for admin access
    private static final String ADMIN_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Load the layout for LoginActivity

        // Find the password input field and login button in the layout
        EditText editPassword = findViewById(R.id.edit_password); // Input field for entering the password
        Button buttonLogin = findViewById(R.id.button_login);     // Button to submit the password

        // Set a click listener on the login button
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the entered password from the input field
                String enteredPassword = editPassword.getText().toString().trim();

                // Check if the entered password matches the hardcoded admin password
                if (enteredPassword.equals(ADMIN_PASSWORD)) {
                    // If the password is correct, navigate to AdminActivity
                    Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                    startActivity(intent); // Start AdminActivity
                    finish(); // Close LoginActivity to prevent returning to it
                } else {
                    // If the password is incorrect, show a toast message
                    Toast.makeText(LoginActivity.this, "Forkert adgangskode!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}