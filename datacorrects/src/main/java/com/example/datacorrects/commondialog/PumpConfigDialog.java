package com.example.datacorrects.commondialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
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
public class PumpConfigDialog extends MyDialog implements BaseAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private PumpModelAdapter mAdapter;
    private List<String> mDatas;
    private List<PumpSetBean> pumpSetBeans = new ArrayList<PumpSetBean>();
    private Context context;
    private PumpConfigListener pumpConfigListener;
    private TextView tvPumpConfig;
    public PumpConfigDialog(@NonNull Context context) {
        super(context);
    }

    public PumpConfigDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public PumpConfigDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public void initView(){
        recyclerView = findViewById(R.id.recyclerview);
        mDatas = new ArrayList<String>();
        //getPumpConfigList();
        //bindAdapter();
    }
//    private void getPumpConfigList() {
//        CorrectSettingService correctSettingService = new CorrectSettingService(context);
//        pumpSetBeans = correctSettingService.getAll();
//        if(pumpSetBeans!=null && pumpSetBeans.size()>0){
//            for (int i = 0; i < pumpSetBeans.size(); i++) {
//                PumpSetBean pumpSetBean = pumpSetBeans.get(i);
//                String modelBean = pumpSetBean.getName();
//                mDatas.add(modelBean);
//            }
//        }
//
//    }
    private void bindAdapter() {
        mAdapter = new PumpModelAdapter(getContext(), mDatas);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mAdapter.setOnItemClickListener(this);
//        mDecoration = new ItemHeaderDecoration(getContext(), mDatas);
//        rvMain.addItemDecoration(mDecoration);
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
