package com.junhsue.ksee;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.easeui.EaseConstant;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.dto.SettingNickNameDTO;
import com.junhsue.ksee.dto.TagListDTO;
import com.junhsue.ksee.entity.TagItem;
import com.junhsue.ksee.entity.UserInfo;
import com.junhsue.ksee.file.ImageLoaderOptions;
import com.junhsue.ksee.net.api.OkHttpILoginImpl;
import com.junhsue.ksee.net.api.OkHttpQiniu;
import com.junhsue.ksee.net.api.SlideDateTimeListener;
import com.junhsue.ksee.net.callback.BroadIntnetConnectListener;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.callback.SaveImgGetTokenCallback;
import com.junhsue.ksee.net.callback.SaveImgSuccessCallback;
import com.junhsue.ksee.net.error.NetResultCode;
import com.junhsue.ksee.profile.UserProfileService;
import com.junhsue.ksee.utils.CommonUtils;
import com.junhsue.ksee.utils.LoginUtils;
import com.junhsue.ksee.utils.OptionsWindowHelperDate;
import com.junhsue.ksee.utils.PopWindowTokenErrorUtils;
import com.junhsue.ksee.utils.StatisticsUtil;
import com.junhsue.ksee.utils.StringUtils;
import com.junhsue.ksee.utils.ToastUtil;
import com.junhsue.ksee.view.ActionBar;
import com.junhsue.ksee.view.CircleImageView;
import com.junhsue.ksee.view.WheelView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.jeesoft.widget.pickerview.CharacterPickerWindow;

public class EditorActivity extends BaseActivity implements View.OnClickListener,PopupWindow.OnDismissListener,SaveImgGetTokenCallback ,SaveImgSuccessCallback {

  private ActionBar mAbar;
  private CircleImageView mImgHeadImg;
  private RadioGroup mRg;
  private TextView mTvName,mTvOrgenization,mTvAddress,mTvJob,mTvSex,mTvBirthday,mTvNameTitle,mTvOrgenizationTitle;

  private UserInfo mUserInfo;
  private PopupWindow mSexPopupWindow,mHeadImgPopWindow,mOrganizationPopupWindow;

  private SaveImgGetTokenCallback saveImgGetTokenCallback = this;
  private SaveImgSuccessCallback saveImgSuccessCallback = this;
  private Activity mContext;

  private SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy.MM.dd");

  //数据源
  private List<TagItem> datalist = new ArrayList<>();

  //popWin弹窗是否被展示的标识符
  private boolean popWinShow = false;

  private static final int requestForNickName = 1;
  private static final int requestForNickOrganization = 2;
  private static final int requestForAddress = 3;

  /* 请求识别码 */
  private static final int CODE_GALLERY_REQUEST = 0xa0;//本地
  private static final int CODE_CAMERA_REQUEST = 0xa1;//拍照
  private static final int CODE_RESULT_REQUEST = 0xa2;//最终裁剪后的结果

  /**
   * 性别标识符
   */
  private static final int man = 0;
  private static final int woman = 1;
  private static final int secert = 2;

  // 职务的下标
  private int job_index = 0;
  private int job_index_first = 0;
  /**
   * 七牛云头像存储所需的token
   */
  private String token;
  /**
   * 头像设置成功获取的数组
   */
  private byte[] headImgByte;

  /**
   * 当PopupWindow显示或者消失时改变背景色
   */
  private WindowManager.LayoutParams lp;

  @Override
  protected void onReceiveArguments(Bundle bundle) {}

  @Override
  protected int setLayoutId() {
    return R.layout.act_editor;
  }

  @Override
  protected void onResume() {
    StatisticsUtil.getInstance(mContext).onCountPage("1.5.4.1");
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

    loadUserInfo();
  }

