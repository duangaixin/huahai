package com.nk.hhImg.user;

import android.util.Pair;

import com.lzy.imagepicker.bean.ImageItem;
import com.nk.hhImg.checkversion.FileUtils;
import com.nk.hhImg.home.bean.SelectInfoResult;

import java.util.List;
import java.util.Map;

/**
 * Created by wuxin on 16/9/23.
 */

public class HistoryHelper {

    public static HistoryResultEntity lastEntiry;

    public static HistoryResultEntity createEntiry() {
        if(lastEntiry == null) {
            lastEntiry = new HistoryResultEntity();
        }
        return lastEntiry;
    }

    public static void recordScanTime() {
        lastEntiry = null;
        createEntiry().setScanTime(System.currentTimeMillis());
    }

    public static void recordScanResult(SelectInfoResult result) {
        createEntiry().setResult(result);
    }

    public static void recordUploadData(Map<Integer, List<ImageItem>> mapList) {
        createEntiry().setMapList(mapList);
    }

    public static Long getScanTime() {
        return createEntiry().getScanTime();
    }

    public static void recordUpladResultNum(Pair pair) {
        createEntiry().setSuccessSize(pair.first.toString());
        createEntiry().setTotleSize(pair.second.toString());
    }

    public static void removeHistoryData(Long scanTime) {
        HistoryResult.getInstance().getHistDatas().remove(scanTime);
    }

    public static void saveHistoryData() {
        HistoryResult.getInstance().getHistDatas().add(createEntiry());

        FileUtils fileUtils = new FileUtils();
        fileUtils.writeObjToFile(FileUtils.HISTORY_DATA, HistoryResult.getInstance());
    }

    public static HistoryResult getHistoryData() {
        FileUtils fileUtils = new FileUtils();
        return (HistoryResult) fileUtils.readObjectFromFile(FileUtils.HISTORY_DATA);
    }
}
