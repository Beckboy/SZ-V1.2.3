package com.junhsue.ksee.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.junhsue.ksee.DownLoadAPKActivity;
import com.junhsue.ksee.MySettingActivity;
import com.junhsue.ksee.R;
import com.junhsue.ksee.dto.VersionUpdateDTO;
import com.junhsue.ksee.net.url.WebViewUrl;
import com.junhsue.ksee.profile.UserProfileService;
import com.junhsue.ksee.view.ActionSheet;
import com.junhsue.ksee.view.RfCommonDialog;
import com.umeng.analytics.social.UMPlatformData;

/**
 * js调用的安卓源码方法
 * Created by Sugar on 17/9/25.
 */

public class JsBridge extends Object {
    private Context mContext;
    private VersionUpdateDTO mVersionUpdateDTO;

    public JsBridge(Context context) {
        mContext = context;
    }

    public JsBridge(Context context, VersionUpdateDTO versionUpdateDTO) {
        mContext = context;
        mVersionUpdateDTO = versionUpdateDTO;
    }

    // 定义JS需要调用的方法
    // 被JS调用的方法必须加入@JavascriptInterface注解
    @JavascriptInterface
    public void shareToWechat() {
        String userId = UserProfileService.getInstance(mContext).getCurrentLoginedUser().user_id;
        if (StringUtils.isBlank(userId)) {
            return;
        }
        final String path = "";//如果分享图片获取该图片的本地存储地址
        final String webPage = String.format(WebViewUrl.H5_INVITATION_SHARE, userId, "0");
        final String title = "秋季招生荒，试试团购营销？免费团购方案包正袭来";
        final String desc = "最潮的教育人栖息地，专业的知识共享社区。立即注册钬花，领取方案包福利！";

        final ActionSheet shareActionSheetDialog = new ActionSheet(mContext);
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

    // 定义JS需要调用的方法
    // 被JS调用的方法必须加入@JavascriptInterface注解
    @JavascriptInterface
    public void versionUpdate() {
        if (!CommonUtils.getIntnetConnect(mContext)){
            ToastUtil.getInstance(mContext).setContent("网络连接异常").setShow();
            return;
        }
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {

            //获得ConnectivityManager对象
            ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

            //获取ConnectivityManager对象对应的NetworkInfo对象
            //获取WIFI连接的信息
            NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (!wifiNetworkInfo.isConnected()) {
                InternetHint();
            } else {
                Toast.makeText(mContext, "网络连接异常", Toast.LENGTH_SHORT).show();
            }
            //API大于23时使用下面的方式进行网络监听
        }else {
            System.out.println("API level 大于23");
            //获得ConnectivityManager对象
            ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            //获取所有网络连接的信息
            Network[] networks = connMgr.getAllNetworks();
            //用于存放网络连接信息
            StringBuilder sb = new StringBuilder();
            //通过循环将网络信息逐个取出来
            for (int i=0; i < networks.length; i++){
                //获取ConnectivityManager对象对应的NetworkInfo对象
                NetworkInfo networkInfo = connMgr.getNetworkInfo(networks[i]);
                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
                    if (networkInfo.isConnected()){
                        versionLoad();
                        return;
                    }
                }
            }
            InternetHint();
        }

    }

    /**
     * 下载更新
     */
    private void versionLoad() {
        Intent updateIntent = new Intent(mContext, DownLoadAPKActivity.class);
        Bundle upDateBundle = new Bundle();
        upDateBundle.putSerializable("version_info", mVersionUpdateDTO);
        upDateBundle.putSerializable("down_update", true);
        updateIntent.putExtras(upDateBundle);
        mContext.startActivity(updateIntent);
    }

    public void InternetHint(){
        RfCommonDialog.Builder builder = new RfCommonDialog.Builder(mContext);
        builder.setTitle("目前是非WIFI网络情况下，下载安装包将耗费较大流量，确认下载吗？");
        builder.setPositiveButton("继续", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                versionLoad();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        RfCommonDialog commonDialog = builder.create();
        commonDialog.setCanceledOnTouchOutside(true);
        commonDialog.setCancelable(true);
        commonDialog.show();
    }

}
