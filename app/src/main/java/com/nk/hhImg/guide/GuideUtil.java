package com.nk.hhImg.guide;

import android.content.Context;
import android.content.SharedPreferences;

import com.nk.framework.baseUtil.AppUtils;

/**
 * Created by dgx on 2016/9/7.
 */

public class GuideUtil {

    public static boolean isShouldOpenNaviForCurVersionCode(Context context) {
        int curVersion = AppUtils.getVersionCode(context);
        SharedPreferences sp = context.getSharedPreferences("local_version", Context.MODE_PRIVATE);

        int localVersion = sp.getInt("local_version", -1);
        if (localVersion == -1) {
            return true;
        } else if (localVersion == curVersion) {
            return false;
        } else {
            return true;
        }
    }

    public static void setShouldOpenNaviFalse(Context context) {
        int curVersion = AppUtils.getVersionCode(context);
        SharedPreferences sp = context.getSharedPreferences("local_version", Context.MODE_PRIVATE);
        sp.edit().putInt("local_version", curVersion).commit();
    }
}
