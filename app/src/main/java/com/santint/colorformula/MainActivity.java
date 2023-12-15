package com.santint.colorformula;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bin.david.form.utils.DensityUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.cyw.mylibrary.application.AppApplication;
import com.cyw.mylibrary.base.BaseAppCompatActivity;
import com.cyw.mylibrary.bean.CannedBean;
import com.cyw.mylibrary.bean.ColorBean;
import com.cyw.mylibrary.bean.FormulaitemsBean;
import com.cyw.mylibrary.bean.MyFormula;
import com.cyw.mylibrary.bean.ResultBreak;
import com.cyw.mylibrary.bean.ResultData;
import com.cyw.mylibrary.customView.BaseAdapter;
import com.cyw.mylibrary.netrequest.utils.NetApplication;
import com.cyw.mylibrary.services.CannedResultService;
import com.cyw.mylibrary.services.ManualColorBeanService;
import com.cyw.mylibrary.services.MyFormulaService;
import com.cyw.mylibrary.services.ResultBreakService;
import com.cyw.mylibrary.services.ResultDataService;
import com.cyw.mylibrary.util.Byte2Hex;
import com.cyw.mylibrary.util.DateFormatter;
import com.cyw.mylibrary.util.GlobalConstants;
import com.cyw.mylibrary.util.IOSDialog;
import com.cyw.mylibrary.util.MyActivityManager;
import com.cyw.mylibrary.util.WriteLog;
import com.google.gson.reflect.TypeToken;
import com.net.netrequest.bean.MessageEventChuanCai;
import com.santint.colorformula.adapter.MainActivityLeftAdapter;
import com.santint.colorformula.bean.LeftBean;
import com.santint.colorformula.enums.BreakCodeEnum;
import com.santint.colorformula.fragment.StandFormulaFragment;
import com.santint.colorformula.fragment.TngFormulaFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.util.DensityUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseAppCompatActivity {
    private final int REQ_WRITE_STORAGE = 11;
    MyFormulaService myFormulaService;
    private MainActivityLeftAdapter leftAdapter;
    private LinearLayoutManager mManager;
    private RecyclerView ry;
    List<MyFormula> all = new ArrayList<>();
    private List<LeftBean> leftList = new ArrayList<>();
    private Toolbar toolbar;
    private View decorView;
    private IOSDialog iosDialog = null;
    List<Fragment> fragmentList = new ArrayList<>();
    //    private AddCannedFinishWindow addCannedFinishWindow;
    private int mCurPosition = 0;
    private long exitTime = 0;
    private ManualColorBeanService manualColorBeanService;
    private boolean isRefuse = false;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        decorView = getWindow().getDecorView();
        AppApplication.getInstance().init(decorView);
        setContentView(R.layout.activity_main);
        initScreen();
        ResultDataService.getInstance().delete(null);
        List<ResultData> allList = ResultDataService.getInstance().getAllList();
        List<ResultBreak> allList1 = ResultBreakService.getInstance().getAllList();
        List<CannedBean> allList2 = CannedResultService.getInstance().getAllList();
        Log.i("TAG", allList.size() + "hh" + allList1.size() + "hh" + allList2.size());

//        ResultData resultData = new ResultData();
//        resultData.setCmdCode("020603");
//        resultData.setTime("2023-10-15 16:42:00");
//        resultData.setCmdDes("新增测试");
//        ResultDataService.getInstance().save(resultData);
//
//
//        ResultData resultData2 = new ResultData();
//        resultData2.setCmdCode("020603");
//        resultData2.setTime("2023-10-13 16:42:00");
//        resultData2.setCmdDes("新增测试2");
//        ResultDataService.getInstance().save(resultData2);
//
//
//        ResultData resultData3 = new ResultData();
//        resultData3.setCmdCode("020603");
//        resultData3.setTime("2023-10-31 16:42:00");
//        resultData3.setCmdDes("新增测试3");
//        ResultDataService.getInstance().save(resultData3);
//        List<ResultData> allList3 = ResultDataService.getInstance().getAllList();
//        Log.i("TAG",allList3.size()+"HHHHH");

        // ResultDataService.getInstance().delete(null);
//        CannedBean cannedBean = new CannedBean();
//        cannedBean.setBarrelNo("1");
//        cannedBean.setCannedRealValue("2");
//        cannedBean.setCannedTargetValue("3");
//        cannedBean.setFormulaName("111");
//        CannedResultService.getInstance().save(cannedBean);
//        try {
//            Thread.sleep(100);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        List<CannedBean> allList = CannedResultService.getInstance().getAllList();
//        Log.i("TAG",allList.size()+"hhhhh");
//        //需要注意
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
//            UtilsWithPermission.readWriteStorage(MainActivity.this, REQ_WRITE_STORAGE);
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !isRefuse) {// android 11  且 不是已经被拒绝
//            // 先判断有没有权限
//            if (!Environment.isExternalStorageManager()) {
//                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
//                intent.setData(Uri.parse("package:" + getPackageName()));
//                startActivityForResult(intent, 1024);
//            }
//        }

//        permissionRequest();
        myFormulaService = new MyFormulaService(this);
        // initService();
        manualColorBeanService = new ManualColorBeanService(this);
        //initService2();
        initStandData();

        EventBus.getDefault().register(this);
        initView();
        initList();
        initFragment();
//        默认选中页面1
        chooseListItem(0);
        //fun();
    }

    private void initStandData() {
        all = myFormulaService.getAll();
        // List<ManualColorBean> manualColorBeans = manualColorBeanService.getAll();
    }

