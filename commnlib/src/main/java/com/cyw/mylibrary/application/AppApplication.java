package com.cyw.mylibrary.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.multidex.MultiDex;

import com.blankj.utilcode.util.SDCardUtils;
import com.blankj.utilcode.util.Utils;
import com.cyw.mylibrary.util.blockcanary.BlockCanary;

import org.xutils.x;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.onAdaptListener;
import me.jessyan.autosize.utils.AutoSizeLog;


/**
 * Created by cyw on 2020/8/3.
 */

public class AppApplication extends Application {
    private static Context context;
    private static AppApplication self;
    public static boolean isChangeTouType = false;
    private int sendCmd = 0;
    private int sendCmd2 = 0;
    private int bigSendCmd = 0;
    private int bigSendCmd2 = 0;
    public List<Activity> activitys = null;
    public static Activity sCurrentActivity;
    public boolean isSendSearchBarrel = false;
    //全区订单数据集合
    private static HashMap<String, String> mGangWeiData;

    public void setSendCmd(int cmd) {
        sendCmd = cmd;
    }

    public int getSendCmd() {
        return sendCmd;
    }

    public void setSendCmd2(int cmd) {
        sendCmd2 = cmd;
    }

    public int getSendCmd2() {
        return sendCmd2;
    }

    public int getBigSendCmd() {
        return bigSendCmd;
    }

    public int getBigSendCmd2() {
        return bigSendCmd2;
    }

    public void setBigSendCmd(int bigSendCmd) {
        this.bigSendCmd = bigSendCmd;
    }

    public void setBigSendCmd2(int bigSendCmd) {
        this.bigSendCmd2 = bigSendCmd;
    }

    public static HashMap<String, String> getmGangWeiData() {
        return mGangWeiData;
    }

    public int curNameDataBeanIndex = 0;
    public boolean isDishUp = false;
    public static int globalCmd = 1;
    //private FileListener fileListener;
    public static File dir2 = new File(SDCardUtils.getSDCardPathByEnvironment() + "/colorlink");

    /**
     * 单例模式中获取唯一的MyApplication实例 application会在进程最开始时候执行
     *
     * @return
     */
    public static AppApplication getInstance() {
        return self;
    }

    public static void setmGangWeiData(HashMap<String, String> mGangWeiData) {
        AppApplication.mGangWeiData = mGangWeiData;
    }

    public static HashMap<String, Integer> getmChuanCaiData() {
        return mChuanCaiData;
    }

    public static void setmChuanCaiData(HashMap<String, Integer> mChuanCaiData) {
        AppApplication.mChuanCaiData = mChuanCaiData;
    }

    //传菜线映射关系
    private static HashMap<String, Integer> mChuanCaiData;
    //心跳
    private static int mTimerHeart;

    public static int getmTimerHeart() {
        return mTimerHeart;
    }

    public static void setmTimerHeart(int mTimerHeart) {
        AppApplication.mTimerHeart = mTimerHeart;
    }


