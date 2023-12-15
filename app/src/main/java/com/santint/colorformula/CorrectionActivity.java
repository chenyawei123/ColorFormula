package com.santint.colorformula;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.GsonUtils;
import com.cyw.mylibrary.application.AppApplication;
import com.cyw.mylibrary.base.BaseAppCompatActivity;
import com.cyw.mylibrary.bean.ColorBean;
import com.cyw.mylibrary.bean.ColorDosBean;
import com.cyw.mylibrary.bean.DDParamsBean;
import com.cyw.mylibrary.bean.FmParamsBean;
import com.cyw.mylibrary.bean.FormulaitemsBean;
import com.cyw.mylibrary.bean.MyFormula;
import com.cyw.mylibrary.bean.PumpConfigBean;
import com.cyw.mylibrary.bean.ResultBreak;
import com.cyw.mylibrary.bean.ResultData;
import com.cyw.mylibrary.netrequest.utils.NetApplication;
import com.cyw.mylibrary.services.FMServiceNew;
import com.cyw.mylibrary.services.MyFormulaService;
import com.cyw.mylibrary.services.PumpConfigBeanService;
import com.cyw.mylibrary.services.ResultBreakService;
import com.cyw.mylibrary.services.ResultDataService;
import com.cyw.mylibrary.util.Byte2Hex;
import com.cyw.mylibrary.util.DateFormatter;
import com.cyw.mylibrary.util.GlobalConstants;
import com.cyw.mylibrary.util.IOSDialog;
import com.cyw.mylibrary.util.MyActivityManager;
import com.cyw.mylibrary.util.MyToastUtil;
import com.cyw.mylibrary.util.SaveJsonFile;
import com.cyw.mylibrary.util.SharedPreferencesHelper;
import com.cyw.mylibrary.util.WriteLog;
import com.example.datacorrects.R;
import com.example.datacorrects.bean.MessageEventMainTean;
import com.example.datacorrects.bean.Tab;
import com.example.datacorrects.fragment.CorrectDataManager;
import com.example.datacorrects.fragment.FragmentTabHost;
import com.example.datacorrects.fragment.MainTenance;
import com.example.datacorrects.fragment.PumpConfig;
import com.example.datacorrects.fragment.SystemSet;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.reflect.TypeToken;
import com.net.netrequest.bean.MessageEventChuanCai;
import com.santint.colorformula.enums.BreakCodeEnum;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *
 */

public class CorrectionActivity extends BaseAppCompatActivity implements TabHost.OnTabChangeListener {
    private ImageView img;
    private TextView text;
    public static int santintIndex = -1;
    private final boolean isPause = false;

    private InputMethodManager imm;
    private FragmentTabHost mTabHost;
    private LayoutInflater mInflater;
    private final List<Tab> mTabs = new ArrayList<Tab>(7);
    private MainTenance mainTenance;

    private PumpConfig pumpConfig;
    private CorrectDataManager correctParam;
    //private ManualOperation manualOperation;
    private final long exitTime = 0;
    public static CorrectionActivity instance = null;
    private boolean flag;
    private static boolean mIsReceivedOff;
    private static final boolean mIsReceived = false;
    private static final int offCount = 0;
    private static final boolean isOrder = false;//检验是否已提交或取消订单

    private int markMain = 0;

    private final int REQ_WRITE_STORAGE = 11;
    public static int POINT_COLUMN_COUNT = 4;
    private FrameLayout tabContent;
    private Toolbar toolbar;
    private View view1, view2, view3, view4, view5;
    private IOSDialog iosDialog;
    private MyFormulaService myFormulaService;
    private PumpConfigBeanService correctConfigService;
    private List<PumpConfigBean> mPumpConfigBeans = new ArrayList<>();
    private FMServiceNew fmService;
    private List<FmParamsBean> mFmParamsBeans = new ArrayList<>();

    private View decorView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setCustomDensity(this,this.getApplication());
        //AndroidBug5497Workaround.assistActivity(this);
        EventBus.getDefault().register(this);
        setContentView(R.layout.layout_correction);
        decorView = getWindow().getDecorView();
        AppApplication.getInstance().init(decorView);
        //AppApplication.getInstance().hideDialogNavigationBar(decorView);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_three_big));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        instance = this;
        //UtilsWithPermission.readWriteStorage(CorrectionActivity.this, REQ_WRITE_STORAGE);
        //SantintApplication.getInstance().addActivitys(this);
        AppApplication.getInstance().addActivitys(this);
        markMain = getIntent().getIntExtra("markMain", 0);
        //acquireWakeLock();
        // StatusBarUtil.setColor(CorrectionActivity.this, getResources().getColor(R.color.white));
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        this.getWindow().getDecorView().setBackgroundResource(R.color.white);//transparent
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // initData();
        //SantintApplication.getInstance().addActivitys(this);
        initTab();
        boolean isBusy = SaveJsonFile.readBusyFile("busy.xml");
        if (!isBusy) {
            GlobalConstants.isManual = false;
        } else {
            GlobalConstants.isManual = true;
        }
        initService();
        getStandardColorBeans();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (!GlobalConstants.isManual) {
            initThread();
        }

    }

    private void initService() {
        myFormulaService = new MyFormulaService(this);
        correctConfigService = PumpConfigBeanService.getInstance();
        mPumpConfigBeans = correctConfigService.getAllListByType();
        fmService = FMServiceNew.getInstance();
        mFmParamsBeans = fmService.getAllListByType();
    }

    private void initThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                startCmd();

            }
        }).start();
