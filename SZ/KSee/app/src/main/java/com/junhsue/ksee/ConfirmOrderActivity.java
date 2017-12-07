package com.junhsue.ksee;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.entity.CommonResultEntity;
import com.junhsue.ksee.entity.GoodsInfo;
import com.junhsue.ksee.entity.OrderEntity;
import com.junhsue.ksee.entity.ReceiptInfoEnity;
import com.junhsue.ksee.entity.UserInfo;
import com.junhsue.ksee.frame.MyApplication;
import com.junhsue.ksee.net.api.OKHttpCourseImpl;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.profile.UserProfileService;
import com.junhsue.ksee.utils.NumberFormatUtils;
import com.junhsue.ksee.utils.StatisticsUtil;
import com.junhsue.ksee.utils.StringUtils;
import com.junhsue.ksee.utils.ToastUtil;
import com.junhsue.ksee.view.ActionBar;
import com.junhsue.ksee.view.OrderAddressView;
import com.junhsue.ksee.view.OrderInfoView;

/**
 * 确认订单
 *
 * 直播，课程主题课，活动共用确认订单
 *
 * <p> 本页面需要带的参数有
 * 图片，商品名，商品类型，单价，是否为免费
 *
 * <p>
 *如果订单为免费，则不走支付流程，直接跳转到支付成功详情页
 *
 *otherwise,跳转到支付宝支付流程
 *
 * Created by longer on 17/3/30.
 *
 */

public class ConfirmOrderActivity extends BaseActivity implements View.OnClickListener {

    /** 商品信息*/
    public final static  String PARAMS_GOODS_INFO="params_goods_info";

    //商品总份数
    private TextView mTxtQuantity;
    //商品总金额
    private TextView mTxtGoodsPrice;
    //总金额
    private TextView mTxtGoodsPrictQuantityPrice;
    private Button mBtnConfirm;
    private RelativeLayout rlReceiptLayout;
    private RelativeLayout mRlReceipt;
    private CheckBox mCbReceipt;
    private TextView mTvReceipt;

    private OrderAddressView mOrderAddressView;

    private OrderInfoView  mOrderInfoView;

    private ActionBar mActionBar;

    private GoodsInfo mGoodsInfo;

    private UserInfo mUserInfo;

    private Context mContext;
    private static int REQUEST_RECEIPT_CODE = 1;
    private ReceiptInfoEnity receiptInfoEnity;

    /**
     * 用户机构信息是否完善
     * true: 完善 ； false：未完善
     */
    private boolean info_flag = true;


    @Override
    protected void onReceiveArguments(Bundle bundle) {
        mGoodsInfo=(GoodsInfo) bundle.getSerializable(PARAMS_GOODS_INFO);
        mUserInfo=UserProfileService.getInstance(getApplicationContext()).getCurrentLoginedUser();

    }


    @Override
    protected int setLayoutId() {
        return R.layout.act_confirm_order;
    }


