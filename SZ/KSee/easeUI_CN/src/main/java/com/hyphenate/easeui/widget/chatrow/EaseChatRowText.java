package com.hyphenate.easeui.widget.chatrow;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.ChatType;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.utils.EaseSmileUtils;
import com.hyphenate.exceptions.HyphenateException;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.Spannable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

public class EaseChatRowText extends EaseChatRow{

	private TextView contentView;

    public EaseChatRowText(Context context, EMMessage message, int position, BaseAdapter adapter) {
		super(context, message, position, adapter);
	}

	@Override
	protected void onInflateView() {
		inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
				R.layout.ease_row_received_message : R.layout.ease_row_sent_message, this);
	}

  /**
   * 当PopupWindow显示或者消失时改变背景色
   */
  private WindowManager.LayoutParams lp;

	@Override
	protected void onFindViewById() {
		contentView = (TextView) findViewById(R.id.tv_chatcontent);
    contentView.setOnLongClickListener(new OnLongClickListener() {
      @Override
      public boolean onLongClick(final View v) {
        final Activity act = (Activity) context;
        lp = act.getWindow().getAttributes();
        //设置contentView
        View popView = LayoutInflater.from(context).inflate(R.layout.itemlongclick_popwindow,null);
        final PopupWindow mPopWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,true);
        mPopWindow.setContentView(popView);
        EaseCommonUtils.showPop(act,mPopWindow,v,lp);

        TextView copy = (TextView) popView.findViewById(R.id.tv_copy);
        TextView del = (TextView) popView.findViewById(R.id.tv_del);
        TextView revoke = (TextView) popView.findViewById(R.id.tv_revoke);
        View vRevoke = popView.findViewById(R.id.v_revoke);
        long st = System.currentTimeMillis();
        long mt = message.getMsgTime();
        final long time = st - mt;
        if (message.direct() == EMMessage.Direct.RECEIVE){
          revoke.setVisibility(GONE);
          vRevoke.setVisibility(GONE);
        }
        copy.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View vv) {
            EaseCommonUtils.clipboardManager(context, (TextView) v);
            mPopWindow.dismiss();
          }
        });
        del.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View vv) {
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
    public void onSetUpView() {
        EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
        Spannable span = EaseSmileUtils.getSmiledText(context, txtBody.getMessage());
        // 设置内容
        contentView.setText(span, BufferType.SPANNABLE);

        handleTextMessage();
    }

    protected void handleTextMessage() {
        if (message.direct() == EMMessage.Direct.SEND) {
            setMessageSendCallback();
            switch (message.status()) {
            case CREATE: 
                progressBar.setVisibility(View.GONE);
                statusView.setVisibility(View.VISIBLE);
                break;
            case SUCCESS:
                progressBar.setVisibility(View.GONE);
                statusView.setVisibility(View.GONE);
                break;
            case FAIL:
                progressBar.setVisibility(View.GONE);
                statusView.setVisibility(View.VISIBLE);
                break;
            case INPROGRESS:
                progressBar.setVisibility(View.VISIBLE);
                statusView.setVisibility(View.GONE);
                break;
            default:
               break;
            }
        }else{
            if(!message.isAcked() && message.getChatType() == ChatType.Chat){
                try {
                    EMClient.getInstance().chatManager().ackMessageRead(message.getFrom(), message.getMsgId());
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onUpdateView() {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onBubbleClick() {
        // TODO Auto-generated method stub
        
    }



}
