package com.junhsue.ksee.net.api;

import com.junhsue.ksee.dto.AnswerCardDetailDTO;
import com.junhsue.ksee.dto.ClassGroupChatDetailsDTO;
import com.junhsue.ksee.dto.ClassRoomListDTO;
import com.junhsue.ksee.dto.InviteDTO;
import com.junhsue.ksee.dto.KnowCalendarDTO;
import com.junhsue.ksee.dto.KnowCalendarPriseDTO;
import com.junhsue.ksee.dto.MsgCenterReceiveReplyDTO;
import com.junhsue.ksee.dto.MyCoinListDTO;
import com.junhsue.ksee.dto.MyFeedbackDTO;
import com.junhsue.ksee.dto.MyOrderListDTO;
import com.junhsue.ksee.dto.MyaskDTO;
import com.junhsue.ksee.dto.MycollectDTO;
import com.junhsue.ksee.dto.MyinviteDTO;
import com.junhsue.ksee.dto.RegisterSuccessDTO;
import com.junhsue.ksee.dto.SaveQNTokenDTO;
import com.junhsue.ksee.dto.SendSMSDTO;
import com.junhsue.ksee.dto.SettingNickNameDTO;
import com.junhsue.ksee.dto.TagListDTO;
import com.junhsue.ksee.dto.UpdatePasswordDTO;
import com.junhsue.ksee.dto.VerifyAccountIsExistDTO;
import com.junhsue.ksee.dto.VerifyVerifyCodeDTO;
import com.junhsue.ksee.dto.VersionUpdateDTO;
import com.junhsue.ksee.entity.AccessToken;
import com.junhsue.ksee.entity.ArticleDetail;
import com.junhsue.ksee.entity.ResultEntity;
import com.junhsue.ksee.entity.UserInfo;
import com.junhsue.ksee.entity.WeChatUserInfo;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.request.GetFormRequest;
import com.junhsue.ksee.net.request.PostFormRequest;
import com.junhsue.ksee.net.request.RequestCall;
import com.junhsue.ksee.net.url.RequestUrl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hunter_J on 17/3/24.
 */

public class OkHttpILoginImpl extends BaseClientApi implements ILogin {

  private static OkHttpILoginImpl mInstance;

  public static OkHttpILoginImpl getInstance(){
    if (null == mInstance){
      synchronized (OkHttpILoginImpl.class){
        if (null == mInstance){
          mInstance = new OkHttpILoginImpl();
        }
      }
    }
    return mInstance;
  }

  @Override
  public <T> void getAccessTokenFormWeChat(String requestUrl, RequestCallback<T> requestCallback) {
    String url = requestUrl;
    GetFormRequest getFormRequest = new GetFormRequest(url,null);
    RequestCall<AccessToken> requestCall = new RequestCall<AccessToken>(getFormRequest, (RequestCallback<AccessToken>) requestCallback,AccessToken.class);
    requestCall.setParser(false);
    submitRequset(requestCall);
  }

  @Override
  public <T> void getWeChatUserInfo(String requestUrl, RequestCallback<T> requestCallback) {
    String url = requestUrl;
    GetFormRequest getFormRequest = new GetFormRequest(url,null);
    RequestCall<WeChatUserInfo> requestCall = new RequestCall<WeChatUserInfo>(getFormRequest, (RequestCallback<WeChatUserInfo>) requestCallback,WeChatUserInfo.class);
    requestCall.setParser(false);
    submitRequset(requestCall);
  }

  /**
   * 手机号码账户密码登录接口
   * @param phonenumber ：账号手机号码
   * @param password ：注册密码
   * @param requestCallback : 请求结果接口
   * @param <T> ：
   */
  @Override
  public <T> void loginByPhonenumber(String logintype, String unionid, String phonenumber, String password, RequestCallback<T> requestCallback) {
    Map<String,String> params = new HashMap<String, String>();
    params.put("logintype",logintype);
    if (null != logintype && logintype.equals("2")) {
      params.put("unionid", unionid);
    }else {
      params.put("phonenumber", phonenumber);
      params.put("password", password);
    }
    String url = RequestUrl.LOGIN_BY_PHONENUMBER;
    PostFormRequest postFormRequest = new PostFormRequest(url,params);
    RequestCall<UserInfo> requestCall = new RequestCall<UserInfo>(postFormRequest, (RequestCallback<UserInfo>) requestCallback,UserInfo.class);
    submitRequset(requestCall);
  }

