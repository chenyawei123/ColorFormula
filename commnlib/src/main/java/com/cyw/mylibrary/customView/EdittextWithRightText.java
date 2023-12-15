package com.cyw.mylibrary.customView;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;

/**
 * author： cyw
 */
public class EdittextWithRightText extends androidx.appcompat.widget.AppCompatEditText {
    private String fixedText="g";
    private int right;
    private int paddingRight=0;
    int width = 0;

    public EdittextWithRightText(Context context) {
        super(context);
    }

    public EdittextWithRightText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EdittextWithRightText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void setWidth(int wid){
        //width = wid;
        ViewTreeObserver vto = getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                width = getMeasuredWidth();
            }
        });
    }
    public void setFixedText(String text){
        fixedText = text;
        paddingRight = getPaddingRight();
        right = paddingRight+(int)getPaint().measureText(fixedText);
        //width = getRight();
        int paddingtop=getPaddingTop();
        int paddingleft = getPaddingLeft();
        int paddingbottom = getPaddingBottom();
        setPadding(paddingleft,paddingtop,right,paddingbottom);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(fixedText,width-right,getBaseline(),getPaint());
    }
}
