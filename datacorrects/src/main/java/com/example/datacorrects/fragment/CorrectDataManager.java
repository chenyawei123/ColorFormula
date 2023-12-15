package com.example.datacorrects.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bin.david.form.core.TableConfig;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.bg.BaseBackgroundFormat;
import com.bin.david.form.data.format.bg.BaseCellBackgroundFormat;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.table.TableData;
import com.bin.david.form.listener.OnColumnItemClickListener;
import com.cyw.mylibrary.application.AppApplication;
import com.cyw.mylibrary.base.BaseFragment;
import com.cyw.mylibrary.bean.DDParamsBean;
import com.cyw.mylibrary.bean.FmParamsBean;
import com.cyw.mylibrary.bean.PumpConfigBean;
import com.cyw.mylibrary.services.DDService;
import com.cyw.mylibrary.services.FMServiceNew;
import com.cyw.mylibrary.services.PumpConfigBeanService;
import com.cyw.mylibrary.util.EtInputFilters;
import com.cyw.mylibrary.util.IOSDialog;
import com.cyw.mylibrary.util.MyToastUtil;
import com.cyw.mylibrary.util.OnMultiClickListener;
import com.cyw.mylibrary.util.SharedPreferencesHelper;
import com.cyw.mylibrary.util.StrMatcher;
import com.example.datacorrects.R;
import com.example.datacorrects.customView.DrawableEditText;
import com.example.datacorrects.databinding.CorrectDataManagerBinding;
import com.google.android.material.snackbar.Snackbar;

import org.xutils.common.util.DensityUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import popup.BasePopup.BasePopupWindow;

/**
 * author： cyw
 */
public class CorrectDataManager extends BaseFragment {
    CorrectDataManagerBinding binding;
    View decorView;
    private FMServiceNew fmService;
    private PumpConfigBeanService correctConfigService;
    private List<PumpConfigBean> pumpConfigBeans = new ArrayList<>();
    List<FmParamsBean> fmAll = new ArrayList<>();
    //点动参数设置
    private Column<String> columnDDNo;
    private Column<String> columnDDTarget;
    private Column<String> columnDDStep;
    private Column<String> columnDDCycle;
    private Column<String> columnDDBeforeStep;

