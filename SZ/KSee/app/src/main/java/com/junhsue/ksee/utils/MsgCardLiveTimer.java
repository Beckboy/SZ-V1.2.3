package com.junhsue.ksee.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.junhsue.ksee.R;

import java.util.Timer;
import java.util.TimerTask;

import cn.iwgang.countdownview.CountdownView;
import cn.iwgang.countdownview.DynamicConfig;

/**
 * 卡片直播倒计时
 * Created by longer on 17/6/28.
 */

public class MsgCardLiveTimer {


    private Context mContext;
    //
    private CountdownView mCountdownView;


    private Timer mTimer;

    private long mSeconds;

    public MsgCardLiveTimer(Context context, CountdownView countdownView) {
        this.mContext = context;
        this.mCountdownView = countdownView;
        initProperty();
    }


    /**
     * @param time 开始的时间
     *             <p>
     *             2.再转换成秒数 出
     *             <p>
     *             服务器的时间 -当前时间
     */

    public void startTime(long time) {
        mTimer = new Timer();
        long currentTime = System.currentTimeMillis();
        mSeconds = (time * 1000 - currentTime);
        if (mSeconds > 0) {
            mSeconds = Math.abs((time * 1000 - currentTime));
            //直播时间超过30分钟 显示 时,分,秒, 否则只显示分钟和秒钟
            if(mSeconds>15*60*1000){
                DynamicConfig.Builder dynamicConfigBuilder = new DynamicConfig.Builder();
                dynamicConfigBuilder.setShowHour(true);
                dynamicConfigBuilder.setShowMinute(true);
                dynamicConfigBuilder.setShowSecond(true);
                mCountdownView.dynamicShow(dynamicConfigBuilder.build());
            }else{
                DynamicConfig.Builder dynamicConfigBuilder = new DynamicConfig.Builder();
                dynamicConfigBuilder.setShowHour(false);
                dynamicConfigBuilder.setShowMinute(true);
                dynamicConfigBuilder.setShowSecond(true);
                mCountdownView.dynamicShow(dynamicConfigBuilder.build());

            }

            mCountdownView.start(mSeconds);
        }


    }


    private void initProperty() {
        DynamicConfig.Builder dynamicConfigBuilder = new DynamicConfig.Builder();
        dynamicConfigBuilder.setSuffixTextColor(Color.parseColor("#ffffffff"));
        mCountdownView.dynamicShow(dynamicConfigBuilder.build());
    }

}
