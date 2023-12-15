package com.santint.colorformula;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcelable;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cyw.mylibrary.base.BaseAppCompatActivity;
import com.cyw.mylibrary.netrequest.service.BackService;
import com.cyw.mylibrary.netrequest.utils.NetApplication;
import com.cyw.mylibrary.services.CannedResultService;
import com.cyw.mylibrary.services.ColorBeanService;
import com.cyw.mylibrary.services.DDService;
import com.cyw.mylibrary.services.FMServiceNew;
import com.cyw.mylibrary.services.FormulaDataService;
import com.cyw.mylibrary.services.PumpConfigBeanService;
import com.cyw.mylibrary.services.ResultBreakService;
import com.cyw.mylibrary.services.ResultDataService;
import com.cyw.mylibrary.util.MyToastUtil;
import com.cyw.mylibrary.util.SharedPreferencesHelper;
import com.cyw.mylibrary.util.WriteLog;
import com.google.android.material.snackbar.Snackbar;
import com.net.netrequest.utils.ConnectedThread;
import com.santint.colorformula.adapter.BluetoothBondAdapter;
import com.santint.colorformula.adapter.BluetoothFindAdapter;
import com.santint.colorformula.bean.BluetoothDeviceBean;
import com.santint.colorformula.utils.BluetoothController;
import com.santint.colorformula.utils.BluetoothParams;
import com.santint.colorformula.utils.ClsUtils;
import com.santint.colorformula.utils.UtilsWithPermission;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * chenyawei
 */

public class BlueToothActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private RecyclerView ryFind;
    private RecyclerView ryBond;

    private BluetoothAdapter bluetoothAdapter;
    private final int REQUEST_ENABLE_BT = 1;
    private final List<BluetoothDevice> bluetoothDevices = new ArrayList<>();
    private List<BluetoothDevice> bondDeviceList = new ArrayList<>();
    private BluetoothAdapter.LeScanCallback mLeScanCallback;
    private final boolean mScanning = false;
    private final int SCAN_PERIOD = 1000;
    private BluetoothController mController;
    private BluetoothFindAdapter bluetoothFindAdapter;
    private BluetoothBondAdapter bluetoothBondAdapter;
    private TextView tvFind, tvBond;
    //ClientThread clientThread;
    ExecutorService threadExecutor;
    private myBtReceiver broadcastReceiver;
    private static final int CONNECT_SUCCESS = 0x01;
    private static final int CONNECT_FAILURE = 0x02;
    private static final int DISCONNECT_SUCCESS = 0x03;
    private static final int SEND_SUCCESS = 0x04;
    private static final int SEND_FAILURE = 0x05;
    private static final int RECEIVE_SUCCESS = 0x06;
    private static final int RECEIVE_FAILURE = 0x07;
    private static final int START_DISCOVERY = 0x08;
    private static final int STOP_DISCOVERY = 0x09;
    private static final int DISCOVERY_DEVICE = 0x0A;
    private static final int DEVICE_BOND_NONE = 0x0B;
    private static final int DEVICE_BONDING = 0x0C;
    private static final int DEVICE_BONDED = 0x0D;
    private final String ACTIONFILTER = "android.bluetooth.device.action.PAIRING_REQUEST";
    private final String pin = "1234";
    private String address = "";
    private ConnectedThread connectThread;
    private boolean isOnce = true;
    private View mDecorView;

    Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case BluetoothParams.MSG_REV_A_CLIENT:
                    //Log.e(TAG,"--------- uihandler set device name, go to data frag");
                    BluetoothDevice clientDevice = (BluetoothDevice) msg.obj;
//                    dataTransFragment.receiveClient(clientDevice);
//                    viewPager.setCurrentItem(1);
                    break;
                case BluetoothParams.MSG_CONNECT_TO_SERVER:
//                    Log.e(TAG,"--------- uihandler set device name, go to data frag");
//                    BluetoothDevice serverDevice = (BluetoothDevice) msg.obj;
//                    dataTransFragment.connectServer(serverDevice);
//                    viewPager.setCurrentItem(1);
                    break;
                case BluetoothParams.MSG_SERVER_REV_NEW:
                    String newMsgFromClient = msg.obj.toString();
                    //dataTransFragment.updateDataView(newMsgFromClient, BluetoothParams.REMOTE);
                    break;
                case BluetoothParams.MSG_CLIENT_REV_NEW:
                    String newMsgFromServer = msg.obj.toString();
                    //dataTransFragment.updateDataView(newMsgFromServer, BluetoothParams.REMOTE);
                    break;
                case BluetoothParams.MSG_WRITE_DATA:
                    String dataSend = msg.obj.toString();
