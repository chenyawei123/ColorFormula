package com.cyw.mylibrary.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * author： cyw
 */
@DatabaseTable(tableName = "resultbreak")
public class ResultBreak {
    @DatabaseField(columnName = "time")
    private String time;
    @DatabaseField(columnName = "breakDes")
    private String breakDes;
    @DatabaseField(columnName = "breakCode")
    private String breakCode;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBreakDes() {
        return breakDes;
    }

    public void setBreakDes(String breakDes) {
        this.breakDes = breakDes;
    }

    public String getBreakCode() {
        return breakCode;
    }

    public void setBreakCode(String breakCode) {
        this.breakCode = breakCode;
    }
}
