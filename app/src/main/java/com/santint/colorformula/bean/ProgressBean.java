package com.santint.colorformula.bean;

import java.io.Serializable;

/**
 * author： cyw
 */
public class ProgressBean implements Serializable {
    private String barrelNo;
    private String colorName;
    private double realValue;
    private double totalValue;
    private boolean finishState;

    public String getBarrelNo() {
        return barrelNo;
    }

    public void setBarrelNo(String barrelNo) {
        this.barrelNo = barrelNo;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public double getRealValue() {
        return realValue;
    }

    public void setRealValue(double realValue) {
        this.realValue = realValue;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(double totalValue) {
        this.totalValue = totalValue;
    }

    public boolean isFinishState() {
        return finishState;
    }

    public void setFinishState(boolean finishState) {
        this.finishState = finishState;
    }
}
