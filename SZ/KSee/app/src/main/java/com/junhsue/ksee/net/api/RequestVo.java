package com.junhsue.ksee.net.api;

import android.content.Context;

import java.util.HashMap;

/**
 * Created by Sugar on 16/10/6.
 */

public class RequestVo {


    public enum  ServerType{
        LOCAL,WECAHT,DEFAULT;
    }

    public Context context;
    /**
     * 请求地址
     */
    public String url = "";//请求地址

    /**
     * 请求参数
     */
    public HashMap<String, String> requestParams = new HashMap<String, String>();

    /**
     * 请求方式
     */
    public RequestType requestType =RequestType.POST;//默认post

    /**
     * 是否显示加载对话框
     */
    public boolean isShowDialog = false;
    //是否自动关闭dialog
    public boolean isAutoCloseDialog=true;
    /**
     * 返回结果信息
     */
    public ResultInfo resultInfo=new ResultInfo();
    /**
     *是否二次解析,自己服务上的要进行二次解析
     */

    //public boolean isSecondParse=false;

    //调用后台接口类型,如果是调用本地后台是需要重新获取key:msg

    public ServerType serverType= ServerType.LOCAL;
}
