package popup.widget;

import android.view.View;

/**
 * Created by cyw on 2019/1/18.
 */
public abstract class OnQuickPopupClickListenerWrapper implements View.OnClickListener {
    QuickPopup mQuickPopup;

    @Deprecated
    @Override
    public void onClick(View v) {
        onClick(mQuickPopup, v);
    }

    public abstract void onClick(QuickPopup basePopup, View v);
}
