package com.example.datacorrects.bean;

import java.io.Serializable;

/**
 * author： cyw
 */
public class TempSaveDataBean implements Serializable {
    private int gStep;
    private double testAverage;
    private double targetValue;
    private int repeatCount;

    public double getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(double targetValue) {
        this.targetValue = targetValue;
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
    }

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
}