    @Override
    public void onCreate() {
        super.onCreate();
//        String dirPath = dir2.getAbsolutePath();
//        fileListener = new FileListener(dirPath);
//        fileListener.startWatching();
//        //GC抑制，延长GC阈值 启动优化
//        //system/lib64/libart.so // Android 10 以前系统，Android 10 之后换了位置
//        StartupOptimize.delayGC();
        x.Ext.init(this);
        Utils.init(this);
        // CrashReport.init(this);
        BlockCanary.install();//卡顿检测，定位到某行
        self = this;
        activitys = new LinkedList<Activity>();
        setTableNmaeEQId();
        context = getApplicationContext();
        mGangWeiData = new HashMap<>();
        mTimerHeart = 3;
       // initBitmapMonitor();
        //BitmapMonitor.start();
//        BitmapMonitor.addListener(new BitmapMonitor.BitmapInfoListener() {
//            @Override
//            public void onBitmapInfoChanged(final BitmapMonitorData data) {
//                Log.d("bitmapmonitor22", "onBitmapInfoChanged: " + data);
//            }
//        });

//        //当 App 中出现多进程, 并且您需要适配所有的进程, 就需要在 App 初始化时调用 initCompatMultiProcess()
//        //在 Demo 中跳转的三方库中的 DefaultErrorActivity 就是在另外一个进程中, 所以要想适配这个 Activity 就需要调用 initCompatMultiProcess()
//        AutoSize.initCompatMultiProcess(this);
//
//        //如果在某些特殊情况下出现 InitProvider 未能正常实例化, 导致 AndroidAutoSize 未能完成初始化
//        //可以主动调用 AutoSize.checkAndInit(this) 方法, 完成 AndroidAutoSize 的初始化后即可正常使用
////        AutoSize.checkAndInit(this);
//
////        如何控制 AndroidAutoSize 的初始化，让 AndroidAutoSize 在某些设备上不自动启动？https://github.com/JessYanCoding/AndroidAutoSize/issues/249
//
//        /**
//         * 以下是 AndroidAutoSize 可以自定义的参数, {@link AutoSizeConfig} 的每个方法的注释都写的很详细
//         * 使用前请一定记得跳进源码，查看方法的注释, 下面的注释只是简单描述!!!
//         */
        AutoSizeConfig.getInstance()

                //是否让框架支持自定义 Fragment 的适配参数, 由于这个需求是比较少见的, 所以须要使用者手动开启
                //如果没有这个需求建议不开启
                .setCustomFragment(true)

                //是否屏蔽系统字体大小对 AndroidAutoSize 的影响, 如果为 true, App 内的字体的大小将不会跟随系统设置中字体大小的改变
                //如果为 false, 则会跟随系统设置中字体大小的改变, 默认为 false
                .setExcludeFontScale(true)

                //区别于系统字体大小的放大比例, AndroidAutoSize 允许 APP 内部可以独立于系统字体大小之外，独自拥有全局调节 APP 字体大小的能力
                //当然, 在 APP 内您必须使用 sp 来作为字体的单位, 否则此功能无效, 不设置或将此值设为 0 则取消此功能
//                .setPrivateFontScale(0.8f)

                //屏幕适配监听器
                .setOnAdaptListener(new onAdaptListener() {
                    @Override
                    public void onAdaptBefore(Object target, Activity activity) {
                        //使用以下代码, 可以解决横竖屏切换时的屏幕适配问题
                        //使用以下代码, 可支持 Android 的分屏或缩放模式, 但前提是在分屏或缩放模式下当用户改变您 App 的窗口大小时
                        //系统会重绘当前的页面, 经测试在某些机型, 某些情况下系统不会主动重绘当前页面, 所以这时您需要自行重绘当前页面
                        //ScreenUtils.getScreenSize(activity) 的参数一定要不要传 Application!!!
//                        AutoSizeConfig.getInstance().setScreenWidth(ScreenUtils.getScreenSize(activity)[0]);
//                        AutoSizeConfig.getInstance().setScreenHeight(ScreenUtils.getScreenSize(activity)[1]);
                        AutoSizeLog.d(String.format(Locale.ENGLISH, "%s onAdaptBefore!", target.getClass().getName()));
                    }

                    @Override
                    public void onAdaptAfter(Object target, Activity activity) {
                        AutoSizeLog.d(String.format(Locale.ENGLISH, "%s onAdaptAfter!", target.getClass().getName()));
                    }
                })
//
//        //是否打印 AutoSize 的内部日志, 默认为 true, 如果您不想 AutoSize 打印日志, 则请设置为 false
////                .setLog(false)
//
//        //是否使用设备的实际尺寸做适配, 默认为 false, 如果设置为 false, 在以屏幕高度为基准进行适配时
//        //AutoSize 会将屏幕总高度减去状态栏高度来做适配
//        //设置为 true 则使用设备的实际屏幕高度, 不会减去状态栏高度
//        //在全面屏或刘海屏幕设备中, 获取到的屏幕高度可能不包含状态栏高度, 所以在全面屏设备中不需要减去状态栏高度，所以可以 setUseDeviceSize(true)
                .setUseDeviceSize(true)
//
                //是否全局按照宽度进行等比例适配, 默认为 true, 如果设置为 false, AutoSize 会全局按照高度进行适配
                .setBaseOnWidth(false);

//        //设置屏幕适配逻辑策略类, 一般不用设置, 使用框架默认的就好
////                .setAutoAdaptStrategy(new AutoAdaptStrategy())
        //  ;
        // customAdaptForExternal();
    }

