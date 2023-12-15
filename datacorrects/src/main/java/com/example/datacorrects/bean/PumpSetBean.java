package com.example.datacorrects.bean;

import java.io.Serializable;

/**
 *
 *
 */

public class PumpSetBean implements Serializable {

    /**
     * materials_type : 液料
     * max_step_count_down : 0
     * max_step_count_up : 12000
     * model : 1Y泵
     * name : 1Y泵
     * pump_type : 单向泵
     * single_amount_down : 0
     * single_amount_up : 0.002454
     */

    private String materials_type;
    private int max_step_count_down;
    private int max_step_count_up;
    private String model;
    private String name;
    private String pump_type;
    private String single_amount_down;
    private String single_amount_up;
    private int id;
    private String topUnit;
    private String bottomUnit;

    public String getTopUnit() {
        return topUnit;
    }

    public void setTopUnit(String topUnit) {
        this.topUnit = topUnit;
    }

    public String getBottomUnit() {
        return bottomUnit;
    }

    public void setBottomUnit(String bottomUnit) {
        this.bottomUnit = bottomUnit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaterials_type() {
        return materials_type;
    }

    public void setMaterials_type(String materials_type) {
        this.materials_type = materials_type;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPump_type() {
        return pump_type;
    }

    public void setPump_type(String pump_type) {
        this.pump_type = pump_type;
    }

    public String getSingle_amount_down() {
        return single_amount_down;
    }

    public void setSingle_amount_down(String single_amount_down) {
        this.single_amount_down = single_amount_down;
    }

    public String getSingle_amount_up() {
        return single_amount_up;
    }

    public void setSingle_amount_up(String single_amount_up) {
        this.single_amount_up = single_amount_up;
    }
}
