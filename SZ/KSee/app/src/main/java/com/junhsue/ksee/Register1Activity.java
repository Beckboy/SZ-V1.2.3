package com.junhsue.ksee;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.dto.RegisterSuccessDTO;
import com.junhsue.ksee.net.api.OkHttpILoginImpl;
import com.junhsue.ksee.net.callback.GetVerifyCodeCallback;
import com.junhsue.ksee.net.callback.PhonenumberExistCallback;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.callback.VerifyCodeVerifySuccessCallback;
import com.junhsue.ksee.utils.CommonUtils;
import com.junhsue.ksee.utils.LoginUtils;
import com.junhsue.ksee.utils.SHA256Encrypt;
import com.junhsue.ksee.utils.ToastUtil;

public class Register1Activity extends BaseActivity implements GetVerifyCodeCallback ,PhonenumberExistCallback,VerifyCodeVerifySuccessCallback{

    private EditText mEditPhone,mEditVerifyCode,mEditPassword;
    public TextView mBtnGetVerifyCode,mTvTitle;
    private CheckBox mCbPasswordeye;
    private TextView mTvNextStup;

    // 注册的手机号码
    private String phonenumber,password;
    // 获取验证码后返回的返回值id（）
    private String msg_id = "";
    // 验证码
    private boolean code = false;
    //微信登录的用户信息
    private boolean isWeChat = false;
    private String nickname,avatar,sex,unionid;

