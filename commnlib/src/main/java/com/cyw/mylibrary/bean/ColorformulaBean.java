package com.cyw.mylibrary.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * author： cyw
 */
public class ColorformulaBean implements Serializable {
    //    private OuterBean outerBean;
//    private FormulaitemsBean formulaitemsBean;
//
//
//    public OuterBean getOuterBean() {
//        return outerBean;
//    }
//
//    public void setOuterBean(OuterBean outerBean) {
//        this.outerBean = outerBean;
//    }
//
//    public FormulaitemsBean getFormulaitemsBean() {
//        return formulaitemsBean;
//    }
//
//    public void setFormulaitemsBean(FormulaitemsBean formulaitemsBean) {
//        this.formulaitemsBean = formulaitemsBean;
//    }
    @SerializedName("FORMULA")
    private FormulaBean formula;
    @SerializedName("FORMULAITEMS")
    private FormulaitemsBean formulaitems;
   // private BaseFormulaItems formulaitems;

    public FormulaBean getFormula() {
        return formula;
    }

    public void setFormula(FormulaBean formula) {
        this.formula = formula;
    }

    public FormulaitemsBean getFormulaitems() {
        return formulaitems;
    }

    public void setFormulaitems(FormulaitemsBean formulaitems) {
        this.formulaitems = formulaitems;
    }
}
