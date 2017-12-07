package com.junhsue.ksee;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.junhsue.ksee.adapter.ActivityCommentAdapter;
import com.junhsue.ksee.dto.ActivityCommentDTO;
import com.junhsue.ksee.entity.ActivityEntity;
import com.junhsue.ksee.entity.GoodsInfo;
import com.junhsue.ksee.entity.UserInfo;
import com.junhsue.ksee.file.FileUtil;
import com.junhsue.ksee.fragment.ColleageActivitiesFragment;
import com.junhsue.ksee.net.api.OKHttpCourseImpl;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.url.WebViewUrl;
import com.junhsue.ksee.profile.UserProfileService;
import com.junhsue.ksee.utils.ShareUtils;
import com.junhsue.ksee.view.*;

import com.junhsue.ksee.view.ActionBar;
import com.junhsue.ksee.view.CommonListView;
import com.umeng.analytics.social.UMPlatformData;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;


/**
 * 活动详情
 * Created by Sugar on 17/4/25.
 */

public class ActivityDetailActivity extends BaseActivity implements View.OnClickListener {


    public final static String PARAMS_ACTIVITY_ENTITY = "params_activity_entity";
    //活动id
    public final static String PARAMS_ACTIVITY_ID = "params_activity_id";
    //业务id
    public final static String PARAMS_BUSINESS_ID = "params_business_id";
    //活动返回评论更新
    public final static int RESULT_CODE_COMMENT_UPDATE = 2;

    private Context mContext;
    private ActionBar actionBar;
    private WebPageView mWebPageView;
    private RelativeLayout rlApproval;
    private ImageView ivApproval;
    private TextView tvApprovaleCount;
    private TextView tvSignUp;
    private ActivityEntity activityEntity;
    private MyScrollView svContent;
    //private CommentFloatSuspendView commentFloatSuspendView;


    // private ScrollView svContent;
    //活动集合
    private CommonListView mListViewConmment;
    //
    private ActivityCommentAdapter mCommentAdapter;
    private PtrClassicFrameLayout mPtrClassicFrameLayout;
    private UserInfo mUserInfo;

    //页数
    private int mPageInde = 0;

    //分享弹框
    private ActionSheet shareActionSheetDialog;
    //活动id
    private String mActivityId = "";


    @Override
    protected void onReceiveArguments(Bundle bundle) {
        //activityEntity = (ActivityEntity) bundle.getSerializable(Constants.ACTIVITY);
        //
        mActivityId = bundle.getString(PARAMS_ACTIVITY_ID, "");
    }


    @Override
    protected int setLayoutId() {
        mContext = this;
        return R.layout.act_activity_detail;
    }


    @Override
    protected void onInitilizeView() {

        mUserInfo = UserProfileService.getInstance(this).getCurrentLoginedUser();
        actionBar = (ActionBar) findViewById(R.id.ab_activity_title);
        svContent = (MyScrollView) findViewById(R.id.sv_content);
        mWebPageView = (WebPageView) findViewById(R.id.wv_activity_content);
        rlApproval = (RelativeLayout) findViewById(R.id.rl_approval);
        ivApproval = (ImageView) findViewById(R.id.iv_approval);
        tvApprovaleCount = (TextView) findViewById(R.id.tv_approval_count);
        tvSignUp = (TextView) findViewById(R.id.tv_sign_up);

        //commentFloatSuspendView = new CommentFloatSuspendView(this);
        //commentFloatSuspendView.setImageView(R.drawable.selector_comment);
        //showFloatButton();
        //commentFloatSuspendView.putIntent(activityEntity);

        setListener();

        mListViewConmment = (CommonListView) findViewById(R.id.lv_comment);
        mPtrClassicFrameLayout = (PtrClassicFrameLayout) findViewById(R.id.ptrClassicFrameLayout);
        mPtrClassicFrameLayout.autoLoadMore();
        //
        mPtrClassicFrameLayout.setPtrHandler(ptrDefaultHandler2);
        mPtrClassicFrameLayout.setMode(PtrFrameLayout.Mode.NONE);

        mCommentAdapter = new ActivityCommentAdapter(this);
        mListViewConmment.setAdapter(mCommentAdapter);
        //
        findViewById(R.id.img_comment).setOnClickListener(this);


        //获取活动详情
        alertLoadingProgress();
        getActivityDetails();
    }


