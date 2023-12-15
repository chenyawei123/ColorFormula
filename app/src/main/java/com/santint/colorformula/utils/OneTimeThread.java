package com.santint.colorformula.utils;

import android.content.Context;

/**
 * author： cyw
 */
public class OneTimeThread extends Thread{
    private static OneTimeThread instance;
    private Context mContext;
    public static OneTimeThread getInstance(Context context) {
        if (instance == null) {
            synchronized (OneTimeThread.class) {
                if (instance == null) {
                    instance = new OneTimeThread(context);
                }
            }
        }
        return instance;
    }

    public OneTimeThread(Context context) {
        mContext = context;
    }

    @Override
    public void run() {
        super.run();
    }
}
