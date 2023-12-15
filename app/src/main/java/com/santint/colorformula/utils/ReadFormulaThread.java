package com.santint.colorformula.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.TaskInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTabHost;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.column.Column;
import com.blankj.utilcode.util.GsonUtils;
import com.cyw.mylibrary.application.AppApplication;
import com.cyw.mylibrary.bean.CannedBean;
import com.cyw.mylibrary.bean.ColorBean;
import com.cyw.mylibrary.bean.ColorDosBean;
import com.cyw.mylibrary.bean.Coloresult;
import com.cyw.mylibrary.bean.DDParamsBean;
import com.cyw.mylibrary.bean.FmParamsBean;
import com.cyw.mylibrary.bean.FormulaitemsBean;
import com.cyw.mylibrary.bean.ManualFormula;
import com.cyw.mylibrary.bean.MyFormula;
import com.cyw.mylibrary.bean.ProgressBean;
import com.cyw.mylibrary.bean.PumpConfigBean;
import com.cyw.mylibrary.bean.ResultBreak;
import com.cyw.mylibrary.bean.ResultData;
import com.cyw.mylibrary.netrequest.utils.NetApplication;
import com.cyw.mylibrary.netrequest.utils.PowderPumpProtocal;
import com.cyw.mylibrary.services.CannedResultService;
import com.cyw.mylibrary.services.ColorBeanService;
import com.cyw.mylibrary.services.DDService;
import com.cyw.mylibrary.services.FMServiceNew;
import com.cyw.mylibrary.services.MyFormulaService;
import com.cyw.mylibrary.services.PumpConfigBeanService;
import com.cyw.mylibrary.services.ResultBreakService;
import com.cyw.mylibrary.services.ResultDataService;
import com.cyw.mylibrary.util.Byte2Hex;
import com.cyw.mylibrary.util.ComputeDoubleUtil;
import com.cyw.mylibrary.util.DateFormatter;
import com.cyw.mylibrary.util.GlobalConstants;
import com.cyw.mylibrary.util.IOSDialog;
import com.cyw.mylibrary.util.MyActivityManager;
import com.cyw.mylibrary.util.SaveJsonFile;
import com.cyw.mylibrary.util.SharedPreferencesHelper;
import com.cyw.mylibrary.util.WriteLog;
import com.google.gson.reflect.TypeToken;
import com.net.netrequest.bean.MessageEventChuanCai;
import com.santint.colorformula.CorrectionActivity;
import com.santint.colorformula.R;
import com.santint.colorformula.commondialog.PopDialog;
import com.santint.colorformula.enums.BreakCodeEnum;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author： cyw
 */
public class ReadFormulaThread extends Thread {
    private Handler handler;
    private ImageView img;
    private TextView text;
    public static int santintIndex = -1;
    private final boolean isPause = false;

    private InputMethodManager imm;
    private FragmentTabHost mTabHost;
    private LayoutInflater mInflater;
    //private ManualOperation manualOperation;
    private final long exitTime = 0;
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
    private IOSDialog iosDialogEmegency;
    private IOSDialog iosDialogWarn;
    private MyFormulaService myFormulaService;
    private PumpConfigBeanService correctConfigService;
    private List<PumpConfigBean> mPumpConfigBeans = new ArrayList<>();
    private FMServiceNew fmService;
    private List<FmParamsBean> mFmParamsBeans = new ArrayList<>();

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

    ////////////////////////////////////////////////workstateactivity
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
    private ManualFormula manualFormula;
    private MyFormula myFormula;
    //    private List<ProgressBean> progressBeans = new ArrayList<>();
    private IOSDialog iosDialog, iosDialogBarrel, iosDialogEmpty, iosDialogPre;
    private String findBarrel = "";
    String errorMsg = "";
    private boolean canBack = false;
    private int currenDosIndex = 0;
    private int curTaskIndex = 0;
    private int curColorIndex = 0;
    private int colorBeanIndex = 0;


