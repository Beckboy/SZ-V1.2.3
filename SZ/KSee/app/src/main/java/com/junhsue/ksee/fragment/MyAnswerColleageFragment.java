package com.junhsue.ksee.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.easeui.EaseConstant;
import com.junhsue.ksee.BaseActivity;
import com.junhsue.ksee.R;
import com.junhsue.ksee.adapter.MycolleageAdapter;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.common.IBusinessType;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.dto.MycollectDTO;
import com.junhsue.ksee.entity.MycollectList;
import com.junhsue.ksee.entity.QuestionEntity;
import com.junhsue.ksee.net.api.OkHttpILoginImpl;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.error.NetResultCode;
import com.junhsue.ksee.profile.UserProfileService;
import com.junhsue.ksee.utils.CommonUtils;
import com.junhsue.ksee.utils.StartActivityUtils;
import com.junhsue.ksee.utils.ToastUtil;
import com.junhsue.ksee.view.CircleImageView;

import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 我的问答 —— 收藏界面
 * Created by hunter_J on 17/4/18.
 */

public class MyAnswerColleageFragment extends BaseFragment {

  //收藏列表更新广播
  public final static  String BROAD_ACTION_COLLEAGE_LIST_UPDATE="com.junhuse.ksee.acion_colleage_list_update";
  public final static  String BROAD_ACTION_COLLEAGE_LIST_ENTITY="com.junhuse.ksee.acion_colleage_list_update";

  private static MyAnswerColleageFragment myAnswerColleageFragment;

  private PtrClassicFrameLayout mPtrFrame;
  private ListView mLv;
  private CircleImageView mCircleNoData;
  private TextView mTvNoData;
  public MycolleageAdapter mycolleageAdapter;
  private BaseActivity mContext;
  public Button btn_reloading;
  private View vHead;

  //当前页数0页开始
  private int pageIndex = 0;
  //当前页数的数据条目
  private int pagesize = 10;
  //数据总数
  private long totlaPage;
  //是否正在预加载的flag；
  private boolean isRefresh = false;
  //是否数据全部加载完毕
  private boolean isFinish = false;

  //更新收藏状态的位置
  private int jump_index = -1;
  //更新收藏状态的回答实体对象
  private MycollectList mycollectList;

  public static MyAnswerColleageFragment newInstance(){
    if (myAnswerColleageFragment == null) {
      myAnswerColleageFragment = new MyAnswerColleageFragment();
    }
    return myAnswerColleageFragment;
  }

  @Override
  protected int setLayoutId() {
    return R.layout.fm_my_answer_colleage;
  }

  @Override
  protected void onInitilizeView(View view) {
    mPtrFrame = (PtrClassicFrameLayout) view.findViewById(R.id.ptrClassicFrameLayout_mycolleage);
    mLv = (ListView) view.findViewById(R.id.lv_myanswer_mycolleage);
    vHead = View.inflate(mContext,R.layout.item_myanswer_head,null);
    vHead.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
    mLv.addHeaderView(vHead,null,true);
    mLv.setHeaderDividersEnabled(true);
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

    mycolleageAdapter = new MycolleageAdapter(mContext);

    if (!CommonUtils.getIntnetConnect(mContext)){
      setNoNet();
    }else {
      btn_reloading.setVisibility(View.GONE);
      mContext.alertLoadingProgress();
    }
    mLv.setAdapter(mycolleageAdapter);
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
      MycollectList mycolleagelist = (MycollectList) mycolleageAdapter.getItem(position);
      StartActivityUtils.startQuestionDetailActivity(mContext,mycolleagelist.content_id+"");

      jump_index = position;

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
//      if ( currentsize == 0 ){
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
   * 获取我收藏的条目数据
   */
  private void getData() {
    isRefresh = true;
    String token = UserProfileService.getInstance(mContext).getCurrentLoginedUser().token;
    String mPageIndex = String.valueOf(pageIndex);
    String mPagesize = String.valueOf(pagesize);
    String business_id = IBusinessType.QUESTION+"";
    OkHttpILoginImpl.getInstance().myanswerMycollect(token, business_id, mPageIndex, mPagesize, new RequestCallback<MycollectDTO>() {
      @Override
      public void onError(int errorCode, String errorMsg) {
        mPtrFrame.refreshComplete();
        isRefresh = false;
        switch (errorCode){
          case NetResultCode.SERVER_NO_DATA:
            if (pageIndex == 0) {
              setNoData(View.VISIBLE);
              mycolleageAdapter.cleanList();
              mycolleageAdapter.modifyList(null);
            }else {
              isFinish = true;
              ToastUtil.getInstance(mContext).setContent(getString(R.string.data_load_completed)).setShow();
            }
            break;
        }
      }

      @Override
      public void onSuccess(MycollectDTO response) {
        mPtrFrame.refreshComplete();
        isRefresh = false;
        setNoData(View.GONE);
        if (null == response || response.result.size() == 0) {
          if (pageIndex == 0) {
            setNoData(View.VISIBLE);
          }
        }
        if (pageIndex == 0){
          mycolleageAdapter.cleanList();
        }
        pageIndex++;
        Log.i("colleage",response.result.toString());
        mycolleageAdapter.modifyList(getList(response.result));
        totlaPage =  (response.totalnum/pagesize);
        if (response.result.size() >= pagesize){
          mPtrFrame.setMode(PtrFrameLayout.Mode.BOTH);
        }
      }
    });
  }


  BroadcastReceiver broadChangeColleage = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      Trace.i("接收到更新收藏状态");
      String action=intent.getAction();
      if( !BROAD_ACTION_COLLEAGE_LIST_UPDATE.equals(action)) return;
      if ( null == intent.getSerializableExtra(BROAD_ACTION_COLLEAGE_LIST_ENTITY) ) return;

      QuestionEntity questionEntity = (QuestionEntity) intent.getSerializableExtra(BROAD_ACTION_COLLEAGE_LIST_ENTITY);
      if (questionEntity.is_favorite){
        mycollectList = (MycollectList) mycolleageAdapter.getList().get(jump_index);
        mycolleageAdapter.getList().remove(jump_index);
      }else {
        mycolleageAdapter.getList().add(jump_index,mycollectList);
      }
      mycolleageAdapter.notifyDataSetChanged();

    }
  };


  /**
   * 如果是否收藏的参数为false，将该参数从列表中移除；
   * 现在后台处理为取消收藏从收藏列表直接删除数据，故该逻辑被注销
   * @param list
   * @return
   */
  private List<MycollectList> getList(List<MycollectList> list) {
//    for (int i = 0; i < list.size();i++){
//      if (!list.get(i).is_favorite){
//        list.remove(i);
//      }
//    }
    return list;
  }


  private void setNoData(int visibility) {
//    if (visibility == View.VISIBLE){
//      mLv.addHeaderView(vHead);
//    }else {
//      mLv.removeHeaderView(vHead);
//    }
    vHead.setVisibility(visibility);
    mCircleNoData.setImageResource(R.drawable.wu_def_collection);
    mTvNoData.setText("你还没有任何收藏哦");
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
    mycolleageAdapter.cleanList();
    mycolleageAdapter.modifyList(null);
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
    mContext.unregisterReceiver(broadChangeColleage);
    Trace.i("销毁广播");
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

  @Override
  public void onStart() {
    super.onStart();
    IntentFilter intentFilter=new IntentFilter();
    intentFilter.addAction(BROAD_ACTION_COLLEAGE_LIST_UPDATE);
    mContext.registerReceiver(broadChangeColleage,intentFilter);
    Trace.i("注册广播");
  }

}
