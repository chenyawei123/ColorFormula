package com.santint.colorformula.utils;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * chenyawei
 */

public class BluetoothController {
    private final BluetoothAdapter bluetoothAdapter;

    public BluetoothController() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    /*
    是否支持蓝牙
     */
    public boolean isSupportBluetooth() {
        return bluetoothAdapter != null && bluetoothAdapter.isEnabled();
    }

    public boolean getBluetoothStatus() {
        assert (bluetoothAdapter != null);
        return bluetoothAdapter.isEnabled();
    }

    public void turnOnBluetooth(Activity activity, int requestCode) {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(intent, requestCode);
    }

    public void turnOffBluetooth() {
        bluetoothAdapter.disable();
    }

    /*
    打开蓝牙可见性
     */
    public void enableVisibly(Context context) {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        context.startActivity(discoverableIntent);
    }

    /*
      是否在搜索
    */
    public boolean isDiscovering() {
        return bluetoothAdapter.isDiscovering();
    }

    /*
     停止搜索
   */
    public void cancelDiscovery() {
        if (isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
    }

    /*
    查找设备
     */
    public void findDevice(Activity activity) {
        assert (bluetoothAdapter != null);
        if (!isDiscovering()) {
            if (Build.VERSION.SDK_INT >= 6.0) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        BluetoothParams.MY_PERMISSION_REQUEST_CONSTANT);
            }
            bluetoothAdapter.startDiscovery();
        }

    }

    /**
     *     * 获取远程设备
     * <p>
     *     *
     * <p>
     *     * @param address
     * <p>
     *     * @return
     * <p>
     *    
     */

    public BluetoothDevice getRemoteDevice(String address) {

        return bluetoothAdapter.getRemoteDevice(address);

    }

    /**
     *     * 获取客户端设备
     * <p>
     *     * @param name
     * <p>
     *     * @param uuid
     * <p>
     *     * @return
     * <p>
     *    
     */

    public BluetoothServerSocket listenUsingRfcommWithServiceRecord(String name, UUID uuid) {

        try {

            return bluetoothAdapter.listenUsingRfcommWithServiceRecord(name, uuid);

        } catch (IOException e) {

            e.printStackTrace();

        }

        return null;

    }


    /*获取绑定设备

     */
    public List<BluetoothDevice> getBondedDeviceList() {
        return new ArrayList<>(bluetoothAdapter.getBondedDevices());
    }
}
