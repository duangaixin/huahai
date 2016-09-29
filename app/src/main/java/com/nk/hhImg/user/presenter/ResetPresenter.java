package com.nk.hhImg.user.presenter;

import android.text.TextUtils;

import com.nk.framework.baseUtil.HhToast;
import com.nk.framework.baseUtil.StringUtil;
import com.nk.hhImg.controller.HhBaseActivity;
import com.nk.hhImg.controller.HhBasePresenter;
import com.nk.hhImg.netmanager.UserIFManager;
import com.nk.hhImg.okhttp.HhOkNetWork;
import com.nk.hhImg.okhttp.callback.ResultCallback;
import com.nk.hhImg.user.activity.ResetLoginPsdActivity;
import com.nk.hhImg.user.interfa.IResetSuccess;
import com.nk.hhImg.user.model.IResetModel;

/**
 * Created by dax on 2016/7/26.
 */
public class ResetPresenter extends HhBasePresenter<ResetLoginPsdActivity> {
    private IResetModel mIResetModel;

    public ResetPresenter(HhBaseActivity<? extends HhBasePresenter> activity) {
        super(activity);
    }

    public ResetPresenter(HhBaseActivity<? extends HhBasePresenter> activity, IResetModel iResetModel) {
        this(activity);
        this.mIResetModel = iResetModel;
    }

    public void commit(String phone, final IResetSuccess iResetSucess) {
        if (TextUtils.isEmpty(mIResetModel.getResetPassword())) {
            HhToast.showShort(getActivity(), "6-20位数字字母组合密码");
            return;
        }
        if (!StringUtil.is620Num(mIResetModel.getResetPassword())) {
            HhToast.showShort(getActivity(), "请输入6-20位数字字母组合密码");
            return;
        }
        if (TextUtils.isEmpty(mIResetModel.getResetConfirmPassword())) {
            HhToast.showShort(getActivity(), "6-20位数字字母组合密码");
            return;
        }
        if (!StringUtil.is620Num(mIResetModel.getResetConfirmPassword())) {
            HhToast.showShort(getActivity(), "请输入6-20位数字字母组合密码");
            return;
        }
        if (!mIResetModel.getResetPassword().equals(mIResetModel.getResetConfirmPassword())) {
            HhToast.showShort(getActivity(), "请输入确认密码");
            return;
        }

        HhOkNetWork.getInstance(getActivity()).hhPost(UserIFManager.getFindPwdRqEntity(phone, mIResetModel.getResetPassword()),
                new ResultCallback<String>() {
                    @Override
                    public void onResponse(String response) {
                        iResetSucess.resetSuccess();
                    }
                });
    }
}
