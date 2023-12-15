package com.santint.colorformula.utils;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.net.netrequest.bean.MessageEventChuanCai;
import com.net.netrequest.utils.WriteLog;
import com.santint.colorformula.CorrectionActivity;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * author： cyw
 */
public class ReadThread extends Thread {
    private WeakReference<BluetoothSocket> mWeakSocket;
    private boolean isStart = true;
    private boolean serviceClose = false;
//        public static BluetoothSocket mSocket;
//        private OutputStream mOutputStream;
//        private InputStream mInputStream;

    public ReadThread(BluetoothSocket socket) {
//            mSocket = socket;
//            InputStream temIn = null;
//            OutputStream temOut = null;
//            try {
//                temIn = socket.getInputStream();
//                temOut = socket.getOutputStream();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            mInputStream = temIn;
//            mOutputStream = temOut;
        //init();
        mWeakSocket = new WeakReference<BluetoothSocket>(socket);

    }
    Handler handler;
    private void initHandler(){
        handler = ReadFormulaThread.getInstance(CorrectionActivity.instance).getHandler();

    }

    public void release() {
        isStart = false;
        WriteLog.writeTxtToFile("releaseLastSocket2-----------------");
        releaseLastSocket(mWeakSocket);
    }
    private void releaseLastSocket(WeakReference<BluetoothSocket> weakSocket) {
        try {
            if (null != weakSocket) {
                BluetoothSocket sk = weakSocket.get();
                if (null != sk) {
//                    if (sk.isConnected()) {//!sk.isClosed()
//                        sk.close();
//                    }
                    sk.close();
                    sk = null;
                }

                weakSocket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        int count = 0;
        //if (count > 0) {
        BluetoothSocket socket = mWeakSocket.get();
        if (null != socket) {
            byte[] oneByte = new byte[1];
            byte[] buffer = new byte[1024];
            int ch;
            int bytes; // 从read()返回bytes
            // while (true) {
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
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }


                int length = 0;
                InputStream inputStream = socket.getInputStream();
//                    int count2 = inputStream.available();
//                    WriteLog.writeTxtToFile("inputstreamavailable------------" + count2 + "ddfdg" + socket.isConnected());
                //byte[] buffer = new byte[count2];
//                        while (socket.isConnected() && isStart) {//&& ((length = inputStream.read(buffer)) != -1)
//                            int byteNum = inputStream.read(buffer);
//                            WriteLog.writeTxtToFile("byteNum----------------"+byteNum);
//                            MessageEventChuanCai messageEventChuanCai = new MessageEventChuanCai(buffer);//进程间通信onevent或者广播无效
//                            EventBus.getDefault().post(messageEventChuanCai);
//
//                        }

                while (socket != null && socket.isConnected()) {//这一行必须有，不然收不到后续//&& isStart
                    bytes = 0;
//                        while ((ch = inputStream.read()) != 3) {//'\n'   // in.read(bs,0,available);
//                            if (ch != -1) {
//                                buffer[bytes] = (byte) ch;
//                                bytes++;
//                            }
//                        }
//                        buffer[bytes] = 3;//'\n'
//                        bytes++;
                    while ((ch = inputStream.read()) != -1) {//'\n'   // in.read(bs,0,available);
                        //if (ch != -1) {
                        buffer[bytes] = (byte) ch;
                        bytes++;
                        if (ch == 3) {
                            break;
                        }
                        //}
                    }
                    byte[] newByte = new byte[1024];
                    System.arraycopy(buffer, 0, newByte, 0, buffer.length);
                    MessageEventChuanCai messageEventChuanCai = new MessageEventChuanCai(newByte);//进程间通信onevent或者广播无效
//                    EventBus.getDefault().post(messageEventChuanCai);
                    Bundle bundle =new Bundle();
                    bundle.putSerializable("messageEventChuanCai",messageEventChuanCai);
                    Message message = Message.obtain();
                    message.obj = bundle;
                    initHandler();
                    if(handler!=null){
                        handler.sendMessage(message);
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
                serviceClose = true;
                WriteLog.writeTxtToFile("inputstreamavailable2------------" + socket.isConnected() + "hhhh" + e.toString());
//                    if (socket != null) {
//                        socket.close();
//                    }
            } finally {
//                    try {
//                        if (socket != null) {
//                            socket.close();
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

            }
            //              }
        }
    }

//        /* Call this from the main activity to send data to the remote device */
//        public void send(byte[] bytes) {
//            try {
//                mOutputStream.write(bytes);
//            } catch (IOException e) {
//            }
//        }
//
//        /* Call this from the main activity to shutdown the connection */
//        public void cancel() {
//            try {
//                mOutputStream.close();
//            } catch (IOException e) {
//            }
//        }
}
