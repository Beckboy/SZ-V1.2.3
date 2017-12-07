package com.junhsue.ksee;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.net.callback.GetVerifyCodeCallback;
import com.junhsue.ksee.net.callback.PhonenumberExistCallback;
import com.junhsue.ksee.net.callback.VerifyCodeVerifySuccessCallback;
import com.junhsue.ksee.utils.CommonUtils;
import com.junhsue.ksee.utils.LoginUtils;
import com.junhsue.ksee.utils.ToastUtil;

import org.w3c.dom.Comment;

public class ResetPassword1Activity extends BaseActivity implements PhonenumberExistCallback,GetVerifyCodeCallback,VerifyCodeVerifySuccessCallback{

    private EditText mEditPhone,mEditVerifyCode;
    private TextView mBtnGetVerifyCode;

    private String phonenumber,verifyCode;
    private String msg_id = "";

    private int time = 60; //验证码重置的时间间隔
    private final int FORGETPASSWORD = 1;

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
            }
            return false;
        }
    });

    @Override
    protected void onReceiveArguments(Bundle bundle) {
      if (null == bundle) return;
      phonenumber = bundle.getString("phonenumber");
    }

    @Override
    protected int setLayoutId() {
        return R.layout.act_reset_password1;
    }

    @Override
    protected void onInitilizeView() {
        initView();
    }

    private void initView() {
        mEditPhone = (EditText) findViewById(R.id.edit_register1_phone);
        mEditVerifyCode = (EditText) findViewById(R.id.edit_register1_password);
        mBtnGetVerifyCode = (TextView) findViewById(R.id.tbBtn_resetpassword1_vitifycode);

        if (null == phonenumber) return;
        mEditPhone.setText(phonenumber);
        mEditPhone.setEnabled(false);
    }


    public void onclick(View view) {
        switch (view.getId()){
            case R.id.tv_resetpassword1_nextstep:
            case R.id.tbNext_resetpassword1_step_imgBtn:
            case R.id.tbNext_resetpassword1_step:
                if (!CommonUtils.getIntnetConnect(this)){
                ToastUtil.getInstance(this).setContent(Constants.INTNET_NOT_CONNCATION).setDuration(Toast.LENGTH_SHORT).setShow();
                return;
                }
                // 进行验证码的验证
                phonenumber = mEditPhone.getText().toString().trim();
                verifyCode = mEditVerifyCode.getText().toString().trim();
                if ("".equals(phonenumber)){
                    ToastUtil.getInstance(this).setContent("号码不能为空").setShow();
                    return;
                }
                if ("".equals(verifyCode)) { //判断验证码不能为空
                    ToastUtil.getInstance(this).setContent("验证码不能为空").setShow();
                    return;
                }
                if ("".equals(msg_id)){
                    ToastUtil.getInstance(this).setContent("请先获取验证码").setShow();
                    return;
                }
                LoginUtils.VerifyVerifyCode(this, this, msg_id, verifyCode);
                Trace.i("msg_id:"+msg_id+"  code:"+verifyCode);
                mEditVerifyCode.setText("");
                break;
            case R.id.tbBtn_resetpassword1_back:
                finish();
                break;
            case R.id.tbBtn_resetpassword1_vitifycode:
                if (!CommonUtils.getIntnetConnect(this)){
                 ToastUtil.getInstance(this).setContent(Constants.INTNET_NOT_CONNCATION).setDuration(Toast.LENGTH_SHORT).setShow();
                 return;
                }
                mBtnGetVerifyCode.setClickable(false);
                phonenumber = mEditPhone.getText().toString().trim();
                LoginUtils.VerifyPhoneAccount(this,phonenumber,this);
                break;
        }
    }

  /**
   * 验证手机账户是否存在的接口回调
   * @param state
   */
  @Override
    public void booleanExist(boolean state) {
        mBtnGetVerifyCode.setClickable(true);
        if (!state) { //state == true ：代表账号存在
            ToastUtil.getInstance(this).setContent("该手机账号未注册").setShow();
            return;
        }
        mBtnGetVerifyCode.setClickable(false);
        mBtnGetVerifyCode.setText("重新获取 "+time+"s");
        handler.sendEmptyMessageDelayed(0,1000); //1s后发送重新获取验证码倒计时
        LoginUtils.SendSMS(phonenumber,this); //发送验证信息
        Trace.i("用户已注册，发送修改密码验证码");
    }

    @Override
    public void setClickble(boolean b) {
        mBtnGetVerifyCode.setClickable(b);
    }

    /**
   * 获取验证码成功后的接口回调
   * @param msg_id
   */
  @Override
    public void getMsg_id(boolean isSuccess, String msg_id) {
      if (!isSuccess){
          ToastUtil.getInstance(this).setContent(msg_id).setShow();
          time = 1;
          return;
      }
      this.msg_id = msg_id;  //验证码发送成功后的信息id返回码
    }

  /**
   * 验证码验证成功后的接口回调
   * @param state
   */
  @Override
    public void getVerifyState(boolean state) {
      if (!state){  //验证不成功，提示验证码错误信息
          ToastUtil.getInstance(this).setContent("验证码错误").setShow();
          return;
      }
      //界面跳转
      Intent intent = new Intent(this,ResetPassword2Activity.class);
      Bundle bundle = new Bundle();
      bundle.putString("phonenumber",phonenumber);
      intent.putExtras(bundle);
      startActivityForResult(intent,FORGETPASSWORD);
//      isVerify = false;
      time = 1;
    }

    @Override
    protected void onDestroy() {
        if (null != handler) {
            handler.removeMessages(0);
            handler = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case FORGETPASSWORD:
                Trace.i("修改");
                if (data == null){
                    return;
                }
                Intent intent = new Intent();
                Bundle bundle = data.getExtras();
                intent.putExtras(bundle);
                setResult(RESULT_OK,intent);
                Trace.i("修改密码界面1");
                finish();
                break;
        }
    }
}
