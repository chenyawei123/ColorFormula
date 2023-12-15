package com.santint.colorformula;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.core.TableConfig;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.bg.BaseBackgroundFormat;
import com.bin.david.form.data.format.bg.BaseCellBackgroundFormat;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.table.TableData;
import com.bin.david.form.listener.OnColumnItemClickListener;
import com.blankj.utilcode.util.GsonUtils;
import com.cyw.mylibrary.application.AppApplication;
import com.cyw.mylibrary.base.BaseAppCompatActivity;
import com.cyw.mylibrary.bean.CannedBean;
import com.cyw.mylibrary.bean.ColorBean;
import com.cyw.mylibrary.bean.ColorDosBean;
import com.cyw.mylibrary.bean.Coloresult;
import com.cyw.mylibrary.bean.DDParamsBean;
import com.cyw.mylibrary.bean.FmParamsBean;
import com.cyw.mylibrary.bean.FormulaitemsBean;
import com.cyw.mylibrary.bean.ManualFormula;
import com.cyw.mylibrary.bean.MyFormula;
import com.cyw.mylibrary.bean.PumpConfigBean;
import com.cyw.mylibrary.netrequest.utils.PowderPumpProtocal;
import com.cyw.mylibrary.services.CannedResultService;
import com.cyw.mylibrary.services.ColorBeanService;
import com.cyw.mylibrary.services.DDService;
import com.cyw.mylibrary.services.FMServiceNew;
import com.cyw.mylibrary.services.PumpConfigBeanService;
import com.cyw.mylibrary.util.ComputeDoubleUtil;
import com.cyw.mylibrary.util.DateFormatter;
import com.cyw.mylibrary.util.GlobalConstants;
import com.cyw.mylibrary.util.IOSDialog;
import com.cyw.mylibrary.util.OnMultiClickListener;
import com.cyw.mylibrary.util.SaveJsonFile;
import com.cyw.mylibrary.util.SharedPreferencesHelper;
import com.cyw.mylibrary.util.WriteLog;
import com.google.gson.reflect.TypeToken;
import com.santint.colorformula.adapter.ColorWorkStateAdapter;
import com.santint.colorformula.bean.ProgressBean;
import com.santint.colorformula.bean.TaskInfo;
import com.santint.colorformula.commondialog.PopDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author： cyw
 */
public class WorkStateActivity extends BaseAppCompatActivity {
    private TextView tvStop;
    private SmartTable<TaskInfo> table;
    private Column<String> columnFormulaName;
    private Column<Integer> columnId;
    private Column<String> columnBarrel;
    private Column<String> columnTarget;
    private Column<String> columnReal;
    private int selRowFm = -1;
    private List<CannedBean> cannedBeans = new ArrayList<>();
    private List<TaskInfo> taskInfos = new ArrayList<>();
    private TextView tvTop, tvBottom, tvDel;
    private List<ColorDosBean> hasDosList = new ArrayList<>();
    private Toolbar toolbar;
    private RecyclerView ryColorWorkState;
    private ColorWorkStateAdapter colorWorkStateAdapter;
    private ManualFormula manualFormula;
    private MyFormula myFormula;
    private PumpConfigBeanService correctConfigService;
    private List<PumpConfigBean> mPumpConfigBeans = new ArrayList<>();
    //    private List<ProgressBean> progressBeans = new ArrayList<>();
    private IOSDialog iosDialog, iosDialogBarrel, iosDialogEmpty, iosDialogPre, iosDialogEmegency;
    private String findBarrel = "";
    String errorMsg = "";
    private boolean canBack = false;
    private int currenDosIndex = 0;
    private int curTaskIndex = 0;
    private int curColorIndex = 0;
    private int ddSendIndex = 0;
    private int colorBeanIndex = 0;
    private List<DDParamsBean> ddSendList = new ArrayList<>();

    private int task = 1;
    String targetDos = "";

    FmParamsBean mFmParamsBean;
    DDParamsBean mDdParamsBean;
    PumpConfigBean mPumpConfigBean;
    private List<ColorBean> mColorBeans = new ArrayList<>();
    private List<FmParamsBean> mFmParamsBeans = new ArrayList<>();
    private FMServiceNew fmService;
    private CannedBean cannedBean;
    //private CannedService cannedService;
    private int taskCount = 1;
    private double scale = 1;

    private String formulaName = "";
    private String mark = "";
    private List<ProgressBean> progressBeanList = new ArrayList<>();

