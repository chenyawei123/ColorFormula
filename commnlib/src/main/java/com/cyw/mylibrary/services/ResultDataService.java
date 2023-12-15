package com.cyw.mylibrary.services;

import android.content.Context;

import com.cyw.mylibrary.bean.ResultData;
import com.cyw.mylibrary.daos.ResultDataDao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * author： cyw
 */
public class ResultDataService {
    private ResultDataDao dao;
    private ResultData cannedBean;
    public List<ResultData> friendList = new ArrayList<ResultData>();
    public List<ResultData> friendInfos = new ArrayList<ResultData>();
    public static ResultDataService instance;

    public static ResultDataService getInstance() {
        if (instance == null) {
            instance = new ResultDataService();
        }
        return instance;
    }

    private List<ResultData> friendInfoss = new ArrayList<ResultData>();

    //聊天好友相关
    public void initDao(Context context) {
        if (dao == null) {
            dao = new ResultDataDao(context);
        }
    }
    public ResultDataDao getDao(){
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
    public List<ResultData> getAllList() {
        // return dao.getShoppingfriendList(UserService.getUserId());
        // RspShoppingfriendListBean rsp = (RspShoppingfriendListBean)
        // CommandHandler
        // .getInstance().execute(CommandConstants.CMD_SHOPPINGCART_LIST,
        // null);
        List<ResultData> friendInfoAll = new ArrayList<ResultData>();
        if(dao!=null){
            friendInfoAll = dao.queryData();
        }

        return friendInfoAll;
    }

    public void addBatchTask(final List<ResultData> infos) {
        try {
            dao.daoOpe.callBatchTasks(new Callable<Void>() {

                @Override
                public Void call() throws Exception {
                    for (ResultData info : infos) {
                        // dao.addOrUpdate(info);
                    }
                    return null;
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void save(ResultData cannedBean){
        dao.insetData(cannedBean);
    }
    public void delete(ResultData resultData){
        dao.deleteData(resultData);
    }
}
