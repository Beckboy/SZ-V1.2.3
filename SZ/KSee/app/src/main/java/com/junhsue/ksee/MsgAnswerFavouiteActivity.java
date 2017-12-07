package com.junhsue.ksee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.junhsue.ksee.adapter.MsgAnserFavouiteAdapter;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.dto.MsgAnswerFavouriteDTO;
import com.junhsue.ksee.entity.MsgAnswerFavouriteEntity;
import com.junhsue.ksee.view.ActionBar;



import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 回答点赞列表
 * Created by longer on 17/6/8.
 */

public class MsgAnswerFavouiteActivity extends BaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener{


    //问题回答列表
    public final static String PARAMS_ANSWER_LIST="params_answer_favourite";

    private PtrClassicFrameLayout mPtrClassicFrameLayout;


    private  ListView mListView;
    private ActionBar  mActionBar;

    private MsgAnserFavouiteAdapter<MsgAnswerFavouriteEntity> mMsgAnserFavouiteAdapter;

    private MsgAnswerFavouriteDTO msgAnswerFavouriteDTO;


    @Override
    protected void onReceiveArguments(Bundle bundle) {
        msgAnswerFavouriteDTO=(MsgAnswerFavouriteDTO) bundle.getSerializable(PARAMS_ANSWER_LIST);
        }



    @Override
    protected int setLayoutId() {
        return R.layout.act_msg_answer_favouite;
    }

    @Override
    protected void onInitilizeView() {

        mPtrClassicFrameLayout=(PtrClassicFrameLayout)findViewById(R.id.ptrClassicFrameLayout);
        mListView=(ListView)findViewById(R.id.anser_list);
        mActionBar=(ActionBar)findViewById(R.id.action_bar);

        mMsgAnserFavouiteAdapter=new MsgAnserFavouiteAdapter(getApplicationContext());
        mListView.setAdapter(mMsgAnserFavouiteAdapter);
        mPtrClassicFrameLayout.setMode(PtrFrameLayout.Mode.REFRESH);
        mPtrClassicFrameLayout.setPtrHandler(ptrDefaultHandler2);
        if(null!=msgAnswerFavouriteDTO){
            mMsgAnserFavouiteAdapter.cleanList();
            mMsgAnserFavouiteAdapter.modifyList(msgAnswerFavouriteDTO.data);
        }
        //
        mActionBar.setOnClickListener(this);
        mListView.setOnItemClickListener(this);

    }



    PtrDefaultHandler2 ptrDefaultHandler2=new PtrDefaultHandler2() {

        @Override
        public void onLoadMoreBegin(PtrFrameLayout frame) {

        }

        @Override
        public void onRefreshBegin(PtrFrameLayout frame) {
            mPtrClassicFrameLayout.refreshComplete();

        }
    };


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        MsgAnswerFavouriteEntity msgAnswerFavouriteEntity= mMsgAnserFavouiteAdapter.getList().get(position);
        Intent intent=new Intent();
        intent.setClass(this,QuestionDetailActivity.class);
        intent.putExtra(Constants.QUESTION_ID,msgAnswerFavouriteEntity.question_id);
        startActivity(intent);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case  R.id.ll_left_layout:
                finish();
                break;

            default: break;
        }
    }
}
