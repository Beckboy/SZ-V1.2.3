package com.junhsue.ksee;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.hyphenate.chat.EMClient;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.entity.ResultEntity;
import com.junhsue.ksee.frame.MyApplication;
import com.junhsue.ksee.net.api.OKHttpStatistics;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.service.KseeService;
import com.junhsue.ksee.utils.CommonAskUtils;
import com.junhsue.ksee.utils.CommonUtils;
import com.junhsue.ksee.utils.DefinedStatisticsManager;
import com.junhsue.ksee.utils.SharedPreferencesUtils;
import com.tencent.stat.MtaSDkException;
import com.tencent.stat.StatConfig;
import com.tencent.stat.StatReportStrategy;
import com.tencent.stat.StatService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class SplashActivity extends BaseActivity {

    public String APP_PATH;
    private String VIDEO_NAME = "s.mp4";

    private Handler handler = new Handler();

    @Override
    protected void onReceiveArguments(Bundle bundle) {
    }

    @Override
    protected int setLayoutId() {
        return R.layout.act_welcome;
    }

    @Override
    protected void onInitilizeView() {
//      if (Build.VERSION.SDK_INT >= 23) {
//        String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
//        ActivityCompat.requestPermissions(this, mPermissionList, 123);
//      }
        APP_PATH = getApplicationContext().getFilesDir().getAbsolutePath();

//      SystemClock.sleep(1000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /**
                 * 设置是否为首次登陆
                 */
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isFirstLogin();
                    }
                });
            }
        }, 1000);


        initStatisticsTime();
        // 启动的service
        Intent service = new Intent(this, KseeService.class);
        startService(service);
//        configYingYongBao();


    }




    @Override
    protected void onResume() {
        super.onResume();
        //统计启动次数,打包之前要修改渠道包
        CommonAskUtils.statisticsAppStart(MyApplication.getConfigChannel(), Constants.APP_RESTART_LAUNCH);
    }

    /**
     * 初始化应用宝统计
     *
     * @param isDebugMode
     */
    private void initMTAConfig(boolean isDebugMode) {
        Trace.d("isDebugMode:" + isDebugMode);

        StatConfig.setReportEventsByOrder(true);
        if (isDebugMode) {
            StatConfig.setDebugEnable(true);

            StatConfig.setAutoExceptionCaught(false);

        } else {

            StatConfig.setDebugEnable(false);

            StatConfig.setAutoExceptionCaught(true);

            StatConfig.setStatSendStrategy(StatReportStrategy.PERIOD);

            StatConfig.setSendPeriodMinutes(10);

            StatConfig.initNativeCrashReport(this, null);
        }
    }


    /**
     * 初始化统计时间的存储
     */
    private void initStatisticsTime() {
        long time = SharedPreferencesUtils.getInstance().getLong(Constants.STATISTICS_LAST_TIME, 0);
        if (time == 0) {
            SharedPreferencesUtils.getInstance().putLong(Constants.STATISTICS_LAST_TIME, System.currentTimeMillis());
        }
    }

    private void isFirstLogin() {

        traceFirstStart();

        boolean isFirst = SharedPreferencesUtils.getInstance().getBoolean(Constants.ISFIRST_LOGIN, true);
        if (!isFirst) {
            finish();
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        } else {
            if (EMClient.getInstance().isLoggedInBefore()) { //环信退出登录
                EMClient.getInstance().logout(true);
            }
            loadData();
        }
    }

    /**
     * 追踪是否是第一次启动
     */
    private void traceFirstStart() {
        boolean isFirstStart = SharedPreferencesUtils.getInstance().getBoolean(Constants.ISFIRST_START, true);
        if (isFirstStart) {
            staticFirstInfo();//统计首次打开应用的设备数据
        }
    }


    protected void loadData() {
        //初始化welcome_media.mp4文件。如果内存卡存在则直接播放，如果不存在则从资源文件中读取写入内存卡
        if (!new File(APP_PATH + VIDEO_NAME).exists()) {
            try {
                //输入流
                InputStream in = getApplicationContext().getResources().openRawResource(R.raw.ss);
                //输出流
                OutputStream out = new FileOutputStream(APP_PATH + VIDEO_NAME);
                //将资源文件welcome_media.mp3写入到sd卡
                byte[] buffer = new byte[1024];
                int length;

                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
                out.flush();       //刷新
                out.close();        //关闭
                in.close();

                Log.i("SplashActivity", "mp4写入成功");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                File file = new File(APP_PATH + VIDEO_NAME);
                if (file.exists()) {
                    //如果视频写入成功，则打开引导页面
                    Intent intent = new Intent(SplashActivity.this, Splash_GuidanceActivity.class);
                    intent.putExtra("VideoPath", APP_PATH + VIDEO_NAME);
                    startActivity(intent);
                } else {
                    //如果视频写入不成功，则跳过引导页，直接打开程序主界面
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        }, 1000);
    }


    /**
     * 统计首次登录手机相关信息
     */
    private void staticFirstInfo() {

        String uuid = CommonUtils.getInstance().getDeviceUUID();
        String device_info = CommonUtils.getInstance().getDeviceInfo();
        String os_info = CommonUtils.getInstance().getDeviceVersion();
        String app_version = CommonUtils.getInstance().getAppVersionName();
        String channel = MyApplication.getConfigChannel();
        OKHttpStatistics.getInstance().uploadFirstOpenAppAction(DefinedStatisticsManager.PLATFORM_ID, channel, uuid, device_info, os_info, app_version, new RequestCallback<ResultEntity>() {

            @Override
            public void onError(int errorCode, String errorMsg) {
                Trace.d("==statistics" + errorCode);

            }

            @Override
            public void onSuccess(ResultEntity response) {
                SharedPreferencesUtils.getInstance().putBoolean(Constants.ISFIRST_START, false);
            }
        });
    }

    /**
     *
     */
    public void configYingYongBao() {
        //暂时停用
        /*配置应用宝业务数据统计*/
        StatService.setContext(this.getApplication());
        initMTAConfig(true);
        // 初始化并启动MTA
        // 第三方SDK必须按以下代码初始化MTA，其中appkey为规定的格式或MTA分配的代码。
        // 其它普通的app可自行选择是否调用
        try {
            // 第三个参数必须为：com.tencent.stat.common.StatConstants.VERSION
            StatService.startStatService(this, Constants.YINGYONGBAO_APP_KEY,
                    com.tencent.stat.common.StatConstants.VERSION);
        } catch (MtaSDkException e) {
            // MTA初始化失败
            Trace.d("MTA start failed.");
        }

    }


}