    //    /**
//     * 给外部的三方库 {@link Activity} 自定义适配参数, 因为三方库的 {@link Activity} 并不能通过实现
//     * {CustomAdapt} 接口的方式来提供自定义适配参数 (因为远程依赖改不了源码)
//     * 所以使用 {ExternalAdaptManager} 来替代实现接口的方式, 来提供自定义适配参数
//     */
//    private void customAdaptForExternal() {
//        /**
//         * {@link ExternalAdaptManager} 是一个管理外部三方库的适配信息和状态的管理类, 详细介绍请看 {@link ExternalAdaptManager} 的类注释
//         */
//        AutoSizeConfig.getInstance().getExternalAdaptManager()
//
//                //加入的 Activity 将会放弃屏幕适配, 一般用于三方库的 Activity, 详情请看方法注释
//                //如果不想放弃三方库页面的适配, 请用 addExternalAdaptInfoOfActivity 方法, 建议对三方库页面进行适配, 让自己的 App 更完美一点
////                .addCancelAdaptOfActivity(DefaultErrorActivity.class)
//
//                //为指定的 Activity 提供自定义适配参数, AndroidAutoSize 将会按照提供的适配参数进行适配, 详情请看方法注释
//                //一般用于三方库的 Activity, 因为三方库的设计图尺寸可能和项目自身的设计图尺寸不一致, 所以要想完美适配三方库的页面
//                //就需要提供三方库的设计图尺寸, 以及适配的方向 (以宽为基准还是高为基准?)
//                //三方库页面的设计图尺寸可能无法获知, 所以如果想让三方库的适配效果达到最好, 只有靠不断的尝试
//                //由于 AndroidAutoSize 可以让布局在所有设备上都等比例缩放, 所以只要您在一个设备上测试出了一个最完美的设计图尺寸
//                //那这个三方库页面在其他设备上也会呈现出同样的适配效果, 等比例缩放, 所以也就完成了三方库页面的屏幕适配
//                //即使在不改三方库源码的情况下也可以完美适配三方库的页面, 这就是 AndroidAutoSize 的优势
//                //但前提是三方库页面的布局使用的是 dp 和 sp, 如果布局全部使用的 px, 那 AndroidAutoSize 也将无能为力
//                //经过测试 DefaultErrorActivity 的设计图宽度在 380dp - 400dp 显示效果都是比较舒服的
//                .addExternalAdaptInfoOfActivity(DefaultErrorActivity.class, new ExternalAdaptInfo(true, 400));
//    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }

//    private void initBitmapMonitor() {
//        long checkInterval = 10;
//        long threshold = 100 * 1024;
//        long restoreImageThreshold = 100 * 1024;
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
//        BitmapMonitor.start(new BitmapMonitor.CurrentSceneProvider() {
//            @Override
//            public String getCurrentScene() {
//                //返回当前顶部页面名称
//                if (sCurrentActivity != null) {
//                    return sCurrentActivity.getClass().getSimpleName();
//                }
//                return null;
//            }
//        });
//        //5.主动 dump 数据
//        //获取所有数据
//        BitmapMonitorData bitmapAllData = BitmapMonitor.dumpBitmapInfo();
//        Log.d("bitmapmonitor", "bitmapAllData: " + bitmapAllData);
//
//        //仅获取数量和内存大小，不获取具体图片信息
//        BitmapMonitorData bitmapCountData = BitmapMonitor.dumpBitmapCount();
//        Log.d("bitmapmonitor", "bitmapCountData: " + bitmapCountData);
//    }

