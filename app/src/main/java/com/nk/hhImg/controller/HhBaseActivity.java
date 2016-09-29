package com.nk.hhImg.controller;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.view.CropImageView;
import com.nk.framework.baseUtil.SPUtils;
import com.nk.framework.controller.BaseActivity;
import com.nk.hhImg.R;
import com.nk.hhImg.home.GlideImageLoader;


/**
 * Created by dgx on 2016/7/11.
 */
public abstract class HhBaseActivity<T extends HhBasePresenter> extends BaseActivity {
    protected T mBasePresenter = null;
    ViewGroup topBox;
    ViewGroup baseContainer;
    ImageView back;
    TextView topTitle;
    LayoutInflater mInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mInflater = LayoutInflater.from(this);
        super.setContentView(R.layout.activity_hhbase);

        back = (ImageView) findViewById(R.id.back);
        topBox = (RelativeLayout) findViewById(R.id.top_box);
        topTitle = (TextView) findViewById(R.id.top_title);
        baseContainer = (ViewGroup) findViewById(R.id.activity_content);
        initImagePicker();
    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);
        imagePicker.setCrop(false);                           //允许裁剪
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(200);              //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(800);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(600);                         //保存文件的高度。单位像素
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        View back = findViewById(R.id.back);
        if (null != back) {
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onNavigateBack();

                }
            });
        }
    }

    protected void onNavigateBack() {
        onBackPressed();
    }


    public void superSetContentView(View v) {
        super.setContentView(v);
    }

    @Override
    public void setContentView(int layoutResID) {
        View contentView = mInflater.inflate(layoutResID, baseContainer, false);
        realSetContentView(contentView, null);
    }

    @Override
    public void setContentView(View view) {
        realSetContentView(view, null);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        realSetContentView(view, params);
    }

    private void realSetContentView(View view, ViewGroup.LayoutParams params) {
        //如果布局中有top_box 则删除 顶层布局中的top_box
        if (null != view.findViewById(R.id.top_box)) {
            ((ViewGroup) topBox.getParent()).removeView(topBox);
        }
        baseContainer.removeAllViews();
        if (null != params) {
            baseContainer.addView(view, params);
        } else {
            baseContainer.addView(view);
        }
        back = (ImageView) findViewById(R.id.back);
        topBox = (RelativeLayout) findViewById(R.id.top_box);
        topTitle = (TextView) findViewById(R.id.top_title);
    }


    protected void setTopTitle(String title) {
        topTitle.setText(title);
    }

    protected void setTopTitle(int resId) {
        setTopTitle(getResources().getString(resId));
    }

    protected void hideBackView(boolean isHide) {
        back.setVisibility(isHide ? View.INVISIBLE : View.VISIBLE);
    }


    public void hideTopBox(boolean isHide) {
        topBox.setVisibility(isHide ? View.GONE : View.VISIBLE);
    }

    public ImageView getBackView() {
        return back;
    }

    public ViewGroup getTopBox() {
        return topBox;
    }
    public TextView getTopTitleView() {
        return topTitle;
    }

    public boolean checkPermission(@NonNull String permission) {
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            SPUtils.clear(this,"sp_sms_reset");
            SPUtils.clear(this,"sp_sms_regist");
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



}
