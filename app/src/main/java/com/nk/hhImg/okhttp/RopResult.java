package com.nk.hhImg.okhttp;

/**
 * Created by dax on 2015/12/3.
 */
public class RopResult {

    public static final String CODE_SUCCESS_99999 = "99999"; //成功
    public static final String CODE_FAIL_100000 = "100000";

    private String code;
    private String message;
    private String result;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public boolean isSuccess() {
        return CODE_SUCCESS_99999.equals(code);
    }

    public boolean isFail() {
        return CODE_FAIL_100000.equals(code);
    }
}
