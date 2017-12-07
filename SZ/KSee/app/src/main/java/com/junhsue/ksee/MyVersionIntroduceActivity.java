package com.junhsue.ksee;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.junhsue.ksee.dto.VersionUpdateDTO;
import com.junhsue.ksee.net.api.OkHttpILoginImpl;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.url.WebViewUrl;
import com.junhsue.ksee.utils.JsBridge;

public class MyVersionIntroduceActivity extends BaseActivity implements View.OnClickListener{

    private WebView mWebView;
    private VersionUpdateDTO isNewVersion;

    @Override
    protected void onReceiveArguments(Bundle bundle) {
        isNewVersion = (VersionUpdateDTO) bundle.getSerializable("is_new");
    }

    @Override
    protected int setLayoutId() {
        return R.layout.act_my_version_introduce;
    }

    @Override
    protected void onInitilizeView() {
        findViewById(R.id.title_my_version_introduce).setOnClickListener(this);
        mWebView = (WebView) findViewById(R.id.web_view_my_version_introduce);

        alertLoadingProgress();
        if (isNewVersion == null){
            OkHttpILoginImpl.getInstance().verityVersionUpdate(new RequestCallback<VersionUpdateDTO>() {
                @Override
                public void onError(int errorCode, String errorMsg) {

                }

                @Override
                public void onSuccess(VersionUpdateDTO response) {
                    isNewVersion = response;
                    loadWebView("");
                }
            });
        }else {
            loadWebView("");
        }
    }

    private void loadWebView(String s) {
        String url = String.format(WebViewUrl.MY_VERSION_INTRODUCE,isNewVersion.is_new == true ? "1" : "0");
        mWebView.getSettings().setJavaScriptEnabled(true);
        //设置编码
        mWebView.getSettings().setDefaultTextEncodingName("utf-8");
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.addJavascriptInterface(new JsBridge(this,isNewVersion), "JsBridge");//AndroidtoJS类对象映射到js的test对象
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