  private void loadUserInfo() {
    if (mUserInfo.avatar != null) {
      ImageLoader.getInstance().displayImage(mUserInfo.avatar, mImgHeadImg, ImageLoaderOptions.option(R.drawable.img_default_course_suject));
    }
    if (mUserInfo.nickname != null) {
      mTvName.setText(mUserInfo.nickname);
    }
    if (mUserInfo.organization != null) {
      mTvOrgenization.setText(mUserInfo.organization);
    }
    if (mUserInfo.province != null) {
      mTvAddress.setText(mUserInfo.province +"  "+mUserInfo.city+"  "+mUserInfo.district+"  "+mUserInfo.address);
    }
    if (mUserInfo.position_name != null) {
      mTvJob.setText(mUserInfo.position_name);
    }
    if (mUserInfo.gender != null) {
      String sex = StringUtils.getSexint2String(Integer.parseInt(mUserInfo.gender));
      mTvSex.setText(sex);
    }
    if (mUserInfo.birthday != null) {
      long time = Long.parseLong(mUserInfo.birthday+"000");
      mTvBirthday.setText(mFormatter.format(time));
    }
  }

  private void initView() {
    mContext = this;
    mAbar = (ActionBar) findViewById(R.id.ab_my_editorData);
    mAbar.setOnClickListener(this);
    mImgHeadImg = (CircleImageView) findViewById(R.id.img_editorData_headImg);
    mTvName = (TextView) findViewById(R.id.tv_editorData_name);
    mTvOrgenization = (TextView) findViewById(R.id.tv_editorData_orgenization);
    mTvAddress = (TextView) findViewById(R.id.tv_editorData_address);
    mTvJob = (TextView) findViewById(R.id.tv_editorData_job);
    mTvSex = (TextView) findViewById(R.id.tv_editorData_sex);
    mTvBirthday = (TextView) findViewById(R.id.tv_editorData_birthday);
    mTvNameTitle = (TextView) findViewById(R.id.tv_editorData_nameTitle);
    mTvOrgenizationTitle = (TextView) findViewById(R.id.tv_editorData_orgenizationTitle);

    lp = getWindow().getAttributes();

    LoginUtils.getHeadImgToken(saveImgGetTokenCallback); //获取服务的token
  }

