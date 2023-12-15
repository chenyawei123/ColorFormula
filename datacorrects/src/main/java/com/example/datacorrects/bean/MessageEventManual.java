package com.example.datacorrects.bean;

import java.io.Serializable;

/**
 * author： cyw
 */
public class MessageEventManual implements Serializable {
    private String dataManual;
    private int littleStep;
    private String errorMsg;

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getDataManual() {
        return dataManual;
    }

    public void setDataManual(String dataManual) {
        this.dataManual = dataManual;
    }

    public int getLittleStep() {
        return littleStep;
    }

    public void setLittleStep(int littleStep) {
        this.littleStep = littleStep;
    }
}
