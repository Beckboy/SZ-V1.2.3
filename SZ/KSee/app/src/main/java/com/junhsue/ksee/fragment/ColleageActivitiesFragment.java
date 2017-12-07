package com.junhsue.ksee.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.junhsue.ksee.ActivityDetailActivity;
import com.junhsue.ksee.R;
import com.junhsue.ksee.adapter.ActivitiesAdapter;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.dto.ActivityDTO;
import com.junhsue.ksee.entity.ActivityEntity;
import com.junhsue.ksee.entity.ResultEntity;
import com.junhsue.ksee.net.api.OKHttpCourseImpl;
import com.junhsue.ksee.net.api.OkHttpSocialCircleImpl;
import com.junhsue.ksee.net.callback.RequestCallback;

import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 活动
 * Created by longer on 17/3/22.
 */

public class ColleageActivitiesFragment extends BaseFragment implements ActivitiesAdapter.OnApprovalListener {


    //活动收藏状态更新
    public final static  String  BROAD_ACTION_APPROVAL_STATUS_UPDATE=
            "com.junhsue.ksee,fragment_action_approval_status_update";

    private ActivitiesAdapter activitiesAdapter;
    private Context mContext;
    private PtrClassicFrameLayout pcflayout;
    private ListView lvAcitivies;
    private boolean isMaxPage;
    private int currentPage;
    private int pageSize = 10;


    public static ColleageActivitiesFragment newInstance() {

        ColleageActivitiesFragment colleageActivitiesFragment = new ColleageActivitiesFragment();

        return colleageActivitiesFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.act_colleage_activities;
    }

    @Override
    protected void onInitilizeView(View view) {

        initLayout(view);
        setListener();
        loadActivitiesFromServer(currentPage, pageSize);

    }

    private void setListener() {

        pcflayout.setPtrHandler(ptrDefaultHandler2);
        activitiesAdapter.setOnApprovalListener(this);
        lvAcitivies.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ActivityEntity entity = (ActivityEntity) parent.getItemAtPosition(position);
                Intent intent = new Intent(mContext, ActivityDetailActivity.class);
                //intent.putExtra(Constants.ACTIVITY, entity);
                intent.putExtra(ActivityDetailActivity.PARAMS_ACTIVITY_ID,entity.id);
                startActivityForResult(intent, Constants.APPROVAL_RESULT);
            }
        });

    }

    private void initLayout(View view) {

        pcflayout = (PtrClassicFrameLayout) view.findViewById(R.id.pcfl_activity_layout);
        lvAcitivies = (ListView) view.findViewById(R.id.lv_activities);
        activitiesAdapter = new ActivitiesAdapter(mContext);
        lvAcitivies.setAdapter(activitiesAdapter);

    }


    /**
     * 刷新监听*/
    PtrDefaultHandler2 ptrDefaultHandler2 = new PtrDefaultHandler2() {

        @Override
        public void onLoadMoreBegin(PtrFrameLayout frame) {
            if (isMaxPage) {
                Toast.makeText(mContext, getString(R.string.data_load_completed), Toast.LENGTH_SHORT).show();
                pcflayout.refreshComplete();//加载完毕
            } else {
                currentPage++;
                loadActivitiesFromServer(currentPage, pageSize);
            }
        }

        @Override
        public void onRefreshBegin(PtrFrameLayout frame) {

            currentPage = 0;
            loadActivitiesFromServer(currentPage,pageSize);
        }
    };



    public void loadActivitiesFromServer(final int currentPage, final int pageSize) {

        OKHttpCourseImpl.getInstance().getActivities(currentPage, pageSize, new RequestCallback<ActivityDTO>() {
            @Override
            public void onError(int errorCode, String errorMsg) {

                pcflayout.refreshComplete();
            }

            @Override
            public void onSuccess(ActivityDTO response) {

                if (currentPage == 0) {
                    activitiesAdapter.cleanList();
                }
                activitiesAdapter.modifyList(response.list);
                if (response.list.size() < pageSize) {
                    isMaxPage = true;
                }
                if(response.totalsize>=10){
                    pcflayout.setMode(PtrFrameLayout.Mode.BOTH);
                }
                pcflayout.refreshComplete();

            }
        });

    }


    @Override
    public void onApprovalClick(ActivityEntity entity, int position) {

        if (entity.is_approval) {
            entity.is_approval = false;
            entity.approvalcount--;
            sendDeleteApprovalToServer(entity);
        } else {
            entity.approvalcount++;
            entity.is_approval = true;
            sendApprovalToServer(entity);
        }
        activitiesAdapter.notifyDataSetChanged();
    }


    /**
     * 点赞
     */
    public void sendApprovalToServer(final ActivityEntity entity) {

        OkHttpSocialCircleImpl.getInstance().senderApproval(entity.offline_activity_id, String.valueOf(entity.business_id),
                new RequestCallback<ResultEntity>() {

            @Override
            public void onError(int errorCode, String errorMsg) {

            }

            @Override
            public void onSuccess(ResultEntity response) {

                Trace.i("approval successful!");
            }
        });

    }


    /**
     * 取消点赞
     */
    public void sendDeleteApprovalToServer(final ActivityEntity entity) {

        OkHttpSocialCircleImpl.getInstance().senderDeleteApproval(entity.offline_activity_id,String.valueOf(entity.business_id), new RequestCallback<ResultEntity>() {

            @Override
            public void onError(int errorCode, String errorMsg) {

            }

            @Override
            public void onSuccess(ResultEntity response) {

                //refreshActivities(entity);
                Trace.i("delete approval successful!");

            }
        });


    }



    BroadcastReceiver receiver=new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            Bundle bundle=intent.getExtras();
            if(null!=bundle && BROAD_ACTION_APPROVAL_STATUS_UPDATE.equals(action)){

                ActivityEntity activityEntity=(ActivityEntity) bundle.getSerializable(ActivityDetailActivity.PARAMS_ACTIVITY_ENTITY);

                if(null==activityEntity) return;

                refreshActivities(activityEntity);
                if(activityEntity.is_approval){
                    sendApprovalToServer(activityEntity);
                }else{
                    sendDeleteApprovalToServer(activityEntity);
                }
            }
        }
    };


    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(BROAD_ACTION_APPROVAL_STATUS_UPDATE);
        mContext.registerReceiver(receiver,intentFilter);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(receiver);
    }



    /**
     * 刷新活动布局
     *
     * @param
     */
    private void refreshActivities(ActivityEntity entity) {
        List<ActivityEntity> list = activitiesAdapter.getList();
        for (int i = 0; i < list.size(); i++) {

            ActivityEntity activityEntity=list.get(i);
            if (activityEntity.id.equals(entity.id)) {

                activityEntity.is_approval =entity.is_approval;
                activityEntity.approvalcount=entity.approvalcount;
                //
                activitiesAdapter.notifyDataSetChanged();
                return;
            }
        }

    }
}
