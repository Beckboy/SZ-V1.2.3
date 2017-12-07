package com.junhsue.ksee;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easefun.polyvsdk.live.chat.ppt.api.PolyvLiveMessage;
import com.easefun.polyvsdk.live.chat.ppt.api.entity.PolyvLiveMessageEntity;
import com.easefun.polyvsdk.live.chat.ppt.api.listener.PolyvLiveMessageListener;
import com.easefun.polyvsdk.live.permission.PolyvPermission;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.entity.GoodsInfo;
import com.junhsue.ksee.entity.LiveEntity;
import com.junhsue.ksee.entity.OrderEntity;
import com.junhsue.ksee.entity.UserInfo;
import com.junhsue.ksee.file.FileUtil;
import com.junhsue.ksee.fragment.ColleageLiveDetailsFragment;
import com.junhsue.ksee.fragment.dialog.LiveApplySuccessDialogFragment;
import com.junhsue.ksee.frame.MyApplication;
import com.junhsue.ksee.net.api.OKHttpCourseImpl;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.url.RequestUrl;
import com.junhsue.ksee.net.url.WebViewUrl;
import com.junhsue.ksee.profile.UserProfileService;
import com.junhsue.ksee.utils.ShareUtils;
import com.junhsue.ksee.utils.StatisticsUtil;
import com.junhsue.ksee.utils.ToastUtil;
import com.junhsue.ksee.view.ActionBar;
import com.junhsue.ksee.view.ActionSheet;
import com.junhsue.ksee.view.WebPageView;
import com.umeng.analytics.social.UMPlatformData;

/**
 * 直播详情页
 * Created by longer on 17/6/15.
 */


public class LiveDetailsActivity extends BaseActivity implements View.OnClickListener {


    //更新直播详情状态
    public final static String BROAD_ACTION_LIVE_DETAILS="com.junhsue.ksee.action.live_details";


    //直播id
    public final static String PARAMS_LIVE_ID = "params_live_id";
    //直播标题
    public final static String PARAMS_LIVE_TITLE = "params_live_title";
    //
    private static final int SETTING = 1;
    private Button mBtnLive;

    //
    private ActionBar mActionBar;
    //
    private LiveEntity mLiveEntity;
    //
    private WebPageView mWebPageView;
    //分享对话框
    private ActionSheet shareActionSheetDialog;
    public String mTitle;
    //
    public String mLiveId;

    //
    private Context mContext=this;
    //

    @Override
    protected void onReceiveArguments(Bundle bundle) {
        mLiveId = bundle.getString(PARAMS_LIVE_ID, "");
        mTitle = bundle.getString(PARAMS_LIVE_TITLE, "直播详情");
    }

    @Override
    protected int setLayoutId() {
        return R.layout.act_colleage_live_details;
    }

    @Override
    protected void onResume() {
        StatisticsUtil.getInstance(this).onCountPage("1.4.2");
        super.onResume();
    }


    @Override
    protected void onInitilizeView() {

        mActionBar = (ActionBar) findViewById(R.id.live_action_bar);
        mBtnLive=(Button)findViewById(R.id.btn_live);
        mWebPageView=(WebPageView)findViewById(R.id.web_page_view);
        //
        mActionBar.setTitleColor(getResources().getColor(R.color.c_gray_242E42));
        UserInfo userInfo=UserProfileService.getInstance(this).getCurrentLoginedUser();
        mWebPageView.loadUrl(String.format(WebViewUrl.H5_LIVE_DETAILS,userInfo.user_id,mLiveId));
        //
        mActionBar.setOnClickListener(this);
        mBtnLive.setOnClickListener(this);
        findViewById(R.id.btn_live_look).setOnClickListener(this);
        //
        getLiveDetails();
    }


    private void getLiveDetails() {

        OKHttpCourseImpl.getInstance().getLiveDetails(mLiveId,
                new RequestCallback<LiveEntity>() {

                    @Override
                    public void onError(int errorCode, String errorMsg) {

                    }

                    @Override
                    public void onSuccess(LiveEntity response) {
                        mLiveEntity = response;
                        initilizeData();
                        setBtnStatus();
                    }
                });
    }

