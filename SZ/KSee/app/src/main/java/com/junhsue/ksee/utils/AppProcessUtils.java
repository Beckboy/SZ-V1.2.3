package com.junhsue.ksee.utils;

import android.app.ActivityManager;
import android.content.Context;

import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.frame.MyApplication;

import java.util.List;

/**
 * 检查app
 * Created by Sugar on 17/9/4.
 */

public class AppProcessUtils {

    /***
     * 判断app是不是在前台运行
     *
     * @return
     */
    public static boolean isAppForeground() {
        boolean isForeground = false;

        List<ActivityManager.RunningAppProcessInfo> appProcesses = getAllRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        String pkgName = CommonUtils.getInstance().getAppPackageName();
        try {
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                // the name of the process that this object is associated with.
                if (appProcess.processName.equals(pkgName)
                        && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    isForeground = true;
                    break;
                }
            }
        } catch (Exception e) {

        }
        Trace.d("App is in foreground = " + isForeground);
        return isForeground;
    }


    private static List<ActivityManager.RunningAppProcessInfo> getAllRunningAppProcesses() {
        // return list of app processes that are running on the device
        ActivityManager am = (ActivityManager) MyApplication.getApplication().getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        if (am == null) {
            return null;
        }
        List<ActivityManager.RunningAppProcessInfo> appProcesses = am.getRunningAppProcesses();
        return appProcesses;
    }
}
