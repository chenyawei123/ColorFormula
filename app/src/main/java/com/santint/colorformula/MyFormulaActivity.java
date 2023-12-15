package com.santint.colorformula;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.core.TableConfig;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.bg.BaseBackgroundFormat;
import com.bin.david.form.data.format.draw.IDrawFormat;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.table.TableData;
import com.bin.david.form.listener.OnColumnItemLongClickListener;
import com.blankj.utilcode.util.GsonUtils;
import com.cyw.mylibrary.application.AppApplication;
import com.cyw.mylibrary.base.BaseAppCompatActivity;
import com.cyw.mylibrary.bean.CannedBean;
import com.cyw.mylibrary.bean.ColorBean;
import com.cyw.mylibrary.bean.ColorDosBean;
import com.cyw.mylibrary.bean.CorrectParamsBean;
import com.cyw.mylibrary.bean.DDParamsBean;
import com.cyw.mylibrary.bean.FmParamsBean;
import com.cyw.mylibrary.bean.FormulaNoBean;
import com.cyw.mylibrary.bean.FormulaitemsBean;
import com.cyw.mylibrary.bean.ManualFormula;
import com.cyw.mylibrary.bean.MyFormula;
import com.cyw.mylibrary.bean.PumpConfigBean;
import com.cyw.mylibrary.bean.SaveBean;
import com.cyw.mylibrary.customView.BaseAdapter;
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
import com.google.gson.reflect.TypeToken;
import com.santint.colorformula.adapter.MyFormulaAdapter;
import com.santint.colorformula.adapter.MyFormulaColorAdapter;

import org.xutils.common.util.DensityUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * author： cyw
 */
public class MyFormulaActivity extends BaseAppCompatActivity {
    private final LayoutInflater inflater = null;
    private MyFormulaColorAdapter myFormulaColorAdapter;
    private View view = null;
    private GridLayoutManager mManager;
    private RecyclerView ryManual;
    private RecyclerView ryFormula;
    private Button btnStart;
    private Button btnStop;
    private PumpConfigBeanService correctConfigService;
    private CorrectDataService correctDataService;
    private MyFormulaService myFormulaService;
    //private ManualColorBeanService manualColorBeanService;
    private FMServiceNew fmService;
    private ManualFormulaService manualFormulaService;
    private FormulaNoService formulaNoService;
    private List<FormulaNoBean> formulaNoBeans = new ArrayList<>();
    private List<FmParamsBean> fmParamsBeans = new ArrayList<>();
    private List<PumpConfigBean> pumpConfigBeans = new ArrayList<PumpConfigBean>();
    private List<SaveBean> saveBeans = new ArrayList<>();
    private List<SaveBean.SaveDataBean> saveDatabeans = new ArrayList<>();
    private List<SaveBean.SaveDataBean> bottomSaveDatabeans = new ArrayList<>();
    private Toolbar toolbar;
    private String saveDataMaterialName = "";
    private String leftWeightName = "";
    private IOSDialog iosDialog = null;
    private CorrectParamsBean correctParamsBean;
    private String touType = "";
    private String mCurTypeName = "";
    private Button btnLeft;
    private Button btnRight;
    private View decorView;
    private int littleStepCount = 0;
    private Column<String> columnName, columDos, columnColor;
    private SmartTable<ColorBean> table;
    private List<ColorBean> manualColorBeans = new ArrayList<>();
    private TextView tvAddFormula;
    private TextView tvModifyFormula;
    private TextView tvCancel;
    private TextView tvSave;
    private int colorBeanIndex = 0;
    private List<ManualFormula> manualFormulas = new ArrayList<>();
    private EditText editBarrelNo;
    private FormulaNoBean newFormulaNoBean;
    private boolean isModify = false;
    private boolean isChangedColorPaste = false;
    private List<ColorDosBean> hasDosList = new ArrayList<>();

    private int task = 1;
    private int ddSendIndex = 0;
    private List<DDParamsBean> ddSendList = new ArrayList<>();

    String targetDos = "";
    String tong = "";
    FmParamsBean fmParamsBean;
    DDParamsBean ddParamsBean;
    PumpConfigBean pumpConfigBean;
    private List<CannedBean> cannedBeans = new ArrayList<>();
    private CannedService cannedService;
    private CannedBean cannedBean;
    private MyFormulaAdapter myFormulaAdapter;

