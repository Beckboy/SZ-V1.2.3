package com.junhsue.ksee;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.junhsue.ksee.adapter.PostCommentAdapter;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.dto.PostCommentDTO;
import com.junhsue.ksee.dto.PostDetailListDTO;
import com.junhsue.ksee.dto.PostSecondDTO;
import com.junhsue.ksee.dto.SendPostResultDTO;
import com.junhsue.ksee.entity.CircleEntity;
import com.junhsue.ksee.entity.CommonResultEntity;
import com.junhsue.ksee.entity.MsgInfoEntity;
import com.junhsue.ksee.entity.PhotoInfo;
import com.junhsue.ksee.entity.PostCommentEntity;
import com.junhsue.ksee.entity.PostDetailEntity;
import com.junhsue.ksee.entity.PostSecondCommentEntity;
import com.junhsue.ksee.entity.ResultEntity;
import com.junhsue.ksee.entity.UserInfo;
import com.junhsue.ksee.file.ImageLoaderOptions;
import com.junhsue.ksee.fragment.CircleDetailAllFragment;
import com.junhsue.ksee.fragment.CommunityCircleFragment;
import com.junhsue.ksee.fragment.CommunityMyCircleFragment;
import com.junhsue.ksee.fragment.KnowledgeFragment;
import com.junhsue.ksee.fragment.dialog.CircleCommonDialog;
import com.junhsue.ksee.net.api.OKHttpNewSocialCircle;
import com.junhsue.ksee.net.api.OkHttpCircleImpI;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.error.NetResultCode;
import com.junhsue.ksee.net.url.WebViewUrl;
import com.junhsue.ksee.profile.UserProfileService;
import com.junhsue.ksee.utils.DateUtils;
import com.junhsue.ksee.utils.GlideCircleTransformUtil;
import com.junhsue.ksee.utils.InputUtil;
import com.junhsue.ksee.utils.ScreenWindowUtil;
import com.junhsue.ksee.utils.ShareUtils;
import com.junhsue.ksee.utils.StringUtils;
import com.junhsue.ksee.view.ActionBar;
import com.junhsue.ksee.view.ActionSheet;
import com.junhsue.ksee.view.CancelEditText;
import com.junhsue.ksee.view.CircleImageView;
import com.junhsue.ksee.view.CommonListView;
import com.junhsue.ksee.view.MultiImageView;
import com.junhsue.ksee.view.RecommendPostItemView;
import com.junhsue.ksee.view.RfCommonDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.social.UMPlatformData;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 帖子详情页
 * Created by Sugar on 17/10/19.
 */

public class PostDetailActivity extends BaseActivity implements View.OnClickListener {

    public static final String SHOW_KEYBOARD_STATUS = "SHOW_KEYBOARD_STATUS";
    //帖子实体
    public static final String POSTS_ENTITY = "posts_entity";
    //入口是否来源于首页
    public static final String PARAMS_IS_FROM_HOME="parmas_isfrom_home";

    private Context mContext;
    private ActionBar actionBar;
    private ActionSheet shareActionSheetDialog;
    private ActionSheet postReportDialog;
    private RelativeLayout rlPostCircleAttention;
    private TextView tvFromCircleName;
    private TextView tvAttention;
    private ImageView ivPostSelectTag;
    private TextView tvPostTitle;
    private CircleImageView civPostUserAvatar;
    private TextView tvPostUserNickname;
    private TextView tvPostPublishTime;
    private TextView tvPostDescription;
    private CommonListView postCommentListView;
    private PostCommentAdapter postCommentAdapter;
    private PtrClassicFrameLayout frameLayout;
    private CancelEditText cetPostComment;
    private LinearLayout llEditLayout;
    private int pageSize = 15;
    private int currentPage = 0;
    private int recommendPageSize = 4;
    private int recommendPage = 0;
    private String postId = "";
    private boolean isMaxPage = false;
    private String senderMsg = "";//发送的消息
    private int senderMsgTag = 1;//1表示帖子回复 2描述评论
    private TextView tvSubmit;
    private LinearLayout llSwitchLayout;
    private LinearLayout llPostFavoriteLayout;
    private ImageView ivPostFavorite;
    private TextView tvPostFavorite;
    private LinearLayout llPostMiddleLayout;
    private LinearLayout llCollectLayout;
    private ImageView ivCollect;
    private TextView tvCollect;
    private TextView tvPostCommentTotalSize;//评论总数
    private PostCommentEntity commentEntity;//评论实体
    private PostDetailEntity postDetailEntity;
    private MultiImageView multiImageView;
    private View vScrollviewHeadDivider;
    private boolean isPostCollect;
    private boolean isPostApproval;
    private String postApprovalNumber;//点赞量
    private RelativeLayout blankPage;
    private long totalsize;//评论数量
    private LinearLayout llNoData;//无数据的占位图
    private LinearLayout llRecommendPostContentLayout;//推荐帖子的总布局
    private LinearLayout llRecommendPostLayout;//推荐帖子布局

    private boolean mIsShowKeyboard;
    //入口是否来源于首页
    private boolean mIsFromHome;

    @Override
    protected void onReceiveArguments(Bundle bundle) {
        mIsShowKeyboard = bundle.getBoolean(SHOW_KEYBOARD_STATUS, false);
        postId = bundle.getString(Constants.POST_DETAIL_ID);
        mIsFromHome=bundle.getBoolean(PARAMS_IS_FROM_HOME,false);
    }

    @Override
    protected int setLayoutId() {
        mContext = this;
        return R.layout.act_post_detail;
    }

