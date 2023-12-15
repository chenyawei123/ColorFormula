package com.santint.colorformula;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.core.TableConfig;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.bg.BaseBackgroundFormat;
import com.bin.david.form.data.format.draw.IDrawFormat;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.table.TableData;
import com.bin.david.form.listener.OnColumnItemLongClickListener;
import com.cyw.mylibrary.application.AppApplication;
import com.cyw.mylibrary.base.BaseAppCompatActivity;
import com.cyw.mylibrary.bean.CannedBean;
import com.cyw.mylibrary.bean.ColorBean;
import com.cyw.mylibrary.bean.ColorDosBean;
import com.cyw.mylibrary.bean.FmParamsBean;
import com.cyw.mylibrary.bean.MyFormula;
import com.cyw.mylibrary.bean.PumpConfigBean;
import com.cyw.mylibrary.services.CannedService;
import com.cyw.mylibrary.services.FMServiceNew;
import com.cyw.mylibrary.services.PumpConfigBeanService;
import com.cyw.mylibrary.util.ComputeDoubleUtil;
import com.cyw.mylibrary.util.IOSDialog;
import com.cyw.mylibrary.util.MyToastUtil;
import com.santint.colorformula.pswkeyboard.VirtualKeyboardView;

import org.xutils.common.util.DensityUtil;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * author： cyw
 */