    //阀门参数设置
    private Column<String> columnFMNo;
    private Column<String> columnHigh;
    private Column<String> columnLow;
    private Column<String> columnClose;
    private Column<String> columnPrecision;
    private Column<Integer> columnDDCount;
    private Column<String> columnName;
    private Column<String> columnOpenBig;
    private Column<String> columnCloseBig;
    private Column<String> columnOpenSmall;
    private Column<String> columnCloseSmall;
    private int selRow = -1;
    private int selRowFm = 0;
    private IOSDialog iosDialog;
    private String mBarrelNo = "";
    private FmParamsBean mFmParamsBean;
    private DDParamsBean mDDParamsBean;
    private int ddType = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // binding = DataBindingUtil.setContentView(getMyActivity(), R.layout.correct_data_manager);
        binding = CorrectDataManagerBinding.inflate(inflater);
        initView();
        initService();
        setOnClick();
        initTable();
        decorView = binding.getRoot();
        return decorView;
    }

    private void initTable() {
        List<DDParamsBean> ddParamsBeans = DDService.getInstance().getAllListByType(mBarrelNo);

        if (ddParamsBeans == null || ddParamsBeans.size() == 0) {
            ddParamsBeans = new ArrayList<>();
        }else{
            compartorSort(ddParamsBeans);
        }
        initData(ddParamsBeans);

        initRightData();
    }

    boolean isHigh = true;

    public void refData() {
        isHigh = SharedPreferencesHelper.getBoolean(getMyActivity(), "isHigh", true);
        if (isHigh) {
            binding.editDdCount.setVisibility(View.VISIBLE);
            binding.tvDdcount.setVisibility(View.VISIBLE);
            binding.drawableEdittextMedi.setClickable(true);
            binding.drawableEdittextMedi.setFocusable(true);
            binding.drawableEdittextMedi.setFocusableInTouchMode(true);
            //editStep.setVisibility(View.VISIBLE);
            numOfDecimals = 3;

        } else {
            binding.editDdCount.setVisibility(View.INVISIBLE);
            binding.tvDdcount.setVisibility(View.INVISIBLE);
            binding.drawableEdittextMedi.setText("0.1");
            binding.drawableEdittextMedi.setClickable(false);
            binding.drawableEdittextMedi.setFocusable(false);
            binding.drawableEdittextMedi.setFocusableInTouchMode(false);
            // editStep.setVisibility(View.GONE);
            numOfDecimals = 1;
        }
        initTable();
    }

    int numOfDecimals = 1; //小数点后几位

    private void initView() {
        isHigh = SharedPreferencesHelper.getBoolean(getMyActivity(), "isHigh", true);
        binding.editNo.setClickable(false);
        binding.editNo.setFocusable(false);
        binding.editNo.setFocusableInTouchMode(false);
        EtInputFilters filter = new EtInputFilters(EtInputFilters.TYPE_MAXNUMBER);
        double minValue = 0;
        if (isHigh) {
            minValue = 0.001;
            numOfDecimals = 3;
        } else {
            minValue = 0.1;
            numOfDecimals = 1;
        }
        filter.setMaxNum(minValue, 1000000, 3);
        binding.drawableEdittextHigh.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        binding.drawableEdittextMedi.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        binding.drawableEdittextLow.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        binding.drawableEdittextClose.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        binding.editDdCount.setInputType(InputType.TYPE_CLASS_NUMBER);
        StrMatcher.setEditTextInhibitInputSpace(binding.drawableEdittextHigh.getEditText());//不能有空格
        StrMatcher.setEditTextInhibitInputSpace(binding.drawableEdittextMedi.getEditText());
        StrMatcher.setEditTextInhibitInputSpace(binding.drawableEdittextLow.getEditText());
        StrMatcher.setEditTextInhibitInputSpace(binding.drawableEdittextClose.getEditText());
        StrMatcher.setEditTextInhibitInputSpace(binding.editDdCount);
        setChangedText(binding.drawableEdittextHigh.getEditText());
        //setChangedText(binding.drawableEdittextMedi.getEditText());
        setChangedText(binding.drawableEdittextLow.getEditText());
        setChangedText(binding.drawableEdittextClose.getEditText());

        binding.editDdCount.setInputType(InputType.TYPE_CLASS_NUMBER);
        setChangedTextDD(binding.editDdCount);


        binding.editOpenbig.setInputType(InputType.TYPE_CLASS_NUMBER);
        setChangedTextDD(binding.editOpenbig);
        //binding.editOpenbig.setFilters(new InputFilter[]{filter});
        binding.editClosebig.setInputType(InputType.TYPE_CLASS_NUMBER);
        setChangedTextDD(binding.editClosebig);
        binding.editOpensmall.setInputType(InputType.TYPE_CLASS_NUMBER);
        setChangedTextDD(binding.editOpensmall);
        binding.editClosesmall.setInputType(InputType.TYPE_CLASS_NUMBER);
        setChangedTextDD(binding.editClosesmall);
    }

    /**
     * 设置小数后位数控制
     */
    private InputFilter lendFilter = new InputFilter() {
        //start == 0  end =1   dstart dend 相等，从0 开始变化  ，source 为新输入的值   dest 为已经输入的值
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            // 删除等特殊字符，直接返回
            if ("".equals(source.toString())) {
                return null;
            }
            String dValue = dest.toString();
            String[] splitArray = dValue.split("\\.");
            if (splitArray.length > 1) {
                String dotValue = splitArray[1];
                int diff = dotValue.length() + 1 - 3;//4表示输入框的小数位数
                if (diff > 0) {
                    return source.subSequence(start, end - diff);
                }
            }
            return null;
        }
    };

    private void setChangedText(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {///start 为 当前输入位置在总字符串中的下标    count为新输入数量
                Log.i("TAGSEQUE", s.toString() + "jj" + start + "hh" + before + "hhh" + count);
                if (start >= 0) {
                    Pattern mPattern = Pattern.compile("^"
                            + "[0-9]*\\.?[0-9]" + (numOfDecimals > 0 ? ("{0," + numOfDecimals + "}$") : "*"));
                    if (!mPattern.matcher(s.toString()).matches()) {
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {//Editable extends CharSequence
                String str = editable.toString();
                int index = editText.getSelectionStart();//获取光标位置
                int posDot = str.indexOf(".");//返回指定字符在此字符串中第一次出现处的索引
                //////////////////////////////////两种限制小数点后几位的方法
//                Pattern mPattern = Pattern.compile("^"
//                        + "[0-9]*\\.?[0-9]" + (numOfDecimals > 0 ? ("{0," + numOfDecimals + "}$") : "*"));
//                if (!mPattern.matcher(str).matches()) {
//                    editable.delete(index-1,index);//删除光标前的字符
//                    return;
//                }
                if (str.length() - posDot - 1 > numOfDecimals)//如果包含小数点
                {
                    editable.delete(index - 1, index);//删除光标前的字符
                    return;
                }
                //////////////////////////////////两种限制小数点后几位的方法

                if (str.equals(".")) {
                    editText.setText("");
                } else if (str.length() > 1 && str.substring(0, 1).equals("0") && !str.substring(1, 2).equals(".")) {
                    editText.setText("0");
                    editText.setSelection(editText.getText().toString().length());
                } else {
//                    item.setColorDos(weight+"");
//                    if(weight!=null && weight.length()>0 ){
//                        if(!weight.substring(weight.length()-1).equals(".") && !weight.equals("0") && Double.parseDouble(weight)>0){
//                            ((ManualOperateActivity)context).isChangedColorPaste(item);
//                        }
//
//                    }else{
//                        ((ManualOperateActivity)context).isChangedColorPaste(item);
//                    }
                }
            }
        });
    }

    private void setChangedTextDD(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                if (str.length() > 1 && str.substring(0, 1).equals("0")) {
                    editText.setText("0");
                }
            }
        });
    }


    private void initService() {
        correctConfigService = PumpConfigBeanService.getInstance();
        if (pumpConfigBeans != null && pumpConfigBeans.size() > 0) {
            pumpConfigBeans.clear();
        }
        pumpConfigBeans = correctConfigService.getAllListByType();
        fmService = FMServiceNew.getInstance();
        if (fmAll != null && fmAll.size() > 0) {
            fmAll.clear();
        }
        fmAll = fmService.getAllListByType();
        if (fmAll != null && fmAll.size() > 0) {
            mFmParamsBean = fmAll.get(0);
            mBarrelNo = mFmParamsBean.getBarrelNo();
            if (mFmParamsBean != null) {
                binding.drawableEdittextMedi.setText(mFmParamsBean.getCannedaPrecision());
            }

            if (isHigh) {
                binding.editDdCount.setVisibility(View.VISIBLE);
                binding.tvDdcount.setVisibility(View.VISIBLE);
                binding.drawableEdittextMedi.setClickable(true);
                binding.drawableEdittextMedi.setFocusable(true);
                binding.drawableEdittextMedi.setFocusableInTouchMode(true);

            } else {
                binding.editDdCount.setVisibility(View.INVISIBLE);
                binding.tvDdcount.setVisibility(View.INVISIBLE);
                binding.drawableEdittextMedi.setText("0.1");
                binding.drawableEdittextMedi.setClickable(false);
                binding.drawableEdittextMedi.setFocusable(false);
                binding.drawableEdittextMedi.setFocusableInTouchMode(false);
            }
            setFMParamsText(mFmParamsBean);

            for (int i = 0; i < fmAll.size(); i++) {
                FmParamsBean fmParamsBean = fmAll.get(i);
                for (int i1 = 0; i1 < pumpConfigBeans.size(); i1++) {
                    PumpConfigBean pumpConfigBean = pumpConfigBeans.get(i1);
                    if (fmParamsBean.getBarrelNo().equals(pumpConfigBean.getAddress())) {
                        fmParamsBean.setMaterialName(pumpConfigBean.getMaterials_name());
                    }
                }
            }
        }
    }

    //    private void updateDataLeft() {
