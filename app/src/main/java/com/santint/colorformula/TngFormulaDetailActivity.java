package com.santint.colorformula;

import android.content.DialogInterface;
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
import com.cyw.mylibrary.bean.DDParamsBean;
import com.cyw.mylibrary.bean.FmParamsBean;
import com.cyw.mylibrary.bean.ManualFormula;
import com.cyw.mylibrary.bean.PumpConfigBean;
import com.cyw.mylibrary.services.CannedService;
import com.cyw.mylibrary.services.FMServiceNew;
import com.cyw.mylibrary.services.ManualColorBeanService;
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
public class TngFormulaDetailActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private final int RIGHT_COLUMN_COUNT = 4;
    private List<ColorBean> manualColorBeans = new ArrayList<>();
    private Column<String> columnNo;
    private Column<String> columnName;
    private Column<String> columDos;
    private Column<String> columRealDos;
    private Column<String> columnColor;
    private SmartTable<ColorBean> table;
    private ManualFormula manualFormula;
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

    FmParamsBean fmParamsBean;
    DDParamsBean ddParamsBean;
    PumpConfigBean pumpConfigBean;
    private double totalDos = 0;
    private double testValue = 0d;
    private IOSDialog iosDialog, iosDialog2;
    private CannedBean cannedBean;
    private CannedService cannedService;
    private List<CannedBean> cannedBeans = new ArrayList<>();
    private ManualColorBeanService manualColorBeanService;
    String strTotalDos = "";
    private String leftWeightName = "";
    private String leftAlarm = "";
    private int taskCount = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tng_formula_detail);
        View decorView = getWindow().getDecorView();
        AppApplication.getInstance().init(decorView);
        manualColorBeans = (List<ColorBean>) getIntent().getExtras().getSerializable("colorBeans");
        manualFormula = (ManualFormula) getIntent().getExtras().getSerializable("manualFormula");
        // EventBus.getDefault().register(this);
        initView();
        initService();
        //initData(colorBeans);
        initScale(1);
        editScale.setText("1");
        tvTotal.setText(strTotalDos);
