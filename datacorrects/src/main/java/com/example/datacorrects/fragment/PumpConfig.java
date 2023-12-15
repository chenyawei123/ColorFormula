package com.example.datacorrects.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
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
import com.cyw.mylibrary.base.BaseFragment;
import com.cyw.mylibrary.bean.FmParamsBean;
import com.cyw.mylibrary.bean.PumpConfigBean;
import com.cyw.mylibrary.bean.SaveBean;
import com.cyw.mylibrary.services.CorrectDataService;
import com.cyw.mylibrary.services.FMServiceNew;
import com.cyw.mylibrary.services.PumpConfigBeanService;
import com.cyw.mylibrary.util.MyToastUtil;
import com.example.datacorrects.R;
import com.example.datacorrects.adapter.PumpConfigAdapter;
import com.example.datacorrects.adapter.PumpConfigLongAdapter;
import com.example.datacorrects.bean.MainLongBean;
import com.example.datacorrects.bean.TableCell;
import com.example.datacorrects.commondialog.PumpConfigDialog;
import com.example.datacorrects.view.PumpSetListener;
import com.google.android.material.snackbar.Snackbar;

import org.xutils.common.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import popup.BasePopup.BasePopupWindow;


/**
 *
 */

public class PumpConfig extends BaseFragment implements View.OnClickListener {
    private View decorView = null;
    private SmartTable<PumpConfigBean> tableView;
    private PumpConfigAdapter madapter;
    private PumpConfigLongAdapter pumpConfigLongAdapter;
    private List<TableCell> cells = null;
    private final View eView = null;
    private LongPopWindow longPopWindow;
    private LayoutInflater inflater = null;
    private final List<MainLongBean> mainLongBeans = new ArrayList<MainLongBean>();
    private View longView = null;
    private View feedView = null;
    private View configPopView = null;
    private RecyclerView ryLong;
    private final List<String> mainFeedBeans = new ArrayList<String>();
    private final int LEFT_COLUMN_COUNT = 10;////////////////////////////master_v1 新增
    private List<PumpConfigBean> pumpConfigBeans = new ArrayList<PumpConfigBean>();
    private PumpConfigBean mPumpConfigBean;
    private int addType = 0;
    private PumpConfigBeanService correctConfigService;
    private CorrectDataService correctDataService;
    private PumpConfigDialog pumpConfigDialog;
    private String configPosition, materialName, highSpeed, midSpeed, lowSpeed, ddSpeed, startSpeed, startStep, alarmValue, maxCapacity,multiple;
    private boolean isTop = false;
    private boolean isBottom = false;
    private int mRowIndex = -1;
    private boolean isOutsideLong = false;
    private RecyclerMarginClickHelper recyclerMarginClickHelper;


