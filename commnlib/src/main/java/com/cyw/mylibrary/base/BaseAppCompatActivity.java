package com.cyw.mylibrary.base;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cyw.mylibrary.R;
import com.cyw.mylibrary.application.AppApplication;

import me.jessyan.autosize.internal.CustomAdapt;

/**
 * author： cyw
 */
public class BaseAppCompatActivity extends AppCompatActivity implements CustomAdapt {//implements CustomAdapt
    private static float sNoncompatDensity;// 系统的Density
    private static float sNoncompatScaleDensity;// 系统的ScaledDensity

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppApplication.getInstance().addActivitys(this);
    }

    @Override
    public boolean isBaseOnWidth() {
        return false;
    }

    @Override
    public float getSizeInDp() {
        return 857;
    }

//    public static void setCustomDensity(Activity activity, final Application application) {
//        final DisplayMetrics appDisplayMetrics = application.getResources().getDisplayMetrics();
//
//        if (sNoncompatDensity == 0) {
//            // 系统的Density
//            sNoncompatDensity = appDisplayMetrics.density;
//            // 系统的ScaledDensity
//            sNoncompatScaleDensity = appDisplayMetrics.scaledDensity;
//            // 监听在系统设置中切换字体
//            application.registerComponentCallbacks(new ComponentCallbacks() {
//                @Override
//                public void onConfigurationChanged(@NonNull Configuration newConfig) {
//                    if (newConfig != null && newConfig.fontScale > 0) {
//                        sNoncompatScaleDensity = application.getResources().getDisplayMetrics().scaledDensity;
//                    }
//                }
//
//                @Override
//                public void onLowMemory() {
//
//                }
//            });
//        }
//        // 此处以360dp的设计图作为例子
//        final float targetDensity = appDisplayMetrics.widthPixels / 1371;
//        final float targetScaledDensity = targetDensity * (sNoncompatScaleDensity / sNoncompatDensity);
//        final int targetDensityDpi = (int) (160 * targetDensity);
//
//        appDisplayMetrics.density = targetDensity;
//        appDisplayMetrics.scaledDensity = targetScaledDensity;
//        appDisplayMetrics.densityDpi = targetDensityDpi;
//
//        final DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
//        activityDisplayMetrics.density = targetDensity;
//        activityDisplayMetrics.scaledDensity = targetScaledDensity;
//        activityDisplayMetrics.densityDpi = targetDensityDpi;
//    }
    public void startActivityAni(Intent it){
        this.startActivity(it);
        this.overridePendingTransition(R.anim.basepopup_fade_in,R.anim.basepopup_fade_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppApplication.getInstance().removeActivity(this);
    }
}
