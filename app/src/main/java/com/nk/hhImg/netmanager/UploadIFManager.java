package com.nk.hhImg.netmanager;

import com.nk.hhImg.okhttp.RequestEntity;

import java.util.HashMap;

/**
 * Created by dax on 2016/8/11.
 */
public class UploadIFManager {


    public static RequestEntity getSelectPageInfoRqEntity(String phoneNum, String scanUrl) {

        HashMap<String, String> params = new HashMap<>();
        params.put("phone_num", phoneNum);

        RequestEntity requestEntity = new RequestEntity();
        requestEntity.url = scanUrl;
        requestEntity.params = params;

        return requestEntity;
    }

}
