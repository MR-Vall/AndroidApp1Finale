package com.example.finalopg1;

import android.content.Intent; // Import for at skifte mellem aktiviteter
import android.os.Bundle; // Import for at håndtere aktiviteter og deres livscyklus
import android.widget.Button; // Import for at bruge knapper i UI

import androidx.appcompat.app.AppCompatActivity; // Baseklasse til aktiviteter med AppCompat support

/**
 * MainActivity serves as the main entry point of the app.
 * It provides two options for the user:
 * 1. Navigate to UserActivity for user-related actions.
 * 2. Navigate to Admin via LoginActivity.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Load the layout for MainActivity

        // Find the "User" button in the layout using its ID
        Button buttonUser = findViewById(R.id.button_user);

        // Find the "Admin" button in the layout using its ID
        Button buttonAdmin = findViewById(R.id.button_admin);

        // Set a click listener on the "User" button
        buttonUser.setOnClickListener(v -> {
            // Create an Intent to navigate to UserActivity
            Intent intent = new Intent(MainActivity.this, UserActivity.class);
            startActivity(intent); // Start UserActivity
        });

        // Set a click listener on the "Admin" button
        buttonAdmin.setOnClickListener(v -> {
            // Create an Intent to navigate to LoginActivity
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent); // Start LoginActivity
        });
    }
}