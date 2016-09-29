package com.nk.hhImg.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuxin on 16/9/23.
 */

public class HistoryResult implements Serializable{

    private List<HistoryResultEntity> histDatas = new ArrayList<>();

    public List<HistoryResultEntity> getHistDatas() {
        return histDatas;
    }

    public void setHistDatas(List<HistoryResultEntity> histDatas) {
        this.histDatas = histDatas;
    }

    private HistoryResult(){
    }

    public static HistoryResult getInstance() {
        return SingleHolder.instance;
    }

    private static class SingleHolder {
        public static HistoryResult instance = new HistoryResult();
    }
}
