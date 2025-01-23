package com.example.finalopg1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find buttons for User and Admin
        Button buttonUser = findViewById(R.id.button_user);
        Button buttonAdmin = findViewById(R.id.button_admin);

        // Open UserActivity when "User" button is clicked
        buttonUser.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, UserActivity.class);
            startActivity(intent);
        });

        // Open LoginActivity (Admin login) when "Admin" button is clicked
        buttonAdmin.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}
