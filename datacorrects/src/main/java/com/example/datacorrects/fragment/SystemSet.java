package com.example.datacorrects.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cyw.mylibrary.base.BaseFragment;
import com.cyw.mylibrary.util.SharedPreferencesHelper;
import com.example.datacorrects.R;
import com.example.datacorrects.customView.DrawableEditText;

/**
 * author： cyw
 */
public class SystemSet extends BaseFragment {
    private View decorView;
    private CheckBox checkBox,checkBoxHigh,checkBoxLarge,checkBoxLeft,checkBoxRight;
    private boolean isFloat = true;
    private boolean isHigh = true;
    private boolean isLeft = true;
    private DrawableEditText drawableEditText;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        decorView = inflater.inflate(R.layout.system_set,container,false);
        isFloat = SharedPreferencesHelper.getBoolean(getMyActivity(),"isFloat",true);
        isHigh = SharedPreferencesHelper.getBoolean(getMyActivity(),"isHigh",true);
        isLeft = SharedPreferencesHelper.getBoolean(getMyActivity(),"isLeft",true);
        initView();
        return decorView;
    }

    private void initView() {
        checkBox = decorView.findViewById(R.id.check_box);
        checkBoxHigh = decorView.findViewById(R.id.check_box_high);
        checkBoxLarge = decorView.findViewById(R.id.check_box_large);
        checkBoxLeft = decorView.findViewById(R.id.check_box_left);
        checkBoxRight = decorView.findViewById(R.id.check_box_right);
        drawableEditText = decorView.findViewById(R.id.drawable_edittext);
        drawableEditText.getEditText().setText("0.02");
        String value = drawableEditText.getEditText().getText().toString();
        SharedPreferencesHelper.putFloat(getMyActivity(),"FloatValue",Float.parseFloat(value));
        checkBox.setChecked(isFloat);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               SharedPreferencesHelper.putBoolean(getMyActivity(),"isFloat",isChecked);
            }
        });

        checkBoxHigh.setChecked(isHigh);
        checkBoxHigh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkBoxLarge.setChecked(!isChecked);
                SharedPreferencesHelper.putBoolean(getMyActivity(),"isHigh",isChecked);

            }
        });

        checkBoxLarge.setChecked(!isHigh);
        checkBoxLarge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkBoxHigh.setChecked(!isChecked);
                SharedPreferencesHelper.putBoolean(getMyActivity(),"isHigh",!isChecked);
            }
        });



        checkBoxLeft.setChecked(isLeft);
        checkBoxLeft.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkBoxRight.setChecked(!isChecked);
                SharedPreferencesHelper.putBoolean(getMyActivity(),"isLeft",isChecked);

            }
        });

        checkBoxRight.setChecked(!isLeft);
        checkBoxRight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkBoxLeft.setChecked(!isChecked);
                SharedPreferencesHelper.putBoolean(getMyActivity(),"isLeft",!isChecked);
            }
        });
    }
}
