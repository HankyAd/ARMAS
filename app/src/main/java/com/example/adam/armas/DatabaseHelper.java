package com.example.adam.armas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Adam on 09/03/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    //database name is ARMASDB
    private static final String DATABASE_NAME = "ARMASDB";

    private static final int DATABASE_VERSION = 2;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table House (House_ID integer primary key not null, House_Number text, House_Street text, House_Postcode); create table Room (Room_ID integer primary key not null, Room_Name text, FOREIGN KEY(House_ID) REFERENCES House(House_ID) ); create table Asbestos(Asbestos_ID integer primary key not null, Asbestos_Desc text, Asbestos_Photo blob, FOREIGN KEY(House_ID) REFERENCES House(House_ID), FOREIGN KEY(Room_ID) REFERENCES Room(Room_ID));";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method is called during creation of the database

    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    // Method is called during an upgrade of the database,

    public void onUpgrade(SQLiteDatabase database,int oldVersion,int newVersion){
        Log.w(DatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS MyEmployees");
        onCreate(database);
    }
}

