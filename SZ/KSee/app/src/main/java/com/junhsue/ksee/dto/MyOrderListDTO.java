package com.junhsue.ksee.dto;

import com.junhsue.ksee.entity.BaseEntity;
import com.junhsue.ksee.entity.MyOrderListEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hunter_J on 17/4/22.
 */

public class MyOrderListDTO extends BaseEntity {

  /**
   * {
   "errno": 0,
       "msg": {
       "list": [
           {
           "order_number": "57371492413440",
           "ali_sign": "alipay_sdk=alipay-sdk-php-20161101&amp;app_id=2016080300160477&amp;biz_content=%7B%22body%22%3A%E5%8F%8C%E5%8D%81%E4%BA%8C6%2C%22subject%22%3A+%E3%80%90%E5%8F%8C%E5%8D%81%E4%BA%8C6%E3%80%91%2C%22out_trade_no%22%3A+57371492413440%2C%22timeout_express%22%3A+30m%2C%22total_amount%22%3A+4.5%2C%22product_code%22%3AQUICK_MSECURITY_PAY%7D&amp;charset=UTF-8&amp;format=json&amp;method=alipay.trade.app.pay&amp;notify_url=http%3A%2F%2F121.42.247.165%3A9000%2Fpay%2Forder%2Fali_notify&amp;sign_type=RSA2&amp;timestamp=2017-04-17+15%3A17%3A20&amp;version=1.0&amp;sign=FfNRpokWxAuj1I6yel9cKH%2Facp4TwnCLndnD65YIzWQ2wgUqmABCLJl4D9B48Fzsezf%2FM63mrToBGizYUheQEfunQzHekFXsPyYnxeulHWlgPzZ3kACWyjsv%2Fd3LN%2B8CxYyglHePSTKOq9Lydl5rndmOw2A0UnRPUd9zQRe%2BpGY0g7mMbjViefqgW7mR%2F%2BnrsEX72WWfAEuSyianz0wRrcI5GpL%2FxBEmOqBnT6nQ5NurmXIvr2H2Xm62JGrE75fKbfbuDqsxA96ipoJjkYDAZmah90cJg08jkpfMi0urW7MDtACnWpXsRtUFgpxmQfVLkQzixK2fIb%2FIjrnfOqolnA%3D%3D",
           "order_status_id": 4,
           "pay_deadline": 1492414640,
           "order_id": 533,
           "good_id": 37,
           "business_id": 9,
           "name": "双十二6",
           "poster": "http://120.26.112.49:8000/uploads/20170325000802923332?v=1490371693065",
           "total_amount": 4.5,
           "amount": 1.5,
           "count": 3,
           "cb_url": "http://121.42.247.165:9000/pay/order/ali_notify"
           },
       ]
       }
   }
   */

  /**
   * 订单列表
   */
  public List<MyOrderListEntity> list = new ArrayList<>();

}
