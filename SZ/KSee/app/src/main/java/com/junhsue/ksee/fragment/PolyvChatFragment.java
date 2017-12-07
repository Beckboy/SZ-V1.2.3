package com.junhsue.ksee.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easefun.polyvsdk.live.adpater.PolyvChatAdapter;
import com.easefun.polyvsdk.live.adpater.PolyvEmoGridViewAdapter;
import com.easefun.polyvsdk.live.adpater.PolyvEmoPagerAdapter;
import com.easefun.polyvsdk.live.chat.PolyvChatManager;
import com.easefun.polyvsdk.live.chat.PolyvChatMessage;
import com.easefun.polyvsdk.live.util.PolyvFaceManager;
import com.junhsue.ksee.R;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.entity.CommonResultEntity;
import com.junhsue.ksee.entity.LiveEntity;
import com.junhsue.ksee.entity.UserInfo;
import com.junhsue.ksee.net.api.OKHttpCourseImpl;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.request.RequestCall;
import com.junhsue.ksee.profile.UserProfileService;
import com.junhsue.ksee.view.DivergeView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifEditText;
import pl.droidsonroids.gif.GifImageSpan;
import pl.droidsonroids.gif.RelativeImageSpan;

/**
 * 聊天Fragment
 */
public class PolyvChatFragment extends Fragment implements OnClickListener {

    private static final int DISCONNECT = 5;
    private static final int RECEIVEMESSAGE = 6;
    private static final int LOGINING = 12;
    private static final int LOGINSUCCESS = 13;
    private static final int RECONNECTING = 19;
    private static final int RECONNECTSUCCESS = 30;
    //刷新点赞数
    private static final  int MSG_CODE_FAVOUR=0x12342;
    // fragmentView
    private View view;
    // 聊天listView
    private ListView lv_chat;
    // 聊天listView的适配器
    private PolyvChatAdapter polyvChatAdapter;
    // 聊天信息列表集合
    private List<PolyvChatMessage> messages;

    // 空信息控件，聊天室状态
    private TextView tv_empty, tv_status;
    // 信息编辑控件
    private GifEditText et_talk;
    // danmuFragment 弹幕
    //private PolyvDanmuFragment danmuFragment;
    // 聊天管理类
    private PolyvChatManager chatManager;
    // 用户id，频道id，昵称(自定义)
    private String userId, roomId, nickName;
    private Animation collapseAnimation;

    // 表情ViewPager
    private ViewPager vp_emo;
    // 表情的父布局
    private RelativeLayout rl_bot;
    // 表情页的下方圆点...，表情开关，发送按钮
    private ImageView iv_page1, iv_page2, iv_page3, iv_page4, iv_page5, iv_emoswitch;
    private LinearLayout mLLSend;
    //点赞数
    private TextView mTxtFavourNumber;
    // 表情的文本长度
    private int emoLength;
    // 列
    private int columns = 7;
    // 行
    private int rows = 3;
    // 页
    private int pages = 5;
    // 是否是ppt直播，ppt直播无需操作聊天室实例
    private boolean isPPTLive;
    //
    private String mLiveId="2";
    private Context mContext=getActivity();
    private Timer mTimer;
    //直播点赞数
    private int mLiveFavorite;

    public void setIsPPTLive(boolean isPPTLive) {
        this.isPPTLive = isPPTLive;
    }

    //点赞效果控件
    private  DivergeView  mDivergeView;