public class StandFormulaDetailActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private final int RIGHT_COLUMN_COUNT = 4;
    private List<ColorBean> mColorBeans = new ArrayList<>();
    private Column<String> columnNo;
    private Column<String> columnName;
    private Column<String> columDos;
    private Column<String> columRealDos;
    private Column<String> columnColor;
    private SmartTable<ColorBean> table;
    private MyFormula myFormula;
    private TextView tvFormulaNo;
    private TextView tvProduct;
    private VirtualKeyboardView virtualKeyboardView;
    private GridView gridView;
    private String editValue = "";
    private EditText editTongshu;
    private TextView tvTotal;
    private EditText editScale;
    private TextView tvZero;
    private ImageView ivDel;
    private TextView tvStart;
    private PumpConfigBeanService correctConfigService;
    private List<PumpConfigBean> pumpConfigBeans = new ArrayList<>();
    private FMServiceNew fmService;
    private List<FmParamsBean> fmParamsBeans = new ArrayList<>();
    private Toolbar toolbar;
    private TextView tvEdit;
    private List<ColorDosBean> hasDosList = new ArrayList<>();
    PumpConfigBean pumpConfigBean;
    private double totalDos = 0;
    private double testValue = 0d;
    private IOSDialog iosDialog, iosDialog2;
    private CannedBean cannedBean;
    private CannedService cannedService;
    private List<CannedBean> cannedBeans = new ArrayList<>();
    private String leftWeightName = "";
    private String leftAlarm = "";
    private int taskCount = 1;
    String strTotalDos = "";
    private int formulaIndex = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stand_formula_detail);
        View decorView = getWindow().getDecorView();
        AppApplication.getInstance().init(decorView);
        List<ColorBean> colorBeans1 = (List<ColorBean>) getIntent().getExtras().getSerializable("colorBeans");
        mColorBeans.addAll(colorBeans1);
        myFormula = (MyFormula) getIntent().getExtras().getSerializable("myFormula");
        formulaIndex = getIntent().getExtras().getInt("formulaIndex");
        // EventBus.getDefault().register(this);
        initView();
        initService();
        // initData();
        initScale(1);
        editScale.setText("1");
        tvTotal.setText(strTotalDos);
        //updateDataLeft();
        // initRightKeyBoard();
    }

    private void initScale(double scale) {
        if (mColorBeans != null && mColorBeans.size() > 0) {
            for (int i = 0; i < mColorBeans.size(); i++) {
                ColorBean colorBean = mColorBeans.get(i);
                String colorDos = colorBean.getColorDos();
                double realDosD = (Double.parseDouble(colorDos) * scale);
                String realDos = ComputeDoubleUtil.computeDouble(realDosD);
                colorBean.setRealDos(realDos);
                totalDos += Double.parseDouble(colorDos);
                //totalDos = Double.parseDouble(ComputeDoubleUtil.computeDouble(sum));
                strTotalDos = ComputeDoubleUtil.computeDouble(totalDos);
            }
        }
    }

    private void changeScale(double scale) {
        if (mColorBeans != null && mColorBeans.size() > 0) {
            for (int i = 0; i < mColorBeans.size(); i++) {
                ColorBean colorBean = mColorBeans.get(i);
                String colorDos = colorBean.getColorDos();
                double realDosD = (Double.parseDouble(colorDos) * scale);
                String realDos = ComputeDoubleUtil.computeDouble(realDosD);
                //String realDos = String.valueOf(realDosD);
                colorBean.setRealDos(realDos);
            }
        }
    }

    //获取泵配置集合
    private void getPumpConfigList() {
        if (pumpConfigBeans != null && pumpConfigBeans.size() > 0) {
            pumpConfigBeans.clear();
        }
        pumpConfigBeans = correctConfigService.getAllListByType();

    }

    private void initService() {
        correctConfigService = PumpConfigBeanService.getInstance();
        getPumpConfigList();
        fmService = FMServiceNew.getInstance();
        if (fmParamsBeans != null && fmParamsBeans.size() > 0) {
            fmParamsBeans.clear();
        }
        fmParamsBeans = fmService.getAllListByType();
        cannedService = new CannedService(StandFormulaDetailActivity.this);
        if (cannedBeans != null && cannedBeans.size() > 0) {
            cannedBeans.clear();
        }
        cannedBeans = cannedService.getAll();
    }

    private void initRightKeyBoard() {
        EditText editText = virtualKeyboardView.getEditText();
        editText.setVisibility(View.GONE);
        gridView = virtualKeyboardView.getGridView();
        tvZero = virtualKeyboardView.getTextView();
        ivDel = virtualKeyboardView.getImageView();
        tvZero.setOnClickListener(this);
        ivDel.setOnClickListener(this);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editValue += String.valueOf(position + 1);
                tvTotal.setText(editValue);
                double real = Double.parseDouble(editValue);
                double scale = real / totalDos;
                changeScale(scale);
                updateDataLeft();
            }
        });
    }

    /**
     * 禁止Edittext弹出软件盘，光标依然正常显示。
     */
    public void disableShowSoftInput() {
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            editScale.setInputType(InputType.TYPE_NULL);
        } else {
            Class<EditText> cls = EditText.class;
            Method method;
            try {
                method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(editScale, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                method = cls.getMethod("setSoftInputShownOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(editScale, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initView() {
        table = findViewById(R.id.tb);
        tvFormulaNo = findViewById(R.id.tv_no);
        tvProduct = findViewById(R.id.tv_product);
        //tvProduct.setText(myFormula.getProductName());
        //tvFormulaNo.setText(myFormula.getFormulaNo());
        editTongshu = findViewById(R.id.edit_tongshu);
        tvTotal = findViewById(R.id.tv_total);
        editScale = findViewById(R.id.edit_scale);
        //disableShowSoftInput();
        editScale.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String s1 = s.toString();
                editScale.setSelection(editScale.getText().toString().length());
                if (s1.equals("0")) {
                    //ToastUtils.showShort("不能输入0开头");
                    //Toast.makeText(StandFormulaDetailActivity.this,"不能输入0开头", Toast.LENGTH_LONG).show();
                    editScale.setText("");

                } else {
                    if (s1 != null && s1.length() > 0) {
                        double scale = Double.parseDouble(s1);
                        changeScale(scale);
                        double scaleTotal = totalDos * scale;
                        String realDos = ComputeDoubleUtil.computeDouble(scaleTotal);
                        tvTotal.setText(realDos);
                        updateDataLeft();
                        // editScale.setText(s1);
                    }
                }

            }
        });
        //virtualKeyboardView = findViewById(R.id.virtualKeyboardView);
        tvStart = findViewById(R.id.tv_start);
        tvStart.setOnClickListener(this);
        tvEdit = findViewById(R.id.tv_edit);
        tvEdit.setOnClickListener(this);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_three_big));
        Drawable drawable = toolbar.getOverflowIcon();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//是否显示返回键

