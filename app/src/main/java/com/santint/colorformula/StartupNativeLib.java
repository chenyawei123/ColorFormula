package com.santint.colorformula;

/**
 * author： cyw
 */
public class StartupNativeLib {
    static {
        System.loadLibrary("startup-optimize");
    }
    public native void delayGC();
}
