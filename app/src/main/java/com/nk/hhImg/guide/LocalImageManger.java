package com.nk.hhImg.guide;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;

import com.nk.framework.application.BaseApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by dgx on 2016/9/7.
 */
public class LocalImageManger {
    private static LocalImageManger mImageManger;
    private static HashMap<Integer, List<View>> mActivityViewListHashMap = new HashMap<Integer, List<View>>();

    public static LocalImageManger getInstance() {
        if (mImageManger == null) {
            mImageManger = new LocalImageManger();
        }
        return mImageManger;
    }

    public void setBackground(int activityCode, View mView, int rid) {
        try {
            Resources mResources = BaseApplication.getInstance().getResources();
            Bitmap bm = BitmapFactory.decodeResource(mResources, rid);
            BitmapDrawable bd = new BitmapDrawable(mResources, bm);
            mView.setBackgroundDrawable(bd);

            if (mActivityViewListHashMap.containsKey(activityCode)) {
                mActivityViewListHashMap.get(activityCode).add(mView);
            } else {
                List<View> mList = new ArrayList<View>();
                mList.add(mView);
                mActivityViewListHashMap.put(activityCode, mList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void recycleBitmap(int activityCode, View mView) {
        try {
            if (mActivityViewListHashMap.containsKey(activityCode)) {
                recycleBitmap(mView);
                mActivityViewListHashMap.get(activityCode).remove(mView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void recycleBitmap(View mView) {
        try {
            BitmapDrawable bd = (BitmapDrawable) mView.getBackground();
            mView.setBackgroundResource(0);
            if (bd != null) {
                bd.setCallback(null);
                if (bd.getBitmap() != null && !bd.getBitmap().isRecycled()) {
                    bd.getBitmap().recycle();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void recycleAllBitmap(int activityCode) {
        try {
            if (mActivityViewListHashMap.containsKey(activityCode)) {
                List<View> mList = mActivityViewListHashMap.get(activityCode);
                for (int i = 0; i < mList.size(); i++) {
                    recycleBitmap(mList.get(i));
                }
                mList.clear();
                mActivityViewListHashMap.remove(activityCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
