package com.cyw.mylibrary.bean;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * author： cyw
 */
@DatabaseTable(tableName = "colorbean")
public class ColorBean implements Serializable {
    @DatabaseField(columnName = "colorBeanId", generatedId = true)
    private int colorBeanId;
    @DatabaseField(columnName = "colorNo")
    @SerializedName("COLORANT")
    private String colorNo;//编码
    //private String coloName;//色浆名称
    @DatabaseField(columnName = "colorDos")
    @SerializedName("AMOUNT")
    private String colorDos;//用量
    //    private String color;//颜色
    @DatabaseField(columnName = "realDos")
    private String realDos;//实际用量
    @DatabaseField(columnName = "id")
    private long id;
    @DatabaseField(columnName = "type")
    private int type;
    private String barrelNo;

    public String getBarrelNo() {
        return barrelNo;
    }

    public void setBarrelNo(String barrelNo) {
        this.barrelNo = barrelNo;
    }

    public int getColorBeanId() {
        return colorBeanId;
    }

    public void setColorBeanId(int colorBeanId) {
        this.colorBeanId = colorBeanId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getColorNo() {
        return colorNo;
    }

    public void setColorNo(String colorNo) {
        this.colorNo = colorNo;
    }

//    public String getColoName() {
//        return coloName;
//    }
//
//    public void setColoName(String coloName) {
//        this.coloName = coloName;
//    }

    public String getColorDos() {
        return colorDos;
    }

    public void setColorDos(String colorDos) {
        this.colorDos = colorDos;
    }

    public String getRealDos() {
        return realDos;
    }

    public void setRealDos(String realDos) {
        this.realDos = realDos;
    }
    //
//    public String getColor() {
//        return color;
//    }
//
//    public void setColor(String color) {
//        this.color = color;
//    }
//
//    public String getRealDos() {
//        return realDos;
//    }
//
//    public void setRealDos(String realDos) {
//        this.realDos = realDos;
//    }

}
