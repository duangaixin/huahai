package com.nk.hhImg.guide.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;

import com.nk.hhImg.guide.GuideUtil;
import com.nk.hhImg.guide.ViewPagerItemView;
import com.nk.hhImg.user.activity.LoginActivity;


/**
 * Created by dgx on 2016/9/7.
 */
public class NaviViewPagerAdapter extends PagerAdapter {
    private Activity mActivity;
    private int[] arrays;

    public NaviViewPagerAdapter(Activity context, int[] arrays) {
        this.mActivity = context;
        this.arrays = arrays;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ViewPagerItemView itemView = (ViewPagerItemView) object;
        itemView.recycle();
    }

    @Override
    public void finishUpdate(View view) {
    }


    @Override
    public int getCount() {
        return arrays.length;
    }

    @Override
    public Object instantiateItem(View container, int position) {
        ViewPagerItemView itemView;
        itemView = new ViewPagerItemView(mActivity);
        itemView.setData(arrays[position]);
        if (position == arrays.length - 1) {
            itemView.getButton().setVisibility(View.VISIBLE);
            itemView.getButton().setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
                    mActivity.finish();
                    GuideUtil.setShouldOpenNaviFalse(mActivity);
                }
            });
        }
        ((ViewPager) container).addView(itemView);
        return itemView;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(View view) {
    }
}