    /**
     * 设置按钮活动状态
     * <p>
     * 1.当前时间大于活动截止的时间,就活动已结束
     * <p>
     * <p>
     * 2.活动截止报名
     */
    private void setBtnStatus() {
        long currentTime = System.currentTimeMillis() / 1000;
        if (currentTime >= activityEntity.end_time) {
            tvSignUp.setText("已结束");
            tvSignUp.setClickable(false);
        } else if (currentTime > activityEntity.signup_deadline) {
            tvSignUp.setText("已截止");
            tvSignUp.setClickable(false);
        } else {
            tvSignUp.setClickable(true);
        }
    }


    PtrDefaultHandler2 ptrDefaultHandler2 = new PtrDefaultHandler2() {

        @Override
        public void onLoadMoreBegin(PtrFrameLayout frame) {
            getCommentList();

        }

        @Override
        public void onRefreshBegin(PtrFrameLayout frame) {
            mPageInde = 0;
            mPtrClassicFrameLayout.refreshComplete();
        }
    };


    private void initLayout() {
        if (activityEntity == null) {
            return;
        }

        if (mUserInfo == null) {
            return;
        }
        //
        String url = String.format(WebViewUrl.H5_ACTIVITY_URL, activityEntity.id, mUserInfo.token);
        mWebPageView.loadUrl(url);

    }

    /**
     * 获取评论列表
     */
    private void getCommentList() {

        String pageIndex = String.valueOf(mPageInde);
        String pageSize = "10";
        String businessId = String.valueOf(activityEntity.business_id);
        String content_id = String.valueOf(activityEntity.id);
        //
        OKHttpCourseImpl.getInstance().getCommentList(pageIndex, pageSize, businessId, content_id,
                new RequestCallback<ActivityCommentDTO>() {

            @Override
            public void onError(int errorCode, String errorMsg) {
                mPtrClassicFrameLayout.refreshComplete();
                //dismissLoadingDialog();
            }

            @Override
            public void onSuccess(ActivityCommentDTO response) {
                if (null == response) return;
                if (mPageInde == 0) {
                    mCommentAdapter.cleanList();
                }
                mCommentAdapter.modifyList(response.result);
                mPtrClassicFrameLayout.refreshComplete();
                if (response.totalsize >= 10) {
                    mPtrClassicFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
                }
                mPageInde++;
                //dismissLoadingDialog();
            }
        });
    }


    private void getActivityDetails() {

        OKHttpCourseImpl.getInstance().getActivitiesDetails(mActivityId, new RequestCallback<ActivityEntity>() {

            @Override
            public void onError(int errorCode, String errorMsg) {

                dismissLoadingDialog();
            }

            @Override
            public void onSuccess(ActivityEntity response) {
                dismissLoadingDialog();

                activityEntity = response;
                setActivityAttribute();
                initLayout();
                setBtnStatus();
            }
        });
    }

    /**
     * 设置活动属性
     */
    private void setActivityAttribute() {

        if (null == activityEntity)
            return;

        tvApprovaleCount.setText(String.format(getString(R.string.msg_approval_count),
                "" + activityEntity.approvalcount));

        if (activityEntity.is_approval) {
            ivApproval.setImageResource(R.drawable.icon_like_hover);
        } else {
            ivApproval.setImageResource(R.drawable.icon_like_normal);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_CODE_COMMENT_UPDATE == resultCode) {
            //更新用户评论
            mPageInde = 0;
            getCommentList();
        }
    }


