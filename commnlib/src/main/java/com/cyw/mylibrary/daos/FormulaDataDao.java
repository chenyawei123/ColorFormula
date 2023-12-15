package com.cyw.mylibrary.daos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cyw.mylibrary.bean.FormulaData;
import com.cyw.mylibrary.dao.IOperation;
import com.cyw.mylibrary.dao.dbhelper.SantintDBHelper;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * author： cyw
 */
public class FormulaDataDao extends IOperation<FormulaData> {
    private SQLiteDatabase db;
    private String TAG = "";
    private Cursor cursor;
    public Dao<FormulaData, Integer> daoOpe;
    private SantintDBHelper helper;
    private FormulaData accountInfo;
    List<FormulaData> accountList = new ArrayList<FormulaData>();

    @SuppressWarnings("unchecked")
    public FormulaDataDao(Context context) {
        // dbHelper = new SantintDBHelper(context);
        // TAG = getClass().getName();
        try {
            helper = SantintDBHelper.getHelper(context);
            daoOpe = helper.getDao(FormulaData.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //    /**
//     * 添加一个账户
//     *
//     */
//    public void add(CannedBean info) {
//        try {
//            daoOpe.create(info);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void update(CannedBean info) {
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
//    public List<CannedBean> queryAllList() {
//        accountList = new ArrayList<CannedBean>();
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
//    public List<CannedBean> getAccountList2(long id) {
//        accountList = new ArrayList<CannedBean>();
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
//    public CannedBean getAccounInfo(int no) {
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
    public boolean insetData(FormulaData cannedBean) {
        try {
            int resultCode = daoOpe.create(cannedBean);
            if(resultCode == -1){
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean deleteData(FormulaData cannedBean) {
        return false;
    }

    @Override
    public boolean updateData(FormulaData cannedBean) {
        try {
            int resultCode = daoOpe.update(cannedBean);
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
    public ArrayList<FormulaData> queryData() {
        accountList = new ArrayList<FormulaData>();
        try {
            accountList = daoOpe.queryForAll();
            Log.i("TAG4", accountList.toString());
            return (ArrayList<FormulaData>) accountList;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<FormulaData> queryDataById(int id) {
        accountList = new ArrayList<FormulaData>();
        try {
            accountList = daoOpe.queryBuilder().where().eq("userID", id)
                    .query();
            return (ArrayList<FormulaData>) accountList;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<FormulaData> queryDataByName(String name) {
        return null;
    }
}
