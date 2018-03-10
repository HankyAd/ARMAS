package com.example.adam.armas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Adam on 09/03/2018.
 */

public class DAO{

    private DatabaseHelper dbHelper;

    private SQLiteDatabase database;

    public final static String House_Table="House_ID"; // name of table

    public final static String House_ID="House_ID"; // id value for employee
    public final static String House_Number="123";  // name of employee

    /**
     *
     * @param context
     */
    public DAO(Context context){
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }


    public long createRecords(String id, String name){
        ContentValues values = new ContentValues();
        values.put(House_Number, name);
        return database.insert(House_Table, null, values);
    }

    public Cursor selectRecords() {
        String[] cols = new String[] {House_ID, House_Number};
        Cursor mCursor = database.query(true, House_Table, cols,null
                , null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor; // iterate to get each value.
    }
}

