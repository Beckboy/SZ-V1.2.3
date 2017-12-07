package com.junhsue.ksee.fragment;

import android.app.Activity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.junhsue.ksee.BaseActivity;
import com.junhsue.ksee.R;
import com.junhsue.ksee.adapter.MyinviteAdapter;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.dto.MyinviteDTO;
import com.junhsue.ksee.entity.MyinviteList;
import com.junhsue.ksee.net.api.OkHttpILoginImpl;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.error.NetResultCode;
import com.junhsue.ksee.profile.UserProfileService;
import com.junhsue.ksee.utils.CommonUtils;
import com.junhsue.ksee.utils.StartActivityUtils;
import com.junhsue.ksee.utils.ToastUtil;
import com.junhsue.ksee.view.CircleImageView;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 我的问答 —— 被邀请页
 * Created by hunter_J on 17/4/18.
 */

public class MyAnswerMyinviteFragment extends BaseFragment {

  private static MyAnswerMyinviteFragment myAnswerMyinviteFragment;

  private PtrClassicFrameLayout mPtrFrame;
  private ListView mLv;
  private CircleImageView mCircleNoData;
  private TextView mTvNoData;
  public MyinviteAdapter myinviteAdapter;
  private BaseActivity mContext;
  public Button btn_reloading;
  private View vHead;

  //当前页数0页开始
  private int pageIndex = 0;
  //当前页数的数据条目
  private int pagesize = 10;
  //数据总数
  private int totlaPage;
  //是否正在预加载的flag；
  private boolean isRefresh = false;
  //是否数据全部加载完毕
  private boolean isFinish = false;


  public static MyAnswerMyinviteFragment newInstance(){
    if (myAnswerMyinviteFragment == null) {
      myAnswerMyinviteFragment = new MyAnswerMyinviteFragment();
    }
    return myAnswerMyinviteFragment;
  }

  @Override
  protected int setLayoutId() {
    return R.layout.fm_my_answer_myinvite;
  }

