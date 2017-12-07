package com.junhsue.ksee;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.junhsue.ksee.adapter.QuestionAdapter;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.dto.SelectedQuestionDTO;
import com.junhsue.ksee.entity.QuestionEntity;
import com.junhsue.ksee.entity.ResultEntity;
import com.junhsue.ksee.net.api.OkHttpSocialCircleImpl;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.utils.StartActivityUtils;
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

/**
 * 搜索结果页
 * Created by Sugar on 17/3/23 in Junhsue.
 */

public class SearchResultActivity extends BaseActivity implements View.OnClickListener, QuestionAdapter.OnCollectListener {

    private String searchResult = ""; //搜索的内容
    private String searchName = ""; //搜索标签标题
    private int searchType = 1; //搜索的类型 1:关键词  2:标签
    private Context mContext;

    private ActionBar actionBar;
    private PtrClassicFrameLayout ptrFrameLayout;
    private ListView lvResultQuestion;
    private RelativeLayout blankPage;
    private QuestionAdapter questionAdapter;

    private int pagesize = 10;//每页数据条目数
    private int currentPage = 0;//当前页
    private boolean isMaxPage;//总共页数
    private ArrayList<SelectedQuestionDTO> questionList;

    @Override
    protected void onReceiveArguments(Bundle bundle) {
        searchType = bundle.getInt(SearchActivity.SEARCH_CONTENT_TYPE,1);
        searchResult = bundle.getString(SearchActivity.SEARCH_CONTENT);
        if (searchType == SearchActivity.SEARCH_KEY_TAG){
            searchName = bundle.getString(SearchActivity.SEARCH_KEY_NAME);
        }
    }

    @Override
    protected int setLayoutId() {
        mContext = this;
        return R.layout.act_search_result;
    }

    @Override
    protected void onInitilizeView() {
        actionBar = (ActionBar) findViewById(R.id.ab_search_result_title);
        ptrFrameLayout = (PtrClassicFrameLayout) findViewById(R.id.pcfl_result_questions_layout);
        lvResultQuestion = (ListView) findViewById(R.id.lv_result_questions);
        blankPage = (RelativeLayout) findViewById(R.id.rl_blank_page);

        questionList = new ArrayList<>();
        questionAdapter = new QuestionAdapter(mContext);
        lvResultQuestion.setAdapter(questionAdapter);
        questionAdapter.notifyDataSetChanged();
        alertLoadingProgress(true);
        setListener();

        loadResultQuestionsFromServer(searchResult, pagesize, currentPage);
    }

    @Override
    protected void onResume() {
        StatisticsUtil.getInstance(mContext).onCountPage("1.3.1.1");//自定义页面统计
        super.onResume();
    }

    private void setListener() {
        actionBar.setOnClickListener(this);
        ptrFrameLayout.setPtrHandler(ptrDefaultHandler2);
        questionAdapter.setOnCollectListener(this);
        lvResultQuestion.setOnItemClickListener(questionDetailItemClickListener);

    }


    /**
     * 加载搜索结果页问题集合
     *
     * @param searchResult 搜索问题
     * @param pagesize     每页问题数量
     * @param currentPage  当前页
     */
    private void loadResultQuestionsFromServer(String searchResult, final int pagesize, final int currentPage) {
        if (StringUtils.isBlank(searchResult.trim())) {
            return;
        }

        if (searchType == SearchActivity.SEARCH_KEY_WORD){
            loadDataByKayWord();
        }else {
            actionBar.setTitle(searchName);
            loadDataByKayTag();
        }

    }


    /**
     * 通过关键词搜索
     */
    private void loadDataByKayWord(){
        OkHttpSocialCircleImpl.getInstance().loadSearchResultQuestionList(searchResult, pagesize, currentPage, new RequestCallback<SelectedQuestionDTO>() {

            @Override
            public void onError(int errorCode, String errorMsg) {
                Trace.i("load selected questions failed info :" + errorMsg);
                ptrFrameLayout.refreshComplete();
                blankPage.setVisibility(View.VISIBLE);
                lvResultQuestion.setEmptyView(blankPage);
                dismissLoadingDialog();
            }

            @Override
            public void onSuccess(SelectedQuestionDTO response) {
                Log.e("==", "===respose:" + response.list.size());
                questionList.clear();
                if (currentPage == 0){
                    questionAdapter.cleanList();
                }
                questionAdapter.modifyList(response.list);
                if (response.list.size() < pagesize) {
                    isMaxPage = true;
                }
                refreshLayout(response);
                ptrFrameLayout.refreshComplete();
                dismissLoadingDialog();
            }
        });
    }

