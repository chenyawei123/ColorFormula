package com.cyw.mylibrary.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;

import com.cyw.mylibrary.R;


public class ZProgressBar extends ProgressBar {
    private static final String TAG = "SimpleProgressbar";

    public static final int DEFAULT_UNREACHED_COLOR = 0xFF7D9EC0;
    public static final int DEFAULT_REACHED_COLOR = 0xFFC1FFC1;
    public static final int DEFAULT_TEXT_COLOR = 0xFF0000CD;
    public static final String DEFAULT_TEXT = "100%";
    // 进度条默认高，单位为 dp
    public static final int DEFAULT_LINE_HEIGHT = 20;
    // 进度条默认宽，单位为 dp
    public static final int DEFAULT_LINE_WIDTH = 100;
    // 文本大小，单位为 sp
    public static final int DEFAULT_TEXT_SIZE = 18;

    private Paint paint;
    private Rect textBound;

    private int reachedColor;
    private int unreachedColor;
    private int textColor;

    private int lineHeight;
    private int minLineHeight;
    private int minLineWidth;

    private int textSize;
    private int textHeight;
    private int textWidth;
    private boolean isShowText;
    private String textOver = "";
    private boolean isFinished = false;

    public ZProgressBar(Context context) {
        //        super(context);
        this(context, null);
    }

    public ZProgressBar(Context context, AttributeSet attrs) {
        //        super(context, attrs);
        this(context, attrs, 0);
    }

    public ZProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        paint = new Paint();
        textBound = new Rect();

        unreachedColor = DEFAULT_UNREACHED_COLOR;
        reachedColor = DEFAULT_REACHED_COLOR;
        textColor = DEFAULT_TEXT_COLOR;

        minLineHeight = dp2px(DEFAULT_LINE_HEIGHT);
        minLineWidth = dp2px(DEFAULT_LINE_WIDTH);
        textSize = sp2px(DEFAULT_TEXT_SIZE);

        isShowText = true;

        obtainStyledAttributes(context, attrs, defStyleAttr);
    }

    @Override
    public void setProgress(int progress, boolean animate) {
        super.setProgress(progress, animate);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        // 计算文本的宽和高
        paint.setTextSize(textSize);
        paint.getTextBounds(DEFAULT_TEXT, 0, DEFAULT_TEXT.length(), textBound);
        textWidth = textBound.width();
        textHeight = textBound.height();
        int minHeight = minLineHeight;
        if (isShowText) {
            // 比较文本的高和线段的高
            minHeight = textHeight > minLineHeight ? textHeight : minLineHeight;
        }

        int desiredWidth = minLineWidth + getPaddingLeft() + getPaddingRight();
        int desiredHeight = minHeight + getPaddingTop() + getPaddingBottom();

        int width;
        int height;

        if (widthMode == MeasureSpec.AT_MOST) {
            width = desiredWidth;
        } else {
            width = Math.max(widthSize, desiredWidth);
        }

        if (heightMode == MeasureSpec.AT_MOST) {
            height = desiredHeight;
            lineHeight = minLineHeight;
        } else {
            height = Math.max(heightSize, desiredHeight);
            lineHeight = height - getPaddingLeft() - getPaddingRight();
        }

        setMeasuredDimension(width, height);
    }
    public void setTextOver(int value,boolean finished){
      textOver = String.valueOf(value);
      isFinished = finished;
        WriteLog.writeTxtToFile("zzprogressbar2-------------"+isFinished+"hhhhgdg");
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected synchronized void onDraw(Canvas canvas) {
        //        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();

        int contentWidth = width - getPaddingLeft() - getPaddingRight();

        if (isShowText) {
            float ratio = getProgress() * 1.0f / getMax();
            int unreachedWidth = (int) ((contentWidth - textWidth) * (1 - ratio));
            int reachedWidth = contentWidth - textWidth - unreachedWidth;

            paint.setColor(reachedColor);
            paint.setStrokeWidth(lineHeight);

            int startX = getPaddingLeft();
            int startY = height / 2;
            int stopX = getPaddingLeft() + reachedWidth;
            int stopY = height / 2;
            canvas.drawRoundRect((float) startX, (float) (startY - 10), (float) stopX, (float) (stopY + DEFAULT_LINE_HEIGHT - 10), (float) 15, (float) 15, paint);
            // canvas.drawLine(startX, startY, stopX, stopY, paint);
            String currentText = "";
            WriteLog.writeTxtToFile("zzprogressbar-------------"+isFinished+"hhhhgdg");
            if(isFinished) {
                currentText = "完成";
            }else{
                currentText = textOver + "%";
            }
            paint.getTextBounds(currentText, 0, currentText.length(), textBound);

            paint.setColor(getResources().getColor(R.color.green));
            paint.setTextSize(textSize);
            startX = getPaddingLeft() + reachedWidth + (textWidth - textBound.width()) / 2;
            Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
            startY = (height - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
            canvas.drawText(currentText, startX, startY, paint);
//            Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.color.green);
//            canvas.drawBitmap(bitmap,0,0,null);
            paint.setColor(unreachedColor);
            // paint.setStrokeWidth(lineHeight);
            startX = getPaddingLeft() + reachedWidth + textWidth;
            startY = height / 2;
            stopX = width - getPaddingRight();
            stopY = height / 2;
            canvas.drawRoundRect((float) startX, (float) (startY - 10), (float) stopX, (float) (stopY + DEFAULT_LINE_HEIGHT - 10), (float) 15, (float) 15, paint);
            //canvas.drawLine(startX, startY, stopX, stopY, paint);
        } else {
            float ratio = getProgress() * 1.0f / getMax();
            int unreachedWidth = (int) (contentWidth * (1 - ratio));
            int reachedWidth = contentWidth - unreachedWidth;

            paint.setColor(reachedColor);
            paint.setStrokeWidth(lineHeight);

            int startX = getPaddingLeft();
            int startY = height / 2;
            int stopX = getPaddingLeft() + reachedWidth;
            int stopY = height / 2;

            canvas.drawLine(startX, startY, stopX, stopY, paint);

            paint.setColor(unreachedColor);
            paint.setStrokeWidth(lineHeight);
            startX = getPaddingLeft() + reachedWidth;
            startY = height / 2;
            stopX = width - getPaddingRight();
            stopY = height / 2;
            canvas.drawLine(startX, startY, stopX, stopY, paint);
        }
    }

    private void obtainStyledAttributes(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ZProgressbar, defStyleAttr, 0);

        int count = a.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.ZProgressbar_reachedColor) {
                reachedColor = a.getColor(attr, DEFAULT_REACHED_COLOR);
            } else if (attr == R.styleable.ZProgressbar_unreachedColor) {
                unreachedColor = a.getColor(attr, DEFAULT_UNREACHED_COLOR);
            } else if (attr == R.styleable.ZProgressbar_textColor) {
                textColor = a.getColor(attr, DEFAULT_TEXT_COLOR);
            } else if (attr == R.styleable.ZProgressbar_textSize) {
                textSize = sp2px((int) a.getDimension(attr, DEFAULT_TEXT_SIZE));
            } else if (attr == R.styleable.ZProgressbar_isShowText) {
                isShowText = a.getBoolean(attr, true);
            }
        }

        a.recycle();
    }

    /**
     * dp 2 px
     *
     * @param dpVal
     */
    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    /**
     * sp 2 px
     *
     * @param spVal
     * @return
     */
    protected int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, getResources().getDisplayMetrics());

    }
}

