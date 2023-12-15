package com.example.datacorrects.adapter;

import android.content.Context;
import android.widget.TextView;

import com.example.datacorrects.customView.BaseViewHolder;
import com.example.datacorrects.customView.SimpleAdapter;
import com.example.datacorrects.R;

import java.util.ArrayList;
import java.util.List;

/**
 * author： cyw
 */
public class TouTypePopAdapter extends SimpleAdapter<String> {
    private List<String> datas = new ArrayList<>();
    private Context mContext;
    private TextView tvOperate;
    public TouTypePopAdapter(Context context, int layoutResId) {
        super(context, layoutResId);
    }

    public TouTypePopAdapter(Context context, List<String> datas) {
        super(context, R.layout.pop_long_dialog_item, datas);
        this.mContext = context;
        this.datas =datas;
    }

    @Override
    protected void convert(BaseViewHolder viewHoder, String item, int position) {
        tvOperate = viewHoder.getTextView(R.id.textView);
        tvOperate.setText(item);
    }
}
