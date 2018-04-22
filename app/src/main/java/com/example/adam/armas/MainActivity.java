package com.example.adam.armas;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.io.*;
import java.io.FileReader;
import java.io.IOException;

import java.util.HashMap;

import cn.easyar.*;

import android.animation.Animator;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.webkit.WebView;
import android.widget.*;
import android.widget.RelativeLayout;


public class MainActivity extends AppCompatActivity{
    private GLView glView;
    private View vw;
    private MessageAlerter onAlert;
    private String targetName;
    private Boolean asbestosDetected = false;
    private String imageName = null;
    private DAO dataAccessObject;


    FloatingActionButton fab1, fab2, fab3, fab4;
    LinearLayout fabLayout1, fabLayout2, fabLayout3;
    View fabBGLayout;
    boolean isFABOpen = false;
    private WebView webView;


    public interface MessageAlerter {
        void invoke(String s);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DAO dao = new DAO(this);
        dataAccessObject = dao;
        System.out.println(dataAccessObject.getAsbIDByImageName("demo1"));

        GlobalClass g = (GlobalClass)this.getApplication();
        g.setDAO(dataAccessObject);
        Cursor asbRow = dao.getAsbestosRow();
        Cursor roomRow = dao.getRoomRow(asbRow);
        Cursor houseRow = dao.getHouseRow(asbRow);

//        System.out.println(asbRow.getString(1) + " | " + asbRow.getString(2) + " | " + roomRow.getString(2) + " | " + houseRow.getString(2));

        String key = "Dvwi44jdfu7RFcq2jN6vFBNUKTHiStKtfoevOz3CzTKGtz4J1tHCu9MtyYorV0sJGJFQs747c7Uu07S39cYooN4sNqBeE7gi8DRg8oZN25VdKdVbK3csqyWPuBNoqmy9FimsvyOU81Bd0LB9XX8Gga13OOp33x1XFtod00m7Wh3xsn8f9VCfsIIucdki8OpnQeuswZNo";
        Engine.initialize(this, key);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        final FloatingActionButton InfoButton = (FloatingActionButton) findViewById(R.id.InfoButton);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);


        //fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        //fab2= (FloatingActionButton) findViewById(R.id.fab2);
        //fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        //fab4 = (FloatingActionButton) findViewById(R.id.fab4);


        fab.setOnClickListener(new View.OnClickListener() { //Camera button
            boolean clicked = false;

            public void onClick(View view) {   // when the camera button is clicked
                if (!clicked) {
                    clicked = true;
                    ((ViewGroup) findViewById(R.id.preview)).addView(glView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)); // Creates a new View
                    glView.onResume();
                } else {
                    clicked = false;
                    glView.onPause();
                    ((ViewGroup) findViewById(R.id.preview)).removeAllViews();
                }

            }
        });


        InfoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (asbestosDetected) {
                    Intent intent2 = new Intent(MainActivity.this, InfoActivity.class);
                    intent2.putExtra("IMAGE_NAME", imageName);
                    startActivity(intent2);
                }

            }
        });


        glView = new GLView(this, this);

        requestCameraPermission(new PermissionCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure() {
            }
        });
    }


    public void setImageName(String imgName) {
        imageName = imgName;
    }

    public String getImageName(){
        return imageName;
    }

    public void setAsbestosTrue() {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    FloatingActionButton InfoButton = (FloatingActionButton) findViewById(R.id.InfoButton);
                    InfoButton.setVisibility(findViewById(R.id.InfoButton).VISIBLE);
                }
            });
            asbestosDetected = true;
        } catch (Exception ex) {

        }
    }

    public void setAsbestosFalse() {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    FloatingActionButton InfoButton = (FloatingActionButton) findViewById(R.id.InfoButton);
                    InfoButton.setVisibility(findViewById(R.id.InfoButton).INVISIBLE);
                }
            });
            asbestosDetected = false;
        } catch (Exception ex) {

        }
    }


    private interface PermissionCallback {
        void onSuccess();

        void onFailure();
    }

    private HashMap<Integer, PermissionCallback> permissionCallbacks = new HashMap<Integer, PermissionCallback>();
    private int permissionRequestCodeSerial = 0;

    @TargetApi(23)
    private void requestCameraPermission(PermissionCallback callback) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                int requestCode = permissionRequestCodeSerial;
                permissionRequestCodeSerial += 1;
                permissionCallbacks.put(requestCode, callback);
                requestPermissions(new String[]{Manifest.permission.CAMERA}, requestCode);
            } else {
                callback.onSuccess();
            }
        } else {
            callback.onSuccess();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (permissionCallbacks.containsKey(requestCode)) {
            PermissionCallback callback = permissionCallbacks.get(requestCode);
            permissionCallbacks.remove(requestCode);
            boolean executed = false;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    executed = true;
                    callback.onFailure();
                }
            }
            if (!executed) {
                callback.onSuccess();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (glView != null) {
            glView.onResume();
        }
    }

    @Override
    protected void onPause() {
        if (glView != null) {
            glView.onPause();
        }
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public DAO getDAO() {
        return dataAccessObject;
    }


    ////untill here
    //Text render code4


}


//  @Override
//public boolean onCreateOptionsMenu(Menu menu) {
//       Inflate the menu; this adds items to the action bar if it is present.
//    getMenuInflater().inflate(R.menu.menu_main, menu);
//  return true;
//}
//@Override
//public boolean onOptionsItemSelected(MenuItem item) {
// Handle action bar item clicks here. The action bar will
// automatically handle clicks on the Home/Up button, so long
// as you specify a parent activity in AndroidManifest.xml.
//    int id = item.getItemId();

//noinspection SimplifiableIfStatement
//  if (id == R.id.action_settings) {
//    return true;
//  }

// return super.onOptionsItemSelected(item);
// }
// }
