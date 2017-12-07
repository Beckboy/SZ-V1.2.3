package com.junhsue.ksee;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.junhsue.ksee.adapter.PostCommentListAdapter;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.dto.PostSecondDTO;
import com.junhsue.ksee.entity.MsgInfoEntity;
import com.junhsue.ksee.entity.PostCommentEntity;
import com.junhsue.ksee.entity.ResultEntity;
import com.junhsue.ksee.file.ImageLoaderOptions;
import com.junhsue.ksee.fragment.dialog.CircleCommonDialog;
import com.junhsue.ksee.net.api.OKHttpNewSocialCircle;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.utils.DateUtils;
import com.junhsue.ksee.utils.InputUtil;
import com.junhsue.ksee.utils.ScreenWindowUtil;
import com.junhsue.ksee.utils.StringUtils;
import com.junhsue.ksee.view.ActionBar;
import com.junhsue.ksee.view.CancelEditText;
import com.junhsue.ksee.view.CircleImageView;
import com.junhsue.ksee.view.CommonListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;


/**
 * 评论列表页
 * Created by Sugar on 17/10/23.
 */

public class PostCommentListActivity extends BaseActivity implements View.OnClickListener {

    private Context mContext;
    private ActionBar actionBar;
    private CircleImageView commentAvatar;
    private TextView tvCommentNickname;
    private TextView tvCommentPublishTime;
    private PostCommentListAdapter postCommentListAdapter;
    private CommonListView lvSecondComment;
    private PtrClassicFrameLayout ptrClassicFrameLayout;
    private String postCommentId;
    private CancelEditText etSendComment;
    private ImageView ivCommentApproval;
    private TextView tvCommentApprovalAccount;
    private LinearLayout llCommentListApprovalLayout;
    private TextView tvCommentSubmit;
    private LinearLayout llBottomEditLayout;
    private TextView tvPostCommentDetailContent;
    private PostCommentEntity postCommentEntity;
    private int currentPage;
    private int pageSize = 15;
    private boolean isMaxPage;

    @Override
    protected void onReceiveArguments(Bundle bundle) {
        postCommentId = bundle.getString("post_comment_id");

    }

    @Override
    protected int setLayoutId() {
        mContext = this;
        return R.layout.act_post_comment_list;
    }

    @Override
    protected void onInitilizeView() {

        actionBar = (ActionBar) findViewById(R.id.ab_post_comment_detail_title);
        commentAvatar = (CircleImageView) findViewById(R.id.civ_post_comment_user_avatar);
        tvCommentNickname = (TextView) findViewById(R.id.tv_post_comment_user_nickname);
        tvCommentPublishTime = (TextView) findViewById(R.id.tv_post_comment_publish_time);
        lvSecondComment = (CommonListView) findViewById(R.id.lv_second_comment);
        ptrClassicFrameLayout = (PtrClassicFrameLayout) findViewById(R.id.ptr_pcf_comment_layout);
        etSendComment = (CancelEditText) findViewById(R.id.et_send_comment);
        llCommentListApprovalLayout = (LinearLayout) findViewById(R.id.ll_post_comment_list_approval_layout);
        ivCommentApproval = (ImageView) findViewById(R.id.iv_post_comment_list_approval);
        tvCommentApprovalAccount = (TextView) findViewById(R.id.tv_comment_list_approval_account);
        tvCommentSubmit = (TextView) findViewById(R.id.tv_comment_list_submit);
        llBottomEditLayout = (LinearLayout) findViewById(R.id.ll_bottom_edit_layout);
        tvPostCommentDetailContent = (TextView) findViewById(R.id.tv_post_comment_detail_content);

        actionBar.setBottomDividerVisible(View.GONE);
        postCommentEntity = new PostCommentEntity();
        postCommentListAdapter = new PostCommentListAdapter(this);
        lvSecondComment.setAdapter(postCommentListAdapter);
        setListener();
        postCommentListAdapter.setMaxSize(0);
        loadComment();
    }

