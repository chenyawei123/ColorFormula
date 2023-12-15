package com.santint.colorformula.utils;

import android.os.FileObserver;
import android.util.Log;

import org.xutils.common.util.LogUtil;

public class FileListener extends FileObserver {

    public EventCallback callback;

    public FileListener(String path) {
        super(path);
        LogUtil.e("进入文件监控");
    }

    public void setEventCallback(EventCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onEvent(int event, String path) {
        LogUtil.e("pathaaaa:" + path);
        if (null != path && !"".equals(path)) {
            String substring = path.substring(path.lastIndexOf(".") + 1);
            LogUtil.e("path:" + path);
            LogUtil.e("substring:" + substring);
            LogUtil.e("event:" + event);

        } else {
            return;
        }

        int e = event & FileObserver.ALL_EVENTS;
        LogUtil.e("event->e:" + e);
        switch (e) {
            case FileObserver.ACCESS:
                Log.e("wannoo", "文件操作___" + e + "__1打开文件后读取文件的操作");
                break;
            case FileObserver.MODIFY:
//                Log.e("wannoo", "文件操作___" + e + "__2文件被修改");
//                boolean isBusy = SaveJsonFile.readBusyFile("busy.xml");
////                if (!isBusy) {
////                    GlobalConstants.isManual = false;
////                } else {
////                    GlobalConstants.isManual = true;
////                }
//                GlobalConstants.isManual = false;
//
//                // new ReadFormulaThread(AppApplication.getInstance()).start();
//                ReadFormulaThread.getInstance(AppApplication.getInstance()).run();
                //OneTimeThread.getInstance(AppApplication.getInstance()).run();
////                MyFormulaService myFormulaService = new MyFormulaService(AppApplication.getInstance().getApplicationContext());
////                myFormulaService.getAll()
                break;
            case FileObserver.ATTRIB:
                Log.e("wannoo", "文件操作___" + e + "__4属性变化");
                break;
            case FileObserver.CLOSE_WRITE:
                Log.e("wannoo", "文件操作___" + e + "__8文件写入或编辑后关闭");
                break;
            case FileObserver.CLOSE_NOWRITE:
                //录音时，最后一个有效回调是这个
                Log.e("wannoo", "文件操作___" + e + "__16只读文件被关闭");
                if(callback!=null){
                    callback.onEvent(path);
                }
                break;
            case FileObserver.OPEN:
                Log.e("wannoo", "文件操作___" + e + "__32文件被打开");
                break;
            case FileObserver.MOVED_FROM:
                Log.e("wannoo", "文件操作___" + e + "__64移出事件");//试了重命名先MOVED_FROM再MOVED_TO
                break;
            case FileObserver.MOVED_TO:
                Log.e("wannoo", "文件操作___" + e + "__128移入事件");
                break;
            case FileObserver.CREATE:
                Log.e("wannoo", "文件操作___" + e + "__256新建文件");//把文件移动给自己先CREATE在DELETE
//                MyFormulaService myFormulaService = new MyFormulaService(AppApplication.getInstance().getApplicationContext());
//                myFormulaService.getAll();
                //AppApplication.getInstance().exit();

                break;
            case FileObserver.DELETE:
                Log.e("wannoo", "文件操作___" + e + "__512有删除文件");//把文件移出去DELETE
                break;
            case FileObserver.DELETE_SELF:
                Log.e("wannoo", "文件操作___" + e + "__1024监听的这个文件夹被删除");
                break;
            case FileObserver.MOVE_SELF:
                Log.e("wannoo", "文件操作___" + e + "__2048监听的这个文件夹被移走");
                break;
            case FileObserver.ALL_EVENTS:
                Log.e("wannoo", "文件操作___" + e + "__4095全部操作");
                break;
        }
    }

    public interface EventCallback {
        void onEvent(String path);
    }

}