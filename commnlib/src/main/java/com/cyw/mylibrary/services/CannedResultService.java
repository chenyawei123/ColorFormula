package com.cyw.mylibrary.services;

import android.content.Context;

import com.cyw.mylibrary.bean.CannedBean;
import com.cyw.mylibrary.daos.CannedResultDao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * author： cyw
 */
public class CannedResultService {
    private CannedResultDao dao;
    private CannedBean cannedBean;
    public List<CannedBean> friendList = new ArrayList<CannedBean>();
    public List<CannedBean> friendInfos = new ArrayList<CannedBean>();
    public static CannedResultService instance;

    public static CannedResultService getInstance() {
        if (instance == null) {
            instance = new CannedResultService();
        }
        return instance;
    }

    private List<CannedBean> friendInfoss = new ArrayList<CannedBean>();

    //聊天好友相关
    public void initDao(Context context) {
        if (dao == null) {
            dao = new CannedResultDao(context);
        }
    }

    public CannedResultDao getDao() {
        return dao;
    }
    /////////////////////////////////////////////////s数据库操作

    /**
     * 用户id获取好友的方法
     *
     * @return
     */
    public List<CannedBean> getAllList() {
        // return dao.getShoppingfriendList(UserService.getUserId());
        // RspShoppingfriendListBean rsp = (RspShoppingfriendListBean)
        // CommandHandler
        // .getInstance().execute(CommandConstants.CMD_SHOPPINGCART_LIST,
        // null);
        List<CannedBean> friendInfoAll = new ArrayList<CannedBean>();
        if(dao!=null){
            friendInfoAll = dao.queryData();
        }

        return friendInfoAll;
    }

    public void addBatchTask(final List<CannedBean> infos) {
        try {
            dao.daoOpe.callBatchTasks(new Callable<Void>() {

                @Override
                public Void call() throws Exception {
                    for (CannedBean info : infos) {
                        // dao.addOrUpdate(info);
                    }
                    return null;
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save(CannedBean cannedBean) {
        dao.insetData(cannedBean);
    }

//    public long getTotalCount() {
//        long count = 0;
//        if (dao != null) {
//            count = dao.getCount();
//        }
//        return count;
//    }

//    public void updateTable() {
//        dao.updateTable();
//    }
//
//    public void saveOrUpdate(FriendInfo info) {
//        //List<FriendInfo> friendInfos = 	dao.getFriendList2(UserService.getUserId());
//        friendInfoss = dao.getFriendListAll();
//        for (int i = 0; i < friendInfoss.size(); i++) {
//            FriendInfo friendInfo = friendInfoss.get(i);
//            Log.i("TAGFFFFA", friendInfo.getMfNick() + friendInfo.getMfNo() + friendInfo.getMfOpsId() + friendInfo.getUserId() + "ddddddddddddddddddd");
//        }
//        if (dao.isExist(info.getMfOpsId())) {
//            Log.i("TAGTAGFRIENDUPDATE", info.getMfHead() + "jjj" + info.getMfOpsId() + "hhhhh" + info.getUserId() + "hhhhhh" + info.getMfNo() + "jjjj" + info.getMfNick() + "hhhhh" + info.getMfPhone());
//            dao.updateFriend(info);
//        } else {
//            if (friendInfoss != null && friendInfoss.size() > 0) {
//                int mfNo = friendInfoss.size() + 1;
//                info.setMfNo(mfNo);
//            } else {
//                info.setMfNo(1);
//            }
//            Log.i("TAGTAGFRIEND", info.getMfHead() + "jjj" + info.getMfOpsId() + "hhhhh" + info.getUserId() + "hhhhhh" + info.getMfNo() + "jjjj" + info.getMfNick() + "hhhhh" + info.getMfPhone());
//            dao.add(info);
//        }
//    }
//
//    public void delFriend(long friendId) {
//        try {
//            DeleteBuilder<FriendInfo, Integer> deleteBuilder = dao.friendDaoOpe.deleteBuilder();
//            deleteBuilder.where().eq("mfOpsId", friendId);
//            deleteBuilder.delete();
////            int value = dao.friendDaoOpe.deleteById((int) friendId);
////            Log.i("TAGVALUE",value+"jjjhh");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 清空好友数据
//     */
//    public void clearFriend() {
//        try {
//            if (dao != null) {
//                if (dao.daoOpe != null) {
//                    dao.daoOpe.queryRaw("delete from mf");// 清空数据
//                }
//            }
//
//            // dao.cartDaoOpe
//            // .queryRaw("update sqlite_sequence SET seq=0 where name = 'sc'");//
//            // 自增长ID为0
//        } catch (SQLException e) {
//            Log.i("TAGCLEARFRIEND", e.toString() + "hhh");
//            e.printStackTrace();
//        }
//    }
//
//
//    public List<CannedBean> getFriendInfos(long off) {
//        return dao.getFriendList(off);
//    }
//
//    public List<CannedBean> getFriendByName(String nick) {
//        return dao.getFriendByName(nick);
//    }
}