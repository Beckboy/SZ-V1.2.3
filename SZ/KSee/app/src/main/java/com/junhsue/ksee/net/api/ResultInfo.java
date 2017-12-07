package com.junhsue.ksee.net.api;



/**
 * 回应信息类
 * Created by Sugar on 16/10/9 in Junhsue.
 */

public class ResultInfo {

    /**
     * 请求失败后的信息
     */
    public ErrorInfo errorInfo=new ErrorInfo();

    /**
     * 请求成功后的实体
     */
    public Object result;

}
