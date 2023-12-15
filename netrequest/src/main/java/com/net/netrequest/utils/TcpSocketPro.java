package com.net.netrequest.utils;

import java.util.List;

import santint.ColdDishDuty.ColdDishDuty;

/**
 * Created by cyw on 2018/10/26.
 */
//pro请求的协议
public class TcpSocketPro {
    public static native String RecvDatas(String data, int dataLength);

    public static native int bigCMD(String data, int dataLength);

    public static native int littleCMD(String data, int dataLength);
    //登录 请求
    public native String loginReq(String user, String pwd);
    //更新
    public native String  versionUpDataReq(String version);
    //炒菜软件登录
    public native String  clientLoginReq(String username, String password,int usertype);
    //请求所有岗位信息 请求
    public native String  requestPostInfoReq();
    //设备注册请求
    public native String  regEquimentReq(String postname,String postnamecode,int usertype,
                                         List<ColdDishDuty.RegEquiment_Req.CodeInfo> codeinfolist);
    public native String  diaoDuRsp(String all_code,List<ColdDishDuty.dishInfo> dishinfolist);
    //传菜回传已送传菜线请求
    public native String  passDishesReq(String all_code,String orderid,String caiid,int cai_jd_index);
    //凉菜做完请求
    public native String coldDishProcessReq(String all_code,String orderid,String caiid,int cai_jd_index,int cai_process);
    //请求所有岗位心跳信息 请求
    public native String  heartbeatReq();
    //凉菜取消应答
    public native String  coldDishCancelRsp(String all_code,List<ColdDishDuty.dishInfo> dishinfolist,String canceldishkey);
}

