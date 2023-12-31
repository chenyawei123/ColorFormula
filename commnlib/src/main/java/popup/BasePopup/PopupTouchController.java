package popup.BasePopup;

import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * Created by cyw on 2017/1/13.
 */

interface PopupTouchController {


    boolean onBeforeDismiss();

    boolean callDismissAtOnce();

    boolean onDispatchKeyEvent(KeyEvent event);

    boolean onInterceptTouchEvent(MotionEvent event);

    boolean onTouchEvent(MotionEvent event);

    boolean onBackPressed();

    boolean onOutSideTouch();


}