    private List<ColorBean> colorBeans = new ArrayList<>();
    private CannedBean cannedBean;
    //private CannedService cannedService;

    private String formulaName = "";
    private String mark = "";
    private Coloresult coloresult = new Coloresult();
    private List<Coloresult.AcutalQuantityBean.ColorantCodeBean> colorCodes = new ArrayList<>();
    Map<String, String> map = new HashMap<>();
    List<String> listkey = new ArrayList<>();
    List<String> listvalue = new ArrayList<>();
    private ColorBean curColorBean = null;
    boolean isCannedSuccess = false;
    int[] ints;
    private static final long PROGRESS_0_RATE = 2 * 1000 * 60;
    private long sendTime = 0L;
    private boolean isFirstProgress = true;
    private double firstValue = 0d;
    private List<ProgressBean> progressBeanList = new ArrayList<>();
    ////////////////////////////////////////////////workstateactivity
    private static ReadFormulaThread instance;
    private Context mContext;

    public Handler getHandler() {//注意哦，在run执行之前，返回的是null
        return handler;
    }

    public static ReadFormulaThread getInstance(Context context) {
        if (instance == null) {
            synchronized (ReadFormulaThread.class) {
                if (instance == null) {
                    instance = new ReadFormulaThread(context);
                }
            }
        }
        return instance;
    }

    public ReadFormulaThread(Context context) {
        mContext = context;

    }