    private Context mContext;
    private VerifyCodeVerifySuccessCallback callback = this;
    private PopupWindow mPopWindow;

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
            code = true;
            mBtnGetVerifyCode.setText("恭喜：验证成功");
            mBtnGetVerifyCode.setClickable(false);
            mEditPhone.setEnabled(false);
            mEditVerifyCode.setEnabled(false);
            break;
        }
        return false;
        }
    });

    @Override
    protected void onReceiveArguments(Bundle bundle) {
        if (!bundle.isEmpty()){
            isWeChat = true;
            nickname = bundle.getString(Constants.REG_NICKNAME);
            avatar = bundle.getString(Constants.REG_AVATAR);
//            sex = bundle.getString(Constants.REG_SEX);
            unionid = bundle.getString(Constants.REG_UNIONID);
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.act_register1;
    }

    @Override
    protected void onInitilizeView() {
        intView();
    }

    private void intView() {

        mContext = this;

        mEditPhone = (EditText) findViewById(R.id.edit_register1_phone);
        mEditVerifyCode = (EditText)findViewById(R.id.edit_register1_verify);
        mEditPassword = (EditText)findViewById(R.id.edit_register1_password);
        mBtnGetVerifyCode = (TextView) findViewById(R.id.btn_register1_verifycode);
        mTvTitle = (TextView) findViewById(R.id.tbTitle_register1);
        mCbPasswordeye = (CheckBox) findViewById(R.id.cb_register1_password);
        mTvNextStup = (TextView) findViewById(R.id.tv_register1_nextstep);
        mTvNextStup.setText("完成");
        mCbPasswordeye.setOnCheckedChangeListener(eyeChangeListener);
        if (isWeChat){
            mTvTitle.setText("完善资料");
        }else {
            mTvTitle.setText("注册");
        }

        mEditVerifyCode.addTextChangedListener(new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
          @Override
          public void onTextChanged(CharSequence s, int start, int before, int count) {}
          @Override
          public void afterTextChanged(Editable s) {
            if ("".equals(msg_id)){
              ToastUtil.getInstance(mContext).setContent("请先获取验证码").setShow();
              return;
            }
            if (s.length() == 6){
                if (!CommonUtils.getIntnetConnect(mContext)){
                    ToastUtil.getInstance(mContext).setContent(Constants.INTNET_NOT_CONNCATION).setDuration(Toast.LENGTH_SHORT).setShow();
                    return;
                }
              LoginUtils.VerifyVerifyCode(mContext, callback, msg_id, s+"");
            }
          }
        });
    }

    /**
     * 密码的显示和隐藏
     */
    CheckBox.OnCheckedChangeListener eyeChangeListener = new CheckBox.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked){
                Trace.i("显示密码");
                mEditPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }else {
                Trace.i("隐藏密码");
                mEditPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    };

    public void onclick(View view) {
        switch(view.getId()){
         case R.id.tv_register1_nextstep:
         case R.id.tbNext_step_Icon:
         case R.id.tbNext_step:  //下一步，同时将手机和密码传递到Register2进行设置
             //验证验证码信息是否正确
             phonenumber = mEditPhone.getText().toString().trim();
             password = mEditPassword.getText().toString().trim();
             if ("".equals(phonenumber)){
               ToastUtil.getInstance(this).setContent("号码不能为空").setShow();
                 return;
             }
             if (!code){
                 ToastUtil.getInstance(this).setContent("验证码验证失败").setShow();
                 return;
             }
             if (!LoginUtils.setPasswordPass(this,password)) { //判断密码格式是否符合要求
                 return;
             }
             mEditPhone.setEnabled(true);
             mEditVerifyCode.setEnabled(true);
             code = false;
             time = 1;
             handler.sendEmptyMessageDelayed(0,1000);
             if (isWeChat){
                 toRegister(Constants.REG_TYPE_W, unionid,phonenumber,password,nickname,null,avatar);
             }else {
                 toRegister(Constants.REG_TYPE_P, null,phonenumber,password,null,null,null);
             }

             /**
              * 改版——取消第二层注册界面
              */
             //界面跳转
             Bundle bundle = new Bundle();
             Class context = null;
             if (true) return;

            if (isWeChat){
                context = Register3Activity.class;
                bundle.putString(Constants.REG_NICKNAME, nickname);
                bundle.putString(Constants.REG_AVATAR, avatar);
//                bundle.putString(Constants.REG_SEX, sex);
                bundle.putString(Constants.REG_UNIONID, unionid);
            }else {
                context = Register2Activity.class;
            }
            Intent intent = new Intent(this,context);
            bundle.putString(Constants.REG_PHONENUMBER, phonenumber);
            bundle.putString(Constants.REG_PASSWORD, password);
            intent.putExtras(bundle);
//            startActivity(intent);
            mEditPassword.setText("");
            mEditVerifyCode.setText("");
            break;
        case R.id.btn_register1_login:
            startActivity(new Intent(Register1Activity.this,LoginActivity.class));
            finish();
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
        case R.id.btn_bind_cancle:
            mPopWindow.dismiss();
            break;
        case R.id.btn_bind_true:
            Intent intentToWeChatBind = new Intent(mContext,WeChatBindPhoneNumberActivity.class);
            intentToWeChatBind.putExtra(Constants.REG_UNIONID,unionid);
            intentToWeChatBind.putExtra(Constants.REG_PHONENUMBER,phonenumber);
            intentToWeChatBind.putExtra(Constants.REG_NICKNAME,nickname);
            intentToWeChatBind.putExtra(Constants.REG_AVATAR,avatar);
            startActivity(intentToWeChatBind);
            mPopWindow.dismiss();
            break;

        }
    }

    /**
     * 微信绑定界面直接注册
     */
    private void toRegister(String logintype, String union_id, final String phone_number, String pass_word, final String nick_name, String organization, final String avatar) {
        password = SHA256Encrypt.bin2hex(pass_word); //SHA256加密

        OkHttpILoginImpl.getInstance().registerByPhonenumberOrWechat(logintype, union_id, phone_number, password, nick_name, null, avatar, null, new RequestCallback<RegisterSuccessDTO>() {
            @Override
            public void onError(int errorCode, String errorMsg) {
                ToastUtil.getInstance(mContext).setContent(errorMsg).setShow();
            }

            @Override
            public void onSuccess(RegisterSuccessDTO response) {
                ToastUtil.getInstance(mContext).setContent("注册成功").setShow();
                Intent intent2Finish = new Intent(mContext,RegisterFinishActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.REG_NICKNAME,nick_name);
                bundle.putString(Constants.REG_PHONENUMBER,phone_number);
                bundle.putString("organization","");
                bundle.putString(Constants.REG_AVATAR,avatar);
                intent2Finish.putExtras(bundle);
                startActivity(intent2Finish);
            }
        });
    }

    //获取验证码后返回的返回值(msg_id)
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
    public void booleanExist(boolean state) {
      mBtnGetVerifyCode.setClickable(true);
        if (!state){  //用户不存在
            mBtnGetVerifyCode.setClickable(false);
            mBtnGetVerifyCode.setText("重新获取 "+time+"s");
            handler.sendEmptyMessageDelayed(0,1000); //1s后发送重新获取验证码倒计时
          /**
           * 暂停发送验证信息功能
           */
            LoginUtils.SendSMS(phonenumber,this);
            Trace.i("用户未注册，允许注册账号");
        }else {
            ToastUtil.getInstance(this).setContent("该手机号已被注册").setShow();
            if (isWeChat) {
                toastToBindPhoneNumber();
            }
        }
    }

    private void toastToBindPhoneNumber() {
        //设置contentView
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_bind_phonenumber,null);
        mPopWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,true);
        mPopWindow.setContentView(contentView);

        //显示PopWindow
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.act_register1,null);
        mPopWindow.showAtLocation(rootView, Gravity.BOTTOM,0,0);
    }

    @Override
    public void setClickble(boolean b) {
        mBtnGetVerifyCode.setClickable(b);
    }

    /**
   * 验证码验证成功的接口回调
   * @param state
   */
  @Override
    public void getVerifyState(boolean state) {
      if (!state){  //验证不成功，提示验证码错误信息
        ToastUtil.getInstance(this).setContent("验证码错误").setShow();
        return;
      }
    Trace.i("验证码输入正确");
    time=1;
    handler.sendEmptyMessageDelayed(1,1000);
    }

}
