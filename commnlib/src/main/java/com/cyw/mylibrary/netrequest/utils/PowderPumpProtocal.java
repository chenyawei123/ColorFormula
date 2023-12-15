package com.cyw.mylibrary.netrequest.utils;

import com.cyw.mylibrary.application.AppApplication;
import com.cyw.mylibrary.bean.FmParamsBean;
import com.cyw.mylibrary.util.Byte2Hex;
import com.cyw.mylibrary.util.DateFormatter;
import com.cyw.mylibrary.util.HightLowEight;
import com.cyw.mylibrary.util.SharedPreferencesHelper;
import com.cyw.mylibrary.util.WriteLog;

import java.util.List;

/**
 * author： cyw
 */
public class PowderPumpProtocal {
    private static int iDataPoint = 0;

    ///开始命令、
    public static void startProtocal() {
        AppApplication.getInstance().setSendCmd(1);
        byte[] bDataSend = new byte[3];
        iDataPoint = 0;
        bDataSend[iDataPoint] = (byte) 2; //起始符02
        iDataPoint = 1;
        bDataSend[iDataPoint] = (byte) 22;
        iDataPoint = 2;
        bDataSend[iDataPoint] = (byte) 3;
        byte[] dataSend = Byte2Hex.subBytes(bDataSend, 0, iDataPoint + 1);
        if (NetApplication.inetrequestInterface != null) {
            NetApplication.inetrequestInterface.sendMsg(dataSend);
        }
//        MessageEventChuanCai messageEventChuanCai = new MessageEventChuanCai(dataSend);//进程间通信onevent或者广播无效
//        EventBus.getDefault().post(messageEventChuanCai);
        String resultShi = Byte2Hex.bytes2HexString(dataSend);
        String time = DateFormatter.getCurrentTime();
        WriteLog.writeTxtToFile(time + "开始命令" + resultShi);
    }

    /**
     * 4、	子任务次序标记命令【TO】:
     * (02)TOAXX(03)
     * A：占1个字节，表示子任务次序（0:首子任务;1:中间子任务;2:末子任务;3:单子任务）；
     * 下位机：(02)(06)(03) //握手命令
     */
    public static void childTaskProtocal(int order) {
        AppApplication.getInstance().setSendCmd(2);
        int len = 7;
        byte[] bDataSend = new byte[len];
        iDataPoint = 0;
        bDataSend[iDataPoint] = (byte) 2; //起始符02
        iDataPoint = 1;
        int a1 = Byte2Hex.HexString2int("54");
        bDataSend[iDataPoint] = (byte) a1;//T
        iDataPoint = 2;
        int a2 = Byte2Hex.HexString2int("4f");
        bDataSend[iDataPoint] = (byte) a2;//o
        iDataPoint = 3;
        //int a3 = Byte2Hex.HexString2int("3");
        // String str= Byte2Hex.dec2Str("03");
        //char ord = (char) order;
        // String hex = Integer.toString(ord,16);
        // int a3 = Integer.parseInt(hex);
        String or = String.valueOf(order);
        //String hexAscii =Byte2Hex.stringToAscii(or);
        String hexAscii = Byte2Hex.str2Hex(or);
        int a3 = Byte2Hex.HexString2int(hexAscii);
        bDataSend[iDataPoint] = (byte) a3;


        ////////////////////////////校验和
        List<Character> integers = HightLowEight.GetHeightLowFour2(bDataSend);
        char i0 = integers.get(0);
        char i1 = integers.get(1);
        String hex1 = Byte2Hex.int2HexString(i0);
        String hex2 = Byte2Hex.int2HexString(i1);
//        String checkAscii1 = Byte2Hex.str2Hex(hex1);
//        String shiAscii = Byte2Hex.str2Dec(String.valueOf(i1));
//        String checkAscii2 = Byte2Hex.str2Hex(String.valueOf(i1));
//        int check1 = Byte2Hex.HexString2int(hex1);
//        int check2 = Byte2Hex.HexString2int(hex2);
        iDataPoint++;
        bDataSend[iDataPoint] = (byte) i0;
        iDataPoint++;
        bDataSend[iDataPoint] = (byte) i1;
        iDataPoint++;
        bDataSend[iDataPoint] = 3;

        byte[] dataSend = Byte2Hex.subBytes(bDataSend, 0, iDataPoint + 1);
        if (NetApplication.inetrequestInterface != null) {
            NetApplication.inetrequestInterface.sendMsg(dataSend);
        }
//        for(int i=0;i<10;i++){
//            MessageEventChuanCai messageEventChuanCai = new MessageEventChuanCai(dataSend);//进程间通信onevent或者广播无效
//            EventBus.getDefault().post(messageEventChuanCai);
//        }

        String resultShi = Byte2Hex.bytes2HexString(dataSend);
        String time = DateFormatter.getCurrentTime();
        WriteLog.writeTxtToFile(time + "子任务次序标记命令" + resultShi);
    }

