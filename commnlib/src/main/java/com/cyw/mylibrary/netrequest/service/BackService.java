package com.cyw.mylibrary.netrequest.service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cyw.mylibrary.netrequest.utils.NetApplication;
import com.net.netrequest.InetrequestInterface;
import com.net.netrequest.bean.MessageEventChuanCai;
import com.net.netrequest.utils.BluetoothParams;
import com.net.netrequest.utils.Byte2Hex;
import com.net.netrequest.utils.DateFormatter;
import com.net.netrequest.utils.WriteLog;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.UUID;

/**
 * author： cyw
 */
public class BackService extends Service {
    /**
     * 心跳频率
     */
    private static final long HEART_BEAT_RATE = 10 * 1000;
    private long sendTime = 0L;
    private Handler mHandler = new Handler();
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;
    // public static BluetoothSocket socket;
    private ReadThread mReadThread;
    private WeakReference<BluetoothSocket> mSocket;
    private final int KEY_CONNECT_SUCCESS = 1;
    private final int KEY_CONNECT_FAIL = 2;
    private BluetoothDevice device = null;
    private boolean isHeartBack = true;
    BluetoothSocket socket = null;
    private boolean serviceClose = false;
    /**
     * 更新进度的回调接口
     */
    private OnReceiveDataListener onReceiveDataListener;


    public interface OnReceiveDataListener {
        void backData(byte[] data);
    }

    /**
     * 注册回调接口的方法，供外部调用
     *
     * @param onReceiveDataListener
     */
    public void setOnReceiveDataListener(OnReceiveDataListener onReceiveDataListener) {
        this.onReceiveDataListener = onReceiveDataListener;
    }

    /**
     * 心跳任务，不断重复调用自己
     */
    private Runnable heartBeatRunnable = new Runnable() {
        @Override
        public void run() {
            if (System.currentTimeMillis() - sendTime >= HEART_BEAT_RATE) {
                boolean isSuccess = heartJump();//原因heartjump发送失败
                //  Log.i("TAGHEARTBACK", isSuccess + "ijjj" + isHeartBack);
                //isHeartBack = false;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!isSuccess || !isHeartBack) {
                    WriteLog.writeTxtToFile("serviceClose-----------------" + serviceClose);
                    //if (serviceClose) {//!socket.isConnected()
                    mHandler.removeCallbacks(heartBeatRunnable);
                    mReadThread.release();
                    WriteLog.writeTxtToFile("releaseLastSocket4-----------------");
                    releaseLastSocket(mSocket);
                    new InitSocketThread().start();
                    // }
//                else {
//                    ToastUtils.showShort("设备连接正常");
//                }

                }
                if (isHeartBack) {
                    isHeartBack = false;
                }
            }
            mHandler.postDelayed(this, HEART_BEAT_RATE);
        }
    };

    public void heartBack() {
        isHeartBack = true;
    }

    private Handler mainHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == KEY_CONNECT_SUCCESS) {
//                ToastUtils.showLong("远端连接成功");
//                ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//                ToastUtils.setBgResource(R.color.gray);
//                ToastUtils.setMsgTextSize(18);
                Intent intent = new Intent();
                intent.putExtra("connectsuccess", true);
                intent.setAction("com.santintteamilk.launchactivty.BlueToothActivity");
                sendBroadcast(intent);
//                Intent intent = new Intent(BackService.this, MainActivity.class);
//                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                //finish();

            } else if (msg.what == KEY_CONNECT_FAIL) {
//                ToastUtils.showLong("远端连接失败");
//                ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//                ToastUtils.setBgResource(R.color.gray);
//                ToastUtils.setMsgTextSize(18);
//                Intent intent = new Intent();
//                intent.putExtra("connectsuccess",false);
//                intent.setAction("com.santintteamilk.launchactivty.BlueToothActivity");
//                sendBroadcast(intent);
            }
        }
    };

    interface OnConnectListener {
        void connectSucces(BluetoothDevice device);

        void connectFail(BluetoothDevice device);
    }

    /**
     * 更新进度的回调接口
     */
    private OnConnectListener onConnectListener;


    /**
     * 注册回调接口的方法，供外部调用
     *
     * @param onConnectListener
     */
    public void setOnConnectListener(OnConnectListener onConnectListener) {
        this.onConnectListener = onConnectListener;
    }

    //此处可以做扩展，自定义binder，拿到msgService实例对象
    private InetrequestInterface.Stub inetrequestInterface = new InetrequestInterface.Stub() {
        @Override
        public boolean sendMessage(byte[] data) throws RemoteException {
            return sendMsg(data);
        }
    };

    public class MsgBinder extends Binder {
        /**
         * 获取当前Service的实例
         *
         * @return
         */
        public BackService getService() {
            return BackService.this;
        }

    }

    int iDataPoint = 0;

    //发送心跳命令
    public boolean heartJump() {
        byte[] bDataSend = new byte[3];
        iDataPoint = 0;
        bDataSend[iDataPoint] = (byte) 2; //起始符02
        iDataPoint = 1;
        bDataSend[iDataPoint] = (byte) 23;//
        iDataPoint = 2;
        bDataSend[iDataPoint] = (byte) 3;
        byte[] dataSend = Byte2Hex.subBytes(bDataSend, 0, iDataPoint + 1);
//        if (NetApplication.inetrequestInterface != null) {
//            NetApplication.inetrequestInterface.sendMsg(dataSend);
//        }
//        MessageEventChuanCai messageEventChuanCai = new MessageEventChuanCai(dataSend);//进程间通信onevent或者广播无效
//        EventBus.getDefault().post(messageEventChuanCai);
        String resultShi = Byte2Hex.bytes2HexString(dataSend);
        String time = DateFormatter.getCurrentTime();
        WriteLog.writeTxtToFile(time + "心跳命令" + resultShi);
        if (NetApplication.inetrequestInterface != null) {
            return NetApplication.inetrequestInterface.sendMsg(dataSend);
            //sendMsg(dataSend);
        }
        return false;

    }

    private boolean sendSuccess = true;

    /* Call this from the main activity to send data to the remote device */
    public boolean sendMsg(byte[] bytes) {
        sendSuccess = true;
        if (null == mSocket || null == mSocket.get()) {
            sendSuccess = false;
            return sendSuccess;
        }
        final BluetoothSocket bluetoothSocket = mSocket.get();
        if (bluetoothSocket.isConnected()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        OutputStream outputStream = bluetoothSocket.getOutputStream();
                        outputStream.write(bytes);
                        outputStream.flush();
                    } catch (IOException e) {
                        Log.i("TAGXINTIAO", e.toString());
                        sendSuccess = false;
                    }
                }
            }).start();
            sendTime = System.currentTimeMillis();
        } else {
            sendSuccess = false;
        }

        return sendSuccess;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        device = intent.getParcelableExtra("device");//接收bindservice的传值
