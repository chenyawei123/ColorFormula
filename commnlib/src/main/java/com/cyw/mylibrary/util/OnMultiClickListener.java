package com.cyw.mylibrary.util;

import android.util.Log;
import android.view.View;


public abstract class OnMultiClickListener implements View.OnClickListener {
    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 500;
    private static long lastClickTime;

    public abstract void onMultiClick(View v);

    @Override
    public void onClick(View v) {
        long curClickTime = System.currentTimeMillis();
        Log.i("TAGSUBSUB2","JJJJJJJJJJJJ");
        if((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            // 超过点击间隔后再将lastClickTime重置为当前点击时间
            Log.i("TAGSUBSUB3","JJJJJJJJJJJJ");
            lastClickTime = curClickTime;
            onMultiClick(v);
        }
    }
}
