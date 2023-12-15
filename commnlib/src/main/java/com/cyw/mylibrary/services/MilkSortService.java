package com.cyw.mylibrary.services;

import android.content.Context;
import android.util.SparseArray;

import com.blankj.utilcode.util.GsonUtils;
import com.cyw.mylibrary.util.SaveJsonFile;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * author： cyw
 */
public class MilkSortService {
    private SparseArray<String> datas = null;
    private final Context mContext;
    private List<String> milkSorts = null;

    public MilkSortService(Context context) {
        datas = new SparseArray<>(100);
        if(milkSorts == null){
            milkSorts = new ArrayList<>();
        }

        // listToSparse();
        this.mContext = context;
        // SharedPreferencesUtil.initialData(context,"milkFormulas",milkFormulas);
    }

    public void put(String milkSort) {
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
        milkSorts.add(milkSort);
        //SharedPreferencesUtil.saveData(mContext,"milkFormulas",milkFormulas);
        // SharedPreferencesHelper.putList(mContext,"milkFormulas",milkFormulas);
        //}
        commit();
    }

    public void update(int index,String saveBean) {
        // datas.put(saveBean.getMilkId(), saveBean);
        milkSorts.set(index,saveBean);
        //SharedPreferencesUtil.saveData(mContext,"milkFormulas",milkFormulas);
        // SharedPreferencesHelper.putList(mContext,"milkFormulas",milkFormulas);
        commit();
    }

    public void delete(int rowIndex,String saveBean) {
        // datas.delete(saveBean.getMilkId());
        milkSorts.remove(rowIndex);
        //SharedPreferencesUtil.saveData(mContext,"milkFormulas",milkFormulas);
        //SharedPreferencesHelper.putList(mContext,"milkFormulas",milkFormulas);
        commit();
    }

    public List<String> getAll() {
        milkSorts = getDataFromLocal();
        if(milkSorts == null){
            milkSorts = new ArrayList<>();
        }
        return milkSorts;
    }

    public List<String> getDataFromLocal() {
        String json = SaveJsonFile.readTextFile("MilkSort.json", 3);
        List<String> saveBeans = new ArrayList<>();
        if (json != null && !"".equals(json)) {
            saveBeans = GsonUtils.fromJson(json, new TypeToken<List<String>>() {
            }.getType());
        }
        // saveBeans = SharedPreferencesHelper.getList(mContext,"milkFormulas");
       // saveBeans = SaveJsonFile.readListFromSdCard("MilkSort.json");
        return saveBeans;
    }

    public void commit() {
        // List<MilkFormula> saveBeans = sparseToList();
        String json = GsonUtils.toJson(milkSorts);
         SaveJsonFile.saveToSDCard(mContext, "MilkSort.json", json);
        //SaveJsonFile.writeListIntoSDcard("MilkSort.json",milkSorts);

    }

    private List<String> sparseToList() {
        int size = milkSorts.size();
        List<String> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(milkSorts.get(i));
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
