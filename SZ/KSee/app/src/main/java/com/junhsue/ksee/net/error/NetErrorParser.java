package com.junhsue.ksee.net.error;


import com.junhsue.ksee.utils.TextUtils;

/**
 * 网络接口错误码请求解析
 */
public class NetErrorParser {


    public static String parserCode(int errorCode) {

        int codeResoureId = ErrorCodeParser.getI18NMessageResourceWithErrorCode(errorCode);
        return TextUtils.trans(codeResoureId);
    }


}
