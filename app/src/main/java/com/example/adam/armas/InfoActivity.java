package com.example.adam.armas;
import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import java.io.IOException;
import java.io.InputStream;

import static android.view.View.VISIBLE;
import static cn.easyar.engine.EasyAR.getApplicationContext;

public class InfoActivity extends Activity {
    private DAO dao;
    private String imageName = "";
    private MainActivity mainActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        FloatingActionButton backFab = (FloatingActionButton) findViewById(R.id.backFabb);
        FloatingActionButton editInfo = (FloatingActionButton) findViewById(R.id.editInfo);

        backFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                    finish();
            }

        });

        editInfo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), editInfo.class));
            }
        });


        String s = getIntent().getStringExtra("IMAGE_NAME");
        System.out.println("IMAGE NAME IS: " + s);
        imageName = s;
        //comment
        GlobalClass g = (GlobalClass)this.getApplication();
        dao = g.getDAO();




        String asbID = dao.getAsbIDByImageName(imageName);
        dao.setAsbID("" + asbID);

        Cursor asbestos = dao.getAsbestosByAsbestosID(asbID);



        Cursor asb = dao.getAsbestosByAsbestosID(asbID);
        asb.moveToFirst();

        TextView t = findViewById(R.id.textView9);
        t.setText("Description: " + asb.getString(1));

        Cursor room = dao.getRoomByID(asbestos.getString(3));
        room.moveToFirst();

        t = findViewById(R.id.textView10);
        t.setText("Room Name: " + room.getString(1));

        Cursor house = dao.getHouseByID(asbestos.getString(3));
        house.moveToFirst();

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
            System.out.println("/storage/emulated/0/Android/data/com.example.adam.armas/files/image/" +imageName+".jpg");
            // get input stream
            InputStream ims = getAssets().open("/storage/emulated/0/Android/data/com.example.adam.armas/files/image/" +imageName+".jpg");
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            // set image to ImageView
            i.setImageDrawable(d);
            i.setVisibility(VISIBLE);
            ims.close();
        }
        catch(IOException ex)
        {
            System.out.println(ex);
        }
    }
}
