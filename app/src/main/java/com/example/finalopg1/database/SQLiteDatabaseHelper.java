package com.example.finalopg1.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "LoanDatabase.db";
    private static final int DATABASE_VERSION = 1;

    // Table name and columns
    public static final String TABLE_NAME = "loans";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TABLET_BRAND = "tablet_brand";
    public static final String COLUMN_CABLE_TYPE = "cable_type";
    public static final String COLUMN_BORROWER_NAME = "borrower_name";
    public static final String COLUMN_CONTACT_INFO = "contact_info";
    public static final String COLUMN_LOAN_DATE = "loan_date";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TABLET_BRAND + " TEXT NOT NULL, " +
                    COLUMN_CABLE_TYPE + " TEXT, " +
                    COLUMN_BORROWER_NAME + " TEXT NOT NULL, " +
                    COLUMN_CONTACT_INFO + " TEXT, " +
                    COLUMN_LOAN_DATE + " TEXT NOT NULL);";

    public SQLiteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
