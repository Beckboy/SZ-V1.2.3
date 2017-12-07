package com.junhsue.ksee.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.junhsue.ksee.common.Trace;

import java.util.ArrayList;
import java.util.List;

/**
 * 添加 ViewPager Fragment
 * Created by longer on 17/3/23.
 */

public class CommonFragmentPageAdapter extends FragmentPagerAdapter {


    private List<Fragment> mFragments = new ArrayList<Fragment>();

    public CommonFragmentPageAdapter(FragmentManager fm){
        super(fm);
    }

    public CommonFragmentPageAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.mFragments = fragments;
    }


    public void modifyList(List<Fragment> fragments) {
        mFragments.addAll(fragments);
        notifyDataSetChanged();
    }

    public void clear() {
        if(mFragments.size()>0){
            mFragments.clear();
        }
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }


    @Override
    public int getCount() {
        return (null == mFragments) ? 0 : mFragments.size();
    }
}
