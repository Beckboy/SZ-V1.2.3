package com.junhsue.ksee;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.entity.LiveEntity;
import com.junhsue.ksee.entity.UserInfo;
import com.junhsue.ksee.entity.VideoEntity;
import com.junhsue.ksee.file.FileUtil;
import com.junhsue.ksee.net.api.OKHttpCourseImpl;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.url.WebViewUrl;
import com.junhsue.ksee.profile.UserProfileService;
import com.junhsue.ksee.utils.DensityUtil;
import com.junhsue.ksee.utils.ScreenWindowUtil;
import com.junhsue.ksee.utils.ShareUtils;
import com.junhsue.ksee.utils.StatisticsUtil;
import com.junhsue.ksee.utils.StringUtils;
import com.junhsue.ksee.video.SampleListener;
import com.junhsue.ksee.view.ActionBar;
import com.junhsue.ksee.view.ActionSheet;
import com.junhsue.ksee.view.WebPageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.umeng.analytics.social.UMPlatformData;

/**
 * 视频详情页
 * Created by Sugar on 17/8/11.
 */

public class VideoDetailActivity extends BaseActivity implements View.OnClickListener {

    private Context mContext;
    private ActionBar actionBar;
    //private WebView webVideoDetail;
    private VideoEntity videoEntity = new VideoEntity();
    private String videoId = "";
    private String videoTitle = "";
    //分享弹出框
    private ActionSheet shareActionSheetDialog;
    //
    private TextView mTxtVideoContent;
    //
    private StandardGSYVideoPlayer  mStandardGSYVideoPlayer;

    private OrientationUtils orientationUtils;

    private boolean isPlay;
    private boolean isPause;

    @Override
    protected void onReceiveArguments(Bundle bundle) {

        videoEntity = (VideoEntity) bundle.getSerializable(Constants.VIDEO_ENTITY);
        videoId = bundle.getString(Constants.VIDEO_ID);
        videoTitle = bundle.getString(Constants.VIDEO_TITLE);
    }

    @Override
    protected int setLayoutId() {
        mContext = this;
        return R.layout.act_video_detail;
    }


    @Override
    protected void onInitilizeView() {
        actionBar = (ActionBar) findViewById(R.id.ab_video_detail_title);
       // webVideoDetail = (WebView) findViewById(R.id.web_video_detail);
        mStandardGSYVideoPlayer=(StandardGSYVideoPlayer)findViewById(R.id.standard_gsyvideo_player);
        mTxtVideoContent=(TextView)findViewById(R.id.txt_video_content);
        initView();
        setListener();

        ViewGroup.LayoutParams params= mStandardGSYVideoPlayer.getLayoutParams();
        params.height= ScreenWindowUtil.getScreenWidth(mContext)*9/16;
        mStandardGSYVideoPlayer.setLayoutParams(params);
    }


    /**
     *初始化直播参数
     */

