package com.santint.colorformula.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cyw.mylibrary.customView.BaseAdapter;
import com.cyw.mylibrary.customView.BaseViewHolder;
import com.cyw.mylibrary.customView.SimpleAdapter;
import com.cyw.mylibrary.util.ComputeDoubleUtil;
import com.cyw.mylibrary.util.ZProgressBar;
import com.santint.colorformula.R;
import com.santint.colorformula.bean.ProgressBean;

import java.util.List;



/**
 * chenyawei
 */

public class ColorWorkStateAdapter extends SimpleAdapter<ProgressBean> implements BaseAdapter.OnItemClickListener {
    private final Context mContext;
    private TextView tvBarrelNo;
    private TextView tvPercent;
    private ZProgressBar zProgressBar;
    private int progress = 0;
    private boolean isFinished = false;



    public ColorWorkStateAdapter(Context mContext, List<ProgressBean> datas, RecyclerView ry) {
        super(mContext, R.layout.color_work_state, datas);
        this.mContext = mContext;
        this.datas = datas;
        setOnItemClickListener(this);
    }
    public void setList(List<ProgressBean> list){
        this.datas = list;
    }
//    public void setFinish(boolean finish){
//        isFinished = finish;
//    }


    @Override
    protected void convert(BaseViewHolder viewHoder, final ProgressBean item, final int position) {
        tvBarrelNo = viewHoder.getTextView(R.id.tv_barrel_no);
        tvPercent = viewHoder.getTextView(R.id.tv_percent);
        ProgressBean progressBean = item;
        tvBarrelNo.setText(progressBean.getColorName());
        zProgressBar = viewHoder.getZProgressBar(R.id.zpb_color);
       // zProgressBar.setMax(115);
        double totalValue = progressBean.getTotalValue();
        double realValue = progressBean.getRealValue();
        String strTotal = ComputeDoubleUtil.computeDouble(totalValue);
        String strReal = ComputeDoubleUtil.computeDouble(realValue);
        tvPercent.setText(strReal+"/"+strTotal);
        double percent = realValue/totalValue*100;
        //zProgressBar.setMax((int)percent);
        zProgressBar.setTextOver((int)percent,item.isFinishState());
        zProgressBar.setProgress((int)percent);
        //if(item.isFinishState()){
            zProgressBar.invalidate();
        //}


           // tvColor.setBackgroundColor(Color.rgb(progressRed * 255 / 100, progressGreen * 255 / 100, progressBlue * 255 / 100));

        //tvName.setText(item.getFormulaName());

    }

    @Override
    public void onItemClick(View view, int position) {

    }
}
