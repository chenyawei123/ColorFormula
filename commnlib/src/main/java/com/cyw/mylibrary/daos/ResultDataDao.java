package com.cyw.mylibrary.daos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cyw.mylibrary.bean.ResultData;
import com.cyw.mylibrary.dao.IOperation;
import com.cyw.mylibrary.dao.dbhelper.SantintDBHelper;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResultDataDao extends IOperation<ResultData> {

    private SQLiteDatabase db;
    private String TAG = "";
    private Cursor cursor;
    public Dao<ResultData, Integer> daoOpe;
    private SantintDBHelper helper;
    private ResultData accountInfo;
    List<ResultData> accountList = new ArrayList<ResultData>();

    @SuppressWarnings("unchecked")
    public ResultDataDao(Context context) {
        // dbHelper = new SantintDBHelper(context);
        // TAG = getClass().getName();
        try {
            helper = SantintDBHelper.getHelper(context);
            daoOpe = helper.getDao(ResultData.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //    /**
//     * 添加一个账户
//     *
//     */
//    public void add(ResultData info) {
//        try {
//            daoOpe.create(info);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void update(ResultData info) {
//        try {
//            daoOpe.update(info);
//        } catch (SQLException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 获取所有交易列表
//     *
//     * @return
//     */
//    public List<ResultData> queryAllList() {
//        accountList = new ArrayList<ResultData>();
//        try {
//            accountList = daoOpe.queryForAll();
//            Log.i("TAG4", accountList.toString());
//            return accountList;
//        } catch (SQLException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        return null;
//    }
//
//    public List<ResultData> getAccountList2(long id) {
//        accountList = new ArrayList<ResultData>();
//        try {
//            accountList = daoOpe.queryBuilder().where().eq("userID", id)
//                    .query();
//            return accountList;
//        } catch (SQLException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public ResultData getAccounInfo(int no) {
//        try {
//            accountInfo = daoOpe.queryForId(no);
//            return accountInfo;
//        } catch (SQLException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public void delete() {
//        try {
//            daoOpe.deleteById(2);
//        } catch (SQLException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
    @Override
    public boolean insetData(ResultData resultData) {
        try {
            int resultCode = daoOpe.create(resultData);
            if (resultCode == -1) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
    //删除上月数据
    @Override
    public boolean deleteData(ResultData resultData) {
        try {
            daoOpe.executeRaw("DELETE FROM resultdata WHERE time between datetime('now','-1 month','start of month') and datetime('now','start of month');");//DELETE FROM resultdata WHERE date('now','-1 months') >= date(time)
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateData(ResultData resultData) {
        try {
            int resultCode = daoOpe.update(resultData);
            if (resultCode == -1) {
                return false;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public ArrayList<ResultData> queryData() {
        accountList = new ArrayList<ResultData>();
        try {
            accountList = daoOpe.queryForAll();
            Log.i("TAG4", accountList.toString());
            return (ArrayList<ResultData>) accountList;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<ResultData> queryDataById(int id) {
        accountList = new ArrayList<ResultData>();
        try {

            accountList = daoOpe.queryBuilder().where().eq("userID", id)
                    .query();
            return (ArrayList<ResultData>) accountList;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<ResultData> queryDataByName(String name) {
        return null;
    }

}
