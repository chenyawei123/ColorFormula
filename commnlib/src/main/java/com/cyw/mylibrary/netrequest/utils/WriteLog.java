package com.net.netrequest.utils;

import android.util.Log;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Calendar;

/**
 * Created by cyw on 2019/12/10.
 */

public class WriteLog {

    public static String getTimeFood(){
        Calendar mInstance = Calendar.getInstance();
        int YEAR = mInstance.get(Calendar.YEAR);
        int MONTH = mInstance.get(Calendar.MONTH)+1;
        int DAY = mInstance.get(Calendar.DAY_OF_MONTH);
        int HOUR = mInstance.get(Calendar.HOUR_OF_DAY);
        int MINUTE = mInstance.get(Calendar.MINUTE);
        int SECOND = mInstance.get(Calendar.SECOND);
        return YEAR+"-"+MONTH+"-"+DAY+"   "+HOUR+"："+MINUTE+"："+SECOND+":\r\n";
    }
   /* public void initFile(){
        Calendar mInstance = Calendar.getInstance();
        int YEAR = mInstance.get(Calendar.YEAR);
        int MONTH = mInstance.get(Calendar.MONTH)+1;
        int DAY = mInstance.get(Calendar.DAY_OF_MONTH);
        fileName = YEAR+"-"+MONTH+"-"+DAY+"log.txt";
    }*/
    // 将字符串写入到文本文件中
    public static void writeTxtToFile(String strcontent) {

        Calendar mInstance = Calendar.getInstance();
        int YEAR = mInstance.get(Calendar.YEAR);
        int MONTH = mInstance.get(Calendar.MONTH)+1;
        int DAY = mInstance.get(Calendar.DAY_OF_MONTH);
        String filePath = "/sdcard/"+"/colorformula"+"/AMilk/";
        String fileName = YEAR+"-"+MONTH+"-"+DAY+"log.txt";
        //生成文件夹之后，再生成文件，不然会出错
        makeFilePath(filePath, fileName);

        String strFilePath = filePath+fileName;
        // 每次写入时，都换行写
        String strContent = strcontent + "\r\n"+"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------"+ "\r\n";
        try {
            File file = new File(strFilePath);
            if (!file.exists()) {
                Log.d("TestFile", "Create the file:" + strFilePath);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
        }
    }
    // 生成文件
    public static File makeFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }
    // 生成文件夹
    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
        }
    }
}
