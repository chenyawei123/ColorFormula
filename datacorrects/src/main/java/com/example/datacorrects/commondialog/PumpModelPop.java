package com.example.datacorrects.commondialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datacorrects.customView.BaseAdapter;
import com.example.datacorrects.R;
import com.example.datacorrects.adapter.PumpModelAdapter;
import com.example.datacorrects.bean.PumpSetBean;
import com.example.datacorrects.view.PumpSetListener;

import java.util.ArrayList;
import java.util.List;

import popup.BasePopup.BasePopupWindow;


/**
 *
 *
 */

public class PumpModelPop extends BasePopupWindow implements View.OnClickListener, BaseAdapter.OnItemLongClickListener, BaseAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private PumpModelAdapter mAdapter;
    private List<String> mDatas;
    private final List<PumpSetBean> pumpSetBeans = new ArrayList<PumpSetBean>();
    private Context context;
    private PumpSetListener pumpSetListener;


    public PumpModelPop(Context context, int width, int height) {
        super(context, width, height);

    }

    public PumpModelPop(Context context) {
        super(context);
    }

    @Override
    public View onCreateContentView() {
        //View view = createPopupById(R.layout.pop_correct_method);
        LayoutInflater layoutInflater= (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.pop_correct_method,null);
        recyclerView = view.findViewById(R.id.recyclerview);
        mDatas = new ArrayList<String>();
        getRightList();
        bindAdapter();
        return view;
    }

    private void getRightList() {
//        CorrectSettingService correctSettingService = new CorrectSettingService(context);
//        pumpSetBeans = correctSettingService.getAll();
//        if(pumpSetBeans!=null && pumpSetBeans.size()>0){
//            for (int i = 0; i < pumpSetBeans.size(); i++) {
//                PumpSetBean pumpSetBean = pumpSetBeans.get(i);
//                String modelBean = pumpSetBean.getModel();
//                mDatas.add(modelBean);
//            }
//        }
        String modelBean = "液料泵";
        mDatas.add(modelBean);
        String modelBean2 = "流量计泵";
        mDatas.add(modelBean2);
        String modelBean3 = "5L泵";
        mDatas.add(modelBean3);
        String modelBean4 = "粉料泵";
        mDatas.add(modelBean4);
//        String modelBean5 = "投料";
//        mDatas.add(modelBean5);

    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {


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
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }
    public void setOnModelListener(PumpSetListener modelValueListener){
        this.pumpSetListener = modelValueListener;
    }

    @Override
    public void onItemClick(View view, int position) {
        dismiss();
        String model = mDatas.get(position);
        pumpSetListener.onModelClick(model);
    }
}
