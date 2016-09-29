package com.lzy.imagepicker.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.Utils;
import com.lzy.imagepicker.bean.ImageItem;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by win on 2016/9/19.
 */
public class UploadImagePageAdapter extends PagerAdapter {
    private ArrayList<ImageItem> images = new ArrayList<>();
    private Activity mActivity;
    private ImagePicker imagePicker;
    private int screenWidth;
    private int screenHeight;
    public PhotoViewClickListener listener;

    public void setPhotoViewClickListener(PhotoViewClickListener listener) {
        this.listener = listener;
    }
    public UploadImagePageAdapter(Activity activity, ArrayList<ImageItem> mData) {
        this.mActivity = activity;
        this.images = mData;
        imagePicker = ImagePicker.getInstance();
        DisplayMetrics dm = Utils.getScreenPix(activity);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(mActivity);
        final ImageItem imageItem = images.get(position);
        imagePicker.getImageLoader().displayImage(mActivity, imageItem.path, photoView, screenWidth, screenHeight);
        photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                if (listener != null)
                    listener.OnPhotoTapListener(view, x, y);
            }
        });
        container.addView(photoView);
        return photoView;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public interface PhotoViewClickListener {
        void OnPhotoTapListener(View view, float v, float v1);
    }
}
