package com.nk.hhImg.netmanager;

import com.nk.framework.encryption.MD5Util;
import com.nk.hhImg.okhttp.RequestEntity;

import java.util.HashMap;

/**
 * Created by dax on 2016/8/16.
 */
public class UserIFManager {
    public static final String LOGIN_METHOD = "login";
    public static final String REGIST_METHOD = "register";
    public static final String LOGIN_VA_CODE_REGIST_METHOD = "getRegisterValidateCode";
    public static final String VALIDATE_VA_CODE_NEXT = "validate";
    public static final String LOGIN_VA_CODE_FIND_METHOD = "getFindValidateCode";
    public static final String RESET_PWD = "update";

    /**
     * 登陆
     */
    public static RequestEntity getLoginRqEntity(String phoneNum, String userPwd) {

        HashMap<String, String> params = new HashMap<>();
        params.put("phone_num", phoneNum);
        params.put("password", MD5Util.MD5(userPwd));

        RequestEntity requestEntity = new RequestEntity();
        requestEntity.url = NetConstant.USER_URL;
        requestEntity.method = LOGIN_METHOD;
        requestEntity.params = params;

        return requestEntity;
    }

    /**
     * 注册
     */
    public static RequestEntity getRegistRqEntity(String phoneNum, String userRealName, String userNum, String userPwd) {

        HashMap<String, String> params = new HashMap<>();
        params.put("phone_num", phoneNum);
        params.put("userName", userRealName);
        params.put("userCode", userNum);
        params.put("password",MD5Util.MD5(userPwd));

        RequestEntity requestEntity = new RequestEntity();
        requestEntity.url = NetConstant.USER_URL;
        requestEntity.method = REGIST_METHOD;
        requestEntity.params = params;

        return requestEntity;
    }

    /**
     * 注册验证码
     *
     * @param phone
     * @return
     */
    public static RequestEntity getVaCodeRegistEntity(String phone) {

        HashMap<String, String> params = new HashMap<>();
        params.put("phone_num", phone);

        RequestEntity requestEntity = new RequestEntity();
        requestEntity.url = NetConstant.USER_URL;
        requestEntity.method = LOGIN_VA_CODE_REGIST_METHOD;
        requestEntity.params = params;

        return requestEntity;
    }

    /**
     * 修改密码验证码
     *
     * @param phone
     * @return
     */
    public static RequestEntity getVaCodeFindPwdEntity(String phone) {

        HashMap<String, String> params = new HashMap<>();
        params.put("phone_num", phone);

        RequestEntity requestEntity = new RequestEntity();
        requestEntity.url = NetConstant.USER_URL;
        requestEntity.method = LOGIN_VA_CODE_FIND_METHOD;
        requestEntity.params = params;

        return requestEntity;
    }


    /**
     * 校验验证码
     *
     * @param ValidateCode
     * @return
     */
    public static RequestEntity getValiteCodeRqEntity(String phone, String ValidateCode) {

        HashMap<String, String> params = new HashMap<>();
        params.put("phone_num", phone);
        params.put("verification_code", ValidateCode);

        RequestEntity requestEntity = new RequestEntity();
        requestEntity.url = NetConstant.USER_URL;
        requestEntity.method = VALIDATE_VA_CODE_NEXT;
        requestEntity.params = params;

        return requestEntity;
    }

    /**
     * 修改密码
     */
    public static RequestEntity getFindPwdRqEntity(String phoneNum, String userPwd) {

        HashMap<String, String> params = new HashMap<>();
        params.put("phone_num", phoneNum);
        params.put("password", MD5Util.MD5(userPwd));

        RequestEntity requestEntity = new RequestEntity();
        requestEntity.url = NetConstant.USER_URL;
        requestEntity.method = RESET_PWD;
        requestEntity.params = params;

        return requestEntity;
    }

}
