package com.junhsue.ksee;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.junhsue.ksee.adapter.RealizeArticleAdapter;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.dto.RealizeArticleDTO;
import com.junhsue.ksee.entity.MycollectList;
import com.junhsue.ksee.entity.QuestionEntity;
import com.junhsue.ksee.entity.RealizeArticleEntity;
import com.junhsue.ksee.net.api.OKHttpHomeImpl;
import com.junhsue.ksee.net.callback.BroadIntnetConnectListener;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.error.NetResultCode;
import com.junhsue.ksee.utils.CommonUtils;
import com.junhsue.ksee.utils.PopWindowTokenErrorUtils;
import com.junhsue.ksee.utils.ToastUtil;
import com.junhsue.ksee.view.ActionBar;
import com.junhsue.ksee.view.CircleImageView;
import com.junhsue.ksee.view.CommonListView;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class HomeTagsListActivity extends BaseActivity implements View.OnClickListener,BroadIntnetConnectListener.InternetChanged{


    private ListView mLVArticle;
    private RealizeArticleAdapter<RealizeArticleEntity> mArticleAdapter;
    private PtrClassicFrameLayout mPtrClassicFrameLayout;
    private ActionBar mAb;
    private BaseActivity  mContext;

    //文章当前页
    private int mPage=0;
    //当前页数的数据条目
    private int pagesize = 10;
    //数据总数
    private int totlaPage;

    private String tag_id = "0";
    private String tag_title = "";
    public static final String TAG_ID = "tag_id";
    public static final String TAG_TITLE = "tag_title";



    private CircleImageView mCircleNoData;
    private TextView mTvNoData;
    public Button btn_reloading;
    private View vHead;
    private BroadIntnetConnectListener con;  //网络连接的广播





    @Override
    protected void onReceiveArguments(Bundle bundle) {
        tag_id = bundle.getString(TAG_ID) == null ? null : bundle.getString(TAG_ID);
        tag_title = bundle.getString(TAG_TITLE,"") == "" ? "" : bundle.getString(TAG_TITLE);
    }

    @Override
    protected int setLayoutId() {
        mContext = this;
        return R.layout.act_home_tags_list;
    }

    @Override
    protected void onInitilizeView() {

        initView();

        mPage=0;
        getArticleList();

        mLVArticle.setOnItemClickListener(articleItemClickListener);
    }

    private void initView() {
        mLVArticle=(ListView)findViewById(R.id.list_view_article);
        mPtrClassicFrameLayout=(PtrClassicFrameLayout)findViewById(R.id.ptr_plassic_frameLayout);
        mAb = (ActionBar) findViewById(R.id.ab_home_tags_head);
        mAb.setOnClickListener(this);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        vHead = inflater.inflate(R.layout.item_myanswer_head,null);
        vHead.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));
        mLVArticle.addHeaderView(vHead);
        mCircleNoData = (CircleImageView) vHead.findViewById(R.id.img_answer_nodata);
        mTvNoData = (TextView) vHead.findViewById(R.id.tv_answer_nodata);
        btn_reloading = (Button) vHead.findViewById(R.id.btn_answer_reloading);
        btn_reloading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDataReset();
            }
        });
        mArticleAdapter=new RealizeArticleAdapter<RealizeArticleEntity>(mContext);
        mPtrClassicFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
        mPtrClassicFrameLayout.setPtrHandler(ptrDefaultHandler2);

        if (!CommonUtils.getIntnetConnect(mContext)){
            setNoNet();
        }else {
            btn_reloading.setVisibility(View.GONE);
            mContext.alertLoadingProgress();
        }

        mLVArticle.setAdapter(mArticleAdapter);

        mAb.setTitle(tag_title);
    }


    PtrDefaultHandler2 ptrDefaultHandler2=new PtrDefaultHandler2() {

        @Override
        public void onLoadMoreBegin(PtrFrameLayout frame) {
            if (!CommonUtils.getIntnetConnect(mContext)){
                setNoNet();
                return;
            }
            btn_reloading.setVisibility(View.GONE);
            if (mPage > totlaPage){
                ToastUtil.getInstance(mContext).setContent(getString(R.string.data_load_completed)).setShow();
                mPtrClassicFrameLayout.refreshComplete();
            }else {
                getArticleList();
            }
        }

        @Override
        public void onRefreshBegin(PtrFrameLayout frame) {
            setDataReset();
        }
    };


    private void getArticleList(){
        //
        String pageC=String.valueOf(mPage);
        //
        String pageSize=String.valueOf(pagesize);
        OKHttpHomeImpl.getInstance().getArticleList(pageC, pageSize, tag_id, new RequestCallback<RealizeArticleDTO>() {
            @Override
            public void onError(int errorCode, String errorMsg) {
                mPtrClassicFrameLayout.refreshComplete();
                mContext.dismissLoadingDialog();
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
            public void onSuccess(RealizeArticleDTO response) {
                mPtrClassicFrameLayout.refreshComplete();
                mContext.dismissLoadingDialog();
                if (null == response || response.list.size() == 0) {
                    if (mPage == 0) {
                        setNoData(View.VISIBLE);
                    }
                    return;
                }
                setNoData(View.GONE);
                //如果当前页为0页
                if(mPage==0){
                    mArticleAdapter.cleanList();
                }
                mPage++;
                mArticleAdapter.modifyList(response.list);
                totlaPage =  (response.total/pagesize);
                if(response.list.size()>=10){
                    mPtrClassicFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
                }else{
                    mPtrClassicFrameLayout.setMode(PtrFrameLayout.Mode.REFRESH);
                }
            }
        });
    }



    private void setNoData(int visibility) {
        vHead.setVisibility(visibility);
        mCircleNoData.setImageResource(R.drawable.wu_def_order);
        mTvNoData.setText("小编正在努力上传...");
        mCircleNoData.setVisibility(visibility);
        mTvNoData.setVisibility(visibility);
    }

    /**
     * 无网络的状态
     */
    private void setNoNet() {
        vHead.setVisibility(View.VISIBLE);
        mCircleNoData.setImageResource(R.drawable.common_def_nonet);
        mTvNoData.setText("网络加载出状况了");
        mCircleNoData.setVisibility(View.VISIBLE);
        mTvNoData.setVisibility(View.VISIBLE);
        ToastUtil.getInstance(mContext).setContent(Constants.INTNET_NOT_CONNCATION).setDuration(Toast.LENGTH_SHORT).setShow();

        btn_reloading.setVisibility(View.VISIBLE);
        mPage = 0;
        mArticleAdapter.cleanList();
        mArticleAdapter.modifyList(null);
        mPtrClassicFrameLayout.refreshComplete();
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
        mPage = 0;
        getArticleList();
    }


    AdapterView.OnItemClickListener articleItemClickListener=new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position>0){
                position -= 1;
            }else {
                return;
            }
            RealizeArticleEntity realizeArticleEntity=mArticleAdapter.getList().get(position);
            Bundle bundle=new Bundle();
            bundle.putString(ArticleDetailActivity.PARAMS_ARTICLE_ID,realizeArticleEntity.id);
            mContext.launchScreen(ArticleDetailActivity.class,bundle);

        }
    };



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_left_layout:
                finish();
                break;
        }
    }

    @Override
    public void onNetChange(boolean netConnection) {
        if (netConnection){
            if (null == mArticleAdapter.getList() || mArticleAdapter.getList().size() == 0) {
                setDataReset();
            }
        }
    }
}
