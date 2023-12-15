package com.enjoy.crash2;

import android.content.Context;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.SDCardUtils;
import com.cyw.mylibrary.application.AppApplication;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private boolean isException = false;
    private static Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler;
    private static Context mContext;

    private CrashHandler() {

    }

    public static void init(Context applicationContext) {
        mContext = applicationContext;
        defaultUncaughtExceptionHandler =
                Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler());
    }

    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
        isException = true;
       // File dir = new File(mContext.getExternalFilesDir(null), "crash_info");//getExternalCacheDir
        File dir = new File(SDCardUtils.getSDCardPathByEnvironment() + "/colorformula" +"/crash_info");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String time = DateFormatter.getCurrentTime("yyyy-MM-dd");
        File file = new File(dir, time + ".txt");

        try {
            PrintWriter pw = new PrintWriter(new FileWriter(file));
            pw.println("time: xxx");
            pw.println("thread: " + t.getName());
            e.printStackTrace(pw);
            pw.flush();
            pw.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            AppApplication.getInstance().exit();
            if (defaultUncaughtExceptionHandler != null) {
                defaultUncaughtExceptionHandler.uncaughtException(t, e);
            }

        }


    }

    public boolean isException() {
        return isException;
    }
}