  /**
   * 手机号码账户快速登录接口
   * @param phonenumber ：账号手机号码
   * @param msg_id ：接受验证码对应的message id
   * @param code ：验证码
   * @param requestCallback : 请求结果接口
   * @param <T> ：
   */
  @Override
  public <T> void loginByVerityCode(String phonenumber, String msg_id, String code, RequestCallback<T> requestCallback) {
    Map<String,String> params = new HashMap<String, String>();
    params.put("logintype","4");
    params.put("phonenumber", phonenumber);
    params.put("msg_id", msg_id);
    params.put("code", code);
    String url = RequestUrl.LOGIN_BY_VERITYCODE;
    PostFormRequest postFormRequest = new PostFormRequest(url,params);
    RequestCall<UserInfo> requestCall = new RequestCall<UserInfo>(postFormRequest, (RequestCallback<UserInfo>) requestCallback,UserInfo.class);
    submitRequset(requestCall);
  }

  /**
   * 手机号码账户登录接口
   * @param token ：现有账号的token值
   * @param requestCallback : 请求结果接口
   * @param <T> ：
   */
  @Override
  public <T> void autologinByToken(String token, RequestCallback<T> requestCallback) {
    String url = RequestUrl.LOGIN_AUTOLOGIN;
    Map<String,String> paramsHead = new HashMap<>();
    paramsHead.put("token",token);
    Map<String,String> paramsBody = new HashMap<>();
    PostFormRequest postFormRequest = new PostFormRequest(url,paramsBody,paramsHead);
    RequestCall<UserInfo> requestCall = new RequestCall<UserInfo>(postFormRequest, (RequestCallback<UserInfo>) requestCallback,UserInfo.class);
    submitRequset(requestCall);
  }

  /**
   * 微信号与已注册的手机号码绑定
   * @param unionid 微信号的唯一识别码
   * @param phonenumber 绑定的手机号码
   * @param avatar 微信头像
   * @param nickname 微信昵称
   * @param requestCallback
     * @param <T>
     */
  @Override
  public <T> void wechatBindPhoneNumber(String unionid, String phonenumber, String avatar, String nickname, RequestCallback<T> requestCallback) {
    String url = RequestUrl.REGISTER_WXLOGIN_BIND_PHONENUMBER;
    Map<String,String> paramsBody = new HashMap<>();
    paramsBody.put("unionid",unionid);
    paramsBody.put("phonenumber",phonenumber);
    paramsBody.put("avatar",avatar);
    paramsBody.put("nickname",nickname);
    PostFormRequest postFormRequest = new PostFormRequest(url,paramsBody);
    RequestCall<SettingNickNameDTO> requestCall = new RequestCall<SettingNickNameDTO>(postFormRequest, (RequestCallback<SettingNickNameDTO>) requestCallback,SettingNickNameDTO.class);
    submitRequset(requestCall);
  }

  /**
   * 发送验证码接口
   * @param phonenumber ：接收验证码的手机号码
   * @param requestCallback ：请求结果接口
   * @param <T>
   */
  @Override
  public <T> void registerSendsms(String phonenumber, RequestCallback<T> requestCallback) {
    Map<String,String> params = new HashMap<>();
    params.put("phonenumber",phonenumber);
    String url = RequestUrl.REGISTER_GET_VERIFYCODE;
    PostFormRequest postFormRequest = new PostFormRequest(url,params);
    RequestCall<SendSMSDTO> requestCall = new RequestCall<SendSMSDTO>(postFormRequest, (RequestCallback<SendSMSDTO>) requestCallback, SendSMSDTO.class);
    submitRequset(requestCall);
  }

  /**
   * 验证验证码接口
   * @param msg_id ：发送验证码接口返回的msg_id
   * @param code ：验证码
   * @param requestCallback ：请求结果接口
   * @param <T>
   */
  @Override
  public <T> void registerVerifysms(String msg_id, String code, RequestCallback<T> requestCallback) {
    Map<String,String> params = new HashMap<>();
    params.put("msg_id",msg_id);
    params.put("code",code);
    String url = RequestUrl.REGISTER_VERIFY_VERIFYCODE;
    PostFormRequest postFormRequest = new PostFormRequest(url,params);
    RequestCall<VerifyVerifyCodeDTO> requestCall = new RequestCall<VerifyVerifyCodeDTO>(postFormRequest, (RequestCallback<VerifyVerifyCodeDTO>) requestCallback,VerifyVerifyCodeDTO.class);
    submitRequset(requestCall);
  }

