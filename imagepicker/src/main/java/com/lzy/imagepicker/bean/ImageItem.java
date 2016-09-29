package com.lzy.imagepicker.bean;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by dax on 2016/8/12.
 */
public class ImageItem implements Serializable {

    public static final int UPLOAD_SUCESS = 100;
    public static final int UPLOAD_FAILURE = 101;
    public static final int UPLOAD_NO = 102;
    public static final int UPLOAD_LOADING = 104;

    public String name;       //图片的名字
    public String filePath;     //图片源路径, 不操作,  只做记录
    public String path;       //图片的副本路径, 用来展示, 以及各种操作
    public String compressPath;  //压缩后图片的路径, 这个用于上传
    public long size;         //图片的大小
    public int width;         //图片的宽度
    public int height;        //图片的高度
    public String mimeType;   //图片的类型
    public long addTime;      //图片的创建时间
    public String code;
    public String img_category;
    public int upladaState = UPLOAD_NO;
    public int uniqueCode;
    public int progress;
    public Bitmap bitmap;   //用于旋转等操作, 存在于内存, 当返回传递时置为null

    /**
     * 图片的路径和创建时间相同就认为是同一张图片, 此处添加了新标记dax
     */
    @Override
    public boolean equals(Object o) {
        try {
            ImageItem other = (ImageItem) o;
            return this.path.equalsIgnoreCase(other.path) && this.addTime == other.addTime && this.uniqueCode == other.uniqueCode;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(o);
    }
}