    /**
     * 1、	罐装命令【FS】：
     * (02)FSAABBBBBBBCCCCCCCDDDDDDDDEEEEEEEFFFFGHXX(03)
     * AAA：占3个字节，表示桶位号；
     * BBBBBBB：占7个字节，表示中低速阀门阈值，单位为g；
     * CCCCCCC：占7个字节，表示提前关闭阀门阈值，单位为g；
     * DDDDDDDD：占8个字节，表示罐装目标量，单位为g；
     * EEEEEEE：占7个字节，表示高中速阀门阈值，单位为g；
     * FFFF：占4个字节，表示罐装精度阈值，单位为g；
     * G：占1个字节，表示罐装目标量和罐装精度精确小数位数，取值0~3；
     * H：占1个字节，表示各阀门控制阈值参数精确小数位数，取值0~3；
     * XX：占2个字节，表示校验位；
     * 下位机：(02)(06)(03) //握手命令
     * 上位机：(02)(09)(03) //结束命令
     * 下位机：(02)(06)(03) //握手命令
     * 下位机：(02)SFSEEAADDDDDDDDGGGGGGGGXX(03) //罐装成功完成命令【SFS：命令名；EE:异常码(在SFS命令中为常量00)；AA：桶位号；DDDDDDDD：实际罐装量，单位为g，小数点根据灌装目标量小数位数决定；GGGGGGGG：毛重，单位为g，小数点根据灌装目标量小数位数决定；XX：偶校验位(采用和调色机相同的校验方式)】
     */
    //一个桶
    //targetDos 罐装目标量 必须大于罐装精度阈值
    public static void cannedProtocal(String targetDos, String tong, FmParamsBean fmParamsBean,double dCannedPrecision) {
        AppApplication.getInstance().setSendCmd(3);
        boolean isHigh = SharedPreferencesHelper.getBoolean(AppApplication.getInstance(),"isHigh",true);
        int len = 44;
        byte[] bDataSend = new byte[len];
        iDataPoint = 0;
        bDataSend[iDataPoint] = (byte) 2; //起始符02
        iDataPoint = 1;
        int a1 = Byte2Hex.HexString2int("46");//F
        bDataSend[iDataPoint] = (byte) a1;//T
        iDataPoint = 2;
        int a2 = Byte2Hex.HexString2int("53");//s
        bDataSend[iDataPoint] = (byte) a2;//o
        //桶号
        long tongInt = Long.parseLong(tong);
        // char tongChar = (char) 3L;
        byte[] bytes2 = Byte2Hex.longToBytes(tongInt);
//        String hex = Long.toString(tongChar,16);
////        String hex = Integer.toString(tongChar,16);
////        int a3 = Byte2Hex.HexString2int(hex);
//        byte[] bytes = Byte2Hex.hexStringToBytes(hex);
        String hexAscii = Byte2Hex.str2Hex(tong);
        byte[] b = Byte2Hex.hexStringToBytes(hexAscii);
        int size = 3;
        int cha = size - b.length;
        int count = 0;
        for (int i = 0; i < size; i++) {
            iDataPoint++;
            if (i < cha) {
                bDataSend[iDataPoint] = 48;
            } else {
                bDataSend[iDataPoint] = b[count];
                count++;
            }
        }
//        int a3 = 0;
//        byte[] result2 = HightLowEightColds.int2ByteArray(a3);
//        iDataPoint = 3;
//        bDataSend[iDataPoint] = result2[2];
//        iDataPoint = 4;
//        bDataSend[iDataPoint] = result2[3];
        int beishu = 0;
        if(isHigh){
            beishu = 1000;
        }else{
            beishu = 10;
        }
        //低速阀门阈值
        double lowInt = Double.parseDouble(fmParamsBean.getLowThreshold());
        int scaleInt = (int) (beishu * lowInt);
        String strLow = String.valueOf(scaleInt);
        hexAscii = Byte2Hex.str2Hex(strLow);
        byte[] bLow = Byte2Hex.hexStringToBytes(hexAscii);
        size = 7;
        cha = size - bLow.length;
        count = 0;
        for (int i = 0; i < size; i++) {
            iDataPoint++;
            if (i < cha) {
                bDataSend[iDataPoint] = 48;
            } else {
                bDataSend[iDataPoint] = bLow[count];
                count++;
            }
        }


        ////////提前关闭阀门阈值
        double closeInt = Double.parseDouble(fmParamsBean.getCloseThreshold());
        scaleInt = (int) (closeInt * beishu);
        String strClose = String.valueOf(scaleInt);
        hexAscii = Byte2Hex.str2Hex(strClose);
        byte[] bClose = Byte2Hex.hexStringToBytes(hexAscii);
        size = 7;
        cha = size - bClose.length;
        count = 0;
        for (int i = 0; i < size; i++) {
            iDataPoint++;
            if (i < cha) {
                bDataSend[iDataPoint] = 48;
            } else {
                bDataSend[iDataPoint] = bClose[count];
                count++;
            }
        }
        ///////////////////////////罐装目标量
        double targetDosInt = Double.parseDouble(targetDos);
        scaleInt = (int) (targetDosInt * beishu);
        String strTargetDos = String.valueOf(scaleInt);

        hexAscii = Byte2Hex.str2Hex(strTargetDos);
        byte[] bTargetDos = Byte2Hex.hexStringToBytes(hexAscii);
        size = 8;
        cha = size - bTargetDos.length;
        count = 0;
        for (int i = 0; i < size; i++) {
            iDataPoint++;
            if (i < cha) {
                bDataSend[iDataPoint] = 48;
            } else {
                bDataSend[iDataPoint] = bTargetDos[count];
                count++;
            }
        }

        //高速阀门阈值
        double highInt = Double.parseDouble(fmParamsBean.getHighThreshold());
        scaleInt = (int) (highInt * beishu);
        String strHigh = String.valueOf(scaleInt);
        hexAscii = Byte2Hex.str2Hex(strHigh);
        byte[] bHigh = Byte2Hex.hexStringToBytes(hexAscii);
        size = 7;
        cha = size - bHigh.length;
        count = 0;
        for (int i = 0; i < size; i++) {
            iDataPoint++;
            if (i < cha) {
                bDataSend[iDataPoint] = 48;
            } else {
                bDataSend[iDataPoint] = bHigh[count];
                count++;
            }
        }


        //罐装精度阈值

        //double precisionInt = Double.parseDouble(fmParamsBean.getCannedaPrecision());

//        if (0.02 * targetDosInt > precisionInt) {
//            precisionInt = 0.02 * targetDosInt;
//        }
        scaleInt = (int) (dCannedPrecision * beishu);
        String strPrecision = String.valueOf(scaleInt);
        hexAscii = Byte2Hex.str2Hex(strPrecision);
        byte[] bPrecision = Byte2Hex.hexStringToBytes(hexAscii);
        size = 4;
        cha = size - bPrecision.length;
        count = 0;
        for (int i = 0; i < size; i++) {
            iDataPoint++;
            if (i < cha) {
                bDataSend[iDataPoint] = 48;
            } else {
                bDataSend[iDataPoint] = bPrecision[count];
                count++;
            }
        }

        int decimalPlaces = 0;
        if(isHigh){
            decimalPlaces = 3;
        }else{
            decimalPlaces = 1;
        }
        //罐装小数位数
        // char tongChar = (char) 3L;
        byte[] bytesCannedUnit = Byte2Hex.longToBytes(3);
        hexAscii = Byte2Hex.str2Hex(String.valueOf(decimalPlaces));
        byte[] bCannedUnit = Byte2Hex.hexStringToBytes(hexAscii);
        size = 1;
        cha = size - bCannedUnit.length;
        count = 0;
        for (int i = 0; i < size; i++) {
            iDataPoint++;
            if (i < cha) {
                bDataSend[iDataPoint] = 48;
            } else {
                bDataSend[iDataPoint] = bCannedUnit[count];
                count++;
            }
        }

        //阀门小数位数
        // char tongChar = (char) 3L;
        byte[] bytesFMUnit = Byte2Hex.longToBytes(3);
        hexAscii = Byte2Hex.str2Hex(String.valueOf(decimalPlaces));
        byte[] bFMUnit = Byte2Hex.hexStringToBytes(hexAscii);
        size = 1;
        cha = size - bFMUnit.length;
        count = 0;
        for (int i = 0; i < size; i++) {
            iDataPoint++;
            if (i < cha) {
                bDataSend[iDataPoint] = 48;
            } else {
                bDataSend[iDataPoint] = bFMUnit[count];
                count++;
            }
        }
        ////////////////////////////校验和
        List<Character> integers = HightLowEight.GetHeightLowFour2(bDataSend);
        char i0 = integers.get(0);
        char i1 = integers.get(1);
        String hex1 = Byte2Hex.int2HexString(i0);
        String hex2 = Byte2Hex.int2HexString(i1);
        iDataPoint++;
        bDataSend[iDataPoint] = (byte) i0;
        iDataPoint++;
        bDataSend[iDataPoint] = (byte) i1;

        iDataPoint++;
        bDataSend[iDataPoint] = 3;


        byte[] dataSend = Byte2Hex.subBytes(bDataSend, 0, iDataPoint + 1);
        if (NetApplication.inetrequestInterface != null) {
            NetApplication.inetrequestInterface.sendMsg(dataSend);
        }
        String resultShi = Byte2Hex.bytes2HexString(dataSend);
        String time = DateFormatter.getCurrentTime();
        WriteLog.writeTxtToFile(time + "罐装命令请求" + resultShi);


    }

