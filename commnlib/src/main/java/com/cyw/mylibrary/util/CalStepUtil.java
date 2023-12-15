package com.cyw.mylibrary.util;

import com.cyw.mylibrary.bean.SaveBean;

import java.util.List;

/**
 * author： cyw
 */
public class CalStepUtil {
    public static int calStep(double targetValue, List<SaveBean.SaveDataBean> saveDatabeans){
        double steps = 0d;
        for (int i = 0; i < saveDatabeans.size(); i++) {
            SaveBean.SaveDataBean data = saveDatabeans.get(i);
            double capacity = data.getTestAverage();
            if (saveDatabeans.size() == 1) {
                int step = data.getgStep();
                if (capacity > 0) {
                    steps = step * targetValue / capacity;
                }
                break;
            }
            if (i == 0 && targetValue <= capacity) {
                SaveBean.SaveDataBean data0 = saveDatabeans.get(0);
                SaveBean.SaveDataBean data1 = saveDatabeans.get(1);
//                int x1 = data0.getgStep();
//                double y1 = data0.getTestAverage();
                int x1 = 0;
                double y1 = 0;
                int x2 = data0.getgStep();
                double y2 = data0.getTestAverage();
                steps = x1 + (targetValue - y1) * (x2 - x1) / (y2 - y1);
                break;
            }
            if (i == saveDatabeans.size() - 1 && targetValue >= capacity) {
                SaveBean.SaveDataBean data2 = saveDatabeans.get(saveDatabeans.size() - 2);
                SaveBean.SaveDataBean data3 = saveDatabeans.get(saveDatabeans.size() - 1);
                int x1 = data2.getgStep();
                double y1 = data2.getTestAverage();
                int x2 = data3.getgStep();
                double y2 = data3.getTestAverage();
                steps = x1 + (targetValue - y1) * (x2 - x1) / (y2 - y1);
                break;
            }
            if (targetValue < capacity) {
                SaveBean.SaveDataBean data4 = saveDatabeans.get(i - 1);
                SaveBean.SaveDataBean data5 = saveDatabeans.get(i);
                int x1 = data4.getgStep();
                double y1 = data4.getTestAverage();
                int x2 = data5.getgStep();
                double y2 = data5.getTestAverage();
                steps = x1 + (targetValue - y1) * (x2 - x1) / (y2 - y1);
                break;
            }

        }
        return (int) steps;
    }
    public static  double calTestValue(int maxStep,List<SaveBean.SaveDataBean> saveDatabeans) {
        double maxCapacity = 0d;
        for (int i = 0; i < saveDatabeans.size(); i++) {
            SaveBean.SaveDataBean data = saveDatabeans.get(i);
            double capacity = data.getTestAverage();
            int step = data.getgStep();
            if (saveDatabeans.size() == 1) {
                if (step > 0) {
                    maxCapacity = maxStep * capacity / step;
                }
                break;
            }
            if (i == 0 && maxStep <= step) {
                SaveBean.SaveDataBean data0 = saveDatabeans.get(0);
                SaveBean.SaveDataBean data1 = saveDatabeans.get(1);
                int x1 = data0.getgStep();
                double y1 = data0.getTestAverage();
                int x2 = data1.getgStep();
                double y2 = data1.getTestAverage();
                //steps = x1 + (targetValue - y1) * (x2 - x1) / (y2 - y1);
                maxCapacity = (maxStep - x1) * (y2 - y1) / (x2 - x1) + y1;
                break;
            }
            if (i == saveDatabeans.size() - 1 && maxStep >= step) {
                SaveBean.SaveDataBean data2 = saveDatabeans.get(saveDatabeans.size() - 2);
                SaveBean.SaveDataBean data3 = saveDatabeans.get(saveDatabeans.size() - 1);
                int x1 = data2.getgStep();
                double y1 = data2.getTestAverage();
                int x2 = data3.getgStep();
                double y2 = data3.getTestAverage();
                //steps = x1 + (targetValue - y1) * (x2 - x1) / (y2 - y1);
                maxCapacity = (maxStep - x1) * (y2 - y1) / (x2 - x1) + y1;
                break;
            }
           if (maxStep < step) {
                SaveBean.SaveDataBean data4 = saveDatabeans.get(i - 1);
                SaveBean.SaveDataBean data5 = saveDatabeans.get(i);
                int x1 = data4.getgStep();
                double y1 = data4.getTestAverage();
                int x2 = data5.getgStep();
                double y2 = data5.getTestAverage();
                //steps = x1 + (targetValue - y1) * (x2 - x1) / (y2 - y1);
                maxCapacity = maxStep * (y2 - y1) - x1 / (x2 - x1) + y1;
                break;
            }

        }
        return maxCapacity;
    }

}
