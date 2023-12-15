package com.santint.mypermission.permissions.aspect;

import android.content.Context;

import androidx.fragment.app.Fragment;

import com.santint.mypermission.IPermissionListener;
import com.santint.mypermission.MyPermissionActivity;
import com.santint.mypermission.PermissionUtils;
import com.santint.mypermission.permissions.annotation.Permission;
import com.santint.mypermission.permissions.annotation.PermissionCancel;
import com.santint.mypermission.permissions.annotation.PermissionDenied;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * author： cyw
 */
@Aspect
public class PermissionAspect {
    // AOP 思维 切面的思维

    // 切点 --- 是注解 @
    // * * 任何函数 都可以使用 此注解
    @Pointcut("execution(@com.santint.mypermission.permissions.annotation.Permission * *(..)) && @annotation(permission)")
    public void pointActionMethod(Permission permission){

    }
    //切面
    @Around("pointActionMethod(permission)")
    // final ProceedingJoinPoint point == AspectJ的API类 （NDK JNI JNIEnv evn）
    public void aProceedingJoinPoint(final ProceedingJoinPoint point,Permission permission) throws Throwable{
        Context context = null;
       final Object aThis = point.getThis();
        if(aThis instanceof Context){
            context =(Context)aThis;
        }else if(aThis instanceof Fragment){
            context = ((Fragment)aThis).getActivity();
        }
        if(null == context || permission == null){
            throw new IllegalAccessException("null == contex || permission == null");
        }
        MyPermissionActivity.requestPermissionAction(context,permission.value(),permission.requestCode(), new IPermissionListener() {
            @Override
            public void ganted() {
                //授权成功
                try {
                    point.proceed();// 允许 该 @Permission 的 函数 执行
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }

            @Override
            public void cancel() {
                // 授权取消

                // 反射 @PermissionCancel 修饰的 函数
                // MainActivity this
                PermissionUtils.invokeAnnotion(aThis, PermissionCancel.class);
            }

            @Override
            public void denied() {
                // 授权拒绝

                // 反射  @PermissionDenied 修饰的 函数
                // MainActivity this
                PermissionUtils.invokeAnnotion(aThis, PermissionDenied.class);
            }
        });

    }
}