//                    dataTransFragment.updateDataView(dataSend, BluetoothParams.ME);
//                    deviceListFragment.writeData(dataSend);

                    break;
                case BluetoothParams.CONNECT_FAILE:
//                    Log.e(TAG,"--------- connect faile");
//                    toast("连接失败");
                    break;
//                case MSG_DATA:
//                    Log.e(TAG,"--------- connect faile");
//                    Toast.makeText(MainActivity.this,msg.getData().getString("data"),Toast.LENGTH_SHORT).show();
//                    break;
                case START_DISCOVERY:
//                    ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//                    ToastUtils.setBgResource(R.color.gray);
//                    ToastUtils.showShort("开始搜索设备...");
                    break;

                case STOP_DISCOVERY:
                    mController.cancelDiscovery();
//                    if (tvFind.isSelected()) {
//                        ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//                        ToastUtils.setBgResource(R.color.gray);
//                        ToastUtils.showShort("停止搜索设备...");
//                    }

                    //BluetoothDevice device = (BluetoothDevice) msg.obj;
                    //  device.fetchUuidsWithSdp();
                    break;

                case DISCOVERY_DEVICE:  //扫描到设备

                    BluetoothDevice bluetoothDevice = (BluetoothDevice) msg.obj;
                    //lvDevicesAdapter.addDevice(bluetoothDevice);
                    if (isNewDevice(bluetoothDevice)) {
                        bluetoothDevices.add(bluetoothDevice);
                        bluetoothFindAdapter.notifyDataSetChanged();
                    }

                    break;

                case CONNECT_FAILURE: //连接失败
                    // Log.d(TAG, "连接失败");
//                    tvCurConState.setText("连接失败");
//                    curConnState = false;
                    break;

                case CONNECT_SUCCESS:  //连接成功
                    // Log.d(TAG, "连接成功");
//                    tvCurConState.setText("连接成功");
//                    curConnState = true;
//                    llDataSendReceive.setVisibility(View.VISIBLE);
//                    llDeviceList.setVisibility(View.GONE);
                    break;

                case DISCONNECT_SUCCESS:
                    //tvCurConState.setText("断开成功");
                    //curConnState = false;

                    break;

                case SEND_FAILURE: //发送失败
                    //Toast.makeText(BlueToothActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                    MyToastUtil.showToastSnackBar(mDecorView,"发送失败",Snackbar.LENGTH_SHORT);
                    break;

                case SEND_SUCCESS:  //发送成功
                    String sendResult = (String) msg.obj;
                    //tvSendResult.setText(sendResult);
                    break;

                case RECEIVE_FAILURE: //接收失败
                    String receiveError = (String) msg.obj;
                    //tvReceive.setText(receiveError);
                    break;

                case RECEIVE_SUCCESS:  //接收成功
                    String receiveResult = (String) msg.obj;
                    //tvReceive.setText(receiveResult);
                    break;
                case DEVICE_BOND_NONE:  //已解除配对
                    // tvCurBondState.setText("解除配对成功");
                    //curBondState = false;

                    break;

                case DEVICE_BONDING:   //正在配对
                    // tvCurBondState.setText("正在配对...");
                    break;

                case DEVICE_BONDED:   //已配对
                    // tvCurBondState.setText("配对成功");
                    // curBondState = true;
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_layout);
        mDecorView = getWindow().getDecorView();
       // moveTaskToBack(true);
        WriteLog.writeTxtToFile("bluetooth------------------oncreate");
//        PowderPumpProtocal.childTaskProtocal();
        // PowderPumpProtocal.cannedProtocal("2","3",null);
        //  AppApplication.getInstance().addActivitys(this);
        threadExecutor = Executors.newFixedThreadPool(1);//Runtime.getRuntime().availableProcessors()*2
        initView();
        BluetoothDeviceBean device = SharedPreferencesHelper.getObject(BlueToothActivity.this, "device");
        if (device != null) {
            address = device.getAddress();
        }

