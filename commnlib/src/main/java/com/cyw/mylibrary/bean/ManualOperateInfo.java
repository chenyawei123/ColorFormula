package com.cyw.mylibrary.bean;

import java.io.Serializable;

/**
 * author： cyw
 */
public class ManualOperateInfo implements Serializable {
    private String weight;
    private String pump_type;
    private int down_speed;
    private String materials_name;
    private String pump_setting;
    private int up_speed;
    private int address;
    private int step;
    private String materials_type;

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPump_type() {
        return pump_type;
    }

    public void setPump_type(String pump_type) {
        this.pump_type = pump_type;
    }

    public int getDown_speed() {
        return down_speed;
    }

    public void setDown_speed(int down_speed) {
        this.down_speed = down_speed;
    }

    public String getMaterials_name() {
        return materials_name;
    }

    public void setMaterials_name(String materials_name) {
        this.materials_name = materials_name;
    }

    public String getPump_setting() {
        return pump_setting;
    }

    public void setPump_setting(String pump_setting) {
        this.pump_setting = pump_setting;
    }

    public int getUp_speed() {
        return up_speed;
    }

    public void setUp_speed(int up_speed) {
        this.up_speed = up_speed;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public String getMaterials_type() {
        return materials_type;
    }

    public void setMaterials_type(String materials_type) {
        this.materials_type = materials_type;
    }
}
