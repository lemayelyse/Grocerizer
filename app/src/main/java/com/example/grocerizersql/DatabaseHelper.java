package com.example.grocerizersql;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.util.ArrayList;

/**
 * All SQLite related functions. Created by Elyse on 11/19/2017.
 */

class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "Groceries.db";
    private static final String TABLE_NAME = "grocery_list";
    private static final String COL_ID = "ID";
    private static final String COL_KW = "Keyword";
    private static final String COL_NAME = "ItemName";
    private static final String COL_PRICE = "ItemPrice";

    DatabaseHelper(android.content.Context context) {
        super(context, DB_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String createTable = "CREATE TABLE " + TABLE_NAME + " ( " +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_KW + " TEXT UNIQUE, " +
                COL_NAME + " TEXT, " + // Empty until we pull the ad
                COL_PRICE + " FLOAT )"; // Empty until we pull the ad

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // This drops the entire table to get rid of the autoincrement.
    void clearTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        onUpgrade(db, 0, 0);
    }

    void printTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        System.out.println(getTableAsString(db, TABLE_NAME));
    }

    // Keywords get added first like a "shopping list"
    void addKeyword(String keyword) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COL_KW, keyword);
        // "Keyword" column is unique
        // Could actually run a search to check if it exists, or a partial match...
        // But that's overkill for an app as simple as this

        try {
            db.insertOrThrow(TABLE_NAME, null, cv);
            System.out.println("Added keyword " + keyword);
        } catch (SQLiteConstraintException exc1) {
            Log.e("SQLiteException", "Keyword already exists!");
        } catch (SQLiteException exc) {
            Log.e("SQLiteException", "Failed to add keyword. Try hitting the Clear button" );
        }
    }

    // Gets list of keywords in table
    // Then calls "addGroceryItem" for each keyword with a matching item in JSON array
    void populateTable(JsonArray items) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = { COL_KW }; // Must be an array to work with db.query

        ArrayList <String> kwList = new ArrayList<String>();
        Cursor c = db.query(TABLE_NAME, args, null, null, null, null, null);
        while (c.moveToNext())
        {
            kwList.add( c.getString(c.getColumnIndex(COL_KW)) ); // Gets the keyword for this row
        }
        c.close();

        for(JsonElement itm : items){
            try {
                String name = itm.getAsJsonObject().get("name").getAsString();
                for (String kw : kwList) {
                    if (name.toLowerCase().contains(kw.toLowerCase())){
                        System.out.println("Match! Keyword: " + kw + ", Name: " + name);
                        addGroceryItem(kw, itm.getAsJsonObject());
                    }
                }
            } catch (JsonParseException exc) {
                Log.e("JsonParseException", exc.toString());
                exc.printStackTrace();
            }
        }
        printTable();
    }

    // Add name and price of individual JSON item into table
    // in row of relevant keyword
    private void addGroceryItem(String keyword, JsonObject itm) {
        SQLiteDatabase db = this.getWritableDatabase();
        Float price = 0.0f;
        String name = "";
        String whereClause = COL_KW + " = ?";

        try {
            price = itm.get("current_price").getAsFloat();
            name = itm.get("name").getAsString();
        } catch (JsonParseException jsonexc) {
            Log.e("JSONException", jsonexc.toString());
            jsonexc.printStackTrace();
        }

        // TODO: work in "price text" so it appears as 3.99/lb instead of 3.99
        // TODO: Make integer floats display as X.00
        ContentValues cv = new ContentValues(); // Java will garbage-clean this
        cv.put(COL_NAME, name);
        cv.put(COL_PRICE, price);

        // TODO: Account for multiple product matches on the same keyword
        // eg "Tri-tip steaks" in a separate row from "Boneless Steaks" for "Steaks" kw
        try {
            db.update(TABLE_NAME, cv, whereClause, new String[]{keyword});
        } catch (SQLiteException exc) {
            Log.e("SQLiteException", "Failed to update table in AddGroceryItem");
        }
    }




    // TODO: Replace this function with one that displays table in app
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
    private String getTableAsString(SQLiteDatabase db, String tableName) {
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
        allRows.close();
        return tableString;
    }

}




/*
This function is no longer needed now that the grocery ad is pulled to JSON on demand.
There is no need to store the ad results permanently... for now.
So the SQL table is just the user's shopping list and individualized results.
Later, I might add a feature to search a stored database while adding keywords,
so it's not necessary to fetch each time you add a new keyword.

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
