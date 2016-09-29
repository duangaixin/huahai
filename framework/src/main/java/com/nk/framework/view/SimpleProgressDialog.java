package com.nk.framework.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nk.framework.R;


/**
 * Created by dax on 2016/7/11.
 */
public class SimpleProgressDialog extends Dialog {

    private ImageView mImageView = null;
    private TextView mTextView = null;
    private AnimationDrawable mAnimationDrawable = null;

    public SimpleProgressDialog(Context context) {
        super(context, R.style.CustomProgressDialog);
        init();
    }

    public SimpleProgressDialog(Context context, int theme) {
        super(context, R.style.CustomProgressDialog);
        init();
    }

    public void init() {
        setContentView(R.layout.view_progressdialog);
        getWindow().getAttributes().gravity = Gravity.CENTER;
        setCancelable(false);
        //
        mTextView = (TextView) findViewById(R.id.text);
        mImageView = (ImageView) findViewById(R.id.image);
        mAnimationDrawable = (AnimationDrawable) mImageView.getBackground();
    }

    @Override
    public void show() {
        super.show();
        mAnimationDrawable.start();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        mAnimationDrawable.stop();
    }

    @Override
    public void hide() {
        super.hide();
        mAnimationDrawable.stop();
    }

    public void setMessage(String msg) {
        if (TextUtils.isEmpty(msg)) {
            mTextView.setVisibility(View.GONE);
        } else {
            mTextView.setVisibility(View.VISIBLE);
            mTextView.setText(msg);
        }
    }
}
