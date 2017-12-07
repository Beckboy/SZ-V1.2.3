package com.junhsue.ksee.utils;

import android.content.pm.PackageManager;

import com.junhsue.ksee.frame.MyApplication;

/**
 * Created by Sugar on 16/11/23.
 */
public class CommitUtil {
    private String appVersionName;

    /**
     * 获取VersionName
     */
    public String getAppVersionName() {
        if (StringUtils.isBlank(appVersionName)) {
            try {
                appVersionName = MyApplication.getApplication().getApplicationContext().getPackageManager().getPackageInfo(
                        MyApplication.getApplication().getApplicationContext().getPackageName(), 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                appVersionName = "";
                e.printStackTrace();
            }
        }
        if (StringUtils.isBlank(appVersionName)) {
            appVersionName = "unknown version name ";
        }
        return appVersionName;
    }
}
