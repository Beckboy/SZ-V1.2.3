package com.junhsue.ksee;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.utils.CommonUtils;
import com.junhsue.ksee.utils.OptionsWindowHelperProvince;
import com.junhsue.ksee.utils.ToastUtil;
import com.junhsue.ksee.view.ActionBar;

import cn.jeesoft.widget.pickerview.CharacterPickerWindow;

public class EditorAddressActivity extends BaseActivity implements View.OnClickListener,PopupWindow.OnDismissListener{

  private ActionBar mAbar;
  private TextView mTvCity;
  private EditText mEditDistrict;

  private String provinces,citys,areas,address;

  /**
   * 当PopupWindow显示或者消失时改变背景色
   */
  private WindowManager.LayoutParams lp;

  @Override
  protected void onReceiveArguments(Bundle bundle) {
    if (null != bundle){
      provinces = bundle.getString("province");
      citys = bundle.getString("city");
      areas = bundle.getString("district");
      address = bundle.getString("address");
    }
  }

  @Override
  protected int setLayoutId() {
    return R.layout.act_editor_address;
  }

  @Override
  protected void onInitilizeView() {
    initView();
  }

  private void initView() {
    mAbar = (ActionBar) findViewById(R.id.ab_my_editor_address);
    mAbar.setOnClickListener(this);
    mTvCity = (TextView) findViewById(R.id.tv_city_my_editor_address);
    mEditDistrict = (EditText) findViewById(R.id.edit_district_my_editor_address);
    if (null != provinces ){
      mTvCity.setText(provinces+"  "+citys+"  "+areas);
    }
    if (null != address){
      mEditDistrict.setText(address);
    }

    lp = getWindow().getAttributes();


  }

  private void showWindowProvince(){
    //初始化
    CharacterPickerWindow window = OptionsWindowHelperProvince.builder(this, new OptionsWindowHelperProvince.OnOptionsSelectListener() {
      @Override
      public void onOptionsSelect(String province, String city, String area) {
        provinces = province;
        citys = city;
        areas = area;
        mTvCity.setText(province+"  "+city+"  "+area);
      }
    });
//    window.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM,0,0);

    window.setOutsideTouchable(true);
    window.setFocusable(true);
    window.setBackgroundDrawable(new BitmapDrawable());
    window.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM,0,0);
    window.setOnDismissListener(this);
    // 设置背景颜色变暗
    lp.alpha = 0.5f;
    getWindow().setAttributes(lp);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.ll_left_layout:
        finish();
        break;
      case R.id.tv_btn_right:
        if (!CommonUtils.getIntnetConnect(this)){
          ToastUtil.getInstance(this).setContent(Constants.INTNET_NOT_CONNCATION).setDuration(Toast.LENGTH_SHORT).setShow();
          return;
        }
        address = mEditDistrict.getText().toString().trim();
        if (null == mTvCity.getText().toString().trim()){
          ToastUtil.getInstance(this).setContent("省市区不能为空").setShow();
          return;
        }
        if (address.equals("")){
          ToastUtil.getInstance(this).setContent("详细地址不能为空").setShow();
          return;
        }
        Intent intentToBack = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("province",provinces);
        bundle.putString("city",citys);
        bundle.putString("area",areas);
        bundle.putString("address",address);
        intentToBack.putExtras(bundle);
        setResult(RESULT_OK,intentToBack);
        finish();
        break;
    }
  }

  public void onclick(View view) {
    switch (view.getId()){
      case R.id.tv_city_my_editor_address:
        showWindowProvince();
        break;
    }
  }

  @Override
  public void onDismiss() {
    // 设置背景颜色变暗
    lp = getWindow().getAttributes();
    lp.alpha = 1.0f;
    getWindow().setAttributes(lp);
  }
}
