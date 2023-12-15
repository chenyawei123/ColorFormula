package com.santint.colorformula;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.bg.BaseBackgroundFormat;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.table.TableData;
import com.bin.david.form.listener.OnColumnItemLongClickListener;
import com.cyw.mylibrary.application.AppApplication;
import com.cyw.mylibrary.base.BaseAppCompatActivity;
import com.cyw.mylibrary.bean.CannedBean;
import com.cyw.mylibrary.bean.ColorBean;
import com.cyw.mylibrary.bean.ColorDosBean;
import com.cyw.mylibrary.bean.CorrectParamsBean;
import com.cyw.mylibrary.bean.FmParamsBean;
import com.cyw.mylibrary.bean.FormulaNoBean;
import com.cyw.mylibrary.bean.ManualFormula;
import com.cyw.mylibrary.bean.PumpConfigBean;
import com.cyw.mylibrary.bean.SaveBean;
import com.cyw.mylibrary.services.CannedService;
import com.cyw.mylibrary.services.CorrectDataService;
import com.cyw.mylibrary.services.FMServiceNew;
import com.cyw.mylibrary.services.FormulaNoService;
import com.cyw.mylibrary.services.ManualFormulaService;
import com.cyw.mylibrary.services.MyFormulaService;
import com.cyw.mylibrary.services.PumpConfigBeanService;
import com.cyw.mylibrary.util.GlobalConstants;
import com.cyw.mylibrary.util.IOSDialog;
import com.cyw.mylibrary.util.MyToastUtil;
import com.cyw.mylibrary.util.OnMultiClickListener;
import com.cyw.mylibrary.util.SharedPreferencesHelper;
import com.google.android.material.snackbar.Snackbar;
import com.santint.colorformula.adapter.ManualOperateAdapter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author： cyw
 */
public class ManualOperateActivity extends BaseAppCompatActivity {
    private final LayoutInflater inflater = null;
    private ManualOperateAdapter manualOperateAdapter;
    private View view = null;
    private GridLayoutManager mManager;
    private RecyclerView ryManual;
    private Button btnStart;
    private Button btnStop;
    private PumpConfigBeanService correctConfigService;
    private CorrectDataService correctDataService;

    //private ManualColorBeanService manualColorBeanService;
    private MyFormulaService myFormulaService;
    private FMServiceNew fmService;
    private ManualFormulaService manualFormulaService;
    private FormulaNoService formulaNoService;
    private List<FormulaNoBean> formulaNoBeans = new ArrayList<>();
    private List<FmParamsBean> mFmParamsBeans = new ArrayList<>();
    private List<PumpConfigBean> mPumpConfigBeans = new ArrayList<PumpConfigBean>();
    private List<SaveBean> saveBeans = new ArrayList<>();
    private Toolbar toolbar;
    private String saveDataMaterialName = "";
    private String leftWeightName = "";
    private String leftAlarm = "";
    private String cannedPrecisionDes = "";
    private IOSDialog iosDialog = null;
    private IOSDialog iosDialog2 = null;
    private CorrectParamsBean correctParamsBean;
    private String touType = "";
    private String mCurTypeName = "";
    private Button btnLeft;
    private Button btnRight;
    private View decorView;
    private int littleStepCount = 0;
    private Column<String> columnName, columDos, columnColor, columnBarrel;
    private SmartTable<ColorBean> table;
    private List<ColorBean> mManualColorBeans = new ArrayList<>();
    private TextView tvStart;
    private TextView tvSave;
    private int colorBeanIndex = 0;
    private List<ManualFormula> manualFormulas = new ArrayList<>();
    private EditText editBarrelNo;
    private FormulaNoBean newFormulaNoBean;
    private boolean isModify = false;
    private boolean isChangedColorPaste = false;
    private List<ColorDosBean> hasDosList = new ArrayList<>();
    private List<CannedBean> cannedBeans = new ArrayList<>();
    private CannedService cannedService;
    private CannedBean cannedBean;
    private List<ColorBean> mColorBeans = new ArrayList<>();
    private EditText editTongshu;
    private int taskCount = 1;


    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manual_operate);
        //colorBeans = (List<ManualColorBean>) getIntent().getExtras().getSerializable("colorBeans");
        decorView = getWindow().getDecorView();
        AppApplication.getInstance().init(decorView);
