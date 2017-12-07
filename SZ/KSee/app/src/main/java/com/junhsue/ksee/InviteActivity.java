package com.junhsue.ksee;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.junhsue.ksee.net.url.WebViewUrl;
import com.junhsue.ksee.profile.UserProfileService;
import com.junhsue.ksee.utils.JsBridge;
import com.junhsue.ksee.utils.ShareUtils;
import com.junhsue.ksee.utils.StatisticsUtil;
import com.junhsue.ksee.utils.StringUtils;
import com.junhsue.ksee.view.ActionBar;
import com.junhsue.ksee.view.ActionSheet;
import com.junhsue.ksee.view.WebPageView;
import com.umeng.analytics.social.UMPlatformData;

/**
 * Created by Sugar on 17/9/23.
 */

public class InviteActivity extends BaseActivity implements View.OnClickListener {

    private Context mContext;

    private ActionBar actionBar;
    private WebPageView wpvInvite;
    //分享弹出框
    private ActionSheet shareActionSheetDialog;

    private String userId = "";

    @Override
    protected void onReceiveArguments(Bundle bundle) {

    }

    @Override
    protected int setLayoutId() {
        mContext = this;
        return R.layout.act_invite;
    }

    @Override
    protected void onInitilizeView() {

        actionBar = (ActionBar) findViewById(R.id.ab_invite);
        wpvInvite = (WebPageView) findViewById(R.id.wpv_invite);

        actionBar.setOnClickListener(this);
        userId = UserProfileService.getInstance(this).getCurrentLoginedUser().user_id;
        loadWebView();


    }

    private void loadWebView() {
        if (StringUtils.isBlank(userId)) {
            return;
        }
        wpvInvite.getWebView().addJavascriptInterface(new JsBridge(this), "JsBridge");//AndroidtoJS类对象映射到js的test对象
        wpvInvite.loadUrl(String.format(WebViewUrl.H5_INVITATION_URL, userId, "0"));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_left_layout://
                finish();
                break;
            case R.id.btn_right_one://分享
                shareToWechat();
                break;
        }

    }


    /**
     * 分享弹出框
     */
    private void shareToWechat() {
        if (StringUtils.isBlank(userId)) {
            return;
        }
        final String path = "";//如果分享图片获取该图片的本地存储地址
        //第一个是userId,第二个是方案包的id
        final String webPage = String.format(WebViewUrl.H5_INVITATION_SHARE, userId, "0");
        final String title = "秋季招生荒，试试团购营销？免费团购方案包正袭来";
        final String desc = "最潮的教育人栖息地，专业的知识共享社区。立即注册钬花，领取方案包福利！";

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
                StatisticsUtil.getInstance(mContext).statisticsInviteShareCount(2);
                ShareUtils.getInstance(mContext).sharePage(ShareUtils.SendToPlatformType.TO_FRIEND, webPage, path,
                        title, desc, UMPlatformData.UMedia.WEIXIN_FRIENDS);
                if (shareActionSheetDialog != null) {
                    shareActionSheetDialog.dismiss();
                }

            }
        });

        llShareCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatisticsUtil.getInstance(mContext).statisticsInviteShareCount(1);
                ShareUtils.getInstance(mContext).sharePage(ShareUtils.SendToPlatformType.TO_CIRCLE, webPage, path,
                        title, desc, UMPlatformData.UMedia.WEIXIN_CIRCLE);
                if (shareActionSheetDialog != null) {
                    shareActionSheetDialog.dismiss();
                }

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
