package com.example.datacorrects.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.datacorrects.customView.BaseViewHolder;
import com.example.datacorrects.customView.SimpleAdapter;
import com.example.datacorrects.R;
import com.example.datacorrects.bean.TableCell;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 */

public class CorrectMethodAdapter extends SimpleAdapter<TableCell> {
    private final Context mContext;
    private TextView mTextItem;
    private List<TableCell> datas = new ArrayList<TableCell>();
    private TextView tvNo;
    private View line;
    public CorrectMethodAdapter(Context mContext,List<TableCell> datas){
        super(mContext, R.layout.item_table_content,datas);
        this.mContext = mContext;
        this.datas = datas;
    }
    @Override
    protected void convert(BaseViewHolder viewHoder, TableCell item, int position) {
        mTextItem = viewHoder.getTextView(R.id.text_content_item);

        line = viewHoder.getView(R.id.id_tv_line);
        mTextItem.setText(datas.get(position).getValue());
        tvNo = viewHoder.getTextView(R.id.id_tv_no);
        if(item.getType() ==2){
            tvNo.setVisibility(View.VISIBLE);
            //line.setVisibility(View.VISIBLE);
            tvNo.setText(String.valueOf(item.getRow()));
        }else{
            tvNo.setVisibility(View.GONE);
           // line.setVisibility(View.GONE);
        }



    }
}
