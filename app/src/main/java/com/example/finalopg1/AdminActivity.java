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
 * Admins can:
 * 1. Filter loans by tablet brand, cable type, and date range.
 * 2. View all loans in a list.
 * 3. Delete a loan by tapping on it in the list.
 */
public class AdminActivity extends AppCompatActivity {

    // UI components
    private Spinner spinnerFilterBrand, spinnerFilterCable; // Spinners for filtering by brand and cable type
    private EditText editStartDate, editEndDate;            // Input fields for start and end dates
    private Button buttonApplyFilters;                     // Button to apply filters
    private ListView listViewLoans;                        // ListView to display loans

    // Database and adapter
    private Database database;                             // Database instance for loan management
    private ArrayAdapter<String> loansAdapter;             // Adapter for displaying loan data in the ListView
    private ArrayList<String> loansList;                   // List of loans to display

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin); // Load the admin activity layout

        // Initialize database
        database = new Database(this);

        // Find views in the layout
        spinnerFilterBrand = findViewById(R.id.spinner_filter_brand);
        spinnerFilterCable = findViewById(R.id.spinner_filter_cable);
        editStartDate = findViewById(R.id.edit_start_date);
        editEndDate = findViewById(R.id.edit_end_date);
        buttonApplyFilters = findViewById(R.id.button_apply_filters);
        listViewLoans = findViewById(R.id.list_view_loans);

        // Initialize the list and adapter for the ListView
        loansList = new ArrayList<>();
        loansAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, loansList);
        listViewLoans.setAdapter(loansAdapter);

        // Set up the dropdown menus (spinners) for filtering
        setupSpinners();

        // Load all loans into the list by default
        loadLoans(null, null, null, null);

        // Apply filters when the "Apply Filters" button is clicked
        buttonApplyFilters.setOnClickListener(v -> {
            String selectedBrand = spinnerFilterBrand.getSelectedItem().toString();
            String selectedCable = spinnerFilterCable.getSelectedItem().toString();
            String startDate = editStartDate.getText().toString().trim();
            String endDate = editEndDate.getText().toString().trim();

            // Apply filters and reload the list
            loadLoans(
                    selectedBrand.equals("Alle") ? null : selectedBrand,
                    selectedCable.equals("Alle") ? null : selectedCable,
                    startDate.isEmpty() ? null : startDate,
                    endDate.isEmpty() ? null : endDate
            );
        });

        // Handle item clicks in the ListView to delete loans
        listViewLoans.setOnItemClickListener((parent, view, position, id) -> {
            String loanInfo = loansList.get(position); // Get the selected loan info
            long loanId = extractLoanId(loanInfo);    // Extract the loan ID from the string

            if (loanId != -1) {
                int rowsDeleted = database.deleteLoan(loanId); // Delete the loan from the database
                if (rowsDeleted > 0) {
                    Toast.makeText(this, "Lån slettet!", Toast.LENGTH_SHORT).show(); // Show success message
                    loadLoans(null, null, null, null); // Reload all loans
                } else {
                    Toast.makeText(this, "Kunne ikke slette lånet!", Toast.LENGTH_SHORT).show(); // Show failure message
                }
            }
        });
    }

    /**
     * Sets up the spinners with options for filtering loans.
     */
    private void setupSpinners() {
        // Set up the spinner for tablet brands
        ArrayAdapter<CharSequence> brandAdapter = ArrayAdapter.createFromResource(this,
                R.array.tablet_brands, android.R.layout.simple_spinner_item);
        brandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilterBrand.setAdapter(brandAdapter);

        // Set up the spinner for cable types
        ArrayAdapter<CharSequence> cableAdapter = ArrayAdapter.createFromResource(this,
                R.array.cable_types, android.R.layout.simple_spinner_item);
        cableAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilterCable.setAdapter(cableAdapter);
    }

    /**
     * Loads loans into the ListView based on the provided filters.
     *
     * @param tabletBrand The selected tablet brand filter.
     * @param cableType   The selected cable type filter.
     * @param startDate   The start date filter.
     * @param endDate     The end date filter.
     */
    private void loadLoans(String tabletBrand, String cableType, String startDate, String endDate) {
        loansList.clear(); // Clear the current list
        Cursor cursor = database.getAllLoans(); // Retrieve all loans from the database

        // Iterate through the loans and apply filters
        while (cursor.moveToNext()) {
            String brand = cursor.getString(cursor.getColumnIndex("tablet_brand"));
            String cable = cursor.getString(cursor.getColumnIndex("cable_type"));
            String name = cursor.getString(cursor.getColumnIndex("borrower_name"));
            String contact = cursor.getString(cursor.getColumnIndex("contact_info"));
            String date = cursor.getString(cursor.getColumnIndex("loan_date"));
            long id = cursor.getLong(cursor.getColumnIndex("id"));

            // Check if the loan matches the filters
            boolean matchesDate = true;
            if (startDate != null && endDate != null) {
                matchesDate = isDateInRange(date, startDate, endDate);
            }

            if ((tabletBrand == null || tabletBrand.equals(brand)) &&
                    (cableType == null || cableType.equals(cable)) &&
                    matchesDate) {
                // Add the loan to the list
                loansList.add("ID: " + id + " | " + brand + " | " + cable + " | " +
                        name + " | " + contact + " | " + date);
            }
        }
        cursor.close(); // Close the database cursor

        // If no loans match the filters, display a message
        if (loansList.isEmpty()) {
            loansList.add("Ingen lån fundet."); // No loans found message
        }
        loansAdapter.notifyDataSetChanged(); // Update the ListView
    }

    /**
     * Checks if a given date is within the specified range.
     *
     * @param date      The date of the loan.
     * @param startDate The start date of the filter range.
     * @param endDate   The end date of the filter range.
     * @return True if the date is within the range, false otherwise.
     */
    private boolean isDateInRange(String date, String startDate, String endDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date loanDate = sdf.parse(date.split(" ")[0]); // Extract only the date part
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);
            return loanDate != null && !loanDate.before(start) && !loanDate.after(end);
        } catch (Exception e) {
            return false; // Return false if date parsing fails
        }
    }

    /**
     * Extracts the loan ID from the loan information string.
     *
     * @param loanInfo The loan information string.
     * @return The loan ID as a long value, or -1 if extraction fails.
     */
    private long extractLoanId(String loanInfo) {
        try {
            String idString = loanInfo.split(" \\| ")[0].replace("ID: ", ""); // Extract ID part
            return Long.parseLong(idString); // Convert to long
        } catch (Exception e) {
            return -1; // Return -1 if extraction fails
        }
    }
}