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
import com.santint.colorformula.MyFormulaActivity;
import com.santint.colorformula.R;

import org.xutils.common.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * author： cyw
 */
public class MyFormulaColorAdapter extends SimpleAdapter<ColorBean> {
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

    public MyFormulaColorAdapter(Context context, List<ColorBean> datas) {
        super(context, R.layout.manual_operation_item, datas);
        mDatas = datas;
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder viewHoder, ColorBean item, int position) {
        View view = viewHoder.getView(R.layout.manual_operation_item);
        editWeight = viewHoder.getCustomEditText(R.id.edit_weight);
        editWeight.setWidth(0);
        editWeight.setFixedText("g");
        tvMaterialName = viewHoder.getTextView(R.id.tv_material_name);
//        int color = Integer.parseInt(item.getColor());
//        tvMaterialName.setBackgroundColor(color);
        tvNumber = viewHoder.getTextView(R.id.tv_number);
        editWeight.setFilters(new InputFilter[]{lendFilter});
        String weight = item.getColorDos();
        if (weight != null && weight.length() > 0) {
            editWeight.setText(weight);
        }

        if (editWeight.getTag() instanceof TextWatcher) {
            editWeight.removeTextChangedListener((TextWatcher) editWeight.getTag());
        }
        //  editWeight.setText(item.getWeight()+"");
        setTextChanged(item, position, editWeight);

        ViewGroup.LayoutParams params = tvMaterialName.getLayoutParams();
        if (item.getColorNo().length() <= 2) {
            params.width = DensityUtil.dip2px(100);
        } else if (item.getColorNo().length() <= 4) {
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

    private void setTextChanged(ColorBean item, int position, EditText editWeight) {
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                pos = position;
                weight = editable.toString();

                if(weight.equals(".")){
                    editWeight.setText("");
                }else if(weight.length()>1 && weight.substring(0,1).equals("0") && !weight.substring(1,2).equals(".")){
                    editWeight.setText("0");
                    editWeight.setSelection(weight.length());
                } else{
                    item.setColorDos(weight+"");
                    if(weight!=null && weight.length()>0){
                       // !weight.substring(weight.length()-1).equals(".") &&
                        if(!weight.equals("0")&& Double.parseDouble(weight)>0){
                            ((MyFormulaActivity)context).isChangedColorPaste(item);
                        }
                    }else{
                        ((MyFormulaActivity)context).isChangedColorPaste(item);
                    }

                }
            }
        };
        editWeight.addTextChangedListener(watcher);
        editWeight.setTag(watcher);
    }
}
