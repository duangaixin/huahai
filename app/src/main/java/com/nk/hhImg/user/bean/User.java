package com.nk.hhImg.user.bean;

import java.io.Serializable;

/**
 * Created by dax on 2016/7/26.
 */
public class User implements Serializable {
    /**
     * token : 20160831175140
     * phoneNum : 18500915646
     * userName :  段改新
     * userCode : 11180209
     */
    private String token;
    private String phoneNum;
    private String userName;
    private String userCode;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }
}
