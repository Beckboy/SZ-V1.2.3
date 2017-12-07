package com.junhsue.ksee.net.request;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.junhsue.ksee.net.OKHttpUtils;
import com.junhsue.ksee.net.callback.FileRequestCallBack;
import com.junhsue.ksee.net.error.NetErrorParser;
import com.junhsue.ksee.net.error.NetResultCode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by longer on 17/5/25.
 */

public class FileRequestCall<T> extends  BaseRequestCall<T> implements Callback {


    private static String TAG = "RequestCall";
    //文件进度条
    private final  static  int  FILE_PROGRESS_CODE=900;
    //请求返回编码
    private static String PARAMS_RESPONSE_CODE = "params_response_code";
    //请求返回信息
    private static String PARAMS_RESPONSE_BODY = "params_response_body";
    //文件进度条
    private static String PARAMS_RESPONSE_FILE_PROGRESS="params_response_progress";

    private OKHttpRequest mOkHttpRequest;

    //目录路径
    private String fileDir;
    //文件名
    private String fileName;

    private FileRequestCallBack  fileRequestCallBack;

    public FileRequestCall(OKHttpRequest okHttpRequest,FileRequestCallBack fileRequestCallBack,
                           String fileDir, String fileName){
        this.mOkHttpRequest=okHttpRequest;
        this.fileRequestCallBack=fileRequestCallBack;
        this.fileDir=fileDir;
        this.fileName=fileName;

    }

    @Override
    protected Call buildCall() {
        Request request = mOkHttpRequest.generateRequest();
        return OKHttpUtils.getInstance().getOkHttpClient().newCall(request);
    }

    @Override
    public void requestExcute() {
        buildCall().enqueue(this);
    }

    @Override
    public void onFailure(Call call, IOException e) {
        Log.d(TAG, "onResponse is time out");
        String errorMsg = NetErrorParser.parserCode(NetResultCode.CODE_ERROR_HTTP);
        sendResponseUI(NetResultCode.CODE_ERROR_HTTP,0f, null);

    }

    private void sendResponseUI(final  int responseCode, final float progress, final  File file) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Message message = Message.obtain();
                Bundle bundle = message.getData();
                bundle.putInt(PARAMS_RESPONSE_CODE, responseCode);
                bundle.putSerializable(PARAMS_RESPONSE_BODY, file);
                bundle.putFloat(PARAMS_RESPONSE_FILE_PROGRESS,progress);
                handler.handleMessage(message);
            }
        });

    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {

        saveFile(response);
    }




    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            if (null == bundle) return;
            int responseCode = bundle.getInt(PARAMS_RESPONSE_CODE);
            float fileProgress=bundle.getFloat(PARAMS_RESPONSE_FILE_PROGRESS);

            //更新文件进度条
            if(FILE_PROGRESS_CODE==responseCode) {
                if (null != fileRequestCallBack) {
                    fileRequestCallBack.inProgress(fileProgress);
                }

            }if (NetResultCode.CODE_SUCCESS == responseCode || NetResultCode.CODE_SUCCESS_HTTP == responseCode) {
                File response =(File) bundle.getSerializable(PARAMS_RESPONSE_BODY);
                if (null != fileRequestCallBack) {
                    fileRequestCallBack.onSuccess(response);
                }
            } else {
                if (null != fileRequestCallBack) {
                    String errorMsg = NetErrorParser.parserCode(responseCode);
                    fileRequestCallBack.onError(responseCode, errorMsg);
                }
            }

        }
    };


    public File saveFile(Response response) throws IOException
    {
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len = 0;
        FileOutputStream fos = null;
        try
        {
            is = response.body().byteStream();
            final long total = response.body().contentLength();

            long sum = 0;

            File dir = new File(fileDir);
            if (!dir.exists())
            {
                dir.mkdirs();
            }
            File file = new File(dir, fileName);
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1)
            {
                sum += len;
                fos.write(buf, 0, len);
                final long finalSum = sum;

                // 更新进度条
                sendResponseUI(FILE_PROGRESS_CODE,finalSum * 1.0f / total,file);
            }
            fos.flush();
            //写入成功后
            sendResponseUI(NetResultCode.CODE_SUCCESS_HTTP,100,file);
            return file;

        } finally
        {
            try
            {
                response.body().close();
                if (is != null) is.close();
            } catch (IOException e)
            {
            }
            try
            {
                if (fos != null) fos.close();
            } catch (IOException e)
            {
            }

        }
    }
}