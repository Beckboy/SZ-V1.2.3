package com.junhsue.ksee.view;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.junhsue.ksee.CommentEditActivity;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.entity.ActivityEntity;

/**
 * 活动悬浮按钮
 * Created by Sugar on 17/4/27.
 */

public class CommentFloatSuspendView extends BaseSuspendView {
    private Activity activity;
    private ActivityEntity entity;

    public CommentFloatSuspendView(Activity activity) {
        this.activity = activity;
        setIsCanMove(true);
        setShow(false);
        createSuspendView(activity);
    }

    @Override
    public void suspendViewOnClick() {
        Intent intent = new Intent(activity, CommentEditActivity.class);
        intent.putExtra(Constants.COMMON_EDIT_TYPE, Constants.ACTIVITY);
        intent.putExtra(Constants.ACTIVITY, entity);
        activity.startActivity(intent);

    }

    @Override
    public View suspendView() {
        return null;
    }

    @Override
    public int[] suspendPosition() {

        return new int[0];
    }


    public void putIntent(ActivityEntity entity) {//待优化

        this.entity = entity;

    }
}
