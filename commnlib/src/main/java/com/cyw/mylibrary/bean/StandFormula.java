package com.cyw.mylibrary.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * author： cyw
 */
public class StandFormula implements Serializable {

    /**
     * COLORFORMULA : {"FORMULA":{"UNIT":"g(罐单位)","COLORGROUP":"颜色分组 ","DESCRIPTION":"颜色描述 ","PRODUCT":"产品","VERSION":"2017-12-1","STANDARDCOLOR":"标准色号 ","VARIANTCODE":"差异色","MANUFACTURER":"制造商","INNERCOLORCODE":"内部色号 ","BARRELS":"1(桶)","BRAND":"品牌"},"FORMULAITEMS":{"FORMULAITEM":[{"AMOUNT":"43.2","COLORANT":"B"},{"AMOUNT":"4.4","COLORANT":"C"},{"AMOUNT":"2.2","COLORANT":"D"},{"AMOUNT":"0.08","COLORANT":"E"}]}}
     */

    @SerializedName("COLORFORMULA")
    private ColorformulaBean colorformula;

    public static class ColorformulaBean implements Serializable {
        /**
         * FORMULA : {"UNIT":"g(罐单位)","COLORGROUP":"颜色分组 ","DESCRIPTION":"颜色描述 ","PRODUCT":"产品","VERSION":"2017-12-1","STANDARDCOLOR":"标准色号 ","VARIANTCODE":"差异色","MANUFACTURER":"制造商","INNERCOLORCODE":"内部色号 ","BARRELS":"1(桶)","BRAND":"品牌"}
         * FORMULAITEMS : {"FORMULAITEM":[{"AMOUNT":"43.2","COLORANT":"B"},{"AMOUNT":"4.4","COLORANT":"C"},{"AMOUNT":"2.2","COLORANT":"D"},{"AMOUNT":"0.08","COLORANT":"E"}]}
         */

        @SerializedName("FORMULA")
        private FormulaBean formula;
        @SerializedName("FORMULAITEMS")
        private FormulaitemsBean formulaitems;

        public static class FormulaBean implements Serializable {
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

            @SerializedName("UNIT")
            private String unit;
            @SerializedName("COLORGROUP")
            private String colorgroup;
            @SerializedName("DESCRIPTION")
            private String description;
            @SerializedName("PRODUCT")
            private String product;
            @SerializedName("VERSION")
            private String version;
            @SerializedName("STANDARDCOLOR")
            private String standardcolor;
            @SerializedName("VARIANTCODE")
            private String variantcode;
            @SerializedName("MANUFACTURER")
            private String manufacturer;
            @SerializedName("INNERCOLORCODE")
            private String innercolorcode;
            @SerializedName("BARRELS")
            private String barrels;
            @SerializedName("BRAND")
            private String brand;
        }

        public static class FormulaitemsBean implements Serializable {
            @SerializedName("FORMULAITEM")
            private List<FormulaitemBean> formulaitem;

            public static class FormulaitemBean implements Serializable {
                /**
                 * AMOUNT : 43.2
                 * COLORANT : B
                 */

                @SerializedName("AMOUNT")
                private String amount;
                @SerializedName("COLORANT")
                private String colorant;
            }
        }
    }
}
