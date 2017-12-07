package com.junhsue.ksee;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.junhsue.ksee.adapter.MyCoinListAdapter;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.dto.MyCoinListDTO;
import com.junhsue.ksee.entity.MyCoinListEntity;
import com.junhsue.ksee.net.api.OkHttpILoginImpl;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.error.NetResultCode;
import com.junhsue.ksee.utils.CommonUtils;
import com.junhsue.ksee.utils.PopWindowTokenErrorUtils;
import com.junhsue.ksee.utils.ToastUtil;
import com.junhsue.ksee.view.ActionBar;
import com.junhsue.ksee.view.CircleImageView;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class MyCoinActivity extends BaseActivity implements View.OnClickListener{

    private MyCoinActivity mContext;

    private ActionBar mAbar;
    private PtrClassicFrameLayout mPtrFram;
    private ListView mLv;

    //数据异常的占位图
    private CircleImageView mCircleNoData;
    private TextView mTvNoData;
    public Button btn_reloading;
    private View vHead;

    private MyCoinListAdapter<MyCoinListEntity> myCoinListAdapter;


    @Override
    protected void onReceiveArguments(Bundle bundle) {}

    @Override
    protected int setLayoutId() {
        return R.layout.act_my_coin;
    }

    @Override
    protected void onInitilizeView() {

        mContext = this;
        initView();

        mPtrFram.setPtrHandler(mPtrDefaultHandler2);

        myCoinListAdapter = new MyCoinListAdapter<>(this);

        if (!CommonUtils.getIntnetConnect(mContext)){
            setNoNet();
        }else {
            btn_reloading.setVisibility(View.GONE);
        }

        mLv.setAdapter(myCoinListAdapter);

        getData();
    }


    private void initView() {
        mAbar = (ActionBar) findViewById(R.id.ab_my_coin_list);
        mAbar.setOnClickListener(this);
        mPtrFram = (PtrClassicFrameLayout) findViewById(R.id.ptrClassicFrameLayout_my_coin_list);
        mLv = (ListView) findViewById(R.id.lv_my_coin_list);

        //数据异常的占位图
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
    }

    /**
     * 获取被邀请的条目数据
     */
    private void getData() {
        OkHttpILoginImpl.getInstance().myCoin( new RequestCallback<MyCoinListDTO>() {
            @Override
            public void onError(int errorCode, String errorMsg) {
                mPtrFram.refreshComplete();
                SystemClock.sleep(50);
                switch (errorCode){
                    case NetResultCode.CODE_LOGIN_STATE_ERROR: //登录态错误，重新登录
                        PopWindowTokenErrorUtils.getInstance(mContext).showPopupWindow(R.layout.act_my_order_list);
                        break;
                    case NetResultCode.SERVER_NO_DATA:
                        setNoData(View.VISIBLE);
                    default:
                        break;
                }
            }

            @Override
            public void onSuccess(MyCoinListDTO response) {
                mPtrFram.refreshComplete();
                if (null == response || response.result.size() == 0) {
                    setNoData(View.VISIBLE);
                    myCoinListAdapter.cleanList();
                    return;
                }
                setNoData(View.GONE);
                myCoinListAdapter.cleanList();
                myCoinListAdapter.modifyList(response.result);
                mPtrFram.setMode(PtrFrameLayout.Mode.REFRESH);
            }
        });
    }


    PtrDefaultHandler2 mPtrDefaultHandler2 = new PtrDefaultHandler2() {
        @Override
        public void onLoadMoreBegin(PtrFrameLayout frame) {
            setDataReset();
        }

        @Override
        public void onRefreshBegin(PtrFrameLayout frame) {
            setDataReset();
        }
    };




    private void setNoData(int visibility) {
        mCircleNoData.setImageResource(R.drawable.wu_def_order);
        mTvNoData.setText("你还没有任何兑换券哦");
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
    public void setDataReset(){
        if (!CommonUtils.getIntnetConnect(mContext)){
            setNoNet();
            return;
        }
        btn_reloading.setVisibility(View.GONE);
        getData();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_left_layout:
                finish();
                break;
        }
    }
}
