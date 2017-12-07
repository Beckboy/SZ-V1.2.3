package com.junhsue.ksee;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.dto.ClassGroupChatDetailsDTO;
import com.junhsue.ksee.dto.ClassRoomListDTO;
import com.junhsue.ksee.entity.ClassRoomListEntity;
import com.junhsue.ksee.entity.UserInfo;
import com.junhsue.ksee.net.api.OkHttpILoginImpl;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.profile.UserProfileService;
import com.junhsue.ksee.utils.HXUtils;
import com.junhsue.ksee.utils.ToastUtil;

public class ClassGroupChatActivity extends BaseActivity implements EaseChatFragment.JumpToGroupChatDetails,EaseChatFragment.SendMyMsgBroadcasd,HXUtils.HXRegisterSuccess{

  private ClassRoomListEntity groupDate;
  private UserInfo userInfo;

  private HXUtils.HXRegisterSuccess hxregistersuccess;
  private ClassGroupChatDetailsDTO persons = new ClassGroupChatDetailsDTO();

  public Handler handler = new Handler(new Handler.Callback() {

    @Override
    public boolean handleMessage(Message msg) {
      switch (msg.what){
        case 0:
          OkHttpILoginImpl.getInstance().colleageClassRoomDetails(groupDate.roomid, new RequestCallback<ClassGroupChatDetailsDTO>() {
            @Override
            public void onError(int errorCode, String errorMsg) {
              ToastUtil.getInstance(ClassGroupChatActivity.this).setContent(errorMsg).setShow();
            }
            @Override
            public void onSuccess(ClassGroupChatDetailsDTO response) {
              chatFragment.setGroupChatCount(response.memberlist.size());
              if (null != persons.memberlist)
                persons.memberlist.clear();
              persons.memberlist.addAll(response.memberlist);
            }
          });
          break;
      }
      return false;
    }
  });


  private EaseChatFragment chatFragment;


  @Override
  protected void onReceiveArguments(Bundle bundle) {
    groupDate = (ClassRoomListEntity) bundle.getSerializable("group");

    if (groupDate != null){
      Log.i("groupDate:",groupDate.toString());
    }
  }

  @Override
  protected int setLayoutId() {
    return R.layout.act_class_group_chat;
  }

  @Override
  protected void onInitilizeView() {
    chatFragment = new EaseChatFragment();
    userInfo = UserProfileService.getInstance(this).getCurrentLoginedUser();
    hxregistersuccess = this;

    if (!EMClient.getInstance().isLoggedInBefore()){
      HXLogin();
    }

    //传入参数
    Bundle args = new Bundle();
    args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_GROUP);
    args.putString(EaseConstant.EXTRA_GROUPCHAT_ID, groupDate.hx_roomid);
    args.putString(EaseConstant.EXTRA_GROUPCHAT_TITLE, groupDate.name);
    args.putString(EaseConstant.EXTRA_USER_AVATAR,userInfo.avatar);
    args.putString(EaseConstant.EXTRA_USER_NICKNAME,userInfo.nickname);
    chatFragment.setArguments(args);
    startFragment(chatFragment,R.id.frame_class_group_chat,false);

