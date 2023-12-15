package com.santint.colorformula;

import android.os.Build;

/**
 * author： cyw
 */
public class StartupOptimize {
    static void delayGC(){
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.Q){
            new StartupNativeLib().delayGC();
        }
    }

}