//        IntentFilter intentFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
//        registerReceiver(broadcastReceiver,intentFilter);
        //蓝牙核心对象获取
        final BluetoothManager bluetoothManager;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            bluetoothAdapter = bluetoothManager.getAdapter();
        }

        //注册蓝牙广播接收器
        broadcastReceiver = new myBtReceiver();
        IntentFilter intentFilter1 = new IntentFilter();
        //开始查找
        intentFilter1.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        //结束查找
        intentFilter1.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        //查找设备
        intentFilter1.addAction(BluetoothDevice.ACTION_FOUND);
        //设备扫描模式改变
        intentFilter1.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        //绑定状态
        intentFilter1.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        // 监视蓝牙设备与APP连接的状态
        intentFilter1.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        intentFilter1.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        intentFilter1.addAction(ACTIONFILTER);
        intentFilter1.addAction(BluetoothDevice.ACTION_UUID);
        intentFilter1.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter1.addAction("com.santintteamilk.launchactivty.BlueToothActivity");
        registerReceiver(broadcastReceiver, intentFilter1);

        broadcastReceiver.SetPairlistener(new MakePariBlueToothListener() {
            @Override
            public void whilePari(BluetoothDevice device) {

            }

            @Override
            public void pairingSuccess(BluetoothDevice device) {
                bluetoothFindAdapter.notifyDataSetChanged();//先配对成功，再连接
                startConnect(device);//不需要再次调用
                // Toast.makeText("配对完成");
            }

            @Override
            public void cancelPari(BluetoothDevice device) {

            }
        });
        mController = new BluetoothController();
        if (!mController.isSupportBluetooth()) {//判断是否开启
            mController.turnOnBluetooth(this, 1);///请求开启蓝牙
//            ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//            ToastUtils.setBgResource(R.color.gray);
//            ToastUtils.showShort("当前设备不支持蓝牙");
//            ToastUtils.setMsgTextSize(22);
//            return;
        }

        //获取代理 ，监听连接状态
        // getProfile();
        bondDeviceList = mController.getBondedDeviceList();
        for (int i = 0; i < bondDeviceList.size(); i++) {
            BluetoothDevice bluetoothDevice = bondDeviceList.get(i);
            WriteLog.writeTxtToFile(bluetoothDevice.getAddress() + "连接设备匹配" + address);
            if (bluetoothDevice.getAddress().equals(address)) {
                WriteLog.writeTxtToFile("bluetooth------------------address");
                startConnect(bluetoothDevice);
                break;
            }
        }
        //requestPermission();
        // mController.findDevice(BlueToothActivity.this);
        readWritePermission();
        //  permissionRequest();
    }

    private boolean isRefuse = false;
    private final int REQ_WRITE_STORAGE = 11;

    private void readWritePermission() {
        //需要注意
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            UtilsWithPermission.readWriteStorage(BlueToothActivity.this, REQ_WRITE_STORAGE);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !isRefuse) {// android 11  且 不是已经被拒绝
            // 先判断有没有权限
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 1024);
            } else {
                permissionBack();
            }

