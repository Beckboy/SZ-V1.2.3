package com.junhsue.ksee;

import android.os.Bundle;
import android.view.View;

import com.junhsue.ksee.fragment.ColleageCourseFragment;

/**
 * 课程
 * Created by longer on 17/6/16.
 */

public class ColleageCourseActivity extends BaseActivity implements View.OnClickListener{


    @Override
    protected void onReceiveArguments(Bundle bundle) {

    }

    @Override
    protected int setLayoutId() {

        return R.layout.act_msg_colleage_course;
    }

    @Override
    protected void onInitilizeView() {

        findViewById(R.id.action_bar).setOnClickListener(this);

        ColleageCourseFragment colleageCourseFragment= ColleageCourseFragment.newInstance();
        startFragment(colleageCourseFragment,R.id.fl_container_courses,false);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.iv_first_btn_left:
            case R.id.tv_second_btn_left:
                finish();
                break;

        }
    }
}
