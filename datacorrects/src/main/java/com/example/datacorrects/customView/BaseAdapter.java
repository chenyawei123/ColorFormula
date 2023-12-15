package com.example.datacorrects.customView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter的简单封装
 */
public abstract class BaseAdapter<T, H extends BaseViewHolder> extends
        RecyclerView.Adapter<BaseViewHolder> {

    protected static final String TAG = BaseAdapter.class.getSimpleName();

    protected final Context context;

    protected final int layoutResId;

    protected List<T> datas;

    private OnItemClickListener mOnItemClickListener = null;
    private OnItemLongClickListener mOnItemLongClickListener = null;

    // HeaderView, FooterView
    private View mHeaderView;
    private View mFooterView;
    public static final int TYPE_HEADER = 0; // 说明是带有Header的
    public static final int TYPE_FOOTER = 1; // 说明是带有Footer的
    public static final int TYPE_NORMAL = 2; // 说明是不带有header和footer的
    public static final int TYPE_EMPTY = 3;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    public interface OnItemLongClickListener{
        void onItemLongClick(View view, int position);
    }

    public BaseAdapter(Context context, int layoutResId) {
        this(context, layoutResId, null);
    }

    public BaseAdapter(Context context, int layoutResId, List<T> datas) {
        this.datas = datas == null ? new ArrayList<T>() : datas;
        this.context = context;
        this.layoutResId = layoutResId;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER) {
            return new BaseViewHolder(mHeaderView, mOnItemClickListener,mOnItemLongClickListener);
        }
        if (mFooterView != null && viewType == TYPE_FOOTER) {
            return new BaseViewHolder(mFooterView, mOnItemClickListener,mOnItemLongClickListener);
        }
//		if(viewType == TYPE_EMPTY){
//			View view = LayoutInflater.from(viewGroup.getContext()).inflate(
//					R.layout.item_empty, viewGroup, false);
//			return new BaseViewHolder(view, mOnItemClickListener,mOnItemLongClickListener);
//		}else{
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                layoutResId, viewGroup, false);
        return new BaseViewHolder(view, mOnItemClickListener,mOnItemLongClickListener);
//		}


        // View view = LayoutInflater.from(viewGroup.getContext()).inflate(
        // layoutResId, viewGroup, false);
        // BaseViewHolder vh = new BaseViewHolder(view, mOnItemClickListener);
        // return vh;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_NORMAL) {
            if (holder instanceof BaseViewHolder) {
                // 这里加载数据的时候要注意，是从position-1开始，因为position==0已经被header占用了
                if (mHeaderView != null) {
                    position--;
                }
                T item = getItem(position);
                Log.i("TAGCOUNT2",datas.size()+"dfdfd"+position);
                convert((H) holder, item,position);
                return;
            }
            return;
        } else if (getItemViewType(position) == TYPE_HEADER) {
            return;
        } else {
            return;
        }

        // T item = getItem(position);
        // convert((H) holder, item);
    }

    // @Override
    // public int getItemCount() {
    // if (datas == null || datas.size() <= 0)
    // return 0;
    //
    // //return datas.size();
    // return datas.size() + (mHeaderView == null ? 0 : 1) + (mFooterView ==
    // null ? 0 : 1);
    // }

    public T getItem(int position) {
        if (position >= datas.size())
            return null;
        return datas.get(position);
        // if (datas != null) {
        // return datas.get(position);
        // } else if (datas == null || position - 1 < 0 || position >
        // datas.size()) {
        // return null;
        // } else {
        // return datas.get(position - 1);
        // }

        // if (datas != null && position == datas.size()) {
        // return datas.get(position - 1);
        // } else if (datas == null || position - 1 < 0 || position >
        // datas.size()) {
        // return null;
        // } else {
        // return datas.get(position);
        // }

        // if (datas == null || position - 1 < 0 || position > datas.size()) {
        // return null;
        // }else{
        // return datas.get(position-1);
        // }
    }

    /**
     * 清空数据
     */
    public void clearData() {
        int size = datas.size();
        datas.clear();
        notifyItemRangeRemoved(0, size);
    }

    /**
     * 下拉刷新，清除原有数据，添加新数据
     *
     * @param newData
     */
    public void refreshData(List<T> newData) {
        datas.clear();
        datas.addAll(newData);
        notifyItemRangeChanged(0, datas.size());
    }

    /**
     * 在原来数据的末尾追加新数据
     *
     * @param moreData
     */
    public void loadMoreData(List<T> moreData) {
        int lastPosition = datas.size();
        datas.addAll(lastPosition, moreData);
        notifyItemRangeInserted(lastPosition, moreData.size());
    }

    /**
     * Implement this method and use the helper to adapt the view to the given
     * item.
     *
     * @param viewHoder
     *            A fully initialized helper.
     * @param item
     *            The item that needs to be displayed.
     */
    protected abstract void convert(H viewHoder, T item,int position);

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;

    }
    public void setOnItemLongClickListener(OnItemLongClickListener longListener) {
        this.mOnItemLongClickListener = longListener;

    }

    // HeaderView和FooterView的get和set函数
    public View getHeaderView() {
        return mHeaderView;
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public View getFooterView() {
        return mFooterView;
    }

    public void setFooterView(View footerView) {
        mFooterView = footerView;
        notifyItemInserted(getItemCount() - 1);
    }

    /** 重写这个方法，很重要，是加入Header和Footer的关键，我们通过判断item的类型，从而绑定不同的view * */
    @Override
    public int getItemViewType(int position) {
        // if (mHeaderView == null && mFooterView == null) {
        // return TYPE_NORMAL;
        // }
        // if (position == 0) {
        // // 第一个item应该加载Header
        // return TYPE_HEADER;
        // }
        // if (position == getItemCount() - 1) {
        // // 最后一个,应该加载Footer
        // return TYPE_FOOTER;
        // }
        // return TYPE_NORMAL;
        if (mHeaderView == null && mFooterView == null) {
            // 普通item
//			if(datas.size()<=0){
//				return TYPE_EMPTY;
//			}else{
            return TYPE_NORMAL;
//			}

        } else if (position == 0 && mHeaderView != null) {
            // 第一个item应该加载Header
            return TYPE_HEADER;
        } else if (position == getItemCount() - 1 && mFooterView != null) {
            // 最后一个,应该加载Footer
            return TYPE_FOOTER;
        }
//		if(datas.size()<=0){
//			return TYPE_EMPTY;
//		}else{
        return TYPE_NORMAL;
//		}

    }

    // 返回View中Item的个数，这个时候，总的个数应该是ListView中Item的个数加上HeaderView和FooterView
    @Override
    public int getItemCount() {
//		if (datas == null || datas.size() <= 0) {
//			// return 0;
//			return 1;
//		} else {
        Log.i("TAGCOUNT",datas.size()+"");
        return datas.size() + (mHeaderView == null ? 0 : 1)
                + (mFooterView == null ? 0 : 1);
//		}
        // if (datas == null) {
        // return 0;
        // } else {
        // return datas.size() + 1;
        // }
    }

}