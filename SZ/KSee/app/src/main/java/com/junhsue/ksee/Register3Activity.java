package com.junhsue.ksee;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.dto.RegisterSuccessDTO;
import com.junhsue.ksee.dto.TagListDTO;
import com.junhsue.ksee.entity.TagItem;
import com.junhsue.ksee.net.api.OkHttpILoginImpl;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.utils.CommonUtils;
import com.junhsue.ksee.utils.DensityUtil;
import com.junhsue.ksee.utils.SHA256Encrypt;
import com.junhsue.ksee.utils.ToastUtil;
import com.junhsue.ksee.view.FlowLayout;

import java.util.ArrayList;
import java.util.List;

public class Register3Activity extends BaseActivity{

    private FlowLayout mFlowLayout;
    private CheckBox itemCb;
    private TextView mhint,mline;
    private EditText mEditOri;

    private ImageView mImgLine;
    private TextView mTvTitle;

    private String logintytpe;

    //数据源
    private List<TagItem> datalist = new ArrayList<>();
    //被选中的数据
    private List<TagItem> dataSelector = new ArrayList<>();

    private String name,school,phonenumber,password,avatar;
    private String sex,unionid;

    @Override
    protected void onReceiveArguments(Bundle bundle) {
      name = bundle.getString(Constants.REG_NICKNAME);
      school = bundle.getString("school",null);
      phonenumber = bundle.getString(Constants.REG_PHONENUMBER);
      password = bundle.getString(Constants.REG_PASSWORD);
      Trace.i("SHA256加密前:"+password);
      password = SHA256Encrypt.bin2hex(password); //SHA256加密
      Trace.i("SHA256加密后:"+password);
      avatar = bundle.getString(Constants.REG_AVATAR);

      unionid = bundle.getString(Constants.REG_UNIONID,null);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.act_register3;
    }

    @Override
    protected void onInitilizeView() {
        initView();
    }

    private void initView() {
      mFlowLayout = (FlowLayout) findViewById(R.id.flowlayout_register3);
      mhint = (TextView) findViewById(R.id.tv_register3_domain);
      mline = (TextView) findViewById(R.id.v_register3_line3);
      mEditOri = (EditText) findViewById(R.id.edit_register3_school);
      mImgLine = (ImageView) findViewById(R.id.img_register3_line3);
      mTvTitle = (TextView) findViewById(R.id.tbTitle_register3);

      if (null == school ){
          mEditOri.setVisibility(View.VISIBLE);
          mline.setVisibility(View.VISIBLE);
          mImgLine.setImageResource(R.drawable.resetpassword_02);
          mTvTitle.setText("完善资料");
          logintytpe = Constants.REG_TYPE_W;
      }else {
          mEditOri.setVisibility(View.GONE);
          mline.setVisibility(View.GONE);
          mImgLine.setImageResource(R.drawable.register_03);
          mTvTitle.setText("注册");
          logintytpe = Constants.REG_TYPE_P;
      }

      String t1 = "<font><big><big>你擅长的领域是？</big></big></font>";
      String t2 =  "<font>(请选择1~3个领域)</font>";
      mhint.setText(Html.fromHtml(t1+t2));
        //加载领域列表的数据源信息
        loadData();
    }

  //加载数据源
  private void loadData() {
    OkHttpILoginImpl.getInstance().registerGetTags(new RequestCallback<TagListDTO>() {
      @Override
      public void onError(int errorCode, String errorMsg) {
        ToastUtil.getInstance(Register3Activity.this).setContent(errorMsg).setShow();
      }

      @Override
      public void onSuccess(TagListDTO response) {
        datalist.clear();
        datalist.addAll(response.result);
        initData();
      }
    });
  }

  //更新领域列表的数据
  private void initData() {
      for (int i= 0; i<datalist.size();i++){
        itemCb = (CheckBox) getLayoutInflater().inflate(R.layout.item_tv_flowlayout_register3,mFlowLayout,false);
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.MarginLayoutParams.WRAP_CONTENT);
        lp.leftMargin = DensityUtil.dip2px(this,10);
        lp.rightMargin = 0;
        lp.topMargin = DensityUtil.dip2px(this,12);
        itemCb.setLayoutParams(lp);
        itemCb.setTag(i);
        itemCb.setText(datalist.get(i).name);
        mFlowLayout.addView(itemCb);

        itemCb.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked){
              if (dataSelector.size() >2){
                buttonView.setChecked(false);
                ToastUtil.getInstance(Register3Activity.this).setContent("擅长领域最多3个").setDuration(Toast.LENGTH_SHORT).setShow();
                return;
              }
              buttonView.setTextColor(getResources().getColor(R.color.c_flow_item_normal));
              dataSelector.add(datalist.get(Integer.parseInt(buttonView.getTag().toString())));
            }else {
              buttonView.setTextColor(getResources().getColor(R.color.c_flow_item_selector));
              dataSelector.remove(datalist.get(Integer.parseInt(buttonView.getTag().toString())));
            }

          }
        });
      }
    }

    public void onclick(View view) {
      switch(view.getId()){
       case R.id.tbBtn_register3_back:
           finish();
          break;
       case R.id.tv_register3_finish:
       case R.id.tbNext_register3_step:
           if (!CommonUtils.getIntnetConnect(this)){
               ToastUtil.getInstance(this).setContent(Constants.INTNET_NOT_CONNCATION).setDuration(Toast.LENGTH_SHORT).setShow();
               return;
           }
            registerUser();
         break;
      }
    }


  /**
   * 完成用户注册
   */
  public void registerUser() {
      school = mEditOri.getText().toString();
    if (null == school){
        ToastUtil.getInstance(this).setContent("组织机构/学校 不能为空").setShow();
        return;
    }
    if (dataSelector.size() == 0){
      ToastUtil.getInstance(this).setContent("请选择领域信息").setShow();
      return;
    }

      StringBuilder tags = new StringBuilder();
      for (int i = 0;i < dataSelector.size(); i++){
        tags.append(datalist.get(i).id);
        if (i != (dataSelector.size()-1)){
          tags.append(",");
        }
      }
      if (null == avatar){
          avatar = "";
      }
      OkHttpILoginImpl.getInstance().registerByPhonenumberOrWechat(logintytpe, unionid, phonenumber, password, name, school, avatar, tags+"", new RequestCallback<RegisterSuccessDTO>() {
        @Override
        public void onError(int errorCode, String errorMsg) {
          ToastUtil.getInstance(Register3Activity.this).setContent(errorMsg).setShow();
        }

        @Override
        public void onSuccess(RegisterSuccessDTO response) {
          Intent intent2Finish = new Intent(Register3Activity.this,RegisterFinishActivity.class);
          Bundle bundle = new Bundle();
          bundle.putString(Constants.REG_NICKNAME,name);
          bundle.putString(Constants.REG_PHONENUMBER,phonenumber);
          bundle.putString("organization",school);
          bundle.putString(Constants.REG_AVATAR,avatar);
          intent2Finish.putExtras(bundle);
          startActivity(intent2Finish);
        }
      });
  }
}
