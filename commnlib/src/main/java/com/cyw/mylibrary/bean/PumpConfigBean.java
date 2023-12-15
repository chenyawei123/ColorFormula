package com.cyw.mylibrary.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 *
 */
@DatabaseTable(tableName = "bucket_info")
public class PumpConfigBean implements Serializable {

    /**
     * address : 2
     * alarm_value : 400
     * density : 0.99
     * down_speed : 2
     * is_enable : true
     * materials_name : 料酒
     * max_capacity : 1200
     * pump_setting : 1Y泵
     * up_speed : 2
     */
    @DatabaseField(columnName = "id",generatedId = true)
    private int id;
    @DatabaseField(columnName = "bucket_name")
    private String address;
    @DatabaseField(columnName = "color_name")
    private String materials_name;
    @DatabaseField(columnName = "high_speed")
    private String highSpeed;
    @DatabaseField(columnName = "mid_speed")
    private String midSpeed;
    @DatabaseField(columnName = "low_speed")
    private String lowSpeed;
    @DatabaseField(columnName = "dian_speed")
    private String ddSpeed;
    @DatabaseField(columnName = "launch_speed")
    private String startSpeed;
    @DatabaseField(columnName = "launch_step")
    private String startStep;
    @DatabaseField(columnName = "warn_rang")
    private String alarm_value;
    @DatabaseField(columnName = "bucket_rang")
    private String max_capacity;
    // private String weight;
    // private int step;
    @DatabaseField(columnName = "curr_free")
    private double curValue;
    @DatabaseField(columnName = "bei_shu")
    private String multiple;
    @DatabaseField(columnName = "bucket_type")
    private String bucket_type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMaterials_name() {
        return materials_name;
    }

    public void setMaterials_name(String materials_name) {
        this.materials_name = materials_name;
    }

    public String getHighSpeed() {
        return highSpeed;
    }

    public void setHighSpeed(String highSpeed) {
        this.highSpeed = highSpeed;
    }

    public String getMidSpeed() {
        return midSpeed;
    }

    public void setMidSpeed(String midSpeed) {
        this.midSpeed = midSpeed;
    }

    public String getLowSpeed() {
        return lowSpeed;
    }

    public void setLowSpeed(String lowSpeed) {
        this.lowSpeed = lowSpeed;
    }

    public String getDdSpeed() {
        return ddSpeed;
    }

    public void setDdSpeed(String ddSpeed) {
        this.ddSpeed = ddSpeed;
    }

    public String getStartSpeed() {
        return startSpeed;
    }

    public void setStartSpeed(String startSpeed) {
        this.startSpeed = startSpeed;
    }

    public String getStartStep() {
        return startStep;
    }

    public void setStartStep(String startStep) {
        this.startStep = startStep;
    }

    public String getAlarm_value() {
        return alarm_value;
    }

    public void setAlarm_value(String alarm_value) {
        this.alarm_value = alarm_value;
    }

    public String getMax_capacity() {
        return max_capacity;
    }

    public void setMax_capacity(String max_capacity) {
        this.max_capacity = max_capacity;
    }

    public double getCurValue() {
        return curValue;
    }

    public void setCurValue(double curValue) {
        this.curValue = curValue;
    }

    public String getMultiple() {
        return multiple;
    }

    public void setMultiple(String multiple) {
        this.multiple = multiple;
    }

    public String getBucket_type() {
        return bucket_type;
    }

    public void setBucket_type(String bucket_type) {
        this.bucket_type = bucket_type;
    }
}
