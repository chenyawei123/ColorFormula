package com.cyw.mylibrary.services;

import android.content.Context;

import com.cyw.mylibrary.bean.FmParamsBean;
import com.cyw.mylibrary.daos.FmDao;
import com.cyw.mylibrary.util.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * author： cyw
 */
public class FMServiceNew {
    private FmDao dao;
    private FmParamsBean cannedBean;
    public List<FmParamsBean> friendList = new ArrayList<FmParamsBean>();
    public List<FmParamsBean> friendInfos = new ArrayList<FmParamsBean>();
    public static FMServiceNew instance;
    private static Context mContext;

    public static FMServiceNew getInstance() {
        if (instance == null) {
            instance = new FMServiceNew();
        }
        return instance;
    }

    private List<FmParamsBean> friendInfoss = new ArrayList<FmParamsBean>();

    //聊天好友相关
    public void initDao(Context context) {
        mContext =context;
        if (dao == null) {
            dao = new FmDao(context);
        }
    }
    public FmDao getDao(){
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
    public List<FmParamsBean> getAllList() {
        // return dao.getShoppingfriendList(UserService.getUserId());
        // RspShoppingfriendListBean rsp = (RspShoppingfriendListBean)
        // CommandHandler
        // .getInstance().execute(CommandConstants.CMD_SHOPPINGCART_LIST,
        // null);
        List<FmParamsBean> friendInfoAll = new ArrayList<FmParamsBean>();
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
    public List<FmParamsBean> getAllListByType() {
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
        List<FmParamsBean> friendInfoAll = new ArrayList<FmParamsBean>();
        if(dao!=null){
            friendInfoAll = dao.queryDataByType(type);
        }
        return friendInfoAll;
    }

    public void addBatchTask(final List<FmParamsBean> infos) {
        try {
            dao.daoOpe.callBatchTasks(new Callable<Void>() {

                @Override
                public Void call() throws Exception {
                    for (FmParamsBean info : infos) {
                        // dao.addOrUpdate(info);
                    }
                    return null;
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void save(FmParamsBean fmParamsBean){
        boolean isHigh = SharedPreferencesHelper.getBoolean(mContext,"isHigh",true);
        int type = 0;
        if(isHigh){
            type =1;
        }else{
            type = 2;
        }
        fmParamsBean.setBucket_type(String.valueOf(type));
        dao.insetData(fmParamsBean);
    }
    public void update(FmParamsBean fmParamsBean){
        dao.updateDataWith(fmParamsBean);
    }
    public void saveOrUpdate(FmParamsBean fmParamsBean){
        List<FmParamsBean> allListByType = getAllListByType();
        boolean isExist = false;
        for (int i = 0; i < allListByType.size(); i++) {
            FmParamsBean fmParamsBean1 = allListByType.get(i);
            if(fmParamsBean.getBarrelNo().equals(fmParamsBean1.getBarrelNo())){
                isExist = true;
                break;
            }
        }
        if(isExist){
            save(fmParamsBean);
        }else{
            update(fmParamsBean);
        }
    }
}
