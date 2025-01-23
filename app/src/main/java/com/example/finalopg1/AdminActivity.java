package com.example.finalopg1;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalopg1.database.Database;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * AdminActivity allows the admin to view and manage all registered loans.
 */
public class AdminActivity extends AppCompatActivity {

    private Spinner spinnerFilterBrand, spinnerFilterCable;
    private EditText editStartDate, editEndDate;
    private Button buttonApplyFilters;
    private ListView listViewLoans;
    private Database database;
    private ArrayAdapter<String> loansAdapter;
    private ArrayList<String> loansList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Initialize database
        database = new Database(this);

        // Find views
        spinnerFilterBrand = findViewById(R.id.spinner_filter_brand);
        spinnerFilterCable = findViewById(R.id.spinner_filter_cable);
        editStartDate = findViewById(R.id.edit_start_date);
        editEndDate = findViewById(R.id.edit_end_date);
        buttonApplyFilters = findViewById(R.id.button_apply_filters);
        listViewLoans = findViewById(R.id.list_view_loans);

        // Initialize list and adapter
        loansList = new ArrayList<>();
        loansAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, loansList);
        listViewLoans.setAdapter(loansAdapter);

        // Set up filter spinners
        setupSpinners();

        // Load all loans initially
        loadLoans(null, null, null, null);

        // Apply filters when button is clicked
        buttonApplyFilters.setOnClickListener(v -> {
            String selectedBrand = spinnerFilterBrand.getSelectedItem().toString();
            String selectedCable = spinnerFilterCable.getSelectedItem().toString();
            String startDate = editStartDate.getText().toString().trim();
            String endDate = editEndDate.getText().toString().trim();

            loadLoans(
                    selectedBrand.equals("Alle") ? null : selectedBrand,
                    selectedCable.equals("Alle") ? null : selectedCable,
                    startDate.isEmpty() ? null : startDate,
                    endDate.isEmpty() ? null : endDate
            );
        });

        // Handle list item click to delete a loan
        listViewLoans.setOnItemClickListener((parent, view, position, id) -> {
            String loanInfo = loansList.get(position);
            long loanId = extractLoanId(loanInfo);

            if (loanId != -1) {
                int rowsDeleted = database.deleteLoan(loanId);
                if (rowsDeleted > 0) {
                    Toast.makeText(this, "Lån slettet!", Toast.LENGTH_SHORT).show();
                    loadLoans(null, null, null, null); // Reload all loans
                } else {
                    Toast.makeText(this, "Kunne ikke slette lånet!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Sets up the filter spinners with options
    private void setupSpinners() {
        ArrayAdapter<CharSequence> brandAdapter = ArrayAdapter.createFromResource(this,
                R.array.tablet_brands, android.R.layout.simple_spinner_item);
        brandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilterBrand.setAdapter(brandAdapter);

        ArrayAdapter<CharSequence> cableAdapter = ArrayAdapter.createFromResource(this,
                R.array.cable_types, android.R.layout.simple_spinner_item);
        cableAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilterCable.setAdapter(cableAdapter);
    }

    // Loads loans based on the selected filters
    private void loadLoans(String tabletBrand, String cableType, String startDate, String endDate) {
        loansList.clear();
        Cursor cursor = database.getAllLoans();

        while (cursor.moveToNext()) {
            String brand = cursor.getString(cursor.getColumnIndex("tablet_brand"));
            String cable = cursor.getString(cursor.getColumnIndex("cable_type"));
            String name = cursor.getString(cursor.getColumnIndex("borrower_name"));
            String contact = cursor.getString(cursor.getColumnIndex("contact_info"));
            String date = cursor.getString(cursor.getColumnIndex("loan_date"));
            long id = cursor.getLong(cursor.getColumnIndex("id"));

            // Apply filters if specified
            boolean matchesDate = true;
            if (startDate != null && endDate != null) {
                matchesDate = isDateInRange(date, startDate, endDate);
            }

            if ((tabletBrand == null || tabletBrand.equals(brand)) &&
                    (cableType == null || cableType.equals(cable)) &&
                    matchesDate) {
                loansList.add("ID: " + id + " | " + brand + " | " + cable + " | " +
                        name + " | " + contact + " | " + date);
            }
        }
        cursor.close();

        if (loansList.isEmpty()) {
            loansList.add("Ingen lån fundet.");
        }
        loansAdapter.notifyDataSetChanged();
    }

    // Check if a date is within a given range
    private boolean isDateInRange(String date, String startDate, String endDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date loanDate = sdf.parse(date.split(" ")[0]); // Extract only the date part
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);
            return loanDate != null && !loanDate.before(start) && !loanDate.after(end);
        } catch (Exception e) {
            return false;
        }
    }

    // Extracts loan ID from the string
    private long extractLoanId(String loanInfo) {
        try {
            String idString = loanInfo.split(" \\| ")[0].replace("ID: ", "");
            return Long.parseLong(idString);
        } catch (Exception e) {
            return -1;
        }
    }
}
