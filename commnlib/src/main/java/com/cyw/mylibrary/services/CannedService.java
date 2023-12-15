package com.cyw.mylibrary.services;

import android.content.Context;
import android.util.SparseArray;

import com.blankj.utilcode.util.GsonUtils;
import com.cyw.mylibrary.bean.CannedBean;
import com.cyw.mylibrary.util.SaveJsonFile;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class CannedService {
    private final SparseArray<CannedBean> datas = null;
    private final Context mContext;
    private List<CannedBean> CannedBeans = null;

    public CannedService(Context context) {
        //datas = new SparseArray<>(100);
        if(CannedBeans == null){
            CannedBeans = new ArrayList<>();
        }

        //listToSparse();
        this.mContext = context;

    }
    public void saveOrUpdate(String tong,CannedBean cannedBean){
            boolean isExist = false;
            int index = -1;
            if(CannedBeans!=null && CannedBeans.size()>0){
                for(int i=0;i<CannedBeans.size();i++){
                    CannedBean cannedBean1 = CannedBeans.get(i);
                    if(tong.equals(cannedBean1.getBarrelNo())){
                        isExist = true;
                        index =i;
                        break;
                    }
                }
            }
            if(isExist){
                update(index,cannedBean);
            }else{
                put(cannedBean);
            }


    }

    public void put(CannedBean saveBean) {
//        boolean isHave = false;
//        for(int i=0;i<CannedBean.size();i++){
//            CannedBean pumpConfigBean1 = pumpConfigBeans.get(i);
//            if(pumpConfigBean1.getAddress()>saveBean.getAddress()){
//                isHave = true;
//                pumpConfigBeans.add(i,saveBean);
//                break;
//            }
//        }
//        if(!isHave){
//            pumpConfigBeans.add(saveBean);
//        }
        CannedBeans.add(saveBean);
        commit();
    }

    public void update(int i, CannedBean saveBean) {
        //datas.put(saveBean.getAddress(),saveBean);
        CannedBeans.set(i, saveBean);
        commit();
    }

    public void delete(int rowIndex,CannedBean saveBean) {
        // datas.delete(saveBean.getAddress());
        //pumpConfigBeans.remove(saveBean);
        CannedBeans.remove(rowIndex);
        commit();
    }

    public List<CannedBean> getAll() {
        CannedBeans = getDataFromLocal();
        if(CannedBeans == null){
            CannedBeans = new ArrayList<>();
        }
        return CannedBeans;
    }

    public List<CannedBean> getDataFromLocal() {
        String json = SaveJsonFile.readTextFile("CannedBean.json", 3);
        List<CannedBean> saveBeans = new ArrayList<>();
        if (json != null) {
            saveBeans = GsonUtils.fromJson(json, new TypeToken<List<CannedBean>>() {
            }.getType());
        }
        if(saveBeans == null || saveBeans.size() == 0){
            saveBeans = new ArrayList<>();
        }
        // saveBeans = SaveJsonFile.readListFromSdCard("PumpConfig.json");
        return saveBeans;
    }

    public void commit() {
        // List<CannedBean> saveBeans = sparseToList();
        String json = GsonUtils.toJson(CannedBeans);
        // PrefereceUtils.putString(context,CART_JSON,)
        SaveJsonFile.saveToSDCard(mContext, "CannedBean.json", json);
        // SaveJsonFile.writeListIntoSDcard("PumpConfig.json",CannedBeans);

    }

    private List<CannedBean> sparseToList() {
        int size = CannedBeans.size();
        List<CannedBean> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(CannedBeans.get(i));
        }
        return list;
    }
    public void topChange(int rowIndex,CannedBean dataBean){
        CannedBeans.set(rowIndex, CannedBeans.get(rowIndex - 1));
        CannedBeans.set(rowIndex - 1, dataBean);
        commit();
    }
    public void bottomChange(int rowIndex,CannedBean dataBean){
        CannedBeans.set(rowIndex, CannedBeans.get(rowIndex + 1));
        CannedBeans.set(rowIndex + 1, dataBean);
        commit();
    }
//    private void listToSparse() {
////        List<CannedBean> saveBeans = getDataFromLocal();
////        if(saveBeans!=null && saveBeans.size()>0){
////            for(CannedBean saveBean: saveBeans){
////                datas.put(saveBean.getAddress(),saveBean);
////            }
////        }
//        CannedBeans = getDataFromLocal();
//
//    }
}