//        if (mReadThread != null) {
//            mReadThread.release();
//        }
//        if (mSocket != null) {
//            WriteLog.writeTxtToFile("releaseLastSocket-----------------");
//            releaseLastSocket(mSocket);
//        }
        WriteLog.writeTxtToFile("bluetooth------------------device");

        new InitSocketThread().start();//重复报远程段连接成功失败异常
        return new MsgBinder();
    }

    // private LocalBroadcastManager mLocalBroadcastManager;

    @Override
    public void onCreate() {
        super.onCreate();
//        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
//       // new InitSocketThread().start();
//        //device = intent.getParcelableExtra("device");
//        Log.i("TAGSOCKETDevice",device+"");//接收bindservice的传值
//        new InitSocketThread().start();//重复报远程段连接成功失败异常
    }

    class InitSocketThread extends Thread {
        @Override
        public void run() {
            super.run();
            initSocket();
        }
    }

    private void initSocket() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            bluetoothAdapter = bluetoothManager.getAdapter();
        }
        BluetoothSocket tmp = null;

        boolean connectSuccess = true;
        WriteLog.writeTxtToFile("bluetooth------------------"+socket );
        if (socket != null) {
            Log.i("TAGSOCKETRELEASE", "ConnectThread-->mmSocket != null先去释放");
            try {
                socket.close();
                socket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            if(tmp == null){
                tmp = device.createRfcommSocketToServiceRecord(UUID.fromString(BluetoothParams.UUID));
            }

        } catch (IOException e) {
            Log.i("TAGSOCKETtmp", " " + e.toString());
            e.printStackTrace();

        }
        socket = tmp;
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        if (socket == null) {
            //Log.e(TAG, "ConnectThread:run-->mmSocket == null");
            return;
        }
        mSocket = new WeakReference<BluetoothSocket>(socket);

        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception

            if (!socket.isConnected()) {
                socket.connect();
            }

        } catch (IOException connectException) {
            // Unable to connect; close the socket and get out
            String connect = connectException.toString();
            WriteLog.writeTxtToFile("connectFail" + connect);
            connectSuccess = false;
            Message message = Message.obtain();
            message.what = KEY_CONNECT_FAIL;
            mainHandler.sendMessage(message);
            // mConnectBack.connectfaile(device);

        }
        Log.i("TAGSOCKETAGAIN", "hhhhh" + socket.isConnected());
        if (socket != null && connectSuccess) {
            // sendTime = System.currentTimeMillis();
            // mConnectBack.connectsuccess(socket,device);
            Message message = Message.obtain();
            message.what = KEY_CONNECT_SUCCESS;
            mainHandler.sendMessage(message);
            mReadThread = new ReadThread(socket);
            mReadThread.start();
           // mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);
        }
    }

    class ReadThread extends Thread {
        private WeakReference<BluetoothSocket> mWeakSocket;
        private boolean isStart = true;
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
            mWeakSocket = new WeakReference<BluetoothSocket>(socket);

        }

        public void release() {
            isStart = false;
            WriteLog.writeTxtToFile("releaseLastSocket2-----------------");
            releaseLastSocket(mWeakSocket);
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
                        EventBus.getDefault().post(messageEventChuanCai);
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

    /**
     * 心跳机制判断出socket已经断开后，就销毁连接方便重新创建连接
     *
     * @param weakSocket
     */
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
    public void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacks(heartBeatRunnable);///如果不调用会导致退出应用后台一直在发心跳
        }
        if (mReadThread != null) {
            mReadThread.release();
        }
        WriteLog.writeTxtToFile("releaseLastSocket3-----------------");
        releaseLastSocket(mSocket);
    }
}
