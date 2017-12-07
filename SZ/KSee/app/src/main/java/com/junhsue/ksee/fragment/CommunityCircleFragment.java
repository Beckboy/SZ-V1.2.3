package com.junhsue.ksee.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.junhsue.ksee.CircleDetailActivity;
import com.junhsue.ksee.R;
import com.junhsue.ksee.adapter.CircleAdapter;
import com.junhsue.ksee.adapter.ImageSelectAdapter;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.dto.CircleListDTO;
import com.junhsue.ksee.entity.CircleEntity;
import com.junhsue.ksee.entity.CommonResultEntity;
import com.junhsue.ksee.entity.MsgInfoEntity;
import com.junhsue.ksee.fragment.dialog.CircleCommonDialog;
import com.junhsue.ksee.net.api.OkHttpCircleImpI;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.view.CommonListView;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 单个圈子
 * Created by longer on 17/10/25.
 */

public class CommunityCircleFragment extends BaseFragment implements CircleAdapter.ICircleListener {

    //更新我的圈子
    public static  final String BROAD_ACTION_UPDATE_CIRCLE="com.junhsue.ksee.action_udpate_circle";
    //
    public static  final String PARAMS_CIRCLE="params_circle";
    //圈子是否收藏
    public static  final String PARAMS_CIRCLE_IS_FAVOURITE="params_is_favourite";

    //圈子父类id
    private static final String PARAMS_CIRCLE_ID = "params_circle_parent_id";

    private PtrClassicFrameLayout mPtrCircle;
    //
    private ListView mListView;
    //
    private CircleAdapter<CircleEntity> mCircleAdapter;
    //
    private Context mContext;
    //
    private String mCircleParentId;

    @Override
    protected int setLayoutId() {
        return R.layout.act_community_circle;
    }

    public static CommunityCircleFragment newInstance(String circleId) {
        CommunityCircleFragment communityCircleFragment = new CommunityCircleFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARAMS_CIRCLE_ID, circleId);
        communityCircleFragment.setArguments(bundle);

