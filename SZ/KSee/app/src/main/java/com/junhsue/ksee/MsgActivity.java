package com.junhsue.ksee;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bm.library.PhotoView;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.dto.MsgCountEntityDTO;
import com.junhsue.ksee.entity.MsgCountEntity;
import com.junhsue.ksee.fragment.KnowledgeFragment;
import com.junhsue.ksee.net.api.OKHttpHomeImpl;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.error.NetResultCode;
import com.junhsue.ksee.view.ActionBar;
import com.junhsue.ksee.view.CircleImageView;
import com.junhsue.ksee.view.MsgItemView;

/**
 * 消息中心
 * Created by longer on 17/11/14.
 */

public class MsgActivity extends BaseActivity implements View.OnClickListener {


    //
    private MsgItemView mMsgFavourite, mMsgReply;
    private CircleImageView mMsgNotice;
    private boolean approvalIsAllRead;
    private boolean replyIsAllRead;
    //
    private ActionBar mActionBar;
    //消息数量更新

    public static final String BROAD_ACTION_MSG_UPDATE_COUNT = "com.junhsue.ksee.msg_update_count";

    @Override
    protected void onReceiveArguments(Bundle bundle) {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.item_msg;
    }


    @Override
    protected void onInitilizeView() {
        mMsgFavourite = (MsgItemView) findViewById(R.id.item_msg_favourite);
        mMsgReply = (MsgItemView) findViewById(R.id.item_msg_reply);
        mMsgNotice = (CircleImageView) findViewById(R.id.img_notice);
        //
        //
        mActionBar = (ActionBar) findViewById(R.id.action_bar);

        //
        mActionBar.setOnClickListener(this);
        mMsgFavourite.setOnClickListener(this);
        mMsgReply.setOnClickListener(this);

        findViewById(R.id.ll_msg_system_notice).setOnClickListener(this);
        //
        getMsgCount();

    }


    /**
     * 获取消息数
     */
    private void getMsgCount() {

        OKHttpHomeImpl.getInstance().getMsgCount(new RequestCallback<MsgCountEntityDTO>() {

            @Override
            public void onError(int errorCode, String errorMsg) {
                if (errorCode == NetResultCode.SERVER_NO_DATA) {
                    Trace.i("msg count no data!!!");

                    mMsgFavourite.setMsgCount(0);
                    mMsgReply.setMsgCount(0);
                    approvalIsAllRead = true;
                    replyIsAllRead = true;
                    mMsgNotice.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onSuccess(MsgCountEntityDTO response) {
                Trace.i("msg count update successful!!!");
                MsgCountEntity msgCountEntity = response.result;
                if (null == msgCountEntity) {
                    return;
                }
                //设置消息数
                mMsgFavourite.setMsgCount((msgCountEntity.message_type_id_16 + msgCountEntity.message_type_id_17));
                mMsgReply.setMsgCount(msgCountEntity.message_type_id_18);

                if ((msgCountEntity.message_type_id_16 + msgCountEntity.message_type_id_17) > 0) {
                    approvalIsAllRead = false;
                } else {
                    approvalIsAllRead = true;
                }

                if (msgCountEntity.message_type_id_18 > 0) {
                    replyIsAllRead = false;
                } else {
                    replyIsAllRead = true;
                }

                if (msgCountEntity.message_type_id_19 == 0) {
                    mMsgNotice.setVisibility(View.GONE);
                } else if (msgCountEntity.message_type_id_19 > 0) {
                    mMsgNotice.setVisibility(View.VISIBLE);

                }
            }
        });

    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Trace.i("msg count update receive!!!");

            String action = intent.getAction();
            if (BROAD_ACTION_MSG_UPDATE_COUNT.equals(action)) {
                getMsgCount();
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BROAD_ACTION_MSG_UPDATE_COUNT);
        registerReceiver(broadcastReceiver, intentFilter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        setResult(KnowledgeFragment.RESULT_CODE_MSG);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_left_layout:
                setResult(KnowledgeFragment.RESULT_CODE_MSG);
                finish();
                break;
            //收到的赞和收藏
            case R.id.item_msg_favourite:
                Intent approvalIntent = new Intent(this, ApprovalAndCollectActivity.class);
                approvalIntent.putExtra(Constants.POST_IS_ALL_READ, approvalIsAllRead);
                startActivity(approvalIntent);

                break;
            //收到的回复
            case R.id.item_msg_reply:
                Intent replyIntent = new Intent(this, MsgReceiveReplyActivity.class);
                replyIntent.putExtra(Constants.POST_IS_ALL_READ, replyIsAllRead);
                startActivity(replyIntent);
                break;
            //消息通知
            case R.id.ll_msg_system_notice:
                mMsgNotice.setVisibility(View.INVISIBLE);
                launchScreen(MsgNoticeActivity.class);
                break;
        }
    }
}
