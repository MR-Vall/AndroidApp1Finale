package com.example.finalopg1.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLiteDatabaseHelper manages the creation, upgrading, and management of the SQLite database.
 * This helper class handles the "loans" table, which stores loan-related data.
 */
public class SQLiteDatabaseHelper extends SQLiteOpenHelper {

    // Name of the database file
    private static final String DATABASE_NAME = "LoanDatabase.db";

    // Version number of the database (used for upgrades)
    private static final int DATABASE_VERSION = 1;

    // Table name and column names for the "loans" table
    public static final String TABLE_NAME = "loans"; // Table name
    public static final String COLUMN_ID = "id"; // Unique ID for each loan (primary key)
    public static final String COLUMN_TABLET_BRAND = "tablet_brand"; // Brand of the tablet
    public static final String COLUMN_CABLE_TYPE = "cable_type"; // Type of cable (optional)
    public static final String COLUMN_BORROWER_NAME = "borrower_name"; // Name of the borrower
    public static final String COLUMN_CONTACT_INFO = "contact_info"; // Contact info (email or phone)
    public static final String COLUMN_LOAN_DATE = "loan_date"; // Date and time of the loan

    // SQL statement to create the "loans" table
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + // Auto-incrementing unique ID
                    COLUMN_TABLET_BRAND + " TEXT NOT NULL, " + // Tablet brand is required
                    COLUMN_CABLE_TYPE + " TEXT, " + // Cable type is optional
                    COLUMN_BORROWER_NAME + " TEXT NOT NULL, " + // Borrower's name is required
                    COLUMN_CONTACT_INFO + " TEXT, " + // Contact info is optional
                    COLUMN_LOAN_DATE + " TEXT NOT NULL);"; // Loan date is required

    /**
     * Constructor for SQLiteDatabaseHelper.
     * @param context The context of the application using this database.
     */
    public SQLiteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time.
     * @param db The SQLiteDatabase instance.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Execute the SQL command to create the "loans" table
        db.execSQL(TABLE_CREATE);
    }

    /**
     * Called when the database needs to be upgraded (e.g., schema changes).
     * @param db The SQLiteDatabase instance.
     * @param oldVersion The current version of the database.
     * @param newVersion The new version of the database.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the existing "loans" table if it exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Recreate the database with the new schema
        onCreate(db);
    }
}