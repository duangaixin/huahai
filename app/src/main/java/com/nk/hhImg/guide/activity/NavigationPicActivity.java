package com.nk.hhImg.guide.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.nk.framework.controller.BaseActivity;
import com.nk.framework.view.CirclePageIndicator;
import com.nk.hhImg.R;
import com.nk.hhImg.guide.LocalImageManger;
import com.nk.hhImg.guide.adapter.NaviViewPagerAdapter;

/**
 * Created by dgx on 2016/9/7.
 */
public class NavigationPicActivity extends BaseActivity {
    private ViewPager mNaviPager;
    private NaviViewPagerAdapter mNaviPagerAdapter;
    private CirclePageIndicator mNaviIndicator;
    private int[] viewBackId = {R.mipmap.az1, R.mipmap.az2, R.mipmap.az3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_single_product_bigimage);
        initPager();
    }

    private void initPager() {
        mNaviPagerAdapter = new NaviViewPagerAdapter(this, viewBackId);
        mNaviPager = (ViewPager) findViewById(R.id.sigle_pager);
        mNaviPager.setAdapter(mNaviPagerAdapter);
        mNaviIndicator = (CirclePageIndicator) findViewById(R.id.sigle_indicator);
        mNaviIndicator.setViewPager(mNaviPager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalImageManger.getInstance().recycleAllBitmap(NavigationPicActivity.this.hashCode());
    }
}
