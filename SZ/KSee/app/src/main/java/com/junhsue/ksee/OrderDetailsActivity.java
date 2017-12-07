package com.junhsue.ksee;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.junhsue.ksee.common.AlipayUtils;
import com.junhsue.ksee.entity.GoodsInfo;
import com.junhsue.ksee.entity.OrderDetailsEntity;
import com.junhsue.ksee.entity.UserInfo;
import com.junhsue.ksee.entity.WechatPayEntity;
import com.junhsue.ksee.fragment.dialog.PayDialogFragment;
import com.junhsue.ksee.net.api.ICourse;
import com.junhsue.ksee.net.api.OKHttpCourseImpl;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.profile.UserProfileService;
import com.junhsue.ksee.utils.DateUtils;
import com.junhsue.ksee.utils.NumberFormatUtils;
import com.junhsue.ksee.utils.StatisticsUtil;
import com.junhsue.ksee.view.ActionBar;
import com.junhsue.ksee.view.OrderAddressView;
import com.junhsue.ksee.view.OrderInfoView;
import com.junhsue.ksee.view.OrderStatusView;

import java.util.Map;

/**
 * 订单详情
 * <p>
 * Created by longer on 17/4/9.
 */


public class OrderDetailsActivity extends OrderPayActivity implements View.OnClickListener {


    //商品订单id
    public final static String PARAMS_ORDER_ID = "params_order_id";
    //订单交易状态
    public final static String PARAMS_ORDER_STATUS = "params_order_status";
    //订单号
    public final static String PARAMS_ORDER_NO="params_order_no";
    //商品id
    public final static String PARAMS_GOODS_ID="params_goods_id";
    //业务id
    public final static String PARAMS_ORDER_BUSINESS_ID="params_order_business_id";
    //支付状态
    public final static String PARAMS_ORDER_PAY_STATUS="params_order_pay_status";

    private OrderStatusView mOrderStatusView;
    //订单编号
    private TextView mTxtOrderNumber;
    //地址
    private OrderAddressView mOrderAddressView;
    //
    private OrderInfoView mOrderInfoView;
    //订单创建的时间
    private TextView mTxtOrderCreateTime;
    //
    private RelativeLayout mRLPay;

    //订单id
    private String mOrderId;
    //订单状态
    private int mOrderStatus;

    //份数
    private TextView mTxtQuantity;
    //实付金额
    private TextView mTxtOrderPrice;
    //订单支付
    private Button mBtnConfirm;
    private ActionBar mActionBar;

    private OrderDetailsEntity mOrderDetailsEntity;

    private Bundle mBundle;

    @Override
    protected void onReceiveArguments(Bundle bundle) {
        mOrderId = bundle.getString(PARAMS_ORDER_ID, "");
        mOrderStatus = bundle.getInt(PARAMS_ORDER_STATUS, 0);
        mBundle=bundle;
    }


    @Override
    protected int setLayoutId() {
        return R.layout.act_order_details;
    }


    @Override
    protected void onInitilizeView() {

        mOrderStatusView = (OrderStatusView) findViewById(R.id.order_status_view);
        mTxtOrderNumber = (TextView) findViewById(R.id.txt_order_number);
        mOrderAddressView = (OrderAddressView) findViewById(R.id.order_address_view);
        mOrderInfoView = (OrderInfoView) findViewById(R.id.order_info_view);
        mTxtOrderCreateTime = (TextView) findViewById(R.id.txt_order_create_time);
        mRLPay = (RelativeLayout) findViewById(R.id.rl_pay);
        mTxtQuantity = (TextView) findViewById(R.id.txt_quantity);
        mTxtOrderPrice = (TextView) findViewById(R.id.txt_price_quantity_bottom);

        mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
        mBtnConfirm.setOnClickListener(this);
        mActionBar=(ActionBar) findViewById(R.id.action_bar);
        mActionBar.setOnClickListener(this);

        //待支付
        if (mOrderStatus == OrderDetailsEntity.OrderStatus.PAY_NO) {
            mRLPay.setVisibility(View.VISIBLE);
        } else {
            mRLPay.setVisibility(View.GONE);
        }
        mOrderStatusView.setOrderStatus(mOrderStatus);

        fillUserInfo();
        //获取商品详情
        getOrderDetails();
    }


    /**
     * 填充用户信息
     */
    private void fillUserInfo() {
        UserInfo userProfileService = UserProfileService.getInstance(getApplicationContext()).getCurrentLoginedUser();
        if (null == userProfileService)
            return;
        mOrderAddressView.setAddressInfo(userProfileService.nickname, userProfileService.phonenumber,
                userProfileService.organization);
    }


