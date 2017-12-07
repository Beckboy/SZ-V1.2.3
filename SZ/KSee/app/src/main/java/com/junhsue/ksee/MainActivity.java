package com.junhsue.ksee;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.junhsue.ksee.adapter.CommonFragmentPageAdapter;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.common.IColleageMenu;
import com.junhsue.ksee.common.IHomeMenu;
import com.junhsue.ksee.dto.VersionUpdateDTO;
import com.junhsue.ksee.entity.UserInfo;
import com.junhsue.ksee.fragment.ColleageCourseSubjectFragment;
import com.junhsue.ksee.fragment.CollegeFragment;
import com.junhsue.ksee.fragment.CommunityHomeFragment;
import com.junhsue.ksee.fragment.KnowledgeFragment;
import com.junhsue.ksee.fragment.MySpaceFragment;
import com.junhsue.ksee.fragment.RealizeFragment;
import com.junhsue.ksee.net.api.OkHttpILoginImpl;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.profile.UserProfileService;
import com.junhsue.ksee.utils.CommitUtil;
import com.junhsue.ksee.utils.CommonUtils;
import com.junhsue.ksee.utils.DateUtils;
import com.junhsue.ksee.utils.HXUtils;
import com.junhsue.ksee.utils.PopWindowTokenErrorUtils;
import com.junhsue.ksee.utils.SharedPreferencesUtils;
import com.junhsue.ksee.utils.StatisticsUtil;
import com.junhsue.ksee.utils.StringUtils;
import com.junhsue.ksee.utils.ToastUtil;
import com.junhsue.ksee.view.CommonNoTouchViewPager;
import com.junhsue.ksee.view.MainTabView;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页
 */
public class MainActivity extends BaseActivity implements MainTabView.IMainTabClickListener, HXUtils.HXRegisterSuccess {


    private ViewPager mViewPager;

    private MainTabView mMainTabView;
    private Context mContext;
    private HXUtils.HXRegisterSuccess context;

    private static List<Fragment> mFragments;

