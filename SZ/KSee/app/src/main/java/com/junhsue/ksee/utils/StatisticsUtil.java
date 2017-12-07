package com.junhsue.ksee.utils;

import android.content.Context;

import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.entity.ResultEntity;
import com.junhsue.ksee.entity.UserDefinedStatisticModel;
import com.junhsue.ksee.entity.UserInfo;
import com.junhsue.ksee.net.api.OKHttpStatistics;
import com.junhsue.ksee.net.api.OkHttpSocialCircleImpl;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.profile.UserProfileService;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.social.UMPlatformData;

import java.util.HashMap;
import java.util.Map;

public class StatisticsUtil {

    private static Context context;

    private static volatile StatisticsUtil instance = null;

    /**
     * 打点事件 eventId
     */
    public static final String TAB_KNOWLEDGE = "tab_knowledge";//拾
    public static final String TAB_REALIZE = "tab_realize";//知
    public static final String TAB_SOCIAL = "tab_social";//社
    public static final String TAB_COLLEAGE = "tab_colleage";//熟
    public static final String TAB_SPACE = "tab_space";//吾

    private StatisticsUtil(Context ctx) {
        this.context = ctx.getApplicationContext();
    }

    public static StatisticsUtil getInstance(Context ctx) {
        if (instance == null) {
            synchronized (StatisticsUtil.class) {
                if (instance == null) {
                    instance = new StatisticsUtil(ctx);
                }
            }
        }
        return instance;
    }

    /**
     * 统计页面（只由Activity构成的应用）
     * 统计新增用户、活跃用户、启动次数、使用时长等基本数据
     * onResume方法调用，子类和父类不可同时调用，以免统计出错
     */
    public void onResume(Context mContext) {
        //应用宝统计：session的统计 onResume
//        StatService.onResume(mContext);
        //友盟统计：session的统计 onResume
        MobclickAgent.onResume(mContext);
        //注意：!!!!!!  不要在这后面添加代码逻辑，因为有的页面会不执行
    }

    /**
     * 统计页面（只由Activity构成的应用）
     * 统计新增用户、活跃用户、启动次数、使用时长等基本数据
     * onPause方法调用，子类和父类不可同时调用，以免统计出错
     */
    public void onPause(Context mContext) {
        //应用宝统计：session的统计 onResume 弃用应用宝的
//        StatService.onPause(mContext);
        //友盟统计：session的统计 onResume
        MobclickAgent.onPause(mContext);
        //注意：!!!!!!  不要在这后面添加代码逻辑，因为有的页面会不执行
    }

    /**
     * 统计页面（由Fragment构成的应用 或者 viewPage+fragment构成的应用）
     * 统计新增用户、活跃用户、启动次数、使用时长等基本数据
     * onResume方法调用，子类和父类不可同时调用，以免统计出错
     */
    public void onResume(String simpleName) {
        //友盟统计：的统计 page
        MobclickAgent.onPageStart(simpleName);
        //LogUtils.e("onResume:当前的Fragment的类名：" + simpleName);
        //注意：!!!!!!  不要在这后面添加代码逻辑，因为有的页面会不执行
    }

    /**
     * 统计页面（只由Fragment构成的应用）
     * 统计新增用户、活跃用户、启动次数、使用时长等基本数据
     * onPause方法调用，子类和父类不可同时调用，以免统计出错
     */
    public void onPause(String simpleName) {
        //友盟统计：session的统计  page
        MobclickAgent.onPageEnd(simpleName);
        //注意：!!!!!!  不要在这后面添加代码逻辑，因为有的页面会不执行
    }

    /**
     * 统计发生次数
     *
     * @param context
     * @param eventId 事件id
     */
    public void onEvent(Context context, String eventId) {
        MobclickAgent.onEvent(context, eventId);
    }

    /**
     * 统计点击行为各属性被触发的次数
     *
     * @param context
     * @param eventId 事件id
     * @param map     map为当前事件的属性和取值（Key-Value键值对）
     */
    public void onEvent(Context context, String eventId, HashMap<String, String> map) {
        MobclickAgent.onEvent(context, eventId, map);
    }

