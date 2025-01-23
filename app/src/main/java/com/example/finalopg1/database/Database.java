package com.example.finalopg1.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Database {

    private SQLiteDatabaseHelper dbHelper;

    public Database(Context context) {
        dbHelper = new SQLiteDatabaseHelper(context);
    }

    // // Insert new loan record
    public long insertLoan(String tabletBrand, String cableType, String borrowerName, String contactInfo, String loanDate) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SQLiteDatabaseHelper.COLUMN_TABLET_BRAND, tabletBrand);
        values.put(SQLiteDatabaseHelper.COLUMN_CABLE_TYPE, cableType);
        values.put(SQLiteDatabaseHelper.COLUMN_BORROWER_NAME, borrowerName);
        values.put(SQLiteDatabaseHelper.COLUMN_CONTACT_INFO, contactInfo);
        values.put(SQLiteDatabaseHelper.COLUMN_LOAN_DATE, loanDate);
        return db.insert(SQLiteDatabaseHelper.TABLE_NAME, null, values);
    }


    // Retrieve all loans
    public Cursor getAllLoans() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(
                SQLiteDatabaseHelper.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    // delete loan
    public int deleteLoan(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(SQLiteDatabaseHelper.TABLE_NAME, SQLiteDatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }
}
