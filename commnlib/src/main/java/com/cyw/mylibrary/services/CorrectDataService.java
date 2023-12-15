package com.cyw.mylibrary.services;

import android.content.Context;
import android.util.SparseArray;

import com.blankj.utilcode.util.GsonUtils;
import com.cyw.mylibrary.bean.SaveBean;
import com.cyw.mylibrary.util.SaveJsonFile;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 */

public class CorrectDataService {
    private SparseArray<SaveBean> datas = null;
    private final Context mContext;
    private List<SaveBean> saveBeans = null;
    public CorrectDataService(Context context){
        datas = new SparseArray<>(100);
        saveBeans = new ArrayList<>();
       // listToSparse();
        this.mContext =context;

    }
    public void put(SaveBean saveBean){
//        SaveBean.DataBean dataBean1 = saveBean.getData();
//        List<String> list1 = dataBean1.get_$1();
//       // SaveBean temp = datas.get(saveBean.getAddress());
////        if(temp!=null){
////            SaveBean.DataBean dataBean = temp.getData();
//////            List<String> list = dataBean.get_$1();
//////            list.addAll(list1);
////        }else{
////            temp = saveBean;
////        }
////        //temp = saveBean;
       // datas.put(saveBean.getAddress(),saveBean);
        saveBeans.add(saveBean);
        commit();
//        int size = 0;
//        if(datas!=null && datas.size()>0){
//            //size =datas.size();
//            size = datas.valueAt(datas.size()-1).getId()+1;
//        }
//        saveBean.setId(size);
//        datas.put(saveBean.getId(),saveBean);
//        commit();
    }
    public void update(int index,SaveBean saveBean){
       // datas.put(saveBean.getAddress(),saveBean);
        saveBeans.set(index,saveBean);
        commit();
    }
    public void delete(int rowIndex,SaveBean saveBean){
        //datas.delete(saveBean.getAddress());
        saveBeans.remove(rowIndex);
        commit();
    }
    public List<SaveBean> getAll(){
        saveBeans = getDataFromLocal();
        if(saveBeans == null){
            saveBeans = new ArrayList<>();
        }
        return saveBeans;
    }
    public List<SaveBean> getDataFromLocal(){
       String json = SaveJsonFile.readTextFile("CalibrationDatas.json",1);
        List<SaveBean> saveBeans = new ArrayList<>();
        if(json!=null){
           saveBeans = GsonUtils.fromJson(json,new TypeToken<List<SaveBean>>(){}.getType());
        }
       // saveBeans = SaveJsonFile.readListFromSdCard("CalibrationDatas.json");
        return saveBeans;
    }
    public void commit(){
       // List<SaveBean> saveBeans = sparseToList();
        String json = GsonUtils.toJson(saveBeans);
       // PrefereceUtils.putString(context,CART_JSON,)
        SaveJsonFile.saveToSDCard(mContext,"CalibrationDatas.json",json);
        //SaveJsonFile.writeListIntoSDcard("CalibrationDatas.json",saveBeans);

    }
    private List<SaveBean> sparseToList(){
        int size = datas.size();
        List<SaveBean> list = new ArrayList<>(size);
        for(int i=0;i<size;i++){
            list.add(datas.valueAt(i));
        }
        return list;
    }
    private void listToSparse(){
        List<SaveBean> saveBeans = getDataFromLocal();
        if(saveBeans!=null && saveBeans.size()>0){
            for(SaveBean saveBean: saveBeans){
                datas.put(saveBean.getAddress(),saveBean);
            }
        }

    }
}
