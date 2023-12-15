package com.example.datacorrects.bean;

/**
 * author： cyw
 */
public class MessageEventMainTean {
    private String dataMainTen;
    private String errorMsg;
    private int[] ints;

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getDataMainTen() {
        return dataMainTen;
    }

    public void setDataMainTen(String dataMainTen) {
        this.dataMainTen = dataMainTen;
    }

    public int[] getInts() {
        return ints;
    }

    public void setInts(int[] ints) {
        this.ints = ints;
    }
}
