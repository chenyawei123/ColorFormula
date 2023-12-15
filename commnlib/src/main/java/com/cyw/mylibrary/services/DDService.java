package com.cyw.mylibrary.services;

import android.content.Context;

import com.cyw.mylibrary.bean.DDParamsBean;
import com.cyw.mylibrary.daos.DDDao;
import com.cyw.mylibrary.util.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * author： cyw
 */
public class DDService {
    private DDDao dao;
    public List<DDParamsBean> friendList = new ArrayList<DDParamsBean>();
    public List<DDParamsBean> friendInfos = new ArrayList<DDParamsBean>();
    public static DDService instance;
    private static Context mContext;

    public static DDService getInstance() {
        if (instance == null) {
            instance = new DDService();
        }
        return instance;
    }

    private List<DDParamsBean> friendInfoss = new ArrayList<DDParamsBean>();

    //聊天好友相关
    public void initDao(Context context) {
        mContext = context;
        if (dao == null) {
            dao = new DDDao(context);
        }
    }
    public DDDao getDao(){
        return dao;
    }
    /////////////////////////////////////////////////s数据库操作

//    /**
//     * 用户id获取好友的方法
//     *
//     * @return
//     */
//    public List<CannedBean> getFriendList() {
//        friendInfos = dao.getFriendList2(UserService
//                .getUserId());
//        return friendInfos;
//    }

    /**
     * 用户id获取好友的方法
     *
     * @return
     */
    public List<DDParamsBean> getAllList() {
        // return dao.getShoppingfriendList(UserService.getUserId());
        // RspShoppingfriendListBean rsp = (RspShoppingfriendListBean)
        // CommandHandler
        // .getInstance().execute(CommandConstants.CMD_SHOPPINGCART_LIST,
        // null);
        List<DDParamsBean> friendInfoAll = new ArrayList<DDParamsBean>();
        if(dao!=null){
            friendInfoAll = dao.queryData();
        }
        return friendInfoAll;
    }
    /**
     * 用户id获取好友的方法
     *
     * @return
     */
    public List<DDParamsBean> getAllListByType(String barrelNo) {
        // return dao.getShoppingfriendList(UserService.getUserId());
        // RspShoppingfriendListBean rsp = (RspShoppingfriendListBean)
        // CommandHandler
        // .getInstance().execute(CommandConstants.CMD_SHOPPINGCART_LIST,
        // null);
        boolean isHigh = SharedPreferencesHelper.getBoolean(mContext,"isHigh",true);
        int type = 0;
        if(isHigh){
            type =1;
        }else{
            type = 2;
        }
        List<DDParamsBean> friendInfoAll = new ArrayList<DDParamsBean>();
        if(dao!=null){
            friendInfoAll = dao.queryDataByType(type,barrelNo);
        }
        return friendInfoAll;
    }

    public void addBatchTask(final List<DDParamsBean> infos) {
        try {
            dao.daoOpe.callBatchTasks(new Callable<Void>() {

                @Override
                public Void call() throws Exception {
                    for (DDParamsBean info : infos) {
                        // dao.addOrUpdate(info);
                    }
                    return null;
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void save(DDParamsBean ddParamsBean){
        boolean isHigh = SharedPreferencesHelper.getBoolean(mContext,"isHigh",true);
        int type = 0;
        if(isHigh){
            type =1;
        }else{
            type = 2;
        }
        ddParamsBean.setBucket_type(String.valueOf(type));
        dao.insetData(ddParamsBean);
    }
    public void update(DDParamsBean ddParamsBean){
        dao.updateDataWithId(ddParamsBean);
    }
    public void delete(DDParamsBean ddParamsBean){
        dao.deleteDataWith(ddParamsBean);
    }
}
