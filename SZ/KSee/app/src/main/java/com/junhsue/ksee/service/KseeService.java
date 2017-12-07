package com.junhsue.ksee.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.utils.DefinedStatisticsManager;
import com.junhsue.ksee.utils.StringUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Sugar on 17/1/9.
 */

public class KseeService extends Service {
    private Context mContext;
    private BroadcastReceiver registerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                return;
            }
            if (mContext == null) {
                mContext = KseeService.this;
            }
            String action = intent.getAction();
            if (StringUtils.isBlank(action)) {
                return;
            }
            if (Constants.STATISTICS_INFO.equals(action)) {
                //当app从后台进入前台，上传埋点统计文件
                DefinedStatisticsManager.getInstance().uploadStatisticsFile();
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = KseeService.this;
        registerStatisticsBroadcast();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public void registerStatisticsBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.STATISTICS_INFO);
        registerReceiver(registerReceiver, intentFilter);
    }

}
