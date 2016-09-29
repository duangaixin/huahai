package com.nk.hhImg.home.presenter;

import android.content.DialogInterface;
import android.util.Log;
import android.util.Pair;

import com.lzy.imagepicker.bean.ImageItem;
import com.nk.framework.baseUtil.HhToast;
import com.nk.framework.baseUtil.L;
import com.nk.framework.baseUtil.NetUtils;
import com.nk.framework.hhDialog.HhDialogManager;
import com.nk.hhImg.controller.HhBaseActivity;
import com.nk.hhImg.controller.HhBasePresenter;
import com.nk.hhImg.home.activity.UploadPicActivity;
import com.nk.hhImg.home.bean.SelectInfoResult;
import com.nk.hhImg.home.interfa.IUploadCallBack;
import com.nk.hhImg.home.interfa.IUploadPicSuccess;
import com.nk.hhImg.okhttp.HhOkNetWork;
import com.nk.hhImg.okhttp.UploadRequestEntity;
import com.nk.hhImg.user.UserApi;
import com.nk.hhImg.user.UserLostException;
import com.nk.hhImg.user.bean.User;

import org.xutils.common.Callback;

import java.io.File;
import java.util.HashMap;

/**
 * Created by dax on 2016/8/11.
 */
public class UploadPresenter extends HhBasePresenter<UploadPicActivity> {
    private IUploadPicSuccess iUploadPicSuccess;
    private String userCode;
    private String userName;

    public UploadPresenter(HhBaseActivity<? extends HhBasePresenter> activity) {
        super(activity);
    }

    public UploadPresenter(HhBaseActivity<? extends HhBasePresenter> activity, IUploadPicSuccess iUploadPicSuccess) {
        super(activity);
        this.iUploadPicSuccess = iUploadPicSuccess;
    }

    public void uploadPicPrepapre(final ImageItem item, final SelectInfoResult selectInfoResult, final IUploadCallBack callBack) {
        if (!NetUtils.isConnected()) {
            UploadPicActivity activity = (UploadPicActivity) getActivity();
            if (activity != null && activity.mHandler != null) {
                activity.mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        HhToast.showShort(getActivity(), "网络未连接");
                        callBack.onUploadStart();
                        callBack.onUploadError();
                        callBack.onUploadFinish();
                    }
                });
            }
            return;

        } else if (NetUtils.isWifi(getActivity())) {
            upload(item, selectInfoResult, callBack);
        } else if (NetUtils.isConnected() && !NetUtils.isWifi(getActivity())) {
            HhDialogManager.showAlert(getActivity(), "温馨提示", "您未处于wifi网络下,需要使用移动流量进行上传么？",
                    "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            upload(item, selectInfoResult, callBack);
                        }
                    }, "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
        }
    }

    private void upload(final ImageItem item, SelectInfoResult selectInfoResult, final IUploadCallBack callBack) {
        Pair<String, File>[] mfiles = new Pair[]{new Pair("myfile", new File(item.compressPath))};

       try {
            User user = UserApi.getUser();
            if (user != null) {
                userCode = user.getUserCode();
                userName = user.getUserName();
            }
        } catch (UserLostException e) {
            e.printStackTrace();
        }
        HashMap<String, String> uploadParams = new HashMap<>();
        uploadParams.put("key", selectInfoResult.getKey());
        uploadParams.put("typeCode", item.code);
       uploadParams.put("user_code", userCode);
        uploadParams.put("user_name",userName);

        UploadRequestEntity requestEntity = new UploadRequestEntity();
        requestEntity.url = selectInfoResult.getUrl();
        L.e(requestEntity.url);
        requestEntity.files = mfiles;
        requestEntity.uploadParams = uploadParams;

        HhOkNetWork.getInstance(getActivity()).hhPostFile(requestEntity, new Callback.ProgressCallback<String>() {
            @Override
            public void onWaiting() {
                L.e("onWaiting");
            }

            @Override
            public void onStarted() {
                L.e("onStarted");
                callBack.onUploadStart();
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                Log.e("onLoading", "total=" + total + "--current==" + current);
                callBack.onUploadLoading(total, current, isDownloading);
            }

            @Override
            public void onSuccess(String result) {
                L.e("onSuccess");
                callBack.onUploadSucess(item);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                L.e("onError");
                ex.printStackTrace();
                callBack.onUploadError();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                L.e("onCancelled");
            }

            @Override
            public void onFinished() {
                L.e("onFinished");
                callBack.onUploadFinish();
            }
        });
    }


}
