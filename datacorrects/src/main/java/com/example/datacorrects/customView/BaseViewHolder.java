package com.example.datacorrects.customView;


import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cyw.mylibrary.util.ZProgressBar;

/**
 * 封装的ViewHolder
 */
public class BaseViewHolder extends RecyclerView.ViewHolder implements
		View.OnClickListener,View.OnLongClickListener {

	// 使用数组把条目中的View保存起来
	private  SparseArray<View> views;

	private BaseAdapter.OnItemClickListener mOnItemClickListener;
	private BaseAdapter.OnItemLongClickListener mOnItemLongClickListener;
	private View mHeaderView;
	private View mFooterView;

	public BaseViewHolder(View itemView,
			BaseAdapter.OnItemClickListener onItemClickListener,BaseAdapter.OnItemLongClickListener onItemLongClickListener) {
		super(itemView);
		if (itemView == mHeaderView) {
			return;
		}
		if (itemView == mFooterView) {
			return;
		}
		itemView.setOnClickListener(this);
		itemView.setOnLongClickListener(this);

		this.mOnItemClickListener = onItemClickListener;
		this.mOnItemLongClickListener = onItemLongClickListener;
		this.views = new SparseArray<View>();
	}

	public LinearLayout getLinearLayout(int viewId) {
		return retrieveView(viewId);
	}

	public RelativeLayout getRelativeLayout(int viewId) {
		return retrieveView(viewId);
	}

	public TextView getTextView(int viewId) {
		return retrieveView(viewId);
	}
	public ZProgressBar getZProgressBar(int viewId){
		return retrieveView(viewId);
	}
	public EditText getEditText(int viewId) {
		return retrieveView(viewId);
	}
	public EdittextWithRightText getCustomEditText(int viewId){
		return retrieveView(viewId);
	}
	public CheckBox getCheckBox(int viewId) {
		return retrieveView(viewId);
	}
	public RatingBar getRatingBar(int viewId){
		return retrieveView(viewId);
	}
	public GridView getGridView(int viewId){
		return retrieveView(viewId);
	}
	public RecyclerView getRyView(int viewId){
		return retrieveView(viewId);
	}



	public Button getButton(int viewId) {
		return retrieveView(viewId);
	}

	public ImageView getImageView(int viewId) {
		return retrieveView(viewId);
	}
	public RadioButton getRadioButton(int viewId) {
		return retrieveView(viewId);
	}

	public View getView(int viewId) {
		return retrieveView(viewId);
	}

	protected <T extends View> T retrieveView(int viewId) {
		View view = views.get(viewId);
		if (view == null) {
			view = itemView.findViewById(viewId);
			views.put(viewId, view);
		}
		return (T) view;
	}

	@Override
	public void onClick(View v) {
		if (mOnItemClickListener != null) {
			mOnItemClickListener.onItemClick(v, getLayoutPosition());
		}
	}

	@Override
	public boolean onLongClick(View v) {
		if(mOnItemLongClickListener!=null){
			mOnItemLongClickListener.onItemLongClick(v, getLayoutPosition());
		}
		return false;
	}
}
