package com.cyw.mylibrary.services;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

import com.blankj.utilcode.util.GsonUtils;
import com.cyw.mylibrary.bean.ColorBean;
import com.cyw.mylibrary.bean.ColorformulaBean;
import com.cyw.mylibrary.bean.FormulaBean;
import com.cyw.mylibrary.bean.FormulaData;
import com.cyw.mylibrary.bean.FormulaitemsBean;
import com.cyw.mylibrary.bean.MyFormula;
import com.cyw.mylibrary.util.GlobalConstants;
import com.cyw.mylibrary.util.SaveJsonFile;
import com.cyw.mylibrary.util.SharedPreferencesHelper;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author： cyw
 */
public class MyFormulaService {
    private SparseArray<MyFormula> datas = null;
    private final Context mContext;
    private List<MyFormula> milkFormulas = null;

    public MyFormulaService(Context context) {
        datas = new SparseArray<>(100);
        milkFormulas = new ArrayList<>();
        // listToSparse();

        this.mContext = context;
        // SharedPreferencesUtil.initialData(context,"milkFormulas",milkFormulas);

    }

    public void put(MyFormula milkFormula) {
//        int size = 0;
//        if (datas != null && datas.size() > 0) {
//            size = datas.size();
//        }
//        saveBean.setMilkId(size);
//        datas.put(saveBean.getMilkId(), saveBean);
//        boolean isHave = false;
//        for(int i=0;i<datas.size();i++){
//            MilkFormula milkFormula1 = milkFormulas.get(i);
//            if(milkFormula1.get>milkFormula.getAddress()){
//                isHave = true;
//                pumpConfigBeans.add(i,saveBean);
//                break;
//            }
//        }
//        if(!isHave){
        milkFormulas.add(milkFormula);
        //SharedPreferencesUtil.saveData(mContext,"milkFormulas",milkFormulas);
        // SharedPreferencesHelper.putList(mContext,"milkFormulas",milkFormulas);
        //}
        commit();
    }

    public void update(int index, MyFormula saveBean) {
        // datas.put(saveBean.getMilkId(), saveBean);
        milkFormulas.set(index, saveBean);
        //SharedPreferencesUtil.saveData(mContext,"milkFormulas",milkFormulas);
        // SharedPreferencesHelper.putList(mContext,"milkFormulas",milkFormulas);
        commit();
    }

    public void delete(int rowIndex, MyFormula saveBean) {
        // datas.delete(saveBean.getMilkId());
        milkFormulas.remove(rowIndex);
        //SharedPreferencesUtil.saveData(mContext,"milkFormulas",milkFormulas);
        //SharedPreferencesHelper.putList(mContext,"milkFormulas",milkFormulas);
        commit();
    }

    public List<MyFormula> getAll() {
        milkFormulas = getDataFromLocal();
        if (milkFormulas == null) {
            milkFormulas = new ArrayList<>();
        }
        return milkFormulas;
    }

