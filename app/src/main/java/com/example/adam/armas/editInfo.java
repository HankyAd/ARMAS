package com.example.adam.armas;

<<<<<<< HEAD
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
=======
import android.database.Cursor;
>>>>>>> d9f0149c0728fb66a56a6347d709f206d0a0e2b7
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
<<<<<<< HEAD
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
=======
import android.widget.EditText;
import android.widget.ImageView;
>>>>>>> d9f0149c0728fb66a56a6347d709f206d0a0e2b7

public class editInfo extends AppCompatActivity {
    private DAO dao;
    private String imageName = "";
    private MainActivity mainActivity;
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;



    public editInfo() {
        super();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_info);


        final FloatingActionButton Save2 = (FloatingActionButton) findViewById(R.id.save2);
        final FloatingActionButton test = (FloatingActionButton) findViewById(R.id.float4);
        final Button button = (Button) findViewById(R.id.button12);
        final ImageView imageView = (ImageView) findViewById(R.id.imageView8);



        test.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }

        });

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                loadImagefromGallery(imageView);
            }

        });




        Save2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                ImageView imagView = (ImageView) findViewById(R.id.imageView8);
                imagView.buildDrawingCache();
                Bitmap bmp = imagView.getDrawingCache();
                File storageLoc = new File(getApplicationContext().getExternalFilesDir(null).getAbsolutePath().toString() + "/image"); //context.getExternalFilesDir(null);

                File file = new File(storageLoc, "demo1" + ".jpg");

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

<<<<<<< HEAD
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                ImageView imgView = (ImageView) findViewById(R.id.imageView8);
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

=======

    public void updateEntry(){

        String asbID = dao.getAsbID();

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


        String hID = dao.getAsbestosByAsbestosID(asbID).getString(4);
        String rID = dao.getAsbestosByAsbestosID(asbID).getString(3);
        ImageView img = findViewById(R.id.photo);
        //WRITE IMAGE TO EXTERNAL MEMORY

        if(asbestosDescrip != null || asbestosDescrip !=""){
            dao.updateRow("Description", asbestosDescrip, asbID, "Asbestos");
        }
        if(houseNum != null || houseNum != ""){
            dao.updateRow("House_Num", houseNum, hID, "House");
        }
        if(houseStreet != null || houseStreet != ""){
            dao.updateRow("House_Street", houseStreet, hID, "House");
        }
        if(housePostCode != null || housePostCode != ""){
            dao.updateRow("House_Postcode", housePostCode, hID, "House");
        }
        if(roomName != null || roomName != ""){
            dao.updateRow("Room_Name", roomName, rID, "Room");
        }
    }
>>>>>>> d9f0149c0728fb66a56a6347d709f206d0a0e2b7

}