//            ActivityCompat.requestPermissions(BlueToothActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    BluetoothParams.MY_PERMISSION_REQUEST_CONSTANT);
        }
    }

    public void permissionBack() {
        CannedResultService.getInstance().initDao(BlueToothActivity.this);
        ResultDataService.getInstance().initDao(BlueToothActivity.this);
        ResultBreakService.getInstance().initDao(BlueToothActivity.this);
        FormulaDataService.getInstance().initDao(BlueToothActivity.this);
        ColorBeanService.getInstance().initDao(BlueToothActivity.this);
        PumpConfigBeanService.getInstance().initDao(BlueToothActivity.this);
        FMServiceNew.getInstance().initDao(BlueToothActivity.this);
        DDService.getInstance().initDao(BlueToothActivity.this);
        mController.findDevice(BlueToothActivity.this);//读写权限申请成功，申请位置权限
    }


    public interface MakePariBlueToothListener {

        void whilePari(BluetoothDevice device);

        void pairingSuccess(BluetoothDevice device);

        void cancelPari(BluetoothDevice device);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            //位置权限申请成功
            case BluetoothParams.MY_PERMISSION_REQUEST_CONSTANT: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 运行时权限已授权
                    //mController.findDevice(BlueToothActivity.this);

                } else {
                    //requestPermission();
                }

            }
        }
    }


    /**
     * 判断搜索的设备是新蓝牙设备，且不重复
     *
     * @param device
     * @return
     */
    private boolean isNewDevice(BluetoothDevice device) {
        boolean repeatFlag = false;
        for (BluetoothDevice d :
                bluetoothDevices) {
            if (d.getAddress().equals(device.getAddress())) {
                repeatFlag = true;
            }
        }
        //不是已绑定状态，且列表中不重复
        return device.getBondState() != BluetoothDevice.BOND_BONDED && !repeatFlag;
    }

    private class myBtReceiver extends BroadcastReceiver {
        MakePariBlueToothListener mMakePariListener;

        public void SetPairlistener(MakePariBlueToothListener makePariBlueToothListener) {
            this.mMakePariListener = makePariBlueToothListener;
        }

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            Log.i("TAGACTION", action + "======================");
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                //蓝牙设备

                //信号强度
                int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);

                Log.i("TAGTAGDEVICE", "扫描到设备：" + bluetoothDevice.getName() + "-->" + bluetoothDevice.getAddress());
                Message message = new Message();
                message.what = DISCOVERY_DEVICE;
                message.obj = bluetoothDevice;
                uiHandler.sendMessage(message);

            } else if (TextUtils.equals(action, BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {

                int bondSate = bluetoothDevice.getBondState();
                switch (bondSate) {
                    case BluetoothDevice.BOND_NONE:
                        Log.d("TAGBOND3", "已解除配对");
//                        Message message1 = new Message();
//                        message1.what = DEVICE_BOND_NONE;
//                        uiHandler.sendMessage(message1);
                        mMakePariListener.cancelPari(bluetoothDevice);
                        break;

                    case BluetoothDevice.BOND_BONDING:
//                        Log.d("TAGBOND1", "正在配对...");
//                        Message message2 = new Message();
//                        message2.what = DEVICE_BONDING;
//                        uiHandler.sendMessage(message2);
                        mMakePariListener.whilePari(bluetoothDevice);
                        break;

                    case BluetoothDevice.BOND_BONDED:
                        Log.d("TAGBOND2", "已配对");
//                        Message message3 = new Message();
//                        message3.what = DEVICE_BONDED;
//                        uiHandler.sendMessage(message3);
                        mMakePariListener.pairingSuccess(bluetoothDevice);
                        break;
                }
            } else if (ACTIONFILTER.equals(action)) {
                try {
                    //1.确认配对
                    ClsUtils.setPairingConfirmation(bluetoothDevice.getClass(), bluetoothDevice, true);
                    //2.终止有序广播
                    Log.i("order...", "isOrderedBroadcast:" + isOrderedBroadcast() + ",isInitialStickyBroadcast:" + isInitialStickyBroadcast());
                    abortBroadcast();//如果没有将广播终止，则会出现一个一闪而过的配对框。
                    //3.调用setPin方法进行配对...
                    boolean ret = ClsUtils.setPin(bluetoothDevice.getClass(), bluetoothDevice, pin);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (TextUtils.equals(action, BluetoothAdapter.ACTION_DISCOVERY_STARTED)) {
                Message message = new Message();
                message.what = START_DISCOVERY;
                uiHandler.sendMessage(message);
            } else if (TextUtils.equals(action, BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                Message message = new Message();
                message.what = STOP_DISCOVERY;
                message.obj = bluetoothDevice;
                uiHandler.sendMessage(message);
            } else if (TextUtils.equals(action, BluetoothDevice.ACTION_UUID)) {
                BluetoothDevice d = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Parcelable[] uuidExtra = intent.getParcelableArrayExtra(BluetoothDevice.EXTRA_UUID);
                if (uuidExtra == null) {
                }
            } else if (TextUtils.equals(action, BluetoothDevice.ACTION_ACL_CONNECTED)) {
//                ToastUtils.showLong("蓝牙已连接");
//                ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//                ToastUtils.setBgResource(R.color.gray);
//                ToastUtils.setMsgTextSize(22);
                // Toast.makeText(BlueToothActivity.this,"蓝牙已连接",Toast.LENGTH_SHORT).show();
               // MyToastUtil.showToast(BlueToothActivity.this, "蓝牙已连接", Toast.LENGTH_SHORT);、
                MyToastUtil.showToastSnackBar(mDecorView,"蓝牙已连接",Snackbar.LENGTH_SHORT);
            } else if (TextUtils.equals(action, BluetoothDevice.ACTION_ACL_DISCONNECTED)) {//蓝牙与app断开连接
//                ToastUtils.showLong("蓝牙已断开连接");
//                ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//                ToastUtils.setBgResource(R.color.gray);
//                ToastUtils.setMsgTextSize(22);
                //MyToastUtil.showToast(BlueToothActivity.this, "蓝牙已断开连接", Toast.LENGTH_SHORT);
                MyToastUtil.showToastSnackBar(mDecorView,"蓝牙已断开连接",Snackbar.LENGTH_SHORT);
            } else if (TextUtils.equals(action, BluetoothAdapter.ACTION_STATE_CHANGED)) {
                int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                switch (blueState) {
                    case BluetoothAdapter.STATE_TURNING_ON:
                       // MyToastUtil.showToast(context, "蓝牙正在打开", Toast.LENGTH_SHORT);
                        MyToastUtil.showToastSnackBar(mDecorView,"蓝牙正在打开",Snackbar.LENGTH_SHORT);
                        break;
                    case BluetoothAdapter.STATE_ON:
//                        ToastUtils.showLong("蓝牙已经打开");
//                        ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//                        ToastUtils.setBgResource(R.color.gray);
//                        ToastUtils.setMsgTextSize(18);
                       // MyToastUtil.showToast(BlueToothActivity.this, "蓝牙已经打开", Toast.LENGTH_LONG);
                        MyToastUtil.showToastSnackBar(mDecorView,"蓝牙已经打开",Snackbar.LENGTH_SHORT);
                        bondDeviceList = mController.getBondedDeviceList();
                        for (int i = 0; i < bondDeviceList.size(); i++) {
                            BluetoothDevice bluetoothDevice2 = bondDeviceList.get(i);
                            if (bluetoothDevice2.getAddress().equals(address)) {
                                startConnect(bluetoothDevice2);
                                break;
                            }
                        }
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        //MyToastUtil.showToast(context, "蓝牙正在关闭", Toast.LENGTH_SHORT);
                        MyToastUtil.showToastSnackBar(mDecorView,"蓝牙正在关闭",Snackbar.LENGTH_SHORT);
                        break;
                    case BluetoothAdapter.STATE_OFF:
//                        ToastUtils.showLong("蓝牙已经关闭");
//                        ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//                        ToastUtils.setBgResource(R.color.gray);
//                        ToastUtils.setMsgTextSize(18);
                       // MyToastUtil.showToast(context, "蓝牙已经关闭", Toast.LENGTH_SHORT);
                        MyToastUtil.showToastSnackBar(mDecorView,"蓝牙已经关闭",Snackbar.LENGTH_SHORT);
                        break;
                }
            } else if (action.equals("com.santintteamilk.launchactivty.BlueToothActivity")) {
                int state = bluetoothAdapter.getProfileConnectionState(BluetoothProfile.GATT);
//                if (state != BluetoothAdapter.STATE_CONNECTED) {
//                    Log.i("TAGbblue", "未连接");
//                } else {
                if (isOnce) {
                    isOnce = false;
                    Intent intent2 = new Intent(BlueToothActivity.this, CorrectionActivity.class);
                    //startActivity(intent2);
                    //intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivityAni(intent2);
                }

                // finish();
                // }

                //finish();
            }
        }
    }

    private void requestPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //允许一个程序访问CellID或WiFi热点来获取粗略的位置
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, BluetoothParams.MY_PERMISSION_REQUEST_CONSTANT);

            } else {
                mController.findDevice(BlueToothActivity.this);

            }

        } else {
            mController.findDevice(BlueToothActivity.this);

        }
    }
