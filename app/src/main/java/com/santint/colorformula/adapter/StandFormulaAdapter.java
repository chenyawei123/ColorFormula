package com.santint.colorformula.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cyw.mylibrary.bean.FormulaBean;
import com.cyw.mylibrary.bean.ManualOperateInfo;
import com.cyw.mylibrary.bean.MyFormula;
import com.cyw.mylibrary.customView.BaseViewHolder;
import com.cyw.mylibrary.customView.EdittextWithRightText;
import com.cyw.mylibrary.customView.SimpleAdapter;
import com.santint.colorformula.R;

import java.util.ArrayList;
import java.util.List;

/**
 * author： cyw
 */
public class StandFormulaAdapter extends SimpleAdapter<MyFormula> {
    private EdittextWithRightText editWeight;
    private TextView tvNo;
    private TextView tvNumber;
    private List<MyFormula> mDatas = new ArrayList<>();
    private Context mContext;
    private final int KEY_EDIT = 1;
    private String weight = "";
    private int pos = 0;
    private RelativeLayout relativeLayout;

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

    public StandFormulaAdapter(Context context, List<MyFormula> datas) {
        super(context, com.santint.colorformula.R.layout.stand_formula_item, datas);
        mDatas = datas;
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder viewHoder, MyFormula item, int position) {
        //View view = viewHoder.getView(R.layout.manual_operation_item);
        FormulaBean formula = item.getColorformula().getFormula();
        String description = formula.getDescription();
        String standardcolor = formula.getStandardcolor();
        tvNo = viewHoder.getTextView(R.id.tv_no);
        tvNo.setText(description);
        //tvNo.setBackgroundResource(Integer.parseInt(item.getColor()));
        relativeLayout = viewHoder.getRelativeLayout(R.id.relative);
        //String color = item.getColor();
//        int colorInt = Integer.parseInt(standardcolor);
//        relativeLayout.setBackgroundResource(colorInt);

    }
}
