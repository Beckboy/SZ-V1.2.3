package com.junhsue.ksee;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.junhsue.ksee.adapter.ApprovalAndCollectAdapter;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.dto.MsgCenterReceiveReplyDTO;
import com.junhsue.ksee.entity.MsgCenterReceiveReplyEntity;
import com.junhsue.ksee.entity.MsgInfoEntity;
import com.junhsue.ksee.entity.ResultEntity;
import com.junhsue.ksee.fragment.dialog.CircleCommonDialog;
import com.junhsue.ksee.net.api.OkHttpILoginImpl;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.error.NetResultCode;
import com.junhsue.ksee.view.ActionBar;

import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 点赞和收藏的消息中心
 * Created by Sugar on 17/11/14.
 */

public class ApprovalAndCollectActivity extends BaseActivity implements View.OnClickListener {

    private ActionBar actionBar;
    private PtrClassicFrameLayout frameLayout;
    private ListView lvApprovalAndCollect;
    private ApprovalAndCollectAdapter approvalAndCollectAdapter;
    private RelativeLayout blankPage;
    private int currentPage;
    private Context mContext;
    private int pageSize = 8;
    private boolean isMaxPage;
    private boolean isAllRead = true;//是否全部已读


    @Override
    protected void onReceiveArguments(Bundle bundle) {
        isAllRead = bundle.getBoolean(Constants.POST_IS_ALL_READ);
    }

    @Override
    protected int setLayoutId() {
        mContext = this;
        return R.layout.act_approval_and_collect;
    }

    @Override
    protected void onInitilizeView() {

        actionBar = (ActionBar) findViewById(R.id.ab_approval_and_collect);
        frameLayout = (PtrClassicFrameLayout) findViewById(R.id.pcfl_approval_and_collect_layout);
        lvApprovalAndCollect = (ListView) findViewById(R.id.lv_approval_and_collect);

        actionBar.setBottomDividerVisible(View.GONE);

        approvalAndCollectAdapter = new ApprovalAndCollectAdapter(this);


        //加载回答列表空白占位图
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View blankView = inflater.inflate(R.layout.component_approval_and_collect_bank_page, null);
        blankPage = (RelativeLayout) blankView.findViewById(R.id.rl_approval_collect_bank_page);
        lvApprovalAndCollect.addFooterView(blankView);
        lvApprovalAndCollect.setAdapter(approvalAndCollectAdapter);
        setListener();

        approvalAndCollectAdapter.setMaxSize(0);
        loadApprovalAndCollectList(currentPage, pageSize);

    }


    @Override
    protected void onResume() {
        //初始化已读状态字体颜色样式
        actionBar.setRightTextStyle(isAllRead ? R.style.text_f_28_c95a3b1 : R.style.text_f_28_c_black_55626e);
        super.onResume();
    }