  public void onclick(View view) {
    switch (view.getId()){
      case R.id.rl_my_editorData_headImg:
        if (popWinShow) return;
        popwinShow(true);
        showHeadImgPopupWindow();
        break;
      case R.id.rl_my_editorData_name:
        Intent intenttochoose1 = new Intent(this, EditorChooseActivity.class);
        Bundle bundle1 = new Bundle();
        bundle1.putString("token",mUserInfo.token);
        bundle1.putString("title",mTvNameTitle.getText().toString().trim());
        bundle1.putString("content",mTvName.getText().toString().trim());
        bundle1.putInt("type",1);
        intenttochoose1.putExtras(bundle1);
        startActivityForResult(intenttochoose1,requestForNickName);
        StatisticsUtil.getInstance(mContext).onCountPage("1.5.4.1.2");
        break;
      case R.id.rl_my_editorData_orgenization:
        Intent intenttochoose2 = new Intent(this, EditorChooseActivity.class);
        Bundle bundle2= new Bundle();
        bundle2.putString("token",mUserInfo.token);
        bundle2.putString("title",mTvOrgenizationTitle.getText().toString().trim());
        bundle2.putString("content",mTvOrgenization.getText().toString().trim());
        bundle2.putInt("type",2);
        intenttochoose2.putExtras(bundle2);
        startActivityForResult(intenttochoose2,requestForNickOrganization);
        StatisticsUtil.getInstance(mContext).onCountPage("1.5.4.1.3");
        break;
      case R.id.rl_my_editorData_address:
        Intent intentGetAddress = new Intent(this, EditorAddressActivity.class);
        Bundle bundleAddress= new Bundle();
        bundleAddress.putString("province",mUserInfo.province);
        bundleAddress.putString("city",mUserInfo.city);
        bundleAddress.putString("district",mUserInfo.district);
        bundleAddress.putString("address",mUserInfo.address);
        intentGetAddress.putExtras(bundleAddress);
        startActivityForResult(intentGetAddress,requestForAddress);
        StatisticsUtil.getInstance(mContext).onCountPage("1.5.4.1.4");
        break;
      case R.id.rl_my_editorData_job:
        if (!CommonUtils.getIntnetConnect(this)){
          ToastUtil.getInstance(this).setContent(Constants.INTNET_NOT_CONNCATION).setDuration(Toast.LENGTH_SHORT).setShow();
          return;
        }
        if (popWinShow) return;
        popwinShow(true);
        showPositionPopupWindow();
        Trace.i("job");
        break;
      case R.id.rl_my_editorData_sex:
        if (popWinShow) return;
        popwinShow(true);
        showSexPopupWindow();
        break;
      case R.id.rl_my_editorData_birthday:
        if (popWinShow) return;
        popwinShow(true);
        showBirthPopupWindow();
        break;
      case R.id.pop_organization_true:
        if (!CommonUtils.getIntnetConnect(this)){
          mOrganizationPopupWindow.dismiss();
          ToastUtil.getInstance(this).setContent(Constants.INTNET_NOT_CONNCATION).setDuration(Toast.LENGTH_SHORT).setShow();
          return;
        }
        Trace.i("点击确定");
        OkHttpILoginImpl.getInstance().settingJob(mUserInfo.token, datalist.get(job_index).id+"", new RequestCallback<SettingNickNameDTO>() {
          @Override
          public void onError(int errorCode, String errorMsg) {
            switch (errorCode){
              case NetResultCode.CODE_LOGIN_STATE_ERROR: //登录态错误，重新登录
                PopWindowTokenErrorUtils.getInstance(mContext).showPopupWindow(R.layout.act_editor);
                break;
              default:
                ToastUtil.getInstance(mContext).setContent(errorMsg).setShow();
                break;
            }
          }

          @Override
          public void onSuccess(SettingNickNameDTO response) {
            job_index_first = job_index;
            mUserInfo.position_id = datalist.get(job_index).id+"";
            mUserInfo.position_name = datalist.get(job_index).name;
            UserProfileService.getInstance(EditorActivity.this).updateCurrentLoginUser(mUserInfo);
            loadUserInfo();
            ToastUtil.getInstance(EditorActivity.this).setContent("职务修改成功").setDuration(Toast.LENGTH_SHORT).setShow();
          }
        });

        mOrganizationPopupWindow.dismiss();
        break;
      case R.id.pop_cancel:
        mHeadImgPopWindow.dismiss();
        break;
      case R.id.pop_camera:  //打开相机
        Intent intentFromCamera = new Intent(this, ImageSelectActivity.class);
        intentFromCamera.putExtra("select",0);
        startActivityForResult(intentFromCamera,CODE_GALLERY_REQUEST);
        mHeadImgPopWindow.dismiss();
        break;
      case R.id.pop_local:  //打开本地相册
        Intent intentFromGallery = new Intent(this, ImageSelectActivity.class);
        intentFromGallery.putExtra("select",1);
        startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
        mHeadImgPopWindow.dismiss();
        break;
    }
  }