    //private List<ColorBean> mColorBeans = new ArrayList<>();
    private EditText editFormulaName;
    private EditText editFormulaColor;
    private int selectFormulaIndex = -1;
    private ManualFormula selectFormula;
    private boolean isAddFormula = true;
    private boolean isClickAdd = false;
    private boolean isClickModify = false;
    private TableData<ColorBean> tableData = null;

    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_formula);
        //colorBeans = (List<ManualColorBean>) getIntent().getExtras().getSerializable("colorBeans");
        decorView = getWindow().getDecorView();
        AppApplication.getInstance().init(decorView);
        initView();

        //initService2();
        initService();
//        initData(mColorBeans);
        initData(new ArrayList<ColorBean>());
        // getDataList();
        getPumpConfigList();
        // getSetList();
        //getManualList();
        bindAdapterFormula();
        bindAdapterManual();
    }

    private void bindAdapterManual() {
        // if(myFormulaColorAdapter == null){
        mManager = new GridLayoutManager(this, 2);
        ryManual.setLayoutManager(mManager);
        myFormulaColorAdapter = new MyFormulaColorAdapter(this, manualColorBeans);
        ryManual.setAdapter(myFormulaColorAdapter);
        //ryManual.addItemDecoration(new DividerItemDecoration(getMyActivity(),DividerItemDecoration.VERTICAL));
//        }else{
//            myFormulaColorAdapter.refreshData(manualColorBeans);
//        }
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ryManual = findViewById(R.id.ry_manual);
        ryFormula = findViewById(R.id.ry_formula);
        table = findViewById(R.id.tb);
        tvAddFormula = findViewById(R.id.tv_add_fomula);
        tvAddFormula.setOnClickListener(l);
        tvModifyFormula = findViewById(R.id.tv_modify_formula);
        tvModifyFormula.setOnClickListener(l);
        tvCancel = findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(l);
        initFormulaButton();
        tvSave = findViewById(R.id.tv_save);
        tvSave.setOnClickListener(l);
        editBarrelNo = findViewById(R.id.edit_no);
        editFormulaName = findViewById(R.id.edit_formula_name);
        editFormulaColor = findViewById(R.id.edit_formula_color);
    }

    private void initService() {
        //correctDataService = new CorrectDataService(this);
        correctConfigService = PumpConfigBeanService.getInstance();
        fmService = FMServiceNew.getInstance();
        if (fmParamsBeans != null && fmParamsBeans.size() > 0) {
            fmParamsBeans.clear();
        }
        fmParamsBeans = fmService.getAllListByType();
        manualFormulaService = new ManualFormulaService(MyFormulaActivity.this);
        if (manualFormulas != null && manualFormulas.size() > 0) {
            manualFormulas.clear();
        }
        manualFormulas = manualFormulaService.getAll();
//        if (manualFormulas != null && manualFormulas.size() > 0) {
//            ManualFormula manualFormula = manualFormulas.get(0);
//            colorBeans = manualFormula.getColorBeans();
//        }
        formulaNoService = new FormulaNoService(MyFormulaActivity.this);
        if (formulaNoBeans != null && formulaNoBeans.size() > 0) {
            formulaNoBeans.clear();
        }
        formulaNoBeans = formulaNoService.getAll();
        myFormulaService = new MyFormulaService(MyFormulaActivity.this);
        getStandardColorBeans();
        cannedService = new CannedService(MyFormulaActivity.this);
        if (cannedBeans != null && cannedBeans.size() > 0) {
            cannedBeans.clear();
        }
        cannedBeans = cannedService.getAll();
    }
    private boolean isContains(String colorNo){
        for (int i = 0; i < manualColorBeans.size(); i++) {
            ColorBean colorBean = manualColorBeans.get(i);
            if(colorBean.getColorNo().equals(colorNo)){
                return true;
            }
        }
        return false;
    }
    private void getStandardColorBeans(){
        if (manualColorBeans != null && manualColorBeans.size() > 0) {
            manualColorBeans.clear();
        }
        List<MyFormula> myFormulas = myFormulaService.getAll();
        for(int i=0;i<myFormulas.size();i++){
            MyFormula myFormula = myFormulas.get(i);
            List<ColorBean> colorBeans = getCommonColorBeans(myFormula);
            for (int i1 = 0; i1 < colorBeans.size(); i1++) {
                ColorBean colorBean = colorBeans.get(i1);
                if(!isContains(colorBean.getColorNo())){
                    colorBean.setColorDos("");
                    manualColorBeans.add(colorBean);
                }
            }
        }
    }
    private List<ColorBean> getCommonColorBeans(MyFormula myFormula) {
        List<ColorBean> colorBeans = new ArrayList<>();
        if (myFormula.getColorformula() != null) {
            FormulaitemsBean formulaitems = myFormula.getColorformula().getFormulaitems();
//            if (formulaitems != null) {
//                if (formulaitems instanceof FormulaitemsBean) {
//                    colorBeans = ((FormulaitemsBean) formulaitems).getColorBeans();
//                } else if (formulaitems instanceof FormulaitemsBean2) {
//                    ColorBean colorBean = ((FormulaitemsBean2) formulaitems).getColorBean();
//                    colorBeans.add(colorBean);
//                }
//            }
            colorBeans = getCommonColorType(formulaitems);
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


    private void initData(List<ColorBean> colorBeans) {
        columnName = new Column<String>("名称", "coloName");
        columDos = new Column<String>("用量", "colorDos");
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
                // Log.i("TAG",cellInfo.data+"hhhg");
                if (cellInfo.data != null && cellInfo.data.length() > 0) {
                    color = Integer.parseInt(cellInfo.data);
                    paint.setStyle(Paint.Style.FILL);
                    paint.setColor(ContextCompat.getColor(MyFormulaActivity.this, color));
                    for (int i = 0; i < colorBeans.size(); i++) {
                        ColorBean colorBean = colorBeans.get(i);
                        double dos = 0;
                        String colorDos = colorBean.getColorDos();
                        if (colorDos != null && colorDos.length() > 0) {
                            dos = Double.parseDouble(colorDos);
                            float bias = (float) (208 - 5 * dos);
                            if (bias < 0) {
                                bias = 0;
                            }
                            if (cellInfo.row == i) {
                                //c.drawRect(rect.left + bias, rect.top + 15, rect.right - bias, rect.bottom - 15, paint);
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
        columnColor.setOnColumnItemLongClickListener(new OnColumnItemLongClickListener<String>() {


            @Override
            public void onLongClick(Column<String> column, String value, String s, int position) {

            }
        });

        tableData = new TableData<ColorBean>("测试标题", colorBeans, columnName, columDos,
                columnColor);
        table.getConfig().setShowTableTitle(false);
        table.setTableData(tableData);
        table.getConfig().setMinTableWidth(920);
        table.getConfig().setShowYSequence(false);
        FontStyle style = new FontStyle();
        style.setTextSpSize(this, 16);
        style.setTextColor(getResources().getColor(R.color.black));
//        table.getConfig().setHorizontalPadding(0);
//        table.getConfig().setSequenceHorizontalPadding(0);
//        table.getConfig().setColumnTitleHorizontalPadding(DensityUtil.dip2px(160));///////
        table.getConfig().setColumnTitleVerticalPadding(DensityUtil.dip2px(20));
        table.getConfig().setVerticalPadding(DensityUtil.dip2px(5));
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

    //获取泵配置集合
    private void getPumpConfigList() {
        if (pumpConfigBeans != null && pumpConfigBeans.size() > 0) {
            pumpConfigBeans.clear();
        }
        pumpConfigBeans = correctConfigService.getAllListByType();

    }

    //从MyFomulaColorAdapeter回调
    public void isChangedColorPaste(ColorBean item) {
        isChangedColorPaste = true;
        String colorNo = item.getColorNo();
        String colorDos = item.getColorDos();
        List<ColorBean> colorBeans = new ArrayList<>();
//        if(tvAddFormula.isEnabled()){
//            colorBeans.add(item);
//            selectFormulaIndex = -1;
//            bindAdapterFormula();
//
//        }else{
            if (tableData != null) {
                colorBeans = tableData.getT();
            }
            if (colorBeans != null && colorBeans.size() > 0) {
                boolean isExist = false;
                for (Iterator iterator = colorBeans.iterator(); iterator.hasNext(); ) {
                    ColorBean next = (ColorBean) iterator.next();
                    if (colorNo.equals(next.getColorNo())) {
                        isExist = true;
                        if (colorDos == null || colorDos.length() == 0) {
                            iterator.remove();
                        } else {
                            //next.setColor(item.getColor());
                            next.setColorNo(item.getColorNo());
                            String cDos = item.getColorDos();
                            if(cDos.endsWith(".")){
                                cDos= cDos+"0";
                            }
                            next.setColorDos(cDos);
                        }
                        break;
                    }
                }
                if (!isExist) {
                    if(colorDos!=null && colorDos.length()>0  && !colorDos.equals("0")){
                        colorBeans.add(item);
                    }
                }
            } else {
                if(colorDos!=null && colorDos.length()>0  && !colorDos.equals("0")){
                    colorBeans.add(item);
                }
            }
        //}
        initData(colorBeans);
    }

    private void bindAdapterFormula() {
        if (myFormulaAdapter == null) {
            ryFormula.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            myFormulaAdapter = new MyFormulaAdapter(this, manualFormulas, ryFormula);
            //ryFind.addItemDecoration(new DividerGridItemDecoration(BlueToothActivity.this));
            ryFormula.setAdapter(myFormulaAdapter);
            myFormulaAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    //if (!isAddFormula) {
                    if (isClickAdd) {
//                        ToastUtils.showShort("请先完成添加保存");
//                        ToastUtils.setMsgTextSize(18);
//                        ToastUtils.setBgResource(R.drawable.bg_gray_gray_corner);
                        MyToastUtil.showToast(MyFormulaActivity.this,"请先完成添加保存",Toast.LENGTH_LONG);
                        return;
                    }
                    clear();
                    if (myFormulaAdapter != null) {
                        myFormulaAdapter.setCheckPos(position);
                    }
                    selectFormulaIndex = position;
                    selectFormula = manualFormulas.get(position);
                    if (selectFormula != null) {
                        List<ColorBean> colorBeans = selectFormula.getColorBeans();//此处有疑问，选中配方的有用量的
                        if (isClickModify) {
                            //isClickModify = false;
                            if (manualColorBeans != null && manualColorBeans.size() > 0 && colorBeans != null && colorBeans.size() > 0) {
                                for (int i = 0; i < manualColorBeans.size(); i++) {
                                    ColorBean manualColorBean = manualColorBeans.get(i);
                                    String colorNo = manualColorBean.getColorNo();
                                    for (int j = 0; j < colorBeans.size(); j++) {
                                        ColorBean manualColorBean1 = colorBeans.get(j);
                                        if (colorNo.equals(manualColorBean1.getColorNo())) {
                                            manualColorBean.setColorDos(manualColorBean1.getColorDos());
                                        }
                                    }
                                }
                            }
                        }

                        bindAdapterManual();
                        //mColorBeans.addAll(colorBeans);
                        initData(colorBeans);
                        //  initData();
                        // editFormulaName.setText(selectFormula.getFormulaName());
                    }
                    if (!isClickModify) {
                        tvModifyFormula.setEnabled(true);
                        tvModifyFormula.setBackgroundResource(R.drawable.bg_blue_blue_corner);
                        tvAddFormula.setEnabled(false);
                        tvAddFormula.setBackgroundResource(R.drawable.bg_gray_gray_corner);
                    } else {
                        editFormulaName.setText(selectFormula.getFormulaName());
                    }


                    //tvAddFormula.setEnabled();
                    //}
//                else{
//                    ToastUtils.showShort("请先选中修改按钮");
//                    ToastUtils.setMsgTextSize(18);
//                    ToastUtils.setBgResource(R.color.gray);
//                }
                }
            });
            myFormulaAdapter.setOnItemLongClickListener(new BaseAdapter.OnItemLongClickListener() {
                @Override
                public void onItemLongClick(View view, int position) {
                    ManualFormula manualFormula = manualFormulas.get(position);
                    delFormula(position, manualFormula);
                }
            });
        } else {
            myFormulaAdapter.setCheckPos(selectFormulaIndex);
            //myFormulaAdapter.notifyItemRangeChanged(0, manualFormulas.size());
           // if(manualFormulas!=null && manualFormulas.size()>0){
                myFormulaAdapter.notifyItemRangeChanged(0, manualFormulas.size());
                //myFormulaAdapter.notifyDataSetChanged();
           // }
        }
    }

    private void delFormula(int position, ManualFormula manualFormula) {
        iosDialog = new IOSDialog.Builder(
                MyFormulaActivity.this)
                .setBtnColor(getResources().getColor(com.example.datacorrects.R.color.blue))
                .setColor(getResources().getColor(com.example.datacorrects.R.color.blue))
                .setMessage("确定要删除当前数据？")
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(
                                    DialogInterface dialog,
                                    int which) {
                                iosDialog.dismiss();
                                manualFormulaService.delete(position, manualFormula);
                                if (myFormulaAdapter != null) {
                                    myFormulaAdapter.notifyItemRemoved(position);
                                }
                                isChangedColorPaste = true;
                                clear();
                                initFormulaButton();

                            }
                        })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(
                                    DialogInterface dialog,
                                    int which) {
                                iosDialog.dismiss();
                            }
                        })

                .create();
        iosDialog.show();
    }

    private void initFormulaButton() {
        isClickAdd = false;
        isClickModify = false;
        isAddFormula = true;
        tvModifyFormula.setEnabled(false);
        tvModifyFormula.setBackgroundResource(R.drawable.bg_gray_gray_corner);
        tvAddFormula.setEnabled(true);
        tvAddFormula.setBackgroundResource(R.drawable.bg_blue_blue_corner);
        bindAdapterManual();
    }

    OnMultiClickListener l = new OnMultiClickListener() {
        @Override
        public void onMultiClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.tv_add_fomula:
                    if (isClickModify) {
//                        ToastUtils.showShort("请先完成修改");
//                        ToastUtils.setMsgTextSize(18);
//                        ToastUtils.setBgResource(R.drawable.bg_gray_gray_corner);
                        MyToastUtil.showToast(MyFormulaActivity.this,"请先完成修改",Toast.LENGTH_LONG);
                        return;
                    }
                    isClickAdd = true;
                    tvModifyFormula.setEnabled(true);
                    tvModifyFormula.setBackgroundResource(R.drawable.bg_blue_blue_corner);
                    isAddFormula = true;
                    tvAddFormula.setEnabled(false);
                    tvAddFormula.setBackgroundResource(R.drawable.bg_gray_gray_corner);

                    break;
                case R.id.tv_modify_formula:
                    if (isClickAdd) {
//                        ToastUtils.showShort("请先完成添加保存");
//                        ToastUtils.setMsgTextSize(18);
//                        ToastUtils.setBgResource(R.drawable.bg_gray_gray_corner);
                        MyToastUtil.showToast(MyFormulaActivity.this,"请先完成添加保存",Toast.LENGTH_LONG);
                        return;
                    }
                    isClickModify = true;
                    if (selectFormula != null) {
                        editFormulaName.setText(selectFormula.getFormulaName());
                        List<ColorBean> colorBeans = selectFormula.getColorBeans();
                        if (manualColorBeans != null && manualColorBeans.size() > 0 && colorBeans != null && colorBeans.size() > 0) {
                            for (int i = 0; i < manualColorBeans.size(); i++) {
                                ColorBean manualColorBean = manualColorBeans.get(i);
                                String colorNo = manualColorBean.getColorNo();
                                for (int j = 0; j < colorBeans.size(); j++) {
                                    ColorBean manualColorBean1 = colorBeans.get(j);
                                    if (colorNo.equals(manualColorBean1.getColorNo())) {
                                        manualColorBean.setColorDos(manualColorBean1.getColorDos());
                                    }
                                }
                            }
                        }
                        bindAdapterManual();
                    }

                    tvAddFormula.setEnabled(true);
                    tvAddFormula.setBackgroundResource(R.drawable.bg_blue_blue_corner);
                    isAddFormula = false;
                    tvModifyFormula.setEnabled(false);
                    tvModifyFormula.setBackgroundResource(R.drawable.bg_gray_gray_corner);

                    break;
                case R.id.tv_save:
                    String formlaName = editFormulaName.getText().toString().trim();
                    if (!isClickAdd && !isClickModify) {//tvAddFormula.isEnabled() && tvModifyFormula.isEnabled()
//                        ToastUtils.showShort("请选择添加还是修改");
//                        ToastUtils.setMsgTextSize(18);
//                        ToastUtils.setBgResource(R.drawable.bg_gray_gray_corner);
                        MyToastUtil.showToast(MyFormulaActivity.this,"请选择添加还是修改",Toast.LENGTH_LONG);
                        return;
                    }
                    if (formlaName == null || formlaName.length() == 0) {
//                        ToastUtils.showShort("请输入配方名称");
//                        ToastUtils.setMsgTextSize(18);
//                        ToastUtils.setBgResource(R.color.gray);
                        MyToastUtil.showToast(MyFormulaActivity.this,"请输入配方名称",Toast.LENGTH_LONG);
                        return;
                    }
                    List<ColorBean> colorBeans = new ArrayList<>();
                    if (tableData != null) {
                        colorBeans = tableData.getT();
                    }
                    if (colorBeans == null || colorBeans.size() == 0) {
//                        ToastUtils.showShort("请添加色浆");
//                        ToastUtils.setMsgTextSize(18);
//                        ToastUtils.setBgResource(R.color.gray);
                        MyToastUtil.showToast(MyFormulaActivity.this,"请添加色浆",Toast.LENGTH_LONG);
                        return;

                    }
                    boolean isRepeat = false;
                    if (manualFormulas != null && manualFormulas.size() > 0) {
                        for (int i = 0; i < manualFormulas.size(); i++) {
                            ManualFormula manualFormula = manualFormulas.get(i);
                            if (formlaName.equals(manualFormula.getFormulaName())) {
                                isRepeat = true;
                                break;
                            }

                        }
                    }
                    if (isAddFormula) {
                        if (isRepeat) {
//                            ToastUtils.showShort("配方名已存在");
//                            ToastUtils.setMsgTextSize(18);
//                            ToastUtils.setBgResource(R.color.gray);
                            MyToastUtil.showToast(MyFormulaActivity.this,"配方名已存在",Toast.LENGTH_LONG);
                            return;
                        }
                    }

                    if (isAddFormula) {
                        ManualFormula manualFormula = new ManualFormula();
                        manualFormula.setColorBeans(colorBeans);
                        manualFormula.setFormulaName(formlaName);
                        manualFormulaService.put(manualFormula);
//                        ToastUtils.showShort("添加配方成功");
//                        ToastUtils.setMsgTextSize(18);
//                        ToastUtils.setBgResource(R.color.gray);
                        MyToastUtil.showToast(MyFormulaActivity.this,"添加配方成功",Toast.LENGTH_LONG);
                        //bindAdapterFormula();

                    } else {
                        selectFormula.setFormulaName(formlaName);
                        selectFormula.setColorBeans(colorBeans);
                        manualFormulaService.update(selectFormulaIndex, selectFormula);
                        //bindAdapterFormula();
//                        ToastUtils.showShort("修改配方成功");
//                        ToastUtils.setMsgTextSize(18);
//                        ToastUtils.setBgResource(R.color.gray);
                        MyToastUtil.showToast(MyFormulaActivity.this,"修改配方成功",Toast.LENGTH_LONG);

                    }
                    clear();
                    initFormulaButton();

//                    tvAddFormula.setBackgroundResource(R.drawable.bg_blue_blue_corner);
//                    tvAddFormula.setEnabled(true);
//                    tvModifyFormula.setBackgroundResource(R.drawable.bg_blue_blue_corner);
//                    tvModifyFormula.setEnabled(true);
                    break;
                case R.id.tv_cancel:
                    clear();
                    initFormulaButton();
                    break;

            }
        }
    };

    private void clear() {
//        if (manualColorBeans != null && manualColorBeans.size() > 0) {
//            manualColorBeans.clear();
//        }
//
//        manualColorBeans = manualColorBeanService.getAll();
        getStandardColorBeans();
        if (isChangedColorPaste) {
            isChangedColorPaste = false;
            manualFormulas = manualFormulaService.getAll();//onItemClick 必须加这一行，否则未保存的修改会生效
            myFormulaAdapter.refreshData(manualFormulas);//清除原有数据，添加新数据
        }else{
            bindAdapterFormula();
        }

//        if (mColorBeans != null && mColorBeans.size() > 0) {
//            mColorBeans.clear();
//        }
        editFormulaName.setText("");
        selectFormulaIndex = -1;

        initData(new ArrayList<ColorBean>());
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
}
