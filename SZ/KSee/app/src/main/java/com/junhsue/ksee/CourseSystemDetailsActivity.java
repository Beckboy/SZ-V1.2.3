package com.junhsue.ksee;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.junhsue.ksee.entity.Course;
import com.junhsue.ksee.entity.CourseSystem;
import com.junhsue.ksee.entity.GoodsInfo;
import com.junhsue.ksee.entity.UserInfo;
import com.junhsue.ksee.file.FileUtil;
import com.junhsue.ksee.net.api.OKHttpCourseImpl;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.url.WebViewUrl;
import com.junhsue.ksee.profile.UserProfileService;
import com.junhsue.ksee.utils.ShareUtils;
import com.junhsue.ksee.utils.StatisticsUtil;
import com.junhsue.ksee.utils.ToastUtil;
import com.junhsue.ksee.view.ActionBar;
import com.junhsue.ksee.view.ActionSheet;
import com.junhsue.ksee.view.WebPageView;
import com.umeng.analytics.social.UMPlatformData;

/**
 * 系统课详情
 * Created by longer on 17/3/30.
 */


public class CourseSystemDetailsActivity extends BaseActivity implements View.OnClickListener {

    //系统课
    public final static String PARAMS_SYSTEM = "params_system";
    //系统课id
    public final static String PARAMS_SYSTEM_COURSE_ID="params_system_course_id";
    //业务id
    public final static String PARAMS_SYSTEM_BUSINESS_ID="params_system_business_id";
    //课程标题
    public final static String PARAMS_SYSTEM_TITLE="params_system_title";

    private ActionBar mActionBar;
    private WebPageView mWebView;

    private Button mBtnBuy;

    private CourseSystem mCourseSystem;
    private Context mContext;
    private UserInfo userInfo;
    private ActionSheet shareActionSheetDialog;
    //
    private String mCourseId;
    //
    private String mTitle;
    //业务id
    private int mCourseBusinessId;
    //

    @Override
    protected void onReceiveArguments(Bundle bundle) {
        mCourseId=bundle.getString(CourseSystemDetailsActivity.PARAMS_SYSTEM_COURSE_ID,"");
        mTitle=bundle.getString(CourseSystemDetailsActivity.PARAMS_SYSTEM_TITLE,"");
        mCourseBusinessId=bundle.getInt(CourseSystemDetailsActivity.PARAMS_SYSTEM_BUSINESS_ID,0);
    }

    @Override
    protected int setLayoutId() {
        mContext = this;
        return R.layout.act_course_system_details;
    }

    @Override
    protected void onResume() {
        StatisticsUtil.getInstance(mContext).onCountPage("1.4.5");
        super.onResume();
    }

    @Override
    protected void onInitilizeView() {
        userInfo = UserProfileService.getInstance(this).getCurrentLoginedUser();
        mActionBar = (ActionBar) findViewById(R.id.action_bar);
        mWebView = (WebPageView) findViewById(R.id.course_system_web_view);
        mBtnBuy = (Button) findViewById(R.id.btn_buy);
        //
        mActionBar.setOnClickListener(this);
        mBtnBuy.setOnClickListener(this);
        //
        //alertLoadingProgress();

        mActionBar.setTitle(mTitle);
        getCourseDetails();
        loadArticleDetails();

    }


    /** 获取课程详情*/

    private void getCourseDetails(){

        OKHttpCourseImpl.getInstance().getCourseSystemDetails(mCourseId, new RequestCallback<CourseSystem>() {

            @Override
            public void onError(int errorCode, String errorMsg) {
//
//                ToastUtil.showToast(getApplicationContext(),getString(R.string.net_error_service_not_accessables));
//
//                dismissLoadingDialog();
            }

            @Override
            public void onSuccess(CourseSystem response) {
                mCourseSystem=response;
//                if (null != mCourseSystem){
//                }
//                else{
//                    dismissLoadingDialog();
//                }


            }
        });
    }


    /**
     * 加载文章详情
     */
    private void loadArticleDetails() {
        if (userInfo == null) {
            return;
        }
        String url = String.format(WebViewUrl.H5_COURSE_URL, mCourseId,mCourseBusinessId, userInfo.token);

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
                if(null==mCourseSystem) return;
                showShareActionSheetDailog();
                break;
            case R.id.btn_buy: //购买
                if(null==mCourseSystem) return;
                GoodsInfo goodsInfo = GoodsInfo.cloneOject(mCourseSystem.poster, mCourseSystem.id, mCourseSystem.title, Double.parseDouble(mCourseSystem.amount),
                        GoodsInfo.GoodsType.getType(mCourseSystem.business_id));
                Bundle bundle = new Bundle();
                bundle.putSerializable(ConfirmOrderActivity.PARAMS_GOODS_INFO, goodsInfo);
                launchScreen(CourseSystemApplyActivity.class, bundle);

                StatisticsUtil.getInstance(mContext).onCountActionDot("5.4.1");
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
        final String path = FileUtil.getImageFolder() + "/" + mCourseSystem.poster.hashCode();//如果分享图片获取该图片的本地存储地址
        final String webPage = String.format(WebViewUrl.H5_SHARE_COURSE, mCourseSystem.id, mCourseSystem.business_id);
        final String title = mCourseSystem.title;
        final String desc = mCourseSystem.description;

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

                StatisticsUtil.getInstance(mContext).onCountActionDot("5.4.3");
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

                StatisticsUtil.getInstance(mContext).onCountActionDot("5.4.2");
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


    @Override
    protected void onPause() {
        super.onPause();
        if (shareActionSheetDialog != null) {
            shareActionSheetDialog.dismiss();
        }
    }
}



