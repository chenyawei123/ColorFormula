package com.santint.colorformula.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cyw.mylibrary.base.BaseFragment;
import com.cyw.mylibrary.bean.ColorBean;
import com.cyw.mylibrary.bean.ManualFormula;
import com.cyw.mylibrary.customView.BaseAdapter;
import com.cyw.mylibrary.services.ManualFormulaService;
import com.santint.colorformula.R;
import com.santint.colorformula.TngFormulaDetailActivity;
import com.santint.colorformula.adapter.TngFormulaAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * author： cyw
 */
public class TngFormulaFragment extends BaseFragment {
    private RecyclerView ry;
    private GridLayoutManager mManager;
    private TngFormulaAdapter tngFormulaAdapter;
    private ManualFormulaService manualFormulaService;
    private List<ManualFormula> all = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tng_formula, container, false);
        ry = view.findViewById(R.id.fragment_ry_formula);
        initService();
        bindAdapter();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        all = manualFormulaService.getAll();
        bindAdapter();
    }

    private void initService() {
        manualFormulaService = new ManualFormulaService(getMyActivity());
        all = manualFormulaService.getAll();
    }

    private void bindAdapter() {
        mManager = new GridLayoutManager(getMyActivity(), 6);

//        mManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                return 2;
//            }
//        });
        ry.setLayoutManager(mManager);

        tngFormulaAdapter = new TngFormulaAdapter(getMyActivity(), all);
        tngFormulaAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                jumpDetail(position);
            }
        });
        ry.setAdapter(tngFormulaAdapter);
    }

    public void jumpDetail(int pos) {
        ManualFormula manualFormula = all.get(pos);
        List<ColorBean> colorBeans = manualFormula.getColorBeans();
        Intent intent = new Intent(getMyActivity(), TngFormulaDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("colorBeans", (Serializable) colorBeans);
        bundle.putSerializable("manualFormula", manualFormula);
        intent.putExtras(bundle);
        startActivityAni(intent);
    }
}
