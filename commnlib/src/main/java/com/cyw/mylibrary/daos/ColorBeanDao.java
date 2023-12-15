package com.cyw.mylibrary.daos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cyw.mylibrary.bean.ColorBean;
import com.cyw.mylibrary.bean.ResultBreak;
import com.cyw.mylibrary.dao.IOperation;
import com.cyw.mylibrary.dao.dbhelper.SantintDBHelper;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * author： cyw
 */
public class ColorBeanDao extends IOperation<ColorBean> {
    private SQLiteDatabase db;
    private String TAG = "";
    private Cursor cursor;
    public Dao<ColorBean, Integer> daoOpe;
    private SantintDBHelper helper;
    private ResultBreak accountInfo;
    List<ColorBean> accountList = new ArrayList<ColorBean>();

    @SuppressWarnings("unchecked")
    public ColorBeanDao(Context context) {
        // dbHelper = new SantintDBHelper(context);
        // TAG = getClass().getName();
        try {
            helper = SantintDBHelper.getHelper(context);
            daoOpe = helper.getDao(ColorBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    /**
//     * 添加一个账户
//     *
//     */
//    public void add(ResultBreak info) {
//        try {
//            daoOpe.create(info);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void update(ResultBreak info) {
//        try {
//            daoOpe.update(info);
//        } catch (SQLException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }

//    /**
//     * 获取所有交易列表
//     *
//     * @return
//     */
//    public List<ResultBreak> queryAllList() {
//        accountList = new ArrayList<ResultBreak>();
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

//    public List<ResultBreak> getAccountList2(long id) {
//        accountList = new ArrayList<ResultBreak>();
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

//    public ResultBreak getAccounInfo(int no) {
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
    public boolean insetData(ColorBean resultBreak) {
        try {
            int resultCode = daoOpe.create(resultBreak);
            if (resultCode == -1) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean insetMultiData(List<ColorBean> colorBeans) {
        try {
            int resultCode = daoOpe.create(colorBeans);
            if (resultCode == -1) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean deleteData(ColorBean resultBreak) {
        return false;
    }

    @Override
    public boolean updateData(ColorBean resultBreak) {
        try {
            int resultCode = daoOpe.update(resultBreak);
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
    public ArrayList<ColorBean> queryData() {
        accountList = new ArrayList<ColorBean>();
        try {
            accountList = daoOpe.queryForAll();
            Log.i("TAG4", accountList.toString());
            return (ArrayList<ColorBean>) accountList;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    public ArrayList<ColorBean> queryDataByIdLong(long id) {
        accountList = new ArrayList<ColorBean>();
        try {
            accountList = daoOpe.queryBuilder().where().eq("id", id)
                    .query();
            return (ArrayList<ColorBean>) accountList;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<ColorBean> queryDataById(int id) {
        accountList = new ArrayList<ColorBean>();
        try {
            accountList = daoOpe.queryBuilder().where().eq("id", id)
                    .query();
            return (ArrayList<ColorBean>) accountList;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<ColorBean> queryDataByIdType(long id, int type) {
        accountList = new ArrayList<ColorBean>();
        try {
            accountList = daoOpe.queryBuilder().where().eq("id", id).and().eq("type", type)
                    .query();
            return (ArrayList<ColorBean>) accountList;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<ColorBean> queryDataByName(String name) {
        return null;
    }
}
