package com.example.datacorrects.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *
 *
 */

public class JsonPointBean {

    @SerializedName("1Y校正")
    private List<_$1Y校正Bean> _$1Y校正;
    @SerializedName("3Y校正")
    private List<_$3Y校正Bean> _$3Y校正;
    @SerializedName("5L泵校正")
    private List<_$5L泵校正Bean> _$5L泵校正;
    private List<粉料校Bean> 粉料校;
    private List<粉料校正Bean> 粉料校正;

    public List<_$1Y校正Bean> get_$1Y校正() {
        return _$1Y校正;
    }

    public void set_$1Y校正(List<_$1Y校正Bean> _$1Y校正) {
        this._$1Y校正 = _$1Y校正;
    }

    public List<_$3Y校正Bean> get_$3Y校正() {
        return _$3Y校正;
    }

    public void set_$3Y校正(List<_$3Y校正Bean> _$3Y校正) {
        this._$3Y校正 = _$3Y校正;
    }

    public List<_$5L泵校正Bean> get_$5L泵校正() {
        return _$5L泵校正;
    }

    public void set_$5L泵校正(List<_$5L泵校正Bean> _$5L泵校正) {
        this._$5L泵校正 = _$5L泵校正;
    }

    public List<粉料校Bean> get粉料校() {
        return 粉料校;
    }

    public void set粉料校(List<粉料校Bean> 粉料校) {
        this.粉料校 = 粉料校;
    }

    public List<粉料校正Bean> get粉料校正() {
        return 粉料校正;
    }

    public void set粉料校正(List<粉料校正Bean> 粉料校正) {
        this.粉料校正 = 粉料校正;
    }

    public static class _$1Y校正Bean {
        /**
         * calibration_point : 0.5
         * change_value : 20
         * offset_value : 15
         * repeat_times : 5
         */

        private double calibration_point;
        private int change_value;
        private int offset_value;
        private int repeat_times;

        public double getCalibration_point() {
            return calibration_point;
        }

        public void setCalibration_point(double calibration_point) {
            this.calibration_point = calibration_point;
        }

        public int getChange_value() {
            return change_value;
        }

        public void setChange_value(int change_value) {
            this.change_value = change_value;
        }

        public int getOffset_value() {
            return offset_value;
        }

        public void setOffset_value(int offset_value) {
            this.offset_value = offset_value;
        }

        public int getRepeat_times() {
            return repeat_times;
        }

        public void setRepeat_times(int repeat_times) {
            this.repeat_times = repeat_times;
        }
    }

    public static class _$3Y校正Bean {
        /**
         * calibration_point : 5
         * change_value : 4
         * offset_value : 5
         * repeat_times : 5
         */

        private int calibration_point;
        private int change_value;
        private int offset_value;
        private int repeat_times;

        public int getCalibration_point() {
            return calibration_point;
        }

        public void setCalibration_point(int calibration_point) {
            this.calibration_point = calibration_point;
        }

        public int getChange_value() {
            return change_value;
        }

        public void setChange_value(int change_value) {
            this.change_value = change_value;
        }

        public int getOffset_value() {
            return offset_value;
        }

        public void setOffset_value(int offset_value) {
            this.offset_value = offset_value;
        }

        public int getRepeat_times() {
            return repeat_times;
        }

        public void setRepeat_times(int repeat_times) {
            this.repeat_times = repeat_times;
        }
    }

    public static class _$5L泵校正Bean {
        /**
         * calibration_point : 5
         * change_value : 5
         * offset_value : 10
         * repeat_times : 5
         */

        private int calibration_point;
        private int change_value;
        private int offset_value;
        private int repeat_times;

        public int getCalibration_point() {
            return calibration_point;
        }

        public void setCalibration_point(int calibration_point) {
            this.calibration_point = calibration_point;
        }

        public int getChange_value() {
            return change_value;
        }

        public void setChange_value(int change_value) {
            this.change_value = change_value;
        }

        public int getOffset_value() {
            return offset_value;
        }

        public void setOffset_value(int offset_value) {
            this.offset_value = offset_value;
        }

        public int getRepeat_times() {
            return repeat_times;
        }

        public void setRepeat_times(int repeat_times) {
            this.repeat_times = repeat_times;
        }
    }

    public static class 粉料校Bean {
        /**
         * calibration_point : 0.5
         * change_value : 35
         * offset_value : 30
         * repeat_times : 3
         */

        private double calibration_point;
        private int change_value;
        private int offset_value;
        private int repeat_times;

        public double getCalibration_point() {
            return calibration_point;
        }

        public void setCalibration_point(double calibration_point) {
            this.calibration_point = calibration_point;
        }

        public int getChange_value() {
            return change_value;
        }

        public void setChange_value(int change_value) {
            this.change_value = change_value;
        }

        public int getOffset_value() {
            return offset_value;
        }

        public void setOffset_value(int offset_value) {
            this.offset_value = offset_value;
        }

        public int getRepeat_times() {
            return repeat_times;
        }

        public void setRepeat_times(int repeat_times) {
            this.repeat_times = repeat_times;
        }
    }

    public static class 粉料校正Bean {
        /**
         * calibration_point : 0.5
         * change_value : 35
         * offset_value : 30
         * repeat_times : 5
         */

        private double calibration_point;
        private int change_value;
        private int offset_value;
        private int repeat_times;

        public double getCalibration_point() {
            return calibration_point;
        }

        public void setCalibration_point(double calibration_point) {
            this.calibration_point = calibration_point;
        }

        public int getChange_value() {
            return change_value;
        }

        public void setChange_value(int change_value) {
            this.change_value = change_value;
        }

        public int getOffset_value() {
            return offset_value;
        }

        public void setOffset_value(int offset_value) {
            this.offset_value = offset_value;
        }

        public int getRepeat_times() {
            return repeat_times;
        }

        public void setRepeat_times(int repeat_times) {
            this.repeat_times = repeat_times;
        }
    }
}