//    // 带回授权结果
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1024 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            // 检查是否有权限
//            if (Environment.isExternalStorageManager()) {
//                isRefuse = false;
//                // 授权成功
//            } else {
//                isRefuse = true;
//                // 授权失败
//            }
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

//    @Permission(value = {Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode = 200)
//    public void permissionRequest() {
//        Toast.makeText(this, "权限申请成功...", Toast.LENGTH_SHORT).show();
//    }
//
//    @PermissionCancel
//    public void permissionCancel() {
//        Toast.makeText(this, "权限被拒绝", Toast.LENGTH_SHORT).show();
//    }
//
//    // 多次拒绝，还勾选了“不再提示”
//    @PermissionDenied
//    public void permissionDenied() {
//        Toast.makeText(this, "权限被拒绝(用户勾选了 不再提示)，注意：你必须要去设置中打开此权限，否则功能无法使用", Toast.LENGTH_SHORT).show();
//    }


    private void initScreen() {
        //获取屏幕实际分辨率
        WindowManager windowManager = getWindow().getWindowManager();
        Point point = new Point();
        windowManager.getDefaultDisplay().getRealSize(point);
        int width = point.x;
        int height = point.y;
        //获取屏幕可用分辨率
        Point point1 = new Point();
        windowManager.getDefaultDisplay().getSize(point1);
        int width1 = point1.x;
        int height1 = point1.y;
        float dpWidth = DensityUtils.px2dp(getApplicationContext(), width);//有小数
        float dpHeight = DensityUtils.px2dp(getApplicationContext(), height);//有小数
        float dpWidth2 = DensityUtil.px2dip(width);//取整
        float dpHeight2 = DensityUtil.px2dip(height);//取整
        Log.i("TAGWWidth", width + "hhh" + width1 + "hh" + height + "hhh" + height1 + "dp宽度" + dpWidth + "dp高度" + dpHeight + "hh" + dpWidth2 + "hhh" + dpHeight2);
    }

    @Override
    protected void onResume() {
        super.onResume();
        chooseListItem(mCurPosition);
        //all = myFormulaService.getAll();

    }

    private void initFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        StandFormulaFragment standFormulaFragment = new StandFormulaFragment();
        TngFormulaFragment tngFormulaFragment = new TngFormulaFragment();
        fragmentList.add(standFormulaFragment);
        fragmentList.add(tngFormulaFragment);
        for (int i = 0; i < fragmentList.size(); i++) {
            transaction.add(R.id.mFrame, fragmentList.get(i));
        }
        transaction.commit();
    }

    private void initList() {
        LeftBean leftBean = new LeftBean();
        leftBean.setName("标准配方");
        leftBean.setCheck(true);
        LeftBean leftBean1 = new LeftBean();
        leftBean1.setName("配方配料");
        leftBean1.setCheck(false);
        leftList.add(leftBean);
        leftList.add(leftBean1);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        //toolbar.getLogo().setAutoMirrored(true);
        //toolbar.getLogo().setBounds(50,50,200,200);
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_three_big));
        Drawable drawable = toolbar.getOverflowIcon();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);//是否显示返回键
        toolbar.getTitle();
