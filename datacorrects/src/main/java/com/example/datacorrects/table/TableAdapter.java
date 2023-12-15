package com.example.datacorrects.table;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.datacorrects.R;
import com.example.datacorrects.bean.TableCell;

import java.util.List;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ItemViewHolder> {

    public interface OnItemClickListener {

        void onItemClick(int position);
    }
    public interface onItemLongClickListener{
        void onItemLongClick(int position);
    }

    private final Context             mContext;
    private final List<TableCell>        mDataList;
    public OnItemClickListener mListener;
    public onItemLongClickListener onItemLongClickListener;

    public TableAdapter(Context context, List<TableCell> dataList) {
        mContext = context;
        mDataList = dataList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    public void setOnItemLongClickListener(TableAdapter.onItemLongClickListener onItemLongClickListener){
        this.onItemLongClickListener = onItemLongClickListener;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_table_content, parent, false));
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        holder.mTextItem.setText(mDataList.get(position).getValue());
//        if (position / CorrectionActivity.POINT_COLUMN_COUNT % 2 == 0) {
//            holder.mTextItem.setBackgroundColor(mContext.getResources().getColor(R.color.blue));
//        } else {
//            holder.mTextItem.setBackgroundColor(mContext.getResources().getColor(R.color.red));
//        }
        holder.mTextItem.setTag(position);
        holder.mTextItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick((Integer) holder.mTextItem.getTag());
                }
            }
        });
        holder.mTextItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(onItemLongClickListener!=null){
                    onItemLongClickListener.onItemLongClick((Integer)holder.mTextItem.getTag());
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView mTextItem;

        ItemViewHolder(View itemView) {
            super(itemView);
            mTextItem = itemView.findViewById(R.id.text_content_item);
        }
    }

}
