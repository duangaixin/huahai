package com.nk.hhImg.user;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.nk.framework.application.BaseApplication;
import com.nk.framework.baseUtil.ActivityUtil;
import com.nk.hhImg.user.activity.LoginActivity;
import com.nk.hhImg.user.bean.User;
import com.nk.hhImg.user.util.UserSPUtils;

/**
 * Created by dax on 2016/7/26.
 */
public class UserApi {
    public static final String ISCHECKED = "isChecked";

    public static boolean isLogin() {
        String value = UserSPUtils.get(BaseApplication.getInstance(), UserSPUtils.SP_KEY_USER, "");
        if (TextUtils.isEmpty(value)) {
            return false;
        } else {
            User user = new Gson().fromJson(value, User.class);
            if (TextUtils.isEmpty(user.getToken())) {
                return false;
            } else {
                return true;
            }
        }
    }

    public static void setUser(User user) {
        String json = new Gson().toJson(user);
        UserSPUtils.put(BaseApplication.getInstance(), UserSPUtils.SP_KEY_USER, json);
    }

    public static User getUser() throws UserLostException {
        String value = UserSPUtils.get(BaseApplication.getInstance(), UserSPUtils.SP_KEY_USER, "");
        if (TextUtils.isEmpty(value)) {
            openLoginActivity(BaseApplication.getInstance());
            throw new UserLostException();
        } else {
            User user = new Gson().fromJson(value, User.class);
            return user;
        }
    }

    protected static void openLoginActivity(Context context) {
        ActivityUtil.moveToActivity((Activity) context, LoginActivity.class);
    }

    public static void logout() {
        UserSPUtils.clear(BaseApplication.getInstance());
    }
}
