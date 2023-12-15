package com.example.datacorrects.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.datacorrects.customView.BaseViewHolder;
import com.example.datacorrects.customView.SimpleAdapter;
import com.example.datacorrects.R;

import java.util.List;


public class MainFeedAdapter extends SimpleAdapter<String> {
    //接收到的已完成菜品信息
    List<String> mFoodOverData;
    private final Context mContext;
    private TextView tvValue;


    public List<String> getFoodOverData() {
        return mFoodOverData;
    }

    public void setFoodOverData(List<String> foodOverData) {
        mFoodOverData = foodOverData;
    }

    private View mInflater;
    //私有属性
    private OnItemClickListener onItemClickListener = null;
    private OnItemLongClickListener onItemLongClickListener = null;
    public MainFeedAdapter(Context mContext,List<String> datas){
        super(mContext, R.layout.feed_dialog_item,datas);
        this.mContext = mContext;
        this.mFoodOverData = datas;
    }

    //setter方法
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener){
        this.onItemLongClickListener= onItemLongClickListener;
    }

    //回调接口
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    //长按回调接口
    public interface  OnItemLongClickListener{
        void onItemLongClick(View view,int position);
    }

    @Override
    protected void convert(BaseViewHolder viewHoder, String item, int position) {
        String foodOverData = mFoodOverData.get(position);
        tvValue = viewHoder.getTextView(R.id.id_tv_value);
        tvValue.setText(foodOverData);


//        //添加点击事件
//        itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (onItemClickListener != null) {
//                    onItemClickListener.onItemClick(view, position);
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mFoodOverData==null?0:mFoodOverData.size();
    }

}
