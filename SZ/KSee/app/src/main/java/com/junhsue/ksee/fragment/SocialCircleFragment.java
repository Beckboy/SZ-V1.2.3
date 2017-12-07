package com.junhsue.ksee.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.junhsue.ksee.AskActivity;
import com.junhsue.ksee.BaseActivity;
import com.junhsue.ksee.R;
import com.junhsue.ksee.SearchActivity;
import com.junhsue.ksee.SelectedQuestionActivity;
import com.junhsue.ksee.adapter.QuestionAdapter;
import com.junhsue.ksee.adapter.SocialCircleGreatestQuestionAdapter;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.common.IBusinessType;
import com.junhsue.ksee.dto.SelectedQuestionDTO;
import com.junhsue.ksee.entity.QuestionEntity;
import com.junhsue.ksee.entity.ResultEntity;
import com.junhsue.ksee.net.api.OkHttpSocialCircleImpl;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.utils.StartActivityUtils;
import com.junhsue.ksee.utils.StatisticsUtil;
import com.junhsue.ksee.view.ActionBar;
import com.junhsue.ksee.view.CommonListView;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;


/**
 * 社
 * Created by longer on 17/3/16 in Junhsue.
 * Modify by Sugar on 17/3/20 in Junshue.
 */

public class SocialCircleFragment extends BaseFragment implements OnClickListener, QuestionAdapter.OnCollectListener {

    private RelativeLayout socialCircleSelectedContentLayout;//精选布局
    private LinearLayout llSelectHeard;
    private ActionBar actionBar;
    private CommonListView lvSelectedQuestions;
    private CommonListView lvNewestQuestions;
    private PtrClassicFrameLayout ptfFrameLayout;
    private ImageView ivAskFloat;
    private QuestionAdapter newestQuestionAdapter;
    private SocialCircleGreatestQuestionAdapter<QuestionEntity> greatestQuestionAdapter;
    //    private ArrayList<QuestionEntity> newestQuestionList;
//    private ArrayList<QuestionEntity> greatestQuestionList;
    private Context mContext;

    private int selectPageSize = 3;
    private int newestPageSize = 5;
    private int currentPage;
    private boolean isMaxPage;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constants.ACTION_REFRESH_QUESTION) || action.equals(Constants.BROAD_ACTION_USERINFO_UPDATE)) {
                refreshAllQuestions();
            }
            if (action.equals(Constants.ACTION_REFRESH_QUESTION_ELEMENT_STATUS)) {

                QuestionEntity entity = (QuestionEntity) intent.getExtras().getSerializable(Constants.QUESTION);

                List<QuestionEntity> questionList = newestQuestionAdapter.getList();
                for (int i = 0; i < questionList.size(); i++) {

                    if (entity.id.equals(questionList.get(i).id)) {
                        questionList.get(i).is_favorite = entity.is_favorite;
                        questionList.get(i).collect = entity.collect;
                        questionList.get(i).reply = entity.reply;
                    }
                }
                newestQuestionAdapter.notifyDataSetChanged();
            }
        }
    };

    public static SocialCircleFragment newInstance() {
        SocialCircleFragment socialCircleFragment = new SocialCircleFragment();
        return socialCircleFragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.act_social_circle;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBroadcast();// 注册广播
    }

    @Override
    protected void onInitilizeView(View view) {

//        greatestQuestionList = new ArrayList<>();
//        newestQuestionList = new ArrayList<>();

        greatestQuestionAdapter = new SocialCircleGreatestQuestionAdapter(mContext);
        newestQuestionAdapter = new QuestionAdapter(mContext);

        actionBar = (ActionBar) view.findViewById(R.id.ab_home_search_heard);
        ptfFrameLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_pcf_layout);
        ivAskFloat = (ImageView) view.findViewById(R.id.iv_ask_float);
        lvNewestQuestions = (CommonListView) view.findViewById(R.id.lv_newest_questions);
