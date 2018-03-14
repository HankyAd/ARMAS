package com.example.adam.armas;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.icu.text.IDNA;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static android.view.View.VISIBLE;

public class InfoActivity extends AppCompatActivity {
    private DAO dao;
    private String imageName = "";
    private MainActivity mainActivity;

    public InfoActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_info);
        final FloatingActionButton backFab = (FloatingActionButton) findViewById(R.id.backFab);

        backFab.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                finish();

            }

        });




        String s = getIntent().getStringExtra("IMAGE_NAME");
        imageName = s;
        //comment
        GlobalClass g = (GlobalClass)this.getApplication();
        dao = g.getDAO();


        setContentView(R.layout.activity_info);
        //fgsbfs/
        System.out.println(imageName + "Hello ");
        int asbID = dao.getAsbIDByImageName(imageName);
        Cursor room = dao.getRoomByAsbestos(asbID);
        Cursor house = dao.getHouseByAsbestos(asbID);
        Cursor asb = dao.getAsbestosByAsbestosID(asbID);

        TextView t = findViewById(R.id.textView9);
        t.setText("Description: " + asb.getString(1));

        t = findViewById(R.id.textView10);
        t.setText("Room Name: " + room.getString(1));

        t = findViewById(R.id.textView11);
        t.setText("Number: " + house.getString(1));

        t = findViewById(R.id.textView12);
        t.setText("Street: " + house.getString(2));

        t = findViewById(R.id.textView13);
        t.setText("Postcode: " + house.getString(3));

        ImageView i = findViewById(R.id.imageView5);

        System.out.println(imageName);

        try
        {
            // get input stream
            InputStream ims = getAssets().open("demo/"+imageName+".jpg");
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            // set image to ImageView
            i.setImageDrawable(d);
            i.setVisibility(VISIBLE);
            ims.close();
        }
        catch(IOException ex)
        {
            return;
        }
    }
}