    /**
     * 2、	泵速参数命令【SP】：
     * (02)SPAAABBCCDDEEFFGGGGXX(03)
     * AAA：占3个字节，表示桶位号；
     * BB：占2个字节，表示高速速度，取值范围0~16；
     * CC：占2个字节，表示中速速度，取值范围0~16；
     * DD：占2个字节，表示低速速度，取值范围0~16；
     * EE：占2个字节，表示点动速度，取值范围0~16；
     * FF：占2个字节，表示启动速度，取值范围0~16；
     * GGGG：占4个字节，表示启动步数，取值范围0~4095；
     */
    public static void pumpSpeedProtocal(long tongInt, long highSpeed, long medSpeed, long lowSpeed, long ddSpeed, long startSpeed, long startStep) {
        AppApplication.getInstance().setSendCmd(4);
        int len = 23;
        byte[] bDataSend = new byte[len];
        iDataPoint = 0;
        bDataSend[iDataPoint] = (byte) 2; //起始符02
        iDataPoint = 1;
        int a1 = Byte2Hex.HexString2int("53");//S
        bDataSend[iDataPoint] = (byte) a1;
        iDataPoint = 2;
        int a2 = Byte2Hex.HexString2int("50");//p
        bDataSend[iDataPoint] = (byte) a2;
        //桶号

        String hexAscii = Byte2Hex.str2Hex(String.valueOf(tongInt));
        byte[] bTong = Byte2Hex.hexStringToBytes(hexAscii);
        int size = 3;
        int cha = size - bTong.length;
        int count = 0;
        for (int i = 0; i < size; i++) {
            iDataPoint++;
            if (i < cha) {
                bDataSend[iDataPoint] = 48;
            } else {
                bDataSend[iDataPoint] = bTong[count];
                count++;
            }
        }
        //////高速速度
        hexAscii = Byte2Hex.str2Hex(String.valueOf(highSpeed));
        byte[] bHighSpeed = Byte2Hex.hexStringToBytes(hexAscii);
        size = 2;
        cha = size - bHighSpeed.length;
        count = 0;
        for (int i = 0; i < size; i++) {
            iDataPoint++;
            if (i < cha) {
                bDataSend[iDataPoint] = 48;
            } else {
                bDataSend[iDataPoint] = bHighSpeed[count];
                count++;
            }
        }
        ////////////////////////中速速度
        hexAscii = Byte2Hex.str2Hex(String.valueOf(medSpeed));
        byte[] bMid = Byte2Hex.hexStringToBytes(hexAscii);
        size = 2;
        cha = size - bMid.length;
        count = 0;
        for (int i = 0; i < size; i++) {
            iDataPoint++;
            if (i < cha) {
                bDataSend[iDataPoint] = 48;
            } else {
                bDataSend[iDataPoint] = bMid[count];
                count++;
            }
        }
        ///////////////////低速速度
        hexAscii = Byte2Hex.str2Hex(String.valueOf(lowSpeed));
        byte[] bLowSpeed = Byte2Hex.hexStringToBytes(hexAscii);
        size = 2;
        cha = size - bLowSpeed.length;
        count = 0;
        for (int i = 0; i < size; i++) {
            iDataPoint++;
            if (i < cha) {
                bDataSend[iDataPoint] = 48;
            } else {
                bDataSend[iDataPoint] = bLowSpeed[count];
                count++;
            }
        }

        //点动速度
        hexAscii = Byte2Hex.str2Hex(String.valueOf(ddSpeed));
        byte[] bDDSpeed = Byte2Hex.hexStringToBytes(hexAscii);
        size = 2;
        cha = size - bDDSpeed.length;
        count = 0;
        for (int i = 0; i < size; i++) {
            iDataPoint++;
            if (i < cha) {
                bDataSend[iDataPoint] = 48;
            } else {
                bDataSend[iDataPoint] = bDDSpeed[count];
                count++;
            }
        }
        ///启动速度
        hexAscii = Byte2Hex.str2Hex(String.valueOf(startSpeed));
        byte[] bStartspped = Byte2Hex.hexStringToBytes(hexAscii);
        size = 2;
        cha = size - bStartspped.length;
        count = 0;
        for (int i = 0; i < size; i++) {
            iDataPoint++;
            if (i < cha) {
                bDataSend[iDataPoint] = 48;
            } else {
                bDataSend[iDataPoint] = bStartspped[count];
                count++;
            }
        }
        //启动步数
        hexAscii = Byte2Hex.str2Hex(String.valueOf(startStep));
        byte[] bStartStep = Byte2Hex.hexStringToBytes(hexAscii);
        size = 4;
        cha = size - bStartStep.length;
        count = 0;
        for (int i = 0; i < size; i++) {
            iDataPoint++;
            if (i < cha) {
                bDataSend[iDataPoint] = 48;
            } else {
                bDataSend[iDataPoint] = bStartStep[count];
                count++;
            }
        }
        ////////////////////////////校验和
        List<Character> integers = HightLowEight.GetHeightLowFour2(bDataSend);
        char i0 = integers.get(0);
        char i1 = integers.get(1);
        iDataPoint++;
        bDataSend[iDataPoint] = (byte) i0;
        iDataPoint++;
        bDataSend[iDataPoint] = (byte) i1;
        iDataPoint++;
        bDataSend[iDataPoint] = 3;


        byte[] dataSend = Byte2Hex.subBytes(bDataSend, 0, iDataPoint + 1);
        if (NetApplication.inetrequestInterface != null) {
            NetApplication.inetrequestInterface.sendMsg(dataSend);
        }
        String resultShi = Byte2Hex.bytes2HexString(dataSend);
        String time = DateFormatter.getCurrentTime();
        WriteLog.writeTxtToFile(time + "泵速参数命令请求" + resultShi);


    }

