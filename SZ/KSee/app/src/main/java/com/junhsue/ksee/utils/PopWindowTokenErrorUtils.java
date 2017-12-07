package com.junhsue.ksee.utils;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.junhsue.ksee.LoginActivity;
import com.junhsue.ksee.R;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.frame.MyApplication;
import com.junhsue.ksee.frame.ScreenManager;
import com.junhsue.ksee.profile.UserProfileService;

/**
 * Created by hunter_J on 2017/7/7.
 * 登录态异常(token错误)时的弹吐框
 */

public class PopWindowTokenErrorUtils {


    private static PopupWindow popupWindowLoginFail;

    private static PopWindowTokenErrorUtils instance;

    private Activity context;

    private PopWindowTokenErrorUtils(Activity context) {
        this.context = context;
    }

    public static PopWindowTokenErrorUtils getInstance(Activity context) {
            instance = new PopWindowTokenErrorUtils(context);
        return instance;
    }


    /**
     * token失效的PopWindow对话框
     */
    public void showPopupWindow(int layoutId) {

        //设置contentView
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_main_login_fail_pop, null);
        popupWindowLoginFail = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindowLoginFail.setContentView(contentView);

        Button tv = (Button) contentView.findViewById(R.id.btn_autologin_fail_exit);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("onclick","点击时间");
                UserProfileService.getInstance(context).updateCurrentLoginUser(null);
                ScreenManager.getScreenManager().popAllActivity();
                context.startActivity(new Intent(context, LoginActivity.class));
            }
        });
        View rootView = context.getWindow().getDecorView().findViewById(android.R.id.content);
        popupWindowLoginFail.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
        Trace.i("token已经有问题了哦"+context.getLocalClassName()+rootView.toString());
    }


}