    /**
     * 设置监听
     */
    private void setListener() {

        actionBar.setOnClickListener(this);
        tvCommentSubmit.setOnClickListener(this);
        llCommentListApprovalLayout.setOnClickListener(this);
        ptrClassicFrameLayout.setPtrHandler(ptrDefaultHandler2);
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);
        etSendComment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    InputUtil.softInputFromWiddow(PostCommentListActivity.this);
                } else {
                    InputUtil.hideSoftInputFromWindow(PostCommentListActivity.this);
                    etSendComment.setEditTextHintContent("回复  " + postCommentEntity.nickname + ":");
                }
            }
        });

        etSendComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (StringUtils.isBlank(etSendComment.getEditTextContent())) {
                    tvCommentSubmit.setTextColor(mContext.getResources().getColor(R.color.c_gray_aebdcd));
                } else {
                    tvCommentSubmit.setTextColor(mContext.getResources().getColor(R.color.c_yellow_ffc84a));
                }

            }
        });

    }

    /**
     * 加载评论列表
     */
    private void loadComment() {
        OKHttpNewSocialCircle.getInstance().loadCommentDetail(postCommentId, currentPage, pageSize, new RequestCallback<PostCommentEntity>() {


            @Override
            public void onError(int errorCode, String errorMsg) {

            }

            @Override
            public void onSuccess(PostCommentEntity response) {
//                Trace.e("===user_id" + response.user_id + "id" + postCommentId + "response.id" + response.id + "appproval_count:" + response.approval_count);
                if (response != null) {
                    postCommentEntity = response;

                    refreshCommentListView(postCommentEntity);
                }

            }
        });

    }

    /**
     * 刷新回答列表
     */
    private void refreshCommentListView(PostCommentEntity postCommentEntity) {

        ImageLoader.getInstance().displayImage(postCommentEntity.avatar, commentAvatar, ImageLoaderOptions.option(R.drawable.pic_default_avatar));
        tvCommentNickname.setText(postCommentEntity.nickname);

        tvCommentPublishTime.setText(DateUtils.formatCurrentTime(
                postCommentEntity.create_at * 1000l, System.currentTimeMillis()));

        tvPostCommentDetailContent.setText(postCommentEntity.content);

        if (postCommentEntity.is_approval) {
            ivCommentApproval.setImageResource(R.drawable.icon_post_approval_light);
            tvCommentApprovalAccount.setTextColor(mContext.getResources().getColor(R.color.c_yellow_ffc84a));
        } else {
            ivCommentApproval.setImageResource(R.drawable.icon_post_approval);
            tvCommentApprovalAccount.setTextColor(mContext.getResources().getColor(R.color.c_gray_aebdcd));
        }

        if (postCommentEntity.approval_count > 0) {
            tvCommentApprovalAccount.setText(postCommentEntity.approval_count + "");

        } else {
            tvCommentApprovalAccount.setText("赞");

        }


        if (currentPage == 0) {
            postCommentListAdapter.cleanList();
        }
        postCommentListAdapter.modifyList(postCommentEntity.repply);

        if (postCommentEntity.repply.size() < pageSize) {
            isMaxPage = true;
            postCommentListAdapter.setMaxSize(postCommentListAdapter.getList().size() + 1);
        }
        ptrClassicFrameLayout.refreshComplete();
    }

    /**
     * 刷新监听
     */
    PtrDefaultHandler2 ptrDefaultHandler2 = new PtrDefaultHandler2() {
        @Override
        public void onLoadMoreBegin(PtrFrameLayout frame) {
            if (isMaxPage) {
                Toast.makeText(mContext, getString(R.string.data_load_completed), Toast.LENGTH_SHORT).show();
                ptrClassicFrameLayout.refreshComplete();//加载完毕
            } else {
                currentPage++;
                loadComment();
            }
        }

        @Override
        public void onRefreshBegin(PtrFrameLayout frame) {
            currentPage = 0;
            postCommentListAdapter.setMaxSize(0);
            loadComment();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_left_layout:
                finishByResult();
                break;
            case R.id.ll_post_comment_list_approval_layout:
                if (postCommentEntity.is_approval) {
                    senderPostDetailCancelApproval();
                } else {
                    senderPostDetailApproval();
                }
                break;
            case R.id.tv_comment_list_submit:
                senderCommentReplyMsgToServer(postCommentEntity.id, PostCommentEntity.POST_COMMENT_BUSINESS_ID, etSendComment.getEditTextContent());
                break;
        }
    }

    /**
     * 帖子详情的点赞
     */
    private void senderPostDetailApproval() {
        OKHttpNewSocialCircle.getInstance().postCommentApproval(postCommentEntity.id, PostCommentEntity.POST_COMMENT_BUSINESS_ID, new RequestCallback<ResultEntity>() {

            @Override
            public void onError(int errorCode, String errorMsg) {
                Toast.makeText(mContext, "" + errorMsg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(ResultEntity response) {
                postCommentEntity.is_approval = true;
                ivCommentApproval.setImageResource(R.drawable.icon_post_approval_light);
                tvCommentApprovalAccount.setTextColor(mContext.getResources().getColor(R.color.c_yellow_ffc84a));
                postCommentEntity.approval_count = postCommentEntity.approval_count + 1;
                tvCommentApprovalAccount.setText(postCommentEntity.approval_count + "");
                showDialog("点赞成功", R.drawable.icon_dialog_success);
            }
        });

    }

    /**
     * 帖子的点赞取消
     */
    private void senderPostDetailCancelApproval() {

        OKHttpNewSocialCircle.getInstance().postCommentCancelApproval(postCommentEntity.id, PostCommentEntity.POST_COMMENT_BUSINESS_ID, new RequestCallback<ResultEntity>() {

            @Override
            public void onError(int errorCode, String errorMsg) {
                Toast.makeText(mContext, "" + errorMsg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(ResultEntity response) {
                postCommentEntity.is_approval = false;
                ivCommentApproval.setImageResource(R.drawable.icon_post_approval);
                tvCommentApprovalAccount.setTextColor(mContext.getResources().getColor(R.color.c_gray_aebdcd));
                postCommentEntity.approval_count = postCommentEntity.approval_count - 1;
                tvCommentApprovalAccount.setText(postCommentEntity.approval_count + "");
                showDialog("点赞取消", R.drawable.icon_dialog_success);
            }
        });
    }

    /**
     * 评论回复
     *
     * @param content
     */
    private void senderCommentReplyMsgToServer(String content_id, String business_id, String content) {
        if (StringUtils.isBlank(content)) {
            //TODO 弹窗提示
            showDialog("发送的内容不能为空哦(>_<)~~", R.drawable.icon_dialog_unsucces);
            return;
        }

        OKHttpNewSocialCircle.getInstance().postCommentReplyCreate(content_id, business_id, content, new RequestCallback<PostSecondDTO>() {

            @Override
            public void onError(int errorCode, String errorMsg) {

            }

            @Override
            public void onSuccess(PostSecondDTO response) {
                InputUtil.hideSoftInputFromWindow(PostCommentListActivity.this);
                etSendComment.setEditTextContent("");
                currentPage = 0;
                loadComment();

            }
        });

    }

    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {

        @Override
        public void onGlobalLayout() {
            Rect r = new Rect();
            //获取当前界面可视部分
            getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
            //获取屏幕的高度
            int screenHeight = getWindow().getDecorView().getRootView().getHeight();
            //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
            int vertialHeigt = ScreenWindowUtil.getBottomStatusHeight(getApplicationContext());
            int heightDifference = screenHeight - r.bottom - vertialHeigt;
            if (heightDifference > 0) {
                tvCommentSubmit.setVisibility(View.VISIBLE);
                llCommentListApprovalLayout.setVisibility(View.GONE);
            } else {
                //当软键盘关闭的时候，同时关掉灰色的层
                tvCommentSubmit.setVisibility(View.GONE);
                llCommentListApprovalLayout.setVisibility(View.VISIBLE);
                etSendComment.setEditTextHintContent("回复  " + postCommentEntity.nickname + ":");
            }

        }

    };


    /**
     * 显示关注成功弹窗
     */
    private void showDialog(String msg, int drawableId) {
        MsgInfoEntity entity = new MsgInfoEntity();
        entity.drawableId = drawableId;
        entity.msgInfo = msg;
        CircleCommonDialog circleCommonDialog = CircleCommonDialog.newInstance(entity);
        circleCommonDialog.show(getSupportFragmentManager(), "");
    }

/*    private void refreshKeyboard() {
        if (mIsShowKeyboard) {//显示软键盘
            etSendComment.requestFocus();
            tvCommentSubmit.setVisibility(View.VISIBLE);
            llCommentListApprovalLayout.setVisibility(View.GONE);
        } else {
            //隐藏软键盘
            hideKeybord();

        }
    }

    *//**
     * 隐藏软键盘
     *//*
    private void hideKeybord() {
        llBottomEditLayout.requestFocus();
        llBottomEditLayout.setFocusable(true);
        llBottomEditLayout.setFocusableInTouchMode(true);
        tvCommentSubmit.setVisibility(View.GONE);
        llCommentListApprovalLayout.setVisibility(View.VISIBLE);
//        etSendComment.setEditTextHintContent("我想说 ...");
    }*/

    /**
     * 带结果值返回
     */
    private void finishByResult() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        PostCommentEntity entity = new PostCommentEntity();
        entity.id = postCommentEntity.id;
        entity.approval_count = postCommentEntity.approval_count;
        entity.is_approval = postCommentEntity.is_approval;
        entity.repply = postCommentEntity.repply;
        bundle.putSerializable(Constants.POST_COMMENT_ID, entity);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        //返回前通知上一个页面刷新变动数据
        finishByResult();
        super.onBackPressed();
    }
}
