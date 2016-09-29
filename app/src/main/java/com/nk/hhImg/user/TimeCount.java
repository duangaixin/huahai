package com.nk.hhImg.user;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.nk.hhImg.R;

import java.lang.reflect.Field;


/**
 * Created by dax on 2016/9/4.
 */
public class TimeCount extends CountDownTimer {

    private TextView mTextView;
    private Context mContext;
    private long mMillisInFuture;
    private static final long ONE_MINUTE = 60000;
    private boolean isRunning;
    private long nowMillisUntilFinished;

    public TimeCount(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    public TimeCount(long millisInFuture, long countDownInterval, TextView textView, Context context) {
        this(millisInFuture, countDownInterval);
        this.mMillisInFuture = millisInFuture;
        this.mTextView = textView;
        this.mContext = context;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        isRunning = true;
        nowMillisUntilFinished = millisUntilFinished;
        mTextView.setEnabled(false);
        mTextView.setTextColor(mContext.getResources().getColor(R.color.color_FFCACACA));
        mTextView.setText("重新发送(" + (millisUntilFinished / 1000) + ")");
    }

    @Override
    public void onFinish() {
        isRunning = false;
        mTextView.setTextColor(mContext.getResources().getColor(R.color.color_FFF2AD45));
        mTextView.setText("重新获取");
        mTextView.setEnabled(true);
        if(mMillisInFuture != ONE_MINUTE) {
            try {
                Field mMillisInFuture = this.getClass().getSuperclass().getDeclaredField("mMillisInFuture");
                mMillisInFuture.setAccessible(true);
                mMillisInFuture.set(this, ONE_MINUTE);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public String getNowTime() {
        return String.valueOf(nowMillisUntilFinished);
    }

    public boolean isRunning() {
        return isRunning;
    }
}
