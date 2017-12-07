package com.junhsue.ksee.entity;

/**
 *微信支付返回
 * Created by longer on 17/7/24.
 */

public class WechatPayEntity extends BaseEntity {


    /**
     * "appId": "wx000c7fdd00dba35e",
     "nonceStr": "zvh1uglq3s0iueowaz0f9jyr0s3yyh19",
     "prepay_id": "wx201707241726159fe7fc93680155640088",
     "signType": "MD5",
     "timeStamp": "1500888375",
     "paySign": "34CFDEDEDA50A62C10CF0BB3847FCF7A"
     */
    //
    public String appId;

    //商户号 固定
    public String partnerId="1482675752";

    //预支付交易会话ID
    public String prepay_id;

    //随机字符串
    public String nonceStr;
    //
    public String timeStamp;
    //
    public String paySign;
    //
    public String sign;
}
