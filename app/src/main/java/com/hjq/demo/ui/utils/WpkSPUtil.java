package com.hjq.demo.ui.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
public class WpkSPUtil {
    private static SharedPreferences sp;
    public static final String WPK_PRODUCT_LABEL = "wpk_product_label";//商品大标签
    public static final String WPK_PRODUCT_SECONDARY_LABEL = "wpk_product_secondary_label";//商品次标签
    public static final String WPK_PRODUCT_INFO = "wpk_product_info";//商品info

    public static final String WPK_HOME_UI_ROW = "wpk_home_ui_row";//每行显示几张图片
    public static final String WPK_PRICE_LIST_IMAGE = "wpk_price_list_image";//价标图片信息
    public static final String WPK_HOME_UI_COLUMNS = "wpk_home_ui_columns";//每列显示几张图片
    public static final String WPK_HOME_EDIT_STATUS = "wpk_home_edit_status";//是否可以编辑

    private WpkSPUtil() {
    }

    /**
     * 初始化SharedPreferencesUtil,只需要初始化一次，建议在Application中初始化
     *
     * @param context 上下文对象
     * @param name    SharedPreferences Name
     */
    public static void getInstance(Context context, String name) {
        //        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE)
        if (sp == null) {
            sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        }
    }

    /**
     * 保存数据到SharedPreferences
     *
     * @param key   键
     * @param value 需要保存的数据
     * @return 保存结果
     */
    public static boolean put(String key, Object value) {
        if (sp == null) {
            return false;
        }
        boolean result;
        SharedPreferences.Editor editor = sp.edit();
        if (value == null) {
            return false;
        }
        String type = value.getClass().getSimpleName();
        try {
            switch (type) {
                case "Boolean":
                    editor.putBoolean(key, (Boolean) value);
                    break;
                case "Long":
                    editor.putLong(key, (Long) value);
                    break;
                case "Float":
                    editor.putFloat(key, (Float) value);
                    break;
                case "String":
                    editor.putString(key, (String) value);
                    break;
                case "Integer":
                    editor.putInt(key, (Integer) value);
                    break;
                default:
                    String json = JSON.toJSONString(value);
                    editor.putString(key, json);
                    break;
            }
            result = true;
        } catch (Exception e) {
            result = false;
            //empty code
        }
        editor.apply();
        return result;
    }

    /**
     * 保存数据到SharedPreferences
     *
     * @param key   键
     * @param value 需要保存的数据
     * @return 保存结果
     */
    public static boolean putCommit(String key, Object value) {
        if (sp == null) {
            return false;
        }
        SharedPreferences.Editor editor = sp.edit();
        if (value == null) {
            return false;
        }
        String type = value.getClass().getSimpleName();
        try {
            switch (type) {
                case "Boolean":
                    editor.putBoolean(key, (Boolean) value);
                    break;
                case "Long":
                    editor.putLong(key, (Long) value);
                    break;
                case "Float":
                    editor.putFloat(key, (Float) value);
                    break;
                case "String":
                    editor.putString(key, (String) value);
                    break;
                case "Integer":
                    editor.putInt(key, (Integer) value);
                    break;
                default:
                    String json = JSON.toJSONString(value);
                    editor.putString(key, json);
                    break;
            }

        } catch (Exception e) {
            return false;
        }

        return editor.commit();
    }

    public static int getInt(String key, int defaultValue) {
        if (sp == null) {
            return defaultValue;
        }
        return sp.getInt(key, defaultValue);
    }

    public static String getString(String key, String defaultValue) {
        if (sp == null) {
            return defaultValue;
        }
        return sp.getString(key, defaultValue);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        if (sp == null) {
            return defaultValue;
        }
        return sp.getBoolean(key, defaultValue);
    }

    public static long getLong(String key, long defaultValue) {
        if (sp == null) {
            return defaultValue;
        }
        return sp.getLong(key, defaultValue);
    }

    public static float getFloat(String key, float defaultValue) {
        if (sp == null) {
            return defaultValue;
        }
        return sp.getFloat(key, defaultValue);
    }

    /**
     * 用于保存集合
     *
     * @param key  key
     * @param list 集合数据
     */
    public static <T> void putListData(String key, List<T> list) {
        if (sp == null) {
            return;
        }
        SharedPreferences.Editor editor = sp.edit();
        JSONArray array = new JSONArray();
        array.addAll(list);
        editor.putString(key, array.toString());
        editor.apply();
    }

    /**
     * 获取保存的List
     *
     * @param key key
     * @return 对应的Lis集合
     */
    public static <T> List<T> getListData(String key, Class<T> cls) {
        List<T> list = new ArrayList<>();
        if (sp == null) {
            return list;
        }
        String json = sp.getString(key, "");
        if (!json.equals("") && json.length() > 0) {
            JSONArray array = JSON.parseArray(json);
            list.addAll(array.toJavaList(cls));
        }
        return list;
    }

    /**
     * 用于保存map
     *
     * @param key key
     * @param map map数据
     * @return 保存结果
     */
    @SuppressWarnings("UnusedReturnValue")
    public static <K, V> boolean putHashMapData(String key, Map<K, V> map) {
        if (sp == null) {
            return false;
        }
        boolean result;
        SharedPreferences.Editor editor = sp.edit();
        try {
            String json = JSON.toJSONString(map);
            editor.putString(key, json);
            result = true;
        } catch (Exception e) {
            result = false;
        }
        editor.apply();
        return result;
    }

    /**
     * 用于获取保存的map
     *
     * @param key key
     * @return HashMap
     */
    @SuppressWarnings({"java:S3740", "java:S1319", "rawtypes"})
    public static HashMap getHashMapData(String key) {
        if (sp == null) {
            return null;
        }
        String json = sp.getString(key, "");
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        return JSON.parseObject(json, HashMap.class);
    }

    //删除某个key的值
    public static void remove(String key) {
        if (sp == null) {
            return;
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key).apply();
    }
}
