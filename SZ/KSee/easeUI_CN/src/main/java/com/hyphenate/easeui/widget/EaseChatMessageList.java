package com.hyphenate.easeui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.adapter.EaseMessageAdapter;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;

public class EaseChatMessageList extends RelativeLayout implements EaseChatRow.DelCallback{
    
    protected static final String TAG = "EaseChatMessageList";
    protected ListView listView;
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected Context context;
    protected EaseChatRow.DelCallback delCallback;
    protected EMConversation conversation;
    protected int chatType;
    protected String toChatUsername;
    protected EaseMessageAdapter messageAdapter;
    protected boolean showUserNick;
    protected boolean showAvatar;
    protected Drawable myBubbleBg;
    protected Drawable otherBuddleBg;

	public EaseChatMessageList(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
    }

    public EaseChatMessageList(Context context, AttributeSet attrs) {
    	super(context, attrs);
    	parseStyle(context, attrs);
    	init(context);
    }

    public EaseChatMessageList(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context){
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.ease_chat_message_list, this);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.chat_swipe_layout);
        listView = (ListView) findViewById(R.id.list);
    }
    
    /**
     * init widget
     * @param toChatUsername
     * @param chatType
     * @param customChatRowProvider
     */
    public void init( String toChatUsername, int chatType, EaseCustomChatRowProvider customChatRowProvider) {
        delCallback = this;
        this.chatType = chatType;
        this.toChatUsername = toChatUsername;
        
        conversation = EMClient.getInstance().chatManager().getConversation(toChatUsername, EaseCommonUtils.getConversationType(chatType), true);
        messageAdapter = new EaseMessageAdapter(delCallback, context, toChatUsername, chatType, listView);
        messageAdapter.setShowAvatar(showAvatar);
        messageAdapter.setShowUserNick(showUserNick);
        messageAdapter.setMyBubbleBg(myBubbleBg);
        messageAdapter.setOtherBuddleBg(otherBuddleBg);
        messageAdapter.setCustomChatRowProvider(customChatRowProvider);
        // set message adapter
        listView.setAdapter(messageAdapter);
        
        refreshSelectLast();

    }
    
    protected void parseStyle(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.EaseChatMessageList);
        showAvatar = ta.getBoolean(R.styleable.EaseChatMessageList_msgListShowUserAvatar, true);
        myBubbleBg = ta.getDrawable(R.styleable.EaseChatMessageList_msgListMyBubbleBackground);
        otherBuddleBg = ta.getDrawable(R.styleable.EaseChatMessageList_msgListMyBubbleBackground);
        showUserNick = ta.getBoolean(R.styleable.EaseChatMessageList_msgListShowUserNick, false);
        ta.recycle();
    }
    
    
    /**
     * refresh
     */
    public void refresh(){
        if (messageAdapter != null) {
            messageAdapter.refresh();
        }
    }
    
    /**
     * refresh and jump to the last
     */
    public void refreshSelectLast(){
        if (messageAdapter != null) {
            messageAdapter.refreshSelectLast();
        }
    }
    
    /**
     * refresh and jump to the position
     * @param position
     */
    public void refreshSeekTo(int position){
        if (messageAdapter != null) {
            messageAdapter.refreshSeekTo(position);
        }
    }
    
	public ListView getListView() {
		return listView;
	} 

	public SwipeRefreshLayout getSwipeRefreshLayout(){
	    return swipeRefreshLayout;
	}
	
	public EMMessage getItem(int position){
	    return messageAdapter.getItem(position);
	}

	public void setShowUserNick(boolean showUserNick){
	    this.showUserNick = showUserNick;
	}
	
	public boolean isShowUserNick(){
	    return showUserNick;
	}

  /**
   * 删除某条信息的接口回调
   * @param msg_id
   */
  @Override
  public void delMsgid(String msg_id) {
    String msg;
    if (conversation.getLastMessage().getMsgId().equals(msg_id)){
      conversation.removeMessage(msg_id);
      messageAdapter.midefyDateSetChanged(conversation);
      if (conversation.getLastMessage() != null){
          String send_nickname = conversation.getLastMessage().getStringAttribute(EaseConstant.MESSAGE_ATTR_USER_NICKNAME,null);
          String massage = EaseCommonUtils.getMessageDigest(conversation.getLastMessage(), context);
          if (conversation.getLastMessage().direct() == EMMessage.Direct.RECEIVE){
            msg = send_nickname + "：" + massage;
          }else {
            msg = massage;
          }
          EaseChatFragment.SendMyMsgBroadcasd sendMyMsgBroadcasd = (EaseChatFragment.SendMyMsgBroadcasd) context;
          sendMyMsgBroadcasd.sendbroad(toChatUsername,msg);

      }
    }else {
      conversation.removeMessage(msg_id);
      messageAdapter.midefyDateSetChanged(conversation);
    }
  }

  /**
   * 撤回某条消息的接口
   * @param msg_id
   */
  @Override
  public void revokeMsgid(String msg_id, String nickName) {
    Toast.makeText(context,"撤回消息"+nickName,Toast.LENGTH_SHORT).show();
    EMMessage cmdMsg = EMMessage.createSendMessage(EMMessage.Type.CMD);
    // 如果是群聊，设置chattype，默认是单聊
    cmdMsg.setChatType(EMMessage.ChatType.GroupChat);
    String action="REVOKE_FLAG";
    EMCmdMessageBody cmdBody=new EMCmdMessageBody(action);
    // 设置消息body
    cmdMsg.addBody(cmdBody);
    // 设置要发给谁，用户username或者群聊groupid
    cmdMsg.setTo(toChatUsername);
    // 通过扩展字段添加要撤回消息的id
    cmdMsg.setAttribute("msgId",msg_id);
    cmdMsg.setAttribute(EaseConstant.MESSAGE_ATTR_USER_NICKNAME,nickName);
    EMClient.getInstance().chatManager().sendMessage(cmdMsg);
    delMsgid(msg_id);
  }


  public interface MessageListItemClickListener{
	    void onResendClick(EMMessage message);
	    /**
	     * there is default handling when bubble is clicked, if you want handle it, return true
	     * another way is you implement in onBubbleClick() of chat row
	     * @param message
	     * @return
	     */
	    boolean onBubbleClick(EMMessage message);
	    void onBubbleLongClick(EMMessage message);
	    void onUserAvatarClick(String username);
	    void onUserAvatarLongClick(String username);
	}
	
	/**
	 * set click listener
	 * @param listener
	 */
	public void setItemClickListener(MessageListItemClickListener listener){
        if (messageAdapter != null) {
            messageAdapter.setItemClickListener(listener);
        }
	}
	
	/**
	 * set chat row provider
	 * @param rowProvider
	 */
	public void setCustomChatRowProvider(EaseCustomChatRowProvider rowProvider){
        if (messageAdapter != null) {
            messageAdapter.setCustomChatRowProvider(rowProvider);
        }
    }
}