    /**
     * 3、	点动参数命令【JP】：
     * (02)JPAAABBBBBCCCCCDDDDDEEEEEXX(03)
     * AAA：占3个字节，表示桶位号；
     * BBBBB：占5个字节，表示抖动目标阈值，单位为0.1mg；
     * CCCCC：占5个字节，表示抖动步数，单位为步；
     * DDDDD：占5个字节，表示抖动周期，单位为ms；
     * EEEEE: 占5个字节，表示抖动前注出步数，单位为步；
     * XX：占2个字节，表示校验位；
     * 下位机：(02)(06)(03) //握手命令
     */
    public static void jogParaProtocal(long tongInt, int DDthreshold, long ddStep, long ddCycle, long ddBeforeStep) {
        AppApplication.getInstance().setSendCmd(5);
        int len = 29;
        byte[] bDataSend = new byte[len];
        iDataPoint = 0;
        bDataSend[iDataPoint] = (byte) 2; //起始符02
        iDataPoint = 1;
        int a1 = Byte2Hex.HexString2int("4A");//S
        bDataSend[iDataPoint] = (byte) a1;
        iDataPoint = 2;
        int a2 = Byte2Hex.HexString2int("50");//p
        bDataSend[iDataPoint] = (byte) a2;
        //桶号
        String hexAscii = Byte2Hex.str2Hex(String.valueOf(tongInt));
        byte[] bTong = Byte2Hex.hexStringToBytes(hexAscii);
        int size = 3;
        int cha = size - bTong.length;
        int count = 0;
        for (int i = 0; i < size; i++) {
            iDataPoint++;
            if (i < cha) {
                bDataSend[iDataPoint] = 48;
            } else {
                bDataSend[iDataPoint] = bTong[count];
                count++;
            }
        }
        //抖动目标阈值 发送时不能带小数
        String strTarget = String.valueOf(DDthreshold);
        hexAscii = Byte2Hex.str2Hex(strTarget);
        byte[] bDDTh = Byte2Hex.hexStringToBytes(hexAscii);
        size = 5;
        cha = size - bDDTh.length;
        count = 0;
        for (int i = 0; i < size; i++) {
            iDataPoint++;
            if (i < cha) {
                bDataSend[iDataPoint] = 48;
            } else {
                bDataSend[iDataPoint] = bDDTh[count];
                count++;
            }
        }
        //抖动步数
        boolean isHigh = SharedPreferencesHelper.getBoolean(AppApplication.getInstance(),"isHigh",true);
        if(!isHigh){
            ddStep =0;
        }
        hexAscii = Byte2Hex.str2Hex(String.valueOf(ddStep));
        byte[] bDDStep = Byte2Hex.hexStringToBytes(hexAscii);
        size = 5;
        cha = size - bDDStep.length;
        count = 0;
        for (int i = 0; i < size; i++) {
            iDataPoint++;
            if (i < cha) {
                bDataSend[iDataPoint] = 48;
            } else {
                bDataSend[iDataPoint] = bDDStep[count];
                count++;
            }
        }
        //抖动周期
        hexAscii = Byte2Hex.str2Hex(String.valueOf(ddCycle));
        byte[] bDDCycle = Byte2Hex.hexStringToBytes(hexAscii);
        size = 5;
        cha = size - bDDCycle.length;
        count = 0;
        for (int i = 0; i < size; i++) {
            iDataPoint++;
            if (i < cha) {
                bDataSend[iDataPoint] = 48;
            } else {
                bDataSend[iDataPoint] = bDDCycle[count];
                count++;
            }
        }
        //抖动前注出步数
        hexAscii = Byte2Hex.str2Hex(String.valueOf(ddBeforeStep));
        byte[] bDDBefore = Byte2Hex.hexStringToBytes(hexAscii);
        size = 5;
        cha = size - bDDBefore.length;
        count = 0;
        for (int i = 0; i < size; i++) {
            iDataPoint++;
            if (i < cha) {
                bDataSend[iDataPoint] = 48;
            } else {
                bDataSend[iDataPoint] = bDDBefore[count];
                count++;
            }
        }
        ////////////////////////////校验和
        List<Character> integers = HightLowEight.GetHeightLowFour2(bDataSend);
        char i0 = integers.get(0);
        char i1 = integers.get(1);
        iDataPoint++;
        bDataSend[iDataPoint] = (byte) i0;
        iDataPoint++;
        bDataSend[iDataPoint] = (byte) i1;
        iDataPoint++;
        bDataSend[iDataPoint] = 3;


        byte[] dataSend = Byte2Hex.subBytes(bDataSend, 0, iDataPoint + 1);
        if (NetApplication.inetrequestInterface != null) {
            NetApplication.inetrequestInterface.sendMsg(dataSend);
        }
        String resultShi = Byte2Hex.bytes2HexString(dataSend);
        String time = DateFormatter.getCurrentTime();
        WriteLog.writeTxtToFile(time + "点动参数命令请求" + resultShi);
    }


