package com.junhsue.ksee.net.api;

import android.content.Context;
import android.util.Log;

import com.junhsue.ksee.BaseActivity;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.net.callback.SaveImgSuccessCallback;
import com.junhsue.ksee.net.callback.VoiceCallBack;
import com.junhsue.ksee.net.url.RequestUrl;
import com.junhsue.ksee.utils.ToastUtil;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONObject;

/**
 * Created by hunter_J on 17/3/29.
 */
public class OkHttpQiniu {

    private static OkHttpQiniu mInstance;
    private static UploadManager uploadManager;

    private OkHttpQiniu() {
        getUploadManager();
    }

    public static OkHttpQiniu getInstance() {
//        if (uploadManager == null) {
//            synchronized (OkHttpQiniu.class) {
//                if (uploadManager == null) {
                    mInstance = new OkHttpQiniu();
                    Trace.i("Qiniu getInstance");
//                }
//            }
//        }
        return mInstance;
    }

    private void getUploadManager() {
        Configuration configuration = new Configuration.Builder()
                .chunkSize(256 * 1024) //分片上传时，每片的大小。默认256K
                .putThreshhold(512 * 1024) //启用分片上传阀值。默认512K
                .connectTimeout(10) //链接超时。默认10秒
                .responseTimeout(60) //服务器响应超时。默认60秒
//                .zone(Zone.httpAutoZone) //http上传，自动识别上传区域
                .build();
        uploadManager = new UploadManager(configuration);
    }

    /**
     * 图片上传
     * @param context
     * @param saveImgSuccessCallback
     * @param data
     * @param key
     * @param token
     */
    public void loadImg(final Context context, final SaveImgSuccessCallback saveImgSuccessCallback, byte[] data, String key, final String token) {
        uploadManager.put(data, key, token, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                if (info.isOK()) { // 上传成功
                    saveImgSuccessCallback.getavatar(RequestUrl.BASE_QINIU_API + key);
                } else {
                    saveImgSuccessCallback.getavatar_fail(RequestUrl.BASE_QINIU_API + key);
                    BaseActivity baseActivity = (BaseActivity) context;
                    baseActivity.dismissLoadingDialog();
                    ToastUtil.getInstance(context).setContent("图像上传失败").setShow();
                }
            }
        }, new UploadOptions(null, null, false, new UpProgressHandler() {
            @Override
            public void progress(String key, double percent) {
                Log.i("qiniu", key + ": " + percent);
            }
        },null));
    }

    /**
     * 上传语音
     *
     * @param context
     * @param data
     * @param key
     * @param token
     */

    public void uploadVoice(final Context context, final VoiceCallBack callBack, byte[] data, String key, final String token) {
        uploadManager.put(data, key, token, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                if (info.isOK()) { //上传成功
                    ToastUtil.getInstance(context).setContent("语音上传成功").setShow();
                    callBack.uploadSuccess(key);
                } else {
                    ToastUtil.getInstance(context).setContent("语音上传失败").setShow();
                    callBack.uploadFailed("Qiniu Upload Failed");
                }
            }
        }, null);
    }


}
