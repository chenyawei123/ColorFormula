package com.santint.colorformula.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.cyw.mylibrary.bean.ColorBean;
import com.cyw.mylibrary.bean.ManualOperateInfo;
import com.cyw.mylibrary.customView.BaseViewHolder;
import com.cyw.mylibrary.customView.EdittextWithRightText;
import com.cyw.mylibrary.customView.SimpleAdapter;
import com.santint.colorformula.R;
import com.santint.colorformula.StandFormulaEditActivity;

import org.xutils.common.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author： cyw
 */
public class StandEditAdapter extends SimpleAdapter<ColorBean> {
    private EdittextWithRightText editWeight;
    private TextView tvMaterialName;
    private TextView tvNumber;
    private List<ColorBean> mDatas = new ArrayList<>();
    private Context mContext;
    private final int KEY_EDIT = 1;
    private String weight = "";
    private int pos = 0;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (KEY_EDIT == msg.what) {
                Bundle bundle = (Bundle) msg.obj;
                ManualOperateInfo item = (ManualOperateInfo) bundle.getSerializable("item");
                editWeight.setText(weight);
                item.setWeight(weight);
                notifyItemChanged(pos);
            }
        }
    };

    public StandEditAdapter(Context context, List<ColorBean> datas) {
        super(context, R.layout.manual_operation_item, datas);
        mDatas = datas;
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder viewHoder, ColorBean item, int position) {
        View view = viewHoder.getView(R.layout.manual_operation_item);
        editWeight = viewHoder.getCustomEditText(R.id.edit_weight);
        editWeight.setText(item.getColorDos());
        editWeight.setWidth(0);
        editWeight.setFixedText("g");
        // editWeight.setFilters(new InputFilter[]{lendFilter});
        tvMaterialName = viewHoder.getTextView(R.id.tv_material_name);
//        int color = Integer.parseInt(item.getColor());
//        tvMaterialName.setBackgroundColor(color);
        tvNumber = viewHoder.getTextView(R.id.tv_number);
        editWeight.setFilters(new InputFilter[]{lendFilter});

        if(editWeight.getTag() instanceof TextWatcher){
            editWeight.removeTextChangedListener((TextWatcher)editWeight.getTag());
        }

        setTextChanged(item,position,editWeight);
        ViewGroup.LayoutParams params = tvMaterialName.getLayoutParams();
        if(item.getColorNo().length()<=2){
            params.width = DensityUtil.dip2px(100);
        }else if(item.getColorNo().length()<=4){
            params.width = DensityUtil.dip2px(140);
        }
        tvMaterialName.setText(item.getColorNo());
        //tvNumber.setText(String.valueOf(position + 1));
        // tvNumber.setText(String.valueOf(item.getAddress()));
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
    private  void setTextChanged(ColorBean item, int position, EditText editWeight){
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //if (!TextUtils.isEmpty(editable)) {
                pos = position;
                weight = editable.toString();

                if(weight.equals(".")){
                    editWeight.setText("");
                }else if(weight.length()>1 && weight.substring(0,1).equals("0") && !weight.substring(1,2).equals(".")){
                    editWeight.setText("0");
                    editWeight.setSelection(weight.length());
                } else{
                    item.setColorDos(weight+"");
                    if(weight!=null && weight.length()>0 ){
                        //!weight.substring(weight.length()-1).equals(".") &&
                        if(!weight.equals("0") && Double.parseDouble(weight)>0){
                            ((StandFormulaEditActivity)context).isChangedColorPaste(item);
                        }

                    }else{
                        ((StandFormulaEditActivity)context).isChangedColorPaste(item);
                    }
                }

            }
        };
        editWeight.addTextChangedListener(watcher);
        editWeight.setTag(watcher);
    }

    public class EditInputFilter implements InputFilter{
        /**
         * 最大数字
         */
        public static final int MAX_VALUE = 10000;
        /**
         * 小数点后的数字的位数
         */
        public static final int PONTINT_LENGTH = 2;
        Pattern p;
        public EditInputFilter(){
            p = Pattern.compile("[0-9]*");   //除数字外的其他的
        }

        /**
         *  source    新输入的字符串
         *  start    新输入的字符串起始下标，一般为0
         *  end    新输入的字符串终点下标，一般为source长度-1
         *  dest    输入之前文本框内容
         *  dstart    原内容起始坐标，一般为0
         *  dend    原内容终点坐标，一般为dest长度-1
         */

        @Override
        public CharSequence filter(CharSequence src, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            String oldtext =  dest.toString();
            System.out.println(oldtext);
            //验证删除等按键
            if ("".equals(src.toString())) {
                return null;
            }
            //验证非数字或者小数点的情况
            Matcher m = p.matcher(src);
            if(oldtext.contains(".")){
                //已经存在小数点的情况下，只能输入数字
                if(!m.matches()){
                    return null;
                }
            }else{
                //未输入小数点的情况下，可以输入小数点和数字
                if(!m.matches() && !src.equals(".") ){
                    return null;
                }
            }
            //验证输入金额的大小
            if(!src.toString().equals("")){
                double dold = Double.parseDouble(oldtext+src.toString());
                if(dold > MAX_VALUE){
                    // CustomerToast.showToast(RechargeActivity.this, "输入的最大金额不能大于MAX_VALUE");
                    return dest.subSequence(dstart, dend);
                }else if(dold == MAX_VALUE){
                    if(src.toString().equals(".")){
                        // CustomerToast.showToast(RechargeActivity.this, "输入的最大金额不能大于MAX_VALUE");
                        return dest.subSequence(dstart, dend);
                    }
                }
            }
            //验证小数位精度是否正确
            if(oldtext.contains(".")){
                int index = oldtext.indexOf(".");
                int len = dend - index;
                //小数位只能2位
                if(len > PONTINT_LENGTH){
                    CharSequence newText = dest.subSequence(dstart, dend);
                    return newText;
                }
            }
            return dest.subSequence(dstart, dend) +src.toString();
        }
    }
}
