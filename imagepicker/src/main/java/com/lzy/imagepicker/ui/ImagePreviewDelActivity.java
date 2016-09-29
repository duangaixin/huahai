package com.lzy.imagepicker.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.imagepicker.CompressImageHelper;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.R;
import com.lzy.imagepicker.bean.ImageItem;
import com.nk.framework.hhDialog.HhDialogManager;

import java.util.ArrayList;

/**
 * Created by dax on 2016/9/12.
 */
public class ImagePreviewDelActivity extends ImagePreviewBaseActivity implements View.OnClickListener {
    private ImageItem item;
    private int degree;
    private TextView right_tv;
    private TextView left_tv;
    private TextView center_tv;
    private LinearLayout ll;
    private ImageView mBtnDel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
          /*
          裁剪
        TextView txt_crop = (TextView) findViewById(R.id.txt_crop);
        txt_crop.setOnClickListener(this);
        txt_crop.setVisibility(View.VISIBLE);*/

        topBar.findViewById(R.id.btn_back).setOnClickListener(this);
        mBtnDel = (ImageView) findViewById(R.id.btn_del);
        ll = (LinearLayout) findViewById(R.id.handle_img);
        right_tv = (TextView) findViewById(R.id.rigth_tv);
        left_tv = (TextView) findViewById(R.id.left_tv);
        center_tv = (TextView) findViewById(R.id.center_tv);

        item = mImageItems.get(mCurrentPosition);
          if (item.upladaState==item.UPLOAD_SUCESS){
              ll.setVisibility(View.GONE);
              mBtnDel.setVisibility(View.GONE);
          }else{
              ll.setVisibility(View.VISIBLE);
              mBtnDel.setVisibility(View.VISIBLE);
          }

        mBtnDel.setOnClickListener(this);
        right_tv.setOnClickListener(this);
        left_tv.setOnClickListener(this);
        center_tv.setOnClickListener(this);


        mTitleCount.setText(getString(R.string.preview_image_count, mCurrentPosition + 1, mImageItems.size()));
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position;
                item = mImageItems.get(mCurrentPosition);
                if (item.upladaState==item.UPLOAD_SUCESS){
                    ll.setVisibility(View.GONE);
                    mBtnDel.setVisibility(View.GONE);
                }else{
                    ll.setVisibility(View.VISIBLE);
                    mBtnDel.setVisibility(View.VISIBLE);
                }

                mTitleCount.setText(getString(R.string.preview_image_count, mCurrentPosition + 1, mImageItems.size()));
            }
        });

    }

    private Bitmap editPicPrepare() {

        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(item.path, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = 800f;
        float ww = 480f;
        int be = 1;
        if (w > h && w > ww) {
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;
        bitmap = BitmapFactory.decodeFile(item.path, newOpts);
        return bitmap;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_del) {
            if (item != null && item.upladaState != item.UPLOAD_SUCESS) {
                showDeleteDialog();
            }
        } else if (id == R.id.btn_back) {
            onBackPressed();
        } else if (id == R.id.txt_crop) {
            //裁剪
            Intent intent = new Intent(this, ImageCropActivity.class);
            intent.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, mImageItems);
            intent.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, mCurrentPosition);
            startActivityForResult(intent, ImagePicker.RESULT_CODE_ITEMS);
        } else if (id == R.id.rigth_tv) {
            if (item != null && item.upladaState != item.UPLOAD_SUCESS) {
                Bitmap bitmap = editPicPrepare();
                degree += 90;
                Bitmap bg = rotateBmp(degree, bitmap);
                item.bitmap = bg;
                mAdapter.notifyDataSetChanged();
            }

        } else if (id == R.id.left_tv) {
            if (item != null && item.upladaState != item.UPLOAD_SUCESS) {
                Bitmap bitmap = editPicPrepare();
                degree -= 90;
                Bitmap bg = rotateBmp(degree, bitmap);
                item.bitmap = bg;
                mAdapter.notifyDataSetChanged();
            }

        } else if (id == R.id.center_tv) {
            if (item != null && item.upladaState != item.UPLOAD_SUCESS) {
                Bitmap bitmap = editPicPrepare();
                degree -= 180;
                Bitmap bg = rotateBmp(degree, bitmap);
                item.bitmap = bg;
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    public static Bitmap rotateBmp(int degree, Bitmap bitmap) {
        Bitmap resultBmp = null;
        if (degree != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(degree);
            resultBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            if (resultBmp != bitmap) {
                bitmap.recycle();
            }
        } else {
            resultBmp = bitmap;
        }
        return resultBmp;
    }

    private void showDeleteDialog() {
        HhDialogManager.showAlert(ImagePreviewDelActivity.this, "温馨提示", "您确定要删除这张照片么？", "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mImageItems.remove(mCurrentPosition);
                if (mImageItems.size() > 0) {
                    mAdapter.setData(mImageItems);
                    mAdapter.notifyDataSetChanged();
                    mTitleCount.setText(getString(R.string.preview_image_count, mCurrentPosition + 1, mImageItems.size()));
                } else {
                    onBackPressed();
                }
            }
        }, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, mImageItems);
        for (ImageItem imageItem : mImageItems) {
            if (imageItem.bitmap != null) {
                imageItem = CompressImageHelper.getInstance().bmpToFile(imageItem);
            }
        }
        setResult(ImagePicker.RESULT_CODE_BACK, intent);
        finish();
        super.onBackPressed();
    }

    /**
     * 单击时，隐藏头和尾
     */
    @Override
    public void onImageSingleTap() {
        if (topBar.getVisibility() == View.VISIBLE) {
            topBar.setAnimation(AnimationUtils.loadAnimation(this, com.lzy.imagepicker.R.anim.top_out));
         //   ll.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out));
            topBar.setVisibility(View.GONE);
          //  ll.setVisibility(View.GONE);
            tintManager.setStatusBarTintResource(com.lzy.imagepicker.R.color.transparent);//通知栏所需颜色
            //给最外层布局加上这个属性表示，Activity全屏显示，且状态栏被隐藏覆盖掉。
            if (Build.VERSION.SDK_INT >= 16)
                content.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        } else {
            topBar.setAnimation(AnimationUtils.loadAnimation(this, com.lzy.imagepicker.R.anim.top_in));
          //  ll.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
            topBar.setVisibility(View.VISIBLE);
          //  ll.setVisibility(View.VISIBLE);
            tintManager.setStatusBarTintResource(com.lzy.imagepicker.R.color.status_bar);//通知栏所需颜色
            //Activity全屏显示，但状态栏不会被隐藏覆盖，状态栏依然可见，Activity顶端布局部分会被状态遮住
            if (Build.VERSION.SDK_INT >= 16)
                content.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ImagePicker.RESULT_CODE_ITEMS && resultCode == ImagePicker.REQUEST_CODE_CROP) {
            Bundle bundle = data.getExtras();
            mCurrentPosition = bundle.getInt(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION);
            mImageItems = (ArrayList<ImageItem>) bundle.getSerializable(ImagePicker.EXTRA_IMAGE_ITEMS);
            mAdapter.setData(mImageItems);
            mAdapter.notifyDataSetChanged();
            mViewPager.setAdapter(mAdapter);
            mViewPager.setCurrentItem(mCurrentPosition, false);
        }
    }
}