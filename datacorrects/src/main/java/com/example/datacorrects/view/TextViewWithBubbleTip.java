package com.example.datacorrects.view;

import android.content.Context;

import com.bin.david.form.data.format.tip.SingleLineBubbleTip;
import com.bin.david.form.data.style.FontStyle;

/**
 * author： cyw
 */
public class TextViewWithBubbleTip<TextView> extends SingleLineBubbleTip<TextView> {
    public TextViewWithBubbleTip(Context context, int backgroundDrawableID, int triangleDrawableID, FontStyle style) {
        super(context, backgroundDrawableID, triangleDrawableID, style);
    }

    @Override
    public boolean isShowTip(TextView textView, int position) {
        return true;
    }

    @Override
    public String format(TextView textView, int position) {
        return "abcde";
    }
}
