package com.example.adam.armas;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import static cn.easyar.engine.EasyAR.getApplicationContext;

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

    public static boolean createDirIfNotExists() {
        boolean ret = true;

        File file = new File(Environment.getExternalStorageDirectory() + "/armas");
        if (!file.exists()) {
            if (!file.mkdirs()) {
                System.out.println("DIRECTORY CREATED");
                ret = false;
            }
        }

        file = new File(Environment.getExternalStorageDirectory() + "/image");
        if (!file.exists()) {
            if (!file.mkdirs()) {
                System.out.println("DIRECTORY CREATED");
                ret = false;
            }
        }

        return ret;
    }

    /**
     * Used to create initial records on initial creation of class. DO NOT RUN OUTSIDE OF INITIATOR
     */
    public void createRecords() {

        System.out.println("BEFORE");
        String query = "select * from Room";
        Cursor mCursor = database.rawQuery(query, null);
        if (mCursor.getCount() == 0) {
            System.out.println("AFTER");



            //insert dummy data into database for table House
            database.execSQL("insert into House (House_Number, House_Street, House_Postcode) values ('111', 'Somewhere Ave', 'PO1 0AA');");
            database.execSQL("insert into House (House_Number, House_Street, House_Postcode) values ('112', 'Somewhere Street', 'PO1 0AB');");
            database.execSQL("insert into House (House_Number, House_Street, House_Postcode) values ('113', 'Somewhere Lane', 'PO1 0AC');");
            database.execSQL("insert into House (House_Number, House_Street, House_Postcode) values ('114', 'Somewhere Square', 'PO1 0AD');");
            database.execSQL("insert into House (House_Number, House_Street, House_Postcode) values ('115', 'Somewhere Creek', 'PO1 0AE');");
            System.out.println("house inserted");
            //insert dummy data into database for table Room
            database.execSQL("insert into Room (Room_Name, House_ID) values ('Upstairs Master Bedroom', 1);");
            database.execSQL("insert into Room (Room_Name, House_ID) values ('Bathroom', 2);");
            database.execSQL("insert into Room (Room_Name, House_ID) values ('Kitchen', 3);");
            database.execSQL("insert into Room (Room_Name, House_ID) values ('Dining Room', 4);");
            database.execSQL("insert into Room (Room_Name, House_ID) values ('Lounge', 5);");
            System.out.println("room inserted");
            //insert dummy data into database for Asbestos

            String sql = "insert into Asbestos (Asbestos_Desc, Asbestos_Image_Name, Room_ID, House_ID) values ('Left window, upperleft side of windowsill', 'demo1' , 1, 1);";
            database.execSQL(sql);

            sql = "insert into Asbestos (Asbestos_Desc, Asbestos_Image_Name, Room_ID, House_ID) values ('Behind sink bowl', 'demo2', 2, 2);";
            database.execSQL(sql);

            sql = "insert into Asbestos (Asbestos_Desc, Asbestos_Image_Name, Room_ID, House_ID) values ('Underneath Oven', 'demo3', 3, 3);";
            database.execSQL(sql);

            sql = "insert into Asbestos (Asbestos_Desc, Asbestos_Image_Name, Room_ID, House_ID) values ('Underneath carpet, north side of room', 'demo4', 4, 4);";
            database.execSQL(sql);

            sql = "insert into Asbestos (Asbestos_Desc, Asbestos_Image_Name, Room_ID, House_ID) values ('Above main entrance doorway', 'demo5', 5, 5);";
            database.execSQL(sql);

            sql = "insert into Asbestos (Asbestos_Desc, Asbestos_Image_Name, Room_ID, House_ID) values ('Underneath carpet. Middle of Room', 'demo6', 5, 5);";
            database.execSQL(sql);

            sql = "insert into Asbestos (Asbestos_Desc, Asbestos_Image_Name, Room_ID, House_ID) values ('Cupboard under sink', 'demo7', 3, 3);";
            database.execSQL(sql);

            sql = "insert into Asbestos (Asbestos_Desc, Asbestos_Image_Name, Room_ID, House_ID) values ('Underneath Bath', 'demo8', 2, 2);";
            database.execSQL(sql);
            System.out.println("asb inserted");
        }
    }

    /**
     * Used to create asbestos record
     * @param desc
     * @param imgName
     * @param roomID
     * @param houseID
     */
    public void createAsbestos(String desc, String imgName, String roomID, String houseID){
        String sql = "insert into Asbestos (Asbestos_Desc, Asbestos_Image_Name, Room_ID, House_ID) values ('"+ desc +"', '"+ imgName +"' , "+roomID+", " + houseID + ");";
        database.execSQL(sql);
    }

    /**
     * Creates record in House table
     * @param houseNum
     * @param houseStreet
     * @param housePostcode
     */
    public void createHouse(String houseNum, String houseStreet, String housePostcode){
        database.execSQL( "insert into House (House_Number, House_Street, House_Postcode) values ('" + houseNum + "', '" + houseStreet + "', '" + housePostcode + "');" );
    }

    /**
     * Creates row in Room
     * @param roomName
     * @param houseID
     */
    public void createRoom(String roomName, String houseID){
        database.execSQL( "insert into Room (Room_Name, House_ID) values ("+ roomName +", "+ houseID +");" );
    }

    public void updateAsbestos(String roomName, String houseID){
        database.execSQL( "insert into Room (Room_Name, House_ID) values ("+ roomName +", "+ houseID +");" );
    }

    /**
     *
     * @param id
     * @return
     */
    public Cursor getRoomByID(String id) {
        String query = "select * from Room where Room_ID = " + id;
        Cursor mCursor = database.rawQuery(query, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor; // iterate to get each value.
    }

    /**
     * Returns row in room by inputted ID
     * @param RoomID
     * @return
     */
    public Cursor getRoomByID(int RoomID){
        String query = "select * from Room where Room_ID = " + RoomID;
        Cursor mCursor = database.rawQuery(query, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor; // iterate to get each value.
    }

    /**
     * Returns row in house by inputted ID
     * @param id
     * @return
     */
    public Cursor getHouseByID(int id){
        String query = "select * from House where House_ID = " + id;
        Cursor mCursor = database.rawQuery(query, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor; // iterate to get each value.
    }

    public Cursor getAsbestosByRoomID(String id){
        String query = "select * from Asbestos where Room_ID = " + id;
        Cursor mCursor = database.rawQuery(query, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor; // iterate to get each value.
    }

    /**
     * Returns row in asbestos by inputted ID
     * @param id
     * @return
     */
    public Cursor getAsbestosByAsbestosID(int id){
        String query = "select * from Asbestos where Asbestos_ID = " + id;
        Cursor mCursor = database.rawQuery(query, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor; // iterate to get each value.
    }

    /**
     * returns row in asbestos based on inputted image name
     * @param imgName
     * @return
     */
    public int getAsbIDByImageName(String imgName){
        String query = "select Asbestos_ID from Asbestos where Asbestos_Image_Name = '" + imgName + "'";
        Cursor mCursor = database.rawQuery(query, null);
        mCursor.moveToFirst();
        return mCursor.getInt(0);
    }

}

