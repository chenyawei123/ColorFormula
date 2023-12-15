package com.example.datacorrects.bean;

import java.io.Serializable;

/**
 * author： cyw
 */
public class MessageEventCorrect implements Serializable {
    private String dataCorrect;
    private byte[] result;
    private String errorMsg;

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getDataCorrect() {
        return dataCorrect;
    }

    public void setDataCorrect(String dataCorrect) {
        this.dataCorrect = dataCorrect;
    }

    public byte[] getResult() {
        return result;
    }

    public void setResult(byte[] result) {
        this.result = result;
    }
}
