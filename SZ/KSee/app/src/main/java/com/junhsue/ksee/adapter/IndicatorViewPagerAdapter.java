package com.junhsue.ksee.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


/**
 * <p/>
 * Viewpager适配器
 */
public class IndicatorViewPagerAdapter extends PagerAdapter {
    private List<View> pagerViewList;
    private int mChildCount = 0;

    public IndicatorViewPagerAdapter(List<View> pager) {
        this.pagerViewList = pager;
    }

    @Override
    public int getCount() {
        return pagerViewList.size();
    }

    @Override  //销毁position位置的界面
    public void destroyItem(ViewGroup container, int position, Object object) {

        ((ViewPager) container).removeView(pagerViewList.get(position));
    }

    @Override //初始化position的界面
    public Object instantiateItem(ViewGroup container, int position) {
        ((ViewPager) container).addView(pagerViewList.get(position));
        return pagerViewList.get(position);

    }

    @Override //判断是否有对象生成界面
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void notifyDataSetChanged() {
        mChildCount = getCount();
        super.notifyDataSetChanged();

    }

    /**
     * 重写该方法用来实现pageAdapter刷新
     * @param object
     * @return
     */
    @Override
    public int getItemPosition(Object object) {
        if (mChildCount > 0) {
            mChildCount--;
            return POSITION_NONE; //该方法返回这个，notityDateSetChanged 才会刷新起作用
        }
        return super.getItemPosition(object);

    }


}
