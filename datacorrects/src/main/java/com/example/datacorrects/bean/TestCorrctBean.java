package com.example.datacorrects.bean;

/**
 * chenyawei
 */

public class TestCorrctBean {
    private String materials_name;
    private String materials_type;
    private double testValue;
    private double difference;
    private double testOffeset;
    private double targetValue;
    private double targetOffset;
    private String data;
    private String unit;
    private String pump_type;
    private int step;
    private double capacity;

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

    public String getPump_type() {
        return pump_type;
    }

    public void setPump_type(String pump_type) {
        this.pump_type = pump_type;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public double getTargetOffset() {
        return targetOffset;
    }

    public void setTargetOffset(double targetOffset) {
        this.targetOffset = targetOffset;
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

    public double getTestOffeset() {
        return testOffeset;
    }

    public void setTestOffeset(double testOffeset) {
        this.testOffeset = testOffeset;
    }
}
