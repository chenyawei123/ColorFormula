package com.santint.colorformula;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cyw.mylibrary.application.AppApplication;
import com.cyw.mylibrary.util.MyActivityManager;
import com.enjoy.crash2.CrashReport;
import com.santint.colorformula.utils.FileListener;

/**
 * author： cyw
 */
public class ColorApplication extends AppApplication {
    private FileListener fileListener;

    @Override
    public void onCreate() {
        super.onCreate();
        String dirPath = dir2.getAbsolutePath();
        fileListener = new FileListener(dirPath);
        fileListener.startWatching();
        //GC抑制，延长GC阈值 启动优化
        //system/lib64/libart.so // Android 10 以前系统，Android 10 之后换了位置
       // StartupOptimize.delayGC();
        //initBitmapMonitor();
        CrashReport.init(this);
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {

            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {
                MyActivityManager.getInstance().setsCurrentActivity(activity);
            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {

            }
        });
    }
//    private void initBitmapMonitor() {
//        long checkInterval = 10;
//        long threshold = 100 * 1024;
//        long restoreImageThreshold = 100 * 1024;
//        ;
//        String dir = this.getExternalFilesDir("bitmap_monitor").getAbsolutePath();
//
//        BitmapMonitor.Config config = new BitmapMonitor.Config.Builder()
//                .checkRecycleInterval(checkInterval)    //检查图片是否被回收的间隔，单位：秒 （建议不要太频繁，默认 5秒）
//                .getStackThreshold(threshold)           //获取堆栈的阈值，当一张图片占据的内存超过这个数值后就会去抓栈
//                .restoreImageThreshold(restoreImageThreshold)   //还原图片的阈值，当一张图占据的内存超过这个数值后，就会还原出一张原始图片
//                .restoreImageDirectory(dir)             //保存还原后图片的目录
//                .showFloatWindow(true)                  //是否展示悬浮窗，可实时查看内存大小（建议只在 debug 环境打开）
//                .isDebug(true)
//                .context(this)
//                .build();
//        BitmapMonitor.init(config);
////        //5.主动 dump 数据
////        //获取所有数据
////        BitmapMonitorData bitmapAllData = BitmapMonitor.dumpBitmapInfo();
////        Log.d("bitmapmonitor", "bitmapAllData: " + bitmapAllData);
////
////        //仅获取数量和内存大小，不获取具体图片信息
////        BitmapMonitorData bitmapCountData = BitmapMonitor.dumpBitmapCount();
////        Log.d("bitmapmonitor", "bitmapCountData: " + bitmapCountData);
//        BitmapMonitor.addListener(new BitmapMonitor.BitmapInfoListener() {
//            @Override
//            public void onBitmapInfoChanged(BitmapMonitorData bitmapMonitorData) {
//                Log.d("bitmapmonitor", "onBitmapInfoChanged: " + bitmapMonitorData);
//            }
//        });
//    }

}
