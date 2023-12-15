package com.example.datacorrects.inject;

import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * author： cyw
 */
public class InjectUtils {
    public static void injectView(Object object){
       reflectFindView(object,null);
       injectMethod(object,null);
    }
    public static void injectView(Object object,View view){
        reflectFindView(object,view);
        injectMethod(object,view);
    }
    private static void reflectFindView(Object object,View view){
        Class<? > cls = object.getClass();
        Field[] declaredFileds = cls.getDeclaredFields();
        for(Field field:declaredFileds){
            if(field.isAnnotationPresent(InjectView.class)){
                InjectView injectView = field.getAnnotation(InjectView.class);
                int id = injectView.value();
                reflectFindView(object,view,field,id);
            }
        }
    }
    private static void reflectFindView(Object object,View view,Field field,int id){
        if(id == -1){
            return;
        }
        Class<?> fieldCls = object.getClass();
        Class<?> findViewClass = (view == null?object:view).getClass();
        try {
            Method method = (findViewClass == null?fieldCls:findViewClass).getMethod("findViewById",int.class);
            //执行该方法，返回一个object类型的view实例
            try {
                Object resView = method.invoke(view == null ?object:view,id);
                field.setAccessible(true);
                //把字段的值设置为该view的实例
                field.set(object,resView);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    /**
     * Annotation method
     *
     * @param object annotation class with declared methods
     * @param view   annotation view in Fragment or ViewHolder
     */
    private static void injectMethod(Object object, View view) {
        long time = System.currentTimeMillis();
        reflectMethod(object, view);

    }
    private static void reflectMethod(Object object,View view){
        Class<?> objectClass = object.getClass();
        Method[] methods = objectClass.getDeclaredMethods();
        for(int i=0;i<methods.length;i++){
            Method method = methods[i];
            method.setAccessible(true);
            OnClick onClick = method.getAnnotation(OnClick.class);
            if(onClick!=null){
                int [] values = onClick.value();
                for(int index = 0;index<values.length;index++){
                    int id = values[index];
                    reflectMethod(object,view,id,index,method);
                }
            }
        }
    }
    private static void reflectMethod(final Object object,View view,int id,int index,final Method method){
        Class<?> objectClass = object.getClass();
        try {
            Method findViewMethod  = (view == null?objectClass:view.getClass()).getMethod("findViewById",int.class);
            try {
                final View resView = (View)findViewMethod.invoke(object,id);//id 不是对象也可以吗
                if(resView == null){
                    return;
                }
                resView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            method.invoke(object,resView);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }


}
