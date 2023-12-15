package com.cyw.mylibrary.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * author： cyw
 */
public class FormulaitemsBean<T> implements Serializable {
    //    private List<FORMULAITEM> colorBeans;
//
//    public List<FORMULAITEM> getColorBeans() {
//        return colorBeans;
//    }
//
//    public void setColorBeans(List<FORMULAITEM> colorBeans) {
//        this.colorBeans = colorBeans;
//    }
    @SerializedName("FORMULAITEM")
    private T colorBeans;

    //    private Object colorBeans;

    public T getColorBeans() {
        return colorBeans;
    }

    public void setColorBeans(T colorBeans) {
        this.colorBeans = colorBeans;
    }
}
