package com.cyw.mylibrary.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * author： cyw
 */
@DatabaseTable(tableName = "cannedresult")
public class CannedBean implements Serializable {
    @DatabaseField(columnName = "id")
    private long id;
    @DatabaseField(columnName = "colorNo")
    private String colorNo;
    @DatabaseField(columnName = "barrelNo")
    private String barrelNo;
    @DatabaseField(columnName = "cannedTargetValue")
    private String cannedTargetValue;
    @DatabaseField(columnName = "cannedRealValue")
    private String cannedRealValue;
    @DatabaseField(columnName = "formulaName")
    private String formulaName;
    @DatabaseField(columnName = "time")
    private String time;


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

    public String getBarrelNo() {
        return barrelNo;
    }

    public void setBarrelNo(String barrelNo) {
        this.barrelNo = barrelNo;
    }

    public String getCannedTargetValue() {
        return cannedTargetValue;
    }

    public void setCannedTargetValue(String cannedTargetValue) {
        this.cannedTargetValue = cannedTargetValue;
    }

    public String getCannedRealValue() {
        return cannedRealValue;
    }

    public void setCannedRealValue(String cannedRealValue) {
        this.cannedRealValue = cannedRealValue;
    }

    public String getFormulaName() {
        return formulaName;
    }

    public void setFormulaName(String formulaName) {
        this.formulaName = formulaName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
