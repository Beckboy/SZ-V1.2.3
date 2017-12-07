package com.junhsue.ksee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.junhsue.ksee.dto.UpdatePasswordDTO;
import com.junhsue.ksee.frame.ScreenManager;
import com.junhsue.ksee.net.api.OkHttpILoginImpl;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.utils.LoginUtils;
import com.junhsue.ksee.utils.SHA256Encrypt;
import com.junhsue.ksee.utils.ScreenWindowUtil;
import com.junhsue.ksee.utils.ToastUtil;


public class ResetPassword2Activity extends BaseActivity {

    private EditText mEditNewPassword,mEditAgainPassword;

    private String phonenumber,password,passwordNew,passwordAgain;

    @Override
    protected void onReceiveArguments(Bundle bundle) {
        phonenumber = bundle.getString("phonenumber");
    }

    @Override
    protected int setLayoutId() {
        return R.layout.act_reset_password2;
    }

    @Override
    protected void onInitilizeView() {
        initView();
    }

    private void initView() {
        mEditNewPassword = (EditText) findViewById(R.id.edit_resetpassword2_newpassword);
        mEditAgainPassword = (EditText) findViewById(R.id.edit_resetpassword2_inputagain);
    }


    public void onclick(View view) {
        switch (view.getId()){
            case R.id.tbBtn_resetpassword2_back:
                finish();
                break;
            case R.id.tv_resetpassword2_nextstep:
            case R.id.tb_resetpassword2_finish:
                resetPassword();
                break;

        }
    }

    private void resetPassword() {
        passwordNew = mEditNewPassword.getText().toString().trim();
        passwordAgain = mEditAgainPassword.getText().toString().trim();
        if (!passwordNew.equals(passwordAgain)){  //密码不相同
            ToastUtil.getInstance(this).setContent("密码不匹配").setShow();
            return;
        }
        // 匹配密码的格式是否符合要求
        if (LoginUtils.setPasswordPass(this,passwordNew)){
            if (LoginUtils.setPasswordPass(this,passwordAgain)){
                password = SHA256Encrypt.bin2hex(passwordNew);
                OkHttpILoginImpl.getInstance().loginForgetPassword(phonenumber, password, new RequestCallback<UpdatePasswordDTO>() {
                    @Override
                    public void onError(int errorCode, String errorMsg) {
                        ToastUtil.getInstance(ResetPassword2Activity.this).setContent(errorMsg).setShow();
                    }

                    @Override
                    public void onSuccess(UpdatePasswordDTO response) {
                        ToastUtil.getInstance(ResetPassword2Activity.this).setContent("密码重置完成").setShow();
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString("phonenumber",phonenumber);
                        intent.putExtras(bundle);
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                });
            }
        }
    }
}