    /**
     * 5、	阀门动作参数命令【NP】:
     * (02)NPAAABCCCDDDEEEFFFGGGHHKKJJJLLLMMMXX(03)
     * AAA：占3个字节，表示桶位号；
     * B：当前桶位分类，取值0时表示粉料桶位；
     * CCC：打开大嘴阀门转动步数，十进制字符串，范围000~999；（暂时不用）
     * DDD：关闭大嘴阀门转动步数，十进制字符串，范围000~999；（暂时不用）
     * EEE：打开小嘴阀门转动步数，十进制字符串，范围000~999；（暂时不用）
     * FFF：关闭小嘴阀门转动步数，十进制字符串，范围000~999；（暂时不用）
     * GGG：内联通阀门转动步数【用于扩展，可以忽略】，十进制字符串，范围000~999；（暂时不用）
     * HH：当前桶位配料过程中如果需要添加色浆寻桶桶位号，十进制字符串，范围01~99；
     * KK：判定色浆打空实际配料增长量连续小于最小增长量次数，十进制字符串，范围01~99；
     * JJJ：打开中嘴阀门转动步数，十进制字符串，范围000~999；（暂时不用）
     * LLL：关闭中嘴阀门转动步数，十进制字符串，范围000~999；（暂时不用）
     * MMM：泄压回吸步数，十进制字符串，范围000~999；（暂时不用）
     * XX：校验位；
     * 下位机：(02)(06)(03) //握手命令
     */
    public static void valVeActionProtocal(String strTong, int ddCount,FmParamsBean fmParamsBean) {
        AppApplication.getInstance().setSendCmd(6);
        int len = 38;
        byte[] bDataSend = new byte[len];
        iDataPoint = 0;
        bDataSend[iDataPoint] = (byte) 2; //起始符02
        iDataPoint = 1;
        int a1 = Byte2Hex.HexString2int("4E");//S
        bDataSend[iDataPoint] = (byte) a1;
        iDataPoint = 2;
        int a2 = Byte2Hex.HexString2int("50");//p
        bDataSend[iDataPoint] = (byte) a2;
        //桶号
        String hexAscii = Byte2Hex.str2Hex(strTong);
        byte[] bTong = Byte2Hex.hexStringToBytes(hexAscii);
        int size = 3;
        int cha = size - bTong.length;
        int count = 0;
        for (int i = 0; i < size; i++) {
            iDataPoint++;
            if (i < cha) {
                bDataSend[iDataPoint] = 48;
            } else {
                bDataSend[iDataPoint] = bTong[count];
                count++;
            }
        }
        ///桶位分类
        hexAscii = Byte2Hex.str2Hex("0");
        byte[] bFenlei = Byte2Hex.hexStringToBytes(hexAscii);
        size = 1;
        cha = size - bFenlei.length;
        count = 0;
        for (int i = 0; i < size; i++) {
            iDataPoint++;
            if (i < cha) {
                bDataSend[iDataPoint] = 48;
            } else {
                bDataSend[iDataPoint] = bFenlei[count];
                count++;
            }
        }
        //打开大嘴阀门转动步数
        String openBig = fmParamsBean.getOpenBig();
        hexAscii = Byte2Hex.str2Hex(openBig);
        byte[] bOpenBigStep = Byte2Hex.hexStringToBytes(hexAscii);
        size = 3;
        cha = size - bOpenBigStep.length;
        count = 0;
        for (int i = 0; i < size; i++) {
            iDataPoint++;
            if (i < cha) {
                bDataSend[iDataPoint] = 48;
            } else {
                bDataSend[iDataPoint] = bOpenBigStep[count];
                count++;
            }
        }
        ///关闭大嘴阀门转动步数
        String closeBig = fmParamsBean.getCloseBig();
        hexAscii = Byte2Hex.str2Hex(closeBig);
        byte[] bCloseBigStep = Byte2Hex.hexStringToBytes(hexAscii);
        size = 3;
        cha = size - bCloseBigStep.length;
        count = 0;
        for (int i = 0; i < size; i++) {
            iDataPoint++;
            if (i < cha) {
                bDataSend[iDataPoint] = 48;
            } else {
                bDataSend[iDataPoint] = bCloseBigStep[count];
                count++;
            }
        }
        //打开小嘴阀门转动步数
        String openSmall = fmParamsBean.getOpenSmall();
        hexAscii = Byte2Hex.str2Hex(openSmall);
        byte[] bOpenSmallStep = Byte2Hex.hexStringToBytes(hexAscii);
        size = 3;
        cha = size - bOpenSmallStep.length;
        count = 0;
        for (int i = 0; i < size; i++) {
            iDataPoint++;
            if (i < cha) {
                bDataSend[iDataPoint] = 48;
            } else {
                bDataSend[iDataPoint] = bOpenSmallStep[count];
                count++;
            }
        }
        //关闭小嘴阀门转动步数
        String closeSmall = fmParamsBean.getCloseSmall();
        hexAscii = Byte2Hex.str2Hex(closeSmall);
        byte[] bCloseSmallStep = Byte2Hex.hexStringToBytes(hexAscii);
        size = 3;
        cha = size - bCloseSmallStep.length;
        count = 0;
        for (int i = 0; i < size; i++) {
            iDataPoint++;
            if (i < cha) {
                bDataSend[iDataPoint] = 48;
            } else {
                bDataSend[iDataPoint] = bCloseSmallStep[count];
                count++;
            }
        }
        //内联通阀门转动步数
        hexAscii = Byte2Hex.str2Hex("0");
        byte[] bInner = Byte2Hex.hexStringToBytes(hexAscii);
        size = 3;
        cha = size - bInner.length;
        count = 0;
        for (int i = 0; i < size; i++) {
            iDataPoint++;
            if (i < cha) {
                bDataSend[iDataPoint] = 48;
            } else {
                bDataSend[iDataPoint] = bInner[count];
                count++;
            }
        }
        //寻桶桶位号
        hexAscii = Byte2Hex.str2Hex("1");
        byte[] bFindTong = Byte2Hex.hexStringToBytes(hexAscii);
        size = 2;
        cha = size - bFindTong.length;
        count = 0;
        for (int i = 0; i < size; i++) {
            iDataPoint++;
            if (i < cha) {
                bDataSend[iDataPoint] = 48;
            } else {
                bDataSend[iDataPoint] = bFindTong[count];
                count++;
            }
        }
        // 判定色浆打空实际配料增长量连续小于最小增长量次数
        boolean isHigh = SharedPreferencesHelper.getBoolean(AppApplication.getInstance(),"isHigh",true);
        if(!isHigh){
            ddCount = 0;
        }
        hexAscii = Byte2Hex.str2Hex(String.valueOf(ddCount));
        byte[] bCompareCount = Byte2Hex.hexStringToBytes(hexAscii);
        size = 2;
        cha = size - bCompareCount.length;
        count = 0;
        for (int i = 0; i < size; i++) {
            iDataPoint++;
            if (i < cha) {
                bDataSend[iDataPoint] = 48;
            } else {
                bDataSend[iDataPoint] = bCompareCount[count];
                count++;
            }
        }
        //打开中嘴阀门转动
        hexAscii = Byte2Hex.str2Hex("0");
        byte[] bOpenMidStep = Byte2Hex.hexStringToBytes(hexAscii);
        size = 3;
        cha = size - bOpenMidStep.length;
        count = 0;
        for (int i = 0; i < size; i++) {
            iDataPoint++;
            if (i < cha) {
                bDataSend[iDataPoint] = 48;
            } else {
                bDataSend[iDataPoint] = bOpenMidStep[count];
                count++;
            }
        }
        //关闭中嘴
        hexAscii = Byte2Hex.str2Hex("0");
        byte[] bCloseMidStep = Byte2Hex.hexStringToBytes(hexAscii);
        size = 3;
        cha = size - bCloseMidStep.length;
        count = 0;
        for (int i = 0; i < size; i++) {
            iDataPoint++;
            if (i < cha) {
                bDataSend[iDataPoint] = 48;
            } else {
                bDataSend[iDataPoint] = bCloseMidStep[count];
                count++;
            }
        }
        //泄压回吸步数
        hexAscii = Byte2Hex.str2Hex("0");
        byte[] bReleaseStep = Byte2Hex.hexStringToBytes(hexAscii);
        size = 3;
        cha = size - bReleaseStep.length;
        count = 0;
        for (int i = 0; i < size; i++) {
            iDataPoint++;
            if (i < cha) {
                bDataSend[iDataPoint] = 48;
            } else {
                bDataSend[iDataPoint] = bReleaseStep[count];
                count++;
            }
        }
        ////////////////////////////校验和
        List<Character> integers = HightLowEight.GetHeightLowFour2(bDataSend);
        char i0 = integers.get(0);
        char i1 = integers.get(1);
        iDataPoint++;
        bDataSend[iDataPoint] = (byte) i0;
        iDataPoint++;
        bDataSend[iDataPoint] = (byte) i1;
        iDataPoint++;
        bDataSend[iDataPoint] = 3;


        byte[] dataSend = Byte2Hex.subBytes(bDataSend, 0, iDataPoint + 1);
        if (NetApplication.inetrequestInterface != null) {
            NetApplication.inetrequestInterface.sendMsg(dataSend);
        }
        String resultShi = Byte2Hex.bytes2HexString(dataSend);
        String time = DateFormatter.getCurrentTime();
        WriteLog.writeTxtToFile(time + "阀门动作参数命令请求" + resultShi);

    }