    public List<MyFormula> getDataFromLocal() {
        String json = "";
        // boolean isBusy = SaveJsonFile.readBusyFile("busy.xml");

        List<MyFormula> saveBeans = new ArrayList<>();
        //if (!GlobalConstants.isManual) {//从xml读取并保存到数据库
            List<String> list = firstRun();
            for (int i = 0; i < list.size(); i++) {
                json = list.get(i);
                if (json != null && !"".equals(json)) {
//                    saveBeans = GsonUtils.fromJson(json, new TypeToken<List<MyFormula>>() {
//                    }.getType());
                    MyFormula myFormula = GsonUtils.fromJson(json, new TypeToken<MyFormula>() {
                    }.getType());
                    // MyFormula myFormula = GsonUtils.fromJson(json, MyFormula.class);
//                    Gson gson = new Gson();
//                    MyFormula myFormula = gson.fromJson(json, MyFormula.class);
//                    FormulaitemsBean formulaitems = myFormula.getColorformula().getFormulaitems();
//                    List<ColorBean> colorBeans = formulaitems.getColorBeans();
//                    if(colorBeans.size()>1){

//                    MyFormula myFormula = new MyFormula();
//                    if(saveBeans!=null && saveBeans.size()>0){
//                        myFormula = saveBeans.get(0);
//                    }
//                    BaseFormulaItems formulaitems = myFormula.getColorformula().getFormulaitems();
//                    if(formulaitems instanceof FormulaitemsBean){
//                        List<ColorBean> colorBeans = ((FormulaitemsBean) formulaitems).getColorBeans();
//                        Log.i("TAG",colorBeans.size()+"");
//                    }else if(formulaitems instanceof  FormulaitemsBean2){
//                        ColorBean colorBean = ((FormulaitemsBean2) formulaitems).getColorBean();
//                        Log.i("TAG",colorBean+"");
//                    }
//                    Log.i("TAG",formulaitems.toString());
                    if (!GlobalConstants.isManual){
                        saveFormula(myFormula);
                        saveBeans.add(myFormula);
                    }
                }
            }
//        } else {//从数据库读取并给集合赋值
//            List<FormulaData> allList = FormulaDataService.getInstance().getAllList();
//            long id = 0;
//            if (allList != null && allList.size() > 0) {
//                FormulaData formulaData = allList.get(allList.size() - 1);
//                id = formulaData.getId();
//                ///////////////////////////////////////////////////
//                String regEx = "[^0-9]";
//                Pattern p = Pattern.compile(regEx);
//                Matcher m = p.matcher(formulaData.getBarrels());
//                String digit = m.replaceAll("").trim();
//                int formulaCount = 1;
//                if (digit != null && digit.length() > 0) {
//                    formulaCount = Integer.parseInt(digit);
//                }
//                /////////////////////////////////////////////////////
//                for (int i = 0; i < formulaCount; i++) {
//                    MyFormula myFormula = new MyFormula();
//                    ColorformulaBean colorformula = new ColorformulaBean();
//                    FormulaitemsBean formulaitems = new FormulaitemsBean();
//                    ///////////////////////////////////////////////////////
//                    FormulaBean formula = new FormulaBean();
//                    formula.setUnit(formulaData.getUnit());
//                    formula.setColorgroup(formulaData.getColorgroup());
//                    formula.setDescription(formulaData.getDescription());
//                    formula.setProduct(formulaData.getProduct());
//                    formula.setVersion(formulaData.getVersion());
//                    formula.setStandardcolor(formulaData.getStandardcolor());
//                    formula.setVariantcode(formulaData.getVariantcode());
//                    formula.setManufacturer(formulaData.getManufacturer());
//                    formula.setInnercolorcode(formulaData.getInnercolorcode());
//
//                    formula.setBarrels(formulaData.getBarrels());
//                    formula.setBrand(formulaData.getBrand());
//                    colorformula.setFormula(formula);
//                    ///////////////////////////////////////////////////////
//                    // int type = i + 1;
//                    List<ColorBean> allList1 = ColorBeanService.getInstance().getListById(id);
//                    if (allList1 != null && allList1.size() > 0) {
//                        formulaitems.setColorBeans(allList1);
//                        colorformula.setFormulaitems(formulaitems);
//                    }
//                    myFormula.setColorformula(colorformula);
//                    saveBeans.add(myFormula);
//                }
//
//            }
//        }


        return saveBeans;
    }

