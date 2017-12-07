package com.junhsue.ksee.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.junhsue.ksee.R;

/**
 * 公用的webview
 * Created by longer on 17/8/25.
 */


public class WebPageView extends FrameLayout {


    private Context mContext;
    //
    private ProgressBar mProgressBar;
    //
    private WebView mWebView;

    private IProgressListener mIProgressListener;


    public WebPageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initializeView(mContext);
    }


    public WebPageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initializeView(mContext);
    }

    public WebPageView(Context context) {
        super(context);
        mContext = context;
        initializeView(mContext);
    }


    private void initializeView(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //
        View view = layoutInflater.inflate(R.layout.component_webview, this);
        //
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        mWebView = (WebView) view.findViewById(R.id.component_web_view);
        //
        setWebViewSetting();
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new MyWebChromeClient());
    }


    private void setWebViewSetting() {

        WebSettings webSettings = mWebView.getSettings();
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        //图片地址http https链接支持
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
    }


    public WebView getWebView() {
        return mWebView;
    }

    /**
     * 加载url
     *
     * @param url http://www.10knowing.com/app/hardcore/42
     */
    public void loadUrl(String url) {
        mWebView.loadUrl(url);
    }

    class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            mProgressBar.setVisibility(View.VISIBLE);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            mProgressBar.setVisibility(View.GONE);
            if (null != mIProgressListener) {
                mIProgressListener.onFinished();
            }
            super.onPageFinished(view, url);
        }
    }


    class MyWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            mProgressBar.setProgress(newProgress);
            super.onProgressChanged(view, newProgress);
        }
    }

    public void setIProgressListener(IProgressListener mIProgressListener) {
        this.mIProgressListener = mIProgressListener;
    }

    public interface IProgressListener {

        void onFinished();
    }

}
