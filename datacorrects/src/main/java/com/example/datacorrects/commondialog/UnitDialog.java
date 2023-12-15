package com.example.datacorrects.commondialog;

import android.content.Context;
import android.view.View;

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
import com.example.datacorrects.view.PumpSetListener;

import java.util.ArrayList;
import java.util.List;


/**
 *
 *
 */

public class UnitDialog extends MyDialog implements View.OnClickListener, BaseAdapter.OnItemLongClickListener, BaseAdapter.OnItemClickListener  {
    private RecyclerView recyclerView;
    private PumpModelAdapter mAdapter;
    private List<String> mDatas;
    private final List<PumpSetBean> pumpSetBeans = new ArrayList<PumpSetBean>();
    private Context context;
    private PumpSetListener pumpSetListener;
    private int type = 0;


    public UnitDialog(@NonNull Context context) {
        super(context);
    }

    public UnitDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public UnitDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
    public void initView(){
        recyclerView = findViewById(R.id.recyclerview);
        mDatas = new ArrayList<String>();
        getRightList();
        bindAdapter();
    }

    private void getRightList() {
        if(mDatas!=null && mDatas.size()>0){
            mDatas.clear();
        }
        String unit = "ml";
        mDatas.add(unit);
        String unit2 = "g";
        mDatas.add(unit2);

    }
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


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {


        }
    }



    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }
    public void setOnUnitListener(PumpSetListener modelValueListener){
        this.pumpSetListener = modelValueListener;
    }

    @Override
    public void onItemClick(View view, int position) {
        dismiss();
        String model = mDatas.get(position);
        pumpSetListener.onUnitClick(type,model);
    }
}