    private EditText editPosition, editName, editHighSpeed, editMidSpped, editLowSpeed, editDDSpeed, editStartSpeed, editStartStep, editCapacity, editPolice,editMulti;
    private TextView tvAerationTimeUnit;
    private Button btnAdd;
    private Button btnCancel;
    private TextView tvPumpTitle;
    private List<SaveBean> saveBeans = new ArrayList<>();
    private int oldConfigAddress;
    private ImageView ivClose;
    /////////////添加config弹窗
    private AddConfigPop addConfigPop;
    /////////////////////////////////默认阀门参数设置
    private FMServiceNew fmService;
    private List<FmParamsBean> fmParamsBeans = new ArrayList<>();
    private Column<String> columnBarrelNo;
    private Column<String> columnMaterialName;
    private Column<String> columnHighSpeed;
    private Column<String> columnMidSpeed;
    private Column<String> columnLowSpeed;
    private Column<String> columnDDSpeed;
    private Column<String> columnStartSpped;
    private Column<String> columnStartStep;
    private Column<String> columnPolice;
    private Column<String> columnCapacity;
    private Column<String> columnMulti;//稀释倍数

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        decorView = inflater.inflate(R.layout.pump_config, null);
        //AppApplication.getInstance().hideDialogNavigationBar(decorView);
        initView();
        getList();
        return decorView;
    }

    private void initView() {
        tableView = decorView.findViewById(R.id.tb);
    }

    private void getList() {
        correctConfigService = PumpConfigBeanService.getInstance();
        if (pumpConfigBeans != null && pumpConfigBeans.size() > 0) {
            pumpConfigBeans.clear();
        }
        pumpConfigBeans = correctConfigService.getAllListByType();
        correctDataService = new CorrectDataService(getMyActivity());
        if (saveBeans != null && saveBeans.size() > 0) {
            saveBeans.clear();
        }
        saveBeans = correctDataService.getAll();
        //initData();
        initRightData();
        fmService = FMServiceNew.getInstance();
        if (fmParamsBeans != null && fmParamsBeans.size() > 0) {
            fmParamsBeans.clear();
        }
        fmParamsBeans = fmService.getAllListByType();
    }

    private void selectRowFM(int position) {
        mRowIndex = position;
        if (mRowIndex >= 0) {
            mPumpConfigBean = pumpConfigBeans.get(mRowIndex);
            oldConfigAddress = Integer.parseInt(mPumpConfigBean.getAddress());
            isTop = false;
            isBottom = false;
            if (mRowIndex == 0) {
                isTop = true;
            } else if (mRowIndex == pumpConfigBeans.size() - 1) {
                isBottom = true;
            }
            isOutsideLong = false;
//            madapter.selRow(mRowIndex);
////                        int startPo = -1;
////                        if(tableCell.getRow() == 1){
////                            startPo = 0;
////                        }else{
////                            startPo = (tableCell.getRow()-1)*10+1;
////                        }
//            madapter.notifyItemRangeChanged(0, cells.size());
            tableView.refreshDrawableState();
            tableView.invalidate();
            showLongDialog();
        }


//        List<DDParamsBean> ddParamsBeans = fmParamsBean.getDdParamsBeans();
//        if (ddParamsBeans == null || ddParamsBeans.size() == 0) {
//            ddParamsBeans = new ArrayList<>();
//        }
//        initData(ddParamsBeans);
    }

    public void refData() {
//        selRow = -1;
//        if(madapter!=null){
//            madapter.selRow(selRow);
//            madapter.notifyItemRangeChanged(0,cells.size());
//        }
    }

    private void initRightData() {
        columnBarrelNo = new Column<String>("桶号", "address");
        columnMaterialName = new Column<String>("料名称", "materials_name");
        columnHighSpeed = new Column<String>("高速速度", "highSpeed");//低速改为了中速
        columnMidSpeed = new Column<String>("中速速度", "midSpeed");//关闭改为了低速
        columnLowSpeed = new Column<String>("低速速度", "lowSpeed");
        columnDDSpeed = new Column<String>("点动速度", "ddSpeed");
        columnStartSpped = new Column<String>("启动速度", "startSpeed");
        columnStartStep = new Column<String>("启动步数", "startStep");
        columnPolice = new Column<String>("报警量", "alarm_value");
        columnCapacity = new Column<String>("桶容量", "max_capacity");
        columnMulti = new Column<String>("稀释倍数", "multiple");
        columnBarrelNo.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
            @Override
            public void onClick(Column<String> column, String value, String integer, int position) {
                selectRowFM(position);
            }
        });
        columnMaterialName.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
            @Override
            public void onClick(Column<String> column, String value, String s, int position) {
                selectRowFM(position);
            }
        });
        columnHighSpeed.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
            @Override
            public void onClick(Column<String> column, String value, String s, int position) {
                selectRowFM(position);
            }
        });
        columnMidSpeed.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
            @Override
            public void onClick(Column<String> column, String value, String s, int position) {
                selectRowFM(position);
            }
        });
        columnLowSpeed.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
            @Override
            public void onClick(Column<String> column, String value, String s, int position) {
                selectRowFM(position);
            }
        });
        columnDDSpeed.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {

            @Override
            public void onClick(Column<String> column, String value, String s, int position) {
                selectRowFM(position);
            }
        });
        columnStartSpped.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {

            @Override
            public void onClick(Column<String> column, String value, String s, int position) {
                selectRowFM(position);
            }
        });
        columnStartStep.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {

            @Override
            public void onClick(Column<String> column, String value, String s, int position) {
                selectRowFM(position);
            }
        });
        columnPolice.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {

            @Override
            public void onClick(Column<String> column, String value, String integer, int position) {
                selectRowFM(position);
            }
        });
        columnCapacity.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {

            @Override
            public void onClick(Column<String> column, String value, String integer, int position) {
                selectRowFM(position);
            }
        });
        columnMulti.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {

            @Override
            public void onClick(Column<String> column, String value, String integer, int position) {
                selectRowFM(position);
            }
        });
        final TableData<PumpConfigBean> tableData = new TableData<PumpConfigBean>("测试标题", pumpConfigBeans, columnBarrelNo,
                columnMaterialName, columnHighSpeed, columnMidSpeed, columnLowSpeed, columnDDSpeed, columnStartSpped, columnStartStep, columnPolice, columnCapacity, columnMulti);
        tableView.getConfig().setShowTableTitle(false);
        tableView.setTableData(tableData);
//        tableView.post(new Runnable() {
//            @Override
//            public void run() {
//                Log.i("TAGMMM",tableView.getMeasuredWidth()+"hfhf"+tableView.getWidth());//1836
//            }
//        });
        //int value = DensityUtil.px2dip(1836);
       // tableView.getConfig().setMinTableWidth(1311);//1311 等价于屏幕宽度     此时该值如果小于 设置每个的dp值之和 就不生效
        tableView.getConfig().setShowYSequence(false);
        FontStyle style = new FontStyle();
        style.setTextSpSize(getMyActivity(), 22);
        style.setTextColor(getResources().getColor(R.color.black));