    /**
     * 填充商品信息
     */
    private void fillOrderInfo(OrderDetailsEntity orderDetailsEntity) {
        if (null == orderDetailsEntity)
            return;
        mTxtOrderNumber.setText("订单号: " + orderDetailsEntity.number);
        mTxtOrderCreateTime.setText("创建时间: " + DateUtils.timeStampToTime(orderDetailsEntity.create_at * 1000));
        GoodsInfo.GoodsType goodsType = GoodsInfo.GoodsType.getType(orderDetailsEntity.business_id);
        mOrderInfoView.setOrderData(orderDetailsEntity.poster, orderDetailsEntity.name,
                orderDetailsEntity.amount, goodsType);
        mOrderInfoView.setGoodsNumberSmall(orderDetailsEntity.count);
        mOrderInfoView.updateGoodsTotal(orderDetailsEntity.count, orderDetailsEntity.amount);
        //
        //mOrderInfoView.setGoodsNumber(orderDetailsEntity.count);
        //如果是待支付就设置金额
        if (orderDetailsEntity.order_status_id == OrderDetailsEntity.OrderStatus.PAY_NO) {
            setPriceQuantity(orderDetailsEntity.count, orderDetailsEntity.amount);
            mOrderStatusView.startOrderTime(orderDetailsEntity.pay_deadline);
            mOrderStatusView.setTimerOnlistener(new OrderStatusView.ITimerOnlistener() {

                @Override
                public void onClose() {
                    mBtnConfirm.setClickable(false);
                }
            });
        }
    }


    /**
     * 设置总金额
     */
    private void setPriceQuantity(int number, double price) {
        mTxtQuantity.setText(String.format("共" + number + "份"));
        SpannableString spannableString = new SpannableString("实付: ¥" + NumberFormatUtils.formatPointTwo(price * number));
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.c_yellow_cdac8d)),
                4, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTxtOrderPrice.setText(spannableString);

    }


    /**
     * 获取订单详情
     */
    private void getOrderDetails() {
        OKHttpCourseImpl.getInstance().getOrderDetails(mOrderId, new RequestCallback<OrderDetailsEntity>() {
            @Override
            public void onError(int errorCode, String errorMsg) {
            }

            @Override
            public void onSuccess(OrderDetailsEntity response) {
                mOrderDetailsEntity=response;
                fillOrderInfo(response);
            }
        });
    }


    @Override
    protected void onPaySuceess() {

        if(null==mOrderDetailsEntity) return;
        //订单编号
        String orderNo=mOrderDetailsEntity.number;
        //订单id
        String orderId=mOrderDetailsEntity.id;
        int businessId=mOrderDetailsEntity.business_id;
        String goodsId=mOrderDetailsEntity.good_id;

        mBundle.putString(PARAMS_ORDER_NO,orderNo);
        mBundle.putString(PARAMS_ORDER_ID,orderId);
        mBundle.putInt(PARAMS_ORDER_BUSINESS_ID,businessId);
        mBundle.putString(PARAMS_GOODS_ID,goodsId);
        //
        Intent intent=new Intent(OrderDetailsActivity.this,OrderPayResultActivity.class);
        intent.putExtras(mBundle);
        startActivityForResult(intent,0);

        setResult(OrderPayResultActivity.CODE_RESULT_PAY_SUCCES);
        finish();

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //支付成功,关闭当前页
        if(resultCode==OrderPayResultActivity.CODE_RESULT_PAY_SUCCES){
            setResult(OrderPayResultActivity.CODE_RESULT_PAY_SUCCES);
            finish();

        }
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_left_layout:
                finish();
                break;
            case R.id.btn_confirm: //订单支付

                PayDialogFragment payDialogFragment=PayDialogFragment.newInstance();
                payDialogFragment.show(getSupportFragmentManager(),null);
                payDialogFragment.setIPayOnClickListener(new PayDialogFragment.IPayOnClickListener() {

                    @Override
                    public void onAliPay() {
                        if(null==mOrderDetailsEntity)
                            return;
                        pay(mOrderDetailsEntity.id, OrderDetailsEntity.OrderPayType.ALIPAY);
                    }

                    @Override
                    public void onWechatPay() {
                        if(null==mOrderDetailsEntity)
                            return;
                            pay(mOrderDetailsEntity.id, OrderDetailsEntity.OrderPayType.WECAHT);
                    }
                });

                StatisticsUtil.getInstance(this).onCountActionDot("4.2.1");

                break;
        }
    }



}
