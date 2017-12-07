package com.junhsue.ksee.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

/**
 * 自定义水平滚动视图，为支持SDK 23以下 监听onChangeLister
 * Created by longer on 17/11/29.
 */

public class MyHorizontalScrollView extends HorizontalScrollView {


    private IOnScrollChangeListener mIOnScrollChangeListener;
    //记录x,y的偏移量
    private int mOffsetX,mOffsetY;



    public MyHorizontalScrollView(Context context) {
        super(context);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        Log.i("test","scorllx="+getScrollX()+",lodL="+oldl);
        mOffsetX=getScrollX();
        mOffsetY=getScrollY();
        if (null != mIOnScrollChangeListener) {
            mIOnScrollChangeListener.onScrollChanged(l, t, oldl, oldt);
        }
    }


    public void setIOnScrollChangeListener(IOnScrollChangeListener mIOnScrollChangeListener) {
        this.mIOnScrollChangeListener = mIOnScrollChangeListener;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
    }

    @Override
    public void scrollBy(int x, int y) {
        super.scrollBy(0, y);
    }


    public interface IOnScrollChangeListener {

        /**
         * This is called in response to an internal scroll in this view (i.e., the
         * view scrolled its own contents). This is typically as a result of
         * {@link #scrollBy(int, int)} or {@link #scrollTo(int, int)} having been
         * called.
         *
         * @param l    Current horizontal scroll origin.
         * @param t    Current vertical scroll origin.
         * @param oldl Previous horizontal scroll origin.
         * @param oldt Previous vertical scroll origin.
         */
        void onScrollChanged(int l, int t, int oldl, int oldt);

    }

    public int getOffsetX() {
        return mOffsetX;
    }

    public int getOffsetY() {
        return mOffsetY;
    }
}