//        binding.smartTable3.getConfig().setSequenceHorizontalPadding(0);
        tableView.getConfig().setHorizontalPadding(31);//
        tableView.getConfig().setColumnTitleHorizontalPadding(31);///////
//        binding.smartTable3.getConfig().setColumnTitleVerticalPadding(DensityUtil.dip2px(5));
//        binding.smartTable3.getConfig().setVerticalPadding(DensityUtil.dip2px(5));
//        binding.smartTable3.getConfig().setTextLeftOffset(0);
        tableView.getConfig().setContentStyle(style);
        tableView.getConfig().setColumnTitleStyle(style);
        tableView.getConfig().setColumnTitleBackground(new BaseBackgroundFormat(getResources().getColor(R.color.blue)));
        tableView.getConfig().setContentCellBackgroundFormat(new BaseCellBackgroundFormat<CellInfo>() {
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
                if (cellInfo.row == mRowIndex) {
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

    public class RecyclerMarginClickHelper {
        public void setOnMarginClickListener(final RecyclerView recyclerView, final View.OnClickListener onClickListener) {
            if (recyclerView == null || onClickListener == null) {
                return;
            }

            final GestureDetector gestureDetector = new GestureDetector(recyclerView.getContext(), new GestureDetector.OnGestureListener() {
                @Override
                public boolean onDown(MotionEvent e) {
                    return false;
                }

                @Override
                public void onShowPress(MotionEvent e) {
                    Log.i("TAGSHOWPRESS", "KKKKKKK");
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    if (onClickListener != null) {
                        onClickListener.onClick(recyclerView);
                    }
                    return false;
                }

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                    return false;
                }

                @Override
                public void onLongPress(MotionEvent e) {
//                    isOutsideLong = true;
//                    showLongDialog(tableView);


                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    return false;
                }
            });

            recyclerView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    //发现只有点击了空白处，v是自身recyclerView
                    if (view instanceof RecyclerView) {
                        return gestureDetector.onTouchEvent(motionEvent);
                    }
                    return false;
                }
            });
        }
    }

    /**
     * 长按弹出对话框
     */
    @SuppressLint("ResourceAsColor")
    private void showLongDialog() {
        inflater = LayoutInflater.from(getMyActivity());
        longPopWindow = new LongPopWindow(getMyActivity(), BasePopupWindow.WRAP_CONTENT, BasePopupWindow.WRAP_CONTENT);//高度必须设置wrap，否则不能覆盖底部导航栏
        longPopWindow.setOutSideDismiss(true);
        longPopWindow.setClipToScreen(true);//设为false导致不能滑动，且无法看到下半部分
        longPopWindow.setAutoLocatePopup(true);
        int width = DensityUtil.getScreenWidth();
        int height = DensityUtil.getScreenHeight();
        //AppApplication.getInstance().init(view);
        // if (isOutsideLong) {
        longPopWindow.showPopupWindow(width / 2, height / 2);
//        } else {
//            longPopWindow.showPopupWindow(view);
//        }
    }

    class LongPopWindow extends BasePopupWindow implements View.OnClickListener {
        public LongPopWindow(Context context, int width, int height) {
            super(context, width, height);
        }

        @Override
        public View onCreateContentView() {
//            longView = createPopupById(R.layout.pop_long_dialog);
            LayoutInflater inflater = (LayoutInflater) getMyActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            longView = inflater.inflate(R.layout.pop_long_dialog, null);
            ryLong = longView.findViewById(R.id.recyclerview);
            getListLong();
            bindLongAdapter();
            return longView;
        }

        private void getListLong() {
            if (mainLongBeans != null && mainLongBeans.size() > 0) {
                mainLongBeans.clear();
            }
            MainLongBean mainLongBean = new MainLongBean();
            mainLongBean.setStep("添加");
            mainLongBeans.add(mainLongBean);
            if (!isOutsideLong) {
                MainLongBean mainLongBean2 = new MainLongBean();
                mainLongBean2.setStep("修改");
                mainLongBeans.add(mainLongBean2);
//                if (pumpConfigBeans.size() > 1) {
//                    if (!isTop) {
//                        MainLongBean mainLongBean3 = new MainLongBean();
//                        mainLongBean3.setStep("上移");
//                        mainLongBeans.add(mainLongBean3);
//                    }
//                    if (!isBottom) {
//                        MainLongBean mainLongBean4 = new MainLongBean();
//                        mainLongBean4.setStep("下移");
//                        mainLongBeans.add(mainLongBean4);
//                    }
//                }


                MainLongBean mainLongBean5 = new MainLongBean();
                mainLongBean5.setStep("删除");
                mainLongBeans.add(mainLongBean5);
            }
        }

        private void bindLongAdapter() {
            if (mainLongBeans != null && mainLongBeans.size() > 0) {
                pumpConfigLongAdapter = new PumpConfigLongAdapter(getMyActivity(),
                        mainLongBeans);
                ryLong.setAdapter(pumpConfigLongAdapter);
                ryLong.setLayoutManager(new LinearLayoutManager(getMyActivity()));
                ryLong.setItemAnimator(new DefaultItemAnimator());
//                ryLong.addItemDecoration(new DividerItemDecoration(
//                        context, DividerItemDecoration.VERTICAL_LIST));

                pumpConfigLongAdapter.setOnItemClickListener(new PumpConfigLongAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (longPopWindow != null) {
                            longPopWindow.dismiss();
                        }
                        if (mainFeedBeans != null && mainFeedBeans.size() > 0) {
                            mainFeedBeans.clear();
                        }
                        if (isOutsideLong) {
                            addType = 1;
                            addConfigPopMethod();//添加泵配置
                        } else {
//                            if (isTop) {
//                                if (position == 0) {
//                                    addType = 1;
//                                    addConfigPopMethod();//添加泵配置
//                                } else if (position == 1) {
//                                    addType = 2;
//                                    addConfigPopMethod();
//                                } else if (position == 2) {
//                                    if (pumpConfigBeans.size() == 1) {
//                                        delConfig();//删除泵配置
//                                    } else {
//                                        bottomChange();
//                                    }
//
//                                } else if (position == 3) {
//                                    delConfig();//删除泵配置
//                                }
//                            } else if (isBottom) {
//                                if (position == 0) {
//                                    addType = 1;
//                                    addConfigPopMethod();//添加泵配置
//                                } else if (position == 1) {
//                                    addType = 2;
//                                    addConfigPopMethod();//添加泵配置
//                                } else if (position == 2) {
//                                    topChange();
//                                } else if (position == 3) {
//                                    delConfig();//删除泵配置
//                                }
//                            } else {
                            if (position == 0) {
                                addType = 1;
                                addConfigPopMethod();//添加泵配置
                            } else if (position == 1) {
                                addType = 2;
                                addConfigPopMethod();//修改泵配置
                            }
//                                else if (position == 2) {
//                                    topChange();
//                                } else if (position == 3) {
//                                    bottomChange();
//                                }
                            else if (position == 2) {
                                delConfig();//删除泵配置
                            }
                            //}
                        }

                    }
                });
            }
        }

        private void topChange() {
//            PumpConfigBean dataBean = pumpConfigBeans.get(mRowIndex);
////            pumpConfigBeans.set(rowIndex, pumpConfigBeans.get(rowIndex - 1));
////            pumpConfigBeans.set(rowIndex - 1, dataBean);
////            PumpConfigBean pumpConfigBean = pumpConfigBeans.get(rowIndex);
////            PumpConfigBean pumpConfigBean1 = pumpConfigBeans.get(rowIndex - 1);
////            int temp = 0;
////            int temp2 = 0;
////            temp = pumpConfigBean1.getId();
////            temp2 = pumpConfigBean.getId();
////            pumpConfigBean.setId(temp);
////            pumpConfigBean1.setId(temp2);
////
////            correctConfigService.update(pumpConfigBean1);
////            correctConfigService.update(pumpConfigBean);
//            correctConfigService.topChange(mRowIndex, dataBean);
        }

        private void bottomChange() {
//            PumpConfigBean dataBean = pumpConfigBeans.get(mRowIndex);
//
////            pumpConfigBeans.set(rowIndex, pumpConfigBeans.get(rowIndex + 1));
////            pumpConfigBeans.set(rowIndex + 1, dataBean);
////            PumpConfigBean pumpConfigBean = pumpConfigBeans.get(rowIndex);
////            PumpConfigBean pumpConfigBean1 = pumpConfigBeans.get(rowIndex + 1);
////            int temp = 0;
////            int temp2 = 0;
////            temp = pumpConfigBean1.getId();
////            temp2 = pumpConfigBean.getId();
////            pumpConfigBean.setId(temp);
////            pumpConfigBean1.setId(temp2);
////
////            correctConfigService.update(pumpConfigBean1);
////            correctConfigService.update(pumpConfigBean);
//            //initData();
//            correctConfigService.bottomChange(mRowIndex, dataBean);
        }

        @Override
        public void dismiss() {
            super.dismiss();
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {

            }
        }
    }

    private void setEditHint(EditText editText, String content) {
        //新建一个可以添加属性的文本对象
        SpannableString ss = new SpannableString(content);
        //新建一个属性对象，设置字体的大小
        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(14, true);
        //附加属性到文本
        ss.setSpan(absoluteSizeSpan, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        editText.setHint(new SpannableString(ss));
    }


    private void delConfig() {
        String address = String.valueOf(mPumpConfigBean.getAddress());
        //correctConfigService.delete(mRowIndex, mPumpConfigBean);
        correctConfigService.delete(mPumpConfigBean);
        initRightData();
        delDefaultFm(address);
    }

    private void saveData() {
        configPosition = editPosition.getText().toString();
        materialName = editName.getText().toString().trim();
        highSpeed = editHighSpeed.getText().toString().trim();
        midSpeed = editMidSpped.getText().toString().trim();
        lowSpeed = editLowSpeed.getText().toString().trim();
        ddSpeed = editDDSpeed.getText().toString().trim();
        startSpeed = editStartSpeed.getText().toString().trim();
        startStep = editStartStep.getText().toString().trim();
        alarmValue = editPolice.getText().toString().trim();
        multiple = editMulti.getText().toString().trim();
        maxCapacity = editCapacity.getText().toString().trim();

        if (configPosition == null || configPosition.length() == 0) {
//            ToastUtils.showShort("请输入位置");
//            ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//            ToastUtils.setBgResource(R.color.gray);
            MyToastUtil.showToastSnackBar(configPopView, "请输入位置", Snackbar.LENGTH_SHORT);
            return;
        }
        if (pumpConfigBeans != null && pumpConfigBeans.size() > 0) {
            for (int i = 0; i < pumpConfigBeans.size(); i++) {
                PumpConfigBean pumpConfigBean = pumpConfigBeans.get(i);
                if (Integer.valueOf(configPosition) == Integer.parseInt(pumpConfigBean.getAddress())) {
                    if (addType == 1) {
//                        ToastUtils.showShort("位置已存在");
//                        ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//                        ToastUtils.setBgResource(R.color.gray);
                        MyToastUtil.showToastSnackBar(configPopView, "位置已存在", Snackbar.LENGTH_SHORT);
                        return;
                    }
                }
            }
        }
//        if (mUnit == null || mUnit.length() == 0) {
//            ToastUtils.showShort("请选择单位");
//            ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//            ToastUtils.setBgResource(R.color.gray);
//            return;
//        }
//
//        if (pumpConfigs == null || pumpConfigs.length() == 0) {
//            ToastUtils.showShort("请选择泵配置");
//            ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//            ToastUtils.setBgResource(R.color.gray);
//            return;
//        }
        if (materialName == null || materialName.length() == 0) {
//            ToastUtils.showShort("请输入料名称");
//            ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//            ToastUtils.setBgResource(R.color.gray);
            MyToastUtil.showToastSnackBar(configPopView, "请输入料名称", Snackbar.LENGTH_SHORT);
            return;
        }
        String thisMaterialName = stringFilter(materialName);
        if (!thisMaterialName.equals(materialName)) {
//            ToastUtils.showShort("料名称请输入正确格式");
//            ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//            ToastUtils.setBgResource(R.color.gray);
//            ToastUtils.setMsgTextSize(18);
            MyToastUtil.showToastSnackBar(configPopView, "料名称请输入正确格式", Snackbar.LENGTH_SHORT);
            return;
        }
        if (highSpeed == null || highSpeed.length() == 0) {
//            ToastUtils.showShort("请输入高速速度");
//            ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//            ToastUtils.setBgResource(R.color.gray);
            MyToastUtil.showToastSnackBar(configPopView, "请输入高速速度", Snackbar.LENGTH_SHORT);
            return;
        }
        if (Integer.parseInt(highSpeed) > 16) {
//            ToastUtils.showShort("高速速度不能大于16");
//            ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//            ToastUtils.setBgResource(R.color.gray);
            MyToastUtil.showToastSnackBar(configPopView, "高速速度不能大于16", Snackbar.LENGTH_LONG);
            return;
        }
        if (midSpeed == null || midSpeed.length() == 0) {
//            ToastUtils.showShort("请输入中速速度");
//            ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//            ToastUtils.setBgResource(R.color.gray);
            MyToastUtil.showToastSnackBar(configPopView, "请输入中速速度", Snackbar.LENGTH_SHORT);
            return;
        }
//        if (Integer.parseInt(midSpeed) > Integer.parseInt(highSpeed)) {
////            ToastUtils.showShort("中速速度不能大于16");
////            ToastUtils.setGravity(Gravity.CENTER, 0, 0);
////            ToastUtils.setBgResource(R.color.gray);
//            MyToastUtil.showToastSnackBar(configPopView,"中速不能大于高速速度", Snackbar.LENGTH_LONG);
//            return;
//        }
        if (lowSpeed == null || lowSpeed.length() == 0) {
//            ToastUtils.showShort("请输入低速速度");
//            ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//            ToastUtils.setBgResource(R.color.gray);
            MyToastUtil.showToastSnackBar(configPopView, "请输入低速速度", Snackbar.LENGTH_SHORT);
            return;
        }
//        if (Integer.parseInt(lowSpeed) > Integer.parseInt(midSpeed)) {
////            ToastUtils.showShort("低速速度不能大于16");
////            ToastUtils.setGravity(Gravity.CENTER, 0, 0);
////            ToastUtils.setBgResource(R.color.gray);
//            MyToastUtil.showToastSnackBar(configPopView,"低速不能大于中速速度", Snackbar.LENGTH_LONG);
//            return;
//        }
        if (ddSpeed == null || ddSpeed.length() == 0) {
//            ToastUtils.showShort("请输入点动速度");
//            ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//            ToastUtils.setBgResource(R.color.gray);
            MyToastUtil.showToastSnackBar(configPopView, "请输入点动速度", Snackbar.LENGTH_SHORT);
            return;
        }
        if (Integer.parseInt(ddSpeed) > 16) {
//            ToastUtils.showShort("点动速度不能大于16");
//            ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//            ToastUtils.setBgResource(R.color.gray);
            MyToastUtil.showToastSnackBar(configPopView, "点动速度不能大于16", Snackbar.LENGTH_LONG);
            return;
        }
        if (startSpeed == null || startSpeed.length() == 0) {
//            ToastUtils.showShort("请输入启动速度");
//            ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//            ToastUtils.setBgResource(R.color.gray);
            MyToastUtil.showToastSnackBar(configPopView, "请输入启动速度", Snackbar.LENGTH_SHORT);
            return;
        }
        if (Integer.parseInt(startSpeed) > 16) {
//            ToastUtils.showShort("启动速度不能大于16");
//            ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//            ToastUtils.setBgResource(R.color.gray);
            MyToastUtil.showToastSnackBar(configPopView, "启动速度不能大于16", Snackbar.LENGTH_LONG);
            return;
        }
        if (startStep == null || startStep.length() == 0) {
//            ToastUtils.showShort("请输入启动步数");
//            ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//            ToastUtils.setBgResource(R.color.gray);
            MyToastUtil.showToastSnackBar(configPopView, "请输入启动步数", Snackbar.LENGTH_SHORT);
            return;
        }
        if (alarmValue == null || alarmValue.length() == 0) {
//            ToastUtils.showShort("请输入报警量");
//            ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//            ToastUtils.setBgResource(R.color.gray);
            MyToastUtil.showToastSnackBar(configPopView, "请输入报警量", Snackbar.LENGTH_SHORT);
            return;
        }
        if (maxCapacity == null || maxCapacity.length() == 0) {
//            ToastUtils.showShort("请输入桶容量");
//            ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//            ToastUtils.setBgResource(R.color.gray);
            MyToastUtil.showToastSnackBar(configPopView, "请输入桶容量", Snackbar.LENGTH_SHORT);
            return;
        }
        if (Integer.parseInt(startStep) <= 0 || Integer.parseInt(startStep) > 4095) {
//            ToastUtils.showShort("启动步数0-4095");
//            ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//            ToastUtils.setBgResource(R.color.gray);
            MyToastUtil.showToastSnackBar(configPopView, "启动步数0-4095", Snackbar.LENGTH_LONG);
            return;
        }
        if (Integer.parseInt(alarmValue) > Integer.parseInt(maxCapacity)) {
//            ToastUtils.showShort("报警量不能大于桶容量");
//            ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//            ToastUtils.setBgResource(R.color.gray);
            MyToastUtil.showToastSnackBar(configPopView, "报警量不能大于桶容量", Snackbar.LENGTH_SHORT);
            return;
        }
        if (multiple == null || multiple.length() == 0) {
//            ToastUtils.showShort("报警量不能大于桶容量");
//            ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//            ToastUtils.setBgResource(R.color.gray);
            MyToastUtil.showToastSnackBar(configPopView, "稀释倍数不能为空", Snackbar.LENGTH_SHORT);
            return;
        }
        if (addConfigPop != null) {
            addConfigPop.dismiss();
        }

        if (addType == 1) {
            PumpConfigBean pumpConfigBean = new PumpConfigBean();
            pumpConfigBean.setAddress(configPosition);
            pumpConfigBean.setMaterials_name(materialName);
            pumpConfigBean.setHighSpeed(highSpeed);
            pumpConfigBean.setMidSpeed(midSpeed);
            pumpConfigBean.setLowSpeed(lowSpeed);
            pumpConfigBean.setDdSpeed(ddSpeed);
            pumpConfigBean.setStartSpeed(startSpeed);
            pumpConfigBean.setStartStep(startStep);
            pumpConfigBean.setAlarm_value(alarmValue);
            pumpConfigBean.setMax_capacity(maxCapacity);
            pumpConfigBean.setMultiple(multiple);
//            int index = -1;
//            for (int i = 0; i < pumpConfigBeans.size(); i++) {
//                PumpConfigBean pumpConfigBean1 = pumpConfigBeans.get(i);
//                int address = pumpConfigBean1.getAddress();
//                if (Integer.parseInt(configPosition) <= address) {
//                    index = i;
//                    break;
//                }
//            }
            correctConfigService.save(pumpConfigBean);
            addDefaultFm(configPosition);

        } else if (addType == 2) {
            mPumpConfigBean.setAddress(configPosition);
            mPumpConfigBean.setMaterials_name(materialName);
            mPumpConfigBean.setHighSpeed(highSpeed);
            mPumpConfigBean.setMidSpeed(midSpeed);
            mPumpConfigBean.setLowSpeed(lowSpeed);
            mPumpConfigBean.setDdSpeed(ddSpeed);
            mPumpConfigBean.setStartSpeed(startSpeed);
            mPumpConfigBean.setStartStep(startStep);
            mPumpConfigBean.setAlarm_value(alarmValue);
            mPumpConfigBean.setMax_capacity(maxCapacity);
            mPumpConfigBean.setMultiple(multiple);
            //correctConfigService.update(mRowIndex, mPumpConfigBean);
            correctConfigService.update(mPumpConfigBean);
            SaveBean saveBean = new SaveBean();
            int outIndex = -1;
            if (saveBeans != null && saveBeans.size() > 0) {
                for (int i = 0; i < saveBeans.size(); i++) {//存在修改后的位置也存在的情况
                    saveBean = saveBeans.get(i);
                    if (saveBean.getAddress() == oldConfigAddress) {
                        outIndex = i;
                        saveBean.setAddress(Integer.parseInt(mPumpConfigBean.getAddress()));
                        break;
                    }
                }
            }
            if (outIndex >= 0) {
                correctDataService.update(outIndex, saveBean);
            }

        }
        //initData();
        initRightData();
    }

    private void addDefaultFm(String position) {
//        FmParamsBean fmParamsBean = new FmParamsBean();
//        fmParamsBean.setBarrelNo(position);
//        fmParamsBean.setHighThreshold("8");
//        fmParamsBean.setLowThreshold("3");
//        fmParamsBean.setCloseThreshold("0.5");
//        fmParamsBean.setCannedaPrecision("0.001");
//        fmService.put(fmParamsBean);
    }

    private void delDefaultFm(String position) {
//        if (fmParamsBeans != null && fmParamsBeans.size() > 0) {
//            int index = -1;
//            FmParamsBean fmParamsBean =null;
//            for (int i=0;i<fmParamsBeans.size();i++) {
//                fmParamsBean = fmParamsBeans.get(i);
//                if (fmParamsBean.getBarrelNo().equals(position)) {
//                    index = i;
//                    break;
//                }
//            }
//            if(index>=0 && fmParamsBean!=null){
//                fmService.delete(index,fmParamsBean);
//            }
//
//        }


    }

    public String stringFilter(String str) throws PatternSyntaxException {
// 只允许字母、数字和汉字其余的还可以随时添加比如下划线什么的，但是注意引文符号和中文符号区别
        String regEx = "[^a-zA-Z0-9\u4E00-\u9FA5-]";//正则表达式
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    @Override
    public void onClick(View v) {

    }

    private void addConfigPopMethod() {
        inflater = LayoutInflater.from(getMyActivity());
        addConfigPop = new AddConfigPop(getMyActivity(), BasePopupWindow.WRAP_CONTENT, BasePopupWindow.WRAP_CONTENT);//高度必须设置wrap，否则不能覆盖底部导航栏
        addConfigPop.setOutSideDismiss(true);
        addConfigPop.setClipToScreen(true);
        addConfigPop.setAutoLocatePopup(true);
        int width = DensityUtil.getScreenWidth();
        int height = DensityUtil.getScreenHeight();
        //AppApplication.getInstance().init(view);
        addConfigPop.setPopupGravity(Gravity.CENTER);
        addConfigPop.showPopupWindow();

    }

    ///////////////////////////////////////添加泵配置弹窗
    class AddConfigPop extends BasePopupWindow implements PumpConfigDialog.PumpConfigListener, PumpSetListener, View.OnClickListener {

        public AddConfigPop(Context context) {
            super(context);
        }

        public AddConfigPop(Context context, boolean delayInit) {
            super(context, delayInit);
        }

        public AddConfigPop(Context context, int width, int height) {
            super(context, width, height);
        }

        public AddConfigPop(Context context, int width, int height, boolean delayInit) {
            super(context, width, height, delayInit);
        }

        @Override
        public View onCreateContentView() {
            LayoutInflater inflater = (LayoutInflater) getMyActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            configPopView = inflater.inflate(R.layout.add_pump_config, null);
            ryLong = longView.findViewById(R.id.recyclerview);
            initView();
            return configPopView;
        }

        private void initView() {
            ivClose = configPopView.findViewById(R.id.iv_close);
            editPosition = configPopView.findViewById(R.id.id_edit_position);
            editHighSpeed = configPopView.findViewById(R.id.edit_highspeed);
            editName = configPopView.findViewById(R.id.id_edit_name);
            editMidSpped = configPopView.findViewById(R.id.edit_midspeed);
            editLowSpeed = configPopView.findViewById(R.id.edit_lowspeed);
            editDDSpeed = configPopView.findViewById(R.id.edit_ddspeed);
            editStartSpeed = configPopView.findViewById(R.id.edit_startspeed);

            editStartStep = configPopView.findViewById(R.id.edit_startstep);
            editCapacity = configPopView.findViewById(R.id.edit_capacity);
            editPolice = configPopView.findViewById(R.id.edit_police);
            editMulti = configPopView.findViewById(R.id.edit_multi);
            btnAdd = configPopView.findViewById(R.id.btn_add);
            btnCancel = configPopView.findViewById(R.id.btn_cancel);
            tvPumpTitle = configPopView.findViewById(R.id.id_tv_title);
            editTextListener(editPosition);
            editTextListener(editHighSpeed);
            editTextListener(editMidSpped);
            editTextListener(editLowSpeed);
            editTextListener(editDDSpeed);
            editTextListener(editStartSpeed);
            editTextListener(editStartStep);
            editTextListener(editCapacity);
            editTextListener(editPolice);
            editTextListener(editMulti);
            if (addType == 1) {
                tvPumpTitle.setText("添加泵配置");
            } else if (addType == 2) {
                tvPumpTitle.setText("修改泵配置");
                editPosition.setText(String.valueOf(mPumpConfigBean.getAddress()));

                editName.setText(mPumpConfigBean.getMaterials_name());
                editHighSpeed.setText(String.valueOf(mPumpConfigBean.getHighSpeed()));

                editMidSpped.setText(String.valueOf(mPumpConfigBean.getMidSpeed()));

                editLowSpeed.setText(String.valueOf(mPumpConfigBean.getLowSpeed()));

                editDDSpeed.setText(String.valueOf(mPumpConfigBean.getDdSpeed()));

                editStartSpeed.setText(String.valueOf(mPumpConfigBean.getStartSpeed()));

                editStartStep.setText(mPumpConfigBean.getStartStep());

                editCapacity.setText(String.valueOf(mPumpConfigBean.getMax_capacity()));

                editPolice.setText(String.valueOf(mPumpConfigBean.getAlarm_value()));
                editMulti.setText(String.valueOf(mPumpConfigBean.getMultiple()));

            }
            setClick();
            setEditHint(editPosition, "1-99");
            setEditHint(editName, "内部物料名称");
            setEditHint(editPolice, "不能大于桶容量");
            setEditHint(editHighSpeed, "不能大于16");
            setEditHint(editMidSpped, "不能大于16");
            setEditHint(editLowSpeed, "不能大于16");
            setEditHint(editDDSpeed, "不能大于16");
            setEditHint(editStartSpeed, "不能大于16");
            setEditHint(editStartStep, "0-4095");
            setEditHint(editMulti,"1-100");
//            setEditHint(editRise, "0-9");
//            setEditHint(editFall, "0-9");
//            setEditHint(editDensity, "大于0");
        }

        private void editTextListener(EditText edittext) {
            edittext.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String editable = s.toString();
                    String trim = edittext.getText().toString().trim();
                    if (editable.equals("0")) {
                        edittext.setText("");
                    } else if (trim.length() > 1 && trim.substring(0, 1).equals("0")) {
                        edittext.setText("");
                    }
                }
            });
        }

        private void setClick() {
            btnCancel.setOnClickListener(this);
            btnAdd.setOnClickListener(this);
            ivClose.setOnClickListener(this);
        }


        @Override
        public void onConfigListener(String config) {
//
        }


        @Override
        public void onModelClick(String model) {

        }

        @Override
        public void onMaterialClick(String materialType) {

        }

        @Override
        public void onPumpTypeClick(String pumpType) {

        }

        @Override
        public void onUnitClick(int type, String unit) {
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.btn_add) {
                saveData();
            } else if (id == R.id.btn_cancel) {
                if (addConfigPop != null) {
                    addConfigPop.dismiss();
                }
            } else if (id == R.id.iv_close) {
                if (addConfigPop != null) {
                    addConfigPop.dismiss();
                }
            }
        }
    }
    //////////////////////////////////添加泵配置弹窗
}
