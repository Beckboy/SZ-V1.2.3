package com.junhsue.ksee;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.WindowManager;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.common.IntentLaunch;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.fragment.dialog.CommonDialog;
import com.junhsue.ksee.frame.IBaseScreen;
import com.junhsue.ksee.frame.MyApplication;
import com.junhsue.ksee.frame.ScreenManager;
import com.junhsue.ksee.net.callback.BroadIntnetConnectListener;
import com.junhsue.ksee.utils.AppProcessUtils;
import com.junhsue.ksee.utils.CommonAskUtils;
import com.junhsue.ksee.utils.PopWindowTokenErrorUtils;
import com.junhsue.ksee.utils.SharedPreferencesUtils;
import com.junhsue.ksee.utils.StatisticsUtil;

/**
 * 所有activity 的基类
 * Created by longer on 17/3/15.
 */

public abstract class BaseActivity extends FragmentActivity implements IBaseScreen {

    private CommonDialog mDialog;
    public BroadIntnetConnectListener con;  //网络连接的广播
    private boolean isBackgroundToForeground = false;//判断前后台切换标识

    private Handler handler = new Handler();
    private Runnable foregroundRunnable = new Runnable() {

        @Override
        public void run() {
            /**
             *  根据应用是不是在前台进程，要通知底层通信该状态 ,因为要考虑到用户可能在非MainFragmentActivity处于后台,所以放在这里
             *  由于isForegroundThread这个是判断的后台的程序,所以不会出现因为Activity的finish而影响到判断应用是不是处于后台 2015-06－15
             * */
            handlerBackgroundApp();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addStack();
        Bundle bundle = getIntent().getExtras();
        onReceiveArguments(bundle != null ? bundle : new Bundle());
        setContentView(setLayoutId());
        onInitilizeView();
    }


    /**
     * receive all params
     */
    protected abstract void onReceiveArguments(Bundle bundle);


    /**
     * set Id for the layout
     */
    protected abstract int setLayoutId();


    /**
     * Initilize source for the ids
     */
    protected abstract void onInitilizeView();

    @Override
    public void addStack() {
        ScreenManager.getScreenManager().pushActivity(this);
    }


    @Override
    public void popStack() {
        ScreenManager.getScreenManager().popActivity(this);
    }

    /**
     * 显示对话框
     */
    public final void alertLoadingProgress(boolean... cancelable) {
        if (mDialog == null) {
            mDialog = new CommonDialog(this);
        }
        if (mDialog.isShowing()) {
            return;
        } else {
            mDialog.show();
        }

    }

    public void dismissLoadingDialog() {
        if (null != mDialog && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    public void EMConnectionListener() {

        EMClient.getInstance().addConnectionListener(new EMConnectionListener() {

            @Override
            public void onConnected() {
                Log.i("EMConnectionListener:", "onConnected");
            }

            @Override
            public void onDisconnected(final int error) {

                Log.i("EMConnectionListener:", "error" + error);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (error == EMError.USER_REMOVED || error == EMError.USER_LOGIN_ANOTHER_DEVICE || error == EMError.SERVER_SERVICE_RESTRICTED) {
                            /**
                             * 登录失败的PopWindow对话框
                             */
                            PopWindowTokenErrorUtils.getInstance(ScreenManager.getScreenManager().currentActivity()).showPopupWindow(0);
                        } else if (error == EMError.NETWORK_ERROR) {
                        } else {
                            EMConnectionListener();
                        }
                    }
                });
            }
        });
    }


    @Override
    public void launchScreen(Class<?> target, Bundle... bd) {
        IntentLaunch.launch(this, target, bd);

    }

    private void setStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 19
     * navigate to specific fragment
     *
     * @param newFragment   target fragment
     * @param addStackTrace true if add to stack trace to fragment manager
     */
    public void startFragment(final Fragment newFragment, int mFrameConatiner, boolean addStackTrace) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.replace(mFrameConatiner, newFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        if (addStackTrace)
            ft.addToBackStack(null);
        ft.commit();
    }


    @Override
    protected void onResume() {
        if (con == null) {
            con = new BroadIntnetConnectListener();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(con, filter);
        System.out.println("注册");
        super.onResume();
        sendStatisticsBroadcast();
        if (isBackgroundToForeground) {
            Trace.v("App background to foreground !");
            isBackgroundToForeground = false;
            long lastTime = SharedPreferencesUtils.getInstance().getLong(Constants.STATISTICS_LAST_TIME, System.currentTimeMillis());
            long duration = System.currentTimeMillis() - lastTime;

            if (duration > 30000) {
                //打包前需要修改渠道
                CommonAskUtils.statisticsAppStart(MyApplication.getConfigChannel(), Constants.APP_RESTART_GO_BACK_FOREGROUND);
            }
        }

        StatisticsUtil.getInstance(this).onResume(this);


    }

    @Override
    protected void onPause() {
        unregisterReceiver(con);
        System.out.println("注销");
        super.onPause();
        StatisticsUtil.getInstance(this).onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        popStack();
        //如果该Activity已经走onDestroy方法，说明该Activity文件已经不需要将链接断掉了
        handler.removeCallbacks(foregroundRunnable);
    }


    private void sendStatisticsBroadcast() {
        Intent intent = new Intent();
        intent.setAction(Constants.STATISTICS_INFO);
        sendBroadcast(intent);
    }

    private void handlerBackgroundApp() {
        try {
            if (AppProcessUtils.isAppForeground()) {
                return;
            }
            isBackgroundToForeground = true;
            long startInBackTime = System.currentTimeMillis();
            SharedPreferencesUtils.getInstance().putLong(Constants.STATISTICS_LAST_TIME, startInBackTime);
        } catch (Exception e) {

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        /**
         *  根据应用是不是在前台进程，要通知底层通信该状态 ,因为要考虑到用户可能在非MainFragmentActivity处于后台,所以放在这里
         *  由于isForegroundThread这个是判断的后台的程序,所以不会出现因为Activity的finish而影响到判断应用是不是处于后台
         * 之所以延时200ms,是为了保证如果是应用走了onDestroy之后,可以将该判断取消掉
         * */
        handler.postDelayed(foregroundRunnable, 200);
    }

    @Override
    public void finish() {
        super.finish();
        //如果该Activity已经走onDestroy方法，说明该Activity文件已经不需要将链接断掉了
        handler.removeCallbacks(foregroundRunnable);
    }
}
