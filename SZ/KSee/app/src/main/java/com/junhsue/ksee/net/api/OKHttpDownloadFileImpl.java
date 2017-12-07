package com.junhsue.ksee.net.api;

import com.junhsue.ksee.net.callback.FileRequestCallBack;
import com.junhsue.ksee.net.request.FileRequestCall;
import com.junhsue.ksee.net.request.GetFormRequest;

import java.util.HashMap;

/**
 * Created by longer on 17/5/25.
 */

public class OKHttpDownloadFileImpl  extends  BaseClientApi implements  IDownloadFile {


    private static OKHttpDownloadFileImpl mOkHttpDownloadFile;

    public static  OKHttpDownloadFileImpl getInstance(){
        if(null==mOkHttpDownloadFile){
            synchronized (OKHttpDownloadFileImpl.class){
                if(null==mOkHttpDownloadFile){
                    mOkHttpDownloadFile=new OKHttpDownloadFileImpl();
                }
            }
        }
        return mOkHttpDownloadFile;
    }
    @Override
    public <T> void downloadFile(String url, String fileDir, String fileName, FileRequestCallBack fileRequestCallBack) {

        HashMap<String,String> params=new HashMap<String,String>();
        GetFormRequest getFormRequest=new GetFormRequest(url,params);
        FileRequestCall fileRequestCall =new FileRequestCall(getFormRequest,fileRequestCallBack,fileDir,fileName);
        submitRequset(fileRequestCall);
    }
}
