package com.example.datacorrects.commondialog;

import android.content.Context;

import com.cyw.mylibrary.commondialog.MyDialog;

/**
 * author： cyw
 */
public class DropdownCountDialog extends MyDialog {
    public DropdownCountDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public DropdownCountDialog(Context context, int theme) {
        super(context, theme);
    }

    public DropdownCountDialog(Context context) {
        super(context);
    }
    public void initView(){

    }
}
