package com.junhsue.ksee;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.entity.CommonResultEntity;
import com.junhsue.ksee.entity.SolutionCouponDTO;
import com.junhsue.ksee.entity.SolutionCouponEntity;
import com.junhsue.ksee.entity.SolutionDetails;
import com.junhsue.ksee.entity.UserInfo;
import com.junhsue.ksee.file.FileUtil;
import com.junhsue.ksee.fragment.dialog.InviteDialogFragment;
import com.junhsue.ksee.fragment.dialog.SolutionSendDialogFragment;
import com.junhsue.ksee.fragment.dialog.SolutionSendSuccessDialogFragment;
import com.junhsue.ksee.fragment.dialog.VoiceRecordDialogFragment;
import com.junhsue.ksee.net.api.OKHttpHomeImpl;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.url.WebViewUrl;
import com.junhsue.ksee.profile.UserProfileService;
import com.junhsue.ksee.utils.ShareUtils;
import com.junhsue.ksee.utils.SharedPreferencesUtils;
import com.junhsue.ksee.utils.StatisticsUtil;
import com.junhsue.ksee.utils.ToastUtil;
import com.junhsue.ksee.view.ActionBar;
import com.junhsue.ksee.view.ActionSheet;
import com.junhsue.ksee.view.WebPageView;
import com.umeng.analytics.social.UMPlatformData;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 方案包详情
 * Created by longer on 17/9/25.
 */

public class SolutionDetailsActivity extends BaseActivity implements View.OnClickListener {


    //发送成功
    public static final String BROAD_ACTION_SOLUTION_NOFITY = "com.junhsue.ksee.action.solution.nofity";

    public static final int REQUEST_CODE_SOLUTION = 0x10023;
    //方案包ids
    public static final String PARMAS_SOLUTION_ID = "params_solution_id";
    //方案包链接
    public static final String PARAMS_SOLUTION_LINK = "parmas_solution_link";
    //方案包标题
    public static final String PARAMS_SOLUTION_TITLE = "params_solution_title";
    //方案包
    private String mSolutionIds;
    //
    private WebPageView mSolutionPageView;
    //
    private Context mContext = this;
    //
    private Button mBtnCash;
    //
    private SolutionDetails mSolutionDetails;
    //是否已兑换
    private boolean isConvert;
    //
    private ActionSheet shareActionSheetDialog;


    private List<SolutionCouponEntity> couponEntityList;
    //
    /**
     * 倒计时
     */

    //每次倒计时为60秒
    private int mMaxTime = 60;
    private Timer mTimer;
    private TimerTask mTask;

    @Override
    protected void onReceiveArguments(Bundle bundle) {
        mSolutionIds = bundle.getString(PARMAS_SOLUTION_ID, "");
    }


    @Override
    protected int setLayoutId() {
        return R.layout.act_solution_details;
    }


    @Override
    protected void onInitilizeView() {
        mSolutionPageView = (WebPageView) findViewById(R.id.solution_web_view);
        mBtnCash = (Button) findViewById(R.id.btn_cash);
        //
        mBtnCash.setOnClickListener(this);
        findViewById(R.id.solution_action_bar).setOnClickListener(this);
        couponEntityList = new ArrayList<>();
        //
        loadWebView();
        getDetails();
    }


    /**
     * 加载网页
     */
    private void loadWebView() {
        UserInfo userInfo = UserProfileService.getInstance(getApplicationContext()).getCurrentLoginedUser();
        String userId = userInfo.user_id;
        String url = String.format(WebViewUrl.H5_SOLUTION, userId, mSolutionIds);
        mSolutionPageView.loadUrl(url);
        getSolutionCoupon();
    }


