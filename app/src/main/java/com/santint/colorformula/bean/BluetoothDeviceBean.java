package com.santint.colorformula.bean;

import java.io.Serializable;

/**
 * author： cyw
 */
public class BluetoothDeviceBean implements Serializable {
    private String name;
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
