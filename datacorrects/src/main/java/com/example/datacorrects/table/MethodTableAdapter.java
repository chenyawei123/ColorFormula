package com.example.datacorrects.table;//package com.example.santintcolddish.correction.table;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//import android.widget.TextView;
//
//import com.example.santintcolddish.R;
//import com.example.santintcolddish.correction.adapter.BaseAdapter;
//import com.example.santintcolddish.correction.bean.TableCell;
//import com.example.santintcolddish.correction.view.BaseTableViewHolder;
//
//import java.util.List;
//
//public class MethodTableAdapter extends RecyclerView.Adapter<MethodTableAdapter.ItemViewHolder> {
//    private int pointCount = 0;
//
//    public interface OnItemClickListener {
//
//        void onItemClick(int position);
//    }
//
//    public interface onItemLongClickListener {
//        void onItemLongClick(int position);
//    }
//
//    private Context mContext;
//    private List<TableCell> mDataList;
//    public OnItemClickListener mListener;
//    public onItemLongClickListener onItemLongClickListener;
//
//    public MethodTableAdapter(Context context, List<TableCell> dataList) {
//        mContext = context;
//        mDataList = dataList;
//    }
//
//    public void setOnItemClickListener(OnItemClickListener listener) {
//        mListener = listener;
//    }
//
//    public void setOnItemLongClickListener(MethodTableAdapter.onItemLongClickListener onItemLongClickListener) {
//        this.onItemLongClickListener = onItemLongClickListener;
//    }
//
//    @Override
//    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        switch (viewType) {
//            case 1:
//                return new ItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_table_content, parent, false));
//            case 2:
//                return new CheckHolder(LayoutInflater.from(mContext).inflate(R.layout.table_cell_check_item,parent));
//            default:
//                return null;
//        }
//
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return mDataList.get(position).getType();
//    }
//
//    @Override
//    public void onBindViewHolder(final ItemViewHolder holder, int position) {
//        TableCell tableCell = mDataList.get(position);
//        holder.mTextItem.setText(tableCell.getValue());
////        if (position / CorrectionActivity.POINT_COLUMN_COUNT % 2 == 0) {
////            holder.mTextItem.setBackgroundColor(mContext.getResources().getColor(R.color.blue));
////        } else {
////            holder.mTextItem.setBackgroundColor(mContext.getResources().getColor(R.color.red));
////        }
//        holder.mTextItem.setTag(position);
//        holder.mTextItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mListener != null) {
//                    mListener.onItemClick((Integer) holder.mTextItem.getTag());
//                }
//            }
//        });
//        holder.mTextItem.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                if (onItemLongClickListener != null) {
//                    onItemLongClickListener.onItemLongClick((Integer) holder.mTextItem.getTag());
//                }
//                return false;
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return mDataList == null ? 0 : mDataList.size();
//    }
//
//    static class ItemViewHolder extends BaseTableViewHolder {
//
//        TextView mTextItem;
//        private CheckBox checkBox;
//
//        ItemViewHolder(View itemView) {
//            super(itemView);
//            mTextItem = itemView.findViewById(R.id.text_content_item);
//            // checkBox = itemView.findViewById(R.id.cb_name);
//        }
//    }
//
//    class CheckHolder extends BaseTableViewHolder {
//
//        CheckBox mBox;
//        TextView tvNo;
//
//        public CheckHolder(View itemView) {
//            super(itemView);
//            mBox = itemView.findViewById(R.id.cb_name);
//            tvNo = itemView.findViewById(R.id.id_tv_no);
//        }
//
//        @Override
//        protected void onBind(TableCell tableCell) {
//            boolean result = Boolean.valueOf(tableCell.getValue());
//            mBox.setChecked(result);
//            tvNo.setText(String.valueOf(tableCell.getRow()));
//            mBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    mBox.setChecked(isChecked);
//                    if (mBox.isChecked()) {
//                        pointCount++;
//                    } else {
//                        pointCount--;
//                    }
//
//                }
//            });
//        }
//    }
//}
