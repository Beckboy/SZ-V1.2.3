package com.junhsue.ksee;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;

import com.junhsue.ksee.fragment.MyAnswerColleageFragment;
import com.junhsue.ksee.fragment.MyAnswerMyinviteFragment;
import com.junhsue.ksee.fragment.MyAnswerQuestionFragment;
import com.junhsue.ksee.net.callback.BroadIntnetConnectListener;
import com.junhsue.ksee.utils.StatisticsUtil;
import com.junhsue.ksee.view.ActionBar;
import com.junhsue.ksee.view.MyAnswerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的问答页
 */
public class MyAnswersActivity extends BaseActivity implements View.OnClickListener, MyAnswerView.IAnswerTabClickListener,BroadIntnetConnectListener.InternetChanged {

  private ActionBar mAbar;
  private MyAnswerView myAnswerView;
  private ViewPager mPager;

  private List<Fragment> pagerList = new ArrayList<>(); //ViewPager的数据源
  private MyAnswerFragPagerAdapter mMyAnswerFragPagerAdapter;
  private FragmentManager fm;
  public BroadIntnetConnectListener con;  //网络连接的广播

  @Override
  protected void onReceiveArguments(Bundle bundle) {}

  @Override
  protected int setLayoutId() {
    return R.layout.act_my_answers;
  }

  @Override
  protected void onInitilizeView() {

    fm = getSupportFragmentManager();

    initView();

    initData();

    initAdapter();

  }

  private void initAdapter() {
    mMyAnswerFragPagerAdapter = new MyAnswerFragPagerAdapter(fm);
    mPager.setAdapter(mMyAnswerFragPagerAdapter);
    mPager.setOnPageChangeListener(onPageChangeListener);
    myAnswerView.setIAnswerTabClickListener(this);
    mPager.setCurrentItem(0);
    mPager.setOffscreenPageLimit(2);
  }

  /**
   * 初始化数据源
   */
  private void initData() {
    pagerList.add(MyAnswerMyinviteFragment.newInstance());
    pagerList.add(MyAnswerQuestionFragment.newInstance());
    pagerList.add(MyAnswerColleageFragment.newInstance());
  }

  private void initView() {
    mAbar = (ActionBar) findViewById(R.id.ab_my_answers);
    mAbar.setOnClickListener(this);
    myAnswerView = (MyAnswerView) findViewById(R.id.answer_tab_view);
    mPager = (ViewPager) findViewById(R.id.viewPager_my_answers);

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
      myAnswerView.setTabStatus(position);
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
    if (netConnection){
      MyAnswerMyinviteFragment invi = (MyAnswerMyinviteFragment) pagerList.get(0);
      if (null == invi.myinviteAdapter.getList()|| invi.myinviteAdapter.getList().size() == 0) {
        invi.setDataReset();
      }
      MyAnswerQuestionFragment ques = (MyAnswerQuestionFragment) pagerList.get(1);
      if (null == ques.myaskAdapter.getList()|| ques.myaskAdapter.getList().size() == 0) {
        ques.setDataReset();
      }
      MyAnswerColleageFragment coll = (MyAnswerColleageFragment) pagerList.get(2);
      if (null == coll.mycolleageAdapter.getList()|| coll.mycolleageAdapter.getList().size() == 0) {
        coll.setDataReset();
      }
    }
  }


  /**
   * 适配器
   */
  public class MyAnswerFragPagerAdapter extends FragmentPagerAdapter{

    public MyAnswerFragPagerAdapter(FragmentManager fm) {
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
