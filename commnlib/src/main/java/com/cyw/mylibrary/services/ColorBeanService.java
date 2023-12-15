package com.cyw.mylibrary.services;

import android.content.Context;

import com.cyw.mylibrary.bean.ColorBean;
import com.cyw.mylibrary.bean.ResultBreak;
import com.cyw.mylibrary.daos.ColorBeanDao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * author： cyw
 */
public class ColorBeanService {
    private ColorBeanDao dao;
    private ResultBreak cannedBean;
    public List<ColorBean> friendList = new ArrayList<ColorBean>();
    public List<ColorBean> friendInfos = new ArrayList<ColorBean>();
    public static ColorBeanService instance;

    public static ColorBeanService getInstance() {
        if (instance == null) {
            instance = new ColorBeanService();
        }
        return instance;
    }
    //聊天好友相关
    public void initDao(Context context) {
        if (dao == null) {
            dao = new ColorBeanDao(context);
        }
    }
    public ColorBeanDao getDao(){
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
     * 获取所有色浆列表
     *
     * @return
     */
    public List<ColorBean> getAllList() {
        // return dao.getShoppingfriendList(UserService.getUserId());
        // RspShoppingfriendListBean rsp = (RspShoppingfriendListBean)
        // CommandHandler
        // .getInstance().execute(CommandConstants.CMD_SHOPPINGCART_LIST,
        // null);
        List<ColorBean> colorBeans = new ArrayList<ColorBean>();
        if(dao!=null){
            colorBeans = dao.queryData();
        }
        return colorBeans;
    }
    /**
     * id获取色浆列表
     *
     * @return
     */
    /**
     * 获取所有色浆列表
     *
     * @return
     */
    public List<ColorBean> getListById(long id) {
        // return dao.getShoppingfriendList(UserService.getUserId());
        // RspShoppingfriendListBean rsp = (RspShoppingfriendListBean)
        // CommandHandler
        // .getInstance().execute(CommandConstants.CMD_SHOPPINGCART_LIST,
        // null);
        List<ColorBean> colorBeans = new ArrayList<ColorBean>();
        if(dao!=null){
            colorBeans = dao.queryDataByIdLong(id);
        }
        return colorBeans;
    }


    public void addBatchTask(final List<ColorBean> infos) {
        try {
            dao.daoOpe.callBatchTasks(new Callable<Void>() {

                @Override
                public Void call() throws Exception {
                    for (ColorBean info : infos) {
                        // dao.addOrUpdate(info);
                    }
                    return null;
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void save(ColorBean cannedBean){
        dao.insetData(cannedBean);
    }
    public void saveMulti(List<ColorBean> colorBeans){
        dao.insetMultiData(colorBeans);
    }
    public void updateData(ColorBean colorBean){
        dao.updateData(colorBean);
    }
}