  /**
   * 查询账号是否存在接口
   * @param phonenumber ：查询的手机号码账号
   * @param requestCallback ：请求结果接口
   * @param <T>
   */
  @Override
  public <T> void loginSearchAccount(String phonenumber,RequestCallback<T> requestCallback) {
    Map<String, String> params = new HashMap<>();
    params.put("phonenumber",phonenumber);
    String url = RequestUrl.REGISTER_VERIFY_PHONENUMBER_CHECKACCOUNT;
    PostFormRequest postFormRequest = new PostFormRequest(url,params);
    RequestCall<VerifyAccountIsExistDTO> requestCall = new RequestCall<VerifyAccountIsExistDTO>(postFormRequest, (RequestCallback<VerifyAccountIsExistDTO>) requestCallback,VerifyAccountIsExistDTO.class);
    submitRequset(requestCall);
  }

  /**
   * 获取七牛云存储的token
   * @param <T> 请求返回结果
   */
  @Override
  public <T> void GetQNToken(RequestCallback<T> requestCallback) {
    String url = RequestUrl.REGISTER_HEADIMG_GETTOKEN;
    GetFormRequest getFormRequest = new GetFormRequest(url,null);
    RequestCall<SaveQNTokenDTO> requestCall = new RequestCall<SaveQNTokenDTO>(getFormRequest, (RequestCallback<SaveQNTokenDTO>) requestCallback,SaveQNTokenDTO.class);
    submitRequset(requestCall);
  }

  /**
   * 手机号码注册
   * @param phonenumber ：手机号码
   * @param password ：密码
   * @param nickname ：昵称
   * @param organization ：组织/机构
   * @param avatar ：头像地址
   * @param tags ：领域信息
   * @param requestCallback
   * @param <T>
   */
  @Override
  public <T> void registerByPhonenumberOrWechat(String logintype, String unionid, String phonenumber, String password, String nickname, String organization, String avatar, String tags, RequestCallback<T> requestCallback) {
    Map<String,String> params = new HashMap<>();
    if (null == logintype) return;
    params.put("logintype",logintype);
    if (logintype.equals("2")){
      if (null == unionid)return;
      params.put("unionid",unionid);
    }
    params.put("phonenumber",phonenumber);
    params.put("password",password);
    if (null != nickname)
      params.put("nickname",nickname);
    if (null != organization)
      params.put("organization",organization);
    if (null != avatar)
      params.put("avatar",avatar);
    if (null != tags)
      params.put("tags",tags);
    String url = RequestUrl.REGISTER_BY_PHONENUMBER;
    PostFormRequest postFormRequest = new PostFormRequest(url,params);
    RequestCall<RegisterSuccessDTO> requestCall = new RequestCall<RegisterSuccessDTO>(postFormRequest, (RequestCallback<RegisterSuccessDTO>) requestCallback,RegisterSuccessDTO.class);
    submitRequset(requestCall);
  }

  /**
   * 修改密码
   * @param phonenumber ：手机号码
   * @param password ：重置密码
   * @param requestCallback
   * @param <T>
   */
  @Override
  public <T> void loginForgetPassword(String phonenumber, String password, RequestCallback<T> requestCallback) {
    Map<String, String> params = new HashMap<>();
    params.put("phonenumber",phonenumber);
    params.put("password",password);
    String url = RequestUrl.LOGIN_FORGETPASSWORD;
    PostFormRequest postFormRequest = new PostFormRequest(url,params);
    RequestCall<UpdatePasswordDTO> requestCall = new RequestCall<UpdatePasswordDTO>(postFormRequest, (RequestCallback<UpdatePasswordDTO>) requestCallback,UpdatePasswordDTO.class);
    submitRequset(requestCall);
  }

  /**
   * 获取擅长领域列表
   * @param <T>
   */
  @Override
  public <T> void registerGetTags(RequestCallback<T> requestCallback) {
    String url = RequestUrl.REGISTER_GET_TAGS;
    GetFormRequest getFormRequest = new GetFormRequest(url,null);
    RequestCall<TagListDTO> requestcall = new RequestCall<TagListDTO>(getFormRequest, (RequestCallback<TagListDTO>) requestCallback, TagListDTO.class);
    submitRequset(requestcall);
  }

