package com.wow.carlauncher.common.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreUtil {
    // SharedPreference文件的文件名
    public final static String XML_Settings = "settings";
    private static SharedPreferences settings;

    private static Context context;

    public static void init(Context context) {
        SharedPreUtil.context = context;
    }

    /**
     * @param key   键
     * @param value 值
     * @category 保存String键值对
     */
    public static void saveSharedPreString(String key, String value) {
        settings = context.getSharedPreferences(XML_Settings, Context.MODE_PRIVATE);
        settings.edit().putString(key, value).apply();
    }

    /**
     * @param key 键
     * @category 获取String键值对
     */
    public static String getSharedPreString(String key) {
        settings = context.getSharedPreferences(XML_Settings, Context.MODE_PRIVATE);
        return settings.getString(key, "");
    }

    /**
     * @param key 键
     * @category 获取String键值对
     */
    public static String getSharedPreString(String key, String value) {
        settings = context.getSharedPreferences(XML_Settings, Context.MODE_PRIVATE);
        return settings.getString(key, value);
    }

    /**
     * @param key 键
     * @return
     * @category 获取boolean键值对
     */
    public static boolean getSharedPreBoolean(String key, boolean value) {
        settings = context.getSharedPreferences(XML_Settings, 0);
        return settings.getBoolean(key, value);
    }

    /**
     * @param key   键
     * @param value 值
     * @category 保存boolean键值对
     */
    public static void saveSharedPreBoolean(String key, boolean value) {
        settings = context.getSharedPreferences(XML_Settings, 0);
        settings.edit().putBoolean(key, value).apply();
    }

    /**
     * @param key   键
     * @param value 值
     * @category 保存int类型的数据到SharedPreference配置文件
     */
    public static void saveSharedPreInteger(String key, int value) {
        settings = context.getSharedPreferences(XML_Settings, Context.MODE_PRIVATE);
        settings.edit().putInt(key, value).apply();
    }

    /**
     * @param key 键
     * @return 返回int类型的value值
     * @category 从SharedPreference配置文件中获取int类型的值
     */
    public static int getSharedPreInteger(String key, int defaultValue) {
        settings = context.getSharedPreferences(XML_Settings, Context.MODE_PRIVATE);
        try {
            return settings.getInt(key, defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 保存Long类型的数据到SharedPreference配置文件
     *
     * @param key
     * @param value
     */
    public static void saveSharedPreLong(String key, Long value) {
        settings = context.getSharedPreferences(XML_Settings, Context.MODE_PRIVATE);
        settings.edit().putLong(key, value).apply();
    }

    /**
     * @param key 键
     * @return 返回int类型的value值
     * @category 从SharedPreference配置文件中获取int类型的值
     */
    public static long getSharedPreLong(String key, long defaultValue) {
        settings = context.getSharedPreferences(XML_Settings, Context.MODE_PRIVATE);
        try {
            return settings.getLong(key, defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * @category 清空SharedPreference中的所有String类型的数值
     */
    public static void clearSave() {
        settings = context.getSharedPreferences(XML_Settings, 0);
        for (String name : settings.getAll().keySet()) {
            saveSharedPreString(name, "");
        }
    }
}
