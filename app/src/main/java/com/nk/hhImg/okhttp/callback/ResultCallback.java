package com.nk.hhImg.okhttp.callback;

import android.content.DialogInterface;
import android.text.TextUtils;

import com.google.gson.internal.$Gson$Types;
import com.nk.framework.application.BaseApplication;
import com.nk.framework.baseUtil.HhToast;
import com.nk.framework.hhDialog.HhDialogManager;
import com.nk.framework.hhDialog.HhTipDialog;
import com.nk.hhImg.controller.HhBasePresenter;
import com.squareup.okhttp.Request;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;


/**
 * Created by dax on 2016/7/15.
 */
public abstract class ResultCallback<T> {
    public Type mType;
    private HhBasePresenter presenter;
    public boolean mIsFinshActivty = false;

    public ResultCallback() {
        mType = getSuperclassTypeParameter(getClass());
    }

    public ResultCallback(HhBasePresenter presenter) {
        this.presenter = presenter;
        mType = getSuperclassTypeParameter(getClass());
    }

    static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("必须指定数据类型");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    public void onStart(Request request) {
        if (presenter != null) {
            presenter.showProgress();
        }
    }

    public void onFinish() {
        if (presenter != null) {
            presenter.dismissProgress();
        }
    }

    public void inProgress(float progress) {
    }

    public void onNoNetwork() {
        HhToast.showShort(BaseApplication.getInstance(), "当前网络不可用");
    }

    public void onError(Request request, Exception e) {
        if (presenter != null) {
            if (e instanceof SocketTimeoutException) {
                HhDialogManager.showTip(presenter.getActivity(), "", "服务器连接超时!", "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            } else {
                HhDialogManager.showTip(presenter.getActivity(), "", "网络连接错误!", "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            }
        }
    }

    public abstract void onResponse(T response);

    public void onResponseError(String err) {
        if (TextUtils.isEmpty(err)) {
            return;
        }
        if (presenter != null) {
            HhTipDialog hhTipDialog = new HhTipDialog(presenter.getActivity());
            hhTipDialog.setTitle("出错了");
            hhTipDialog.setMessage(err);
            hhTipDialog.setOKButton(null, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (mIsFinshActivty) {
                        presenter.getActivity().finish();
                    }
                }
            });
            hhTipDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if (mIsFinshActivty) {
                        presenter.getActivity().finish();
                    }
                }
            });
            hhTipDialog.show();
            /* if(presenter.getActivity().hasWindowFocus()) {
                hhTipDialog.show();
            }*/
        }
    }

    public static final ResultCallback<String> DEFAULT_RESULT_CALLBACK = new ResultCallback<String>() {
        @Override
        public void onResponse(String response) {
        }
    };

}
