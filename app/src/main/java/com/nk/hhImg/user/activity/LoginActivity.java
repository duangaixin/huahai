package com.nk.hhImg.user.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.lzy.imagepicker.view.SuperCheckBox;
import com.nk.framework.baseUtil.ActivityUtil;
import com.nk.framework.baseUtil.KeyValueSPUtils;
import com.nk.framework.view.AlphaButton2;
import com.nk.framework.view.ClearEditText;
import com.nk.hhImg.R;
import com.nk.hhImg.controller.HhBaseActivity;
import com.nk.hhImg.home.activity.HomeActivity;
import com.nk.hhImg.user.UserApi;
import com.nk.hhImg.user.bean.User;
import com.nk.hhImg.user.interfa.ILoginSuccess;
import com.nk.hhImg.user.model.ILoginModel;
import com.nk.hhImg.user.presenter.LoginPresenter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dax on 2016/7/26.
 */
public class LoginActivity extends HhBaseActivity<LoginPresenter> implements ILoginModel {
    @Bind(R.id.et_login_username)
    ClearEditText et_login_username;
    @Bind(R.id.et_login_userpwd)
    ClearEditText et_login_userpwd;
    @Bind(R.id.txt_resetpwd)
    TextView txt_resetPwd;
    @Bind(R.id.btn_login)
    AlphaButton2 btn_login;
    @Bind(R.id.btn_login_regist)
    AlphaButton2 btn_regist;
    @Bind(R.id.home_ck)
    SuperCheckBox home_ck;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mBasePresenter = new LoginPresenter(this, this);
        hideBackView(true);
    }

    @OnClick(R.id.btn_login)
    void toLogin() {
        mBasePresenter.login(new ILoginSuccess() {
            @Override
            public void LoginSucess(User user) {
                UserApi.setUser(user);
                if (home_ck.isChecked()) {
                    KeyValueSPUtils.putBoolean(LoginActivity.this, UserApi.ISCHECKED, true);
                } else {
                    KeyValueSPUtils.putBoolean(LoginActivity.this, UserApi.ISCHECKED, false);
                }
                ActivityUtil.moveToActivityAndFinish(LoginActivity.this, HomeActivity.class);
            }
        });

    }

    @OnClick(R.id.btn_login_regist)
    public void toRegist() {
        ActivityUtil.moveToActivity(this, RegistSmsActivity.class);
    }


    @OnClick(R.id.txt_resetpwd)
    public void resetPassword() {
        ActivityUtil.moveToActivity(this, ResetPwdSmsActivity.class);
    }

    @Override
    public String getLoginPhone() {
        return et_login_username.getText().toString();
    }

    @Override
    public String getLoginPassword() {
        return et_login_userpwd.getText().toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
