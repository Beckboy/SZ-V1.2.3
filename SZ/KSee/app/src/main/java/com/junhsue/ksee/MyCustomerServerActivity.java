package com.junhsue.ksee;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.junhsue.ksee.net.url.WebViewUrl;
import com.junhsue.ksee.utils.StatisticsUtil;
import com.junhsue.ksee.view.ActionBar;

public class MyCustomerServerActivity extends BaseActivity implements View.OnClickListener{

  private ActionBar mActionBar;
  private WebView mWebView;

  private Context mContext;

  @Override
  protected void onReceiveArguments(Bundle bundle) {
  }



  @Override
  protected int setLayoutId() {
    mContext = this;
    return R.layout.act_my_customer_server;
  }

  @Override
  protected void onResume() {
    StatisticsUtil.getInstance(mContext).onCountPage("1.5.5");
    super.onResume();
  }

  @Override
  protected void onInitilizeView() {
    mActionBar = (ActionBar) findViewById(R.id.title_my_customer_server);
    mWebView = (WebView) findViewById(R.id.web_view_my_customer_server);
    
    mActionBar.setOnClickListener(this);

    alertLoadingProgress();
    loadArticleDetails("");
  }

  private void loadArticleDetails(String s) {
    String url = String.format(WebViewUrl.MY_CUSTOMER_SERVER);

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
