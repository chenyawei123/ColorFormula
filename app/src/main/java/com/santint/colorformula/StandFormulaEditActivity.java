package com.santint.colorformula;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.cyw.mylibrary.bean.FmParamsBean;
import com.cyw.mylibrary.bean.FormulaNoBean;
import com.cyw.mylibrary.bean.FormulaitemsBean;
import com.cyw.mylibrary.bean.ManualFormula;
import com.cyw.mylibrary.bean.MyFormula;
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
import com.cyw.mylibrary.util.OnMultiClickListener;
import com.google.gson.reflect.TypeToken;
import com.santint.colorformula.adapter.StandEditAdapter;

import org.xutils.common.util.DensityUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * author： cyw
 */
public class StandFormulaEditActivity extends BaseAppCompatActivity {
    private final LayoutInflater inflater = null;
    private StandEditAdapter manualOperateAdapter;
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
    private List<FmParamsBean> fmParamsBeans = new ArrayList<>();
    private List<PumpConfigBean> pumpConfigBeans = new ArrayList<PumpConfigBean>();
    private List<SaveBean> saveBeans = new ArrayList<>();
    private Toolbar toolbar;
    private String saveDataMaterialName = "";
    private String leftWeightName = "";
    private String leftAlarm = "";
    private IOSDialog iosDialog = null;
    private IOSDialog iosDialog2 = null;
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
    private MyFormula myFormula;
    private int formulaIndex = 0;


    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.standformula_edit);

        //colorBeans = (List<ManualColorBean>) getIntent().getExtras().getSerializable("colorBeans");
        decorView = getWindow().getDecorView();
        AppApplication.getInstance().init(decorView);
        myFormula = (MyFormula) getIntent().getExtras().getSerializable("myFormula");
        formulaIndex = getIntent().getExtras().getInt("formulaIndex");
        initView();
        initService();
        updateDataLeft();
        // getDataList();
        getPumpConfigList();
        mManager = new GridLayoutManager(this, 2);
        ryManual.setLayoutManager(mManager);
        manualOperateAdapter = new StandEditAdapter(this, manualColorBeans);
        ryManual.setAdapter(manualOperateAdapter);
        //ryManual.addItemDecoration(new DividerItemDecoration(getMyActivity(),DividerItemDecoration.VERTICAL));
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ryManual = findViewById(R.id.ry_manual);
        table = findViewById(R.id.tb);
