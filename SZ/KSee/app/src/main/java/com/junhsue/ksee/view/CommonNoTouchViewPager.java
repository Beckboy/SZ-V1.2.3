package com.junhsue.ksee.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 *
 * 禁用左右切换的ViewPager
 * Created by longer on 17/3/24.
 */



public class CommonNoTouchViewPager extends ViewPager{


    private Context mContext;

    //是否左右切换
    private boolean isTouchMove=false;


    public CommonNoTouchViewPager(Context context) {
        super(context);
        mContext=context;
    }

    public CommonNoTouchViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(isTouchMove==false){
            return false;
        }
        return super.onTouchEvent(ev);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(isTouchMove==false){
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
