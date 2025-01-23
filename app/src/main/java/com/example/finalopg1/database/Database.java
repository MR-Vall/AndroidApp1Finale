package com.example.finalopg1.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Database class provides methods to interact with the SQLite database.
 * It handles CRUD operations for the "loans" table.
 */
public class Database {

    private SQLiteDatabaseHelper dbHelper; // Instance of SQLiteDatabaseHelper for database management

    /**
     * Constructor for the Database class.
     * @param context The application context.
     */
    public Database(Context context) {
        dbHelper = new SQLiteDatabaseHelper(context); // Initialize the database helper
    }

    /**
     * Inserts a new loan record into the "loans" table.
     * @param tabletBrand The brand of the tablet being loaned.
     * @param cableType The type of cable being loaned (optional).
     * @param borrowerName The name of the borrower.
     * @param contactInfo The contact information of the borrower (email or phone).
     * @param loanDate The date and time of the loan.
     * @return The row ID of the newly inserted record, or -1 if an error occurred.
     */
    public long insertLoan(String tabletBrand, String cableType, String borrowerName, String contactInfo, String loanDate) {
        SQLiteDatabase db = dbHelper.getWritableDatabase(); // Get writable database instance
        ContentValues values = new ContentValues(); // Prepare key-value pairs for the record
        values.put(SQLiteDatabaseHelper.COLUMN_TABLET_BRAND, tabletBrand); // Tablet brand
        values.put(SQLiteDatabaseHelper.COLUMN_CABLE_TYPE, cableType);     // Cable type
        values.put(SQLiteDatabaseHelper.COLUMN_BORROWER_NAME, borrowerName); // Borrower's name
        values.put(SQLiteDatabaseHelper.COLUMN_CONTACT_INFO, contactInfo);   // Borrower's contact info
        values.put(SQLiteDatabaseHelper.COLUMN_LOAN_DATE, loanDate);         // Loan date
        return db.insert(SQLiteDatabaseHelper.TABLE_NAME, null, values); // Insert record and return row ID
    }

    /**
     * Retrieves all loan records from the "loans" table.
     * @return A Cursor object containing the query results.
     */
    public Cursor getAllLoans() {
        SQLiteDatabase db = dbHelper.getReadableDatabase(); // Get readable database instance
        return db.query(
                SQLiteDatabaseHelper.TABLE_NAME, // Table name
                null,                            // Retrieve all columns
                null,                            // No WHERE clause (retrieve all records)
                null,                            // No selection arguments
                null,                            // No GROUP BY clause
                null,                            // No HAVING clause
                null                             // Default sort order
        );
    }

    /**
     * Deletes a loan record from the "loans" table based on its ID.
     * @param id The unique ID of the loan record to delete.
     * @return The number of rows affected (1 if successful, 0 otherwise).
     */
    public int deleteLoan(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase(); // Get writable database instance
        return db.delete(
                SQLiteDatabaseHelper.TABLE_NAME,                   // Table name
                SQLiteDatabaseHelper.COLUMN_ID + "=?",             // WHERE clause for deletion
                new String[]{String.valueOf(id)}                   // Arguments for the WHERE clause
        );
    }
}