    public static PolyvChatFragment newInstance(String liveId){
        PolyvChatFragment polyvChateFragment=new PolyvChatFragment();
        Bundle bundle=new Bundle();
        bundle.putString("live_id",liveId);
        polyvChateFragment.setArguments(bundle);
        return polyvChateFragment;
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RECEIVEMESSAGE:
                    PolyvChatMessage chatMessage = (PolyvChatMessage) msg.obj;
                    PolyvChatMessage.User userL = chatMessage.getUser();
                    if(null!=userL){
                        Log.i("chat", "recevie->" + userL.getNick());
                    }
                    if (chatMessage.getChatType() == PolyvChatMessage.CHATTYPE_RECEIVE) {
                        //danmuFragment.sendDanmaku(chatMessage.getValues()[0]);
                        //通知消息
                        polyvChatAdapter.add(chatMessage);
                    } else if (chatMessage.getChatType() == PolyvChatMessage.CHATTYPE_RECEIVE_NOTICE) {
                        switch (chatMessage.getEvent()) {
                            // 用户被踢，不能发送信息，再次连接聊天室可恢复
                            case PolyvChatMessage.EVENT_KICK:
                                String nick = chatMessage.getUser().getNick();
                                if (chatMessage.getUser().getUserId().equals(userId))
                                    nick = nick + "(自己)";
                                // 这里需要自定义显示的信息
                                chatMessage.setValues(new String[]{nick + "被踢"});
                                break;
                            // 用户被禁言，不能接收或发送信息，不能再次连接聊天室，需要在后台恢复
                            case PolyvChatMessage.EVENT_SHIELD:
                                String nick2 = chatMessage.getUser().getNick();
                                if (chatMessage.getUser().getUserId().equals(userId))
                                    nick2 = nick2 + "(自己)";
                                chatMessage.setValues(new String[]{nick2 + "被禁言"});
                                break;
                            // 聊天室关闭时，不能接收或发送信息
                            case PolyvChatMessage.EVENT_CLOSEROOM:
                                boolean isClose = chatMessage.getValue().isClosed();
                                if (isClose)
                                    chatMessage.setValues(new String[]{"聊天室关闭"});
                                else
                                    chatMessage.setValues(new String[]{"聊天室开启"});
                                break;
                            // 公告
                            case PolyvChatMessage.EVENT_GONGGAO:
                                chatMessage.setValues(new String[]{"公告 " + chatMessage.getContent()});
                                break;
                        }
                    }
                    break;
                case DISCONNECT:
                    // ((PolyvChatManager.ConnectStatus) msg.obj).getDescribe()的值与PolyvCahtManager的DISCONNECT_常量对应
                    //tv_status.setText("连接失败(" + ((PolyvChatManager.ConnectStatus) msg.obj).getDescribe() + ")");
                    //tv_status.setVisibility(View.VISIBLE);
                    break;
                case LOGINING:
                   // tv_status.setText("正在登录中...");
                    //tv_status.setVisibility(View.VISIBLE);
                    break;
                case LOGINSUCCESS:
                    //tv_status.setText("登录成功");
                    //tv_status.clearAnimation();
                    //tv_status.startAnimation(collapseAnimation);
                    //lv_chat.setVisibility(View.VISIBLE);
                    break;
                case RECONNECTING:
                    //tv_status.setText("正在重连中...");
                    //tv_status.setVisibility(View.VISIBLE);
                    break;
                case RECONNECTSUCCESS:
                    //tv_status.setText("重连成功");
                    //tv_status.clearAnimation();
                    //tv_status.startAnimation(collapseAnimation);
                    break;
                case MSG_CODE_FAVOUR:
                    liveApproval(mCount);
                    mCount=0;
                    break;
            }
        }
    };

    /**
     * ppt直播初始化聊天室配置信息
     *
     * @param chatManager
     */
    public void initPPTLiveChatConfig(PolyvChatManager chatManager, String userId, String roomId, String nickName) {
        this.userId = userId;
        this.roomId = roomId;
        this.nickName = nickName;
        this.chatManager = chatManager;
        this.chatManager.setOnChatManagerListener(new PolyvChatManager.ChatManagerListener() {

            @Override
            public void connectStatus(PolyvChatManager.ConnectStatus connect_status) {
                switch (connect_status) {
                    case DISCONNECT:
                        handler.sendMessage(handler.obtainMessage(DISCONNECT, connect_status.DISCONNECT));
                        break;
                    case LOGINING:
                        handler.sendEmptyMessage(LOGINING);
                        break;
                    case LOGINSUCCESS:
                        handler.sendEmptyMessage(LOGINSUCCESS);
                        break;
                    case RECONNECTING:
                        handler.sendEmptyMessage(RECONNECTING);
                        break;
                    case RECONNECTSUCCESS:
                        handler.sendEmptyMessage(RECONNECTSUCCESS);
                        break;
                }
            }

            @Override
            public void receiveChatMessage(PolyvChatMessage chatMessage) {
                Message message = handler.obtainMessage();
                message.obj = chatMessage;
                message.what = RECEIVEMESSAGE;
                handler.sendMessage(message);
            }
        });
    }

    private void getLiveDetails() {


        OKHttpCourseImpl.getInstance().getLiveDetails(mLiveId,
                new RequestCallback<LiveEntity>() {

                    @Override
                    public void onError(int errorCode, String errorMsg) {

                        Trace.i("live get approval fail");
                    }


                    @Override
                    public void onSuccess(LiveEntity response) {
                        Trace.i("live get approval success!");
                        mLiveFavorite=response.clicknumber;
                        mTxtFavourNumber.setText(String.valueOf(response.clicknumber));
                    }
                });

    }

    /** 记录本地的点赞数,*/
    private int mCount=0;

    //直播点赞
    private void  liveApproval(int count){
        OKHttpCourseImpl.getInstance().liveApproval(mLiveId,String.valueOf(count), new RequestCallback<CommonResultEntity>() {
            @Override
            public void onError(int errorCode, String errorMsg) {
                Trace.i("live approval fail!");
            }

            @Override
            public void onSuccess(CommonResultEntity response) {
                Trace.i("live approval success!");
                //mTxtFavourNumber.setText(String.valueOf(mLiveFavorite+1));
                getLiveDetails();
            }
        });
    }

    private void startTimer(){
        //TODO
        long currentTime=System.currentTimeMillis();
        mTimer=new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(MSG_CODE_FAVOUR);
            }
        },2000,10*1000l);
    }


    /**
     * 非ppt直播，初始化聊天室配置
     *
     * @param userId   用户id
     * @param roomId   房间id
     * @param nickName 昵称
     */
    public void initChatConfig(String userId, String roomId, String nickName) {
        initPPTLiveChatConfig(new PolyvChatManager(), userId, roomId, nickName);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.polyv_fragment_chat, null);
        Bundle bundle=getArguments();
        if(null!=bundle){
            mLiveId=bundle.getString("live_id");
        }
        //getLiveDetails();
        startTimer();
        return view;
    }

    //根据索引获取表情页的view
    private View getEmoGridView(int position) {
        GridView gv_emo = (GridView) LayoutInflater.from(getContext()).inflate(com.easefun.polyvsdk.live.R.layout.polyv_gridview_emo, null)
                .findViewById(com.easefun.polyvsdk.live.R.id.gv_emo);
        List<String> lists = new ArrayList<String>();
        lists.addAll(PolyvFaceManager.getInstance().getFaceMap().keySet());
        final List<String> elists = lists.subList(position * (columns * rows), (position + 1) * (columns * rows));
        PolyvEmoGridViewAdapter emoGridViewAdapter = new PolyvEmoGridViewAdapter(elists, getContext());
        gv_emo.setAdapter(emoGridViewAdapter);
        gv_emo.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (position == elists.size() - 1)
                    deleteEmoText();
                else
                    appendEmo(elists.get(position));
            }
        });
        return gv_emo;
    }

    private void findIdAndNew() {

        mDivergeView=(DivergeView)view.findViewById(R.id.divergeView);
        mTxtFavourNumber=(TextView)view.findViewById(R.id.txtFavourNumber);
        lv_chat = (ListView) view.findViewById(R.id.lv_chat);
        tv_empty = (TextView) view.findViewById(R.id.tv_empty);
        tv_status = (TextView) view.findViewById(R.id.tv_status);
        mLLSend = (LinearLayout) view.findViewById(R.id.ll_send);
        et_talk = (GifEditText) view.findViewById(R.id.et_talk);
        vp_emo = (ViewPager) view.findViewById(R.id.vp_emo);
        iv_page1 = (ImageView) view.findViewById(R.id.iv_page1);
        iv_page2 = (ImageView) view.findViewById(R.id.iv_page2);
        iv_page3 = (ImageView) view.findViewById(R.id.iv_page3);
        iv_page4 = (ImageView) view.findViewById(R.id.iv_page4);
        iv_page5 = (ImageView) view.findViewById(R.id.iv_page5);
        iv_emoswitch = (ImageView) view.findViewById(R.id.iv_emoswitch);
        rl_bot = (RelativeLayout) view.findViewById(R.id.rl_bot);
       // danmuFragment = (PolyvDanmuFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.fl_danmu);
        messages = new ArrayList<PolyvChatMessage>();
        collapseAnimation = AnimationUtils.loadAnimation(getContext(), com.easefun.polyvsdk.live.R.anim.polyv_collapse);
        collapseAnimation.setAnimationListener(new ViewAnimationListener(tv_status));
        view.findViewById(R.id.imgfire).setOnClickListener(this);
        //
    }

    private class ViewAnimationListener implements AnimationListener {
        private View view;

        public ViewAnimationListener(View view) {
            this.view = view;
        }

        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            view.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    }

    // 删除表情
    private void deleteEmoText() {
        int start = et_talk.getSelectionStart();
        int end = et_talk.getSelectionEnd();
        if (end > 0) {
            if (start != end) {
                et_talk.getText().delete(start, end);
            } else if (isEmo(end)) {
                et_talk.getText().delete(end - emoLength, end);
            } else {
                et_talk.getText().delete(end - 1, end);
            }
        }
    }

    //判断是否是表情
    private boolean isEmo(int end) {
        String preMsg = et_talk.getText().subSequence(0, end).toString();
        int regEnd = preMsg.lastIndexOf("]");
        int regStart = preMsg.lastIndexOf("[");
        if (regEnd == end - 1 && regEnd - regStart >= 2) {
            String regex = preMsg.substring(regStart);
            emoLength = regex.length();
            if (PolyvFaceManager.getInstance().getFaceId(regex) != -1)
                return true;
        }
        return false;
    }

    //添加表情
    private void appendEmo(String emoKey) {
        SpannableStringBuilder span = new SpannableStringBuilder(emoKey);
        int textSize = (int) et_talk.getTextSize();
        Drawable drawable = null;
        ImageSpan imageSpan = null;
        try {
            drawable = new GifDrawable(getResources(), PolyvFaceManager.getInstance().getFaceId(emoKey));
            imageSpan = new GifImageSpan(drawable, RelativeImageSpan.ALIGN_CENTER);
        } catch (NotFoundException | IOException e) {
            drawable = getResources().getDrawable(PolyvFaceManager.getInstance().getFaceId(emoKey));
            imageSpan = new RelativeImageSpan(drawable, RelativeImageSpan.ALIGN_CENTER);
        }
        drawable.setBounds(0, 0, textSize + 8, textSize + 8);
        span.setSpan(imageSpan, 0, span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        int selectionStart = et_talk.getSelectionStart();
        int selectionEnd = et_talk.getSelectionEnd();
        if (selectionStart != selectionEnd)
            et_talk.getText().replace(selectionStart, selectionEnd, span);
        else
            et_talk.getText().insert(selectionStart, span);
    }

    @SuppressWarnings("deprecation")
    private void initView() {
        // 登录聊天室
        if (!isPPTLive && chatManager != null)
            chatManager.login(userId, roomId, nickName);
        polyvChatAdapter = new PolyvChatAdapter(getContext(), messages, lv_chat);
        polyvChatAdapter.setChatManager(chatManager);
        //polyvChatAdapter.setDanmuFragment(danmuFragment);
        polyvChatAdapter.setOnItemClickListener(new PolyvChatAdapter.OnItemClickListener() {

            @Override
            public void onClick(View view) {
                closeKeybordAndEmo(et_talk, getContext());
            }
        });
        //lv_chat.setEmptyView(tv_empty);
        lv_chat.setAdapter(polyvChatAdapter);
        lv_chat.setSelection(polyvChatAdapter.getCount() - 1);
        lv_chat.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    closeKeybordAndEmo(et_talk, getContext());
                }
                return false;
            }
        });
        mLLSend.setOnClickListener(this);
        // 表情
        List<View> lists = new ArrayList<>();
        for (int i = 0; i < pages; i++) {
            lists.add(getEmoGridView(i));
        }
        PolyvEmoPagerAdapter emoPagerAdapter = new PolyvEmoPagerAdapter(lists, getContext());
        vp_emo.setAdapter(emoPagerAdapter);
        vp_emo.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                resetPageImageView();
                switch (arg0) {
                    case 0:
                        iv_page1.setSelected(true);
                        break;
                    case 1:
                        iv_page2.setSelected(true);
                        break;
                    case 2:
                        iv_page3.setSelected(true);
                        break;
                    case 3:
                        iv_page4.setSelected(true);
                        break;
                    case 4:
                        iv_page5.setSelected(true);
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        iv_page1.setSelected(true);
        iv_page1.setOnClickListener(this);
        iv_page2.setOnClickListener(this);
        iv_page3.setOnClickListener(this);
        iv_page4.setOnClickListener(this);
        iv_page5.setOnClickListener(this);
        iv_emoswitch.setOnClickListener(this);
        et_talk.setOnClickListener(this);
    }

    private void resetPageImageView() {
        iv_page1.setSelected(false);
        iv_page2.setSelected(false);
        iv_page3.setSelected(false);
        iv_page4.setSelected(false);
        iv_page5.setSelected(false);
    }

    /**
     * 重置表情布局的可见性
     *
     * @param isgone 以隐藏方式
     */
    public void resetEmoLayout(boolean isgone) {
        if (rl_bot.getVisibility() == View.VISIBLE || isgone) {
            rl_bot.setVisibility(View.GONE);
        } else {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            rl_bot.setVisibility(View.VISIBLE);
            closeKeybord(et_talk, getContext());
            et_talk.requestFocus();
        }
    }

    // 发送聊天信息
    private void sendMsg() {
        UserInfo userInfo= UserProfileService.getInstance(mContext).getCurrentLoginedUser();

        String msg = et_talk.getText().toString();
        if (msg.trim().length() == 0) {
            Toast.makeText(getContext(), "发送信息不能为空!", Toast.LENGTH_SHORT).show();
            return;
        }
         PolyvChatMessage message = new PolyvChatMessage(msg);
        message.setChatType(PolyvChatMessage.CHATTYPE_SEND);
        PolyvChatMessage.User user = new PolyvChatMessage.User("", "CHEN", "", "", "", "", "");
        user.setNick(userInfo.nickname);
        user.setPic(userInfo.avatar);
        user.setUid(userInfo.user_id);
        message.setUser(user);
        Log.i("chat", "send msg->" + user.toString());
        polyvChatAdapter.add(message);
        final int lastPosition = polyvChatAdapter.getCount() - 1;
        if (chatManager.sendChatMsg(message)) {
            //danmuFragment.sendDanmaku(message.getValues()[0]);
            polyvChatAdapter.updateStatusView(true, false, lastPosition);
        } else {
            polyvChatAdapter.updateStatusView(false, false, lastPosition);
        }
        lv_chat.setSelection(lastPosition);
        et_talk.setText("");
        closeKeybordAndEmo(et_talk, getContext());
    }

    //关闭键盘和表情布局
    private void closeKeybordAndEmo(EditText mEditText, Context mContext) {
        closeKeybord(mEditText, mContext);
        resetEmoLayout(true);
    }

    //关闭键盘
    private void closeKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    //表情布局是否可见
    public boolean emoLayoutIsVisible() {
        return rl_bot.getVisibility() == View.VISIBLE ? true : false;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findIdAndNew();
        initView();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            closeKeybordAndEmo(et_talk, getContext());
    }


    /**
     * 开启点赞动画
     */
    ArrayList<Bitmap> mList=new ArrayList<Bitmap>();
    private void startFavoritAnimation(){
        mList.add(((BitmapDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.icon_fire, null)).getBitmap());
        mDivergeView.post(
                new Runnable() {
                    @Override
                    public void run() {
                        mDivergeView.setEndPoint(new PointF(mDivergeView.getMeasuredWidth()/2,0));
                        mDivergeView.setDivergeViewProvider(new Provider());
                    }
                });
        mDivergeView.startDiverges(0);
    }



    class Provider implements DivergeView.DivergeViewProvider{

        @Override
        public Bitmap getBitmap(Object obj) {
            return mList == null ? null : mList.get((int)obj);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!isPPTLive && chatManager != null) {
            // 关闭聊天室
            chatManager.disconnect();
            chatManager.setOnChatManagerListener(null);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // 隐藏表情布局
        resetEmoLayout(true);
        // 清除焦点
        et_talk.clearFocus();
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_send:
                sendMsg();
                break;
            case  R.id.imgfire: //直播点赞
                mCount++;
                int txtCount=Integer.parseInt(mTxtFavourNumber.getText().toString());
                mTxtFavourNumber.setText(String.valueOf(txtCount+1));
                startFavoritAnimation();
                break;
            case R.id.iv_page1:
                vp_emo.setCurrentItem(0);
                break;
            case R.id.iv_page2:
                vp_emo.setCurrentItem(1);
                break;
            case R.id.iv_page3:
                vp_emo.setCurrentItem(2);
                break;
            case R.id.iv_page4:
                vp_emo.setCurrentItem(3);
                break;
            case R.id.iv_page5:
                vp_emo.setCurrentItem(4);
                break;
            case R.id.iv_emoswitch:
                resetEmoLayout(false);
                break;
            case R.id.et_talk:
                resetEmoLayout(true);
                break;
        }
    }
}
