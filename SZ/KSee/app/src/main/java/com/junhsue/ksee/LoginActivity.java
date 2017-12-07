package com.junhsue.ksee;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.hyphenate.chat.EMClient;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.common.IntentLaunch;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.entity.UserInfo;
import com.junhsue.ksee.frame.MyApplication;
import com.junhsue.ksee.frame.ScreenManager;
import com.junhsue.ksee.net.api.OkHttpILoginImpl;
import com.junhsue.ksee.net.callback.GetVerifyCodeCallback;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.profile.UserProfileService;
import com.junhsue.ksee.utils.CommonUtils;
import com.junhsue.ksee.utils.InputUtil;
import com.junhsue.ksee.utils.LoginUtils;
import com.junhsue.ksee.utils.SHA256Encrypt;
import com.junhsue.ksee.utils.ScreenWindowUtil;
import com.junhsue.ksee.utils.StringUtils;
import com.junhsue.ksee.utils.ToastUtil;

import java.io.File;

public class LoginActivity extends BaseActivity implements GetVerifyCodeCallback {


    //登录全局
    private RadioGroup mRg;
    private RadioButton mRbQuick,mRbPass;
    private View mVQuick,mVPass;
    private VideoView videoView;
    private Button mBtnLogin;
    private RelativeLayout mRlBottom;

    //快速登录
    private EditText mEditphone_Q,mEditPassword_Q;
    private TextView mTvVitifycode_Q;
    private TextView mTvLine1_Q,mTvLine2_Q;
    private ImageButton mBtnCleanPhone_Q;

    //密码登录
    private EditText mEditphone_P,mEditPassword_P;
    private CheckBox mCbPasswordeye_P;
    private TextView mTvLine1_P,mTvLine2_P;
    private ImageButton mBtnCleanPhone_P;

    private Context mContext;
    private String phonenumber = "";
    private String password;

    private final int FORGETPASSWORD = 0;  //忘记密码界面跳转的请求码

