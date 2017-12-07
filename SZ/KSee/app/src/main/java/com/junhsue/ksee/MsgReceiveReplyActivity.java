package com.junhsue.ksee;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.junhsue.ksee.adapter.MsgReceiveReplyAdapter;
import com.junhsue.ksee.adapter.MyCoinListAdapter;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.dto.MsgCenterReceiveReplyDTO;
import com.junhsue.ksee.entity.Course;
import com.junhsue.ksee.entity.MsgCenterReceiveReplyEntity;
import com.junhsue.ksee.entity.MsgInfoEntity;
import com.junhsue.ksee.entity.MyCoinListEntity;
import com.junhsue.ksee.entity.PostDetailEntity;
import com.junhsue.ksee.entity.ResultEntity;
import com.junhsue.ksee.fragment.dialog.CircleCommonDialog;
import com.junhsue.ksee.net.api.OkHttpILoginImpl;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.error.NetResultCode;
import com.junhsue.ksee.utils.CommonUtils;
import com.junhsue.ksee.utils.PopWindowTokenErrorUtils;
import com.junhsue.ksee.utils.ToastUtil;
import com.junhsue.ksee.view.ActionBar;
import com.junhsue.ksee.view.CircleImageView;

import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class MsgReceiveReplyActivity extends BaseActivity implements View.OnClickListener {

    private MsgReceiveReplyActivity mContext;

    private ActionBar mAbar;
    private PtrClassicFrameLayout mPtrFram;
    private ListView mLv;

    //数据异常的占位图
    private CircleImageView mCircleNoData;
    private TextView mTvNoData;
    public Button btn_reloading;
    private View vHead;

    private boolean isAllRead = true;//是否全部已读

    private int page = 0;
    private int pageSize = 8;
    private MsgReceiveReplyAdapter<MsgCenterReceiveReplyEntity> myCoinListAdapter;


    @Override
    protected void onReceiveArguments(Bundle bundle) {

        isAllRead = bundle.getBoolean(Constants.POST_IS_ALL_READ);

    }

    @Override
    protected int setLayoutId() {
        return R.layout.act_msg_receive_reply;
    }

    @Override
    protected void onInitilizeView() {

        mContext = this;
        initView();

        mPtrFram.setPtrHandler(mPtrDefaultHandler2);

        myCoinListAdapter = new MsgReceiveReplyAdapter<>(this);

        if (!CommonUtils.getIntnetConnect(mContext)) {
            setNoNet();
        } else {
            btn_reloading.setVisibility(View.GONE);
        }

        mLv.setAdapter(myCoinListAdapter);
        mLv.setOnItemClickListener(replyItemClickListener);

        getData();

    }

    @Override
    protected void onResume() {
        //初始化已读状态字体颜色样式
        mAbar.setRightTextStyle(isAllRead?R.style.text_f_28_c95a3b1:R.style.text_f_28_c_black_55626e);
        super.onResume();
    }

    private void initView() {
        mAbar = (ActionBar) findViewById(R.id.ab_receive_reply_list);
        mAbar.setOnClickListener(this);
        mPtrFram = (PtrClassicFrameLayout) findViewById(R.id.ptrClassicFrameLayout_receive_reply_list);
        mLv = (ListView) findViewById(R.id.lv_receive_reply_list);

        //数据异常的占位图
        vHead = View.inflate(mContext, R.layout.item_myanswer_head, null);
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
    }


    /**
     * 获取被邀请的条目数据
     */
    private void getData() {
        OkHttpILoginImpl.getInstance().msgCenterReceiveReply("18", page + "", pageSize + "", new RequestCallback<MsgCenterReceiveReplyDTO>() {
            @Override
            public void onError(int errorCode, String errorMsg) {
                mPtrFram.refreshComplete();
                SystemClock.sleep(50);
                switch (errorCode) {
                    case NetResultCode.CODE_LOGIN_STATE_ERROR: //登录态错误，重新登录
                        PopWindowTokenErrorUtils.getInstance(mContext).showPopupWindow(R.layout.act_my_order_list);
                        break;
                    case NetResultCode.SERVER_NO_DATA:
                        isAllRead = true;
                        if (page == 0) {
                            setNoData(View.VISIBLE);
                            myCoinListAdapter.cleanList();
                            myCoinListAdapter.modifyList(null);
                        } else {
                            myCoinListAdapter.setMaxSize(myCoinListAdapter.getList().size() + 1);
                            ToastUtil.getInstance(mContext).setContent(getString(R.string.data_load_completed)).setShow();
                        }
                    default:
                        break;
                }
            }

            @Override
            public void onSuccess(MsgCenterReceiveReplyDTO response) {
                mPtrFram.refreshComplete();
                if (null == response || response.result.size() == 0) {
                    setNoData(View.VISIBLE);
                    myCoinListAdapter.cleanList();
                    return;
                }
                setNoData(View.GONE);
                if (page == 0) {
                    myCoinListAdapter.cleanList();
                }
                page++;
                myCoinListAdapter.modifyList(response.result);

                if (response.result.size() >= pageSize) {
                    mPtrFram.setMode(PtrFrameLayout.Mode.BOTH);
                } else {
                    myCoinListAdapter.setMaxSize(myCoinListAdapter.getList().size() + 1);
                    mPtrFram.setMode(PtrFrameLayout.Mode.REFRESH);
                }

                List<MsgCenterReceiveReplyEntity> msgList = myCoinListAdapter.getList();
                for (int i = 0; i < myCoinListAdapter.getList().size(); i++) {
                    if (msgList.get(i).status != 1) {//没有已读过
                        isAllRead = false;
                        mAbar.setRightTextStyle(R.style.text_f_28_c_black_55626e);
                        break;
                    }
                    isAllRead = true;
                    mAbar.setRightTextStyle(R.style.text_f_28_c95a3b1);
                }

                myCoinListAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 列表点击事件
     */
    AdapterView.OnItemClickListener replyItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            MsgCenterReceiveReplyEntity course = myCoinListAdapter.getList().get(position - 1);
            setIsRead(course.id + "", position - 1);
            Intent intent = new Intent(mContext, PostDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(Constants.POST_DETAIL_ID, course.list.post_bar_id);
            intent.putExtras(bundle);
            startActivity(intent);

        }
    };


    PtrDefaultHandler2 mPtrDefaultHandler2 = new PtrDefaultHandler2() {
        @Override
        public void onLoadMoreBegin(PtrFrameLayout frame) {
            if (!CommonUtils.getIntnetConnect(mContext)) {
                setNoNet();
                return;
            }
            btn_reloading.setVisibility(View.GONE);
            if (myCoinListAdapter.getList().size() % pageSize != 0) {
                ToastUtil.getInstance(mContext).setContent(getString(R.string.data_load_completed)).setShow();
                mPtrFram.refreshComplete();
            } else {
                getData();
            }
        }

        @Override
        public void onRefreshBegin(PtrFrameLayout frame) {
            setDataReset();
        }
    };


    private void setNoData(int visibility) {
        mCircleNoData.setImageResource(R.drawable.icon_post_comment_blank);
        mTvNoData.setText("目前还没有收到回复哦 ╮(╯▽╰)╭");
        mCircleNoData.setVisibility(visibility);
        mTvNoData.setVisibility(visibility);
    }

    /**
     * 无网络的状态
     */
    private void setNoNet() {
        mCircleNoData.setImageResource(R.drawable.common_def_nonet);
        mTvNoData.setText("网络加载出状况了");
        mCircleNoData.setVisibility(View.VISIBLE);
        mTvNoData.setVisibility(View.VISIBLE);
        ToastUtil.getInstance(mContext).setContent(Constants.INTNET_NOT_CONNCATION).setDuration(Toast.LENGTH_SHORT).setShow();

        btn_reloading.setVisibility(View.VISIBLE);
        myCoinListAdapter.cleanList();
        myCoinListAdapter.modifyList(null);
        mPtrFram.refreshComplete();
    }

    /**
     * 数据重新刷新
     */
    public void setDataReset() {
        if (!CommonUtils.getIntnetConnect(mContext)) {
            setNoNet();
            return;
        }
        page = 0;
        myCoinListAdapter.setMaxSize(0);
        btn_reloading.setVisibility(View.GONE);
        getData();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_left_layout:
                finish();
                break;
            case R.id.tv_btn_right:
                if (!CommonUtils.getIntnetConnect(mContext)) {
                    showDialog("网络异常", R.drawable.icon_dialog_unsucces);
                    return;
                }
                setAllRead();
                break;
        }
    }

    /**
     * 一键置为已读
     */
    private void setAllRead() {
        List<MsgCenterReceiveReplyEntity> msgList = myCoinListAdapter.getList();
        for (int i = 0; i < myCoinListAdapter.getList().size(); i++) {
            if (msgList.get(i).status != 1) {//没有已读过
                isAllRead = false;
                break;
            }
        }
        if (isAllRead) {
            return;
        }
        OkHttpILoginImpl.getInstance().msgCenterReceiveReplyAllRead("18", "1", new RequestCallback<ResultEntity>() {
            @Override
            public void onError(int errorCode, String errorMsg) {
                switch (errorCode) {
                    case NetResultCode.SERVER_NO_DATA: //登录态错误，重新登录
                        showDialog("全部已读", R.drawable.icon_dialog_success);
                        mAbar.setRightTextStyle(R.style.text_f_28_c95a3b1);
                        isAllRead = true;
                        break;
                }
            }

            @Override
            public void onSuccess(ResultEntity response) {
                notifyMsgCount();
                mAbar.setRightTextStyle(R.style.text_f_28_c95a3b1);
                mAbar.setRightText("全部已读");
                showDialog("全部已读", R.drawable.icon_dialog_success);
                for (MsgCenterReceiveReplyEntity replyEntity : myCoinListAdapter.getList()) {
                    replyEntity.status = 1;
                }
                isAllRead = true;
                myCoinListAdapter.notifyDataSetChanged();
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
                myCoinListAdapter.getList().get(position).status = 1;
                myCoinListAdapter.notifyDataSetChanged();
                //
                notifyMsgCount();
            }

        });
    }

}
