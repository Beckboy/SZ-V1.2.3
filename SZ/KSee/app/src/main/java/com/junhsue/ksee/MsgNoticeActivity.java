package com.junhsue.ksee;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.junhsue.ksee.adapter.MsgNoticeAdapter;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.common.MsgType;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.dto.MsgCenterReceiveReplyDTO;
import com.junhsue.ksee.entity.CommonResultEntity;
import com.junhsue.ksee.entity.MsgCenterReceiveReplyEntity;
import com.junhsue.ksee.net.api.OKHttpHomeImpl;
import com.junhsue.ksee.net.api.OkHttpILoginImpl;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.error.NetResultCode;
import com.junhsue.ksee.view.ActionBar;
import com.junhsue.ksee.view.CommonListView;

import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 系统消息
 * Created by longer on 17/11/15.
 */

public class MsgNoticeActivity extends BaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener {


    private ActionBar mActionBar;
    //
    //系统消息
    private CommonListView mLVNotices;
    //
    private PtrClassicFrameLayout mPtrClassic;
    private int mPage;
    private MsgNoticeAdapter<MsgCenterReceiveReplyEntity> mNoticeAdapter;
    //
    private Context mContext = this;
    //
    private LinearLayout mLLEmptyView;
    //没有更多信息时显示底部文案
    private LinearLayout mRL_Footer;

    @Override
    protected void onReceiveArguments(Bundle bundle) {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.act_msg_notice;
    }


    @Override
    protected void onInitilizeView() {
        mActionBar = (ActionBar) findViewById(R.id.action_bar);
        mLVNotices = (CommonListView) findViewById(R.id.lv_notices);
        //
        mLLEmptyView=(LinearLayout)findViewById(R.id.empty_view);
        mPtrClassic = (PtrClassicFrameLayout) findViewById(R.id.ptr_plassic_frameLayout);
        mRL_Footer=(LinearLayout) findViewById(R.id.rl_footer);
        //
        mPtrClassic.setMode(PtrFrameLayout.Mode.REFRESH);
        mPtrClassic.setPtrHandler(ptrDefaultHandler);
        mActionBar.setOnClickListener(this);
        //
        mActionBar.setTitleColor(R.color.c_gray_242E42);
        //
        mNoticeAdapter = new MsgNoticeAdapter<MsgCenterReceiveReplyEntity>(mContext);
        mLVNotices.setAdapter(mNoticeAdapter);
        mLVNotices.setOnItemClickListener(this);
        //
        getNotices();
        updateMsgStatus();
        //notifyMsgCount();
    }


    /**
     * 系统消息标记为已读
     */
    private void updateMsgStatus() {
        String typeId = String.valueOf(MsgType.MSG_SYSTEM_NOTICLE);
        //0 未读1 已读 2 删除
        String status = "1";
        OKHttpHomeImpl.getInstance().updateMsgStatus(typeId, status, new RequestCallback<CommonResultEntity>() {
            @Override
            public void onError(int errorCode, String errorMsg) {
                Trace.i("update status faile");

            }

            @Override
            public void onSuccess(CommonResultEntity response) {
                Trace.i("update status successful!!");
            }
        });
    }


    PtrDefaultHandler2 ptrDefaultHandler = new PtrDefaultHandler2() {

        @Override
        public void onLoadMoreBegin(PtrFrameLayout frame) {
            getNotices();
        }

        @Override
        public void onRefreshBegin(PtrFrameLayout frame) {
            //
            mPage = 3;
            getNotices();

        }
    };


    /**
     * 通知消息中心更新数量
     */
    private void notifyMsgCount() {
        Intent intent = new Intent(MsgActivity.BROAD_ACTION_MSG_UPDATE_COUNT);
        sendBroadcast(intent);
    }

    /**
     * 获取系统消息
     */
    private void getNotices() {
        //系统消m息
        String typeId = "19";
        final String pageSize = "10";
        OkHttpILoginImpl.getInstance().msgCenterReceiveReply(typeId, String.valueOf(mPage)
                , pageSize, new RequestCallback<MsgCenterReceiveReplyDTO>() {

                    @Override
                    public void onError(int errorCode, String errorMsg) {
                        mPtrClassic.refreshComplete();
                        if(mPage==0 && errorCode==NetResultCode.SERVER_NO_DATA){
                            mLLEmptyView.setVisibility(View.VISIBLE);
                            mRL_Footer.setVisibility(View.GONE);
                        }else if(mPage>0 && errorCode==NetResultCode.SERVER_NO_DATA){
                            mRL_Footer.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onSuccess(MsgCenterReceiveReplyDTO response) {
                        mPtrClassic.refreshComplete();
                        if(mPage==0 && response.result.size()==0){
                            mLLEmptyView.setVisibility(View.VISIBLE);
                            mRL_Footer.setVisibility(View.GONE);
                            return;
                        }else{
                            mLLEmptyView.setVisibility(View.GONE);
                        }
                        //
                        mNoticeAdapter.cleanList();
                        mNoticeAdapter.modifyList(response.result);
                        mPage++;
                        //
                        if (response.result.size() >= 10) {
                            mPtrClassic.setMode(PtrFrameLayout.Mode.BOTH);
                        }else{
                            mPtrClassic.setMode(PtrFrameLayout.Mode.REFRESH);
                            mRL_Footer.setVisibility(View.VISIBLE);
                        }
                        mPage++;
                    }
                });

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ll_left_layout:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        List<MsgCenterReceiveReplyEntity> lists=mNoticeAdapter.getList();
        MsgCenterReceiveReplyEntity msgCenterReceiveReplyEntity=lists.get(position);
        if(null==msgCenterReceiveReplyEntity.list)return;
        //如果是加精帖子消息就可以进行跳转
        if(msgCenterReceiveReplyEntity.list.bar_info_type_id==1){
            Bundle bundle=new Bundle();
            bundle.putString(Constants.POST_DETAIL_ID,msgCenterReceiveReplyEntity.list.post_bar_id);
            launchScreen(PostDetailActivity.class,bundle);
        }
    }
}
