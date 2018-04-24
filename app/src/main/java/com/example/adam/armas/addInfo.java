package com.example.adam.armas;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import static android.media.MediaScannerConnection.scanFile;
import static android.view.View.VISIBLE;
import static cn.easyar.engine.EasyAR.getApplicationContext;

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

        final FloatingActionButton Save1 = (FloatingActionButton) findViewById(R.id.Save1);
        final FloatingActionButton test = (FloatingActionButton) findViewById(R.id.floatingActionButton4);
        final Button button = (Button) findViewById(R.id.button10);
        final ImageView imageView = (ImageView) findViewById(R.id.imageView5);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                loadImagefromGallery(imageView);
            }

        });


        test.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }

        });


        Save1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(validEntry()){
                    storeEntry();
                }


                ImageView imagView = (ImageView) findViewById(R.id.imageView5);
                imagView.buildDrawingCache();
                Bitmap bmp = imagView.getDrawingCache();
                File storageLoc = new File(getApplicationContext().getExternalFilesDir(null).getAbsolutePath().toString() + "/image"); //context.getExternalFilesDir(null);

                File file = new File(storageLoc, "image" + ".jpg");

                try{

                    FileOutputStream fos = new FileOutputStream(file);
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }






            }



        });


    }






    public void loadImagefromGallery(View view) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent

        galleryIntent.setType("image/*");
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
        System.out.println(roomName);

        dao.createHouse(houseNum, houseStreet, housePostCode);
        Cursor house = dao.getHouseID(houseNum, houseStreet, housePostCode);
        System.out.println("before");
        if(house.getCount()>0){
            System.out.println("house getcount > 0    = " + house.getCount());
            dao.createRoom(roomName, house.getString(0));
            System.out.println(house.getString(0));
            Cursor room = dao.getRoomByHouseID(house.getString(0));
            if(room.getCount() > 0){
                System.out.println("HELP "+dao.retRoomByHouseID(house.getString(0)));
                dao.createAsbestos(asbestosDescrip, "image", house.getString(0), room.getString(0));

            }
        }
    }

}