//        toolbar.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                String versionName = String.valueOf(AppVersionUtils.getVersionName(MainActivity.this));
////                if (versionName != null && !"".equals(versionName)) {
////                    tvVersion.setText("三华餐饮Android版-" + versionName);
////                }
//                ToastUtils.showLong("三华科技Android版本" + "1.6.9");//versionName
//                ToastUtils.setMsgTextSize(18);
//                return false;
//            }
//        });
        ry = findViewById(R.id.ry_stand_formula);
        bindAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
//        MenuItem searchItem = menu.findItem(R.id.action_search);
//        SearchView searchView =
//                (SearchView) searchItem.getActionView();
//
//        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
//            @Override
//            public boolean onMenuItemActionExpand(MenuItem menuItem) {
//                return true;
//            }
//
//            @Override
//            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
//                return true;
//            }
//        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        AppApplication.getInstance().hideDialogNavigationBar(decorView);
        switch (item.getItemId()) {
            case R.id.action_correct:
                //if (!isMaking && !isMakingR) {
                Intent intent = new Intent(MainActivity.this, CorrectionActivity.class);
                startActivityAni(intent);
//                } else {
//                    ToastUtils.showShort("菜品制做中，不可操作其他页面");
//                    ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//                    ToastUtils.setBgResource(R.color.gray);
//                    ToastUtils.setMsgTextSize(18);
//                }

                return true;

//            case R.id.action_formula:
//                Intent intent_formula = new Intent(MainActivity.this, FormulaActivity.class);
//                startActivity(intent_formula);
//                return true;
            case R.id.action_manual:
//                Intent intent_manual = new Intent(MainActivity.this, ManualOperateActivity.class);
//                startActivity(intent_manual);
                jumpManual();
                return true;
            case R.id.action_my_formula:
                jumpMyFormla();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveMessage2(MessageEventChuanCai event) {
        byte[] result = new byte[1024];
        byte[] result2 = event.getResult();
        System.arraycopy(result2, 0, result, 0, result2.length);
        String resultShi = Byte2Hex.bytes2HexString(result);
        String time = DateFormatter.getCurrentTime();
        WriteLog.writeTxtToFile(time + "回复数据" + resultShi);
        /////////保存结果数据
        ResultData resultData = new ResultData();
        resultData.setTime(time);
        resultData.setCmdDes("回复数据");
        resultData.setCmdCode(resultShi);
        ResultDataService.getInstance().save(resultData);
        /////////保存结果数据

        int[] ints = new int[result.length];
        char[] chars = new char[result.length];
        for (int i = 0; i < result.length; i++) {
            byte b1 = result[i];
            int aInt = Byte2Hex.byteToInteger(b1);
            ints[i] = aInt;
            if (aInt > 127) {
                chars[i] = (char) (256 - aInt);
            } else {
                chars[i] = (char) aInt;
            }
        }
        WriteLog.writeTxtToFile("------------------ints" + ints[1] + "jjjj" + ints[2] + "dfdf" + ints[3]);
        ResultBreak resultBreak = new ResultBreak();
        if (ints[1] == 6) {
            Bundle bundle = new Bundle();
            bundle.putString("cmdMark", "cmdMark");
            bundle.putIntArray("ints", ints);
            EventBus.getDefault().post(bundle);
        } else if (ints[1] == 7) {//调色机忙 020703
            //////////////////////保存故障
            resultBreak.setTime(DateFormatter.getCurrentTime());
            resultBreak.setBreakCode(resultShi);
            resultBreak.setBreakDes("E07:调色机忙");
            ResultBreakService.getInstance().save(resultBreak);
            //////////////////////保存故障
            Bundle bundle = new Bundle();
            bundle.putString("cmdMark", "machineBusy");
            bundle.putIntArray("ints", ints);
            EventBus.getDefault().post(bundle);
        } else if (ints[1] == 8) { //校验和错误 020803
            //////////////////////保存故障
            resultBreak.setTime(DateFormatter.getCurrentTime());
            resultBreak.setBreakCode(resultShi);
            resultBreak.setBreakDes("E08:校验和错误");
            ResultBreakService.getInstance().save(resultBreak);
            //////////////////////保存故障
            Bundle bundle = new Bundle();
            bundle.putString("cmdMark", "checkSumError");
            bundle.putIntArray("ints", ints);
            EventBus.getDefault().post(bundle);
        } else if (ints[1] == 9) {
            if (ints[2] == 3) {

            }
        } else if (ints[1] == 83) {/////////////////////////字符s
            if (ints[2] == 70) {//字符F
                if (ints[3] == 83) {//字符s
                    //////////SFS 罐装成功完成命令
                    Bundle bundle = new Bundle();
                    bundle.putString("cmdMark", "cannedSuccess");
                    bundle.putIntArray("ints", ints);
                    EventBus.getDefault().post(bundle);
                } else if (ints[3] == 80) {//字符p
                    /////////////SFP 进度反馈
                    Bundle bundle = new Bundle();
                    bundle.putString("workState", "workState");
                    bundle.putIntArray("ints", ints);
                    EventBus.getDefault().post(bundle);
                }
            } else if (ints[2] == 83) {//字符s
                if (ints[3] == 73) {//字符I
                    //////////////////SSI  消除弹窗
                    handleClearPop(resultBreak, ints);
                }
            }

        } else if (ints[1] == 69) {///////////////////////字符E
            if (ints[2] == 71) {//字符G
                if (ints[3] == 73) {//字符I
                    //////////////////EGI   未找到调色对应的桶
                    handleBarrelNotFind(ints, chars);
                }
            } else if (ints[2] == 69) {//字符E
                if (ints[3] == 73) {//字符I
                    /////////////////EEI  弹出弹窗
                    handlePop(resultBreak, ints);

                }
            } else if (ints[2] == 70) {//字符F
                if (ints[3] == 83) { //字符S
                    //////////////////EFS  罐装失败结束命令
                    handleCannedFail(resultBreak, ints);
                }
            }
        } else if (ints[1] == 24) { //心跳回复  16进制为18
            NetApplication.inetrequestInterface.heartBack();

        }
    }

    //////////////////SSI  消除弹窗
    private void handleClearPop(ResultBreak resultBreak, int[] ints) {
        String exceptionCode = getExceptrionCode(ints);
        String exceptionMsg = getExceptionMsg(exceptionCode);
        //////////////////////保存故障
        resultBreak.setTime(DateFormatter.getCurrentTime());
        resultBreak.setBreakCode(exceptionCode);
        resultBreak.setBreakDes(exceptionMsg);
        ResultBreakService.getInstance().save(resultBreak);
        //////////////////////保存故障
        if (exceptionMsg.equals("急停开关并未打开")) {
            if (iosDialog != null) {
                iosDialog.dismiss();
            }
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("clearWindow", "clearWindow");
            //bundle.putIntArray("ints", ints);
            EventBus.getDefault().post(bundle);
        }

//                    if (iosDialog != null) {
//                        iosDialog.dismiss();
//                    }
    }

    //////////////////EGI   未找到调色对应的桶
    private void handleBarrelNotFind(int[] ints, char[] chars) {
        int length = ints.length;
        int tongShu = 0;
        if (ints[4] == 48) {
            tongShu = Integer.parseInt(String.valueOf(chars[5]));
        } else if (ints[4] == 49) {
            tongShu = 10 + Integer.parseInt(String.valueOf(chars[5]));
        }
        List<List<Character>> tongList = new ArrayList<>();
        int repeatCount = 0;
        int nextIndex = 0;
        int repeatStart = 0;
        List<Character> integers = new ArrayList<>();
        //桶遍历获取
        for (int i = 6; i < 6 + 3 * tongShu; i++) {
            repeatStart = 6 + 3 * repeatCount;
            if (i == repeatStart) {
                integers = new ArrayList<>();
                integers.add(chars[i]);
                repeatCount++;

            } else {
                integers.add(chars[i]);
                if (i == 5 + 3 * repeatCount) {
                    tongList.add(integers);
                }
            }
        }
        String tongStr = "";
        //拿到开始分发
        if (tongList != null && tongList.size() > 0) {
            for (int j = 0; j < tongList.size(); j++) {
                List<Character> integers1 = tongList.get(j);
                for (int k = 0; k < 3; k++) {
                    int val = Integer.parseInt(String.valueOf(integers1.get(k)));
                    if (k == 1) {
                        if (val != 0) {
                            tongStr += (val + "");
                        }

                    } else if (k == 2) {
                        tongStr += (val + ",");
                    }

                }
            }
        }
        tongStr += "号桶未找到";
        Bundle bundle = new Bundle();
        bundle.putString("manualOperateTong", tongStr);
        EventBus.getDefault().post(bundle);
    }

    /////////////////EEI  弹出弹窗
    private void handlePop(ResultBreak resultBreak, int[] ints) {
        //if ((System.currentTimeMillis() - exitTime) > 5000) {
        String exceptionCode = getExceptrionCode(ints);
        String exceptionMsg = getExceptionMsg(exceptionCode);
        //////////////////////保存故障
        resultBreak.setTime(DateFormatter.getCurrentTime());
        resultBreak.setBreakCode(exceptionCode);
        resultBreak.setBreakDes(exceptionMsg);
        ResultBreakService.getInstance().save(resultBreak);
        //////////////////////保存故障
        if (exceptionMsg.equals(BreakCodeEnum.Emegency.getDescription())) {
            Activity currentActivity = MyActivityManager.getInstance().getCurrentActivity();
            errorCodeDialog(exceptionMsg, currentActivity);
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("manualOperateError", exceptionMsg);
            EventBus.getDefault().post(bundle);
            // exitTime = System.currentTimeMillis();
        }
        //}
    }

    //////////////////EFS  罐装失败结束命令
    private void handleCannedFail(ResultBreak resultBreak, int[] ints) {
        if (ints[4] == 48 && ints[5] == 50) {//02 主动终止罐装命令回复

        } else {
            String exceptionCode = getExceptrionCode(ints);
            String exceptionMsg = getExceptionMsg(exceptionCode);
            //////////////////////保存故障
            resultBreak.setTime(DateFormatter.getCurrentTime());
            resultBreak.setBreakCode(exceptionCode);
            resultBreak.setBreakDes(exceptionMsg);
            ResultBreakService.getInstance().save(resultBreak);
            //////////////////////保存故障
            if (exceptionMsg.equals("急停开关并未打开")) {
                Activity currentActivity = MyActivityManager.getInstance().getCurrentActivity();
                errorCodeDialog(exceptionMsg, currentActivity);
            } else {
                Bundle bundle = new Bundle();
                bundle.putString("manualOperateError", exceptionMsg);
                EventBus.getDefault().post(bundle);
            }


        }
    }

    private void errorCodeDialog(String errorMsg, Activity currentActivity) {
        iosDialog = new IOSDialog.Builder(
                currentActivity)
                .setBtnColor(getResources().getColor(R.color.blue))
                .setColor(getResources().getColor(R.color.blue))
                .setMessage(errorMsg)
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(
                                    DialogInterface dialog,
                                    int which) {
                                if (iosDialog != null) {
                                    iosDialog.dismiss();
                                }
                            }
                        })
                .create();
        iosDialog.show();
    }

    private String getExceptrionCode(int[] ints) {
        String exception = "";
        if (ints[4] == 48) {
            exception += "0";
        } else if (ints[4] == 49) {
            exception += "1";
        } else if (ints[4] == 56) {
            exception += "8";
        } else if (ints[4] == 57) {
            exception += "9";
        }
        if (ints[5] == 48) {
            exception += "0";
        } else if (ints[5] == 49) {
            exception += "1";
        } else if (ints[5] == 50) {
            exception += "2";
        } else if (ints[5] == 51) {
            exception += "3";
        } else if (ints[5] == 52) {
            exception += "4";
        } else if (ints[5] == 53) {
            exception += "5";
        } else if (ints[5] == 54) {
            exception += "6";
        } else if (ints[5] == 55) {
            exception += "7";
        } else if (ints[5] == 56) {
            exception += "8";
        }
        return exception;
    }

    private String getExceptionMsg(String exception) {
        String exceptionMsg = "";

        if (exception.equals("01")) {
            exceptionMsg = BreakCodeEnum.Emegency.getDescription();
        } else if (exception.equals("02")) {
            exceptionMsg = BreakCodeEnum.StopCannedFail.getDescription();
        } else if (exception.equals("04")) {
            exceptionMsg = BreakCodeEnum.DoorOpen.getDescription();

        } else if (exception.equals("11")) {
            exceptionMsg = BreakCodeEnum.BarrelNot.getDescription();

        } else if (exception.equals("05")) {
            exceptionMsg = BreakCodeEnum.SpliceBoxBreak.getDescription();

        } else if (exception.equals("80")) {
            exceptionMsg = BreakCodeEnum.MotorBreak.getDescription();

        } else if (exception.equals("81")) {
            exceptionMsg = BreakCodeEnum.TurnMotorBreak.getDescription();

        } else if (exception.equals("82")) {
            exceptionMsg = BreakCodeEnum.EncodeBreak.getDescription();

        } else if (exception.equals("84")) {
            exceptionMsg = BreakCodeEnum.TelescopicBreak.getDescription();

        } else if (exception.equals("86")) {
            exceptionMsg = BreakCodeEnum.ElectronicBreak.getDescription();

        } else if (exception.equals("87")) {
            exceptionMsg = BreakCodeEnum.ElectronicOver.getDescription();

        } else if (exception.equals("88")) {
            exceptionMsg = BreakCodeEnum.ElectronicUnder.getDescription();

        } else if (exception.equals("85")) {
            exceptionMsg = BreakCodeEnum.EmptyOver.getDescription();

        } else if (exception.equals("90")) {
            exceptionMsg = BreakCodeEnum.BarcodeScanner.getDescription();
        } else if (exception.equals("91")) {
            exceptionMsg = BreakCodeEnum.OutletSealing.getDescription();
        } else if (exception.equals("92")) {
            exceptionMsg = BreakCodeEnum.BarrelRemoved.getDescription();
        }else if(exception.equals("94")){
            exceptionMsg = BreakCodeEnum.ValveBreak.getDescription();
        }
        return exceptionMsg;
    }

    public void jumpManual() {
        if (all != null && all.size() > 0) {
            MyFormula myFormula = all.get(0);
            List<ColorBean> colorBeans = getCommonColorBeans(myFormula);
            Intent intent = new Intent(this, ManualOperateActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("colorBeans", (Serializable) colorBeans);
            intent.putExtras(bundle);
            startActivityAni(intent);
        }

    }
    private List<ColorBean> getCommonColorBeans(MyFormula myFormula) {
        List<ColorBean> colorBeans = new ArrayList<>();
        if (myFormula.getColorformula() != null) {
            FormulaitemsBean formulaitems = myFormula.getColorformula().getFormulaitems();
            if (formulaitems != null) {
//                if (formulaitems instanceof FormulaitemsBean) {
//                    colorBeans = ((FormulaitemsBean) formulaitems).getColorBeans();
//                } else if (formulaitems instanceof FormulaitemsBean2) {
//                    ColorBean colorBean = ((FormulaitemsBean2) formulaitems).getColorBean();
//                    colorBeans.add(colorBean);
//                }
                colorBeans = getCommonColorType(formulaitems);
            }
        }
        return colorBeans;
    }
    private List<ColorBean> getCommonColorType(FormulaitemsBean formulaitems) {
        List<ColorBean> colorBeanList = new ArrayList<>();
        if (GlobalConstants.isManual) {
            if (formulaitems.getColorBeans() instanceof List) {
                colorBeanList = (List<ColorBean>) formulaitems.getColorBeans();
            } else {
                ColorBean colorBean = (ColorBean) formulaitems.getColorBeans();
                colorBeanList.add(colorBean);
            }
        } else {
            if (formulaitems.getColorBeans() instanceof List) {
                colorBeanList = GsonUtils.fromJson(formulaitems.getColorBeans() + "", new TypeToken<List<ColorBean>>() {
                }.getType());
            } else {
                ColorBean colorBean = GsonUtils.fromJson(formulaitems.getColorBeans() + "", new TypeToken<ColorBean>() {
                }.getType());
                colorBeanList.add(colorBean);
            }
        }

        return colorBeanList;
    }


    public void jumpMyFormla() {
//        MyFormula myFormula = all.get(0);
//        List<ColorBean> colorBeans = myFormula.getColorBeans();
        Intent intent = new Intent(this, MyFormulaActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("colorBeans", (Serializable) colorBeans);
//        intent.putExtras(bundle);
        startActivityAni(intent);
    }

    private void bindAdapter() {
        mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        mManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                return 2;
//            }
//        });
        ry.setLayoutManager(mManager);
        leftAdapter = new MainActivityLeftAdapter(this, leftList);
        leftAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //        点击item切换fragment
                mCurPosition = position;
                chooseListItem(position);
                for (int i = 0; i < leftList.size(); i++) {
//            先把所有的item标记为未被选中
                    leftList.get(i).setCheck(false);
                }
//        找出被选中的item，把user中的ischecked属性改为true
                leftList.get(position).setCheck(true);
//        刷新适配器
                leftAdapter.notifyDataSetChanged();
            }
        });
        ry.setAdapter(leftAdapter);
    }

    //    切换fragment
    private void chooseListItem(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        隐藏所有的fragment
        for (int i = 0; i < fragmentList.size(); i++) {
            Fragment fragment = fragmentList.get(i);
            transaction.hide(fragment);
        }
        transaction.show(fragmentList.get(position));
        transaction.commit();
    }

    int[] colorArray = {R.color.green, R.color.colorOdd, R.color.blue_btn_normal, R.color.yellow_fooddetail_cart_add,
            R.color.Khaki, R.color.Gold, R.color.color_split_line_d9d9d9, R.color.purple_200, R.color.Coral,
            R.color.Orchid, R.color.Teal, R.color.BlanchedAlmond};

