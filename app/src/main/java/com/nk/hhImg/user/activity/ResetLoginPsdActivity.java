package com.nk.hhImg.user.activity;


import android.os.Bundle;
import android.widget.EditText;

import com.nk.framework.application.BaseApplication;
import com.nk.framework.baseUtil.ActivityUtil;
import com.nk.framework.baseUtil.HhToast;
import com.nk.framework.baseUtil.SPUtils;
import com.nk.framework.view.AlphaButton2;
import com.nk.hhImg.R;
import com.nk.hhImg.controller.HhBaseActivity;
import com.nk.hhImg.user.interfa.IResetSuccess;
import com.nk.hhImg.user.model.IResetModel;
import com.nk.hhImg.user.presenter.ResetPresenter;
import com.nk.hhImg.user.util.MaxLengthWatcher;
import com.nk.hhImg.user.util.SearchWather;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dax on 2016/7/26.
 */
public class ResetLoginPsdActivity extends HhBaseActivity<ResetPresenter> implements IResetModel {
    @Bind(R.id.et_pwd_reset)
    EditText pwd_reset;
    @Bind(R.id.et_pwd_reset_confirm)
    EditText pwd_reset_confirm;
    @Bind(R.id.btn_reset_confirm)
    AlphaButton2 btn_confirm;

    String phoneNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        ButterKnife.bind(this);
        SPUtils.clear(this,"sp_sms_reset");
        mBasePresenter = new ResetPresenter(this, this);
        phoneNum = getIntent().getExtras().getString("phoneNum");
        initView();
    }

    private void initView(){
        pwd_reset.addTextChangedListener(new SearchWather(pwd_reset));
        pwd_reset.addTextChangedListener(new MaxLengthWatcher(this, 20, pwd_reset));
        pwd_reset_confirm.addTextChangedListener(new SearchWather(pwd_reset_confirm));
    }

    @Override
    public String getResetPassword() {
        return pwd_reset.getText().toString();
    }

    @Override
    public String getResetConfirmPassword() {
        return pwd_reset_confirm.getText().toString();
    }

    @OnClick(R.id.btn_reset_confirm)
    void resetPwd() {
        mBasePresenter.commit(phoneNum, new IResetSuccess() {
            @Override
            public void resetSuccess() {
                HhToast.showShort(ResetLoginPsdActivity.this, "密码重置成功，请重新登录");
                BaseApplication.closeActivity(LoginActivity.class);
                ActivityUtil.moveToActivityAndFinish(ResetLoginPsdActivity.this, LoginActivity.class);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}

