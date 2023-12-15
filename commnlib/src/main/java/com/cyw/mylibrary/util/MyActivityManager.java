package com.cyw.mylibrary.util;

import android.app.Activity;

import java.lang.ref.WeakReference;

/**
 * author： cyw
 */
public class MyActivityManager {
    private WeakReference<Activity> sCurrentActivityWeakRef;
    private static volatile  MyActivityManager instance;
    private MyActivityManager(){

    }
    public static MyActivityManager getInstance(){
        if(instance == null){
            synchronized (MyActivityManager.class){
                if(instance == null){
                    instance = new MyActivityManager();
                }
            }
        }
        return instance;
    }
    public void setsCurrentActivity(Activity activity){
        sCurrentActivityWeakRef = new WeakReference<Activity>(activity);
    }
    public Activity getCurrentActivity(){
        Activity currentActivity = null;
        if(sCurrentActivityWeakRef!=null){
            currentActivity = sCurrentActivityWeakRef.get();
        }
        return currentActivity;
    }
}
