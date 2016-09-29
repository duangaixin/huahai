package com.nk.hhImg.user.util;

import android.content.Context;
import android.text.TextUtils;

import com.nk.framework.baseUtil.SPUtils;

import java.util.Map;

/**
 * Created by dax on 2015/12/15.
 */
public class UserSPUtils {
    private static final String FILE_NAME = "user";
    public static final String SP_KEY_USER = "user";
    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context
     * @param key
     */
    public static void put(Context context, String key, String value) {
        if (!TextUtils.isEmpty(value)) {
            SPUtils.put(context, key, value, FILE_NAME);
        }
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context
     * @param key
     * @return
     */
    public static String get(Context context, String key, String defaultValue) {
        String value = SPUtils.get(context, key, defaultValue, FILE_NAME);
        return value;
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param context
     * @param key
     */
    public static void remove(Context context, String key) {
        SPUtils.remove(context, key, FILE_NAME);
    }

    /**
     * 清除所有数据
     *
     * @param context
     */
    public static void clear(Context context) {
        SPUtils.clear(context, FILE_NAME);
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean contains(Context context, String key) {

        return SPUtils.contains(context, key, FILE_NAME);
    }

    /**
     * 返回所有的键值对
     *
     * @param context
     * @return
     */
    public static Map<String, ?> getAll(Context context) {

        return SPUtils.getAll(context, FILE_NAME);
    }


}
