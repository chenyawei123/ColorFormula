package com.cyw.mylibrary.daos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cyw.mylibrary.bean.DDParamsBean;
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
public class DDDao extends IOperation<DDParamsBean> {
    private SQLiteDatabase db;
    private String TAG = "";
    private Cursor cursor;
    public Dao<DDParamsBean, Integer> daoOpe;
    private SantintDBHelper helper;
    private DDParamsBean accountInfo;
    List<DDParamsBean> accountList = new ArrayList<DDParamsBean>();

    @SuppressWarnings("unchecked")
    public DDDao(Context context) {
        // dbHelper = new SantintDBHelper(context);
        // TAG = getClass().getName();
        try {
            helper = SantintDBHelper.getHelper(context);
            daoOpe = helper.getDao(DDParamsBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean insetData(DDParamsBean ddParamsBean) {
        try {
            int resultCode = daoOpe.create(ddParamsBean);
            if(resultCode == -1){
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean deleteData(DDParamsBean ddParamsBean) {
        return false;
    }
    public void deleteDataWith(DDParamsBean ddParamsBean){
        DeleteBuilder<DDParamsBean, Integer> deleteBuilder = daoOpe.deleteBuilder();
        try {
            deleteBuilder.where().eq("id",ddParamsBean.getId());
            deleteBuilder.delete();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void updateDataWith(DDParamsBean ddParamsBean){

        try {
            UpdateBuilder updateBuilder = daoOpe.updateBuilder();
            updateBuilder.where().eq("bucket_name",ddParamsBean.getBarrelNo()).and().eq("bucket_type",ddParamsBean.getBucket_type());
            updateBuilder.updateColumnValue("dian_num",ddParamsBean.getDdTargetDos());
            updateBuilder.updateColumnValue("shake_steps",ddParamsBean.getDdStep());
            updateBuilder.updateColumnValue("dian_time",ddParamsBean.getDdCycle());
            updateBuilder.updateColumnValue("dian_front",ddParamsBean.getDdBeforeStep());
            updateBuilder.update();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void updateDataWithId(DDParamsBean ddParamsBean){
        try {
            UpdateBuilder updateBuilder = daoOpe.updateBuilder();
            updateBuilder.where().eq("id",ddParamsBean.getId());
            updateBuilder.updateColumnValue("dian_num",ddParamsBean.getDdTargetDos());
            updateBuilder.updateColumnValue("shake_steps",ddParamsBean.getDdStep());
            updateBuilder.updateColumnValue("dian_time",ddParamsBean.getDdCycle());
            updateBuilder.updateColumnValue("dian_front",ddParamsBean.getDdBeforeStep());
            updateBuilder.update();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    @Override
    public boolean updateData(DDParamsBean ddParamsBean) {
        try {
            int resultCode = daoOpe.update(ddParamsBean);
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
    public ArrayList<DDParamsBean> queryData() {
        accountList = new ArrayList<DDParamsBean>();
        try {
            accountList = daoOpe.queryForAll();
            Log.i("TAG4", accountList.toString());
            return (ArrayList<DDParamsBean>) accountList;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    public ArrayList<DDParamsBean> queryDataByType(int type,String barrelNo) {
        accountList = new ArrayList<DDParamsBean>();
        try {
            accountList = daoOpe.queryBuilder().where().eq("bucket_type",type).and().eq("bucket_name",barrelNo).query();
            Log.i("TAG4", accountList.toString());
            return (ArrayList<DDParamsBean>) accountList;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<DDParamsBean> queryDataById(int id) {
        accountList = new ArrayList<DDParamsBean>();
        try {
            accountList = daoOpe.queryBuilder().where().eq("userID", id)
                    .query();
            return (ArrayList<DDParamsBean>) accountList;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<DDParamsBean> queryDataByName(String name) {
        return null;
    }
}
