package com.example.datacorrects.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.datacorrects.R;
import com.example.datacorrects.bean.MainLongBean;

import java.util.List;


public class MainLongAdapter extends RecyclerView.Adapter<MainLongAdapter.AdapterRYViewHolder>  {
    //接收到的已完成菜品信息
    List<MainLongBean> mFoodOverData;
    private final Context mContext;


    public List<MainLongBean> getFoodOverData() {
        return mFoodOverData;
    }

    public void setFoodOverData(List<MainLongBean> foodOverData) {
        mFoodOverData = foodOverData;
    }

    private View mInflater;
    //私有属性
    private OnItemClickListener onItemClickListener = null;
    private OnItemLongClickListener onItemLongClickListener = null;
    public MainLongAdapter(Context mContext,List<MainLongBean> datas){
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
    public AdapterRYViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.pop_long_dialog_item,parent,false);
        AdapterRYViewHolder ryViewHolder = new AdapterRYViewHolder(mInflater);
        return ryViewHolder;
    }

    @Override
    public void onBindViewHolder(final AdapterRYViewHolder holder, final int position) {
        MainLongBean foodOverData = mFoodOverData.get(position);
        holder.tvStep.setText(foodOverData.getStep());


        //添加点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(view, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFoodOverData==null?0:mFoodOverData.size();
    }

    class AdapterRYViewHolder extends RecyclerView.ViewHolder {
        TextView tvStep;
        public AdapterRYViewHolder(View itemView) {
            super(itemView);
            tvStep = itemView.findViewById(R.id.textView);

        }
    }
}
