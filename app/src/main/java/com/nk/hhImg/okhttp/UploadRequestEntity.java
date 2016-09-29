package com.nk.hhImg.okhttp;

import android.util.Pair;

import java.io.File;
import java.util.Map;

/**
 * Created by dax on 2015/12/31.
 */
public class UploadRequestEntity extends RequestEntity {

    public Pair<String, File>[] files;
    public Map<String, String> uploadParams;
}
