package com.junhsue.ksee.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.junhsue.ksee.ArticleDetailActivity;
import com.junhsue.ksee.CourseDetailsActivity;
import com.junhsue.ksee.CourseSystemDetailsActivity;
import com.junhsue.ksee.LiveDetailsActivity;
import com.junhsue.ksee.PostDetailActivity;
import com.junhsue.ksee.QuestionDetailActivity;
import com.junhsue.ksee.VideoDetailActivity;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.common.IBusinessType;

/**
 * Created by Sugar on 17/3/28.
 */

public class StartActivityUtils {


    /**
     * 跳转到问答详情页
     *
     * @param questionId 问答id
     */
    public static void startQuestionDetailActivity(Context mContext, String questionId) {

        if (StringUtils.isBlank(questionId)) {
            return;
        }
        Intent intent = new Intent(mContext, QuestionDetailActivity.class);
        intent.putExtra(Constants.QUESTION_ID, questionId);
        mContext.startActivity(intent);

    }


    /**
     * 首页Banner的跳转
     */
    public static void startHomeBannerActivity(Context mContext, int business_id, String content_id) {
        Intent jump2activity = null;
        Bundle bundle = new Bundle();
        switch (business_id) {

            //挑战到帖子详情
            case IBusinessType.POST_DETAILS:
                jump2activity = new Intent(mContext, PostDetailActivity.class);
                bundle.putString(Constants.POST_DETAIL_ID, content_id);
                break;
            //问题
            case IBusinessType.QUESTION:
                jump2activity = new Intent(mContext, QuestionDetailActivity.class);
                bundle.putString(Constants.QUESTION_ID, content_id);
                break;
            //直播
            case IBusinessType.COURSE_LIVE:
                jump2activity = new Intent(mContext, LiveDetailsActivity.class);
                bundle.putString(LiveDetailsActivity.PARAMS_LIVE_ID, content_id);
                break;
            //系统课
            case IBusinessType.COURSE_SYSTEM:
                jump2activity = new Intent(mContext, CourseSystemDetailsActivity.class);
                bundle.putInt(CourseSystemDetailsActivity.PARAMS_SYSTEM_BUSINESS_ID, business_id);
                bundle.putString(CourseSystemDetailsActivity.PARAMS_SYSTEM_COURSE_ID, content_id);
                bundle.putString(CourseSystemDetailsActivity.PARAMS_SYSTEM_TITLE, "");
                break;
            //主题课
            case IBusinessType.COURSE_SUJECT:
                jump2activity = new Intent(mContext, CourseDetailsActivity.class);
                bundle.putInt(CourseDetailsActivity.PARAMS_BUSINESS_ID, business_id);
                bundle.putString(CourseDetailsActivity.PARAMS_COURSE_ID, content_id);
                bundle.putString(CourseDetailsActivity.PARAMS_COURSE_TITLE, "");
                break;
            //干货
            case IBusinessType.REALIZE_ARTICLE:
                jump2activity = new Intent(mContext, ArticleDetailActivity.class);
                bundle.putString(ArticleDetailActivity.PARAMS_ARTICLE_ID, content_id);
                break;
            //视频
            case IBusinessType.COLLEAGE_VEDIO:
                jump2activity = new Intent(mContext, VideoDetailActivity.class);
                bundle.putString(Constants.VIDEO_ID, content_id);
                bundle.putString(Constants.VIDEO_TITLE, "");
                break;

            default:
                break;
        }
        if (null != jump2activity) {
            jump2activity.putExtras(bundle);
            mContext.startActivity(jump2activity);
        }

    }

}
