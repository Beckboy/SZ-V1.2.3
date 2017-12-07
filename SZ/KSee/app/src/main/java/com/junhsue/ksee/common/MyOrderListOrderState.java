package com.junhsue.ksee.common;

/**
 * Created by hunter_J on 17/4/22.
 */

public interface MyOrderListOrderState {

  /**
   * 订单状态(1:已⽀支付 2：未⽀支付：3：异常 4：已关闭
   */

  //1. 已支支付
  public final static int ORDER_FINISH = 1;

  //2. 未支支付
  public final static int ORDER_WAIT = 2;

  //3. 异常
  public final static int ORDER_UNUSUAL = 3;

  //4. 已关闭
  public final static int ORDER_CLOSE = 4;

}
