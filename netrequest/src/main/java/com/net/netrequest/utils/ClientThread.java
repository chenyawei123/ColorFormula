package com.net.netrequest.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import com.net.netrequest.bean.MessageEventChuanCai;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.UUID;

/**
 * chenyawei
 */

public class ClientThread implements Runnable {
    final String TAG = "ClientThread";

    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice device;

    Handler uiHandler;
    Handler writeHandler;

    public static BluetoothSocket socket;
    //    OutputStream out;
//    InputStream in;
    BufferedReader reader;

    ConnectBack mConnectBack;
    private Thread readerThread;
    private ConnectedThread connectThread;


    public void SetConnectBack(ConnectBack ConnectBack) {
        this.mConnectBack = ConnectBack;
    }

    public ClientThread(BluetoothAdapter bluetoothAdapter, BluetoothDevice device,
                        Handler handler) {
        this.bluetoothAdapter = bluetoothAdapter;
        this.device = device;
        this.uiHandler = handler;
        BluetoothSocket tmp = null;
        if (socket != null) {
            Log.e(TAG, "ConnectThread-->mmSocket != null先去释放");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            tmp = device.createRfcommSocketToServiceRecord(UUID.fromString(BluetoothParams.UUID));
        } catch (IOException e) {
            e.printStackTrace();
        }
        socket = tmp;


//        InputStream temIn = null;
//        OutputStream temOut = null;
//        try {
//            temIn = socket.getInputStream();
//            temOut = socket.getOutputStream();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        in = temIn;
//        out = temOut;
    }

    public BluetoothSocket getSocket() {
        return socket;
    }

    @Override
    public void run() {
        Log.e(TAG, "----------------- do client thread run()");
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        if (socket == null) {
            Log.e(TAG, "ConnectThread:run-->mmSocket == null");
            return;
        }

        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception

            if (!socket.isConnected()) {
                socket.connect();
            }

        } catch (IOException connectException) {
            // Unable to connect; close the socket and get out
            String connect = connectException.toString();
//                try {
//                    Method m = device.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
//                    try {
//                        socket = (BluetoothSocket) m.invoke(device, 1);
//                        socket.connect();
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    } catch (InvocationTargetException e) {
//                        e.printStackTrace();
//                    }
//                } catch (NoSuchMethodException e) {
//                    e.printStackTrace();
//                }

            mConnectBack.connectfaile(device);
            cancel();

        }
        if(socket!=null){
            mConnectBack.connectsuccess(socket,device);
        }

//            if (socket != null) {
//                Log.i("TAGCONREADER", "hhhhhh");
//                ConcectReader();
//            }

    }

    /**
     * 释放
     *
     * @return true 断开成功  false 断开失败
     */
    public boolean cancel() {
        try {
            if (socket != null) {
                socket.close();   //关闭socket
            }
//            if(connectThread != null){
//                connectThread.cancel();
//            }
//
//            connectThread = null;
            socket = null;

            Log.w(TAG, "ConnectedThread:cancel-->成功断开连接");
            return true;

        } catch (IOException e) {
            // 任何一部分报错，都将强制关闭socket连接
            socket = null;

            Log.e(TAG, "ConnectedThread:cancel-->断开连接异常！" + e.getMessage());
            return false;
        }
    }

    //    private void ConcectReader() throws IOException {
