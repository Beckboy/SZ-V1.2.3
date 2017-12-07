package com.junhsue.ksee.interfaces;

/**
 * Created by Sugar on 16/11/29.
 */

public interface DownLoadApkCallBack {
    public void onLoadSuccess(String filePath);

    public void onLoading(float value);

    public void onLoadFail();
}
