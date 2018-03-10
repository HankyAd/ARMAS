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

    /**
     *
     * @param context
     */
    public DAO(Context context){
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        createRecords();
    }


    public void createRecords(){
        database.execSQL("insert into House (House_Number, House_Street, House_Postcode) values ('123', 'Somewhere Ave', 'PO1 0AA');");
    }

    public Cursor selectRecords() {
        Cursor mCursor = database.rawQuery("select * from House",null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor; // iterate to get each value.
    }
}

