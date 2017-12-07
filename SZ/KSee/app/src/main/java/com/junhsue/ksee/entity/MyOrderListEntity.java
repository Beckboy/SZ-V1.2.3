package com.junhsue.ksee.entity;

/**
 * Created by hunter_J on 17/4/22.
 */

public class MyOrderListEntity extends BaseEntity {

  /**
   * {
   "errno": 0,
   "msg": {
       "list": [
           {
              "id": 1857,
              "user_id": 9999,
              "number": "150060790127309009999",
              "name": "测试了",
              "poster": null,
              "count": 1111,
              "amount": "0.01",
              "total_amount": "11.11",
              "discount": null,
              "business_id": 9,
              "good_id": 1,
              "order_status_id": 2,
              "pay_deadline": 1500609101,
              "pay_time": null,
              "receipt_id": 13,
              "create_at": 1500607901,
              "create_by": null,
              "update_at": null,
              "update_by": null,
              "is_valid": 1,
              "ali_sign": null,
              "wx_sign": null,
              "pay_type_id": 1,
              "transaction": null,
              "receipt_data": "",
              "price_id": "comLOG"
           },
       ]
       }
   }
   */

  /**
   * 订单编号
   */
  public String number;

  /**
   * 用户id
   */
  public String user_id;

  /**
   * 阿里支付签名
   */
  public String ali_sign;

  /**
   * 订单状态(1:已⽀支付 2：未⽀支付：3：异常 4：已关闭
   */
  public int order_status_id;

  /**
   * 时间戳
   */
  public long pay_deadline;

  /**
   * 订单id
   */
  public String id;

  /**
   * 商品id
   */
  public String good_id;

  /**
   * 业务id
   */
  public int business_id;

  /**
   * 订单名称
   */
  public String name;

  /**
   * 海报
   */
  public String poster;

  /**
   * 总价
   */
  public double total_amount;

  /**
   * 单价
   */
  public double amount;

  /**
   * 数量
   */
  public int count;

  public String discount;

  public String pay_time;

  public String receipt_id;

  public String create_at;

  public String create_by;

  public String update_at;

  public String update_by;

  public String is_valid;

  public String wx_sign;

  public String pay_type_id;

  public String transaction;

  public String receipt_data;

  public String price_id;
}
