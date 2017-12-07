package com.junhsue.ksee.utils;

import android.content.Context;
import android.net.Uri;
import android.text.*;
import android.text.style.ImageSpan;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EMLog;
import com.junhsue.ksee.R;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.entity.UserInfo;
import com.junhsue.ksee.profile.UserProfileService;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hunter_J on 17/4/28.
 */

public class HXUtils {




  /**
   * Get digest according message type and content
   *
   * @param message
   * @param context
   * @return
   */
  public static String getMessageDigest(EMMessage message, Context context) {
    String digest = "";
    switch (message.getType()) {
      case LOCATION:
        if (message.direct() == EMMessage.Direct.RECEIVE) {
          digest = getString(context, R.string.location_recv);
          digest = String.format(digest, message.getFrom());
          return digest;
        } else {
          digest = getString(context, R.string.location_prefix);
        }
        break;
      case IMAGE:
        digest = getString(context, R.string.picture);
        break;
      case VOICE:
        digest = getString(context, R.string.voice_prefix);
        break;
      case VIDEO:
        digest = getString(context, R.string.video);
        break;
      case TXT:
        EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
        if(message.getBooleanAttribute(Constants.MESSAGE_ATTR_IS_VOICE_CALL, false)){
          digest = getString(context, R.string.voice_call) + txtBody.getMessage();
        }else if(message.getBooleanAttribute(Constants.MESSAGE_ATTR_IS_VIDEO_CALL, false)){
          digest = getString(context, R.string.video_call) + txtBody.getMessage();
        }else if(message.getBooleanAttribute(Constants.MESSAGE_ATTR_IS_BIG_EXPRESSION, false)){
          if(!android.text.TextUtils.isEmpty(txtBody.getMessage())){
            digest = txtBody.getMessage();
          }else{
            digest = getString(context, R.string.dynamic_expression);
          }
        }else{
          digest = txtBody.getMessage();
        }
        break;
      case FILE:
        digest = getString(context, R.string.file);
        break;
      default:
        EMLog.e("HX", "error, unknow type");
        return "";
    }

    return digest;
  }

  static String getString(Context context, int resId){
    return context.getResources().getString(resId);
  }


  private static final Spannable.Factory spannableFactory = Spannable.Factory
      .getInstance();

  public static Spannable getSmiledText(Context context, CharSequence text) {
    Spannable spannable = spannableFactory.newSpannable(text);
    addSmiles(context, spannable);
    return spannable;
  }


  private static final Map<Pattern, Object> emoticons = new HashMap<Pattern, Object>();

  /**
   * replace existing spannable with smiles
   * @param context
   * @param spannable
   * @return
   */
  public static boolean addSmiles(Context context, Spannable spannable) {
    boolean hasChanges = false;
    for (Map.Entry<Pattern, Object> entry : emoticons.entrySet()) {
      Matcher matcher = entry.getKey().matcher(spannable);
      while (matcher.find()) {
        boolean set = true;
        for (ImageSpan span : spannable.getSpans(matcher.start(),
            matcher.end(), ImageSpan.class))
          if (spannable.getSpanStart(span) >= matcher.start()
              && spannable.getSpanEnd(span) <= matcher.end())
            spannable.removeSpan(span);
          else {
            set = false;
            break;
          }
        if (set) {
          hasChanges = true;
          Object value = entry.getValue();
          if(value instanceof String && !((String) value).startsWith("http")){
            File file = new File((String) value);
            if(!file.exists() || file.isDirectory()){
              return false;
            }
            spannable.setSpan(new ImageSpan(context, Uri.fromFile(file)),
                matcher.start(), matcher.end(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
          }else{
            spannable.setSpan(new ImageSpan(context, (Integer)value),
                matcher.start(), matcher.end(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
          }
        }
      }
    }

    return hasChanges;
  }


  /**
   * 注册环信
   * @param context
   * @param username
   * @param password
   */
  public static void registerHX(final HXRegisterSuccess context, final String username, final String password){
    new Thread(new Runnable() {
      @Override
      public void run() {
        //注册失败会抛出HyphenateException
        try {
          EMClient.getInstance().createAccount(password, username);//同步方法
          Trace.i("环信注册成功");
          if (context == null) return;
        } catch (HyphenateException e) {
          e.printStackTrace();
          Trace.i("环信注册失败");
        }
        context.isRegister();
      }
    }).start();
  }

  public interface HXRegisterSuccess{
    void isRegister();
  }

  /**
   * 检测是否登录环信
   */
  public static void verifyloginHX(Context mContext){
    verifyloginHX(mContext, null);
  }

  public static void verifyloginHX(Context mContext, HXRegisterSuccess context){
    UserInfo mUserInfo = UserProfileService.getInstance(mContext).getCurrentLoginedUser();
    if (!EMClient.getInstance().isLoggedInBefore()){
      registerHX(context,mUserInfo.user_id,mUserInfo.user_id);
    }else {
      context.isRegister();
    }
  }



}
