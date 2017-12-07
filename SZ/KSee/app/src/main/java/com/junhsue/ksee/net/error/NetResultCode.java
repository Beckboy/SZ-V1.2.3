package com.junhsue.ksee.net.error;

/**
 * 网络返回编码
 * Created by longer on 17/3/19.
 */

public interface NetResultCode {

    //无数据
    public static int NOT_DATA = -1;

    //网络返回成功
    public static int CODE_SUCCESS_HTTP = 200;

    //网络返回成功
    public static int CODE_SUCCESS = 0;

    //默认请求网络链接超时
    public static int CODE_ERROR_HTTP=400;

    //服务器存储失败
    public static int SERVER_SAVE_LOSE = 88888;

    //无数据
    public static int SERVER_NO_DATA = 99989;

    //参数不合法
    public static int PERAMETER_WRONGFUL = 99991;

    //用户或密码错误
    public static int USER_OR_PASSWORD_ERROR = 99992;

    //用户已存在
    public static int USER_EXIST = 99993;

    //用户账号异常
    public static int USER_ACCOUNT_UNUSUAL = 99994;

    //密码错误
    public static int PASSWORD_ERROR = 99995;

    //用户不存在哦
    public static int USER_NOT_EXIST = 99996;

    //参数缺失
    public static int PARAMETER_LESS = 99997;

    //登录态错误
    public static int CODE_LOGIN_STATE_ERROR = 99998;

    //接口不存在
    public static int CODE_INTERFACE_NOT_EXIST = 99999;
}
