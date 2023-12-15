package com.cyw.mylibrary.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 *
 *
 */

public class SaveBean implements Serializable {

    /**
     * address : 1
     * alarm_value : 300
     * data : {"1":["0,0","175,0.754333","351,1.355","526,1.71867","702,2.18233","877,2.703","1053,3.25167","1404,4.39867","1754,5.33333","2105,6.24333","2456,7.41433","2807,8.5075","3509,10.386","4211,12.314","5263,15.3905","7018,20.388","10526,30.431","99999999,286289"]}
     * density : 1.4
     * down_speed : 9
     * is_enable : true
     * materials_name : 味精
     * materials_type : 粉料
     * max_capacity : 600
     * max_step_count_down : 0
     * max_step_count_up : 99999999
     * model : 粉料泵
     * pump_setting : 味精泵
     * pump_type : 单向泵
     * up_speed : 9
     */
    private int id;
    private int address;
    private int alarm_value;
    private DataBean data;
    private double density;
    private int down_speed;
    private boolean is_enable;
    private String materials_name;
    private String materials_type;
    private int max_capacity;//桶容量
    private int max_step_count_down;
    private int max_step_count_up;
    private String model;
    private String pump_setting;
    private String pump_type;
    private int up_speed;
    private double targetValue;
    private double testValue;
    private double difference;
    private double testOffset;
    private double percent;
    private int singleAmountUp;
    private int singleAmountDown;


    public SaveBean(){

    }
    public SaveBean(int address, int alarm_value, DataBean data, double density, int down_speed, boolean is_enable, String materials_name, String materials_type, int max_capacity, int max_step_count_down, int max_step_count_up, String model, String pump_setting, String pump_type, int up_speed) {
        this.address = address;
        this.alarm_value = alarm_value;
        this.data = data;
        this.density = density;
        this.down_speed = down_speed;
        this.is_enable = is_enable;
        this.materials_name = materials_name;
        this.materials_type = materials_type;
        this.max_capacity = max_capacity;
        this.max_step_count_down = max_step_count_down;
        this.max_step_count_up = max_step_count_up;
        this.model = model;
        this.pump_setting = pump_setting;
        this.pump_type = pump_type;
        this.up_speed = up_speed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public int getAlarm_value() {
        return alarm_value;
    }

    public void setAlarm_value(int alarm_value) {
        this.alarm_value = alarm_value;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public double getDensity() {
        return density;
    }

    public void setDensity(double density) {
        this.density = density;
    }

    public int getDown_speed() {
        return down_speed;
    }

    public void setDown_speed(int down_speed) {
        this.down_speed = down_speed;
    }

    public boolean isIs_enable() {
        return is_enable;
    }

    public void setIs_enable(boolean is_enable) {
        this.is_enable = is_enable;
    }

    public String getMaterials_name() {
        return materials_name;
    }

    public void setMaterials_name(String materials_name) {
        this.materials_name = materials_name;
    }

    public String getMaterials_type() {
        return materials_type;
    }

    public void setMaterials_type(String materials_type) {
        this.materials_type = materials_type;
    }

    public int getMax_capacity() {
        return max_capacity;
    }

    public void setMax_capacity(int max_capacity) {
        this.max_capacity = max_capacity;
    }

    public int getMax_step_count_down() {
        return max_step_count_down;
    }

    public void setMax_step_count_down(int max_step_count_down) {
        this.max_step_count_down = max_step_count_down;
    }

    public int getMax_step_count_up() {
        return max_step_count_up;
    }

    public void setMax_step_count_up(int max_step_count_up) {
        this.max_step_count_up = max_step_count_up;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPump_setting() {
        return pump_setting;
    }

    public void setPump_setting(String pump_setting) {
        this.pump_setting = pump_setting;
    }

    public String getPump_type() {
        return pump_type;
    }

    public void setPump_type(String pump_type) {
        this.pump_type = pump_type;
    }

    public int getUp_speed() {
        return up_speed;
    }

    public void setUp_speed(int up_speed) {
        this.up_speed = up_speed;
    }

    public double getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(double targetValue) {
        this.targetValue = targetValue;
    }

    public double getTestValue() {
        return testValue;
    }

    public void setTestValue(double testValue) {
        this.testValue = testValue;
    }

    public double getDifference() {
        return difference;
    }

    public void setDifference(double difference) {
        this.difference = difference;
    }

    public double getTestOffset() {
        return testOffset;
    }

    public void setTestOffset(double testOffset) {
        this.testOffset = testOffset;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public int getSingleAmountUp() {
        return singleAmountUp;
    }

    public void setSingleAmountUp(int singleAmountUp) {
        this.singleAmountUp = singleAmountUp;
    }

    public int getSingleAmountDown() {
        return singleAmountDown;
    }

    public void setSingleAmountDown(int singleAmountDown) {
        this.singleAmountDown = singleAmountDown;
    }

    public static class DataBean implements Serializable {
        @SerializedName("1")
        private List<SaveDataBean> _$1;
        private List<SaveDataBean> bottomSaveDataBeans;

        public List<SaveDataBean> getBottomSaveDataBeans() {
            return bottomSaveDataBeans;
        }

        public void setBottomSaveDataBeans(List<SaveDataBean> bottomSaveDataBeans) {
            this.bottomSaveDataBeans = bottomSaveDataBeans;
        }

        public List<SaveDataBean> get_$1() {
            return _$1;
        }

        public void set_$1(List<SaveDataBean> _$1) {
            this._$1 = _$1;
        }
    }
    public static class SaveDataBean implements Serializable{
        private int gStep;
        private double testAverage;
        private double targetValue;

        public int getgStep() {
            return gStep;
        }

        public void setgStep(int gStep) {
            this.gStep = gStep;
        }

        public double getTestAverage() {
            return testAverage;
        }

        public void setTestAverage(double testAverage) {
            this.testAverage = testAverage;
        }

        public double getTargetValue() {
            return targetValue;
        }

        public void setTargetValue(double targetValue) {
            this.targetValue = targetValue;
        }
    }
}
