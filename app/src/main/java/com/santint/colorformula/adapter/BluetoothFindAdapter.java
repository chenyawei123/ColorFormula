package com.santint.colorformula.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cyw.mylibrary.customView.BaseAdapter;
import com.cyw.mylibrary.customView.BaseViewHolder;
import com.cyw.mylibrary.customView.SimpleAdapter;
import com.santint.colorformula.BlueToothActivity;
import com.santint.colorformula.R;

import java.util.ArrayList;
import java.util.List;



/**
 * chenyawei
 */

public class BluetoothFindAdapter extends SimpleAdapter<BluetoothDevice> implements BaseAdapter.OnItemClickListener {
    private final Context mContext;
    private TextView mTextItem;
    private List<BluetoothDevice> datas = new ArrayList<BluetoothDevice>();
    private final String foodName = "";
    private final String tableId = "";
    private TextView tvStart;
    private int bigStepCount = 0;
    private final boolean isCurBig = false;
    private boolean isAskNext = false;
    private final int maxFoodCount = 7;
    private TextView tvName;
    private TextView tvConnect;
    private TextView tvAddress;
    private String getAddress;

    public BluetoothFindAdapter(Context mContext, List<BluetoothDevice> datas, RecyclerView ry) {
        super(mContext, R.layout.blue_find_item, datas);
        this.mContext = mContext;
        this.datas = datas;
        setOnItemClickListener(this);
    }


    @Override
    protected void convert(BaseViewHolder viewHoder, final BluetoothDevice item, final int position) {
        tvName = viewHoder.getTextView(R.id.id_tv_device_name);
        tvName.setText(item.getName());
        tvAddress = viewHoder.getTextView(R.id.id_tv_address);
        tvAddress.setText(item.getAddress());
        tvConnect = viewHoder.getTextView(R.id.id_tv_connect);
        if(getAddress!=null && getAddress.length()>0){
            if(getAddress.equals(item.getAddress())){
                ((BlueToothActivity)mContext).itmClikPostion(position);
            }
        }
        tvConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // ((BlueToothActivity)mContext).bondDevice(item);
                ((BlueToothActivity)mContext).itmClikPostion(position);
            }
        });
    }

    public void setAddress(String address) {
        this.getAddress = address;
    }

    public void setBigStepCount(int bigStepCount) {
        this.bigStepCount = bigStepCount;
    }

    @Override
    public void onItemClick(View view, int position) {
        ((BlueToothActivity)mContext).itmClikPostion(position);
    }
}
