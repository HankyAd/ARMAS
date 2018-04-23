package com.example.adam.armas;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class editInfo extends AppCompatActivity {
    private DAO dao;
    private String imageName = "";
    private MainActivity mainActivity;

    public editInfo() {
        super();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_info);
    }
}
