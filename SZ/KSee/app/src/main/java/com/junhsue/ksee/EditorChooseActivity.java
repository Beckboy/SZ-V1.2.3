package com.junhsue.ksee;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.dto.SettingNickNameDTO;
import com.junhsue.ksee.net.api.OkHttpILoginImpl;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.error.NetResultCode;
import com.junhsue.ksee.profile.UserProfileService;
import com.junhsue.ksee.utils.CommonUtils;
import com.junhsue.ksee.utils.InputUtil;
import com.junhsue.ksee.utils.PopWindowTokenErrorUtils;
import com.junhsue.ksee.utils.ToastUtil;
import com.junhsue.ksee.view.ActionBar;

/**
 * 设置编辑页面 ,修改姓名,头像、机构
 * created by Hunter on xx in Junhsue .
 */
public class EditorChooseActivity extends BaseActivity implements View.OnClickListener {

    private ActionBar mAbar;
    private TextView mAbBarTitle;
    private EditText mEditContent;

    private Activity mContext;

    private String token, title, content;
    private int type;

    @Override
    protected void onReceiveArguments(Bundle bundle) {
        token = bundle.getString("token");
        title = bundle.getString("title");
        content = bundle.getString("content");
        type = bundle.getInt("type", 0);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.act_editor_choose;
    }

    @Override
    protected void onInitilizeView() {
        initView();
    }

    private void initView() {
        mContext = this;
        mAbar = (ActionBar) findViewById(R.id.ab_my_editorData_choose);
        mAbar.setOnClickListener(this);
        mAbBarTitle = (TextView) findViewById(R.id.tv_title);
        mAbBarTitle.setText(title);
        mEditContent = (EditText) findViewById(R.id.edit_my_editorData);
        mEditContent.setText(content);
        mEditContent.setSelection(content.length());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_left_layout:
                finish();
                break;
            case R.id.tv_btn_right:
                if (!CommonUtils.getIntnetConnect(this)){
                    ToastUtil.getInstance(this).setContent(Constants.INTNET_NOT_CONNCATION).setDuration(Toast.LENGTH_SHORT).setShow();
                    return;
                }
                submit();
                break;
        }
    }

    private void submit() {
        final String value = mEditContent.getText().toString().trim();
        if (value.equals("")){
            ToastUtil.getInstance(this).setContent(title+" 不能为空").setShow();
            return;
        }
        Trace.i("修改的Token：" + token);
        if (InputUtil.noContainsEmoji(value)){
            ToastUtil.getInstance(this).setContent(title+" 中不能含有表情").setShow();
            return;
        }
        switch (type) {
            case 1:
                if (value.length() > 10){
                    ToastUtil.getInstance(this).setContent(title+" 长度不能超过10位字符 ").setShow();
                    return;
                }
                OkHttpILoginImpl.getInstance().settingNickName(token, value, new RequestCallback<SettingNickNameDTO>() {
                    @Override
                    public void onError(int errorCode, String errorMsg) {
                        Trace.i("Change NickName errorMsg:" + errorMsg);
                        switch (errorCode){
                            case NetResultCode.CODE_LOGIN_STATE_ERROR: //登录态错误，重新登录
                                PopWindowTokenErrorUtils.getInstance(mContext).showPopupWindow(R.layout.act_editor_choose);
                                break;
                            default:
                                ToastUtil.getInstance(mContext).setContent(errorMsg).setShow();
                                break;
                        }
                    }

                    @Override
                    public void onSuccess(SettingNickNameDTO response) {
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString("value", value);
                        intent.putExtras(bundle);
                        setResult(RESULT_OK, intent);
                        finish();
                        Trace.i("姓名请求成功：" + response.toString());
                    }
                });
                break;
            case 2:
                if (value.length() > 30){
                    ToastUtil.getInstance(this).setContent(title+" 长度不能超过30位字符 ").setShow();
                    return;
                }
                OkHttpILoginImpl.getInstance().settingOrganization(token, value, new RequestCallback<SettingNickNameDTO>() {
                    @Override
                    public void onError(int errorCode, String errorMsg) {
                        Trace.i("Change NickName errorMsg:" + errorMsg);
                        switch (errorCode){
                            case NetResultCode.CODE_LOGIN_STATE_ERROR: //登录态错误，重新登录
                                PopWindowTokenErrorUtils.getInstance(mContext).showPopupWindow(R.layout.act_editor_choose);
                                break;
                            default:
                                ToastUtil.getInstance(mContext).setContent(errorMsg).setShow();
                                break;
                        }
                    }

                    @Override
                    public void onSuccess(SettingNickNameDTO response) {
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString("value", value);
                        intent.putExtras(bundle);
                        setResult(RESULT_OK, intent);
                        finish();
                        Trace.i("组织请求成功：" + response.toString());
                    }
                });
                break;
            case 0:
                ToastUtil.getInstance(this).setContent("用户信息有误").setShow();
                break;
        }
    }

    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.imgBtn_my_editorData_clean:
                mEditContent.setText("");
                break;
        }
    }

}
