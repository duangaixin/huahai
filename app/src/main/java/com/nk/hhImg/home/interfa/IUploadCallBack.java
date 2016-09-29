package com.nk.hhImg.home.interfa;

import com.lzy.imagepicker.bean.ImageItem;

/**
 * Created by dax on 2016/8/23.
 */
public interface IUploadCallBack {
    void onUploadSucess(ImageItem item);

    void onUploadFinish();

    void onUploadError();

    void onUploadStart();

    void onUploadLoading(long total, long current, boolean isDownloading);
}
