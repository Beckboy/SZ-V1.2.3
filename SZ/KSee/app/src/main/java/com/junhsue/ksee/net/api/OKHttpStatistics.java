package com.junhsue.ksee.net.api;

import com.junhsue.ksee.entity.ResultEntity;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.request.PostFormRequest;
import com.junhsue.ksee.net.request.RequestCall;
import com.junhsue.ksee.net.url.RequestUrl;
import com.junhsue.ksee.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 统计
 * Created by Sugar on 17/8/25.
 */

public class OKHttpStatistics extends BaseClientApi implements IStatistics {

    private static OKHttpStatistics mInstance;

    public static OKHttpStatistics getInstance() {

        if (null == mInstance) {
            synchronized (OKHttpStatistics.class) {
                if (null == mInstance) {
                    mInstance = new OKHttpStatistics();
                }
            }
        }
        return mInstance;

    }


    @Override
    public <T> void uploadStatisticsFile(String path, String piling, RequestCallback<T> requestCallback) {
        Map<String, String> params = new HashMap<String, String>();

        if (StringUtils.isNotBlank(path)) {
            params.put("path", path);
        }
        if (StringUtils.isNotBlank(piling)) {
            params.put("piling", piling);
        }
        String url = RequestUrl.DEFINED_STATICS_UPLOAD_URL;
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<ResultEntity> requestCall = new RequestCall<ResultEntity>(postFormRequest, (RequestCallback<ResultEntity>) requestCallback, ResultEntity.class);
        submitRequset(requestCall);
    }

    /**
     * @param platform_id     平台
     * @param market_id       市场编号
     * @param uuid            设备唯一识别号
     * @param device_info     设备信息
     * @param os_info         设备系统版本号
     * @param app_version     APP当前版本号
     * @param requestCallback
     * @param <T>
     */
    @Override
    public <T> void uploadFirstOpenAppAction(String platform_id, String market_id, String uuid, String device_info, String os_info, String app_version, RequestCallback<T> requestCallback) {

        Map<String, String> params = new HashMap<String, String>();

        params.put("platform_id", platform_id);
        params.put("market_id", market_id);
        params.put("uuid", uuid);
        params.put("device_info", device_info);
        params.put("os_info", os_info);
        params.put("app_version", app_version);

        String url = RequestUrl.FIRST_OPEN_RECORDE_URL;
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<ResultEntity> requestCall = new RequestCall<ResultEntity>(postFormRequest, (RequestCallback<ResultEntity>) requestCallback, ResultEntity.class);
        submitRequset(requestCall);
    }

    @Override
    public <T> void uploadStatisticsAppStart(String platform_id, String market_id, String uuid, String device_info, String os_info, String app_version, String restart, RequestCallback<T> requestCallback) {
        Map<String, String> params = new HashMap<String, String>();

        params.put("platform_id", platform_id);
        params.put("market_id", market_id);
        params.put("uuid", uuid);
        params.put("device_info", device_info);
        params.put("os_info", os_info);
        params.put("app_version", app_version);
        params.put("restart", restart);

        String url = RequestUrl.STATISTICS_START_URL;
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<ResultEntity> requestCall = new RequestCall<ResultEntity>(postFormRequest, (RequestCallback<ResultEntity>) requestCallback, ResultEntity.class);
        submitRequset(requestCall);
    }

    @Override
    public <T> void uploadStatisticsQuestionShare(String question_id, RequestCallback<T> requestCallback) {
        Map<String, String> params = new HashMap<String, String>();

        params.put("question_id", question_id);

        String url = RequestUrl.STATISTICS_QUESTION_SHARE_COUNT_URL;
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<ResultEntity> requestCall = new RequestCall<ResultEntity>(postFormRequest, (RequestCallback<ResultEntity>) requestCallback, ResultEntity.class);
        submitRequset(requestCall);
    }

    @Override
    public <T> void uploadCalendarShareCount(String calendar_id, RequestCallback<T> requestCallback) {
        Map<String, String> params = new HashMap<String, String>();

        params.put("calendar_id", calendar_id);

        String url = RequestUrl.STATISTICS_CALENDAR_SHARE_COUNT_URL;
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<ResultEntity> requestCall = new RequestCall<ResultEntity>(postFormRequest, (RequestCallback<ResultEntity>) requestCallback, ResultEntity.class);
        submitRequset(requestCall);
    }

    @Override
    public <T> void uploadStatisticsInviteShareCount(String invite_content_id, String invite_type_id, RequestCallback<T> requestCallback) {

        Map<String, String> params = new HashMap<String, String>();

        params.put("invite_content_id", invite_content_id);

        params.put("invite_type_id", invite_type_id);

        String url = RequestUrl.STATISTICS_INVITE_SHARE;
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<ResultEntity> requestCall = new RequestCall<ResultEntity>(postFormRequest, (RequestCallback<ResultEntity>) requestCallback, ResultEntity.class);
        submitRequset(requestCall);
    }


}
