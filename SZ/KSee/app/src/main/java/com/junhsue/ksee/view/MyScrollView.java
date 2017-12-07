package com.junhsue.ksee.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by Sugar on 17/4/28.
 */

public class MyScrollView extends ScrollView {

    private OnScrollChangedListener scrollListener;

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        if (scrollListener != null) {
            scrollListener.onScrollChanged(MyScrollView.this, x, y, oldx, oldy);
        }
    }

    public interface OnScrollChangedListener {
        void onScrollChanged(MyScrollView view, int x, int y, int oldx, int oldy);
    }

    public void setScrollChangedListener(OnScrollChangedListener listener) {
        scrollListener = listener;
    }

}
