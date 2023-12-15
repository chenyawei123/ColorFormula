package com.cyw.mylibrary.util;

//import android.content.Context;
//import android.content.SharedPreferences;
//import android.text.TextUtils;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.io.Serializable;
//import java.util.List;
//import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 保存信息配置类
 *
 * @author admin
 */
public class SharedPreferencesHelper {
//    private SharedPreferences sharedPreferences;
//    /*
//     * 保存手机里面的名字
//     */private SharedPreferences.Editor editor;
//
//    public SharedPreferencesHelper(Context context,String FILE_NAME) {
//        sharedPreferences = context.getSharedPreferences(FILE_NAME,
//                Context.MODE_PRIVATE);
//        editor = sharedPreferences.edit();
//    }
//
//    /**
//     * 存储
//     */
//    public void put(String key, Object object) {
//        if (object instanceof String) {
//            editor.putString(key, (String) object);
//        } else if (object instanceof Integer) {
//            editor.putInt(key, (Integer) object);
//        } else if (object instanceof Boolean) {
//            editor.putBoolean(key, (Boolean) object);
//        } else if (object instanceof Float) {
//            editor.putFloat(key, (Float) object);
//        } else if (object instanceof Long) {
//            editor.putLong(key, (Long) object);
//        } else {
//            editor.putString(key, object.toString());
//        }
//        editor.commit();
//    }
//
//    /**
//     * 获取保存的数据
//     */
//    public Object getSharedPreference(String key, Object defaultObject) {
//        if (defaultObject instanceof String) {
//            return sharedPreferences.getString(key, (String) defaultObject);
//        } else if (defaultObject instanceof Integer) {
//            return sharedPreferences.getInt(key, (Integer) defaultObject);
//        } else if (defaultObject instanceof Boolean) {
//            return sharedPreferences.getBoolean(key, (Boolean) defaultObject);
//        } else if (defaultObject instanceof Float) {
//            return sharedPreferences.getFloat(key, (Float) defaultObject);
//        } else if (defaultObject instanceof Long) {
//            return sharedPreferences.getLong(key, (Long) defaultObject);
//        } else {
//           // return sharedPreferences.getString(key, null);
//            return sharedPreferences.getString(key, defaultObject.toString());
//        }
//    }
//
//
//    /**
//     * 保存List
//     * @param tag
//     * @param datalist
//     */
//    public <T> void setDataList(String tag, List<T> datalist) {
//        if (null == datalist || datalist.size() <= 0)
//            return;
//
//        Gson gson = new Gson();
//        //转换成json数据，再保存
//        String strJson = gson.toJson(datalist);
//        editor.clear();
//        editor.putString(tag, strJson);
//        editor.commit();
//
//    }
//
//    /**
//     * 获取List
//     * @param tag
//     * @return
//     */
//    public <T> List<T> getDataList(String tag) {
//        List<T> datalist=new ArrayList<T>();
//        String strJson = sharedPreferences.getString(tag, null);
//        if (null == strJson) {
//            return datalist;
//        }
//        Gson gson = new Gson();
//        datalist = gson.fromJson(strJson, new TypeToken<List<T>>() {
//        }.getType());
//        return datalist;
//
//    }
//
//
//    /**
//     * 移除某个key值已经对应的值
//     */
//    public void remove(String key) {
//        editor.remove(key);
//        editor.commit();
//    }
//
//    /**
//     * 清除所有数据
//     */
//    public void clear() {
//        editor.clear();
//        editor.commit();
//    }
//
//    /**
//     * 查询某个key是否存在
//     */
//    public Boolean contain(String key) {
//        return sharedPreferences.contains(key);
//    }
//
//    /**
//     * 返回所有的键值对
//     */
//    public Map<String, ?> getAll() {
//        return sharedPreferences.getAll();
//    }




    private static SharedPreferences sp;

