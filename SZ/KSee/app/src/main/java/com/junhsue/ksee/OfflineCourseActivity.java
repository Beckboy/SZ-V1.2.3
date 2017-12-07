package com.junhsue.ksee;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.junhsue.ksee.adapter.CourseSubjectAdapter;
import com.junhsue.ksee.dto.CourseDTO;
import com.junhsue.ksee.entity.Course;
import com.junhsue.ksee.net.api.ICourse;
import com.junhsue.ksee.net.api.OKHttpCourseImpl;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.view.ActionBar;
import com.junhsue.ksee.view.CommonListView;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 线下活动
 * Created by Sugar on 17/8/12.
 */

public class OfflineCourseActivity extends BaseActivity implements View.OnClickListener {
    private Context mContext;
    private ActionBar actionBar;
    private PtrClassicFrameLayout frameLayout;
    private CommonListView mCourseListView;
    private CourseSubjectAdapter mCourseSubjectApdater;
    private int mPageIndex;
    private String mLimit = "10";

    @Override
    protected void onReceiveArguments(Bundle bundle) {

    }

    @Override
    protected int setLayoutId() {
        mContext = this;
        return R.layout.act_offline_course;
    }

    @Override
    protected void onInitilizeView() {
        actionBar = (ActionBar) findViewById(R.id.ab_offline_course_title);
        frameLayout = (PtrClassicFrameLayout) findViewById(R.id.ptr_FrameLayout);
        mCourseListView = (CommonListView) findViewById(R.id.elv_offline_courses);

        mCourseSubjectApdater = new CourseSubjectAdapter(mContext);
        mCourseListView.setAdapter(mCourseSubjectApdater);


        actionBar.setOnClickListener(this);
        frameLayout.setPtrHandler(ptrDefaultHandler2);

        mCourseListView.setOnItemClickListener(courseItemClickListener);

        getCourse();

    }


    /**
     * 线下活动 列表点击事件
     */
    AdapterView.OnItemClickListener courseItemClickListener=new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Course course= (Course) mCourseSubjectApdater.getList().get(position);

            Bundle bundle=new Bundle();
            bundle.putString(CourseDetailsActivity.PARAMS_COURSE_ID,course.id);
            bundle.putInt(CourseDetailsActivity.PARAMS_BUSINESS_ID,course.business_id);
            bundle.putString(CourseDetailsActivity.PARAMS_COURSE_TITLE,course.title);
            launchScreen(CourseDetailsActivity.class,bundle);

        }
    };


    /**
     * 获取课程
     */
    private void getCourse() {

        String business_id = String.valueOf(ICourse.ICourseType.COURSE_TYPE_SUJECT);
        String pageIndex = String.valueOf(mPageIndex);
        OKHttpCourseImpl.getInstance().getCourseList(business_id, mLimit, pageIndex, new RequestCallback<CourseDTO>() {
            @Override
            public void onError(int errorCode, String errorMsg) {
                frameLayout.refreshComplete();
            }

            @Override
            public void onSuccess(CourseDTO response) {
                frameLayout.refreshComplete();
                if(null==response || response.list.size()==0) return;
                if(mPageIndex==0) mCourseSubjectApdater.cleanList();
                mPageIndex++;
                mCourseSubjectApdater.modifyList(response.list);
                if (response.list.size()>=10) {
                    if (frameLayout.getMode() != PtrFrameLayout.Mode.BOTH) {
                        frameLayout.setMode(PtrFrameLayout.Mode.BOTH);
                    }
                }
            }
        });
    }



    PtrDefaultHandler2 ptrDefaultHandler2 = new PtrDefaultHandler2() {

        @Override
        public void onLoadMoreBegin(PtrFrameLayout frame) {
            getCourse();
        }

        @Override
        public void onRefreshBegin(PtrFrameLayout frame) {
            mPageIndex = 0;
            getCourse();
        }

    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_left_layout:
                finish();
                break;
        }
    }
}
