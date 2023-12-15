package com.example.datacorrects.utils;

import android.app.Application;
import android.content.Context;

/**
 * Created by cyw on 2020/8/3.
 */

public class CorrectApplication extends Application {
    private static Context context;


    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
    public static Context getAppContext(){
        return context;
    }

}
