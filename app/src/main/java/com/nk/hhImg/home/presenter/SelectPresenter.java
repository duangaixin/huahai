package com.nk.hhImg.home.presenter;

import com.nk.hhImg.controller.HhBaseActivity;
import com.nk.hhImg.controller.HhBasePresenter;
import com.nk.hhImg.home.activity.SelectPicActivity;
import com.nk.hhImg.home.bean.SelectInfoResult;
import com.nk.hhImg.home.interfa.IGetSelectInfoResult;
import com.nk.hhImg.netmanager.UploadIFManager;
import com.nk.hhImg.okhttp.HhOkNetWork;
import com.nk.hhImg.okhttp.callback.ResultCallback;

/**
 * Created by win on 2016/8/18.
 */
public class SelectPresenter extends HhBasePresenter<SelectPicActivity> {
    private IGetSelectInfoResult iGetSelectInfoResult;

    public SelectPresenter(HhBaseActivity<? extends HhBasePresenter> activity) {
        super(activity);
    }

    public SelectPresenter(HhBaseActivity<? extends HhBasePresenter> activity, IGetSelectInfoResult iGetSelectInfoResult) {
        super(activity);
        this.iGetSelectInfoResult = iGetSelectInfoResult;
    }

    public void getSelectPageInfo(String phoneNum, String scanUrl, final IGetSelectInfoResult iGetSelectInfoResult) {
        HhOkNetWork.getInstance(getActivity()).hhPost(UploadIFManager.getSelectPageInfoRqEntity(phoneNum, scanUrl), new ResultCallback<SelectInfoResult>() {
            @Override
            public void onResponse(SelectInfoResult response) {
                if (response == null) {
                    return;
                }
                iGetSelectInfoResult.getSelectInfoSuccess(response);
            }
        });
    }
}
