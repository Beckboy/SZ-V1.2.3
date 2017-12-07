package com.junhsue.ksee;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.entity.AnswerEntity;
import com.junhsue.ksee.entity.ResultEntity;
import com.junhsue.ksee.file.ImageLoaderOptions;
import com.junhsue.ksee.net.api.OkHttpSocialCircleImpl;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.utils.DateUtils;
import com.junhsue.ksee.utils.StatisticsUtil;
import com.junhsue.ksee.view.ActionBar;
import com.junhsue.ksee.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 回答详情页
 * Created by Sugar on 17/3/28 in Junhsue.
 */

public class AnswerDetailActivity extends BaseActivity implements View.OnClickListener {

    
    private TextView tvQuestionTitle;
    private CircleImageView civAvatar;
    private TextView tvNickname;
    private TextView tvFromD;
    private TextView tvApprovalCount;
    private TextView tvPublishTime;
    private TextView tvAnswerContent;
    private ActionBar actionBar;
    private Context mContext;
    private AnswerEntity answerEntity;

    @Override
    protected void onReceiveArguments(Bundle bundle) {

        answerEntity = (AnswerEntity) bundle.getSerializable(Constants.ANSWER);
//        Log.e("==", "AnswerEntity===id:" + answerEntity.id + "===business_id:" + answerEntity.business_id);

    }

    @Override
    protected int setLayoutId() {
        mContext = this;
        return R.layout.act_answer_detail;
    }

    @Override
    protected void onInitilizeView() {

        actionBar = (ActionBar) findViewById(R.id.ab_answer_detail_title);
        tvQuestionTitle = (TextView) findViewById(R.id.tv_question_title);
        civAvatar = (CircleImageView) findViewById(R.id.civ_avatar);
        tvNickname = (TextView) findViewById(R.id.tv_nickname);
        tvFromD = (TextView) findViewById(R.id.tv_from_d);
        tvAnswerContent = (TextView) findViewById(R.id.tv_answer_content);
        tvApprovalCount = (TextView) findViewById(R.id.tv_approval_count);
        tvPublishTime = (TextView) findViewById(R.id.tv_publish_time);

        refreshLayoutGetIntent();

        actionBar.setOnClickListener(this);

    }

    @Override
    protected void onResume() {

        StatisticsUtil.getInstance(mContext).onCountPage("1.3.3.1");//自定义页面统计
        super.onResume();
    }


    private void refreshLayoutGetIntent() {
        if (answerEntity == null) {
            return;
        }
        tvQuestionTitle.setText(answerEntity.questionTitleOfAnswer);
        ImageLoader.getInstance().displayImage(answerEntity.avatar, civAvatar,
                ImageLoaderOptions.option(R.drawable.icon_head_default_96px));
        tvNickname.setText(answerEntity.nickname);
        tvFromD.setText(answerEntity.organization);
        tvAnswerContent.setText(answerEntity.content);

        tvApprovalCount.setText("赞同 " + answerEntity.approval_count);

        tvPublishTime.setText(DateUtils.fromTheCurrentTime(
                answerEntity.publish_time, System.currentTimeMillis()));

        if (answerEntity.is_approval) {
            actionBar.setRightImgeOne(R.drawable.icon_agree_press);
        } else {
            actionBar.setRightImgeOne(R.drawable.icon_agree_normal);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_left_layout:
                finishByResult();
                break;
            case R.id.btn_right_one://点赞

                if (answerEntity.is_approval) {
                    senderDeleteApprovalToServer(answerEntity.id, answerEntity.business_id);
                } else {
                    senderApprovalToServer(answerEntity.id, answerEntity.business_id);
                }

                StatisticsUtil.getInstance(mContext).onCountActionDot("3.6.1");//自定义埋点统计
                break;
        }

    }

    /**
     * 带结果值返回
     */
    private void finishByResult() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.ANSWER, answerEntity);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }


    /**
     * @param content_id
     * @param business_id
     */
    public void senderApprovalToServer(String content_id, String business_id) {
        OkHttpSocialCircleImpl.getInstance().senderApproval(content_id, business_id, new RequestCallback<ResultEntity>() {

            @Override
            public void onError(int errorCode, String errorMsg) {

            }

            @Override
            public void onSuccess(ResultEntity response) {
                answerEntity.is_approval = true;
                answerEntity.approval_count = answerEntity.approval_count + 1;
                actionBar.setRightImgeOne(R.drawable.icon_agree_press);
                tvApprovalCount.setText("赞同 " + answerEntity.approval_count);
                Toast.makeText(mContext, "点赞成功", Toast.LENGTH_SHORT).show();

            }
        });

    }


    /**
     * @param content_id
     * @param business_id
     */
    public void senderDeleteApprovalToServer(String content_id, String business_id) {
        OkHttpSocialCircleImpl.getInstance().senderDeleteApproval(content_id, business_id, new RequestCallback<ResultEntity>() {

            @Override
            public void onError(int errorCode, String errorMsg) {

            }

            @Override
            public void onSuccess(ResultEntity response) {
                answerEntity.is_approval = false;
                answerEntity.approval_count = answerEntity.approval_count - 1;
                actionBar.setRightImgeOne(R.drawable.icon_agree_normal);
                tvApprovalCount.setText("赞同 " + answerEntity.approval_count);
                Toast.makeText(mContext, "取消点赞成功", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