//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        toolbar.getTitle();
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

    private void updateDataLeft() {
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
        initData();
        // }
//        if (colorBeans.size() >= 9) {//已经到达底部边界
//            Log.i("TAGRECTBO", colorBeans.size() + "hhhhhh" + tableViewLeft.getMatrixHelper().toRectBottom());
//            tableViewLeft.getMatrixHelper().flingBottom(1000);
//        }

        // }

    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEvent(Bundle bundle) {
//        if (bundle != null) {
//            String cmd = bundle.getString("cmdMark");
//            int[] ints = bundle.getIntArray("ints");
//            int ddSize = ddSendList.size();
//            if (cmd.equals("cmdMark")) {
//                if (task == 1) {
//                    task++;
//                    PowderPumpProtocal.childTaskProtocal(3);
//                } else if (task == 2) {
//                    task++;
//                    sendCmdCanned();
//                } else if (task == 3) {
//                    task++;
//                    sendCmdPumpSpeed();
//                } else if (task == 4) {
//                    //task++;
//                    if (ddSendIndex <= ddSize - 1) {
//                        ddParamsBean = ddSendList.get(ddSendIndex);
//                        ddSendIndex++;
//                        sendCmdjogParam();
//                    } else {
//                        task++;
//                        sendCmdVal();
//                    }
//
//
//                } else if (task == 5) {//接收到valve回复之后的处理,发送结束命令
//                    colorBeanIndex++;
//                    if (colorBeanIndex <= hasDosList.size() - 1) {//开启下一个色浆
//                        initCmd();
//                        task = 3;
//                        sendCmdCanned();
//                        // PowderPumpProtocal.startProtocal();
//
//                    } else {
//                        task++;
//                        PowderPumpProtocal.endProtocal();
//                        colorBeanIndex = 0;
//                    }
//
//                } else if (task == 6) {//收到结束命令，真正完成
////                    colorBeanIndex++;
////                    if (colorBeanIndex <= hasDosList.size() - 1) {//开启下一个色浆
////                        initCmd();
////                        task = 3;
////                        sendCmdCanned();
////                    } else {//全部完成
////                        tvStart.setEnabled(true);
////                        tvStart.setBackgroundResource(R.drawable.tv_blue_orange_selector);
////                        colorBeanIndex = 0;
////                        task = 1;
////                    }
//                }
//
//            } else if (cmd.equals("cannedSuccess")) {
//
//                char[] chars = new char[ints.length];
//                for (int j = 0; j < ints.length; j++) {
//                    int aInt = ints[j];
//                    if (aInt > 127) {
//                        chars[j] = (char) (256 - aInt);
//                    } else {
//                        chars[j] = (char) aInt;
//                    }
//                }
//                String realValue = "";
//                for (int i = 9; i < 17; i++) {//小数点根据灌装目标量小数位数决定,如果是3，则后3位是小数
//                    realValue += String.valueOf(chars[i]);
//                }
//                WriteLog.writeTxtToFile(realValue + "realvalue=========================");
//                String targetValue = "";
//                String materialName = "";
//                if (hasDosList != null && hasDosList.size() > 0) {
//                    ColorDosBean colorDosBean = hasDosList.get(colorBeanIndex);
//                    targetValue = colorDosBean.getColorDos();
//                    materialName = colorDosBean.getColorName();
//                    minusDosCurValue(materialName, targetValue);
//                }
//                String finals = "";
//                if (realValue != null && realValue.length() > 0) {
//                    String substring = realValue.substring(0, 5);
//                    String substring2 = realValue.substring(5, realValue.length());
//                    int i1 = Integer.parseInt(substring);
//                    finals = i1 + "." + substring2;
//                    saveCanned(targetValue, finals);
//                }
////                    colorBeanIndex++;
////                    if (colorBeanIndex <= hasDosList.size() - 1) {//开启下一个色浆
////                        initCmd();
////                        task = 1;
////                        //sendCmdCanned();
////                        PowderPumpProtocal.startProtocal();
////
////                    } else {//全部完成
////                tvStart.setEnabled(true);
////                tvStart.setBackgroundResource(R.drawable.tv_blue_orange_selector);
//                colorBeanIndex = 0;
//                task = 1;
//                Bundle bundle1 = new Bundle();
//                bundle1.putString("cannedSuccess", "cannedSuccess");
//                EventBus.getDefault().postSticky(bundle1);
//                // addCannedFinishMethod();
//                //findBarrelDialog("罐装成功！");
//                // }
//            }
//
//        }
//    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getPumpConfigList();
    }

    private void initData() {
        columnNo = new Column<String>("编码", "colorNo");
        columnName = new Column<String>("名称", "coloName");
        columDos = new Column<String>("标准配方", "colorDos");
        columRealDos = new Column<String>("注出量", "realDos");
        columnColor = new Column<String>("颜色", "color", new IDrawFormat<String>() {
            @Override
            public int measureWidth(Column<String> column, int position, TableConfig config) {
                return DensityUtil.dip2px(80);
            }

            @Override
            public int measureHeight(Column<String> column, int position, TableConfig config) {
                return DensityUtil.dip2px(30);
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void draw(Canvas c, Rect rect, CellInfo<String> cellInfo, TableConfig config) {
                Paint paint = config.getPaint();
                int color;
                if (cellInfo.data != null && cellInfo.data.length() > 0) {
                    color = Integer.parseInt(cellInfo.data);
                    paint.setStyle(Paint.Style.FILL);
                    paint.setColor(ContextCompat.getColor(StandFormulaDetailActivity.this, color));
                    for (int i = 0; i < mColorBeans.size(); i++) {
                        ColorBean colorBean = mColorBeans.get(i);
                        double dos = 0d;
                        String colorDos = colorBean.getColorDos();
                        if (colorDos != null && colorDos.length() > 0) {
                            dos = Double.parseDouble(colorBean.getColorDos());
                            float bias = (float) (208 - 5 * dos);
                            if (bias < 0) {
                                bias = 0;
                            }
                            if (cellInfo.row == i) {
                                //c.drawRect((rect.left + bias), (rect.top + 15),  (rect.right - bias),  (rect.bottom - 15), paint);
                                c.drawRoundRect(rect.left + bias, rect.top + 15, rect.right - bias - 15, rect.bottom - 15, 15, 15, paint);
                            }
                        }
                    }
                }
            }
        });

        columnNo.setOnColumnItemLongClickListener(new OnColumnItemLongClickListener<String>() {
            @Override
            public void onLongClick(Column<String> column, String value, String s, int position) {
                //selectRow(position);
            }
        });
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
        columRealDos.setOnColumnItemLongClickListener(new OnColumnItemLongClickListener<String>() {
            @Override
            public void onLongClick(Column<String> column, String value, String s, int position) {
                //Toast.makeText(getMyActivity(),value+"hh"+s+"jj"+position,Toast.LENGTH_SHORT).show();
                // selectRow(position);
            }
        });
        columnColor.setOnColumnItemLongClickListener(new OnColumnItemLongClickListener<String>() {


            @Override
            public void onLongClick(Column<String> column, String value, String s, int position) {

            }
        });

        final TableData<ColorBean> tableData = new TableData<ColorBean>("测试标题", mColorBeans, columnNo, columnName, columDos, columRealDos,
                columnColor);
        table.getConfig().setShowTableTitle(false);
        table.getConfig().setShowYSequence(false);
        table.setTableData(tableData);
        table.getConfig().setMinTableWidth(1250);
        FontStyle style = new FontStyle();
        style.setTextSpSize(this, 16);
        style.setTextColor(getResources().getColor(R.color.black));
//        table.getConfig().setHorizontalPadding(0);
//        table.getConfig().setSequenceHorizontalPadding(0);
//        table.getConfig().setColumnTitleHorizontalPadding(DensityUtil.dip2px(68));///////
        // table.getConfig().setColumnTitleVerticalPadding(DensityUtil.dip2px(5));
        // table.getConfig().setVerticalPadding(DensityUtil.dip2px(5));
//        table.getConfig().setTextLeftOffset(0);
        table.getConfig().setContentStyle(style);
        table.getConfig().setColumnTitleStyle(style);
//        table.getConfig().setContentCellBackgroundFormat(new BaseCellBackgroundFormat<CellInfo>() {
//            @Override
//            public int getBackGroundColor(CellInfo cellInfo) {
//                if (cellInfo.col == 3) {
//                    String value = cellInfo.value;
//                    return ContextCompat.getColor(StandFormulaDetailActivity.this, Integer.parseInt(value));
//
//                } else {
//                    return TableConfig.INVALID_COLOR;
//                }
//
//            }
//        });
        //table.getConfig().setContentBackground(new BaseBackgroundFormat(R.color.blue));
        table.getConfig().setColumnTitleBackground(new BaseBackgroundFormat(getResources().getColor(R.color.blue)));
        // tableViewLeft.getConfig().setMinTableWidth(DensityUtil.dip2px(1024));
        //tableViewLeft.setZoom(true,1.2f, 0.8f);


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_start:
//                tvStart.setEnabled(false);
//                tvStart.setBackgroundResource(R.drawable.bg_gray_gray_corner);
                String tongshu = editTongshu.getText().toString().trim();

                String scale = editScale.getText().toString().trim();
                if (tongshu == null || tongshu.length() == 0) {
//                    ToastUtils.showShort("请输入桶数");
//                    ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//                    ToastUtils.setMsgTextSize(22);
                    MyToastUtil.showToast(StandFormulaDetailActivity.this, "请输入桶数", Toast.LENGTH_LONG);
                    return;
                }
                taskCount = Integer.parseInt(tongshu);
                if (scale == null || scale.length() == 0) {
//                    ToastUtils.showShort("请输入配方倍数");
//                    ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//                    ToastUtils.setMsgTextSize(22);
                    MyToastUtil.showToast(StandFormulaDetailActivity.this, "请输入配方倍数", Toast.LENGTH_LONG);
                    return;
                }
                double scaleInt = Double.parseDouble(scale);

                if (hasDosList != null && hasDosList.size() > 0) {
                    hasDosList.clear();
                }
                for (int i = 0; i < mColorBeans.size(); i++) {
                    ColorBean colorBean = mColorBeans.get(i);
                    String colorDos = colorBean.getColorDos();
                    String colorNo= colorBean.getColorNo();
                    double colorDosInt = -1;
                    if (colorDos != null && colorDos.length() > 0) {
                        colorDosInt = Double.parseDouble(colorDos);
                        if (colorDosInt > 0) {
                            ColorDosBean colorDosBean = new ColorDosBean();
                            colorDosBean.setColorDos(colorDos);
                            colorDosBean.setPosition(i);
                            colorDosBean.setColorName(colorNo);
                            hasDosList.add(colorDosBean);
                        }
                    }
                }
                leftWeightName = "";
                leftAlarm = "";
                for (int j = 0; j < hasDosList.size(); j++) {
                    ColorDosBean colorDosBean = hasDosList.get(j);
                    int position = colorDosBean.getPosition();
                    ColorBean colorBean = mColorBeans.get(position);
                    String colorNo = colorBean.getColorNo();
                    String targetDos = colorBean.getColorDos();
                    if (pumpConfigBeans != null && pumpConfigBeans.size() > 0) {
                        for (int k = 0; k < pumpConfigBeans.size(); k++) {
                            pumpConfigBean = pumpConfigBeans.get(k);
                            int alarm_value = Integer.parseInt(pumpConfigBean.getAlarm_value());
                            if (colorNo.equals(pumpConfigBean.getMaterials_name())) {
                                String tong = String.valueOf(pumpConfigBean.getAddress());
                                colorDosBean.setBarrelNo(tong);
                                if (Double.parseDouble(targetDos) * taskCount > pumpConfigBean.getCurValue()) {
                                    leftWeightName += colorNo + "剩余" + pumpConfigBean.getCurValue();
                                }
                                if (alarm_value > pumpConfigBean.getCurValue()) {
                                    leftAlarm += colorNo + "剩余" + pumpConfigBean.getCurValue();
                                }
                            }
                        }
                    }
                }
                if (leftAlarm != null && leftAlarm.length() > 0) {
//                    ToastUtils.showLong(leftAlarm + ",剩余量不能小于报警量");
//                    ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//                    ToastUtils.setBgResource(R.color.gray);
//                    ToastUtils.setMsgTextSize(22);
                    MyToastUtil.showToast(StandFormulaDetailActivity.this, leftAlarm + ",剩余量不能小于报警量", Toast.LENGTH_LONG);
                    leftAlarm = "";
                    return;
                }
                if (leftWeightName != null && leftWeightName.length() > 0) {
//                    ToastUtils.showLong(leftWeightName + ",剩余量不能小于注出量");
//                    ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//                    ToastUtils.setBgResource(R.color.gray);
//                    ToastUtils.setMsgTextSize(22);
                    MyToastUtil.showToast(StandFormulaDetailActivity.this, leftWeightName + ",剩余量不能小于注出量", Toast.LENGTH_LONG);
                    leftWeightName = "";
                    return;
                }
//                task = 1;
//                initCmd();
//                PowderPumpProtocal.startProtocal();
//                //PowderPumpProtocal.childTaskProtocal(3);
                if (hasDosList != null && hasDosList.size() > 0) {
                    Intent intent = new Intent(StandFormulaDetailActivity.this, WorkStateActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("mark", "standDetail");
                    bundle.putSerializable("hasDosList", (Serializable) hasDosList);
                    bundle.putSerializable("myFormula", myFormula);
                    //bundle.putSerializable("pumpConfigBeans", (Serializable) pumpConfigBeans);
                    //bundle.putString("formulaName", myFormula.getFormulaName());
                    bundle.putInt("taskCount", taskCount);
                    bundle.putDouble("scale", scaleInt);
                    intent.putExtras(bundle);
                    startActivityAni(intent);
                }
                break;
            case R.id.tv_edit:
                Intent intent = new Intent(StandFormulaDetailActivity.this, StandFormulaEditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("myFormula", myFormula);
                bundle.putInt("formulaIndex",formulaIndex);
                intent.putExtras(bundle);
                startActivityAni(intent);

                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            mColorBeans = (List<ColorBean>) data.getSerializableExtra("colorBeans");
            updateDataLeft();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // EventBus.getDefault().unregister(this);
    }
}