    //区分页面切换来源 底部tab点击切换还是menu切换
    private boolean mIsMenuClick;
    //设置熟模块下标
    private int mColleagePagePosition;

    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    verityAutoLogin(mUserInfo);
                    Log.i("loginagain", "开启自动登录的验证" + System.currentTimeMillis());
                    break;
                case 1:
                    verityVersionUpdate();
                    break;
            }
            return false;
        }
    });


    static {
        mFragments = new ArrayList<>();
        mFragments.add(KnowledgeFragment.newInstance());
        mFragments.add(RealizeFragment.newInstance());
        //问答
        //mFragments.add(SocialCircleFragment.newInstance());
        //社区
        mFragments.add(CommunityHomeFragment.newInstance());
        //mFragments.add(CollegeFragment.newInstance());
        mFragments.add(ColleageCourseSubjectFragment.newInstance());
        mFragments.add(MySpaceFragment.newIntance());
    }

    private UserInfo mUserInfo;


    @Override
    protected void onReceiveArguments(Bundle bundle) {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.act_main;
    }

    @Override
    protected void onInitilizeView() {

        /**
         * 设置是否为首次登陆
         */
        mUserInfo = UserProfileService.getInstance(this).getCurrentLoginedUser();
        if (null == mUserInfo) {
            finish();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            if (EMClient.getInstance().isLoggedInBefore()) { //环信退出登录
                EMClient.getInstance().logout(true);
            }
            return;
        }

        SharedPreferencesUtils.getInstance().putBoolean(Constants.ISFIRST_LOGIN, false);
        StatisticsUtil.getInstance(this).onProfileSignIn(mUserInfo.user_id);

        mContext = this;
        context = this;

        mViewPager = (CommonNoTouchViewPager) findViewById(R.id.view_pager);
        mMainTabView = (MainTabView) findViewById(R.id.main_tab);
        //
        CommonFragmentPageAdapter commonFragmentPageApdater = new CommonFragmentPageAdapter(getSupportFragmentManager(), mFragments);


        mViewPager.setAdapter(commonFragmentPageApdater);
        //
        mMainTabView.setIMainTabClickListener(this);
        mViewPager.addOnPageChangeListener(onPageChangeListener);
        mViewPager.setOffscreenPageLimit(2);
        //
        mViewPager.setCurrentItem(0, false);

        if (!CommonUtils.getIntnetConnect(mContext)) {
            ToastUtil.getInstance(mContext).setContent("网络连接异常").setShow();
            return;
        }

        handler.sendEmptyMessageDelayed(0,500);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
    }

    /**
     * 验证自动登录的token
     */
    private void verityAutoLogin(UserInfo userInfo) {
        OkHttpILoginImpl.getInstance().autologinByToken(userInfo.token, new RequestCallback<UserInfo>() {

            @Override
            public void onError(int errorCode, String errorMsg) {
                if (EMClient.getInstance().isLoggedInBefore()) { //环信退出登录
                    EMClient.getInstance().logout(true);
                }
                /**
                 * 登录失败的PopWindow对话框
                 */
                PopWindowTokenErrorUtils.getInstance((Activity) mContext).showPopupWindow(R.layout.act_main);
            }

            @Override
            public void onSuccess(UserInfo response) {
                Log.i("loginagain", "自动登录的验证成功" + System.currentTimeMillis());
                HXUtils.verifyloginHX(mContext, context);

                /**
                 * 登录成功之后 版本是否需要更新
                 */
                handler.sendEmptyMessage(1);
            }
        });
    }


    /**
     * 验证版本更新的version
     */
    private void verityVersionUpdate() {
        // TODO 添加版本更新间隔判断
        int num = SharedPreferencesUtils.getInstance(mContext).getInt(Constants.APP_UPDATE_REFUSE_NUM,0);
        if (num >= 2) return;
        if (num == 1) {
            String time = SharedPreferencesUtils.getInstance(mContext).getString(Constants.APP_UPDATE_REFUSE_TIME,"");
            if (time.equals(DateUtils.getCurrentDate())) return; //当天
        }
        OkHttpILoginImpl.getInstance().verityVersionUpdate(new RequestCallback<VersionUpdateDTO>() {
            @Override
            public void onError(int errorCode, String errorMsg) {

            }

            @Override
            public void onSuccess(VersionUpdateDTO response) {
                /**
                 * 版本更新的PopWindow对话框
                 */
                CommitUtil util = new CommitUtil();
                String localVersion = util.getAppVersionName();
                if (!StringUtils.isNewVersion(localVersion, response.version)) {
                    Intent updateIntent = new Intent(mContext, DownLoadAPKActivity.class);
                    Bundle upDateBundle = new Bundle();
                    upDateBundle.putSerializable("version_info", response);
                    updateIntent.putExtras(upDateBundle);
                    startActivity(updateIntent);
                }
            }
        });
    }


    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageSelected(int position) {

            //position 代表底部模块的下标,值等于3为塾模块,跳转后指定塾下标
            if (mIsMenuClick && position == 3) {
                //获取塾模块实例
                Fragment baseFragment = mFragments.get(position);
                if (baseFragment instanceof CollegeFragment)
                    ((CollegeFragment) baseFragment).setCurrentPage(mColleagePagePosition);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    @Override
    public void onClick(int index) {
        mIsMenuClick = false;
        mViewPager.setCurrentItem(index, false);
    }


    /**
     * 设置当前页面切换
     * <p>
     * 把首页菜单栏分为
     * <p>
     * 0 直播
     * <p>
     * 1 问答
     * <p>
     * 2 教室
     * <p>
     * 3 线下课
     */
    public void setSelectPage(int menuType) {
        mIsMenuClick = true;
        switch (menuType) {
            case IHomeMenu.QUETION:
                mViewPager.setCurrentItem(2, false);
                mMainTabView.setTabColor(2);
                break;

            case IHomeMenu.LIVE:
                mColleagePagePosition = IColleageMenu.LIVE;
                mViewPager.setCurrentItem(3, false);
                mMainTabView.setTabColor(3);
                break;

            case IHomeMenu.CLASSS_ROOM:
                mColleagePagePosition = IColleageMenu.CLASSROOM;
                mViewPager.setCurrentItem(3, false);
                mMainTabView.setTabColor(3);
                break;

            case IHomeMenu.COURSE:
                mColleagePagePosition = IColleageMenu.COURSE;
                mViewPager.setCurrentItem(3, false);
                mMainTabView.setTabColor(3);
                break;
            case IHomeMenu.SOURCE:
                mViewPager.setCurrentItem(1, false);
                mMainTabView.setTabColor(1);
                break;
        }
    }

    /**
     * 环信登录
     */
    public void isHXLogin() {
        EMClient.getInstance().login(mUserInfo.user_id, mUserInfo.user_id, new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                Log.i("huanxin", "登录聊天服务器成功！");
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
            }

            @Override
            public void onProgress(int progress, String status) {
            }

            @Override
            public void onError(int code, String message) {
                Log.d("huanxin", "登录聊天服务器失败！");
                if (code == EMError.USER_NOT_LOGIN) {
                    verityAutoLogin(mUserInfo);
                }
            }
        });
    }

    /**
     * 环信注册成功的回调
     */
    @Override
    public void isRegister() {
        isHXLogin();
        EMConnectionListener();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent setIntent = new Intent();
        setIntent.setAction(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);

    }


    @Override
    protected void onResume() {
        super.onResume();
    }


}