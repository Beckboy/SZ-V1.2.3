package com.junhsue.ksee.net.api;

import com.alipay.android.phone.mrpc.core.Request;
import com.junhsue.ksee.dto.CircleListDTO;
import com.junhsue.ksee.dto.HomeBannerDTO;
import com.junhsue.ksee.dto.HomeContentDTO;
import com.junhsue.ksee.dto.HomeManagersListDTO;
import com.junhsue.ksee.dto.HomePostsHotDTO;
import com.junhsue.ksee.dto.MsgCountEntityDTO;
import com.junhsue.ksee.dto.RealizeArticleDTO;
import com.junhsue.ksee.dto.SolutionListDTO;
import com.junhsue.ksee.entity.CommonResultEntity;
import com.junhsue.ksee.entity.MsgCountEntity;
import com.junhsue.ksee.entity.SolutionCouponDTO;
import com.junhsue.ksee.entity.SolutionDetails;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.request.PostFormRequest;
import com.junhsue.ksee.net.request.RequestCall;
import com.junhsue.ksee.net.url.RequestUrl;

import java.util.HashMap;

/**
 *首页
 * Created by longer on 17/8/11.
 */

public class OKHttpHomeImpl extends BaseClientApi implements IHome {


    private static OKHttpHomeImpl mOkHttpHomeIml;

    public static OKHttpHomeImpl getInstance() {
        if (null == mOkHttpHomeIml) {
            synchronized (OKHttpHomeImpl.class) {
                mOkHttpHomeIml = new OKHttpHomeImpl();
            }
        }
        return mOkHttpHomeIml;
    }

    @Override
    public <T> void getHomeContent(RequestCallback<T> requestCallback) {
        HashMap<String, String> params = new HashMap<String, String>();
        //
        String url = RequestUrl.REALIZE_HOME_CONTENT;
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<HomeContentDTO> requestCall = new RequestCall<HomeContentDTO>(postFormRequest,
                (RequestCallback<HomeContentDTO>) requestCallback, HomeContentDTO.class);
        submitRequset(requestCall);
    }

    /**
     * 获取首页轮播
     * @param requestCallback
     * @param <T>
     */
    @Override
    public <T> void getHomeBanner(String version, RequestCallback<T> requestCallback) {
        HashMap<String, String> params = new HashMap<String, String>();
        //
        String url = RequestUrl.HOME_BANNER;
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<HomeBannerDTO> requestCall = new RequestCall<HomeBannerDTO>(postFormRequest,
                (RequestCallback<HomeBannerDTO>) requestCallback, HomeBannerDTO.class);
        submitRequset(requestCall);
    }

    /**
     * 获取首页轮播
     * @param requestCallback
     * @param <T>
     */
    @Override
    public <T> void getHomeManagerTags(RequestCallback<T> requestCallback) {
        HashMap<String, String> params = new HashMap<String, String>();
        //
        String url = RequestUrl.HOME_MANAGER_TAGS;
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<HomeManagersListDTO> requestCall = new RequestCall<HomeManagersListDTO>(postFormRequest,
                (RequestCallback<HomeManagersListDTO>) requestCallback, HomeManagersListDTO.class);
        submitRequset(requestCall);
    }

    /**
     * 获取文章列表
     * @param page
     * @param pagesize
     * @param tag_id 筛选文章的id标签
     * @param requestCallback
     * @param <T>
     */
    @Override
    public <T> void getArticleList(String page, String pagesize, String tag_id, RequestCallback<T> requestCallback) {
        HashMap<String,String> params=new HashMap<String,String>();
        params.put("page",page);
        params.put("pagesize",pagesize);
        params.put("tag_id",tag_id);
        //
        String url = RequestUrl.HOME_MANAGER_TAG_ARTICLE_LIST;
        PostFormRequest postFormRequest = new PostFormRequest(url,params);
        RequestCall<RealizeArticleDTO> requestCall = new RequestCall<RealizeArticleDTO>(postFormRequest, (RequestCallback<RealizeArticleDTO>) requestCallback,RealizeArticleDTO.class);
        submitRequset(requestCall);
    }

    @Override
    public <T> void getSolutiondDetails(String id, RequestCallback<T> requestCallback) {
        HashMap<String,String> params=new HashMap<>();
        params.put("id",id);
        //
        String url=RequestUrl.SOLUTION_DETAILS;
        PostFormRequest postFormRequest=new PostFormRequest(url,params);
        RequestCall<SolutionDetails> requestCall=new RequestCall<SolutionDetails>(postFormRequest
                ,(RequestCallback<SolutionDetails>)requestCallback,SolutionDetails.class);
        submitRequset(requestCall);
    }

