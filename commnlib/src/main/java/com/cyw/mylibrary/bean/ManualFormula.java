package com.cyw.mylibrary.bean;

import java.io.Serializable;
import java.util.List;

/**
 * author： cyw
 */
public class ManualFormula implements Serializable {
    private String client;//客户
    private String productName;//产品
    private String formulaNo;//配方编码
    private String formulaName;//配方名称   可以删除
    private String base;// 基础漆
    private String canned; //罐规格
    private String tong;//桶
    private String color;//配方颜色
    private List<ColorBean> colorBeans;//色浆列表

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getFormulaNo() {
        return formulaNo;
    }

    public void setFormulaNo(String formulaNo) {
        this.formulaNo = formulaNo;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<ColorBean> getColorBeans() {
        return colorBeans;
    }

    public void setColorBeans(List<ColorBean> colorBeans) {
        this.colorBeans = colorBeans;
    }

    public String getFormulaName() {
        return formulaName;
    }

    public void setFormulaName(String formulaName) {
        this.formulaName = formulaName;
    }
}