//    @Permission(value=Manifest.permission.ACCESS_COARSE_LOCATION,requestCode = 100)
//    public void permissionRequest(){
//        Toast.makeText(this, "权限申请成功...", Toast.LENGTH_SHORT).show();
//        mController.findDevice(BlueToothActivity.this);
//    }
//    @PermissionCancel
//    public void permissionCancel(){
//        Toast.makeText(this, "权限被拒绝", Toast.LENGTH_SHORT).show();
//    }
//    // 多次拒绝，还勾选了“不再提示”
//    @PermissionDenied
//    public void permissionDenied(){
//        Toast.makeText(this, "权限被拒绝(用户勾选了 不再提示)，注意：你必须要去设置中打开此权限，否则功能无法使用", Toast.LENGTH_SHORT).show();
//    }

    //  private InetrequestInterface inetrequestInterface;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //NetApplication.inetrequestInterface = InetrequestInterface.Stub.asInterface(service);
            NetApplication.inetrequestInterface = ((BackService.MsgBinder) service).getService();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //NetApplication.inetrequestInterface = null;
            NetApplication.inetrequestInterface = null;

        }
    };

    public void startConnect(BluetoothDevice device) {
        //mController.getRemoteDevice(device.getAddress());
        Intent serviceIntent = new Intent(this, BackService.class);
        //serviceIntent.put("device",bluetoothAdapter);
        serviceIntent.putExtra("device", device);//传递parcelable数据
        serviceIntent.setType(device.getAddress());//可以多次绑定service
        bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE);
        BluetoothDeviceBean deviceBean = new BluetoothDeviceBean();
        deviceBean.setName(device.getName());
        deviceBean.setAddress(device.getAddress());
        SharedPreferencesHelper.putObject(BlueToothActivity.this, "device", (Serializable) deviceBean);
        // 开启客户端线程，连接点击的远程设备