    String mTong = ""; //发送
    private Coloresult coloresult = new Coloresult();
    private List<Coloresult.AcutalQuantityBean.ColorantCodeBean> colorCodes = new ArrayList<>();
    //private List<Coloresult.AcutalQuantityBean.ActualQuaBean> actualValues = new ArrayList<>();
    private View decorView;
    //List<ProgressBean> progressBeans = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_state);
        decorView = getWindow().getDecorView();
        AppApplication.getInstance().init(decorView);
        initView();
        initService();
        toolbar = findViewById(R.id.toolbar);
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_three_big));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        EventBus.getDefault().register(this);
        receiveIntent();
        startCmd();

        // saveCanned("1", "1.1", "1");
        if (hasDosList != null && hasDosList.size() > 0) {
            for (int j = 0; j < taskCount; j++) {
                for (int i = 0; i < hasDosList.size(); i++) {
                    ColorDosBean colorDosBean = hasDosList.get(i);
                    String colorDos = colorDosBean.getColorDos();
                    String barrelNo = colorDosBean.getBarrelNo();
                    CannedBean cannedBean = new CannedBean();
                    double targetValue = Double.parseDouble(colorDos) * scale;
                    String str = ComputeDoubleUtil.computeDouble(targetValue);
                    cannedBean.setCannedTargetValue(str);
                    cannedBean.setBarrelNo(barrelNo);
                    cannedBean.setFormulaName(formulaName);
                    cannedBeans.add(cannedBean);


                }
                TaskInfo taskInfo = new TaskInfo();
                taskInfo.setTaskId(j + 1);
                taskInfo.setTaskName(formulaName);
                taskInfos.add(taskInfo);

            }
        }

        bindAdapterColorState();
        initRightData();
        //removeTongDialog("请移走桶");
        ///errorCodeDialog("请移走桶");
    }

    private void startCmd() {
        if (initCmd()) {
            task = 1;
            PowderPumpProtocal.startProtocal();
            sendCmdCanned();
//            sendCmdPumpSpeed();
//            sendCmdjogParam();
        }

    }

    private void receiveIntent() {
        Bundle bundle = getIntent().getExtras();
        hasDosList = (List<ColorDosBean>) bundle.getSerializable("hasDosList");
        //pumpConfigBeans = (List<PumpConfigBean>) bundle.getSerializable("pumpConfigBeans");
        formulaName = bundle.getString("formulaName");
        taskCount = bundle.getInt("taskCount");
        scale = bundle.getDouble("scale");
        mark = bundle.getString("mark");
        if (mark == null) {
            mark = "";
        }
        if (mark.equals("manualOperate")) {
            mColorBeans = (List<ColorBean>) bundle.getSerializable("colorBeans");
        } else if (mark.equals("tngDetail")) {
            manualFormula = (ManualFormula) bundle.getSerializable("manualFormula");
        } else if (mark.equals("standDetail")) {
            myFormula = (MyFormula) bundle.getSerializable("myFormula");
        }
        for (int i = 0; i < taskCount; i++) {
            handleIntent(mark, i, scale);

        }
    }

    private void handleIntent(String mark, int index, double scale) {
        List<ProgressBean> progressBeans = new ArrayList<>();
        if (mark.equals("manualOperate")) {
            if (hasDosList != null && hasDosList.size() > 0) {
                // for (int j = 0; j < taskCount; j++) {
                for (int i = 0; i < hasDosList.size(); i++) {
                    ColorDosBean colorDosBean = hasDosList.get(i);
                    String coloName = colorDosBean.getColorName();
                    String colorDos = colorDosBean.getColorDos();
                    ProgressBean progressBean = new ProgressBean();
                    progressBean.setColorName(coloName);
                    double totalValue = Double.parseDouble(colorDos) * scale;
                    String str = ComputeDoubleUtil.computeDouble(totalValue);
                    progressBean.setTotalValue(Double.parseDouble(str));//Double.parseDouble(colorDos)
//                    double finalsValue = Double.parseDouble("0.004");
//                    progressBean.setRealValue(finalsValue);
                    String tong = "";
                    if (mPumpConfigBeans != null && mPumpConfigBeans.size() > 0) {
                        for (int k = 0; k < mPumpConfigBeans.size(); k++) {
                            PumpConfigBean pumpConfigBean = mPumpConfigBeans.get(k);
                            if (coloName.equals(pumpConfigBean.getMaterials_name())) {
                                tong = String.valueOf(pumpConfigBean.getAddress());
                                break;
                            }
                        }
                    }
                    progressBean.setBarrelNo(tong);
                    progressBeans.add(progressBean);
                }
                // }
            }
        } else if (mark.equals("tngDetail")) {
            // manualFormula = (ManualFormula) bundle.getSerializable("manualFormula");
            if (manualFormula != null) {
                mColorBeans = manualFormula.getColorBeans();
                if (mColorBeans != null && mColorBeans.size() > 0) {
                    // for (int i = 0; i < taskCount; i++) {
                    for (int j = 0; j < mColorBeans.size(); j++) {
                        ColorBean manualColorBean = mColorBeans.get(j);
                        String colorNo = manualColorBean.getColorNo();
                        String colorDos = manualColorBean.getColorDos();
                        ProgressBean progressBean = new ProgressBean();
                        progressBean.setColorName(colorNo);
                        double totalValue = Double.parseDouble(colorDos) * scale;
                        String str = ComputeDoubleUtil.computeDouble(totalValue);
                        progressBean.setTotalValue(Double.parseDouble(str));
                        String tong = "";
                        if (mPumpConfigBeans != null && mPumpConfigBeans.size() > 0) {
                            for (int k = 0; k < mPumpConfigBeans.size(); k++) {
                                PumpConfigBean pumpConfigBean = mPumpConfigBeans.get(k);
                                if (colorNo.equals(pumpConfigBean.getMaterials_name())) {
                                    tong = String.valueOf(pumpConfigBean.getAddress());
                                    break;
                                }
                            }
                        }
                        progressBean.setBarrelNo(tong);
                        progressBeans.add(progressBean);
                    }
                    //  }
                }

            }
        } else if (mark.equals("standDetail")) {
            //  myFormula = (MyFormula) bundle.getSerializable("myFormula");
            if (myFormula != null) {
                mColorBeans = getCommonColorBeans(myFormula);
                if (mColorBeans != null && mColorBeans.size() > 0) {
                    // for (int i = 0; i < taskCount; i++) {
                    for (int j = 0; j < mColorBeans.size(); j++) {
                        ColorBean colorBean = mColorBeans.get(j);
                        String colorNo = colorBean.getColorNo();
                        String colorDos = colorBean.getColorDos();
                        ProgressBean progressBean = new ProgressBean();
                        progressBean.setColorName(colorNo);
                        double totalValue = Double.parseDouble(colorDos) * scale;
                        String str = ComputeDoubleUtil.computeDouble(totalValue);
                        progressBean.setTotalValue(Double.parseDouble(str));
                        String tong = "";
                        if (mPumpConfigBeans != null && mPumpConfigBeans.size() > 0) {
                            for (int k = 0; k < mPumpConfigBeans.size(); k++) {
                                PumpConfigBean pumpConfigBean = mPumpConfigBeans.get(k);
                                if (colorNo.equals(pumpConfigBean.getMaterials_name())) {
                                    tong = String.valueOf(pumpConfigBean.getAddress());
                                    break;
                                }
                            }
                        }
                        progressBean.setBarrelNo(tong);
                        progressBeans.add(progressBean);
                    }
                }
                //}
            }
        }
        progressBeanList.addAll(progressBeans);
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

    private void initService() {
        correctConfigService = PumpConfigBeanService.getInstance();
        mPumpConfigBeans = correctConfigService.getAllListByType();
        fmService = FMServiceNew.getInstance();
        mFmParamsBeans = fmService.getAllListByType();
        //cannedService = new CannedService(WorkStateActivity.this);
        if (cannedBeans != null && cannedBeans.size() > 0) {
            cannedBeans.clear();
        }
        //cannedBeans = cannedService.getAll();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (canBack) {
                    finish();
                }

                return true;


            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            return canBack;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }


    private void initView() {
        tvStop = findViewById(R.id.tv_stop);
        tvStop.setOnClickListener(l);
        table = findViewById(R.id.tb);
        tvTop = findViewById(R.id.tv_top);
        tvTop.setOnClickListener(l);
        tvBottom = findViewById(R.id.tv_bottom);
        tvBottom.setOnClickListener(l);
        tvDel = findViewById(R.id.tv_del);
        tvDel.setOnClickListener(l);
        ryColorWorkState = findViewById(R.id.ry_color_workstate);
        ryColorWorkState.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

    }

    private void bindAdapterColorState() {
        if (colorWorkStateAdapter == null) {
            colorWorkStateAdapter = new ColorWorkStateAdapter(this, progressBeanList, ryColorWorkState);
            // ryColorWorkState.addItemDecoration(new DividerGridItemDecoration(WorkStateActivity.this));
            ryColorWorkState.setAdapter(colorWorkStateAdapter);
        } else {
            WriteLog.writeTxtToFile("currentDosIndex ===============" + currenDosIndex + "djjj" + curColorIndex);
            colorWorkStateAdapter.notifyItemRangeChanged(curColorIndex, 1);
        }


    }

    OnMultiClickListener l = new OnMultiClickListener() {
        @Override
        public void onMultiClick(View v) {
            int id = v.getId();
            if (id == R.id.tv_stop) {
                String trim = tvStop.getText().toString().trim();
                if (trim.equals("停止")) {
                    canBack = true;
                    PowderPumpProtocal.stopCannedProtocal();
                } else if (trim.equals("继续")) {
                    tvStop.setText("停止");
                    startCmd();
                } else if (trim.equals("完成")) {
                    finish();
                }

            } else if (id == R.id.tv_top) {
                topChange();
            } else if (id == R.id.tv_bottom) {
                bottomChange();
            } else if (id == R.id.tv_del) {
                delRow();
            }

        }
    };

    /**
     * 判断某activity是否处于栈顶
     *
     * @return true在栈顶 false不在栈顶
     */
    private boolean isActivityTop(Class cls, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String name = manager.getRunningTasks(1).get(0).topActivity.getClassName();
        return name.equals(cls.getName());
    }

    private String cannedPrecision = "";
    private ColorBean curColorBean = null;
    private String innerCode = "";
    private double dCannedPrecision = 0d;

    private boolean initCmd() {
        ddSendIndex = 0;
        dCannedPrecision = 0;
        if (ddSendList != null && ddSendList.size() > 0) {
            ddSendList.clear();
        }
        if (hasDosList != null && hasDosList.size() > 0) {
            mTong = "";
            ColorDosBean colorDosBean = hasDosList.get(colorBeanIndex);
            int index = colorDosBean.getPosition();
            innerCode = colorDosBean.getInnerCode();
            mTong = colorDosBean.getBarrelNo();
            curColorBean = mColorBeans.get(index);
            String colorName = curColorBean.getColorNo();
            double dTarget = Double.parseDouble(curColorBean.getColorDos()) * scale;
            targetDos = ComputeDoubleUtil.computeDouble(dTarget);
            dCannedPrecision = colorDosBean.getCannedPrecision();

            if (mPumpConfigBeans != null && mPumpConfigBeans.size() > 0) {
                for (int m = 0; m < mPumpConfigBeans.size(); m++) {
                    mPumpConfigBean = mPumpConfigBeans.get(m);
                    String address = String.valueOf(mPumpConfigBean.getAddress());
                    if (address.equals(mTong)) {
                        break;
                    }
                }
            }
            if (mFmParamsBeans != null && mFmParamsBeans.size() > 0) {
                for (int k = 0; k < mFmParamsBeans.size(); k++) {
                    mFmParamsBean = mFmParamsBeans.get(k);
                    if (mTong.equals(mFmParamsBean.getBarrelNo())) {
                        break;
                    }
                }
            }
////点动 有可能有多个，需要发多个
            List<DDParamsBean> ddParamsBeans = DDService.getInstance().getAllListByType(mTong);
           // List<DDParamsBean> ddParamsBeans = mFmParamsBean.getDdParamsBeans();
            if (ddParamsBeans != null && ddParamsBeans.size() > 0) {
                for (int k = 0; k < ddParamsBeans.size(); k++) {
                    DDParamsBean ddParamsBean = ddParamsBeans.get(k);
                    String barrelNo = ddParamsBean.getBarrelNo();
                    if (barrelNo.equals(mTong)) {
                        ddSendList.add(ddParamsBean);//只添加第一次循环

                    }
                }
            }

        }
        return true;
    }


    private void sendCmdCanned() {
        if (mTong != null && mTong.length() > 0) {
            //if (mark.equals("canned")) {
            // double precision = Double.parseDouble(fmParamsBean.getCannedaPrecision());
//            double cannedTarget = Double.parseDouble(targetDos);
//            if (dCannedPrecision >= cannedTarget) {//罐装目标量必须大于罐装精度阈值
////                ToastUtils.showShort("罐装目标量必须大于罐装精度阈值");
////                ToastUtils.setMsgTextSize(18);
//                MyToastUtil.showToastSnackBar(decorView, "罐装目标量必须大于等于罐装精度阈值", Snackbar.LENGTH_LONG);
//                return;
//            }
            PowderPumpProtocal.cannedProtocal(targetDos, mTong, mFmParamsBean, dCannedPrecision);
            // }
        }

    }

    private void sendCmdPumpSpeed() {
        if (mTong != null && mTong.length() > 0) {
            long tongLong = Long.parseLong(mTong);
            long highSpeed = Long.parseLong(mPumpConfigBean.getHighSpeed());
            long midSpeed = Long.parseLong(mPumpConfigBean.getMidSpeed());
            long lowSpeed = Long.parseLong(mPumpConfigBean.getLowSpeed());
            long ddSpeed = Long.parseLong(mPumpConfigBean.getDdSpeed());
            long startSpeed = Long.parseLong(mPumpConfigBean.getStartSpeed());
            long startStep = Long.parseLong(mPumpConfigBean.getStartStep());
//            if (mark.equals("pumpSpeed")) {
            PowderPumpProtocal.pumpSpeedProtocal(tongLong, highSpeed, midSpeed, lowSpeed, ddSpeed, startSpeed, startStep);
//            }
        }
    }

    private void sendCmdjogParam() {
        if (mTong != null && mTong.length() > 0) {
            long tongLong = Long.parseLong(mTong);
            int ddTargetDos = 0;
            long ddStep = 0;
            long ddCycle = 0;
            long ddDDBeforeStep = 0;
            if(mDdParamsBean!=null){

                if(mDdParamsBean.getDdTargetDos()!=null && mDdParamsBean.getDdTargetDos().length()>0){
                    ddTargetDos = Integer.parseInt(mDdParamsBean.getDdTargetDos());
                }
                //if (ddSendIndex == 1) {
                if(mDdParamsBean.getDdStep()!=null && mDdParamsBean.getDdStep().length()>0){
                    ddStep = Long.parseLong(mDdParamsBean.getDdStep());
                }

                //}
                ddCycle = Long.parseLong(mDdParamsBean.getDdCycle());
                ddDDBeforeStep = Long.parseLong(mDdParamsBean.getDdBeforeStep());
            }

            // if (mark.equals("jogParam")) {

            PowderPumpProtocal.jogParaProtocal(tongLong, ddTargetDos, ddStep, ddCycle, ddDDBeforeStep);

            //  }
        }
    }

    private void sendCmdVal() {
        if (mTong != null && mTong.length() > 0) {
            //if (mark.equals("valVe")) {
            int ddCount = 0;
            if(mFmParamsBean!=null){
                ddCount = mFmParamsBean.getDdCount();
            }

            PowderPumpProtocal.valVeActionProtocal(mTong, ddCount,mFmParamsBean);
            // }
        }
    }

    boolean isCannedSuccess = false;
    int[] ints;
    private static final long PROGRESS_0_RATE = 2 * 1000 * 60;
    private long sendTime = 0L;
    private boolean isFirstProgress = true;
    private double firstValue = 0d;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Bundle bundle) {
        if (bundle != null) {
            //////////////////////////命令标记
            String cmd = bundle.getString("cmdMark");

            if (cmd != null && cmd.length() > 0) {
                if (cmd.equals("cmdMark")) {
                    handleCmdMark();
                } else if (cmd.equals("cannedSuccess")) {
                    ints = bundle.getIntArray("ints");
                    char[] chars = intArrayToCharArray();
                    handleCannedSuccess(chars);
                } else if (cmd.equals("machineBusy")) {
                    errorCodeDialog("E07:调色机忙");
                } else if (cmd.equals("checkSumError")) {
                    errorCodeDialog("E08:校验和错误");
                } else if (cmd.equals("workState")) {///////////////////////////////罐装进度
                    ints = bundle.getIntArray("ints");
                    char[] chars = intArrayToCharArray();
                    handleWorkState(chars);
                } else if (cmd.equals("clearWindow")) {////////////////////////消除弹窗
                    // int[] ints = bundle.getIntArray("ints");
                    if (iosDialog != null) {
                        iosDialog.dismiss();
                    }
                    if (popDialog != null) {
                        popDialog.dismiss();
                    }
                    if (isCannedSuccess) {//请移桶
                        isCannedSuccess = false;
//                        if (curTaskIndex < taskCount) {//多任务
//                            startCmd();
//                            //tvStop.setText("继续");
//                        } else {
//                            curTaskIndex = 0;
//                            tvStop.setText("完成");
//                            canBack = true;
//                        }
                    } else {//请放桶

                    }
                } else if (cmd.equals("cannedFail")) {//罐装失败
                    String mark = bundle.getString("cannedFail");
                    ints = bundle.getIntArray("ints");
                    char[] chars = intArrayToCharArray();
                    handleCannedFail(chars);
                    if (mark.equals("topFail")) {

                    } else if (mark.equals("bottomFail")) {
                        String errorMsg = bundle.getString("errorMsg");
                        if (errorMsg != null && errorMsg.length() > 0) {
                            if (errorMsg.equals("E04:门未关")) {
                                canBack = false;
                            } else {
                                canBack = true;
                            }
                            // minusDosCurValue();
                            errorCodeDialog(errorMsg);
                            errorMsg = "";
                        }
                    } else if (mark.equals("emegencyFail")) {

                    }
                } else if (cmd.equals("sensorFail")) {//传感器反馈失败
                    String mark = bundle.getString("sensorFail");
                    ints = bundle.getIntArray("ints");
                    char[] chars = intArrayToCharArray();
                    if (mark.equals("emegencyFail")) {

                    } else if (mark.equals("otherSensorFail")) {
                        String errorMsg = bundle.getString("errorMsg");
                        if (errorMsg != null && errorMsg.length() > 0) {
                            if (errorMsg.equals("E04:门未关")) {
                                canBack = false;
                            } else {
                                canBack = true;
                            }
                            // minusDosCurValue();
                            errorCodeDialog(errorMsg);
                            errorMsg = "";
                        }
                    }
                }
            }

            //////////////////////////错误弹窗
            findBarrel = bundle.getString("manualOperateTong");
//            errorMsg = bundle.getString("manualOperateError");
//            if (errorMsg.equals("急停开关并未打开")) {
//                Activity currentActivity = MyActivityManager.getInstance().getCurrentActivity();
//                emegencyDialog(errorMsg, currentActivity);
//            } else {
            if (isActivityTop(WorkStateActivity.class, WorkStateActivity.this)) {
//                if (errorMsg != null && errorMsg.length() > 0) {
//                    if (errorMsg.equals("门未关")) {
//                        canBack = false;
//                    } else {
//                        canBack = true;
//                    }
//                    // minusDosCurValue();
//                    errorCodeDialog(errorMsg);
//                    errorMsg = "";
//                }
                if (findBarrel != null && findBarrel.length() > 0) {
                    findBarrelDialog(findBarrel);
                    findBarrel = "";
                }
            }
            //}

//            String topStop = bundle.getString("topstop");
//            if (topStop != null && topStop.length() > 0) {
//                if (topStop.equals("topstop")) {
//                    //minusDosCurValue();
//                    ints = bundle.getIntArray("ints");
//                    char[] chars = intArrayToCharArray();
//                    handleStop(chars);
//                }
//            }

        }
    }

    private void handleCannedFail(char[] chars) {
        String barrelNo = "";
        for (int k = 6; k < 9; k++) { //桶号
            barrelNo += String.valueOf(chars[k]);
        }
        String realValue = "";
        for (int i = 9; i < 17; i++) {//小数点根据灌装目标量小数位数决定,如果是3，则后3位是小数
            realValue += String.valueOf(chars[i]);
        }
        String finals = "";
        if (realValue != null && realValue.length() > 0) {
            boolean isHigh = SharedPreferencesHelper.getBoolean(AppApplication.getInstance(),"isHigh",true);
            int decimalDiv = 0;
            if(isHigh){ //三位小数
                decimalDiv = 5;
            }else{//一位小数
                decimalDiv=7;
            }
            String substring = realValue.substring(0, decimalDiv);
            String substring2 = realValue.substring(decimalDiv, realValue.length());
            int i1 = Integer.parseInt(substring);
            finals = i1 + "." + substring2;
            String targetValue = "";
            String materialName = "";
            int index = 0;
            int begin = curTaskIndex * hasDosList.size();
            if (hasDosList != null && hasDosList.size() > 0) {
                for (int i = begin; i < hasDosList.size(); i++) {
                    ColorDosBean colorDosBean = hasDosList.get(i);
                    String tong = colorDosBean.getBarrelNo();
                    if (tong.length() == 1) {
                        tong = "00" + tong;
                    } else if (tong.length() == 2) {
                        tong = "0" + tong;
                    }
                    if (barrelNo.equals(tong)) {
                        index = i;
                        break;
                    }
                }
                ColorDosBean colorDosBean = hasDosList.get(index);
                double dTarget = Double.parseDouble(colorDosBean.getColorDos()) * scale;
                targetValue = ComputeDoubleUtil.computeDouble(dTarget);
                materialName = colorDosBean.getColorName();
                minusDosCurValue(materialName, finals);

                backResult(finals);
                backResult2();
                if (!GlobalConstants.isManual) {
                    SaveJsonFile.writeXml("0", listkey, listvalue, innerCode);
                    if (listkey != null && listkey.size() > 0) {
                        listkey.clear();
                    }
                    if (listvalue != null && listvalue.size() > 0) {
                        listvalue.clear();
                    }
                }
            }
        }
    }

    private void handleCmdMark() {
        int ddSize = ddSendList.size();
        if (task == 1) {
            task++;
            PowderPumpProtocal.childTaskProtocal(3);
        } else if (task == 2) {//接收子任务命令
            task++;
            sendCmdCanned();
        } else if (task == 3) {//接收罐装命令
            task++;
            sendCmdPumpSpeed();
        } else if (task == 4) {
            //task++;
            if (ddSendIndex <= ddSize - 1) {
                mDdParamsBean = ddSendList.get(ddSendIndex);
                ddSendIndex++;
                sendCmdjogParam();
            } else {
                task++;
                sendCmdVal();
            }
        } else if (task == 5) {//接收到valve回复之后的处理,发送结束命令
            colorBeanIndex++;
            if (colorBeanIndex <= hasDosList.size() - 1) {//开启下一个色浆
                if (initCmd()) {
                    task = 3;
                    sendCmdCanned();
                }

                //PowderPumpProtocal.startProtocal();
            } else {//全部完成
                task++;
                PowderPumpProtocal.endProtocal();//发送完结束后，才能收到罐装成功
                colorBeanIndex = 0;
            }
        } else if (task == 6) {//收到结束命令，真正完成
        }
    }

    /////////////////////////////////罐装成功
    private void handleCannedSuccess(char[] chars) {
        String barrelNo = "";
        for (int k = 6; k < 9; k++) { //桶号
            barrelNo += String.valueOf(chars[k]);
        }
        String realValue = "";
        for (int i = 9; i < 17; i++) {//小数点根据灌装目标量小数位数决定,如果是3，则后3位是小数
            realValue += String.valueOf(chars[i]);
        }
        String finals = "";
        if (realValue != null && realValue.length() > 0) {
            boolean isHigh = SharedPreferencesHelper.getBoolean(AppApplication.getInstance(),"isHigh",true);
            int decimalDiv = 0;
            if(isHigh){ //三位小数
                decimalDiv = 5;
            }else{//一位小数
                decimalDiv=7;
            }
            String substring = realValue.substring(0, decimalDiv);
            String substring2 = realValue.substring(decimalDiv, realValue.length());
            int i1 = Integer.parseInt(substring);
            finals = i1 + "." + substring2;
            String targetValue = "";
            String materialName = "";
            int index = 0;
            int begin = curTaskIndex * hasDosList.size();
            if (hasDosList != null && hasDosList.size() > 0) {
                for (int i = begin; i < hasDosList.size(); i++) {
                    ColorDosBean colorDosBean = hasDosList.get(i);
                    String tong = colorDosBean.getBarrelNo();
                    if (tong.length() == 1) {
                        tong = "00" + tong;
                    } else if (tong.length() == 2) {
                        tong = "0" + tong;
                    }
                    if (barrelNo.equals(tong)) {
                        index = i;
                        break;
                    }
                }
                ColorDosBean colorDosBean = hasDosList.get(index);
                double dTarget = Double.parseDouble(colorDosBean.getColorDos()) * scale;
                targetValue = ComputeDoubleUtil.computeDouble(dTarget);
                materialName = colorDosBean.getColorName();
                minusDosCurValue(materialName, finals);

            }
            saveCanned(targetValue, finals, barrelNo);
            for (int i = begin; i < cannedBeans.size(); i++) {
                CannedBean cannedBean = cannedBeans.get(i);
                String tong = cannedBean.getBarrelNo();
                if (tong.length() == 1) {
                    tong = "00" + tong;
                } else if (tong.length() == 2) {
                    tong = "0" + tong;
                }
                if (barrelNo.equals(tong)) {
                    //cannedBean.setCannedTargetValue(targetValue);
                    cannedBean.setCannedRealValue(finals);
                    cannedBean.setFormulaName(formulaName);
                    break;
                }
            }
            curColorIndex = 0;
            for (int i = begin; i < progressBeanList.size(); i++) {
                ProgressBean progressBean = progressBeanList.get(i);
                String tong = progressBean.getBarrelNo();
                if (tong.length() == 1) {
                    tong = "00" + tong;
                } else if (tong.length() == 2) {
                    tong = "0" + tong;
                }
                if (barrelNo.equals(tong)) {
                    curColorIndex = i;
                    break;
                }
            }
            double finalsValue = Double.parseDouble(finals);
            ProgressBean progressBean = progressBeanList.get(curColorIndex);
            progressBean.setRealValue(finalsValue);
            progressBean.setFinishState(true);
            bindAdapterColorState();//单个色浆完成
            double totalValue = progressBean.getTotalValue();
            double cannedD = Double.parseDouble(cannedPrecision);
            /////////////////////////精度
//            double wucha = Math.abs(finalsValue - totalValue);
//            if (wucha > cannedD) {
//
//                String colorNo = curColorBean.getColorNo();
//                precisionDialog("编码:" + colorNo + "目标值:" + totalValue + "实际值:" + finalsValue + "误差:" + wucha + "，超出精度阈值，是否继续");
//            }
            ////////////////////////////////////////////////更新数据库单个色浆数据

            long id = curColorBean.getId();
            int type = curColorBean.getType();
            curColorBean.setRealDos(finals);
            ColorBeanService.getInstance().updateData(curColorBean);
            ////////////////////////////////////////////////更新数据库单个色浆数据
            //initRightData();
//                        taskInfos.remove(curTaskIndex);
//                        updateData();
            backResult(finals);

        }
        isFirstProgress = true;//空打
        //sendTime = System.currentTimeMillis();//空打
        colorBeanIndex = 0; //单个色浆完成
        // curColorIndex++; //不退出页面不设0
        task = 1;
        ////////////////////////////////////////////////罐装成功
        currenDosIndex++;  //粘性导致重新进入该页面直接调用
        WriteLog.writeTxtToFile("currentDosIndex+++++++++++" + curColorIndex + "hhhh" + curTaskIndex + "hhhh" + currenDosIndex);
        if (currenDosIndex == hasDosList.size()) { //单任务全部色浆打完

            currenDosIndex = 0;
//                        if (success.equals("cannedSuccess")) {
//                            //finish();

            removeTongDialog("配方完成，请移走桶");
            isCannedSuccess = true;
            if (taskInfos != null && taskInfos.size() > 0) {
                taskInfos.remove(0);
            }

            updateData();
            curTaskIndex++;
            if (curTaskIndex < taskCount) {//多任务
                //startCmd();
                backResult2();
                tvStop.setText("继续");
            } else {
                curTaskIndex = 0;
                tvStop.setText("完成");
                canBack = true;
                String result = backResult2();

                if (!GlobalConstants.isManual) {
                    SaveJsonFile.writeXml("1", listkey, listvalue, innerCode);

                    if (listkey != null && listkey.size() > 0) {
                        listkey.clear();
                    }
                    if (listvalue != null && listvalue.size() > 0) {
                        listvalue.clear();
                    }
                    // SaveJsonFile.readJsonFile(result, 0);
                    SaveJsonFile.deleteBusyFile("busy.xml");
                    SaveJsonFile.deleteFormulaFile("");
                }
            }
            // }
        } else {
            //currenDosIndex = 0;
        }
        //}
    }

    private void handleWorkState(char[] chars) {
        String barrelNo = "";
        for (int k = 6; k < 9; k++) { //桶号
            barrelNo += String.valueOf(chars[k]);
        }
        String realValue = "";
        for (int i = 9; i < 17; i++) {//小数点根据灌装目标量小数位数决定,如果是3，则后3位是小数
            realValue += String.valueOf(chars[i]);
        }
        int begin = curTaskIndex * hasDosList.size();
        for (int i = begin; i < progressBeanList.size(); i++) {
            ProgressBean progressBean = progressBeanList.get(i);
            String tong = progressBean.getBarrelNo();
            if (tong.length() == 1) {
                tong = "00" + tong;
            } else if (tong.length() == 2) {
                tong = "0" + tong;
            }
            if (barrelNo.equals(tong)) {
                curColorIndex = i;
                break;
            }
        }
        ProgressBean progressBean = progressBeanList.get(curColorIndex);
        String tong = progressBean.getBarrelNo();
        if (tong.length() == 1) {
            tong = "00" + tong;
        } else if (tong.length() == 2) {
            tong = "0" + tong;
        }
        if (barrelNo.equals(tong)) {
            boolean isHigh = SharedPreferencesHelper.getBoolean(AppApplication.getInstance(),"isHigh",true);
            int decimalDiv = 0;
            if(isHigh){ //三位小数
                decimalDiv = 5;
            }else{//一位小数
                decimalDiv=7;
            }
            String substring = realValue.substring(0, decimalDiv);
            String substring2 = realValue.substring(decimalDiv, realValue.length());
            int i1 = Integer.parseInt(substring);
            String finals = i1 + "." + substring2;
            double finalsValue = Double.parseDouble(finals);
            WriteLog.writeTxtToFile(realValue + "realvalue3++++++++++++++++++++");
            progressBean.setRealValue(finalsValue);
            progressBean.setFinishState(false);
//                        double totalValue = progressBean.getTotalValue();
//                        double cannedD = Double.parseDouble(cannedPrecision);
//                        /////////////////////////精度
//                        if(finalsValue-totalValue>cannedD){
//                            PowderPumpProtocal.stopCannedProtocal();
//                            precisionDialog("超出精度阈值，是否继续");
//                        }
            //////////////////////////////////////////////空打判断
            if (isFirstProgress) {
                isFirstProgress = false;
                sendTime = System.currentTimeMillis();
                firstValue = finalsValue;
            }
            if (System.currentTimeMillis() - sendTime > PROGRESS_0_RATE) {
                if (finalsValue - firstValue < 0.001) {
                    isFirstProgress = true;
                    PowderPumpProtocal.stopCannedProtocal();
                    emptyCycleDialog("料可能阻塞，可终止当前任务");
                }
            }
            //////////////////////////////////////////////////空打判断
        }
        //     }
        //  }
        bindAdapterColorState();
        // updateData();
    }

    private String backResult2() {
        Coloresult coloresult = new Coloresult();
        Coloresult.AcutalQuantityBean acutalQuantityBean = new Coloresult.AcutalQuantityBean();
        acutalQuantityBean.setControlCode("1");
        acutalQuantityBean.setInnerColorCode(innerCode);
        // acutalQuantityBean.setActualQua(actualValues);
        acutalQuantityBean.setColorantCodeBean(colorCodes);
        coloresult.setAcutalQuantity(acutalQuantityBean);
        String result = GsonUtils.toJson(coloresult);
//        SaveJsonFile.readJsonFile(result, 0);
//        SaveJsonFile.deleteBusyFile("busy.xml");
//        SaveJsonFile.deleteFormulaFile("f");
        String result2 = result.replace("<colorantCodeBean>", "");
        String result3 = result2.replace("</colorantCodeBean>", "");

        colorCodes = new ArrayList<>();
        // actualValues = new ArrayList<>();
        return result3;
    }


    /////////
    Map<String, String> map = new HashMap<>();
    List<String> listkey = new ArrayList<>();
    List<String> listvalue = new ArrayList<>();

    private void backResult(String finals) {
        ColorDosBean colorDosBean = hasDosList.get(currenDosIndex);
        final String colorNo = colorDosBean.getColorName();
        Coloresult.AcutalQuantityBean.ColorantCodeBean colorantCodeBean = new Coloresult.AcutalQuantityBean.ColorantCodeBean();
        colorantCodeBean.setColorantCode(colorNo);
        colorantCodeBean.setActualQua(finals);
//        Coloresult.AcutalQuantityBean acutalQuantityBean = new Coloresult.AcutalQuantityBean();
//        acutalQuantityBean.setControlCode("1");
        colorCodes.add(colorantCodeBean);
//        map.put("ColorantCode", colorNo);
//        map.put("ActualQua", finals);
        listkey.add(colorNo);
        listvalue.add(finals);
//        Coloresult.AcutalQuantityBean.ActualQuaBean actualQuaBean = new Coloresult.AcutalQuantityBean.ActualQuaBean();
//        actualQuaBean.setContent(finals);
//        actualValues.add(actualQuaBean);
    }


    private char[] intArrayToCharArray() {
        char[] chars = new char[ints.length];
        for (int j = 0; j < ints.length; j++) {
            int aInt = ints[j];
            if (aInt > 127) {
                chars[j] = (char) (256 - aInt);
            } else {
                chars[j] = (char) aInt;
            }
        }
        return chars;
    }

    private void minusDosCurValue(String materialName, String weight) {
        if (mPumpConfigBeans != null && mPumpConfigBeans.size() > 0) {
            for (int j = 0; j < mPumpConfigBeans.size(); j++) {
                PumpConfigBean pumpConfigBean = mPumpConfigBeans.get(j);
                //String weight = nameDataBean.getWeight();

                double iWeight = 0;
                if (weight != null && weight.length() > 0) {
                    iWeight = Double.parseDouble(weight);
                }
                if (materialName != null && materialName.length() > 0 && materialName.equals(pumpConfigBean.getMaterials_name())) {
                    double curValue = pumpConfigBean.getCurValue();
                    curValue = curValue - iWeight;
                    String s = ComputeDoubleUtil.computeDouble(curValue);
                    double curValue2 = Double.parseDouble(s);
                    pumpConfigBean.setCurValue(curValue2);
                    //correctConfigService.update(j, pumpConfigBean);
                    correctConfigService.update(pumpConfigBean);
                    break;
                }
            }
        }
    }

    private void saveCanned(String targetValue, String realValue, String barrelNo) {
        cannedBean = new CannedBean();
        cannedBean.setCannedTargetValue(targetValue);
        cannedBean.setCannedRealValue(realValue);
        cannedBean.setBarrelNo(barrelNo);
        cannedBean.setFormulaName(formulaName);
        cannedBean.setId(curColorBean.getId());
        cannedBean.setColorNo(curColorBean.getColorNo());
        String currentTime = DateFormatter.getCurrentTime();
        cannedBean.setTime(currentTime);
        // cannedService.saveOrUpdate(tong, cannedBean);
        //cannedService.put(cannedBean);
        CannedResultService.getInstance().save(cannedBean);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (errorMsg != null && errorMsg.length() > 0) {
            errorCodeDialog(errorMsg);
            errorMsg = "";
        }
        if (findBarrel != null && findBarrel.length() > 0) {
            findBarrelDialog(findBarrel);
            findBarrel = "";
        }
    }

    PopDialog popDialog;

    private void removeTongDialog(String errorMsg) {
        popDialog = new PopDialog(WorkStateActivity.this, R.style.ios_dialog_style);
        //final View view = View.inflate(getMyActivity(), R.layout.pop_correct_method,null);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.pop_dialog, null);
        popDialog.setView(view);
        popDialog.initView();
        popDialog.setMessage(errorMsg);
        popDialog.setProperty(0, 0, 300, 150);//设置坐标和宽高
        //popDialog.setProperty(tvPumpConfig);
        popDialog.setCanceledOnTouchOutside(false);
        popDialog.show();
        // popDialog.setOnConfigListener(this);
    }

    private void errorCodeDialog(String errorMsg) {
        IOSDialog.Builder builder = new IOSDialog.Builder(
                WorkStateActivity.this)
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
                                if (errorMsg.equals("请移走桶")) {
//                                    if (isCannedSuccess) {
//                                        isCannedSuccess = false;
//                                        //finish();
//                                    }
                                } else {
                                    //                                   if (!errorMsg.equals("门未关")) {
//                                        char[] chars = intArrayToCharArray();
//                                        String realValue = "";
//                                        for (int i = 9; i < 17; i++) {//小数点根据灌装目标量小数位数决定,如果是3，则后3位是小数
//                                            realValue += String.valueOf(chars[i]);
//                                        }
//                                        String finals = "";
//                                        if (realValue != null && realValue.length() > 0) {
//                                            String substring = realValue.substring(0, 5);
//                                            String substring2 = realValue.substring(5, realValue.length());
//                                            int i1 = Integer.parseInt(substring);
//                                            finals = i1 + "." + substring2;
//                                        }
//                                    }
                                }
                            }
                        });
        iosDialog = builder.create();
        iosDialog.show();
    }

    private void emegencyDialog(String errorMsg, Activity currentActivity) {
        iosDialogEmegency = new IOSDialog.Builder(
                currentActivity)
                .setBtnColor(getResources().getColor(com.example.datacorrects.R.color.blue))
                .setColor(getResources().getColor(com.example.datacorrects.R.color.blue))
                .setMessage(errorMsg)
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(
                                    DialogInterface dialog,
                                    int which) {
                                if (iosDialogEmegency != null) {
                                    iosDialogEmegency.dismiss();
                                }
                            }
                        })
                .create();
        iosDialogEmegency.show();
    }


    private void findBarrelDialog(String errorMsg) {
        iosDialogBarrel = new IOSDialog.Builder(
                WorkStateActivity.this)
                .setBtnColor(getResources().getColor(R.color.blue))
                .setColor(getResources().getColor(R.color.black))
                .setMessage(errorMsg)
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(
                                    DialogInterface dialog,
                                    int which) {
                                if (iosDialogBarrel != null) {
                                    iosDialogBarrel.dismiss();
                                }
                                task = 10000;
                                PowderPumpProtocal.continueWork();
                            }
                        })
                .create();
        iosDialogBarrel.show();
    }

    private void emptyCycleDialog(String errorMsg) {
        iosDialogEmpty = new IOSDialog.Builder(
                WorkStateActivity.this)
                .setBtnColor(getResources().getColor(R.color.blue))
                .setColor(getResources().getColor(R.color.black))
                .setMessage(errorMsg)
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(
                                    DialogInterface dialog,
                                    int which) {
                                if (iosDialogEmpty != null) {
                                    iosDialogEmpty.dismiss();
                                }
                                //PowderPumpProtocal.stopCannedProtocal();

                            }
                        })
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (iosDialogEmpty != null) {
//                            iosDialogEmpty.dismiss();
//                        }
//                    }
//                })
                .create();
        iosDialogEmpty.show();
    }

    private void precisionDialog(String errorMsg) {
        iosDialogPre = new IOSDialog.Builder(
                WorkStateActivity.this)
                .setBtnColor(getResources().getColor(R.color.blue))
                .setColor(getResources().getColor(R.color.black))
                .setMessage(errorMsg)
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (iosDialogPre != null) {
                            iosDialogPre.dismiss();
                        }
                        PowderPumpProtocal.stopCannedProtocal();
//                        task = 10000;
//                        PowderPumpProtocal.continueWork();

                    }
                })
                .setPositiveButton("是",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(
                                    DialogInterface dialog,
                                    int which) {
                                if (iosDialogPre != null) {
                                    iosDialogPre.dismiss();
                                }
                                task = 10000;
                                PowderPumpProtocal.continueWork();
                                //PowderPumpProtocal.stopCannedProtocal();

                            }
                        })
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (iosDialogEmpty != null) {
//                            iosDialogEmpty.dismiss();
//                        }
//                    }
//                })
                .create();
        iosDialogPre.show();
    }

    private void updateData() {
        if (taskInfos != null && taskInfos.size() > 0) {
            table.getTableData().setT(taskInfos);
//            final TableData<TestCorrctBean> tableData = new TableData<TestCorrctBean>("测试标题",testCorrctBeans,columnMaterialName,columnMaterialType,columPumpType,
//                    columnStep,columnTargetValue,columnTestValue,columnDifference,columnTargetOffset,columnTestOffset);
//            tableViewLeft.getConfig().setShowTableTitle(false);
//            tableViewLeft.setTableData(tableData);
            table.notifyDataChanged();
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        } else {
            initRightData();
        }
    }

    private void delRow() {
        if (cannedBeans != null && cannedBeans.size() > 0) {
            cannedBeans.remove(selRowFm);
            initRightData();
        }
    }

    private void initRightData() {
        columnId = new Column<Integer>("作业ID", "taskId");
        columnFormulaName = new Column<String>("配方名称", "taskName");
//        columnBarrel = new Column<String>("桶号", "barrelNo");
//        columnTarget = new Column<String>("目标值", "cannedTargetValue");
//        columnReal = new Column<String>("实际值", "cannedRealValue");
        columnId.setOnColumnItemClickListener(new OnColumnItemClickListener<Integer>() {
            @Override
            public void onClick(Column<Integer> column, String value, Integer integer, int position) {
                selectRowFM(position);
            }
        });
        columnFormulaName.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
            @Override
            public void onClick(Column<String> column, String value, String s, int position) {
                selectRowFM(position);
            }
        });

