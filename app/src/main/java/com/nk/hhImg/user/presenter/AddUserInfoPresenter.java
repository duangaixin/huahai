package com.nk.hhImg.user.presenter;

import android.text.TextUtils;

import com.nk.framework.baseUtil.HhToast;
import com.nk.framework.baseUtil.StringUtil;
import com.nk.hhImg.controller.HhBaseActivity;
import com.nk.hhImg.controller.HhBasePresenter;
import com.nk.hhImg.netmanager.UserIFManager;
import com.nk.hhImg.okhttp.HhOkNetWork;
import com.nk.hhImg.okhttp.callback.ResultCallback;
import com.nk.hhImg.user.activity.AddUserInfoActivity;
import com.nk.hhImg.user.interfa.IRegistSucess;
import com.nk.hhImg.user.model.IAddUserInfoModel;

/**
 * Created by dax on 2016/8/17.
 */
public class AddUserInfoPresenter extends HhBasePresenter<AddUserInfoActivity> {
    private IAddUserInfoModel mIAddUserInfoModel;

    public AddUserInfoPresenter(HhBaseActivity<? extends HhBasePresenter> activity) {
        super(activity);
    }

    public AddUserInfoPresenter(HhBaseActivity<? extends HhBasePresenter> activity, IAddUserInfoModel iAddUserInfoModel) {
        super(activity);
        this.mIAddUserInfoModel = iAddUserInfoModel;
    }

    public void regist(String phoneNum, final IRegistSucess iRegistSucess) {
        if (TextUtils.isEmpty(mIAddUserInfoModel.getUserRealName())) {
            HhToast.showShort(getActivity(), "请输入员工姓名");
            return;
        }
        if (TextUtils.isEmpty(mIAddUserInfoModel.getUserNum())) {
            HhToast.showShort(getActivity(), "请输入员工号");
            return;
        }
        if (TextUtils.isEmpty(mIAddUserInfoModel.getRegistPassword())) {
            HhToast.showShort(getActivity(), "6-20位数字字母组合密码");
            return;
        }
        if (!StringUtil.is620Num(mIAddUserInfoModel.getRegistPassword())) {
            HhToast.showShort(getActivity(), "请输入6-20位数字字母组合密码");
            return;
        }
        if (TextUtils.isEmpty(mIAddUserInfoModel.getRegistConfirmPassword())) {
            HhToast.showShort(getActivity(), "6-20位数字字母组合密码");
            return;
        }
        if (!StringUtil.is620Num(mIAddUserInfoModel.getRegistConfirmPassword())) {
            HhToast.showShort(getActivity(), "请输入6-20位数字字母组合密码");
            return;
        }
        if (!mIAddUserInfoModel.getRegistConfirmPassword().equals(mIAddUserInfoModel.getRegistPassword())) {
            HhToast.showShort(getActivity(), "两次输入密码不一致");
            return;
        }

        HhOkNetWork.getInstance(getActivity()).hhPost(UserIFManager.getRegistRqEntity
                        (phoneNum,
                                mIAddUserInfoModel.getUserRealName(),
                                mIAddUserInfoModel.getUserNum(),
                                mIAddUserInfoModel.getRegistPassword()),
                new ResultCallback<String>() {
                    @Override
                    public void onResponse(String response) {
                        iRegistSucess.registSucess();
                    }

                });
    }
}
