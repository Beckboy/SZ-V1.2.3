package com.junhsue.ksee.dto;

import com.junhsue.ksee.entity.BaseEntity;
import com.junhsue.ksee.entity.OrderDetailsEntity;
import com.junhsue.ksee.entity.WechatPayEntity;

/**
 * 订单支付
 * Created by longer on 17/7/25.
 */

public class OrderPayDTO extends BaseEntity {


    //订单信息
    public OrderDetailsEntity order;

    //微信支付
    public WechatPayEntity wechat_pay;
    //
    public String cb_url;
}
