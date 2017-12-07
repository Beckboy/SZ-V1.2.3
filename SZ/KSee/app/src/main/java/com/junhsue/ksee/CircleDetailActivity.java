package com.junhsue.ksee;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.junhsue.ksee.fragment.CircleDetailAllFragment;
import com.junhsue.ksee.fragment.CircleDetailBestFragment;
import com.junhsue.ksee.fragment.MyAnswerColleageFragment;
import com.junhsue.ksee.fragment.MyAnswerMyinviteFragment;
import com.junhsue.ksee.fragment.MyAnswerQuestionFragment;
import com.junhsue.ksee.net.callback.BroadIntnetConnectListener;
import com.junhsue.ksee.utils.StatisticsUtil;
import com.junhsue.ksee.view.ActionBar;
import com.junhsue.ksee.view.PostListTabView;

import java.util.ArrayList;
import java.util.List;

public class CircleDetailActivity extends BaseActivity implements View.OnClickListener, PostListTabView.IAnswerTabClickListener,BroadIntnetConnectListener.InternetChanged{

    //圈子id
    public static  final String PARAMS_CIRCLE_ID="params_circle_id";
    //圈子标题
    public static  final String PARAMS_CIRLCE_TITLE="params_circle_title";
    //圈子公告
    public static  final String PARAMS_CIRLCE_NOTICE="params_circle_notice";

    private ActionBar mAbr;
    private PostListTabView mTabPost;
    private ViewPager mPager;
    public ImageView mImgPost;
    //滑动状态
    public boolean isOnScroll = false;

    private List<Fragment> pagerList = new ArrayList<>(); //ViewPager的数据源
    private PoserFragPagerAdapter mCircleDetailsFragPagerAdapter;
    private FragmentManager fm;
    public BroadIntnetConnectListener con;  //网络连接的广播
    //
    private String mCircleId;
    //
    private String mCircleTitle;
    //
    private String mCircleNotice;

    @Override
    protected void onReceiveArguments(Bundle bundle) {
        mCircleId=bundle.getString(PARAMS_CIRCLE_ID,"");
        mCircleTitle=bundle.getString(PARAMS_CIRLCE_TITLE,"");
        mCircleNotice = bundle.getString(PARAMS_CIRLCE_NOTICE,"");
    }

    @Override
    protected int setLayoutId() {
        return R.layout.act_circle_detail;
    }

    @Override
    protected void onInitilizeView() {

        fm = getSupportFragmentManager();

        initView();

        initData();

        initAdapter();
    }

    private void initAdapter() {
        mCircleDetailsFragPagerAdapter = new PoserFragPagerAdapter(fm);
        mPager.setAdapter(mCircleDetailsFragPagerAdapter);
        mPager.setOnPageChangeListener(onPageChangeListener);
        mTabPost.setIPostTabClickListener(this);
        mPager.setCurrentItem(0);
    }

    /**
     * 初始化数据源
     */
    private void initData() {
        pagerList.add(CircleDetailAllFragment.newInstance(mCircleId,null,mCircleNotice));
        pagerList.add(CircleDetailBestFragment.newInstance(mCircleId,"1"));
    }

    private void initView() {
        mAbr = (ActionBar) findViewById(R.id.ab_circle_detail);
        mAbr.setOnClickListener(this);
        mTabPost = (PostListTabView) findViewById(R.id.tab_circle_view);
        mPager = (ViewPager) findViewById(R.id.viewPager_circle_detail);
        mImgPost = (ImageView) findViewById(R.id.iv_post);
        mImgPost.setOnClickListener(this);

        mAbr.setTitle(mCircleTitle);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_left_layout:
                finish();
                break;
            case R.id.iv_post:
                /** 发帖界面的跳转 **/
                Intent intent2SendPost = new Intent(this, SendPostActivity.class);
                Bundle bundle2SendPost = new Bundle();
                bundle2SendPost.putInt(SendPostActivity.JUMP_BY, SendPostActivity.JUMP_BY_SON);
                bundle2SendPost.putString(SendPostActivity.JUMP_CIRCLE_ID, mCircleId);
                bundle2SendPost.putString(SendPostActivity.JUMP_CIRCLE_NAME, mCircleTitle );
                intent2SendPost.putExtras(bundle2SendPost);
                startActivity(intent2SendPost);
                break;
        }
    }
    /**
     * Tab的点击事件
     * @param index
     */
    @Override
    public void onClick(int index) {
        mPager.setCurrentItem(index);
    }


    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            mTabPost.setTabStatus(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    /**
     * 网络状态发生改变的监听回调
     * @param netConnection
     */
    @Override
    public void onNetChange(boolean netConnection) {
//        if (netConnection){
//            CircleDetailAllFragment invi = (CircleDetailAllFragment) pagerList.get(0);
//            if (null == invi.myinviteAdapter.getList()|| invi.myinviteAdapter.getList().size() == 0) {
//                invi.setDataReset();
//            }
//            MyAnswerMyinviteFragment ques = (MyAnswerMyinviteFragment) pagerList.get(1);
//            if (null == ques.myinviteAdapter.getList()|| ques.myinviteAdapter.getList().size() == 0) {
//                ques.setDataReset();
//            }
//        }
    }


    /**
     * 适配器
     */
    public class PoserFragPagerAdapter extends FragmentPagerAdapter {

        public PoserFragPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return pagerList.get(position);
        }

        @Override
        public int getCount() {
            return pagerList.size();
        }

    }


    @Override
    protected void onResume() {
        if (con == null) {
            con = new BroadIntnetConnectListener();
            con.setInternetChanged(this);
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(con, filter);
        System.out.println("注册");
        super.onResume();
        StatisticsUtil.getInstance(this).onResume(this);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(con);
        System.out.println("注销");
        super.onPause();
        StatisticsUtil.getInstance(this).onPause(this);
    }

}
