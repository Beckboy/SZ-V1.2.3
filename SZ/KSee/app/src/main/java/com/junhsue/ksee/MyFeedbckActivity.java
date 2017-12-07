package com.junhsue.ksee;

import android.content.Context;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;

import com.junhsue.ksee.dto.MyFeedbackDTO;
import com.junhsue.ksee.entity.UserInfo;
import com.junhsue.ksee.net.api.OkHttpILoginImpl;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.profile.UserProfileService;
import com.junhsue.ksee.utils.StatisticsUtil;
import com.junhsue.ksee.utils.ToastUtil;
import com.junhsue.ksee.view.ActionBar;

public class MyFeedbckActivity extends BaseActivity implements View.OnClickListener{

  private ActionBar mActionBar;
  private EditText mEdit;

  private Context mContext;
  private UserInfo mUser;

  @Override
  protected void onReceiveArguments(Bundle bundle) {}

  @Override
  protected int setLayoutId() {
    mContext = this;
    return R.layout.act_my_feedbck;
  }

  @Override
  protected void onResume() {
    StatisticsUtil.getInstance(mContext).onCountPage("1.5.4.4");
    super.onResume();
  }

  @Override
  protected void onInitilizeView() {

    initView();

    mUser = UserProfileService.getInstance(mContext).getCurrentLoginedUser();

  }

  private void initView() {
    mActionBar = (ActionBar) findViewById(R.id.title_feedback);
    mEdit = (EditText) findViewById(R.id.edit_feedback);
    mActionBar.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.ll_left_layout:
        finish();
        break;
      case R.id.tv_btn_right:
        String content =mEdit.getText().toString().trim();
        if (content.equals("")){
          ToastUtil.getInstance(mContext).setContent("内容不能为空").setShow();
          break;
        }
        if (null == mUser){
          ToastUtil.getInstance(mContext).setContent("请先登录账户").setShow();
          break;
        }
        submitFeedback(content);
        break;
    }
  }

  /**
   * 提交意见反馈信息
   */
  private void submitFeedback(String content) {
    OkHttpILoginImpl.getInstance().myFeedback(mUser.token, content, "2", new RequestCallback<MyFeedbackDTO>() {
      @Override
      public void onError(int errorCode, String errorMsg) {
        ToastUtil.getInstance(mContext).setContent(errorMsg).setShow();
      }

      @Override
      public void onSuccess(MyFeedbackDTO response) {
        ToastUtil.getInstance(mContext).setContent("提交成功，感谢您的反馈!").setShow();
        new Thread(new Runnable() {
          @Override
          public void run() {
            try {
              Thread.sleep(1000);
              runOnUiThread(new Runnable() {
                @Override
                public void run() {
                  mEdit.setText("");
                }
              });
              MyFeedbckActivity.this.finish();
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
        }).start();
      }
    });
  }
}
