package com.junhsue.ksee.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.felipecsl.gifimageview.library.GifImageView;
import com.junhsue.ksee.ActivityDetailActivity;
import com.junhsue.ksee.AnswerCardDetailActivity;
import com.junhsue.ksee.ApplicationEnterActivity;
import com.junhsue.ksee.ArticleDetailActivity;
import com.junhsue.ksee.AskActivity;
import com.junhsue.ksee.BaseActivity;
import com.junhsue.ksee.CircleDetailActivity;
import com.junhsue.ksee.ClassGroupChatActivity;
import com.junhsue.ksee.ColleageCourseActivity;
import com.junhsue.ksee.CourseDetailsActivity;
import com.junhsue.ksee.CourseSystemDetailsActivity;
import com.junhsue.ksee.HomeTagsListActivity;
import com.junhsue.ksee.LiveDetailsActivity;
import com.junhsue.ksee.LiveListActivity;
import com.junhsue.ksee.MainActivity;
import com.junhsue.ksee.MsgActivity;
import com.junhsue.ksee.OfflineCourseActivity;
import com.junhsue.ksee.PostDetailActivity;
import com.junhsue.ksee.QuestionDetailActivity;
import com.junhsue.ksee.R;
import com.junhsue.ksee.RealizeTagsListActivity;
import com.junhsue.ksee.SelectedQuestionActivity;
import com.junhsue.ksee.SolutionDetailsActivity;
import com.junhsue.ksee.SolutionListActivity;
import com.junhsue.ksee.VideoActivity;
import com.junhsue.ksee.VideoDetailActivity;
import com.junhsue.ksee.adapter.HomeManagerTagsAdapter;
import com.junhsue.ksee.adapter.HomePostNormalAdapter;
import com.junhsue.ksee.adapter.HomeSolutionPackageAdapter;
import com.junhsue.ksee.adapter.ImageSelectAdapter;
import com.junhsue.ksee.adapter.MsgAdapter;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.common.IArticleTag;
import com.junhsue.ksee.common.IHomeMenu;
import com.junhsue.ksee.common.ISavePointRecordValue;
import com.junhsue.ksee.common.IntentLaunch;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.dto.CircleListDTO;
import com.junhsue.ksee.dto.HomeBannerDTO;
import com.junhsue.ksee.dto.HomeContentDTO;
import com.junhsue.ksee.dto.HomeManagersListDTO;
import com.junhsue.ksee.dto.HomePostsHotDTO;
import com.junhsue.ksee.dto.KnowCalendarDTO;
import com.junhsue.ksee.dto.KnowCalendarPriseDTO;
import com.junhsue.ksee.dto.MessageDTO;
import com.junhsue.ksee.dto.MsgAnswerFavouriteDTO;
import com.junhsue.ksee.dto.MsgCountEntityDTO;
import com.junhsue.ksee.dto.PostDetailListDTO;
import com.junhsue.ksee.dto.SolutionPackageDTO;
import com.junhsue.ksee.entity.CircleEntity;
import com.junhsue.ksee.entity.ClassRoomListEntity;
import com.junhsue.ksee.entity.HomeLiveEntity;
import com.junhsue.ksee.entity.HomeManagersListEntity;
import com.junhsue.ksee.entity.HomeQuestionEntity;
import com.junhsue.ksee.entity.HomeVideoEntity;
import com.junhsue.ksee.entity.MsgActivityEntity;
import com.junhsue.ksee.entity.MsgActivityNewEntity;
import com.junhsue.ksee.entity.MsgClassRoomEntity;
import com.junhsue.ksee.entity.MsgCountEntity;
import com.junhsue.ksee.entity.MsgCourseEntity;
import com.junhsue.ksee.entity.MsgCourseJoinEntity;
import com.junhsue.ksee.entity.MsgEntity;
import com.junhsue.ksee.entity.MsgInfoEntity;
import com.junhsue.ksee.entity.MsgLiveEntity;
import com.junhsue.ksee.entity.MsgLiveNewEntity;
import com.junhsue.ksee.entity.MsgSystemUpdate;
import com.junhsue.ksee.entity.PostCommentEntity;
import com.junhsue.ksee.entity.PostDetailEntity;
import com.junhsue.ksee.entity.RealizeArticleEntity;
import com.junhsue.ksee.entity.ResultEntity;
import com.junhsue.ksee.entity.Solution;
import com.junhsue.ksee.entity.SolutionGroup;
import com.junhsue.ksee.entity.SolutionPackage;
import com.junhsue.ksee.entity.UserInfo;
import com.junhsue.ksee.entity.VideoEntity;
import com.junhsue.ksee.file.FileUtil;
import com.junhsue.ksee.file.ImageLoaderOptions;
import com.junhsue.ksee.fragment.dialog.CircleCommonDialog;
import com.junhsue.ksee.net.api.ICourse;
import com.junhsue.ksee.net.api.OKHttpCourseImpl;
import com.junhsue.ksee.net.api.OKHttpHomeImpl;
import com.junhsue.ksee.net.api.OKHttpNewSocialCircle;
import com.junhsue.ksee.net.api.OkHttpILoginImpl;
import com.junhsue.ksee.net.callback.BroadIntnetConnectListener;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.error.NetResultCode;
import com.junhsue.ksee.net.url.WebViewUrl;
import com.junhsue.ksee.profile.UserProfileService;
import com.junhsue.ksee.utils.DataGsonUitls;
import com.junhsue.ksee.utils.DateUtils;
import com.junhsue.ksee.utils.DensityUtil;
import com.junhsue.ksee.utils.ScreenWindowUtil;
import com.junhsue.ksee.utils.ShareUtils;
import com.junhsue.ksee.utils.StartActivityUtils;
import com.junhsue.ksee.utils.StatisticsUtil;
import com.junhsue.ksee.utils.StringUtils;
import com.junhsue.ksee.utils.ToastUtil;
import com.junhsue.ksee.view.ActionSheet;
import com.junhsue.ksee.view.CircleImageView;
import com.junhsue.ksee.view.CommonBanner;
import com.junhsue.ksee.view.CommonListView;
import com.junhsue.ksee.view.HomeArticleItemView;
import com.junhsue.ksee.view.HomeNormalPostItemView;
import com.junhsue.ksee.view.HomeSolutionPView;
import com.junhsue.ksee.view.HomeVideoItemView;
import com.junhsue.ksee.view.IndicatorView;
import com.junhsue.ksee.view.MyHorizontalScrollView;
import com.junhsue.ksee.view.Spanny;
import com.junhsue.ksee.view.fancycoverflow.FancyCoverFlow;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.social.UMPlatformData;


import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.loadmore.LoadMoreContainer;
import in.srain.cube.views.loadmore.LoadMoreHandler;
import in.srain.cube.views.loadmore.LoadMoreListViewContainer;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 拾
 * Created by longer on 17/3/16 in Junhsue
 *
 * @describe 描述
 * 首页卡片构思
 */

