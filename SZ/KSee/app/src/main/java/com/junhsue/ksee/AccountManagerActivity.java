package com.junhsue.ksee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.entity.UserInfo;
import com.junhsue.ksee.frame.ScreenManager;
import com.junhsue.ksee.profile.UserProfileService;
import com.junhsue.ksee.utils.StatisticsUtil;
import com.junhsue.ksee.utils.ToastUtil;
import com.junhsue.ksee.view.ActionBar;

public class AccountManagerActivity extends BaseActivity implements View.OnClickListener{

  private ActionBar mAbar;
  private TextView mTvname,mTvPhonenumber;

  private UserInfo mUserInfo;

  private final int RESETPASSWORD = 0;

  @Override
  protected void onReceiveArguments(Bundle bundle) {

  }

  @Override
  protected int setLayoutId() {
    return R.layout.act_account_manager;
  }

  @Override
  protected void onResume() {
    StatisticsUtil.getInstance(this).onCountPage("1.5.4.2");
    super.onResume();
  }

  
  @Override
  protected void onInitilizeView() {
    initView();
    initUserInfo();
  }

  private void initUserInfo() {
    mUserInfo = UserProfileService.getInstance(this).getCurrentLoginedUser();
    if (mUserInfo == null){
      ToastUtil.getInstance(this).setContent("用户不存在").setShow();
      return;
    }
    mTvname.setText(mUserInfo.nickname);
    mTvPhonenumber.setText(mUserInfo.phonenumber);
  }

  private void initView() {
    mAbar = (ActionBar) findViewById(R.id.ab_my_accountManager);
    mAbar.setOnClickListener(this);

    mTvname = (TextView) findViewById(R.id.tv_accountManager_name);
    mTvPhonenumber = (TextView) findViewById(R.id.tv_accountManager_phone);
  }

  public void onclick(View view) {
    switch (view.getId()){
      case R.id.rl_my_accountManager_name:
        break;

      case R.id.rl_my_accountManager_phonenumber:
        break;

      case R.id.rl_my_accountManager_resetPassword:
        Intent intent = new Intent(AccountManagerActivity.this,ResetPassword1Activity.class);
        Bundle bundle = new Bundle();
        bundle.putString("phonenumber",mUserInfo.phonenumber);
        intent.putExtras(bundle);
        startActivityForResult(intent,RESETPASSWORD);
        break;

      case R.id.rl_my_accountManager_sina:
        ToastUtil.getInstance(this).setContent("新浪").setShow();
        break;

      case R.id.rl_my_accountManager_weixin:
        ToastUtil.getInstance(this).setContent("微信").setShow();
        break;

      case R.id.rl_my_accountManager_QQ:
        ToastUtil.getInstance(this).setContent("QQ").setShow();
        break;
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK){
      switch (requestCode){
        case RESETPASSWORD:
          Trace.i("修改成功");
          ScreenManager.getScreenManager().popAllActivity();
          UserProfileService.getInstance(this).updateCurrentLoginUser(null);
          startActivity(new Intent(AccountManagerActivity.this,LoginActivity.class));
          break;
      }
    }
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.ll_left_layout:
        finish();
        break;
    }
  }
}
