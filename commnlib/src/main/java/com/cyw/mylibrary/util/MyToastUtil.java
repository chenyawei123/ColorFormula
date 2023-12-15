package com.cyw.mylibrary.util;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cyw.mylibrary.R;
import com.google.android.material.snackbar.Snackbar;

/**
 * author： cyw
 */
public class MyToastUtil {
    //    public static void showToast(Context context,String text,int duration){
//        Toast toast = Toast.makeText(context, text, duration);
//        toast.setGravity(Gravity.CENTER, 0, 0); //居中显示
////        LinearLayout linearLayout = (LinearLayout) toast.getView();
////        TextView messageTextView = (TextView) linearLayout.getChildAt(0);
////        messageTextView.setTextSize(25);//设置toast字体大小
//        toast.show();
//    }
    public static void showToast(Context context, String text, int duration) {
        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.CENTER, 0, 0); //居中显示
//        LinearLayout linearLayout = (LinearLayout) toast.getView();
//        TextView messageTextView = (TextView) linearLayout.getChildAt(0);
//        messageTextView.setTextSize(25);//设置toast字体大小
        toast.show();
    }
    public static void showToastSnackBar(View view,String text,int duration){
        Snackbar snackbar = Snackbar.make(view,text,duration);
        View snackbarView = snackbar.getView();
        TextView viewById = snackbarView.findViewById(R.id.snackbar_text);
        // viewById.setTextColor(Color.RED);
        viewById.setTextSize(30);
        snackbar.show();
    }

}
