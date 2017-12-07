package com.junhsue.ksee;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.entity.ActivityEntity;
import com.junhsue.ksee.entity.AnswerEntity;
import com.junhsue.ksee.entity.QuestionEntity;
import com.junhsue.ksee.entity.ResultEntity;
import com.junhsue.ksee.net.api.OKHttpCourseImpl;
import com.junhsue.ksee.net.api.OkHttpSocialCircleImpl;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.utils.InputUtil;
import com.junhsue.ksee.utils.StatisticsUtil;
import com.junhsue.ksee.utils.StringUtils;
import com.junhsue.ksee.utils.ToastUtil;
import com.junhsue.ksee.view.ActionBar;

/**
 * 问答回答编辑页面
 * Created by Sugar on 17/4/6 in Junhsue.
 */

public class CommentEditActivity extends BaseActivity implements View.OnClickListener {

    private Context mContext;
    private ActionBar actionBar;
    private EditText etContent;
    private String questionId;//问题ID
    private ActivityEntity activityEntity;//活动
    private TextView tvMsgRemind;
    private String type;
    private String msg;

    @Override
    protected void onReceiveArguments(Bundle bundle) {
        getIntentByLast(bundle);

    }

    private void getIntentByLast(Bundle bundle) {
        type = bundle.getString(Constants.COMMON_EDIT_TYPE);
        if (type == null) {
            return;
        }
        if (type.equals(Constants.ANSWER)) {
            questionId = bundle.getString(Constants.QUESTION_ID);
        } else if (type.equals(Constants.ACTIVITY)) {
            activityEntity = (ActivityEntity) bundle.getSerializable(Constants.ACTIVITY);
        }


    }

    @Override
    protected int setLayoutId() {
        mContext = this;
        return R.layout.act_answer_edit;
    }

    @Override
    protected void onInitilizeView() {
        actionBar = (ActionBar) findViewById(R.id.ab_answer_edit_title);
        etContent = (EditText) findViewById(R.id.et_comment_content);

        tvMsgRemind = (TextView) findViewById(R.id.tv_comment_edit_remind);

        actionBar.setBottomDividerVisible(View.GONE);

        if (type.equals(Constants.ANSWER)) {
            tvMsgRemind.setText(mContext.getString(R.string.msg_answer_reply));
        } else if (type.equals(Constants.ACTIVITY)) {
            tvMsgRemind.setText(mContext.getString(R.string.answer_edit_hint));
        }
        setListener();

    }


    @Override
    protected void onResume() {
        StatisticsUtil.getInstance(mContext).onCountPage("1.3.3.2");//自定义页面统计
        super.onResume();
    }

    private void setListener() {
        actionBar.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_left_layout:
                finish();
                break;
            case R.id.tv_btn_right:
                alertLoadingProgress(true);
                if (type.equals(Constants.ANSWER)) {//文字回答
                    senderAnswerToServer(questionId, etContent.getText().toString().trim(), AnswerEntity.TXT_REPLY_TYPE_VALUE);
                } else if (type.equals(Constants.ACTIVITY)) {
                    senderCommentToServer(activityEntity);
                }

                break;

        }

    }


    /**
     * 发送回答
     *
     * @param questionId
     * @param content
     * @param type
     */
    public void senderAnswerToServer(final String questionId, String content, int type) {

        if (StringUtils.isBlank(content)) {
            Toast.makeText(mContext, "回答未编辑哦", Toast.LENGTH_SHORT).show();
            dismissLoadingDialog();
            return;
        }

        msg = null;

        OkHttpSocialCircleImpl.getInstance().loadAnswerReplay(questionId, content, type, "0", new RequestCallback<ResultEntity>() {

            @Override
            public void onError(int errorCode, String errorMsg) {
                ToastUtil.getInstance(mContext).showToast(mContext, "发送失败");
                dismissLoadingDialog();
            }

            @Override
            public void onSuccess(ResultEntity response) {
                ToastUtil.getInstance(mContext).setHint();
                msg = "发送成功";
                ToastUtil.getInstance(mContext).setContent(msg).setShow();
                //
                QuestionEntity questionEntity = new QuestionEntity();
                questionEntity.id = questionId;


                dismissLoadingDialog();
                finish();
            }
        });

    }


    public void senderCommentToServer(ActivityEntity activityEntity) {

        if (StringUtils.isBlank(etContent.getText().toString().trim())) {
            Toast.makeText(mContext, "回答未编辑哦", Toast.LENGTH_SHORT).show();
            dismissLoadingDialog();
            return;
        }

        OKHttpCourseImpl.getInstance().createComment(activityEntity.id, activityEntity.business_id + "", etContent.getText().toString().trim(), new RequestCallback<ResultEntity>() {

            @Override
            public void onError(int errorCode, String errorMsg) {
                Toast.makeText(mContext, "发送失败", Toast.LENGTH_SHORT).show();
                dismissLoadingDialog();
            }

            @Override
            public void onSuccess(ResultEntity response) {
                Toast.makeText(mContext, "发送成功", Toast.LENGTH_SHORT).show();
                dismissLoadingDialog();
                finish();
            }
        });


    }
}
