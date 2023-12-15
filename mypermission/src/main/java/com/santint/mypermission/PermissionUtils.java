package com.santint.mypermission;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * author： cyw
 */
public class PermissionUtils {
    public static boolean hasPermissionRequest(Context context,String... permissions){
        for(String permission:permissions){
            if(isPermissionRequest(context,permission) == false){
                return false;
            }
        }
        return true;

    }
    private static boolean isPermissionRequest(Context context,String permission){
        try {
            int checkSelfPermission = ContextCompat.checkSelfPermission(context, permission);
            return checkSelfPermission == PackageManager.PERMISSION_GRANTED;
        } catch (Exception e) {
            return false;
        }
    }
    public static boolean requestPermissionSuccess(int... grantedResult){
        if(grantedResult == null || grantedResult.length<=0){
            return false;
        }
        for(int permissionValue:grantedResult){
            if(permissionValue!=PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }
    public static boolean shouldShowRequestPermissionRationale(Activity activity,String... permissions){
        for(String permission:permissions){
            if(ActivityCompat.shouldShowRequestPermissionRationale(activity,permission)){
                return true;
            }
        }
        return false;
    }
    // TODO 专门去 回调 MainActivity (被@PermissionCancel/PermissionDenied) 的 函数
    public static void invokeAnnotion(Object object, Class annotionClass) {
        Class<?> objectClass = object.getClass(); // 可能是 MainActivity this

        // 遍历所有函数
        Method[] methods = objectClass.getDeclaredMethods(); // 得到所有的 MainActivity的 公开 私有 函数
        for (Method method : methods) {
            method.setAccessible(true); // 让虚拟机不要去检测 private

            // 判断是否被 annotionClass 注解过的函数
            boolean annotationPresent = method.isAnnotationPresent(annotionClass); // PermissionCancel == annotationPresent

            if (annotationPresent) {
                // 当前方法 annotionClass 注解过的函数
                try {
                    method.invoke(object);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
