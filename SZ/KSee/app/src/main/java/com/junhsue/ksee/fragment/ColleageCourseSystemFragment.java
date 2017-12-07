package com.junhsue.ksee.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.junhsue.ksee.BaseActivity;
import com.junhsue.ksee.CourseDetailsActivity;
import com.junhsue.ksee.CourseSystemApplyActivity;
import com.junhsue.ksee.CourseSystemDetailsActivity;
import com.junhsue.ksee.R;
import com.junhsue.ksee.adapter.CourseSystemAdapter;
import com.junhsue.ksee.dto.CourseDTO;
import com.junhsue.ksee.dto.CourseSystemDTO;
import com.junhsue.ksee.entity.CourseSystem;
import com.junhsue.ksee.net.api.ICourse;
import com.junhsue.ksee.net.api.OKHttpCourseImpl;
import com.junhsue.ksee.net.callback.RequestCallback;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 系统课
 * Created by longer on 17/3/24
 */

public class ColleageCourseSystemFragment extends BaseFragment
        implements AdapterView.OnItemClickListener {


    private ListView mListView;

    private PtrClassicFrameLayout mPtrClassicFrameLayout;

    private CourseSystemAdapter<CourseSystem> mCourseSystemAdapter;

    //当前页
    private int mPageIndex = 0;

    private int mLimit=10;

    private BaseActivity mBaseActivity;

    public static ColleageCourseSystemFragment newInstance() {
        ColleageCourseSystemFragment colleageCourseSystemFragment = new ColleageCourseSystemFragment();
        return colleageCourseSystemFragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.act_colleage_course_system;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mBaseActivity=(BaseActivity)activity;
    }


    @Override
    protected void onInitilizeView(View view) {

        mListView = (ListView) view.findViewById(R.id.lv_course_sysytem);
        mPtrClassicFrameLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptrClassicFrameLayout);

        mCourseSystemAdapter = new CourseSystemAdapter<CourseSystem>(getActivity());
        mListView.setAdapter(mCourseSystemAdapter);
        //
        mListView.setOnItemClickListener(this);
        mPtrClassicFrameLayout.setPtrHandler(ptrDefaultHandler);
        //getCourseSystem();
    }




    PtrDefaultHandler2 ptrDefaultHandler=new PtrDefaultHandler2() {

        @Override
        public void onLoadMoreBegin(PtrFrameLayout frame) {
            mCourseSystemAdapter.cleanList();
            getCourseSystem();
        }

        @Override
        public void onRefreshBegin(PtrFrameLayout frame) {
            getCourseSystem();
        }
    };




    /**
     * 获取系统课*/
    private void getCourseSystem(){
        String business_id=String.valueOf(ICourse.ICourseType.COURSE_TYPE_SYSTEM);
        String pageIndex=String.valueOf(mPageIndex);
        OKHttpCourseImpl.getInstance().getCourseSystem(business_id,String.valueOf(mLimit), pageIndex,
                new RequestCallback<CourseSystemDTO>() {

            @Override
            public void onError(int errorCode, String errorMsg) {

            }

            @Override
            public void onSuccess(CourseSystemDTO response) {
                mPtrClassicFrameLayout.refreshComplete();
                if(null==response || response.list.size()==0) return;
                if(mPageIndex==0)
                    mCourseSystemAdapter.cleanList();

                mCourseSystemAdapter.modifyList(response.list);

                if (mCourseSystemAdapter.getList().size()>=10) {
                    if (mPtrClassicFrameLayout.getMode() != PtrFrameLayout.Mode.BOTH) {
                        mPtrClassicFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
                    }
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        CourseSystem courseSystem=mCourseSystemAdapter.getList().get(position);

        Bundle bundle=new Bundle();
        bundle.putString(CourseDetailsActivity.PARAMS_COURSE_ID,courseSystem.id);
        mBaseActivity.launchScreen(CourseSystemDetailsActivity.class,bundle);
    }


}
