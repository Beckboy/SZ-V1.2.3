package com.junhsue.ksee.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.junhsue.ksee.BaseActivity;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.dto.SaveQNTokenDTO;
import com.junhsue.ksee.dto.SendSMSDTO;
import com.junhsue.ksee.dto.VerifyAccountIsExistDTO;
import com.junhsue.ksee.dto.VerifyVerifyCodeDTO;
import com.junhsue.ksee.frame.MyApplication;
import com.junhsue.ksee.net.api.OkHttpILoginImpl;
import com.junhsue.ksee.net.callback.GetVerifyCodeCallback;
import com.junhsue.ksee.net.callback.PhonenumberExistCallback;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.callback.SaveImgGetTokenCallback;
import com.junhsue.ksee.net.callback.VerifyCodeVerifySuccessCallback;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;

import java.util.UUID;

/**
 * Created by hunter_J on 17/3/24.
 */

public class LoginUtils {

  private static Handler handler = new Handler();


  /**
   * 微信登录
   *
   * @param mContext
   */
  public static void wechatLogin(Context mContext) {
    if (!isWXAppInstalledAndSupported(MyApplication.getIwxapi())) {
      Toast.makeText(mContext, "您未安装微信客户端", Toast.LENGTH_SHORT).show();
      BaseActivity baseActivity = (BaseActivity) mContext;
      baseActivity.dismissLoadingDialog();
      return;
    }
    String uuid = UUID.randomUUID().toString();
    final SendAuth.Req req = new SendAuth.Req();
    req.scope = "snsapi_userinfo";
    req.state = uuid;
    Trace.i("code:调用微信");
    MyApplication.getIwxapi().sendReq(req);
  }


  public static boolean isWXAppInstalledAndSupported(IWXAPI api) {
    return api.isWXAppInstalled() && api.isWXAppSupportAPI();
  }


  /**
   * 验证手机格式是否正确
   * @param context :上下午对象
   * @param phonenumber :要验证的手机号码
   */
  public static void VerifyPhoneAccount(Context context,String phonenumber,PhonenumberExistCallback phonenumberExistCallback){
    if (StringUtils.isBlank(phonenumber)) {
      phonenumberExistCallback.setClickble(true);
      ToastUtil.getInstance(context).setContent("手机号码不能为空").setShow();
      return;
    }
    if (!StringUtils.isPhoneNumber(phonenumber)) {
      phonenumberExistCallback.setClickble(true);
      ToastUtil.getInstance(context).setContent("请输入正确的手机号码！").setShow();
      return;
    }
    verifyPhoneExist(phonenumber,phonenumberExistCallback);  //验证手机账户是否存在
  }

  /**
   * 验证手机账户是否存在
   * @param phonenumber ： 验证的手机号码
   */
  public static void verifyPhoneExist(final String phonenumber, final PhonenumberExistCallback phonenumberExistCallback) {
    OkHttpILoginImpl.getInstance().loginSearchAccount(phonenumber, new RequestCallback<VerifyAccountIsExistDTO>() {
      @Override
      public void onError(int errorCode, String errorMsg) {
        phonenumberExistCallback.booleanExist(false);
      }

      @Override
      public void onSuccess(VerifyAccountIsExistDTO response) {
        phonenumberExistCallback.booleanExist(true);
      }
    });
  }

  /**
   * 发送手机验证码
   * @param phonenumber : 接收的手机号码
   */
  public static void SendSMS(String phonenumber, final GetVerifyCodeCallback getVerifyCodeCallback) {
    OkHttpILoginImpl.getInstance().registerSendsms(phonenumber, new RequestCallback<SendSMSDTO>() {

      @Override
      public void onError(int errorCode, String errorMsg) {
         Trace.i("vetifyCode send failed info :" + errorMsg);
          getVerifyCodeCallback.getMsg_id(false,"验证码发送失败");
      }

      @Override
      public void onSuccess(SendSMSDTO response) {
        JsonObject jo1 = new JsonParser().parse(response.body).getAsJsonObject();
        String msg_id = String.valueOf(jo1.get("msg_id"));
//        去掉字符串中的""
        if(msg_id.contains("\"")){
            msg_id=msg_id.replaceAll("\"","");
        }
        Trace.i("发送成功:response msg_id:"+msg_id);
        getVerifyCodeCallback.getMsg_id(true, msg_id+"");
      }
    });
  }

  /**
   * 验证手机验证码
   * @param msg_id ：发送验证码时返回的消息id
   * @param code ：验证码
   */
  public static void VerifyVerifyCode(final Context context, final VerifyCodeVerifySuccessCallback verifyCodeVerifySuccessCallback, String msg_id, String code){
    OkHttpILoginImpl.getInstance().registerVerifysms(msg_id, code, new RequestCallback<VerifyVerifyCodeDTO>() {
      @Override
      public void onError(int errorCode, String errorMsg) {
        ToastUtil.getInstance(context).setContent("验证码错误").setShow();
        verifyCodeVerifySuccessCallback.getVerifyState(false);
      }

      @Override
      public void onSuccess(VerifyVerifyCodeDTO response) {
        JsonObject jo1 = new JsonParser().parse(response.body).getAsJsonObject();
        String valid = String.valueOf(jo1.get("is_valid"));
        if (valid.equals("true")){
          verifyCodeVerifySuccessCallback.getVerifyState(true);
        }
      }
    });
//      context.startActivity(intent);
  }

  /**
   * 验证设置的密码格式是否正确（6-24为有效密码:数字、英文大小写、特殊符号）
   * @param password : 设置的密码
   * @return true:m密码格式正确 false：密码格式错误
   */
  public static boolean setPasswordPass(Context context, String password) {
    Trace.i("password:"+password);
    if (StringUtils.isNotBlank(password)){ //密码不为空
      if (StringUtils.isValidPwd(context, password)){
        return true;
      }
    }else {
      ToastUtil.getInstance(context).setContent("密码不能为空").setShow();
    }
    return false;
  }

  /**
   * 获取七牛云上传照片的需要的token
   */
  public static void getHeadImgToken(final SaveImgGetTokenCallback saveImgGetTokenCallback){
    OkHttpILoginImpl.getInstance().GetQNToken(new RequestCallback<SaveQNTokenDTO>() {
      @Override
      public void onError(int errorCode, String errorMsg) {
        Trace.i("vetifyCode send failed info :" + errorMsg);
      }

      @Override
      public void onSuccess(final SaveQNTokenDTO response) {
        Trace.i("发送成功:response:"+response.token);
        saveImgGetTokenCallback.getToken(response.token);
      }
    });
  }



}
