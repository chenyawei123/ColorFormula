package com.example.datacorrects.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.GsonUtils;
import com.cyw.mylibrary.application.AppApplication;
import com.cyw.mylibrary.base.BaseFragment;
import com.cyw.mylibrary.bean.ColorBean;
import com.cyw.mylibrary.bean.ColorformulaBean;
import com.cyw.mylibrary.bean.CorrectParamsBean;
import com.cyw.mylibrary.bean.FmParamsBean;
import com.cyw.mylibrary.bean.FormulaitemsBean;
import com.cyw.mylibrary.bean.MyFormula;
import com.cyw.mylibrary.bean.PumpConfigBean;
import com.cyw.mylibrary.netrequest.utils.PowderPumpProtocal;
import com.cyw.mylibrary.services.FMServiceNew;
import com.cyw.mylibrary.services.ManualColorBeanService;
import com.cyw.mylibrary.services.MyFormulaService;
import com.cyw.mylibrary.services.PumpConfigBeanService;
import com.cyw.mylibrary.util.ComputeDoubleUtil;
import com.cyw.mylibrary.util.IOSDialog;
import com.cyw.mylibrary.util.MyToastUtil;
import com.cyw.mylibrary.util.SharedPreferencesHelper;
import com.example.datacorrects.R;
import com.example.datacorrects.adapter.MainFeedAdapter;
import com.example.datacorrects.adapter.MainLongAdapter;
import com.example.datacorrects.adapter.MainTenanceAdapter;
import com.example.datacorrects.bean.MainLongBean;
import com.example.datacorrects.bean.MainTenanceBean;
import com.example.datacorrects.bean.MessageEventMainTean;
import com.example.datacorrects.bean.PumpSetBean;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.util.DensityUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import popup.BasePopup.BasePopupWindow;


/**
 *
 */

public class MainTenance extends BaseFragment implements MainTenanceAdapter.OnItemClickListener, MainTenanceAdapter.OnItemLongClickListener, View.OnClickListener {
    private MainTenanceAdapter mAdapter;
    private View decorview = null;
    private RecyclerView rvMain;
    private final List<MainTenanceBean> mDatas = new ArrayList<MainTenanceBean>();
    private LongPopWindow longPopWindow;
    private AddMaterialPop addMaterialPop;
    private LayoutInflater inflater = null;
    private View longView = null;
    private View materialView = null;
    private RecyclerView ryLong, ryFeed;
    private MainLongAdapter mainLongAdapter;
    private MainFeedAdapter mainFeedAdapter;
    private final List<MainLongBean> mainLongBeans = new ArrayList<MainLongBean>();
    private final List<String> mainFeedBeans = new ArrayList<String>();
    private View eView;
    private View lastView;
    private int curPos;
    private PumpConfigBeanService correctConfigService;
    private List<PumpConfigBean> mPumpConfigBeans = new ArrayList<>();

    /////////////////manualDryDialog
    private EditText editDryTime;
    private String dryTime;
    private TextView tvPosition;
    private TextView tvManualDry;
    private Button btnLeft;
    private Button btnRight;
    private Dialog manualDryDialog;
    private View manulDryView;
    private String aerationDryTime = "";
    private int configAddress = 0;
    private PumpConfigBean mPumpConfigBean = null;
    private PumpSetBean mPumpSetBean = null;
    private IOSDialog iosDialog = null;
    private CorrectParamsBean correctParamsBean;
    private String touType = "";
    private String mCurTypeName = "";
    private ManualColorBeanService manualColorBeanService;
    private MyFormulaService myFormulaService;
    private List<ColorBean> manualColorBeans = new ArrayList<>();

