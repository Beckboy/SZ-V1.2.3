package com.junhsue.ksee;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.junhsue.ksee.adapter.QuestionAdapter;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.dto.SelectedQuestionDTO;
import com.junhsue.ksee.entity.AnswerEntity;
import com.junhsue.ksee.entity.QuestionEntity;
import com.junhsue.ksee.entity.ResultEntity;
import com.junhsue.ksee.net.api.OkHttpSocialCircleImpl;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.utils.StartActivityUtils;
import com.junhsue.ksee.utils.StatisticsUtil;
import com.junhsue.ksee.view.ActionBar;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

import static in.srain.cube.views.ptr.PtrFrameLayout.Mode.BOTH;
import static in.srain.cube.views.ptr.PtrFrameLayout.Mode.REFRESH;

/**
 * 精选结果列表页
 * Created by Sugar on 17/3/21 in junhsue.
 */

public class SelectedQuestionActivity extends BaseActivity implements View.OnClickListener, QuestionAdapter.OnCollectListener {
    private PtrClassicFrameLayout ptrFramelaout;
    private ListView lvQuestions;//
    private ActionBar actionBar;
    private QuestionAdapter questionAdapter;
    private Context mContext;
    private int pagesize = 10;//每页数据条目数
    private int currentPage = 0;//当前页
    private boolean isMaxPage;//总共页数

    @Override
    protected void onReceiveArguments(Bundle bundle) {

    }

    @Override
    protected int setLayoutId() {
        mContext = this;
        return R.layout.act_selected_question;
    }

    @Override
    protected void onInitilizeView() {

        actionBar = (ActionBar) findViewById(R.id.ab_search_title);
        ptrFramelaout = (PtrClassicFrameLayout) findViewById(R.id.pcfl_questions_layout);
        lvQuestions = (ListView) findViewById(R.id.lv_questions);

        questionAdapter = new QuestionAdapter(mContext);
        lvQuestions.setAdapter(questionAdapter);
        questionAdapter.notifyDataSetChanged();
        alertLoadingProgress(true);
        loadQuestionsFromServer(pagesize, currentPage);
        setListener();

    }


    @Override
    protected void onResume() {
        StatisticsUtil.getInstance(mContext).onCountPage("1.3.2");//自定义统计
        super.onResume();
    }

    private void setListener() {
        //设置刷新监听
        ptrFramelaout.setPtrHandler(ptrDefaultHandler2);
        actionBar.setOnClickListener(this);
        questionAdapter.setOnCollectListener(this);
        lvQuestions.setOnItemClickListener(questionDetailItemClickListener);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_left_layout:
                finish();
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
                ptrFramelaout.refreshComplete();//加载完毕
                ptrFramelaout.setMode(REFRESH);//只能刷新
            } else {
                ptrFramelaout.setMode(BOTH);//重置刷新模式
                currentPage++;
                loadQuestionsFromServer(pagesize, currentPage);
            }
        }

        @Override
        public void onRefreshBegin(PtrFrameLayout frame) {
            currentPage = 0;
            ptrFramelaout.setMode(BOTH);//重置刷新模式
            loadQuestionsFromServer(pagesize, currentPage);
        }
    };

    /**
     * 加载精选问答列表
     *
     * @param pagesize
     * @param currentPage
     */
    private void loadQuestionsFromServer(final int pagesize, final int currentPage) {
        OkHttpSocialCircleImpl.getInstance().loadQuestionList(pagesize, currentPage, new RequestCallback<SelectedQuestionDTO>() {

            @Override
            public void onError(int errorCode, String errorMsg) {
                Trace.i("load selected questions failed info :" + errorMsg);
                ptrFramelaout.refreshComplete();
                dismissLoadingDialog();
            }

            @Override
            public void onSuccess(SelectedQuestionDTO response) {
                if (currentPage == 0) {
                    questionAdapter.cleanList();
                }
                questionAdapter.modifyList(response.list);
                if (response.list.size() < pagesize) {
                    isMaxPage = true;
                }
                ptrFramelaout.refreshComplete();
                dismissLoadingDialog();
            }
        });

    }


    AdapterView.OnItemClickListener questionDetailItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            QuestionEntity questionEntity = (QuestionEntity) greatestQuestionAdapter.getItem(position);
            QuestionEntity questionEntity = (QuestionEntity) parent.getItemAtPosition(position);
            String questionId = questionEntity.id;
            Intent intent = new Intent(mContext, QuestionDetailActivity.class);
            intent.putExtra(Constants.QUESTION_ID, questionId);
            startActivityForResult(intent, Constants.FAVORITE_RESULT);

            StatisticsUtil.getInstance(mContext).onCountActionDot("3.3.1");//自定义埋点统计

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

}
