package com.junhsue.ksee.net.callback;

import java.io.File;

/**
 * Created by longer on 17/5/25.
 */

public interface  FileRequestCallBack{

    void onError(int errorCode, String errorMsg);

    void onSuccess(File file);

    /** 进度条*/
    void inProgress(float progress);
}