////        if (madapterLeft != null) {
////            madapterLeft.clearData();
////           // List<TableCell>tableCells = obtainLeftDataList();
////            obtainLeftDataList();
////            madapterLeft.setDatas(leftCells);
////           // layoutManager.scrollVerticallyBy(0,,)
////            Log.i("TAGmadapterLeft2",madapterLeft.getDatas().size()+"hhh");
////        } else {
////            initData();
////            tableViewLeft.notifyDataChanged();
////        }
//        //obtainLeftDataList()
//        if (ddAll != null && ddAll.size() > 0) {
//            binding.smartTable2.getTableData().setT(ddAll);
////            final TableData<TestCorrctBean> tableData = new TableData<TestCorrctBean>("测试标题",testCorrctBeans,columnMaterialName,columnMaterialType,columPumpType,
////                    columnStep,columnTargetValue,columnTestValue,columnDifference,columnTargetOffset,columnTestOffset);
////            tableViewLeft.getConfig().setShowTableTitle(false);
////            tableViewLeft.setTableData(tableData);
//            binding.smartTable2.notifyDataChanged();
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        } else {
//            initData();
//        }
//    }
    private void updateDataRight() {
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
//        if (fmAll != null && fmAll.size() > 0) {
//            binding.smartTable3.getTableData().setT(fmAll);
////            final TableData<TestCorrctBean> tableData = new TableData<TestCorrctBean>("测试标题",testCorrctBeans,columnMaterialName,columnMaterialType,columPumpType,
////                    columnStep,columnTargetValue,columnTestValue,columnDifference,columnTargetOffset,columnTestOffset);
////            tableViewLeft.getConfig().setShowTableTitle(false);
////            tableViewLeft.setTableData(tableData);
//            binding.smartTable3.notifyDataChanged();
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        } else {
        initRightData();
        //}
    }

    private void initData(List<DDParamsBean> ddParamsBeans) {
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
        columnDDNo = new Column<String>("桶号", "barrelNo");
        columnDDTarget = new Column<String>("点动目标量(mg)", "ddTargetDos");
        columnDDStep = new Column<String>("振动步数", "ddStep");
        columnDDCycle = new Column<String>("点动周期(ms)", "ddCycle");
        columnDDBeforeStep = new Column<String>("点动前步数", "ddBeforeStep");
        columnDDNo.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
            @Override
            public void onClick(Column<String> column, String value, String s, int position) {
                selectRow(position);
            }
        });
        columnDDTarget.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
            @Override
            public void onClick(Column<String> column, String value, String s, int position) {
                selectRow(position);
            }
        });
        columnDDStep.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
            @Override
            public void onClick(Column<String> column, String value, String s, int position) {
                selectRow(position);
            }
        });
        columnDDCycle.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
            @Override
            public void onClick(Column<String> column, String value, String s, int position) {
                //Toast.makeText(getMyActivity(),value+"hh"+s+"jj"+position,Toast.LENGTH_SHORT).show();
                selectRow(position);
            }
        });
        columnDDBeforeStep.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
            @Override
            public void onClick(Column<String> column, String value, String s, int position) {
                selectRow(position);
            }
        });
        TableData<DDParamsBean> tableData = null;
        if (isHigh) {
            tableData = new TableData<DDParamsBean>("测试标题", ddParamsBeans, columnDDNo, columnDDTarget,
                    columnDDStep, columnDDCycle, columnDDBeforeStep);
        } else {
            tableData = new TableData<DDParamsBean>("测试标题", ddParamsBeans, columnDDNo, columnDDTarget,
                    columnDDCycle, columnDDBeforeStep);
        }

        binding.smartTable2.getConfig().setShowTableTitle(false);
        binding.smartTable2.setTableData(tableData);
        binding.smartTable2.getConfig().setMinTableWidth(950);
        binding.smartTable2.getConfig().setShowYSequence(false);
        FontStyle style = new FontStyle();
        style.setTextSpSize(getMyActivity(), 18);
        style.setTextColor(getResources().getColor(R.color.black));
        binding.smartTable2.getConfig().setHorizontalPadding(DensityUtil.dip2px(1));
//        binding.smartTable2.getConfig().setSequenceHorizontalPadding(0);
        binding.smartTable2.getConfig().setColumnTitleHorizontalPadding(DensityUtil.dip2px(1));///////
