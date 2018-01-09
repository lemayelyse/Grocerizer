package com.example.grocerizersql;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Elyse on 11/19/2017.
 */

class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "Groceries.db";
    public static final String TABLE_NAME = "grocery_list";
    public static final String COL_ID = "ID";
    public static final String COL_KW = "Keyword";
    public static final String COL_NAME = "ItemName";
    public static final String COL_PRICE = "ItemPrice";

    public DatabaseHelper(android.content.Context context) {
        super(context, DB_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String createTable = "CREATE TABLE " + TABLE_NAME + " ( " +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_KW + " TEXT, " +
                COL_NAME + " TEXT, " + // Empty until we pull the ad
                COL_PRICE + " FLOAT )"; // Empty until we pull the ad

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void clearTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }

    // Keywords get added first like a "shopping list"
    public long addKeyword(String keyword) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COL_KW, keyword);
        // TODO: Check if keyword already exists before adding
        System.out.print(getTableAsString(db, TABLE_NAME));

        return db.insert(TABLE_NAME, null, cv);
    }

    // Add results from JSON parser into table
    public long addGroceryItem(String keyword, String name, Float price) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, name);
        cv.put(COL_PRICE, price);

        return db.update(TABLE_NAME, cv, "WHERE COL_NAME = ?", new String[] {keyword});
    }


    /**
     * FROM STACKOVERFLOW - I DID NOT WRITE THIS
     * This is just for debugging
     * * Helper function that parses a given table into a string
     * and returns it for easy printing. The string consists of
     * the table name and then each row is iterated through with
     * column_name: value pairs printed out.
     *
     * @param db the database to get the table from
     * @param tableName the the name of the table to parse
     * @return the table tableName as a string
     */
    public String getTableAsString(SQLiteDatabase db, String tableName) {
        String tableString = String.format("Table %s:\n", tableName);
        Cursor allRows  = db.rawQuery("SELECT * FROM " + tableName, null);
        if (allRows.moveToFirst() ){
            String[] columnNames = allRows.getColumnNames();
            do {
                for (String name: columnNames) {
                    tableString += String.format("%s: %s\n", name,
                            allRows.getString(allRows.getColumnIndex(name)));
                }
                tableString += "\n";

            } while (allRows.moveToNext());
        }

        return tableString;
    }

}


/*
This function is no longer needed now that the grocery ad is pulled to JSON on demand.
There is no need to store the ad results permanently... for now.
So the SQL table is just the user's shopping list and individualized results.

    // Search "Name" column for keyword
    // Display any matching rows
    public void searchKeyword(String keyword) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.query(TABLE_NAME, new String[]{COL_NAME},
                "COL_NAME LIKE ?" ,new String[]{"%"+keyword+"%"}, null, null, null);
        while(c.moveToNext())
        {
            // Display results
        }
        close();
    }
 */
