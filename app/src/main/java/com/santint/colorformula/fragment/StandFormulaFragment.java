package com.santint.colorformula.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.GsonUtils;
import com.cyw.mylibrary.base.BaseFragment;
import com.cyw.mylibrary.bean.ColorBean;
import com.cyw.mylibrary.bean.FormulaitemsBean;
import com.cyw.mylibrary.bean.MyFormula;
import com.cyw.mylibrary.customView.BaseAdapter;
import com.cyw.mylibrary.services.MyFormulaService;
import com.cyw.mylibrary.util.GlobalConstants;
import com.cyw.mylibrary.util.WriteLog;
import com.google.gson.reflect.TypeToken;
import com.santint.colorformula.R;
import com.santint.colorformula.StandFormulaDetailActivity;
import com.santint.colorformula.adapter.StandFormulaAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * author： cyw
 */
public class StandFormulaFragment extends BaseFragment {
    private RecyclerView ry;
    private GridLayoutManager mManager;
    private StandFormulaAdapter standFormulaAdapter;
    private MyFormulaService myFormulaService;
    private List<MyFormula> all = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stand_formula, container, false);
        ry = view.findViewById(R.id.fragment_ry_formula);
        initService();
        bindAdapter();
        return view;
    }

    private void initService() {
        myFormulaService = new MyFormulaService(getMyActivity());
        all = myFormulaService.getAll();
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

        standFormulaAdapter = new StandFormulaAdapter(getMyActivity(), all);
        standFormulaAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                jumpDetail(position);
            }
        });
        ry.setAdapter(standFormulaAdapter);
    }

    public void jumpDetail(int pos) {
        MyFormula myFormula = all.get(pos);
        List<ColorBean> colorBeans = new ArrayList<>();

        colorBeans.addAll(getCommonColorBeans(myFormula));
        Intent intent = new Intent(getMyActivity(), StandFormulaDetailActivity.class);
        Bundle bundle = new Bundle();
        WriteLog.writeTxtToFile("colorbeansize------------------------" + colorBeans.size());
        bundle.putSerializable("colorBeans", (Serializable) colorBeans);
        bundle.putSerializable("myFormula", myFormula);
        bundle.putInt("formulaIndex", pos);
        intent.putExtras(bundle);
        startActivityAni(intent);
    }

    private List<ColorBean> getCommonColorBeans(MyFormula myFormula) {
        List<ColorBean> colorBeans = new ArrayList<>();
        if (myFormula.getColorformula() != null) {
            FormulaitemsBean formulaitems = myFormula.getColorformula().getFormulaitems();
            if (formulaitems != null) {
//                if (formulaitems instanceof FormulaitemsBean) {
//                    colorBeans = ((FormulaitemsBean) formulaitems).getColorBeans();
//                } else if (formulaitems instanceof FormulaitemsBean2) {
//                    ColorBean colorBean = ((FormulaitemsBean2) formulaitems).getColorBean();
//                    colorBeans.add(colorBean);
//                }
                colorBeans = getCommonColorType(formulaitems);
            }
        }
        return colorBeans;
    }
    private List<ColorBean> getCommonColorType(FormulaitemsBean formulaitems) {
        List<ColorBean> colorBeanList = new ArrayList<>();
        if (GlobalConstants.isManual) {
            if (formulaitems.getColorBeans() instanceof List) {
                colorBeanList = (List<ColorBean>) formulaitems.getColorBeans();
            } else {
                ColorBean colorBean = (ColorBean) formulaitems.getColorBeans();
                colorBeanList.add(colorBean);
            }
        } else {
            if (formulaitems.getColorBeans() instanceof List) {
                colorBeanList = GsonUtils.fromJson(formulaitems.getColorBeans() + "", new TypeToken<List<ColorBean>>() {
                }.getType());
            } else {
                ColorBean colorBean = GsonUtils.fromJson(formulaitems.getColorBeans() + "", new TypeToken<ColorBean>() {
                }.getType());
                colorBeanList.add(colorBean);
            }
        }

        return colorBeanList;
    }


    @Override
    public void onResume() {
        super.onResume();
        all = myFormulaService.getAll();
        bindAdapter();
    }
}