    private void setListener() {
        actionBar.setOnClickListener(this);
        rlApproval.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll_left_layout:
                sendBroadCast();
                finish();
                break;
            case R.id.rl_approval:
                if (null == activityEntity) return;

                if (activityEntity.is_approval) {
                    activityEntity.approvalcount--;
                    activityEntity.is_approval = false;
                } else {
                    activityEntity.approvalcount++;
                    activityEntity.is_approval = true;
                }
                setActivityAttribute();

                break;
            case R.id.tv_sign_up:

                if (null == activityEntity) return;

                GoodsInfo goodsInfo = GoodsInfo.cloneOject(activityEntity.poster, activityEntity.id, activityEntity.title,
                        activityEntity.price,
                        GoodsInfo.GoodsType.getType(activityEntity.business_id));
                Bundle bd = new Bundle();
                bd.putSerializable(ConfirmOrderActivity.PARAMS_GOODS_INFO, goodsInfo);
                launchScreen(ConfirmOrderActivity.class, bd);

                break;


            case R.id.img_comment:

                if (null == activityEntity) return;

                Bundle bundle = new Bundle();
                bundle.putString(PARAMS_ACTIVITY_ID, activityEntity.id);
                bundle.putString(PARAMS_BUSINESS_ID, String.valueOf(activityEntity.business_id));
                //launchScreen(ActivityCommentActivity.class, bundle);
                Intent intent = new Intent(this, ActivityCommentActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, 0x101);
                break;

            case R.id.btn_right_one://分享
                showShareActionSheetDailog();
                break;
        }


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sendBroadCast();
    }


    private void sendBroadCast() {
//        Intent intent = new Intent();
//        intent.putExtra(PARAMS_ACTIVITY_ENTITY, activityEntity);
//        intent.setAction(ColleageActivitiesFragment.BROAD_ACTION_APPROVAL_STATUS_UPDATE);
//        sendBroadcast(intent);
    }


    public class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            getCommentList();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        //closeFloatButton();
        if (shareActionSheetDialog != null) {
            shareActionSheetDialog.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //showFloatButton();
    }

//    public void closeFloatButton() {
//        if (commentFloatSuspendView != null && commentFloatSuspendView.isCreatedSuspendView()) {
//            commentFloatSuspendView.dismissSuspendView();
//        }
//    }

//    public void showFloatButton() {
//
//        if (commentFloatSuspendView != null && commentFloatSuspendView.isCreatedSuspendView()) {
//            commentFloatSuspendView.showSuspendView();
//        }
//    }

    /**
     * 分享弹出框
     */
    private void showShareActionSheetDailog() {
        final String path = FileUtil.getImageFolder() + "/" + String.valueOf(activityEntity.poster.hashCode());//如果分享图片获取该图片的本地存储地址
        final String webPage = String.format(WebViewUrl.H5_SHARE_ACTIVITY, activityEntity.id);
        final String title = activityEntity.title;
        final String desc = activityEntity.description;

        shareActionSheetDialog = new ActionSheet(this);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.component_share_dailog, null);
        shareActionSheetDialog.setContentView(view);
        shareActionSheetDialog.show();


        LinearLayout llShareFriend = (LinearLayout) view.findViewById(R.id.ll_share_friend);
        LinearLayout llShareCircle = (LinearLayout) view.findViewById(R.id.ll_share_circle);
        TextView cancelButton = (TextView) view.findViewById(R.id.tv_cancel);

        llShareFriend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ShareUtils.getInstance(mContext).sharePage(ShareUtils.SendToPlatformType.TO_FRIEND, webPage, path,
                        title, desc, UMPlatformData.UMedia.WEIXIN_FRIENDS);

                if (shareActionSheetDialog != null) {
                    shareActionSheetDialog.dismiss();
                }

            }
        });

        llShareCircle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ShareUtils.getInstance(mContext).sharePage(ShareUtils.SendToPlatformType.TO_CIRCLE, webPage, path,
                        title, desc, UMPlatformData.UMedia.WEIXIN_CIRCLE);

                if (shareActionSheetDialog != null) {
                    shareActionSheetDialog.dismiss();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shareActionSheetDialog.isShowing()) {
                    shareActionSheetDialog.dismiss();
                }
            }
        });


    }

}
