package com.cyw.mylibrary.bean;

import java.io.Serializable;

/**
 * author： cyw
 */
public class CorrectParamsBean implements Serializable {
    private boolean isPreOutPut =false;//是否勾选
    private int preOutPut;//预注出量
    private int preDigit;//小数位数
    private boolean isPeel;//是否去皮
    private int capacity;//罐容量
    private String scaleType;//秤型号
    private String weight;//称重多少克
    private String maxWeight;
    private String touType;//单双头

    public String getTouType() {
        return touType;
    }

    public void setTouType(String touType) {
        this.touType = touType;
    }

    public boolean isPreOutPut() {
        return isPreOutPut;
    }

    public void setPreOutPut(boolean preOutPut) {
        isPreOutPut = preOutPut;
    }

    public int getPreOutPut() {
        return preOutPut;
    }

    public void setPreOutPut(int preOutPut) {
        this.preOutPut = preOutPut;
    }

    public int getPreDigit() {
        return preDigit;
    }

    public void setPreDigit(int preDigit) {
        this.preDigit = preDigit;
    }

    public boolean isPeel() {
        return isPeel;
    }

    public void setPeel(boolean peel) {
        isPeel = peel;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getScaleType() {
        return scaleType;
    }

    public void setScaleType(String scaleType) {
        this.scaleType = scaleType;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(String maxWeight) {
        this.maxWeight = maxWeight;
    }
}
