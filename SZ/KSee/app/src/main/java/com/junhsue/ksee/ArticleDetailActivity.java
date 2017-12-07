package com.junhsue.ksee;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.common.IBusinessType;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.dto.KnowCalendarPriseDTO;
import com.junhsue.ksee.entity.ArticleDetail;
import com.junhsue.ksee.entity.ResultEntity;
import com.junhsue.ksee.entity.UserInfo;
import com.junhsue.ksee.file.FileUtil;
import com.junhsue.ksee.net.api.OkHttpILoginImpl;
import com.junhsue.ksee.net.api.OkHttpSocialCircleImpl;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.url.WebViewUrl;
import com.junhsue.ksee.profile.UserProfileService;
import com.junhsue.ksee.utils.CommonUtils;
import com.junhsue.ksee.utils.InputUtil;
import com.junhsue.ksee.utils.ShareUtils;
import com.junhsue.ksee.utils.StatisticsUtil;
import com.junhsue.ksee.utils.ToastUtil;
import com.junhsue.ksee.view.ActionBar;
import com.junhsue.ksee.view.ActionSheet;
import com.junhsue.ksee.view.WebPageView;
import com.umeng.analytics.social.UMPlatformData;

/**
 * 知 —— 文章详情页
 */
public class ArticleDetailActivity extends BaseActivity implements View.OnClickListener {

    //文章id
    public final static String  PARAMS_ARTICLE_ID="params_article_id";
    private ActionBar mAb;
    private WebPageView mWebView;
    private LinearLayout mLLzan,mLLtalk,mLLspeak;
    private TextView mTvZan,mTvSend;
    private EditText mEditSpeak;
    private ImageView mImgZan;

    private ArticleDetail mArticle;
    private String mArticleId;

    private Context mContext;
    private ActionSheet shareActionSheetDialog;

    @Override
    protected void onReceiveArguments(Bundle bundle) {
        mArticleId = bundle.getString(PARAMS_ARTICLE_ID) == null ? null : bundle.getString(PARAMS_ARTICLE_ID);
    }

    @Override
    protected int setLayoutId() {
        mContext = this;
        return R.layout.act_article_detail;
    }

    @Override
    protected void onResume() {
        StatisticsUtil.getInstance(mContext).onCountPage("1.2.3");
        super.onResume();
    }

    @Override
    protected void onInitilizeView() {
        initView();

        if (mArticleId == null) return;

        //alertLoadingProgress();
        getArticleDetails();
        loadArticleDetails();
    }

