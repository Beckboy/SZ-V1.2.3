package com.junhsue.ksee;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.junhsue.ksee.adapter.ProfessorAdapter;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.dto.ProfessorDTO;
import com.junhsue.ksee.entity.ProfessorEntity;
import com.junhsue.ksee.entity.QuestionEntity;
import com.junhsue.ksee.entity.ResultEntity;
import com.junhsue.ksee.net.api.OkHttpSocialCircleImpl;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.utils.StatisticsUtil;
import com.junhsue.ksee.view.ActionBar;


import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;


/**
 * 邀请人员回答
 * Created by Sugar on 17/4/11.
 */

public class ProfessorActivity extends BaseActivity implements View.OnClickListener {

    private ActionBar actionBar;
    private Context mContext;
    private TextView mTvTitle;
    private PtrClassicFrameLayout ptrClassicFrameLayout;
    private ListView lvProfessor;
    private ProfessorAdapter professorAdapter;
    private int count = 10;
    private QuestionEntity questionEntity;

    private ArrayList<String> professorIds;

    @Override
    protected void onReceiveArguments(Bundle bundle) {
        questionEntity = (QuestionEntity) bundle.getSerializable("question");
//        Log.e("===", "===questionEntity:" + questionEntity.title + questionEntity.content + questionEntity.fromtopic);

    }

    @Override
    protected int setLayoutId() {
        mContext = this;
        return R.layout.act_professor;
    }

    @Override
    protected void onInitilizeView() {

        professorIds = new ArrayList<>();
        mTvTitle = (TextView) findViewById(R.id.tv_logo_msg);
        actionBar = (ActionBar) findViewById(R.id.ab_professor_title);
//        ptrClassicFrameLayout = (PtrClassicFrameLayout) findViewById(R.id.pcfl_professor_layout);
        lvProfessor = (ListView) findViewById(R.id.lv_professors);
        professorAdapter = new ProfessorAdapter(mContext);
        lvProfessor.setAdapter(professorAdapter);

        actionBar.setBottomDividerVisible(View.GONE);

        loadProfessorFromServer(count);

        String title = "<font><big><big>" + getString(R.string.ask_for_professor_title) + "</big></big></font>";
        String count = "<font>" + getString(R.string.ask_for_professor_count) + "</font>";

        mTvTitle.setText(Html.fromHtml(title + count));

        setListener();
    }

    @Override
    protected void onResume() {
        StatisticsUtil.getInstance(mContext).onCountPage("1.3.4.1");//自定义页面统计
        super.onResume();
    }

    /**
     * 监听
     */
    private void setListener() {

        actionBar.setOnClickListener(this);
        lvProfessor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //刷新选中的人员
                refreshSelectedProfessor(position);


            }
        });

    }


    /**
     * 刷新选中的人员
     *
     * @param position
     */
    private void refreshSelectedProfessor(int position) {

        //标记是否可以继续选择的
        boolean isSelect = false;

        List<ProfessorEntity> professorList = professorAdapter.getList();

        for (int i = 0; i < professorIds.size(); i++) {
            //判断点击的是否是在之前点击过的选项,如果是那就是可以重新选择的
            if (professorIds.get(i).equals(professorList.get(position).id + "")) {
                isSelect = true;
            }
        }

        //点击选项数量超过三位,同时点击的选项不在是之前的范围,没有机会再点击其他以外的选项
        if (professorIds.size() > 2 && isSelect == false) {
            return;
        }

        if (professorList.get(position).isSelected) {
            professorList.get(position).isSelected = false;
            // 移除
            for (int i = 0; i < professorIds.size(); i++) {
                if (professorIds.get(i).equals(professorList.get(position).id + "")) {
                    professorIds.remove(professorList.get(position).id);
                }
            }

        } else {
            professorList.get(position).isSelected = true;
            //添加
            professorIds.add(professorList.get(position).id);
        }
        professorAdapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_left_layout:
                finish();
                break;
            case R.id.tv_btn_right:
                senderQuestion();
                break;

        }

    }


    /**
     * 邀请人员列表
     *
     * @param count
     */
    public void loadProfessorFromServer(int count) {

        OkHttpSocialCircleImpl.getInstance().askForProfessors(count, questionEntity.fromtopic, new RequestCallback<ProfessorDTO>() {

            @Override
            public void onError(int errorCode, String errorMsg) {

            }

            @Override
            public void onSuccess(ProfessorDTO response) {

                if (response.result.size() > 0) {

                    professorAdapter.modifyList(response.result);

                }

            }
        });
    }


    /**
     * 是否关闭上一页面
     *
     * @param isFinish
     */
    public void finishLastPageByResult(boolean isFinish) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.IS_FINISH, isFinish);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
    }


    /**
     * 发送问题
     */
    public void senderQuestion() {
        if (professorIds.size() <= 0) {
            Toast.makeText(mContext, "你还没选择小伙伴哦", Toast.LENGTH_SHORT).show();
            return;
        }

        String ids = "";
        for (int i = 0; i < professorIds.size(); i++) {
            if (i == 0) {
                ids = professorIds.get(i);
            } else {
                ids = ids + "," + professorIds.get(i);//需要优化
            }

        }

        alertLoadingProgress(false);
        OkHttpSocialCircleImpl.getInstance().senderQuestion(questionEntity.title, questionEntity.content, questionEntity.fromtopic, ids, new RequestCallback<ResultEntity>() {
            @Override
            public void onError(int errorCode, String errorMsg) {
                Toast.makeText(mContext, "发送失败", Toast.LENGTH_SHORT).show();
                dismissLoadingDialog();
            }

            @Override
            public void onSuccess(ResultEntity response) {
                Toast.makeText(mContext, "发布成功", Toast.LENGTH_SHORT).show();
                finishLastPageByResult(true);
                sendToRefreshQuestionBroadcast();
                dismissLoadingDialog();
                finish();

            }
        });

        StatisticsUtil.getInstance(mContext).onCountActionDot("3.8.1");//自定义埋点统计
    }

    /**
     * 刷新社首页的问答列表
     */
    private void sendToRefreshQuestionBroadcast() {
        Intent intent = new Intent();
        intent.setAction(Constants.ACTION_REFRESH_QUESTION);
        mContext.sendBroadcast(intent);
    }

}