  /**
   * 设置出生年月的弹吐框
   */
  private void showBirthPopupWindow() {
    //初始化
    CharacterPickerWindow window = OptionsWindowHelperDate.builder(this, new OptionsWindowHelperDate.OnOptionsSelectListener() {
      @Override
      public void onOptionsSelect(String province, String city, String area) {
        final String birthday = province+"."+city+"."+area;
        OkHttpILoginImpl.getInstance().settingBirthday(mUserInfo.token, birthday, new RequestCallback<SettingNickNameDTO>() {
          @Override
          public void onError(int errorCode, String errorMsg) {
            switch (errorCode){
              case NetResultCode.CODE_LOGIN_STATE_ERROR: //登录态错误，重新登录
                PopWindowTokenErrorUtils.getInstance(mContext).showPopupWindow(R.layout.act_editor);
                break;
              default:
                ToastUtil.getInstance(mContext).setContent(errorMsg).setShow();
                break;
            }
          }

          @Override
          public void onSuccess(SettingNickNameDTO response) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
            String birth = "";
            try {
              birth = String.valueOf(dateFormat.parse(birthday).getTime());
            } catch (ParseException e) {
              e.printStackTrace();
            }
//            long time = Long.parseLong(birth);
            mUserInfo.birthday = birth.substring(0,birth.length()-3);
            Log.i("date",birth.substring(0,birth.length()-3));
            UserProfileService.getInstance(EditorActivity.this).updateCurrentLoginUser(mUserInfo);
            loadUserInfo();
            ToastUtil.getInstance(EditorActivity.this).setContent("生日修改成功").setDuration(Toast.LENGTH_SHORT).setShow();
          }
        });
      }
    });
    window.setOutsideTouchable(true);
    window.setFocusable(true);
    window.setBackgroundDrawable(new BitmapDrawable());
    window.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM,0,0);
    window.setOnDismissListener(this);
    // 设置背景颜色变暗
    lp.alpha = 0.5f;
    getWindow().setAttributes(lp);

  }

  /**
   * 修改职务的弹吐框
   */
  private void showPositionPopupWindow() {

    // 构建弹出框View
    final View outerView = LayoutInflater.from(this).inflate(R.layout.dialog_setting_organization_popupwindow, null);

    final WheelView wv = (WheelView) outerView.findViewById(R.id.wheel1);

    OkHttpILoginImpl.getInstance().settingGetJobList(new RequestCallback<TagListDTO>() {
      @Override
      public void onError(int errorCode, String errorMsg) {
        popwinShow(false);
        switch (errorCode){
          case NetResultCode.CODE_LOGIN_STATE_ERROR: //登录态错误，重新登录
            PopWindowTokenErrorUtils.getInstance(mContext).showPopupWindow(R.layout.act_editor);
            break;
          default:
            ToastUtil.getInstance(mContext).setContent(errorMsg).setShow();
            break;
        }
      }

      @Override
      public void onSuccess(TagListDTO response) {
        datalist.clear();
        datalist.addAll(response.result);
        initData(outerView, wv);
      }
    });
  }

  /**
   * 初始化职务列表的数据源
   */
  private void initData(View outerView, WheelView wheelView) {
    // wv1.setOffset(0);// 偏移量
    wheelView.setOffset(1);// 偏移量
    final ArrayList<String> data = new  ArrayList<String>();
    data.clear();
    for (int i = 0; i < datalist.size();i++){ // 实际内容
//      data.addAll(Arrays.asList(PLANETS));
      data.add(datalist.get(i).name);
    }
    if (null != mUserInfo.position_id){
      job_index_first = Integer.parseInt(mUserInfo.position_id);
    }else {
      job_index_first = 1;
    }
//    wv1.setItems(Arrays.asList(PLANETS));// 实际内容
    wheelView.setItems(data);// 实际内容
    wheelView.setSeletion(0*data.size()+job_index_first-1);// 设置默认被选中的项目
    wheelView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
      @Override
      public void onSelected(int selectedIndex, String item) {
        // 选中后的处理事件
        Trace.i("[Dialog]selectedIndex: " + selectedIndex
            + ", item: " + item+":"+(selectedIndex%data.size()));
        job_index = selectedIndex%data.size();
      }
    });

    mOrganizationPopupWindow = new PopupWindow(outerView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,true);
    mOrganizationPopupWindow.setContentView(outerView);
    mOrganizationPopupWindow.setOnDismissListener(this);

    //显示PopWindow
    View rootView = LayoutInflater.from(this).inflate(R.layout.act_editor,null);
    mOrganizationPopupWindow.setOutsideTouchable(true);
    mOrganizationPopupWindow.setFocusable(true);
    mOrganizationPopupWindow.setBackgroundDrawable(new BitmapDrawable());
