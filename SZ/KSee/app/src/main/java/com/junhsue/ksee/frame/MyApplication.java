package com.junhsue.ksee.frame;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.easefun.polyvsdk.live.PolyvLiveSDKClient;
import com.easefun.polyvsdk.live.chat.PolyvChatManager;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.utils.HXHelper;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.entity.UserInfo;
import com.junhsue.ksee.file.FileUtil;
import com.junhsue.ksee.net.OKHttpUtils;
import com.junhsue.ksee.profile.UserProfileService;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.wxop.stat.StatService;
import com.umeng.analytics.MobclickAgent;

import java.io.File;

import okhttp3.OkHttpClient;

/**
 * 全局应用类,
 * Created by longer on 17/3/16.
 */

public class MyApplication extends MultiDexApplication {

    private static MyApplication mInstance;

    //用户登陆的token T1RBek5BPT0rTWpscFoyMTJiRzF1WVRVNFoza3hZVGRuY2pkeWVUZGxZMk13ZFhObFkzaz0
    private static String mToken = "";

    private static IWXAPI wxapi;

    public static IWXAPI getIwxapi() {
        return wxapi;
    }

    /**
     * 登录聊天室/ppt直播所需，请填写自己的appId和appSecret，否则无法登陆
     * appId和appSecret在直播系统管理后台的用户信息页的API设置中用获取
     */
    private static final String appId = "eufwi3t14h";
    private static final String appSecret = "b072e94379d14aac94d50bfbf67a27de";
    //账户id
    public static final String POLYV_ACCOUNT_USER_ID="ehkgb3o895";



    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        initChatIM();

        initOkHttp();
        initImageLoader(this);
        registerWeixin();
        StatService.setContext(this);
        MobclickAgent.setDebugMode(false);
        //初始化直播
        initPolyvChatConfig();
    }

/*    @Override
    protected voidattachBaseContext(Context base) {

        super.attachBaseContext(base);

        MultiDex.install(base);

    }*/

    public void registerWeixin() {
        wxapi = WXAPIFactory.createWXAPI(this, Constants.WEIXIN_APP_ID);
        wxapi.registerApp(Constants.WEIXIN_APP_ID);
    }

    private void initChatIM() {
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);

        //初始化
        if (EaseUI.getInstance().init(this, options)){
            //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
            EMClient.getInstance().setDebugMode(true);
            HXHelper.getInstance().init(getApplicationContext(),options);
        }
    }


    public static MyApplication getApplication() {
        return mInstance;
    }


    private void initOkHttp() {
        OkHttpClient okHttpClient = new OkHttpClient();
        OKHttpUtils.initClient(okHttpClient);
    }


    public static String getToken() {
        UserInfo userInfo = UserProfileService.getInstance(getApplication()).getCurrentLoginedUser();
        if (null != userInfo) {
            Trace.i("token->：" + userInfo.token);
            return userInfo.token;
        }
        //Trace.i("请求的token："+mToken);
        return mToken;
    }

    public static void setToken(String token) {
        mToken = token;
    }


    /**
     * 获取配置打包渠道号
     *
     * 每次渠道打包之前,需要修改
     *
     * @see  Constants
     */
    public static String  getConfigChannel(){

        return Constants.XIAO_MI; //update
//        return Constants.C360; //update
    }


    /**
     * 图片配置
     */
    public static void initImageLoader(Context context) {
        File cacheDir = new File(FileUtil.getImageFolder());
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()

                //.discCache(new FileCountLimitedDiscCache(cacheDir, 5000))
                .discCache(new UnlimitedDiscCache(cacheDir))
                .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                //.imageDecoder(new NutraBaseImageDecoder(true))
                //CompressFormat.PNG类型，70质量（0-100）
                .discCacheExtraOptions(720, 1280, Bitmap.CompressFormat.PNG, 70)
                .memoryCacheExtraOptions(480, 800)
                .build();
        ImageLoader.getInstance().init(config);
    }



    /**
     * 初始化聊天室配置
     */
    public void initPolyvChatConfig() {
        PolyvChatManager.initConfig(appId, appSecret);
    }

//    public void initPolyvCilent() {
//        PolyvLiveSDKClient client = PolyvLiveSDKClient.getInstance();
//        //启动Bugly
//        //client.initCrashReport(getApplicationContext());
//        //启动Bugly后，在学员登录时设置学员id
//        //client.crashReportSetUserId(userId);
//    }
}
