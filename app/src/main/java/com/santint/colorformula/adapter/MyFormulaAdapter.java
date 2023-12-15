package com.santint.colorformula.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cyw.mylibrary.bean.ManualFormula;
import com.cyw.mylibrary.customView.BaseAdapter;
import com.cyw.mylibrary.customView.BaseViewHolder;
import com.cyw.mylibrary.customView.SimpleAdapter;
import com.santint.colorformula.R;

import java.util.List;



/**
 * chenyawei
 */

public class MyFormulaAdapter extends SimpleAdapter<ManualFormula> implements BaseAdapter.OnItemClickListener {
    private final Context mContext;
    private TextView tvName;
    private int checkPos = -1;
    private LinearLayout linearLayout;


    public MyFormulaAdapter(Context mContext, List<ManualFormula> datas, RecyclerView ry) {
        super(mContext, R.layout.my_formula_item, datas);
        this.mContext = mContext;
        this.datas = datas;
        setOnItemClickListener(this);
    }


    @Override
    protected void convert(BaseViewHolder viewHoder, final ManualFormula item, final int position) {
        tvName = viewHoder.getTextView(R.id.tv_formula_name);
        tvName.setText(item.getFormulaName());
        linearLayout = viewHoder.getLinearLayout(R.id.linearlayout);
//        if (position == checkPos) {
//            tvName.setBackgroundColor(Color.parseColor("#f3f3f3"));
//            tvName.setTextColor(Color.parseColor("#0068cf"));
//        } else {
//            tvName.setBackgroundColor(Color.parseColor("#FFFFFF"));
//            tvName.setTextColor(Color.parseColor("#1e1d1d"));
//        }
        if(position == checkPos){
            tvName.setTextColor(Color.WHITE);
            //holder.tvLine.setVisibility(View.VISIBLE);
            //tvName.setBackgroundResource(R.drawable.bg_orange_orange_corner);
            linearLayout.setBackgroundResource(R.color.orange);
        }else{
            tvName.setTextColor(Color.BLACK);
            //holder.tvLine.setVisibility(View.GONE);
           // tvName.setBackgroundResource(R.drawable.bg_blue_blue_corner);
            linearLayout.setBackgroundResource(R.color.light_gray);
        }

    }
    public void setCheckPos(int pos){
        checkPos = pos;
        //notifyDataSetChanged();
        //notifyItemChanged(pos);
        notifyItemRangeChanged(0,datas.size());
    }

    @Override
    public void onItemClick(View view, int position) {
//        checkPos =position;
//        notifyDataSetChanged();
    }
}