//        if (!EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().register(this);
//        }
        initView();

        //initService2();
        initService();
        updateDataLeft();
        //  getPumpConfigList();
        mManager = new GridLayoutManager(this, 1);

//        mManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                return 2;
//            }
//        });
        ryManual.setLayoutManager(mManager);
        manualOperateAdapter = new ManualOperateAdapter(this, mManualColorBeans);
        ryManual.setAdapter(manualOperateAdapter);
        //ryManual.addItemDecoration(new DividerItemDecoration(getMyActivity(),DividerItemDecoration.VERTICAL));
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ryManual = findViewById(R.id.ry_manual);
        table = findViewById(R.id.tb);
        tvStart = findViewById(R.id.tv_start);
        tvStart.setOnClickListener(l);
        tvSave = findViewById(R.id.tv_save);
        tvSave.setOnClickListener(l);
        editBarrelNo = findViewById(R.id.edit_no);
        editTongshu = findViewById(R.id.edit_tongshu);
//        editTongshu.setFocusableInTouchMode(false);//不可编辑
//        editTongshu.setKeyListener(null);//不可粘贴，长按不会弹出粘贴框
//        editTongshu.setClickable(false);//不可点击，但是这个效果我这边没体现出来，不知道怎没用
//        editTongshu.setFocusable(false);//不可编辑
//        editTongshu.setEnabled(false);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getPumpConfigList();
    }

    private String mTong = "";
    private PumpConfigBean mPumpConfigBean;

    OnMultiClickListener l = new OnMultiClickListener() {
        @Override
        public void onMultiClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.tv_start:
//                    tvStart.setEnabled(false);
//                    tvStart.setBackgroundResource(R.drawable.bg_gray_gray_corner);
                    String tongshu = editTongshu.getText().toString().trim();
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
                        String barrelNo = colorBean.getBarrelNo();
                        double colorDosInt = 0;
                        if (colorDos != null && colorDos.length() > 0) {
                            colorDosInt = Double.parseDouble(colorDos);
                            if (colorDosInt > 0) {
                                ColorDosBean colorDosBean = new ColorDosBean();
                                colorDosBean.setColorDos(colorDos);
                                colorDosBean.setPosition(i);
                                colorDosBean.setColorName(colorNo);
                                colorDosBean.setBarrelNo(barrelNo);
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
                    for (int j = 0; j < hasDosList.size(); j++) {
                        ColorDosBean colorDosBean = hasDosList.get(j);
                        int position = colorDosBean.getPosition();
                        ColorBean manualColorBean = mManualColorBeans.get(position);
                        String colorNo = manualColorBean.getColorNo();
                        String targetDos = manualColorBean.getColorDos();
                        String barrelNo = colorDosBean.getBarrelNo();
                        /////////////////////////计算精度
                        double dTarget = Double.parseDouble(targetDos);
                        mTong = "";
                        double dCannedPrecision = calPre(dTarget, colorNo);//拿到桶号 和pumpconfigbean 以及罐装精度
                        if (dCannedPrecision <= 0) {
                            cannedPrecisionDes += " 桶号:" + barrelNo + " 物料编号:" + colorNo + " 目标值:" + dTarget;
                        } else {
                            colorDosBean.setBarrelNo(mTong);
                            colorDosBean.setCannedPrecision(dCannedPrecision);
                        }
                        /////////////////////////计算精度


                        if (mPumpConfigBeans != null && mPumpConfigBeans.size() > 0) {
                            for (int k = 0; k < mPumpConfigBeans.size(); k++) {
                                PumpConfigBean pumpConfigBean = mPumpConfigBeans.get(k);

                                int alarm_value = 0;
                                if(pumpConfigBean.getAlarm_value()!=null && pumpConfigBean.getAlarm_value().length()>0){
                                    alarm_value = Integer.parseInt(pumpConfigBean.getAlarm_value());
                                }
                                String tong = String.valueOf(pumpConfigBean.getAddress());
                                if (barrelNo.equals(tong)) {
                                    double zhuchu = Double.parseDouble(targetDos) * taskCount;
                                    if (zhuchu > pumpConfigBean.getCurValue()) {
                                        leftWeightName += barrelNo + "号桶" + colorNo + "剩余" + pumpConfigBean.getCurValue();
                                    }
                                    if (alarm_value - zhuchu > pumpConfigBean.getCurValue()) {
                                        leftAlarm += barrelNo + "号桶" + colorNo + "剩余" + pumpConfigBean.getCurValue();
                                    }
                                }
                            }
                        }

                    }
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
                        showDialogWarn(msg, ManualOperateActivity.this);
                        cannedPrecisionDes = "";
                        return;
                    }
//                    task = 1;
//                    initCmd();
//                    PowderPumpProtocal.startProtocal();

                    if (hasDosList != null && hasDosList.size() > 0) {
                        Intent intent = new Intent(ManualOperateActivity.this, WorkStateActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("mark", "manualOperate");
                        bundle.putSerializable("hasDosList", (Serializable) hasDosList);
                        bundle.putSerializable("colorBeans", (Serializable) mManualColorBeans);
                        // bundle.putSerializable("pumpConfigBeans", (Serializable) pumpConfigBeans);
                        bundle.putString("formulaName", "手动配料");
                        bundle.putInt("taskCount", taskCount);
                        bundle.putDouble("scale", 1);
                        intent.putExtras(bundle);
                        startActivityAni(intent);
                    }

                    break;
                case R.id.tv_save:
//                    if (!isChangedColorPaste) {
//                        ToastUtils.showShort("请输入色浆量");
//                        ToastUtils.setMsgTextSize(18);
//                        return;
//                    }
//                    addSaveFormulaWindow = new AddSaveFormulaWindow(ManualOperateActivity.this, BasePopupWindow.WRAP_CONTENT, BasePopupWindow.WRAP_CONTENT);
//                    addSaveFormulaWindow.setOutSideDismiss(true);
//                    // addSaveFormulaDialog.setPopupGravity(Gravity.CENTER);
//                    //addSaveFormulaDialog.setPopupGravity(Gravity.FILL_HORIZONTAL);
//                    addSaveFormulaWindow.setAdjustInputMethod(true, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//                    AppApplication.getInstance().init(decorView);
//                    addSaveFormulaWindow.showPopupWindow();
                    break;

            }
        }
    };
    private IOSDialog iosDialogWarn;

    private void showDialogWarn(String errorMsg, Context context) {
        iosDialogWarn = new IOSDialog.Builder(
                context)
                .setBtnColor(context.getResources().getColor(R.color.blue))
                .setColor(context.getResources().getColor(R.color.blue))
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


    private FmParamsBean mFmParamsBean;

    private double calPre(double dTarget, String colorNo) {
        boolean isFloat = SharedPreferencesHelper.getBoolean(ManualOperateActivity.this, "isFloat", false);
        boolean isHigh = SharedPreferencesHelper.getBoolean(AppApplication.getInstance(),"isHigh",true);
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
                float floatValue = SharedPreferencesHelper.getFloat(ManualOperateActivity.this, "FloatValue", 0);
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
                            if(isHigh){
                                dCannedPrecision = Double.parseDouble(cannedPrecision);
                            }else{
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


    public void isChangedColorPaste(ColorBean item) {
        isChangedColorPaste = true;
        String colorNo = item.getColorNo();
        String colorDos = item.getColorDos();

//        if (mColorBeans != null && mColorBeans.size() > 0) {
//            boolean isExist = false;
//            for (int i = 0; i < mColorBeans.size(); i++) {
//                ColorBean manualColorBean = mColorBeans.get(i);
//                if (colorName.equals(manualColorBean.getColoName())) {
//                    isExist = true;
//                    manualColorBean.setColor(item.getColor());
//                    manualColorBean.setColoName(item.getColoName());
//                    manualColorBean.setColorNo(item.getColorNo());
//                    manualColorBean.setColorDos(item.getColorDos());
//                }
//            }
//            if (!isExist) {
//                mColorBeans.add(item);
//            }
//        } else {
//            mColorBeans.add(item);
//        }

        if (mColorBeans != null && mColorBeans.size() > 0) {
            boolean isExist = false;
            for (Iterator iterator = mColorBeans.iterator(); iterator.hasNext(); ) {
                ColorBean next = (ColorBean) iterator.next();
                if (colorNo.equals(next.getColorNo())) {
                    isExist = true;
                    if (colorDos == null || colorDos.length() == 0) {
                        iterator.remove();
                    } else {
                        //next.setColor(item.getColor());
                        next.setColorNo(item.getColorNo());
                        next.setColorNo(item.getColorNo());
                        String cDos = item.getColorDos();
                        if (cDos.endsWith(".")) {
                            cDos = cDos + "0";
                        }
                        next.setColorDos(cDos);
                    }
                    break;
                }
            }
            if (!isExist) {
                if (colorDos != null && colorDos.length() > 0 && !colorDos.equals("0")) {
                    mColorBeans.add(item);
                }
            }
        } else {
            if (colorDos != null && colorDos.length() > 0 && !colorDos.equals("0")) {
                mColorBeans.add(item);
            }

        }
        updateDataLeft();
    }

    public void updateDataLeft() {
//        if (madapterLeft != null) {
//            madapterLeft.clearData();
//           // List<TableCell>tableCells = obtainLeftDataList();
//            obtainLeftDataList();
//            madapterLeft.setDatas(leftCells);
//           // layoutManager.scrollVerticallyBy(0,,)
//            Log.i("TAGmadapterLeft2",madapterLeft.getDatas().size()+"hhh");
//        } else {
//            initData();
//            tableViewLeft.notifyDataChanged();
//        }
        //obtainLeftDataList()
//        if (colorBeans != null && colorBeans.size() > 0) {
//            table.getTableData().setT(colorBeans);
////            final TableData<TestCorrctBean> tableData = new TableData<TestCorrctBean>("测试标题",testCorrctBeans,columnMaterialName,columnMaterialType,columPumpType,
////                    columnStep,columnTargetValue,columnTestValue,columnDifference,columnTargetOffset,columnTestOffset);
////            tableViewLeft.getConfig().setShowTableTitle(false);
////            tableViewLeft.setTableData(tableData);
//            table.notifyDataChanged();
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        } else {
        initData(mColorBeans);
        // }
//        if (colorBeans.size() >= 9) {//已经到达底部边界
//            Log.i("TAGRECTBO", colorBeans.size() + "hhhhhh" + tableViewLeft.getMatrixHelper().toRectBottom());
//            tableViewLeft.getMatrixHelper().flingBottom(1000);
//        }

        // }

    }

    private void initData(List<ColorBean> colorBeans) {
//        madapterLeft = new CorrectAdapter(getMyActivity(), obtainLeftDataList());
//        tableViewLeft.setAdapter(madapterLeft);
//        madapterLeft.setOnItemLongClickListener(this);
//        madapterLeft.setOnItemClickListener(new TableAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//
//            }
//        });
//        madapterLeft.setOnItemLongClickListener(this);
        //columnNo = new Column<String>("编码", "colorNo");
        columnName = new Column<String>("编码", "colorNo");
        columDos = new Column<String>("用量", "colorDos");
        columnBarrel = new Column<String>("桶号", "barrelNo");
//        columnColor = new Column<String>("颜色", "color", new IDrawFormat<String>() {
//            @Override
//            public int measureWidth(Column<String> column, int position, TableConfig config) {
////                if(position == 0){
////                    return DensityUtil.dip2px(1);
////                }else if(position == 1){
////                    return DensityUtil.dip2px(10);
////                }else if(position == 2){
////                    return DensityUtil.dip2px(20);
////                }else if(position == 3){
////                    return DensityUtil.dip2px(30);
////                }else if(position == 4){
////                    return DensityUtil.dip2px(40);
////                }else{
//                return DensityUtil.dip2px(80);
//                // }
//
//            }
//
//            @Override
//            public int measureHeight(Column<String> column, int position, TableConfig config) {
////                if(position == 0){
////                    return DensityUtil.dip2px(10);
////                }else if(position == 1){
////                    return DensityUtil.dip2px(20);
////                }else if(position == 2){
////                    return DensityUtil.dip2px(30);
////                }else if(position == 3){
////                    return DensityUtil.dip2px(40);
////                }else if(position == 4){
////                    return DensityUtil.dip2px(50);
////                }else{
//                return DensityUtil.dip2px(30);
//                // }
//            }
//
//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//            @Override
//            public void draw(Canvas c, Rect rect, CellInfo<String> cellInfo, TableConfig config) {
//                Paint paint = config.getPaint();
//
//                int color;
//                // Log.i("TAG",cellInfo.data+"hhhg");
//                if (cellInfo.data != null && cellInfo.data.length() > 0) {
//                    color = Integer.parseInt(cellInfo.data);
//                    paint.setStyle(Paint.Style.FILL);
//                    paint.setColor(ContextCompat.getColor(ManualOperateActivity.this, color));
//                    for (int i = 0; i < colorBeans.size(); i++) {
//                        ColorBean colorBean = colorBeans.get(i);
//                        double dos = 0;
//                        String colorDos = colorBean.getColorDos();
//                        if (colorDos != null && colorDos.length() > 0) {
//                            dos = Double.parseDouble(colorDos);
//                            float bias = (float) (208 - 5 * dos);//(rect.right-rect.left)/2
//                            if (bias < 0) {
//                                bias = 0;
//                            }
//                            if (cellInfo.row == i) {
//                                // c.drawRect(rect.left + bias, rect.top + 15, rect.right - bias, rect.bottom - 15, paint);
//                                c.drawRoundRect(rect.left + bias, rect.top + 15, rect.right - bias - 15, rect.bottom - 15, 15, 15, paint);
//                            }
//                        }
//
//
//                    }
//                }
//
////                if(cellInfo.row == 0){
////                    c.drawRect(rect.left+5,rect.top+15,rect.right-5,rect.bottom-15,paint);
////                }else if(cellInfo.row == 1){
////                    c.drawRect(rect.left+15,rect.top+15,rect.right-15,rect.bottom-15,paint);
////                }else if(cellInfo.row == 2){
////                    c.drawRect(rect.left+25,rect.top+15,rect.right-25,rect.bottom-15,paint);
////                }else if(cellInfo.row == 3){
////                    c.drawRect(rect.left+35,rect.top+15,rect.right-35,rect.bottom-15,paint);
////                }else if(cellInfo.row == 4){
////                    c.drawRect(rect.left+35,rect.top+15,rect.right-35,rect.bottom-15,paint);
////                }
//
//
//            }
//        });
//
//        columnNo.setOnColumnItemLongClickListener(new OnColumnItemLongClickListener<String>() {
//            @Override
//            public void onLongClick(Column<String> column, String value, String s, int position) {
//                //selectRow(position);
//            }
//        });
        columnName.setOnColumnItemLongClickListener(new OnColumnItemLongClickListener<String>() {
            @Override
            public void onLongClick(Column<String> column, String value, String s, int position) {
                // selectRow(position);
            }
        });
        columDos.setOnColumnItemLongClickListener(new OnColumnItemLongClickListener<String>() {
            @Override
            public void onLongClick(Column<String> column, String value, String s, int position) {
                //Toast.makeText(getMyActivity(),value+"hh"+s+"jj"+position,Toast.LENGTH_SHORT).show();
                // selectRow(position);
            }
        });
        columnBarrel.setOnColumnItemLongClickListener(new OnColumnItemLongClickListener<String>() {
            @Override
            public void onLongClick(Column<String> column, String value, String s, int position) {
                //Toast.makeText(getMyActivity(),value+"hh"+s+"jj"+position,Toast.LENGTH_SHORT).show();
                // selectRow(position);
            }
        });
//        columnColor.setOnColumnItemLongClickListener(new OnColumnItemLongClickListener<String>() {
//
//
//            @Override
//            public void onLongClick(Column<String> column, String value, String s, int position) {
//
//            }
//        });

        final TableData<ColorBean> tableData = new TableData<ColorBean>("测试标题", colorBeans, columnName, columDos, columnBarrel);
        table.getConfig().setShowTableTitle(false);
        table.setTableData(tableData);
        table.getConfig().setMinTableWidth(1250);//1250
        table.getConfig().setShowYSequence(false);
        FontStyle style = new FontStyle();
        style.setTextSpSize(this, 16);
        style.setTextColor(getResources().getColor(R.color.black));
//        table.getConfig().setHorizontalPadding(0);
//        table.getConfig().setSequenceHorizontalPadding(0);
//        table.getConfig().setColumnTitleHorizontalPadding(DensityUtil.dip2px(96));///////
//        table.getConfig().setColumnTitleVerticalPadding(DensityUtil.dip2px(20));
//        table.getConfig().setVerticalPadding(DensityUtil.dip2px(20));
//        table.getConfig().setTextLeftOffset(0);
        table.getConfig().setContentStyle(style);
        table.getConfig().setColumnTitleStyle(style);
        table.getConfig().setColumnTitleBackground(new BaseBackgroundFormat(getResources().getColor(R.color.blue)));
//        table.getConfig().setContentCellBackgroundFormat(new BaseCellBackgroundFormat<CellInfo>() {
//            @Override
//            public int getBackGroundColor(CellInfo cellInfo) {
//                if (cellInfo.row == selRow) {
//                    Log.i("TAGTAGCELL", cellInfo.row + "jjj" + selRow);
//
//                    WriteLog.writeTxtToFile("TAGCELLINFO====================" + cellInfo.row + "Hh" + selRow);
//                    return ContextCompat.getColor(getMyActivity(), com.example.datacorrects.R.color.blue);
//
//                } else {
//                    return TableConfig.INVALID_COLOR;
//                }
//            }
//        });
        // tableViewLeft.getConfig().setMinTableWidth(DensityUtil.dip2px(1024));
        //tableViewLeft.setZoom(true,1.2f, 0.8f);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;


            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void initService() {
        //correctDataService = new CorrectDataService(this);
        correctConfigService = PumpConfigBeanService.getInstance();
        getPumpConfigList();
        fmService = FMServiceNew.getInstance();
        if (mFmParamsBeans != null && mFmParamsBeans.size() > 0) {
            mFmParamsBeans.clear();
        }
        mFmParamsBeans = fmService.getAllListByType();
        manualFormulaService = new ManualFormulaService(ManualOperateActivity.this);
        if (manualFormulas != null && manualFormulas.size() > 0) {
            manualFormulas.clear();
        }
        manualFormulas = manualFormulaService.getAll();
//        if (manualFormulas != null && manualFormulas.size() > 0) {
//            ManualFormula manualFormula = manualFormulas.get(0);
//            colorBeans = manualFormula.getColorBeans();
//        }
        formulaNoService = new FormulaNoService(ManualOperateActivity.this);
        if (formulaNoBeans != null && formulaNoBeans.size() > 0) {
            formulaNoBeans.clear();
        }
        formulaNoBeans = formulaNoService.getAll();

//        manualColorBeanService = new ManualColorBeanService(ManualOperateActivity.this);
//        if (mManualColorBeans != null && mManualColorBeans.size() > 0) {
//            mManualColorBeans.clear();
//        }
//        mManualColorBeans = manualColorBeanService.getAll();

        myFormulaService = new MyFormulaService(ManualOperateActivity.this);
        getStandardColorBeans();
        cannedService = new CannedService(ManualOperateActivity.this);
        if (cannedBeans != null && cannedBeans.size() > 0) {
            cannedBeans.clear();
        }
        cannedBeans = cannedService.getAll();

    }

    private void getStandardColorBeans() {
        if (mManualColorBeans != null && mManualColorBeans.size() > 0) {
            mManualColorBeans.clear();
        }
        if (mPumpConfigBeans != null && mPumpConfigBeans.size() > 0) {
            for (int i = 0; i < mPumpConfigBeans.size(); i++) {
                PumpConfigBean pumpConfigBean = mPumpConfigBeans.get(i);
                String materials_name = pumpConfigBean.getMaterials_name();
                int address = Integer.parseInt(pumpConfigBean.getAddress());
                if (address > 0 && !isContainsAddress(address) && materials_name != null && materials_name.length() > 0) {
                    //colorBean.setColorDos("");
                    ColorBean colorBean = new ColorBean();
                    colorBean.setColorNo(materials_name);
                    colorBean.setBarrelNo(String.valueOf(pumpConfigBean.getAddress()));
                    mManualColorBeans.add(colorBean);
                    mColorBeans.add(colorBean);
                }
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


    private void getDataList() {
        if (saveBeans != null && saveBeans.size() > 0) {
            saveBeans.clear();
        }
        saveBeans = correctDataService.getAll();

    }

    //获取泵配置集合
    private void getPumpConfigList() {
        if (mPumpConfigBeans != null && mPumpConfigBeans.size() > 0) {
            mPumpConfigBeans.clear();
        }
        mPumpConfigBeans = correctConfigService.getAllListByType();

    }

    public void refData() {
        getDataList();
        getPumpConfigList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // EventBus.getDefault().unregister(this);
    }
}