    @Override
    protected void onInitilizeView() {
        mContext = this;
        mActionBar = (ActionBar) findViewById(R.id.action_bar);
        mTxtGoodsPrice = (TextView) findViewById(R.id.txt_unit_price);
        mTxtGoodsPrice = (TextView) findViewById(R.id.txt_prict_quantity);
        mTxtQuantity = (TextView) findViewById(R.id.txt_quantity);
        mTxtGoodsPrictQuantityPrice = (TextView) findViewById(R.id.txt_price_quantity_bottom);
        mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
        mOrderAddressView=(OrderAddressView)findViewById(R.id.order_address_view);
        mOrderInfoView=(OrderInfoView)findViewById(R.id.order_info_view);
        rlReceiptLayout=(RelativeLayout) findViewById(R.id.rl_receipt_enter);
        mRlReceipt = (RelativeLayout) findViewById(R.id.cb_receipt_enter);
        mCbReceipt = (CheckBox) findViewById(R.id.cb_receipt_enter1);
        mTvReceipt = (TextView) findViewById(R.id.tv_receipt_enter);
        mCbReceipt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    switch (Integer.parseInt(receiptInfoEnity.receipt_type)){
                        case 1:
                            mTvReceipt.setText("收据");
                            break;
                        case 2:
                            mTvReceipt.setText("增值税普通发票");
                            break;
                        case 3:
                            mTvReceipt.setText("增值税专用发票");
                            break;
                    }
                }else {
                    mTvReceipt.setText("我要开发票");
                }
            }
        });
        mRlReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCbReceipt.isChecked()){
                    mCbReceipt.setChecked(false);
                }else {
                    if (null == receiptInfoEnity){
                        rlReceiptLayout.callOnClick();
                        return;
                    }
                    mCbReceipt.setChecked(true);
                }
            }
        });
        //
        mActionBar.setOnClickListener(this);
        mOrderAddressView.setOnClickListener(this);
        mBtnConfirm.setOnClickListener(this);
        mOrderInfoView.setIOrderChangeClickListener(iOrderChangeClickListener);
        rlReceiptLayout.setOnClickListener(this);
        fillOrderInfo();
    }


    /** 填充用户信息*/
    private void fillUserInfo(){
        mUserInfo=UserProfileService.getInstance(getApplicationContext()).getCurrentLoginedUser();
        if(null==mUserInfo)
            return;
        mOrderAddressView.setAddressInfo(mUserInfo.nickname,mUserInfo.phonenumber,mUserInfo.organization);

    }

    /** 填充商品信息*/
    private void fillOrderInfo(){
        if(null==mGoodsInfo)
            return;
        mOrderInfoView.setOrderData(mGoodsInfo.imgUrl,mGoodsInfo.goodsName,mGoodsInfo.price,mGoodsInfo.goodsType);
        mOrderInfoView.updateGoodsTotal(1,mGoodsInfo.price);
        setPriceQuantity(1,mGoodsInfo.price);
    }


    /**
     * 检查用户信息是否完整
     */
    private void checkUserInfo(){
        String token="";
        if(null!=mUserInfo){
            token=mUserInfo.token;
        }else{
            token= MyApplication.getToken();
        }
        OKHttpCourseImpl.getInstance().checkUserInfo(token, new RequestCallback<CommonResultEntity>() {
            @Override
            public void onError(int errorCode, String errorMsg) {
                mOrderAddressView.setShowHint(true);
            }

            @Override
            public void onSuccess(CommonResultEntity response) {
                mOrderAddressView.setShowHint(false);
            }
        });
    }



    OrderInfoView.IOrderChangeClickListener iOrderChangeClickListener=new
            OrderInfoView.IOrderChangeClickListener() {

        @Override
        public void onChange(int number) {
            setPriceQuantity(number,mGoodsInfo.price);
        }
    };


    /**
     * 接收更新列表消息的广播
     */
    BroadcastReceiver receiverUserInfoUpdate = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case Constants.BROAD_ACTION_USERINFO_UPDATE:
                    if (mUserInfo != null) {
                        fillUserInfo();
                        checkUserInfoByLocation();
                    }
                    break;
            }
        }
    };


    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.BROAD_ACTION_USERINFO_UPDATE);
        this.registerReceiver(receiverUserInfoUpdate, intentFilter);
        Trace.i("注册广播");
    }

    @Override
    public void onDestroy() {
        this.unregisterReceiver(receiverUserInfoUpdate);
        Trace.i("销毁广播");
        super.onDestroy();
    }


    /** 设置总金额*/
    private void setPriceQuantity(int number,double price){
        mTxtQuantity.setText(String.format("共"+number+"份"));
        SpannableString spannableString=new SpannableString("实付: ¥"+ NumberFormatUtils.formatPointTwo(price*number));
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.c_yellow_cdac8d)),
                4,spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTxtGoodsPrictQuantityPrice.setText(spannableString);

    }

    

    /**
     * 创建订单
     */
    private void createOrder(){
        String token="";
        if(null!=mUserInfo){
             token=mUserInfo.token;
        }else{
            token= MyApplication.getToken();
        }
        String businessId=String.valueOf(mGoodsInfo.goodsType.getValue());
        String amount=String.valueOf(mGoodsInfo.price);
        String goodsId=mGoodsInfo.id;
        String poster=mGoodsInfo.imgUrl == null ? null:mGoodsInfo.imgUrl;
        String goodsName=mGoodsInfo.goodsName;
        String count=String.valueOf(mOrderInfoView.getGoodsNumber());
        String is_receipt=mCbReceipt.isChecked()?"1":"0";
        String receipt_type_id = receiptInfoEnity == null ? "" : receiptInfoEnity.receipt_type;
        String receipt_content_id = receiptInfoEnity == null ? "" : receiptInfoEnity.fee_type;
        String organization = receiptInfoEnity == null ? "" : receiptInfoEnity.organization_name;
        String contact = receiptInfoEnity == null ? "" : receiptInfoEnity.receiver_person_name;
        String contact_phone = receiptInfoEnity == null ? "" : receiptInfoEnity.receiver_person_phone;
        String contact_address = receiptInfoEnity == null ? "" : receiptInfoEnity.receiver_person_address;
        String uniform_code = receiptInfoEnity == null ? "" : receiptInfoEnity.organization_identify;
        String register_address = receiptInfoEnity == null ? "" : receiptInfoEnity.organization_address;
        String register_phone = receiptInfoEnity == null ? "" : receiptInfoEnity.organization_phone;
        String bank = receiptInfoEnity == null ? "" : receiptInfoEnity.organization_bank_name;
        String bank_account = receiptInfoEnity == null ? "" : receiptInfoEnity.organization_bank_account;
        OKHttpCourseImpl.getInstance().createOrder(token, poster, businessId, amount, goodsId, goodsName, count,
                is_receipt, receipt_type_id, receipt_content_id, organization, contact, contact_phone,
                contact_address, uniform_code, register_address, register_phone, bank, bank_account, new RequestCallback<OrderEntity>() {
            @Override
            public void onError(int errorCode, String errorMsg) {

            }

            @Override
            public void onSuccess(OrderEntity response) {
                //
                Bundle bundle=new Bundle();
                bundle.putString(OrderDetailsActivity.PARAMS_ORDER_ID,response.order_id);
                bundle.putInt(OrderDetailsActivity.PARAMS_ORDER_STATUS,response.order_status_id);
                Intent intent=new Intent(ConfirmOrderActivity.this, OrderDetailsActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent,0);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        //填充用户信息
        fillUserInfo();
//        checkUserInfo();
        checkUserInfoByLocation();

    }

    /**
     * 通过本地缓存判断用户信息是否完善
     */
    private void checkUserInfoByLocation() {
        if (mUserInfo.organization == null || TextUtils.isEmpty(mUserInfo.organization) || mUserInfo.organization.contains("未注册")){
            mOrderAddressView.setShowHint(true);
            info_flag = false;
        }else {
            mOrderAddressView.setShowHint(false);
            info_flag = true;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //支付成功,关闭当前页
        if(resultCode==OrderPayResultActivity.CODE_RESULT_PAY_SUCCES){
            finish();
        }else if (resultCode == ReceiptActivity.CODE_RESULT_RECEIPT_SUCCES){
            receiptInfoEnity = (ReceiptInfoEnity) data.getSerializableExtra("receipt");
            mRlReceipt.setClickable(true);
            mCbReceipt.setClickable(true);
            mCbReceipt.setChecked(true);
            switch (Integer.parseInt(receiptInfoEnity.receipt_type)){
                case 1:
                    mTvReceipt.setText("收据");
                    break;
                case 2:
                    mTvReceipt.setText("增值税普通发票");
                    break;
                case 3:
                    mTvReceipt.setText("增值税专用发票");
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_left_layout:
                finish();
                break;
            case R.id.btn_confirm:

                if (!info_flag){
                    ToastUtil.getInstance(this).setContent("请完善机构信息").setShow();
                    mOrderAddressView.setOrgColor(getResources().getColor(R.color.c_red_d94141));
                    return;
                }

                createOrder();
                StatisticsUtil.getInstance(mContext).onCountActionDot("4.1.4");
                break;
            case R.id.order_address_view:
                 launchScreen(EditorActivity.class);
                StatisticsUtil.getInstance(mContext).onCountActionDot("4.1.1");
                break;
            case R.id.rl_receipt_enter:

                Intent intent2Receipt = new Intent(mContext,ReceiptActivity.class);
                if (null != receiptInfoEnity){
                    intent2Receipt.putExtra("receipt",receiptInfoEnity);
                }
                startActivityForResult(intent2Receipt,REQUEST_RECEIPT_CODE);
                break;
        }

    }

}
