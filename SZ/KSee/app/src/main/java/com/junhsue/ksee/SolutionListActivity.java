package com.junhsue.ksee;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;

import com.junhsue.ksee.adapter.SolutionListAdapter;
import com.junhsue.ksee.dto.SolutionListDTO;
import com.junhsue.ksee.entity.Solution;
import com.junhsue.ksee.entity.SolutionList;
import com.junhsue.ksee.entity.SolutionListItem;
import com.junhsue.ksee.net.api.OKHttpHomeImpl;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.utils.ToastUtil;
import com.junhsue.ksee.view.ActionBar;

import java.util.ArrayList;

/**
 * 方案包列表
 * <p>
 * Created by longer on 17/10/11.
 */

public class SolutionListActivity extends BaseActivity implements View.OnClickListener {


    private ActionBar mActionBar;
    private ExpandableListView mExpandableListView;
    private SolutionListAdapter mSolutionListAdapter;
    private Context mContext = this;

    @Override
    protected void onReceiveArguments(Bundle bundle) {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.act_solution_list;
    }


    @Override
    protected void onInitilizeView() {
        mActionBar = (ActionBar) findViewById(R.id.action_bar_solution_list);
        mExpandableListView = (ExpandableListView) findViewById(R.id.expandableb_list_view_solution_list);
        //
        mActionBar.setOnClickListener(this);
        mExpandableListView.setOnChildClickListener(onChildClickListener);
        mExpandableListView.setOnGroupClickListener(onGroupClickListener);
        //
        mSolutionListAdapter = new SolutionListAdapter(mContext);
        mExpandableListView.setAdapter(mSolutionListAdapter);
        alertLoadingProgress();
        mActionBar.setTitleColor(getResources().getColor(R.color.c_gray_242E42));
        getSolutions();
    }


    private void getSolutions() {

        OKHttpHomeImpl.getInstance().getSolutionList(new RequestCallback<SolutionListDTO>() {

            @Override
            public void onError(int errorCode, String errorMsg) {
                dismissLoadingDialog();
                ToastUtil.showToast(mContext, mContext.getString(R.string.net_error_service_not_accessables));
            }

            @Override
            public void onSuccess(SolutionListDTO response) {
                dismissLoadingDialog();
                mSolutionListAdapter.clearData();
                mSolutionListAdapter.modifyData(response.msg);
                for(int i=0;i<mSolutionListAdapter.getList().size();i++){
                    mExpandableListView.expandGroup(i);
                }
            }
        });
    }


    ExpandableListView.OnChildClickListener onChildClickListener=new ExpandableListView.OnChildClickListener() {

        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            //
            SolutionList solutionList=mSolutionListAdapter.getList().get(groupPosition);
            SolutionListItem solution=solutionList.solutions.get(childPosition);
            //
            Intent intent=new Intent(mContext, SolutionDetailsActivity.class);
            intent.putExtra(SolutionDetailsActivity.PARMAS_SOLUTION_ID,solution.id);
            intent.putExtra(SolutionDetailsActivity.PARAMS_SOLUTION_TITLE,solution.title);
            startActivity(intent);
            return false;
        }
    };


    ExpandableListView.OnGroupClickListener onGroupClickListener=new ExpandableListView.OnGroupClickListener() {
        @Override
        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
            return true;
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
