package com.hyphenate.easeui.widget.chatrow;

import com.hyphenate.chat.EMFileMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.util.EMLog;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class EaseChatRowVoice extends EaseChatRowFile{

    private ImageView voiceImageView;
    private TextView voiceLengthView;
    private ImageView readStatusView;

    public EaseChatRowVoice(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {
        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                R.layout.ease_row_received_voice : R.layout.ease_row_sent_voice, this);
    }

    /**
     * 当PopupWindow显示或者消失时改变背景色
     */
    private WindowManager.LayoutParams lp;

    @Override
    protected void onFindViewById() {
        voiceImageView = ((ImageView) findViewById(R.id.iv_voice));
        voiceLengthView = (TextView) findViewById(R.id.tv_length);
        readStatusView = (ImageView) findViewById(R.id.iv_unread_voice);
        voiceImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBubbleClick();
            }
        });
        voiceImageView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                final Activity act = (Activity) context;
                lp = act.getWindow().getAttributes();
                //设置contentView
                View contentView = LayoutInflater.from(context).inflate(R.layout.itemlongclick_popwindow,null);
                final PopupWindow mPopWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,true);
                mPopWindow.setContentView(contentView);
                EaseCommonUtils.showPop(act,mPopWindow,v,lp);

                TextView copy = (TextView) contentView.findViewById(R.id.tv_copy);
                View view = contentView.findViewById(R.id.v_copy);
                TextView del = (TextView) contentView.findViewById(R.id.tv_del);
                TextView revoke = (TextView) contentView.findViewById(R.id.tv_revoke);
                View vRevoke = contentView.findViewById(R.id.v_revoke);
                long st = System.currentTimeMillis();
                long mt = message.getMsgTime();
                final long time = st - mt;
                if (message.direct() == EMMessage.Direct.RECEIVE ){
                    revoke.setVisibility(GONE);
                    vRevoke.setVisibility(GONE);
                }
                copy.setVisibility(GONE);
                view.setVisibility(GONE);
                del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View vv) {
                        Log.i("img","message:"+message.getBody().toString()+":"+message.getMsgId());
                        delCallback.delMsgid(message.getMsgId());
                        mPopWindow.dismiss();
                    }
                });
                revoke.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View vv) {
                        mPopWindow.dismiss();
                        if (time > 1000*60*2){
                            Toast.makeText(context,"超过2分钟不能撤回",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        delCallback.revokeMsgid(message.getMsgId(),message.getStringAttribute(EaseConstant.MESSAGE_ATTR_USER_NICKNAME,"匿名"));
                    }
                });
                return true;
            }
        });
    }

    @Override
    protected void onSetUpView() {
        EMVoiceMessageBody voiceBody = (EMVoiceMessageBody) message.getBody();
        int len = voiceBody.getLength();
        if(len>0){
            voiceLengthView.setText(voiceBody.getLength() + "\"");
            voiceLengthView.setVisibility(View.VISIBLE);
        }else{
            voiceLengthView.setVisibility(View.INVISIBLE);
        }
        if (EaseChatRowVoicePlayClickListener.playMsgId != null
                && EaseChatRowVoicePlayClickListener.playMsgId.equals(message.getMsgId()) && EaseChatRowVoicePlayClickListener.isPlaying) {
            AnimationDrawable voiceAnimation;
            if (message.direct() == EMMessage.Direct.RECEIVE) {
                voiceImageView.setImageResource(R.drawable.voice_from_icon);
            } else {
                voiceImageView.setImageResource(R.drawable.voice_to_icon);
            }
            voiceAnimation = (AnimationDrawable) voiceImageView.getDrawable();
            voiceAnimation.start();
        } else {
            if (message.direct() == EMMessage.Direct.RECEIVE) {
                voiceImageView.setImageResource(R.drawable.icon_others_play03);
            } else {
                voiceImageView.setImageResource(R.drawable.icon_self_play_normal);
            }
        }
        
        if (message.direct() == EMMessage.Direct.RECEIVE) {
            if (message.isListened()) {
                // hide the unread icon
                readStatusView.setVisibility(View.INVISIBLE);
            } else {
                readStatusView.setVisibility(View.VISIBLE);
            }
            EMLog.d(TAG, "it is receive msg");
            if (voiceBody.downloadStatus() == EMFileMessageBody.EMDownloadStatus.DOWNLOADING ||
                    voiceBody.downloadStatus() == EMFileMessageBody.EMDownloadStatus.PENDING) {
                progressBar.setVisibility(View.VISIBLE);
                setMessageReceiveCallback();
            } else {
                progressBar.setVisibility(View.INVISIBLE);

            }
            return;
        }

        // until here, handle sending voice message
        handleSendMessage();
    }

    @Override
    protected void onUpdateView() {
        super.onUpdateView();
    }

    @Override
    protected void onBubbleClick() {
        new EaseChatRowVoicePlayClickListener(message, voiceImageView, readStatusView, adapter, activity).onClick(bubbleLayout);
    }
    
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (EaseChatRowVoicePlayClickListener.currentPlayListener != null && EaseChatRowVoicePlayClickListener.isPlaying) {
            EaseChatRowVoicePlayClickListener.currentPlayListener.stopPlayVoice();
        }
    }
    
}
