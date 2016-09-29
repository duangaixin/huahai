package com.lzy.imagepicker.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.imagepicker.R;
import com.lzy.imagepicker.Utils;
import com.lzy.imagepicker.adapter.UploadImagePageAdapter;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.view.ViewPagerFixed;

import java.util.ArrayList;

/**
 * Created by dax on 2016/9/19.
 */
public class UploadImagePreviewActivity extends ImageBaseActivity {
    private View topBar;
    private View content;
    private int mCurrentPosition;
    ArrayList<ImageItem> mData;
    TextView mTitleCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadimage_preview);

        ViewPagerFixed viewPager = (ViewPagerFixed) findViewById(R.id.viewpager);
       mTitleCount = (TextView) findViewById(R.id.tv_des);
        content = findViewById(R.id.content);
        topBar = findViewById(R.id.top_bar);
        topBar.findViewById(R.id.btn_ok).setVisibility(View.GONE);

        Bundle bundle = getIntent().getExtras();
        int position = (int) bundle.get("position");
        mData = (ArrayList<ImageItem>) bundle.getSerializable("data");

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) topBar.getLayoutParams();
        params.topMargin = Utils.getStatusHeight(this);
        topBar.setLayoutParams(params);

        topBar.findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        UploadImagePageAdapter mAdapter = new UploadImagePageAdapter(this, mData);
        mAdapter.setPhotoViewClickListener(new UploadImagePageAdapter.PhotoViewClickListener() {
            @Override
            public void OnPhotoTapListener(View view, float v, float v1) {
                onImageSingleTap();
            }
        });
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(position, false);
        mTitleCount.setText(getString(R.string.preview_image_count, position + 1, mData.size()));
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mCurrentPosition = position;
                mTitleCount.setText(getString(R.string.preview_image_count, mCurrentPosition + 1, mData.size()));
            }
        });

    }


    public void onImageSingleTap() {
        if (topBar.getVisibility() == View.VISIBLE) {
            topBar.setAnimation(AnimationUtils.loadAnimation(this, R.anim.top_out));
            topBar.setVisibility(View.GONE);
            tintManager.setStatusBarTintResource(R.color.transparent);//通知栏所需颜色
            //给最外层布局加上这个属性表示，Activity全屏显示，且状态栏被隐藏覆盖掉。
            if (Build.VERSION.SDK_INT >= 16)
                content.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        } else {
            topBar.setAnimation(AnimationUtils.loadAnimation(this, R.anim.top_in));
            topBar.setVisibility(View.VISIBLE);
            tintManager.setStatusBarTintResource(R.color.status_bar);//通知栏所需颜色
            //Activity全屏显示，但状态栏不会被隐藏覆盖，状态栏依然可见，Activity顶端布局部分会被状态遮住
            if (Build.VERSION.SDK_INT >= 16)
                content.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }
}
