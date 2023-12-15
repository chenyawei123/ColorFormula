package com.net.netrequest.bean;

import java.io.Serializable;

/**
 * Created by cyw on 2018/7/17.
 */

public class MessageEventChuanCai implements Serializable {
    byte[] result;

    public byte[] getResult() {
        return result;
    }

    public void setResult(byte[] result) {
        this.result = result;
    }
    public MessageEventChuanCai(byte[] result) {

        this.result = result;
    }
}