//        ReadFormulaThread.getInstance(CorrectionActivity.this).start();
        // OneTimeThread.getInstance(CorrectionActivity.this).start();
    }

    private int task = 1;
    FmParamsBean mFmParamsBean;
    DDParamsBean mDdParamsBean;
    PumpConfigBean mPumpConfigBean;
    private String cannedPrecision = "";
    String mTong = ""; //发送
    private int ddSendIndex = 0;
    private List<DDParamsBean> ddSendList = new ArrayList<>();
    String targetDos = "";
    private double scale = 1;
    private List<ColorDosBean> hasDosList = new ArrayList<>();
    private List<ColorBean> mManualColorBeans = new ArrayList<>();
    private int taskCount = 1;
    private String leftWeightName = "";
    private String leftAlarm = "";
    private String cannedPrecisionDes = "";
    private List<MyFormula> myFormulas = new ArrayList<>();
    private String innerCode;

    private void getStandardColorBeans() {
        if (mManualColorBeans != null && mManualColorBeans.size() > 0) {
            mManualColorBeans.clear();
        }
        myFormulas = myFormulaService.getAll();
        if (myFormulas != null && myFormulas.size() > 0) {
            // MyFormula myFormula1 = myFormulas.get(0);
            //editTongshu.setText(myFormula1.getColorformula().getFormula().getBarrels());
            for (int i = 0; i < myFormulas.size(); i++) {
                MyFormula myFormula = myFormulas.get(i);
                FormulaitemsBean formulaitems = myFormula.getColorformula().getFormulaitems();
                innerCode = myFormula.getColorformula().getFormula().getInnercolorcode();
                List<ColorBean> colorBeans = new ArrayList<>();
//                if (formulaitems instanceof FormulaitemsBean) {
//                    colorBeans = ((FormulaitemsBean) formulaitems).getColorBeans();
//
//                } else if (formulaitems instanceof FormulaitemsBean2) {
//                    ColorBean colorBean = ((FormulaitemsBean2) formulaitems).getColorBean();
//                    colorBeans.add(colorBean);
//                }
                colorBeans = getCommonColorType(formulaitems);
                List<PumpConfigBean> pumpConfigBeans = new ArrayList<>();
                if (colorBeans != null && colorBeans.size() > 0) {
                    for (int i1 = 0; i1 < colorBeans.size(); i1++) {
                        ColorBean colorBean = colorBeans.get(i1);
                        String colorNo = colorBean.getColorNo();
//                        for(int j=0;j<mPumpConfigBeans.size();j++){
//                            PumpConfigBean pumpConfigBean = mPumpConfigBeans.get(j);
//                            if(colorNo.equals(pumpConfigBean.getMaterials_name())){
//                                pumpConfigBeans.add(pumpConfigBean);
//                            }
//                        }
                        mManualColorBeans.add(colorBean);

                    }
                }

//                if (pumpConfigBeans != null && pumpConfigBeans.size() > 0) {
//                    for (int k = 0; k < pumpConfigBeans.size(); k++) {
//                        PumpConfigBean pumpConfigBean = pumpConfigBeans.get(k);
//                        String materials_name = pumpConfigBean.getMaterials_name();
//                        int address = pumpConfigBean.getAddress();
//                        if (address > 0 && !isContainsAddress(address) && materials_name!=null && materials_name.length()>0) {
//                            //colorBean.setColorDos("");
//                            ColorBean colorBean = new ColorBean();
//                            colorBean.setColorNo(materials_name);
//                            colorBean.setBarrelNo(String.valueOf(pumpConfigBean.getAddress()));
//                            mManualColorBeans.add(colorBean);
//                            //mColorBeans.add(colorBean);
//                        }
//                    }
//                }

            }
        }

    }

    private boolean isContainsAddress(int address) {
        if (mManualColorBeans != null && mManualColorBeans.size() > 0) {
            for (int i = 0; i < mManualColorBeans.size(); i++) {
                ColorBean colorBean = mManualColorBeans.get(i);
                if (colorBean.getBarrelNo().equals(String.valueOf(address))) {
                    return true;
                }
            }
        }

        return false;
    }

    private List<ColorBean> getCommonColorType(FormulaitemsBean formulaitems) {
        List<ColorBean> colorBeanList = new ArrayList<>();
//        if (GlobalConstants.isManual) {
//            if (formulaitems.getColorBeans() instanceof List) {
//                colorBeanList = (List<ColorBean>) formulaitems.getColorBeans();
//            } else {
//                ColorBean colorBean = (ColorBean) formulaitems.getColorBeans();
//                colorBeanList.add(colorBean);
//            }
//        } else {
        if (formulaitems.getColorBeans() instanceof List) {
            colorBeanList = GsonUtils.fromJson(formulaitems.getColorBeans() + "", new TypeToken<List<ColorBean>>() {
            }.getType());
        } else {
            ColorBean colorBean = GsonUtils.fromJson(formulaitems.getColorBeans() + "", new TypeToken<ColorBean>() {
            }.getType());
            colorBeanList.add(colorBean);
        }
        //       }

        return colorBeanList;
    }


    public void startCmd() {
        if (mPumpConfigBeans != null && mPumpConfigBeans.size() > 0 && mFmParamsBeans != null && mFmParamsBeans.size() > 0) {

            String tongshu = "";
            if (myFormulas != null && myFormulas.size() > 0) {
                MyFormula myFormula1 = myFormulas.get(0);
                tongshu = myFormula1.getColorformula().getFormula().getBarrels();
            }
            //String tongshu = editTongshu.getText().toString().trim();
            String regEx = "[^0-9]";

            Pattern p = Pattern.compile(regEx);

            Matcher m = p.matcher(tongshu);
            String digit = m.replaceAll("").trim();
            if (digit == null || digit.length() == 0) {
//                        ToastUtils.showShort("请输入桶数");
//                        ToastUtils.setGravity(Gravity.CENTER,0,0);
//                        ToastUtils.setMsgTextSize(22);
                MyToastUtil.showToastSnackBar(decorView, "请输入桶数", Snackbar.LENGTH_SHORT);
                return;
            }
            taskCount = Integer.parseInt(digit);
            if (hasDosList != null && hasDosList.size() > 0) {
                hasDosList.clear();
            }
            for (int i = 0; i < mManualColorBeans.size(); i++) {
                ColorBean colorBean = mManualColorBeans.get(i);
                String colorDos = colorBean.getColorDos();
                String colorNo = colorBean.getColorNo();
                // String barrelNo = colorBean.getBarrelNo();
                double colorDosInt = 0;
                if (colorDos != null && colorDos.length() > 0) {
                    colorDosInt = Double.parseDouble(colorDos);
                    if (colorDosInt > 0) {
                        ColorDosBean colorDosBean = new ColorDosBean();
                        colorDosBean.setColorDos(colorDos);
                        colorDosBean.setPosition(i);
                        colorDosBean.setColorName(colorNo);
                        colorDosBean.setInnerCode(innerCode);
                        // colorDosBean.setBarrelNo(barrelNo);
                        hasDosList.add(colorDosBean);
                    }
                }
            }
            if (hasDosList == null || hasDosList.size() == 0) {
//                        ToastUtils.showShort("请输入色浆量");
//                        ToastUtils.setGravity(Gravity.CENTER,0,0);
//                        ToastUtils.setMsgTextSize(22);
                MyToastUtil.showToastSnackBar(decorView, "请输入色浆量", Snackbar.LENGTH_SHORT);
                return;
            }
            leftWeightName = "";
            leftAlarm = "";
            cannedPrecisionDes = "";
            ///////////////////////////////拿到当前每一个桶剩余量
            for (int j = 0; j < hasDosList.size(); j++) {
                ColorDosBean colorDosBean = hasDosList.get(j);
                int position = colorDosBean.getPosition();
                ColorBean manualColorBean = mManualColorBeans.get(position);
                String colorNo = manualColorBean.getColorNo();
                String targetDos = manualColorBean.getColorDos();
                /////////////////////////计算精度
                double dTarget = Double.parseDouble(targetDos);
                mTong = "";
                double dCannedPrecision = calPre(dTarget, colorNo);//拿到桶号 和pumpconfigbean 以及罐装精度
                colorDosBean.setBarrelNo(mTong);
                if (dCannedPrecision <= 0) {
                    cannedPrecisionDes += " 桶号：" + mTong + " 物料编号:" + colorNo + " 目标值:" + dTarget;
                } else {
                    colorDosBean.setCannedPrecision(dCannedPrecision);
                }
                /////////////////////////计算精度


                if (mPumpConfigBeans != null && mPumpConfigBeans.size() > 0) {
                    for (int k = 0; k < mPumpConfigBeans.size(); k++) {
                        PumpConfigBean pumpConfigBean = mPumpConfigBeans.get(k);
                        int alarm_value = Integer.parseInt(pumpConfigBean.getAlarm_value());
                        String tong = String.valueOf(pumpConfigBean.getAddress());
                        if (mTong.equals(tong)) {
                            double zhuchu = Double.parseDouble(targetDos) * taskCount;
                            if (zhuchu > pumpConfigBean.getCurValue()) {
                                leftWeightName += mTong + "号桶" + colorNo + "剩余" + pumpConfigBean.getCurValue();
                            }
                            if (alarm_value - zhuchu > pumpConfigBean.getCurValue()) {
                                leftAlarm += mTong + "号桶" + colorNo + "剩余" + pumpConfigBean.getCurValue();
                            }
                        }
                    }
                }

            }
            ///////////////////////////////拿到当前每一个桶剩余量
            if (leftAlarm != null && leftAlarm.length() > 0) {
//                        ToastUtils.showLong(leftAlarm + ",剩余量不能小于报警量");
//                        ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//                        ToastUtils.setBgResource(R.color.gray);
//                        ToastUtils.setMsgTextSize(22);
                MyToastUtil.showToastSnackBar(decorView, leftAlarm + ",剩余量不能小于报警量", Snackbar.LENGTH_SHORT);
                leftAlarm = "";
                return;
            }
            if (leftWeightName != null && leftWeightName.length() > 0) {
//                        ToastUtils.showLong(leftWeightName + ",剩余量不能小于注出量");
//                        ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//                        ToastUtils.setBgResource(R.color.gray);
//                        ToastUtils.setMsgTextSize(22);
                MyToastUtil.showToastSnackBar(decorView, leftWeightName + ",剩余量不能小于注出量", Snackbar.LENGTH_SHORT);
                leftWeightName = "";
                return;
            }
            if (cannedPrecisionDes != null && cannedPrecisionDes.length() > 0) {
//                        ToastUtils.showLong(leftWeightName + ",剩余量不能小于注出量");
//                        ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//                        ToastUtils.setBgResource(R.color.gray);
//                        ToastUtils.setMsgTextSize(22);
                // MyToastUtil.showToastSnackBar(decorView, "目标值小于0.05不能打出,不能满足浮动精度 " + cannedPrecisionDes, Snackbar.LENGTH_SHORT);
                String msg = "目标值小于0.05不能打出,不能满足浮动精度 " + cannedPrecisionDes;
                showDialogWarn(msg, CorrectionActivity.this);
                cannedPrecisionDes = "";
                return;
            }

            if (hasDosList != null && hasDosList.size() > 0) {
                Intent intent = new Intent(CorrectionActivity.this, WorkStateActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("mark", "manualOperate");
                bundle.putSerializable("hasDosList", (Serializable) hasDosList);
                bundle.putSerializable("colorBeans", (Serializable) mManualColorBeans);
                // bundle.putSerializable("pumpConfigBeans", (Serializable) pumpConfigBeans);
                bundle.putString("formulaName", "配方打料");
                bundle.putInt("taskCount", taskCount);
                bundle.putDouble("scale", 1);
                intent.putExtras(bundle);
                startActivityAni(intent);
            }
        }
    }

    private IOSDialog iosDialogWarn;

    private void showDialogWarn(String errorMsg, Context context) {
        iosDialogWarn = new IOSDialog.Builder(
                context)
                .setBtnColor(context.getResources().getColor(com.santint.colorformula.R.color.blue))
                .setColor(context.getResources().getColor(com.santint.colorformula.R.color.blue))
                .setMessage(errorMsg)
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(
                                    DialogInterface dialog,
                                    int which) {
                                if (iosDialogWarn != null) {
                                    iosDialogWarn.dismiss();
                                }
                            }
                        })
                .create();
        iosDialogWarn.show();
    }

    private double calPre(double dTarget, String colorNo) {
        boolean isFloat = SharedPreferencesHelper.getBoolean(CorrectionActivity.this, "isFloat", false);
        boolean isHigh = SharedPreferencesHelper.getBoolean(AppApplication.getInstance(), "isHigh", true);
        List<PumpConfigBean> pumpConfigList = new ArrayList<>();
        if (mPumpConfigBeans != null && mPumpConfigBeans.size() > 0) {
            for (int i = 0; i < mPumpConfigBeans.size(); i++) {
                PumpConfigBean pumpConfigBean = mPumpConfigBeans.get(i);
                if (pumpConfigBean.getMaterials_name() != null && pumpConfigBean.getMaterials_name().equals(colorNo)) {
                    pumpConfigList.add(pumpConfigBean);
                }
            }
        }
        double outVal = 0.0d;
        int nIndex = 0;
        double start_value = 0d;
        if (colorNo.equals("k8730") || colorNo.equals("MA100")) {
            start_value = 0.1;
        } else {
            start_value = 0.05;
        }
        if (isFloat) {
            if (!GlobalConstants.isManual) {
                return floatFormula(dTarget, start_value, outVal, nIndex, pumpConfigList);
            } else {//浮动  手动
                if (dTarget < start_value) {
                    return 0;
                }
                if (mPumpConfigBeans != null && mPumpConfigBeans.size() > 0) {
                    for (int i = 0; i < mPumpConfigBeans.size(); i++) {
                        PumpConfigBean pumpConfigBean = mPumpConfigBeans.get(i);
                        if (mTong.equals(String.valueOf(pumpConfigBean.getAddress()))) {
                            mPumpConfigBean = pumpConfigBean;
                            mTong = String.valueOf(mPumpConfigBean.getAddress());
                            break;
                        }
                    }
                }
                float floatValue = SharedPreferencesHelper.getFloat(CorrectionActivity.this, "FloatValue", 0);
                BigDecimal b = new BigDecimal(String.valueOf(floatValue));
                double d = b.doubleValue();
                return d;

            }
        } else {//固定精度
            if (dTarget < start_value) {
                return 0;
            } else {
                if (pumpConfigList != null && pumpConfigList.size() > 0) {
                    mPumpConfigBean = pumpConfigList.get(nIndex);
                    mTong = String.valueOf(mPumpConfigBean.getAddress());
                }
                if (mFmParamsBeans != null && mFmParamsBeans.size() > 0) {
                    for (int k = 0; k < mFmParamsBeans.size(); k++) {
                        mFmParamsBean = mFmParamsBeans.get(k);
                        if (mTong.equals(mFmParamsBean.getBarrelNo())) {
                            String cannedPrecision = mFmParamsBean.getCannedaPrecision();
                            double dCannedPrecision = 0;
                            if (isHigh) {
                                dCannedPrecision = Double.parseDouble(cannedPrecision);
                            } else {
                                dCannedPrecision = 0.1;
                            }
//                        if (0.02 * dTarget > dCannedPrecision) {
//                            dCannedPrecision = 0.02 * dTarget;
//                        }
                            return dCannedPrecision;
                        }
                    }
                }
            }
        }

        return 0;
    }

    private double floatFormula(double dTarget, double start_value, double outVal, int nIndex, List<PumpConfigBean> pumpConfigList) {
        if (dTarget < start_value) {
            for (int i = 0; i < pumpConfigList.size(); i++) {
                PumpConfigBean pumpConfigBean1 = pumpConfigList.get(i);
                if (pumpConfigList.size() > nIndex + 1) {
                    outVal = dTarget * Integer.parseInt(pumpConfigBean1.getMultiple());
                    if (outVal >= start_value) {
                        nIndex = i;
                        if (pumpConfigList != null && pumpConfigList.size() > 0) {
                            mPumpConfigBean = pumpConfigList.get(nIndex);
                            mTong = String.valueOf(mPumpConfigBean.getAddress());

                        }
                        break;
                    }
                } else {
                    return 0;
                }

            }
        }
        if ((outVal >= start_value && outVal < 0.5) || (dTarget >= start_value && dTarget < 0.5)) {
            if ((nIndex + 1) == pumpConfigList.size()) {
                //这种情况是稀释倍数用完，当前已经没有稀释倍数可用了
                mPumpConfigBean = pumpConfigList.get(nIndex);
                mTong = String.valueOf(mPumpConfigBean.getAddress());
                if (outVal >= start_value) {//用z值
                    return outVal;

                } else {
                    return dTarget;
                }

            } else {//仍可稀释
                double outVal_1 = 0.0d;
                for (int i = nIndex; i < pumpConfigList.size(); i++) {
                    PumpConfigBean pumpConfigBean = pumpConfigList.get(i);
                    outVal_1 = dTarget * Integer.parseInt(pumpConfigBean.getMultiple());
                    if (outVal_1 >= 5.0) {
                        mPumpConfigBean = pumpConfigList.get(nIndex);
                        mTong = String.valueOf(mPumpConfigBean.getAddress());
                        if (outVal >= start_value) {
                            return outVal;
                        } else {
                            return dTarget;
                        }
                    } else {
                        nIndex = i;
                        mPumpConfigBean = pumpConfigList.get(nIndex);
                        mTong = String.valueOf(mPumpConfigBean.getAddress());
                        return outVal_1;
                    }
                }

            }
        }
        if (dTarget >= 0.5 || outVal >= 0.5) {
            if (outVal >= 0.5) {
                return outVal;
            } else {
                return dTarget;
            }
        }
        return 0;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.santint.colorformula.R.menu.toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();//            case android.R.id.home:
//                finish();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
        if (itemId == R.id.action_correct) {//if (!isMaking && !isMakingR) {
            Intent intent = new Intent(CorrectionActivity.this, CorrectionActivity.class);
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
        } else if (itemId == R.id.action_manual) {//                Intent intent_manual = new Intent(MainActivity.this, ManualOperateActivity.class);
//                startActivity(intent_manual);
            jumpManual();
            return true;
        } else if (itemId == R.id.action_my_formula) {
            //jumpMyFormla();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void jumpManual() {
//        if (myFormulas != null && myFormulas.size() > 0) {
//            MyFormula myFormula = myFormulas.get(0);
//            FormulaitemsBean formulaitems = myFormula.getColorformula().getFormulaitems();
//            List<ColorBean> colorBeans = new ArrayList<>();
////            if(formulaitems instanceof FormulaitemsBean){
////                colorBeans = ((FormulaitemsBean)formulaitems).getColorBeans();
////            }else if(formulaitems instanceof FormulaitemsBean2){
////                ColorBean colorBean = ((FormulaitemsBean2) formulaitems).getColorBean();
//            //               colorBeans.add(colorBean);
//            //           }
//            colorBeans = (List<ColorBean>) ((FormulaitemsBean) formulaitems).getColorBeans();
//
//            Intent intent = new Intent(this, ManualOperateActivity.class);
////            Bundle bundle = new Bundle();
////            bundle.putSerializable("colorBeans", (Serializable) colorBeans);
////            intent.putExtras(bundle);
//            startActivityAni(intent);
//        } else {
//            MyToastUtil.showToast(CorrectionActivity.this, "配方文件为空", Toast.LENGTH_LONG);
//        }
        if (myFormulas == null || myFormulas.size() == 0) {
            Intent intent = new Intent(this, ManualOperateActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("colorBeans", (Serializable) colorBeans);
//            intent.putExtras(bundle);
            startActivityAni(intent);
        } else {
            MyToastUtil.showToast(CorrectionActivity.this, "配方文件打料中", Toast.LENGTH_LONG);
        }


    }

//    public void jumpMyFormla() {
////        MyFormula myFormula = all.get(0);
////        List<ColorBean> colorBeans = myFormula.getColorBeans();
//        Intent intent = new Intent(this, MyFormulaActivity.class);
////        Bundle bundle = new Bundle();
////        bundle.putSerializable("colorBeans", (Serializable) colorBeans);
////        intent.putExtras(bundle);
//        startActivityAni(intent);
//    }

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
            if (AppApplication.getInstance().isSendSearchBarrel) {
                AppApplication.getInstance().isSendSearchBarrel = false;
                MessageEventMainTean messageEventMainTean = new MessageEventMainTean();
                messageEventMainTean.setDataMainTen("cmdMark");
                EventBus.getDefault().post(messageEventMainTean);
            }else{
                Bundle bundle = new Bundle();
                bundle.putString("cmdMark", "cmdMark");
                bundle.putIntArray("ints", ints);
                EventBus.getDefault().post(bundle);
            }



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

                    //}
                } else if (ints[3] == 80) {//字符p
                    /////////////SFP 进度反馈
                    Bundle bundle = new Bundle();
                    bundle.putString("cmdMark", "workState");
                    bundle.putIntArray("ints", ints);
                    EventBus.getDefault().post(bundle);

                }
            } else if (ints[2] == 71) {
                if (ints[3] == 84) { //SGT  寻桶完成命令
                    MessageEventMainTean messageEventMainTean = new MessageEventMainTean();
                    messageEventMainTean.setDataMainTen("success");
                    messageEventMainTean.setInts(ints);
                    EventBus.getDefault().post(messageEventMainTean);
                }

            } else if (ints[2] == 83) {//字符s
                if (ints[3] == 73) {//字符I
                    //////////////////SSI  消除弹窗
                    String exceptionCode = getExceptrionCode(ints);
                    String exceptionMsg = getExceptionMsg(exceptionCode);
                    //////////////////////保存故障
                    resultBreak.setTime(DateFormatter.getCurrentTime());
                    resultBreak.setBreakCode(exceptionCode);
                    resultBreak.setBreakDes(exceptionMsg);
                    ResultBreakService.getInstance().save(resultBreak);
                    //////////////////////保存故障
                    if (exceptionMsg.equals("E01:急停开关并未打开")) {
                        if (iosDialog != null) {
                            iosDialog.dismiss();
                        }
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putString("cmdMark", "clearWindow");
                        //bundle.putIntArray("ints", ints);
                        EventBus.getDefault().post(bundle);

                    }
                }
            }

        } else if (ints[1] == 69) {///////////////////////字符E
            if (ints[2] == 71) {//字符G
                if (ints[3] == 73) {//字符I
                    //////////////////EGI   未找到调色对应的桶
                    handleEGI(ints, chars);
                } else if (ints[3] == 84) {//EGT 寻桶失败命令
                    MessageEventMainTean messageEventMainTean = new MessageEventMainTean();
                    messageEventMainTean.setDataMainTen("failure");
                    messageEventMainTean.setInts(ints);
                    EventBus.getDefault().post(messageEventMainTean);
                }
            } else if (ints[2] == 69) {//字符E
                if (ints[3] == 73) {//字符I
                    /////////////////EEI  弹出弹窗   传感器未感应反馈命令
                    //if ((System.currentTimeMillis() - exitTime) > 5000) {
                    String exceptionCode = getExceptrionCode(ints);
                    String exceptionMsg = getExceptionMsg(exceptionCode);
                    //////////////////////保存故障
                    resultBreak.setTime(DateFormatter.getCurrentTime());
                    resultBreak.setBreakCode(exceptionCode);
                    resultBreak.setBreakDes(exceptionMsg);
                    ResultBreakService.getInstance().save(resultBreak);
                    //////////////////////保存故障
                    if (exceptionMsg.equals("E01:急停开关并未打开")) {
                        Activity currentActivity = MyActivityManager.getInstance().getCurrentActivity();
                        errorCodeDialog(exceptionMsg, currentActivity);
                        Bundle bundle = new Bundle();
                        bundle.putString("cmdMark", "sensorFail");
                        bundle.putString("sensorFail", "emegencyFail");
                        bundle.putIntArray("ints", ints);
                        EventBus.getDefault().post(bundle);
                    } else {
//                        Bundle bundle = new Bundle();
//                        bundle.putString("manualOperateError", exceptionMsg);
//                        EventBus.getDefault().post(bundle);
                        Bundle bundle = new Bundle();
                        bundle.putString("cmdMark", "sensorFail");
                        bundle.putString("sensorFail", "otherSensorFail");
                        bundle.putString("errorMsg", exceptionMsg);
                        bundle.putIntArray("ints", ints);
                        EventBus.getDefault().post(bundle);
                        // exitTime = System.currentTimeMillis();
                        //  }
                    }

                }
            } else if (ints[2] == 70) {//字符F
                if (ints[3] == 83) { //字符S
                    //////////////////EFS  罐装失败结束命令
//                    String barrelNo = "";
//                    for (int k = 6; k < 9; k++) { //桶号
//                        barrelNo += String.valueOf(chars[k]);
//                    }
//                    String realValue = "";
//                    for (int i = 9; i < 17; i++) {//小数点根据灌装目标量小数位数决定,如果是3，则后3位是小数
//                        realValue += String.valueOf(chars[i]);
//                    }
//                    String substring = realValue.substring(0, 5);
//                    String substring2 = realValue.substring(5, realValue.length());
//                    int i1 = Integer.parseInt(substring);
//                    String finals = i1 + "." + substring2;
//                    double finalsValue = Double.parseDouble(finals);


                    if (ints[4] == 48 && ints[5] == 50) {//02 上位机主动终止罐装命令回复
//                        Bundle bundle = new Bundle();
//                        bundle.putString("topstop", "topstop");
//                        bundle.putIntArray("ints", ints);
//                        EventBus.getDefault().post(bundle);
                        Bundle bundle = new Bundle();
                        bundle.putString("cmdMark", "cannedFail");
                        bundle.putString("cannedFail", "topFail");
                        bundle.putIntArray("ints", ints);
                        EventBus.getDefault().post(bundle);
                    } else { //下位机出现异常罐装命令交互 【急停也在这里】
                        String exceptionCode = getExceptrionCode(ints);
                        String exceptionMsg = getExceptionMsg(exceptionCode);
                        //////////////////////保存故障
                        resultBreak.setTime(DateFormatter.getCurrentTime());
                        resultBreak.setBreakCode(exceptionCode);
                        resultBreak.setBreakDes(exceptionMsg);
                        ResultBreakService.getInstance().save(resultBreak);
                        //////////////////////保存故障
                        if (exceptionMsg.equals("E01:急停开关并未打开")) {
                            Activity currentActivity = MyActivityManager.getInstance().getCurrentActivity();
                            errorCodeDialog(exceptionMsg, currentActivity);
                            Bundle bundle = new Bundle();
                            bundle.putString("cmdMark", "cannedFail");
                            bundle.putString("cannedFail", "emegencyFail");
                            bundle.putIntArray("ints", ints);
                            EventBus.getDefault().post(bundle);
                        } else {
//                            Bundle bundle = new Bundle();
//                            bundle.putString("manualOperateError", exceptionMsg);
//                            EventBus.getDefault().post(bundle);
                            Bundle bundle = new Bundle();
                            bundle.putString("cmdMark", "cannedFail");
                            bundle.putString("cannedFail", "bottomFail");
                            bundle.putString("errorMsg", exceptionMsg);
                            bundle.putIntArray("ints", ints);
                            EventBus.getDefault().post(bundle);
                        }


                    }
                }
            }
        } else if (ints[1] == 24) { //心跳回复  16进制为18
            NetApplication.inetrequestInterface.heartBack();

        }
    }

    private void handleEGI(int[] ints, char[] chars) {
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

    /**
     * 初始化底部导航栏
     */
    private void initTab() {
        mInflater = LayoutInflater.from(CorrectionActivity.this);
        Tab maintenance = new Tab(R.string.maintenance, R.drawable.biaopng);
        maintenance.setFragment(MainTenance.class);
//        Tab correct = new Tab(R.string.correct, R.drawable.biaopng);
//        correct.setFragment(Correction.class);
//        Tab correctMethod = new Tab(R.string.correctmethod, R.drawable.biaopng);
//        correctMethod.setFragment(CorrectMethod.class);
        Tab pumpConfig = new Tab(R.string.pumpconfig,
                R.drawable.biaopng);
        pumpConfig.setFragment(PumpConfig.class);
//        Tab pumpSet = new Tab(R.string.pumpset, R.drawable.biaopng);
//        pumpSet.setFragment(PumpSet.class);
        Tab correctParam = new Tab(R.string.correctparam, R.drawable.biaopng);
        correctParam.setFragment(CorrectDataManager.class);

        Tab systemSet = new Tab(R.string.systemset, R.drawable.biaopng);
        systemSet.setFragment(SystemSet.class);

        mTabs.add(maintenance);
//        mTabs.add(correct);
//        mTabs.add(correctMethod);
        mTabs.add(pumpConfig);
        // mTabs.add(pumpSet);
        mTabs.add(correctParam);
        //mTabs.add(manualOperation);
        mTabs.add(systemSet);

        mTabHost = findViewById(android.R.id.tabhost);
        tabContent = findViewById(R.id.realtabcontent);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        for (Tab tab : mTabs) {
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(getString(tab
                    .getTitle()));
            tabSpec.setIndicator(buildIndicator(tab));
            mTabHost.addTab(tabSpec, tab.getFragment(), null);// 重点，不加会报错
        }
        mTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        mTabHost.setCurrentTab(0);
        mTabHost.setOnTabChangedListener(this);
        view1 = mTabHost.getTabWidget().getChildTabViewAt(0);
        view2 = mTabHost.getTabWidget().getChildTabViewAt(1);
        view3 = mTabHost.getTabWidget().getChildTabViewAt(2);
//        view4 = mTabHost.getTabWidget().getChildTabViewAt(3);
//        view5 = mTabHost.getTabWidget().getChildTabViewAt(4);
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
        } else if (exception.equals("94")) {
            exceptionMsg = BreakCodeEnum.ValveBreak.getDescription();
        }
        return exceptionMsg;
    }

    public void setTab(int tab) {
        mTabHost.setCurrentTab(tab);
    }

    private View buildIndicator(Tab tab) {
        View view = mInflater.inflate(R.layout.tab_indicator, null);
        //img = view.findViewById(R.id.id_iv_tab);
        text = view.findViewById(R.id.id_tv_indicator);
        //img.setBackgroundResource(tab.getIcon());
        text.setText(tab.getTitle());
        return view;
    }

    @Override
    public void onTabChanged(String tabId) {
        //  if (!isCorrect) {
        if (!isPause) {
            if (tabId.equals(getString(R.string.maintenance))) {
                if (mainTenance == null) {
                    // Fragment fragment = getSupportFragmentManager()
                    // .findFragmentById(R.string.home);
                    Fragment fragment = getSupportFragmentManager()
                            .findFragmentByTag(getString(R.string.maintenance));
                    if (fragment != null) {
                        mainTenance = (MainTenance) fragment;
                        mainTenance.refData();
                    }
                } else {
                    mainTenance.refData();
                }

            } else if (tabId.equals(getString(R.string.pumpconfig))) {
                if (pumpConfig == null) {
                    // Fragment fragment = getSupportFragmentManager()
                    // .findFragmentById(R.string.home);
                    Fragment fragment = getSupportFragmentManager()
                            .findFragmentByTag(getString(R.string.pumpconfig));
                    if (fragment != null) {
                        pumpConfig = (PumpConfig) fragment;
                        pumpConfig.refData();
                    }
                } else {
                    pumpConfig.refData();
                }

            } else if (tabId.equals(getString(R.string.correctparam))) {
                if (correctParam == null) {
                    // Fragment fragment = getSupportFragmentManager()
                    // .findFragmentById(R.string.home);
                    Fragment fragment = getSupportFragmentManager()
                            .findFragmentByTag(getString(R.string.correctparam));
                    if (fragment != null) {
                        correctParam = (CorrectDataManager) fragment;
                        correctParam.refData();
                    }
                } else {
                    correctParam.refData();
                }
            }
        }
//        } else {
//            ToastUtils.showShort("校正中，请先终止，再进行其他操作");
//            ToastUtils.setBgResource(R.color.gray);
//            ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//            if(correction!=null){
//                if(correction.isCorrect){
//                    ToastUtils.showShort("校正中，如想取消，请点击停止按钮");
//                    ToastUtils.setGravity(Gravity.CENTER,0,0);
//                }else{
//                    finish();
//                }
//            }else{
//                finish();
//            }
            finish();
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        AppApplication.getInstance().removeActivity(this);
    }
}
