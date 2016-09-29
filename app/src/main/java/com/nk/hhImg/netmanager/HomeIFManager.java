package com.nk.hhImg.netmanager;

import com.nk.hhImg.okhttp.RequestEntity;

import java.util.HashMap;

/**
 * Created by dax on 2016/9/7.
 */
public class HomeIFManager {
    /**
     * 检查版本
     */
    public static RequestEntity getCheckVersionRqEntity() {
        HashMap<String, String> params = new HashMap<>();
        params.put("phoneType", "android");

        RequestEntity requestEntity = new RequestEntity();
        requestEntity.url = NetConstant.CHECK_VERSION;
        requestEntity.params = params;

        return requestEntity;
    }
}