//
        socialCircleSelectedContentLayout = (RelativeLayout) view.findViewById(R.id.rl_social_selected_content_layout);
        llSelectHeard = (LinearLayout) view.findViewById(R.id.ll_social_circle_selected_heard);
        lvSelectedQuestions = (CommonListView) view.findViewById(R.id.lv_selected_questions);

        lvSelectedQuestions.setAdapter(greatestQuestionAdapter);

        lvNewestQuestions.setAdapter(newestQuestionAdapter);

        refreshAllQuestions();

        setListener();

    }


    /**
     * 刷新所有问答
     */
    private void refreshAllQuestions() {

        //刷新精选数据
        loadSelectedQuestions();

    }


    /**
     * 设置监听
     */
    private void setListener() {

        llSelectHeard.setOnClickListener(this);
        actionBar.setOnClickListener(this);
        ivAskFloat.setOnClickListener(this);
        newestQuestionAdapter.setOnCollectListener(this);
        ptfFrameLayout.setPtrHandler(ptrDefaultHandler2);
        lvSelectedQuestions.setOnItemClickListener(greatestQuestionsItemClickListener);
        lvNewestQuestions.setOnItemClickListener(newestQuestionsItemClickListener);

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = (BaseActivity) activity;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_social_circle_selected_heard:
                mContext.startActivity(new Intent(mContext, SelectedQuestionActivity.class));
                break;
            case R.id.btn_right_one:
                Intent intent = new Intent(mContext, SearchActivity.class);
                Bundle bundle_search = new Bundle();
                bundle_search.putInt(SearchActivity.SEARCH_BUSINESS_TYPE, IBusinessType.ANSWER);
                intent.putExtras(bundle_search);
                mContext.startActivity(intent);
                break;
            case R.id.iv_ask_float:
                mContext.startActivity(new Intent(mContext, AskActivity.class));

                StatisticsUtil.getInstance(mContext).onCountActionDot("3.2");//自定义埋点统计
                break;
        }
    }


    /**
     * 加载精选数据(1-3条)
     */
    private void loadSelectedQuestions() {

        OkHttpSocialCircleImpl.getInstance().loadQuestionList(selectPageSize, 0, new RequestCallback<SelectedQuestionDTO>() {

            @Override
            public void onError(int errorCode, String errorMsg) {
                ptfFrameLayout.refreshComplete();
                //refreshLayout();
                //刷新最新数据
                loadNewestQuestions(newestPageSize, currentPage);
            }

            @Override
            public void onSuccess(SelectedQuestionDTO response) {
                // ptfFrameLayout.refreshComplete();
                //greatestQuestionAdapter.notifyDataSetChanged();
                // newestQuestionAdapter.notifyDataSetChanged();
                if (currentPage == 0) {
                    greatestQuestionAdapter.cleanList();
                }
                //greatestQuestionList.addAll(response.list);
                refreshLayout(response.list);
                loadNewestQuestions(newestPageSize, currentPage);
            }
        });

    }

    /**
     * 加载最新
     *
     * @param pagesize
     * @param page
     */
    private void loadNewestQuestions(final int pagesize, final int page) {
        OkHttpSocialCircleImpl.getInstance().loadQuestionNewest(null ,pagesize, page, new RequestCallback<SelectedQuestionDTO>() {

            @Override
            public void onError(int errorCode, String errorMsg) {

                ptfFrameLayout.refreshComplete();

            }

            @Override
            public void onSuccess(SelectedQuestionDTO response) {
                ///newestQuestionList.clear();
                if (page == 0) {
                    newestQuestionAdapter.cleanList();
                }
                newestQuestionAdapter.modifyList(response.list);
                ptfFrameLayout.refreshComplete();
                if (response.list.size() >= pagesize) {
                    ptfFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
                } else {
                    isMaxPage = true;
                }

            }
        });

    }


    AdapterView.OnItemClickListener greatestQuestionsItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            QuestionEntity questionEntity = greatestQuestionAdapter.getList().get(position);
            String questionId = questionEntity.id;
            StartActivityUtils.startQuestionDetailActivity(mContext, questionId);

            StatisticsUtil.getInstance(mContext).onCountActionDot("3.3.1");//自定义埋点统计

        }
    };


    AdapterView.OnItemClickListener newestQuestionsItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            QuestionEntity questionEntity = (QuestionEntity) newestQuestionAdapter.getItem(position);
            String questionId = questionEntity.id;
            StartActivityUtils.startQuestionDetailActivity(mContext, questionId);

            StatisticsUtil.getInstance(mContext).onCountActionDot("3.3.1");//自定义埋点统计

        }
    };


    /**
     * 刷新监听
     */
    PtrDefaultHandler2 ptrDefaultHandler2 = new PtrDefaultHandler2() {

        @Override
        public void onLoadMoreBegin(PtrFrameLayout frame) {

            if (isMaxPage) {
                Toast.makeText(mContext, getString(R.string.data_load_completed), Toast.LENGTH_SHORT).show();
                ptfFrameLayout.refreshComplete();//加载完毕
            } else {
                currentPage++;
                loadNewestQuestions(newestPageSize, currentPage);
            }

        }

        @Override
        public void onRefreshBegin(PtrFrameLayout frame) {
            currentPage = 0;
            refreshAllQuestions();
        }
    };


    /**
     * 刷新精选布局
     */
    private void refreshLayout(ArrayList<QuestionEntity> arrayList) {

        if (arrayList.size() <= 0) {
            socialCircleSelectedContentLayout.setVisibility(View.GONE);
        } else {
            socialCircleSelectedContentLayout.setVisibility(View.VISIBLE);
            greatestQuestionAdapter.modifyList(arrayList);
            //greatestQuestionList.clear();
        }
    }

    @Override
    public void onCollectClick(QuestionEntity entity, int position) {

        List<QuestionEntity> list = newestQuestionAdapter.getList();

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
                newestQuestionAdapter.notifyDataSetChanged();

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
                newestQuestionAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 注册广播
     */
    private void registerBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ACTION_REFRESH_QUESTION);
        intentFilter.addAction(Constants.ACTION_REFRESH_QUESTION_ELEMENT_STATUS);
        intentFilter.addAction(Constants.BROAD_ACTION_USERINFO_UPDATE);
        mContext.registerReceiver(mBroadcastReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(mBroadcastReceiver);
    }
}