  /**
   * 修改头像
   * @param token 请求头
   * @param value 请求体
   * @param requestCallback
   * @param <T>
   */
  @Override
  public <T> void settingAvatar(String token, String value, RequestCallback<T> requestCallback) {
    String url = RequestUrl.SETTING_AVATAR;
    Map<String,String> paramsHead = new HashMap<>();
    paramsHead.put("token",token);
    Map<String,String> paramsBody = new HashMap<>();
    paramsBody.put("value",value);
    PostFormRequest postFormRequest = new PostFormRequest(url,paramsBody,paramsHead);
    RequestCall<SettingNickNameDTO> requestCall = new RequestCall<SettingNickNameDTO>(postFormRequest, (RequestCallback<SettingNickNameDTO>) requestCallback,SettingNickNameDTO.class);
    submitRequset(requestCall);
  }

  /**
   * 修改昵称
   * @param token 请求头
   * @param value 请求体
   * @param requestCallback
   * @param <T>
   */
  @Override
  public <T> void settingNickName(String token, String value, RequestCallback<T> requestCallback) {
    String url = RequestUrl.SETTING_NICKNAME;
    Map<String,String> paramsHead = new HashMap<>();
    paramsHead.put("token",token);
    Map<String,String> paramsBody = new HashMap<>();
    paramsBody.put("value",value);
    PostFormRequest postFormRequest = new PostFormRequest(url,paramsBody,paramsHead);
    RequestCall<SettingNickNameDTO> requestCall = new RequestCall<SettingNickNameDTO>(postFormRequest, (RequestCallback<SettingNickNameDTO>) requestCallback,SettingNickNameDTO.class);
    submitRequset(requestCall);
  }

  /**
   * 修改联系地址
   * @param token 请求头
   * @param province 省
   * @param city 市
   * @param district 区
   * @param address 详细地址
   * @param requestCallback
   * @param <T>
   */
  @Override
  public <T> void settingAddress(String token, String province, String city, String district, String address, RequestCallback<T> requestCallback) {
    String url = RequestUrl.SETTING_ADDRESS;
    Map<String,String> paramsHead = new HashMap<>();
    paramsHead.put("token",token);
    Map<String,String> paramsBody = new HashMap<>();
    paramsBody.put("province",province);
    paramsBody.put("city",city);
    paramsBody.put("district",district);
    paramsBody.put("address",address);
    PostFormRequest postFormRequest = new PostFormRequest(url,paramsBody,paramsHead);
    RequestCall<SettingNickNameDTO> requestCall = new RequestCall<SettingNickNameDTO>(postFormRequest, (RequestCallback<SettingNickNameDTO>) requestCallback,SettingNickNameDTO.class);
    submitRequset(requestCall);
  }

  /**
   * 修改学校/机构
   * @param token 请求头
   * @param value 请求体
   * @param requestCallback
   * @param <T>
   */
  @Override
  public <T> void settingOrganization(String token, String value, RequestCallback<T> requestCallback) {
    String url = RequestUrl.SETTING_ORGANIZATION;
    Map<String,String> paramsHead = new HashMap<>();
    paramsHead.put("token",token);
    Map<String,String> paramsBody = new HashMap<>();
    paramsBody.put("value",value);
    PostFormRequest postFormRequest = new PostFormRequest(url,paramsBody,paramsHead);
    RequestCall<SettingNickNameDTO> requestCall = new RequestCall<SettingNickNameDTO>(postFormRequest, (RequestCallback<SettingNickNameDTO>) requestCallback,SettingNickNameDTO.class);
    submitRequset(requestCall);
  }

  /**
   * 获取职务列表
   * @param requestCallback
   * @param <T>
   */
  @Override
  public <T> void settingGetJobList(RequestCallback<T> requestCallback) {
    String url = RequestUrl.SETTING_JOB_LIST;
    GetFormRequest getFormRequest = new GetFormRequest(url,null);
    RequestCall<TagListDTO> requestcall = new RequestCall<TagListDTO>(getFormRequest, (RequestCallback<TagListDTO>) requestCallback, TagListDTO.class);
    submitRequset(requestcall);
  }

