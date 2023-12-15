package com.cyw.mylibrary.services;

import android.content.Context;
import android.util.SparseArray;

import com.blankj.utilcode.util.GsonUtils;
import com.cyw.mylibrary.bean.ColorBean;
import com.cyw.mylibrary.util.SaveJsonFile;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * author： cyw
 */
public class ManualColorBeanService {
    private SparseArray<ColorBean> datas = null;
    private final Context mContext;
    private List<ColorBean> milkFormulas = null;

    public ManualColorBeanService(Context context) {
        datas = new SparseArray<>(100);
        milkFormulas = new ArrayList<>();
        // listToSparse();

        this.mContext = context;
        // SharedPreferencesUtil.initialData(context,"milkFormulas",milkFormulas);

    }

    public void put(ColorBean milkFormula) {
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

    public void update(int index,ColorBean saveBean) {
        // datas.put(saveBean.getMilkId(), saveBean);
        milkFormulas.set(index,saveBean);
        //SharedPreferencesUtil.saveData(mContext,"milkFormulas",milkFormulas);
        // SharedPreferencesHelper.putList(mContext,"milkFormulas",milkFormulas);
        commit();
    }

    public void delete(int rowIndex,ColorBean saveBean) {
        // datas.delete(saveBean.getMilkId());
        milkFormulas.remove(rowIndex);
        //SharedPreferencesUtil.saveData(mContext,"milkFormulas",milkFormulas);
        //SharedPreferencesHelper.putList(mContext,"milkFormulas",milkFormulas);
        commit();
    }

    public List<ColorBean> getAll() {
        milkFormulas = getDataFromLocal();
        if(milkFormulas == null){

            milkFormulas = new ArrayList<>();
        }
        //WriteLog.writeTxtToFile(milkFormulas.size()+"==================");
        return milkFormulas;
    }

    public List<ColorBean> getDataFromLocal() {
        String json = SaveJsonFile.readTextFile("ManualColorBean.json", 3);
        List<ColorBean> saveBeans = new ArrayList<>();
        if (json != null && !"".equals(json)) {
            saveBeans = GsonUtils.fromJson(json, new TypeToken<List<ColorBean>>() {
            }.getType());
        }
        // saveBeans =SharedPreferencesHelper.getList(mContext,"milkFormulas");
        // saveBeans = SaveJsonFile.readListFromSdCard("MilkFormula.json");
        return saveBeans;
    }

    public void commit() {
        // List<MilkFormula> saveBeans = sparseToList();
        String json = GsonUtils.toJson(milkFormulas);
        SaveJsonFile.saveToSDCard(mContext, "ManualColorBean.json", json);
        // SaveJsonFile.writeListIntoSDcard("MilkFormula.json",milkFormulas);

    }

    private List<ColorBean> sparseToList() {
        int size = milkFormulas.size();
        List<ColorBean> list = new ArrayList<>(size);
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
