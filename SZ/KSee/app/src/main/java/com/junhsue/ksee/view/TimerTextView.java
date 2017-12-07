package com.junhsue.ksee.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 记时TextView
 * Created by Sugar on 17/6/19.
 */

public class TimerTextView extends TextView {

    private Context mContext;

    private Timer timer;
    private TimerTask task;
    //启动时段
    private int currentTime = 0;
    //默认时长60秒
    private int timerLength = 60;

    public TimerTextView(Context context) {
        super(context, null);
    }

    public TimerTextView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public TimerTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        setText(currentTime + "''");
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 3:
                    setText(getCurrentTimer() + "''");
                    Log.e("==","===timerLength:"+timerLength+"==getCurrentTimer:"+getCurrentTimer());
                    if (timerLength == getCurrentTimer()) {
                        stopTask();
                    }
                    break;
            }
            return false;
        }
    });


    public void setMaxTimerLength(int maxCount) {

        this.timerLength = maxCount;
        Log.e("==","===timerLength:"+maxCount);

    }

    public int getCurrentTimer() {
        return this.currentTime;
    }

    public void setCurrentTimer(int time) {
        this.currentTime = time;
    }

    /**
     * 开始倒计时任务
     */
    public void startTimerTask() {
        if (timer == null) {
            timer = new Timer();
        }

        if (task != null) {
            return;
        }
        if (task == null) {
            task = new TimerTask() {

                @Override
                public void run() {
                    currentTime++;
                    Message message = new Message();
                    message.what = 3;
                    handler.sendMessage(message);
                }
            };
        }
        if (timer != null && task != null) {
            timer.schedule(task, 0, 1000);
        }
    }


    /**
     * 停止计时器
     */
    public void stopTask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        if (task != null) {
            task.cancel();
            task = null;
        }
        setCurrentTimer(0);
    }

    public void cancelTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

}
