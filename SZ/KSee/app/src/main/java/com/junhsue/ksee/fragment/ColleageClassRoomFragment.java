package com.junhsue.ksee.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.exceptions.HyphenateException;
import com.junhsue.ksee.BaseActivity;
import com.junhsue.ksee.ClassGroupChatActivity;
import com.junhsue.ksee.R;
import com.junhsue.ksee.adapter.MyClassRoomAdapter;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.dto.ClassRoomListDTO;
import com.junhsue.ksee.entity.ClassRoomListEntity;
import com.junhsue.ksee.entity.UserInfo;
import com.junhsue.ksee.net.api.OkHttpILoginImpl;
import com.junhsue.ksee.net.callback.BroadIntnetConnectListener;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.error.NetResultCode;
import com.junhsue.ksee.profile.UserProfileService;
import com.junhsue.ksee.utils.CommonUtils;
import com.junhsue.ksee.utils.HXUtils;
import com.junhsue.ksee.utils.PopWindowTokenErrorUtils;
import com.junhsue.ksee.utils.StringUtils;
import com.junhsue.ksee.utils.ToastUtil;
import com.junhsue.ksee.view.CircleImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 教室
 * Created by longer on 17/3/22.
 */
public class ColleageClassRoomFragment extends BaseFragment implements HXUtils.HXRegisterSuccess, BroadIntnetConnectListener.InternetChanged{

    //群消息列表更新广播
    public final static  String BROAD_ACTION_GROUPCHAT_LIST_UPDATE="com.junhuse.ksee.acion_groupchat_list_update";

    private HXUtils.HXRegisterSuccess hxregistersuccess;

    private static ColleageClassRoomFragment colleageClassRooomFragment;

    private PtrClassicFrameLayout mPtrFram;
    private ListView mLv;
    private CircleImageView mCircleNoData;
    private TextView mTvNoData;
    private MyClassRoomAdapter<ClassRoomListEntity> myClassRoomAdapter;
    private BaseActivity mContext;
    public Button btn_reloading;
    private View vHead;

    private ClassRoomListDTO classRoomListDTO = new ClassRoomListDTO();
    protected List<EMConversation> conversationList = new ArrayList<EMConversation>();
    private UserInfo mUserInfo;

    public static  ColleageClassRoomFragment newInstance(){
        if (colleageClassRooomFragment == null) {
            colleageClassRooomFragment = new ColleageClassRoomFragment();
        }
        return colleageClassRooomFragment;
    }

    private Handler handler = new Handler();

    @Override
    protected int setLayoutId() {
        return R.layout.act_colleage_classroom;
    }

    @Override
        protected void onInitilizeView(View view) {

        initView(view);

        mPtrFram.setPtrHandler(mPtrDefaultHandler2);
        myClassRoomAdapter = new MyClassRoomAdapter(mContext);

        mLv.setAdapter(myClassRoomAdapter);

        if (!CommonUtils.getIntnetConnect(mContext)){
            setNoNet();
        }else {
            btn_reloading.setVisibility(View.GONE);
            mContext.alertLoadingProgress();
            getData();
        }

        registerForContextMenu(mLv);

    }

