package com.junhsue.ksee.common;


import com.google.gson.JsonObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 支付宝订单
 * Created by longer on 17/4/13.
 */

public class AlipayUtils {


    //支付应用id
    private final static String APP_ID = "2017041306688854";

    //销售产品码，商家和支付宝签约的产品码
    private final static String PRODUCT_CODE = "QUICK_MSECURITY_PA";

    /**
     * @param sign         签名
     * @param timestamp    发送请求的时间，格式"yyyy-MM-dd HH:mm:ss"
     * @param notify_url   支付宝服务器主动通知商户服务器里指定的页面http/https路径。
     * @param suject
     * @param nubmer
     * @param total_amount 总金额
     * @param
     * @return
     */
    public static String bulidOrderInfo(String sign, String timestamp, String notify_url,
                                        String suject, String nubmer, String total_amount) {

        String businessInfo = bulidBusinessInfo(suject, nubmer, total_amount);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("app_id", APP_ID);
        //业务参数
        params.put("biz_content", businessInfo);
        params.put("charset", "utf-8");

        params.put("method", "alipay.trade.app.pay");
        params.put("sign_type", "RSA2");
        params.put("timestamp", timestamp);
        params.put("version", "1.0");


        params.put("sign", sign);
        params.put("notify_url", notify_url);

        String result = buildOrderParam(params);

        Trace.i("apliy--->request=," + result);

        return result;
    }


    /**
     * 构建订单业务参数
     *
     * @param subject
     * @param number       商户网站唯一订单号
     * @param total_amount
     * @param
     * @return
     */
    private static String bulidBusinessInfo(String subject, String number, String total_amount) {
        JsonObject jsonObject = new JsonObject();
        //业务参数
        //jsonObject.addProperty("body",body);
        jsonObject.addProperty("subject", subject);//主题
        jsonObject.addProperty("out_trade_no", number);//商户网站唯一订单号
        jsonObject.addProperty("total_amount", total_amount);//总金额
        //销售产品码，商家和支付宝签约的产品码，为固定值QUICK_MSECURITY_PA
        jsonObject.addProperty("product_code", PRODUCT_CODE);//商品id

        return jsonObject.toString();
    }


    /**
     * 构造支付订单参数信息
     *
     * @param map 支付订单参数
     * @return
     */
    private static String buildOrderParam(Map<String, String> map) {
        List<String> keys = new ArrayList<String>(map.keySet());
        Collections.sort(keys);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = map.get(key);
            sb.append(buildKeyValue(key, value, true));
            sb.append("&");
        }

        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        sb.append(buildKeyValue(tailKey, tailValue, true));

        return sb.toString().trim();
    }


    /**
     * 拼接键值对
     *
     * @param key
     * @param value
     * @param isEncode
     * @return
     */
    private static String buildKeyValue(String key, String value, boolean isEncode) {
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        sb.append("=");
//        if (isEncode) {
//            try {
//                sb.append(URLEncoder.encode(value, "UTF-8"));
//            } catch (UnsupportedEncodingException e) {
//                sb.append(value);
//            }
//        } else {
//            sb.append(value);
//        }
        sb.append(value);

        Trace.i("apliy," + sb.toString());
        return sb.toString().trim();
    }


}
