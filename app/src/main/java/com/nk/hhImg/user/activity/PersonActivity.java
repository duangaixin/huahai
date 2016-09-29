package com.nk.hhImg.user.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nk.framework.application.BaseApplication;
import com.nk.framework.baseUtil.ActivityUtil;
import com.nk.framework.baseUtil.HhToast;
import com.nk.framework.baseUtil.IOUtil;
import com.nk.framework.hhDialog.HhDialogManager;
import com.nk.framework.view.AlphaButton;
import com.nk.hhImg.R;
import com.nk.hhImg.controller.HhBaseActivity;
import com.nk.hhImg.home.GlideImageLoader;
import com.nk.hhImg.home.activity.HomeActivity;
import com.nk.hhImg.user.UserApi;
import com.nk.hhImg.user.UserLostException;
import com.nk.hhImg.user.bean.User;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dax on 2016/7/27.
 */
public class PersonActivity extends HhBaseActivity {
    @Bind(R.id.rl_about_us)
    RelativeLayout rlAboutUs;
    @Bind(R.id.btn_quit)
    AlphaButton rlCheckUpdata;
    @Bind(R.id.rl_clear_cache)
    RelativeLayout rlClearCache;
    @Bind(R.id.cache_size)
    TextView cache_size;
    @Bind(R.id.person_code)
    TextView person_code;
    @Bind(R.id.person_name)
    TextView person_name;
    @Bind(R.id.rl_history)
    RelativeLayout rlHistory;

    private File directoryName;
    private LayoutInflater mInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        ButterKnife.bind(this);
        mInflater = LayoutInflater.from(this);
        setTopTitle("个人中心");
        initView();
    }

    private void initView() {
        try {
            User user = UserApi.getUser();
            if (user != null) {
                person_code.setText(user.getUserCode());
                person_name.setText(user.getUserName());
            }
        } catch (UserLostException e) {
            e.printStackTrace();
        }
        directoryName = new File(Environment.getExternalStorageDirectory().getPath(), "HuaHai/Images");
        long dirSize = IOUtil.getFileSize(directoryName);
        String size = IOUtil.formatFileSize(dirSize);
        cache_size.setText(size);
    }

    @OnClick(value = {R.id.rl_history,R.id.rl_about_us,
            R.id.rl_clear_cache, R.id.btn_quit})
    void event(View v) {
        switch (v.getId()) {
            case R.id.rl_history:
                ActivityUtil.moveToActivity(this,HistoryActivity.class);
                break;
            case R.id.rl_about_us:
                ActivityUtil.moveToActivity(this, AboutUsActivity.class);
                break;
            case R.id.rl_clear_cache:
                clearCache();
                break;
            case R.id.btn_quit:
                quitLogin();
                break;

        }
    }

    private void clearCache() {
        HhDialogManager.showAlert(this,  "清除缓存", "您确定要清除本地缓存吗？", "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new GlideImageLoader().clearMemoryCache();
                IOUtil.deleteFile(directoryName);
                cache_size.setText("0KB");
                HhToast.showShort(PersonActivity.this, "缓存清除成功!");
            }
        }, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
    }

    private void quitLogin() {
        HhDialogManager.showAlert(this, "", "您确定要退出吗？", "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UserApi.logout();
                BaseApplication.closeActivity(HomeActivity.class);
                ActivityUtil.moveToActivityAndFinish(PersonActivity.this, LoginActivity.class);
            }
        }, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
