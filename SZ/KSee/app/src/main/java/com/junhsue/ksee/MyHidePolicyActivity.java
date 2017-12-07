package com.junhsue.ksee;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.junhsue.ksee.net.url.WebViewUrl;

public class MyHidePolicyActivity extends BaseActivity implements View.OnClickListener{

    private WebView mWebView;

    @Override
    protected void onReceiveArguments(Bundle bundle) {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.act_my_hide_policy;
    }

    @Override
    protected void onInitilizeView() {
        findViewById(R.id.title_my_hide_policy).setOnClickListener(this);
        mWebView = (WebView) findViewById(R.id.web_view_my_hide_policy);

        alertLoadingProgress();
        loadArticleDetails("");
    }

    private void loadArticleDetails(String s) {
        String url = String.format(WebViewUrl.MY_HIDE_POLICY);

        mWebView.getSettings().setJavaScriptEnabled(true);
        //设置编码
        mWebView.getSettings().setDefaultTextEncodingName("utf-8");
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.loadUrl(url);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_left_layout:
                finish();
                break;
        }
    }
}
