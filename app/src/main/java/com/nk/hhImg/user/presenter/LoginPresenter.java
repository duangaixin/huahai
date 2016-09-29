package com.nk.hhImg.user.presenter;

import android.text.TextUtils;

import com.nk.framework.baseUtil.CheckUtils;
import com.nk.framework.baseUtil.HhToast;
import com.nk.hhImg.controller.HhBaseActivity;
import com.nk.hhImg.controller.HhBasePresenter;
import com.nk.hhImg.netmanager.UserIFManager;
import com.nk.hhImg.okhttp.HhOkNetWork;
import com.nk.hhImg.okhttp.callback.ResultCallback;
import com.nk.hhImg.user.activity.LoginActivity;
import com.nk.hhImg.user.bean.User;
import com.nk.hhImg.user.interfa.ILoginSuccess;
import com.nk.hhImg.user.model.ILoginModel;

/**
 * Created by dax on 2016/7/26.
 */
public class LoginPresenter extends HhBasePresenter<LoginActivity> {
    private ILoginModel mLoginModel;

    public LoginPresenter(HhBaseActivity<? extends HhBasePresenter> activity) {
        super(activity);
    }

    public LoginPresenter(HhBaseActivity<? extends HhBasePresenter> activity, ILoginModel loginModel) {
        this(activity);
        this.mLoginModel = loginModel;
    }

    public void login(final ILoginSuccess iLoginSuccess) {
        if (TextUtils.isEmpty(mLoginModel.getLoginPhone())) {
            HhToast.showShort(getActivity(), "手机号码不能为空");
            return;
        }
        if (!CheckUtils.isMobileNO(mLoginModel.getLoginPhone())) {
            HhToast.showShort(getActivity(), "请输入正确的手机号");
            return;
        }
        if (TextUtils.isEmpty(mLoginModel.getLoginPassword())) {
            HhToast.showShort(getActivity(), "请输入密码");
            return;
        }

        HhOkNetWork.getInstance(getActivity()).hhPost(UserIFManager.getLoginRqEntity(mLoginModel.getLoginPhone(), mLoginModel.getLoginPassword()),
                new ResultCallback<User>(this) {
                    @Override
                    public void onResponse(User user) {
                        iLoginSuccess.LoginSucess(user);
                    }
                });
    }
}
