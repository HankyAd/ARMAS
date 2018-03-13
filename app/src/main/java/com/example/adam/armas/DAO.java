package com.example.adam.armas;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Adam on 09/03/2018.
 */

public class DAO {

    private DatabaseHelper dbHelper;

    private SQLiteDatabase database;

    private Context cont;

    /**
     * @param context
     */
    public DAO(Context context) {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        cont = context;
        createRecords();
    }


    public void createRecords() {
        //insert dummy data into database for table House
        database.execSQL("insert into House (House_Number, House_Street, House_Postcode) values ('111', 'Somewhere Ave', 'PO1 0AA');");
        database.execSQL("insert into House (House_Number, House_Street, House_Postcode) values ('112', 'Somewhere Street', 'PO1 0AB');");
        database.execSQL("insert into House (House_Number, House_Street, House_Postcode) values ('113', 'Somewhere Lane', 'PO1 0AC');");
        database.execSQL("insert into House (House_Number, House_Street, House_Postcode) values ('114', 'Somewhere Square', 'PO1 0AD');");
        database.execSQL("insert into House (House_Number, House_Street, House_Postcode) values ('115', 'Somewhere Creek', 'PO1 0AE');");
        //insert dummy data into database for table Room
        database.execSQL("insert into Room (Room_Name, House_ID) values ('Upstairs Master Bedroom', 1);");
        database.execSQL("insert into Room (Room_Name, House_ID) values ('Bathroom', 2);");
        database.execSQL("insert into Room (Room_Name, House_ID) values ('Kitchen', 3);");
        database.execSQL("insert into Room (Room_Name, House_ID) values ('Dining Room', 4);");
        database.execSQL("insert into Room (Room_Name, House_ID) values ('Lounge', 5);");
        //insert dummy data into database for Asbestos

        String sql = "insert into Asbestos (Asbestos_Desc, Asbestos_Image_Name, Room_ID, House_ID) values ('Left window, upperleft side of windowsill', 'image1' , 1, 1);";
        database.execSQL(sql);

        sql = "insert into Asbestos (Asbestos_Desc, Asbestos_Image_Name, Room_ID, House_ID) values ('Behind sink bowl', 'image2', 2, 2);";
        database.execSQL(sql);

        sql = "insert into Asbestos (Asbestos_Desc, Asbestos_Image_Name, Room_ID, House_ID) values ('Underneath Oven', 'image3', 3, 3);";
        database.execSQL(sql);

        sql = "insert into Asbestos (Asbestos_Desc, Asbestos_Image_Name, Room_ID, House_ID) values ('Underneath carpet, north side of room', 'image4', 4, 4);";
        database.execSQL(sql);

        sql = "insert into Asbestos (Asbestos_Desc, Asbestos_Image_Name, Room_ID, House_ID) values ('Above main entrance doorway', 'image5', 5, 5);";
        database.execSQL(sql);
    }

    public Cursor getAsbestosRow() {
        Cursor mCursor = database.rawQuery("select * from Asbestos", null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor; // iterate to get each value.
    }

    public Cursor getRoomRow(Cursor asb) {
        String query = "select * from Room where Room_ID = " + asb.getString(3);
        Cursor mCursor = database.rawQuery(query, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor; // iterate to get each value.
    }

    public Cursor getHouseRow(Cursor asb) {
        String query = "select * from House where House_ID = " + asb.getString(4);
        Cursor mCursor = database.rawQuery(query, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor; // iterate to get each value.
    }

    public Cursor selectRecords() {
        Cursor mCursor = database.rawQuery("select * from House", null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor; // iterate to get each value.
    }

    public static Bitmap getBitmapFromAsset(Context context, String filePath) {
        AssetManager assetManager = context.getAssets();

        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(filePath);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            // handle exception
        }

        return bitmap;
    }

    public Cursor getRoomByID(String id) {
        String query = "select * from Room where Room_ID = " + id;
        Cursor mCursor = database.rawQuery(query, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        System.out.println("ROOM NAME IS " + mCursor.getString(1));
        return mCursor; // iterate to get each value.
    }
}