public class KnowledgeFragment extends BaseFragment implements View.OnClickListener,
        MsgEntity.IMsgClickListener, AdapterView.OnItemClickListener, BroadIntnetConnectListener.InternetChanged,
        CommonBanner.OnViewPagerClickListener, HomeManagersListEntity.IManagersClickListener {

    //消息更新返回编码
    public static final int RESULT_CODE_MSG = 0x2012;
    //帖子更新
    public static final String BROAD_ACTION_POSTS_UPDATE = "com.junhsue.ksee.action_posts_update";
    //
    private ImageView mImgCalendar, mImgCalendarEmpty, mImgShare;
    private TextView mTvAgree;
    private BaseActivity mContext;
    private MainActivity mMainActivity;
    private PtrClassicFrameLayout mPtrClassicFrameLayout;
    //直播
    private LinearLayout mLLLiveForeshow;
    //
    //private ImageView mImgLiveStatusTag;
    private ImageView mImgLiveForeshow;
    //直播标题
    private TextView mTxtLiveTtile;
    //直播开始的时间
    private TextView mTxtLiveTime;
    //演讲人
    private TextView mtxtLiveSpeaker;

    /**
     * 快捷入口菜单
     */
    private LinearLayout mLLMenuTab;

    /**
     * 问答
     */
    //private LinearLayout mLLContentQuestion;
    //private IndicatorView mIvQuestionListView;
    // private LinearLayout mLLQuestionAskEnter;


    /**
     * 方案包
     */
    //private HomeSolutionPView mHomeSolutionPView;
    //方案包全局布局
    private LinearLayout llHomeSolution;
    //方案包标题
    private TextView mTxtSolutionTitle;
    private TextView mTxtSolutionDesc;
    private LinearLayout mLLSolutionTagIndex;
    private LinearLayout mHomeSolution;

    //

    private FancyCoverFlow mFancyCoverFlow;
    private HomeSolutionPackageAdapter mHomeSolutionPackageAdapter;
    //方案包列表
    private ArrayList<Solution> mListSolution = new ArrayList<Solution>();

    /**
     * 文章
     */
    private LinearLayout mLLContentArticle;
    private TextView mTxtContentArticleMore;
    private LinearLayout mLLContentArticleBlock;
    private LinearLayout mLLArticleExpend;

    /**
     * 单个视频
     */
    private LinearLayout llNewNoneVideo;

    /**
     * 帖子普通
     */
    private CommonListView lvNormalPost;
    private HomePostNormalAdapter homePostNormalAdapter;
    private int postPage = 0;
    private boolean isMaxPage;


    /**
     * 视频
     */
    private LinearLayout mLLContentVideo;
    private TextView mTxtContentVideoMore;
    private LinearLayout mLLContentVideoBlock;
    private RelativeLayout mRLLive;

    /**
     * 精选内容
     */
    private ImageView mImgQeustionReply, mImgHR, mImgOrganizationjoin, author;
    //消息列表
    //private CommonListView mLVMsg;
    private UserInfo mUserInfo;

    private ActionSheet shareActionSheetDialog;

    private KnowCalendarDTO mKnowCalendar;

    //当前页
    private final int mCurrentPage = 0;

    //版本号
    private String version;

    private MsgAdapter<MsgEntity> mMsgAdapter;

    /**
     * 轮播
     */
    //首页轮播图
    private CommonBanner mBanner;
    //数据源
    private List<String> mLists = new ArrayList<String>();
    private HomeBannerDTO mHomeBannerDTO;
    //ViewPager的点击事件
    private CommonBanner.OnViewPagerClickListener onViewPagerClickListener;

    /**
     * 办学标签列表*/
    private CommonListView mLvArticle;
    private HomeManagerTagsAdapter<HomeManagersListEntity> mLvArticleAdapter;
    //
    private ImageView mImgEmpty;

    /**
     * 精选帖子
     *
     * @return
     */
    private LinearLayout mLLContentPostSelection;


    /**
     * 最热帖子
     */
    private HomePostsHotDTO mHomePostHotDTO;

    /**
     * 首页消息
     */
    private TextView mTxtMsgCount;
    private RelativeLayout mRLMsg;

    //精选帖子
    private ArrayList<PostDetailEntity> mListPostSelection = new ArrayList<PostDetailEntity>();
    //每天读一点的前4条帖子
    private ArrayList<PostDetailEntity> mListPostNormal = new ArrayList<PostDetailEntity>();
    //
    private LinearLayout mLLPostSelection;

    /**
     * 加载更多
     */
    private LoadMoreListViewContainer mLoadMoreListViewContainer;
    private ScrollView mScrollview;

    public static KnowledgeFragment newInstance() {
        KnowledgeFragment knowledgeFragment = new KnowledgeFragment();
        return knowledgeFragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.act_knowledge;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = (BaseActivity) activity;
        mMainActivity = (MainActivity) activity;
    }

    @Override
    protected void onInitilizeView(View view) {
        initView(view);
    }

    private void initView(View v) {
        mScrollview = (ScrollView) v.findViewById(R.id.scrollview);
        mLoadMoreListViewContainer = (LoadMoreListViewContainer) v.findViewById(R.id.loadmore);
        mLoadMoreListViewContainer.useDefaultFooter();
        mLoadMoreListViewContainer.setAutoLoadMore(true);
        mLoadMoreListViewContainer.setLoadMoreHandler(loadMoreHandler);
        //
        mRLMsg = (RelativeLayout) v.findViewById(R.id.rl_msg);
        mTxtMsgCount = (TextView) v.findViewById(R.id.msg_count);
        mLLContentPostSelection = (LinearLayout) v.findViewById(R.id.ll_content_post_selection);
        mLLPostSelection = (LinearLayout) v.findViewById(R.id.ll_post_selection);

        mImgEmpty = (ImageView) v.findViewById(R.id.img);
        mImgCalendar = (ImageView) v.findViewById(R.id.img_know_calendar);
        mImgCalendarEmpty = (ImageView) v.findViewById(R.id.img_know_calendar_empty);
        mImgShare = (ImageView) v.findViewById(R.id.img_know_share);
        //mLVMsg = (CommonListView) v.findViewById(R.id.lv_msg);
        mTvAgree = (TextView) v.findViewById(R.id.tv_know_agree);
        //
        mLLLiveForeshow = (LinearLayout) v.findViewById(R.id.ll_home_live_foreshow);
        //mImgLiveStatusTag = (Button) v.findViewById(R.id.img_live_foreshow_status);
        mTxtLiveTtile = (TextView) v.findViewById(R.id.txt_live_foreshow_title);
        mTxtLiveTime = (TextView) v.findViewById(R.id.txt_live_content_start_time);
        mtxtLiveSpeaker = (TextView) v.findViewById(R.id.txt_live_content_speaker);
        mImgLiveForeshow = (ImageView) v.findViewById(R.id.img_live_foreshow);
        mRLLive = (RelativeLayout) v.findViewById(R.id.rl_content_live);

        //
        //mLLContentQuestion = (LinearLayout) v.findViewById(R.id.ll_home_content_question);
        //mIvQuestionListView = (IndicatorView) v.findViewById(R.id.iv_question_list);
        //mLLQuestionAskEnter = (LinearLayout) v.findViewById(R.id.ll_home_question_ask_enter);
        //文章
        mLLContentArticle = (LinearLayout) v.findViewById(R.id.ll_home_content_article);
//        mTxtContentArticleMore = (TextView) v.findViewById(R.id.txt_aritcle_more);
        mLLContentArticleBlock = (LinearLayout) v.findViewById(R.id.ll_home_content_article_block);
//        mLLArticleExpend = (LinearLayout) v.findViewById(R.id.ll_article_expend);
        llNewNoneVideo = (LinearLayout) v.findViewById(R.id.ll_new_none_video);
        //帖子
        lvNormalPost = (CommonListView) v.findViewById(R.id.lv_post_load_more);

        //
        mLLContentVideo = (LinearLayout) v.findViewById(R.id.ll_home_content_video);
        mTxtContentVideoMore = (TextView) v.findViewById(R.id.txt_video_more);
        mLLContentVideoBlock = (LinearLayout) v.findViewById(R.id.ll_home_content_video_block);
        //
        //mHomeSolutionPView=(HomeSolutionPView)v.findViewById(R.id.home_solution_p_view);
        llHomeSolution = (LinearLayout) v.findViewById(R.id.ll_home_solution);
        mTxtSolutionTitle = (TextView) v.findViewById(R.id.txt_solution_title);
        mTxtSolutionDesc = (TextView) v.findViewById(R.id.txt_solution_desc);
        mLLSolutionTagIndex = (LinearLayout) v.findViewById(R.id.ll_content_tag);
        mHomeSolution = (LinearLayout) v.findViewById(R.id.ll_home_solution);
        mFancyCoverFlow = (FancyCoverFlow) v.findViewById(R.id.fancyCoverFlow);
        //快捷菜单入口
        mLLMenuTab = (LinearLayout) v.findViewById(R.id.ll_menu_tag);
        //
        mImgQeustionReply = (ImageView) v.findViewById(R.id.img_content_bottom_ranking_question);
        mImgHR = (ImageView) v.findViewById(R.id.img_content_bottom_hr);
        mImgOrganizationjoin = (ImageView) v.findViewById(R.id.img_content_bottom_organization_join);
        author = (ImageView) v.findViewById(R.id.img_content_bottom_author);
        mPtrClassicFrameLayout = (PtrClassicFrameLayout) v.findViewById(R.id.ptr_plassic_frameLayout);
        mPtrClassicFrameLayout.setMode(PtrFrameLayout.Mode.REFRESH);
        mPtrClassicFrameLayout.setPtrHandler(ptrDefaultHandler);
        mPtrClassicFrameLayout.disableWhenHorizontalMove(true);



        /*帖子数据初始化*/
        homePostNormalAdapter = new HomePostNormalAdapter(getContext());
        lvNormalPost.setAdapter(homePostNormalAdapter);

        homePostNormalAdapter.setHomePostListener(new HomePostNormalAdapter.HomePostLister() {
            @Override
            public void refreshApproval(PostDetailEntity postDetailEntity, View v) {
                if (postDetailEntity.is_approval) {
                    senderPostDetailCancelApproval(postDetailEntity);
                } else {
                    senderPostDetailApproval(postDetailEntity);
                }
            }
        });
        lvNormalPost.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, PostDetailActivity.class);
                Bundle bundle = new Bundle();
                PostDetailEntity postDetailEntity = (PostDetailEntity) homePostNormalAdapter.getList().get(position);
                bundle.putString(Constants.POST_DETAIL_ID, postDetailEntity.id);
                bundle.putBoolean(PostDetailActivity.PARAMS_IS_FROM_HOME,true);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

//        mMsgAdapter = new MsgAdapter<MsgEntity>(mContext);
//        mLVMsg.setAdapter(mMsgAdapter);
//        mLVMsg.setOnItemClickListener(this);
//        mMsgAdapter.setIMsgClickListener(this);

        //
        mImgCalendar.setOnClickListener(this);
        mImgShare.setOnClickListener(this);
        mTvAgree.setOnClickListener(this);
        mUserInfo = UserProfileService.getInstance(mContext).getCurrentLoginedUser();

        //快捷菜单
//        v.findViewById(R.id.rl_live).setOnClickListener(this);
//        v.findViewById(R.id.rl_question).setOnClickListener(this);
//        //v.findViewById(R.id.rl_class_room).setOnClickListener(this);
//        v.findViewById(R.id.rl_course).setOnClickListener(this);
//        v.findViewById(R.id.ll_source).setOnClickListener(this);
//        v.findViewById(R.id.ll_entrepreneurs).setOnClickListener(this);
//        v.findViewById(R.id.ll_student_join).setOnClickListener(this);
//        v.findViewById(R.id.ll_manage).setOnClickListener(this);
//        v.findViewById(R.id.ll_video).setOnClickListener(this);
        v.findViewById(R.id.txt_content_live_more).setOnClickListener(this);//直播列表开放
        v.findViewById(R.id.txt_solution_more).setOnClickListener(this);//方案包开放
        //
        mImgQeustionReply.setOnClickListener(this);
        mImgHR.setOnClickListener(this);
        mImgOrganizationjoin.setOnClickListener(this);
        author.setOnClickListener(this);
        mRLMsg.setOnClickListener(this);
        //
//        setImgLayoutparams();
        setContentBottomParams();
//        setImgCalendar();
        setBanner(v);
//        setManagerTagsData();
        initSolutionView();
        //
        getMenuTab();
        //
        getPostSelection();
        getBannerData();
        getContent();
        getMsgCount();
        //getMsgList();
//        getManagerTagsData();
        if (null != mContext.con) {
            mContext.con.setInternetChanged(this);
        }

    }


    /**
     * 获取消息数
     */
    private void getMsgCount() {

        OKHttpHomeImpl.getInstance().getMsgCount(new RequestCallback<MsgCountEntityDTO>() {

            @Override
            public void onError(int errorCode, String errorMsg) {
                if (errorCode == NetResultCode.SERVER_NO_DATA) {
                    Trace.i("msg count no data!!!");

                    mTxtMsgCount.setVisibility(View.GONE);
                }
            }

            @Override
            public void onSuccess(MsgCountEntityDTO response) {
                Trace.i("msg count update successful!!!");
                MsgCountEntity msgCountEntity = response.result;
                if (null == msgCountEntity) {
                    mTxtMsgCount.setVisibility(View.GONE);
                    return;
                }
                if (null != response.result) {
                    if (msgCountEntity.message_type_id_0 == 0) {
                        mTxtMsgCount.setVisibility(View.GONE);

                    } else if (msgCountEntity.message_type_id_0 > 0 && msgCountEntity.message_type_id_0 < 100) {
                        mTxtMsgCount.setVisibility(View.VISIBLE);
                        mTxtMsgCount.setText(String.valueOf(msgCountEntity.message_type_id_0));
                    } else if (msgCountEntity.message_type_id_0 > 99) {
                        mTxtMsgCount.setVisibility(View.VISIBLE);
                        mTxtMsgCount.setText("99+");
                    }
                }
                //设置消息数

            }
        });

    }

    /**
     * 设置底部图片参数
     */
    private void setContentBottomParams() {
        int width = ScreenWindowUtil.getScreenWidth(mContext) / 2 - DensityUtil.dip2px(mContext, 40);
        ViewGroup.LayoutParams params = mImgQeustionReply.getLayoutParams();
        //params.width=width;
        params.width = ScreenWindowUtil.getScreenWidth(mContext) / 2 - DensityUtil.dip2px(mContext, 16);
        params.height = (int) (2 / 3f * width);
        //
        mImgQeustionReply.setLayoutParams(params);
        mImgHR.setLayoutParams(params);
        mImgOrganizationjoin.setLayoutParams(params);
        author.setLayoutParams(params);
    }

    private String getVersionName() throws Exception {
        // 获取packagemanager的实例
        PackageManager packageManager = mContext.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
        String version = packInfo.versionName;
        return version;
    }


    /**
     * 初始化Banner
     *
     * @param v
     */
    private void setBanner(View v) {
        onViewPagerClickListener = this;
        mBanner = (CommonBanner) v.findViewById(R.id.banner_know);
        // mLvArticle = (CommonListView) v.findViewById(R.id.lv_managers);
        int width = ScreenWindowUtil.getScreenWidth(mContext);

        //
        ViewGroup.LayoutParams layoutParams = mBanner.getLayoutParams();
        layoutParams.height = width * 166 / 375;
        mBanner.setLayoutParams(layoutParams);
    }


    /**
     * 获取首页轮播
     */
    private void getBannerData() {
        try {
            version = getVersionName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        OKHttpHomeImpl.getInstance().getHomeBanner(version, new RequestCallback<HomeBannerDTO>() {

            @Override
            public void onError(int errorCode, String errorMsg) {
                mBanner.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(HomeBannerDTO response) {

                mHomeBannerDTO = response;
                mLists.clear();
                for (int i = 0; i < response.size; i++) {
                    mLists.add(response.list.get(i).poster);
                }
                mBanner.setData(mLists, onViewPagerClickListener);
            }
        });
    }


    /**
     * 初始化方案包
     */
    private void initSolutionView() {

        mFancyCoverFlow.setUnselectedAlpha(0.5f);
        mFancyCoverFlow.setUnselectedSaturation(0.0f);
        mFancyCoverFlow.setUnselectedScale(0.6f);
        mFancyCoverFlow.setSpacing(-35);
        mFancyCoverFlow.setMaxRotation(0);
        mFancyCoverFlow.setScaleDownGravity(0.6f);
        mFancyCoverFlow.setActionDistance(FancyCoverFlow.ACTION_DISTANCE_AUTO);
        //
        mHomeSolutionPackageAdapter = new HomeSolutionPackageAdapter(mContext);
        mFancyCoverFlow.setAdapter(mHomeSolutionPackageAdapter);
        int height = (int) (ScreenWindowUtil.getScreenWidth(mContext) / 2 * 114 / 95f);
        mFancyCoverFlow.setLayoutParams(new LinearLayout.LayoutParams(ScreenWindowUtil.getScreenWidth(mContext),
                height));
    }

    /**
     * 获取精选帖子列表
     */
    private void getPostSelection() {
        String page = "0";
        String paseSize = "3";
        String top = "1";
        OKHttpNewSocialCircle.getInstance().getCircleBarList(page, paseSize, top, new RequestCallback<PostDetailListDTO>() {

            @Override
            public void onError(int errorCode, String errorMsg) {

            }

            @Override
            public void onSuccess(PostDetailListDTO response) {

                if (response.list.size() == 0) {
                    //做相应的处理
                    mLLContentPostSelection.setVisibility(View.GONE);

                } else {
                    mLLContentPostSelection.setVisibility(View.VISIBLE);
                    mListPostSelection.clear();
                    mListPostSelection.addAll(response.list);
                    fillPostSelection(response.list);
                }
            }
        });
    }


    /**
     * 填充精选帖子
     */

    private void fillPostSelection(List<PostDetailEntity> lists) {
        ArrayList<PostDetailEntity> postS = new ArrayList<PostDetailEntity>();
        mLLPostSelection.removeAllViews();
        postS.addAll(lists);

        for (int i = 0; i < postS.size(); i++) {
            PostDetailEntity postDetailEntity = postS.get(i);
            addPostView(postDetailEntity);
        }
    }

    /**
     * 修改点赞状态
     *
     * @param circleId   圈子
     * @param isFavorite
     */
    private void updatePostLikeStatus(String circleId, boolean isFavorite, ImageView imgApproval, TextView tvApprovalCount) {
        for (int i = 0; i < mListPostSelection.size(); i++) {
            PostDetailEntity postDetailEntity = mListPostSelection.get(i);
            int count = Integer.parseInt(postDetailEntity.approvalcount);
            if (postDetailEntity.id == circleId) {
                postDetailEntity.is_approval = isFavorite;
                postDetailEntity.approvalcount = isFavorite ? String.valueOf(count + 1)
                        : String.valueOf(count - 1);
                if (Integer.parseInt(postDetailEntity.commentcount) > 0) {
                    tvApprovalCount.setText(postDetailEntity.commentcount);
                } else {
                    tvApprovalCount.setText("评论");
                }
                if (postDetailEntity.is_approval) {
                    tvApprovalCount.setTextColor(Color.parseColor("#FFC84A"));
                    imgApproval.setBackgroundResource(R.drawable.icon_home_post_selected);
                } else {
                    tvApprovalCount.setTextColor(Color.parseColor("#AEBDCD"));
                    imgApproval.setBackgroundResource(R.drawable.icon_home_post_normal);
                }
                break;
            }
        }
    }

    /**
     * 修改点赞状态
     *
     * @param circleId   圈子
     * @param isFavorite
     */

    private void updatePostNormalStatus(String circleId, boolean isFavorite, ImageView imgApproval, TextView tvApprovalCount) {
        for (int i = 0; i < mListPostNormal.size(); i++) {
            PostDetailEntity postDetailEntity = mListPostNormal.get(i);
            int count = Integer.parseInt(postDetailEntity.approvalcount);
            if (postDetailEntity.id == circleId) {
                postDetailEntity.is_approval = isFavorite;
                postDetailEntity.approvalcount = isFavorite ? String.valueOf(count + 1)
                        : String.valueOf(count - 1);
                if (Integer.parseInt(postDetailEntity.commentcount) > 0) {
                    tvApprovalCount.setText(postDetailEntity.commentcount);
                } else {
                    tvApprovalCount.setText("赞");
                }
                if (postDetailEntity.is_approval) {
                    tvApprovalCount.setTextColor(Color.parseColor("#FFC84A"));
                    imgApproval.setBackgroundResource(R.drawable.icon_home_post_selected);
                } else {
                    tvApprovalCount.setTextColor(Color.parseColor("#AEBDCD"));
                    imgApproval.setBackgroundResource(R.drawable.icon_home_post_normal);
                }
                break;
            }
        }


    }

    /**
     * 精选贴子视图
     * @param postDetailEntity
     */
    private void addPostView(final PostDetailEntity postDetailEntity) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_home_post_selection, null);
        //裙子
        TextView mTxtTag = (TextView) view.findViewById(R.id.txt_tag);
        //帖子标题
        TextView mTxtPostTitle = (TextView) view.findViewById(R.id.txt_title);
        TextView txtPostDesc = (TextView) view.findViewById(R.id.txt_post_desc);
        RoundedImageView roundedImageView = (RoundedImageView) view.findViewById(R.id.img_post);
        CircleImageView circleImageView = (CircleImageView) view.findViewById(R.id.img_post_user);
        TextView tvNickame = (TextView) view.findViewById(R.id.tv_post_user_nickname);
        //帖子点赞
        LinearLayout llPostLike = (LinearLayout) view.findViewById(R.id.ll_post_like);
        final ImageView imgPostLike = (ImageView) view.findViewById(R.id.img_post_like);
        final TextView txtLikeCount = (TextView) view.findViewById(R.id.txt_post_count);
        //评论数
        TextView txtCommandCount = (TextView) view.findViewById(R.id.txt_msg_count);

        mTxtTag.setText(postDetailEntity.circle_name);
        mTxtPostTitle.setText("       " + postDetailEntity.title);
        txtPostDesc.setText(postDetailEntity.description);

        if (Integer.parseInt(postDetailEntity.approvalcount) == 0) {
            txtLikeCount.setText("赞");

        } else {
            txtLikeCount.setText(StringUtils.tranNum(postDetailEntity.approvalcount));

        }

        if (Integer.parseInt(postDetailEntity.commentcount) == 0) {
            txtCommandCount.setText("评论");
        } else {
            txtCommandCount.setText(StringUtils.tranNum(postDetailEntity.commentcount));
        }


        if (postDetailEntity.is_anonymous) {
            tvNickame.setText("匿名");
        } else {
            tvNickame.setText(postDetailEntity.nickname);
        }

        String url = postDetailEntity.posters.size() > 0 ? postDetailEntity.posters.get(0) : "";
        ImageLoader.getInstance().displayImage(url, roundedImageView, ImageLoaderOptions.option(R.drawable.img_loading_default));
        if (!TextUtils.isEmpty(url)) {
            roundedImageView.setVisibility(View.VISIBLE);
            //有图正文显示3行，无图显示5行
            txtPostDesc.setMaxLines(3);
        } else {
            roundedImageView.setVisibility(View.GONE);
            txtPostDesc.setMaxLines(5);
        }
        //图片适配
        ViewGroup.LayoutParams params = roundedImageView.getLayoutParams();
        int width = (int) ((ScreenWindowUtil.getScreenWidth(mContext) - mContext.getResources().getDimension(R.dimen.dimen_40px) * 2));
        //int height = (int) (((ScreenWindowUtil.getScreenWidth(mContext) - -mContext.getResources().getDimension(R.dimen.dimen_40px) * 2) * 9 / 16));
        int height = ScreenWindowUtil.getScreenWidth(mContext)  * 9 / 16;

        params.width = width;
        params.height = height;
        roundedImageView.setLayoutParams(params);
        //添加视图
        mLLPostSelection.addView(view);
        if (postDetailEntity.is_approval) {
            txtLikeCount.setTextColor(Color.parseColor("#FFC84A"));
            imgPostLike.setBackgroundResource(R.drawable.icon_home_post_selected);
        } else {
            txtLikeCount.setTextColor(Color.parseColor("#AEBDCD"));
            imgPostLike.setBackgroundResource(R.drawable.icon_home_post_normal);
        }

        ImageLoader.getInstance().displayImage(postDetailEntity.avatar, circleImageView
                , ImageLoaderOptions.option(R.drawable.pic_default_avatar));

        //点赞
        llPostLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postDetailEntity.is_approval) {
                    //取消点赞
                    senderPostDetailCancelApproval(postDetailEntity);
                    updatePostLikeStatus(postDetailEntity.id, false, imgPostLike, txtLikeCount);
                } else {
                    //点赞
                    senderPostDetailApproval(postDetailEntity);
                    updatePostLikeStatus(postDetailEntity.id, true, imgPostLike, txtLikeCount);
                }
                fillPostSelection(mListPostSelection);
            }
        });

        //跳转到贴子详情
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PostDetailActivity.class);
                intent.putExtra(Constants.POST_DETAIL_ID, postDetailEntity.id);
                intent.putExtra(PostDetailActivity.PARAMS_IS_FROM_HOME,true);
                startActivity(intent);
            }
        });
    }

    /**
     * 设置办学标签适配器
     */
    private void setManagerTagsData() {
        mLvArticleAdapter = new HomeManagerTagsAdapter<>(mContext);
//        mLvArticle.setAdapter(mLvArticleAdapter);
        mLvArticleAdapter.setIManagersClickListener(this);
    }


    /**
     * 获取首页办学标签列表
     */
    private void getManagerTagsData() {
        OKHttpHomeImpl.getInstance().getHomeManagerTags(new RequestCallback<HomeManagersListDTO>() {
            @Override
            public void onError(int errorCode, String errorMsg) {
            }

            @Override
            public void onSuccess(HomeManagersListDTO response) {
                mLvArticleAdapter.cleanList();
                mLvArticleAdapter.modifyList(response.list);
            }
        });
    }

    /**
     * 设置单项历尺寸
     */
    private void setImgLayoutparams() {
        int width = ScreenWindowUtil.getScreenWidth(mContext);
        ViewGroup.LayoutParams params = mImgCalendar.getLayoutParams();
        params.width = width;
        params.height = (int) ((110.0 / 375.0) * width);
        mImgCalendar.setLayoutParams(params);
        mImgCalendar.setBackgroundResource(R.drawable.shi_calendar_def);
    }


    /**
     * 加载更多
     */
    LoadMoreHandler loadMoreHandler = new LoadMoreHandler(){
        @Override
        public void onLoadMore(LoadMoreContainer loadMoreContainer) {
            if (isMaxPage) {
                mPtrClassicFrameLayout.refreshComplete();//加载完毕
            } else {
                postPage++;
                loadNormalPost(postPage);
            }
        }
    };


    /**
     * 刷新监听
     */
    PtrDefaultHandler ptrDefaultHandler = new PtrDefaultHandler() {
   /*     @Override
        public void onLoadMoreBegin(PtrFrameLayout frame) {
            if (isMaxPage) {
                mPtrClassicFrameLayout.refreshComplete();//加载完毕
            } else {
                postPage++;
                loadNormalPost(postPage);
            }
        }*/

        @Override
        public void onRefreshBegin(PtrFrameLayout frame) {
//            setImgCalendar();
            getMenuTab();
            getPostSelection();
            getBannerData();
            getContent();
            getMsgCount();
            //getMsgList();
//            getManagerTagsData();

            //刷新普通帖子 //
            postPage = 0;
            loadPostsHot();
            //loadNormalPost(postPage);
        }
    };


    /**
     * 获取首页快捷入口
     */
    private void getMenuTab() {

        String meunSize = "8";

        OKHttpHomeImpl.getInstance().getMenuTab(meunSize, new RequestCallback<CircleListDTO>() {

            @Override
            public void onError(int errorCode, String errorMsg) {

            }

            @Override
            public void onSuccess(CircleListDTO response) {
                if (null == response || response.list.size() == 0) {
                    mLLMenuTab.setVisibility(View.GONE);
                    return;
                } else {
                    mLLMenuTab.setVisibility(View.VISIBLE);
                }
                mLLMenuTab.removeAllViews();
                fillMenuTab(response.list);
            }
        });
    }


    private void fillMenuTab(ArrayList<CircleEntity> list) {

        for (int i = 0; i < list.size(); i++) {
            //如果是第四个就换行
            if (i % 4 == 0) {
                LinearLayout ll = new LinearLayout(mContext);
                ll.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                        , ViewGroup.LayoutParams.WRAP_CONTENT));
                int leftPadding = DensityUtil.dip2px(mContext, 10);
                ll.setPadding(0, 0, 0, leftPadding);
                ll.setGravity(Gravity.CENTER);
                ll.setOrientation(LinearLayout.HORIZONTAL);
                mLLMenuTab.addView(ll);
            }
            CircleEntity circleEntity = list.get(i);
            LinearLayout linearLayout = (LinearLayout) mLLMenuTab.getChildAt(mLLMenuTab.getChildCount() - 1);
            linearLayout.addView(getTabView(circleEntity));
        }

    }

    private LinearLayout getTabView(CircleEntity circleEntity) {
        final String circleId = circleEntity.id;
        String url = circleEntity.poster;
        final String name = circleEntity.name;
        final String notice = circleEntity.notice;
        LinearLayout linearLayout = new LinearLayout(mContext);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(lp);
        linearLayout.setGravity(Gravity.CENTER);
        //
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_home_tab, null);
        linearLayout.addView(view);

        TextView txtName = (TextView) view.findViewById(R.id.txt_name);
        CircleImageView circleImageView = (CircleImageView) view.findViewById(R.id.img);
        //
        ImageLoader.getInstance().displayImage(url, circleImageView
                , ImageLoaderOptions.option(R.drawable.img_default_course_system));
        txtName.setText(name);
        linearLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, CircleDetailActivity.class);
                intent.putExtra(CircleDetailActivity.PARAMS_CIRCLE_ID, circleId);
                intent.putExtra(CircleDetailActivity.PARAMS_CIRLCE_TITLE, name);
                intent.putExtra(CircleDetailActivity.PARAMS_CIRLCE_NOTICE, notice);
                startActivity(intent);
            }
        });
        return linearLayout;
    }

    /**
     * 获取内容区域
     */
    private void getContent() {

        OKHttpHomeImpl.getInstance().getHomeContent(new RequestCallback<HomeContentDTO>() {

            @Override
            public void onError(int errorCode, String errorMsg) {
                mPtrClassicFrameLayout.refreshComplete();
                //加载普通帖子
                loadNormalPost(postPage);
            }

            @Override
            public void onSuccess(HomeContentDTO response) {
                mPtrClassicFrameLayout.refreshComplete();

                fillContentSolution(response.solution);
                fillLive(response.live);
                //fillContentTitle(response.question);
//                fillContentArticle(response.article);
//                fillContentVideo(response.video);
                fillNewContentVideo(response.video);
                //加载热门帖子
                loadPostsHot();
            }
        });
    }


    /**
     * 加载热门帖子
     * <p>
     * note 加载热门帖子成功后再加载图片帖子
     * <p>
     * 固定page=0页，pageSize大小为8个
     */
    private void loadPostsHot() {
        String page = "0";
        String pageSize = "8";

        OKHttpHomeImpl.getInstance().getHomePostsHot(page, pageSize, new RequestCallback<HomePostsHotDTO>() {
            @Override
            public void onError(int errorCode, String errorMsg) {
                //加载普通帖子
                loadNormalPost(postPage);

            }

            @Override
            public void onSuccess(HomePostsHotDTO response) {
                mHomePostHotDTO = response;
                //加载普通帖子
                loadNormalPost(postPage);

            }
        });

    }


    /**
     * 加载普遍帖子
     *
     * @param page
     */
    private void loadNormalPost(final int page) {

        int pageSize = 20;

        OKHttpNewSocialCircle.getInstance().getCircleBarList(null, page + "", pageSize + "", null
                , new RequestCallback<PostDetailListDTO>() {


            @Override
            public void onError(int errorCode, String errorMsg) {
                mPtrClassicFrameLayout.refreshComplete();
            }

            @Override
            public void onSuccess(PostDetailListDTO response) {
                if (page == 0) {
                    //清空前4条
                    //清空后面加载更多的帖子
                    homePostNormalAdapter.cleanList();

                    if (response.list.size() > 5) {
                        List<PostDetailEntity> tempPosts = new ArrayList<PostDetailEntity>();
                        for (int i = 0; i < 5; i++) {
                            tempPosts.add(response.list.get(i));
                        }
                        mListPostNormal.clear();
                        mListPostNormal.addAll(tempPosts);
                        fillReadMoreFourHeardPost(tempPosts);
                        //

                        List<PostDetailEntity> tempMorePosts = new ArrayList<PostDetailEntity>();
                        for (int i = 5; i < response.list.size(); i++) {
                            tempMorePosts.add(response.list.get(i));
                        }
                        homePostNormalAdapter.modifyList(tempMorePosts);

                    } else {
                        fillReadMoreFourHeardPost(response.list);
                    }
                } else {
                    homePostNormalAdapter.modifyList(response.list);
                }

           /*     if (response.list.size() < pageSize) {
                    isMaxPage = true;
                }*/
                mPtrClassicFrameLayout.refreshComplete();

            }
        });

    }

    /**
     * 填充热门帖子
     *
     * @param homePostsHotDto
     * @return
     */
    private LinearLayout fillPostsHot(HomePostsHotDTO homePostsHotDto) {
        //当无热门贴不添加
        if (null == homePostsHotDto || homePostsHotDto.list.size() == 0) {
            return new LinearLayout(mContext);
        }
        LinearLayout llPostHot = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.component_home_posts_hot, null);
        final MyHorizontalScrollView horizontliualScrollView = (MyHorizontalScrollView) llPostHot.findViewById(R.id.scrollView);
        final int offsetX=horizontliualScrollView.getOffsetX();
        final int offsetY=horizontliualScrollView.getOffsetY();

        //
        TextView mTxtTag = (TextView) llPostHot.findViewById(R.id.txt_tag);
        TextView mTxtTitle = (TextView) llPostHot.findViewById(R.id.txt_title);
        //
        mTxtTag.setText(homePostsHotDto.billboard.catagory);
        mTxtTitle.setText(" | " + homePostsHotDto.billboard.boardtitle);
        //
        LinearLayout llContent = (LinearLayout) llPostHot.findViewById(R.id.ll_posts);
        List<PostDetailEntity> posts = homePostsHotDto.list;
        for (int i = 0; i < posts.size(); i++) {
            PostDetailEntity postDetailEntity = posts.get(i);
            llContent.addView(fillPostsHotItem(postDetailEntity));
        }


        return llPostHot;
    }


    /**
     * 热门帖子子视图
     * @param postDetailEntity
     * @return
     */
    private View fillPostsHotItem(final PostDetailEntity postDetailEntity) {

        LinearLayout llPostHot = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.item_home_posts_hot, null);
        //
        RoundedImageView roundedImg = (RoundedImageView) llPostHot.findViewById(R.id.img_post);
        TextView txtTitle = (TextView) llPostHot.findViewById(R.id.txt_posts_title);
        CircleImageView cirlceImgUer = (CircleImageView) llPostHot.findViewById(R.id.img_post_user);
        TextView tvNickame = (TextView) llPostHot.findViewById(R.id.tv_post_user_nickname);
        //帖子点赞
        LinearLayout llPostLike = (LinearLayout) llPostHot.findViewById(R.id.ll_post_like);
        final ImageView imgPostLike = (ImageView) llPostHot.findViewById(R.id.img_post_like);
        final TextView txtLikeCount = (TextView) llPostHot.findViewById(R.id.txt_post_count);
        //评论数
        TextView txtCommandCount = (TextView) llPostHot.findViewById(R.id.txt_msg_count);

        tvNickame.setText(postDetailEntity.nickname);
        txtTitle.setText("" + postDetailEntity.title);

        ImageLoader.getInstance().displayImage(postDetailEntity.avatar, cirlceImgUer,
                ImageLoaderOptions.option(R.drawable.pic_default_avatar));
        //
        String url = postDetailEntity.posters.size() > 0 ? postDetailEntity.posters.get(0) : "";
        ImageLoader.getInstance().displayImage(url, roundedImg, ImageLoaderOptions.option(R.drawable.img_loading_default));

        if (Integer.valueOf(postDetailEntity.approvalcount) == 0) {
            txtLikeCount.setText("赞");
        } else {
            txtLikeCount.setText(StringUtils.tranNum(postDetailEntity.approvalcount));

        }
        if (Integer.valueOf(postDetailEntity.commentcount) == 0) {
            txtCommandCount.setText("评论");
        } else {
            txtCommandCount.setText(postDetailEntity.commentcount);

        }

        //图片适配
        int width = (int) ((ScreenWindowUtil.getScreenWidth(mContext) - mContext.getResources().getDimension(R.dimen.dimen_40px) * 2));
        int height = ScreenWindowUtil.getScreenWidth(mContext) * 9 / 16;
        int roundedImgWidth = (int) (width - mContext.getResources().getDimension(R.dimen.common_padding));
        roundedImg.setLayoutParams(new LinearLayout.LayoutParams(roundedImgWidth, height));
        llPostHot.setLayoutParams(new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT));

        llPostHot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, PostDetailActivity.class);
                intent.putExtra(Constants.POST_DETAIL_ID, postDetailEntity.id);
                intent.putExtra(PostDetailActivity.PARAMS_IS_FROM_HOME,true);
                startActivity(intent);

            }
        });
        return llPostHot;
    }


    /**
     * 填出直播数据
     *
     * @param liveEntity
     */
    private void fillLive(HomeLiveEntity liveEntity) {
        if (null == liveEntity) {
            mLLLiveForeshow.setVisibility(View.GONE);
            return;
        } else {
            mLLLiveForeshow.setVisibility(View.VISIBLE);
        }

        final String liveId = liveEntity.id;
        final String liveTitle = liveEntity.title;
//        if (liveEntity.status == LiveEntity.LiveStatus.LIVING) {
//            mImgLiveStatusTag.setBackgroundResource(R.drawable.icon_home_live_tag_living);
//        } else if (liveEntity.status == LiveEntity.LiveStatus.NO_START) {
//            mImgLiveStatusTag.setBackgroundResource(R.drawable.icon_home_live_tag_nostart);
//        } else if (liveEntity.status == LiveEntity.LiveStatus.END) {
//            mImgLiveStatusTag.setBackgroundResource(R.drawable.icon_home_live_tag_end);
//        }
        //
        ViewGroup.LayoutParams params = mImgLiveForeshow.getLayoutParams();
        params.height = ScreenWindowUtil.getScreenWidth(mContext) * 9 / 16;
        mImgLiveForeshow.setLayoutParams(params);
        //为保证分享,下载一个直播详情图
        ImageLoader.getInstance().displayImage(liveEntity.poster, mImgEmpty,
                ImageLoaderOptions.option(R.drawable.img_default_course_system));

        Glide.with(mContext).load(liveEntity.poster).error(R.drawable.img_default_course_suject).into(mImgLiveForeshow);

        mTxtLiveTtile.setText("         " + liveEntity.title);
        mTxtLiveTime.setText("时间: " + DateUtils.getFormatData(liveEntity.start_time * 1000L, "MM-dd HH:mm"));
        mtxtLiveSpeaker.setText("主讲人: " + liveEntity.speaker);
        mRLLive.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                StatisticsUtil.getInstance(mContext).onCountActionDot(ISavePointRecordValue.HOME_RECOMMEND_LIVE);
                Intent intent = new Intent();
                intent.putExtra(LiveDetailsActivity.PARAMS_LIVE_ID, liveId);
                intent.putExtra(LiveDetailsActivity.PARAMS_LIVE_TITLE, liveTitle);
                intent.setClass(mContext, LiveDetailsActivity.class);
                startActivity(intent);
            }
        });
    }


    /**
     * 填充问答数据
     */