    /**
     * 统计数值型变量的值的分布
     * V5.2.2之后(>=)才提供
     *
     * @param context
     * @param id       为事件ID
     * @param map      为当前事件的属性和取值
     * @param duration 为当前事件的数值为当前事件的数值，取值范围是-2,147,483,648 到 +2,147,483,647 之间的有符号整数，即int 32类型，如果数据超出了该范围，会造成数据丢包，影响数据统计的准确性。
     */
    public void onEventValue(Context context, String id, Map<String, String> map, int duration) {
        MobclickAgent.onEventValue(context, id, map, duration);
    }

    public void statisticsSetScenarioType(Context context1, int EScenarioType) {
        context = context1;
        MobclickAgent.setScenarioType(context, MobclickAgent.EScenarioType.E_UM_NORMAL);
    }

    public void onKillProcess() {
        MobclickAgent.onKillProcess(context);
    }

    public void statisticsOnPageStart(String pageName) {
        MobclickAgent.onPageStart(pageName);
    }

    public void statisticsOnPageEnd(String pageName) {
        MobclickAgent.onPageEnd(pageName);
    }

    public void onProfileSignIn(String ID) {
        MobclickAgent.onProfileSignIn(ID);
    }

    public void onProfileSignIn(String provider, String ID) {
        MobclickAgent.onProfileSignIn(provider, ID);
    }

    public void onProfileSignOff() {
        MobclickAgent.onProfileSignOff();
    }


    public void onSocialEvent(Context context, UMPlatformData.UMedia meida) {
        UserInfo userInfo = UserProfileService.getInstance(context).getCurrentLoginedUser();
        UMPlatformData platform = new UMPlatformData(meida, userInfo.user_id);
        platform.setGender(UMPlatformData.GENDER.MALE); //optional
        platform.setWeiboId("weiboId");  //optional
        MobclickAgent.onSocialEvent(context, platform);
    }

    /**
     * 自定义跳转页面统计
     */
    public void onCountPage(String path) {
        UserDefinedStatisticModel model = new UserDefinedStatisticModel();

        model.type = 1;
        model.path = path;
        model.time = DateUtils.getTimesTamp(System.currentTimeMillis()) + "";
        DefinedStatisticsManager.getInstance().pageViewWithName(model);
    }

    /**
     * 自定义的埋点统计
     *
     * @param path
     */
    public void onCountActionDot(String path) {

        UserDefinedStatisticModel model = new UserDefinedStatisticModel();

        model.type = 2;
        model.path = path;
        model.time = DateUtils.getTimesTamp(System.currentTimeMillis()) + "";
        DefinedStatisticsManager.getInstance().countActionNum(model);
    }


    /**
     * 统计问答分享次数
     *
     * @param questionId
     */
    public void statisticsQuestionShareCount(String questionId) {
        OKHttpStatistics.getInstance().uploadStatisticsQuestionShare(questionId, new RequestCallback<ResultEntity>() {

            @Override
            public void onError(int errorCode, String errorMsg) {
                Trace.d("==question share count ,info :" + errorCode + errorMsg);
            }

            @Override
            public void onSuccess(ResultEntity response) {
                Trace.d("==question share count success ");
            }
        });

    }

    /**
     * 单向历分享
     *
     * @param calendar
     */
    public void statisticsCalendarShareCount(String calendar) {
        OKHttpStatistics.getInstance().uploadCalendarShareCount(calendar, new RequestCallback<Object>() {

            @Override
            public void onError(int errorCode, String errorMsg) {
                Trace.d("==calendar share count ,info :" + errorCode + errorMsg);
            }

            @Override
            public void onSuccess(Object response) {
                Trace.d("==calendar share count success ");
            }
        });
    }

    /**
     * 邀请好友可以拿到方案包的统计
     *
     * @param share_type
     */
    public void statisticsInviteShareCount(int share_type) {
        OKHttpStatistics.getInstance().uploadStatisticsInviteShareCount(1 + "", share_type + "", new RequestCallback<ResultEntity>() {


            @Override
            public void onError(int errorCode, String errorMsg) {

            }

            @Override
            public void onSuccess(ResultEntity response) {

            }
        });

    }

}