    @Override
    protected void onInitilizeView() {

        actionBar = (ActionBar) findViewById(R.id.ab_post_detail_title);
        rlPostCircleAttention = (RelativeLayout) findViewById(R.id.rl_post_circle_attention);
        llPostMiddleLayout = (LinearLayout) findViewById(R.id.ll_post_middle_layout);
        multiImageView = (MultiImageView) findViewById(R.id.mliv_post_multiImagView);
        tvFromCircleName = (TextView) findViewById(R.id.tv_from_circle_name);
        tvAttention = (TextView) findViewById(R.id.tv_attention);
        vScrollviewHeadDivider = findViewById(R.id.v_post_scrollview_head_divider);
        ivPostSelectTag = (ImageView) findViewById(R.id.iv_post_detail_select_tag);
        tvPostTitle = (TextView) findViewById(R.id.tv_post_title);
        civPostUserAvatar = (CircleImageView) findViewById(R.id.civ_post_user_avatar);
        tvPostUserNickname = (TextView) findViewById(R.id.tv_post_user_nickname);
        tvPostPublishTime = (TextView) findViewById(R.id.tv_post_publish_time);
        tvPostDescription = (TextView) findViewById(R.id.tv_post_description);
        frameLayout = (PtrClassicFrameLayout) findViewById(R.id.ptr_pcf_layout);
        postCommentListView = (CommonListView) findViewById(R.id.clv_post_comment_list);
        cetPostComment = (CancelEditText) findViewById(R.id.et_send_post_comment);
        llEditLayout = (LinearLayout) findViewById(R.id.ll_edit_layout);
        llSwitchLayout = (LinearLayout) findViewById(R.id.ll_switch_layout);

        llPostFavoriteLayout = (LinearLayout) findViewById(R.id.ll_post_favorite_layout);
        ivPostFavorite = (ImageView) findViewById(R.id.iv_post_approval);
        tvPostFavorite = (TextView) findViewById(R.id.tv_post_favorite_account);

        //推荐帖子布局
        llRecommendPostContentLayout = (LinearLayout) findViewById(R.id.ll_recommend_post_content_layout);
        llRecommendPostLayout = (LinearLayout) findViewById(R.id.ll_recommend_post);

        tvPostCommentTotalSize = (TextView) findViewById(R.id.tv_post_comment_total_size);
        llCollectLayout = (LinearLayout) findViewById(R.id.ll_bottom_collect);
        ivCollect = (ImageView) findViewById(R.id.iv_post_collect);
        tvCollect = (TextView) findViewById(R.id.tv_post_collect);
        tvSubmit = (TextView) findViewById(R.id.tv_submit);
        llNoData = (LinearLayout) findViewById(R.id.ll_no_data);
        //加载回答列表空白占位图
        blankPage = (RelativeLayout) findViewById(R.id.rl_post_comment_blank_page);

        registerForContextMenu(tvPostDescription);//注册上下文菜单

        initLayout();
        refreshKeyboard();
        setListener();

        loadPostDetail();

    }

    /**
     * 初始化布局
     */
    private void initLayout() {
        commentEntity = new PostCommentEntity();
        postDetailEntity = new PostDetailEntity();
        postCommentAdapter = new PostCommentAdapter(mContext);
        postCommentListView.setAdapter(postCommentAdapter);
        actionBar.setBottomDividerVisible(View.GONE);
        postCommentAdapter.setMaxSize(0);

    }

    /**
     * 事件监听设置
     */
    private void setListener() {
        actionBar.setOnClickListener(this);
        tvAttention.setOnClickListener(this);
        llPostFavoriteLayout.setOnClickListener(this);
        cetPostComment.setOnClickListener(this);
        llCollectLayout.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
        frameLayout.setPtrHandler(ptrDefaultHandler2);
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);
/*        llPostMiddleLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //关闭软键盘
                InputUtil.hideSoftInputFromWindow(PostDetailActivity.this);
                cetPostComment.setEditTextHintContent("我想说...");
                return true;
            }
        });*/

