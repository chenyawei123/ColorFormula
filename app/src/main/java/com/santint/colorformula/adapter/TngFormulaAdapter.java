package com.santint.colorformula.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cyw.mylibrary.bean.ManualFormula;
import com.cyw.mylibrary.bean.ManualOperateInfo;
import com.cyw.mylibrary.customView.BaseViewHolder;
import com.cyw.mylibrary.customView.EdittextWithRightText;
import com.cyw.mylibrary.customView.SimpleAdapter;
import com.santint.colorformula.R;

import java.util.ArrayList;
import java.util.List;

/**
 * author： cyw
 */
public class TngFormulaAdapter extends SimpleAdapter<ManualFormula> {
    private EdittextWithRightText editWeight;
    private TextView tvNo;
    private TextView tvNumber;
    private List<ManualFormula> mDatas = new ArrayList<>();
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

    public TngFormulaAdapter(Context context, List<ManualFormula> datas) {
        super(context, R.layout.tng_formula_item, datas);
        mDatas = datas;
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder viewHoder, ManualFormula item, int position) {
        //View view = viewHoder.getView(R.layout.manual_operation_item);
        tvNo = viewHoder.getTextView(R.id.tv_no);
        tvNo.setText(item.getFormulaName());
        //tvNo.setBackgroundResource(Integer.parseInt(item.getColor()));
        relativeLayout = viewHoder.getRelativeLayout(R.id.relative);
       // relativeLayout.setBackgroundResource(Integer.parseInt(item.getColor()));

    }

}
