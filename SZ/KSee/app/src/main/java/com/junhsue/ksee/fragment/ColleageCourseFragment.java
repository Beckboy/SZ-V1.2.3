package com.junhsue.ksee.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.junhsue.ksee.R;
import com.junhsue.ksee.adapter.CommonFragmentPageAdapter;
import com.junhsue.ksee.view.ColleageCourseView;

import java.util.ArrayList;
import java.util.List;

/**
 * 课程
 * Created by longer on 17/3/22.
 */

public class ColleageCourseFragment extends BaseFragment implements ColleageCourseView.IColleageTabClickListener {


    private ViewPager mViewPager;

    private List<Fragment> mFragments=new ArrayList<Fragment>();


    private CommonFragmentPageAdapter mCommonFragmentPageAdapter;

    private ColleageCourseView mColleageCourseView;

    private Activity mActivity;

    public static  ColleageCourseFragment newInstance(){
        ColleageCourseFragment colleageCourseFragment=new ColleageCourseFragment();
        return colleageCourseFragment;
    }


    @Override
    protected int setLayoutId() {
        return R.layout.act_colleage_course;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity=(Activity)activity;
    }

    @Override
    protected void onInitilizeView(View view) {

        if(mFragments.size()>0)
        mFragments.clear();
        mFragments.add(ColleageCourseSubjectFragment.newInstance());
//        mFragments.add(ColleageCourseSystemFragment.newInstance());

        mViewPager=(ViewPager)view.findViewById(R.id.course_view_pager);
        mColleageCourseView=(ColleageCourseView)view.findViewById(R.id.colleage_tab_view);

        mCommonFragmentPageAdapter=new CommonFragmentPageAdapter(getChildFragmentManager(),mFragments);
        mViewPager.setAdapter(mCommonFragmentPageAdapter);
        //
        mViewPager.setOnPageChangeListener(onPageChangeListener);
        mColleageCourseView.setIColleageTabClickListener(this);
        mViewPager.setCurrentItem(0);

    }


    @Override
    public void onClick(int index) {
        mViewPager.setCurrentItem(index);
    }



    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {


        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            mColleageCourseView.setTabStatus(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