//    private void initService() {
//        for (int i = 0; i < 12; i++) {
//            MyFormula myFormula = new MyFormula();
//            myFormula.setFormulaNo("color" + i + 1);
//            myFormula.setFormulaName("color" + i + 1);
//            myFormula.setProductName("product" + i);
//            myFormula.setColor(String.valueOf(colorArray[i]));
////            if (i == 0) {
////                myFormula.setColor(String.valueOf(R.color.green));
////            } else if (i == 1) {
////                myFormula.setColor(String.valueOf(R.color.colorOdd));
////            } else if (i == 2) {
////                myFormula.setColor(String.valueOf(R.color.blue_btn_normal));
////            } else if (i == 3) {
////                myFormula.setColor(String.valueOf(R.color.yellow_fooddetail_cart_add));
////            } else if (i == 4) {
////                myFormula.setColor(String.valueOf(R.color.purple_200));
////            } else if (i == 5) {
////                myFormula.setColor(String.valueOf(R.color.colorOdd));
////            } else if (i == 6) {
////                myFormula.setColor(String.valueOf(R.color.blue));
////            } else if (i == 7) {
////                myFormula.setColor(String.valueOf(R.color.color_split_line_d9d9d9));
////            } else if (i == 8) {
////                myFormula.setColor(String.valueOf(R.color.Gold));
////            } else if (i == 9) {
////                myFormula.setColor(String.valueOf(R.color.Khaki));
////            }
//            List<ColorBean> colorBeanList = new ArrayList<>();
//            for (int j = 0; j < i + 1; j++) {
//                ColorBean colorBean = new ColorBean();
//                colorBean.setColorNo("A" + String.valueOf(j + 1));
//                colorBean.setColoName("A" + String.valueOf(j + 1));
//                colorBean.setColorDos("0.01");//String.valueOf(11 + j)
//                colorBean.setColor(String.valueOf(colorArray[j]));
//                colorBeanList.add(colorBean);
//            }
//            myFormula.setColorBeans(colorBeanList);
//            myFormulaService.put(myFormula);
//        }
//    }

