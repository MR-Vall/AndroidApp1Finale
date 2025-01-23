package com.example.finalopg1;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalopg1.database.Database;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * UserActivity allows the user to register loans for tablets.
 * It validates input fields and stores data in the local database.
 */
public class UserActivity extends AppCompatActivity {

    private Spinner spinnerTabletBrand, spinnerCableType;
    private EditText editBorrowerName, editContactInfo;
    private Button buttonRegister;
    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // Initialize the database
        database = new Database(this);

        // Find views by ID
        spinnerTabletBrand = findViewById(R.id.spinner_tablet_brand);
        spinnerCableType = findViewById(R.id.spinner_cable_type);
        editBorrowerName = findViewById(R.id.edit_borrower_name);
        editContactInfo = findViewById(R.id.edit_contact_info);
        buttonRegister = findViewById(R.id.button_register);

        // Set adapters for the spinners
        ArrayAdapter<CharSequence> tabletAdapter = ArrayAdapter.createFromResource(this,
                R.array.user_tablet_brands, android.R.layout.simple_spinner_item);
        tabletAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTabletBrand.setAdapter(tabletAdapter);

        ArrayAdapter<CharSequence> cableAdapter = ArrayAdapter.createFromResource(this,
                R.array.user_cable_types, android.R.layout.simple_spinner_item);
        cableAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCableType.setAdapter(cableAdapter);

        // Set onClickListener for the register button
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tabletBrand = spinnerTabletBrand.getSelectedItem().toString();
                String cableType = spinnerCableType.getSelectedItem().toString();
                String borrowerName = editBorrowerName.getText().toString().trim();
                String contactInfo = editContactInfo.getText().toString().trim();
                String loanDate = getCurrentDateTime();

                // Validate required fields
                if (tabletBrand.isEmpty() || borrowerName.isEmpty()) {
                    Toast.makeText(UserActivity.this, "Udfyld venligst alle obligatoriske felter!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validate contact information (email or phone)
                if (!isValidEmail(contactInfo) && !isValidPhone(contactInfo)) {
                    Toast.makeText(UserActivity.this, "Indtast venligst en gyldig e-mail eller telefonnummer!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Insert the loan into the database
                long result = database.insertLoan(tabletBrand, cableType, borrowerName, contactInfo, loanDate);
                if (result != -1) {
                    Toast.makeText(UserActivity.this, "Lån registreret!", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity
                } else {
                    Toast.makeText(UserActivity.this, "Kunne ikke registrere lånet!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Helper method to validate email format.
     * @param email The email address to validate.
     * @return True if the email is valid, false otherwise.
     */
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Helper method to validate phone number format.
     * @param phone The phone number to validate.
     * @return True if the phone number is valid, false otherwise.
     */
    private boolean isValidPhone(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    /**
     * Helper method to get the current date and time in a readable format.
     * @return The current date and time as a string.
     */
    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}