    //倒计时时间
    private int time = 60;
    // 获取验证码后返回的返回值id（）
    private String msg_id = "";
    // 快速登录——验证码
    private String vetityCode = "";

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 0: //发送验证码倒计时
                    time--;
                    if (time == 0){
                        mTvVitifycode_Q.setClickable(true);
                        mTvVitifycode_Q.setText("获取验证码");
                        time = 60;
                    }else {
                        mTvVitifycode_Q.setText("重新获取 "+time+"s");
                        handler.sendEmptyMessageDelayed(0,1000);
                    }
                    break;
                case 1:
                    mTvVitifycode_Q.setText("恭喜：验证成功");
                    mTvVitifycode_Q.setClickable(false);
                    break;
            }
            return false;
        }
    });


    @Override
    protected void onReceiveArguments(Bundle bundle) {
        phonenumber = bundle.getString(Constants.REG_PHONENUMBER);
        Trace.i("phonenumber:"+phonenumber);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.act_login;
    }

    @Override
    protected void onInitilizeView() {
        initView();
    }

    private void initView() {
        mContext = this;
        //登录全局
        videoView = (VideoView) this.findViewById(R.id.video_login);
        mRg = (RadioGroup) findViewById(R.id.rg_login_type);
        mRbQuick = (RadioButton) findViewById(R.id.rb_quick);
        mRbPass = (RadioButton) findViewById(R.id.rb_pass);
        mVQuick = findViewById(R.id.include_login_quick);
        mVPass = findViewById(R.id.include_login_password);
        mBtnLogin = (Button) findViewById(R.id.btn_login_jumpIn);
        mRlBottom = (RelativeLayout) findViewById(R.id.rl_login_bottom);

        //快速登录
        initQuickView();

        //密码登录
        initPassView();

        //监听
        setListener();

        HXlogout();

        //播放背景视频
        loadData();
    }

    /**
     * 快速登录初始化
     */
    private void initQuickView() {
        mEditphone_Q = (EditText) findViewById(R.id.edit_login_phone_quick);
        mEditPassword_Q = (EditText) findViewById(R.id.edit_login_password_quick);
        mTvVitifycode_Q = (TextView) findViewById(R.id.tv_login_vitifycode_quick);
        mTvLine1_Q = (TextView) findViewById(R.id.txt_login_line1_quick);
        mTvLine2_Q = (TextView) findViewById(R.id.txt_login_line2_quick);
        mBtnCleanPhone_Q = (ImageButton) findViewById(R.id.btn_login_cleanPhone_quick);
        //
        if (!"".equals(phonenumber)){
            mEditphone_Q.setText(phonenumber);
            phonenumber = "";
        }
    }

    /**
     * 密码登录初始化
     */
    private void initPassView() {
        mEditphone_P = (EditText) findViewById(R.id.edit_login_phone_pass);
        mEditPassword_P = (EditText) findViewById(R.id.edit_login_password_pass);
        mCbPasswordeye_P = (CheckBox)findViewById(R.id.cb_login_showPassword_pass);
        mTvLine1_P = (TextView) findViewById(R.id.txt_login_line1_pass);
        mTvLine2_P = (TextView) findViewById(R.id.txt_login_line2_pass);
        mBtnCleanPhone_P = (ImageButton) findViewById(R.id.btn_login_cleanPhone_pass);

        if (!"".equals(phonenumber)){
            mEditphone_P.setText(phonenumber);
            phonenumber = "";
        }
    }

    /**
     * 监听初始化
     */
    private void setListener() {
        mRg.setOnCheckedChangeListener(rgOnCheckedChangeListener);
        mCbPasswordeye_P.setOnCheckedChangeListener(eyeChangeListener);
        //快速登录输入框
        mEditphone_P.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    mBtnCleanPhone_P.setVisibility(View.VISIBLE);
                    mTvLine1_P.setBackgroundColor(getResources().getColor(R.color.c_yellow_e9d09a));
                }else {
                    mBtnCleanPhone_P.setVisibility(View.INVISIBLE);
                    mTvLine1_P.setBackgroundColor(Color.parseColor("#CCCCCC"));
                }
            }
        });
        mEditPassword_P.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    mTvLine2_P.setBackgroundColor(getResources().getColor(R.color.c_yellow_e9d09a));
                }else {
                    mTvLine2_P.setBackgroundColor(Color.parseColor("#CCCCCC"));
                }
            }
        });
        //密码登录输入框
        mEditphone_Q.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    mBtnCleanPhone_Q.setVisibility(View.VISIBLE);
                    mTvLine1_Q.setBackgroundColor(getResources().getColor(R.color.c_yellow_e9d09a));
                }else {
                    mBtnCleanPhone_Q.setVisibility(View.INVISIBLE);
                    mTvLine1_Q.setBackgroundColor(Color.parseColor("#CCCCCC"));
                }
            }
        });
        mEditPassword_Q.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    mTvLine2_Q.setBackgroundColor(getResources().getColor(R.color.c_yellow_e9d09a));
                }else {
                    mTvLine2_Q.setBackgroundColor(Color.parseColor("#CCCCCC"));
                }
            }
        });
    }


    /**
   * 退出环信
   */
  private void HXlogout() {
     if (EMClient.getInstance().isLoggedInBefore()){
         EMClient.getInstance().logout(true);
     }

  }

    /**
     * 快速登录、密码登录——切换监听
     */
  RadioGroup.OnCheckedChangeListener rgOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
          switch (checkedId){
              case R.id.rb_quick:
                  mVQuick.setVisibility(View.VISIBLE);
                  mVPass.setVisibility(View.INVISIBLE);
                  mRbQuick.setTextColor(getResources().getColor(R.color.c_gray_ccc));
                  mRbPass.setTextColor(getResources().getColor(R.color.c_gray_f5));
                  mBtnLogin.setText("快 速 登 录");
                  mRlBottom.setVisibility(View.INVISIBLE);
                  InputUtil.hideSoftInputFromWindow((Activity) mContext);
                  phonenumber = mEditphone_P.getText().toString().trim();
                  mEditphone_Q.setText(phonenumber);
                  break;
              case R.id.rb_pass:
                  mVQuick.setVisibility(View.INVISIBLE);
                  mVPass.setVisibility(View.VISIBLE);
                  mRbQuick.setTextColor(getResources().getColor(R.color.c_gray_f5));
                  mRbPass.setTextColor(getResources().getColor(R.color.c_gray_ccc));
                  mBtnLogin.setText("密 码 登 录");
                  mRlBottom.setVisibility(View.VISIBLE);
                  InputUtil.hideSoftInputFromWindow((Activity) mContext);
                  phonenumber = mEditphone_Q.getText().toString().trim();
                  mEditphone_P.setText(phonenumber);
                  break;
          }
      }
  };

    /**
     * 密码的显示和隐藏
     */
    CheckBox.OnCheckedChangeListener eyeChangeListener = new CheckBox.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked){
                Trace.i("显示密码");
                mEditPassword_P.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }else {
                Trace.i("隐藏密码");
                mEditPassword_P.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    };

    public void onclick(View view) {
        switch(view.getId()){
         case R.id.btn_login_cleanPhone_pass:
             mEditphone_P.setText("");
            break;
        case R.id.btn_login_cleanPhone_quick:
            mEditphone_Q.setText("");
            break;
        case R.id.btn_login_jumpIn:
            if (!CommonUtils.getIntnetConnect(this)){
                ToastUtil.getInstance(this).setContent(Constants.INTNET_NOT_CONNCATION).setDuration(Toast.LENGTH_SHORT).setShow();
                return;
            }
            switch (mRg.getCheckedRadioButtonId()){
                case R.id.rb_quick:
                    verifyLoginInfoByVetityCode();
                    break;
                case R.id.rb_pass:
                    verifyLoginInfo(); //验证登录信息
                    break;
            }
            break;
        case R.id.tv_login_vitifycode_quick:
            if (!CommonUtils.getIntnetConnect(this)){
                ToastUtil.getInstance(this).setContent(Constants.INTNET_NOT_CONNCATION).setDuration(Toast.LENGTH_SHORT).setShow();
                return;
            }
            //用户手机号码格式验证
            phonenumber = mEditphone_Q.getText().toString().trim();
            if (StringUtils.isBlank(phonenumber)){
                ToastUtil.getInstance(this).setContent("手机号码不能为空").setShow();
                return;
            }
            if (!StringUtils.isPhoneNumber(phonenumber)){
                ToastUtil.getInstance(this).setContent("手机号码格式错误").setShow();
                return;
            }
            mTvVitifycode_Q.setClickable(false);
            mTvVitifycode_Q.setText("重新获取 "+time+"s");
            handler.sendEmptyMessageDelayed(0,1000); //1s后发送重新获取验证码倒计时
            LoginUtils.SendSMS(phonenumber,this);
            break;
        case R.id.btn_social:
            alertLoadingProgress();
            LoginUtils.wechatLogin(this);
            break;
        case R.id.tv_register:
            startActivity(new Intent(LoginActivity.this,Register1Activity.class));
            break;
        case R.id.tv_forgetPassword:
            startActivityForResult(new Intent(LoginActivity.this,ResetPassword1Activity.class),FORGETPASSWORD);
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case FORGETPASSWORD: //忘记密码界面的跳转
                Trace.i("忘记密码界面跳回");
                if (data != null){
                    phonenumber = data.getExtras().getString("phonenumber");
                    mEditphone_P.setText(phonenumber);
                }
            break;
        }
    }

    /**
     * 快速登录——验证验证码信息
     */
    private void verifyLoginInfoByVetityCode() {
        phonenumber = mEditphone_Q.getText().toString().trim();
        vetityCode = mEditPassword_Q.getText().toString().trim();
        //用户手机号码格式验证
        if (StringUtils.isBlank(phonenumber)){
            ToastUtil.getInstance(this).setContent("手机号码不能为空").setShow();
            return;
        }
        if (!StringUtils.isPhoneNumber(phonenumber)){
            ToastUtil.getInstance(this).setContent("手机号码格式错误").setShow();
            return;
        }
        if ("".equals(msg_id)) { //判断验证码不能为空
            ToastUtil.getInstance(this).setContent("验证码不能为空").setShow();
            return;
        }
        if ("".equals(vetityCode)) { //判断验证码不能为空
            ToastUtil.getInstance(this).setContent("验证码不能为空").setShow();
            return;
        }
        time = 1;
        OkHttpILoginImpl.getInstance().loginByVerityCode(phonenumber, msg_id, vetityCode, new RequestCallback<UserInfo>() {
            @Override
            public void onError(int errorCode, String errorMsg) {
                ToastUtil.getInstance(LoginActivity.this).setContent("验证失败").setShow();
            }

            @Override
            public void onSuccess(UserInfo response) {
                //登录请求成功，将用户信息进行保存
                ScreenManager.getScreenManager().popAllActivity();
                UserProfileService.getInstance(getApplication()).updateCurrentLoginUser(response);
                //MyApplication.setToken(response.token);
                IntentLaunch.launch(LoginActivity.this,MainActivity.class);
                mEditPassword_Q.setText("");
            }
        });
    }



  /**
   * 密码登录——验证用户登录信息
   *
   */
    private void verifyLoginInfo() {
        phonenumber = mEditphone_P.getText().toString().trim();
        password = mEditPassword_P.getText().toString().trim();
        //用户手机号码格式验证
        if (!StringUtils.isBlank(phonenumber)){
            if (StringUtils.isPhoneNumber(phonenumber)){
                verifyPasswordInfo(password);
            }else {
                ToastUtil.getInstance(this).setContent("手机号码格式错误").setShow();
            }
        }else {
            ToastUtil.getInstance(this).setContent("手机号码不能为空").setShow();
        }
    }

  /**
   * 验证密码格式
   */
    private void verifyPasswordInfo(String password) {
        if (StringUtils.isBlank(password)){
          ToastUtil.getInstance(this).setContent("密码不能为空").setShow();
          return;
        }
        if (password.length()< 6 || !StringUtils.hasNum(password) || !StringUtils.hasEng(password)){
            ToastUtil.getInstance(this).setContent("密码不正确").setShow();
            return;
        }
        password = SHA256Encrypt.bin2hex(password);
        OkHttpILoginImpl.getInstance().loginByPhonenumber("1", null, phonenumber, password, new RequestCallback<UserInfo>() {
            @Override
            public void onError(int errorCode, String errorMsg) {
              ToastUtil.getInstance(LoginActivity.this).setContent("账号或密码不正确").setShow();
            }

            @Override
            public void onSuccess(UserInfo response) {
                //登录请求成功，将用户信息进行保存
                ScreenManager.getScreenManager().popAllActivity();
                UserProfileService.getInstance(LoginActivity.this).updateCurrentLoginUser(response);
                //MyApplication.setToken(response.token);
                IntentLaunch.launch(LoginActivity.this,MainActivity.class);
            }
        });
    }


    protected void loadData() {
        File file = new File(getApplicationContext().getFilesDir().getAbsolutePath()+"s.mp4");
        if (!file.exists()){
            Trace.i("视频文件不存在");
        }else {
            videoView.setVideoPath(file.getPath());
            int width = ScreenWindowUtil.getScreenWidth(this);
            int height = ScreenWindowUtil.getScreenHeight(this);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    //设置为填充父窗体
                    width,
                    height);
            //设置布局
//      videoView.setLayoutParams(layoutParams);
            //循环播放
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                }
            });
            videoView.start();
        }
    }

    @Override
    protected void onStop() {
        dismissLoadingDialog();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoView != null){
            //释放掉占用的内存
            videoView.suspend();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (null != videoView ){
            loadData();
        }
    }

    /**
     * 发送验证码的接口回调
     * @param msg_id
     */
    @Override
    public void getMsg_id(boolean isSuccess, String msg_id) {
        if (!isSuccess){
            ToastUtil.getInstance(mContext).setContent(msg_id).setShow();
            time = 1;
            return;
        }
        this.msg_id = msg_id;
    }



}
