package com.example.datacorrects.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.datacorrects.customView.BaseViewHolder;
import com.example.datacorrects.customView.SimpleAdapter;
import com.example.datacorrects.R;
import com.example.datacorrects.bean.CorrectPlanBean;

import java.util.List;


public class PopCorrectAdapter extends SimpleAdapter<CorrectPlanBean> {
    //接收到的已完成菜品信息
    List<CorrectPlanBean> mFoodOverData;
    private final Context mContext;
    private TextView textView;


    public List<CorrectPlanBean> getFoodOverData() {
        return mFoodOverData;
    }

    public void setFoodOverData(List<CorrectPlanBean> foodOverData) {
        mFoodOverData = foodOverData;
    }

    private View mInflater;
    //私有属性
    private final OnItemClickListener onItemClickListener = null;
    public PopCorrectAdapter(Context mContext,List<CorrectPlanBean> datas){
        super(mContext, R.layout.pop_correct_method_item,datas);
        this.mContext = mContext;
        this.mFoodOverData = datas;
    }

    @Override
    protected void convert(BaseViewHolder viewHoder, CorrectPlanBean item, int position) {
        CorrectPlanBean foodOverData = mFoodOverData.get(position);
//        ShowFoodData showFoodData = new Gson().fromJson(foodOverData, ShowFoodData.class);
//        String allCode = showFoodData.getAllCode();
//        String caiID = showFoodData.getCaiID();
//        final String tableName = showFoodData.getTableName();
//        String orderID = showFoodData.getOrderID();
//        String caiJdIndex = showFoodData.getCaiJdIndex();
//        String tableID = showFoodData.getTableID();
//        String foodName = showFoodData.getFoodName();
//        holder.mTvFoodName.setText(foodName);
//        holder.mTvTableName.setText(tableName);
//        DaYinUtils.dayin(tableName, foodName, orderID, caiID);
        textView = viewHoder.getTextView(R.id.textview);
        textView.setText(foodOverData.getMethod_name());
    }

}
