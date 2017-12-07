package com.junhsue.ksee;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.dto.VersionUpdateDTO;
import com.junhsue.ksee.interfaces.DownLoadApkCallBack;
import com.junhsue.ksee.net.api.RequestType;
import com.junhsue.ksee.net.api.RequestVo;
import com.junhsue.ksee.utils.DateUtils;
import com.junhsue.ksee.utils.DownloadAPK;
import com.junhsue.ksee.utils.SharedPreferencesUtils;
import com.junhsue.ksee.utils.ToastUtil;

public class DownLoadAPKActivity extends BaseActivity implements View.OnClickListener, DownLoadApkCallBack{

    private Context mContext;

    private ProgressBar mProgressBar;
    private TextView mTvProHint;
    private Button mBtnYes;

    private VersionUpdateDTO versionEntity;
    private RequestVo requestVo;
    private boolean downCur = false;

    @Override
    protected void onReceiveArguments(Bundle bundle) {
        versionEntity = (VersionUpdateDTO) bundle.getSerializable("version_info");
        downCur = bundle.getBoolean("down_update", false);
        requestVo = new RequestVo();
        requestVo.url = versionEntity.address;
        requestVo.requestType = RequestType.GET;
    }

    @Override
    protected int setLayoutId() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        return R.layout.dialog_version_less_pop;
    }

    @Override
    protected void onInitilizeView() {

        initView();

        DownloadAPK.getInstance().registerDownloadListener(this);

    }

    private void initView() {
        mContext = this;
        mProgressBar = (ProgressBar) findViewById(R.id.pb_version);
        mTvProHint = (TextView) findViewById(R.id.tv_version_hint);
        findViewById(R.id.imgBtn_no).setOnClickListener(this);
        mBtnYes = (Button) findViewById(R.id.imgBtn_yes);
        mBtnYes.setOnClickListener(this);
        if (downCur && mBtnYes.isClickable()){
            mBtnYes.callOnClick();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgBtn_no:
                finish();
                int num = SharedPreferencesUtils.getInstance(mContext).getInt(Constants.APP_UPDATE_REFUSE_NUM,0);
                String time = SharedPreferencesUtils.getInstance(mContext).getString(Constants.APP_UPDATE_REFUSE_TIME,"");
                if (!time.equals(DateUtils.getCurrentDate())) {
                    SharedPreferencesUtils.getInstance(mContext).putInt(Constants.APP_UPDATE_REFUSE_NUM, num + 1);
                    SharedPreferencesUtils.getInstance(mContext).putString(Constants.APP_UPDATE_REFUSE_TIME, DateUtils.getCurrentDate());
                }
                break;
            case R.id.imgBtn_yes:
                SharedPreferencesUtils.getInstance(mContext).putInt(Constants.APP_UPDATE_REFUSE_NUM,0);
                //判断是否已经下载了
                if (DownloadAPK.getInstance().readApkInfoIsLaster(mContext, Constants.APP_UPDATE_NAME)) {
                    ToastUtil.getInstance(mContext).setContent("下载成功").setShow();
                    DownloadAPK.getInstance().installApk(mContext, DownloadAPK.getInstance().getApkPath(Constants.APP_UPDATE_NAME));
                    finish();
                    return;
                }
                downLoadApk();
                break;
        }
    }


    private void downLoadApk() {

        mBtnYes.setClickable(false);

        DownloadAPK.getInstance().downLoadApk(mContext,
                requestVo, Constants.APP_UPDATE_NAME);
    }


    @Override
    public void onLoadSuccess(String filePath) {
        mBtnYes.setClickable(true);
        ToastUtil.getInstance(mContext).setContent("下载完成").setShow();
        //安装
        DownloadAPK.getInstance().installApk(mContext, DownloadAPK.getInstance().getApkPath(Constants.APP_UPDATE_NAME));
        finish();
    }

    @Override
    public void onLoading(float value) {
        mProgressBar.setProgress((int) value);
        mBtnYes.setClickable(false);
        mTvProHint.setText("现已下载："+(int)value + "%\n请您稍等片刻");
    }


    @Override
    public void onLoadFail() {
        mBtnYes.setClickable(true);
        ToastUtil.getInstance(mContext).setContent("下载失败").setShow();
        finish();
    }
}
