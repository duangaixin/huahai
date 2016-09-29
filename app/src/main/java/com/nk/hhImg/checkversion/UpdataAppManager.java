package com.nk.hhImg.checkversion;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;

import com.nk.framework.baseUtil.ActivityUtil;
import com.nk.framework.baseUtil.AppUtils;
import com.nk.framework.baseUtil.KeyValueSPUtils;
import com.nk.hhImg.checkversion.activity.UpdateActivity;
import com.nk.hhImg.checkversion.bean.CheckAppVersionContent;
import com.nk.hhImg.netmanager.HomeIFManager;
import com.nk.hhImg.okhttp.HhOkNetWork;
import com.nk.hhImg.okhttp.callback.ResultCallback;

/**
 * Created by dgx on 2016/9/7.
 */
public class UpdataAppManager {
    Activity mActivity;

    CheckAppVersionContent mCheckAppVersionContent;
    public static final String NO_NEED_FORCE_UPDATETR = "0";
    public static final String NEED_FORCE_UPDATETR = "1";
    public static final String INTENT_UPDATEAPP = "update";
    /**
     * 保存当前忽略的版本号。如果下次还是这个版本升级 那么就不提示。
     */
    public static final String XML_KEY_INGNORE = "ignore";

    public void startCheck(final Activity mActivity) {
        this.mActivity = mActivity;
        HhOkNetWork.getInstance(mActivity).hhPost(HomeIFManager.getCheckVersionRqEntity(),
                new ResultCallback<CheckAppVersionContent>() {
                    @Override
                    public void onResponse(CheckAppVersionContent response) {
                        mCheckAppVersionContent = response;
                        checkUp();
                    }
                });
    }

    private void checkUp() {
        // 有新版本 无新版 不处理
        if (!String.valueOf(AppUtils.getVersionCode(mActivity)).equals(mCheckAppVersionContent.getVersion())) {
            if (UpdataAppManager.NEED_FORCE_UPDATETR
                    .equals(mCheckAppVersionContent.getState())) {
                toUpdateActivity();
            } else {
                String ignoreVersion = KeyValueSPUtils.getString(mActivity,
                        XML_KEY_INGNORE, "");
                if (TextUtils.isEmpty(ignoreVersion)) {
                    toUpdateActivity();
                } else {
                    // 如果是忽略版本那么不处理 反之
                    if (!ignoreVersion
                            .equals(mCheckAppVersionContent.getState())) {
                        toUpdateActivity();
                    }
                }
            }
        }
    }

    void toUpdateActivity() {

        /*Intent mIntent = new Intent(mActivity,
                UpdateActivity.class);
        mIntent.putExtra(INTENT_UPDATEAPP, mCheckAppVersionContent);
        mActivity.startActivity(mIntent);*/

        Bundle bundle = new Bundle();
        bundle.putString(INTENT_UPDATEAPP, String.valueOf(mCheckAppVersionContent));
        ActivityUtil.moveToActivityAndFinish(mActivity, UpdateActivity.class, bundle);
    }
}
