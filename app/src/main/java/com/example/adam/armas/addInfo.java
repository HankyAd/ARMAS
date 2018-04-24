package com.example.adam.armas;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

import static android.view.View.VISIBLE;

public class addInfo extends AppCompatActivity {
    private DAO dao;
    private String imageName = "";
    private MainActivity mainActivity;
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;
    public addInfo() {
        super();
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        GlobalClass g = (GlobalClass)this.getApplication();
        dao = g.getDAO();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_info);


        FloatingActionButton test = (FloatingActionButton) findViewById(R.id.floatingActionButton3);
        final Button button = (Button) findViewById(R.id.button10);
        final ImageView imgView = (ImageView) findViewById(R.id.imageView5);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                loadImagefromGallery(imgView);
            }

        });


        test.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }

        });



    }




    public void loadImagefromGallery(View view) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                ImageView imgView = (ImageView) findViewById(R.id.imageView5);
                // Set the Image in ImageView after decoding the String
                imgView.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }

    public boolean validEntry(){
        return true;
    }

    public void storeEntry(){
        EditText t = findViewById(R.id.descip);
        String asbestosDescrip = t.getText().toString();
        t = findViewById(R.id.houseNumber);
        String houseNum = t.getText().toString();
        t = findViewById(R.id.street);
        String houseStreet = t.getText().toString();
        t = findViewById(R.id.postCode);
        String housePostCode = t.getText().toString();
        t = findViewById(R.id.roomName);
        String roomName = t.getText().toString();


        ImageView img = findViewById(R.id.photo);
        //WRITE IMAGE TO EXTERNAL MEMORY

        dao.createHouse(houseNum, houseStreet, housePostCode);
        Cursor house = dao.getHouseID(houseNum, houseStreet, housePostCode);
        if(house.getCount()>0){
            dao.createRoom(roomName, house.getString(0));
            Cursor room = dao.getRoomByHouseID(house.getString(0));
            if(room.getCount() > 0){
                dao.createAsbestos(asbestosDescrip, "", house.getString(0), room.getString(0));
            }
        }
    }

}

