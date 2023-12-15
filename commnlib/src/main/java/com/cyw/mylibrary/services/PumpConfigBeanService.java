package com.cyw.mylibrary.services;

import android.content.Context;

import com.cyw.mylibrary.bean.PumpConfigBean;
import com.cyw.mylibrary.daos.PumpConfigDao;
import com.cyw.mylibrary.util.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * author： cyw
 */
public class PumpConfigBeanService {
    private PumpConfigDao dao;
    private PumpConfigBean cannedBean;
    public List<PumpConfigBean> friendList = new ArrayList<PumpConfigBean>();
    public List<PumpConfigBean> friendInfos = new ArrayList<PumpConfigBean>();
    public static PumpConfigBeanService instance;
    private static Context mContext;

    public static PumpConfigBeanService getInstance() {
        if (instance == null) {
            instance = new PumpConfigBeanService();
        }
        return instance;
    }

    private List<PumpConfigBean> friendInfoss = new ArrayList<PumpConfigBean>();

    //聊天好友相关
    public void initDao(Context context) {
        mContext = context;
        if (dao == null) {
            dao = new PumpConfigDao(context);
        }
    }
    public PumpConfigDao getDao(){
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
    public List<PumpConfigBean> getAllList() {
        // return dao.getShoppingfriendList(UserService.getUserId());
        // RspShoppingfriendListBean rsp = (RspShoppingfriendListBean)
        // CommandHandler
        // .getInstance().execute(CommandConstants.CMD_SHOPPINGCART_LIST,
        // null);
        List<PumpConfigBean> friendInfoAll = new ArrayList<PumpConfigBean>();
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
    public List<PumpConfigBean> getAllListByType() {
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
        List<PumpConfigBean> friendInfoAll = new ArrayList<PumpConfigBean>();
        if(dao!=null){
            friendInfoAll = dao.queryDataByType(type);
        }
        return friendInfoAll;
    }

    public void addBatchTask(final List<PumpConfigBean> infos) {
        try {
            dao.daoOpe.callBatchTasks(new Callable<Void>() {

                @Override
                public Void call() throws Exception {
                    for (PumpConfigBean info : infos) {
                        // dao.addOrUpdate(info);
                    }
                    return null;
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void save(PumpConfigBean pumpConfigBean){
        boolean isHigh = SharedPreferencesHelper.getBoolean(mContext,"isHigh",true);
        int type = 0;
        if(isHigh){
            type =1;
        }else{
            type = 2;
        }
        pumpConfigBean.setBucket_type(String.valueOf(type));
        dao.insetData(pumpConfigBean);
    }
    public void update(PumpConfigBean pumpConfigBean){
        boolean isHigh = SharedPreferencesHelper.getBoolean(mContext,"isHigh",true);
        int type = 0;
        if(isHigh){
            type =1;
        }else{
            type = 2;
        }
        dao.updateDataWith(pumpConfigBean);
    }
    public void saveOrUpdate(PumpConfigBean pumpConfigBean){
        List<PumpConfigBean> allListByType = getAllListByType();
        boolean isExist = false;
        for (int i = 0; i < allListByType.size(); i++) {
            PumpConfigBean pumpConfigBean1 = allListByType.get(i);
            if(pumpConfigBean.getAddress().equals(pumpConfigBean1.getAddress())){
                isExist = true;
                break;
            }
        }
        if(isExist){
            save(pumpConfigBean);
        }else{
            update(pumpConfigBean);
        }
    }
    public void delete(PumpConfigBean pumpConfigBean){
        dao.deleteDataWith(pumpConfigBean);
    }
}
