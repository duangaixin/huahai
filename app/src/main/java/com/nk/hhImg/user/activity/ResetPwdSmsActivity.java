package com.nk.hhImg.user.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.nk.framework.baseUtil.ActivityUtil;
import com.nk.framework.baseUtil.HhToast;
import com.nk.framework.baseUtil.SPUtils;
import com.nk.framework.view.AlphaButton2;
import com.nk.hhImg.R;
import com.nk.hhImg.controller.HhBaseActivity;
import com.nk.hhImg.user.TimeCount;
import com.nk.hhImg.user.interfa.IValidateCodeForRegistResult;
import com.nk.hhImg.user.interfa.IValidateSuccessForFindResult;
import com.nk.hhImg.user.model.IValidateModel;
import com.nk.hhImg.user.presenter.ValidateCodeForFindPresenter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dax on 2016/9/4.
 */
public class ResetPwdSmsActivity extends HhBaseActivity<ValidateCodeForFindPresenter> implements IValidateModel {
    @Bind(R.id.et_regist_phoneNum)
    EditText et_phoneNum;
    @Bind(R.id.txt_regist_getSms)
    TextView txt_getSms;
    @Bind(R.id.et_regist_sms)
    EditText et_regist_sms;
    @Bind(R.id.btn_next)
    AlphaButton2 btn_next;

    String phoneNum;
    private TimeCount mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        ButterKnife.bind(this);
        mBasePresenter = new ValidateCodeForFindPresenter(this, this);
        setTopTitle("找回密码");
    }

    @OnClick(R.id.txt_regist_getSms)
    void getVerifyCode() {
        mBasePresenter.getValidateCode(new IValidateCodeForRegistResult() {
            @Override
            public void loginVaCodeResultSucess() {
                HhToast.showShort(ResetPwdSmsActivity.this, "验证码发送成功");
                if (null == mTimer) {
                    mTimer = new TimeCount(60000, 1000, txt_getSms, ResetPwdSmsActivity.this);
                }
                mTimer.start();
            }
        });
    }

    @OnClick(R.id.btn_next)
    void goToResetActivity() {
        mBasePresenter.goToResetPwdActivity(new IValidateSuccessForFindResult() {

            @Override
            public void validateSuccess() {
                goToResetPwdActivity();
            }
        });
    }

    public void goToResetPwdActivity() {

        Bundle bundle = new Bundle();
        bundle.putString("phoneNum", phoneNum);
        ActivityUtil.moveToActivityAndFinish(ResetPwdSmsActivity.this, ResetLoginPsdActivity.class, bundle);
    }

    @Override
    public String getRegistPhone() {
        phoneNum = et_phoneNum.getText().toString();
        return phoneNum;
    }

    @Override
    public String getRegistValidateCode() {
        return et_regist_sms.getText().toString();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String pauseTime = SPUtils.get(ResetPwdSmsActivity.this, "pauseTime", null, "sp_sms_reset");
        String phoneNum = SPUtils.get(ResetPwdSmsActivity.this, "ResetPwdPhoneNum", null, "sp_sms_reset");
        if (!TextUtils.isEmpty(pauseTime) && !TextUtils.isEmpty(phoneNum)) {
            et_phoneNum.setText(phoneNum);
            long pauseTimeL = Long.parseLong(pauseTime);
            mTimer = new TimeCount(pauseTimeL, 1000, txt_getSms, ResetPwdSmsActivity.this);
            mTimer.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mTimer != null && mTimer.isRunning()) {
            String nowTime = mTimer.getNowTime();
            String phone = et_phoneNum.getText().toString();
            SPUtils.put(this, "ResetPwdPhoneNum", phone, "sp_sms_reset");
            SPUtils.put(this, "pauseTime", nowTime, "sp_sms_reset");
            mTimer.cancel();
            mTimer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
