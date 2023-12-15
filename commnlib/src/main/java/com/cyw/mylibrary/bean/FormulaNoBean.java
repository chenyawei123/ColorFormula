package com.cyw.mylibrary.bean;

import java.io.Serializable;

/**
 * author： cyw
 */
public class FormulaNoBean implements Serializable {
    private String formulaNo;
    private String formulaName;
    private String color;

    public String getFormulaNo() {
        return formulaNo;
    }

    public void setFormulaNo(String formulaNo) {
        this.formulaNo = formulaNo;
    }

    public String getFormulaName() {
        return formulaName;
    }

    public void setFormulaName(String formulaName) {
        this.formulaName = formulaName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
