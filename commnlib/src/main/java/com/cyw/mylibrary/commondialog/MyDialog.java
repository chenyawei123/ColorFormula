package com.cyw.mylibrary.commondialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.cyw.mylibrary.application.AppApplication;

/**
 * author： cyw
 */
public class MyDialog extends Dialog {
    private Window window = null;

    public MyDialog(Context context, boolean cancelable,
                    OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public MyDialog(Context context, int theme) {
        super(context, theme);
    }

    public MyDialog(Context context) {
        super(context);
    }

    public void setView(View view) {
        setContentView(view);
    }
    public void setView(int id) {
        setContentView(id);
    }
    public void setProperty(View view){
        window = getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();

        int[] location = new int[2];
        view.getLocationOnScreen(location);
        Rect out = new Rect();
        view.getWindowVisibleDisplayFrame(out);
        int height = view.getHeight();
        int width = view.getWidth();
        wl.x = location[0];
        wl.y = location[1] + height;
        // 设置dialog应该占的控件参数
        wl.width = width;
        wl.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.LEFT|Gravity.TOP);
        window.setAttributes(wl);
    }
    public void setProperty(TextView textView){
        window = getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();

        int[] location = new int[2];
        textView.getLocationOnScreen(location);
        Rect out = new Rect();
        textView.getWindowVisibleDisplayFrame(out);
        int height = textView.getHeight();
        int width = textView.getWidth();
        wl.x = location[0];
        wl.y = location[1] + height;
        // 设置dialog应该占的控件参数
        wl.width = width;
        wl.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.LEFT|Gravity.TOP);
        window.setAttributes(wl);
    }

    public void setProperty(int x, int y, int w, int h) {
        window = getWindow();//得到对话框的窗口．
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = x;//设置对话框的位置．0为中间
        wl.y = y;
        wl.width = w;
        wl.height = h;
        wl.alpha = 1f;// 设置对话框的透明度,1f不透明
        wl.gravity = Gravity.CENTER;//设置显示在中间
        window.setAttributes(wl);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        View decorView = window.getDecorView();
        AppApplication.getInstance().init(decorView);
    }
}
