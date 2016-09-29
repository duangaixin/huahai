package com.nk.framework.baseUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by dgx on 2016/7/26.
 */
public class ActivityUtil {
    public static void moveToActivity(Activity activity, Class targetActivityClass) {
    moveToActivity(activity, targetActivityClass, null);
}

        public static void moveToActivityAndFinish(Activity activity, Class targetActivityClass) {
            moveToActivity(activity, targetActivityClass, null);
            activity.finish();
        }

        public static void moveToActivityAndFinish(Activity activity, Class targetActivityClass, Bundle bundle) {
            moveToActivity(activity, targetActivityClass, bundle);
            activity.finish();
        }

        public static void moveToActivity(Activity activity, Class targetActivityClass, Bundle bundle) {
            if (activity == null || activity.isFinishing()) {
                return;
            }

            Intent intent = new Intent(activity, targetActivityClass);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            activity.startActivity(intent);
         /*   int version = Integer.valueOf(android.os.Build.VERSION.SDK);
            if(version  >= 5) {
                activity.overridePendingTransition(0,0);
            }*/
        }

        public static void moveToActivityForResult(Activity activity, Class targetActivityClass, int resultCode) {
            moveToActivityForResult(activity, targetActivityClass, resultCode, null);
        }

        public static void moveToActivityForResult(Activity activity, Class targetActivityClass, int requestCode, Bundle bundle) {
            if (activity == null || activity.isFinishing()) {
                return;
            }

            Intent intent = new Intent(activity, targetActivityClass);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            activity.startActivityForResult(intent, requestCode);
      /*      int version = Integer.valueOf(android.os.Build.VERSION.SDK);
            if(version  >= 5) {
                activity.overridePendingTransition(0, 0);
            }*/
        }
}