//        binding.smartTable2.getConfig().setColumnTitleVerticalPadding(DensityUtil.dip2px(20));
//        binding.smartTable2.getConfig().setVerticalPadding(DensityUtil.dip2px(20));
//        binding.smartTable2.getConfig().setTextLeftOffset(0);
        binding.smartTable2.getConfig().setContentStyle(style);
        binding.smartTable2.getConfig().setColumnTitleStyle(style);
        binding.smartTable2.getConfig().setColumnTitleBackground(new BaseBackgroundFormat(getResources().getColor(R.color.blue)));
        binding.smartTable2.getConfig().setContentCellBackgroundFormat(new BaseCellBackgroundFormat<CellInfo>() {
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
                if (cellInfo.row == selRow) {
                    // WriteLog.writeTxtToFile("TAGCELLINFO====================" + cellInfo.row + "Hh" + selRow);
                    return ContextCompat.getColor(getMyActivity(), R.color.Aqua);

                } else {
                    return TableConfig.INVALID_COLOR;
                }
            }
        });
        // tableViewLeft.getConfig().setMinTableWidth(DensityUtil.dip2px(1024));
        //tableViewLeft.setZoom(true,1.2f, 0.8f);

    }

    private void initRightData() {
        columnFMNo = new Column<String>("桶号", "barrelNo");
        columnHigh = new Column<String>("高速阈值", "highThreshold");
        columnLow = new Column<String>("中速阈值", "lowThreshold");//低速改为了中速
        columnClose = new Column<String>("低速阈值", "closeThreshold");//关闭改为了低速
        columnPrecision = new Column<String>("罐装精度", "cannedaPrecision");
        columnDDCount = new Column<Integer>("抖动次数", "ddCount");
        columnName = new Column<String>("物料名称", "materialName");
        columnOpenBig = new Column<String>("开大嘴", "openBig");
        columnCloseBig = new Column<String>("关大嘴", "closeBig");
        columnOpenSmall = new Column<String>("开小嘴", "openSmall");
        columnCloseSmall = new Column<String>("关小嘴", "closeSmall");
        columnFMNo.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
            @Override
            public void onClick(Column<String> column, String value, String s, int position) {
                selectRowFM(position);
            }
        });
        columnHigh.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
            @Override
            public void onClick(Column<String> column, String value, String s, int position) {
                selectRowFM(position);
            }
        });
        columnLow.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
            @Override
            public void onClick(Column<String> column, String value, String s, int position) {
                //Toast.makeText(getMyActivity(),value+"hh"+s+"jj"+position,Toast.LENGTH_SHORT).show();
                selectRowFM(position);
            }
        });
        columnClose.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
            @Override
            public void onClick(Column<String> column, String value, String s, int position) {
                //Toast.makeText(getMyActivity(),value+"hh"+s+"jj"+position,Toast.LENGTH_SHORT).show();
                selectRowFM(position);
            }
        });
        columnPrecision.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
            @Override
            public void onClick(Column<String> column, String value, String s, int position) {
                //Toast.makeText(getMyActivity(),value+"hh"+s+"jj"+position,Toast.LENGTH_SHORT).show();
                selectRowFM(position);
            }
        });
        columnDDCount.setOnColumnItemClickListener(new OnColumnItemClickListener<Integer>() {

            @Override
            public void onClick(Column<Integer> column, String value, Integer integer, int position) {
                selectRowFM(position);
            }
        });
        columnName.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
            @Override
            public void onClick(Column<String> column, String value, String s, int position) {
                //Toast.makeText(getMyActivity(),value+"hh"+s+"jj"+position,Toast.LENGTH_SHORT).show();
                selectRowFM(position);
            }
        });
        columnOpenBig.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
            @Override
            public void onClick(Column<String> column, String value, String s, int position) {
                //Toast.makeText(getMyActivity(),value+"hh"+s+"jj"+position,Toast.LENGTH_SHORT).show();
                selectRowFM(position);
            }
        });
        columnCloseBig.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
            @Override
            public void onClick(Column<String> column, String value, String s, int position) {
                //Toast.makeText(getMyActivity(),value+"hh"+s+"jj"+position,Toast.LENGTH_SHORT).show();
                selectRowFM(position);
            }
        });
        columnOpenSmall.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
            @Override
            public void onClick(Column<String> column, String value, String s, int position) {
                //Toast.makeText(getMyActivity(),value+"hh"+s+"jj"+position,Toast.LENGTH_SHORT).show();
                selectRowFM(position);
            }
        });
        columnCloseSmall.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
            @Override
            public void onClick(Column<String> column, String value, String s, int position) {
                //Toast.makeText(getMyActivity(),value+"hh"+s+"jj"+position,Toast.LENGTH_SHORT).show();
                selectRowFM(position);
            }
        });
        TableData<FmParamsBean> tableData = null;
        if (isHigh) {
            tableData = new TableData<FmParamsBean>("测试标题", fmAll, columnFMNo,
                    columnHigh, columnLow, columnClose, columnPrecision, columnDDCount, columnName, columnOpenBig, columnCloseBig, columnOpenSmall, columnCloseSmall);
        } else {
            tableData = new TableData<FmParamsBean>("测试标题", fmAll, columnFMNo,
                    columnHigh, columnLow, columnClose, columnPrecision, columnName, columnOpenBig, columnCloseBig, columnOpenSmall, columnCloseSmall);
        }

        binding.smartTable3.getConfig().setShowTableTitle(false);
        binding.smartTable3.setTableData(tableData);
        //binding.smartTable3.getConfig().setMinTableWidth(900);
        binding.smartTable3.getConfig().setShowYSequence(false);
        FontStyle style = new FontStyle();
        style.setTextSpSize(getMyActivity(), 22);
        style.setTextColor(getResources().getColor(R.color.black));

//        binding.smartTable3.getConfig().setSequenceHorizontalPadding(0);
        binding.smartTable3.getConfig().setHorizontalPadding(DensityUtil.dip2px(20));//
        binding.smartTable3.getConfig().setColumnTitleHorizontalPadding(DensityUtil.dip2px(20));///////
//        binding.smartTable3.getConfig().setColumnTitleVerticalPadding(DensityUtil.dip2px(5));
//        binding.smartTable3.getConfig().setVerticalPadding(DensityUtil.dip2px(5));
//        binding.smartTable3.getConfig().setTextLeftOffset(0);
        binding.smartTable3.getConfig().setContentStyle(style);
        binding.smartTable3.getConfig().setColumnTitleStyle(style);
        binding.smartTable3.getConfig().setColumnTitleBackground(new BaseBackgroundFormat(getResources().getColor(R.color.blue)));
        binding.smartTable3.getConfig().setContentCellBackgroundFormat(new BaseCellBackgroundFormat<CellInfo>() {
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
                if (cellInfo.row == selRowFm) {

                    //WriteLog.writeTxtToFile("TAGCELLINFO====================" + cellInfo.row + "Hh" + selRow);
                    return ContextCompat.getColor(getMyActivity(), R.color.Aqua);

                } else {
                    return TableConfig.INVALID_COLOR;
                }
            }
        });
        // tableViewLeft.getConfig().setMinTableWidth(DensityUtil.dip2px(1024));
        //tableViewLeft.setZoom(true,1.2f, 0.8f);

    }

    private void selectRow(int position) {
        selRow = position;
        List<DDParamsBean> ddParamsBeans = DDService.getInstance().getAllListByType(mBarrelNo);
        mDDParamsBean = ddParamsBeans.get(position);
        binding.smartTable2.refreshDrawableState();
        binding.smartTable2.invalidate();


    }

    private void selectRowFM(int position) {
        selRowFm = position;
        mFmParamsBean = fmAll.get(position);
        if (mFmParamsBean != null) {
            mBarrelNo = mFmParamsBean.getBarrelNo();
            setFMParamsText(mFmParamsBean);
        }
        binding.smartTable3.refreshDrawableState();
        binding.smartTable3.invalidate();

        List<DDParamsBean> ddParamsBeans = DDService.getInstance().getAllListByType(mBarrelNo);
        if (ddParamsBeans == null || ddParamsBeans.size() == 0) {
            ddParamsBeans = new ArrayList<>();
        }else{
            compartorSort(ddParamsBeans);
        }
        initData(ddParamsBeans);
    }

    //    private List<DDParamsBean> getDDParamsByBarrel(){
