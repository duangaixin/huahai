package com.nk.framework.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

/**
 * Created by dax on 2016/7/11.
 */
public class AlphaButton extends Button {

    private boolean changeBackgroudAlphaOnPress = false;

    private Drawable mBgDrawable;

    private int originalAlpha = 0xff;

    Paint paint;

    public AlphaButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public AlphaButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AlphaButton(Context context) {
        super(context);
        init();
    }

    private void init() {

        Drawable background = getBackground();
        if (background != null && !(background instanceof StateListDrawable)) {
            changeBackgroudAlphaOnPress = true;
            mBgDrawable = background;
            // originalAlpha = background.getAlpha();
        }

    }


    @SuppressLint("NewApi")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (changeBackgroudAlphaOnPress)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        setAlpha(0.7f);
                    } else
                        mBgDrawable.setAlpha((int) (originalAlpha * (0.7f)));
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (changeBackgroudAlphaOnPress)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        setAlpha(1f);
                    } else
                        mBgDrawable.setAlpha(originalAlpha);
                break;
            default:
                break;
        }

        return super.onTouchEvent(event);
    }
}