  @Override
  protected void onInitilizeView(View view) {
    mPtrFrame = (PtrClassicFrameLayout) view.findViewById(R.id.ptrClassicFrameLayout_myinvite);
    mLv = (ListView) view.findViewById(R.id.lv_myanswer_myinvite);
    vHead = View.inflate(mContext,R.layout.item_myanswer_head,null);
    vHead.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
    mLv.addHeaderView(vHead,null,true);
    mLv.setHeaderDividersEnabled(false);
    mCircleNoData = (CircleImageView) mLv.findViewById(R.id.img_answer_nodata);
    mTvNoData = (TextView) mLv.findViewById(R.id.tv_answer_nodata);
    btn_reloading = (Button) mLv.findViewById(R.id.btn_answer_reloading);
    btn_reloading.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        setDataReset();
      }
    });

    mPtrFrame.setPtrHandler(mPtrDefaultHandler2);

    myinviteAdapter = new MyinviteAdapter(mContext);

    if (!CommonUtils.getIntnetConnect(mContext)){
      setNoNet();
    }else {
      btn_reloading.setVisibility(View.GONE);
      mContext.alertLoadingProgress();
    }

    mLv.setAdapter(myinviteAdapter);

    mLv.setOnItemClickListener(itemClickListener);
    mLv.setOnScrollListener(new AbsListView.OnScrollListener() {
      @Override
      public void onScrollStateChanged(AbsListView view, int scrollState) {
      }
      @Override
      public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem + pagesize/2 + visibleItemCount == totalItemCount){
          if (isRefresh) return;
          if (isFinish) return;

          if (!CommonUtils.getIntnetConnect(mContext)){
            setNoNet();
            return;
          }
          btn_reloading.setVisibility(View.GONE);
          if (pageIndex > totlaPage){
            isFinish = true;
            mPtrFrame.refreshComplete();
          }else {
            getData();
          }
        }
      }
    });
  }

  AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      if (position>0){
        position -= 1;
      }else {
        return;
      }
      MyinviteList myinvitelist = (MyinviteList) myinviteAdapter.getItem(position);
      StartActivityUtils.startQuestionDetailActivity(mContext,myinvitelist.id+"");
    }
  };


  PtrDefaultHandler2 mPtrDefaultHandler2 = new PtrDefaultHandler2() {
    @Override
    public void onLoadMoreBegin(PtrFrameLayout frame) {
      if (!CommonUtils.getIntnetConnect(mContext)){
        setNoNet();
        return;
      }
      btn_reloading.setVisibility(View.GONE);
      if (pageIndex > totlaPage){
        isFinish = true;
        ToastUtil.getInstance(mContext).setContent(getString(R.string.data_load_completed)).setShow();
        mPtrFrame.refreshComplete();
      }else {
        getData();
      }
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
      setDataReset();
    }
  };

  /**
   * 获取被邀请的条目数据
   */
  public void getData() {
    isRefresh = true;
    String token = UserProfileService.getInstance(mContext).getCurrentLoginedUser().token;
    final String mPageIndex = String.valueOf(pageIndex);
    String mPagesize = String.valueOf(pagesize);
    OkHttpILoginImpl.getInstance().myanswerInvite(token, mPageIndex, mPagesize, new RequestCallback<MyinviteDTO>() {
      @Override
      public void onError(int errorCode, String errorMsg) {
        isRefresh = false;
        mPtrFrame.refreshComplete();
        mContext.dismissLoadingDialog();
        switch (errorCode){
          case NetResultCode.SERVER_NO_DATA:
            if (pageIndex == 0) {
              setNoData(View.VISIBLE);
              pageIndex = 0;
              myinviteAdapter.cleanList();
              myinviteAdapter.modifyList(null);
            }else {
              isFinish = true;
              ToastUtil.getInstance(mContext).setContent(getString(R.string.data_load_completed)).setShow();
            }
            break;
        }
      }

      @Override
      public void onSuccess(MyinviteDTO response) {
        isRefresh = false;
        mPtrFrame.refreshComplete();
        mContext.dismissLoadingDialog();
        if (null == response || response.list.size() == 0){
          if (pageIndex == 0){
            setNoData(View.VISIBLE);
          }
          return;
        }
        setNoData(View.GONE);
        if (pageIndex == 0){
          myinviteAdapter.cleanList();
        }
        pageIndex++;
        myinviteAdapter.modifyList(response.list);
        totlaPage = response.total/pagesize;
        if (response.list.size() >= pagesize){
          mPtrFrame.setMode(PtrFrameLayout.Mode.BOTH);
        }
      }
    });
  }


  private void setNoData(int visibility) {
//    if (visibility == View.VISIBLE){
//      mLv.addHeaderView(vHead);
//    }else {
//      mLv.removeHeaderView(vHead);
//    }
    vHead.setVisibility(visibility);
    mCircleNoData.setImageResource(R.drawable.wu_def_invitation);
    mTvNoData.setText("还没人邀请你回答问题哦");
    mCircleNoData.setVisibility(visibility);
    mTvNoData.setVisibility(visibility);
  }

  /**
   * 无网络的状态
   */
  private void setNoNet() {
    vHead.setVisibility(View.VISIBLE);
    mCircleNoData.setImageResource(R.drawable.common_def_nonet);
    mTvNoData.setText("网络加载出状况了");
    mCircleNoData.setVisibility(View.VISIBLE);
    mTvNoData.setVisibility(View.VISIBLE);
    ToastUtil.getInstance(mContext).setContent(Constants.INTNET_NOT_CONNCATION).setDuration(Toast.LENGTH_SHORT).setShow();
    btn_reloading.setVisibility(View.VISIBLE);
    pageIndex = 0;
    myinviteAdapter.cleanList();
    myinviteAdapter.modifyList(null);
    mPtrFrame.refreshComplete();
  }

  /**
   * 数据重新刷新
   */
  public void setDataReset(){
    if (!CommonUtils.getIntnetConnect(mContext)){
      setNoNet();
      return;
    }
    btn_reloading.setVisibility(View.GONE);
    pageIndex = 0;
    getData();
    isFinish = false;
  }

  @Override
  public void onDestroy() {
    pageIndex = 0;
    super.onDestroy();
  }

  @Override
  public void onDestroyView() {
    pageIndex = 0;
    super.onDestroyView();
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    mContext = (BaseActivity) activity;
  }
}
