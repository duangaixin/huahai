package com.nk.hhImg.controller;

import android.app.Activity;
import android.content.Context;

import com.nk.framework.view.SimpleProgressDialog;


/**
 * Created by dax on 2016/7/11.
 */
public class HhBasePresenter<T> {
    protected SimpleProgressDialog mLoadingDialog = null;
    public T mView;
    public HhBasePresenter(final HhBaseActivity<? extends HhBasePresenter> activity) {
        init(activity);
        setView((T) activity);
    }


    private void init(Context context){

        mLoadingDialog = new SimpleProgressDialog(context);
    }

    public void setView(T view){
        mView =  view;
    }


    /**
     * 显示进度对话框，如果创建了
     */
    public void showProgress() {
        if (mLoadingDialog != null) {
            mLoadingDialog.show();
        }
    }
    /**
     * 隐藏进度对话框，如果创建了
     */
    public void dismissProgress() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }
    /**
     * 加载对话框是否在展示,用这个判断是否在加载要谨慎
     */
    public boolean isLoadingDialogShowing() {
        if (mLoadingDialog != null) {
            return mLoadingDialog.isShowing();
        }
        return false;
    }

    /**
     * 获取 Activity Context
     */
    public Activity getActivity(){
        if(mView instanceof Activity){
            return (Activity) mView;
        }
        else{
            return null;
        }
    }
}
