package com.cyw.mylibrary.util;


import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cyw on 2019/12/16.
 */

public class HightLowEight {

//    /**
//     * 验证校验和
//     */
//    public static boolean IsLowEight(byte[] dataByte) {
//        boolean isTrue = false;
//        List<Integer> lowEightList = GetLowEight(dataByte);
//        int lowOne = lowEightList.get(0);
//        int lowTwo = lowEightList.get(1);
//        //获取两位校验和（接收获取）
//        int lowOneGet = dataByte[dataByte.length - 2];
//        int lowTwoGet = dataByte[dataByte.length - 3];
//        //本地计算和接收获取进行对比
//        //LogUtils.eTag("需要对比的数据", lowOne+"+"+lowOneGet+"+"+lowTwo+"+"+lowTwoGet);
//        isTrue = lowOne == lowOneGet && lowTwo == lowTwoGet;
//        String mcId = SPUtils.getInstance().getString("McId", "");
//        final byte[] bytesMcId = mcId.getBytes();
//        if (isTrue == true) {
//            new Thread(new Runnable() {
//                public void run() {
//                    try {
//                        Thread.sleep(400);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    byte[] rsp = new byte[2];
//                    rsp[0] = 6;
//                    rsp[1] = bytesMcId[0];
//                    NetApplication.mSocketClientFood.send(rsp);
////                    LogUtils.eTag("接收到传送线校验和回复接受（接收）",
////                            "回复接受"+rsp[0]+"+"+rsp[1]);
//                }
//            }).start();
//        } else {
//            new Thread(new Runnable() {
//                public void run() {
//                    try {
//                        Thread.sleep(400);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    byte[] rsp = new byte[2];
//                    rsp[0] = 21;
//                    rsp[1] = bytesMcId[0];
//                    NetApplication.mSocketClientFood.send(rsp);
////                    LogUtils.eTag("接收到传送线校验和回复拒绝（接收）",
////                            "回复拒绝");
//                }
//            }).start();
//        }
//        return isTrue;
//    }

    /**
     * 计算低8位
     */

    public static List<Integer> GetLowEight(byte[] dataByte) {
        List<Integer> lowList = new ArrayList<>();
       /*
       1、计算命令字+标识位+数据位
       计算方法：
              1、每个数组的第一位和最后三位不加计算
       */
        int sum = 0;
        for (int i = 1; i < dataByte.length - 3; i++) {
            sum = sum + dataByte[i];
        }
        String sumStr = Integer.toHexString(sum);
//        LogUtils.eTag("获取到的有效累加和：", sum + "+" + sumStr);
        //计算数据总长度
        int longLen = sumStr.length();
        //获取最后两位下标，这两位为计算得到的校验和位（2byte）
        int lowOne = longLen - 1;
        int lowTwo = longLen - 2;
        //根据后两位下标获取后两位的数据
        int lowOneChr = sumStr.charAt(lowOne);
        int lowTwoChr = sumStr.charAt(lowTwo);
//        LogUtils.eTag("获取到转换后的数据：", lowOneChr + "+" + lowTwoChr);
        //根据ASCII码判断数据
        /*
                1、返回的数据中的字母全部为大写字母
                2、本地转换拿到的数据中的字母全为小写字母
                3、所以需要根据ASCII码，把小写字母转换成大写字母,+32
        * */
        if (lowOneChr > 96 && lowOneChr < 123) {
//            LogUtils.eTag("lowOneChr", lowOneChr + "+lowOneChr-" + (lowOneChr - 32));
            lowOneChr = lowOneChr - 32;
        } else {
//            LogUtils.eTag("lowOneChr", lowOneChr + "");
            lowOneChr = lowOneChr;
        }
        if (lowTwoChr > 96 && lowTwoChr < 123) {
            //LogUtils.eTag("lowTwoChr", lowTwoChr + "+lowTwoChr-" + (lowTwoChr - 32));
            lowTwoChr = lowTwoChr - 32;
        } else {
            lowTwoChr = lowTwoChr;
//            LogUtils.eTag("lowTwoChr", lowTwoChr + "");
        }
        //把数据添加到List中返回
        lowList.add(lowOneChr);
        lowList.add(lowTwoChr);
        return lowList;
    }

