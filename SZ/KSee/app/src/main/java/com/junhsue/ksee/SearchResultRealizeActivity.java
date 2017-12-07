package com.junhsue.ksee;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.junhsue.ksee.adapter.QuestionAdapter;
import com.junhsue.ksee.adapter.RealizeArticleAdapter;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.dto.RealizeArticleDTO;
import com.junhsue.ksee.dto.SelectedQuestionDTO;
import com.junhsue.ksee.entity.QuestionEntity;
import com.junhsue.ksee.entity.RealizeArticleEntity;
import com.junhsue.ksee.entity.ResultEntity;
import com.junhsue.ksee.net.api.OkHttpRealizeImpl;
import com.junhsue.ksee.net.api.OkHttpSocialCircleImpl;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.utils.StatisticsUtil;
import com.junhsue.ksee.utils.StringUtils;
import com.junhsue.ksee.view.ActionBar;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

import static in.srain.cube.views.ptr.PtrFrameLayout.Mode.BOTH;
import static in.srain.cube.views.ptr.PtrFrameLayout.Mode.REFRESH;

public class SearchResultRealizeActivity extends BaseActivity implements View.OnClickListener{

    private String searchResult = "";
    private Context mContext;

    private ActionBar actionBar;
    private PtrClassicFrameLayout ptrFrameLayout;
    private ListView lvResultQuestion;
    private RelativeLayout blankPage;
    private RealizeArticleAdapter<RealizeArticleEntity> mArticleAdapter;

    private int pagesize = 10;//每页数据条目数
    private int currentPage = 0;//当前页
    private boolean isMaxPage;//总共页数

    @Override
    protected void onReceiveArguments(Bundle bundle) {
        searchResult = bundle.getString(SearchActivity.SEARCH_CONTENT);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.act_search_result;
    }

    @Override
    protected void onInitilizeView() {
        mContext = this;
        actionBar = (ActionBar) findViewById(R.id.ab_search_result_title);
        ptrFrameLayout = (PtrClassicFrameLayout) findViewById(R.id.pcfl_result_questions_layout);
        lvResultQuestion = (ListView) findViewById(R.id.lv_result_questions);
        blankPage = (RelativeLayout) findViewById(R.id.rl_blank_page);

        mArticleAdapter = new RealizeArticleAdapter<RealizeArticleEntity>(mContext);
        lvResultQuestion.setAdapter(mArticleAdapter);
        mArticleAdapter.notifyDataSetChanged();
        alertLoadingProgress(true);
        loadResultArticleFromServer(searchResult, pagesize, currentPage);
        setListener();
    }


    @Override
    protected void onResume() {
        StatisticsUtil.getInstance(mContext).onCountPage("1.3.1.1");//自定义页面统计
        super.onResume();
    }

    private void setListener() {
        actionBar.setOnClickListener(this);
        ptrFrameLayout.setPtrHandler(ptrDefaultHandler2);
        lvResultQuestion.setOnItemClickListener(questionDetailItemClickListener);

    }

    /**
     * 加载搜索结果页问题集合
     *
     * @param searchResult 搜索问题
     * @param pagesize     每页问题数量
     * @param currentPage  当前页
     */
    private void loadResultArticleFromServer(String searchResult, final int pagesize, final int currentPage) {
        if (StringUtils.isBlank(searchResult.trim())) {
            return;
        }
        OkHttpRealizeImpl.newInstance().loadSearchResultRealizeList(searchResult, pagesize, currentPage, new RequestCallback<RealizeArticleDTO>() {

            @Override
            public void onError(int errorCode, String errorMsg) {
                Trace.i("load selected questions failed info :" + errorMsg);
                ptrFrameLayout.refreshComplete();
                blankPage.setVisibility(View.VISIBLE);
                lvResultQuestion.setEmptyView(blankPage);
                dismissLoadingDialog();
            }

            @Override
            public void onSuccess(RealizeArticleDTO response) {
                Log.e("==", "===respose:" + response.list.size());
                if (currentPage == 0){
                    mArticleAdapter.cleanList();
                }
                mArticleAdapter.modifyList(response.list);
                if (response.list.size() < pagesize) {
                    isMaxPage = true;
                }
                refreshLayout(response);
                ptrFrameLayout.refreshComplete();
                dismissLoadingDialog();
            }
        });
    }

    private void refreshLayout(RealizeArticleDTO response) {
        if (response.list.size() <= 0) {
            lvResultQuestion.setEmptyView(blankPage);
            blankPage.setVisibility(View.VISIBLE);
        } else {
            blankPage.setVisibility(View.GONE);
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
                ptrFrameLayout.refreshComplete();//加载完毕
                ptrFrameLayout.setMode(REFRESH);//只能刷新
            } else {
                currentPage++;
                loadResultArticleFromServer(searchResult, pagesize, currentPage);
            }
        }

        @Override
        public void onRefreshBegin(PtrFrameLayout frame) {
            ptrFrameLayout.setMode(BOTH);//重置刷新模式
            currentPage = 0;
            loadResultArticleFromServer(searchResult, pagesize, currentPage);

        }
    };

    AdapterView.OnItemClickListener questionDetailItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            RealizeArticleEntity realizeArticleEntity = mArticleAdapter.getList().get(position);
            Bundle bundle = new Bundle();
            bundle.putString(ArticleDetailActivity.PARAMS_ARTICLE_ID, realizeArticleEntity.id);
            launchScreen(ArticleDetailActivity.class, bundle);
        }
    };



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_left_layout:
                finish();
                break;

        }
    }
}
