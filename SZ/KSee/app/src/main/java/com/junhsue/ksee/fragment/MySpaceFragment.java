package com.junhsue.ksee.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.junhsue.ksee.ApprovalAndCollectActivity;
import com.junhsue.ksee.EditorActivity;
import com.junhsue.ksee.InviteActivity;
import com.junhsue.ksee.MsgReceiveReplyActivity;
import com.junhsue.ksee.MyCoinActivity;
import com.junhsue.ksee.MyCollectPosterActivity;
import com.junhsue.ksee.MyCustomerServerActivity;
import com.junhsue.ksee.MyOrderListActivity;
import com.junhsue.ksee.MyPosterListActivity;
import com.junhsue.ksee.MySettingActivity;
import com.junhsue.ksee.R;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.common.IntentLaunch;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.entity.UserInfo;
import com.junhsue.ksee.file.ImageLoaderOptions;
import com.junhsue.ksee.profile.UserProfileService;
import com.junhsue.ksee.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 吾
 * Created by longer on 17/3/16 in Junhsue.
 */

public class MySpaceFragment extends BaseFragment implements View.OnClickListener {

    private CircleImageView mImgHeadImg;
    private TextView mTvName, mTvOrganization;
    private Button mBtnQusetion, mBtnOrder, mBtnCoin, mBtnBag, mBtnCustom, mBtnSetting;

    private Context mContext;
    private UserInfo userInfo;

    public static MySpaceFragment newIntance() {
        MySpaceFragment mySpaceFragment = new MySpaceFragment();
        return mySpaceFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.act_my_space;
    }


    @Override
    protected void onInitilizeView(View view) {

        initView(view);

        userInfo = UserProfileService.getInstance(mContext).getCurrentLoginedUser();
        if (userInfo != null) {
            initViewUserInfo();
        }

    }

    /**
     * 用户登录成功，完善用户信息
     */
    private void initViewUserInfo() {
        ImageLoader.getInstance().displayImage(userInfo.avatar, mImgHeadImg, ImageLoaderOptions.option(R.drawable.img_default_course_suject));
        mTvName.setText(userInfo.nickname);
        mTvOrganization.setText(userInfo.organization);
    }

    private void initView(View view) {
        mImgHeadImg = (CircleImageView) view.findViewById(R.id.img_my_headimg);
        mTvName = (TextView) view.findViewById(R.id.tv_my_name);
        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/fzqkbysj.TTF");
        mTvName.setTypeface(typeface);
        mTvOrganization = (TextView) view.findViewById(R.id.tv_my_organization);

        view.findViewById(R.id.rl_list_set).setOnClickListener(this);
        view.findViewById(R.id.btn_my_speak).setOnClickListener(this);
        view.findViewById(R.id.btn_my_order).setOnClickListener(this);
        view.findViewById(R.id.btn_my_collect).setOnClickListener(this);
        view.findViewById(R.id.btn_my_coin).setOnClickListener(this);
        view.findViewById(R.id.btn_my_custom).setOnClickListener(this);
        view.findViewById(R.id.btn_my_setting).setOnClickListener(this);
        view.findViewById(R.id.btn_my_bag).setOnClickListener(this);

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
                    userInfo = UserProfileService.getInstance(mContext).getCurrentLoginedUser();
                    if (userInfo != null) {
                        initViewUserInfo();
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
        mContext.registerReceiver(receiverUserInfoUpdate, intentFilter);
    }

    @Override
    public void onDestroy() {
        mContext.unregisterReceiver(receiverUserInfoUpdate);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_list_set: //头部头像
                startActivity(new Intent(mContext, EditorActivity.class));
                break;
            case R.id.btn_my_speak: //我的发言
//                startActivity(new Intent(getActivity(), MyAnswersActivity.class));
                startActivity(new Intent(getActivity(), MyPosterListActivity.class));
                break;
            case R.id.btn_my_order: //我的订单
                startActivity(new Intent(getActivity(), MyOrderListActivity.class));
                break;
            case R.id.btn_my_collect: //我的收藏
                startActivity(new Intent(getActivity(), MyCollectPosterActivity.class));
                break;
            case R.id.btn_my_coin: //我的兑换券
                startActivity(new Intent(getActivity(), MyCoinActivity.class));
                break;
            case R.id.btn_my_bag: //邀请有奖
                startActivity(new Intent(getActivity(), InviteActivity.class));
                break;
            case R.id.btn_my_custom: //我的客服
                startActivity(new Intent(getActivity(), MyCustomerServerActivity.class));
                break;
            case R.id.btn_my_setting: //设置
                IntentLaunch.launch(getActivity(), MySettingActivity.class);
                break;
        }
    }
}
