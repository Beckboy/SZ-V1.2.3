package com.junhsue.ksee.utils;

import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.entity.ResultEntity;
import com.junhsue.ksee.net.api.OKHttpStatistics;
import com.junhsue.ksee.net.callback.RequestCallback;

/**
 * Created by Sugar on 17/9/4.
 */

public class CommonAskUtils {
    /**
     * 统计启动次数
     */
    public static void statisticsAppStart(String channelPlantForm, String appStartType) {

        String uuid = CommonUtils.getInstance().getDeviceUUID();
        String device_info = CommonUtils.getInstance().getDeviceInfo();
        String os_info = CommonUtils.getInstance().getDeviceVersion();
        String app_version = CommonUtils.getInstance().getAppVersionName();

        OKHttpStatistics.getInstance().uploadStatisticsAppStart(DefinedStatisticsManager.PLATFORM_ID, channelPlantForm, uuid, device_info, os_info, app_version, appStartType, new RequestCallback<ResultEntity>() {

            @Override
            public void onError(int errorCode, String errorMsg) {
                Trace.d("==statistics count" + errorCode);
            }

            @Override
            public void onSuccess(ResultEntity response) {

            }
        });

    }
}
