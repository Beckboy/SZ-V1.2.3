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
import android.widget.Toast;

import com.junhsue.ksee.entity.Course;
import com.junhsue.ksee.entity.GoodsInfo;
import com.junhsue.ksee.entity.UserInfo;
import com.junhsue.ksee.file.FileUtil;
import com.junhsue.ksee.fragment.ColleageCourseSubjectFragment;
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
 * 主题课程详情页
 * Created by longer on 17/3/30.
 */

public class CourseDetailsActivity extends BaseActivity implements View.OnClickListener {


    //课程id
    public final static String PARAMS_COURSE_ID = "params_course_id";
    //业务id
    public final static String PARAMS_BUSINESS_ID = "params_business_id";
    //课程标题
    public final static String PARAMS_COURSE_TITLE = "params_course_title";
    //课程类型
    public final static String PARAMS_COURSE_TYPE = "params_course_type";

    private Course mCourse;

    private ActionBar mActionBar;
    private WebPageView mWebView;

    private ActionSheet shareActionSheetDialog;
    private Context mContext;
    private Button mBtnBuy;
    private UserInfo userInfo;
    //
    private String mCourseId;
    //标题
    private String mCourseTitle;
    //业务id
    private int mBusinessId;
    //
    //private int mCourseType;

    @Override
    protected void onReceiveArguments(Bundle bundle) {
        mCourseId = bundle.getString(PARAMS_COURSE_ID, "");
        mCourseTitle = bundle.getString(PARAMS_COURSE_TITLE, "");
        mBusinessId = bundle.getInt(PARAMS_BUSINESS_ID, 0);
    }

    @Override
    protected int setLayoutId() {
        mContext = this;
        return R.layout.act_course_subject_details;
    }

    @Override
    protected void onResume() {
        StatisticsUtil.getInstance(mContext).onCountPage("1.4.6");
        super.onResume();
    }

    @Override
    protected void onInitilizeView() {
        userInfo = UserProfileService.getInstance(this).getCurrentLoginedUser();
        mActionBar = (ActionBar) findViewById(R.id.title_sub_course);
        mWebView = (WebPageView) findViewById(R.id.course_subject_web_view);
        mBtnBuy = (Button) findViewById(R.id.btn_buy);
        //
        mActionBar.setOnClickListener(this);
        mBtnBuy.setOnClickListener(this);
        //
        //if (null != mCourse)
        mActionBar.setTitle(mCourseTitle);

        //alertLoadingProgress();
        getCourseDetails();
        loadArticleDetails();

    }


    /**
     * 获取课程详情
     */
    private void getCourseDetails() {
        OKHttpCourseImpl.getInstance().getCourseSubjectDetails(mCourseId, new RequestCallback<Course>() {

            @Override
            public void onError(int errorCode, String errorMsg) {

            }

            @Override
            public void onSuccess(Course response) {
                mCourse = response;
            }
        });
    }


    /**
     * 加载文章详情
     */
    private void loadArticleDetails() {
        String url = String.format(WebViewUrl.H5_COURSE_URL, mCourseId, mBusinessId, userInfo.token);

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
                if (null == mCourse) return;
                showShareActionSheetDailog();
                break;
            case R.id.btn_buy: //购买
                if (null == mCourse) return;
                GoodsInfo goodsInfo = GoodsInfo.cloneOject(mCourse.poster, mCourse.id, mCourse.title, mCourse.amount,
                        GoodsInfo.GoodsType.getType(mCourse.business_id));
                Bundle bundle = new Bundle();
                bundle.putSerializable(ConfirmOrderActivity.PARAMS_GOODS_INFO, goodsInfo);
                launchScreen(ConfirmOrderActivity.class, bundle);

                StatisticsUtil.getInstance(mContext).onCountActionDot("5.3.1");
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
        final String path = FileUtil.getImageFolder() + "/" + mCourse.poster.hashCode();//如果分享图片获取该图片的本地存储地址
        final String webPage = String.format(WebViewUrl.H5_SHARE_COURSE, mCourse.id, mCourse.business_id);
        final String title = mCourse.title;
        final String desc = mCourse.description;

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
                StatisticsUtil.getInstance(mContext).onCountActionDot("5.3.3");
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
                StatisticsUtil.getInstance(mContext).onCountActionDot("5.3.2");
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
