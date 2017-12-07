package com.junhsue.ksee.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.junhsue.ksee.R;
import com.junhsue.ksee.entity.OrderDetailsEntity;
import com.junhsue.ksee.utils.DateUtils;

import java.util.Timer;
import java.util.TimerTask;

import cn.iwgang.countdownview.CountdownView;
import cn.iwgang.countdownview.DynamicConfig;

/**
 * 订单状态View 显示
 * Created by longer on 17/4/11.
 */

public class OrderStatusView extends FrameLayout {


    private Context mContext;
    //订单状态
    private int mOrderStatus = 1;
    //
    private LinearLayout mLLContainer;
    //交易状态
    private TextView mTxtOrderStatus;
    //
    private TextView mTxtOrderHint;

    //订单倒计时
    //private TextView mTxtTime;
    //订单计时的秒数
    private long mSeconds;

    private ITimerOnlistener mTimerOnlistener;

    private CountdownView mCountDownTime;
    //支付倒计时
    //private Timer mTimer = new Timer();

    public OrderStatusView(Context context) {
        super(context);
        this.mContext = context;
        initializeView(context);
    }


    public OrderStatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initializeView(context);
    }

    public OrderStatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initializeView(context);
    }


    private void initializeView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.component_order_status_head, this);
        mLLContainer = (LinearLayout) view.findViewById(R.id.ll_container);
        mTxtOrderStatus = (TextView) view.findViewById(R.id.txt_order_status);
        mTxtOrderHint=(TextView)view.findViewById(R.id.txt_order_hint);
        mCountDownTime=(CountdownView)view.findViewById(R.id.count_donw_view);
        //
        //mTxtTime = (TextView) view.findViewById(R.id.txt_time);
        //
        initProperty();
        setOrderStatus(2);
    }


    /**
     * 根据相应的订单状态设置不同的样式
     *
     * @param orderStatus
     */
    public void setOrderStatus(int orderStatus) {
        this.mOrderStatus = orderStatus;
        fillOrderHead(orderStatus);
    }


    /**
     * 填充订单头部
     **/
    private void fillOrderHead(int orderStatus) {


        switch (orderStatus) {

            case OrderDetailsEntity.OrderStatus.PAY_OK://已支付
                mTxtOrderStatus.setText(mContext.getString(R.string.msg_order_pay_ok));
                mLLContainer.setBackgroundColor(mContext.getResources().getColor(R.color.c_green_59b197));
                //mTxtTime.setVisibility(View.GONE);
                mTxtOrderHint.setVisibility(View.GONE);
                break;
            case OrderDetailsEntity.OrderStatus.PAY_NO://待支付
                mTxtOrderStatus.setText(mContext.getString(R.string.msg_order_pay_no));
                //mTxtTime.setVisibility(View.VISIBLE);
                mTxtOrderHint.setVisibility(View.VISIBLE);

                mLLContainer.setBackgroundColor(mContext.getResources().getColor(R.color.c_yellow_e9d09a));
                break;
            case OrderDetailsEntity.OrderStatus.PAY_ERROR:
            case OrderDetailsEntity.OrderStatus.PAY_CLOSE: //交易关闭
                mTxtOrderStatus.setText(mContext.getString(R.string.msg_order_pay_close));
                mLLContainer.setBackgroundColor(mContext.getResources().getColor(R.color.c_gray_c7cdd5));
                //mTxtTime.setVisibility(View.GONE);
                mTxtOrderHint.setVisibility(View.GONE);
                break;


        }
    }


    /**
     * @param time 订单截止的时间
     *1. 用当前时间减去订单支付的时间
     *
     *2.再转换成秒数 出
     */
    public  void startOrderTime(long time) {
        long currentTime = System.currentTimeMillis();
        mSeconds = Math.abs((time * 1000l) - currentTime);
        if (mSeconds > 0) {
            mSeconds = Math.abs((time * 1000 - currentTime));
            //直播时间超过60分钟 显示 时,分,秒, 否则只显示分钟和秒钟
            if (mSeconds > 60 * 60 * 1000) {
                DynamicConfig.Builder dynamicConfigBuilder = new DynamicConfig.Builder();
                dynamicConfigBuilder.setShowHour(true);
                dynamicConfigBuilder.setShowMinute(true);
                dynamicConfigBuilder.setShowSecond(true);
                mCountDownTime.dynamicShow(dynamicConfigBuilder.build());
            } else {
                DynamicConfig.Builder dynamicConfigBuilder = new DynamicConfig.Builder();
                dynamicConfigBuilder.setShowHour(false);
                dynamicConfigBuilder.setShowMinute(true);
                dynamicConfigBuilder.setShowSecond(true);
                mCountDownTime.dynamicShow(dynamicConfigBuilder.build());

            }
            mCountDownTime.start(mSeconds);

            mCountDownTime.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                @Override
                public void onEnd(CountdownView cv) {
                    if(null!=mTimerOnlistener){
                        mTimerOnlistener.onClose();
                        mCountDownTime.setVisibility(GONE);
                    }
                }
            });
            //mTimer.schedule(timerTask,1000,mSeconds);

        }
    }



//    TimerTask timerTask=new TimerTask(){
//
//        @Override
//        public void run() {
//            ((Activity)mContext).runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    mSeconds--;
//                    //mTxtTime.setText(DateUtils.timeStampToSeconds(mSeconds*1000));
//                    if(mSeconds<=0){
//                        cancel();
//                        setOrderStatus(OrderDetailsEntity.OrderStatus.PAY_CLOSE);
//                        mTimerOnlistener.onClose();
//                    }
//                }
//            });
//        }
//    };



    public void setTimerOnlistener(ITimerOnlistener timerOnlistener) {
        this.mTimerOnlistener = timerOnlistener;
    }


    private void initProperty() {
        DynamicConfig.Builder dynamicConfigBuilder = new DynamicConfig.Builder();
        dynamicConfigBuilder.setSuffixTextColor(Color.parseColor("#ffffffff"));
        mCountDownTime.dynamicShow(dynamicConfigBuilder.build());
    }

    /**
     * 倒计时监听
     */
    public interface  ITimerOnlistener{

        void onClose();
    }



}