    ///结束命令、
    public static void endProtocal() {
        AppApplication.getInstance().setSendCmd(7);
        byte[] bDataSend = new byte[3];
        iDataPoint = 0;
        bDataSend[iDataPoint] = (byte) 2; //起始符02
        iDataPoint = 1;
        bDataSend[iDataPoint] = (byte) 9;
        iDataPoint = 2;
        bDataSend[iDataPoint] = (byte) 3;
        byte[] dataSend = Byte2Hex.subBytes(bDataSend, 0, iDataPoint + 1);
        if (NetApplication.inetrequestInterface != null) {
            NetApplication.inetrequestInterface.sendMsg(dataSend);
        }
        String resultShi = Byte2Hex.bytes2HexString(dataSend);
        String time = DateFormatter.getCurrentTime();
        WriteLog.writeTxtToFile(time + "结束命令" + resultShi);
    }


    /**
     * 6、	上位机终止罐装命令【CC】：
     * 上位机：(02)CCXX(03) //终止罐装命令
     * 下位机：(02)EFSEEAADDDDDDDDGGGGGGGGXX(03) //罐装失败结束命令【EFS：命令名；EE:异常码(此时为常量02)；
     * AA：桶位号(如果桶位号不确定为00)；DDDDDDDD：实际罐装量，单位为g，小数点根据灌装目标量小数位数决定；
     * GGGGGGGG：毛重，单位为g，小数点根据灌装目标量小数位数决定；XX：偶校验位(采用和调色机相同的校验方式)】
     */
    public static void stopCannedProtocal() {
        AppApplication.getInstance().setSendCmd(8);
        int len = 6;
        byte[] bDataSend = new byte[len];
        iDataPoint = 0;
        bDataSend[iDataPoint] = (byte) 2; //起始符02
        iDataPoint = 1;
        int a1 = Byte2Hex.HexString2int("43");//C
        bDataSend[iDataPoint] = (byte) a1;
        iDataPoint = 2;
        int a2 = Byte2Hex.HexString2int("43");//C
        bDataSend[iDataPoint] = (byte) a2;

        ////////////////////////////校验和
        List<Character> integers = HightLowEight.GetHeightLowFour2(bDataSend);
        char i0 = integers.get(0);
        char i1 = integers.get(1);
        iDataPoint++;
        bDataSend[iDataPoint] = (byte) i0;
        iDataPoint++;
        bDataSend[iDataPoint] = (byte) i1;
        iDataPoint++;
        bDataSend[iDataPoint] = 3;


        byte[] dataSend = Byte2Hex.subBytes(bDataSend, 0, iDataPoint + 1);
        if (NetApplication.inetrequestInterface != null) {
            NetApplication.inetrequestInterface.sendMsg(dataSend);
        }
        String resultShi = Byte2Hex.bytes2HexString(dataSend);
        String time = DateFormatter.getCurrentTime();
        WriteLog.writeTxtToFile(time + "上位机终止罐装命令请求" + resultShi);

    }


