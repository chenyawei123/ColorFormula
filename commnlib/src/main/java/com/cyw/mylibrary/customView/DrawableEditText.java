package com.cyw.mylibrary.customView;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cyw.mylibrary.R;
import com.cyw.mylibrary.util.ComputeDoubleUtil;


/**
 * author： cyw
 */
public class DrawableEditText extends RelativeLayout implements View.OnClickListener {
    private Context mContext;
    private EditText mEditText;
    private ImageView mImageTop, mImageBottom;
    private int imgRes;
    private int nullImgRes;
    private TextView tvUnit;

    public DrawableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
        imgRes = attrs.getAttributeIntValue(null, "imgRes", 0);
        nullImgRes = attrs.getAttributeIntValue(null, "nullImgRes", 0);
        if (nullImgRes != 0) {
            setDrawable();
        }
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.drawable_edittext, this);
        mEditText = view.findViewById(R.id.edit_text);
        mImageTop = view.findViewById(R.id.image);
        mImageBottom = view.findViewById(R.id.image2);
        mImageTop.setOnClickListener(this);
        mImageBottom.setOnClickListener(this);
        tvUnit = view.findViewById(R.id.tv_unit);
    }

    private void setDrawable() {
//        if(mEditText.getText().toString().equals("")){
//            mImage.setImageResource(nullImgRes);
//        }else{
        //mImage.setImageResource(imgRes);
        // }
    }

    public boolean isEmpty() {
        return getText() == null || getText().length() == 0 ? true : false;
    }

    public String getText() {
        return mEditText.getText().toString();
    }

    public void setText(String s) {
        mEditText.setText(s);
    }

    public void setTextUnit(String s) {
        tvUnit.setText(s);
    }

    public EditText getEditText() {
        return mEditText;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setDrawable();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.image) {
            if (getText() != null && getText().length() > 0) {
                double original = Double.parseDouble(getText());
                double now = original + 1;
                String format = ComputeDoubleUtil.computeDouble(now);;
                setText(format);
            }

        } else if (id == R.id.image2) {
            if (getText() != null && getText().length() > 0) {
                double original = Double.parseDouble(getText());
                if (original >= 1) {
                    double now = original - 1;
                    String format = ComputeDoubleUtil.computeDouble(now);;
                    setText(format);
                }
            }


        }
    }
}
