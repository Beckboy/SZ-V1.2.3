package com.junhsue.ksee.net.api;

import com.junhsue.ksee.net.callback.FileRequestCallBack;

/**
 * 文件下载定义
 * Created by longer on 17/5/25.
 */

public interface IDownloadFile  {


    public <T> void downloadFile(String url, String fileDir, String fileName, FileRequestCallBack fileRequestCallBack);
}
