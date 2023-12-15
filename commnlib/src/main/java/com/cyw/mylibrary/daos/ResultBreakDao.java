package com.cyw.mylibrary.daos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cyw.mylibrary.bean.ResultBreak;
import com.cyw.mylibrary.dao.IOperation;
import com.cyw.mylibrary.dao.dbhelper.SantintDBHelper;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResultBreakDao extends IOperation<ResultBreak> {

    private SQLiteDatabase db;
    private String TAG = "";
    private Cursor cursor;
    public Dao<ResultBreak, Integer> daoOpe;
    private SantintDBHelper helper;
    private ResultBreak accountInfo;
    List<ResultBreak> accountList = new ArrayList<ResultBreak>();

    @SuppressWarnings("unchecked")
    public ResultBreakDao(Context context) {
        // dbHelper = new SantintDBHelper(context);
        // TAG = getClass().getName();
        try {
            helper = SantintDBHelper.getHelper(context);
            daoOpe = helper.getDao(ResultBreak.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean insetData(ResultBreak resultBreak) {
        try {
            int resultCode = daoOpe.create(resultBreak);
            if(resultCode == -1){
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean deleteData(ResultBreak resultBreak) {
        return false;
    }

    @Override
    public boolean updateData(ResultBreak resultBreak) {
        try {
            int resultCode = daoOpe.update(resultBreak);
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
    public ArrayList<ResultBreak> queryData() {
        accountList = new ArrayList<ResultBreak>();
        try {
            accountList = daoOpe.queryForAll();
            Log.i("TAG4", accountList.toString());
            return (ArrayList<ResultBreak>) accountList;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<ResultBreak> queryDataById(int id) {
        accountList = new ArrayList<ResultBreak>();
        try {
            accountList = daoOpe.queryBuilder().where().eq("userID", id)
                    .query();
            return (ArrayList<ResultBreak>) accountList;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<ResultBreak> queryDataByName(String name) {
        return null;
    }
}