    private void setListener() {

        actionBar.setOnClickListener(this);
        frameLayout.setPtrHandler(ptrDefaultHandler2);
        lvApprovalAndCollect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > approvalAndCollectAdapter.getList().size()) {//点击尾部布局不跳转
                    return;
                }
                MsgCenterReceiveReplyEntity entity = (MsgCenterReceiveReplyEntity) parent.getItemAtPosition(position);
                setIsRead(entity.id + "", position);
                Intent intent = new Intent(mContext, PostDetailActivity.class);
                intent.putExtra(Constants.POST_DETAIL_ID, entity.list.post_bar_id);
                mContext.startActivity(intent);
                //
            }
        });


    }

    /**
     * 通知消息中心更新数量
     */
    private void notifyMsgCount() {
        Intent intent = new Intent(MsgActivity.BROAD_ACTION_MSG_UPDATE_COUNT);
        sendBroadcast(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_left_layout:
                finish();
                break;
            case R.id.tv_btn_right:
                setAllRead();
                break;
        }

    }


    /**
     * 刷新监听
     */
    PtrDefaultHandler2 ptrDefaultHandler2 = new PtrDefaultHandler2() {
        @Override
        public void onLoadMoreBegin(PtrFrameLayout frame) {
            if (isMaxPage) {
                Toast.makeText(mContext, getString(R.string.data_load_completed), Toast.LENGTH_SHORT).show();
                frameLayout.refreshComplete();//加载完毕
            } else {
                currentPage++;
                loadApprovalAndCollectList(currentPage, pageSize);
            }
        }

        @Override
        public void onRefreshBegin(PtrFrameLayout frame) {
            currentPage = 0;
            approvalAndCollectAdapter.setMaxSize(0);
            loadApprovalAndCollectList(currentPage, pageSize);
        }
    };


    /**
     * 加载数据
     *
     * @param currentPage
     * @param pageSize
     */
    private void loadApprovalAndCollectList(int currentPage, final int pageSize) {

        OkHttpILoginImpl.getInstance().msgCenterReceiveReply("16,17", currentPage + "", pageSize + "", new RequestCallback<MsgCenterReceiveReplyDTO>() {


            @Override
            public void onError(int errorCode, String errorMsg) {
                frameLayout.refreshComplete();
                if (approvalAndCollectAdapter.getList().size() <= 0) {
                    blankPage.setVisibility(View.VISIBLE);
                    isAllRead = true;

                } else {
                    blankPage.setVisibility(View.GONE);
                }
                dismissLoadingDialog();
            }

            @Override
            public void onSuccess(MsgCenterReceiveReplyDTO response) {

                if (null != response) {
                    refreshContentView(response);
                }
                dismissLoadingDialog();


                if (response.result.size() >= pageSize) {
                    frameLayout.setMode(PtrFrameLayout.Mode.BOTH);
                } else {
                    approvalAndCollectAdapter.setMaxSize(approvalAndCollectAdapter.getList().size() + 1);
                    frameLayout.setMode(PtrFrameLayout.Mode.REFRESH);
                }
            }
        });
    }

    /**
     * 刷新布局
     *
     * @param msgCenterReceiveReplyDTO
     */
    private void refreshContentView(MsgCenterReceiveReplyDTO msgCenterReceiveReplyDTO) {
        if (currentPage == 0) {
            approvalAndCollectAdapter.cleanList();
        }
        approvalAndCollectAdapter.modifyList(msgCenterReceiveReplyDTO.result);

        if (msgCenterReceiveReplyDTO.result.size() < pageSize) {
            isMaxPage = true;
        }
        if (approvalAndCollectAdapter.getList().size() <= 0) {
            blankPage.setVisibility(View.VISIBLE);

        } else {
            blankPage.setVisibility(View.GONE);
        }

        List<MsgCenterReceiveReplyEntity> msgList = approvalAndCollectAdapter.getList();
        for (int i = 0; i < approvalAndCollectAdapter.getList().size(); i++) {
            if (msgList.get(i).status != 1) {//没有已读过
                isAllRead = false;
                actionBar.setRightTextStyle(R.style.text_f_28_c_black_55626e);
                break;
            }
            isAllRead = true;
            actionBar.setRightTextStyle(R.style.text_f_28_c95a3b1);

        }

        frameLayout.refreshComplete();
    }


    /**
     * 一键置为已读
     */
    private void setAllRead() {
        List<MsgCenterReceiveReplyEntity> msgList = approvalAndCollectAdapter.getList();
        for (int i = 0; i < approvalAndCollectAdapter.getList().size(); i++) {
            if (msgList.get(i).status != 1) {//没有已读过
                isAllRead = false;
                break;
            }
        }
        if (isAllRead) {
            return;
        }
        OkHttpILoginImpl.getInstance().msgCenterReceiveReplyAllRead("16,17", "1", new RequestCallback<ResultEntity>() {
            @Override
            public void onError(int errorCode, String errorMsg) {
                switch (errorCode) {
                    case NetResultCode.SERVER_NO_DATA: //登录态错误，重新登录
                        showDialog("全部已读", R.drawable.icon_dialog_success);
                        actionBar.setRightTextStyle(R.style.text_f_28_c95a3b1);
                        isAllRead = true;
                        break;
                }
            }

            @Override
            public void onSuccess(ResultEntity response) {
                //
                notifyMsgCount();
                actionBar.setRightTextStyle(R.style.text_f_28_c95a3b1);
                actionBar.setRightText("全部已读");
                showDialog("全部已读", R.drawable.icon_dialog_success);
                List<MsgCenterReceiveReplyEntity> msgCenterReceiveReplyEntities = approvalAndCollectAdapter.getList();
                for (int i = 0; i < msgCenterReceiveReplyEntities.size(); i++) {
                    msgCenterReceiveReplyEntities.get(i).status = 1;
                }
                approvalAndCollectAdapter.notifyDataSetChanged();
                isAllRead = true;
                //
            }
        });
    }

    /**
     * 显示关注成功弹窗
     */
    private void showDialog(String msg, int drawableId) {
        MsgInfoEntity entity = new MsgInfoEntity();
        entity.drawableId = drawableId;
        entity.msgInfo = msg;
        CircleCommonDialog circleCommonDialog = CircleCommonDialog.newInstance(entity);
        circleCommonDialog.show(getSupportFragmentManager(), "");
    }


    /**
     * 置指定消息为已读
     *
     * @param post_bar_id
     * @param position
     */
    private void setIsRead(String post_bar_id, final int position) {
        OkHttpILoginImpl.getInstance().msgCenterReceiveReplyIsRead(post_bar_id, "1", new RequestCallback<ResultEntity>() {
            @Override
            public void onError(int errorCode, String errorMsg) {

            }

            @Override
            public void onSuccess(ResultEntity response) {
                MsgCenterReceiveReplyEntity entity = (MsgCenterReceiveReplyEntity) approvalAndCollectAdapter.getList().get(position);
                entity.status = 1;
                approvalAndCollectAdapter.notifyDataSetChanged();
                //
                notifyMsgCount();

            }
        });
    }
}
