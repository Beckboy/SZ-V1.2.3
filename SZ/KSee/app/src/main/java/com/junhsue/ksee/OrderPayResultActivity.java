package com.junhsue.ksee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.junhsue.ksee.entity.GoodsInfo;
import com.junhsue.ksee.entity.OrderDetailsEntity;
import com.junhsue.ksee.fragment.ColleageLiveDetailsFragment;
import com.junhsue.ksee.fragment.ColleageLiveFragment;
import com.junhsue.ksee.view.ActionBar;

/**
 *
 * 订单支付结果页
 * Created by longer on 17/4/17.
 */


public class OrderPayResultActivity extends BaseActivity implements View.OnClickListener {


    //订单支付成功结果回传
    public final static int CODE_RESULT_PAY_SUCCES = 0X60001;

    //是否来源于吾订单列表支付,默认未其他页面
    public final static String PARAMS_IS_FROM_ORDER_LIST = "params_is_from_order_list";
    //订单号
    private String mOrderNo;
    //业务id
    private int mBusinessId;
    //
    private TextView mTxtOrderNo;
    //订单提示文案
    private TextView mTxtOrderHint;
    //
    private ActionBar mActionBar;

    private Bundle mBundle;

    //是否来自于吾订单列表支付,默认为false
    private boolean mIsFromOrderList = false;


    @Override
    protected void onReceiveArguments(Bundle bundle) {
        mBundle = bundle;
        mOrderNo = bundle.getString(OrderDetailsActivity.PARAMS_ORDER_NO, "");
        mBusinessId=bundle.getInt(OrderDetailsActivity.PARAMS_ORDER_BUSINESS_ID,0);
        String orderId=bundle.getString(OrderDetailsActivity.PARAMS_ORDER_ID,"");
        mIsFromOrderList=bundle.getBoolean(PARAMS_IS_FROM_ORDER_LIST,false);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.act_order_pay_result;
    }


    @Override
    protected void onInitilizeView() {
        mActionBar = (ActionBar) findViewById(R.id.action_bar);
        mTxtOrderNo = (TextView) findViewById(R.id.txt_order_no);
        //
        mTxtOrderHint=(TextView)findViewById(R.id.txt_order_hint);
        //
        findViewById(R.id.btn_order_details).setOnClickListener(this);
        mActionBar.setOnClickListener(this);
        //
        mTxtOrderNo.setText(String.format("订单号: %s", mOrderNo));

        fillOrderHint(GoodsInfo.GoodsType.getType(mBusinessId));
        notifyUpdate();

    }


    private void  fillOrderHint(GoodsInfo.GoodsType goodsType){

        if(goodsType== GoodsInfo.GoodsType.ON_LINE_ACTIVITY ||
                goodsType==GoodsInfo.GoodsType.off_line_Activity){
            //活动
            mTxtOrderHint.setText(String.format(getString(R.string.msg_order_pay_result_hint),
                    "活动"));
        }else if(goodsType==GoodsInfo.GoodsType.LIVE){
            //直播
            mTxtOrderHint.setText(String.format(getString(R.string.msg_order_pay_result_hint),
                    "直播"));

        }else if(goodsType== GoodsInfo.GoodsType.SYSTEM_COURSE ||
                goodsType==GoodsInfo.GoodsType.SUBJECT_COURSE){
            //课程
            mTxtOrderHint.setText(String.format(getString(R.string.msg_order_pay_result_hint),
                    "课程"));
        }
    }

    /**
     * 通知各个页面
     * <p>
     * 支付成功后,
     * <1> 关闭之前两个页面 OrderDetailsActivity,ConfirmOrderActivity,采用OnActivity
     * <2>通知不同页面更新,支付状态,这个地方要考虑到吾莫模块我的订单支付
     * <p>
     * 吾模块订单支付入口统一用一个入口
     * 待一个参数来区分,其他的分别业务id来区分
     */

    private void notifyUpdate() {

        if (null == mBundle) return;
        Intent intent = new Intent();

        mBundle.putInt(OrderDetailsActivity.PARAMS_ORDER_PAY_STATUS, OrderDetailsEntity.OrderStatus.PAY_OK);
        intent.putExtras(mBundle);
        int businessId = mBundle.getInt(OrderDetailsActivity.PARAMS_ORDER_BUSINESS_ID);

        if (mIsFromOrderList) { //更新吾模块单个订单状态
            intent.setAction(MyOrderListActivity.BROAD_ACTION_ORDER_LIST_UPDATE);

        } else {
            if (businessId == GoodsInfo.GoodsType.LIVE.getValue()) {
                //更新直播具体直播列表
                intent.setAction(LiveDetailsActivity.BROAD_ACTION_LIVE_DETAILS);
                
                //launchScreen(Cole);
            } else if (businessId == GoodsInfo.GoodsType.off_line_Activity.getValue()) {
                //线下活动
                //launchScreen();

            }
        }
        sendBroadcast(intent);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_order_details:
                if (null == mBundle) return;
                mBundle.putInt(OrderDetailsActivity.PARAMS_ORDER_STATUS, OrderDetailsEntity.OrderStatus.PAY_OK);
                launchScreen(OrderDetailsActivity.class,mBundle);

            case R.id.tv_btn_right: //完成

                finish();

                break;
        }
    }
}
