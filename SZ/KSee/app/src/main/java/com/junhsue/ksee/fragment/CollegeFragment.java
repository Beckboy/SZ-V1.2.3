package com.junhsue.ksee.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.junhsue.ksee.BaseActivity;
import com.junhsue.ksee.R;
import com.junhsue.ksee.adapter.CommonFragmentPageAdapter;
import com.junhsue.ksee.common.IColleageMenu;
import com.junhsue.ksee.view.ColleageTabView;
import com.junhsue.ksee.view.CommonNoTouchViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * 塾
 * Created by longer on 17/3/16 in Junhsue.
 */

public class CollegeFragment extends BaseFragment implements ColleageTabView.IColleageTabClickListener {


    private static  String TAG="CollegeFragment";

    private BaseActivity mBaseActivity;

    private   List<Fragment> mFragments=new ArrayList<Fragment>();

    private CommonFragmentPageAdapter mCommonFragmentPageAdapter;

    private ViewPager mViewPager;

    private ColleageTabView mColleageTabView;


    public static CollegeFragment newInstance() {
        CollegeFragment collegeFragment = new CollegeFragment();
        return collegeFragment;
    }


    @Override
    protected int setLayoutId() {
        return R.layout.act_college;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mBaseActivity=(BaseActivity) activity;
    }


    @Override
    protected void onInitilizeView(View view) {
        if(mFragments.size()>0)
            mFragments.clear();
//        mFragments.add(ColleageLiveFragment.newInstance());
        //mFragments.add(ColleageCourseFragment.newInstance());
        mFragments.add(ColleageCourseSubjectFragment.newInstance());
        //mFragments.add(ColleageActivitiesFragment.newInstance());
//        mFragments.add(ColleageClassRoomFragment.newInstance());
        //

        mColleageTabView = (ColleageTabView) view.findViewById(R.id.colleageTabView);
        mColleageTabView.setIColleageTabClickListener(this);

        mCommonFragmentPageAdapter=new CommonFragmentPageAdapter(getChildFragmentManager(),mFragments);
        mViewPager=(CommonNoTouchViewPager)view.findViewById(R.id.view_pager);
        mViewPager.setAdapter(mCommonFragmentPageAdapter);
        mViewPager.setCurrentItem(0,false);
        mViewPager.setOffscreenPageLimit(1);

        mViewPager.setOnPageChangeListener(new PageChangeListener());
    }


    public void setCurrentPage(int position){
        //int  positionC=position>mFragments.size()-1?mFragments.size()-1:position;
        mViewPager.setCurrentItem(position,false);

    }

    @Override
    protected void loadData() {
        super.loadData();
    }

    @Override
    public void onClick(int position) {
        mViewPager.setCurrentItem(position,false);
    }


    class   PageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //改变上面标题的字体选择颜色
            mColleageTabView.setTabIndexColor(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
