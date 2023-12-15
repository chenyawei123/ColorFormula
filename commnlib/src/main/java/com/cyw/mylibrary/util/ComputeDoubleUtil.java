package com.cyw.mylibrary.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * author： cyw
 */
public class ComputeDoubleUtil {
    public static String computeDouble(double value){
        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.DOWN);
        String realDos = df.format(value);
        return realDos;
    }
}
