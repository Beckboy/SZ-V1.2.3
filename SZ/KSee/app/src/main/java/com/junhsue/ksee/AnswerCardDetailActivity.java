package com.junhsue.ksee;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.junhsue.ksee.adapter.AnswerCardDetailAdapter;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.dto.AnswerCardDetailDTO;
import com.junhsue.ksee.entity.AnswerCardDetailsEntity;
import com.junhsue.ksee.net.api.OkHttpILoginImpl;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.error.NetResultCode;
import com.junhsue.ksee.profile.UserProfileService;
import com.junhsue.ksee.utils.CommonUtils;
import com.junhsue.ksee.utils.StartActivityUtils;
import com.junhsue.ksee.utils.StatisticsUtil;
import com.junhsue.ksee.utils.ToastUtil;
import com.junhsue.ksee.view.ActionBar;
import com.junhsue.ksee.view.CircleImageView;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 问答卡片 二级消息列表目录
 */
public class AnswerCardDetailActivity extends BaseActivity implements View.OnClickListener{

    private ActionBar mAbar;
    private PtrClassicFrameLayout mPtrFram;
    private ListView mLv;
    private CircleImageView mCircleNoData;
    private TextView mTvNoData;
    public Button btn_reloading;
    private View vHead;

    private AnswerCardDetailAdapter myAnswerCardListAdapter;
    private AnswerCardDetailActivity mContext;

    //当前页数0页开始
    private int pageIndex = 0;
    //当前页数的数据条目
    private int pagesize = 10;
    //数据总数
    private int totlaPage;
    //是否正在预加载的flag；
    private boolean isRefresh = false;
    //是否数据全部加载完毕
    private boolean isFinish = false;

    @Override
    protected void onReceiveArguments(Bundle bundle) {}

    @Override
    protected int setLayoutId() {
        return R.layout.act_answer_card_detail;
    }

    @Override
    protected void onInitilizeView() {
        mContext = this;
        initView();

    }

    private void initView() {
        mAbar = (ActionBar) findViewById(R.id.ab_answer_card_detail);
        mAbar.setOnClickListener(mContext);
        mPtrFram = (PtrClassicFrameLayout) findViewById(R.id.ptrClassicFrameLayout_my_answer_card_detail);
        mLv = (ListView) findViewById(R.id.lv_my_answer_card_detail);
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

        mPtrFram.setPtrHandler(mPtrDefaultHandler2);
        myAnswerCardListAdapter = new AnswerCardDetailAdapter(mContext);

        if (!CommonUtils.getIntnetConnect(mContext)){
            setNoNet();
        }else {
            btn_reloading.setVisibility(View.GONE);
            mContext.alertLoadingProgress();
            getData();
        }

        mLv.setAdapter(myAnswerCardListAdapter);

        mLv.setOnItemClickListener(itemClickListener);
        mLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + pagesize/2 + visibleItemCount >= totalItemCount){
                    if (isRefresh) return;
                    if (isFinish) return;
                    if (!CommonUtils.getIntnetConnect(mContext)){
                        setNoNet();
                        return;
                    }
                    btn_reloading.setVisibility(View.GONE);
                    if (pageIndex > totlaPage){
                        isFinish = true;
                        mPtrFram.refreshComplete();
                    }else {
                        getData();
                    }
                }
            }
        });
    }

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position>=0){
//                position -= 1;
            }else {
                return;
            }
            StatisticsUtil.getInstance(mContext).onCountPage("1.1.1");

            AnswerCardDetailsEntity myanswercardlist = (AnswerCardDetailsEntity) myAnswerCardListAdapter.getItem(position);
            String question_id = myanswercardlist.list.question_id;
            if (myanswercardlist.type_id == 4){
                question_id = myanswercardlist.list.data.get(0).question_id ;
            }
            StartActivityUtils.startQuestionDetailActivity(mContext,question_id);
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
            if (pageIndex > totlaPage){
                ToastUtil.getInstance(mContext).setContent(getString(R.string.data_load_completed)).setShow();
                mPtrFram.refreshComplete();
                isFinish = true;
            }else {
                getData();
            }
        }

        @Override
        public void onRefreshBegin(PtrFrameLayout frame) {
            setDataReset();
        }
    };



    /**
     * 获取被邀请的条目数据
     */
    public void getData() {
        isRefresh = true;
        String token = UserProfileService.getInstance(mContext).getCurrentLoginedUser().token;
        final String mPageIndex = String.valueOf(pageIndex);
        String mPagesize = String.valueOf(pagesize);
        OkHttpILoginImpl.getInstance().myanswerCardList(token, mPageIndex, mPagesize, new RequestCallback<AnswerCardDetailDTO>() {
            @Override
            public void onError(int errorCode, String errorMsg) {
                isRefresh = false;
                mPtrFram.refreshComplete();
                mContext.dismissLoadingDialog();
                switch (errorCode){
                    case NetResultCode.SERVER_NO_DATA:
                        if (pageIndex == 0) {
                            setNoData(View.VISIBLE);
                            myAnswerCardListAdapter.cleanList();
                            myAnswerCardListAdapter.modifyList(null);
                        }else {
                            isFinish = true;
                            ToastUtil.getInstance(mContext).setContent(getString(R.string.data_load_completed)).setShow();
                        }
                        break;
                }
            }

            @Override
            public void onSuccess(AnswerCardDetailDTO response) {
                isRefresh = false;
                mPtrFram.refreshComplete();
                mContext.dismissLoadingDialog();
                if (null == response || response.result.size() == 0){
                    if (pageIndex == 0){
                        setNoData(View.VISIBLE);
                    }
                    return;
                }
                setNoData(View.GONE);
                if (pageIndex == 0){
                    myAnswerCardListAdapter.cleanList();
                }
                pageIndex++;
                myAnswerCardListAdapter.modifyList(response.result);
                totlaPage = response.result.size()/pagesize;
                if (response.result.size() >= pagesize){
                    mPtrFram.setMode(PtrFrameLayout.Mode.BOTH);
                }
            }
        });
    }


    private void setNoData(int visibility) {
        if (visibility == View.VISIBLE){
            mLv.addHeaderView(vHead);
        }else {
            mLv.removeHeaderView(vHead);
        }
        mCircleNoData.setImageResource(R.drawable.wu_def_collection);
        mTvNoData.setText("暂无新消息");
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
        pageIndex = 0;
        myAnswerCardListAdapter.cleanList();
        myAnswerCardListAdapter.modifyList(null);
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
        pageIndex = 0;
        getData();
        isFinish = false;
    }

    @Override
    public void onDestroy() {
        pageIndex = 0;
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_left_layout:
                finish();
                break;

        }
    }
}
