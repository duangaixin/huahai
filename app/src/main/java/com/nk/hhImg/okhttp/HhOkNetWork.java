package com.nk.hhImg.okhttp;

import android.util.Pair;

import com.nk.framework.baseUtil.HhUtils;
import com.nk.framework.baseUtil.L;
import com.nk.hhImg.okhttp.callback.ResultCallback;
import com.nk.hhImg.okhttp.request.OkHttpPostRequest;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by dax on 2015/12/2.
 */
public class HhOkNetWork {

    private static Object tag;
    private static HhOkNetWork mInstance;
    private Map<String, String> mPracticalParams;
    private static final int TIME_OUT = 60 * 1000;
    //private static final String MESSAGE_FORMAT = "json";

    private HhOkNetWork() {
    }

    public static HhOkNetWork getInstance(Object obj) {
        tag = obj;
        if (mInstance == null) {
            synchronized (OkHttpClientManager.class) {
                if (mInstance == null) {
                    mInstance = new HhOkNetWork();
                }
            }
        }
        return mInstance;
    }

    /**
     * 普通请求
     *
     * @param entity
     * @param callback
     */
    public void hhPost(RequestEntity entity, ResultCallback callback) {
        entity.params.put("method", entity.method == null ? "" : entity.method);
        OkHttpPostRequest okHttpPostRequest = new OkHttpPostRequest(entity.url, tag, entity.params, null, null, null, null, null);
        L.i("request======== \n" + entity.url + "?" + HhUtils.getUrlParamsByMap(entity.params));
        okHttpPostRequest.invokeAsyn(callback);
    }

    /**
     * 上传文件请求
     *
     * @param entity
     * @param callback
     */
    public void hhPostFile(UploadRequestEntity entity, Callback.ProgressCallback callback) {
        mPracticalParams = entity.params;
        Map<String, String> finalParams = getParames(entity);
        RequestParams requestParams = new RequestParams(entity.url);
        requestParams.setConnectTimeout(TIME_OUT);
        requestParams.setMultipart(true);

        for (Pair<String, File> pair : entity.files) {
            requestParams.addBodyParameter(pair.first, pair.second);
        }

        if (entity.uploadParams != null) {
            for (String key : entity.uploadParams.keySet()) {
                requestParams.addBodyParameter(key, entity.uploadParams.get(key));
            }
        }
        x.http().post(requestParams, callback);
    }

    /**
     * 普通请求参数封装
     *
     * @param entity
     * @return
     */
    private Map<String, String> getParames(RequestEntity entity) {
        HashMap<String, String> parames = new HashMap<>();
        parames.put("method", entity.method == null ? "" : entity.method);
        if (mPracticalParams == null) {
            mPracticalParams = Collections.emptyMap();
        } else {
            //参数为null时，换位空字符串
            for (String key : mPracticalParams.keySet()) {
                if (null == mPracticalParams.get(key)) {
                    mPracticalParams.put(key, "");
                }
            }
        }
        String practicalParamsStr = practicalParamsMap2Str();
        L.i("params === \n" + practicalParamsStr);
        if (entity.paramEncode) {
        } else {
        }
        return parames;
    }

    private void addCommonParams(HashMap<String, String> parames) {
    }
    private String practicalParamsMap2Str() {
        if (!mPracticalParams.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("{");
            for (String practicalKey : mPracticalParams.keySet()) {
                stringBuilder.append("\"").append(practicalKey).append("\":");
                String value = mPracticalParams.get(practicalKey);
                if (value.startsWith("{") || value.startsWith("[")) {
                    stringBuilder.append(value).append(",");
                } else {
                    stringBuilder.append("\"").append(value == null ? "" : value).append("\",");
                }
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1).append("}");
            return stringBuilder.toString();
        } else {
            return "{}";
        }
    }
}