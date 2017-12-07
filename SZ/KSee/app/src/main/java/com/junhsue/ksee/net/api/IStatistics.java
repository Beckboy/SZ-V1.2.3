package com.junhsue.ksee.net.api;

import com.junhsue.ksee.net.callback.RequestCallback;

/**
 * 统计相关接口
 * Created by Sugar on 17/8/25.
 */

public interface IStatistics {

    public <T> void uploadStatisticsFile(String path, String piling, RequestCallback<T> requestCallback);

    public <T> void uploadFirstOpenAppAction(String platform_id, String market_id, String uuid, String device_info, String os_info, String app_version, RequestCallback<T> requestCallback);

    public <T> void uploadStatisticsAppStart(String platform_id, String market_id, String uuid, String device_info, String os_info, String app_version, String start, RequestCallback<T> requestCallback);

    public <T> void uploadStatisticsQuestionShare(String question_id, RequestCallback<T> requestCallback);

    public <T> void uploadCalendarShareCount(String calendar_id, RequestCallback<T> requestCallback);

    public <T> void uploadStatisticsInviteShareCount(String invite_content_id, String invite_type_id, RequestCallback<T> requestCallback);

}
