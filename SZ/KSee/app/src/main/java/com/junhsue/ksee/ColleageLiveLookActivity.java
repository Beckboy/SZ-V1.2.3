package com.junhsue.ksee;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.junhsue.ksee.entity.LiveEntity;
import com.junhsue.ksee.entity.UserInfo;
import com.junhsue.ksee.file.FileUtil;
import com.junhsue.ksee.net.url.WebViewUrl;
import com.junhsue.ksee.profile.UserProfileService;
import com.junhsue.ksee.utils.ShareUtils;
import com.junhsue.ksee.utils.StatisticsUtil;
import com.junhsue.ksee.view.ActionBar;
import com.junhsue.ksee.view.ActionSheet;
import com.umeng.analytics.social.UMPlatformData;

/**
 * 直播观看页
 * Created by longer on 17/4/28.
 */

public class ColleageLiveLookActivity extends BaseActivity implements View.OnClickListener {

    //标题
    public final static String PARAMS_LIVE = "params_live";

    private ActionBar mActionBar;
    private WebView mWebView;
    //标题
    private LiveEntity mLiveEntity;
    private UserInfo userInfo;
    private Context mContext=this;
    //分享对话框
    private ActionSheet shareActionSheetDialog;

    @Override
    protected void onReceiveArguments(Bundle bundle) {
        mLiveEntity = (LiveEntity) bundle.getSerializable(PARAMS_LIVE);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.act_colleage_live_look;
    }

    @Override
    protected void onResume() {
        StatisticsUtil.getInstance(this).onCountPage("1.4.2.1");
        super.onResume();
    }

    @Override
    protected void onInitilizeView() {
        userInfo = UserProfileService.getInstance(this).getCurrentLoginedUser();
        mActionBar = (ActionBar) findViewById(R.id.action_bar);
        mWebView = (WebView) findViewById(R.id.web_view);
        //
        mActionBar.setOnClickListener(this);
        //
        if (mLiveEntity == null) {
            return;
        }
        mActionBar.setTitle(mLiveEntity.title);
        loadArticleDetails("");
    }


    /**
     * 加载直播
     */
    private void loadArticleDetails(String id) {
        if (userInfo == null) {
            return;
        }
        String url = String.format(WebViewUrl.H5_LIVE_URL, mLiveEntity.live_id, userInfo.user_id, mLiveEntity.channel_number, userInfo.nickname, mLiveEntity.hx_roomid, userInfo.avatar, mLiveEntity.title);

        mWebView.getSettings().setJavaScriptEnabled(true);
        //设置编码
        mWebView.getSettings().setDefaultTextEncodingName("utf-8");
        mWebView.setWebViewClient(new MyWebViewClient());

        mWebView.loadUrl(url);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_left_layout:
                finish();
                break;
            case R.id.btn_right_one: //分享
                showShareActionSheetDailog();
                break;


        }
    }




    /**
     * 分享弹出框
     */
    private void showShareActionSheetDailog() {
        UserInfo userInfo = UserProfileService.getInstance(this).getCurrentLoginedUser();
        final String path = FileUtil.getImageFolder() + "/" + mLiveEntity.poster.hashCode();//如果分享图片获取该图片的本地存储地址
        final String webPage = String.format(WebViewUrl.H5_SHARE_LIVE_LOOK, mLiveEntity.live_id);
        final String title = mLiveEntity.title;
        final String desc = mLiveEntity.content;

        shareActionSheetDialog = new ActionSheet(mContext);
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
                StatisticsUtil.getInstance(mContext).onCountActionDot("5.1.4");

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
                StatisticsUtil.getInstance(mContext).onCountActionDot("5.1.3");
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
        }
    }

}