    private static SharedPreferences getSp(Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences("SpUtil", Context.MODE_PRIVATE);
        }
        return sp;
    }


    /**
     * 存入字符串
     *
     * @param context 上下文
     * @param key     字符串的键
     * @param value   字符串的值
     */
    public static void putString(Context context, String key, String value) {
        SharedPreferences preferences = getSp(context);
        //存入数据
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();//apply替换commit
    }

    /**
     * 获取字符串
     *
     * @param context 上下文
     * @param key     字符串的键
     * @return 得到的字符串
     */
    public static String getString(Context context, String key) {
        SharedPreferences preferences = getSp(context);
        return preferences.getString(key, "");
    }

    /**
     * 获取字符串
     *
     * @param context 上下文
     * @param key     字符串的键
     * @param value   字符串的默认值
     * @return 得到的字符串
     */
    public static String getString(Context context, String key, String value) {
        SharedPreferences preferences = getSp(context);
        return preferences.getString(key, value);
    }

    /**
     * 保存布尔值
     *
     * @param context 上下文
     * @param key     键
     * @param value   值
     */
    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sp     = getSp(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.apply();//apply替换commit
    }

    /**
     * 获取布尔值
     *
     * @param context  上下文
     * @param key      键
     * @param defValue 默认值
     * @return 返回保存的值
     */
    public static boolean getBoolean(Context context, String key, boolean defValue) {
        SharedPreferences sp = getSp(context);
        return sp.getBoolean(key, defValue);
    }

    /**
     * 保存long值
     *
     * @param context 上下文
     * @param key     键
     * @param value   值
     */
    public static void putLong(Context context, String key, long value) {
        SharedPreferences sp     = getSp(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key, value);
        editor.apply();//apply替换commit
    }

    /**
     * 获取long值
     *
     * @param context  上下文
     * @param key      键
     * @param defValue 默认值
     * @return 保存的值
     */
    public static long getLong(Context context, String key, long defValue) {
        SharedPreferences sp = getSp(context);
        return sp.getLong(key, defValue);
    }

    public static void putFloat(Context context, String key, float value) {
        SharedPreferences sp     = getSp(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat(key, value);
        editor.apply();//apply替换commit
    }

    /**
     * 获取long值
     *
     * @param context  上下文
     * @param key      键
     * @param defValue 默认值
     * @return 保存的值
     */
    public static float getFloat(Context context, String key, float defValue) {
        SharedPreferences sp = getSp(context);
        return sp.getFloat(key, defValue);
    }

    /**
     * 保存int值
     *
     * @param context 上下文
     * @param key     键
     * @param value   值
     */
    public static void putInt(Context context, String key, int value) {
        SharedPreferences sp     = getSp(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.apply();//apply替换commit
    }

    /**
     * 获取long值
     *
     * @param context  上下文
     * @param key      键
     * @param defValue 默认值
     * @return 保存的值
     */
    public static int getInt(Context context, String key, int defValue) {
        SharedPreferences sp = getSp(context);
        return sp.getInt(key, defValue);
    }

    /**
     * 保存对象
     *
     * @param context 上下文
     * @param key     键
     * @param obj     要保存的对象（Serializable的子类）
     * @param <T>     泛型定义
     */
    public static <T extends Serializable> void putObject(Context context, String key, T obj) {
        try {
            put(context, key, obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取对象
     *
     * @param context 上下文
     * @param key     键
     * @param <T>     指定泛型
     * @return 泛型对象
     */
    public static <T extends Serializable> T getObject(Context context, String key) {
        try {
            return (T) get(context, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static <T extends Parcelable> void putObject2(Context context, String key, T obj) {
        try {
            put(context, key, obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取对象
     *
     * @param context 上下文
     * @param key     键
     * @param <T>     指定泛型
     * @return 泛型对象
     */
    public static <T extends Parcelable> T getObject2(Context context, String key) {
        try {
            return (T) get(context, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 存储List集合
     * @param context 上下文
     * @param key 存储的键
     * @param list 存储的集合
     */
    public static void putList(Context context, String key, List<? extends Serializable> list) {
        try {
            put(context, key, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取List集合
     * @param context 上下文
     * @param key 键
     * @param <E> 指定泛型
     * @return List集合
     */
    public static <E extends Serializable> List<E> getList(Context context, String key) {
        try {
            return (List<E>) get(context, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 存储Map集合
     * @param context 上下文
     * @param key 键
     * @param map 存储的集合
     * @param <K> 指定Map的键
     * @param <V> 指定Map的值
     */
    public static <K extends Serializable, V extends Serializable> void putMap(Context context,
                                                                               String key, Map<K, V> map)
    {
        try {
            put(context, key, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <K extends Serializable, V extends Serializable> Map<K, V> getMap(Context context,
                                                                                    String key)
    {
        try {
            return (Map<K, V>) get(context, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**存储对象*/
    private static void put(Context context, String key, Object obj)
            throws IOException
    {
        if (obj == null) {//判断对象是否为空
            return;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos  = null;
        oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        // 将对象放到OutputStream中
        // 将对象转换成byte数组，并将其进行base64编码
        String objectStr = new String(android.util.Base64.encode(baos.toByteArray(), android.util.Base64.DEFAULT));
        baos.close();
        oos.close();

        putString(context, key, objectStr);
    }

    /**获取对象*/
    private static Object get(Context context, String key)
            throws IOException, ClassNotFoundException
    {
        String wordBase64 = getString(context, key);
        // 将base64格式字符串还原成byte数组
        if (TextUtils.isEmpty(wordBase64)) { //不可少，否则在下面会报java.io.StreamCorruptedException
            return null;
        }
        byte[] objBytes = android.util.Base64.decode(wordBase64.getBytes(), android.util.Base64.DEFAULT);
        ByteArrayInputStream bais     = new ByteArrayInputStream(objBytes);
        ObjectInputStream ois      = new ObjectInputStream(bais);
        // 将byte数组转换成product对象
        Object obj = ois.readObject();
        bais.close();
        ois.close();
        return obj;
    }
    public  static void remove(Context context,String key){
        SharedPreferences sp  = getSp(context);
        SharedPreferences.Editor editor = sp.edit();

        editor.remove(key);
        //editor.clear();
        editor.apply();//apply替换commit

    }


}