//    mOrganizationPopupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
    mOrganizationPopupWindow.showAtLocation(rootView, Gravity.CENTER,0,0);
    // 设置背景颜色变暗
    lp.alpha = 0.5f;
    getWindow().setAttributes(lp);
  }


  /**
   * 修改头像的PopWindow对话框
   */
  private void showHeadImgPopupWindow(){
    //设置contentView
    View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_register2_popupwindow,null);
    mHeadImgPopWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,true);
    mHeadImgPopWindow.setContentView(contentView);
    mHeadImgPopWindow.setOnDismissListener(this);

    //显示PopWindow
    View rootView = LayoutInflater.from(this).inflate(R.layout.act_editor,null);
    mHeadImgPopWindow.setOutsideTouchable(true);
    mHeadImgPopWindow.setFocusable(true);
    mHeadImgPopWindow.setBackgroundDrawable(new BitmapDrawable());
    mHeadImgPopWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
    mHeadImgPopWindow.showAtLocation(rootView, Gravity.BOTTOM,0,0);
    // 设置背景颜色变暗
    lp.alpha = 0.5f;
    getWindow().setAttributes(lp);
  }

  /**
   * 展示性别的PopWindow对话框
   */
  private void showSexPopupWindow(){
    //设置contentView
    View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_setting_sex_popupwindow,null);
    mSexPopupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,true);
    mSexPopupWindow.setContentView(contentView);
    mSexPopupWindow.setOnDismissListener(this);

    //显示PopWindow
    View rootView = LayoutInflater.from(this).inflate(R.layout.act_editor,null);
    mSexPopupWindow.setOutsideTouchable(true);
    mSexPopupWindow.setFocusable(true);
    mSexPopupWindow.setBackgroundDrawable(new BitmapDrawable());
