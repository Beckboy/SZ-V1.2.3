package com.junhsue.ksee;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.dto.OrderPayDTO;
import com.junhsue.ksee.entity.OrderDetailsEntity;
import com.junhsue.ksee.entity.WechatPayEntity;
import com.junhsue.ksee.frame.MyApplication;
import com.junhsue.ksee.net.api.OKHttpCourseImpl;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.utils.ToastUtil;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * 订单支付基类
 * Created by longer on 17/4/26.
 */

public abstract class   OrderPayActivity extends BaseActivity {



    /** 微信支付*/
    public final static  String BROAD_ACTION_WECHAT_PAY="com.junhsue.ksee.action.wechat_pay";
    //
    public final static int MSG_CODE_SDK_PAY_FLAG = 0x0012;

    /**
     * 9000	订单支付成功
     * 8000	正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
     * 4000	订单支付失败
     * 5000	重复请求
     * 6001	用户中途取消
     * 6002	网络连接出错
     * 6004	支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
     */
    //支付成功
    private final static String ALI_PAY_RESULT_CODE_SUCEESS = "9000";
    //支付失败
    private final static String ALI_PAY_RESULT_CODE_FAIL = "4000";
    //中途取消
    private final static String ALI_PAY_RESULT_CODE_CANCEL = "6001";
    //网络连接错误
    private final static String ALI_PAY_RESULT_CODE_NETWORK_FAIL = "60002";

    /**
     * 订单支付
     *
     * @param orderInfo 订单信息 ,由后台返回
     */
    private  void toPay(String orderInfo) {
        final String orderInfoStr = orderInfo;
        new Thread(new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(OrderPayActivity.this);
                Map<String, String> result = alipay.payV2(orderInfoStr, false);

                Message msg = new Message();
                msg.what = MSG_CODE_SDK_PAY_FLAG;
                msg.obj = result;
                mHandlerPay.sendMessage(msg);
            }
        }).start();
    }


    /**
     *支付
     * @param  orderId 订单id
     * @param  payType 支付类型
     */
    protected void pay(String orderId, final int payType){
        alertLoadingProgress();
        OKHttpCourseImpl.getInstance().orderPay(orderId, payType, new RequestCallback<OrderPayDTO>() {

            @Override
            public void onError(int errorCode, String errorMsg) {
                dismissLoadingDialog();
                ToastUtil.showToast(getApplicationContext(),getString(R.string.net_error_service_not_accessables));
            }

            @Override
            public void onSuccess(OrderPayDTO response) {
                dismissLoadingDialog();
                if(null==response) return;
                if(payType== OrderDetailsEntity.OrderPayType.WECAHT){
                    WechatPayEntity wechatPayEntity=response.wechat_pay;
                    PayReq request = new PayReq();
                    request.appId =wechatPayEntity.appId;
                    request.partnerId =wechatPayEntity.partnerId;
                    request.prepayId= wechatPayEntity.prepay_id;
                    request.packageValue = "Sign=WXPay";
                    request.nonceStr=wechatPayEntity.nonceStr;
                    request.timeStamp=wechatPayEntity.timeStamp;
                    request.sign=wechatPayEntity.paySign;

                    IWXAPI api= MyApplication.getIwxapi();
                    boolean result=api.sendReq(request);
                    //Trace.i("wechat pay->"+result);
                }else if(payType== OrderDetailsEntity.OrderPayType.ALIPAY){
                    OrderDetailsEntity  orderDetailsEntity=response.order;
                    toPay(orderDetailsEntity.ali_sign);
                }

            }
        });
    }



    private Handler mHandlerPay = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case MSG_CODE_SDK_PAY_FLAG:
                    Map<String, String> map = (HashMap<String, String>) msg.obj;
                    String resultStatus = map.get("resultStatus");
                    //支付成功
                    if (resultStatus.equals(ALI_PAY_RESULT_CODE_SUCEESS)) {
                        onPaySuceess();
                    } else if (resultStatus.equals(ALI_PAY_RESULT_CODE_CANCEL)) {
                        //中途取消
                    } else if (resultStatus.equals(ALI_PAY_RESULT_CODE_NETWORK_FAIL)) {
                        ToastUtil.getInstance(OrderPayActivity.this).setContent(getString(R.string.msg_pay_network_timeout)).setShow();
                    } else {
                        ToastUtil.getInstance(OrderPayActivity.this).setContent(getString(R.string.msg_pay_fail)).setShow();
                    }
                    break;
            }
        }
    };


    /**
     *
     * 微信支付成功页
     **/
    BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            //微信支付成功回道
            if(BROAD_ACTION_WECHAT_PAY.equals(action)) {
                onPaySuceess();
            }
        }
    };

    /**
     * 订单支付成功
     */
    protected abstract void onPaySuceess();


    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(BROAD_ACTION_WECHAT_PAY);
        registerReceiver(broadcastReceiver,intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
