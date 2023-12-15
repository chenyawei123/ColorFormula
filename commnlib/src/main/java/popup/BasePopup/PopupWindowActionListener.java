package popup.BasePopup;

/**
 * Created by cyw on 2018/11/26.
 */
interface PopupWindowActionListener {

    void onShow(boolean hasAnimate);

    void onDismiss(boolean hasAnimate);

    boolean onUpdate();
}