        cetPostComment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    InputUtil.softInputFromWiddow(PostDetailActivity.this);
                } else {
                    InputUtil.hideSoftInputFromWindow(PostDetailActivity.this);
                    cetPostComment.setEditTextHintContent("我想说...");
                }
            }
        });

        cetPostComment.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (StringUtils.isBlank(cetPostComment.getEditTextContent())) {
                    tvSubmit.setTextColor(mContext.getResources().getColor(R.color.c_gray_aebdcd));
                } else {
                    tvSubmit.setTextColor(mContext.getResources().getColor(R.color.c_yellow_ffc84a));
                }
            }
        });


        postCommentAdapter.setPostListener(new PostCommentAdapter.PostListener() {
            @Override
            public void toNext(PostCommentEntity postCommentEntity, View v) {
                Intent intent = new Intent(mContext, PostCommentListActivity.class);
                intent.putExtra("post_comment_id", postCommentEntity.id);
                startActivityForResult(intent, Constants.POST_COMMENT_RESULT);
            }

            @Override
            public void replyOrRemove(PostCommentEntity postCommentEntity, View v) {

                showCommentReplyOrRemove(postCommentEntity);

            }

            @Override
            public void refreshApproval(PostCommentEntity postCommentEntity, View v) {
                if (postCommentEntity.is_approval) {
                    postCommentEntity.is_approval = false;
                    postCommentEntity.approval_count = postCommentEntity.approval_count - 1;
                    senderCancelPostCommentApproval(postCommentEntity);
                } else {
                    postCommentEntity.is_approval = true;
                    postCommentEntity.approval_count = postCommentEntity.approval_count + 1;
                    senderPostCommentApproval(postCommentEntity);
                }
            }

            @Override
            public void onReplySecond(PostCommentEntity postCommentEntity, View v) {
                //帖子回复
                cetPostComment.requestFocus();
                commentEntity.id = postCommentEntity.id;
                commentEntity.nickname = postCommentEntity.nickname;
                senderMsgTag = 2;
                cetPostComment.setEditTextHintContent("回复  " + postCommentEntity.nickname + ":");
                InputUtil.softInputFromWiddow(PostDetailActivity.this);
            }
        });
    }

    //通知帖子数据更新
    private void nofityHomePosts(PostDetailEntity posts){
        Intent intent = new Intent(KnowledgeFragment.BROAD_ACTION_POSTS_UPDATE);
        intent.putExtra(PostDetailActivity.POSTS_ENTITY,posts);
        sendBroadcast(intent);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_left_layout:
                if(mIsFromHome==false){
                    finishByResult();
                }
                finish();
                break;
            case R.id.btn_right_one://举报或者删除弹窗
                showPostReportDialog();
                break;
            case R.id.btn_right_two://分享弹窗
                showShareActionSheetDailog();
                break;
            case R.id.tv_attention:
                favouriteCircle(postDetailEntity);
                break;
            case R.id.ll_bottom_collect:
                if (isPostCollect) {
                    senderCancelCollectPost(postId);
                } else {
                    senderCollectPost(postId);
                }
                break;
            case R.id.ll_post_favorite_layout:
                if (isPostApproval) {
                    senderPostDetailCancelApproval();
                } else {
                    senderPostDetailApproval();
                }
                break;
            case R.id.et_send_post_comment:
                senderMsgTag = 1;
                cetPostComment.setEditTextHintContent("我想说...");
                break;
            case R.id.tv_submit:
                //根据tag,分别设置业务ID请求接口
                if (senderMsgTag == 1) {
                    senderCommentReplyMsgToServer(postId, PostDetailEntity.BUSINESS_ID, cetPostComment.getEditTextContent());
                }
                if (senderMsgTag == 2) {
                    senderCommentReplyMsgToServer(commentEntity.id, PostCommentEntity.POST_COMMENT_BUSINESS_ID, cetPostComment.getEditTextContent());
                }
                break;
        }
    }

    /**
     * 带结果值返回
     */
    private void finishByResult() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        PostDetailEntity entity = new PostDetailEntity();
        entity.id = postDetailEntity.id;
        entity.approvalcount = postApprovalNumber;
        entity.is_approval = isPostApproval;
        entity.is_favorite = isPostCollect;
        entity.commentcount = totalsize + "";
        bundle.putSerializable(Constants.POST_DETAIL_ID, entity);
        entity.commentcount = totalsize + "";
        intent.putExtras(bundle);
        intent.setAction(Constants.ACTION_REFRESH_POST);
        sendBroadcast(intent);
    }

    /**
     * 二级评论的弹窗
     *
     * @param postCommentEntity
     */
    private void showCommentReplyOrRemove(final PostCommentEntity postCommentEntity) {

        postReportDialog = new ActionSheet(this);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.component_reply_or_remove_post_comment, null);
        postReportDialog.setContentView(view);
        postReportDialog.show();

        TextView tvPostCommentContent = (TextView) view.findViewById(R.id.tv_post_comment_content);
        LinearLayout llPostSecondReplyLayout = (LinearLayout) view.findViewById(R.id.ll_post_bottom_second_reply_layout);
        final TextView tvPostSecondReplyOrRemove = (TextView) view.findViewById(R.id.tv_post_second_reply_or_remove);
        final View divider = view.findViewById(R.id.v_middle_second_divider);
        final LinearLayout llPostSecondReport = (LinearLayout) view.findViewById(R.id.ll_post_bottom_second_report_layout);
        TextView tvSecondBottomCancel = (TextView) view.findViewById(R.id.tv_post_second_bottom_cancel);

        final String content = postCommentEntity.nickname + ":" + postCommentEntity.content;
        tvPostCommentContent.setText(content);

        //获取当前用户
        final UserInfo mUserInfo = UserProfileService.getInstance(this).getCurrentLoginedUser();
        //根据用户id做判断显示
        if (mUserInfo.user_id.equals(postCommentEntity.user_id)) {
            llPostSecondReport.setVisibility(View.GONE);
            divider.setVisibility(View.GONE);
            tvPostSecondReplyOrRemove.setText("删除");


        } else {
            llPostSecondReport.setVisibility(View.VISIBLE);
            divider.setVisibility(View.VISIBLE);
            tvPostSecondReplyOrRemove.setText("回复");
        }


        llPostSecondReplyLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (postReportDialog != null) {
                    postReportDialog.dismiss();
                }

                if (mUserInfo.user_id.equals(postCommentEntity.user_id)) {//帖子删除
                    showDeletePostDialog(postCommentEntity.id, 2);
                } else {//帖子回复
                    InputUtil.softInputFromWiddow(PostDetailActivity.this);
                    cetPostComment.requestFocus();
                    commentEntity.id = postCommentEntity.id;
                    commentEntity.nickname = postCommentEntity.nickname;
                    senderMsgTag = 2;
                    cetPostComment.setEditTextHintContent("回复  " + postCommentEntity.nickname + ":");
                }
            }
        });

        llPostSecondReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (postReportDialog != null) {
                    postReportDialog.dismiss();
                }
                //TODO 举报评论的编辑页面的跳转
                Intent intent = new Intent(mContext, ReportEditActivity.class);
                intent.putExtra(Constants.REPORT_CONTENT_ID, postCommentEntity.id);
                intent.putExtra(Constants.REPORT_BUSINESS_ID, PostCommentEntity.POST_COMMENT_BUSINESS_ID);
                mContext.startActivity(intent);

            }
        });

        tvSecondBottomCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postReportDialog.isShowing()) {
                    postReportDialog.dismiss();
                }
            }
        });

    }

    /**
     * 长按保存弹出窗
     */
    private void showPostReportDialog() {

        postReportDialog = new ActionSheet(this);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.component_post_report, null);
        postReportDialog.setContentView(view);
        postReportDialog.show();

        LinearLayout llPostBottomShareLayout = (LinearLayout) view.findViewById(R.id.ll_post_bottom_share_layout);//分享
        LinearLayout llPostBottomReportLayout = (LinearLayout) view.findViewById(R.id.ll_post_bottom_report_layout);//举报
        TextView tvPostOrDelete = (TextView) view.findViewById(R.id.tv_post_report_or_delete);
        TextView tvPostBottomCancel = (TextView) view.findViewById(R.id.tv_post_bottom_cancel);//取消
        llPostBottomShareLayout.setVisibility(View.GONE);

        //获取当前用户
        final UserInfo mUserInfo = UserProfileService.getInstance(this).getCurrentLoginedUser();
        if (mUserInfo.user_id.equals(postDetailEntity.user_id)) {//帖子删除
            tvPostOrDelete.setText("删除");
        } else {//帖子举报
            tvPostOrDelete.setText("举报");
        }

        //举报
        llPostBottomReportLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (postReportDialog != null) {
                    postReportDialog.dismiss();
                }

                if (mUserInfo.user_id.equals(postDetailEntity.user_id)) {//帖子删除
                    //

                    showDeletePostDialog(postDetailEntity.id, 1);

                } else {//帖子举报
                    //TODO 举报帖子编辑页面的跳转
                    Intent intent = new Intent(mContext, ReportEditActivity.class);
                    intent.putExtra(Constants.REPORT_CONTENT_ID, postId);
                    intent.putExtra(Constants.REPORT_BUSINESS_ID, PostDetailEntity.BUSINESS_ID);
                    mContext.startActivity(intent);
                }

            }
        });

        tvPostBottomCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postReportDialog.isShowing()) {
                    postReportDialog.dismiss();
                }
            }
        });

    }

    /**
     * 分享弹出框
     */
    private void showShareActionSheetDailog() {
        final String path = "";//如果分享图片获取该图片的本地存储地址
        final String webPage = String.format(WebViewUrl.H5_POST_SHARE, postDetailEntity.id);
        final String title = postDetailEntity.title;
        final String desc = postDetailEntity.description;

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


    /**
     * 加载帖子内容
     */
    private void loadPostDetail() {

        OKHttpNewSocialCircle.getInstance().getPostDetail(postId, new RequestCallback<PostDetailEntity>() {

            @Override
            public void onError(int errorCode, String errorMsg) {

                switch (errorCode) {
                    case NetResultCode.SERVER_NO_DATA:
                        llNoData.setVisibility(View.VISIBLE);
                        frameLayout.setVisibility(View.GONE);
                        break;
                }
                //加载推荐帖子
                loadRecommendPost(postId, recommendPage, recommendPageSize);
                //加载评论列表页
                loadPostCommentList(currentPage, pageSize, postId);


            }


            @Override
            public void onSuccess(PostDetailEntity response) {
                if (response != null) {
                    llNoData.setVisibility(View.GONE);
                    frameLayout.setVisibility(View.VISIBLE);
                    postDetailEntity = response;

                    //通知首页帖子更新
                    Intent intent = new Intent(KnowledgeFragment.BROAD_ACTION_POSTS_UPDATE);
                    intent.putExtra(PostDetailActivity.POSTS_ENTITY, postDetailEntity);
                    sendBroadcast(intent);

//                    Trace.e("===postDetailEntity.id" + response.id + "appproval_count:" + response.approvalcount + "approval:" + response.is_approval);
                    tvFromCircleName.setText(postDetailEntity.circle_name);
                    Glide.with(mContext).load(response.avatar).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.pic_default_avatar).into(civPostUserAvatar);
                    tvPostPublishTime.setText(DateUtils.formatCurrentTime(postDetailEntity.publish_at * 1000l, System.currentTimeMillis()));

                    if (postDetailEntity.is_top) {
                        ivPostSelectTag.setVisibility(View.VISIBLE);
                        tvPostTitle.setText("     " + postDetailEntity.title);
                    } else {
                        ivPostSelectTag.setVisibility(View.GONE);
                        tvPostTitle.setText(postDetailEntity.title);
                    }

                    tvPostDescription.setText(postDetailEntity.description);
                    tvPostFavorite.setText(StringUtils.tranNum(postDetailEntity.approvalcount));

                    isPostApproval = postDetailEntity.is_approval;
                    postApprovalNumber = StringUtils.tranNum(postDetailEntity.approvalcount);
                    isPostCollect = postDetailEntity.is_favorite;
                    totalsize = Integer.valueOf(postDetailEntity.commentcount);

                    if (totalsize > 0) {
                        tvPostCommentTotalSize.setText(StringUtils.tranNum(totalsize + ""));
                        blankPage.setVisibility(View.GONE);
                        postCommentListView.setVisibility(View.VISIBLE);
                    } else {
                        tvPostCommentTotalSize.setText("评论");
                        blankPage.setVisibility(View.VISIBLE);
                        postCommentListView.setVisibility(View.GONE);
                    }

                    if (postDetailEntity.is_anonymous) {
                        tvPostUserNickname.setText("匿名");
                    } else {
                        tvPostUserNickname.setText(postDetailEntity.nickname);
                    }

                    if (postDetailEntity.is_approval) {
                        ivPostFavorite.setImageResource(R.drawable.icon_post_approval_light);
                        tvPostFavorite.setTextColor(mContext.getResources().getColor(R.color.c_yellow_ffc84a));
                    } else {
                        ivPostFavorite.setImageResource(R.drawable.icon_post_approval);
                        tvPostFavorite.setTextColor(mContext.getResources().getColor(R.color.c_gray_aebdcd));
                    }

                    if (Integer.valueOf(postApprovalNumber) > 0) {
                        tvPostFavorite.setText(postApprovalNumber + "");
                    } else {
                        tvPostFavorite.setText("赞");
                    }

                    if (postDetailEntity.is_concern) {
                        rlPostCircleAttention.setVisibility(View.GONE);
                        vScrollviewHeadDivider.setVisibility(View.GONE);
                        tvAttention.setTextColor(mContext.getResources().getColor(R.color.c_gray_aebdcd));
                    } else {
                        rlPostCircleAttention.setVisibility(View.VISIBLE);
                        vScrollviewHeadDivider.setVisibility(View.VISIBLE);
                        tvAttention.setTextColor(mContext.getResources().getColor(R.color.c_yellow_ffc84a));
                    }
                    if (isPostCollect) {
                        ivCollect.setImageResource(R.drawable.icon_post_collect_light);
                        tvCollect.setText("已收藏");
                        tvCollect.setTextColor(mContext.getResources().getColor(R.color.c_red_fc613c));
                    } else {
                        ivCollect.setImageResource(R.drawable.icon_post_collect);
                        tvCollect.setText("收藏");
                        tvCollect.setTextColor(mContext.getResources().getColor(R.color.c_gray_aebdcd));
                    }

                    //TODO 图片处理,九宫格
                    setPosters(multiImageView, postDetailEntity.posters);
                    //加载推荐帖子
                    loadRecommendPost(postId, recommendPage, recommendPageSize);
                    //加载评论列表页
                    loadPostCommentList(currentPage, pageSize, postId);

                } else {
                    llNoData.setVisibility(View.VISIBLE);
                    frameLayout.setVisibility(View.GONE);
                }
            }
        });

    }

    /**
     * 评论列表加载
     *
     * @param currentPage
     * @param pageSize
     * @param postId
     */
    private void loadPostCommentList(int currentPage, int pageSize, String postId) {

        if (StringUtils.isBlank(postId)) {
            return;
        }

        OKHttpNewSocialCircle.getInstance().getPostCommentList(currentPage, pageSize, postId, new RequestCallback<PostCommentDTO>() {

            @Override
            public void onError(int errorCode, String errorMsg) {


                frameLayout.refreshComplete();
                dismissLoadingDialog();
            }

            @Override
            public void onSuccess(PostCommentDTO response) {

                PostCommentDTO postCommentDto = response;
                refreshCommentListView(postCommentDto);
                dismissLoadingDialog();

            }
        });

    }

    /**
     * 刷新监听
     */
    PtrDefaultHandler2 ptrDefaultHandler2 = new PtrDefaultHandler2() {
        @Override
        public void onLoadMoreBegin(PtrFrameLayout frame) {
            if (isMaxPage) {
                frameLayout.refreshComplete();//加载完毕
            } else {
                currentPage++;
                loadPostCommentList(currentPage, pageSize, postId);
            }
        }

        @Override
        public void onRefreshBegin(PtrFrameLayout frame) {
            currentPage = 0;
            refreshAll();
        }
    };


    /**
     * 全部布局整体刷新
     */
    private void refreshAll() {
        currentPage = 0;
        postCommentAdapter.setMaxSize(0);
        loadPostDetail();

    }

    /**
     * 刷新回答列表
     */
    private void refreshCommentListView(PostCommentDTO postCommentDTO) {

        if (currentPage == 0) {
            postCommentAdapter.cleanList();
        }
        postCommentAdapter.modifyList(postCommentDTO.result);

        if (postCommentDTO.result.size() < pageSize) {
            isMaxPage = true;
            postCommentAdapter.setMaxSize(postCommentAdapter.getList().size() + 1);
            frameLayout.setMode(PtrFrameLayout.Mode.REFRESH);
        } else {
            frameLayout.setMode(PtrFrameLayout.Mode.BOTH);
        }

        frameLayout.refreshComplete();
    }

    private void refreshKeyboard() {
        if (mIsShowKeyboard) {//显示软键盘
            cetPostComment.requestFocus();
            tvSubmit.setVisibility(View.VISIBLE);
            llSwitchLayout.setVisibility(View.GONE);
            if (senderMsgTag == 1) {
                cetPostComment.setEditTextHintContent("我想说 ...");
            }
        } else {
            //隐藏软键盘
            hideKeybord();

        }
    }

    /**
     * 隐藏软键盘
     */
    private void hideKeybord() {
        llEditLayout.requestFocus();
        llEditLayout.setFocusable(true);
        llEditLayout.setFocusableInTouchMode(true);
        tvSubmit.setVisibility(View.GONE);
        llSwitchLayout.setVisibility(View.VISIBLE);
        cetPostComment.setEditTextHintContent("我想说 ...");
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
                tvSubmit.setVisibility(View.VISIBLE);
                llSwitchLayout.setVisibility(View.GONE);
                if (senderMsgTag == 1) {
                    cetPostComment.setEditTextHintContent("我想说...");
                } else if (senderMsgTag == 2) {
                    cetPostComment.setEditTextHintContent("回复  " + commentEntity.nickname + ":");
                }
            } else {
                //当软键盘关闭的时候，同时关掉灰色的层
                tvSubmit.setVisibility(View.GONE);
                llSwitchLayout.setVisibility(View.VISIBLE);
                cetPostComment.setEditTextHintContent("我想说...");
            }

        }

    };

    /**
     * 收藏帖子
     *
     * @param postId
     */
    private void senderCollectPost(String postId) {
        OKHttpNewSocialCircle.getInstance().collectPost(postId, PostDetailEntity.BUSINESS_ID, new RequestCallback<ResultEntity>() {

            @Override
            public void onError(int errorCode, String errorMsg) {
                Toast.makeText(mContext, "" + errorMsg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(ResultEntity response) {
                isPostCollect = true;
                ivCollect.setImageResource(R.drawable.icon_post_collect_light);
                tvCollect.setText("已收藏");
                tvCollect.setTextColor(mContext.getResources().getColor(R.color.c_red_fc613c));
                showDialog("收藏成功", R.drawable.icon_dialog_success);
            }
        });
    }

    /**
     * 取消收藏帖子
     *
     * @param postId
     */
    private void senderCancelCollectPost(final String postId) {
        OKHttpNewSocialCircle.getInstance().cancelCollectPost(postId, PostDetailEntity.BUSINESS_ID, new RequestCallback<ResultEntity>() {

            @Override
            public void onError(int errorCode, String errorMsg) {
                Toast.makeText(mContext, "" + errorMsg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(ResultEntity response) {
                isPostCollect = false;
                ivCollect.setImageResource(R.drawable.icon_post_collect);
                tvCollect.setText("收藏");
                tvCollect.setTextColor(mContext.getResources().getColor(R.color.c_gray_aebdcd));
                showDialog("收藏取消", R.drawable.icon_dialog_success);
            }
        });
    }


    /**
     * 帖子评论的点赞取消
     */
    private void senderCancelPostCommentApproval(final PostCommentEntity entity) {
        OKHttpNewSocialCircle.getInstance().postCommentCancelApproval(entity.id, PostCommentEntity.POST_COMMENT_BUSINESS_ID, new RequestCallback<ResultEntity>() {
            @Override
            public void onError(int errorCode, String errorMsg) {

            }

            @Override
            public void onSuccess(ResultEntity response) {
                List<PostCommentEntity> PostCommentList = postCommentAdapter.getList();
                for (int i = 0; i < PostCommentList.size(); i++) {
                    if (entity.id.equals(PostCommentList.get(i).id)) {
                        PostCommentList.get(i).is_approval = entity.is_approval;
                        PostCommentList.get(i).approval_count = entity.approval_count;
                    }
                }
                postCommentAdapter.notifyDataSetChanged();
                showDialog("点赞取消", R.drawable.icon_dialog_success);
            }
        });
    }


    /**
     * 帖子评论的点赞
     */
    private void senderPostCommentApproval(final PostCommentEntity entity) {
        OKHttpNewSocialCircle.getInstance().postCommentApproval(entity.id, PostCommentEntity.POST_COMMENT_BUSINESS_ID, new RequestCallback<ResultEntity>() {

            @Override
            public void onError(int errorCode, String errorMsg) {

            }

            @Override
            public void onSuccess(ResultEntity response) {

                List<PostCommentEntity> postCommentList = postCommentAdapter.getList();
                for (int i = 0; i < postCommentList.size(); i++) {

                    if (entity.id.equals(postCommentList.get(i).id)) {
                        postCommentList.get(i).is_approval = entity.is_approval;
                        postCommentList.get(i).approval_count = entity.approval_count;
                    }
                }
                postCommentAdapter.notifyDataSetChanged();

                showDialog("点赞成功", R.drawable.icon_dialog_success);


            }
        });
    }

    /**
     * 评论回复
     *
     * @param content
     */
    private void senderCommentReplyMsgToServer(final String content_id, final String business_id, String content) {

        if (StringUtils.isBlank(content)) {
            Toast.makeText(mContext, "发表内容不能为空哦O(∩_∩)O~", Toast.LENGTH_SHORT).show();
            return;
        }

        OKHttpNewSocialCircle.getInstance().postCommentReplyCreate(content_id, business_id, content, new RequestCallback<PostSecondDTO>() {


            @Override
            public void onError(int errorCode, String errorMsg) {

            }

            @Override
            public void onSuccess(PostSecondDTO response) {
                InputUtil.hideSoftInputFromWindow(PostDetailActivity.this);

                //评论完后进行初始化到帖子评论标记
                if (business_id.equals(PostCommentEntity.POST_COMMENT_BUSINESS_ID)) {
                    senderMsgTag = 1;
                }
                cetPostComment.setEditTextContent("");

                if (!PostCommentEntity.POST_COMMENT_BUSINESS_ID.equals(business_id)) {//如果是对帖子评论（一级评论），就进行网络刷新，其余本地刷新
                    //更新首页评论过数
                    postDetailEntity.commentcount=String.valueOf(Integer.parseInt(postDetailEntity.commentcount)+1);
                    nofityHomePosts(postDetailEntity);
                    //重新刷新一级评论列表
                    currentPage = 0;
                    postCommentAdapter.setMaxSize(0);
                    loadPostCommentList(currentPage,pageSize,postId);
                    return;
                }

                PostSecondCommentEntity secondCommentEntity = response.value;

                if (null == secondCommentEntity) {
                    return;
                }

                List<PostCommentEntity> postCommentList = postCommentAdapter.getList();
                for (int i = 0; i < postCommentList.size(); i++) {

                    if (content_id.equals(postCommentList.get(i).id)) {

                        List<PostSecondCommentEntity> tempReply = new ArrayList<>();
                        tempReply.add(secondCommentEntity);
                        tempReply.addAll(postCommentList.get(i).repply);
                        postCommentList.get(i).repply = tempReply;
                    }
                }
                postCommentAdapter.notifyDataSetChanged();

            }
        });

    }

    /**
     * 帖子详情的点赞
     */
    private void senderPostDetailApproval() {
        OKHttpNewSocialCircle.getInstance().postCommentApproval(postId, PostDetailEntity.BUSINESS_ID, new RequestCallback<ResultEntity>() {

            @Override
            public void onError(int errorCode, String errorMsg) {
                Toast.makeText(mContext, "" + errorMsg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(ResultEntity response) {
                isPostApproval = true;
                ivPostFavorite.setImageResource(R.drawable.icon_post_approval_light);
                tvPostFavorite.setTextColor(mContext.getResources().getColor(R.color.c_yellow_ffc84a));
                postApprovalNumber = (Integer.valueOf(postApprovalNumber) + 1) + "";
                //通知首页帖子更新
                postDetailEntity.is_approval = true;
                postDetailEntity.approvalcount = String.valueOf(postApprovalNumber);
                //
                nofityHomePosts(postDetailEntity);
                //
                if (Integer.valueOf(postApprovalNumber) > 0) {
                    tvPostFavorite.setText(postApprovalNumber + "");
                } else {
                    tvPostFavorite.setText("赞");
                }
                showDialog("点赞成功", R.drawable.icon_dialog_success);
            }
        });

    }

    /**
     * 帖子的点赞取消
     */
    private void senderPostDetailCancelApproval() {
        OKHttpNewSocialCircle.getInstance().postCommentCancelApproval(postId, PostDetailEntity.BUSINESS_ID, new RequestCallback<ResultEntity>() {

            @Override
            public void onError(int errorCode, String errorMsg) {
                Toast.makeText(mContext, "" + errorMsg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(ResultEntity response) {
                isPostApproval = false;
                ivPostFavorite.setImageResource(R.drawable.icon_post_approval);
                tvPostFavorite.setTextColor(mContext.getResources().getColor(R.color.c_gray_aebdcd));
                postApprovalNumber = Integer.valueOf(postApprovalNumber) - 1 + "";
                //通知首页帖子更新
                postDetailEntity.is_approval = false;
                postDetailEntity.approvalcount = String.valueOf(postApprovalNumber);
                //
                nofityHomePosts(postDetailEntity);
                if (Integer.valueOf(postApprovalNumber) > 0) {
                    tvPostFavorite.setText(postApprovalNumber + "");
                } else {
                    tvPostFavorite.setText("赞");
                }

                showDialog("点赞取消", R.drawable.icon_dialog_success);

            }
        });
    }


    /**
     * 关注圈子
     */
    private void favouriteCircle(final PostDetailEntity postDetailEntity) {
        final PostDetailEntity entity = postDetailEntity;

        OkHttpCircleImpI.getInstance().circleFavourite(entity.circle_id, new RequestCallback<CommonResultEntity>() {
            @Override
            public void onError(int errorCode, String errorMsg) {
                Trace.i("circle favourite fail!");
            }

            @Override
            public void onSuccess(CommonResultEntity response) {
                Trace.i("circle favourite successful!");
                tvAttention.setText("已关注");
                tvAttention.setTextColor(mContext.getResources().getColor(R.color.c_gray_aebdcd));

                showDialog("关注成功 ^_^ ", R.drawable.icon_dialog_success);

                CircleEntity circleEntity = new CircleEntity();
                circleEntity.is_concern = true;
                circleEntity.id = entity.circle_id;
                circleEntity.name = entity.circle_name;
                circleEntity.poster = entity.circle_poster;
                circleEntity.notice = entity.circle_notice;
                circleEntity.description = entity.description;
                Intent intent_cir = new Intent(CommunityCircleFragment.BROAD_ACTION_UPDATE_CIRCLE);
                intent_cir.putExtra(CommunityCircleFragment.PARAMS_CIRCLE, circleEntity);
                intent_cir.putExtra(CommunityCircleFragment.PARAMS_CIRCLE_IS_FAVOURITE, circleEntity.is_concern);
                mContext.sendBroadcast(intent_cir);
                Intent intent_mycir = new Intent(CommunityMyCircleFragment.BROAD_ACTION_UPDATE_MY_CIRCLE);
                intent_mycir.putExtra(CommunityCircleFragment.PARAMS_CIRCLE, circleEntity);
                intent_mycir.putExtra(CommunityCircleFragment.PARAMS_CIRCLE_IS_FAVOURITE, circleEntity.is_concern);
                mContext.sendBroadcast(intent_mycir);
            }
        });
    }

    /**
     * 删除帖子评论
     *
     * @param commentId
     */
    private void deleteCommentById(String commentId) {

        OKHttpNewSocialCircle.getInstance().deleteComment(commentId, new RequestCallback<ResultEntity>() {

            @Override
            public void onError(int errorCode, String errorMsg) {

            }

            @Override
            public void onSuccess(ResultEntity response) {
                refreshAll();
                //发送删除帖子的广播
                Intent intentRefreshPostList = new Intent(Constants.ACTION_REFRESH_POST_LIST);
                mContext.sendBroadcast(intentRefreshPostList);
            }
        });

    }

    /**
     * 删除帖子
     *
     * @param id
     */
    private void senderDeletePost(final String id) {

        OKHttpNewSocialCircle.getInstance().deletePost(id, new RequestCallback<SendPostResultDTO>() {

            @Override
            public void onError(int errorCode, String errorMsg) {

            }

            @Override
            public void onSuccess(SendPostResultDTO response) {
                //通知帖子列表更新数据
                Intent intent = new Intent(CircleDetailAllFragment.BROAD_ACTION_POSTS_DELETE);
                intent.putExtra(Constants.POST_DETAIL_ID, id);
                sendBroadcast(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        //返回前通知上一个页面刷新变动数据,首页不刷新
        if(mIsFromHome==false){
            finishByResult();
        }
        super.onBackPressed();
    }

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

    /**
     * 帖子删除相关弹窗
     *
     * @param id
     * @param type 1表示帖子删除,2表示评论删除
     */
    private void showDeletePostDialog(final String id, final int type) {
        final RfCommonDialog.Builder builder = new RfCommonDialog.Builder(mContext);
        if (type == 1) {
            builder.setTitle("确认删除该条帖子？");
        } else {
            builder.setTitle("确认删除该条评论？");
        }

        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (type == 1) {
                    senderDeletePost(id);
                } else {
                    deleteCommentById(id);
                }

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        RfCommonDialog commonDialog = builder.create();
        commonDialog.setCanceledOnTouchOutside(true);
        commonDialog.setCancelable(true);
        commonDialog.show();
    }

    private void setPosters(MultiImageView multiImageView, final List<String> posters) {
        List<PhotoInfo> photos = null;
        if (photos == null) {
            photos = new ArrayList<>();
        } else {
            photos.clear();
        }
        for (String photoUrl : posters) {
            PhotoInfo photoInfo = new PhotoInfo();
            photoInfo.url = photoUrl + Constants.IMAGE_TAILOR_URL;
            photos.add(photoInfo);
        }
        if (photos != null && photos.size() > 0) {
            multiImageView.setVisibility(View.VISIBLE);
            multiImageView.setList(photos);
            multiImageView.setmOnItemClickListener(new MultiImageView.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    //imagesize是作为loading时的图片size
                    Intent intent2PicDetail = new Intent(mContext, BigPictureActivity.class);
                    intent2PicDetail.putStringArrayListExtra(BigPictureActivity.PICTURE_LIST, (ArrayList<String>) posters);
                    intent2PicDetail.putExtra(BigPictureActivity.CURRENT_ITEM, position);
                    mContext.startActivity(intent2PicDetail);
                }
            });
        } else {
            multiImageView.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) {
            return;
        }
        Bundle bundle = data.getExtras();
        if (bundle == null) {
            return;
        }
        if (requestCode != Constants.POST_COMMENT_RESULT) {
            return;
        }

        PostCommentEntity entity = (PostCommentEntity) bundle.getSerializable(Constants.POST_COMMENT_ID);

        List<PostCommentEntity> postCommentList = postCommentAdapter.getList();
        for (int i = 0; i < postCommentList.size(); i++) {

            if (entity.id.equals(postCommentList.get(i).id)) {
                postCommentList.get(i).is_approval = entity.is_approval;
                postCommentList.get(i).approval_count = entity.approval_count;
                postCommentList.get(i).repply = entity.repply;
            }
        }
        postCommentAdapter.notifyDataSetChanged();

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(Menu.NONE, Menu.FIRST, 1, "复制");
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case Menu.FIRST:
                copyContentText(tvPostDescription);
                break;
        }
        return super.onContextItemSelected(item);
    }

    /**
     * @param textView
     */
    private void copyContentText(TextView textView) {

        // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        cm.setText(textView.getText());

    }

    /**
     * 加载推荐帖子
     *
     * @param page
     * @param pageSize
     */
    private void loadRecommendPost(String postId, int page, int pageSize) {
        OKHttpNewSocialCircle.getInstance().loadRecommendlist(postId, page, pageSize, new RequestCallback<PostDetailListDTO>() {

            @Override
            public void onError(int errorCode, String errorMsg) {

            }

            @Override
            public void onSuccess(PostDetailListDTO response) {

                List<PostDetailEntity> postList = response.list;
                refreshRecommendPostListView(postList);

            }
        });

    }


    /**
     * 刷新推荐帖子
     *
     * @param postList
     */
    public void refreshRecommendPostListView(final List<PostDetailEntity> postList) {

        if (null == postList) {
            llRecommendPostContentLayout.setVisibility(View.GONE);
            return;
        }
        if (postList.size() <= 0) {//优化
            llRecommendPostContentLayout.setVisibility(View.GONE);
        } else {
            llRecommendPostContentLayout.setVisibility(View.VISIBLE);
            llRecommendPostLayout.removeAllViews();
        }

        for (int i = 0; i < postList.size(); i++) {
            final PostDetailEntity entity = postList.get(i);
            RecommendPostItemView recommendPostItemView = new RecommendPostItemView(mContext);
            recommendPostItemView.setData(entity);
            recommendPostItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PostDetailActivity.class);
                    intent.putExtra(Constants.POST_DETAIL_ID, entity.id);
                    startActivity(intent);
                }
            });
            llRecommendPostLayout.addView(recommendPostItemView);
        }
    }

}
