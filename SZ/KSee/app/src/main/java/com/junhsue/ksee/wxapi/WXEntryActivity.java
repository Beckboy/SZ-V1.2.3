package com.junhsue.ksee.wxapi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.junhsue.ksee.BaseActivity;

import com.junhsue.ksee.MainActivity;
import com.junhsue.ksee.R;
import com.junhsue.ksee.Register1Activity;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.common.IntentLaunch;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.entity.AccessToken;
import com.junhsue.ksee.entity.UserInfo;
import com.junhsue.ksee.entity.WeChatUserInfo;
import com.junhsue.ksee.frame.MyApplication;
import com.junhsue.ksee.frame.ScreenManager;
import com.junhsue.ksee.net.api.OkHttpILoginImpl;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.url.RequestUrl;
import com.junhsue.ksee.profile.UserProfileService;
import com.junhsue.ksee.utils.ToastUtil;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;


/**
 * Created by Sugar on 16/10/12.
 */

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private IWXAPI api;
    private Context mContext;


    @Override
    protected void onReceiveArguments(Bundle extras) {

    }

    @Override
    protected int setLayoutId() {
        this.mContext = this;
        return R.layout.weixin_enter;
    }


    @Override
    protected void onInitilizeView() {
        api = MyApplication.getIwxapi();
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        Trace.i("code:微信调用完毕");
        if (baseResp instanceof SendAuth.Resp) {
            switch (baseResp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    /**
                     * 先获取用户信息
                     * 查询微信是否已经绑定
                     * 1.如果已经绑定调用微信登陆
                     * 2.otherwise 跳转手机号绑定
                     */
                    //通过code获取access_token
                    String code = ((SendAuth.Resp) baseResp).code;
                    Trace.i("code=" + code);
                    getAccessTokenFromService(code);
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL: //用户取消
                    Trace.i("code=用户取消");
                    finish();
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED: // 用户拒绝授权
                    Trace.i("code=用户拒绝授权");
                    finish();
                    break;
                default:
                    Trace.i("code=default");
                    break;
            }
        } else {
            switch (baseResp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    Toast.makeText(getApplication(), "分享成功", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case BaseResp.ErrCode.ERR_SENT_FAILED:
                    finish();
                    Toast.makeText(getApplication(), "分享失败", Toast.LENGTH_SHORT).show();
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    Toast.makeText(getApplication(), "分享取消", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
        }

    }


    /**
     * 获取AccessToken
     *
     * @param code
     */
    private void getAccessTokenFromService(String code) {
        String accessToken = RequestUrl.URL_GET_ACCESS_TOKEN + "?" + "appid=" + Constants.WEIXIN_APP_ID + "&secret=" + Constants.WEIXIN_APP_SECRET.trim()
                + "&code=" + code + "&grant_type=authorization_code";
        Trace.i("code:" + accessToken);
        OkHttpILoginImpl.getInstance().getAccessTokenFormWeChat(accessToken, new RequestCallback<AccessToken>() {

            @Override
            public void onError(int errorCode, String errorMsg) {
                Trace.i("getting AccessToken failed,failed info:" + errorMsg);
                dismissLoadingDialog();
                finish();
            }

            @Override
            public void onSuccess(AccessToken response) {
//                //获取微信用户信息
                getUserInfo(response);
                Trace.i("code:" + response);
            }
        });
    }


    /**
     * 发送请求获取用户信息
     *
     * @param accessToken
     */
    private void getUserInfo(AccessToken accessToken) {
        String userInfoUrl = "access_token=" + accessToken.access_token + "&openid=" + accessToken.openid;
        OkHttpILoginImpl.getInstance().getWeChatUserInfo((RequestUrl.URL_GET_USER_INFO + "?" + userInfoUrl), new RequestCallback<WeChatUserInfo>() {
            @Override
            public void onError(int errorCode, String errorMsg) {
                dismissLoadingDialog();
                finish();
            }

            @Override
            public void onSuccess(final WeChatUserInfo response) {
//                //发送登录请求
                toLogin(response);
            }
        });
    }

    private void toLogin(final WeChatUserInfo response) {
        OkHttpILoginImpl.getInstance().loginByPhonenumber("2", response.unionid, null, null, new RequestCallback<UserInfo>() {
            @Override
            public void onError(int errorCode, String errorMsg) {
                Intent intent = new Intent(mContext, Register1Activity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.REG_NICKNAME, response.nickname);
                bundle.putString(Constants.REG_AVATAR, response.headimgurl);
                bundle.putString(Constants.REG_SEX, response.sex);
                bundle.putString(Constants.REG_UNIONID, response.unionid);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }

            @Override
            public void onSuccess(UserInfo user) {
                //登录请求成功，将用户信息进行保存
                ScreenManager.getScreenManager().popAllActivity();
                //ToastUtil.getInstance(mContext).setContent("登录成功").setShow();
                UserProfileService.getInstance(mContext).updateCurrentLoginUser(user);
                MyApplication.setToken(user.token);
                IntentLaunch.launch(WXEntryActivity.this, MainActivity.class);
                finish();
            }
        });
    }

}
