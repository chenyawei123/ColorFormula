package com.net.netrequest.bean;

/**
 * Created by cyw on 2018/7/17.
 */

public class MessageEvent {

    private String data;
    private int bigCmd;
    private int littleCmd;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getBigCmd() {
        return bigCmd;
    }

    public void setBigCmd(int bigCmd) {
        this.bigCmd = bigCmd;
    }

    public int getLittleCmd() {
        return littleCmd;
    }

    public void setLittleCmd(int littleCmd) {
        this.littleCmd = littleCmd;
    }

    public MessageEvent(String data, int bigCmd, int littleCmd) {

        this.data = data;
        this.bigCmd = bigCmd;
        this.littleCmd = littleCmd;
    }
}