  /**
   * 修改职位
   * @param token 请求头
   * @param value 请求体
   * @param requestCallback
   * @param <T>
   */
  @Override
  public <T> void settingJob(String token, String value, RequestCallback<T> requestCallback) {
    String url = RequestUrl.SETTING_JOB;
    Map<String,String> paramsHead = new HashMap<>();
    paramsHead.put("token",token);
    Map<String,String> paramsBody = new HashMap<>();
    paramsBody.put("value",value);
    PostFormRequest postFormRequest = new PostFormRequest(url,paramsBody,paramsHead);
    RequestCall<SettingNickNameDTO> requestCall = new RequestCall<SettingNickNameDTO>(postFormRequest, (RequestCallback<SettingNickNameDTO>) requestCallback,SettingNickNameDTO.class);
    submitRequset(requestCall);
  }


  /**
   * 修改性别
   * @param token 请求头
   * @param value 请求体
   * @param requestCallback
   * @param <T>
   */
  @Override
  public <T> void settingSex(String token, String value, RequestCallback<T> requestCallback) {
    String url = RequestUrl.SETTING_SEX;
    Map<String,String> paramsHead = new HashMap<>();
    paramsHead.put("token",token);
    Map<String,String> paramsBody = new HashMap<>();
    paramsBody.put("value",value);
    PostFormRequest postFormRequest = new PostFormRequest(url,paramsBody,paramsHead);
    RequestCall<SettingNickNameDTO> requestCall = new RequestCall<SettingNickNameDTO>(postFormRequest, (RequestCallback<SettingNickNameDTO>) requestCallback,SettingNickNameDTO.class);
    submitRequset(requestCall);
  }

  /**
   * 修改出生年月
   * @param token 请求头
   * @param value 请求体
   * @param requestCallback
   * @param <T>
   */
  @Override
  public <T> void settingBirthday(String token, String value, RequestCallback<T> requestCallback) {
    String url = RequestUrl.SETTING_BIRTHDAY;
    Map<String,String> paramsHead = new HashMap<>();
    paramsHead.put("token",token);
    Map<String,String> paramsBody = new HashMap<>();
    paramsBody.put("value",value);
    PostFormRequest postFormRequest = new PostFormRequest(url,paramsBody,paramsHead);
    RequestCall<SettingNickNameDTO> requestCall = new RequestCall<SettingNickNameDTO>(postFormRequest, (RequestCallback<SettingNickNameDTO>) requestCallback,SettingNickNameDTO.class);
    submitRequset(requestCall);
  }

  /**
   * 我的问答 —— 被邀请的
   * @param token 请求头
   * @param page 当前页
   * @param pagesize 当前页显示个数
   * @param requestCallback
   * @param <T>
   */
  @Override
  public <T> void myanswerInvite(String token, String page, String pagesize, RequestCallback<T> requestCallback) {
    String url = RequestUrl.MYANSWER_MYINVITE;
    Map<String,String> paramsHead = new HashMap<>();
    paramsHead.put("token",token);
    Map<String,String> paramsBody = new HashMap<>();
    paramsBody.put("page",page);
    paramsBody.put("pagesize",pagesize);
    PostFormRequest postFormRequest = new PostFormRequest(url,paramsBody,paramsHead);
    RequestCall<MyinviteDTO> requestCall = new RequestCall<MyinviteDTO>(postFormRequest, (RequestCallback<MyinviteDTO>) requestCallback,MyinviteDTO.class);
    submitRequset(requestCall);
  }


  /**
   * 我的问答 —— 我提问的
   * @param token 请求头
   * @param page 当前页
   * @param pagesize 当前页显示个数
   * @param requestCallback
   * @param <T>
   */
  @Override
  public <T> void myanswerMyask(String token, String page, String pagesize, RequestCallback<T> requestCallback) {
    String url = RequestUrl.MYANSWER_MYASK;
    Map<String,String> paramsHead = new HashMap<>();
    paramsHead.put("token",token);
    Map<String,String> paramsBody = new HashMap<>();
    paramsBody.put("page",page);
    paramsBody.put("pagesize",pagesize);
    PostFormRequest postFormRequest = new PostFormRequest(url,paramsBody,paramsHead);
    RequestCall<MyaskDTO> requestCall = new RequestCall<MyaskDTO>(postFormRequest, (RequestCallback<MyaskDTO>) requestCallback,MyaskDTO.class);
    submitRequset(requestCall);
  }