//        ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//        ToastUtils.setBgResource(R.color.gray);
//        ToastUtils.showShort("连接中...");
//        ToastUtils.setMsgTextSize(22);
        //Toast.makeText(BlueToothActivity.this,"连接中",Toast.LENGTH_SHORT).show();
        //MyToastUtil.showToast(BlueToothActivity.this, "连接中", Toast.LENGTH_SHORT);
        MyToastUtil.showToastSnackBar(getWindow().getDecorView(),"连接中", Snackbar.LENGTH_LONG);
//        //if (NetApplication.clientThread == null) {
//        NetApplication.clientThread = new ClientThread(bluetoothAdapter, device, uiHandler);
//        NetApplication.clientThread.SetConnectBack(new ClientThread.ConnectBack() {
//            @Override
//            public void connectsuccess(BluetoothSocket socket,BluetoothDevice device) {
//                // 通知 ui 连接的服务器端设备
//                ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//                ToastUtils.setBgResource(R.color.gray);
//                ToastUtils.showShort(device.getName() + "连接成功！");
//                NetApplication.connectedThread = new ConnectedThread(socket);
//                NetApplication.connectedThread.start();
//                Intent intent = new Intent(BlueToothActivity.this, MainActivity.class);
//                startActivity(intent);
//                //finish();
////                Message message = new Message();
////                message.what = BluetoothParams.MSG_CONNECT_TO_SERVER;
////                message.obj = device;
////                uiHandler.sendMessage(message);
//            }
//
//            @Override
//            public void connectfaile(BluetoothDevice device) {
//                ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//                ToastUtils.setBgResource(R.color.gray);
//                ToastUtils.showShort("连接失败，请检查对方蓝牙是否打开");
//                // mtoast.toast("连接失败,请检查服务端是否打开。");
//            }
//
//            @Override
//            public void connecting(BluetoothDevice device) {
//                // mtoast.toast("请稍等，正在连接中。");
//            }
//        });
//        // }

//        threadExecutor.execute(NetApplication.clientThread);
    }

