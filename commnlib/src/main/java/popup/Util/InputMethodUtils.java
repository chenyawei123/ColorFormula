package popup.Util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by cyw on 2016/1/14.
 * 显示键盘d工具类
 */
public class InputMethodUtils {
    /**
     * 显示软键盘
     */
    public static void showInputMethod(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);//第二个参数不会影响显示
        }
    }

    /**
     * 显示软键盘
     */
    public static void showInputMethod(Context context) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);//第二个参数不会影响显示
    }

    /**
     * 多少时间后显示软键盘
     */
    public static void showInputMethod(final View view, long delayMillis) {
        if (view == null) return;
        // 显示输入法
        view.postDelayed(new Runnable() {

            @Override
            public void run() {
                InputMethodUtils.showInputMethod(view);
            }
        }, delayMillis);
    }

    /**
     * 隐藏软键盘
     */
    public static void close(Activity activity) {
        if (activity == null) {
            return;
        }
        View view = activity.getWindow().getDecorView().getRootView();
        try {
            InputMethodManager imm = (InputMethodManager) view.getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);//InputMethodManager.HIDE_NOT_ALWAYS 和0都是正确的
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 隐藏软键盘
     */
    public static void close(View view) {
        try {
            InputMethodManager imm = (InputMethodManager) view.getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);//InputMethodManager.HIDE_NOT_ALWAYS 和0都是正确的
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void observerKeyboardVisibleChange(final View decorView, final OnKeyboardStateChangeListener listener) {
        if (decorView == null || listener == null) return;
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            int preKeyboardHeight = -1;
            final Rect rect = new Rect();
            boolean preVisible = false;

            @Override
            public void onGlobalLayout() {
                rect.setEmpty();
                decorView.getWindowVisibleDisplayFrame(rect);
                int displayHeight = rect.height();
                int windowHeight = decorView.getHeight();
                int keyboardHeight = windowHeight - displayHeight;
                if (preKeyboardHeight != keyboardHeight) {
                    //判定可见区域与原来的window区域占比是否小于0.75,小于意味着键盘弹出来了。
                    boolean isVisible = (displayHeight * 1.0f / windowHeight * 1.0f) < 0.75f;
                    if (isVisible != preVisible) {
                        listener.onKeyboardChange(keyboardHeight, isVisible);
                        preVisible = isVisible;
                    }
                }
                preKeyboardHeight = keyboardHeight;

            }
        });

    }


    //=============================================================interface
    public interface OnKeyboardStateChangeListener {
        void onKeyboardChange(int keyboardHeight, boolean isVisible);
    }
}
