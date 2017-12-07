package com.junhsue.ksee;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.net.api.OkHttpQiniu;
import com.junhsue.ksee.net.callback.SaveImgGetTokenCallback;
import com.junhsue.ksee.net.callback.SaveImgSuccessCallback;
import com.junhsue.ksee.utils.CommonUtils;
import com.junhsue.ksee.utils.InputUtil;
import com.junhsue.ksee.utils.LoginUtils;
import com.junhsue.ksee.utils.StringUtils;
import com.junhsue.ksee.utils.ToastUtil;
import com.junhsue.ksee.view.CircleImageView;

import java.io.ByteArrayOutputStream;


public class Register2Activity extends BaseActivity implements SaveImgGetTokenCallback, SaveImgSuccessCallback {

    private EditText mEditName,mEditSchool;
    private CircleImageView mImgBtnHeadImg;
    private PopupWindow mPopWindow;

    private String phonenumber,password,name,school,token;

    private Bitmap cameraBitmap;  //相机获取的照片

    private SaveImgGetTokenCallback callback = this;

    /* 请求识别码 */
    private static final int CODE_GALLERY_REQUEST = 0xa0;//本地

    private SaveImgSuccessCallback savecallback = this;


    @Override
    protected void onReceiveArguments(Bundle bundle) {
        phonenumber = bundle.getString(Constants.REG_PHONENUMBER);
        password = bundle.getString(Constants.REG_PASSWORD);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.act_register2;
    }

    @Override
    protected void onInitilizeView() {
        initView();
    }

    private void initView() {
        mEditName = (EditText) findViewById(R.id.edit_register2_name);
        mEditSchool = (EditText) findViewById(R.id.edit_register2_school);
        mImgBtnHeadImg = (CircleImageView) findViewById(R.id.imgBtn_register2_camera);

        LoginUtils.getHeadImgToken(callback); //获取服务的token
    }

    public void onclick(View view) {
        switch (view.getId()){
            case R.id.tbBtn_register2_back:
                finish();
                break;
            case R.id.rl_register2_nextstep:
            case R.id.tbNext_register2_step:
                if (!CommonUtils.getIntnetConnect(this)){
                    ToastUtil.getInstance(this).setContent(Constants.INTNET_NOT_CONNCATION).setDuration(Toast.LENGTH_SHORT).setShow();
                    return;
                }
                name = mEditName.getText().toString();
                school = mEditSchool.getText().toString();
                if (!StringUtils.isNotBlank(name)){
                    ToastUtil.getInstance(this).setContent("姓名 不能为空").setShow();
                    return;
                }
                if (InputUtil.noContainsEmoji(name)){
                    ToastUtil.getInstance(this).setContent("姓名 中不能含有表情 ").setShow();
                    return;
                }
                if (name.length() > 10){
                    ToastUtil.getInstance(this).setContent("姓名 长度不能超过10位字符 ").setShow();
                    return;
                }
                if (!StringUtils.isNotBlank(school)){
                    ToastUtil.getInstance(this).setContent("机构/学校 不能为空").setShow();
                    return;
                }
                if (InputUtil.noContainsEmoji(school)){
                    ToastUtil.getInstance(this).setContent("机构/学校 中不能含有表情 ").setShow();
                    return;
                }
                if (school.length() > 30){
                    ToastUtil.getInstance(this).setContent("机构/学校 长度不能超过30位字符 ").setShow();
                    return;
                }
//                if (null == cameraBitmap ){
//                    ToastUtil.getInstance(this).setContent("头像 不能为空").setShow();
//                    return;
//                }
                alertLoadingProgress();
                if (null == cameraBitmap){
                    getavatar(null);
                    return;
                }
                saveImgToQiniu(); //将头像保存到七牛云

                break;
            case R.id.imgBtn_register2_camera:
                showPopupWindow();
                break;
            case R.id.pop_cancel:
                mPopWindow.dismiss();
                break;
            case R.id.pop_camera:  //打开相机
                Intent intentFromCamera = new Intent(this, ImageSelectActivity.class);
                intentFromCamera.putExtra("select",0);
                startActivityForResult(intentFromCamera,CODE_GALLERY_REQUEST);
                mPopWindow.dismiss();
                break;
            case R.id.pop_local:  //打开本地相册
                Intent intentFromGallery = new Intent(this, ImageSelectActivity.class);
                intentFromGallery.putExtra("select",1);
                startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
                mPopWindow.dismiss();
                break;
        }
    }


    private void saveImgToQiniu() {
        final String key = phonenumber+"_"+System.currentTimeMillis();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        cameraBitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        final byte[] data = baos.toByteArray();
        OkHttpQiniu.getInstance().loadImg(this,savecallback,data,key,token);
    }

  /**
   * 展示PopWindow对话框
   */
    private void showPopupWindow(){
        //设置contentView
        View contentView = LayoutInflater.from(Register2Activity.this).inflate(R.layout.dialog_register2_popupwindow,null);
        mPopWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,true);
        mPopWindow.setContentView(contentView);

        //显示PopWindow
        View rootView = LayoutInflater.from(Register2Activity.this).inflate(R.layout.act_register2,null);
        mPopWindow.showAtLocation(rootView, Gravity.BOTTOM,0,0);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //通过data获取数据
            switch (requestCode) {
                case CODE_GALLERY_REQUEST:
                    byte[] bitmap=data.getByteArrayExtra("bitmap");
                    Glide.with(this).load(bitmap).into(mImgBtnHeadImg);
                    cameraBitmap = BitmapFactory.decodeByteArray(bitmap,0,bitmap.length);
                    break;
            }
        }
    }

  /**
   * 上传头像所需从服务器获取的token
   * @param token
   */
  @Override
    public void getToken(String token) {
        this.token = token;
    }

    @Override
    public void getavatar(String avatar) {
        Intent intent2register3 = new Intent(this,Register3Activity.class);
        Bundle bundle2register3 = new Bundle();
        bundle2register3.putString(Constants.REG_NICKNAME,name);
        bundle2register3.putString("school",school);
        bundle2register3.putString(Constants.REG_PHONENUMBER,phonenumber);
        bundle2register3.putString(Constants.REG_PASSWORD,password);
        bundle2register3.putString(Constants.REG_AVATAR,avatar);
        intent2register3.putExtras(bundle2register3);
        startActivity(intent2register3);
        dismissLoadingDialog();
    }

    /**
     * 头像上传失败接口回调
     */
    @Override
    public void getavatar_fail(String msg) {

    }

}