  /**
   * 我的问答 —— 我收藏的
   * @param token 请求头
   * @param page 当前页
   * @param pagesize 当前页显示个数
   * @para  business_id 收藏的业务id
   * @param requestCallback
   * @param <T>
   */
  @Override
  public <T> void myanswerMycollect(String token, String business_id, String page, String pagesize, RequestCallback<T> requestCallback) {
    String url = RequestUrl.MYANSWER_MYCOLLECT;
    Map<String,String> paramsHead = new HashMap<>();
    paramsHead.put("token",token);
    Map<String,String> paramsBody = new HashMap<>();
    paramsBody.put("business_id",business_id);
    paramsBody.put("page",page);
    paramsBody.put("pagesize",pagesize);
    PostFormRequest postFormRequest = new PostFormRequest(url,paramsBody,paramsHead);
    RequestCall<MycollectDTO> requestCall = new RequestCall<MycollectDTO>( postFormRequest, (RequestCallback<MycollectDTO>) requestCallback,MycollectDTO.class);
    submitRequset(requestCall);
  }

  /**
   * 吾 —— 意见反馈
   * @param token 请求头
   * @param content 反馈内容
   * @param platform_id （1：IOS; 2：Android; 3：WebView）
   * @param requestCallback
   * @param <T>
   */
  @Override
  public <T> void myFeedback(String token, String content, String platform_id, RequestCallback<T> requestCallback) {
    String url = RequestUrl.MY_FEEDBACK;
    Map<String,String> paramsHead = new HashMap<>();
    paramsHead.put("token",token);
    Map<String,String> paramsBody = new HashMap<>();
    paramsBody.put("content",content);
    paramsBody.put("platform_id",platform_id);
    PostFormRequest postFormRequest = new PostFormRequest(url,paramsBody,paramsHead);
    RequestCall<MyFeedbackDTO> requestCall = new RequestCall<MyFeedbackDTO>( postFormRequest, (RequestCallback<MyFeedbackDTO>) requestCallback,MyFeedbackDTO.class);
    submitRequset(requestCall);
  }

  /**
   * 吾 —— 我的订单
   * @param token 请求头
   * @param pagenum 当前页
   * @param pagesize 当前页显示个数
   * @param requestCallback
   * @param <T>
   */
  @Override
  public <T> void myOrderList(String token, String pagenum, String pagesize, RequestCallback<T> requestCallback) {
    String url = RequestUrl.ORDER_LIST;
//    Map<String,String> paramsHead = new HashMap<>();
//    paramsHead.put("token",token);
    Map<String,String> paramsBody = new HashMap<>();
//    paramsBody.put("pagenum",pagenum);
//    paramsBody.put("pagesize",pagesize);
    PostFormRequest postFormRequest = new PostFormRequest(url,paramsBody);
    RequestCall<MyOrderListDTO> requestCall = new RequestCall<MyOrderListDTO>( postFormRequest, (RequestCallback<MyOrderListDTO>) requestCallback,MyOrderListDTO.class);
    submitRequset(requestCall);
  }

  /**
   * 熟 —— 教室列表
   * @param token
   * @param <T>
   */
  @Override
  public <T> void colleageClassRoomList(String token, RequestCallback<T> requestCallback) {
    String url = RequestUrl.CLASSROOM_LIST;
    Map<String,String> paramsHead = new HashMap<>();
    paramsHead.put("token",token);
    Map<String,String> paramsBody = new HashMap<>();
    PostFormRequest postFormRequest = new PostFormRequest(url,paramsHead,paramsBody);
    RequestCall<ClassRoomListDTO> requestCall = new RequestCall<ClassRoomListDTO>( postFormRequest, (RequestCallback<ClassRoomListDTO>) requestCallback,ClassRoomListDTO.class);
    submitRequset(requestCall);
  }

  /**
   * 熟 —— 教室详情页
   * @param roomid 教室id
   * @param requestCallback
   * @param <T>
   */
  @Override
  public <T> void colleageClassRoomDetails(String roomid, RequestCallback<T> requestCallback) {
    String url = RequestUrl.CLASSROOM_DETAILS;
//    Map<String,String> paramsHead = new HashMap<>();
    Map<String,String> paramsBody = new HashMap<>();
    paramsBody.put("roomid",roomid);
    PostFormRequest postFormRequest = new PostFormRequest(url,paramsBody);
    RequestCall<ClassGroupChatDetailsDTO> requestCall = new RequestCall<ClassGroupChatDetailsDTO>( postFormRequest, (RequestCallback<ClassGroupChatDetailsDTO>) requestCallback,ClassGroupChatDetailsDTO.class);
    submitRequset(requestCall);
  }

