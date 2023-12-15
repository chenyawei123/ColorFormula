package com.example.datacorrects.commondialog;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datacorrects.customView.BaseAdapter;
import com.example.datacorrects.R;
import com.example.datacorrects.adapter.PumpModelAdapter;
import com.example.datacorrects.bean.PumpSetBean;


import java.util.ArrayList;
import java.util.List;

import popup.BasePopup.BasePopupWindow;


/**
 *
 *
 */

public class PumpConfigPop extends BasePopupWindow implements View.OnClickListener, BaseAdapter.OnItemLongClickListener, BaseAdapter.OnItemClickListener  {
    private RecyclerView recyclerView;
    private PumpModelAdapter mAdapter;
    private List<String> mDatas;
    private List<PumpSetBean> pumpSetBeans = new ArrayList<PumpSetBean>();
    private Context context;
    private PumpConfigListener pumpConfigListener;


    public PumpConfigPop(Context context, int width, int height) {
        super(context, width, height);
        this.context = context;

    }

    public PumpConfigPop(Context context) {
        super(context);
    }

    @Override
    public View onCreateContentView() {
        View view = createPopupById(R.layout.pop_correct_method);
        recyclerView = view.findViewById(R.id.recyclerview);
        mDatas = new ArrayList<String>();
        getPumpConfigList();
        bindAdapter();
        return view;
    }

    private void getPumpConfigList() {

        if(pumpSetBeans!=null && pumpSetBeans.size()>0){
            for (int i = 0; i < pumpSetBeans.size(); i++) {
                PumpSetBean pumpSetBean = pumpSetBeans.get(i);
                String modelBean = pumpSetBean.getName();
                mDatas.add(modelBean);
            }
        }

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
}
