package com.example.datacorrects.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.datacorrects.customView.BaseViewHolder;
import com.example.datacorrects.customView.SimpleAdapter;
import com.example.datacorrects.R;
import com.example.datacorrects.bean.TableCell;

import java.util.ArrayList;
import java.util.List;

/**
 * chenyawei
 */

public class CorrectAdapter extends SimpleAdapter<TableCell> {
    private final Context mContext;
    private TextView mTextItem, tvNo;
    private List<TableCell> datas = new ArrayList<TableCell>();
    private LinearLayout linearLayout;

    public CorrectAdapter(Context mContext, List<TableCell> datas) {
        super(mContext, R.layout.correct_table_content,datas);
        this.mContext = mContext;
        this.datas = datas;
    }

    @Override
    protected void convert(BaseViewHolder viewHoder, TableCell item, int position) {
        mTextItem = viewHoder.getTextView(R.id.text_content_item);
        mTextItem.setText(datas.get(position).getValue());
        tvNo = viewHoder.getTextView(R.id.id_tv_no);
        if (item.getType() == 2) {
            tvNo.setVisibility(View.VISIBLE);
            //line.setVisibility(View.VISIBLE);
            tvNo.setText(String.valueOf(item.getRow()));
        } else {
            tvNo.setVisibility(View.GONE);
            // line.setVisibility(View.GONE);
        }
        linearLayout = viewHoder.getLinearLayout(R.id.id_linear_check);
    }

    public void setDatas(List<TableCell> tableCells) {
        this.datas = tableCells;
        //notifyItemRangeChanged(datas.size()-1,datas.size());
        notifyDataSetChanged();
    }

    public List<TableCell> getDatas() {
        return datas;
    }
}
