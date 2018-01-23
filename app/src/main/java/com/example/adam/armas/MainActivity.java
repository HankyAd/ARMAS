package com.example.adam.armas;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import cn.easyar.*;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String key = "Dvwi44jdfu7RFcq2jN6vFBNUKTHiStKtfoevOz3CzTKGtz4J1tHCu9MtyYorV0sJGJFQs747c7Uu07S39cYooN4sNqBeE7gi8DRg8oZN25VdKdVbK3csqyWPuBNoqmy9FimsvyOU81Bd0LB9XX8Gga13OOp33x1XFtod00m7Wh3xsn8f9VCfsIIucdki8OpnQeuswZNo";
        Engine.initialize(this, key);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraDevice cd = new CameraDevice();
                CameraFrameStreamer cfs = new CameraFrameStreamer();
                cfs.attachCamera(cd);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
