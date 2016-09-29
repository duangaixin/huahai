package com.nk.hhImg.guide.activity;

import android.os.Bundle;
import android.os.Handler;

import com.nk.framework.baseUtil.ActivityUtil;
import com.nk.framework.baseUtil.KeyValueSPUtils;
import com.nk.framework.controller.BaseActivity;
import com.nk.hhImg.R;
import com.nk.hhImg.guide.GuideUtil;
import com.nk.hhImg.home.activity.HomeActivity;
import com.nk.hhImg.user.UserApi;
import com.nk.hhImg.user.activity.LoginActivity;

/**
 * Created by dax on 2016/7/26.
 */
public class SplashActivity extends BaseActivity {
    private boolean isChecked;
    private boolean quit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        isChecked = KeyValueSPUtils.getBoolean(SplashActivity.this, UserApi.ISCHECKED);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!quit) {
                    toNaviActivity();
                }
            }
        }, 1000);
    }

    void toNaviActivity() {
        if (GuideUtil.isShouldOpenNaviForCurVersionCode(SplashActivity.this)) {
            ActivityUtil.moveToActivityAndFinish(this, NavigationPicActivity.class);
        } else {
            if (isChecked && UserApi.isLogin()) {
                ActivityUtil.moveToActivityAndFinish(this, HomeActivity.class);
            } else {
                ActivityUtil.moveToActivityAndFinish(this, LoginActivity.class);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        quit = true;
    }
}
