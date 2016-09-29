package com.nk.hhImg.user.presenter;

import android.text.TextUtils;

import com.nk.framework.baseUtil.CheckUtils;
import com.nk.framework.baseUtil.HhToast;
import com.nk.hhImg.controller.HhBaseActivity;
import com.nk.hhImg.controller.HhBasePresenter;
import com.nk.hhImg.netmanager.UserIFManager;
import com.nk.hhImg.okhttp.HhOkNetWork;
import com.nk.hhImg.okhttp.callback.ResultCallback;
import com.nk.hhImg.user.activity.RegistSmsActivity;
import com.nk.hhImg.user.interfa.IValidateCodeForRegistResult;
import com.nk.hhImg.user.interfa.IValidateSuccessForRegistResult;
import com.nk.hhImg.user.model.IValidateModel;

/**
 * Created by dax on 2016/8/17.
 */
public class ValidateCodeForRegistPresenter extends HhBasePresenter<RegistSmsActivity> {
    private IValidateModel mIValidateModel;

    public ValidateCodeForRegistPresenter(HhBaseActivity<? extends HhBasePresenter> activity) {
        super(activity);
    }

    public ValidateCodeForRegistPresenter(HhBaseActivity<? extends HhBasePresenter> activity, IValidateModel iValidateModel) {
        this(activity);
        this.mIValidateModel = iValidateModel;
    }

    /**
     * 获取注册验证码
     */
    public void getValidateCode(final IValidateCodeForRegistResult iValidateCodeResult) {
        if (TextUtils.isEmpty(mIValidateModel.getRegistPhone())) {
            HhToast.showShort(getActivity(), "请输入手机号码");
            return;
        }
        if (!CheckUtils.isMobileNO(mIValidateModel.getRegistPhone())) {
            HhToast.showShort(getActivity(), "请输入正确的手机号");
            return;
        }

        HhOkNetWork.getInstance(getActivity()).hhPost(UserIFManager.getVaCodeRegistEntity(mIValidateModel.getRegistPhone()), new ResultCallback<String>(this) {
            @Override
            public void onResponse(String response) {
                iValidateCodeResult.loginVaCodeResultSucess();
            }
        });
    }

    public void goToAddUserInfoActivity(final IValidateSuccessForRegistResult validateSuccessResult) {

        if (TextUtils.isEmpty(mIValidateModel.getRegistPhone())) {
            HhToast.showShort(getActivity(), "请输入手机号码");
            return;
        }
        if (!CheckUtils.isMobileNO(mIValidateModel.getRegistPhone())) {
            HhToast.showShort(getActivity(), "请输入正确的手机号");
            return;
        }
        if (TextUtils.isEmpty(mIValidateModel.getRegistValidateCode())) {
            HhToast.showShort(getActivity(), "请输入验证码");
            return;
        }

        HhOkNetWork.getInstance(getActivity()).hhPost(UserIFManager.getValiteCodeRqEntity(mIValidateModel.getRegistPhone(), mIValidateModel.getRegistValidateCode()),
                new ResultCallback<String>() {
                    @Override
                    public void onResponse(String response) {
                        validateSuccessResult.validateSuccess();
                    }
                });
    }

}
