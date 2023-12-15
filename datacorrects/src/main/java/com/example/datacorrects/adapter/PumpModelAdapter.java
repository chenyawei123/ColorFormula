package com.example.datacorrects.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.datacorrects.customView.BaseViewHolder;
import com.example.datacorrects.customView.SimpleAdapter;
import com.example.datacorrects.R;

import java.util.List;

/**
 *
 *
 */

public class PumpModelAdapter extends SimpleAdapter<String> {
    //接收到的已完成菜品信息
    List<String> mFoodOverData;
    private final Context mContext;
    private TextView textView;


    public List<String> getFoodOverData() {
        return mFoodOverData;
    }

    public void setFoodOverData(List<String> foodOverData) {
        mFoodOverData = foodOverData;
    }

    private View mInflater;
    //私有属性
    //private BaseAdapter.OnItemClickListener onItemClickListener = null;
    public PumpModelAdapter(Context mContext,List<String> datas){
        super(mContext, R.layout.pop_correct_method_item,datas);
        this.mContext = mContext;
        this.mFoodOverData = datas;
    }

    @Override
    protected void convert(BaseViewHolder viewHoder, String item, int position) {
        String foodOverData = mFoodOverData.get(position);
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
        textView.setText(foodOverData);
    }
}