    /**
     * 7、	寻桶装命令【GT】：
     * 上位机：02)GTAAAXX(03)//寻桶命令
     * 下位机：(02)SGTAAAXX(03) //寻桶完成命令【SGT：命令名；AA：桶位号(如果桶位号不确定为00)； XX：偶校验位(采用和调色机相同的校验方式)】
     * 下位机：(02)EGTAAABBXX(03) //寻桶失败命令【EGT：命令名；AA：桶位号(如果桶位号不确定为00)； XX：偶校验位(采用和调色机相同的校验方式)】
     * 或者(02)EFSAABBXX(03)
     */
    public static void barrelHuntingProtocal(String strTong) {
        AppApplication.getInstance().setSendCmd(9);
        int len = 9;
        byte[] bDataSend = new byte[len];
        iDataPoint = 0;
        bDataSend[iDataPoint] = (byte) 2; //起始符02
        iDataPoint = 1;
        int a1 = Byte2Hex.HexString2int("47");//G
        bDataSend[iDataPoint] = (byte) a1;
        iDataPoint = 2;
        int a2 = Byte2Hex.HexString2int("54");//T
        bDataSend[iDataPoint] = (byte) a2;
        //桶号
        String hexAscii = Byte2Hex.str2Hex(strTong);
        byte[] bTong = Byte2Hex.hexStringToBytes(hexAscii);
        int size = 3;
        int cha = size - bTong.length;
        int count = 0;
        for (int i = 0; i < size; i++) {
            iDataPoint++;
            if (i < cha) {
                bDataSend[iDataPoint] = 48;
            } else {
                bDataSend[iDataPoint] = bTong[count];
                count++;
            }
        }

        ////////////////////////////校验和
        List<Character> integers = HightLowEight.GetHeightLowFour2(bDataSend);
        char i0 = integers.get(0);
        char i1 = integers.get(1);
        iDataPoint++;
        bDataSend[iDataPoint] = (byte) i0;
        iDataPoint++;
        bDataSend[iDataPoint] = (byte) i1;
        iDataPoint++;
        bDataSend[iDataPoint] = 3;


        byte[] dataSend = Byte2Hex.subBytes(bDataSend, 0, iDataPoint + 1);
        if (NetApplication.inetrequestInterface != null) {
            NetApplication.inetrequestInterface.sendMsg(dataSend);
        }
        String resultShi = Byte2Hex.bytes2HexString(dataSend);
        String time = DateFormatter.getCurrentTime();
        WriteLog.writeTxtToFile(time + "寻桶装命令请求" + resultShi);

    }

