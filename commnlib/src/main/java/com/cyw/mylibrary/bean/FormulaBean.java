package com.cyw.mylibrary.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FormulaBean implements Serializable {
    /**
     * UNIT : g(罐单位)
     * COLORGROUP : 颜色分组
     * DESCRIPTION : 颜色描述
     * PRODUCT : 产品
     * VERSION : 2017-12-1
     * STANDARDCOLOR : 标准色号
     * VARIANTCODE : 差异色
     * MANUFACTURER : 制造商
     * INNERCOLORCODE : 内部色号
     * BARRELS : 1(桶)
     * BRAND : 品牌
     */
  //  @DatabaseField(columnName = "")
    private long id;
    //@DatabaseField(columnName = "")
    @SerializedName("UNIT")
    private String unit;
   // @DatabaseField(columnName = "")
    @SerializedName("COLORGROUP")
    private String colorgroup;
   // @DatabaseField(columnName = "")
    @SerializedName("DESCRIPTION")
    private String description;
   // @DatabaseField(columnName = "product")
    @SerializedName("PRODUCT")
    private String product;
    //@DatabaseField(columnName = "version")
    @SerializedName("VERSION")
    private String version;
   // @DatabaseField(columnName = "standardcolor")
    @SerializedName("STANDARDCOLOR")
    private String standardcolor;
   // @DatabaseField(columnName = "variantcode")
    @SerializedName("VARIANTCODE")
    private String variantcode;
   // @DatabaseField(columnName = "manufacturer")
    @SerializedName("MANUFACTURER")
    private String manufacturer;
   // @DatabaseField(columnName = "innercolorcode")
    @SerializedName("INNERCOLORCODE")
    private String innercolorcode;
    //@DatabaseField(columnName = "barrels")
    @SerializedName("BARRELS")
    private String barrels;
    //@DatabaseField(columnName = "brand")
    @SerializedName("BRAND")
    private String brand;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getColorgroup() {
        return colorgroup;
    }

    public void setColorgroup(String colorgroup) {
        this.colorgroup = colorgroup;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getStandardcolor() {
        return standardcolor;
    }

    public void setStandardcolor(String standardcolor) {
        this.standardcolor = standardcolor;
    }

    public String getVariantcode() {
        return variantcode;
    }

    public void setVariantcode(String variantcode) {
        this.variantcode = variantcode;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getInnercolorcode() {
        return innercolorcode;
    }

    public void setInnercolorcode(String innercolorcode) {
        this.innercolorcode = innercolorcode;
    }

    public String getBarrels() {
        return barrels;
    }

    public void setBarrels(String barrels) {
        this.barrels = barrels;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}