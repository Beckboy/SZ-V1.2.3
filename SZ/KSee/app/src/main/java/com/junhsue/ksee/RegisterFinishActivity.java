package com.junhsue.ksee;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class RegisterFinishActivity extends BaseActivity {

    private TextView mTvName,mTvOrganization;
    private CircleImageView mImgPerson;

    private String name,phonenumber,organization,avatar;

    @Override
    protected void onReceiveArguments(Bundle bundle) {
        name = bundle.getString(Constants.REG_NICKNAME);
        phonenumber = bundle.getString(Constants.REG_PHONENUMBER);
        organization = bundle.getString("organization");
        avatar = bundle.getString(Constants.REG_AVATAR);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.act_register_finish;
    }

    @Override
    protected void onInitilizeView() {
        initView();
    }

    private void initView() {
        mTvName = (TextView) findViewById(R.id.tv_finish_name);
        mTvOrganization = (TextView) findViewById(R.id.tv_finish_company);
        mImgPerson = (CircleImageView) findViewById(R.id.img_finish_person);

//        ImageLoader.getInstance().displayImage(avatar,mImgPerson);
        Glide.with(this).load(avatar).error(R.drawable.icon_head_default_50px).into(mImgPerson);
        mTvName.setText(name);
        mTvOrganization.setText(organization);
    }

    public void onclick(View view) {
        switch (view.getId()){
            case R.id.btn_register_finish:
                Intent intent2Login = new Intent(this,LoginActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.REG_PHONENUMBER,phonenumber);
                intent2Login.putExtras(bundle);
                startActivity(intent2Login);
                break;
        }
    }
}