    @Override
    public <T> void solutionConvert(String coupon_id, String solution_id, String email, String link
            , String title, String nickname, RequestCallback<T> requestCallback) {
        HashMap<String,String> params=new HashMap<>();
        params.put("coupon_id",coupon_id);
        params.put("solution_id",solution_id);
        params.put("email",email);
        params.put("link",link);
        params.put("title",title);
        params.put("nickname",nickname);
        //
        String url=RequestUrl.SOLUTION_CONVERT;
        PostFormRequest postFormRequest=new PostFormRequest(url,params);
        RequestCall<CommonResultEntity> requestCall=new RequestCall<>(postFormRequest,(RequestCallback<CommonResultEntity>)requestCallback,
                CommonResultEntity.class);
        submitRequset(requestCall);
    }


    @Override
    public <T> void getSolutionCouponList(RequestCallback<T> requestCallback) {
        HashMap<String,String> params=new HashMap<>();
        //
        String url=RequestUrl.SOLUTION_COUPON;
        PostFormRequest postFormRequest=new PostFormRequest(url,params);
        RequestCall<SolutionCouponDTO> requestCall=new RequestCall<>(postFormRequest,(RequestCallback<SolutionCouponDTO>)requestCallback,
                SolutionCouponDTO.class);
        submitRequset(requestCall);
    }

    @Override
    public <T> void sendSolutionEmail(String link, String email, String title, String nickname, RequestCallback<T> requestCallback) {
        HashMap<String,String> params=new  HashMap<String,String>();
        params.put("link",link);
        params.put("email",email);
        params.put("title",title);
        params.put("nickname",nickname);
        //
        String url=RequestUrl.SOLUTION_EMAIL_SEND;
        PostFormRequest postFormRequest=new PostFormRequest(url,params);
        RequestCall<Object> requestCall=new RequestCall<>(postFormRequest,(RequestCallback<Object>)requestCallback,
                Object.class);
        submitRequset(requestCall);

    }

    @Override
    public <T> void getSolutionList(RequestCallback<T> requestCallback) {
        HashMap<String,String> params=new HashMap<>();
        //
        String url=RequestUrl.SOLUTION_LIST;
        PostFormRequest postFormRequest=new PostFormRequest(url,params);
        RequestCall<SolutionListDTO> requestCall=new RequestCall<>(postFormRequest,(RequestCallback<SolutionListDTO>)requestCallback,
                SolutionListDTO.class);
        requestCall.setParser(false);
        submitRequset(requestCall);
    }

    /**
     *
     * @param size  菜单显示的数量
     * @param requestCallback
     * @param <T>
     */
    @Override
    public <T> void getMenuTab(String size, RequestCallback<T> requestCallback) {
        HashMap<String,String> params=new HashMap<>();
        //值为1，指的是没有层级显示
        params.put("layer","1");
        params.put("page","0");
        params.put("pagesize",size);
        //
        String url=RequestUrl.HOME_MENU_TAB;
        PostFormRequest postFormRequest=new PostFormRequest(url,params);
        RequestCall<CircleListDTO> requestCall=new RequestCall<>(postFormRequest,(RequestCallback<CircleListDTO>)requestCallback,
                CircleListDTO.class);

        submitRequset(requestCall);
    }

    @Override
    public <T> void getMsgCount(RequestCallback<T> requestCallback) {
        HashMap<String,String> params=new HashMap<String,String>();
        //
        String url=RequestUrl.MSG_CENTER_COUNT;
        PostFormRequest postFormRequest=new PostFormRequest(url,params);
        RequestCall<MsgCountEntityDTO> requestCall=new RequestCall<>(postFormRequest,(RequestCallback<MsgCountEntityDTO>)requestCallback
        ,MsgCountEntityDTO.class);
        submitRequset(requestCall);

     }

    @Override
    public <T> void updateMsgStatus(String type_id, String status, RequestCallback<T> requestCallback) {
        HashMap<String,String> params=new HashMap<>();
        params.put("type_id",type_id);
        params.put("status",status);
        //
        String url= RequestUrl.MSG_UPDATE_STATUS;
        PostFormRequest postFormRequest=new PostFormRequest(url,params);
        RequestCall<CommonResultEntity> requestCall=new RequestCall<>(postFormRequest
                ,(RequestCallback<CommonResultEntity>)requestCallback
                ,CommonResultEntity.class);
        submitRequset(requestCall);
    }

    @Override
    public <T> void getHomePostsHot(String page, String pagesize, RequestCallback<T> requestCallback) {
        HashMap<String,String> params=new HashMap<>();
        params.put("page",page);
        params.put("pagesize",pagesize);
        //
        String url=RequestUrl.HOME_POSTS_HOT;
        PostFormRequest postFormRequest=new PostFormRequest(url,params);
        //
        RequestCall<HomePostsHotDTO> requestCall=new RequestCall<>(postFormRequest,(RequestCallback<HomePostsHotDTO>)requestCallback
                ,HomePostsHotDTO.class);
        submitRequset(requestCall);
    }
}
