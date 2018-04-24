package com.example.adam.armas;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;

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

}
