package com.junhsue.ksee.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.junhsue.ksee.utils.DensityUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 录音分贝的长度
 * Created by hunter_J on 2017/6/16.
 */

public class VoiceDecibelLengthAnimView extends LinearLayout {

    private Context mContext;
    //分贝,默认初始值为4
    private int voice_db = 4;
    //分贝的一边方向的条目数
    private int view_count = 10;
    //
    private int count = view_count;
    //
    private TextView tvCountDownTxt;

    private Timer timer;
    private TimerTask task;
    //倒计时默认60秒
    private int maxCount = 60;

    private RecordVoiceFinishListener recordVoiceFinishListener;

    public VoiceDecibelLengthAnimView(Context context) {
        super(context, null);
    }

    public VoiceDecibelLengthAnimView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init(context, null);
    }

    public VoiceDecibelLengthAnimView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 初始化
     *
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        this.mContext = context;
        for (int i = 0; i < view_count; i++) {
            TextView tv = new TextView(context);
            LayoutParams params1 = new LayoutParams(DensityUtil.px2dip(mContext, 20), voice_db);
            params1.setMargins(10, 0, 0, 0);
            tv.setLayoutParams(params1);
            tv.setId(i + 1);
            tv.setBackgroundColor(Color.RED);
            this.addView(tv);
        }

        tvCountDownTxt = new TextView(context);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 0, 20, 0);
        tvCountDownTxt.setLayoutParams(params);
        tvCountDownTxt.setText("60''");
        this.addView(tvCountDownTxt);

        for (int i = view_count - 1; i >= 0; i--) {
            TextView tv = new TextView(context);
            LayoutParams params2 = new LayoutParams(DensityUtil.px2dip(mContext, 20), voice_db);
            params2.setMargins(10, 0, 0, 0);
            tv.setLayoutParams(params2);
            tv.setId(i + 11);
            tv.setBackgroundColor(Color.RED);
            this.addView(tv);
        }
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (count == 0) {
                        handler.removeMessages(0);
                        break;
                    }
                    count--;
                    xh();
                    handler.sendEmptyMessageAtTime(0, 100);
                    break;
                case 1:
                    tvCountDownTxt.setText(getMaxCount() + "''");
                    if (0 == getMaxCount()) {
                        stopTask();
                        //
                        if (recordVoiceFinishListener != null) {

                            recordVoiceFinishListener.recordVoiceEnd();
                        }
                    }
                    break;
            }
            return false;
        }
    });

    /**
     * 重置高度
     *
     * @param height
     */
    public void setResetTVHeight(int height) {
        count = view_count;
        handler.removeMessages(0);
        setFirstHeight(height + voice_db);
        Message msg = new Message();
        msg.what = 0;
        handler.sendMessageDelayed(msg, 100);
    }

    public void xh() {
        for (int i = 0; i < view_count; i++) {

            if (i == view_count - 1) {
                View v_l = this.getChildAt(i);
                View v_r = this.getChildAt(view_count * 2 - i);
                LayoutParams lp_l = (LayoutParams) v_l.getLayoutParams();
                LayoutParams lp_r = (LayoutParams) v_r.getLayoutParams();
                lp_l.height = voice_db;
                lp_r.height = voice_db;
                v_l.setLayoutParams(lp_l);
                v_r.setLayoutParams(lp_r);
            } else {
                View v_l = this.getChildAt(i);
                View v_r = this.getChildAt(view_count * 2 - i);
                LayoutParams lp_l = (LayoutParams) v_l.getLayoutParams();
                LayoutParams lp_r = (LayoutParams) v_r.getLayoutParams();
                lp_l.height = this.getChildAt(i + 1).getHeight();
                lp_r.height = this.getChildAt(view_count * 2 - i - 1).getHeight();
                v_l.setLayoutParams(lp_l);
                v_r.setLayoutParams(lp_r);
            }
        }
    }

    /**
     * @param height 高度
     */
    private void setFirstHeight(int height) {
        View v_l = this.getChildAt(view_count - 1);
        View v_r = this.getChildAt(view_count + 1);
        LayoutParams lp_l = (LayoutParams) v_l.getLayoutParams();
        LayoutParams lp_r = (LayoutParams) v_r.getLayoutParams();
        lp_l.height = height;
        lp_r.height = height;
        v_l.setLayoutParams(lp_l);
        v_r.setLayoutParams(lp_r);
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public int getMaxCount() {
        return this.maxCount;
    }

    /**
     * 开始倒计时任务
     */
    public void startCountdownTimerTask() {
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
                    maxCount--;
                    Message message = new Message();
                    message.what = 1;
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
        setMaxCount(60);
        tvCountDownTxt.setText("60''");
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


    public interface RecordVoiceFinishListener {

        void recordVoiceEnd();//录音结束

    }

    public void setRecordVoiceFinishListener(RecordVoiceFinishListener recordVoiceFinishListener) {

        this.recordVoiceFinishListener = recordVoiceFinishListener;

    }


}
