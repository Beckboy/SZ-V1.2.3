package com.junhsue.ksee;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.junhsue.ksee.file.FileUtil;
import com.junhsue.ksee.net.url.WebViewUrl;
import com.junhsue.ksee.utils.ShareUtils;
import com.junhsue.ksee.utils.StatisticsUtil;
import com.junhsue.ksee.view.ActionBar;
import com.junhsue.ksee.view.ActionSheet;
import com.junhsue.ksee.view.WebPageView;
import com.umeng.analytics.social.UMPlatformData;

/**
 * 知 —— 申请入驻
 */
public class ApplicationEnterActivity extends BaseActivity implements View.OnClickListener{

    private ActionBar mAb;
    private WebPageView mWebView;

    private Context mContext;
    private ActionSheet shareActionSheetDialog;

    private String title,url,h5_url;

    public static final String TITLE = "title";
    public static final String URL = "url";
    public static final String H5_URL = "H5_url";

    @Override
    protected void onReceiveArguments(Bundle bundle) {
        title = bundle.getString(TITLE,null);
        url = bundle.getString(URL,null);
        h5_url = bundle.getString(H5_URL,null);
    }

    @Override
    protected int setLayoutId() {
        mContext = this;
        return R.layout.act_application_enter;
    }

    @Override
    protected void onResume() {
        if (title.equals(getString(R.string.title_author_enter))) {
            StatisticsUtil.getInstance(mContext).onCountPage("1.2.1");
        }else if (title.equals(getString(R.string.title_orz_enter))){
            StatisticsUtil.getInstance(mContext).onCountPage("1.4.7");
        }
        super.onResume();
    }

    @Override
    protected void onInitilizeView() {

        mAb = (ActionBar) findViewById(R.id.title_application_enter_detail);
        mWebView = (WebPageView) findViewById(R.id.application_web_view);
        mAb.setOnClickListener(this);

        if (title != null){
            mAb.setTitle(title);
        }

        //alertLoadingProgress();
        getApplicationEnter();
    }

    private void getApplicationEnter() {
        url = url == null ? null : url;
        if (url == null) return;
//        mWebView.getSettings().setJavaScriptEnabled(true);
//        //设置编码
//        mWebView.getSettings().setDefaultTextEncodingName("utf-8");
//        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.loadUrl(url);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_left_layout:
                finish();
                break;
            case R.id.btn_right_one: //分享
                showShareActionArticleDailog();
                break;
        }
    }

    /**
     * 文章详情页的分享弹吐框
     */
    private void showShareActionArticleDailog() {
        final String path = "";//如果分享图片获取该图片的本地存储地址
        final String webPage = h5_url;
        final String title = "申请成为#钬花#驻站"+ this.title.substring(0,2) +"，与千万校长分享办学经历";
        final String desc = "在钬花教育社区，同行办学路，尽显真知灼见。";

        shareActionSheetDialog = new ActionSheet(this);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.component_share_dailog, null);
        shareActionSheetDialog.setContentView(view);
        shareActionSheetDialog.show();

        LinearLayout llShareFriend = (LinearLayout) view.findViewById(R.id.ll_share_friend);
        LinearLayout llShareCircle = (LinearLayout) view.findViewById(R.id.ll_share_circle);
        TextView cancelButton = (TextView) view.findViewById(R.id.tv_cancel);

        StatisticsUtil.getInstance(mContext).onCountActionDot("2.2.1.1");

        llShareFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtils.getInstance(mContext).sharePage(ShareUtils.SendToPlatformType.TO_FRIEND, webPage, path,
                        title, desc, UMPlatformData.UMedia.WEIXIN_FRIENDS);
                if (shareActionSheetDialog != null) {
                    shareActionSheetDialog.dismiss();
                }

                StatisticsUtil.getInstance(mContext).onCountActionDot("2.2.1.2");
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

                StatisticsUtil.getInstance(mContext).onCountActionDot("2.2.1.1");
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
