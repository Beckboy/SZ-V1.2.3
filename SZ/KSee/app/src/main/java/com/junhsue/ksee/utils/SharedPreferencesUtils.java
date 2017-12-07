package com.junhsue.ksee.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.junhsue.ksee.frame.MyApplication;

/**
 * SharedPreferences帮助类
 */
public class SharedPreferencesUtils {
    public static final String KSEE = "KSEE";
    /**
     * 应用第一次启动
     */
    public static final String KEY_LOGIN = "some_times";//用来判断是否是第一次启动app,便于用于引导页
    public static final String VALUE_LOGIN = "second";//统计登录时间,可以预留以后超时链接,

    public static final String UUID = "device_id.xml";
    public static final String KEY_UUID = "device_id";

    private static SharedPreferencesUtils sharedPreferencesUtils;
    private static SharedPreferences sharedPreferences;

    private SharedPreferencesUtils() {
    }

    /**
     * 获取一个app默认的SharedPreferencesUtils
     */
    public synchronized static SharedPreferencesUtils getInstance() {
        sharedPreferencesUtils = getInstance(MyApplication.getApplication().getApplicationContext());
        return sharedPreferencesUtils;

    }

    /**
     * 获取一个SharedPreferencesUtils实例,并创建SharedPreferences
     */
    public synchronized static SharedPreferencesUtils getInstance(Context context, String name) {
        if (sharedPreferencesUtils == null) {
            sharedPreferencesUtils = new SharedPreferencesUtils();
        }
        sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sharedPreferencesUtils;

    }

    /**
     * 获取一个SharedPreferencesUtils实例,并创建SharedPreferences
     * <p/>
     * 默认的都写入FM820，创建该FM820的sharedPreferences是放在引导页中，其余的均可以用这个方法获取
     */
    public synchronized static SharedPreferencesUtils getInstance(Context context) {
        sharedPreferencesUtils = getInstance(context, KSEE);
        return sharedPreferencesUtils;
    }

    /**
     * 将传入的key和value放入到SharedPreferences中
     */
    public void putString(String key, String value) {
        if (sharedPreferences == null) {
            return;
        }
        Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 根据传入的key和defValue从SharedPreferences中获取相应的值
     */
    public String getString(String key, String defValue) {
        if (sharedPreferences == null) {
            return defValue;
        }
        String value = sharedPreferences.getString(key, defValue);
        return value;
    }


    /**
     * 将传入的key和value放入到SharedPreferences中
     */
    public void putBoolean(String key, boolean value) {
        if (sharedPreferences == null) {
            return;
        }
        Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * 根据传入的key和defValue从SharedPreferences中获取相应的值
     */
    public boolean getBoolean(String key, boolean defValue) {
        if (sharedPreferences == null) {
            return defValue;
        }
        boolean value = sharedPreferences.getBoolean(key, defValue);
        return value;
    }


    /**
     * 将传入的key和value放入到SharedPreferences中
     */
    public void putInt(String key, int value) {
        if (sharedPreferences == null) {
            return;
        }
        Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * 根据传入的key和defValue从SharedPreferences中获取相应的值
     */
    public int getInt(String key, int defValue) {
        if (sharedPreferences == null) {
            return defValue;
        }
        int value = sharedPreferences.getInt(key, defValue);
        return value;
    }


    /**
     * 将传入的key和value放入到SharedPreferences中
     */
    public void putLong(String key, long value) {
        if (sharedPreferences == null) {
            return;
        }
        Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * 根据传入的key和defValue从SharedPreferences中获取相应的值
     */
    public long getLong(String key, long defValue) {
        if (sharedPreferences == null) {
            return defValue;
        }
        long value = sharedPreferences.getLong(key, defValue);
        return value;
    }

}
