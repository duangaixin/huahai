package com.nk.framework.controller;


import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.nk.framework.application.BaseApplication;

/**
 * Created by dax on 2016/7/26.
 */
public class  BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseApplication.getActivityList().add(this);
         setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseApplication.getActivityList().remove(this);

    }

    public void finishNoAnimation() {
        super.finish();

        overridePendingTransition(0, 0);
    }

}