    @Override
    public void run() {
        super.run();
        init();
        if (mPumpConfigBeans != null && mPumpConfigBeans.size() > 0 && mFmParamsBeans != null && mFmParamsBeans.size() > 0) {
            startCmd();
        }
        Looper.prepare();
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Bundle bundleRcv = (Bundle) msg.obj;
                MessageEventChuanCai messageEventChuanCai = (MessageEventChuanCai) bundleRcv.getSerializable("messageEventChuanCai");
                byte[] result = new byte[1024];
                byte[] result2 = messageEventChuanCai.getResult();
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
                    handleCmdMark();

                } else if (ints[1] == 7) {//调色机忙 020703
                    //////////////////////保存故障
                    resultBreak.setTime(DateFormatter.getCurrentTime());
                    resultBreak.setBreakCode(resultShi);
                    resultBreak.setBreakDes("E07:调色机忙");
                    ResultBreakService.getInstance().save(resultBreak);
                    //////////////////////保存故障
                    errorCodeDialog("E07:调色机忙");
                } else if (ints[1] == 8) { //校验和错误 020803
                    //////////////////////保存故障
                    resultBreak.setTime(DateFormatter.getCurrentTime());
                    resultBreak.setBreakCode(resultShi);
                    resultBreak.setBreakDes("E08:校验和错误");
                    ResultBreakService.getInstance().save(resultBreak);
                    //////////////////////保存故障
                    errorCodeDialog("E08:校验和错误");
                } else if (ints[1] == 9) {
                    if (ints[2] == 3) {

                    }
                } else if (ints[1] == 83) {/////////////////////////字符s
                    if (ints[2] == 70) {//字符F
                        if (ints[3] == 83) {//字符s
                            //////////SFS 罐装成功完成命令
                            handleCannedSuccess(chars);

                            //}
                        } else if (ints[3] == 80) {//字符p
                            /////////////SFP 进度反馈
                            handleWorkState(chars);

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
                                if (iosDialogEmegency != null) {
                                    iosDialogEmegency.dismiss();
                                }
                            } else {
                                if (iosDialog != null) {
                                    iosDialog.dismiss();
                                }
                                if (popDialog != null) {
                                    popDialog.dismiss();
                                }
                                if (isCannedSuccess) {//请移桶
                                    isCannedSuccess = false;
//                                    if (curTaskIndex < taskCount) {//多任务
//                                        startCmd2();
//                                        //tvStop.setText("继续");
//                                    } else {
////                                        curTaskIndex = 0;
////                                        tvStop.setText("完成");
//                                        canBack = true;
//                                    }
                                } else {//请放桶

                                }

                            }
                        }
                    }

                } else if (ints[1] == 69) {///////////////////////字符E
                    if (ints[2] == 71) {//字符G
                        if (ints[3] == 73) {//字符I
                            //////////////////EGI   未找到调色对应的桶
                            handleEGI(ints, chars);
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
                                errorCodeDialogEmegency(exceptionMsg, currentActivity);
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
                            handleCannedFail(chars);
                            if (ints[4] == 48 && ints[5] == 50) {//02 上位机主动终止罐装命令回复

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
                                    errorCodeDialogEmegency(exceptionMsg, currentActivity);
                                } else {
                                    if (exceptionMsg != null && exceptionMsg.length() > 0) {
                                        if (exceptionMsg.equals("E04:门未关")) {

                                        } else {

                                        }
                                        // minusDosCurValue();
                                        errorCodeDialog(exceptionMsg);
                                        exceptionMsg = "";
                                    }
                                }


                            }
                        }
                    }
                } else if (ints[1] == 24) { //心跳回复  16进制为18
                    NetApplication.inetrequestInterface.heartBack();

                }
            }
        };
        Looper.loop();
    }

    PopDialog popDialog;

    private void removeTongDialog(String errorMsg) {
        popDialog = new PopDialog(mContext, R.style.ios_dialog_style);
        //final View view = View.inflate(getMyActivity(), R.layout.pop_correct_method,null);
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            curColorBean = mManualColorBeans.get(index);
            String colorName = curColorBean.getColorNo();
            double dTarget = Double.parseDouble(curColorBean.getColorDos()) * scale;
            targetDos = ComputeDoubleUtil.computeDouble(dTarget);
            dCannedPrecision = colorDosBean.getCannedPrecision();
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

    private double calPre(double dTarget, String colorNo) {
        boolean isFloat = SharedPreferencesHelper.getBoolean(mContext, "isFloat", false);
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
                float floatValue = SharedPreferencesHelper.getFloat(mContext, "FloatValue", 0);
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

    private void sendCmdCanned() {
        if (mTong != null && mTong.length() > 0) {
            //if (mark.equals("canned")) {
//            double precision = Double.parseDouble(fmParamsBean.getCannedaPrecision());
//            double cannedTarget = Double.parseDouble(targetDos);
//            if (precision > cannedTarget) {//罐装目标量必须大于罐装精度阈值
////                ToastUtils.showShort("罐装目标量必须大于罐装精度阈值");
////                ToastUtils.setMsgTextSize(18);
//                //MyToastUtil.showToastSnackBar(decorView, "罐装目标量必须大于罐装精度阈值", Snackbar.LENGTH_LONG);
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
            int ddTargetDos = Integer.parseInt(mDdParamsBean.getDdTargetDos());
            long ddStep = 0;
            //if (ddSendIndex == 1) {
            ddStep = Long.parseLong(mDdParamsBean.getDdStep());
            //}
            long ddCycle = Long.parseLong(mDdParamsBean.getDdCycle());
            long ddDDBeforeStep = Long.parseLong(mDdParamsBean.getDdBeforeStep());
            // if (mark.equals("jogParam")) {
            PowderPumpProtocal.jogParaProtocal(tongLong, ddTargetDos, ddStep, ddCycle, ddDDBeforeStep);

            //  }
        }
    }

    private void sendCmdVal() {
        if (mTong != null && mTong.length() > 0) {
            //if (mark.equals("valVe")) {
            int ddCount = mFmParamsBean.getDdCount();
            PowderPumpProtocal.valVeActionProtocal(mTong, ddCount,mFmParamsBean);
            // }
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
            ProgressBean progressBean = progressBeanList.get(curColorIndex);
            progressBean.setRealValue(Double.parseDouble(finals));
            progressBean.setFinishState(true);
            //bindAdapterColorState();//单个色浆完成
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

            //updateData();
            curTaskIndex++;
            if (curTaskIndex < taskCount) {//多任务
                startCmd2();
                //backResult2();
                // tvStop.setText("继续");
            } else {
                curTaskIndex = 0;
                // tvStop.setText("完成");
                canBack = true;
                //String result = backResult2();
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
        //bindAdapterColorState();
        // updateData();
    }

    private void emptyCycleDialog(String errorMsg) {
        iosDialogEmpty = new IOSDialog.Builder(
                mContext)
                .setBtnColor(mContext.getResources().getColor(R.color.blue))
                .setColor(mContext.getResources().getColor(R.color.black))
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

    private void showDialogWarn(String errorMsg, Context currentActivity) {
        iosDialogWarn = new IOSDialog.Builder(
                currentActivity)
                .setBtnColor(mContext.getResources().getColor(R.color.blue))
                .setColor(mContext.getResources().getColor(R.color.blue))
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

    private void errorCodeDialogEmegency(String errorMsg, Activity currentActivity) {
        iosDialogEmegency = new IOSDialog.Builder(
                currentActivity)
                .setBtnColor(mContext.getResources().getColor(R.color.blue))
                .setColor(mContext.getResources().getColor(R.color.blue))
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

    private void errorCodeDialog(String errorMsg) {
        IOSDialog.Builder builder = new IOSDialog.Builder(
                mContext)
                .setBtnColor(mContext.getResources().getColor(R.color.blue))
                .setColor(mContext.getResources().getColor(R.color.blue))
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
        }else if(exception.equals("94")){
            exceptionMsg = BreakCodeEnum.ValveBreak.getDescription();
        }
        return exceptionMsg;
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
//        Bundle bundle = new Bundle();
//        bundle.putString("manualOperateTong", tongStr);
//        EventBus.getDefault().post(bundle);
    }

    private void handleFindBarrel() {
        if (isActivityTop(CorrectionActivity.class, mContext)) {
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
    }

    private void findBarrelDialog(String errorMsg) {
        iosDialogBarrel = new IOSDialog.Builder(
                mContext)
                .setBtnColor(mContext.getResources().getColor(R.color.blue))
                .setColor(mContext.getResources().getColor(R.color.black))
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
                // backResult2();
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
                colorBeans = manualFormula.getColorBeans();
                if (colorBeans != null && colorBeans.size() > 0) {
                    // for (int i = 0; i < taskCount; i++) {
                    for (int j = 0; j < colorBeans.size(); j++) {
                        ColorBean manualColorBean = colorBeans.get(j);
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
                colorBeans = getCommonColorBeans(myFormula);
                if (colorBeans != null && colorBeans.size() > 0) {
                    // for (int i = 0; i < taskCount; i++) {
                    for (int j = 0; j < colorBeans.size(); j++) {
                        ColorBean colorBean = colorBeans.get(j);
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

    private void init() {
        initService();
        getStandardColorBeans();
//        if (pumpConfigBeans != null && pumpConfigBeans.size() > 0 && fmParamsBeans != null && fmParamsBeans.size() > 0) {
//            startCmd();
//        }
    }

    private void initService() {
        myFormulaService = new MyFormulaService(mContext);
        correctConfigService = correctConfigService.getInstance();
        mPumpConfigBeans = correctConfigService.getAllListByType();
        fmService = FMServiceNew.getInstance();
        mFmParamsBeans = fmService.getAllListByType();
    }

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
//                        for (int j = 0; j < mPumpConfigBeans.size(); j++) {
//                            PumpConfigBean pumpConfigBean = mPumpConfigBeans.get(j);
//                            if (colorNo.equals(pumpConfigBean.getMaterials_name())) {
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
//                        if (address > 0 && !isContainsAddress(address) && materials_name != null && materials_name.length() > 0) {
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


    private void startCmd() {
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
            //MyToastUtil.showToastSnackBar(decorView, "请输入桶数", Snackbar.LENGTH_SHORT);
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
            double colorDosInt = 0;
            if (colorDos != null && colorDos.length() > 0) {
                colorDosInt = Double.parseDouble(colorDos);
                if (colorDosInt > 0) {
                    ColorDosBean colorDosBean = new ColorDosBean();
                    colorDosBean.setColorDos(colorDos);
                    colorDosBean.setPosition(i);
                    colorDosBean.setColorName(colorNo);
                    colorDosBean.setInnerCode(innerCode);
                    hasDosList.add(colorDosBean);
                }
            }
        }
        if (hasDosList == null || hasDosList.size() == 0) {
//                        ToastUtils.showShort("请输入色浆量");
//                        ToastUtils.setGravity(Gravity.CENTER,0,0);
//                        ToastUtils.setMsgTextSize(22);
            //MyToastUtil.showToastSnackBar(decorView, "请输入色浆量", Snackbar.LENGTH_SHORT);
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

            /////////////////////////计算精度
            double dTarget = Double.parseDouble(targetDos);
            mTong = "";
            double dCannedPrecision = calPre(dTarget, colorNo);//拿到桶号 和pumpconfigbean 以及罐装精度
            if (dCannedPrecision <= 0) {
                cannedPrecisionDes += " 桶号：" + mTong + " 物料编号:" + colorNo + " 目标值:" + dTarget;
            } else {
                colorDosBean.setBarrelNo(mTong);
                colorDosBean.setCannedPrecision(dCannedPrecision);
            }
            /////////////////////////计算精度
            if (mPumpConfigBeans != null && mPumpConfigBeans.size() > 0) {
                for (int k = 0; k < mPumpConfigBeans.size(); k++) {
                    PumpConfigBean pumpConfigBean = mPumpConfigBeans.get(k);
                    int alarm_value = Integer.parseInt(pumpConfigBean.getAlarm_value());
                    String tong = String.valueOf(pumpConfigBean.getAddress());
                    if (mTong.equals(tong)) {
                        if (Double.parseDouble(targetDos) * taskCount > pumpConfigBean.getCurValue()) {
                            leftWeightName += mTong + "号桶" + colorNo + "剩余" + pumpConfigBean.getCurValue();
                        }
                        if (alarm_value > pumpConfigBean.getCurValue()) {
                            leftAlarm += mTong + "号桶" + colorNo + "剩余" + pumpConfigBean.getCurValue();
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
            // MyToastUtil.showToastSnackBar(decorView, leftAlarm + ",剩余量不能小于报警量", Snackbar.LENGTH_SHORT);
            leftAlarm = "";
            return;
        }
        if (leftWeightName != null && leftWeightName.length() > 0) {
//                        ToastUtils.showLong(leftWeightName + ",剩余量不能小于注出量");
//                        ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//                        ToastUtils.setBgResource(R.color.gray);
//                        ToastUtils.setMsgTextSize(22);
            // MyToastUtil.showToastSnackBar(decorView, leftWeightName + ",剩余量不能小于注出量", Snackbar.LENGTH_SHORT);
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
            showDialogWarn(msg, mContext);
            cannedPrecisionDes = "";
            return;
        }
        formulaName = "配方打料";
        for (int i = 0; i < taskCount; i++) {
            handleIntent("manualOperate", i, scale);

        }
        startCmd2();

//        if (hasDosList != null && hasDosList.size() > 0) {
//            Intent intent = new Intent(CorrectionActivity.this, WorkStateActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putString("mark", "manualOperate");
//            bundle.putSerializable("hasDosList", (Serializable) hasDosList);
//            bundle.putSerializable("colorBeans", (Serializable) manualColorBeans);
//            // bundle.putSerializable("pumpConfigBeans", (Serializable) pumpConfigBeans);
//            bundle.putString("formulaName", "手动配料");
//            bundle.putInt("taskCount", taskCount);
//            bundle.putDouble("scale", 1);
//            intent.putExtras(bundle);
//            startActivityAni(intent);
//        }
    }


    private void startCmd2() {
        if (initCmd()) {
            task = 1;
            PowderPumpProtocal.startProtocal();
        }

    }
}
