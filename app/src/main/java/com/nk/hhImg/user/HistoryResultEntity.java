package com.nk.hhImg.user;

import android.text.TextUtils;

import com.lzy.imagepicker.bean.ImageItem;
import com.nk.hhImg.home.bean.SelectInfoResult;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by wuxin on 16/9/23.
 */

public class HistoryResultEntity implements Serializable{
    private Long scanTime;
    private Map<Integer, List<ImageItem>> mapList;
    private SelectInfoResult result;
    private String totleSize;
    private String successSize;

    public Long getScanTime() {
        return TextUtils.isEmpty(String.valueOf(scanTime)) ? System.currentTimeMillis() : scanTime;
    }

    public void setScanTime(Long scanTime) {
        this.scanTime = scanTime;
    }

    public Map<Integer, List<ImageItem>> getMapList() {
        return mapList;
    }

    public void setMapList(Map<Integer, List<ImageItem>> mapList) {
        this.mapList = mapList;
    }

    public SelectInfoResult getResult() {
        return result;
    }

    public void setResult(SelectInfoResult result) {
        this.result = result;
    }

    public String getTotleSize() {
        return totleSize;
    }

    public void setTotleSize(String totleSize) {
        this.totleSize = totleSize;
    }

    public String getSuccessSize() {
        return successSize;
    }

    public void setSuccessSize(String successSize) {
        this.successSize = successSize;
    }
}
