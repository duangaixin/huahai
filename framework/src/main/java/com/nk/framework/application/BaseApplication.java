package com.nk.framework.application;

import android.app.Application;

import com.nk.framework.baseUtil.L;
import com.nk.framework.controller.BaseActivity;

import org.xutils.x;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by dgx on 2016/7/26.
 */
public class BaseApplication extends Application {
    private static BaseApplication mApplication;
    private static List<BaseActivity> activitylist = new LinkedList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;

        x.Ext.init(this);
        L.isDebug=true;
        x.Ext.setDebug(true);
    }


    public static BaseApplication getInstance() {
        return mApplication;
    }

    public static List<BaseActivity> getActivityList() {
        return activitylist;
    }


    public static void clearAllActivity() {
        for (BaseActivity activity : activitylist) {
            if (!activity.isFinishing())
                activity.finishNoAnimation();

        }

        activitylist.clear();
    }

    public static <T> void closeActivity(Class<T> clazz) {
        List<BaseActivity> activities = new ArrayList<BaseActivity>();
        for (int i = 0; i < activitylist.size(); i++) {
            if (activitylist.get(i).getClass() == clazz) {
                activities.add(activitylist.get(i));
            }

        }
        if (!activities.isEmpty()) {
            for (BaseActivity activity : activities) {
                activity.finishNoAnimation();

            }
            activitylist.remove(activities);
        }

    }


    public static void exit() {
        clearAllActivity();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}

