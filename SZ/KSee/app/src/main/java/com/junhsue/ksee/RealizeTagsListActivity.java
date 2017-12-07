package com.junhsue.ksee;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.junhsue.ksee.adapter.RealizeArticleAdapter;
import com.junhsue.ksee.dto.RealizeArticleDTO;
import com.junhsue.ksee.entity.RealizeArticleEntity;
import com.junhsue.ksee.net.api.OkHttpRealizeImpl;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.utils.StatisticsUtil;
import com.junhsue.ksee.view.CommonListView;
import com.junhsue.ksee.view.RealizeTagsListHeadView;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 百科标签列表页
 */
public class RealizeTagsListActivity extends BaseActivity {
    
    
    private CommonListView mLVArticle;
    private RealizeArticleAdapter<RealizeArticleEntity> mArticleAdapter;
    private PtrClassicFrameLayout mPtrClassicFrameLayout;
    private RealizeTagsListHeadView mRealizeTagsListHeadView;
    private BaseActivity  mContext;

    //文章当前页
    private int mPage=0;

    private String tag_id = "1,2,3,4";
    private int tag_index = 0;
    public static final String TAG_ID = "tag_id";
    public static final String TAG_INDEX = "tag_index";

    @Override
    protected void onReceiveArguments(Bundle bundle) {
        tag_id = bundle.getString(TAG_ID) == null ? null : bundle.getString(TAG_ID);
        tag_index = bundle.getInt(TAG_INDEX,-1) == -1 ? 0 : bundle.getInt(TAG_INDEX);
    }

    @Override
    protected int setLayoutId() {
        mContext = this;
        return R.layout.act_realize_tags_list;
    }

    @Override
    protected void onResume() {
        StatisticsUtil.getInstance(mContext).onCountPage("1.2.2");
        super.onResume();
    }

    @Override
    protected void onInitilizeView() {

        initView();

        mPage=0;
        getArticleList();

        mLVArticle.setOnItemClickListener(articleItemClickListener);
    }

    private void initView() {
        mLVArticle=(CommonListView)findViewById(R.id.list_view_article);
        mPtrClassicFrameLayout=(PtrClassicFrameLayout)findViewById(R.id.ptr_plassic_frameLayout);
        mRealizeTagsListHeadView = (RealizeTagsListHeadView) findViewById(R.id.img_realize_tags_head);
        mArticleAdapter=new RealizeArticleAdapter<RealizeArticleEntity>(mContext);
        mPtrClassicFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
        mPtrClassicFrameLayout.setPtrHandler(ptrDefaultHandler2);
        mLVArticle.setAdapter(mArticleAdapter);

        mRealizeTagsListHeadView.setBackground(tag_index);
    }



    PtrDefaultHandler2 ptrDefaultHandler2=new PtrDefaultHandler2() {

        @Override
        public void onLoadMoreBegin(PtrFrameLayout frame) {
            getArticleList();
        }

        @Override
        public void onRefreshBegin(PtrFrameLayout frame) {
            mPage=0;
            getArticleList();
        }
    };

    private void getArticleList(){
        //
        String pageC=String.valueOf(mPage);
        //
        String pageSize=String.valueOf("10");
        OkHttpRealizeImpl.newInstance().getArticleList(pageC, pageSize, tag_id, new RequestCallback<RealizeArticleDTO>() {
            @Override
            public void onError(int errorCode, String errorMsg) {
                mPtrClassicFrameLayout.refreshComplete();
            }

            @Override
            public void onSuccess(RealizeArticleDTO response) {
                mPtrClassicFrameLayout.refreshComplete();
                //如果当前页为0页
                if(mPage==0){
                    mArticleAdapter.cleanList();
                }
                mArticleAdapter.modifyList(response.list);
                mPage++;
                if(response.list.size()>=10){
                    mPtrClassicFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
                }else{
                    mPtrClassicFrameLayout.setMode(PtrFrameLayout.Mode.REFRESH);

                }
            }
        });
    }



    AdapterView.OnItemClickListener articleItemClickListener=new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            RealizeArticleEntity realizeArticleEntity=mArticleAdapter.getList().get(position);
            Bundle bundle=new Bundle();
            bundle.putString(ArticleDetailActivity.PARAMS_ARTICLE_ID,realizeArticleEntity.id);
            mContext.launchScreen(ArticleDetailActivity.class,bundle);

        }
    };



}