    public static List<Byte> GetLowEight2(byte[] dataByte) {
        List<Byte> list = new ArrayList<>();
        int sum = 0;
        //i从1 开始 第一位不参与计算
        for (int i = 1; i < dataByte.length; i++) {
            int b = dataByte[i];
            if (b < 0) {
                b = b + 256;
            }
            sum = sum + b;
        }
        byte high = (byte) (sum & 0xff);
        byte low = (byte) ((sum & 0xff00) >> 8);
        list.add(low);
        list.add(high);

//        //新版
//        int lowInt=(sum & 0xff00) >> 8;
//        int qufan = ~lowInt+1;
//        byte high1 = (byte)(qufan & 0xff);
//        byte low1 = (byte)((qufan & 0xff00) >> 8);
//        list.add(high1);
//        list.add(low1);
        return list;
    }

    /**
     * 计算低8位 取反+1   得到高四位和第四位ascii
     */

    public static List<Integer> GetHeightLowFour(byte[] dataByte) {
        List<Integer> list = new ArrayList<>();
        int sum = 0;
        //i从1 开始 第一位不参与计算
        for (int i = 1; i < dataByte.length; i++) {
            int b = dataByte[i];
            if (b < 0) {
                b = b + 256;
            }
            sum = sum + b;
        }
//        byte high = (byte) (sum & 0xff);
//        byte low = (byte) ((sum & 0xff00) >> 8);
        String sumStr = Integer.toHexString(sum);
        int longLen = sumStr.length();
        int lowOne = longLen - 1;
        int lowTwo = longLen - 2;
        //根据后两位下标获取后两位的数据
        int lowOneChr = sumStr.charAt(lowOne);
        int lowTwoChr = sumStr.charAt(lowTwo);
        byte[] bytes = Byte2Hex.bigEndian(sum);//int 转字节数组
//
////        list.add(low);
////        list.add(high);
//        Log.i("TAG",bytes+"hhh");
        byte low = bytes[3];
        // int low = sum >> 8;
        //新版
        // int lowInt = (sum & 0xff00) >> 8;
        byte qufan = (byte) ((~low) + 1);
        int high2 = Byte2Hex.getHeight4(qufan);
        int low2 = Byte2Hex.getLow4(qufan);
        // byte[] bytes1 = Byte2Hex.bigEndian(qufan);
//        byte high1 = (byte) (qufan & 0xff);
//        byte low1 = (byte) ((qufan & 0xff00) >> 4);
//        list.add(high1);
//        list.add(low1);
//        byte high1 = bytes1[2];
//        byte low1 = bytes1[3];
        list.add(high2);
        list.add(low2);
        return list;

    }

    /**
     * 计算低8位 取反+1   得到高四位和第四位ascii
     */

    public static List<Character> GetHeightLowFour2(byte[] dataByte) {
        List<Character> list = new ArrayList<>();
        int sum = 0;
        //i从1 开始 第一位不参与计算
        for (int i = 1; i < dataByte.length; i++) {
            int b = dataByte[i];
            if (b < 0) {
                b = b + 256;
            }
            sum = sum + b;
        }
//        byte high = (byte) (sum & 0xff);
//        byte low = (byte) ((sum & 0xff00) >> 8);
        byte[] bytes = Byte2Hex.bigEndian(sum);
        byte low = bytes[3];
        // int low = sum >> 8;
        //新版
        // int lowInt = (sum & 0xff00) >> 8;
        byte qufan = (byte) ((~low) + 1);
        int high2 = Byte2Hex.getHeight4(qufan);
        int low2 = Byte2Hex.getLow4(qufan);
        if (high2 >= 10) {
            high2 = high2 + 7;   //查询ascii码表可知  9 和A 之间有7个数
        }
        if (low2 >= 10) {
            low2 = low2 + 7;
        }
        char high3 = (char) (high2 + 48);//int转char 需要加48
        char low3 = (char) (low2 + 48);
        // byte[] bytes1 = Byte2Hex.bigEndian(qufan);
        list.add(high3);
        list.add(low3);
        return list;

    }
}