  /**
   * 拾 —— 单向历展示
   * @param token
   * @param version 当前app版本号
   * @param requestCallback
   * @param <T>
   */
  @Override
  public <T> void knowCalendarQuery(String token, String version, RequestCallback<T> requestCallback) {
    String url = RequestUrl.KNOW_CALENDAR_QUERY;
    Map<String,String> paramsHead = new HashMap<>();
    paramsHead.put("token",token);
    if (null != version) {
      paramsHead.put("version", version);
    }
    Map<String,String> paramsBody = new HashMap<>();
    PostFormRequest postFormRequest = new PostFormRequest(url,paramsBody,paramsHead);
    RequestCall<KnowCalendarDTO> requestcall = new RequestCall<KnowCalendarDTO>(postFormRequest, (RequestCallback<KnowCalendarDTO>) requestCallback, KnowCalendarDTO.class);
    submitRequset(requestcall);
  }

  /**
   * 单向历点赞
   * @param token
   * @param calendar_id
   * @param type
   * @param requestCallback
     * @param <T>
     */
  @Override
  public <T> void knowCalendarPrise(String token, String calendar_id, String type, RequestCallback<T> requestCallback) {
    String url = RequestUrl.KNOW_CALENDAR_PRISE;
    Map<String,String> paramsHead = new HashMap<>();
    paramsHead.put("token",token);
    Map<String,String> paramsBody = new HashMap<>();
    paramsBody.put("calendar_id",calendar_id);
    paramsBody.put("type",type);
    PostFormRequest postFormRequest = new PostFormRequest(url,paramsBody,paramsHead);
    RequestCall<KnowCalendarPriseDTO> requestcall = new RequestCall<KnowCalendarPriseDTO>(postFormRequest, (RequestCallback<KnowCalendarPriseDTO>) requestCallback, KnowCalendarPriseDTO.class);
    submitRequset(requestcall);
  }

  /**
   * 我的问答卡片
   * @param token 请求头
   * @param page 当前页
   * @param pagesize 当前页显示个数
   * @param requestCallback
   * @param <T>
   */
  @Override
  public <T> void myanswerCardList(String token, String page, String pagesize, RequestCallback<T> requestCallback) {
    String url = RequestUrl.MYANSWER_CARD_LIST;
    Map<String,String> paramsHead = new HashMap<>();
    paramsHead.put("token",token);
    Map<String,String> paramsBody = new HashMap<>();
    paramsBody.put("page",page);
    paramsBody.put("pagesize",pagesize);
    PostFormRequest postFormRequest = new PostFormRequest(url,paramsBody,paramsHead);
    RequestCall<AnswerCardDetailDTO> requestCall = new RequestCall<AnswerCardDetailDTO>(postFormRequest, (RequestCallback<AnswerCardDetailDTO>) requestCallback,AnswerCardDetailDTO.class);
    submitRequset(requestCall);
  }

  /**
   * 知 —— 文章详情页
   * @param id
   * @param requestCallback
   * @param <T>
   */
  @Override
  public <T> void getRealizeArticleDetail(String id, RequestCallback<T> requestCallback) {
    String url = RequestUrl.REALIZE_ARTICLE_DETAIL;
    Map<String,String> paramsBody = new HashMap<>();
    paramsBody.put("id",id);
    PostFormRequest postFormRequest = new PostFormRequest(url,paramsBody);
    RequestCall<ArticleDetail> requestcall = new RequestCall<ArticleDetail>(postFormRequest, (RequestCallback<ArticleDetail>) requestCallback, ArticleDetail.class);
    submitRequset(requestcall);
  }

  /**
   * 知：文章详情页点赞
   * @param <T>
   */
  @Override
  public <T> void realizeArticleDetailPrise(String business_id, RequestCallback<T> requestCallback) {
    String url = RequestUrl.KNOW_CALENDAR_PRISE;
    Map<String,String> paramsBody = new HashMap<>();
    paramsBody.put("business_id",business_id);
    PostFormRequest postFormRequest = new PostFormRequest(url,paramsBody);
    RequestCall<KnowCalendarPriseDTO> requestcall = new RequestCall<KnowCalendarPriseDTO>(postFormRequest, (RequestCallback<KnowCalendarPriseDTO>) requestCallback, KnowCalendarPriseDTO.class);
    submitRequset(requestcall);
  }