////        InputStream temIn = null;
////        OutputStream temOut = null;
////        try {
////            temIn = socket.getInputStream();
////            temOut = socket.getOutputStream();
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////        in = temIn;
////        out = temOut;
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Log.e(TAG, "-----------do client read run()");
//                int count = 0;
//                //if (count > 0) {
//                byte[] oneByte = new byte[1];
//                byte[] buffer = new byte[1024];
//                int len = 0;
//                String content;
//                //  while (true) {
//                try {
//                    while ((len = in.read(oneByte, 0, 1)) != -1) {
////                        content = new String(buffer, 0, len);
////                        Message message = new Message();
////                        message.what = BluetoothParams.MSG_CLIENT_REV_NEW;
////                        message.obj = content;
////                        uiHandler.sendMessage(message);
////                        Log.e(TAG, "------------- client read data in while ,send msg ui" + content);
//                        buffer[count] = oneByte[0];
//                        count++;
//                        int[] ints = new int[1];
//                        byte b1 = oneByte[0];
//                        int aInt = Byte2Hex.byteToInteger(b1);
//                        ints[0] = aInt;
//                        Log.i("TAGRECVINT", ints[0] + "jjjhhh");
//                        // WriteLog.writeTxtToFile("回复数据单字节" + ints[0]);
////                    int bInt = Byte2Hex.byteToInteger(buffer[3]);
//                        if (ints[0] == 239) {//ints[0] == 239
//                            // byte[] data = resetArray(buffer);
//                            Log.i("TAGRECVINT2", buffer.length + "hhhhg");
//                            MessageEventChuanCai messageEventChuanCai = new MessageEventChuanCai(buffer);
//                            EventBus.getDefault().post(messageEventChuanCai);
//                            //resetArray(buffer);
//                            count = 0;
//                            try {
//                                Thread.sleep(100);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                    // }
//                } catch (IOException e) {
//                    e.printStackTrace();
////                    if (socket != null) {
////                        socket.close();
////                    }
//                    // break;
//                } finally {
//
////                    try {
////                        if (socket != null) {
////                            socket.close();
////                        }
////                    } catch (IOException e) {
////                        e.printStackTrace();
////                    }
//
//                }
//                // }
//            }
//        }).start();
//    }
    private void ConcectReader() throws IOException {
//        InputStream temIn = null;
//        OutputStream temOut = null;
//        try {
//            temIn = socket.getInputStream();
//            temOut = socket.getOutputStream();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        in = temIn;
//        out = temOut;

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Log.e(TAG, "-----------do client read run()");
//                int count = 0;
//                //if (count > 0) {
//                byte[] oneByte = new byte[1];
//                byte[] buffer = new byte[1024];
//                int len = 0;
//                String content;
//                while (true) {
//                    try {
////                        while ((len = in.read(oneByte, 0, 1)) != -1) {
////                            buffer[count] = oneByte[0];
////                            count++;
////                            int[] ints = new int[1];
////                            byte b1 = oneByte[0];
////                            int aInt = Byte2Hex.byteToInteger(b1);
////                            ints[0] = aInt;
////                            Log.i("TAGRECVINT", ints[0] + "jjjhhh");
////                            // WriteLog.writeTxtToFile("回复数据单字节" + ints[0]);
//////                    int bInt = Byte2Hex.byteToInteger(buffer[3]);
////                            if (ints[0] == 239) {//ints[0] == 239
////                                // byte[] data = resetArray(buffer);
////                                Log.i("TAGRECVINT2", buffer.length + "hhhhg");
////                                MessageEventChuanCai messageEventChuanCai = new MessageEventChuanCai(buffer);
////                                EventBus.getDefault().post(messageEventChuanCai);
////                                //resetArray(buffer);
////                                count = 0;
////                                try {
////                                    Thread.sleep(100);
////                                } catch (InterruptedException e) {
////                                    e.printStackTrace();
////                                }
////                            }
////                        }
//                        in.read(buffer);
//                        MessageEventChuanCai messageEventChuanCai = new MessageEventChuanCai(buffer);
//                        EventBus.getDefault().post(messageEventChuanCai);
//
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
////                    if (socket != null) {
////                        socket.close();
////                    }
//                        // break;
//                    } finally {
//
////                    try {
////                        if (socket != null) {
////                            socket.close();
////                        }
////                    } catch (IOException e) {
////                        e.printStackTrace();
////                    }
//
//                    }
//                }
//            }
//        }).start();
    }


    private void proecessuffer(byte[] buff, int size) {
        int length = 0;
        for (int i = 0; i < size; i++) {
            if (buff[i] > '\0') {
                length++;
            } else {
                break;
            }
        }
        byte[] newBuff = new byte[length];
        for (int j = 0; j < length; j++) {
            newBuff[j] = buff[j];
        }
        MessageEventChuanCai messageEventChuanCai = new MessageEventChuanCai(newBuff);
        EventBus.getDefault().post(messageEventChuanCai);
    }


//    public void send(String data) {
////        data = data+"\r\n";
//
//        try {
//            //out = socket.getOutputStream();
//            out.write(data.getBytes(StandardCharsets.UTF_8));
//            Log.e(TAG, "---------- write data ok " + data);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void send(byte[] data) {
//        try {
//            //out = socket.getOutputStream();
//            out.write(data);
//            Log.e(TAG, "---------- write data ok " + data);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public interface ConnectBack {

        void connectsuccess(BluetoothSocket socket,BluetoothDevice device);

        void connectfaile(BluetoothDevice device);

        void connecting(BluetoothDevice device);
    }

}