//        List<DDParamsBean> ddParamsBeans = new ArrayList<>();
//        if(ddAll!=null && ddAll.size()>0){
//            for(int i=0;i<ddAll.size();i++){
//                DDParamsBean ddParamsBean = ddAll.get(i);
//                if(ddParamsBean.getBarrelNo().equals(barrelNo)){
//                    ddParamsBeans.add(ddParamsBean);
//                }
//            }
//        }
//    }
    private void setFMParamsText(FmParamsBean fmParamsBean) {
        binding.editNo.setText(fmParamsBean.getBarrelNo());
        binding.drawableEdittextHigh.setText(fmParamsBean.getHighThreshold());

        binding.drawableEdittextLow.setText(fmParamsBean.getLowThreshold());
        binding.drawableEdittextClose.setText(fmParamsBean.getCloseThreshold());
//        binding.drawableEdittextMedi.setText(fmParamsBean.getCannedaPrecision());
        binding.drawableEdittextMedi.setTextUnit("");
        binding.editDdCount.setText(String.valueOf(fmParamsBean.getDdCount()));
        binding.editMaterialName.setText(fmParamsBean.getMaterialName());

        binding.editOpenbig.setText(fmParamsBean.getOpenBig());
        binding.editClosebig.setText(fmParamsBean.getCloseBig());
        binding.editOpensmall.setText(fmParamsBean.getOpenSmall());
        binding.editClosesmall.setText(fmParamsBean.getCloseSmall());

        String barrelNo = fmParamsBean.getBarrelNo();
        String materialName = "";
        if (pumpConfigBeans != null && pumpConfigBeans.size() > 0) {
            for (int i = 0; i < pumpConfigBeans.size(); i++) {
                PumpConfigBean pumpConfigBean = pumpConfigBeans.get(i);
                if (barrelNo.equals(pumpConfigBean.getAddress())) {
                    materialName = pumpConfigBean.getMaterials_name();
                    break;
                }
            }
        }
        fmParamsBean.setMaterialName(materialName);

        binding.editMaterialName.setText(materialName);
    }

    private void delDDDialog(DDParamsBean ddParamsBean) {
        iosDialog = new IOSDialog.Builder(
                getMyActivity())
                .setBtnColor(getResources().getColor(R.color.blue))
                .setColor(getResources().getColor(R.color.blue))
                .setMessage("确定要删除当前数据？")
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(
                                    DialogInterface dialog,
                                    int which) {
                                iosDialog.dismiss();
                                //  newDataBeans.remove(dataBean);
                                List<DDParamsBean> ddParamsBeans = DDService.getInstance().getAllListByType(mBarrelNo);
                                if (ddParamsBeans != null && ddParamsBeans.size() > 0) {
                                    ddParamsBeans.remove(selRow);//ddParamsBeans.remove(ddParamsBean); 这样写错误
                                    Log.i("TAG",ddParamsBeans.size()+"");
                                    DDService.getInstance().delete(ddParamsBean);
                                    //fmService.saveOrUpdate(mFmParamsBean);
                                    // handleDelData();
                                    selRow = -1;
                                    initData(ddParamsBeans);
                                }

                                //updateDataLeft();

                            }
                        })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(
                                    DialogInterface dialog,
                                    int which) {
                                iosDialog.dismiss();
                                selRow = -1;
                            }
                        })

                .create();
        iosDialog.show();
    }
