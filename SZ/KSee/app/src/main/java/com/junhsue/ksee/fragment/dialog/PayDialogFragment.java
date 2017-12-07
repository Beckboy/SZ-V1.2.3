package com.junhsue.ksee.fragment.dialog;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.junhsue.ksee.R;

/**
 * 订单支付对话框
 * Created by longer on 17/7/17.
 */

public class PayDialogFragment extends BaseDialogFragment implements  View.OnClickListener{


    private RelativeLayout mRLAliPay;
    private RelativeLayout mRLWechatPay;
    private ImageView  mImgAliPayTag;
    private ImageView mImgWechatPayTag;
    private Button mBtnPay;

    private IPayOnClickListener iPayOnClickListener;

    //支付方式,is true 为支付宝支付,otherwise 为微信支付,默认为true
    private boolean mIsAlipay=true;

    public static PayDialogFragment newInstance() {
        PayDialogFragment payDialogFragment = new PayDialogFragment();
        return payDialogFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.common_dialog_style);
    }



    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_order_pay, null);
        //显示动画
        getDialog().getWindow().setWindowAnimations(R.style.ActionSheetStyle);
        getDialog().setCanceledOnTouchOutside(true);
        //
        initilizeView(view);
        return view;
    }



    private void initilizeView(View view){
        mRLAliPay=(RelativeLayout)view.findViewById(R.id.rl_alipay);
        mRLWechatPay=(RelativeLayout)view.findViewById(R.id.rl_wechat_pay);
        mImgAliPayTag=(ImageView)view.findViewById(R.id.img_alipay_tag);
        mImgWechatPayTag=(ImageView)view.findViewById(R.id.img_wechat_tag);
        mBtnPay=(Button)view.findViewById(R.id.btn_pay);
        //
        mRLWechatPay.setOnClickListener(this);
        mRLAliPay.setOnClickListener(this);
        mBtnPay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case  R.id.rl_alipay:
                mImgAliPayTag.setBackgroundResource(R.drawable.icon_pay_selected);
                mImgWechatPayTag.setBackgroundResource(R.drawable.icon_pay_normal);
                mIsAlipay=true;
                break;

            case R.id.rl_wechat_pay:
                mImgAliPayTag.setBackgroundResource(R.drawable.icon_pay_normal);
                mImgWechatPayTag.setBackgroundResource(R.drawable.icon_pay_selected);
                mIsAlipay=false;
                break;
            case R.id.btn_pay:
                if(null==iPayOnClickListener) return;
                dismissAllowingStateLoss();

                if(mIsAlipay){
                    iPayOnClickListener.onAliPay();
                }else{
                    iPayOnClickListener.onWechatPay();

                }
                break;
        }
    }





    /**
     * 支付选择监听事件
     * onAlipay() 支付宝
     *
     * onWechatPay() 微信
     */
    public interface  IPayOnClickListener{

        void onAliPay();

        void onWechatPay();
    }


    public void setIPayOnClickListener(IPayOnClickListener iPayOnClickListener) {
        this.iPayOnClickListener = iPayOnClickListener;
    }
}