//    mSexPopupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
    mSexPopupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);
    Trace.i("mSexPopupWindow.showAtLocation");
    // 设置背景颜色变暗
    lp.alpha = 0.5f;
    getWindow().setAttributes(lp);

    mRg = (RadioGroup) contentView.findViewById(R.id.pop_sex_rg);
    if (null != mUserInfo.gender) {
      RadioButton rb = (RadioButton) mRg.getChildAt(Integer.parseInt(mUserInfo.gender));
      rb.setChecked(true);
    }

    mRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (!CommonUtils.getIntnetConnect(EditorActivity.this)){
          mSexPopupWindow.dismiss();
          ToastUtil.getInstance(EditorActivity.this).setContent(Constants.INTNET_NOT_CONNCATION).setDuration(Toast.LENGTH_SHORT).setShow();
        }else {
          switch (checkedId) {
            case R.id.pop_sex_rb_man:
              updateSex(man + "");
              break;
            case R.id.pop_sex_rb_woman:
              updateSex(woman + "");
              break;
            case R.id.pop_sex_rb_secret:
              updateSex(secert + "");
              break;
            default:
              break;
          }
        }
      }
    });
  }

  private void popwinShow(boolean b) {
    popWinShow = b;
  }

  private void updateSex(final String sex) {
    OkHttpILoginImpl.getInstance().settingSex(mUserInfo.token, sex, new RequestCallback<SettingNickNameDTO>() {
      @Override
      public void onError(int errorCode, String errorMsg) {
        mSexPopupWindow.dismiss();
        switch (errorCode){
          case NetResultCode.CODE_LOGIN_STATE_ERROR: //登录态错误，重新登录
            PopWindowTokenErrorUtils.getInstance(mContext).showPopupWindow(R.layout.act_editor);
            break;
          default:
            ToastUtil.getInstance(mContext).setContent(errorMsg).setShow();
            break;
        }
      }

      @Override
      public void onSuccess(SettingNickNameDTO response) {
        new Thread(new Runnable() {
          @Override
          public void run() {
            try {
              Thread.sleep(100);
              runOnUiThread(new Runnable() {
                @Override
                public void run() {
                  mSexPopupWindow.dismiss();
                }
              });
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
        }).start();
        mUserInfo.gender = sex;
        UserProfileService.getInstance(EditorActivity.this).updateCurrentLoginUser(mUserInfo);
        loadUserInfo();
        ToastUtil.getInstance(EditorActivity.this).setContent("性别修改成功").setDuration(Toast.LENGTH_SHORT).setShow();
      }
    });
  }
  
  

  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.ll_left_layout:
        finish();
        break;
    }
  }

  @Override
  protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK) {
      switch (requestCode) {
        case requestForNickName: //修改昵称
          mUserInfo.nickname = data.getExtras().getString("value");
          UserProfileService.getInstance(this).updateCurrentLoginUser(mUserInfo);
          loadUserInfo();
          sendBroad("昵称");
          break;
        case requestForNickOrganization://修改学校/机构
          mUserInfo.organization = data.getExtras().getString("value");
          UserProfileService.getInstance(this).updateCurrentLoginUser(mUserInfo);
          loadUserInfo();
          sendBroad("学校/机构");
          break;
        //头像
        case CODE_GALLERY_REQUEST:
          byte[] bitmap=data.getByteArrayExtra("bitmap");
          headImgByte = bitmap.clone();
          final String key = mUserInfo.phonenumber+"_"+System.currentTimeMillis();
          alertLoadingProgress();
          OkHttpQiniu.getInstance().loadImg(this,saveImgSuccessCallback,bitmap,key,token);
          break;
        case requestForAddress://修改联系地址
          final String strP = data.getExtras().getString("province");
          final String strC = data.getExtras().getString("city");
          final String strArea = data.getExtras().getString("area");
          final String strAddress = data.getExtras().getString("address");
          OkHttpILoginImpl.getInstance().settingAddress(mUserInfo.token, strP, strC, strArea, strAddress, new RequestCallback<SettingNickNameDTO>() {
            @Override
            public void onError(int errorCode, String errorMsg) {
              switch (errorCode){
                case NetResultCode.CODE_LOGIN_STATE_ERROR: //登录态错误，重新登录
                  PopWindowTokenErrorUtils.getInstance(mContext).showPopupWindow(R.layout.act_editor);
                  break;
                default:
                  ToastUtil.getInstance(mContext).setContent(errorMsg).setShow();
                  break;
              }
            }

            @Override
            public void onSuccess(SettingNickNameDTO response) {
              mUserInfo.province = strP;
              mUserInfo.city = strC;
              mUserInfo.district = strArea;
              mUserInfo.address = strAddress;
              UserProfileService.getInstance(EditorActivity.this).updateCurrentLoginUser(mUserInfo);
              loadUserInfo();
              ToastUtil.getInstance(EditorActivity.this).setContent("联系地址修改成功").setDuration(Toast.LENGTH_SHORT).setShow();
            }
          });
          break;
      }
    }
  }

  @Override
  public void onDismiss() {
    // 设置背景颜色变暗
    lp = getWindow().getAttributes();
    lp.alpha = 1.0f;
    getWindow().setAttributes(lp);
    popwinShow(false);
  }

  /**
   * 获取七牛云头像上传所需token的接口回调
   * @param token
   */
  @Override
  public void getToken(String token) {
    this.token = token;
  }

  /**
   * 头像上传成功接口回调
   * @param avatar
   */
  @Override
  public void getavatar(final String avatar) {
//    Glide.with(this).load(headImgByte).into(mImgHeadImg);
    Trace.i("头像上传成功接口回调");
    headImgByte = null;
    OkHttpILoginImpl.getInstance().settingAvatar(mUserInfo.token, avatar, new RequestCallback<SettingNickNameDTO>() {
      @Override
      public void onError(int errorCode, String errorMsg) {
        dismissLoadingDialog();
        switch (errorCode){
          case NetResultCode.CODE_LOGIN_STATE_ERROR: //登录态错误，重新登录
            PopWindowTokenErrorUtils.getInstance(mContext).showPopupWindow(R.layout.act_editor);
            break;
          default:
            ToastUtil.getInstance(EditorActivity.this).setContent(errorMsg).setShow();
            break;
        }
      }

      @Override
      public void onSuccess(SettingNickNameDTO response) {
        dismissLoadingDialog();
        mUserInfo.avatar = avatar;
        UserProfileService.getInstance(EditorActivity.this).updateCurrentLoginUser(mUserInfo);
        loadUserInfo();
        sendBroad("头像");
      }
    });
  }

  /**
   * 头像上传失败接口回调
   */
  @Override
  public void getavatar_fail(String msg) {

  }


  /**
   * 通知MySpaceFragment更新用户的信息
   */
  public void sendBroad(String title) {
    Intent intent = new Intent();
    intent.setAction(Constants.BROAD_ACTION_USERINFO_UPDATE);
    sendBroadcast(intent);
    Trace.i("发送广播更新用户信息");
    ToastUtil.getInstance(this).setContent(title+"修改成功").setDuration(Toast.LENGTH_SHORT).setShow();
  }



}
