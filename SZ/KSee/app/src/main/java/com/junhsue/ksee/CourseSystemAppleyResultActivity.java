package com.junhsue.ksee;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.junhsue.ksee.file.ImageLoaderOptions;
import com.junhsue.ksee.utils.ScreenWindowUtil;
import com.junhsue.ksee.utils.StatisticsUtil;
import com.junhsue.ksee.view.ActionBar;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 系统课报名结果页
 * Created by longer on 17/4/13.
 */

public class CourseSystemAppleyResultActivity extends BaseActivity implements View.OnClickListener {

    public final static String PARAMS_IMG = "params_img";

    private ImageView mImg;

    private String mImgUrl;

    private ActionBar mActionBar;

    @Override
    protected void onReceiveArguments(Bundle bundle) {
        mImgUrl = bundle.getString(PARAMS_IMG, "");
    }


    @Override
    protected int setLayoutId() {
        return R.layout.act_course_system_appley_result;
    }

    @Override
    protected void onResume() {
        StatisticsUtil.getInstance(this).onCountPage("1.4.5.1.1");
        super.onResume();
    }

    @Override
    protected void onInitilizeView() {
        mImg = (ImageView) findViewById(R.id.img);
        mActionBar = (ActionBar) findViewById(R.id.action_bar);
        //
        mActionBar.setOnClickListener(this);
        //
        setImage();
    }


    private void setImage() {
        ViewGroup.LayoutParams params = mImg.getLayoutParams();
        params.height = ScreenWindowUtil.getScreenWidth(this) * 9 / 16;
        mImg.setLayoutParams(params);
        //
        ImageLoader.getInstance().displayImage(mImgUrl, mImg,
                ImageLoaderOptions.option(R.drawable.img_default_course_system));


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_left_layout:
                finish();
                break;
        }
    }
}