//    public void startConnect(BluetoothDevice device) {
//        BluetoothDeviceBean deviceBean = new BluetoothDeviceBean();
//        deviceBean.setName(device.getName());
//        deviceBean.setAddress(device.getAddress());
//        SharedPreferencesHelper.putObject(BlueToothActivity.this, "device", (Serializable) deviceBean);
//        // 开启客户端线程，连接点击的远程设备
//        ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//        ToastUtils.setBgResource(R.color.gray);
//        ToastUtils.showShort("连接中...");
//        //if (NetApplication.clientThread == null) {
//        NetApplication.clientThread = new ClientThread(bluetoothAdapter, device, uiHandler);
//        NetApplication.clientThread.SetConnectBack(new ClientThread.ConnectBack() {
//            @Override
//            public void connectsuccess(BluetoothSocket socket,BluetoothDevice device) {
//                // 通知 ui 连接的服务器端设备
//                ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//                ToastUtils.setBgResource(R.color.gray);
//                ToastUtils.showShort(device.getName() + "连接成功！");
//                NetApplication.connectedThread = new ConnectedThread(socket);
//                NetApplication.connectedThread.start();
//                Intent intent = new Intent(BlueToothActivity.this, MainActivity.class);
//                startActivity(intent);
//                //finish();
////                Message message = new Message();
////                message.what = BluetoothParams.MSG_CONNECT_TO_SERVER;
////                message.obj = device;
////                uiHandler.sendMessage(message);
//            }
//
//            @Override
//            public void connectfaile(BluetoothDevice device) {
//                ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//                ToastUtils.setBgResource(R.color.gray);
//                ToastUtils.showShort("连接失败，请检查对方蓝牙是否打开");
//                // mtoast.toast("连接失败,请检查服务端是否打开。");
//            }
//
//            @Override
//            public void connecting(BluetoothDevice device) {
//                // mtoast.toast("请稍等，正在连接中。");
//            }
//        });
//        // }
//
//        threadExecutor.execute(NetApplication.clientThread);
//
//    }

    /**
     * 断开已有的连接
     */
    public void clearConnectedThread() {

        //connectedThread断开已有连接
        if (NetApplication.connectedThread == null) {
//            Log.e(TAG,"clearConnectedThread-->connectedThread == null");
            return;
        }
        //NetApplication.connectedThread .terminalClose(connectThread);

        //等待线程运行完后再断开
        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                NetApplication.connectedThread.cancel();  //释放连接

                NetApplication.connectedThread = null;
            }
        }, 10);

        //Log.w(TAG,"clearConnectedThread-->成功断开连接");
        Message message = new Message();
        message.what = DISCONNECT_SUCCESS;
        // mHandler.sendMessage(message);

    }

    private void initView() {
        ryFind = findViewById(R.id.id_ry_find);
        ryBond = findViewById(R.id.id_ry_bond);
        tvFind = findViewById(R.id.id_tv_find);
        tvBond = findViewById(R.id.id_tv_bond);
        tvFind.setOnClickListener(this);
        tvBond.setOnClickListener(this);

        tvFind.setSelected(true);
        tvBond.setSelected(false);
        bindAdapter();

    }

    /**
     * 用户打开蓝牙后，显示已绑定的设备列表
     */
    private void showBondDevice() {
        // bondDeviceList.clear();
//        Set<BluetoothDevice> tmp = bluetoothBondAdapter.getBondedDevices();
//        for (BluetoothDevice d :
//                tmp) {
//            deviceList.add(d);
//        }
//        listAdapter.notifyDataSetChanged();
        //bluetoothBondAdapter.notifyDataSetChanged();
        ryBond.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        bluetoothBondAdapter = new BluetoothBondAdapter(this, bondDeviceList, ryBond);
        ryBond.setAdapter(bluetoothBondAdapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    public void itmClikPostion(int position) {
//        if (serverThread != null) {
//            serverThread.cancel();
//            serverThread = null;
////            Log.e(TAG, "---------------client item click , cancel server thread ," +
////                    "server thread is null");
//        }
        final BluetoothDevice device1 = bluetoothDevices.get(position);
        bondDevice(device1);
//        // 开启客户端线程，连接点击的远程设备
//        clientThread = new ClientThread(bluetoothAdapter, device1, uiHandler);
//        threadExecutor.execute(clientThread);
////                new Thread(clientThread).start();
//        clientThread.SetConnectBack(new ClientThread.ConnectBack() {
//            @Override
//            public void connectsuccess(BluetoothDevice device) {
//                // 通知 ui 连接的服务器端设备
//                Log.i("TAGSUCCESS", "HHHHHHHHHHH");
////                Message message = new Message();
////                message.what = BluetoothParams.MSG_CONNECT_TO_SERVER;
////                message.obj = device;
////                uiHandler.sendMessage(message);
//            }
//
//            @Override
//            public void connectfaile(BluetoothDevice device) {
//                Log.i("TAGFAILURE", "HHHH");
//                // mtoast.toast("连接失败,请检查服务端是否打开。");
//            }
//
//            @Override
//            public void connecting(BluetoothDevice device) {
//                Log.i("TAGIN", "HHHHH");
//                // mtoast.toast("请稍等，正在连接中。");
//            }
//        });


    }

    public void bondDevice(BluetoothDevice device) {
        //配对之前把扫描关闭
        if (mController.isDiscovering()) {
            mController.cancelDiscovery();
        }

        //判断设备是否配对，没有配对在配，配对了就不需要配了
        if (device.getBondState() == BluetoothDevice.BOND_NONE) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                device.createBond();
//            }
            Method method = null;
            try {
                method = BluetoothDevice.class.getMethod("createBond");
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            Log.e(getPackageName(), "开始配对");
            try {
                method.invoke(device);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
//        Intent intent = new Intent(BlueToothActivity.this, MainActivity.class);
//        startActivity(intent);
    }


    //得到配对的设备列表，清除已配对的设备
    public void removePairDevice(String name) {
        if (bluetoothBondAdapter != null) {
            //List<BluetoothDevice> bondedDevices = mController.getBondedDeviceList();
//            for (BluetoothDevice device : bondDeviceList) {
//                bondDeviceList.remove(device);
//                unpairDevice(device);
//            }
            for (Iterator iterator = bondDeviceList.iterator(); iterator.hasNext(); ) {
                BluetoothDevice device = (BluetoothDevice) iterator.next();
                if (name.equals(device.getName())) {
                    iterator.remove();
                    unpairDevice(device);
                }

            }
            bluetoothBondAdapter.notifyDataSetChanged();
        }

    }

    //反射来调用BluetoothDevice.removeBond取消设备的配对
    private void unpairDevice(BluetoothDevice device) {
        try {
            Method m = device.getClass()
                    .getMethod("removeBond", (Class[]) null);
            m.invoke(device, (Object[]) null);
        } catch (Exception e) {

        }
    }

    private void bindAdapter() {
        ryFind.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        bluetoothFindAdapter = new BluetoothFindAdapter(this, bluetoothDevices, ryFind);
        bluetoothFindAdapter.setAddress(address);
        //ryFind.addItemDecoration(new DividerGridItemDecoration(BlueToothActivity.this));
        ryFind.setAdapter(bluetoothFindAdapter);

        ryBond.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //ryBond.addItemDecoration(new DividerGridItemDecoration(BlueToothActivity.this));
        bluetoothBondAdapter = new BluetoothBondAdapter(this, bondDeviceList, ryBond);
        ryBond.setAdapter(bluetoothBondAdapter);
//        // 默认开启服务线程监听
//        if (serverThread != null) {
//            serverThread.cancel();
//        }
//        // Log.e(TAG, "-------------- new server thread");
//        serverThread = new ServerThread(bluetoothFindAdapter, uiHandler);
//        new Thread(serverThread).start();


//        // 蓝牙未打开，询问打开
//        if (!bluetoothAdapter.isEnabled()) {
//            Intent turnOnBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(turnOnBtIntent, BluetoothParams.REQUEST_ENABLE_BT);
//        }
//
//
//        // 蓝牙已开启
//        if (bluetoothAdapter.isEnabled()) {
//            showBondDevice();
//            // 默认开启服务线程监听
//            if (serverThread != null) {
//                serverThread.cancel();
//            }
//           // Log.e(TAG, "-------------- new server thread");
//            serverThread = new ServerThread(bluetoothAdapter, uiHandler);
//            new Thread(serverThread).start();
//        }
//        listView.seton(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                // 关闭服务器监听
//                if (serverThread != null) {
//                    serverThread.cancel();
//                    serverThread = null;
//                    Log.e(TAG, "---------------client item click , cancel server thread ," +
//                            "server thread is null");
//                }
//                BluetoothDevice device = deviceList.get(position);
//                // 开启客户端线程，连接点击的远程设备
//                clientThread = new ClientThread(bluetoothAdapter, device, uiHandler);
//                new Thread(clientThread).start();
//
//                // 通知 ui 连接的服务器端设备
//                Message message = new Message();
//                message.what = Params.MSG_CONNECT_TO_SERVER;
//                message.obj = device;
//                uiHandler.sendMessage(message);
//
//            }
//        });
    }

    // 带回授权结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Android 11 以上读写权限申请
        if (requestCode == 1024 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 检查是否有权限
            if (Environment.isExternalStorageManager()) {
                isRefuse = false;
                permissionBack();
                // 授权成功
            } else {
                isRefuse = true;
                // 授权失败
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.id_tv_find:
                ryFind.setVisibility(View.VISIBLE);
                ryBond.setVisibility(View.GONE);
                tvFind.setSelected(true);
                tvBond.setSelected(false);
                bluetoothFindAdapter.notifyDataSetChanged();
                break;
            case R.id.id_tv_bond:
                ryFind.setVisibility(View.GONE);
                ryBond.setVisibility(View.VISIBLE);
                tvFind.setSelected(false);
                tvBond.setSelected(true);
                bondDeviceList = mController.getBondedDeviceList();
                showBondDevice();
                break;

        }
    }


//    private void initBluetooth(){
//        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
//        bluetoothAdapter = bluetoothManager.getAdapter();
//        isBluetoothEnable();
//        final BluetoothAdapter.LeScanCallback callback = new BluetoothAdapter.LeScanCallback() {
//            @Override
//            public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
//                bluetoothDevices.add(device);
//            }
//        };
//        bluetoothAdapter.startLeScan(callback);
//    }
//    private void scanLeDevice(final boolean enable) {
//        if (enable) {
//            // Stops scanning after a pre-defined scan period.
//            // 预先定义停止蓝牙扫描的时间（因为蓝牙扫描需要消耗较多的电量）
//            mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    mScanning = false;
//                    bluetoothAdapter.stopLeScan(mLeScanCallback);
//                }
//            }, SCAN_PERIOD);
//            mScanning = true;
//
//            // 定义一个回调接口供扫描结束处理
//            bluetoothAdapter.startLeScan(mLeScanCallback);
//        } else {
//            mScanning = false;
//            bluetoothAdapter.stopLeScan(mLeScanCallback);
//        }
//    }
//
//    private void isBluetoothEnable(){
//        if(bluetoothAdapter == null || !bluetoothAdapter.isEnabled()){
//            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(intent,REQUEST_ENABLE_BT);
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WriteLog.writeTxtToFile("bluetooth--------------ondestory");
        unbindService(serviceConnection);
        // AppApplication.getInstance().removeActivity(this);
        unregisterReceiver(broadcastReceiver);
    }
}
