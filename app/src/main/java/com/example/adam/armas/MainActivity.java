package com.example.adam.armas;
import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
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


public class MainActivity extends AppCompatActivity {
    private GLView glView;
    private MessageAlerter onAlert;
    private String targetName;
    private Boolean asbestosDetected = false;
    private String imageName = null;
    //DAO dao = new DAO(this);

    public interface MessageAlerter
    {
        void invoke(String s);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //dao.createRecords();
        //Cursor c = dao.selectRecords();
        //AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        //alertDialog.setMessage(c.getString(2));
        //alertDialog.show();
        String key = "Dvwi44jdfu7RFcq2jN6vFBNUKTHiStKtfoevOz3CzTKGtz4J1tHCu9MtyYorV0sJGJFQs747c7Uu07S39cYooN4sNqBeE7gi8DRg8oZN25VdKdVbK3csqyWPuBNoqmy9FimsvyOU81Bd0LB9XX8Gga13OOp33x1XFtod00m7Wh3xsn8f9VCfsIIucdki8OpnQeuswZNo";
        Engine.initialize(this, key);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton InfoButton = (FloatingActionButton) findViewById(R.id.InfoButton);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            boolean clicked = false;
            public void onClick(View view) {
                if (!clicked) {
                    clicked = true;
                    ((ViewGroup) findViewById(R.id.preview)).addView(glView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    //InfoButton.setVisibility(view.VISIBLE);
                    glView.onResume();
                } else {
                    clicked = false;
                    glView.onPause();
                    ((ViewGroup) findViewById(R.id.preview)).removeAllViews();
                    //InfoButton.setVisibility(view.INVISIBLE);
                }

            }
        });


        InfoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (asbestosDetected) {
                    System.out.println("Detected");
                } else {
                    System.out.println("Not Detected");
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

    public void setImageName(String imgName){
        imageName = imgName;
    }

    public void setAsbestosTrue(){
        FloatingActionButton InfoButton = (FloatingActionButton) findViewById(R.id.InfoButton);
        InfoButton.setVisibility(glView.VISIBLE);
        asbestosDetected = true;

    }
    public void setAsbestosFalse(){
        FloatingActionButton InfoButton = (FloatingActionButton) findViewById(R.id.InfoButton);
        InfoButton.setVisibility(glView.INVISIBLE);
        asbestosDetected = false;
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