    /**
     * 通过标签搜索
     */
    private void loadDataByKayTag(){

        OkHttpSocialCircleImpl.getInstance().loadQuestionNewest(searchResult, pagesize, currentPage, new RequestCallback<SelectedQuestionDTO>() {

            @Override
            public void onError(int errorCode, String errorMsg) {
                Trace.i("load selected questions failed info :" + errorMsg);
                ptrFrameLayout.refreshComplete();
                blankPage.setVisibility(View.VISIBLE);
                lvResultQuestion.setEmptyView(blankPage);
                dismissLoadingDialog();
            }

            @Override
            public void onSuccess(SelectedQuestionDTO response) {
                Log.e("==", "===respose:" + response.list.size());
                questionList.clear();
                if (currentPage == 0){
                    questionAdapter.cleanList();
                }
                questionAdapter.modifyList(response.list);
                if (response.list.size() < pagesize) {
                    isMaxPage = true;
                }
                refreshLayout(response);
                ptrFrameLayout.refreshComplete();
                dismissLoadingDialog();
            }
        });
    }

    private void refreshLayout(SelectedQuestionDTO response) {
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
                loadResultQuestionsFromServer(searchResult, pagesize, currentPage);
            }
        }

        @Override
        public void onRefreshBegin(PtrFrameLayout frame) {
            ptrFrameLayout.setMode(BOTH);//重置刷新模式
            currentPage = 0;
            loadResultQuestionsFromServer(searchResult, pagesize, currentPage);

        }
    };

    AdapterView.OnItemClickListener questionDetailItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        /*    QuestionEntity questionEntity = (QuestionEntity) parent.getItemAtPosition(position);
            String questionId = questionEntity.id;

            StartActivityUtils.startQuestionDetailActivity(mContext, questionId);*/

            QuestionEntity questionEntity = (QuestionEntity) parent.getItemAtPosition(position);
            String questionId = questionEntity.id;
            Intent intent = new Intent(mContext, QuestionDetailActivity.class);
            intent.putExtra(Constants.QUESTION_ID, questionId);
            startActivityForResult(intent, Constants.FAVORITE_RESULT);

        }
    };


    @Override
    public void onCollectClick(QuestionEntity entity, int position) {

        List<QuestionEntity> list = questionAdapter.getList();

        for (int i = 0; i < list.size(); i++) {
            if (entity.id.equals(list.get(i).id)) {

                if (list.get(position).is_favorite) {
                    senderDeleteFavoriteToServer(list, i);
                } else {
                    senderFavoriteToServer(list, i);
                }
            }
        }

    }


    /**
     * 收藏
     */
    public void senderFavoriteToServer(final List<QuestionEntity> list, final int i) {

        OkHttpSocialCircleImpl.getInstance().senderFavorite(list.get(i).id, list.get(i).business_id, new RequestCallback<ResultEntity>() {

            @Override
            public void onError(int errorCode, String errorMsg) {

            }

            @Override
            public void onSuccess(ResultEntity response) {

                list.get(i).is_favorite = true;
                list.get(i).collect = list.get(i).collect + 1;
                Toast.makeText(mContext, "收藏成功", Toast.LENGTH_SHORT).show();
                questionAdapter.notifyDataSetChanged();

            }
        });

    }


    /**
     * 取消收藏
     */
    public void senderDeleteFavoriteToServer(final List<QuestionEntity> list, final int i) {

        OkHttpSocialCircleImpl.getInstance().senderDeleteFavorite(list.get(i).id, list.get(i).business_id, new RequestCallback<ResultEntity>() {

            @Override
            public void onError(int errorCode, String errorMsg) {

            }

            @Override
            public void onSuccess(ResultEntity response) {

                list.get(i).is_favorite = false;
                list.get(i).collect = list.get(i).collect - 1;
                Toast.makeText(mContext, "取消收藏成功", Toast.LENGTH_SHORT).show();
                questionAdapter.notifyDataSetChanged();

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) {
            return;
        }
        Bundle bundle = data.getExtras();
        if (bundle == null) {
            return;
        }
        if (requestCode != Constants.FAVORITE_RESULT) {
            return;
        }

        QuestionEntity entity = (QuestionEntity) bundle.getSerializable(Constants.QUESTION);

        List<QuestionEntity> questionList = questionAdapter.getList();
        for (int i = 0; i < questionList.size(); i++) {

            if (entity.id.equals(questionList.get(i).id)) {
                questionList.get(i).is_favorite = entity.is_favorite;
                questionList.get(i).collect = entity.collect;
            }
        }
        questionAdapter.notifyDataSetChanged();

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
