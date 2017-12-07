package com.junhsue.ksee;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.junhsue.ksee.entity.ResultEntity;
import com.junhsue.ksee.net.api.OKHttpCourseImpl;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.utils.InputUtil;
import com.junhsue.ksee.utils.ToastUtil;

/**
 *
 * 活动评论页
 * Created by longer on 17/5/4.
 */

public class ActivityCommentActivity extends BaseActivity implements View.OnClickListener{



    //内容id
    private String mContentId;
    //业务id
    private String mBusinessId;

    //评论内容
    private EditText mTxtContent;


    @Override
    protected void onReceiveArguments(Bundle bundle) {
        mContentId=bundle.getString(ActivityDetailActivity.PARAMS_ACTIVITY_ID,"");
        mBusinessId=bundle.getString(ActivityDetailActivity.PARAMS_BUSINESS_ID,"");
    }


    @Override
    protected int setLayoutId() {
        return R.layout.act_activity_comment;
    }


    @Override
    protected void onInitilizeView() {

        mTxtContent=(EditText)findViewById(R.id.txt_contenet);
        //
        findViewById(R.id.txt_cancel).setOnClickListener(this);
        findViewById(R.id.txt_confirm).setOnClickListener(this);
    }


    /**
     * 发表评论*/
    private void  createComment(String content){

        OKHttpCourseImpl.getInstance().createComment(mContentId, mBusinessId, content,
                new RequestCallback<ResultEntity>() {

            @Override
            public void onError(int errorCode, String errorMsg) {
                dismissLoadingDialog();
                ToastUtil.getInstance(getApplicationContext()).setContent(getString(R.string.net_error_service_not_accessables)).setShow();
            }

            @Override
            public void onSuccess(ResultEntity response) {
                dismissLoadingDialog();
                ToastUtil.getInstance(getApplicationContext()).setContent(getString(R.string.msg_activity_comment_push_success)).setShow();
                //发表成功
                Intent intent=new Intent();
                intent.putExtra(ActivityDetailActivity.PARAMS_ACTIVITY_ID,mContentId);
                setResult(ActivityDetailActivity.RESULT_CODE_COMMENT_UPDATE);
                finish();

            }
        });
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.txt_cancel:
                    finish();
                break;
            case R.id.txt_confirm:

                 String  content=mTxtContent.getText().toString().trim();
                 if(TextUtils.isEmpty(content)){
                     ToastUtil.getInstance(getApplicationContext()).setContent(getString(R.string.msg_activity_conmment_empty)).setShow();
                 }else{
                     alertLoadingProgress();
                     InputUtil.hideSoftInputFromWindow(this);
                     createComment(content);
                 }

                break;
        }
    }
}