  @Override
  public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
    MenuInflater menuInflater = mContext.getMenuInflater();
    menuInflater.inflate(R.menu.menu_groupchat_list, menu);
    super.onCreateContextMenu(menu, v, menuInfo);
  }

  @Override
  public boolean onContextItemSelected(MenuItem item) {
    AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
    ClassRoomListEntity classRoomListEntity = (ClassRoomListEntity) myClassRoomAdapter.getItem(menuInfo.position);
    switch (item.getItemId()){
//      case R.id.gc_settop:
//        ToastUtil.getInstance(mContext).setContent(classRoomListEntity+"："+item.getItemId()).setShow();
//        break;
      case R.id.gc_setread:
        classRoomListDTO.roomlist.get(menuInfo.position).is_read = true;
        myClassRoomAdapter.cleanList();
        myClassRoomAdapter.modifyList(classRoomListDTO.roomlist);
        break;
      case R.id.gc_cleaninfo:
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(classRoomListEntity.hx_roomid);
        if (null != conversation) {
          EMClient.getInstance().chatManager().getConversation(classRoomListEntity.hx_roomid).clearAllMessages();
        }
        classRoomListDTO.roomlist.get(menuInfo.position).hx_message = "";
        classRoomListDTO.roomlist.get(menuInfo.position).hx_lastmsg_time = 0;
        classRoomListDTO.roomlist.get(menuInfo.position).is_read = true;
        myClassRoomAdapter.cleanList();
        myClassRoomAdapter.modifyList(classRoomListDTO.roomlist);
        break;
    }
    return super.onContextItemSelected(item);
  }

  private void initView(View view) {

        hxregistersuccess = this;
        mUserInfo = UserProfileService.getInstance(mContext).getCurrentLoginedUser();

        mPtrFram = (PtrClassicFrameLayout) view.findViewById(R.id.ptrClassicFrameLayout_colleage_classroom_list);
        mLv = (ListView) view.findViewById(R.id.lv_colleage_classroom_list);
        vHead = View.inflate(mContext,R.layout.item_myanswer_head,null);
        vHead.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));
        mLv.addHeaderView(vHead);
        mCircleNoData = (CircleImageView) mLv.findViewById(R.id.img_answer_nodata);
        mTvNoData = (TextView) mLv.findViewById(R.id.tv_answer_nodata);
        btn_reloading = (Button) mLv.findViewById(R.id.btn_answer_reloading);
        btn_reloading.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              setDataReset();
          }
      });

      setNoData(View.GONE);
        mLv.setOnItemClickListener(itemClickListener);
        if (null != mContext.con){
            mContext.con.setInternetChanged(this);
        }
    }

    EMMessageListener msgListener = new EMMessageListener() {


        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            Log.i("getMSG:","收到消息"+messages.toString()+"："+messages.get(messages.size()-1).getMsgId());
            changeMsg(messages);
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //收到透传消息
            String msg_id = messages.get(messages.size()-1).getStringAttribute("msgId","0");
            Log.i("getMSG:","收到透传消息"+msg_id);
            getCMDmsg(messages);
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
            //收到已读回执
            Log.i("getMSG:","收到已读回执");
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
            //收到已送达回执
            Log.i("getMSG:","收到已送达回执");
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            //消息状态变动
            Log.i("getMSG:","消息状态变动");
        }
    };

  /**
   * 收到透传消息
   * @param messages
   */
  private void getCMDmsg(List<EMMessage> messages) {
      for (int i = 0;i < messages.size(); i++){
          for (int j = 0; j < classRoomListDTO.roomlist.size(); j++) {
              if (messages.get(i).getTo().
                  equals(classRoomListDTO.roomlist.get(j).hx_roomid)) {

                  EMMessage message = messages.get(i);
                  EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
                  String action = cmdMsgBody.action();//获取自定义action
                  if(action.equals("REVOKE_FLAG")){
                      String nameFrom = message.getStringAttribute(EaseConstant.MESSAGE_ATTR_USER_NICKNAME,"XXX");
    //                          --删除消息来表示撤回--
                      classRoomListDTO.roomlist.get(j).hx_lastmsg_time = message.getMsgTime();
                      classRoomListDTO.roomlist.get(j).hx_message = "\""+nameFrom+"\"" + "撤回一条消息";
                      EMClient.getInstance().chatManager().getConversation(message.getTo()).removeMessage(message.getStringAttribute("msgId","0"));
                      mContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                          myClassRoomAdapter.cleanList();
                          myClassRoomAdapter.modifyList(classRoomListDTO.roomlist);
                          return;

                        }
                      });
                  }

              }
          }
      }
    }


    /**
   * 收到消息
   * @param messages
   */
  private void changeMsg(List<EMMessage> messages) {
        for (int i = 0;i < messages.size(); i++) {
            for (int j = 0; j < classRoomListDTO.roomlist.size(); j++) {
                if (messages.get(i).getTo().
                    equals(classRoomListDTO.roomlist.get(j).hx_roomid)) {
                    Log.i("getHX","就是这个群组:"+classRoomListDTO.roomlist.get(i).name);
                    int noread_count = EMClient.getInstance().chatManager().getConversation(messages.get(i).getTo()).getUnreadMsgCount();
                    String send_nickname = messages.get(i).getStringAttribute(EaseConstant.MESSAGE_ATTR_USER_NICKNAME, null);
                    String noreadmsg = "";
                    if (noread_count > 0) {
                        noreadmsg = "[" + noread_count + "条] ";
                    }
                    if (classRoomListDTO.roomlist.get(j).hx_message.substring(0,6).equals("[有人@我]")){
                        noreadmsg = "[有人@我]";
                    }
                    try {
                        if ( null != messages.get(i).getStringAttribute(mUserInfo.user_id)){
                            noreadmsg = "[" + messages.get(i).getStringAttribute(mUserInfo.user_id,"[@]") +"]";
                            Log.i("mmmm",mUserInfo.user_id);
                        }
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                    classRoomListDTO.roomlist.get(j).hx_message = noreadmsg + send_nickname + "：" + HXUtils.getMessageDigest(messages.get(i), mContext);
                    classRoomListDTO.roomlist.get(j).hx_lastmsg_time = messages.get(i).getMsgTime();
                    if (noread_count > 0) {
                        classRoomListDTO.roomlist.get(j).is_read = false;
                    } else {
                        classRoomListDTO.roomlist.get(j).is_read = true;
                    }
                    classRoomListDTO.roomlist.add(0, classRoomListDTO.roomlist.get(j));
                    classRoomListDTO.roomlist.remove(j + 1);
                    mContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            myClassRoomAdapter.cleanList();
                            myClassRoomAdapter.modifyList(classRoomListDTO.roomlist);
                            return;

                        }
                    });
                }
            }
        }
    }


    /**
     * load conversation list
     * @return
     */
    protected List<EMConversation> loadConversationList(){
        // get all conversations
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        /**
         * lastMsgTime will change if there is new message during sorting
         * so use synchronized to make sure timestamp of last message won't change.
         */
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        return list;
    }

    /**
     * sort conversations according time stamp of last message
     * @param conversationList
     */
    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {
                if (con1.first.equals(con2.first)) {
                    return 0;
                } else if (con2.first.longValue() > con1.first.longValue()) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }


    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position>0){
                position -= 1;
            }else {
                return;
            }
            ClassRoomListEntity classRoomListEntity = (ClassRoomListEntity) myClassRoomAdapter.getItem(position);
            Intent intentToGropChat = new Intent(mContext, ClassGroupChatActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("group", classRoomListEntity);
            intentToGropChat.putExtras(bundle);
            startActivity(intentToGropChat);
        }
    };


    PtrDefaultHandler2 mPtrDefaultHandler2 = new PtrDefaultHandler2() {
        @Override
        public void onLoadMoreBegin(PtrFrameLayout frame) {
            if (!CommonUtils.getIntnetConnect(mContext)){
                setNoNet();
                return;
            }
            btn_reloading.setVisibility(View.GONE);
            getData();
        }

        @Override
        public void onRefreshBegin(PtrFrameLayout frame) {
            if (!CommonUtils.getIntnetConnect(mContext)){
                setNoNet();
                return;
            }
            btn_reloading.setVisibility(View.GONE);
            getData();
        }
    };

  /**
   * 对列表中的消息数列进行时间排序
   * @param classRoomListDTO
   * @return
   */
  private ClassRoomListDTO sortByLastMSGTime(ClassRoomListDTO classRoomListDTO){
    //对群组根据最新消息的时间进行排序
    for (int i = classRoomListDTO.roomlist.size()-1;i >=0;i--){
        for (int j = i - 1; j >= 0; j--) {
          long firstDTO_time = classRoomListDTO.roomlist.get(j).hx_lastmsg_time;
          long nextDTO_time = classRoomListDTO.roomlist.get(i).hx_lastmsg_time;
          if (nextDTO_time == 0){
            classRoomListDTO.roomlist.add(classRoomListDTO.roomlist.size(),classRoomListDTO.roomlist.get(i));
            classRoomListDTO.roomlist.remove(i);
            continue;
          }
          if (firstDTO_time < nextDTO_time) {
            classRoomListDTO.roomlist.add(j, classRoomListDTO.roomlist.get(i));
            classRoomListDTO.roomlist.add(i+1, classRoomListDTO.roomlist.get(j+1));
            classRoomListDTO.roomlist.remove(j + 1);
            classRoomListDTO.roomlist.remove(i + 1);
          }
        }
    }
    return classRoomListDTO;
  }

    /**
     * 获取被邀请的条目数据
     */
    private void getData() {
        String token = UserProfileService.getInstance(mContext).getCurrentLoginedUser().token;
        OkHttpILoginImpl.getInstance().colleageClassRoomList(token, new RequestCallback<ClassRoomListDTO>() {
            @Override
            public void onError(int errorCode, String errorMsg) {
                mPtrFram.refreshComplete();
                mContext.dismissLoadingDialog();
                switch (errorCode){
                    case NetResultCode.CODE_LOGIN_STATE_ERROR: //登录态错误，重新登录
                        PopWindowTokenErrorUtils.getInstance(mContext).showPopupWindow(R.layout.act_main);
                        break;
                    case NetResultCode.SERVER_NO_DATA:
                        setNoData(View.VISIBLE);
                        myClassRoomAdapter.cleanList();
                        myClassRoomAdapter.modifyList(null);
                        break;
                    default:
                        ToastUtil.getInstance(mContext).setContent(errorMsg).setShow();
                        break;
                }
                switch (errorCode){
                    case NetResultCode.CODE_LOGIN_STATE_ERROR: //登录态错误，重新登录
                        PopWindowTokenErrorUtils.getInstance(mContext).showPopupWindow(R.layout.act_editor_choose);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onSuccess(ClassRoomListDTO response) {
                mPtrFram.refreshComplete();
                mContext.dismissLoadingDialog();
                setNoData(View.GONE);
                if (null == response || response.roomlist.size() == 0) {
                    setNoData(View.VISIBLE);
                    return;
                }
                classRoomListDTO = sortByLastMSGTime(response);
                myClassRoomAdapter.cleanList();
                myClassRoomAdapter.modifyList(classRoomListDTO.roomlist);
                mPtrFram.setMode(PtrFrameLayout.Mode.BOTH);
                VotifyHXLogin(classRoomListDTO);
            }
        });
    }

    /**
     * 无网络的状态
     */
    private void setNoNet() {
        vHead.setVisibility(View.VISIBLE);
        mLv.setHeaderDividersEnabled(false);
        mCircleNoData.setImageResource(R.drawable.common_def_nonet);
        mTvNoData.setText("网络加载出状况了");
        mCircleNoData.setVisibility(View.VISIBLE);
        mTvNoData.setVisibility(View.VISIBLE);
        ToastUtil.getInstance(mContext).setContent(Constants.INTNET_NOT_CONNCATION).setDuration(Toast.LENGTH_SHORT).setShow();

        btn_reloading.setVisibility(View.VISIBLE);
        myClassRoomAdapter.cleanList();
        myClassRoomAdapter.modifyList(null);
        mPtrFram.refreshComplete();
    }

    /**
     * 数据重新刷新
     */
    public void setDataReset(){
        if (!CommonUtils.getIntnetConnect(mContext)){
            setNoNet();
            return;
        }
        btn_reloading.setVisibility(View.GONE);
        getData();
    }

    private void setNoData(int visibility) {
        vHead.setVisibility(visibility);
        mCircleNoData.setImageResource(R.drawable.shu_def_class);
        mTvNoData.setText("你还没有参加教室哦");
        mCircleNoData.setVisibility(visibility);
        mTvNoData.setVisibility(visibility);
    }


    /**
      * 登录环信
      */
    private void VotifyHXLogin(ClassRoomListDTO response) {
        if (!EMClient.getInstance().isLoggedInBefore()){
            HXLogin(response);
        }else {
            Trace.i("环信自动登录成功");
            updateMsgByHX(response);
        }
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }

  /**
   * 未曾登录，动态登录
   * @param response
   */
  private void HXLogin(final ClassRoomListDTO response) {
        EMClient.getInstance().login(mUserInfo.user_id,mUserInfo.user_id,new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                Log.i("huanxin", "登录聊天服务器成功！");
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                EMClient.getInstance().chatManager().addMessageListener(msgListener);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        updateMsgByHX(response);
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {}

            @Override
            public void onError(int code, String message) {
                Log.d("huanxin", "登录聊天服务器失败！");
                if (code == EMError.USER_NOT_LOGIN){
                    HXUtils.registerHX(hxregistersuccess,mUserInfo.user_id,mUserInfo.user_id);
                }
            }
        });
    }

    /**
     * 完成环信登录
     * 更新列表中的数据信息
     * @param response
     */
    private void updateMsgByHX(ClassRoomListDTO response) {
        conversationList.clear();
        conversationList.addAll(loadConversationList());
        for (int i = 0; i < response.roomlist.size(); i++) {
            for (int j = 0; j < conversationList.size(); j++) {
                if (response.roomlist.get(i).hx_roomid.equals(conversationList.get(j).conversationId())) {
                    response.roomlist.get(i).hx_lastmsg_time = conversationList.get(j).getLastMessage().getMsgTime();
                    int noreadcount = conversationList.get(j).getUnreadMsgCount();
                    String send_name = conversationList.get(j).getLastMessage().getStringAttribute(EaseConstant.MESSAGE_ATTR_USER_NICKNAME,"匿名");
                    //设置未读数
                    if (noreadcount > 0){
                        String name = send_name;
                        if (noreadcount > 99)
                            send_name = "[99+条] "+name+"：";
                        else
                            send_name = "["+noreadcount+"条] "+name+"：";
                        int i1 = 0;
                        for (int k = 0; k < conversationList.get(j).getAllMessages().size();k++){
                            if (i1 == noreadcount) break;
                            if (conversationList.get(j).getAllMessages().get(conversationList.get(j).getAllMessages().size()-k-1).direct() != EMMessage.Direct.RECEIVE) continue;
                            i1++;
                            try {
                                if ( null != conversationList.get(j).getAllMessages().get(conversationList.get(j).getAllMessages().size()-k-1).getStringAttribute(mUserInfo.user_id)){
                                    send_name = "[" + conversationList.get(j).getAllMessages().get(conversationList.get(j).getAllMessages().size()-k-1).getStringAttribute(mUserInfo.user_id,"[@]") +"]"+name+"：";
                                }
                            } catch (HyphenateException e) {
                                e.printStackTrace();
                            }
                        }
                    }else {
                        send_name = send_name+"：";
                    }
                    //设置姓名
                    if (send_name.contains(mUserInfo.nickname)){ //自己发送时
                        send_name = "";
                        noreadcount = 0;
                    }
                    //设置群组信息是否已被阅读
                    if (noreadcount>0) {
                        response.roomlist.get(i).is_read = false; //未读
                    } else {
                        response.roomlist.get(i).is_read = true; //已读
                    }
                    response.roomlist.get(i).hx_message = send_name + HXUtils.getMessageDigest(conversationList.get(j).getLastMessage(), mContext);
                    Log.i("hxMessage:", response.roomlist.get(i).hx_message.toString());
                    break;
                }
            }
        }
        Trace.i("hxMessage:进入排序");
        classRoomListDTO = sortByLastMSGTime(response);
        myClassRoomAdapter.cleanList();
        myClassRoomAdapter.modifyList(classRoomListDTO.roomlist);
    }

    /**
     * 完成更新列表信息的广播接收
     * 更新列表中的数据信息
     * @param hx_roomid
     */
    private void updateMsgByBroad(String hx_roomid) {
        for (int i = 0; i < classRoomListDTO.roomlist.size();i++){

            if (!classRoomListDTO.roomlist.get(i).hx_roomid.equals(hx_roomid)) continue;

            if (classRoomListDTO.roomlist.get(i).is_read == true) return;
            classRoomListDTO.roomlist.get(i).is_read = true;
            String message = classRoomListDTO.roomlist.get(i).hx_message;
            classRoomListDTO.roomlist.get(i).hx_message = StringUtils.StrSplit(message,"]");
            myClassRoomAdapter.cleanList();
            myClassRoomAdapter.modifyList(classRoomListDTO.roomlist);
            Trace.i("更新未读消息");
            break;
        }
    }

  /**
   * 完成更新列表信息的广播接收
   * 更新列表中的数据信息
   * @param hx_roomid
   */
  private void updateMsgByBroad(String hx_roomid, String msg) {
        for (int i = 0; i < classRoomListDTO.roomlist.size();i++){

            if (!classRoomListDTO.roomlist.get(i).hx_roomid.equals(hx_roomid)) continue;

            classRoomListDTO.roomlist.get(i).is_read = true;
            classRoomListDTO.roomlist.get(i).hx_message = msg;
            classRoomListDTO.roomlist.get(i).hx_lastmsg_time = System.currentTimeMillis();

            classRoomListDTO.roomlist.add(0,classRoomListDTO.roomlist.get(i));
            classRoomListDTO.roomlist.remove(i+1);

            myClassRoomAdapter.cleanList();
            myClassRoomAdapter.modifyList(classRoomListDTO.roomlist);
            Trace.i("更新未读消息");
            break;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = (BaseActivity) activity;
    }


    /**
     * 环信注册成功的接口回调
     */
    @Override
    public void isRegister() {
        HXLogin(classRoomListDTO);
    }


    /**
    * 接收更新列表消息的广播
    */
    BroadcastReceiver receiverInfoBeRead=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Trace.i("接收到更新未读消息");
            String action=intent.getAction();
            Bundle bundle=intent.getExtras();
            if( null == bundle ) return;
            if( !BROAD_ACTION_GROUPCHAT_LIST_UPDATE.equals(action)) return;

            String hx_roomid = bundle.getString(EaseConstant.BROAD_GROUPCHAT_UPDATE_HXROOM_ID);
            //更新群组信息已被读取的广播
            if (bundle.getInt(EaseConstant.BROAD_GROUPCHAT_UPDATE_TYPE) == EaseConstant.BROAD_GROUPCHAT_UPDATE_TYPE_READ){ //消息为更新已读状态
                updateMsgByBroad(hx_roomid);
            }
            //更新最新聊天记录的广播
            else if (bundle.getInt(EaseConstant.BROAD_GROUPCHAT_UPDATE_TYPE) == EaseConstant.BROAD_GROUPCHAT_UPDATE_TYPE_MSG){ //消息为更新最新消息
                String message = bundle.getString(EaseConstant.BROAD_GROUPCHAT_UPDATE_CONTENT,"");
                updateMsgByBroad(hx_roomid,message);
            }
        }
    };


    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(BROAD_ACTION_GROUPCHAT_LIST_UPDATE);
        mContext.registerReceiver(receiverInfoBeRead,intentFilter);
        Trace.i("注册广播");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
        mContext.unregisterReceiver(receiverInfoBeRead);
        Trace.i("销毁广播");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getData();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onNetChange(boolean netConnection) {
        if (netConnection){
            if (null == myClassRoomAdapter.getList()|| myClassRoomAdapter.getList().size() == 0) {
                setDataReset();
            }
        }
    }
}