//        columnBarrel.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
//            @Override
//            public void onClick(Column<String> column, String value, String s, int position) {
//                selectRowFM(position);
//            }
//        });
//        columnTarget.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
//            @Override
//            public void onClick(Column<String> column, String value, String s, int position) {
//                selectRowFM(position);
//            }
//        });
//        columnReal.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
//            @Override
//            public void onClick(Column<String> column, String value, String s, int position) {
//                //Toast.makeText(getMyActivity(),value+"hh"+s+"jj"+position,Toast.LENGTH_SHORT).show();
//                selectRowFM(position);
//            }
//        });

        final TableData<TaskInfo> tableData = new TableData<TaskInfo>("测试标题", taskInfos, columnId, columnFormulaName);
        table.getConfig().setShowTableTitle(false);
        table.setTableData(tableData);
        table.getConfig().setMinTableWidth(950);
        FontStyle style = new FontStyle();
        style.setTextSpSize(WorkStateActivity.this, 18);
        style.setTextColor(getResources().getColor(com.example.datacorrects.R.color.black));
//        binding.smartTable3.getConfig().setHorizontalPadding(0);
//        binding.smartTable3.getConfig().setSequenceHorizontalPadding(0);
//        table.getConfig().setColumnTitleHorizontalPadding(DensityUtil.dip2px(16));///////
//        binding.smartTable3.getConfig().setColumnTitleVerticalPadding(DensityUtil.dip2px(20));
//        binding.smartTable3.getConfig().setVerticalPadding(DensityUtil.dip2px(20));
//        binding.smartTable3.getConfig().setTextLeftOffset(0);
        table.getConfig().setContentStyle(style);
        table.getConfig().setColumnTitleStyle(style);
        table.getConfig().setColumnTitleBackground(new BaseBackgroundFormat(getResources().getColor(R.color.blue)));
        table.getConfig().setContentCellBackgroundFormat(new BaseCellBackgroundFormat<CellInfo>() {
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
                if (cellInfo.row == selRowFm) {

                    //WriteLog.writeTxtToFile("TAGCELLINFO====================" + cellInfo.row + "Hh" + selRow);
                    return ContextCompat.getColor(WorkStateActivity.this, R.color.Aqua);

                } else {
                    return TableConfig.INVALID_COLOR;
                }
            }
        });
        // tableViewLeft.getConfig().setMinTableWidth(DensityUtil.dip2px(1024));
        //tableViewLeft.setZoom(true,1.2f, 0.8f);

    }

    private void selectRowFM(int position) {
        selRowFm = position;
    }

    private void topChange() {
        if (selRowFm != 0) {
            CannedBean dataBean = cannedBeans.get(selRowFm);
            cannedBeans.set(selRowFm, cannedBeans.get(selRowFm - 1));
            cannedBeans.set(selRowFm - 1, dataBean);
            initRightData();
        }
    }

    private void bottomChange() {
        if (cannedBeans != null && cannedBeans.size() > 0) {
            int end = cannedBeans.size() - 1;
            if (selRowFm != end) {
                CannedBean dataBean = cannedBeans.get(selRowFm);
                cannedBeans.set(selRowFm, cannedBeans.get(selRowFm + 1));
                cannedBeans.set(selRowFm + 1, dataBean);
                initRightData();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
