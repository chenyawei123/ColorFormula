package com.cyw.mylibrary.daos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cyw.mylibrary.bean.FmParamsBean;
import com.cyw.mylibrary.dao.IOperation;
import com.cyw.mylibrary.dao.dbhelper.SantintDBHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * author： cyw
 */
public class FmDao extends IOperation<FmParamsBean> {
    private SQLiteDatabase db;
    private String TAG = "";
    private Cursor cursor;
    public Dao<FmParamsBean, Integer> daoOpe;
    private SantintDBHelper helper;
    private FmParamsBean accountInfo;
    List<FmParamsBean> accountList = new ArrayList<FmParamsBean>();

    @SuppressWarnings("unchecked")
    public FmDao(Context context) {
        // dbHelper = new SantintDBHelper(context);
        // TAG = getClass().getName();
        try {
            helper = SantintDBHelper.getHelper(context);
            daoOpe = helper.getDao(FmParamsBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean insetData(FmParamsBean fmParamsBean) {
        try {
            int resultCode = daoOpe.create(fmParamsBean);
            if(resultCode == -1){
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean deleteData(FmParamsBean fmParamsBean) {
        return false;
    }

    @Override
    public boolean updateData(FmParamsBean fmParamsBean) {
        try {
            int resultCode = daoOpe.update(fmParamsBean);
            if(resultCode == -1){
                return false;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }
    public void updateDataWith(FmParamsBean fmParamsBean){

        try {
            UpdateBuilder updateBuilder = daoOpe.updateBuilder();
            updateBuilder.where().eq("bucket_name",fmParamsBean.getBarrelNo()).and().eq("bucket_type",fmParamsBean.getBucket_type());
            updateBuilder.updateColumnValue("high_thred",fmParamsBean.getHighThreshold());
            updateBuilder.updateColumnValue("mid_thred",fmParamsBean.getLowThreshold());
            updateBuilder.updateColumnValue("low_thred",fmParamsBean.getCloseThreshold());
            updateBuilder.updateColumnValue("guan_pre",fmParamsBean.getCannedaPrecision());
            updateBuilder.updateColumnValue("shake_times",fmParamsBean.getDdCount());
            updateBuilder.updateColumnValue("open_big",fmParamsBean.getOpenBig());
            updateBuilder.updateColumnValue("close_big",fmParamsBean.getCloseBig());
            updateBuilder.updateColumnValue("open_small",fmParamsBean.getOpenSmall());
            updateBuilder.updateColumnValue("close_small",fmParamsBean.getCloseSmall());
            updateBuilder.update();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<FmParamsBean> queryData() {
        accountList = new ArrayList<FmParamsBean>();
        try {
            accountList = daoOpe.queryForAll();
            Log.i("TAG4", accountList.toString());
            return (ArrayList<FmParamsBean>) accountList;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    public ArrayList<FmParamsBean> queryDataByType(int type) {
        accountList = new ArrayList<FmParamsBean>();
        try {
            accountList = daoOpe.queryBuilder().where().eq("bucket_type",type).query();
            Log.i("TAG4", accountList.toString());
            return (ArrayList<FmParamsBean>) accountList;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<FmParamsBean> queryDataById(int id) {
        accountList = new ArrayList<FmParamsBean>();
        try {
            accountList = daoOpe.queryBuilder().where().eq("userID", id)
                    .query();
            return (ArrayList<FmParamsBean>) accountList;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<FmParamsBean> queryDataByName(String name) {
        return null;
    }
}
