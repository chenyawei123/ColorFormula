package com.cyw.mylibrary.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cyw on 2019/12/16.
 */

public class HightLowEightColds {

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
//       // LogUtils.eTag("需要对比的数据", lowOne+"+"+lowOneGet+"+"+lowTwo+"+"+lowTwoGet);
//        isTrue = lowOne == lowOneGet && lowTwo == lowTwoGet;
//        if (isTrue==true){
//            new Thread(new Runnable() {
//                public void run() {
//                    try {
//                        Thread.sleep(400);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    byte[] rsp = new byte[2];
//                    rsp[0] = 6;
//                    rsp[1] = 49;
//                    NetApplication.mSocketClientFood.send(rsp);
////                    LogUtils.eTag("接收到传送线校验和回复接受（接收）",
////                            "回复接受");
//                }
//            }).start();
//        }else {
//            new Thread(new Runnable() {
//                public void run() {
//                    try {
//                        Thread.sleep(400);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    byte[] rsp = new byte[2];
//                    rsp[0] = 21;
//                    rsp[1] = 49;
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

    public static List<Byte> GetLowEight(byte[] dataByte) {
        List<Integer> lowList = new ArrayList<>();
        List<Byte> list = new ArrayList<>();
       /*
       1、计算命令字+标识位+数据位
       计算方法：
              1、每个数组的第一位和最后三位不加计算
       */
        int sum = 0;
        for (int i = 0; i < dataByte.length - 3; i++) {
            int b = dataByte[i];
            if(b<0){
                b = b+256;
            }
            sum = sum + b;
        }
        sum+=239;
        String sumStr = Integer.toHexString(sum);
        //LogUtils.eTag("获取到的有效累加和：", sum + "+" + sumStr);
        //计算数据总长度
        int longLen = sumStr.length();
        //获取最后两位下标，这两位为计算得到的校验和位（2byte）
        int lowOne = longLen - 2;
        int lowTwo = longLen - 3;
        byte[] b = Byte2Hex.toByteArray(sumStr);
        String str = "";
        String sum1="";
        String sum2 = "";
        if(sumStr.length() ==2 || sumStr.length() == 3){
            sum1 = sumStr.substring(0,1);
            sum2 = sumStr.substring(1,3);
        }else if(sumStr.length() == 4){
            sum1 = sumStr.substring(0,2);
            sum2 = sumStr.substring(2,4);
        }

        int lowOnes = Integer.parseInt(sum1,16);
        int lowTwos = Integer.parseInt(sum2,16);
//        int lowOnes = dataByte[lowOne];
//        int lowTwos = dataByte[lowTwo];
        //根据后两位下标获取后两位的数据
//        int lowOneChr = sumStr.charAt(lowOne);
//        int lowTwoChr = sumStr.charAt(lowTwo);
       // LogUtils.eTag("获取到转换后的数据：", lowOneChr + "+" + lowTwoChr);
//        //根据ASCII码判断数据
//        /*
//                1、返回的数据中的字母全部为大写字母
//                2、本地转换拿到的数据中的字母全为小写字母
//                3、所以需要根据ASCII码，把小写字母转换成大写字母,+32
//        * */
//        if (lowOneChr > 96 && lowOneChr < 123) {
//            LogUtils.eTag("lowOneChr", lowOneChr + "+lowOneChr-" + (lowOneChr - 32));
//            lowOneChr = lowOneChr - 32;
//        } else {
//            LogUtils.eTag("lowOneChr", lowOneChr + "");
//            lowOneChr = lowOneChr;
//        }
//        if (lowTwoChr > 96 && lowTwoChr < 123) {
//            LogUtils.eTag("lowTwoChr", lowTwoChr + "+lowTwoChr-" + (lowTwoChr - 32));
//            lowTwoChr = lowTwoChr - 32;
//        } else {
//            lowTwoChr = lowTwoChr;
//            LogUtils.eTag("lowTwoChr", lowTwoChr + "");
//        }
        //把数据添加到List中返回
        lowList.add(lowOnes);
        lowList.add(lowTwos);
        byte high = (byte)(sum&0xff);
        byte low = (byte)((sum& 0xff00)>>8);
        list.add(low);
        list.add(high);
        return list;
    }
    public static byte[] int2ByteArray(int i) {
        byte[] result = new byte[4];
        result[0] = (byte) ((i >> 24) & 0xFF);
        result[1] = (byte) ((i >> 16) & 0xFF);
        result[2] = (byte) ((i >> 8) & 0xFF);
        result[3] = (byte) (i & 0xFF);
        return result;
    }
}
