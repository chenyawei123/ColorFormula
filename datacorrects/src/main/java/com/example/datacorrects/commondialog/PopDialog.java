package com.example.datacorrects.commondialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.cyw.mylibrary.commondialog.MyDialog;
import com.example.datacorrects.R;
import com.example.datacorrects.adapter.PumpModelAdapter;
import com.example.datacorrects.bean.PumpSetBean;
import com.example.datacorrects.customView.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * author： cyw
 */
public class PopDialog extends MyDialog implements BaseAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private PumpModelAdapter mAdapter;
    private List<String> mDatas;
    private List<PumpSetBean> pumpSetBeans = new ArrayList<PumpSetBean>();
    private Context context;
    private PumpConfigListener pumpConfigListener;
    private TextView tvMessage;
    private LinearLayout linearLayout;
    private LinearLayout linearLine;

    public PopDialog(@NonNull Context context) {
        super(context);
    }

    public PopDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public PopDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public void initView(){
        tvMessage= findViewById(R.id.message);
        linearLayout = findViewById(R.id.linear);
        linearLayout.setVisibility(View.GONE);
        linearLine = findViewById(R.id.linear_line);
        linearLine.setVisibility(View.GONE);

    }
    public void setMessage(String msg){
        tvMessage.setText(msg);
    }

    public void setOnConfigListener(PumpConfigListener pumpConfigListener){
        this.pumpConfigListener = pumpConfigListener;
    }
    public interface PumpConfigListener{
        void onConfigListener(String config);
    }

    @Override
    public void onItemClick(View view, int position) {
        dismiss();
        String model = mDatas.get(position);
        pumpConfigListener.onConfigListener(model);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
