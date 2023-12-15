package com.example.datacorrects.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.datacorrects.customView.BaseViewHolder;
import com.example.datacorrects.customView.SimpleAdapter;
import com.example.datacorrects.R;
import com.example.datacorrects.bean.TableCell;

import java.util.ArrayList;
import java.util.List;

/**
 * chenyawei
 */

public class PopWtAdapter extends SimpleAdapter<TableCell> {
    private final Context mContext;
    private TextView mTextItem;
    private EditText mEditItem;
    private List<TableCell> datas = new ArrayList<TableCell>();
    private OnWtListener onWtListener;
    private List<Integer> rowList = new ArrayList<>();
    public PopWtAdapter(Context mContext,List<TableCell> datas){
        super(mContext, R.layout.wt_table_item,datas);
        this.mContext = mContext;
        this.datas = datas;
    }
    public List<Integer> getRowList(){
        return rowList;
    }
    @Override
    protected void convert(BaseViewHolder viewHoder, TableCell item, int position) {
//        mTextItem = viewHoder.getTextView(R.id.text_content_item);
//        mTextItem.setText(item.getValue());
        mEditItem = viewHoder.getEditText(R.id.edit_content_item);
        mEditItem.setText(item.getValue());
//        if(item.getName().equals("位置")){
//           mTextItem.setVisibility(View.VISIBLE);
//           mEditItem.setVisibility(View.GONE);
//        }else{
        final EditText editText = mEditItem;
        editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    Log.i("TAG","HHHHHHH");
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Log.i("TAG","HHHHHHH");
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(item.getName().equals("计算值")){
                        String value = editText.getText().toString();
                        Log.i("TAG",value+"jj"+s.toString());
                        item.setValue(s.toString());
                        rowList.add(item.getRow());
                        //onWtListener.onWt(item.getRow());
                    }
                }
            });
            //mTextItem.setVisibility(View.GONE);
            mEditItem.setVisibility(View.VISIBLE);

       // }
    }
    public String getTestValue(){
        return mEditItem.getText().toString().trim();
    }
    public void setOnWtListener(OnWtListener onWtListener){
        this.onWtListener = onWtListener;
    }
    public interface OnWtListener{
        void onWt(int row);
    }
}
