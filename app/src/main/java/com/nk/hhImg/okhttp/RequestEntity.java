package com.nk.hhImg.okhttp;

import java.util.Map;

/**
 * Created by dax on 2015/12/3.
 */
public class RequestEntity {

    public String url;
    public String method;
    public Map<String, String> params;
    public boolean paramEncode = false;

    public RequestEntity() {
    }
}
