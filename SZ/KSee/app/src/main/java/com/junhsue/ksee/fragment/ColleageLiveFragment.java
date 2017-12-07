package com.junhsue.ksee.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.junhsue.ksee.BaseActivity;
import com.junhsue.ksee.OrderDetailsActivity;
import com.junhsue.ksee.R;
import com.junhsue.ksee.adapter.CommonFragmentPageAdapter;
import com.junhsue.ksee.adapter.LiveFragmentAdapter;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.dto.LiveDTO;
import com.junhsue.ksee.entity.GoodsInfo;
import com.junhsue.ksee.entity.LiveEntity;
import com.junhsue.ksee.entity.OrderDetailsEntity;
import com.junhsue.ksee.net.api.OKHttpCourseImpl;
import com.junhsue.ksee.net.callback.RequestCallback;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * 直播
 * Created by longer on 17/3/22.*/

public class ColleageLiveFragment extends BaseFragment {


    //更新直播支付状态
    public final  static  String BROAD_ACTION_COLLEAGE_LIVE_PAY_STATUS_UPDATE="com.junhsue.ksee.action.live.update";
    
    private ViewPager mViewPager;

    private LiveFragmentAdapter mLiveFragmentAdapter;
    //
    private List<LiveEntity> mLives = new ArrayList<LiveEntity>();

    //记录当前页的下标
    private int mCurrentIndex;

    private BaseActivity mBaseActivity;

    public static ColleageLiveFragment newInstance() {
        ColleageLiveFragment colleageLiveFragment = new ColleageLiveFragment();
        return colleageLiveFragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.act_colleage_live;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mBaseActivity=(BaseActivity)activity;
    }



    @Override
    protected void onInitilizeView(View view) {

        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);

        mLiveFragmentAdapter = new LiveFragmentAdapter(getChildFragmentManager());


        mViewPager.setAdapter(mLiveFragmentAdapter);
        mViewPager.setOnPageChangeListener(onPageChangeListener);

    }


    @Override
    protected void loadData() {
        super.loadData();
        //
        getLiveList();
    }

    @Override
    public void onResume() {
        super.onResume();
        Trace.i("----onResume");
    }

    /**
     * 获取直播列表* */
    private void getLiveList() {

        OKHttpCourseImpl.getInstance().getLiveList(new RequestCallback<LiveDTO>() {

            @Override
            public void onError(int errorCode, String errorMsg) {

            }

            @Override
            public void onSuccess(LiveDTO response) {
                if (null == response.list) return;
//                mLiveFragmentAdapter.clearList();
                mLives.clear();
                mLives.addAll(response.list);
                loadFragment(response.currentpage);
            }
        });
    }


    /**
     * 加载Fragment
     *
     * @param currentPage 当前页
     */
    private void loadFragment(int currentPage) {
        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < mLives.size(); i++) {
            ColleageLiveDetailsFragment colleageLiveDetailsFragment =
                    ColleageLiveDetailsFragment.newInstance(mLives.get(i).live_id); //update
            fragments.add(colleageLiveDetailsFragment);
        }

        if(fragments.size()>0) {
            mLiveFragmentAdapter.modifyData(fragments);
        }
        mViewPager.setCurrentItem(currentPage, false);
        //
        mCurrentIndex=currentPage;
    }


    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mCurrentIndex=position;
            ColleageLiveDetailsFragment liveDetailsFragment=(ColleageLiveDetailsFragment) mLiveFragmentAdapter.getList().get(position);
            //liveDetailsFragment.setLiveTag(position,mLiveFragmentAdapter.getList().size());
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action=intent.getAction();
            Bundle bundle=intent.getExtras();
            //更新直播支付状态
            if(BROAD_ACTION_COLLEAGE_LIVE_PAY_STATUS_UPDATE.equalsIgnoreCase(action)){
                if(null==bundle) return;
                 //商品id
                 String  goodId=bundle.getString(OrderDetailsActivity.PARAMS_GOODS_ID);
                 //业务id
                 int  businessId=bundle.getInt(OrderDetailsActivity.PARAMS_ORDER_BUSINESS_ID);
                 //支付状态,默认为支付成功
                 int     payStatus=bundle.getInt(OrderDetailsActivity.PARAMS_ORDER_PAY_STATUS,
                         OrderDetailsEntity.OrderStatus.PAY_OK);
                modifyLivePayStatus(goodId,businessId,payStatus);
                mLiveFragmentAdapter.notifyDataSetChanged();
                Trace.i("udapte success");
            }
        }
    };


    /** 修改直播支付状态
     *
     * @param goodId  商品id
     * @param businessId  业务id
     * @param payStatus 直播状态
     * */
    private void modifyLivePayStatus(String goodId,int businessId, int payStatus){
        int count=mLives.size();
        for(int i=0;i<count;i++){
            LiveEntity liveEntity= mLives.get(i);
            if(liveEntity.live_id.equals(goodId) && liveEntity.business_id==businessId){
                liveEntity.status=payStatus;
                return;
            }
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(BROAD_ACTION_COLLEAGE_LIVE_PAY_STATUS_UPDATE);
        mBaseActivity.registerReceiver(broadcastReceiver,intentFilter);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        mBaseActivity.unregisterReceiver(broadcastReceiver);
    }
}