//    private void fillContentTitle(List<HomeQuestionEntity> questionEntityList) {
//        if (questionEntityList.size() <= 0) {
//            mLLContentQuestion.setVisibility(View.GONE);
//            return;
//        } else {
//            mLLContentQuestion.setVisibility(View.VISIBLE);
////            mTxtContentQuestionTitle.setText(homeQuestionEntity.content);
//
//        }
//
///*        final String questionId = homeQuestionEntity.id;
//        mLLContentQuestion.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                StatisticsUtil.getInstance(mContext).onCountActionDot(ISavePointRecordValue.HOME_RECOMMEND_QUESTION);
//                Intent intent = new Intent(mContext, QuestionDetailActivity.class);
//                intent.putExtra(Constants.QUESTION_ID, questionId);
//                startActivity(intent);
//            }
//        });*/
//        mLLQuestionAskEnter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, AskActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        List<View> pageView = new ArrayList<>();
//        for (int i = 0; i < questionEntityList.size(); i++) {
//            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View questionView = layoutInflater.inflate(R.layout.component_home_question_item, null);
//            TextView tvTitle = (TextView) questionView.findViewById(R.id.tv_home_question_title);
//            TextView tvDescription = (TextView) questionView.findViewById(R.id.tv_home_question_description);
//            //            tvTitle.setText(questionEntityList.get(i).content);
//
//            if (questionEntityList.get(i).is_hot > 0) {
//                Spanny spanny = new Spanny();
//                spanny.append(" " + questionEntityList.get(i).content, new ImageSpan(mContext, R.drawable.icon_she_hot));
//                tvTitle.setText(spanny);
//            } else {
//                tvTitle.setText(questionEntityList.get(i).content);
//            }
//
//            tvDescription.setText(questionEntityList.get(i).description);
//            final String questionId = questionEntityList.get(i).id;
//            questionView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    StatisticsUtil.getInstance(mContext).onCountActionDot(ISavePointRecordValue.HOME_RECOMMEND_QUESTION);
//                    Intent intent = new Intent(mContext, QuestionDetailActivity.class);
//                    intent.putExtra(Constants.QUESTION_ID, questionId);
//                    startActivity(intent);
//                }
//            });
//            pageView.add(questionView);
//        }
//        mIvQuestionListView.setPagerViewList(pageView);
//    }
    private void fillContentArticle(final List<RealizeArticleEntity> article) {
        final List<RealizeArticleEntity> listRealize = article;
        //
        if (listRealize.size() <= 0) {
            mLLContentArticle.setVisibility(View.GONE);
        } else {
            mLLContentArticle.setVisibility(View.VISIBLE);
            mLLContentArticleBlock.removeAllViews();
        }

        if (listRealize.size() > 4) {
            mLLArticleExpend.setVisibility(View.VISIBLE);
        } else {
            mLLArticleExpend.setVisibility(View.GONE);
        }

        for (int i = 0; i < listRealize.size() / 2; i++) {
            final RealizeArticleEntity realizeArticleEntity = listRealize.get(i);
            HomeArticleItemView homeArticleItemView = new HomeArticleItemView(getActivity());
            if (i % 2 != 0) {
                homeArticleItemView.setBackgroundColor(mContext.getResources().getColor(R.color.c_gray_f3f6f7));
            }
            ArrayList<String> tags = realizeArticleEntity.tags;
            homeArticleItemView.setData(realizeArticleEntity.title, realizeArticleEntity.poster, realizeArticleEntity.publish_at, realizeArticleEntity.readcount,
                    tags.size() > 0 ? tags.get(0) : "");
            mLLContentArticleBlock.addView(homeArticleItemView);
            homeArticleItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StatisticsUtil.getInstance(mContext).onCountActionDot(ISavePointRecordValue.HOME_RECOMMND_SOURCE);

                    Intent intent = new Intent(mContext, ArticleDetailActivity.class);
                    intent.putExtra(ArticleDetailActivity.PARAMS_ARTICLE_ID, realizeArticleEntity.id);
                    startActivity(intent);
                }
            });
        }
        mTxtContentArticleMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatisticsUtil.getInstance(mContext).onCountActionDot(ISavePointRecordValue.HOME_RECOMMED_SOURCE_MORE);
                mMainActivity.setSelectPage(IHomeMenu.SOURCE);

            }
        });

        mLLArticleExpend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showArticleBySize(article, article.size() / 2);
                mLLArticleExpend.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 每天读一点的前4条
     */
    private void fillReadMoreFourHeardPost(List<PostDetailEntity> postList) {
        final List<PostDetailEntity> listPosts = postList;
        //热门帖子
        LinearLayout llHotPost=null;
        //
        if (listPosts.size() <= 0) {
            mLLContentArticle.setVisibility(View.GONE);
        } else {
            mLLContentArticle.setVisibility(View.VISIBLE);
            mLLContentArticleBlock.removeAllViews();
        }

        for (int i = 0; i < listPosts.size(); i++) {
            final PostDetailEntity postDetailEntity = listPosts.get(i);

            final HomeNormalPostItemView homeNormalPostItemView = new HomeNormalPostItemView(getActivity());
            homeNormalPostItemView.setData(postDetailEntity);
            /**
             *
             * 添加热门帖子
             * */
            if (i == 2) {
                llHotPost=fillPostsHot(mHomePostHotDTO);
                mLLContentArticleBlock.addView(llHotPost);
            }
            mLLContentArticleBlock.addView(homeNormalPostItemView);
            homeNormalPostItemView.llApprovalLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (postDetailEntity.is_approval) {
                        senderPostDetailCancelApproval(postDetailEntity);
                        //
                        updatePostNormalStatus(postDetailEntity.id, false, homeNormalPostItemView.ivApproval, homeNormalPostItemView.tvApprovalCount);
                    } else {
                        senderPostDetailApproval(postDetailEntity);
                        updatePostNormalStatus(postDetailEntity.id, true, homeNormalPostItemView.ivApproval, homeNormalPostItemView.tvApprovalCount);
                    }

                    fillReadMoreFourHeardPost(mListPostNormal);
                }
            });
            homeNormalPostItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PostDetailActivity.class);
                    intent.putExtra(Constants.POST_DETAIL_ID, postDetailEntity.id);
                    intent.putExtra(PostDetailActivity.PARAMS_IS_FROM_HOME,true);
                    startActivity(intent);
                }
            });
        }

        final MyHorizontalScrollView horizontliualScrollView = (MyHorizontalScrollView)llHotPost.findViewById(R.id.scrollView);

        mContext.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                horizontliualScrollView.scrollTo(200,0);
                horizontliualScrollView.smoothScrollTo(200,0);

            }
        });


    }

    private void showArticleBySize(List<RealizeArticleEntity> article, int size) {
        for (int i = size; i < article.size(); i++) {//待处理和分割以4个为准
            final RealizeArticleEntity realizeArticleEntity = article.get(i);
            HomeArticleItemView homeArticleItemView = new HomeArticleItemView(getActivity());
            if (i % 2 != 0) {
                homeArticleItemView.setBackgroundColor(mContext.getResources().getColor(R.color.c_gray_f3f6f7));
            }
            ArrayList<String> tags = realizeArticleEntity.tags;
            homeArticleItemView.setData(realizeArticleEntity.title, realizeArticleEntity.poster, realizeArticleEntity.publish_at, realizeArticleEntity.readcount,
                    tags.size() > 0 ? tags.get(0) : "");
            mLLContentArticleBlock.addView(homeArticleItemView);
            homeArticleItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StatisticsUtil.getInstance(mContext).onCountActionDot(ISavePointRecordValue.HOME_RECOMMND_SOURCE);

                    Intent intent = new Intent(mContext, ArticleDetailActivity.class);
                    intent.putExtra(ArticleDetailActivity.PARAMS_ARTICLE_ID, realizeArticleEntity.id);
                    startActivity(intent);
                }
            });
        }
    }


    /**
     * 填充方案包
     * TODO
     */
    private void fillContentSolution(SolutionPackageDTO solutionPackageDto) {
        if (null == solutionPackageDto) {
            llHomeSolution.setVisibility(View.GONE);
            return;
        }
        llHomeSolution.setVisibility(View.VISIBLE);

        SolutionGroup solutionGroup = solutionPackageDto.group;
        mTxtSolutionTitle.setText(solutionGroup.title);
        mTxtSolutionDesc.setText(solutionGroup.description);
        //
        if (mListSolution.size() > 0) {
            mListSolution.clear();
        }
        mHomeSolutionPackageAdapter.modifyData(solutionPackageDto.list);
        if (mListSolution.size() > 1) {
            mFancyCoverFlow.setSelection(1);
        }
        //
        mFancyCoverFlow.setOnItemClickListener(onItemClickListenerFancyCoverFlow);
    }


    AdapterView.OnItemClickListener onItemClickListenerFancyCoverFlow =
            new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    ArrayList<Solution> arrayList = mHomeSolutionPackageAdapter.getList();
                    Solution solution = arrayList.get(position);
                    Intent intent = new Intent(mContext, SolutionDetailsActivity.class);
                    intent.putExtra(SolutionDetailsActivity.PARMAS_SOLUTION_ID, solution.id);
                    intent.putExtra(SolutionDetailsActivity.PARAMS_SOLUTION_TITLE, solution.title);
                    startActivity(intent);
                }
            };


    /**
     * 填充录播视频
     *
     * @param
     */
    private void fillContentVideo(ArrayList<HomeVideoEntity> videoArr) {
        final ArrayList<HomeVideoEntity> video = videoArr;

        if (video.size() <= 0) {
            mLLContentVideo.setVisibility(View.GONE);
            return;
        } else {
            mLLContentVideoBlock.removeAllViews();
            mLLContentVideo.setVisibility(View.GONE);
        }
        //
        for (int i = 0; i < video.size(); i++) {
            HomeVideoEntity homeVideoEntities = video.get(i);

            HomeVideoItemView homeVideoItemView = new HomeVideoItemView(getActivity());
            homeVideoItemView.setData(homeVideoEntities);
            //
            if (i == video.size() - 1) {
                homeVideoItemView.hindLine();
            }
            mLLContentVideoBlock.addView(homeVideoItemView);

            final VideoEntity videoEntity = new VideoEntity();
            videoEntity.id = homeVideoEntities.id;
            videoEntity.title = homeVideoEntities.title;

            homeVideoItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StatisticsUtil.getInstance(mContext).onCountActionDot(ISavePointRecordValue.HOME_RECOMMED_VIDEO);

                    Intent intent = new Intent(mContext, VideoDetailActivity.class);
                    intent.putExtra(Constants.VIDEO_ENTITY, videoEntity);
                    startActivity(intent);
                }
            });
        }
    }


    /**
     * 视频插入每天读一点
     *
     * @param videoArr
     */
    private void fillNewContentVideo(ArrayList<HomeVideoEntity> videoArr) {
        final ArrayList<HomeVideoEntity> video = videoArr;

        if (video.size() <= 0) {
            llNewNoneVideo.setVisibility(View.GONE);
            return;
        } else {
            llNewNoneVideo.removeAllViews();
            llNewNoneVideo.setVisibility(View.VISIBLE);
        }

        if (video.size() > 0) {
            final HomeVideoEntity homeVideoEntity = video.get(0);
            View view = LayoutInflater.from(mContext).inflate(R.layout.component_new_none_video, null);
            TextView tvVideoTitle = (TextView) view.findViewById(R.id.tv_home_video_title);
            RelativeLayout rlPostLayout = (RelativeLayout) view.findViewById(R.id.rl_video_post_layout);
            ImageView ivHomePoster = (ImageView) view.findViewById(R.id.iv_home_poster);
            TextView tvHomeVideoDesc = (TextView) view.findViewById(R.id.tv_home_video_desc);
            TextView tvReadCount = (TextView) view.findViewById(R.id.tv_read_count);
            TextView tvDuration = (TextView) view.findViewById(R.id.tv_duration);
            llNewNoneVideo.addView(view);
            //比例适配
            ViewGroup.LayoutParams params = ivHomePoster.getLayoutParams();
            int width = (int) ((ScreenWindowUtil.getScreenWidth(mContext) - mContext.getResources().getDimension(R.dimen.dimen_40px) * 2));
            //int height = (int) (((ScreenWindowUtil.getScreenWidth(mContext) - mContext.getResources().getDimension(R.dimen.dimen_40px) * 2) * 9.0) / 16.0);
            int height = ScreenWindowUtil.getScreenWidth(mContext) * 9/ 16;

            params.width = width;
            params.height = height;
            ivHomePoster.setLayoutParams(params);

            final VideoEntity videoEntity = new VideoEntity();
            videoEntity.id = homeVideoEntity.id;
            videoEntity.title = homeVideoEntity.title;

            tvVideoTitle.setText("         " + homeVideoEntity.title);
            ImageLoader.getInstance().displayImage(homeVideoEntity.poster, ivHomePoster,
                    ImageLoaderOptions.optionRounded(R.drawable.img_default_course_system, 10));
            tvHomeVideoDesc.setText(homeVideoEntity.description);
            tvReadCount.setText(homeVideoEntity.readcount + "次播放");
            tvDuration.setText(homeVideoEntity.duration);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, VideoDetailActivity.class);
                    intent.putExtra(Constants.VIDEO_ENTITY, videoEntity);
                    startActivity(intent);
                }
            });
        }
    }


    /**
     * 获取消息卡片
     */
    private void getMsgList() {

        String page = String.valueOf(mCurrentPage);
        String pageSize = "8";
        OKHttpCourseImpl.getInstance().getMsgList(page, pageSize, new RequestCallback<MessageDTO>() {

            @Override
            public void onError(int errorCode, String errorMsg) {

                mPtrClassicFrameLayout.refreshComplete();
            }

            @Override
            public void onSuccess(MessageDTO response) {

                if (mCurrentPage == 0) {
                    mMsgAdapter.cleanList();
                }
                mMsgAdapter.modifyList(response.result);
                mPtrClassicFrameLayout.refreshComplete();
            }
        });
    }

    public void setImgCalendar() {

        try {
            version = getVersionName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String token = mUserInfo.token;
        mMainActivity.alertLoadingProgress();
        OkHttpILoginImpl.getInstance().knowCalendarQuery(token, version, new RequestCallback<KnowCalendarDTO>() {
            @Override
            public void onError(int errorCode, String errorMsg) {
                mMainActivity.dismissLoadingDialog();
            }

            @Override
            public void onSuccess(final KnowCalendarDTO response) {
                mKnowCalendar = response;
                ImageLoader.getInstance().displayImage(response.url, mImgCalendar,
                        ImageLoaderOptions.option(R.drawable.shi_calendar_def));

//                ImageLoader.getInstance().displayImage(response.url_share, mImgCalendarEmpty,
//                        ImageLoaderOptions.option(R.drawable.shi_calendar_def));

                mTvAgree.setVisibility(View.VISIBLE);

                ImageLoader.getInstance().loadImage(response.url_share, null);
                mTvAgree.setVisibility(View.VISIBLE);
                mImgShare.setVisibility(View.VISIBLE);
                if (response.is_liked == 0) {
                    mTvAgree.setSelected(false);
                } else {
                    mTvAgree.setSelected(true);
                }
                mTvAgree.setText(response.like_count + "");
                mMainActivity.dismissLoadingDialog();

            }

        });
    }


    /**
     * 单向历图片弹出框
     */
    private void showCalendarActionSheetDailog() {

        if (TextUtils.isEmpty(mKnowCalendar.url_share)) {
            return;
        }

        shareActionSheetDialog = new ActionSheet(getActivity());
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.component_calendar_dailog, null);
        shareActionSheetDialog.setContentView(view);
        shareActionSheetDialog.show();

        RelativeLayout llShareFriend = (RelativeLayout) view.findViewById(R.id.ll_share_friend);
        RelativeLayout llShareCircle = (RelativeLayout) view.findViewById(R.id.ll_share_circle);
        ImageView imgCalendar = (ImageView) view.findViewById(R.id.img_calendar);
        ImageView cancelButton = (ImageView) view.findViewById(R.id.img_circle_calendar);
        ImageView imgFriend = (ImageView) view.findViewById(R.id.tv_test);
        ImageView imgCircle = (ImageView) view.findViewById(R.id.tv_test_right);

        int width = (int) (ScreenWindowUtil.getScreenWidth(mContext) * 0.853);
        int height = (int) (ScreenWindowUtil.getScreenWidth(mContext) * 1.333);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
        params.width = width;
        params.height = height;
        float width_top = cancelButton.getLayoutParams().width;
        params.topMargin = (int) (width_top * 7.0 / 13.0);
        imgCalendar.setLayoutParams(params);
        int width_share = (int) (ScreenWindowUtil.getScreenWidth(mContext) * 0.219);
        int height_share = (int) (ScreenWindowUtil.getScreenWidth(mContext) * 0.24);
        RelativeLayout.LayoutParams params_share = new RelativeLayout.LayoutParams(width_share, height_share);
        params_share.bottomMargin = (int) (ScreenWindowUtil.getScreenWidth(mContext) * 0.048);
        imgFriend.setLayoutParams(params_share);
        imgCircle.setLayoutParams(params_share);

        ImageLoader.getInstance().displayImage(mKnowCalendar.url_share, (ImageView) imgCalendar,
                ImageLoaderOptions.optionRounded(R.drawable.img_calendar, 20));

        final String path = FileUtil.getImageFolder() + "/" + String.valueOf(mKnowCalendar.url_share.hashCode());//如果分享图片获取该图片的本地存储地址

        llShareFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatisticsUtil.getInstance(mContext).statisticsCalendarShareCount(mKnowCalendar.id);//单向历分享统计
                //新增埋点
                StatisticsUtil.getInstance(mContext).onCountActionDot(ISavePointRecordValue.HOME_SINGLE_CALENDAR_SHARE_FRIENDS);
                ShareUtils.getInstance(mContext).shareImage(ShareUtils.SendToPlatformType.TO_FRIEND, path, UMPlatformData.UMedia.WEIXIN_FRIENDS);
                if (shareActionSheetDialog != null) {
                    shareActionSheetDialog.dismiss();
                }
            }
        });

        llShareCircle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                StatisticsUtil.getInstance(mContext).statisticsCalendarShareCount(mKnowCalendar.id);//单向历分享统计
                //新增埋点
                StatisticsUtil.getInstance(mContext).onCountActionDot(ISavePointRecordValue.HOME_SINGLE_CALENDAR_SHARE_FRIENDS_CIRCLE);
                ShareUtils.getInstance(mContext).shareImage(ShareUtils.SendToPlatformType.TO_CIRCLE, path, UMPlatformData.UMedia.WEIXIN_CIRCLE);
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
     * 分享弹出框
     */
    private void showShareActionSheetDailog() {

//        if (mKnowCalendar.url_share == null){
//            if (mKnowCalendar.url != null) {
//                mKnowCalendar.url_share = mKnowCalendar.url;
//            }else {
//                return;
//            }
//        }

        if (TextUtils.isEmpty(mKnowCalendar.url_share)) {
            return;
        }

        final String path = FileUtil.getImageFolder() + "/" + String.valueOf(mKnowCalendar.url_share.hashCode());//如果分享图片获取该图片的本地存储地址

        shareActionSheetDialog = new ActionSheet(getActivity());
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
                StatisticsUtil.getInstance(mContext).statisticsCalendarShareCount(mKnowCalendar.id);//单向历分享统计
                //新增埋点
                StatisticsUtil.getInstance(mContext).onCountActionDot(ISavePointRecordValue.HOME_SINGLE_CALENDAR_SHARE_FRIENDS);

                ShareUtils.getInstance(mContext).shareImage(ShareUtils.SendToPlatformType.TO_FRIEND, path, UMPlatformData.UMedia.WEIXIN_FRIENDS);

                if (shareActionSheetDialog != null) {
                    shareActionSheetDialog.dismiss();
                }

            }
        });

        llShareCircle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                StatisticsUtil.getInstance(mContext).statisticsCalendarShareCount(mKnowCalendar.id);//单向历分享统计
                //新增埋点
                StatisticsUtil.getInstance(mContext).onCountActionDot(ISavePointRecordValue.HOME_SINGLE_CALENDAR_SHARE_FRIENDS_CIRCLE);

                ShareUtils.getInstance(mContext).shareImage(ShareUtils.SendToPlatformType.TO_CIRCLE, path, UMPlatformData.UMedia.WEIXIN_CIRCLE);
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
     * 广播监听
     * <p>
     */
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            Bundle bundle = intent.getExtras();
            if (action.equals(BROAD_ACTION_POSTS_UPDATE)
                    && null != bundle) {
                PostDetailEntity postDetailEntity = (PostDetailEntity) bundle.getSerializable(PostDetailActivity.POSTS_ENTITY);
                updatePosts(postDetailEntity);
            }
        }
    };

    /**
     * 更新帖子
     *
     * @param post
     */
    private void updatePosts(PostDetailEntity post) {
        //更新精选帖子
        for (int i = 0; i < mListPostSelection.size(); i++) {
            PostDetailEntity postDetailEntity = mListPostSelection.get(i);
            if (postDetailEntity.id.equals(post.id)) {
                postDetailEntity.commentcount = post.commentcount;
                postDetailEntity.is_favorite = post.is_favorite;
                postDetailEntity.approvalcount = post.approvalcount;
                postDetailEntity.is_concern = post.is_concern;
                if (post.is_approval) {
                    postDetailEntity.is_approval = true;
                } else {
                    postDetailEntity.is_approval = false;
                }

                fillPostSelection(mListPostSelection);
                Trace.i("update posts successful!!!");
                break;
            }
        }
        //查找已加载的普通帖子
        for (int i = 0; i < mListPostNormal.size(); i++) {
            PostDetailEntity postDetailEntity = mListPostNormal.get(i);
            if (postDetailEntity.id.equals(post.id)) {
                postDetailEntity.commentcount = post.commentcount;
                postDetailEntity.is_favorite = post.is_favorite;
                postDetailEntity.approvalcount = post.approvalcount;
                postDetailEntity.is_concern = post.is_concern;
                if (post.is_approval) {
                    postDetailEntity.is_approval = true;
                } else {
                    postDetailEntity.is_approval = false;
                }
                fillReadMoreFourHeardPost(mListPostNormal);
                Trace.i("update posts successful!!!");
                break;
            }
        }
        //查找热门帖子
        if (null != mHomePostHotDTO) {
            List<PostDetailEntity> postSHot = mHomePostHotDTO.list;
            for (int i = 0; i < postSHot.size(); i++) {
                PostDetailEntity postDetailEntity = postSHot.get(i);
                if (postDetailEntity.id.equals(post.id)) {
                    postDetailEntity.commentcount = post.commentcount;
                    postDetailEntity.is_favorite = post.is_favorite;
                    postDetailEntity.approvalcount = post.approvalcount;
                    postDetailEntity.is_concern = post.is_concern;
                    if (post.is_approval) {
                        postDetailEntity.is_approval = true;
                    } else {
                        postDetailEntity.is_approval = false;
                    }
                    fillReadMoreFourHeardPost(mListPostNormal);
                    Trace.i("update posts hot successful!!!");
                    break;
                }

            }
            //
            List<PostDetailEntity> lists = homePostNormalAdapter.getList();
            for (int i = 0; i < lists.size(); i++) {
                PostDetailEntity postDetailEntity = lists.get(i);
                if (postDetailEntity.id.equals(post.id)) {
                    postDetailEntity.commentcount = post.commentcount;
                    postDetailEntity.is_favorite = post.is_favorite;
                    postDetailEntity.approvalcount = post.approvalcount;
                    postDetailEntity.is_concern = post.is_concern;
                    if (post.is_approval) {
                        postDetailEntity.is_approval = true;
                    } else {
                        postDetailEntity.is_approval = false;
                    }
                    homePostNormalAdapter.notifyDataSetChanged();
                    Trace.i("update posts successful!!!");
                    break;
                }
            }
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BROAD_ACTION_POSTS_UPDATE);
        mContext.registerReceiver(broadcastReceiver, intentFilter);

    }

    @Override
    public void onResume() {
        super.onResume();

//        if (mIvQuestionListView != null) {//问答自动轮播开始
//            mIvQuestionListView.startAutoPlay();
//        }

        if (mBanner != null) {//Banner自动轮播开始
            mBanner.onActivityResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (shareActionSheetDialog != null) {
            shareActionSheetDialog.dismiss();
        }

//        if (mIvQuestionListView != null) {//问答自动轮播停止
//            mIvQuestionListView.stopAutoPlay();
//
//        }

        if (mBanner != null) { //Banner自动轮播停止
            mBanner.onActivityDestroy();
        }
    }


    @Override
    public void onDestroy() {
        if (mBanner != null) {
            mBanner.onActivityDestroy();
        }
        mContext.unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    /**
     * 设置单相历点赞状态
     */
    private void setCalendarTextStaus(int isLike, int likeCount) {

        if (isLike == 0) {
            mTvAgree.setSelected(false);
            mTvAgree.setText(String.valueOf(likeCount));
            //ToastUtil.getInstance(mContext).setContent("取消点赞成功").setDuration(Toast.LENGTH_SHORT).setShow();

        } else {
            mTvAgree.setSelected(true);
            mTvAgree.setText(String.valueOf(likeCount));
            //ToastUtil.getInstance(mContext).setContent("取消点赞成功").setDuration(Toast.LENGTH_SHORT).setShow();

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_know_agree:

                if (null == mKnowCalendar) return;

                if (mKnowCalendar.is_liked == 0) {
                    mKnowCalendar.is_liked = 1;
                    mKnowCalendar.like_count++;

                } else {
                    mKnowCalendar.is_liked = 0;
                    mKnowCalendar.like_count--;

                }
                setCalendarTextStaus(mKnowCalendar.is_liked, mKnowCalendar.like_count);
                setCanlendarIsLike(mKnowCalendar.id, mKnowCalendar.is_liked);

                StatisticsUtil.getInstance(mContext).onCountActionDot(ISavePointRecordValue.HOME_SINGLE_CALENDAR_LIKE);

                break;
            case R.id.img_know_share:
                //直接单向历分享
//                showShareActionSheetDailog();
//                break;
            case R.id.img_know_calendar:
                //弹出单向历朦层弹框
                showCalendarActionSheetDailog();
                break;
            //直播
            case R.id.rl_live:
                //mMainActivity.setSelectPage(IHomeMenu.LIVE);
                StatisticsUtil.getInstance(mContext).onCountActionDot(ISavePointRecordValue.HOME_MENU_LIVE);

                mContext.launchScreen(LiveListActivity.class);
                break;

            //社模块
            case R.id.rl_question:
                //
                StatisticsUtil.getInstance(mContext).onCountActionDot(ISavePointRecordValue.HOME_MENU_QUESTION);

                mMainActivity.setSelectPage(IHomeMenu.QUETION);
                break;

            //到教室
            case R.id.rl_class_room:
                mMainActivity.setSelectPage(IHomeMenu.CLASSS_ROOM);
                break;

            //课程
            case R.id.rl_course:
                StatisticsUtil.getInstance(mContext).onCountActionDot(ISavePointRecordValue.HOME_MENU_COURSE);

                //mMainActivity.setSelectPage(IHomeMenu.COURSE);
                Intent intentCourse = new Intent(mContext, OfflineCourseActivity.class);
                startActivity(intentCourse);
                break;
            //干货
            case R.id.ll_source:
                StatisticsUtil.getInstance(mContext).onCountActionDot(ISavePointRecordValue.HOME_MENU_SOURCE);

                mMainActivity.setSelectPage(IHomeMenu.SOURCE);
                break;
            //创业者
            case R.id.ll_entrepreneurs:
                StatisticsUtil.getInstance(mContext).onCountActionDot(ISavePointRecordValue.HOME_MENU_ENTREPRENEURS);
                //
                Intent intentEntrepreneurs = new Intent(mContext, RealizeTagsListActivity.class);
                intentEntrepreneurs.putExtra(RealizeTagsListActivity.TAG_ID, IArticleTag.RECRUIT_STUDENTS_IDS);
                intentEntrepreneurs.putExtra(RealizeTagsListActivity.TAG_INDEX, IArticleTag.RECRUIT_STUDENTS_ID);
                startActivity(intentEntrepreneurs);
                break;
            //招生
            case R.id.ll_student_join:
                StatisticsUtil.getInstance(mContext).onCountActionDot(ISavePointRecordValue.HOME_MENU_STUDENT_JOIN);
                Intent intentStudentJoin = new Intent(mContext, RealizeTagsListActivity.class);
                intentStudentJoin.putExtra(RealizeTagsListActivity.TAG_ID, IArticleTag.ENTREPRENEURS_IDS);
                intentStudentJoin.putExtra(RealizeTagsListActivity.TAG_INDEX, IArticleTag.ENTREPRENEURS_ID);
                startActivity(intentStudentJoin);
                break;
            //管理
            case R.id.ll_manage:
                StatisticsUtil.getInstance(mContext).onCountActionDot(ISavePointRecordValue.HOME_MENU_MANGE);

                Intent intentManage = new Intent(mContext, RealizeTagsListActivity.class);
                intentManage.putExtra(RealizeTagsListActivity.TAG_ID, IArticleTag.PRINCIPAL_IDS);
                intentManage.putExtra(RealizeTagsListActivity.TAG_INDEX, IArticleTag.PRINCIPAL_ID);
                startActivity(intentManage);
                break;
            //录播视屏
            case R.id.ll_video:
                StatisticsUtil.getInstance(mContext).onCountActionDot(ISavePointRecordValue.HOME_MENU_VIDEO);

                mContext.launchScreen(VideoActivity.class);

                break;
            //跳转到精选问答列表
            case R.id.img_content_bottom_ranking_question:
                mContext.launchScreen(SelectedQuestionActivity.class);

                break;
            //行政人事
            case R.id.img_content_bottom_hr:

                Intent intentHr = new Intent(mContext, RealizeTagsListActivity.class);
                intentHr.putExtra(RealizeTagsListActivity.TAG_ID, IArticleTag.HR_IDS);
                intentHr.putExtra(RealizeTagsListActivity.TAG_INDEX, IArticleTag.HR_ID);
                startActivity(intentHr);

                break;
            //机构入驻
            case R.id.img_content_bottom_organization_join:
                Intent intentOrganization = new Intent(mContext, ApplicationEnterActivity.class);
                intentOrganization.putExtra(ApplicationEnterActivity.URL, WebViewUrl.H5_REALIZE_ORIGAN_DETAILS);
                intentOrganization.putExtra(ApplicationEnterActivity.TITLE, getString(R.string.title_orz_enter));
                intentOrganization.putExtra(ApplicationEnterActivity.H5_URL, WebViewUrl.H5_SHARE_ORZ_DETAILS);
                startActivity(intentOrganization);
                break;
            //作者入驻
            case R.id.img_content_bottom_author:
                Intent intentAuthor = new Intent(mContext, ApplicationEnterActivity.class);
                intentAuthor.putExtra(ApplicationEnterActivity.URL, WebViewUrl.H5_SHARE_AUTHOR_DETAILS);
                intentAuthor.putExtra(ApplicationEnterActivity.TITLE, getString(R.string.title_author_enter));
                intentAuthor.putExtra(ApplicationEnterActivity.H5_URL, WebViewUrl.H5_SHARE_AUTHOR_DETAILS);
                startActivity(intentAuthor);
                break;
            case R.id.txt_content_live_more:
                StatisticsUtil.getInstance(mContext).onCountActionDot(ISavePointRecordValue.HOME_MENU_LIVE);
                mContext.launchScreen(LiveListActivity.class);
                break;
            case R.id.rl_content_live:
                break;
            case R.id.txt_solution_more:
                Intent intentSolution = new Intent(mContext, SolutionListActivity.class);
                startActivity(intentSolution);
                break;
            case R.id.rl_msg:
                Intent intent = new Intent(mContext, MsgActivity.class);
                startActivityForResult(intent, 0x012);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CODE_MSG) {
            //更新消息数量
            getMsgCount();
        }
    }


    /**
     * 设置单项历收藏数
     */
    private void setCanlendarIsLike(String id, int isliked) {
        OkHttpILoginImpl.getInstance().knowCalendarPrise(mUserInfo.token, id,
                String.valueOf(isliked),
                new RequestCallback<KnowCalendarPriseDTO>() {
                    @Override
                    public void onError(int errorCode, String errorMsg) {
                        Trace.i("operting faile!");
                    }

                    @Override
                    public void onSuccess(KnowCalendarPriseDTO response) {
                        Trace.i("operting success!");

                    }
                });
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        MsgEntity msgEntity = mMsgAdapter.getList().get(position);
        String msg = "" + DataGsonUitls.toJson(msgEntity.list);

        int type = msgEntity.type_id;

        Intent intent = new Intent();

        switch (type) {

            /** 新手任务*/
            case MsgEntity.IMsgType.TASK:

                break;
            /**点赞 */
            case MsgEntity.IMsgType.ANSWER_FAVOURITE:

//                 MsgAnswerFavouriteDTO msgAnswerFavouriteDTO = DataGsonUitls.format(msg,
//                         MsgAnswerFavouriteDTO.class);
//                 jumpFavoritePage(msgAnswerFavouriteDTO);
//
//                break;

                /**收藏的问题被回答 */
            case MsgEntity.IMsgType.QUESTION_FAVOURTE_REPLY:

//                 MsgQuestionFavourteReplyDTO msgQuestionFavourteReplyDTO = DataGsonUitls.format(msg, MsgQuestionFavourteReplyDTO.class);
//                intent.setClass(mContext,QuestionDetailActivity.class);
//                intent.putExtra(Constants.QUESTION_ID,msgQuestionFavourteReplyDTO.question_id);
//                startActivity(intent);

//                IntentLaunch.launch(getActivity(), AnswerCardDetailActivity.class);
//                break;
                /** 邀请被回答*/
            case MsgEntity.IMsgType.QUESTION_INVITE:

//                MsgQuestionInviteEntity msgQuestionInviteEntity = DataGsonUitls.format(msg,
//                        MsgQuestionInviteEntity.class);
//                intent.setClass(mContext,QuestionDetailActivity.class);
//                intent.putExtra(Constants.QUESTION_ID,msgQuestionInviteEntity.question_id);
//                startActivity(intent);

//                break;

                /** 提问者被回答*/
            case MsgEntity.IMsgType.QUESITON_REPLY:

//                 MsgQuestionReplyDTO msgQuestionReplyDTO = DataGsonUitls.format(msg, MsgQuestionReplyDTO.class);
//                 intent.setClass(mContext,QuestionDetailActivity.class);
//                 intent.putExtra(Constants.QUESTION_ID,msgQuestionReplyDTO.question_id);
//                 startActivity(intent);
                //

                StatisticsUtil.getInstance(mContext).onCountActionDot(ISavePointRecordValue.HOME_CARD_QUETION_DETAILS);

                IntentLaunch.launch(getActivity(), AnswerCardDetailActivity.class);

                break;

            /*** 新的活动*/
            case MsgEntity.IMsgType.ACTIVITY_NEW:
                MsgActivityNewEntity msgActivityNewEntity = DataGsonUitls.format(msg, MsgActivityNewEntity.class);
                intent.setClass(mContext, ActivityDetailActivity.class);
                intent.putExtra(ActivityDetailActivity.PARAMS_ACTIVITY_ID, msgActivityNewEntity.activity_id);
                startActivity(intent);

                break;
            /*** 报名活动提醒*/
            case MsgEntity.IMsgType.ACTIVITY_START:
                MsgActivityEntity msgActivityEntity = DataGsonUitls.format(msg, MsgActivityEntity.class);
                intent.setClass(mContext, ActivityDetailActivity.class);
                intent.putExtra(ActivityDetailActivity.PARAMS_ACTIVITY_ID, msgActivityEntity.activity_id);
                startActivity(intent);
                break;

            /*** 教室加入申请*/
            case MsgEntity.IMsgType.CLASSROOM_JOIN:

                MsgClassRoomEntity msgClassRoomEntity = DataGsonUitls.format(msg, MsgClassRoomEntity.class);
                ClassRoomListEntity classRoomListEntity = new ClassRoomListEntity();
                classRoomListEntity.roomid = msgClassRoomEntity.classroom_id;
                classRoomListEntity.hx_roomid = msgClassRoomEntity.hx_roomid;
                classRoomListEntity.name = msgClassRoomEntity.name;
                jumpClassRoom(classRoomListEntity);
                break;

            /*** 新的直播上架*/
            case MsgEntity.IMsgType.LIVE_NEW:
                StatisticsUtil.getInstance(mContext).onCountActionDot(ISavePointRecordValue.HOME_CARD_NEW_LIVE_DETAILS);

                MsgLiveNewEntity msgLiveNewEntity = DataGsonUitls.format(msg, MsgLiveNewEntity.class);
                jumpLiveDetails(msgLiveNewEntity.live_id, msgLiveNewEntity.title);
                break;

            /*** 报名的直播*/
            case MsgEntity.IMsgType.LIVE_START:
                StatisticsUtil.getInstance(mContext).onCountActionDot(ISavePointRecordValue.HOME_CARD_LIVE_TIMER_DETAILS);

                MsgLiveEntity msgLiveEntity = DataGsonUitls.format(msg, MsgLiveEntity.class);
                jumpLiveDetails(msgLiveEntity.live_id, msgLiveEntity.title);
                break;

            /*** 新建课程列表*/
            case MsgEntity.IMsgType.COLLEAGE_TABLE:
                StatisticsUtil.getInstance(mContext).onCountActionDot(ISavePointRecordValue.HOME_CARD_COURSE_MONTH_DETAILS);

                // MsgCourseListEntity msgCourseListEntity=DataGsonUitls.format(msg,MsgCourseListEntity.class);
                intent.setClass(mContext, ColleageCourseActivity.class);
                startActivity(intent);
                break;
            /** *
             * 新的课程提醒*/
            case MsgEntity.IMsgType.COLLEAGE_NEW:
                StatisticsUtil.getInstance(mContext).onCountActionDot(ISavePointRecordValue.HOME_CARD_COURSE_NEW_DETAILS);

                MsgCourseEntity msgCourseEntity = DataGsonUitls.format(msg, MsgCourseEntity.class);
                jumpCourseDetails(msgCourseEntity.business_id, msgCourseEntity.cour_id);
                break;


            /**
             * 报名的课程提醒*/
            case MsgEntity.IMsgType.COLLEAGE_START:
                StatisticsUtil.getInstance(mContext).onCountActionDot(ISavePointRecordValue.HOME_CARD_COURSE_TIMER_DETAILS);

                MsgCourseJoinEntity msgCourseJoinEntity = DataGsonUitls.format(msg, MsgCourseJoinEntity.class);
                jumpCourseDetails(msgCourseJoinEntity.business_id, msgCourseJoinEntity.cour_id);
                break;

            /**
             *系统版本更新*/
            case MsgEntity.IMsgType.SYSTEM_UPDATE:
                MsgSystemUpdate msgSystemUpdate = DataGsonUitls.format(msg, MsgSystemUpdate.class);
                String url = msgSystemUpdate.address;
                if (TextUtils.isEmpty(msgSystemUpdate.address)) {
                    return;
                }
                Intent intentSystemUdapte = new Intent(Intent.ACTION_VIEW);
                intentSystemUdapte.setData(Uri.parse(url));
                startActivity(intentSystemUdapte);
                break;

        }

    }

    @Override
    public void onClick(int cardType, String id) {
        Intent intent = new Intent();
        switch (cardType) {

            case MsgEntity.IMsgType.QUESTION_FAVOURTE_REPLY:

            case MsgEntity.IMsgType.QUESTION_INVITE:

            case MsgEntity.IMsgType.QUESITON_REPLY:
//                intent.setClass(mContext,QuestionDetailActivity.class);
//                intent.putExtra(Constants.QUESTION_ID,id);
//                startActivity(intent);
                StatisticsUtil.getInstance(mContext).onCountActionDot(ISavePointRecordValue.HOME_CARD_QUETION_DETAILS);

                IntentLaunch.launch(getActivity(), AnswerCardDetailActivity.class);
                break;

            //新活动
            case MsgEntity.IMsgType.ACTIVITY_NEW:

                intent.setClass(mContext, ActivityDetailActivity.class);
                intent.putExtra(ActivityDetailActivity.PARAMS_ACTIVITY_ID, id);
                startActivity(intent);

                break;
            case MsgEntity.IMsgType.ACTIVITY_START:

                intent.setClass(mContext, ActivityDetailActivity.class);
                intent.putExtra(ActivityDetailActivity.PARAMS_ACTIVITY_ID, id);
                startActivity(intent);
                break;

            case MsgEntity.IMsgType.LIVE_NEW:


            case MsgEntity.IMsgType.LIVE_START:

                intent.setClass(mContext, LiveDetailsActivity.class);
                intent.putExtra(LiveDetailsActivity.PARAMS_LIVE_ID, id);

                break;

            //课程列表
            case MsgEntity.IMsgType.COLLEAGE_TABLE:
                intent.setClass(mContext, ColleageCourseActivity.class);
                startActivity(intent);
                break;

        }

    }

    /**
     * 跳转到回答点赞列表
     *
     * @param
     */
    @Override
    public void jumpFavoritePage(MsgAnswerFavouriteDTO msgAnswerFavouriteDTO) {
        IntentLaunch.launch(getActivity(), AnswerCardDetailActivity.class);
    }

    @Override
    public void jumpClassRoom(ClassRoomListEntity classRoomListEntity) {
        StatisticsUtil.getInstance(mContext).onCountActionDot(ISavePointRecordValue.HOME_CARD_CALSSROOM_DETAILS);

        Intent intent = new Intent();
        intent.setClass(mContext, ClassGroupChatActivity.class);
        intent.putExtra("group", classRoomListEntity);
        startActivity(intent);

    }

    @Override
    public void jumpLiveDetails(String liveId, String liveTitle) {
        Intent intent = new Intent(mContext, LiveDetailsActivity.class);
        intent.putExtra(LiveDetailsActivity.PARAMS_LIVE_ID, liveId);
        intent.putExtra(LiveDetailsActivity.PARAMS_LIVE_TITLE, liveTitle);
        startActivity(intent);
    }

    @Override
    public void jumpCourseDetails(int type, String courseId) {

        //主题课
        if (type == ICourse.ICourseType.COURSE_TYPE_SUJECT) {
            Intent intent = new Intent(mContext, CourseDetailsActivity.class);
            intent.putExtra(CourseDetailsActivity.PARAMS_COURSE_ID, courseId);
            startActivity(intent);

        } else if (type == ICourse.ICourseType.COURSE_TYPE_SYSTEM) {
            Intent intent = new Intent(mContext, CourseSystemDetailsActivity.class);
            intent.putExtra(CourseDetailsActivity.PARAMS_COURSE_ID, courseId);
            startActivity(intent);
        }

    }


    @Override
    public void ignoreMsg(int cardType, String msgId, int ingoreType) {

        //添加埋点
        switch (cardType) {

            case MsgEntity.IMsgType.ANSWER_FAVOURITE:
            case MsgEntity.IMsgType.QUESTION_FAVOURTE_REPLY:
            case MsgEntity.IMsgType.QUESTION_INVITE:
            case MsgEntity.IMsgType.QUESITON_REPLY:
                StatisticsUtil.getInstance(mContext).onCountActionDot(ISavePointRecordValue.HOME_CARD_IGNORE_QUESTION);
                break;
            case MsgEntity.IMsgType.CLASSROOM_JOIN:
                StatisticsUtil.getInstance(mContext).onCountActionDot(ISavePointRecordValue.HOME_CARD_IGNORE_CALSSROOM);
                break;
            case MsgEntity.IMsgType.LIVE_NEW:
                StatisticsUtil.getInstance(mContext).onCountActionDot(ISavePointRecordValue.HOME_CARD_IGNOGE_LIVE_NEW);
                break;
            case MsgEntity.IMsgType.LIVE_START:
                StatisticsUtil.getInstance(mContext).onCountActionDot(ISavePointRecordValue.HOME_CARD_IGNORE_LIVE_TIMER_DETAILS);
                break;
            case MsgEntity.IMsgType.COLLEAGE_TABLE:
                StatisticsUtil.getInstance(mContext).onCountActionDot(ISavePointRecordValue.HOEM_CARD_IGNORE_COURSE_DETAILS);
                break;
            case MsgEntity.IMsgType.COLLEAGE_NEW:
                StatisticsUtil.getInstance(mContext).onCountActionDot(ISavePointRecordValue.HOEM_CARD_IGNORE_COURSE_NEW_DETAILS);
                break;

            case MsgEntity.IMsgType.COLLEAGE_START:
                StatisticsUtil.getInstance(mContext).onCountActionDot(ISavePointRecordValue.HOEM_CARD_IGNORE_COURSE_TIMER_DETAILS);
                break;
            default:
                break;
        }
        removeItemMsg(msgId);
        //隐藏
        String status = "2";
        String type = String.valueOf(ingoreType);
        //0 未读 1 已读 2 隐藏
        OKHttpCourseImpl.getInstance().ignoreMsg(msgId, status, type, new RequestCallback<ResultEntity>() {


            @Override
            public void onError(int errorCode, String errorMsg) {
                Trace.i("ignore fail!");

            }

            @Override
            public void onSuccess(ResultEntity response) {

                Trace.i("ignore successful!");
            }
        });
    }

    @Override
    public void update(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    /**
     * 移除消息卡片
     *
     * @param id
     */
    private void removeItemMsg(String id) {

        List<MsgEntity> list = mMsgAdapter.getList();
        if (null != list && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                MsgEntity msgEntity = list.get(i);
                if (id.equals(msgEntity.id)) {
                    list.remove(i);
                    mMsgAdapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    }


    /**
     * 恢复网络连接
     *
     * @param netConnection
     */
    @Override
    public void onNetChange(boolean netConnection) {
        if (netConnection) {
//            setImgCalendar();
        }
    }

    /**
     * 办学标签——跳转至文章详情页
     *
     * @param article_id
     */
    @Override
    public void jumpArticlePage(int article_id) {
        Bundle bundle = new Bundle();
        bundle.putString(ArticleDetailActivity.PARAMS_ARTICLE_ID, article_id + "");
        mContext.launchScreen(ArticleDetailActivity.class, bundle);
    }

    /**
     * 办学标签——跳转至标签三级目录——文章列表页
     *
     * @param tag_id
     * @param tag_title
     */
    @Override
    public void jumpTagsListPage(String tag_id, String tag_title) {
        Bundle bundle = new Bundle();
        bundle.putString(HomeTagsListActivity.TAG_ID, tag_id);
        bundle.putString(HomeTagsListActivity.TAG_TITLE, tag_title);
        IntentLaunch.launch(mContext, HomeTagsListActivity.class, bundle);
    }

    /**
     * ViewPager的点击事件
     *
     * @param index
     */
    @Override
    public void onClick(int index) {
        if (mHomeBannerDTO.list.get(index).content_id == null) return;
        int business_id = mHomeBannerDTO.list.get(index).business_id;
        String content_id = mHomeBannerDTO.list.get(index).content_id;
        StartActivityUtils.startHomeBannerActivity(mContext, business_id, content_id);
    }

    /**
     * 帖子的点赞
     */
    private void senderPostDetailApproval(final PostDetailEntity postDetailEntity) {
        OKHttpNewSocialCircle.getInstance().postCommentApproval(postDetailEntity.id, PostDetailEntity.BUSINESS_ID, new RequestCallback<ResultEntity>() {

            @Override
            public void onError(int errorCode, String errorMsg) {
            }

            @Override
            public void onSuccess(ResultEntity response) {

                List<PostDetailEntity> postDetailList = homePostNormalAdapter.getList();
                for (int i = 0; i < postDetailList.size(); i++) {

                    if (postDetailEntity.id.equals(postDetailList.get(i).id)) {
                        postDetailList.get(i).is_approval = true;
                        postDetailList.get(i).approvalcount = (Integer.valueOf(postDetailEntity.approvalcount) + 1) + "";
                    }
                }
                homePostNormalAdapter.notifyDataSetChanged();
                showDialog("点赞成功", R.drawable.icon_dialog_success);
            }
        });

    }

    /**
     * 帖子的点赞取消
     */
    private void senderPostDetailCancelApproval(final PostDetailEntity postDetailEntity) {
        OKHttpNewSocialCircle.getInstance().postCommentCancelApproval(postDetailEntity.id, PostDetailEntity.BUSINESS_ID, new RequestCallback<ResultEntity>() {

            @Override
            public void onError(int errorCode, String errorMsg) {
                Toast.makeText(mContext, "" + errorMsg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(ResultEntity response) {
                List<PostDetailEntity> postDetailList = homePostNormalAdapter.getList();
                for (int i = 0; i < postDetailList.size(); i++) {

                    if (postDetailEntity.id.equals(postDetailList.get(i).id)) {
                        postDetailList.get(i).is_approval = false;
                        postDetailList.get(i).approvalcount = (Integer.valueOf(postDetailEntity.approvalcount) - 1) + "";
                    }
                }
                homePostNormalAdapter.notifyDataSetChanged();
                showDialog("点赞取消", R.drawable.icon_dialog_success);
            }
        });
    }

    /**
     * 显示关注成功弹窗
     */
    private void showDialog(String msg, int drawableId) {
        MsgInfoEntity entity = new MsgInfoEntity();
        entity.drawableId = drawableId;
        entity.msgInfo = msg;
        CircleCommonDialog circleCommonDialog = CircleCommonDialog.newInstance(entity);
        circleCommonDialog.show(getFragmentManager(), "");
    }

}

