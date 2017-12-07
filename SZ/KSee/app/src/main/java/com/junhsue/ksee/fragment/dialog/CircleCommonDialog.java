package com.junhsue.ksee.fragment.dialog;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.junhsue.ksee.R;
import com.junhsue.ksee.entity.MsgInfoEntity;

/**
 * 社区试用的各种弹窗
 * Created by Sugar on 17/11/6.
 */

public class CircleCommonDialog extends BaseDialogFragment {

    private long time;
    private int interval;
    private CountTime countTime;//计时器

    public static final String SHOW_INFO = "show_info";

    public static CircleCommonDialog newInstance(MsgInfoEntity info) {
        CircleCommonDialog circleCommonDialog = new CircleCommonDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable(SHOW_INFO, info);
        circleCommonDialog.setArguments(bundle);
        return circleCommonDialog;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.common_dialog_style);
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setGravity(Gravity.CENTER);
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.component_common_dialog_layout, null);
        getDialog().setCanceledOnTouchOutside(true);
        setTime(3000);
        startCountDownTime();//启动倒计时
        initView(view);
        return view;
    }

    private void initView(View view) {
        ImageView btn = (ImageView) view.findViewById(R.id.iv_btn);
        TextView tvMsg = (TextView) view.findViewById(R.id.tv_remind_msg);

        MsgInfoEntity showInfo = (MsgInfoEntity) getArguments().getSerializable(SHOW_INFO);

        btn.setImageResource(showInfo.drawableId);
        tvMsg.setText(showInfo.msgInfo);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        tvMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


    public void startCountDownTime() {
        cancelCountDownTime();
        if (time <= 1000) {
            return;
        }
        countTime = new CountTime(getTime(), getInterval());
        countTime.start();

    }

    private void cancelCountDownTime() {//待优化
        if (countTime == null) {
            return;
        }
        countTime.cancel();
        countTime = null;
    }


    public void setTime(long time) {
        this.time = time;
    }

    private long getTime() {
        return time;
    }

    private void setInterval(int interval) {
        this.interval = interval;
    }

    private int getInterval() {
        if (interval == 0) {
            interval = 1000;
        }
        return interval;
    }

    class CountTime extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public CountTime(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (millisUntilFinished <= 2000) {
                dismiss();
            }
        }

        @Override
        public void onFinish() {
            dismiss();
        }
    }
}