    /**
     * 获取方案包详情
     */
    private void getDetails() {

        OKHttpHomeImpl.getInstance().getSolutiondDetails(mSolutionIds, new RequestCallback<SolutionDetails>() {

            @Override
            public void onError(int errorCode, String errorMsg) {

            }

            @Override
            public void onSuccess(SolutionDetails response) {
                mSolutionDetails = response;
                mBtnCash.setClickable(true);
                isConvert = response.is_auth;
                setBtnStatus();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        isConvert = true;
        setBtnStatus();
    }

    /**
     * 分享弹出框
     */
    private void showShareActionSheetDailog() {
        if (null == mSolutionDetails) {
            return;
        }

        final String path = FileUtil.getImageFolder() + "/" + mSolutionDetails.poster.hashCode();//如果分享图片获取该图片的本地存储地址
        UserInfo userInfo = (UserInfo) UserProfileService.getInstance(mContext).getCurrentLoginedUser();

        final String webPage = String.format(WebViewUrl.H5_SHARE_SOLUTION, userInfo.user_id, mSolutionDetails.id);
        final String title = mSolutionDetails.title;
        final String desc = mSolutionDetails.description;

        shareActionSheetDialog = new ActionSheet(this);
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

                StatisticsUtil.getInstance(mContext).onCountActionDot("5.4.3");
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

                StatisticsUtil.getInstance(mContext).onCountActionDot("5.4.2");
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


    /**
     * 发送邮箱
     * 发送成功后倒计时60s,再次发送
     */
    private void sendMail(String link, String email, String title) {

        UserInfo userInfo = UserProfileService.getInstance(mContext).getCurrentLoginedUser();
        String nickName = userInfo.nickname;
        OKHttpHomeImpl.getInstance().sendSolutionEmail(link, email, title, nickName, new RequestCallback<Object>() {
            @Override
            public void onError(int errorCode, String errorMsg) {

                ToastUtil.showToast(mContext, mContext.getString(R.string.msg_email_send_fail));
            }

            @Override
            public void onSuccess(Object response) {
                ToastUtil.showToast(mContext, mContext.getString(R.string.msg_email_send_success));
                startTask();

            }
        });
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mBtnCash.setText(mMaxTime + "s");
                    //零秒时
                    if (mMaxTime == 0) {
                        stopTask();
                        mBtnCash.setClickable(true);
                        mBtnCash.setText(getApplicationContext().getString(R.string.msg_coupon_convert));

                    }
                    break;
            }
        }

    };

    /**
     * 开始任务
     */
    public void startTask() {
        mBtnCash.setClickable(false);
        if (mTimer == null) {
            mTimer = new Timer();
        }

        if (mTask == null) {
            mTask = new TimerTask() {

                @Override
                public void run() {
                    mMaxTime--;
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                }
            };
        }
        mTimer.schedule(mTask, 0, 1000);
    }

    /**
     * 停止计时器
     */
    public void stopTask() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }

        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        mMaxTime = 60;
    }


    private void setBtnStatus() {
        if (isConvert) {
            mBtnCash.setText(getApplicationContext().getString(R.string.msg_solution_email_send_again));

        } else {
            mBtnCash.setText(getApplicationContext().getString(R.string.msg_coupon_convert));
        }
    }

    /**
     *
     *
     */
    private void handleBtnCash() {


        if (null == mSolutionDetails) {
            return;
        }
        final boolean isCashF = isConvert;

        final String link = mSolutionDetails.resource_link;
        final String solutionIdL = mSolutionIds;
        final String title = mSolutionDetails.title;
        final String emailLocal = SharedPreferencesUtils.getInstance(mContext).getString(Constants.SF_KEY_EMAIL, "");

        //如果已兑换发送邮箱
        if (isCashF) {
            SolutionSendDialogFragment solutionSendDialogFragment = SolutionSendDialogFragment.newInstance();
            solutionSendDialogFragment.show(getSupportFragmentManager(), "");
            solutionSendDialogFragment.setIDialogListener(new SolutionSendSuccessDialogFragment.IDialogListener() {
                @Override
                public void onConfirm() {
                    //发送抵扣券到邮箱

                    sendMail(link, emailLocal, title);
                }
            });

        } else {

            if (couponEntityList == null || couponEntityList.size() <= 0) {
                InviteDialogFragment inviteDialogFragment = InviteDialogFragment.newInstance();
                inviteDialogFragment.setInvitationToShareListener(new InviteDialogFragment.InvitationToShareListener() {
                    @Override
                    public void toShare() {
                        showShareActionSheetDailog();
                    }
                });
                inviteDialogFragment.show(getSupportFragmentManager(), "");

            } else {
                Intent intent = new Intent(mContext, SolutionConvertActivity.class);
                intent.putExtra(SolutionDetailsActivity.PARMAS_SOLUTION_ID, solutionIdL);
                intent.putExtra(SolutionDetailsActivity.PARAMS_SOLUTION_LINK, link);
                intent.putExtra(SolutionDetailsActivity.PARAMS_SOLUTION_TITLE, title);
                startActivity(intent);
            }
        }


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.solution_web_view:
                //handleBtnCash();
                break;
            case R.id.ll_left_layout:
                finish();
                break;
            case R.id.btn_right_one: //分享
                if (null == mSolutionDetails) return;
                showShareActionSheetDailog();
                break;
            case R.id.btn_cash:

                handleBtnCash();
                break;

        }
    }


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            isConvert = true;
            setBtnStatus();
        }
    };


    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BROAD_ACTION_SOLUTION_NOFITY);
        registerReceiver(broadcastReceiver, intentFilter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }


    /**
     * 获取抵扣券列表
     */
    private void getSolutionCoupon() {

        OKHttpHomeImpl.getInstance().getSolutionCouponList(new RequestCallback<SolutionCouponDTO>() {

            @Override
            public void onError(int errorCode, String errorMsg) {

            }

            @Override
            public void onSuccess(SolutionCouponDTO response) {

                couponEntityList.addAll(response.result);
            }
        });
    }
}
