package com.bin.david.form.listener;

import com.bin.david.form.data.column.Column;


public interface OnColumnItemLongClickListener<T> {

    void onLongClick(Column<T> column,String value, T t, int position);
}