    private void initilizeVideo(String url,String conetent){

        mTxtVideoContent.setText(conetent);
        //增加封面
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //增加title
        mStandardGSYVideoPlayer.getTitleTextView().setVisibility(View.GONE);
        mStandardGSYVideoPlayer.getBackButton().setVisibility(View.GONE);

        //
        //外部辅助的旋转，帮助全屏
        orientationUtils = new OrientationUtils(this, mStandardGSYVideoPlayer);
        //初始化不打开外部的旋转
        orientationUtils.setEnable(false);
        //

        GSYVideoOptionBuilder gsyVideoOption = new GSYVideoOptionBuilder();
        gsyVideoOption.setThumbImageView(imageView)
                .setIsTouchWiget(true)
                .setRotateViewAuto(false)
                .setLockLand(false)
                .setShowFullAnimation(false)
                .setNeedLockFull(true)
                .setSeekRatio(1)
                .setUrl(url)
                .setCacheWithPlay(false)
                //.setVideoTitle("测试视频")
                .setStandardVideoAllCallBack(new SampleListener() {
                    @Override
                    public void onPrepared(String url, Object... objects) {
                        super.onPrepared(url, objects);
                        //开始播放了才能旋转和全屏
                        orientationUtils.setEnable(true);
                        isPlay = true;
                    }

                    @Override
                    public void onEnterFullscreen(String url, Object... objects) {
                        super.onEnterFullscreen(url, objects);
                    }

                    @Override
                    public void onAutoComplete(String url, Object... objects) {
                        super.onAutoComplete(url, objects);
                    }

                    @Override
                    public void onClickStartError(String url, Object... objects) {
                        super.onClickStartError(url, objects);
                    }

                    @Override
                    public void onQuitFullscreen(String url, Object... objects) {
                        super.onQuitFullscreen(url, objects);
                        if (orientationUtils != null) {
                            orientationUtils.backToProtVideo();
                        }
                    }
                })
                .setLockClickListener(new LockClickListener() {
                    @Override
                    public void onClick(View view, boolean lock) {
                        if (orientationUtils != null) {
                            //配合下方的onConfigurationChanged
                            orientationUtils.setEnable(!lock);
                        }
                    }
                }).build(mStandardGSYVideoPlayer);

        mStandardGSYVideoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //直接横屏 TODO
                //orientationUtils.resolveByClick();

                //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                //mStandardGSYVideoPlayer.startWindowFullscreen(mContext, true, true);
            }
        });
    }
    private void setListener() {
        actionBar.setOnClickListener(this);
    }

    private void initView() {
        String tempVideoId = "";
        if (videoEntity != null && !StringUtils.isBlank(videoEntity.id)) {
            tempVideoId = videoEntity.id;
            actionBar.setTitle(videoEntity.title);
        } else if (!StringUtils.isBlank(videoId)) {
            tempVideoId = videoId;
            actionBar.setTitle(videoTitle + "");
        }
        actionBar.setBottomDividerVisible(View.GONE);
//        String url = String.format(WebViewUrl.H5_VIDEO_DETAIL_URL, tempVideoId);
//        webVideoDetail.getSettings().setJavaScriptEnabled(true);
//        //设置编码
//        webVideoDetail.getSettings().setDefaultTextEncodingName("utf-8");
//        webVideoDetail.setWebViewClient(new MyWebViewClient());
//        webVideoDetail.loadUrl(url);

        getVedioDetails(tempVideoId);

    }


    private void getVedioDetails(String video_id) {

        OKHttpCourseImpl.getInstance().getVideoDetails(video_id,
                new RequestCallback<VideoEntity>() {

                    @Override
                    public void onError(int errorCode, String errorMsg) {
                    }

                    @Override
                    public void onSuccess(VideoEntity response) {
                        if (null == response ){
                            actionBar.setRightVisible(View.GONE);
                            return;
                        }
                        videoEntity = response;
                        actionBar.setTitle(videoEntity.title);
                        initilizeVideo(videoEntity.address,"介绍\n\n"+videoEntity.description);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_left_layout:
                finish();
                break;
            case R.id.btn_right_one://分享
                showShareActionSheetDailog();
                break;
        }

    }


    public class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            dismissLoadingDialog();
        }
    }


    /**
     * 分享弹出框
     */
    private void showShareActionSheetDailog() {
        final String path = FileUtil.getImageFolder() + "/" + videoEntity.poster.hashCode();
        ;//如果分享图片获取该图片的本地存储地址
        final String webPage = String.format(WebViewUrl.H5_VIDEO_SHARE_URL, videoEntity.id);
        final String title = videoEntity.title;
        final String desc = videoEntity.description;

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

                StatisticsUtil.getInstance(mContext).onCountActionDot("5.2.3");
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

                StatisticsUtil.getInstance(mContext).onCountActionDot("5.2.2");
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

    private GSYVideoPlayer getCurPlay() {
        if (mStandardGSYVideoPlayer.getFullWindowPlayer() != null) {
            return  mStandardGSYVideoPlayer.getFullWindowPlayer();
        }
        return mStandardGSYVideoPlayer;
    }


    @Override
    public void onBackPressed() {

        if (orientationUtils != null) {
            orientationUtils.backToProtVideo();
        }

        if (StandardGSYVideoPlayer.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }


    @Override
    protected void onPause() {
        getCurPlay().onVideoPause();
        super.onPause();
        isPause = true;
    }

    @Override
    protected void onResume() {
        StatisticsUtil.getInstance(mContext).onCountPage("1.4.4");
        getCurPlay().onVideoResume();
        super.onResume();
        isPause = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isPlay) {
            getCurPlay().release();
        }
        //GSYPreViewManager.instance().releaseMediaPlayer();
        if (orientationUtils != null)
            orientationUtils.releaseListener();
    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (isPlay && !isPause) {
            mStandardGSYVideoPlayer.onConfigurationChanged(this, newConfig, orientationUtils);
        }
    }
}