    /**
     * 直播底部按钮分为不同的状态
     * <p>
     * <可以直接进入直播观看页>
     * <p>
     * 1.正在直播&直播免费
     * <p>
     * 2.正在直播&用户已购买
     * <p>
     * <p>
     * #按钮设置成"进入直播"
     * <p>
     * <正在直播>
     * <p>
     * 1.用户未购买& 非免费直播,按钮设置成"购买直播",点击跳转到购买页
     * <p>
     * <直播未开始>
     * <p>
     * 1.用户未购买,非免费直播&可以直接进入购买详情,按钮设置成 "购买直播",按钮可以点击
     * <p>
     * 2.用户已购买 或者 免费和收费直播,按钮设置成 "进入直播",点击按钮提示"直播还未开始"
     * <p>
     * <p>
     * <直播已结束>
     * <p>
     * 1.不能购买,按钮设置成"已结束",按钮不能点击
     *
     *
     *
     *
     * 专享直播 是不允许报名的
     *
     */

    private void setBtnStatus() {
        if(null==mLiveEntity) return;
        if(mLiveEntity.shelf_status==2){
            mBtnLive.setBackgroundColor(getResources().getColor(R.color.c_gray_9da1a7));
            mBtnLive.setText("已下架");
            return;
        }
        if (mLiveEntity.living_status == LiveEntity.LiveStatus.END) {
            mBtnLive.setBackgroundColor(getResources().getColor(R.color.c_gray_9da1a7));
            mBtnLive.setText("已结束");
            return;
        }
        //添加白名单机制, 如果是专享直播是不能参与报名
        if(mLiveEntity.is_private==1 && mLiveEntity.status == LiveEntity.LivePayStatus.PAY_NO){
            mBtnLive.setText("专享观看");
            return ;
        }

        if(mLiveEntity.is_private==1
                && mLiveEntity.status == LiveEntity.LivePayStatus.PAY_OK ){
            mBtnLive.setText("立即观看");
            return;
        }


        if (mLiveEntity.living_status== LiveEntity.LiveStatus.NO_START &&
                mLiveEntity.status == LiveEntity.LivePayStatus.PAY_NO &&
                mLiveEntity.is_free == 0) {
            //mBtnLive.setBackgroundColor(getResources().getColor(R.color.c_red_B33030));
            mBtnLive.setText("购买直播");
        } else if (mLiveEntity.living_status == LiveEntity.LiveStatus.LIVING &&
                mLiveEntity.status == LiveEntity.LivePayStatus.PAY_NO &&
                mLiveEntity.is_free == 0) {
            mBtnLive.setBackgroundColor(getResources().getColor(R.color.c_red_B33030));
            mBtnLive.setText("购买直播");
        }
//        else if((mLiveEntity.living_status==LiveEntity.LiveStatus.LIVING &&
//                mLiveEntity.is_free == 1) || (mLiveEntity.living_status==LiveEntity.LiveStatus.LIVING &&
//                mLiveEntity.status==LiveEntity.LivePayStatus.PAY_OK)){
//            mBtnLive.setText("立即观看");
//            mBtnLive.setBackgroundColor(getResources().getColor(R.color.c_green_59b197));
//        }
//        else if((mLiveEntity.living_status==LiveEntity.LiveStatus.NO_START &&
//                mLiveEntity.status==LiveEntity.LivePayStatus.PAY_OK) &&
//                mLiveEntity.living_status==LiveEntity.LiveStatus.NO_START &&
//                        mLiveEntity.is_free == 1){
//            mBtnLive.setText("立即观看");
//            mBtnLive.setBackgroundColor(getResources().getColor(R.color.c_green_59b197));//       }

    else if(mLiveEntity.living_status == LiveEntity.LiveStatus.NO_START &&
                mLiveEntity.is_free == 1 &&
                mLiveEntity.status==LiveEntity.LivePayStatus.PAY_NO){
                //直播未开始,免费,未报名,显示免费报名
            mBtnLive.setText("免费报名");
        }else if(mLiveEntity.living_status == LiveEntity.LiveStatus.LIVING &&
                mLiveEntity.is_free == 1 &&
                mLiveEntity.status==LiveEntity.LivePayStatus.PAY_NO){
            //直播正在开始,免费,未报名,显示免费报名
            mBtnLive.setText("免费报名");
        }else if(mLiveEntity.living_status == LiveEntity.LiveStatus.NO_START &&
                mLiveEntity.is_free == 1 &&
                mLiveEntity.status==LiveEntity.LivePayStatus.PAY_OK) {
            //直播未开始,免费,未报名,显示免费博阿明
            mBtnLive.setText("已报名");
        }else if(mLiveEntity.living_status == LiveEntity.LiveStatus.LIVING &&
                mLiveEntity.is_free == 1 &&
                mLiveEntity.status==LiveEntity.LivePayStatus.PAY_OK) {
            //直播未开始,免费,未报名,显示免费博阿明
            mBtnLive.setText("立即观看");
        }

    }


