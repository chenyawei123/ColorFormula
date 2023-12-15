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
public class TopBottomPopAdapter extends SimpleAdapter<String> {
    private List<String> mDatas = new ArrayList<>();
    private Context mContext;
    private TextView textView;
    public TopBottomPopAdapter(Context context, List<String> datas){
        super(context, R.layout.pop_correct_method_item,datas);
        this.mContext = context;
        this.mDatas = datas;
    }
    @Override
    protected void convert(BaseViewHolder viewHoder, String item, int position) {
        textView = viewHoder.getTextView(R.id.textview);
        textView.setText(item);
    }
}
