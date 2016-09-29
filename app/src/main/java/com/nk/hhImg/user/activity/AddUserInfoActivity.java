package com.nk.hhImg.user.activity;

import android.os.Bundle;
import android.widget.EditText;

import com.nk.framework.application.BaseApplication;
import com.nk.framework.baseUtil.ActivityUtil;
import com.nk.framework.baseUtil.SPUtils;
import com.nk.framework.view.AlphaButton2;
import com.nk.hhImg.R;
import com.nk.hhImg.controller.HhBaseActivity;
import com.nk.hhImg.user.interfa.IRegistSucess;
import com.nk.hhImg.user.model.IAddUserInfoModel;
import com.nk.hhImg.user.presenter.AddUserInfoPresenter;
import com.nk.hhImg.user.util.MaxLengthWatcher;
import com.nk.hhImg.user.util.SearchWather;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dax on 2016/8/16.
 */
public class AddUserInfoActivity extends HhBaseActivity<AddUserInfoPresenter> implements IAddUserInfoModel {
    @Bind(R.id.et_userRealName)
    EditText userRealName;
    @Bind(R.id.et_userNum)
    EditText userNum;
    @Bind(R.id.et_userpwd)
    EditText userPwd;
    @Bind(R.id.et_userpwd_confirm)
    EditText userPwd_confirm;
    @Bind(R.id.btn_regist)
    AlphaButton2 btn_regist;

    String phoneNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adduserinfo);
        ButterKnife.bind(this);
        SPUtils.clear(this,"sp_sms_regist");
        mBasePresenter = new AddUserInfoPresenter(this, this);
        phoneNum = getIntent().getExtras().getString("phoneNum");
         initView();

    }

     private void initView(){
         userPwd.addTextChangedListener(new SearchWather(userPwd));
         userPwd.addTextChangedListener(new MaxLengthWatcher(this, 20, userPwd));
         userPwd_confirm.addTextChangedListener(new SearchWather(userPwd_confirm));
     }

    @OnClick(R.id.btn_regist)
    void regist() {
        mBasePresenter.regist(phoneNum, new IRegistSucess() {
            @Override
            public void registSucess() {
                BaseApplication.closeActivity(LoginActivity.class);
                ActivityUtil.moveToActivityAndFinish(AddUserInfoActivity.this, LoginActivity.class);
            }
        });
    }

    @Override
    public String getUserRealName() {
        return userRealName.getText().toString();
    }

    @Override
    public String getUserNum() {
        return userNum.getText().toString();
    }

    @Override
    public String getRegistPassword() {
        return userPwd.getText().toString();
    }

    @Override
    public String getRegistConfirmPassword() {
        return userPwd_confirm.getText().toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }


}
