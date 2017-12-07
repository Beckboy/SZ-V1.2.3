package com.junhsue.ksee;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.junhsue.ksee.dto.VersionUpdateDTO;
import com.junhsue.ksee.net.url.WebViewUrl;
import com.junhsue.ksee.profile.UserProfileService;
import com.junhsue.ksee.utils.CommitUtil;
import com.junhsue.ksee.utils.ScreenWindowUtil;
import com.junhsue.ksee.utils.ShareUtils;
import com.junhsue.ksee.utils.StatisticsUtil;
import com.junhsue.ksee.utils.StringUtils;
import com.junhsue.ksee.view.ActionSheet;
import com.umeng.analytics.social.UMPlatformData;

public class MyAboutSZActivity extends BaseActivity implements View.OnClickListener{

  private Context mContext;
  private TextView tvVersion,tvRed;
  private ImageView mImgCode;
  private VersionUpdateDTO isNewVersion;

  @Override
  protected void onReceiveArguments(Bundle bundle) {
    isNewVersion = (VersionUpdateDTO) bundle.getSerializable("is_new");
  }

  @Override
  protected int setLayoutId() {
    mContext = this;
    return R.layout.act_my_about_sz;
  }

  @Override
  protected void onResume() {
    StatisticsUtil.getInstance(mContext).onCountPage("1.5.4.3");
    super.onResume();
  }


  @Override
  protected void onInitilizeView() {
    initView();
  }


  private void initView() {
    findViewById(R.id.title_about_sz_server).setOnClickListener(this);
    findViewById(R.id.ll_new_version_introduce).setOnClickListener(this);
    findViewById(R.id.ll_hide_policy).setOnClickListener(this);
    tvRed = (TextView) findViewById(R.id.tv_red_circle);
    tvVersion = (TextView) findViewById(R.id.tv_cur_version);
    CommitUtil util = new CommitUtil();
    tvVersion.setText("当前版本：V"+util.getAppVersionName());
    mImgCode = (ImageView) findViewById(R.id.img_load_code);
    setImgCodeLayoutparams();
    mImgCode.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View view) {
        shareToWechatAboutSZ();
        return false;
      }
    });
    if (isNewVersion != null && !isNewVersion.is_new){
      tvRed.setVisibility(View.VISIBLE);
    }else {
      tvRed.setVisibility(View.GONE);
    }
  }

  /**
   * 设置下载二维码尺寸
   */
  private void setImgCodeLayoutparams() {
    int width = (int) (28.0 / 75.0 * ScreenWindowUtil.getScreenWidth(mContext));
    ViewGroup.LayoutParams params = mImgCode.getLayoutParams();
    params.width = width;
    params.height = width ;
    mImgCode.setLayoutParams(params);
  }

  public void shareToWechatAboutSZ() {
    String userId = UserProfileService.getInstance(mContext).getCurrentLoginedUser().user_id;
    if (StringUtils.isBlank(userId)) {
      return;
    }
    final String path = "";//如果分享图片获取该图片的本地存储地址
    final String webPage = WebViewUrl.H5_INVITATION_DOWNLOAD;
    final String title = "钬花教育社区";
    final String desc = "教育行业最钬的社区型APP\n100000+教育人的知识共享平台及沟通交流社区";

    final ActionSheet shareActionSheetDialog = new ActionSheet(mContext);
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

        StatisticsUtil.getInstance(mContext).statisticsInviteShareCount(2);

        ShareUtils.getInstance(mContext).sharePage(ShareUtils.SendToPlatformType.TO_FRIEND, webPage, path,
                title, desc, UMPlatformData.UMedia.WEIXIN_FRIENDS);
        if (shareActionSheetDialog != null) {
          shareActionSheetDialog.dismiss();
        }

      }
    });

    llShareCircle.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        StatisticsUtil.getInstance(mContext).statisticsInviteShareCount(1);
        ShareUtils.getInstance(mContext).sharePage(ShareUtils.SendToPlatformType.TO_CIRCLE, webPage, path,
                title, desc, UMPlatformData.UMedia.WEIXIN_CIRCLE);
        if (shareActionSheetDialog != null) {
          shareActionSheetDialog.dismiss();
        }

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


  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.ll_left_layout:
        finish();
        break;
      case R.id.ll_new_version_introduce:
        Intent intent2Version = new Intent(MyAboutSZActivity.this,MyVersionIntroduceActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("is_new", isNewVersion);
        intent2Version.putExtras(bundle);
        startActivity(intent2Version);
        break;
      case R.id.ll_hide_policy:
        startActivity(new Intent(MyAboutSZActivity.this,MyHidePolicyActivity.class));
        break;
    }
  }
}
