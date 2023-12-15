package com.net.netrequest.utils;

import android.app.Application;
import android.content.Context;

import com.net.netrequest.service.BackService;

/**
 * author： cyw
 *
 */
public class NetApplication extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
    public static Context getAppContext(){
        return context;
    }
    //服务器
    //public static SocketClient mSocketClient = new SocketClient("111.231.71.45", 12345);
    public static SocketClient mSocketClient;
    //穿菜线
    public static SocketClient mSocketClientFood = new SocketClient("172.16.100.2", 23);
    public static SocketClient mSocketClientCold = new SocketClient("10.10.12.158",1001);
    public static ClientThread clientThread = null;
    public static ConnectedThread connectedThread = null;
    public static BackService inetrequestInterface = null;

}