//    private void initService2() {
////        manualColorBeanService = new ManualColorBeanService(MainActivity.this);
//        List<ColorBean> colorBeanList = new ArrayList<>();
//        for (int j = 0; j < 12; j++) {
//            ColorBean manualColorBean = new ColorBean();
//            manualColorBean.setColorNo("A" + String.valueOf(j + 1));
//            manualColorBean.setColoName("A" + String.valueOf(j + 1));
//            //manualColorBean.setColorDos(String.valueOf(11 + j));
//            if (j == 0) {
//                manualColorBean.setColor(String.valueOf(R.color.green));
//            } else if (j == 1) {
//                manualColorBean.setColor(String.valueOf(R.color.colorOdd));
//            } else if (j == 2) {
//                manualColorBean.setColor(String.valueOf(R.color.blue_btn_normal));
//            } else if (j == 3) {
//                manualColorBean.setColor(String.valueOf(R.color.yellow_fooddetail_cart_add));
//            } else if (j == 4) {
//                manualColorBean.setColor(String.valueOf(R.color.Khaki));
//            } else if (j == 5) {
//                manualColorBean.setColor(String.valueOf(R.color.Gold));
//            } else if (j == 6) {
//                manualColorBean.setColor(String.valueOf(R.color.color_split_line_d9d9d9));
//            } else if (j == 7) {
//                manualColorBean.setColor(String.valueOf(R.color.purple_200));
//            } else if (j == 8) {
//                manualColorBean.setColor(String.valueOf(R.color.Coral));
//            } else if (j == 9) {
//                manualColorBean.setColor(String.valueOf(R.color.Orchid));
//            } else if (j == 10) {
//                manualColorBean.setColor(String.valueOf(R.color.Teal));
//            } else if (j == 11) {
//                manualColorBean.setColor(String.valueOf(R.color.BlanchedAlmond));
//            }
//            //colorBeanList.add(manualColorBean);
//
//            manualColorBeanService.put(manualColorBean);
//        }
//    }
}