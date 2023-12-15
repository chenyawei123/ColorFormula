package com.santint.colorformula.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cyw.mylibrary.bean.ManualOperateInfo;
import com.cyw.mylibrary.customView.BaseViewHolder;
import com.cyw.mylibrary.customView.EdittextWithRightText;
import com.cyw.mylibrary.customView.SimpleAdapter;
import com.santint.colorformula.R;
import com.santint.colorformula.bean.LeftBean;

import java.util.ArrayList;
import java.util.List;

/**
 * author： cyw
 */
public class MainActivityLeftAdapter extends SimpleAdapter<LeftBean> {
    private EdittextWithRightText editWeight;
    private TextView tvNo;
    private TextView tvNumber;
    private List<LeftBean> mDatas = new ArrayList<>();
    private Context mContext;
    private final int KEY_EDIT = 1;
    private String weight = "";
    private int pos = 0;
    private LinearLayout linearLayout;

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

    public MainActivityLeftAdapter(Context context, List<LeftBean> datas) {
        super(context, R.layout.left_adapter_item, datas);
        mDatas = datas;
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder viewHoder, LeftBean item, int position) {
        //View view = viewHoder.getView(R.layout.manual_operation_item);
        tvNo = viewHoder.getTextView(R.id.tv_no);
        tvNo.setText(item.getName());
        linearLayout = viewHoder.getLinearLayout(R.id.linearlayout);
        if(datas.get(position).isCheck()){
            tvNo.setTextColor(Color.WHITE);
            linearLayout.setBackgroundResource(R.color.orange);
            //holder.tvLine.setVisibility(View.VISIBLE);
            //tvNo.setBackgroundResource(R.drawable.bg_orange_orange_corner);
        }else{
            tvNo.setTextColor(Color.BLACK);
            linearLayout.setBackgroundResource(R.color.light_gray);
            //holder.tvLine.setVisibility(View.GONE);
           // tvNo.setBackgroundResource(R.drawable.bg_blue_blue_corner);
        }
//        tvNo.setText(item.getFormulaNo());
//        tvNo.setBackgroundResource(Integer.parseInt(item.getColor()));

    }
}
