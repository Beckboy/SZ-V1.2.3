package com.junhsue.ksee;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.dto.VersionUpdateDTO;
import com.junhsue.ksee.entity.UserInfo;
import com.junhsue.ksee.file.FileUtil;
import com.junhsue.ksee.frame.ScreenManager;
import com.junhsue.ksee.net.api.OkHttpILoginImpl;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.profile.UserProfileService;
import com.junhsue.ksee.utils.CommitUtil;
import com.junhsue.ksee.utils.StatisticsUtil;
import com.junhsue.ksee.utils.StringUtils;
import com.junhsue.ksee.utils.ToastUtil;
import com.junhsue.ksee.view.ActionBar;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MySettingActivity extends BaseActivity implements View.OnClickListener {

    private ActionBar mAbar;
    private TextView mTv,mTvVersion,mTvRed;

    private UserInfo mUserInfo;

    //版本信息
    private String version;
    private VersionUpdateDTO versionUpdateDTO;

    public static final int CLEAR_CACHE = 100;
    //文件缓存大小
    private static final int CACHE_SIZE = 0x148;
    //缓存大小
    private static final String PARAMS_CACHE_SIZE = "params_cache_size";


    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == CLEAR_CACHE) {
                mTv.setText("0.00M");
                Trace.i("--notify homepage");
//        notifyHomePage();
            } else if (msg.what == CACHE_SIZE) {//设置缓存大小
                Bundle bundle = msg.getData();
                if (null != bundle)
                    mTv.setText(bundle.getString(PARAMS_CACHE_SIZE, ""));

            }
        }
    };

    @Override
    protected void onReceiveArguments(Bundle bundle) {
    }

    @Override
    protected int setLayoutId() {
        return R.layout.act_my_setting;
    }

    @Override
    protected void onResume() {
        StatisticsUtil.getInstance(this).onCountPage("1.5.4");
        super.onResume();
    }

    @Override
    protected void onInitilizeView() {

        initView();
        initUserInfo();

    }


    private void initUserInfo() {
        mUserInfo = UserProfileService.getInstance(this).getCurrentLoginedUser();
        if (mUserInfo == null) {
            ToastUtil.getInstance(this).setContent("用户不存在").setShow();
            return;
        }
    }

    private void initView() {
        mAbar = (ActionBar) findViewById(R.id.ab_my_setting);
        mTv = (TextView) findViewById(R.id.tv_my_setting_SaveSize);
        mTvVersion = (TextView) findViewById(R.id.tv_my_setting_aboutSZ);
        mTvRed = (TextView) findViewById(R.id.tv_red_circle);
        mAbar.setOnClickListener(this);

        showCacheData();
        showVersion();
    }


  /**
   * 版本更新
   */
  private void showVersion() {
    CommitUtil util = new CommitUtil();
    mTvVersion.setText("V"+util.getAppVersionName());
    OkHttpILoginImpl.getInstance().verityVersionUpdate(new RequestCallback<VersionUpdateDTO>() {
      @Override
      public void onError(int errorCode, String errorMsg) {

      }

      @Override
      public void onSuccess(VersionUpdateDTO response) {
        version = response.version;
        CommitUtil util = new CommitUtil();
        String localVersion = util.getAppVersionName();
        versionUpdateDTO = response;
        if (StringUtils.isNewVersion(localVersion, version)) {
          versionUpdateDTO.is_new = true;
          mTvRed.setVisibility(View.GONE);
        } else {
          versionUpdateDTO.is_new = false;
          mTvRed.setVisibility(View.VISIBLE);
        }
      }
    });
  }


  /**
   * 缓存
   */
  private void showCacheData() {
    try {
      String cacheSize = FileUtil.getAutoFileOrFilesSizeFormatMB(FileUtil.getImageFolder());
      mTv.setText(cacheSize);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  private void cache() {
    new Thread(new Runnable() {
      @Override
      public void run() {
        FileUtil.deleteFileOfDir(FileUtil.getBaseFilePath(), true);
        ImageLoader.getInstance().clearMemoryCache();
        handler.sendEmptyMessageDelayed(CLEAR_CACHE, 2000);
      }
    }).start();
  }

  public void onclick(View view) {
      switch (view.getId()) {
          case R.id.tv_my_setting_editData:
              startActivity(new Intent(MySettingActivity.this, EditorActivity.class));
              break;
          case R.id.tv_my_setting_accountManager:
              startActivity(new Intent(MySettingActivity.this, AccountManagerActivity.class));
              break;
          case R.id.rl_my_setting_aboutSZ:
              Intent intent2aboutSZ = new Intent(MySettingActivity.this, MyAboutSZActivity.class);
              Bundle bundle = new Bundle();
              bundle.putSerializable("is_new", versionUpdateDTO);
              intent2aboutSZ.putExtras(bundle);
              startActivity(intent2aboutSZ);
              break;
          case R.id.rl_my_setting_versionUpdate:
              if (versionUpdateDTO.is_new) {
                  Toast.makeText(MySettingActivity.this, "当前已经是最新钬花教育社区", Toast.LENGTH_LONG).show();
              } else {
                  Intent updateIntent = new Intent(MySettingActivity.this, DownLoadAPKActivity.class);
                  Bundle upDateBundle = new Bundle();
                  upDateBundle.putSerializable("version_info", versionUpdateDTO);
                  updateIntent.putExtras(upDateBundle);
                  startActivity(updateIntent);
              }
              break;
          case R.id.tv_my_setting_idearesult:
              startActivity(new Intent(MySettingActivity.this, MyFeedbckActivity.class));
              break;
          case R.id.tv_my_setting_receiver:
              ToastUtil.getInstance(this).setContent("接受推送信息").setShow();
              break;
          case R.id.rl_my_setting_cleanSave:
              ToastUtil.getInstance(this).setContent("清除缓存成功").setShow();
              StatisticsUtil.getInstance(this).onCountActionDot("6.3.2");
              if (FileUtil.isFileClear(FileUtil.getBaseFilePath()) == false) {
                  cache();
              }
              break;
          case R.id.btn_my_setting_quitLogin:
//        ToastUtil.getInstance(this).setContent("退出登录成功").setShow();
              Intent intent2login = new Intent(MySettingActivity.this, LoginActivity.class);
              intent2login.putExtra(Constants.REG_PHONENUMBER, mUserInfo.phonenumber);
              mUserInfo = null;
              UserProfileService.getInstance(this).updateCurrentLoginUser(mUserInfo);
              ScreenManager.getScreenManager().popAllActivity();
              startActivity(intent2login);
              StatisticsUtil.getInstance(this).onProfileSignOff();
              StatisticsUtil.getInstance(this).onCountActionDot("6.3.1");
              break;
      }
  }




      @Override
      public void onClick(View v){
          switch (v.getId()) {
              case R.id.ll_left_layout:
                  finish();
                  break;
          }
      }
}
