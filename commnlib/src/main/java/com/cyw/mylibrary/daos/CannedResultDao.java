package com.cyw.mylibrary.daos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cyw.mylibrary.bean.CannedBean;
import com.cyw.mylibrary.dao.IOperation;
import com.cyw.mylibrary.dao.dbhelper.SantintDBHelper;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CannedResultDao extends IOperation<CannedBean> {

    private SQLiteDatabase db;
    private String TAG = "";
    private Cursor cursor;
    public Dao<CannedBean, Integer> daoOpe;
    private SantintDBHelper helper;
    private CannedBean accountInfo;
    List<CannedBean> accountList = new ArrayList<CannedBean>();

    @SuppressWarnings("unchecked")
    public CannedResultDao(Context context) {
        // dbHelper = new SantintDBHelper(context);
        // TAG = getClass().getName();
        try {
            helper = SantintDBHelper.getHelper(context);
            daoOpe = helper.getDao(CannedBean.class);
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
public boolean insetData(CannedBean cannedBean) {
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
    public boolean deleteData(CannedBean cannedBean) {
        return false;
    }

    @Override
    public boolean updateData(CannedBean cannedBean) {
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
    public ArrayList<CannedBean> queryData() {
        accountList = new ArrayList<CannedBean>();
        try {
            accountList = daoOpe.queryForAll();
            Log.i("TAG4", accountList.toString());
            return (ArrayList<CannedBean>) accountList;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<CannedBean> queryDataById(int id) {
        accountList = new ArrayList<CannedBean>();
        try {
            accountList = daoOpe.queryBuilder().where().eq("userID", id)
                    .query();
            return (ArrayList<CannedBean>) accountList;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<CannedBean> queryDataByName(String name) {
        return null;
    }

}
