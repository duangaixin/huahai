package com.nk.framework.view;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class NoScrollGridview extends GridView {
    public NoScrollGridview (Context context) {
        super(context);
    }

    public NoScrollGridview (Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollGridview (Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}