        return communityCircleFragment;
    }


    @Override
    protected void onInitilizeView(View view) {

        mContext = getContext();
        mPtrCircle = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_circle);
        mListView = (ListView) view.findViewById(R.id.lv_circle);
        //
        mCircleAdapter = new CircleAdapter<>(mContext);
        mListView.setAdapter(mCircleAdapter);
        //
        mPtrCircle.setPtrHandler(ptrDefaultHandler);

        mCircleParentId = getArguments().getString(PARAMS_CIRCLE_ID, "");
        getCircleList();
        //
        mCircleAdapter.setICircleListener(this);
        mListView.setOnItemClickListener(onItemClickListener);
    }


    PtrDefaultHandler ptrDefaultHandler = new PtrDefaultHandler() {

        @Override
        public void onRefreshBegin(PtrFrameLayout frame) {

            getCircleList();
        }
    };


    AdapterView.OnItemClickListener onItemClickListener=new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            CircleEntity circleEntity=mCircleAdapter.getList().get(position);
            Intent intent=new Intent(mContext,CircleDetailActivity.class);
            intent.putExtra(CircleDetailActivity.PARAMS_CIRCLE_ID,circleEntity.id);
            intent.putExtra(CircleDetailActivity.PARAMS_CIRLCE_TITLE,circleEntity.name);
            intent.putExtra(CircleDetailActivity.PARAMS_CIRLCE_NOTICE,circleEntity.notice);
            startActivity(intent);

        }
    };

    private void getCircleList() {

        OkHttpCircleImpI.getInstance().getCircleList(mCircleParentId,
                new RequestCallback<CircleListDTO>() {

                    @Override
                    public void onError(int errorCode, String errorMsg) {
                        mPtrCircle.refreshComplete();
                    }

                    @Override
                    public void onSuccess(CircleListDTO response) {
                        mPtrCircle.refreshComplete();
                        if (null == response || response.list.size() == 0) return;
                        mCircleAdapter.cleanList();
                        mCircleAdapter.modifyList(response.list);
                    }
                });
    }

    /**
     * 显示关注成功弹窗
     */
    private void showDialog(String msg, int drawableId) {
        MsgInfoEntity entity = new MsgInfoEntity();
        entity.drawableId = drawableId;
        entity.msgInfo = msg;
        CircleCommonDialog circleCommonDialog = CircleCommonDialog.newInstance(entity);
        circleCommonDialog.show(getFragmentManager(), "");
    }


    @Override
    public void addFavourite(int position) {
        String circleId = updateFavouriteStatus(position);
        //
        CircleEntity circleEntity = mCircleAdapter.getList().get(position);
        //
        Intent intent=new Intent(CommunityMyCircleFragment.BROAD_ACTION_UPDATE_MY_CIRCLE);
        intent.putExtra(CommunityMyCircleFragment.PARAMS_CIRCLE,circleEntity);
        intent.putExtra(CommunityMyCircleFragment.PARAMS_CIRCLE_IS_FAVOURITE,circleEntity.is_concern);
        mContext.sendBroadcast(intent);
        if (circleEntity.is_concern) {
            favouriteCircle(circleId);
            showDialog("关注成功",R.drawable.icon_dialog_success);
        } else {
            unFavouriteCircle(circleId);
            showDialog("取消关注",R.drawable.icon_dialog_success);
        }

    }

    /**
     * 关注圈子
     *
     * @param circleId 圈子id
     */
    private void favouriteCircle(String circleId) {

        OkHttpCircleImpI.getInstance().circleFavourite(circleId, new RequestCallback<CommonResultEntity>() {
            @Override
            public void onError(int errorCode, String errorMsg) {
                Trace.i("circle favourite fail!");
            }

            @Override
            public void onSuccess(CommonResultEntity response) {
                Trace.i("circle favourite successful!");
            }
        });
    }

    private void unFavouriteCircle(String circleId) {

        OkHttpCircleImpI.getInstance().circleUnFavourite(circleId, new RequestCallback<CommonResultEntity>() {
            @Override
            public void onError(int errorCode, String errorMsg) {
                Trace.i("circle  unFavourite fail!");

            }

            @Override
            public void onSuccess(CommonResultEntity response) {
                Trace.i("circle  unFavourite successful!");

            }
        });
    }


    /**
     * 更新收藏状态
     *
     * @param postion selected  index for the item adapter
     * @return 返回圈子id
     */
    private String updateFavouriteStatus(int postion) {

        List<CircleEntity> list = mCircleAdapter.getList();

        CircleEntity circleEntity = list.get(postion);
        if (circleEntity.is_concern) {
            circleEntity.is_concern = false;
        } else {
            circleEntity.is_concern = true;
        }
        mCircleAdapter.notifyDataSetChanged();

        return circleEntity.id;
    }

    /**
     * 根据关注，和取消关注选项，推荐圈子选项重新设置我的圈子
     *
     * @param circleEntity  圈子
     * @param isAdd 是否添加到我的圈子 true  添加,false 取消添加
     */
    private void updateCircle(CircleEntity  circleEntity,boolean isAdd) {
        for(int i=0;i<mCircleAdapter.getList().size();i++){
            if(circleEntity.id.equals(mCircleAdapter.getList().get(i).id)){
                mCircleAdapter.getList().get(i).is_concern = isAdd;
                break;
            }
        }
        mCircleAdapter.notifyDataSetChanged();
    }

    /**
     * 是否已关注
     *
     * @param position
     * @return
     */
    private boolean isFavourite(int position) {
        CircleEntity circleEntity = mCircleAdapter.getList().get(position);
        //
        return circleEntity.is_concern;
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if(action.equals(BROAD_ACTION_UPDATE_CIRCLE)
                    && null!=intent.getExtras()){
                CircleEntity circleEntity=(CircleEntity)intent.getExtras().getSerializable(PARAMS_CIRCLE);
                boolean  isFAVOURITE=intent.getExtras().getBoolean(PARAMS_CIRCLE_IS_FAVOURITE);
                updateCircle(circleEntity,isFAVOURITE);
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(BROAD_ACTION_UPDATE_CIRCLE);
        mContext.registerReceiver(broadcastReceiver,intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(broadcastReceiver);
    }
}
