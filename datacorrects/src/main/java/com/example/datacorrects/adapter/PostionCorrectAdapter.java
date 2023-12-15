package com.example.datacorrects.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.cyw.mylibrary.bean.PumpConfigBean;
import com.example.datacorrects.R;
import com.example.datacorrects.customView.BaseViewHolder;
import com.example.datacorrects.customView.SimpleAdapter;

import java.util.List;


public class PostionCorrectAdapter extends SimpleAdapter<PumpConfigBean> {
    //接收到的已完成菜品信息
    List<PumpConfigBean> mFoodOverData;
    private final Context mContext;
    private TextView textView;


    public List<PumpConfigBean> getFoodOverData() {
        return mFoodOverData;
    }

    public void setFoodOverData(List<PumpConfigBean> foodOverData) {
        mFoodOverData = foodOverData;
    }

    private View mInflater;
    public PostionCorrectAdapter(Context mContext,List<PumpConfigBean> datas){
        super(mContext, R.layout.pop_correct_method_item,datas);
        this.mContext = mContext;
        this.mFoodOverData = datas;
    }


    @Override
    protected void convert(BaseViewHolder viewHoder, PumpConfigBean item, int position) {
        PumpConfigBean foodOverData = mFoodOverData.get(position);
        textView = viewHoder.getTextView(R.id.textview);
        textView.setText(String.valueOf(foodOverData.getAddress()));
    }

    @Override
    public int getItemCount() {
        return mFoodOverData==null?0:mFoodOverData.size();
    }

}