    private FMServiceNew fmService;
    private List<FmParamsBean> fmParamsBeans = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        decorview = inflater.inflate(R.layout.main_tenance, container, false);
        // AppApplication.getInstance().hideDialogNavigationBar(decorview);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initView();
        return decorview;
    }

    private void initView() {
        rvMain = decorview.findViewById(R.id.rv_main);
        getService();
        Boolean first_run = SharedPreferencesHelper.getBoolean(getMyActivity(), "First", true);
        if (first_run) {
            SharedPreferencesHelper.putBoolean(getMyActivity(), "First", false);
            initConfigList();
        } else {
            if (mPumpConfigBeans != null && mPumpConfigBeans.size() > 0) {
                mPumpConfigBeans.clear();
            }
            mPumpConfigBeans = correctConfigService.getAllListByType();
            if (mPumpConfigBeans != null && mPumpConfigBeans.size() > 0) {
                getConfigList();
            } else {
                initConfigList();
            }
        }

        bindAdapter();
    }


    private void getService() {
//        correctDataService = new CorrectDataService(getMyActivity());
        correctConfigService = PumpConfigBeanService.getInstance();
//        manualColorBeanService = new ManualColorBeanService(getMyActivity());
//        if (manualColorBeans != null && manualColorBeans.size() > 0) {
//            manualColorBeans.clear();
//        }
//        manualColorBeans = manualColorBeanService.getAll();
        myFormulaService = new MyFormulaService(getMyActivity());
        getStandardColorBeans();
        fmService = FMServiceNew.getInstance();
        if (fmParamsBeans != null && fmParamsBeans.size() > 0) {
            fmParamsBeans.clear();
        }
        fmParamsBeans = fmService.getAllListByType();

    }

    private void getStandardColorBeans() {
        if (manualColorBeans != null && manualColorBeans.size() > 0) {
            manualColorBeans.clear();
        }
        List<MyFormula> myFormulas = myFormulaService.getAll();
        for (int i = 0; i < myFormulas.size(); i++) {
            MyFormula myFormula = myFormulas.get(i);
            ColorformulaBean colorformula = myFormula.getColorformula();
//            FormulaitemsBean formulaitems = colorformula.getFormulaitems();
            FormulaitemsBean formulaitems = colorformula.getFormulaitems();
            List<ColorBean> colorBeans = new ArrayList<>();
//            if (formulaitems instanceof FormulaitemsBean) {
//                colorBeans = ((FormulaitemsBean) formulaitems).getColorBeans();
//
//            } else if (formulaitems instanceof FormulaitemsBean2) {
//                ColorBean colorBean = ((FormulaitemsBean2) formulaitems).getColorBean();
//                colorBeans.add(colorBean);
//            }
            colorBeans = getCommonColorType(formulaitems);
            for (int i1 = 0; i1 < colorBeans.size(); i1++) {
                ColorBean colorBean = colorBeans.get(i1);
                if (!isContains(colorBean.getColorNo())) {
                    colorBean.setColorDos("");
                    manualColorBeans.add(colorBean);
                }
            }

        }
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
        //}

        return colorBeanList;
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


    private boolean isExistMaterial(String materialName) {
        boolean isExist = false;
        if (mPumpConfigBeans != null && mPumpConfigBeans.size() > 0) {
            for (int i = 0; i < mPumpConfigBeans.size(); i++) {
                PumpConfigBean pumpConfigBean = mPumpConfigBeans.get(i);
                if (materialName.equals(pumpConfigBean.getMaterials_name())) {
                    isExist = true;
                    break;
                }
            }
        }
        return isExist;
    }

    private boolean isExistTong(String tong) {
        boolean isExist = false;
        if (fmParamsBeans != null && fmParamsBeans.size() > 0) {
            for (int i = 0; i < fmParamsBeans.size(); i++) {
                FmParamsBean fmParamsBean = fmParamsBeans.get(i);
                if (tong.equals(fmParamsBean.getBarrelNo())) {
                    isExist = true;
                    break;
                }
            }
        }
        return isExist;
    }

    private void initConfigList() {
        if (mPumpConfigBeans != null && mPumpConfigBeans.size() > 0) {
            mPumpConfigBeans.clear();
        }
        mPumpConfigBeans = correctConfigService.getAllListByType();
        int address = 1;
//        if (manualColorBeans != null && manualColorBeans.size() > 0) {
        if (mPumpConfigBeans == null || mPumpConfigBeans.size() == 0) {
            for (int i = 0; i < 12; i++) {
//                ColorBean manualColorBean = manualColorBeans.get(i);
//                String colorNo = manualColorBean.getColorNo();
                //if (!isExistMaterial(colorNo)) {
                address = i + 1;
                //correctConfigService.update();
                PumpConfigBean pumpConfigBean = new PumpConfigBean();
                // pumpConfigBean.setMaterials_name(colorNo);
                pumpConfigBean.setAddress(String.valueOf(address));
                pumpConfigBean.setHighSpeed("");
                pumpConfigBean.setMidSpeed("");
                pumpConfigBean.setLowSpeed("");
                pumpConfigBean.setDdSpeed("");
                pumpConfigBean.setStartSpeed("");
                pumpConfigBean.setStartStep("");
                pumpConfigBean.setAlarm_value("");
                pumpConfigBean.setMax_capacity("");
                pumpConfigBean.setMultiple("1");
                correctConfigService.save(pumpConfigBean);
                //}
                if (!isExistTong(String.valueOf(address))) {
                    addDefaultFm(String.valueOf(address));
                }
            }
        }

        // }
        combineData();
    }

    private void getConfigList() {
        if (mPumpConfigBeans != null && mPumpConfigBeans.size() > 0) {
            mPumpConfigBeans.clear();
        }
        mPumpConfigBeans = correctConfigService.getAllListByType();
//        int address = 1;
//        int size = 0;
//
////        if (manualColorBeans != null && manualColorBeans.size() > 0) {
//        for (int i = 0; i < 12; i++) {
////                ColorBean manualColorBean = manualColorBeans.get(i);
////                String colorNo = manualColorBean.getColorNo();
//            //if (!isExistMaterial(colorNo)) {
//            address = pumpConfigBeans.size() + 1;
//            //correctConfigService.update();
//            PumpConfigBean pumpConfigBean = new PumpConfigBean();
//            // pumpConfigBean.setMaterials_name(colorNo);
//            pumpConfigBean.setAddress(address);
//            pumpConfigBean.setHighSpeed("");
//            pumpConfigBean.setMidSpeed("");
//            pumpConfigBean.setLowSpeed("");
//            pumpConfigBean.setDdSpeed("");
//            pumpConfigBean.setStartSpeed("");
//            pumpConfigBean.setStartStep("");
//            correctConfigService.put(pumpConfigBean);
//            //}
//            if (!isExistTong(String.valueOf(address))) {
//                addDefaultFm(String.valueOf(address));
//            }
//        }
//        // }
        combineData();
    }

    private void addDefaultFm(String position) {
        FmParamsBean fmParamsBean = new FmParamsBean();
        fmParamsBean.setBarrelNo(position);
        fmParamsBean.setHighThreshold("8");
        fmParamsBean.setLowThreshold("3");
        fmParamsBean.setCloseThreshold("0.5");
        fmParamsBean.setCannedaPrecision("0.001");
        fmParamsBean.setDdCount(5);
        fmParamsBean.setOpenBig(String.valueOf(60));
        fmParamsBean.setCloseBig(String.valueOf(83));
        fmParamsBean.setOpenSmall(String.valueOf(62));
        fmParamsBean.setCloseSmall(String.valueOf(85));

        fmService.save(fmParamsBean);
    }


    private void combineData() {
        if (mDatas != null && mDatas.size() > 0) {
            mDatas.clear();
        }
        if (mPumpConfigBeans != null && mPumpConfigBeans.size() > 0) {
            for (int i = 0; i < mPumpConfigBeans.size(); i++) {
                PumpConfigBean pumpConfigBean = mPumpConfigBeans.get(i);
                String materials_name = pumpConfigBean.getMaterials_name();
                if (materials_name != null && materials_name.length() > 0) {
                    MainTenanceBean mainTenanceBean = new MainTenanceBean();
                    mainTenanceBean.setMax(Integer.parseInt(pumpConfigBean.getMax_capacity()));
                    mainTenanceBean.setValue(Integer.parseInt(pumpConfigBean.getAlarm_value()));
                    mainTenanceBean.setType(pumpConfigBean.getMaterials_name());
                    mainTenanceBean.setId(Integer.parseInt(pumpConfigBean.getAddress()));
                    mainTenanceBean.setChangeValue(pumpConfigBean.getCurValue());
                    mDatas.add(mainTenanceBean);
                }
            }
        }
    }

    public void refData() {
        getService();
        getConfigList();
        bindAdapter();
    }


    private void bindAdapter() {
        mAdapter = new MainTenanceAdapter(getMyActivity(), mDatas);
        rvMain.setAdapter(mAdapter);
        rvMain.setLayoutManager(new GridLayoutManager(getMyActivity(), 10));
        rvMain.setItemAnimator(new DefaultItemAnimator());
        //     rvMain.addItemDecoration(new DividerGridItemDecoration(getMyActivity()));//GridSpacingItemDecoration(6, 10, true)
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemLongClickListener(this);
//        mDecoration = new ItemHeaderDecoration(getMyActivity(), mDatas);
//        rvMain.addItemDecoration(mDecoration);
    }


    //    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void receiveMessage2(MessageEventChuanCai event) {
//        byte[] result = event.getResult();
//        LogUtils.eTag("传菜线数据", result.length + "+" + result);
//
//        String dataByteData = "";
//        dataByteData = Byte2Hex.byte2Hex(result);
//        String resultShi = Byte2Hex.bytes2HexString(result);
//        int lastIndex = resultShi.lastIndexOf("EF");
//        resultShi = resultShi.substring(0, lastIndex);
//        long l = System.currentTimeMillis();
//        String time = DateFormatter.getCurrentTime();
//        WriteLog.writeTxtToFile(time + "回复数据3" + resultShi);
//        int[] ints = new int[result.length];
//        char[] chars = new char[result.length];
//        for (int i = 0; i < result.length; i++) {
//            byte b1 = result[i];
//            int aInt = Byte2Hex.byteToInteger(b1);
//            ints[i] = aInt;
//            if (aInt > 127) {
//                chars[i] = (char) (256 - aInt);
//            } else {
//                chars[i] = (char) aInt;
//            }
//
//        }
//        Log.i("TAGINT", "hhhhhh" + ints.length + chars.length + "JJJ" + ints[1]);
//        if (ints[1] == 225) {///ack
//
//            if (AppApplication.getInstance().getSendCmd() == 5) {
//                int mingling = ints[4];
//                int receiveBigStep = ints[3];
//                int pos = ints[2];//位置
//                ColdFoodSocket.bigStep(receiveBigStep, pos);
//            }
//            if(AppApplication.getInstance().getBigSendCmd() == 9 && AppApplication.getInstance().getSendCmd() == 5){
//                AppApplication.getInstance().setBigSendCmd(0);
//                if (manualDryDialog != null) {
//                    manualDryDialog.dismiss();
//                }
//                ToastUtils.showShort("手动干燥完成");
//            }
//        }
//    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEventMainTean messageEventMainTean) {
//        String mainTen = messageEventMainTean.getDataMainTen();
//        String errorMsg = messageEventMainTean.getErrorMsg();
//        if (mainTen.equals("dataMainTen")) {
//            if (manualDryDialog != null) {
//                manualDryDialog.dismiss();
//            }
//            //ToastUtils.showShort("手动干燥完成");
//        } else if (mainTen.equals("dataMainTenError")) {
//            errorCodeDialog(errorMsg);
//        }
        int[] ints = messageEventMainTean.getInts();
        String mainTen = messageEventMainTean.getDataMainTen();
        if (mainTen.equals("cmdMark")) {
            int tongInt = Integer.parseInt(mPumpConfigBean.getAddress());
            boolean isLeft = SharedPreferencesHelper.getBoolean(getMyActivity(), "isLeft", true);
            String tong = "";
            if (isLeft) {
                if (tongInt == 10) {
                    tong = String.valueOf(1);
                } else if (tongInt == 11) {
                    tong = String.valueOf(2);
                } else if (tongInt == 12) {
                    tong = String.valueOf(3);
                } else {
                    tong = String.valueOf(tongInt + 3);
                }

            } else {
                if (tongInt == 1) {
                    tong = String.valueOf(10);
                } else if (tongInt == 2) {
                    tong = String.valueOf(11);
                } else if (tongInt == 3) {
                    tong = String.valueOf(12);
                } else {
                    tong = String.valueOf(tongInt - 3);
                }
            }
            PowderPumpProtocal.barrelHuntingProtocal(tong);
        } else if (mainTen.equals("success")) {
            showMaterialDialog();
        } else if (mainTen.equals("failure")) {
            MyToastUtil.showToastSnackBar(decorview, "寻桶失败", Snackbar.LENGTH_SHORT);
        }
    }

    /**
     * 长按弹出对话框
     */
    private void showLongDialog(View view, int pos) {
        inflater = LayoutInflater.from(getMyActivity());
        longPopWindow = new LongPopWindow(getMyActivity(), BasePopupWindow.WRAP_CONTENT, BasePopupWindow.WRAP_CONTENT);
        longPopWindow.setOutSideDismiss(true);
        longPopWindow.setPopupGravity(Gravity.CENTER);
        int[] location = new int[2];
        view.getLocationInWindow(location);
        int screenHeight = DensityUtil.getScreenHeight();
        int screenWidth = DensityUtil.getScreenWidth();
        int viewWidth = view.getWidth();
        float viewX = view.getRight();
        int x = (int) (viewX + DensityUtil.dip2px(30));
        int y = (int) view.getY() + DensityUtil.dip2px(150);
        //  longPopWindow.showPopupWindow(screenWidth/4-location[0],screenHeight/4-location[1]);
//        if(screenHeight-location[1]<DensityUtil.dip2px(80)){
//            longPopWindow.getPopupWindow().showAsDropDown(view,screenWidth/4-DensityUtil.dip2px(40),-view.getHeight()-DensityUtil.dip2px(80));
//        }else{
//            longPopWindow.getPopupWindow().showAsDropDown(view,screenWidth/4-DensityUtil.dip2px(40),0);
//        }
//        longPopWindow.setClipToScreen(true);
//        longPopWindow.setAutoLocatePopup(true);
        AppApplication.getInstance().init(decorview);
        longPopWindow.showPopupWindow(x, y);
    }

    @Override
    public void onItemClick(View view, int position) {
        MainTenanceBean mainTenanceBean = mDatas.get(position);
        lastView = view;
        curPos = position;
        clearView();
        RelativeLayout textView = view.findViewById(R.id.id_rel);
        textView.setSelected(true);
        String showText = mainTenanceBean.getType() + " ID: " + mainTenanceBean.getId() + " 最大值：" + mainTenanceBean.getMax() + " 警告值：" + mainTenanceBean.getValue();
//        ToastUtils.showShort(mainTenanceBean.getType() + " ID: " + mainTenanceBean.getId() + " 最大值：" + mainTenanceBean.getMax() + " 警告值：" + mainTenanceBean.getValue());
//        ToastUtils.setMsgTextSize(18);
        MyToastUtil.showToastSnackBar(decorview, showText, Snackbar.LENGTH_LONG);
    }

    private void clearView() {
        for (int i = 0; i < rvMain.getChildCount(); i++) {
            RelativeLayout layout = (RelativeLayout) rvMain.getChildAt(i);
            RelativeLayout textView = layout.findViewById(R.id.id_rel);
            textView.setSelected(false);
        }
    }


    @Override
    public void onItemLongClick(View view, int position) {
        MainTenanceBean mainTenanceBean = mDatas.get(position);
        if (mainTenanceBean != null) {
            if (mPumpConfigBeans != null && mPumpConfigBeans.size() > 0) {
                for (int i = 0; i < mPumpConfigBeans.size(); i++) {
                    PumpConfigBean pumpConfigBean = mPumpConfigBeans.get(i);
                    if (mainTenanceBean.getId() == Integer.parseInt(pumpConfigBean.getAddress())) {
                        mPumpConfigBean = pumpConfigBean;
                        break;
                    }
                }
            }
        }
        //mPumpConfigBean = pumpConfigBeans.get(position);
        configAddress = Integer.parseInt(mPumpConfigBean.getAddress());
        //  mPumpSetBean = getPumpSet();
        clearView();
        RelativeLayout textView = view.findViewById(R.id.id_rel);
        textView.setSelected(true);
//        if (mainLongBeans != null && mainLongBeans.size() > 0) {
//            mainLongBeans.clear();
//        }
//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
        showLongDialog(view, position);
//            }
//        });

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_manual_dry) {
            aerationDryTime = editDryTime.getText().toString();
            if (aerationDryTime == null || aerationDryTime.length() == 0) {
//                ToastUtils.showShort("请填写干燥时间");
//                ToastUtils.setMsgTextSize(18);
//                ToastUtils.setGravity(Gravity.CENTER, 0, 0);
                MyToastUtil.showToast(getMyActivity(), "请填写干燥时间", Toast.LENGTH_LONG);
                return;
            }
            if (mCurTypeName == null || mCurTypeName.length() == 0) {
//                ToastUtils.showShort("请选择左右类型");
//                ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//                ToastUtils.setBgResource(R.color.gray);
                MyToastUtil.showToast(getMyActivity(), "请选择左右类型", Toast.LENGTH_LONG);
                return;
            }
            int location = 1;
            if (mCurTypeName.equals("左侧")) {
                location = 2;
            }
            // ColdFoodSocket.manualDry(configAddress, aerationDryTime, location);
        } else if (id == R.id.btn_left) {
            mCurTypeName = "左侧";
            btnLeft.setSelected(true);
            btnRight.setSelected(false);

        } else if (id == R.id.btn_right) {
            mCurTypeName = "右侧";
            btnLeft.setSelected(false);
            btnRight.setSelected(true);
        }
    }


    class LongPopWindow extends BasePopupWindow implements View.OnClickListener {
        public LongPopWindow(Context context, int width, int height) {
            super(context, width, height);
        }

        @Override
        public View onCreateContentView() {
            //longView = createPopupById(R.layout.pop_long_dialog);
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
//            MainLongBean mainLongBean2 = new MainLongBean();
//            mainLongBean2.setStep("清洗");
//            MainLongBean mainLongBean3 = new MainLongBean();
//            mainLongBean3.setStep("搅拌");
//            MainLongBean mainLongBean4 = new MainLongBean();
//            mainLongBean4.setStep("排空");
            mainLongBeans.add(mainLongBean);
//            mainLongBeans.add(mainLongBean2);
//            mainLongBeans.add(mainLongBean3);
            // mainLongBeans.add(mainLongBean4);
            ////////////////////////////master_v1 新增
//                if (mPumpSetBean != null) {
//                    if (mPumpSetBean.getMaterials_type().equals("粉料")) {
//                        MainLongBean mainLongBean5 = new MainLongBean();
//                        mainLongBean5.setStep("冲气");
//                        mainLongBeans.add(mainLongBean5);
//                    }
//                } else {
//                    ToastUtils.showShort("请去泵设置设置该物料");
//                    ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//                    ToastUtils.setBgResource(R.color.gray);
//                    ToastUtils.setMsgTextSize(18);
//                }
        }

        private void bindLongAdapter() {
            if (mainLongBeans != null && mainLongBeans.size() > 0) {
                mainLongAdapter = new MainLongAdapter(getMyActivity(),
                        mainLongBeans);
                ryLong.setAdapter(mainLongAdapter);
                ryLong.setLayoutManager(new LinearLayoutManager(getMyActivity()));
                ryLong.setItemAnimator(new DefaultItemAnimator());
//                ryLong.addItemDecoration(new DividerItemDecoration(
//                        context, DividerItemDecoration.VERTICAL_LIST));

                mainLongAdapter.setOnItemClickListener(new MainLongAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (longPopWindow != null) {
                            longPopWindow.dismiss();
                        }
                        if (position == 0) {
                            if (mainFeedBeans != null && mainFeedBeans.size() > 0) {
                                mainFeedBeans.clear();
                            }
                            AppApplication.getInstance().isSendSearchBarrel = true;
//                            showMaterialDialog();
                        } else if (position == 1) {
                            // manualDialog();
                        } else if (position == 2) {

                        } else if (position == 3) {

                        }

                    }
                });
            }
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

    /**
     * 弹出二级对话框
     */
    @SuppressLint("ResourceAsColor")
    private void showMaterialDialog() {
        inflater = LayoutInflater.from(getMyActivity());
        addMaterialPop = new AddMaterialPop(getMyActivity(), BasePopupWindow.WRAP_CONTENT, BasePopupWindow.WRAP_CONTENT);
        addMaterialPop.setOutSideDismiss(false);
        addMaterialPop.setPopupGravity(Gravity.CENTER);
        addMaterialPop.showPopupWindow();

    }

    class AddMaterialPop extends BasePopupWindow implements View.OnClickListener {
        private EditText editAdd;
        private Button btnSure, btnAddFull;
        private ImageView ivClose;

        private TextView tvMaterialName;
        private String addValue;
        private TextView tvMaxValue;
        private TextView tvMinValue;
        private TextView tvCurrentValue;
        private TextView tvAlarmValue;
        private TextView tvLocationName;
        private TextView tvUnit;

        public AddMaterialPop(Context context, int width, int height) {
            super(context, width, height);
        }

        @Override
        public View onCreateContentView() {
            //materialView = createPopupById(R.layout.add_material_pop);
            LayoutInflater inflater = (LayoutInflater) getMyActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            materialView = inflater.inflate(R.layout.add_material_pop, null);
            tvMaxValue = materialView.findViewById(R.id.tv_max_value);
            tvMinValue = materialView.findViewById(R.id.tv_min_value);
            tvCurrentValue = materialView.findViewById(R.id.tv_current_value);
            tvAlarmValue = materialView.findViewById(R.id.tv_alarm_value);
            tvMaterialName = materialView.findViewById(R.id.tv_material_name);
            tvLocationName = materialView.findViewById(R.id.tv_location_name);
            editAdd = materialView.findViewById(R.id.edit_add);
            editAdd.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String trim = editAdd.getText().toString().trim();
                    if (trim.equals("0")) {
                        editAdd.setText("");
                    }
                }
            });
            tvUnit = materialView.findViewById(R.id.tv_unit);
            btnSure = materialView.findViewById(R.id.btn_sure);
            btnAddFull = materialView.findViewById(R.id.btn_add_full);
            ivClose = materialView.findViewById(R.id.iv_close);
            ivClose.setOnClickListener(this);
            btnAddFull.setOnClickListener(this);
            btnSure.setOnClickListener(this);
            setMaterialText();
            return materialView;
        }

        private void setMaterialText() {
            String materialName = "";
            String locationName = "";
            String maxValue = "";
            String minValue = "0";
            String currentValue = "";
            String alarmValue = "";
            if (mPumpConfigBean != null) {
                materialName = mPumpConfigBean.getMaterials_name();
                locationName = mPumpConfigBean.getAddress() + "";
                maxValue = mPumpConfigBean.getMax_capacity() + "";
                alarmValue = mPumpConfigBean.getAlarm_value() + "";
                DecimalFormat df = new DecimalFormat();
                currentValue = df.format(mPumpConfigBean.getCurValue());

            }
            String materialType = "液料";
            if (mPumpSetBean != null) {
                materialType = mPumpSetBean.getMaterials_type();
            }
            tvMaterialName.setText(materialName);
            tvLocationName.setText(locationName);
            tvMaxValue.setText(maxValue);
            tvMinValue.setText(minValue);
            tvCurrentValue.setText(currentValue);
            tvAlarmValue.setText(alarmValue);
            tvUnit.setText("g");
//            if (materialType.equals("液料")) {
//                tvUnit.setText("ml");
//            } else if (materialType.equals("粉料")) {
//                tvUnit.setText("g");
//            }

        }

        @Override
        public void dismiss() {
            super.dismiss();
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            String adds = editAdd.getText().toString().trim();
            if (id == R.id.btn_sure) {
                if (adds == null || adds.length() == 0) {
//                    ToastUtils.showShort("请输入添加量");
//                    ToastUtils.setGravity(Gravity.CENTER, 0, 0);
                    MyToastUtil.showToastSnackBar(materialView, "请输入添加量", Snackbar.LENGTH_SHORT);
                    return;
                }

                int location = Integer.parseInt(mPumpConfigBean.getAddress());
                double oldCurValue = mPumpConfigBean.getCurValue();
                String materialName = mPumpConfigBean.getMaterials_name();
                int maxValue = Integer.parseInt(mPumpConfigBean.getMax_capacity());
                int alarm_value = Integer.parseInt(mPumpConfigBean.getAlarm_value());
                double addValue = Double.parseDouble(adds);
                double curValue = oldCurValue + addValue;
                if (curValue > maxValue) {
//                    ToastUtils.showShort("加料超出最大值");
//                    ToastUtils.setMsgTextSize(18);
//                    ToastUtils.setGravity(Gravity.CENTER, 0, 0);
                    MyToastUtil.showToastSnackBar(materialView, "当前值不应超出最大值", Snackbar.LENGTH_SHORT);
                    return;
                }
                if (curValue < alarm_value) {
//                    ToastUtils.showShort("加料超出最大值");
//                    ToastUtils.setMsgTextSize(18);
//                    ToastUtils.setGravity(Gravity.CENTER, 0, 0);
                    MyToastUtil.showToastSnackBar(materialView, "当前值不应小于报警量", Snackbar.LENGTH_SHORT);
                    return;
                }
                boolean isHigh = SharedPreferencesHelper.getBoolean(getMyActivity(), "isHigh", true);
                if (isHigh) {
                    handleAddSure();
                } else {

                    PowderPumpProtocal.startProtocal();
                }

                if (addMaterialPop != null) {
                    addMaterialPop.dismiss();
                }
                // ColdFoodSocket.addFeedReq(1, configAddress);
            } else if (id == R.id.btn_add_full) {

                int location = Integer.parseInt(mPumpConfigBean.getAddress());
                double oldCurValue = mPumpConfigBean.getCurValue();
                String materialName = mPumpConfigBean.getMaterials_name();
                int maxValue = Integer.parseInt(mPumpConfigBean.getMax_capacity());
                String showText = "桶" + location + "已添满" + materialName + ",值从" + oldCurValue + "增加到了" + maxValue;
//                ToastUtils.showShort("桶" + location + "已添满" + materialName + ",值从" + oldCurValue + "增加到了" + maxValue);
//                ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//                ToastUtils.setMsgTextSize(18);
                MyToastUtil.showToastSnackBar(materialView, showText, Snackbar.LENGTH_LONG);
                int index = mPumpConfigBeans.indexOf(mPumpConfigBean);
                mPumpConfigBean.setCurValue(maxValue);
                // correctConfigService.update(index, mPumpConfigBean);
                correctConfigService.update(mPumpConfigBean);
                getConfigList();
                bindAdapter();
                if (addMaterialPop != null) {
                    addMaterialPop.dismiss();
                }
            } else if (id == R.id.iv_close) {
                if (addMaterialPop != null) {
                    addMaterialPop.dismiss();
                }
            }
        }

        private void handleAddSure() {
            String adds = editAdd.getText().toString().trim();
            int location = Integer.parseInt(mPumpConfigBean.getAddress());
            double oldCurValue = mPumpConfigBean.getCurValue();
            String materialName = mPumpConfigBean.getMaterials_name();
            int maxValue = Integer.parseInt(mPumpConfigBean.getMax_capacity());
            int alarm_value = Integer.parseInt(mPumpConfigBean.getAlarm_value());
            double addValue = Double.parseDouble(adds);
            double curValue = oldCurValue + addValue;
            String showText = "桶" + location + "添加了" + addValue + ",值从" + oldCurValue + "增加到了" + curValue;
//                ToastUtils.showShort("桶" + location + "添加了" + addValue + ",值从" + oldCurValue + "增加到了" + curValue);
//                ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//                ToastUtils.setMsgTextSize(18);
            MyToastUtil.showToastSnackBar(materialView, showText, Snackbar.LENGTH_LONG);
            int index = mPumpConfigBeans.indexOf(mPumpConfigBean);
            String s = ComputeDoubleUtil.computeDouble(curValue);
            double curValue2 = Double.parseDouble(s);
            mPumpConfigBean.setCurValue(curValue2);
            //correctConfigService.update(index, mPumpConfigBean);
            correctConfigService.update(mPumpConfigBean);
            combineData();
            bindAdapter();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
