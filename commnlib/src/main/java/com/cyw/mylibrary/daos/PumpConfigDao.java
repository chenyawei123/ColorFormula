package com.cyw.mylibrary.daos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cyw.mylibrary.bean.PumpConfigBean;
import com.cyw.mylibrary.dao.IOperation;
import com.cyw.mylibrary.dao.dbhelper.SantintDBHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * author： cyw
 */
public class PumpConfigDao extends IOperation<PumpConfigBean> {
    private SQLiteDatabase db;
    private String TAG = "";
    private Cursor cursor;
    public Dao<PumpConfigBean, Integer> daoOpe;
    private SantintDBHelper helper;
    private PumpConfigBean accountInfo;
    List<PumpConfigBean> accountList = new ArrayList<PumpConfigBean>();

    @SuppressWarnings("unchecked")
    public PumpConfigDao(Context context) {
        // dbHelper = new SantintDBHelper(context);
        // TAG = getClass().getName();
        try {
            helper = SantintDBHelper.getHelper(context);
            daoOpe = helper.getDao(PumpConfigBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean insetData(PumpConfigBean pumpConfigBean) {
        try {
            int resultCode = daoOpe.create(pumpConfigBean);
            if(resultCode == -1){
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean deleteData(PumpConfigBean pumpConfigBean) {

        return false;
    }
    public void deleteDataWith(PumpConfigBean pumpConfigBean){
        DeleteBuilder<PumpConfigBean, Integer> deleteBuilder = daoOpe.deleteBuilder();
        try {
            deleteBuilder.where().eq("bucket_name",pumpConfigBean.getAddress()).and().eq("bucket_type",pumpConfigBean.getBucket_type());
            deleteBuilder.delete();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void updateDataWith(PumpConfigBean pumpConfigBean){

        try {
            UpdateBuilder updateBuilder = daoOpe.updateBuilder();
            updateBuilder.where().eq("bucket_name",pumpConfigBean.getAddress()).and().eq("bucket_type",pumpConfigBean.getBucket_type());
            updateBuilder.updateColumnValue("color_name",pumpConfigBean.getMaterials_name());
            updateBuilder.updateColumnValue("high_speed",pumpConfigBean.getHighSpeed());
            updateBuilder.updateColumnValue("mid_speed",pumpConfigBean.getMidSpeed());
            updateBuilder.updateColumnValue("low_speed",pumpConfigBean.getLowSpeed());
            updateBuilder.updateColumnValue("dian_speed",pumpConfigBean.getDdSpeed());
            updateBuilder.updateColumnValue("launch_speed",pumpConfigBean.getStartSpeed());
            updateBuilder.updateColumnValue("launch_step",pumpConfigBean.getStartStep());
            updateBuilder.updateColumnValue("warn_rang",pumpConfigBean.getAlarm_value());
            updateBuilder.updateColumnValue("bucket_rang",pumpConfigBean.getMax_capacity());
            updateBuilder.updateColumnValue("curr_free",pumpConfigBean.getCurValue());
            updateBuilder.updateColumnValue("bei_shu",pumpConfigBean.getMultiple());
            updateBuilder.update();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public boolean updateData(PumpConfigBean pumpConfigBean) {
        try {
            int resultCode = daoOpe.update(pumpConfigBean);
            if(resultCode == -1){
                return false;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }


    @Override
    public ArrayList<PumpConfigBean> queryData() {
        accountList = new ArrayList<PumpConfigBean>();
        try {
            accountList = daoOpe.queryForAll();
            Log.i("TAG4", accountList.toString());
            return (ArrayList<PumpConfigBean>) accountList;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<PumpConfigBean> queryDataByType(int type) {
        accountList = new ArrayList<PumpConfigBean>();
        try {
            accountList = daoOpe.queryBuilder().where().eq("bucket_type",type).query();
            Log.i("TAG4", accountList.toString());
            return (ArrayList<PumpConfigBean>) accountList;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<PumpConfigBean> queryDataById(int id) {
        accountList = new ArrayList<PumpConfigBean>();
        try {
            accountList = daoOpe.queryBuilder().where().eq("userID", id)
                    .query();
            return (ArrayList<PumpConfigBean>) accountList;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<PumpConfigBean> queryDataByName(String name) {
        return null;
    }
}