//        editCanned.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                String canned = editCanned.getText().toString();
//                editCanned.setSelection(editCanned.getText().toString().length());
//
//                if(canned!=null && canned.length()>0){
//                    double real = Double.parseDouble(canned);
//                    double scale = real / totalDos;
//                    changeScale(scale);
//                    updateDataLeft();
//                }
//
//            }
//        });
       // updateDataLeft();
        // initRightKeyBoard();
    }

    private void initScale(double scale) {
        if (manualColorBeans != null && manualColorBeans.size() > 0) {
            for (int i = 0; i < manualColorBeans.size(); i++) {
                ColorBean colorBean = manualColorBeans.get(i);
                String colorDos = colorBean.getColorDos();
                double realDosD = (Double.parseDouble(colorDos) * scale);
                String realDos = ComputeDoubleUtil.computeDouble(realDosD);
                ;
                colorBean.setRealDos(realDos);
                double colorDosD = Double.parseDouble(colorDos);
                totalDos = totalDos + colorDosD;
                strTotalDos = ComputeDoubleUtil.computeDouble(totalDos);
            }
        }
    }

    private void changeScale(double scale) {
        if (manualColorBeans != null && manualColorBeans.size() > 0) {
            for (int i = 0; i < manualColorBeans.size(); i++) {
                ColorBean colorBean = manualColorBeans.get(i);
                String colorDos = colorBean.getColorDos();
                double realDosD = (Double.parseDouble(colorDos) * scale);
                String realDos = ComputeDoubleUtil.computeDouble(realDosD);
                //String realDos = String.valueOf(realDosD);
                colorBean.setRealDos(realDos);
                //totalDos += Integer.parseInt(colorDos);
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
//        if (pumpConfigBeans != null && pumpConfigBeans.size() > 0) {
//            pumpConfigBeans.clear();
//        }
//        pumpConfigBeans = correctConfigService.getAll();
        getPumpConfigList();

        fmService = FMServiceNew.getInstance();
        if (fmParamsBeans != null && fmParamsBeans.size() > 0) {
            fmParamsBeans.clear();
        }
        fmParamsBeans = fmService.getAllListByType();
        cannedService = new CannedService(TngFormulaDetailActivity.this);
        if (cannedBeans != null && cannedBeans.size() > 0) {
            cannedBeans.clear();
        }
        cannedBeans = cannedService.getAll();
//        if (manualColorBeans != null && manualColorBeans.size() > 0) {
//            manualColorBeans.clear();
//        }
//        manualColorBeans = manualColorBeanService.getAll();
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
        //tvProduct.setText(manualFormula.getProductName());
        tvFormulaNo.setText(manualFormula.getFormulaNo());
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
                   // ToastUtils.showShort("不能输入0开头");
                    editScale.setText("");

                } else {
                    // tvTotal.setText(editValue);
//                    double real = Double.parseDouble(s1);
//                    double scale = real / totalDos;
                    if (s1 != null && s1.length() > 0) {
                        double scale = Double.parseDouble(s1);
                        changeScale(scale);
                        double scaleTotal = totalDos*scale;
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
        initData(manualColorBeans);
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
////            String findBarrel= bundle.getString("manualOperateTong");
////            String  errorMsg= bundle.getString("manualOperateError");
////            if (errorMsg != null && errorMsg.length() > 0) {
////                errorCodeDialog(errorMsg);
////            }
////            if (findBarrel != null && findBarrel.length() > 0) {
////                findBarrelDialog(findBarrel);
////            }
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
////                    task++;
////                    PowderPumpProtocal.endProtocal();
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
//                //                   colorBeanIndex++;
////                    if (colorBeanIndex <= hasDosList.size() - 1) {//开启下一个色浆
////                        initCmd();
////                        task = 1;
////                        //sendCmdCanned();
////                        PowderPumpProtocal.startProtocal();
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
//
//    }

    private void errorCodeDialog(String errorMsg) {

        iosDialog = new IOSDialog.Builder(
                TngFormulaDetailActivity.this)
                .setBtnColor(getResources().getColor(R.color.blue))
                .setColor(getResources().getColor(R.color.blue))
                .setMessage(errorMsg)
//                .setPositiveButton("确定",
//                        new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(
//                                    DialogInterface dialog,
//                                    int which) {
//                                iosDialog.dismiss();
//
//                            }
//                        })
                .create();
        iosDialog.show();
    }

    private void findBarrelDialog(String errorMsg) {

        iosDialog2 = new IOSDialog.Builder(
                TngFormulaDetailActivity.this)
                .setBtnColor(getResources().getColor(R.color.blue))
                .setColor(getResources().getColor(R.color.black))
                .setMessage(errorMsg)
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(
                                    DialogInterface dialog,
                                    int which) {
                                if (iosDialog2 != null) {
                                    iosDialog2.dismiss();
                                }


                            }
                        })
                .create();
        iosDialog2.show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getPumpConfigList();
    }

    private void initData(List<ColorBean> colorBeans) {
        columnNo = new Column<String>("编码", "colorNo");
        columnName = new Column<String>("名称", "coloName");
        columDos = new Column<String>("标准配方", "colorDos");
        columRealDos = new Column<String>("注出量", "realDos");
        columnColor = new Column<String>("颜色", "color", new IDrawFormat<String>() {
            @Override
            public int measureWidth(Column<String> column, int position, TableConfig config) {
//                if(position == 0){
//                    return DensityUtil.dip2px(1);
//                }else if(position == 1){
//                    return DensityUtil.dip2px(10);
//                }else if(position == 2){
//                    return DensityUtil.dip2px(20);
//                }else if(position == 3){
//                    return DensityUtil.dip2px(30);
//                }else if(position == 4){
//                    return DensityUtil.dip2px(40);
//                }else{
                return DensityUtil.dip2px(80);
                // }

            }

            @Override
            public int measureHeight(Column<String> column, int position, TableConfig config) {
//                if(position == 0){
//                    return DensityUtil.dip2px(10);
//                }else if(position == 1){
//                    return DensityUtil.dip2px(20);
//                }else if(position == 2){
//                    return DensityUtil.dip2px(30);
//                }else if(position == 3){
//                    return DensityUtil.dip2px(40);
//                }else if(position == 4){
//                    return DensityUtil.dip2px(50);
//                }else{
                return DensityUtil.dip2px(30);
                // }
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void draw(Canvas c, Rect rect, CellInfo<String> cellInfo, TableConfig config) {
                Paint paint = config.getPaint();
                int color;
                if (cellInfo.data != null && cellInfo.data.length() > 0) {
                    color = Integer.parseInt(cellInfo.data);
                    paint.setStyle(Paint.Style.FILL);
                    paint.setColor(ContextCompat.getColor(TngFormulaDetailActivity.this, color));
                    for (int i = 0; i < colorBeans.size(); i++) {
                        ColorBean colorBean = colorBeans.get(i);
                        double dos = 0;
                        String colorDos = colorBean.getColorDos();
                        if (colorDos != null && colorDos.length() > 0) {
                            dos = Double.parseDouble(colorBean.getColorDos());
                            float bias = (float) (208 - 5 * dos);
                            if (bias < 0) {
                                bias = 0;
                            }
                            if (cellInfo.row == i) {
                                // c.drawRect((rect.left + bias), (rect.top + 15), (rect.right - bias),(rect.bottom - 15), paint);
                                c.drawRoundRect(rect.left + bias, rect.top + 15, rect.right - bias - 15, rect.bottom - 15, 15, 15, paint);
                            }
                        }
                    }
                }

//                if(cellInfo.row == 0){
//                    c.drawRect(rect.left+5,rect.top+15,rect.right-5,rect.bottom-15,paint);
//                }else if(cellInfo.row == 1){
//                    c.drawRect(rect.left+15,rect.top+15,rect.right-15,rect.bottom-15,paint);
//                }else if(cellInfo.row == 2){
//                    c.drawRect(rect.left+25,rect.top+15,rect.right-25,rect.bottom-15,paint);
//                }else if(cellInfo.row == 3){
//                    c.drawRect(rect.left+35,rect.top+15,rect.right-35,rect.bottom-15,paint);
//                }else if(cellInfo.row == 4){
//                    c.drawRect(rect.left+35,rect.top+15,rect.right-35,rect.bottom-15,paint);
//                }


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

        final TableData<ColorBean> tableData = new TableData<ColorBean>("测试标题", colorBeans, columnNo, columnName, columDos, columRealDos,
                columnColor);
        table.getConfig().setShowTableTitle(false);
        table.setTableData(tableData);
        table.getConfig().setMinTableWidth(1250);
        table.getConfig().setShowYSequence(false);
        FontStyle style = new FontStyle();
        style.setTextSpSize(this, 16);
        style.setTextColor(getResources().getColor(R.color.black));
//        table.getConfig().setHorizontalPadding(0);
//        table.getConfig().setSequenceHorizontalPadding(0);
//        table.getConfig().setColumnTitleHorizontalPadding(DensityUtil.dip2px(68));///////
//        table.getConfig().setColumnTitleVerticalPadding(DensityUtil.dip2px(5));
//        table.getConfig().setVerticalPadding(DensityUtil.dip2px(5));
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
            case R.id.id_tv_zero:
                editValue = virtualKeyboardView.getEditText().getText().toString();
                if (editValue != null && !editValue.equals("")) {
                    editValue += "0";
                    tvTotal.setText(editValue);
                    double real = Double.parseDouble(editValue);
                    double scale = real / totalDos;
                    changeScale(scale);
                    updateDataLeft();
                }
                break;
            case R.id.id_iv_del:
                editValue = "";
                tvTotal.setText(editValue);
                if (editValue == null || editValue.length() == 0) {
                    changeScale(1);
                    updateDataLeft();
                }
                break;
            case R.id.tv_start:
//                tvStart.setEnabled(false);
//                tvStart.setBackgroundResource(R.drawable.bg_gray_gray_corner);
                String tongshu = editTongshu.getText().toString().trim();
                if(tongshu == null || tongshu.length() == 0){
//                    ToastUtils.showShort("请输入桶数");
//                    ToastUtils.setGravity(Gravity.CENTER,0,0);
//                    ToastUtils.setMsgTextSize(22);
                    MyToastUtil.showToast(TngFormulaDetailActivity.this,"请输入桶数",Toast.LENGTH_SHORT);
                    return;
                }
                taskCount = Integer.parseInt(tongshu);

                String scale = editScale.getText().toString().trim();
                if(scale == null || scale.length() == 0){
//                    ToastUtils.showShort("请输入配方倍数");
//                    ToastUtils.setGravity(Gravity.CENTER,0,0);
//                    ToastUtils.setMsgTextSize(22);
                    MyToastUtil.showToast(TngFormulaDetailActivity.this,"请输入配方倍数",Toast.LENGTH_SHORT);
                    return;
                }
                double scaleInt = Double.parseDouble(scale);
                if (hasDosList != null && hasDosList.size() > 0) {
                    hasDosList.clear();
                }
                for (int i = 0; i < manualColorBeans.size(); i++) {
                    ColorBean colorBean = manualColorBeans.get(i);
                    String colorDos = colorBean.getColorDos();
                    String colorNo = colorBean.getColorNo();
                    double colorDosInt = 0;
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
                    ColorBean manualColorBean = manualColorBeans.get(position);
                    String colorNo = manualColorBean.getColorNo();
                    String targetDos = manualColorBean.getColorDos();
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
                    MyToastUtil.showToast(TngFormulaDetailActivity.this,leftAlarm+",剩余量不能小于报警量",Toast.LENGTH_SHORT);
                    leftAlarm = "";
                    return;
                }
                if (leftWeightName != null && leftWeightName.length() > 0) {
//                    ToastUtils.showLong(leftWeightName + ",剩余量不能小于注出量");
//                    ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//                    ToastUtils.setBgResource(R.color.gray);
//                    ToastUtils.setMsgTextSize(22);
                    MyToastUtil.showToast(TngFormulaDetailActivity.this,leftWeightName+",剩余量不能小于注出量",Toast.LENGTH_SHORT);
                    leftWeightName = "";
                    return;
                }
//                task = 1;
//                initCmd();
//                PowderPumpProtocal.startProtocal();
                if (hasDosList != null && hasDosList.size() > 0) {
                    Intent intent = new Intent(TngFormulaDetailActivity.this, WorkStateActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("mark", "tngDetail");
                    bundle.putSerializable("hasDosList", (Serializable) hasDosList);
                    bundle.putSerializable("manualFormula", manualFormula);
                    // bundle.putSerializable("pumpConfigBeans", (Serializable) pumpConfigBeans);
                    bundle.putString("formulaName", manualFormula.getFormulaName());
                    bundle.putInt("taskCount", taskCount);
                    bundle.putDouble("scale",scaleInt);
                    intent.putExtras(bundle);
                    startActivityAni(intent);
                }
                //PowderPumpProtocal.childTaskProtocal(3);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            manualColorBeans = (List<ColorBean>) data.getSerializableExtra("colorBeans");
            updateDataLeft();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // EventBus.getDefault().unregister(this);
    }
}
