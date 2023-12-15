package com.example.datacorrects.table;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.datacorrects.R;
import com.example.datacorrects.bean.TableCell;
import com.example.datacorrects.view.BaseTableViewHolder;

import java.util.List;

public class MethodTableAdapter2 extends RecyclerView.Adapter<BaseTableViewHolder> {

    private final Context mContext;
    private List<TableCell> mDataList;
    private int viewType = 0;
    private CheckBoxListListener checkBoxListListener;
    ItemViewHolder itemViewHolder;
    private boolean isCheckAll = false;


    public MethodTableAdapter2(Context context, List<TableCell> dataList) {
        mContext = context;
        mDataList = dataList;
    }

    //    public void setOnItemClickListener(OnItemClickListener listener) {
//        mListener = listener;
//    }
//
//    public void setOnItemLongClickListener(MethodTableAdapter.onItemLongClickListener onItemLongClickListener) {
//        this.onItemLongClickListener = onItemLongClickListener;
//    }
    public void setOnCheckBoxListener(CheckBoxListListener checkBoxListener) {
        this.checkBoxListListener = checkBoxListener;
    }

    @Override
    public BaseTableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //this.viewType = viewType;
        return new ItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.method_table_content, parent, false));

    }

    @Override
    public int getItemViewType(int position) {
        return mDataList.get(position).getType();//mDataList.get(position).getType()
    }

    @Override
    public void onBindViewHolder(final BaseTableViewHolder holder, int position) {
        this.viewType = getItemViewType(position);
        final TableCell tableCell = mDataList.get(position);
        //final ItemViewHolder itemViewHolder = ((ItemViewHolder)holder);
        itemViewHolder = ((ItemViewHolder) holder);
        if (viewType == 1) {
            ((ItemViewHolder) holder).mTextItem.setVisibility(View.VISIBLE);
            ((ItemViewHolder) holder).linearCheck.setVisibility(View.GONE);
            itemViewHolder.mTextItem.setText(tableCell.getValue());
//        if (position / CorrectionActivity.POINT_COLUMN_COUNT % 2 == 0) {
//            holder.mTextItem.setBackgroundColor(mContext.getResources().getColor(R.color.blue));
//        } else {
//            holder.mTextItem.setBackgroundColor(mContext.getResources().getColor(R.color.red));
//        }
            itemViewHolder.mTextItem.setTag(position);
//            itemViewHolder.mTextItem.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mListener != null) {
//                        mListener.onItemClick((Integer) itemViewHolder.mTextItem.getTag());
//                    }
//                }
//            });
//            itemViewHolder.mTextItem.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    if (onItemLongClickListener != null) {
//                        onItemLongClickListener.onItemLongClick((Integer) holder.mTextItem.getTag());
//                    }
//                    return false;
//                }
//            });
        } else if (viewType == 2) {
            final CheckBox checkBox = itemViewHolder.mBox;
            ((ItemViewHolder) holder).linearCheck.setVisibility(View.VISIBLE);
            ((ItemViewHolder) holder).mTextItem.setVisibility(View.GONE);
            //final CheckHolder checkHolder = ((CheckHolder)holder);
            boolean result = Boolean.valueOf(tableCell.getValue());
            checkBox.setChecked(result);
            itemViewHolder.tvNo.setText(String.valueOf(tableCell.getRow()));
//            itemViewHolder.mBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    itemViewHolder.mBox.setChecked(isChecked);
//                    if (itemViewHolder.mBox.isChecked()) {
//                        checkBoxListListener.onCheckBoxList(tableCell.getRow(), 1);
//                    } else {
//                        checkBoxListListener.onCheckBoxList(tableCell.getRow(), 2);
//                    }
//
//                }
//            });

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //itemViewHolder.mBox.setChecked(isChecked);
                    //checkBox.setChecked(!checkBox.isChecked());
                    if (checkBox.isChecked()) {
                        checkBox.setChecked(true);
                        checkBoxListListener.onCheckBoxList(tableCell.getRow(), 1);

                    } else {
                        checkBox.setChecked(false);
                        checkBoxListListener.onCheckBoxList(tableCell.getRow(), 2);

                    }

                }
            }) ;
        }
    }
    public void setCheckAll(boolean isCheckAll){
        this.isCheckAll = isCheckAll;
    }

    public List<TableCell> getDatas() {
        return mDataList;
    }

    public void clear() {
        if (mDataList != null && mDataList.size() > 0) {
            mDataList.clear();
        }
        notifyDataSetChanged();
        //notifyItemRangeChanged(0,mDataList.size());
    }

    public void setDatas(List<TableCell> tableCells) {
        mDataList = tableCells;
        notifyDataSetChanged();
    }

    public void clearAllCheck() {

        itemViewHolder.mBox.setChecked(false);
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    class ItemViewHolder extends BaseTableViewHolder {

        TextView mTextItem;
        CheckBox mBox;
        TextView tvNo;
        private final LinearLayout linearCheck;

        ItemViewHolder(View itemView) {
            super(itemView);
            mTextItem = itemView.findViewById(R.id.text_content_item);
            mBox = itemView.findViewById(R.id.cb_name);
            tvNo = itemView.findViewById(R.id.id_tv_no);
            linearCheck = itemView.findViewById(R.id.id_linear_check);

        }
    }

    static class CheckHolder extends BaseTableViewHolder {

        CheckBox mBox;
        TextView tvNo;

        public CheckHolder(View itemView) {
            super(itemView);
            mBox = itemView.findViewById(R.id.cb_name);
            tvNo = itemView.findViewById(R.id.id_tv_no);
        }

    }

    public interface CheckBoxListListener {
        void onCheckBoxList(int row, int type);
    }
}
