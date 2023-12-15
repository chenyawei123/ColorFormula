package com.bin.david.form.listener;

import com.bin.david.form.data.column.Column;


public interface OnColumnItemClickListener<T> {

    void onClick(Column<T> column,String value, T t, int position);
}
