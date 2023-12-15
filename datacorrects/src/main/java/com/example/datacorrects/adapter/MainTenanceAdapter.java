package com.example.datacorrects.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.datacorrects.R;
import com.example.datacorrects.bean.MainTenanceBean;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;


public class MainTenanceAdapter extends RecyclerView.Adapter<MainTenanceAdapter.AdapterRYViewHolder>  {
    //接收到的已完成菜品信息
    List<MainTenanceBean> mFoodOverData;
    private final Context mContext;


    public List<MainTenanceBean> getFoodOverData() {
        return mFoodOverData;
    }

    public void setFoodOverData(List<MainTenanceBean> foodOverData) {
        mFoodOverData = foodOverData;
    }

    private View mInflater;
    //私有属性
    private OnItemClickListener onItemClickListener = null;
    private OnItemLongClickListener onItemLongClickListener = null;
    public MainTenanceAdapter(Context mContext,List<MainTenanceBean> datas){
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
        mInflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.maintenance_item,parent,false);
        AdapterRYViewHolder ryViewHolder = new AdapterRYViewHolder(mInflater);
        return ryViewHolder;
    }

    @Override
    public void onBindViewHolder(final AdapterRYViewHolder holder, final int position) {
        MainTenanceBean foodOverData = mFoodOverData.get(position);
        ViewGroup.LayoutParams layoutParams2 = (ViewGroup.LayoutParams)holder.rel.getLayoutParams();
        int parentHeight = layoutParams2.height;
        double changeValue = foodOverData.getChangeValue();
        int maxValue = foodOverData.getMax();
        String  type = foodOverData.getType();//料名称
        if(changeValue>0){
            ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) holder.tvProgress.getLayoutParams();
            BigDecimal b = new BigDecimal((float)(changeValue*parentHeight/maxValue));
            Double ratio = b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
            int height = 0;
            if(ratio<=1){
                height = 1;
            }else{
                height =ratio.intValue() ;
            }

            layoutParams.height = height;//
           // layoutParams.width = DensityUtil.dip2px(30);

            holder.tvProgress.setLayoutParams(layoutParams);
        }


        holder.tvMaxValue.setText("-"+foodOverData.getMax());
        holder.tvType.setText(foodOverData.getType());
        holder.tvMainId.setText(foodOverData.getId()+"号桶");


        //添加点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(view, position);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(onItemLongClickListener!=null){
                    onItemLongClickListener.onItemLongClick(view,position);
                }
                return true;//返回true，长按事件生效
            }
        });
    }
    public static String formatDouble4(double d) {
        DecimalFormat df = new DecimalFormat("#0.000");
        return df.format(d);
    }


    @Override
    public int getItemCount() {
        return mFoodOverData==null?0:mFoodOverData.size();
    }

    class AdapterRYViewHolder extends RecyclerView.ViewHolder {
        TextView tvProgress;
        TextView tvType;
        TextView tvMainId;
        TextView tvMaxValue;
        RelativeLayout rel;
        public AdapterRYViewHolder(View itemView) {
            super(itemView);
            tvProgress = itemView.findViewById(R.id.id_progressbar);
            tvType = itemView.findViewById(R.id.id_tv_type);
            tvMainId  = itemView.findViewById(R.id.id_tv_main_id);
            tvMaxValue = itemView.findViewById(R.id.tv_max_value);
            rel = itemView.findViewById(R.id.id_rel);
        }
    }
}
