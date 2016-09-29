package com.nk.framework.hhDialog;

import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by dgx on 2016/7/11.
 */
public class HhDialogManager {

    /**
     * 显示一个alert dialog
     *
     * @param context        要显示dialog的context
     * @param title          dialog标题，为空则不显示标题
     * @param msg            dialog 内容信息
     * @param okText         dialog确定按钮文字，传空则显示确定
     * @param okListener     dialog确定按钮事件监听
     * @param cancelText     dialog取消按按钮文字，传空则显示确定
     * @param cancelListener dialog取消按钮事件监听
     * @return MSAlertDialog
     */
    public static HhAlertDialog showAlert(Context context, CharSequence title, CharSequence msg, CharSequence okText, DialogInterface.OnClickListener okListener, CharSequence cancelText, DialogInterface.OnClickListener cancelListener) {
        HhAlertDialog msAlertDialog = new HhAlertDialog(context);
        msAlertDialog.setTitle(title);
        msAlertDialog.setMessage(msg);
        msAlertDialog.setOKButton(okText, okListener);
        msAlertDialog.setCancelButton(cancelText, cancelListener);
        msAlertDialog.show();
        return msAlertDialog;
    }


    /**
     * 显示一个tip dialog
     *
     * @param context    要显示dialog的context
     * @param title      dialog标题，为空则不显示标题
     * @param msg        dialog 内容信息
     * @param okText     dialog确定按钮文字，传空则显示确定
     * @param okListener dialog确定按钮事件监听
     * @return
     */

    public static HhTipDialog showTip(Context context, CharSequence title, CharSequence msg, CharSequence okText, DialogInterface.OnClickListener okListener) {
        HhTipDialog hhTipDialog = new HhTipDialog(context);
        hhTipDialog.setTitle(title);
        hhTipDialog.setMessage(msg);
        hhTipDialog.setOKButton(okText, okListener);
        hhTipDialog.show();
        return hhTipDialog;
    }


}