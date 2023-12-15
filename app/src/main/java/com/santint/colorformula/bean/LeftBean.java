package com.santint.colorformula.bean;

import java.io.Serializable;

/**
 * author： cyw
 */
public class LeftBean implements Serializable {
    private String name;
    private boolean check;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