    //自己理解  （只隐藏导航栏，不隐藏状态栏）
    public void init(View decorView) {
        int flag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE//① 一般和②配合使用 隐藏导航栏和状态栏
                // | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION//②内容设置为显示在导航栏的后面，这样内容就不会随着导航栏的隐藏和显示调整大小(加上时弹出框会使底部导航栏消失)
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN //③内容设置为显示在状态栏后面
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // ④hide导航栏 一般和⑤混合使用
//                | View.SYSTEM_UI_FLAG_FULLSCREEN //⑤ 隐藏状态栏
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        //判断当前版本在4.0以上并且存在虚拟按键，否则不做操作
        if (Build.VERSION.SDK_INT < 19 || !checkDeviceHasNavigationBar()) {
            //一定要判断是否存在按键，否则在没有按键的手机调用会影响别的功能。如之前没有考虑到，导致图传全屏变成小屏显示。
            return;
        }
        //自定义工具，设置状态栏颜色是透明
        //  ViewUtil.setWindowStatusBarColor(this,R.color.transparent);
        // 获取属性
        decorView.setSystemUiVisibility(flag);
    }

    //隐藏导航栏和状态栏（监听）
    public void hideDialogNavigationBar(View decorView) {
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);//默认隐藏导航栏
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |//①和②配合使用，隐藏导航栏和状态栏
//布局位于状态栏下方（内容显示在导航栏后面）
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |//②
//全屏（隐藏状态栏）
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
//隐藏导航栏
//内容显示在状态栏后面                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION   |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                if (Build.VERSION.SDK_INT >= 19) {
                    uiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                } else {
                    uiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
                }
                decorView.setSystemUiVisibility(uiOptions);
            }
        });
    }

    ///dialog需要全屏的时候，show前调用focusnotale，show后调用
    public void focusNotAle(Window window) {
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }

    public void clearFocusNotAle(Window window) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }

    /**
     * 判断是否存在虚拟按键
     *
     * @return
     */

    public boolean checkDeviceHasNavigationBar() {
        boolean hasNavigationBar = false;

        Resources rs = getResources();

        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");

        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);

        }

        try {
            Class<?> systemPropertiesClass = Class.forName("android.os.SystemProperties");

            Method m = systemPropertiesClass.getMethod("get", String.class);

            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");

            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;

            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;

            }

        } catch (Exception e) {
        }

        return hasNavigationBar;

    }

    public static Context getAppContext() {
        return context;
    }

    //    static {
////        System.loadLibrary("VSClinetT");
////        System.loadLibrary("Qt5Network");
//        //       System.loadLibrary("Qt5Test");
//        //      System.loadLibrary("Qt5Core");
//        //System.loadLibrary("Cypto");
//        System.loadLibrary("gnustl_shared");
//        System.loadLibrary("native-lib");
//    }
    //设置餐桌与传菜线设备ID对应表
    private void setTableNmaeEQId() {
        mChuanCaiData = new HashMap<>();
        mChuanCaiData.put("大厅001号桌", 15);
        mChuanCaiData.put("大厅002号桌", 16);
        mChuanCaiData.put("大厅003号桌", 17);
        mChuanCaiData.put("大厅005号桌", 25);
        mChuanCaiData.put("大厅006号桌", 24);
        mChuanCaiData.put("大厅008号桌", 23);
        mChuanCaiData.put("大厅009号桌", 22);
        mChuanCaiData.put("大厅010号桌", 21);
        mChuanCaiData.put("大厅011号桌", 20);
        mChuanCaiData.put("大厅012号桌", 19);
        mChuanCaiData.put("大厅013号桌", 18);
        mChuanCaiData.put("大厅015号桌", 7);
        mChuanCaiData.put("大厅016号桌", 6);
        mChuanCaiData.put("大厅019号桌", 9);
        mChuanCaiData.put("大厅020号桌", 11);
        mChuanCaiData.put("大厅021号桌", 8);
        mChuanCaiData.put("大厅022号桌", 10);
        mChuanCaiData.put("大厅023号桌", 14);
        mChuanCaiData.put("包间1号桌", 13);
        mChuanCaiData.put("包间2号桌", 12);
    }

    public void exit() {
        deleteActivity();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    // 添加Activity到容器中
    public void addActivitys(Activity activity) {
        if (activitys != null && activitys.size() > 0) {
            if (!activitys.contains(activity)) {
                activitys.add(activity);
            }
        } else {
            //activitys = new LinkedList<Activity>();
            activitys.add(activity);
        }

    }

    public void deleteActivity() {
        if (activitys != null && activitys.size() > 0) {
            for (Activity activity : activitys) {
                activity.finish();
            }
        }
    }

    public void removeActivity(Activity activity) {
        activitys.remove(activity);
    }


}
