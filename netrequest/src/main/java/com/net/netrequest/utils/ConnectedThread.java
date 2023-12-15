package com.net.netrequest.utils;

import android.bluetooth.BluetoothSocket;

import com.net.netrequest.bean.MessageEventChuanCai;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * author： cyw
 */
public class ConnectedThread extends Thread {
    public static BluetoothSocket mSocket;
    private OutputStream mOutputStream;
    private InputStream mInputStream;

    public ConnectedThread(BluetoothSocket socket) {
        mSocket = socket;
        InputStream temIn = null;
        OutputStream temOut = null;
        try {
            temIn = socket.getInputStream();
            temOut = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mInputStream = temIn;
        mOutputStream = temOut;

    }

    @Override
    public void run() {
        int count = 0;
        //if (count > 0) {
        byte[] oneByte = new byte[1];
        byte[] buffer = new byte[1024];
        int len = 0;
        String content;
//       // while (true) {
//            try {
//                while ((len = mInputStream.read(oneByte, 0, 1)) != -1) {
////                        content = new String(buffer, 0, len);
////                        Message message = new Message();
////                        message.what = BluetoothParams.MSG_CLIENT_REV_NEW;
////                        message.obj = content;
////                        uiHandler.sendMessage(message);
////                        Log.e(TAG, "------------- client read data in while ,send msg ui" + content);
//                    buffer[count] = oneByte[0];
//                    count++;
//                    int[] ints = new int[1];
//                    byte b1 = oneByte[0];
//                    int aInt = Byte2Hex.byteToInteger(b1);
//                    ints[0] = aInt;
//                    Log.i("TAGRECVINT", ints[0] + "jjjhhh");
//                    WriteLog.writeTxtToFile("回复数据单字节" + ints[0]);
//                    if (ints[0] == 239) {
//                        // byte[] data = resetArray(buffer);
//                        Log.i("TAGRECVINT2", buffer.length + "hhhhg");
//                        MessageEventChuanCai messageEventChuanCai = new MessageEventChuanCai(buffer);
//                        EventBus.getDefault().post(messageEventChuanCai);
//                        //resetArray(buffer);
//                        count = 0;
//                        try {
//                            Thread.sleep(100);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                   // resetArray(oneByte);
//                }
//                // }
//            } catch (IOException e) {
//                e.printStackTrace();
//               // break;
//
////                try {
////                    if (mInputStream != null) {
////                        mInputStream.close();
////                    }
////                    if (mSocket != null) {
////                        mSocket.close();
////                        mSocket = null;
////                    }
////                } catch (IOException ioException) {
////                    ioException.printStackTrace();
////                }
//            }
//    //    }
        while (true) {
            try {
//                        while ((len = in.read(oneByte, 0, 1)) != -1) {
//                            buffer[count] = oneByte[0];
//                            count++;
//                            int[] ints = new int[1];
//                            byte b1 = oneByte[0];
//                            int aInt = Byte2Hex.byteToInteger(b1);
//                            ints[0] = aInt;
//                            Log.i("TAGRECVINT", ints[0] + "jjjhhh");
//                            // WriteLog.writeTxtToFile("回复数据单字节" + ints[0]);
////                    int bInt = Byte2Hex.byteToInteger(buffer[3]);
//                            if (ints[0] == 239) {//ints[0] == 239
//                                // byte[] data = resetArray(buffer);
//                                Log.i("TAGRECVINT2", buffer.length + "hhhhg");
//                                MessageEventChuanCai messageEventChuanCai = new MessageEventChuanCai(buffer);
//                                EventBus.getDefault().post(messageEventChuanCai);
//                                //resetArray(buffer);
//                                count = 0;
//                                try {
//                                    Thread.sleep(100);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
                mInputStream.read(buffer);
                MessageEventChuanCai messageEventChuanCai = new MessageEventChuanCai(buffer);
                EventBus.getDefault().post(messageEventChuanCai);


            } catch (IOException e) {
                e.printStackTrace();
//                    if (socket != null) {
//                        socket.close();
//                    }
                // break;
            } finally {

//                    try {
//                        if (socket != null) {
//                            socket.close();
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

            }
        }
    }

    /* Call this from the main activity to send data to the remote device */
    public void send(byte[] bytes) {
        try {
            mOutputStream.write(bytes);
        } catch (IOException e) {
        }
    }

    /* Call this from the main activity to shutdown the connection */
    public void cancel() {
        try {
            mOutputStream.close();
        } catch (IOException e) {
        }
    }
}
