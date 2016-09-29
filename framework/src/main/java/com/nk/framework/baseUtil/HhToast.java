package com.nk.framework.baseUtil;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by dax on 2016/7/11.
 */
public class HhToast {
    private HhToast() {

        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static boolean isShow = true;

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, CharSequence message) {
        if (isShow)
            setToast(Toast.makeText(context, message, Toast.LENGTH_SHORT)).show();
    }

    private static Toast setToast(Toast toast) {
        toast.setGravity(Gravity.CENTER, 0, 0);// 设置居中
        toast.show();
        return toast;
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, int message) {
        if (isShow)
            setToast(Toast.makeText(context, message, Toast.LENGTH_SHORT)).show();
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, CharSequence message) {
        if (isShow)
            setToast(Toast.makeText(context, message, Toast.LENGTH_LONG)).show();
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, int message) {
        if (isShow)
            setToast(Toast.makeText(context, message, Toast.LENGTH_LONG)).show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, CharSequence message, int duration) {
        if (isShow)
            setToast(Toast.makeText(context, message, duration)).show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, int message, int duration) {
        if (isShow)
            setToast(Toast.makeText(context, message, duration)).show();
    }
}