    private void initView() {
        mAb = (ActionBar) findViewById(R.id.title_art_detail);
        mWebView = (WebPageView) findViewById(R.id.artice_web_page_view);
        mLLzan = (LinearLayout) findViewById(R.id.ll_art_detail_zan);
        mLLtalk = (LinearLayout) findViewById(R.id.ll_art_detail_talk);
        mLLspeak = (LinearLayout) findViewById(R.id.ll_speak);
        mTvSend = (TextView) findViewById(R.id.tv_article_send);
        mEditSpeak = (EditText) findViewById(R.id.edit_article_talk);
        mTvZan = (TextView) findViewById(R.id.tv_art_detail_zan);
        mImgZan = (ImageView) findViewById(R.id.img_art_detail_zan);
        mAb.setOnClickListener(this);
        mLLzan.setOnClickListener(this);
        mLLtalk.setOnClickListener(this);
        mTvSend.setOnClickListener(this);
        mEditSpeak.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    InputUtil.softInputFromWiddow((ArticleDetailActivity) mContext);
                }else {
                    InputUtil.hideSoftInputFromWindow((ArticleDetailActivity) mContext);
                }
            }
        });
    }

    /** 获取课程详情*/
    private void getArticleDetails(){
        OkHttpILoginImpl.getInstance().getRealizeArticleDetail(mArticleId , new RequestCallback<ArticleDetail>() {

            @Override
            public void onError(int errorCode, String errorMsg) {
                //ToastUtil.showToast(getApplicationContext(),getString(R.string.net_error_service_not_accessables));
                //dismissLoadingDialog();
            }

            @Override
            public void onSuccess(ArticleDetail response) {
                mArticle=response;
                if (null != mArticle){
                    //alertLoadingProgress();
                    setArticleTextStaus(mArticle.is_approval, mArticle.approvalcount);
                }else{
                    //dismissLoadingDialog();
                }

            }
        });
    }

    /**
     * 获取文章详情
     */
    public void loadArticleDetails() {
        String url = String.format(WebViewUrl.H5_REALIZE_ARTICLE,mArticleId);
//        mWebView.getSettings().setJavaScriptEnabled(true);
//        //设置编码
//        mWebView.getSettings().setDefaultTextEncodingName("utf-8");
//        mWebView.setWebViewClient(new MyWebViewClienlt());
//        mWebView.addJavascriptInterface(null, "jsBridge");
         mWebView.loadUrl(url);
         mWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputUtil.hideSoftInputFromWindow((Activity) mContext);
                return false;
            }
        });
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_left_layout:
                finish();
                break;
            case R.id.btn_right_one: //分享
                showShareActionArticleDailog();
                break;
            case R.id.ll_art_detail_zan:  //只允许点赞，不允许取消点赞
                if (null == mArticle) return;
                if (!mArticle.is_approval) {
                    setArticleIsApproval(mArticle.id+"");
                } else {
//                    setArticleIsDelApproval(mArticle.id+"");
                }
                break;
            case R.id.ll_art_detail_talk:
                mLLspeak.setVisibility(View.VISIBLE);
                mEditSpeak.requestFocus();
                break;
            case R.id.tv_article_send:
                toSpeak();
                mLLspeak.setVisibility(View.GONE);
                mEditSpeak.setText("");
                InputUtil.hideSoftInputFromWindow(this);
                break;
        }
    }

    /**
     * 评论
     */
    private void toSpeak() {
        UserInfo userInfo = UserProfileService.getInstance(this).getCurrentLoginedUser();
        String nickname = userInfo.nickname;
        String avatar = userInfo.avatar;
        String token = userInfo.token;
        String content = mEditSpeak.getText().toString().trim();
        String create_at = "刚刚";
        String js_string = String.format("javascript:get_appUser('%s','%s','%s','%s','%s')",nickname,avatar,content,create_at,token);
        Log.i("js",js_string);
        mWebView.loadUrl(js_string);

        StatisticsUtil.getInstance(mContext).onCountActionDot("2.1.1.2");

    }

    /**
     * 设置文章详情页点赞状态
     */
    private void setArticleTextStaus(boolean is_approval,int likeCount){
        if(!is_approval){
            mLLzan.setSelected(false);
            mImgZan.setBackgroundResource(R.drawable.zhi_article_icon_thumb_up);
        }else{
            mLLzan.setSelected(true);
            mImgZan.setBackgroundResource(R.drawable.zhi_article_icon_thumb_up_zan);
        }
        mTvZan.setText(String.valueOf(likeCount)+"人觉得有用");
    }

    /**
     * 文章详情页点赞
     **/
    private void setArticleIsApproval(String id){
        OkHttpSocialCircleImpl.getInstance().senderApproval(id, Constants.ARTICLE+"",
                new RequestCallback<ResultEntity>() {
                    @Override
                    public void onError(int errorCode, String errorMsg) {
                        Trace.i("operting faile!");
                    }
                    @Override
                    public void onSuccess(ResultEntity response) {
                        Trace.i("operting success!");
                        mArticle.is_approval = true;
                        mArticle.approvalcount++;
                        setArticleTextStaus(mArticle.is_approval, mArticle.approvalcount);
                        ToastUtil.getInstance(mContext).setContent("点赞成功").setDuration(Toast.LENGTH_SHORT).setShow();
                    }
                });
    }

    /**
     * 文章详情页点赞
     **/
    private void setArticleIsDelApproval(String id){
        OkHttpSocialCircleImpl.getInstance().senderDeleteApproval(id, Constants.ARTICLE+"",
                new RequestCallback<ResultEntity>() {
                    @Override
                    public void onError(int errorCode, String errorMsg) {
                        Trace.i("operting faile!");
                    }
                    @Override
                    public void onSuccess(ResultEntity response) {
                        Trace.i("operting success!");
                        mArticle.is_approval = false;
                        mArticle.approvalcount--;
                        setArticleTextStaus(mArticle.is_approval, mArticle.approvalcount);
                        ToastUtil.getInstance(mContext).setContent("取消点赞成功").setDuration(Toast.LENGTH_SHORT).setShow();
                    }
                });
    }

    /**
     * 文章详情页的分享弹吐框
     */
    private void showShareActionArticleDailog() {
        final String path = FileUtil.getImageFolder() + "/" + String.valueOf(mArticle.poster.hashCode());//如果分享图片获取该图片的本地存储地址
        final String webPage = String.format(WebViewUrl.H5_SHARE_REALIZE_ARTICLE, mArticleId);
        final String title = mArticle.title;
        final String desc = mArticle.description;

        shareActionSheetDialog = new ActionSheet(this);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.component_share_dailog, null);
        shareActionSheetDialog.setContentView(view);
        shareActionSheetDialog.show();

        LinearLayout llShareFriend = (LinearLayout) view.findViewById(R.id.ll_share_friend);
        LinearLayout llShareCircle = (LinearLayout) view.findViewById(R.id.ll_share_circle);
        TextView cancelButton = (TextView) view.findViewById(R.id.tv_cancel);

        StatisticsUtil.getInstance(mContext).onCountActionDot("2.1.1.1");

        llShareFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtils.getInstance(mContext).sharePage(ShareUtils.SendToPlatformType.TO_FRIEND, webPage, path,
                        title, desc, UMPlatformData.UMedia.WEIXIN_FRIENDS);
                if (shareActionSheetDialog != null) {
                    shareActionSheetDialog.dismiss();
                }

                StatisticsUtil.getInstance(mContext).onCountActionDot("2.1.1.3");
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

                StatisticsUtil.getInstance(mContext).onCountActionDot("2.1.1.1");
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

}
