package com.cyw.mylibrary.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * author： cyw
 */
@DatabaseTable(tableName = "resultdata")
public class ResultData implements Serializable {
    @DatabaseField(columnName = "time")
    private String time;
    @DatabaseField(columnName = "cmdDes")
    private String cmdDes;
    @DatabaseField(columnName = "cmdCode")
    private String cmdCode;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCmdDes() {
        return cmdDes;
    }

    public void setCmdDes(String cmdDes) {
        this.cmdDes = cmdDes;
    }

    public String getCmdCode() {
        return cmdCode;
    }

    public void setCmdCode(String cmdCode) {
        this.cmdCode = cmdCode;
    }
}
