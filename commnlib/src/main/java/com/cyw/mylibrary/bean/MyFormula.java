package com.cyw.mylibrary.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * author： cyw
 * "UNIT": "g(罐单位)",
 * 			"COLORGROUP": "颜色分组 ",
 * 			"DESCRIPTION": "颜色描述 ",
 * 			"PRODUCT": "产品",
 * 			"VERSION": "2017-12-1",
 * 			"STANDARDCOLOR": "标准色号 ",
 * 			"VARIANTCODE": "差异色",
 * 			"MANUFACTURER": "制造商",
 * 			"INNERCOLORCODE": "内部色号 ",
 * 			"BARRELS": "1(桶)",
 * 			"BRAND": "品牌"
 */
public class MyFormula implements Serializable {
    @SerializedName("COLORFORMULA")
    private ColorformulaBean colorformula;

    public ColorformulaBean getColorformula() {
        return colorformula;
    }

    public void setColorformula(ColorformulaBean colorformula) {
        this.colorformula = colorformula;
    }
}
