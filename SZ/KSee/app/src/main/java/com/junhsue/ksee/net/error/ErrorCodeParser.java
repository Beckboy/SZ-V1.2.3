package com.junhsue.ksee.net.error;


import com.junhsue.ksee.R;

public class ErrorCodeParser {


    public static int getI18NMessageResourceWithErrorCode(int appErrorCode) {
        switch (appErrorCode) {
            case NetResultCode.CODE_ERROR_HTTP:  //默认请求网络链接超时
                return R.string.net_error_service_not_accessables;

            case NetResultCode.CODE_INTERFACE_NOT_EXIST:  //接口不存在
                return R.string.interface_not_exist;

            case NetResultCode.CODE_LOGIN_STATE_ERROR:  //登录态错误
                return R.string.login_state_error;

            case NetResultCode.PARAMETER_LESS:  //参数缺失
                return R.string.perameter_lose;

            case NetResultCode.USER_NOT_EXIST:  //用户不存在哦
                return R.string.user_not_exist;

            case NetResultCode.PASSWORD_ERROR: //密码错误
                return R.string.password_error;

            case NetResultCode.USER_ACCOUNT_UNUSUAL: //用户账号异常
                return R.string.user_account_unusual;

            case NetResultCode.USER_EXIST:  //用户已存在
                return R.string.user_exist;

            case NetResultCode.USER_OR_PASSWORD_ERROR:  //用户或密码错误
                return R.string.user_or_password_error;

            case NetResultCode.SERVER_SAVE_LOSE:  //服务器存储失败
                return R.string.server_save_lose;

            case NetResultCode.SERVER_NO_DATA: //服务端无数据
                return R.string.not_data;

            case NetResultCode.PERAMETER_WRONGFUL:  //参数不合法
                return R.string.perameter_wrongful;

            case NetResultCode.NOT_DATA:  //无数据
                return R.string.not_data;

            default:
                break;
        }
        return R.string.net_error_service_not_accessables;
    }
}
