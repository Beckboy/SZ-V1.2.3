package com.junhsue.ksee.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * 直播live
 * Created by longer on 17/4/18.
 */


public class LiveFragmentAdapter extends FragmentStatePagerAdapter implements IAdapterOperate<Fragment> {


    private List<Fragment> mFragments = new ArrayList<>();


    public LiveFragmentAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public int getCount() {
        return mFragments.size();
    }


    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }


    @Override
    public void modifyData(List<Fragment> list) {
        if (null != list ) {
            mFragments.clear();
            mFragments.addAll(list);
            notifyDataSetChanged();
        }
    }


    @Override
    public List<Fragment> getList() {
        return mFragments;
    }


    @Override
    public void clearList() {
        mFragments.clear();
    }
}
