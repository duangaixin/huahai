package com.nk.hhImg.home.message;


import com.lzy.imagepicker.bean.ImageItem;
import com.nk.framework.message.IMessage;

import java.util.List;
import java.util.Map;

/**
 * Created by wuxin on 16/9/10.
 */
public class RereshMessage implements IMessage {
    public Map<Integer, List<ImageItem>> mapList;

    public RereshMessage(Map<Integer, List<ImageItem>> map) {
        this.mapList = map;
    }
}