    ///继续进行调色
    public static void continueWork() {
        AppApplication.getInstance().setSendCmd(10);
        byte[] bDataSend = new byte[6];
        iDataPoint = 0;
        bDataSend[iDataPoint] = (byte) 2; //起始符02
        iDataPoint = 1;
        int a1 = Byte2Hex.HexString2int("4A");//J
        bDataSend[iDataPoint] = (byte) a1;
        iDataPoint = 2;
        int a2 = Byte2Hex.HexString2int("53");//S
        bDataSend[iDataPoint] = (byte) a2;

        ////////////////////////////校验和
        List<Character> integers = HightLowEight.GetHeightLowFour2(bDataSend);
        char i0 = integers.get(0);
        char i1 = integers.get(1);
        iDataPoint++;
        bDataSend[iDataPoint] = (byte) i0;
        iDataPoint++;
        bDataSend[iDataPoint] = (byte) i1;
        iDataPoint = 5;
        bDataSend[iDataPoint] = (byte) 3;
        byte[] dataSend = Byte2Hex.subBytes(bDataSend, 0, iDataPoint + 1);
        if (NetApplication.inetrequestInterface != null) {
            NetApplication.inetrequestInterface.sendMsg(dataSend);
        }
        String resultShi = Byte2Hex.bytes2HexString(dataSend);
        String time = DateFormatter.getCurrentTime();
        WriteLog.writeTxtToFile(time + "继续打料命令" + resultShi);
    }

}