    /**
     * 分享弹出框
     */
    private void showShareActionSheetDailog() {
        UserInfo userInfo=UserProfileService.getInstance(this).getCurrentLoginedUser();
        final String path = FileUtil.getImageFolder() + "/" + mLiveEntity.poster.hashCode();//如果分享图片获取该图片的本地存储地址
        final String webPage = String.format(WebViewUrl.H5_SHARE_H5_LIVE_DETAILS, userInfo.user_id,mLiveEntity.live_id);
        final String title = mLiveEntity.title;
        final String desc = mLiveEntity.content;

        shareActionSheetDialog = new ActionSheet(mContext);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.component_share_dailog, null);
        shareActionSheetDialog.setContentView(view);
        shareActionSheetDialog.show();


        LinearLayout llShareFriend = (LinearLayout) view.findViewById(R.id.ll_share_friend);
        LinearLayout llShareCircle = (LinearLayout) view.findViewById(R.id.ll_share_circle);
        TextView cancelButton = (TextView) view.findViewById(R.id.tv_cancel);

        llShareFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShareUtils.getInstance(mContext).sharePage(ShareUtils.SendToPlatformType.TO_FRIEND, webPage, path,
                        title, desc, UMPlatformData.UMedia.WEIXIN_FRIENDS);

                if (shareActionSheetDialog != null) {
                    shareActionSheetDialog.dismiss();
                }
                StatisticsUtil.getInstance(mContext).onCountActionDot("5.1.4");

            }
        });

        llShareCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtils.getInstance(mContext).sharePage(ShareUtils.SendToPlatformType.TO_CIRCLE, webPage, path,
                        title, desc, UMPlatformData.UMedia.WEIXIN_CIRCLE);
                if (shareActionSheetDialog != null) {
                    shareActionSheetDialog.dismiss();
                }
                StatisticsUtil.getInstance(mContext).onCountActionDot("5.1.3");
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shareActionSheetDialog.isShowing()) {
                    shareActionSheetDialog.dismiss();
                }
            }
        });


    }
    private void initilizeData() {

   /*     if (null != mLiveEntity) {
            if (mLiveEntity.living_status == LiveEntity.LiveStatus.END) {
                mBtnLive.setText("已结束");
                //mLLLiveNoticeTime.setVisibility(View.INVISIBLE);
            } else if (mLiveEntity.living_status == LiveEntity.LiveStatus.NO_START) {

            } else if (mLiveEntity.living_status == LiveEntity.LiveStatus.LIVING) {
                //mLLLiveNoticeTime.setVisibility(View.INVISIBLE);

            }
        }*/
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_live:
                //
                handleBtn();
                break;

            case R.id.ll_left_layout:
                finish();
                break;
            case R.id.btn_right_one: //分享
                showShareActionSheetDailog();
                break;
            case  R.id.btn_live_look:
                //gotoPlay();
                //loginLive();
                break;
        }
    }

    private PolyvPermission polyvPermission = null;

    private void loginLive(){
        polyvPermission = new PolyvPermission();
        polyvPermission.setResponseCallback(new PolyvPermission.ResponseCallback() {
            @Override
            public void callback() {
                requestPermissionWriteSettings();
            }
        });
    }

    /**
     * 请求写入设置的权限
     */
    @SuppressLint("InlinedApi")
    private void requestPermissionWriteSettings() {
        if (!PolyvPermission.canMakeSmores()) {
            gotoPlay();
        } else if (Settings.System.canWrite(this)) {
            gotoPlay();
        } else {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + this.getPackageName()));
            startActivityForResult(intent, SETTING);
        }
    }

    private void gotoPlay() {
        //final String channelId = et_channelid.getText().toString();
        alertLoadingProgress();
        final String polyUserId=MyApplication.POLYV_ACCOUNT_USER_ID;
        final String channelId=mLiveEntity.channel_number;
        new PolyvLiveMessage().getLiveType(channelId, new PolyvLiveMessageListener() {
            @Override
            public void success(boolean isPPTLive, PolyvLiveMessageEntity entity) {
                dismissLoadingDialog();
                Intent playUrl=new Intent(LiveDetailsActivity.this,PolyvPlayerActivity.class);
                playUrl.putExtra("uid",polyUserId);
                playUrl.putExtra("cid", channelId);
                playUrl.putExtra(PARAMS_LIVE_ID,mLiveId);
                playUrl.putExtra(PARAMS_LIVE_TITLE,mTitle);
                startActivity(playUrl);
            }

            @Override
            public void fail(final String failTips, final int code) {
                dismissLoadingDialog();
                LiveDetailsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LiveDetailsActivity.this, "获取直播信息失败\n" + failTips + "-" + code, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * 申请直播报名
     */
    private void applyLive(){
        String token="";
        UserInfo mUserInfo=UserProfileService.getInstance(getApplicationContext()).getCurrentLoginedUser();

        if(null!=mUserInfo){
            token=mUserInfo.token;
        }else{
            token= MyApplication.getToken();
        }

        GoodsInfo goodsInfo = GoodsInfo.cloneOject(mLiveEntity.poster, mLiveEntity.live_id,
                mLiveEntity.title, mLiveEntity.price,
                GoodsInfo.GoodsType.getType(mLiveEntity.business_id));

        String businessId=String.valueOf(goodsInfo.goodsType.getValue());
        String amount=String.valueOf(goodsInfo.price);
        String goodsId=goodsInfo.id;
        String poster=goodsInfo.imgUrl == null ? null:goodsInfo.imgUrl;
        String goodsName=goodsInfo.goodsName;
        String count=String.valueOf(1);
        String is_receipt="";
        String receipt_type_id ="";
        String receipt_content_id ="";
        String organization ="";
        String contact ="";
        String contact_phone ="";
        String contact_address ="";
        String uniform_code ="";
        String register_address ="";
        String register_phone ="";
        String bank ="";
        String bank_account = "";
        OKHttpCourseImpl.getInstance().createOrder(token, poster, businessId, amount, goodsId, goodsName, count,
                is_receipt, receipt_type_id, receipt_content_id, organization, contact, contact_phone,
                contact_address, uniform_code, register_address, register_phone, bank, bank_account, new RequestCallback<OrderEntity>() {
                    @Override
                    public void onError(int errorCode, String errorMsg) {
                        ToastUtil.showToast(mContext,getString(R.string.live_apply_fail));

                    }

                    @Override
                    public void onSuccess(OrderEntity response) {
                        //ToastUtil.showToast(mContext,getString(R.string.live_apply_successful));
                        showLiveApplyDialog();
                        getLiveDetails();
                    }
                });
    }


    /**
     * 显示直播申请成功dialog
     */
    private void showLiveApplyDialog(){
        LiveApplySuccessDialogFragment liveApplySuccessDialogFragment= LiveApplySuccessDialogFragment.newInstance();
        liveApplySuccessDialogFragment.show(getSupportFragmentManager(),"");
    }
    /**
     * 按钮处理
     */
    private void handleBtn() {
        if(null==mLiveEntity) return;

        //已下架
        if(mLiveEntity.shelf_status==2){
            return;
        }
        //专享直播, 白名单可直接跳转到视频观看详情页,otherwise文案提示
        //非专享直播,可以走免费报名功能
        if(mLiveEntity.is_private==1 && mLiveEntity.status == LiveEntity.LivePayStatus.PAY_OK){
            enterLiveLookPage();
            return ;
        }else if(mLiveEntity.is_private==1
                && mLiveEntity.status == LiveEntity.LivePayStatus.PAY_NO){
            ToastUtil.showToast(mContext,getString(R.string.live_no_permission));
            return;
        }

        if (mLiveEntity.living_status == LiveEntity.LiveStatus.LIVING && mLiveEntity.is_free == 1) {
            enterLiveLookPage();

        } else if (mLiveEntity.living_status == LiveEntity.LiveStatus.LIVING &&
                mLiveEntity.status == LiveEntity.LivePayStatus.PAY_OK) {
            enterLiveLookPage();

        } else if (mLiveEntity.living_status == LiveEntity.LiveStatus.LIVING &&
                mLiveEntity.status == LiveEntity.LivePayStatus.PAY_NO &&
                mLiveEntity.is_free == 0) {

            jumpLivePay();
        } else if (mLiveEntity.living_status == LiveEntity.LiveStatus.NO_START &&
                mLiveEntity.status == LiveEntity.LivePayStatus.PAY_NO &&
                mLiveEntity.is_free == 0) {
            // 直播未开始 & 未支付 &非免费
            jumpLivePay();
        }
//        else if (mLiveEntity.living_status == LiveEntity.LiveStatus.NO_START &&
//                mLiveEntity.status == LiveEntity.LivePayStatus.PAY_NO &&
//                mLiveEntity.is_free == 1) {
//            ToastUtil.getInstance(getApplicationContext()).setContent("直播还未开始").setShow();
//        }
        else if (mLiveEntity.living_status == LiveEntity.LiveStatus.NO_START &&
                mLiveEntity.status == LiveEntity.LivePayStatus.PAY_OK &&
                mLiveEntity.is_free == 0) {
            ToastUtil.getInstance(getApplicationContext()).setContent("直播还未开始").setShow();
        } else if (mLiveEntity.living_status == LiveEntity.LiveStatus.END) {
            return;
        }else if(mLiveEntity.living_status == LiveEntity.LiveStatus.NO_START &&
                mLiveEntity.is_free == 1 &&
                mLiveEntity.status==LiveEntity.LivePayStatus.PAY_NO){
            /**
             * 直播未开始,免费,未报名
             */
            applyLive();

        }else if(mLiveEntity.living_status == LiveEntity.LiveStatus.LIVING &&
                mLiveEntity.is_free == 1 &&
                mLiveEntity.status==LiveEntity.LivePayStatus.PAY_NO){
            /**
             *
             * 直播正在开始,免费,未报名
             */
            applyLive();
        }else if(mLiveEntity.living_status == LiveEntity.LiveStatus.NO_START &&
                mLiveEntity.is_free == 1 &&
                mLiveEntity.status==LiveEntity.LivePayStatus.PAY_OK) {
            /**
             * 直播未开始,免费,已报名
             */
            //ToastUtil.getInstance(getApplicationContext()).setContent("直播还未开始").setShow();

        }else if(mLiveEntity.living_status == LiveEntity.LiveStatus.LIVING &&
                mLiveEntity.is_free == 1 &&
                mLiveEntity.status==LiveEntity.LivePayStatus.PAY_OK) {
            /**
             *直播已开始,免费,已报名
             */
            enterLiveLookPage();
        }

    }


    /**
     * 跳转到直播观看页
     */
    private void enterLiveLookPage() {
        if (null == mLiveEntity) return;

        gotoPlay();

//        Bundle bundle = new Bundle();
//        bundle.putSerializable(ColleageLiveLookActivity.PARAMS_LIVE, mLiveEntity);
//        launchScreen(ColleageLiveLookActivity.class, bundle);
//
        StatisticsUtil.getInstance(mContext).onCountActionDot("5.1.2");
    }


    /**
     * 跳转直播购买页
     */
    private void jumpLivePay() {
        GoodsInfo goodsInfo = GoodsInfo.cloneOject(mLiveEntity.poster, mLiveEntity.live_id,
                mLiveEntity.title, mLiveEntity.price,
                GoodsInfo.GoodsType.getType(mLiveEntity.business_id));
        Bundle bundle = new Bundle();
        bundle.putSerializable(ConfirmOrderActivity.PARAMS_GOODS_INFO, goodsInfo);
        launchScreen(ConfirmOrderActivity.class, bundle);

        StatisticsUtil.getInstance(mContext).onCountActionDot("5.1.1");

    }

    BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action=intent.getAction();
            //更新直播状态
            if(BROAD_ACTION_LIVE_DETAILS.equals(action)){
                Trace.i("udapte live details!");
                getLiveDetails();
            }
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        if (shareActionSheetDialog != null) {
            shareActionSheetDialog.dismiss();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(BROAD_ACTION_LIVE_DETAILS);
        registerReceiver(broadcastReceiver,intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