    private void saveFormula(MyFormula myFormula) {
        if (myFormula != null) {
            ColorformulaBean colorformula = myFormula.getColorformula();
            if (colorformula != null) {
                FormulaBean formula = colorformula.getFormula();
                FormulaData formulaData = null;
                int formulaCount = 1;
                if (formula != null) {
                    formulaData = new FormulaData();
                    formulaData.setUnit(formula.getUnit());
                    formulaData.setColorgroup(formula.getColorgroup());
                    formulaData.setDescription(formula.getDescription());
                    formulaData.setProduct(formula.getProduct());
                    formulaData.setVersion(formula.getVersion());
                    formulaData.setStandardcolor(formula.getStandardcolor());
                    formulaData.setVariantcode(formula.getVariantcode());
                    formulaData.setManufacturer(formula.getManufacturer());
                    formulaData.setInnercolorcode(formula.getInnercolorcode());
                    String regEx = "[^0-9]";
                    Pattern p = Pattern.compile(regEx);
                    Matcher m = p.matcher(formula.getBarrels());
                    String digit = m.replaceAll("").trim();
                    if (digit != null && digit.length() > 0) {
                        formulaCount = Integer.parseInt(digit);
                    }
                    formulaData.setBarrels(formula.getBarrels());
                    formulaData.setBrand(formula.getBrand());
                    SaveJsonFile.saveFileNameId(formulaData);///如果文件名已存在不保存
                }

                long id = 0;
                if (formulaData != null) {
                    id = formulaData.getId();
                }
                // FormulaitemsBean formulaitems = colorformula.getFormulaitems();
                List<ColorBean> colorBeanList = new ArrayList<>();
//                if(formulaitems instanceof FormulaitemsBean){
//                    colorBeanList = ((FormulaitemsBean)formulaitems).getColorBeans();
//                }else if(formulaitems instanceof FormulaitemsBean2){
//                    ColorBean colorBean = ((FormulaitemsBean2) formulaitems).getColorBean();
//                    colorBeanList.add(colorBean);
//                }
                //Gson gson = new Gson();
                FormulaitemsBean formulaitems = colorformula.getFormulaitems();
                colorBeanList = getCommonColorType(formulaitems);
                Log.i("TAG", colorBeanList + "");
//                if(listFormulaitemsBean.getColorBeans() instanceof List){
//                    colorBeanList= (List<ColorBean>) formulaitems.getColorBeans();
//                    Log.i("TAG",colorBeanList.size()+"");
//                }else{
//                    ColorBean colorBean = GsonUtils.fromJson(formulaitems.getColorBeans() + "", ColorBean.class);
//                    Log.i("TAG",colorBean+"");
//                    colorBeanList.add(colorBean);
//
//                }
                // colorBeanList = (List<ColorBean>) ((FormulaitemsBean)formulaitems).getColorBeans();
                if (colorBeanList != null) {
                    List<ColorBean> colorBeans = new ArrayList<>();
                    colorBeans.addAll(colorBeanList);
                    if (colorBeans != null && colorBeans.size() > 0) {
                        // for (int j = 0; j < formulaCount; j++) {
                        for (int i = 0; i < colorBeans.size(); i++) {
                            ColorBean colorBean = (ColorBean) colorBeans.get(i);
                            colorBean.setId(id);
                            //colorBean.setType(j + 1);
                        }
                        SaveJsonFile.saveFormulaItem(colorBeans);

                        Log.i("TAG", colorBeans.size() + "");
                        // }
                    }
                }
            }

        }

    }

    private List<ColorBean> getCommonColorType(FormulaitemsBean formulaitems) {
        List<ColorBean> colorBeanList = new ArrayList<>();
        if (formulaitems.getColorBeans() instanceof List) {
            colorBeanList = GsonUtils.fromJson(formulaitems.getColorBeans() + "", new TypeToken<List<ColorBean>>() {
            }.getType());
        } else {
            ColorBean colorBean = GsonUtils.fromJson(formulaitems.getColorBeans() + "", new TypeToken<ColorBean>() {
            }.getType());
            colorBeanList.add(colorBean);
        }
        return colorBeanList;
    }

    //读取xml文件
    private List<String> firstRun() {
        String json = "";
        Boolean first_run = SharedPreferencesHelper.getBoolean(mContext, "First", true);
//        if (first_run) {
//            SharedPreferencesHelper.putBoolean(mContext,"First", false);
//           // json = SaveJsonFile.readAssetFile("MyFormula.json", 3,mContext);
//           // json = SaveJsonFile.readXmlFile("MyFormula.xml", 3);
//
//
//
//        } else {
//            json = SaveJsonFile.readTextFile("MyFormula.json", 3);
//        }


        // if (!SaveJsonFile.readBusyFile("busy.xml")) {
        //json = SaveJsonFile.readXmlFile("MyFormula.xml", 3);
        // }
        // json = SaveJsonFile.readXmlFile("MyFormula.xml", 3);
        List<String> list = null;
        list = SaveJsonFile.readAllFile(null, 0, mContext);


        return list;
    }


    public void commit() {
        // List<MilkFormula> saveBeans = sparseToList();
        String json = GsonUtils.toJson(milkFormulas);
        SaveJsonFile.saveToSDCard(mContext, "MyFormula.json", json);
        // SaveJsonFile.writeListIntoSDcard("MilkFormula.json",milkFormulas);

    }

    private List<MyFormula> sparseToList() {
        int size = milkFormulas.size();
        List<MyFormula> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(milkFormulas.get(i));
        }
        return list;
    }

//    private void listToSparse() {
//        List<MilkFormula> saveBeans = getDataFromLocal();
//        if (saveBeans != null && saveBeans.size() > 0) {
//            for (MilkFormula saveBean : saveBeans) {
//                datas.put(saveBean.getMilkId(), saveBean);
//            }
//        }
//
//    }
}