    cleanNoRead();
  }

  private void cleanNoRead() {
    EMConversation conversation = EMClient.getInstance().chatManager().getConversation(groupDate.hx_roomid);
    //指定会话消息未读数清零
    if (conversation == null) return;
    conversation.markAllMessagesAsRead();
    groupDate.is_read = true;
    //把一条消息置为已读
//    conversation.markMessageAsRead(messageId);
    //所有未读消息数清零
//    EMClient.getInstance().chatManager().markAllConversationsAsRead();

    sendBroad(EaseConstant.BROAD_GROUPCHAT_UPDATE_TYPE_READ,groupDate.hx_roomid,null);

  }

  /**
   * 未曾登录，动态登录
   */
  private void HXLogin() {
    EMClient.getInstance().login(userInfo.user_id,userInfo.user_id,new EMCallBack() {//回调
      @Override
      public void onSuccess() {
        Log.i("huanxin", "登录聊天服务器成功！");
        EMClient.getInstance().groupManager().loadAllGroups();
        EMClient.getInstance().chatManager().loadAllConversations();
      }

      @Override
      public void onProgress(int progress, String status) {}

      @Override
      public void onError(int code, String message) {
        Log.d("huanxin", "登录聊天服务器失败！");
        if (code == EMError.USER_NOT_LOGIN){
          HXUtils.registerHX(hxregistersuccess,userInfo.user_id,userInfo.user_id);
        }
      }
    });
  }

  public void sendBroad(int broadGroupchatUpdateType, String rood_id,String msg) {
    Intent intent = new Intent();
    Bundle bundle = new Bundle();
    bundle.putInt(EaseConstant.BROAD_GROUPCHAT_UPDATE_TYPE,broadGroupchatUpdateType);
    bundle.putString(EaseConstant.BROAD_GROUPCHAT_UPDATE_HXROOM_ID,rood_id);
    if (msg != null){
      bundle.putString(EaseConstant.BROAD_GROUPCHAT_UPDATE_CONTENT,msg);
    }
    intent.putExtras(bundle);
    intent.setAction(EaseConstant.BROAD_ACTION_GROUPCHAT_LIST_UPDATE);
    sendBroadcast(intent);
    Trace.i("发送广播更新未读消息");
  }

  @Override
  public void startActivity(int what, Bundle bundle) {
    switch (what){
      case 0:
        Intent intentToGroupDetails = new Intent(this,ClassGroupChatContactListActivity.class);
        bundle.putSerializable(EaseConstant.GROUPCHAT_USER_LIST,persons);
        intentToGroupDetails.putExtras(bundle);
        if (persons != null && persons.memberlist.size() != 0) {
          startActivity(intentToGroupDetails);
        }else {
          ToastUtil.getInstance(this).setContent("数据加载中...").setShow();
        }
        break;
      case 1:
        handler.sendEmptyMessage(0);
        break;

    }
  }


  /**
   * 接收更新列表消息的广播
   */
  BroadcastReceiver receiverImgLoading = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      String action=intent.getAction();
      Bundle bundle=intent.getExtras();
      if( null == bundle ) return;
      if( !EaseConstant.BROAD_ACTION_BIG_IMG_LOADING.equals(action)) return;
      String path = bundle.getString(EaseConstant.BROAD_BIG_IMG_LOADING);
      Trace.i("接收到更新:"+path);
//      ImageLoader.getInstance().loadImage(path, new ImageLoadingListener() {
//        @Override
//        public void onLoadingStarted(String s, View view) {
//        }
//
//        @Override
//        public void onLoadingFailed(String s, View view, FailReason failReason) {
//        }
//
//        @Override
//        public void onLoadingComplete(String s, View view, Bitmap bitmap) {
//          ToastUtil.getInstance(ClassGroupChatActivity.this).setContent("保存成功").setShow();
//        }
//
//        @Override
//        public void onLoadingCancelled(String s, View view) {
//        }
//      });
    }
  };

  @Override
  protected void onStart() {
    super.onStart();
    IntentFilter intentFilter=new IntentFilter();
    intentFilter.addAction(EaseConstant.BROAD_ACTION_BIG_IMG_LOADING);
    this.registerReceiver(receiverImgLoading,intentFilter);
    Trace.i("注册广播");
  }

  @Override
  protected void onDestroy() {
    cleanNoRead();
    this.unregisterReceiver(receiverImgLoading);
    Trace.i("销毁广播");
    super.onDestroy();
  }

  @Override
  public void sendbroad(String hx_roomId, String msg) {
    sendBroad(EaseConstant.BROAD_GROUPCHAT_UPDATE_TYPE_MSG,hx_roomId,msg);
  }

  @Override
  public void isRegister() {
    HXLogin();
  }
}
