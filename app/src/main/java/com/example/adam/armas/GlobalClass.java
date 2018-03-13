package com.example.adam.armas;

import android.app.Application;

/**
 * Created by Adam on 13/03/2018.
 */

public class GlobalClass extends Application {
    public static DAO dao;

    public void setDAO(DAO d){
        dao = d;
    }

    public DAO getDAO(){
        return dao;
    }
}
