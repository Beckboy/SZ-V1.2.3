package com.junhsue.ksee.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.file.FileUtil;
import com.junhsue.ksee.interfaces.DownLoadApkCallBack;
import com.junhsue.ksee.net.api.RequestVo;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;

/**
 * 版本更新
 */
public class DownloadAPK {
    public static final int DOWN_ERROR = 0;

    private static final int DOWNLAODSUCCESS = 10;
    private static final int DOWNLAODERROR = 11;
    private static final int DOWNLAODING = 12;
    private static final int DOWNLAODCANCEL = 13;
    private static final int DOWNLAODPAUSE = 12;


    private boolean downloadPause = false;
    private boolean downloadTaskCancel = false;

    private List<DownLoadApkCallBack> downLoadApkCallBacks = new ArrayList<>();

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (downLoadApkCallBacks.isEmpty()) {
                return;
            }

            switch (msg.what) {
                case DOWNLAODSUCCESS:
                    Object obj = msg.obj;
                    String path = (String) obj;
                    for (int i = 0; i < downLoadApkCallBacks.size(); i++) {
                        downLoadApkCallBacks.get(i).onLoadSuccess(path);
                    }
                    removeAllListener();
                    break;
                case DOWNLAODERROR:
                    for (int i = 0; i < downLoadApkCallBacks.size(); i++) {
                        downLoadApkCallBacks.get(i).onLoadFail();
                    }
                    removeAllListener();
                    break;
                case DOWNLAODING:
                    for (int i = 0; i < downLoadApkCallBacks.size(); i++) {
                        downLoadApkCallBacks.get(i).onLoading(msg.arg1);
                    }
                    break;
                case DOWNLAODCANCEL:
                    for (int i = 0; i < downLoadApkCallBacks.size(); i++) {
                        downLoadApkCallBacks.get(i).onLoadFail();
                    }
                    break;
            }
        }
    };


    private static DownloadAPK downloadAPK;

    private DownloadAPK() {
    }

    public static DownloadAPK getInstance() {
        if (downloadAPK == null) {
            downloadAPK = new DownloadAPK();
        }
        return downloadAPK;
    }

    public void registerDownloadListener(DownLoadApkCallBack callBack) {
        downLoadApkCallBacks.add(callBack);
    }

    public void unRegisterDownLoadListener(DownLoadApkCallBack callBack) {
        downLoadApkCallBacks.remove(callBack);
    }

    private void removeAllListener() {
        downLoadApkCallBacks.clear();
    }


    public void setDownloadTaskCancel() {
        downloadTaskCancel = false;
    }

    /**
     * 获取apk路径
     *
     * @param pakName
     * @return
     */
    public String getApkPath(String pakName) {
        return FileUtil.getBaseFilePath() + "/"
                + Constants.APP_UPDATE_FOLDER + "/" + pakName;
    }

    /**
     * 文件夹的安装包是否最新的安装包
     *
     * @param context
     * @param apkName
     * @return
     */
    public boolean readApkInfoIsLaster(Context context, String apkName) {
        String archiveFilePath = getApkPath(apkName);//安装包路径
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            String version = info.versionName;       //得到安装包的版本信息
            CommitUtil commitUtil = new CommitUtil();
            if (version.equals(commitUtil.getAppVersionName())) {//判断安装包和本当前app版本是否相符
                return true;
            }
        }
        return false;
    }

    /*
     * 从服务器中下载APK
     */
    public void downLoadApk(final Context context, final RequestVo requestVo, final String apkName) {
        new Thread() {
            @Override
            public void run() {
                if (isInstallNewest(apkName, context)) {

                    Log.i("success","是最新的isInstallNewest");

                    return;
                }
                if (readApkInfoIsLaster(context, apkName)) {

                    Log.i("success","是最新的readApkInfoIsLaster");

                    Message msg = new Message();
                    msg.obj = getApkPath(apkName);
                    msg.what = DOWNLAODSUCCESS;
                    handler.sendMessage(msg);
                    return;
                }
                getFileFromServer(requestVo, apkName);
            }
        }.start();
    }

    /**
     * 判断安装的应用是否最新版本
     *
     * @param apkName
     * @param context
     * @return
     */
    private boolean isInstallNewest(String apkName, Context context) {
        String archiveFilePath = getApkPath(apkName);//安装包路径
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            String packageName = appInfo.packageName;  //得到安装包名称
            int versionCode = info.versionCode;       //得到版本信息
            return isNotNeedInstall(pm, packageName, versionCode);
        }
        return false;
    }

    private JHOkHttpUtils JHOkHttpUtils;

    /**
     * 从服务器上下载文件
     *
     * @param requestVo
     * @param apkName
     */
    private void getFileFromServer(final RequestVo requestVo, final String apkName) {

        Log.i("success",requestVo.url);

        if (requestVo == null) {
            return;
        }
        if (JHOkHttpUtils == null){
            JHOkHttpUtils = new JHOkHttpUtils();
        } else {
            return;
        }
        JHOkHttpUtils.setHttpRequestCallBack(new JHOkHttpUtils.HttpRequestCallBack() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(Response response) {
                // 如果相等的话表示当前的sdcard挂载在手机上并且是可用的
                Log.d("===", "getFileFromServer path :" + requestVo.url);
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

                    try {

                        // 获取到文件的大小
                        long totalLength = response.body().contentLength();
                        InputStream is = response.body().byteStream();
                        //apk 的文件路径
                        File folder = new File(FileUtil.getBaseFilePath() + "/"
                                + Constants.APP_UPDATE_FOLDER);
                        if (!folder.exists()) {
                            folder.mkdirs();
                        }

                        File file = new File(folder, apkName);

                        FileOutputStream fos = new FileOutputStream(file);
                        BufferedInputStream bis = new BufferedInputStream(is);
                        byte[] buffer = new byte[1024];
                        int len;
                        int total = 0;
                        long time = System.currentTimeMillis();
                        while ((len = bis.read(buffer)) != -1 && !downloadTaskCancel) {
                            fos.write(buffer, 0, len);
                            total += len;
                            //每500毫秒通知更新一次ui
                            if (System.currentTimeMillis() - time >= 500) {
                                time = System.currentTimeMillis();
                                // 获取当前下载量
                                Message msg = new Message();
                                msg.arg1 = (int) ((float) total / totalLength * 100);
                                msg.what = DOWNLAODING;
                                handler.sendMessage(msg);
                            }

                        }
                        if (downloadTaskCancel) {
                            handler.sendEmptyMessage(DOWNLAODCANCEL);
                            return;
                        }
                        Message msg = new Message();
                        msg.obj = file.getAbsolutePath();
                        msg.what = DOWNLAODSUCCESS;
                        handler.sendMessage(msg);
                        fos.close();
                        bis.close();
                        is.close();
                        return;

                    } catch (Exception e) {
                        e.printStackTrace();
                        handler.sendEmptyMessage(DOWNLAODERROR);
                        return;
                    }
                } else {
                    handler.sendEmptyMessage(DOWNLAODERROR);
                    return;
                }
            }
        });

        JHOkHttpUtils.executeRequest(requestVo);

    }

    // 安装apk
    public void installApk(Context mContext, File file) {
        SharedPreferencesUtils.getInstance(mContext).putBoolean("isUpdateSelf", true);
        Intent intent = new Intent();
        // 执行动作
        intent.setAction(Intent.ACTION_VIEW);
        // 执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        mContext.startActivity(intent);
    }

    // 安装apk
    public void installApk(Context mContext, String apkPath) {
        if (apkPath == null) {
            Log.d("==", "apkpath");
            return;
        }
        try {
            SharedPreferencesUtils.getInstance(mContext).putBoolean("isUpdateSelf", true);
            Intent intent = new Intent();
            // 执行动作
            intent.setAction(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // 执行的数据类型
            Uri uri = Uri.fromFile(new File(apkPath));
            //intent.setDataAndType(Uri.parse(apkPath), "application/vnd.android.package-archive");
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("==", "==安装失败");
        }

    }

    /**
     * 判断该应用在手机中的是否需要安装
     *
     * @param pm          PackageManager
     * @param packageName 要判断应用的包名
     * @param versionCode 要判断应用的版本号
     */
    private boolean isNotNeedInstall(PackageManager pm, String packageName, int versionCode) {
        List<PackageInfo> pakageinfos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (PackageInfo pi : pakageinfos) {
            String pi_packageName = pi.packageName;
            int pi_versionCode = pi.versionCode;
            //如果这个包名在系统已经安装过的应用中存在
            if (packageName.endsWith(pi_packageName)) {
                if (versionCode == pi_versionCode) {
//                    Log.e("===test" , "已经安装，不用更新，可以卸载该应用");
                    return true;
                } else if (versionCode > pi_versionCode) {
//                    Log.e("====test" , "已经安装，有更新");
                    return false;
                }
            }
        }
//        Log.e("===test" , "未安装该应用，可以安装");
        return false;
    }




}
