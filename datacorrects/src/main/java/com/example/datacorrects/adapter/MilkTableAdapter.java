package com.example.datacorrects.adapter;

import android.content.Context;
import android.widget.TextView;

import com.example.datacorrects.customView.BaseViewHolder;
import com.example.datacorrects.customView.SimpleAdapter;
import com.example.datacorrects.R;
import com.example.datacorrects.bean.TableCell;

import java.util.ArrayList;
import java.util.List;

/**
 * author： cyw
 *
 */
public class MilkTableAdapter extends SimpleAdapter<TableCell> {
    private final Context mContext;
    private TextView mTextItem;
    private List<TableCell> datas = new ArrayList<TableCell>();
    public MilkTableAdapter(Context mContext,List<TableCell> datas){
        super(mContext, R.layout.item_table_content,datas);
        this.mContext = mContext;
        this.datas = datas;
    }
    @Override
    protected void convert(BaseViewHolder viewHoder, TableCell item, int position) {
        mTextItem = viewHoder.getTextView(R.id.text_content_item);
        mTextItem.setText(datas.get(position).getValue());
    }
}
