package com.junhsue.ksee;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.dto.SettingNickNameDTO;
import com.junhsue.ksee.net.api.OkHttpILoginImpl;
import com.junhsue.ksee.net.callback.GetVerifyCodeCallback;
import com.junhsue.ksee.net.callback.PhonenumberExistCallback;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.callback.VerifyCodeVerifySuccessCallback;
import com.junhsue.ksee.utils.CommonUtils;
import com.junhsue.ksee.utils.LoginUtils;
import com.junhsue.ksee.utils.ToastUtil;

public class WeChatBindPhoneNumberActivity extends BaseActivity implements PhonenumberExistCallback,GetVerifyCodeCallback,VerifyCodeVerifySuccessCallback {

    private View mViewLine;
    private EditText mEditPassword,mEditPhone,mEditVerifyCode;
    private TextView mTvTitle,mTvNextStup,mBtnGetVerifyCode,mTvTitlePassword;
    private CheckBox mCbPasswordeye;

    private String phonenumber,nickname,unionid,avatar;
    // 获取验证码后返回的返回值id（）
    private String msg_id = "";

    private Context mContext;
    private VerifyCodeVerifySuccessCallback callback = this;


    //倒计时时间
    private int time = 60;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
        switch (msg.what){
            case 0: //发送验证码倒计时
                time--;
                if (time == 0){
                    mBtnGetVerifyCode.setClickable(true);
                    mBtnGetVerifyCode.setText("获取验证码");
                    time = 60;
                }else {
                    mBtnGetVerifyCode.setText("重新获取 "+time+"s");
                    handler.sendEmptyMessageDelayed(0,1000);
                }
                break;
            case 1:
                time = 1;
                handler.sendEmptyMessage(0);
                ToastUtil.getInstance(mContext).setContent("验证成功").setShow();
                break;
        }
        return false;
        }
    });

    @Override
    protected void onReceiveArguments(Bundle bundle) {
        phonenumber = bundle.getString(Constants.REG_PHONENUMBER);
        nickname = bundle.getString(Constants.REG_NICKNAME);
        unionid = bundle.getString(Constants.REG_UNIONID);
        avatar = bundle.getString(Constants.REG_AVATAR);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.act_we_chat_bind_phone_number;
    }

    @Override
    protected void onInitilizeView() {
        
        initView();

    }

    private void initView() {
        mContext = this;
        mViewLine = findViewById(R.id.v_register1_line3);
        mEditPassword = (EditText) findViewById(R.id.edit_register1_password);
        mTvTitlePassword = (TextView) findViewById(R.id.tv_password_title);
        mCbPasswordeye = (CheckBox) findViewById(R.id.cb_register1_password);
        mViewLine.setVisibility(View.GONE);
        mEditPassword.setVisibility(View.GONE);
        mTvTitlePassword.setVisibility(View.GONE);
        mCbPasswordeye.setVisibility(View.GONE);
        mEditPhone = (EditText) findViewById(R.id.edit_register1_phone);
        mBtnGetVerifyCode = (TextView) findViewById(R.id.btn_register1_verifycode);
        mEditVerifyCode = (EditText) findViewById(R.id.edit_register1_verify);
        mTvTitle = (TextView) findViewById(R.id.tbTitle_register1);
        mTvNextStup = (TextView) findViewById(R.id.tv_register1_nextstep);
        mTvTitle.setText("关联手机号");
        mTvNextStup.setText("完成");

        mEditPhone.setText(phonenumber);
    }


    public void onclick(View view) {
        switch(view.getId()){
            case R.id.tv_register1_nextstep:
            case R.id.tbNext_step_Icon:
            case R.id.tbNext_step:  //下一步，同时将手机和密码传递到Register2进行设置
                //验证验证码信息是否正确
                phonenumber = mEditPhone.getText().toString().trim();
                if ("".equals(phonenumber)){
                    ToastUtil.getInstance(this).setContent("号码不能为空").setShow();
                    return;
                }
                alertLoadingProgress();
                LoginUtils.VerifyVerifyCode(mContext, callback, msg_id, mEditVerifyCode.getText().toString().trim());
                break;
            case R.id.btn_register1_verifycode:
                if (!CommonUtils.getIntnetConnect(mContext)){
                    ToastUtil.getInstance(mContext).setContent(Constants.INTNET_NOT_CONNCATION).setDuration(Toast.LENGTH_SHORT).setShow();
                    return;
                }
                mBtnGetVerifyCode.setClickable(false);
                phonenumber = mEditPhone.getText().toString().trim();
                LoginUtils.VerifyPhoneAccount(this,phonenumber,this);
                break;

        }
    }


    @Override
    public void booleanExist(boolean state) {
        mBtnGetVerifyCode.setClickable(true);
        if (state){  //用户不存在
            mBtnGetVerifyCode.setClickable(false);
            mBtnGetVerifyCode.setText("重新获取 "+time+"s");
            handler.sendEmptyMessageDelayed(0,1000); //1s后发送重新获取验证码倒计时
            /**
             * 暂停发送验证信息功能
             */
            LoginUtils.SendSMS(phonenumber,this);
            Trace.i("用户未注册，允许注册账号");
        }else {
            ToastUtil.getInstance(this).setContent("该手机号未被注册").setShow();
        }
    }

    @Override
    public void setClickble(boolean b) {
        mBtnGetVerifyCode.setClickable(b);
    }

    @Override
    public void getMsg_id(boolean isSuccess, String msg_id) {
        if (!isSuccess){
            ToastUtil.getInstance(mContext).setContent(msg_id).setShow();
            time = 1;
            return;
        }
        this.msg_id= msg_id;
    }

    @Override
    public void getVerifyState(boolean state) {
        dismissLoadingDialog();
        if (!state){  //验证不成功，提示验证码错误信息
            time = 1;
            handler.sendEmptyMessageDelayed(0,1000);
            ToastUtil.getInstance(this).setContent("验证码错误").setShow();
            return;
        }
        Trace.i("验证码输入正确");
        time=1;
        handler.sendEmptyMessageDelayed(1,1000);

        OkHttpILoginImpl.getInstance().wechatBindPhoneNumber(unionid, phonenumber, avatar, nickname, new RequestCallback<SettingNickNameDTO>() {
            @Override
            public void onError(int errorCode, String errorMsg) {
                ToastUtil.getInstance(mContext).setContent("关联失败").setShow();
                finish();
            }

            @Override
            public void onSuccess(SettingNickNameDTO response) {
                ToastUtil.getInstance(mContext).setContent("关联成功").setShow();
                Intent intentToLogin = new Intent(mContext, LoginActivity.class);
                intentToLogin.putExtra(Constants.REG_PHONENUMBER,phonenumber);
                startActivity(intentToLogin);
                finish();
            }
        });
    }
}