//    private void handleDelData() {
//        for (Iterator iterator = tempSaveDatabeans.iterator(); iterator.hasNext(); ) {
//            TempSaveDataBean tempSaveDataBean = (TempSaveDataBean) iterator.next();
//            double totalValue = 0d;
//            boolean isHave = false;
//            int count = 0;
//            for (int i = 0; i < testCorrctBeans.size(); i++) {
//                TestCorrctBean testCorrctBean = testCorrctBeans.get(i);
//                if (testCorrctBean.getTargetValue() == tempSaveDataBean.getTargetValue()) {
//                    count++;
//                    isHave = true;
//                    totalValue += testCorrctBean.getTestValue();
//                    double average = totalValue / count;
//                    tempSaveDataBean.setTestAverage(average);
//                }
//            }
//            if (!isHave) {
//                iterator.remove();
//            }
//        }
//    }


    private void setOnClick() {
        BtnClickListener btnClickListener = new BtnClickListener();
        binding.ivDdAdd.setOnClickListener(btnClickListener);
        binding.ivDdEdit.setOnClickListener(btnClickListener);
        binding.ivDdDel.setOnClickListener(btnClickListener);
        binding.btnSave.setOnClickListener(btnClickListener);
    }

    AddDDParamsDialog addDDParamsDialog;

    class BtnClickListener extends OnMultiClickListener {

        @Override
        public void onMultiClick(View v) {
            int id = v.getId();
            if (id == R.id.iv_dd_add) {
                if (mBarrelNo == null && mBarrelNo.length() == 0) {
//                    ToastUtils.showShort("请先设置桶号");
//                    ToastUtils.setMsgTextSize(18);
                    MyToastUtil.showToastSnackBar(decorView, "请设置桶号", Snackbar.LENGTH_LONG);
                    return;
                }
                ddType = 1;
                addDDParamsDialog = new AddDDParamsDialog(getMyActivity(), BasePopupWindow.WRAP_CONTENT, BasePopupWindow.WRAP_CONTENT);
                addDDParamsDialog.setOutSideDismiss(true);
                addDDParamsDialog.setPopupGravity(Gravity.CENTER);
                //addDDParamsDialog.setPopupGravity(Gravity.FILL_HORIZONTAL);
                addDDParamsDialog.setAdjustInputMethod(true, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                AppApplication.getInstance().init(decorView);
                addDDParamsDialog.showPopupWindow();
            } else if (id == R.id.iv_dd_edit) {
                if (mBarrelNo == null && mBarrelNo.length() == 0) {
//                    ToastUtils.showShort("请先设置桶号");
//                    ToastUtils.setMsgTextSize(22);
//                    ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//                    ToastUtils.setBgResource(R.color.gray);
                    MyToastUtil.showToastSnackBar(decorView, "请设置桶号", Snackbar.LENGTH_LONG);
                    return;
                }
                if (selRow == -1) {
//                    ToastUtils.showShort("请先选中点动行数");
//                    ToastUtils.setMsgTextSize(22);
//                    ToastUtils.setBgResource(R.color.gray);
//                    ToastUtils.setGravity(Gravity.CENTER, 0, 0);
                    MyToastUtil.showToastSnackBar(decorView, "请选中点动行数", Snackbar.LENGTH_LONG);
                    return;
                }
                if (mDDParamsBean != null) {
                    ddType = 2;
                    addDDParamsDialog = new AddDDParamsDialog(getMyActivity(), BasePopupWindow.WRAP_CONTENT, BasePopupWindow.WRAP_CONTENT);
                    addDDParamsDialog.setOutSideDismiss(true);
                    addDDParamsDialog.setPopupGravity(Gravity.CENTER);
                    //addDDParamsDialog.setPopupGravity(Gravity.FILL_HORIZONTAL);
                    addDDParamsDialog.setAdjustInputMethod(true, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    AppApplication.getInstance().init(decorView);
                    addDDParamsDialog.showPopupWindow();
                }

            } else if (id == R.id.iv_dd_del) {
                List<DDParamsBean> ddParamsBeans = DDService.getInstance().getAllListByType(mBarrelNo);
                if (ddParamsBeans != null && ddParamsBeans.size() > 0) {
                    if (selRow >= 0) {
                        DDParamsBean ddParamsBean = ddParamsBeans.get(selRow);
                        delDDDialog(ddParamsBean);
                    }

                }

            } else if (id == R.id.btn_save) {
                String tong = binding.editNo.getText().toString();
                String high = binding.drawableEdittextHigh.getText();
                String low = binding.drawableEdittextLow.getText();
                String close = binding.drawableEdittextClose.getText();
                String precision = binding.drawableEdittextMedi.getText();
                String ddCount = binding.editDdCount.getText().toString().trim();
                String materialName = binding.editMaterialName.getText().toString().trim();
                binding.drawableEdittextMedi.getEditText().setHint("不能大于罐装目标量");
                String openBig = binding.editOpenbig.getText().toString().trim();
                String closeBig = binding.editClosebig.getText().toString().trim();
                String openSmall = binding.editOpensmall.getText().toString().trim();
                String closeSmall = binding.editClosesmall.getText().toString().trim();
                if (high == null || high.length() == 0) {
//                    ToastUtils.showShort("高速阈值不能为空");
//                    ToastUtils.setMsgTextSize(18);
                    MyToastUtil.showToastSnackBar(decorView, "高速阈值不能为空", Snackbar.LENGTH_LONG);
                    return;
                }
                if (endWithDian(high)) {
                    MyToastUtil.showToastSnackBar(decorView, "高速阈值格式不正确", Snackbar.LENGTH_LONG);
                    return;
                }
                if (low == null || low.length() == 0) {
//                    ToastUtils.showShort("中速阈值不能为空");
//                    ToastUtils.setMsgTextSize(18);
                    MyToastUtil.showToastSnackBar(decorView, "中速阈值不能为空", Snackbar.LENGTH_LONG);
                    return;
                }
                if (endWithDian(low)) {
                    MyToastUtil.showToastSnackBar(decorView, "中速阈值格式不正确", Snackbar.LENGTH_LONG);
                    return;
                }
                if (close == null || close.length() == 0) {
//                    ToastUtils.showShort("低速阈值不能为空");
//                    ToastUtils.setMsgTextSize(18);
                    MyToastUtil.showToastSnackBar(decorView, "低速阈值不能为空", Snackbar.LENGTH_LONG);
                    return;
                }
                if (endWithDian(close)) {
                    MyToastUtil.showToastSnackBar(decorView, "低速阈值格式不正确", Snackbar.LENGTH_LONG);
                    return;
                }
                if (Double.parseDouble(high) <= Double.parseDouble(low)) {
                    MyToastUtil.showToastSnackBar(decorView, "高速阈值必须大于中速阈值", Snackbar.LENGTH_LONG);
                    return;
                }
                if (Double.parseDouble(low) <= Double.parseDouble(close)) {
                    MyToastUtil.showToastSnackBar(decorView, "中速阈值必须大于低速阈值", Snackbar.LENGTH_LONG);
                    return;
                }
                if (precision == null || precision.length() == 0) {
//                    ToastUtils.showShort("罐装精度不能为空");
//                    ToastUtils.setMsgTextSize(18);
                    MyToastUtil.showToastSnackBar(decorView, "罐装精度不能为空", Snackbar.LENGTH_LONG);
                    return;
                }
                if (endWithDian(precision)) {
                    MyToastUtil.showToastSnackBar(decorView, "罐装精度格式不正确", Snackbar.LENGTH_LONG);
                    return;
                }
                if (tong == null || tong.length() == 0) {
//                    ToastUtils.showShort("桶号不能为空");
//                    ToastUtils.setMsgTextSize(18);
                    MyToastUtil.showToastSnackBar(decorView, "桶号不能为空", Snackbar.LENGTH_LONG);
                    return;
                }
                if (ddCount == null || ddCount.length() == 0) {
//                    ToastUtils.showShort("抖动次数不能为空");
//                    ToastUtils.setMsgTextSize(18);
                    MyToastUtil.showToastSnackBar(decorView, "抖动次数不能为空", Snackbar.LENGTH_LONG);
                    return;
                }
                if (materialName == null || materialName.length() == 0) {
//                    ToastUtils.showShort("抖动次数不能为空");
//                    ToastUtils.setMsgTextSize(18);
                    MyToastUtil.showToastSnackBar(decorView, "物料名称不能为空", Snackbar.LENGTH_LONG);
                    return;
                }
                if (openBig == null || openBig.length() == 0) {
//                    ToastUtils.showShort("抖动次数不能为空");
//                    ToastUtils.setMsgTextSize(18);
                    MyToastUtil.showToastSnackBar(decorView, "开大嘴不能为空", Snackbar.LENGTH_LONG);
                    return;
                }
                if (closeBig == null || closeBig.length() == 0) {
//                    ToastUtils.showShort("抖动次数不能为空");
//                    ToastUtils.setMsgTextSize(18);
                    MyToastUtil.showToastSnackBar(decorView, "关大嘴不能为空", Snackbar.LENGTH_LONG);
                    return;
                }
                if (openSmall == null || openSmall.length() == 0) {
//                    ToastUtils.showShort("抖动次数不能为空");
//                    ToastUtils.setMsgTextSize(18);
                    MyToastUtil.showToastSnackBar(decorView, "开小嘴不能为空", Snackbar.LENGTH_LONG);
                    return;
                }
                if (closeSmall == null || closeSmall.length() == 0) {
//                    ToastUtils.showShort("抖动次数不能为空");
//                    ToastUtils.setMsgTextSize(18);
                    MyToastUtil.showToastSnackBar(decorView, "关小嘴不能为空", Snackbar.LENGTH_LONG);
                    return;
                }
                mFmParamsBean.setBarrelNo(tong);
                mFmParamsBean.setHighThreshold(high);
                mFmParamsBean.setLowThreshold(low);
                mFmParamsBean.setCloseThreshold(close);
                mFmParamsBean.setCannedaPrecision(precision);
                mFmParamsBean.setDdCount(Integer.parseInt(ddCount));
                mFmParamsBean.setMaterialName(materialName);
                mFmParamsBean.setOpenBig(openBig);
                mFmParamsBean.setCloseBig(closeBig);
                mFmParamsBean.setOpenSmall(openSmall);
                mFmParamsBean.setCloseSmall(closeSmall);
                fmService.saveOrUpdate(mFmParamsBean);
                updateDataRight();
//                ToastUtils.showShort("保存成功");
//                ToastUtils.setMsgTextSize(18);
                MyToastUtil.showToastSnackBar(decorView, "保存成功", Snackbar.LENGTH_LONG);


            }
        }
    }

    private boolean endWithDian(String str) {
        if (str.endsWith(".")) {
            return true;
        }
        return false;
    }

    private DrawableEditText editStep;
    private LinearLayout linearShakeStep;
    class AddDDParamsDialog extends BasePopupWindow implements View.OnClickListener, TextView.OnEditorActionListener {
        private Button btnSure, btnCancel;
        private EditText editDDNo;
        private DrawableEditText editTarget, editInterval, editDDBeforeStep;
        private ImageView ivClose;
        private View ddView;

        public AddDDParamsDialog(Context context, int width, int height) {
            super(context, width, height);
        }

        public AddDDParamsDialog(Context context) {
            super(context);
        }

        @Override
        public View onCreateContentView() {
            ddView = createPopupById(R.layout.pop_add_ddparams);
            btnSure = ddView.findViewById(R.id.btn_sure);
            // btnCancel = view.findViewById(R.id.btn_cancel);
            editDDNo = ddView.findViewById(R.id.edit_dd_no);
            editDDNo.setText(mBarrelNo);
            editDDNo.setClickable(false);
            editDDNo.setFocusable(false);
            editDDNo.setFocusableInTouchMode(false);
            editTarget = ddView.findViewById(R.id.edit_dd_target);
            editTarget.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
            setChangedTextDD(editTarget.getEditText());
            editStep = ddView.findViewById(R.id.edit_dd_step);
            editStep.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
            setChangedTextDD(editStep.getEditText());
            linearShakeStep = ddView.findViewById(R.id.linear_shakestep);
            if (isHigh) {
                editStep.setVisibility(View.VISIBLE);
                linearShakeStep.setVisibility(View.VISIBLE);
            } else {
                editStep.setVisibility(View.GONE);
                linearShakeStep.setVisibility(View.GONE);
            }
            editInterval = ddView.findViewById(R.id.edit_dd_interval);
            editInterval.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
            setChangedTextDD(editInterval.getEditText());
            editDDBeforeStep = ddView.findViewById(R.id.edit_dd_before_step);
            editDDBeforeStep.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
            setChangedTextDD(editDDBeforeStep.getEditText());
            ivClose = ddView.findViewById(R.id.iv_close);
            if (ddType == 1) {
                editTarget.setText("");
            } else {
                editTarget.setText(mDDParamsBean.getDdTargetDos());
                editInterval.setText(mDDParamsBean.getDdCycle());
                editStep.setText(mDDParamsBean.getDdStep());
                editDDBeforeStep.setText(mDDParamsBean.getDdBeforeStep());
            }

            editTarget.setTextUnit("mg");
            editInterval.setTextUnit("ms");
            editStep.setTextUnit("");
            editDDBeforeStep.setTextUnit("");
            btnSure.setOnClickListener(this);
            // btnCancel.setOnClickListener(this);
            ivClose.setOnClickListener(this);
            return ddView;
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.btn_sure) {
                String ddNo = editDDNo.getText().toString().trim();
                String target = editTarget.getText().toString().trim();
                String step = "";
                if (isHigh) {
                    step = editStep.getText().toString().trim();
                }
                String cycle = editInterval.getText().toString().trim();
                String ddBeforeStep = editDDBeforeStep.getText().toString().trim();
                if (target == null || target.length() == 0) {
//                    ToastUtils.showShort("请填写点动目标量");
//                    ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//                    ToastUtils.setBgResource(R.color.gray);
//                    ToastUtils.setMsgTextSize(18);
                    MyToastUtil.showToastSnackBar(ddView, "请填写点动目标量", Snackbar.LENGTH_SHORT);
                    return;
                }

                if (isHigh) {
                    if (step == null || step.length() == 0) {
//                    ToastUtils.showShort("请填写振动步数");
//                    ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//                    ToastUtils.setBgResource(R.color.gray);
//                    ToastUtils.setMsgTextSize(18);
                        MyToastUtil.showToastSnackBar(ddView, "请填写振动步数", Snackbar.LENGTH_SHORT);
                        return;
                    }
                }

                if (cycle == null || cycle.length() == 0) {
//                    ToastUtils.showShort("请填写点动周期");
//                    ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//                    ToastUtils.setBgResource(R.color.gray);
//                    ToastUtils.setMsgTextSize(18);
                    MyToastUtil.showToastSnackBar(ddView, "请填写点动周期", Snackbar.LENGTH_SHORT);
                    return;
                }
                if (ddBeforeStep == null || ddBeforeStep.length() == 0) {
//                    ToastUtils.showShort("请填写点动前步数");
//                    ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//                    ToastUtils.setBgResource(R.color.gray);
//                    ToastUtils.setMsgTextSize(18);
                    MyToastUtil.showToastSnackBar(ddView, "请填写点动前步数", Snackbar.LENGTH_SHORT);
                    return;
                }

                List<DDParamsBean> ddParamsBeans = DDService.getInstance().getAllListByType(mBarrelNo);
                if (ddParamsBeans == null || ddParamsBeans.size() == 0) {
                    ddParamsBeans = new ArrayList<>();
                }else{
                    compartorSort(ddParamsBeans);
                }

                if (ddType == 1) {
                    if (ddParamsBeans.size() == 5) {
//                        ToastUtils.showShort("点动参数最多新建5个");
//                        ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//                        ToastUtils.setBgResource(R.color.gray);
//                        ToastUtils.setMsgTextSize(18);
                        MyToastUtil.showToastSnackBar(ddView, "点动参数最多五个", Snackbar.LENGTH_SHORT);
                        return;
                    }
                }

                //if (ddType == 1) {
                boolean isExist = false;
                if (ddParamsBeans != null && ddParamsBeans.size() > 0) {
                    for (int i = 0; i < ddParamsBeans.size(); i++) {
                        DDParamsBean ddParamsBean = ddParamsBeans.get(i);
                        if (ddType == 2) {
                            if (Double.parseDouble(mDDParamsBean.getDdTargetDos()) != Double.parseDouble(ddParamsBean.getDdTargetDos())) {
                                if (Double.parseDouble(target) == (Double.parseDouble(ddParamsBean.getDdTargetDos()))) {
                                    isExist = true;
                                }
                            }
                        } else if (ddType == 1) {
                            if (Double.parseDouble(target) == (Double.parseDouble(ddParamsBean.getDdTargetDos()))) {
                                isExist = true;
                            }
                        }
                    }
                }
                if (isExist) {
//                    ToastUtils.showShort("点动目标量已存在");
//                    ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//                    ToastUtils.setBgResource(R.color.gray);
//                    ToastUtils.setMsgTextSize(18);
                    MyToastUtil.showToastSnackBar(ddView, "点动目标量已存在", Snackbar.LENGTH_SHORT);
                    return;
                }
                //}
                if (ddType == 1) {
                    mDDParamsBean = new DDParamsBean();
                }

                mDDParamsBean.setBarrelNo(ddNo);
                mDDParamsBean.setDdTargetDos(target);
                mDDParamsBean.setDdStep(step);
                mDDParamsBean.setDdCycle(cycle);
                mDDParamsBean.setDdBeforeStep(ddBeforeStep);
                if(isHigh){
                    mDDParamsBean.setBucket_type("1");
                }else{
                    mDDParamsBean.setBucket_type("2");
                }

                int index = -1;
                for (int i = 0; i < ddParamsBeans.size(); i++) {
                    DDParamsBean ddParamsBean1 = ddParamsBeans.get(i);
                    String ddTargetDos = ddParamsBean1.getDdTargetDos();
                    if (Double.parseDouble(target) <= Double.parseDouble(ddTargetDos)) {
                        index = i;
                        break;
                    }
                }
                if (ddType == 1) {//添加点动
                    if (index == -1) {
                        ddParamsBeans.add(mDDParamsBean);
                    } else {
                        ddParamsBeans.add(index, mDDParamsBean);
                    }
                    DDService.getInstance().save(mDDParamsBean);
                } else if (ddType == 2) {//修改点动

                    ddParamsBeans.set(selRow,mDDParamsBean);
                    compartorSort(ddParamsBeans);
                    DDService.getInstance().update(mDDParamsBean);
                }
                //fmService.saveOrUpdate(mFmParamsBean);
                initData(ddParamsBeans);
                //updateDataLeft();
                dismiss();
            } else if (id == R.id.iv_close) {
                dismiss();
            }
        }



        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            switch (actionId) {
                case EditorInfo.IME_ACTION_DONE:
                    //next();
                    break;
            }
            return false;
        }

        @Override
        public void dismiss() {
            super.dismiss();
        }


    }
    private void compartorSort(List<DDParamsBean> ddParamsBeans) {
        Collections.sort(ddParamsBeans, new Comparator<DDParamsBean>() {
            @Override
            public int compare(DDParamsBean o1, DDParamsBean o2) {
                int i = Integer.parseInt(o1.getDdTargetDos());
                int i1 = Integer.parseInt(o2.getDdTargetDos());
                return i - i1;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
