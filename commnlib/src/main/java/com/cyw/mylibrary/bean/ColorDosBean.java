package com.cyw.mylibrary.bean;

import java.io.Serializable;

/**
 * author： cyw
 */
public class ColorDosBean implements Serializable {
    private String barrelNo;
    private int position;
    private String colorDos;
    private String colorName;
    private String innerCode;
    private double cannedPrecision;

    public String getBarrelNo() {
        return barrelNo;
    }

    public void setBarrelNo(String barrelNo) {
        this.barrelNo = barrelNo;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getColorDos() {
        return colorDos;
    }

    public void setColorDos(String colorDos) {
        this.colorDos = colorDos;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public String getInnerCode() {
        return innerCode;
    }

    public void setInnerCode(String innerCode) {
        this.innerCode = innerCode;
    }

    public double getCannedPrecision() {
        return cannedPrecision;
    }

    public void setCannedPrecision(double cannedPrecision) {
        this.cannedPrecision = cannedPrecision;
    }
}
