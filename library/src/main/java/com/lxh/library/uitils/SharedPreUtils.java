package com.lxh.library.uitils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Caoxiangyu  2014.6.5
 * SharedPre封装工具类
 */
public class SharedPreUtils {

    private static SharedPreferences preferences;

    /**
     * @param context
     * @return
     */
    public synchronized static void getSharedPreferencesInstance(Context context) {
        if (preferences == null) {
            preferences = context.getSharedPreferences("lxh", Context.MODE_PRIVATE);
        }
    }

    public static final byte STRING = 0;
    public static final byte INT = 1;
    public static final byte BOOLEAN = 2;
    public static final byte LONG = 3;

    /**
     * @param key     键
     * @param value   值
     * @param type    传入值的类型
     * @param context 上下文
     * @type类型 SharedPreUtils.STRING， SharedPreUtils.INT，
     * SharedPreUtils.BOOLEAN， SharedPreUtils.LONG
     */
    public static void putValue(String key, Object value, byte type, Context context) {
        SharedPreUtils.getSharedPreferencesInstance(context);
        Editor edit = preferences.edit();
        switch (type) {
            case STRING:
                edit.putString(key, (String) value);
                break;
            case INT:
                edit.putInt(key, (Integer) value);
                break;
            case BOOLEAN:
                edit.putBoolean(key, (Boolean) value);
                break;
            case LONG:
                edit.putLong(key, (Long) value);
                break;
        }
        edit.commit();
    }

    /**
     * 实做数据存储
     *
     * @param key     键
     * @param value   值
     * @param context 上下文
     */
    private static void putValue(String key, Object value, Context context) {
        SharedPreUtils.getSharedPreferencesInstance(context);
        Editor editor = preferences.edit();
        if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else {
            editor.putString(key, (String) value);
        }
        editor.apply();
        editor.commit();
    }

    /**
     * @param key     键
     * @param value   值
     * @param context 上下文
     */
    public static void putString(String key, String value, Context context) {
        putValue(key, value, context);
    }

    public static void putString(String key, String[] phone, Context context) {
        putValue(key, phone, context);
    }

    public static void getString(String key, String[] phone, Context context) {
        putValue(key, phone, context);
    }

    /**
     * @param key     键
     * @param value   值
     * @param context 上下文
     */
    public static void putInt(String key, int value, Context context) {
        putValue(key, value, context);
    }

    /**
     * @param key     键
     * @param value   值
     * @param context 上下文
     */
    public static void putLong(String key, long value, Context context) {
        putValue(key, value, context);
    }

    /**
     * @param key     键
     * @param value   值
     * @param context 上下文
     */
    public static void putFloat(String key, float value, Context context) {
        putValue(key, value, context);
    }

    /**
     * @param key     键
     * @param value   值
     * @param context 上下文
     */
    public static void putBoolean(String key, boolean value, Context context) {
        putValue(key, value, context);
    }

    /**
     * @param key      键
     * @param defValue 默认值
     * @param context  上下文
     * @return
     */
    public static String getString(String key, String defValue, Context context) {
        SharedPreUtils.getSharedPreferencesInstance(context);
        return preferences.getString(key, defValue);
    }

    /**
     * @param key     键
     * @param context 上下文
     * @return
     */
    public static String getString(String key, Context context) {
        SharedPreUtils.getSharedPreferencesInstance(context);
        return preferences.getString(key, "");
    }

    /**
     * @param key      键
     * @param defValue 默认值
     * @param context  上下文
     * @return
     */
    public static int getInt(String key, int defValue, Context context) {
        SharedPreUtils.getSharedPreferencesInstance(context);
        return preferences.getInt(key, defValue);
    }

    /**
     * @param key     键
     * @param context 上下文
     * @return
     */
    public static int getInt(String key, Context context) {
        SharedPreUtils.getSharedPreferencesInstance(context);
        return preferences.getInt(key, 0);
    }

    /**
     * @param key      键
     * @param defValue 默认值
     * @param context  上下文
     * @return
     */
    public static long getLong(String key, long defValue, Context context) {
        SharedPreUtils.getSharedPreferencesInstance(context);
        return preferences.getLong(key, defValue);
    }

    /**
     * @param key     键
     * @param context 上下文
     * @return
     */
    public static long getLong(String key, Context context) {
        SharedPreUtils.getSharedPreferencesInstance(context);
        return preferences.getLong(key, 0);
    }

    /**
     * @param key      键
     * @param defValue 默认值
     * @param context  上下文
     * @return
     */
    public static float getFloat(String key, float defValue, Context context) {
        SharedPreUtils.getSharedPreferencesInstance(context);
        return preferences.getFloat(key, defValue);
    }

    /**
     * @param key     键
     * @param context 上下文
     * @return
     */
    public static float getFloat(String key, Context context) {
        SharedPreUtils.getSharedPreferencesInstance(context);
        return preferences.getFloat(key, (float) 0.0);
    }

    /**
     * @param key      键
     * @param defValue 默认值
     * @param context  上下文
     * @return
     */
    public static boolean getBoolean(String key, boolean defValue, Context context) {
        SharedPreUtils.getSharedPreferencesInstance(context);
        return preferences.getBoolean(key, defValue);
    }

    /**
     * @param key     键
     * @param context 上下文
     * @return
     */
    public static boolean getBoolean(String key, Context context) {
        SharedPreUtils.getSharedPreferencesInstance(context);
        return preferences.getBoolean(key, false);
    }

    /**
     * @param key     键
     * @param context 上下文
     * @return
     */
    public static boolean contains(String key, Context context) {
        SharedPreUtils.getSharedPreferencesInstance(context);
        return preferences.contains(key);
    }

    /**
     * @param key     键
     * @param type    传入值的类型
     * @param context 上下文
     * @type类型 SharedPreUtils.STRING， SharedPreUtils.INT，
     * SharedPreUtils.BOOLEAN， SharedPreUtils.LONG
     */
    public static Object getValue(String key, byte type, Context context) {
        SharedPreUtils.getSharedPreferencesInstance(context);
        switch (type) {
            case STRING:
                return preferences.getString(key, "");
            case INT:
                return preferences.getInt(key, -1);
            case BOOLEAN:
                return preferences.getBoolean(key, true);
            case LONG:
                return preferences.getLong(key, 0);
        }
        return null;
    }

    /**
     * @param key
     * @param type
     * @param defValue
     * @param context  上下文
     * @return
     */
    public static Object getValue(String key, byte type, Object defValue, Context context) {
        SharedPreUtils.getSharedPreferencesInstance(context);
        switch (type) {
            case STRING:
                return preferences.getString(key, (String) defValue);
            case INT:
                return preferences.getInt(key, (Integer) defValue);
            case BOOLEAN:
                return preferences.getBoolean(key, (Boolean) defValue);
            case LONG:
                return preferences.getLong(key, (Long) defValue);
        }
        return null;
    }


    public static void clear() {

        Editor et = preferences.edit();
        et.clear();
        et.commit();
    }


}
