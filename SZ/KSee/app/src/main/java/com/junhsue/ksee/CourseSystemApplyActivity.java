package com.junhsue.ksee;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.entity.CommonResultEntity;
import com.junhsue.ksee.entity.CourseSystemApplyEntity;
import com.junhsue.ksee.entity.GoodsInfo;
import com.junhsue.ksee.entity.UserInfo;
import com.junhsue.ksee.fragment.ColleageCourseSubjectFragment;
import com.junhsue.ksee.frame.MyApplication;
import com.junhsue.ksee.net.api.OKHttpCourseImpl;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.profile.UserProfileService;
import com.junhsue.ksee.utils.StatisticsUtil;
import com.junhsue.ksee.utils.StringUtils;
import com.junhsue.ksee.utils.ToastUtil;
import com.junhsue.ksee.view.ActionBar;
import com.junhsue.ksee.view.OrderAddressView;
import com.junhsue.ksee.view.OrderInfoView;

/**
 * 系统课报名
 * Created by longer on 17/4/12.
 */


public class CourseSystemApplyActivity extends BaseActivity  implements View.OnClickListener,OrderInfoView.IOrderChangeClickListener {


    private ActionBar mActionBar;
    //
    private OrderAddressView mOrderAddressView;
    //
    private OrderInfoView mOrderInfoView;

    private UserInfo mUserInfo;
    //
    private GoodsInfo mGoodsInfo;
    //
    private TextView mTxtQuantity;

    //用户机构信息是否完善
    private boolean info_flag = true;

    @Override
    protected void onReceiveArguments(Bundle bundle) {
        mUserInfo= UserProfileService.getInstance(getApplicationContext()).getCurrentLoginedUser();
        mGoodsInfo=(GoodsInfo)bundle.getSerializable(ConfirmOrderActivity.PARAMS_GOODS_INFO);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.act_course_system_apply;
    }


    @Override
    protected void onInitilizeView() {
        mActionBar=(ActionBar)findViewById(R.id.action_bar);
        mOrderAddressView=(OrderAddressView)findViewById(R.id.order_address_view);
        mOrderInfoView=(OrderInfoView)findViewById(R.id.order_info_view);
        mTxtQuantity=(TextView)findViewById(R.id.txt_quantity);

        //
        mActionBar.setOnClickListener(this);
        mOrderAddressView.setOnClickListener(this);
        mOrderInfoView.setIOrderChangeClickListener(this);
        findViewById(R.id.btn_confirm).setOnClickListener(this);
        //
        fillOrderInfo();
        //
        fillUserInfo();
        //设置默认份数为1
        setApplyNumber(1);
    }

    /** 填充商品信息*/
    private void fillOrderInfo(){
        if(null==mGoodsInfo)
            return;
        mOrderInfoView.setOrderData(mGoodsInfo.imgUrl,mGoodsInfo.goodsName,mGoodsInfo.price,mGoodsInfo.goodsType);
        mOrderInfoView.updateGoodsTotal(1,mGoodsInfo.price);
    }


    /** 填充用户信息*/
    private void fillUserInfo(){
        mUserInfo = UserProfileService.getInstance(getApplicationContext()).getCurrentLoginedUser();
        if(null==mUserInfo)
            return;
        mOrderAddressView.setAddressInfo(mUserInfo.nickname,mUserInfo.phonenumber,mUserInfo.organization);

    }


    @Override
    protected void onResume() {
        StatisticsUtil.getInstance(this).onCountPage("1.4.5.1");
        super.onResume();
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
                info_flag = false;
            }

            @Override
            public void onSuccess(CommonResultEntity response) {
                mOrderAddressView.setShowHint(false);
                info_flag = true;
            }
        });
    }

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

    @Override
    public void onChange(int number) {
        setApplyNumber(number);
    }



    private void setApplyNumber(int number){
        String str=String.format(getString(R.string.msg_course_system_apply_number),String.valueOf(number));
        mTxtQuantity.setText(str);
    }


    /**
     * 系统课报名*/
    private void  applyCourse(){

        String count=String.valueOf(mOrderInfoView.getGoodsNumber());
        String syscoursecount="";
        OKHttpCourseImpl.getInstance().courseApply(mGoodsInfo.id,count,syscoursecount,new RequestCallback<CourseSystemApplyEntity>() {
            @Override
            public void onError(int errorCode, String errorMsg) {

            }

            @Override
            public void onSuccess(CourseSystemApplyEntity response) {
                //报名成功
                Bundle bundle=new Bundle();
                bundle.putString(CourseSystemAppleyResultActivity.PARAMS_IMG,mGoodsInfo.imgUrl);
                launchScreen(CourseSystemAppleyResultActivity.class,bundle);
                finish();
            }
        });
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.ll_left_layout:
                finish();
                break;

            case R.id.iv_btn_two: //分享
                break;

            case R.id.order_address_view:
                launchScreen(EditorActivity.class);
                break;
            case R.id.btn_confirm:

                if (!info_flag){
                    ToastUtil.getInstance(this).setContent("请完善机构信息").setShow();
                    mOrderAddressView.setOrgColor(getResources().getColor(R.color.c_red_d94141));
                    return;
                }

                 applyCourse();
                break;

        }
    }
}
