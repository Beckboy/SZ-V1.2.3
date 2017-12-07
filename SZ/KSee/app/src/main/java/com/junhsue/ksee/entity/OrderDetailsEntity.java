package com.junhsue.ksee.entity;

/**
 * 订单详情
 * <p>
 * Created by longer on 17/4/7.
 */

public class OrderDetailsEntity extends BaseEntity {


    /**
     * "id": 37,
     * "user_id": 1,
     * "number": "1371491758425",
     * "name": "双十二6",
     * "count": 2,
     * "poster": null,
     * "amount": "699",
     * "total_amount": "1398",
     * "discount": null,
     * "business_id": 9,
     * "order_status_id": 2,
     * "pay_deadline": "2017-04-10 01:40:25",
     * "pay_time": null,
     * "receipt": 0,
     * "receipt_type": null,
     * "receipt_title": null,
     * "create_at": "2017-04-10 01:20:25",
     * "create_by": null,
     * "update_at": null,
     * "update_by": null,
     * "is_valid": 1,
     * "good_id": 37
     * "transaction": null,
     * "receipt_data": "",
     * "price_id": null
     * "pay_type_id": 3,
     * "ali_sign": "app_id=2017041306688854&biz_content=%7B%22timeout_express%22%3A%2230m%22%2C%22body%22%3A%22%5Cu6d4b%5Cu8bd5%5Cu4e86%22%2C%22total_amount%22%3A%2211.11%22%2C%22subject%22%3A%22%5Cu6d4b%5Cu8bd5%5Cu4e86%22%2C%22out_trade_no%22%3A%22999911500365579%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2F121.42.247.165%3A9000%2Fpay%2Forder%2Fali_notify&sign_type=RSA2&timestamp=2017-07-18+10%3A16%3A13&version=1.0&sign=fxiVgAHwanrWF5yi5VjBC%2BoB3OoJ3QRxvvr0SJC6j1V06uClXTr6KblxvBtLhmd3VRDtbQRRxzh5D1609MNCpkHRtP%2BCZOcwTFn1Axs8J9sBhn4rtuoOkAL3yHVcISwoiJ624WPU0L%2BoiSu3PXH%2F79EKn3Y2rlU0qLGEG99AkLTZuThJKSo2XPWHutf3HdXAGL8gXPBNiz6kZJpdujDucfVGn%2FB26L7djd7Xeb2Di6826tMkEv3207%2FL1ID4V%2F8i0OVHy2ktInKZhz%2FN7y5prpmITb9aV7zuAK2AMEp6RvETPaJgf9QtVTY8j%2FUXQ81pMplqxCovzRibk2Z6ZX720Q%3D%3D",
     * "wx_sign": null,
     *"receipt_id": 0,
     */
    //订单id
    public String id;
    //商品id
    public String good_id;
    //用户id
    public String user_id;
    //
    public String number;
    //商品图片
    public String poster;
    //商品名称
    public String name;
    //购买数量
    public int count;
    //价格
    public double amount;
    //总价格
    public double total_amount;
    //折扣
    public double discount;
    //订单状态
    public int order_status_id;
    //截止支付时间
    public long pay_deadline;
    //业务id
    public int business_id;
    //订单创建的时间
    public long create_at;
    //订单创建者
    public String create_by;
    //订单更新时间
    public long update_at;
    //订单更新者
    public String update_by;
    //订单支付签名
    public String ali_sign;
    //微信支付
    //public WechatPayEntity wechat_pay;
    //发票id
    public String receipt_id;
    //微信支付签名
    public String wx_sign;
    //支付方式
    public String pay_type_id;
    //支付时间
    public String pay_time;
    //
    public String is_valid;
    //
    public String transaction;
    //发票信息
    public String receipt_data;
    //APP GO
    public String price_id;
    /**
     * 订单状态(1:已支付 2：未支付：3：异常 4：已关闭)
     **/
    public static class OrderStatus {
        //已支付
        public final static int PAY_OK = 1;
        //待支付
        public final static int PAY_NO = 2;
        //异常
        public final static int PAY_ERROR = 3;
        //已关闭
        public final static int PAY_CLOSE = 4;
    }

    /**
     * 支付类型
     */
    public static class OrderPayType{
        //支付宝
        public final static  int  ALIPAY=1;
        //微信支付
        public final static  int  WECAHT=2;
        //苹果支付
        public final static  int APPLEPAY=3;

    }
}
