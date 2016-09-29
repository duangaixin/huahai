package com.nk.framework.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by dax on 2016/7/11.
 */
public class AlphaButton2 extends Button {

    private Paint paint;
    private static final int FOREGROUND_COLOR = 0X3D000000;
    private boolean lastStateIsPressed = false;

    public AlphaButton2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public AlphaButton2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AlphaButton2(Context context) {
        super(context);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(FOREGROUND_COLOR);
        setDrawingCacheEnabled(true);

    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (isPressed()) {
            Bitmap alphaBitmap = getDrawingCache().extractAlpha();
            canvas.drawBitmap(alphaBitmap, 0, 0, paint);
        }
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (isPressed()) {
            lastStateIsPressed = true;
            invalidate();
        } else if (lastStateIsPressed) {
            lastStateIsPressed = false;
            invalidate();
        }
    }
}

