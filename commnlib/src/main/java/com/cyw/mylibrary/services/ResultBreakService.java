package com.cyw.mylibrary.services;

import android.content.Context;

import com.cyw.mylibrary.bean.ResultBreak;
import com.cyw.mylibrary.daos.ResultBreakDao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * author： cyw
 */
public class ResultBreakService {
    private ResultBreakDao dao;
    private ResultBreak cannedBean;
    public List<ResultBreak> friendList = new ArrayList<ResultBreak>();
    public List<ResultBreak> friendInfos = new ArrayList<ResultBreak>();
    public static ResultBreakService instance;

    public static ResultBreakService getInstance() {
        if (instance == null) {
            instance = new ResultBreakService();
        }
        return instance;
    }

    private List<ResultBreak> friendInfoss = new ArrayList<ResultBreak>();

    //聊天好友相关
    public void initDao(Context context) {
        if (dao == null) {
            dao = new ResultBreakDao(context);
        }
    }
    public ResultBreakDao getDao(){
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
    public List<ResultBreak> getAllList() {
        // return dao.getShoppingfriendList(UserService.getUserId());
        // RspShoppingfriendListBean rsp = (RspShoppingfriendListBean)
        // CommandHandler
        // .getInstance().execute(CommandConstants.CMD_SHOPPINGCART_LIST,
        // null);
        List<ResultBreak> friendInfoAll = new ArrayList<ResultBreak>();
        if(dao!=null){
            friendInfoAll = dao.queryData();
        }
        return friendInfoAll;
    }

    public void addBatchTask(final List<ResultBreak> infos) {
        try {
            dao.daoOpe.callBatchTasks(new Callable<Void>() {

                @Override
                public Void call() throws Exception {
                    for (ResultBreak info : infos) {
                        // dao.addOrUpdate(info);
                    }
                    return null;
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void save(ResultBreak cannedBean){
        dao.insetData(cannedBean);
    }
}