  @Override
  public <T> void inviteList(String source_user_id, RequestCallback<T> requestCallback) {
    String url = RequestUrl.INVITE_LIST_URL;
    Map<String,String> paramsBody = new HashMap<>();
    paramsBody.put("source_user_id",source_user_id);
    PostFormRequest postFormRequest = new PostFormRequest(url,paramsBody);
    RequestCall<InviteDTO> requestcall = new RequestCall<InviteDTO>(postFormRequest, (RequestCallback<InviteDTO>) requestCallback, InviteDTO.class);
    submitRequset(requestcall);
  }

  /**
   * 吾
   * 我的兑换券
   * @param requestCallback
   * @param <T>
   */
  @Override
  public <T> void myCoin(RequestCallback<T> requestCallback) {
    String url = RequestUrl.MY_COIN;
    Map<String,String> paramsBody = new HashMap<>();
    PostFormRequest getFormRequest = new PostFormRequest(url,paramsBody);
    RequestCall<MyCoinListDTO> requestcall = new RequestCall<MyCoinListDTO>(getFormRequest, (RequestCallback<MyCoinListDTO>) requestCallback, MyCoinListDTO.class);
    submitRequset(requestcall);
  }

  /**
   * 验证版本更新接口
   * @param requestCallback : 请求结果接口
   * @param <T> ：
   */
  @Override
  public <T> void verityVersionUpdate(RequestCallback<T> requestCallback) {
    String url = RequestUrl.MY_VERSION_UPDATE;
    Map<String,String> paramsBody = new HashMap<>();
    paramsBody.put("platform_id","2");
    PostFormRequest postFormRequest = new PostFormRequest(url,paramsBody);
    RequestCall<VersionUpdateDTO> requestCall = new RequestCall<VersionUpdateDTO>(postFormRequest, (RequestCallback<VersionUpdateDTO>) requestCallback,VersionUpdateDTO.class);
    submitRequset(requestCall);
  }

  /**
   * 消息中心
   * 收到的回复
   * @param page
   * @param pagesize
   * @param requestCallback
   * @param <T>
   */
  @Override
  public <T> void msgCenterReceiveReply(String id, String page, String pagesize, RequestCallback<T> requestCallback) {
    String url = RequestUrl.MSG_CENTER_RECEIVE_REPLY;
    Map<String,String> paramsBody = new HashMap<>();
    paramsBody.put("type_id",id);
    paramsBody.put("page",page);
    paramsBody.put("pagesize",pagesize);
    PostFormRequest postFormRequest = new PostFormRequest(url,paramsBody);
    RequestCall<MsgCenterReceiveReplyDTO> requestCall = new RequestCall<MsgCenterReceiveReplyDTO>(postFormRequest, (RequestCallback<MsgCenterReceiveReplyDTO>) requestCallback,MsgCenterReceiveReplyDTO.class);
    submitRequset(requestCall);
  }

  //消息中心——修改某条信息状态置为已读
  @Override
  public <T> void msgCenterReceiveReplyIsRead(String message_id , String status, RequestCallback<T> requestCallback) {
    String url = RequestUrl.MSG_CENTER_RECEIVE_REPLY_ISREAD;
    Map<String,String> paramsBody = new HashMap<>();
    paramsBody.put("message_id",message_id);
    paramsBody.put("status",status);
    PostFormRequest postFormRequest = new PostFormRequest(url,paramsBody);
    RequestCall<ResultEntity> requestCall = new RequestCall<ResultEntity>(postFormRequest, (RequestCallback<ResultEntity>) requestCallback,ResultEntity.class);
    submitRequset(requestCall);
  }

  /**
   * 消息中心——修改所有信息状态置为已读
   * @param type_id 16:点赞消息；17:收藏消息；18:回复消息；19:系统消息；
   * @param status 指定修改状态：0:未读；1:已读；2:删除；
   * @param requestCallback
   * @param <T>
   */
  @Override
  public <T> void msgCenterReceiveReplyAllRead(String type_id, String status, RequestCallback<T> requestCallback) {
    String url = RequestUrl.MSG_CENTER_RECEIVE_REPLY_ALLREAD;
    Map<String,String> paramsBody = new HashMap<>();
    paramsBody.put("type_id",type_id);
    paramsBody.put("status",status);
    PostFormRequest postFormRequest = new PostFormRequest(url,paramsBody);
    RequestCall<ResultEntity> requestCall = new RequestCall<ResultEntity>(postFormRequest, (RequestCallback<ResultEntity>) requestCallback,ResultEntity.class);
    submitRequset(requestCall);
  }
}
