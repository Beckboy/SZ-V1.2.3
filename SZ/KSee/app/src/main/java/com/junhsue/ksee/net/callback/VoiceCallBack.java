package com.junhsue.ksee.net.callback;

/**
 * Created by Sugar on 17/5/23.
 */

public interface VoiceCallBack {
    void uploadSuccess(String key);

    void uploadFailed(String info);
}