//        tvStart = findViewById(R.id.tv_start);
//        tvStart.setOnClickListener(l);
        tvSave = findViewById(R.id.tv_save);
        tvSave.setOnClickListener(l);
        editBarrelNo = findViewById(R.id.edit_no);
        editTongshu = findViewById(R.id.edit_tongshu);
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

    OnMultiClickListener l = new OnMultiClickListener() {
        @Override
        public void onMultiClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.tv_save:
                    FormulaitemsBean formulaitems = myFormula.getColorformula().getFormulaitems();
//                    if (formulaitems instanceof FormulaitemsBean) {
//                        ((FormulaitemsBean) formulaitems).setColorBeans(manualColorBeans);
//                    } else if (formulaitems instanceof FormulaitemsBean2) {
//                        if (manualColorBeans != null && manualColorBeans.size() > 0) {
//                            ((FormulaitemsBean2) formulaitems).setColorBean(manualColorBeans.get(0));
//                        }
//                    }
                    ((FormulaitemsBean) formulaitems).setColorBeans(manualColorBeans);
                    myFormulaService.update(formulaIndex, myFormula);
                    finish();
                    break;
                case R.id.tv_cancel:
                    // finish();
                    break;

            }
        }
    };


    public void isChangedColorPaste(ColorBean item) {
        isChangedColorPaste = true;
        String colorNo = item.getColorNo();
        String colorDos = item.getColorDos();
        if (manualColorBeans != null && manualColorBeans.size() > 0) {
            boolean isExist = false;
            for (Iterator iterator = manualColorBeans.iterator(); iterator.hasNext(); ) {
                ColorBean next = (ColorBean) iterator.next();
                if (colorNo.equals(next.getColorNo())) {
                    isExist = true;
                    if (colorDos == null || colorDos.length() == 0) {
                        iterator.remove();
                    } else {
                        //next.setColor(item.getColor());
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
//            if (!isExist) {
//                if (colorDos != null && colorDos.length() > 0 && !colorDos.equals("0")) {
//                    manualColorBeans.add(item);
//                }
//            }
        }
//        else {
//            if (colorDos != null && colorDos.length() > 0 && !colorDos.equals("0")) {
//                manualColorBeans.add(item);
//            }
//
//        }
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
        initData(manualColorBeans);
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
                    paint.setColor(ContextCompat.getColor(StandFormulaEditActivity.this, color));
                    for (int i = 0; i < colorBeans.size(); i++) {
                        ColorBean colorBean = colorBeans.get(i);
                        double dos = 0;
                        String colorDos = colorBean.getColorDos();
                        if (colorDos != null && colorDos.length() > 0) {
                            dos = Double.parseDouble(colorDos);
                            float bias = (float) (208 - 5 * dos);//(rect.right-rect.left)/2
                            if (bias < 0) {
                                bias = 0;
                            }
                            if (cellInfo.row == i) {
                                // c.drawRect(rect.left + bias, rect.top + 15, rect.right - bias, rect.bottom - 15, paint);
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
        columnColor.setOnColumnItemLongClickListener(new OnColumnItemLongClickListener<String>() {


            @Override
            public void onLongClick(Column<String> column, String value, String s, int position) {

            }
        });

        final TableData<ColorBean> tableData = new TableData<ColorBean>("测试标题", colorBeans, columnName, columDos,
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
        fmService = FMServiceNew.getInstance();
        if (fmParamsBeans != null && fmParamsBeans.size() > 0) {
            fmParamsBeans.clear();
        }
        fmParamsBeans = fmService.getAllListByType();
        manualFormulaService = new ManualFormulaService(StandFormulaEditActivity.this);
        if (manualFormulas != null && manualFormulas.size() > 0) {
            manualFormulas.clear();
        }
        manualFormulas = manualFormulaService.getAll();
//        if (manualFormulas != null && manualFormulas.size() > 0) {
//            ManualFormula manualFormula = manualFormulas.get(0);
//            colorBeans = manualFormula.getColorBeans();
//        }
        formulaNoService = new FormulaNoService(StandFormulaEditActivity.this);
        if (formulaNoBeans != null && formulaNoBeans.size() > 0) {
            formulaNoBeans.clear();
        }
        formulaNoBeans = formulaNoService.getAll();

//        manualColorBeanService = new ManualColorBeanService(ManualOperateActivity.this);
//        if (manualColorBeans != null && manualColorBeans.size() > 0) {
//            manualColorBeans.clear();
//        }
//        manualColorBeans = manualColorBeanService.getAll();

        myFormulaService = new MyFormulaService(StandFormulaEditActivity.this);
        getStandardColorBeans();
        List<ColorBean> colorBeans = getCommonColorBeans(myFormula);
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
        cannedService = new CannedService(StandFormulaEditActivity.this);
        if (cannedBeans != null && cannedBeans.size() > 0) {
            cannedBeans.clear();
        }
        cannedBeans = cannedService.getAll();

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



    private void getStandardColorBeans() {
        if (manualColorBeans != null && manualColorBeans.size() > 0) {
            manualColorBeans.clear();
        }
        List<MyFormula> myFormulas = myFormulaService.getAll();
        for (int i = 0; i < myFormulas.size(); i++) {
            MyFormula myFormula = myFormulas.get(i);
            List<ColorBean> colorBeans = getCommonColorBeans(myFormula);
            for (int i1 = 0; i1 < colorBeans.size(); i1++) {
                ColorBean colorBean = colorBeans.get(i1);
                if (!isContains(colorBean.getColorNo())) {
                    colorBean.setColorDos("");
                    manualColorBeans.add(colorBean);
                }
            }
        }
    }

    private boolean isContains(String colorNo) {
        for (int i = 0; i < manualColorBeans.size(); i++) {
            ColorBean colorBean = manualColorBeans.get(i);
            if (colorBean.getColorNo().equals(colorNo)) {
                return true;
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
        if (pumpConfigBeans != null && pumpConfigBeans.size() > 0) {
            pumpConfigBeans.clear();
        }
        pumpConfigBeans = correctConfigService.getAllListByType();

    }


//    private boolean getPumpSetBeanByName(NameDataBean nameDataBean) {
//        boolean isHaveSet = false;
//        PumpSetBean pumpSetBean = null;
//        String weight = nameDataBean.getWeight();
//        if (weight != null && weight.length() > 0) {
//            if (pumpSetBeans != null && pumpSetBeans.size() > 0) {
//                for (int i = 0; i < pumpSetBeans.size(); i++) {
//                    PumpSetBean pumpSetBean1 = pumpSetBeans.get(i);
//                    if (pumpSetBean1.getName().equals(nameDataBean.getPump_setting())) {
//                        isHaveSet = true;
//                        pumpSetBean = pumpSetBean1;
//                        nameDataBean.setPump_type(pumpSetBean.getPump_type());
//                        nameDataBean.setMaterials_type(pumpSetBean.getMaterials_type());
//                        nameDataBean.setMax_step_count_down(pumpSetBean.getMax_step_count_down());
//                        nameDataBean.setMax_step_count_up(pumpSetBean.getMax_step_count_up());
//                        nameDataBean.setSingle_amount_up(pumpSetBean.getSingle_amount_up());
//                        nameDataBean.setSingle_amount_down(pumpSetBean.getSingle_amount_down());
//                        nameDataBean.setTopUnit(pumpSetBean.getTopUnit());
//                        break;
//                    }
//                }
//            }
//            if (!isHaveSet) {
//                saveDataMaterialName += nameDataBean.getName() + " ";
//            }
//        }
//        return isHaveSet;
//    }

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
