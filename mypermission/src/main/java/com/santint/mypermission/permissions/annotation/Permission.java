package com.santint.mypermission.permissions.annotation;

import com.santint.mypermission.MyPermissionActivity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * author： cyw
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Permission {
    String[] value(); // 具体申请的权限

    // 默认的  权限码是为了以后扩展的，目前用不到，备用的，为了扩展的
    int requestCode() default MyPermissionActivity.PARAM_PERMSSION_CODE_DEFAULT;
}
