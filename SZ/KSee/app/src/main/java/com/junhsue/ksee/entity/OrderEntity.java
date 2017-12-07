package com.junhsue.ksee.entity;

/**
 * 创建订单
 * Created by longer on 17/4/7.
 */
public class OrderEntity extends BaseEntity {


    //订单id
    public String order_id;
    //订单编号
    public String order_number;
    //订单状态
    public int order_status_id;
    //截⽌止⽀支付时间
    public String pay_deadline;
    //支付宝签名返回
    public String result;
}
