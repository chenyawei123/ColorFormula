package com.cyw.mylibrary.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * author： cyw
 * 阀门参数设置
 */
@DatabaseTable(tableName = "bucket_param")
public class FmParamsBean implements Serializable {
    private String materialName;//原料量
    @DatabaseField(columnName = "id",generatedId = true)
    private int id;
    @DatabaseField(columnName = "bucket_name")
    private String barrelNo;//桶号
    @DatabaseField(columnName = "high_thred")
    private String highThreshold;//高速阈值
    @DatabaseField(columnName = "mid_thred")
    private String lowThreshold;//中速阈值
    @DatabaseField(columnName = "low_thred")
    private String closeThreshold;//低速阈值
    @DatabaseField(columnName = "guan_pre")
    private String cannedaPrecision;//小数double
    @DatabaseField(columnName = "shake_times")
    private int ddCount;//抖动次数
    @DatabaseField(columnName = "open_big")
    private String openBig;
    @DatabaseField(columnName = "close_big")
    private String closeBig;
    @DatabaseField(columnName = "open_small")
    private String openSmall;
    @DatabaseField(columnName = "close_small")
    private String closeSmall;
    @DatabaseField(columnName = "bucket_type")
    private String bucket_type;

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getBarrelNo() {
        return barrelNo;
    }

    public void setBarrelNo(String barrelNo) {
        this.barrelNo = barrelNo;
    }

    public String getHighThreshold() {
        return highThreshold;
    }

    public void setHighThreshold(String highThreshold) {
        this.highThreshold = highThreshold;
    }

    public String getLowThreshold() {
        return lowThreshold;
    }

    public void setLowThreshold(String lowThreshold) {
        this.lowThreshold = lowThreshold;
    }

    public String getCloseThreshold() {
        return closeThreshold;
    }

    public void setCloseThreshold(String closeThreshold) {
        this.closeThreshold = closeThreshold;
    }

    public String getCannedaPrecision() {
        return cannedaPrecision;
    }

    public void setCannedaPrecision(String cannedaPrecision) {
        this.cannedaPrecision = cannedaPrecision;
    }

    public int getDdCount() {
        return ddCount;
    }

    public void setDdCount(int ddCount) {
        this.ddCount = ddCount;
    }

    public String getOpenBig() {
        return openBig;
    }

    public void setOpenBig(String openBig) {
        this.openBig = openBig;
    }

    public String getCloseBig() {
        return closeBig;
    }

    public void setCloseBig(String closeBig) {
        this.closeBig = closeBig;
    }

    public String getOpenSmall() {
        return openSmall;
    }

    public void setOpenSmall(String openSmall) {
        this.openSmall = openSmall;
    }

    public String getCloseSmall() {
        return closeSmall;
    }

    public void setCloseSmall(String closeSmall) {
        this.closeSmall = closeSmall;
    }

    public String getBucket_type() {
        return bucket_type;
    }

    public void setBucket_type(String bucket_type) {
        this.bucket_type = bucket_type;
    }
}
