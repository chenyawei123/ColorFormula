package com.example.datacorrects.bean;

import java.io.Serializable;
import java.util.List;

/**
 *
 */

public class CorrectPlanBean implements Serializable {

    /**
     * method_name : 1Y校正
     * data : [{"calibration_point":0.5,"change_value":20,"offset_value":15,"repeat_times":5},{"calibration_point":1,"change_value":10,"offset_value":8,"repeat_times":5},{"calibration_point":3,"change_value":4,"offset_value":5,"repeat_times":5},{"calibration_point":5,"change_value":2,"offset_value":3,"repeat_times":5},{"calibration_point":10,"change_value":1.5,"offset_value":2,"repeat_times":3},{"calibration_point":27,"change_value":1,"offset_value":1.5,"repeat_times":3}]
     */

    private String method_name;
    private List<DataBean> data ;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMethod_name() {
        return method_name;
    }

    public void setMethod_name(String method_name) {
        this.method_name = method_name;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * calibration_point : 0.5
         * change_value : 20
         * offset_value : 15
         * repeat_times : 5
         */

        private double calibration_point;
        private double change_value;
        private double offset_value;
        private int repeat_times;
        private int row;

        public int getRow() {
            return row;
        }

        public void setRow(int row) {
            this.row = row;
        }

        public double getCalibration_point() {
            return calibration_point;
        }

        public void setCalibration_point(double calibration_point) {
            this.calibration_point = calibration_point;
        }

        public double getChange_value() {
            return change_value;
        }

        public void setChange_value(double change_value) {
            this.change_value = change_value;
        }

        public double getOffset_value() {
            return offset_value;
        }

        public void setOffset_value(double offset_value) {
            this.offset_value = offset_value;
        }

        public int getRepeat_times() {
            return repeat_times;
        }

        public void setRepeat_times(int repeat_times) {
            this.repeat_times = repeat_times;
        }

//        //按照单个属性（double数据类型）排序
//        @Override
//        public int compareTo(DataBean dataBean) {
//            return dataBean.getCalibration_point() - this.getCalibration_point()> 0 ? 1 : ((this.getCalibration_point() == dataBean.getCalibration_point()) ? 0 : -1);   //降序：返回值为1 或-1 升序改变变量位置即可
//        }
    }
}
