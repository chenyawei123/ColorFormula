package com.cyw.mylibrary.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * author： cyw
 */
@DatabaseTable(tableName = "bucket_extern")
public class DDParamsBean implements Serializable {
    @DatabaseField(columnName = "id",generatedId = true)
    private int id;
    @DatabaseField(columnName = "bucket_name")
    private String barrelNo; //桶号
    @DatabaseField(columnName = "dian_num")
    private String ddTargetDos;//点动目标量
    @DatabaseField(columnName = "shake_steps")
    private String ddStep;//点动步数
    @DatabaseField(columnName = "dian_time")
    private String ddCycle;//点动周期
    @DatabaseField(columnName = "dian_front")
    private String ddBeforeStep;//抖动前步数
    @DatabaseField(columnName = "bucket_type")
    private String bucket_type;

    public String getBarrelNo() {
        return barrelNo;
    }

    public void setBarrelNo(String barrelNo) {
        this.barrelNo = barrelNo;
    }

    public String getDdTargetDos() {
        return ddTargetDos;
    }

    public void setDdTargetDos(String ddTargetDos) {
        this.ddTargetDos = ddTargetDos;
    }

    public String getDdStep() {
        return ddStep;
    }

    public void setDdStep(String ddStep) {
        this.ddStep = ddStep;
    }

    public String getDdCycle() {
        return ddCycle;
    }

    public void setDdCycle(String ddCycle) {
        this.ddCycle = ddCycle;
    }

    public String getDdBeforeStep() {
        return ddBeforeStep;
    }

    public void setDdBeforeStep(String ddBeforeStep) {
        this.ddBeforeStep = ddBeforeStep;
    }

    public String getBucket_type() {
        return bucket_type;
    }

    public void setBucket_type(String bucket_type) {
        this.bucket_type = bucket_type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
