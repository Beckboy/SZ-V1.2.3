package com.junhsue.ksee;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.junhsue.ksee.adapter.MyorderListAdapter;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.dto.MyOrderListDTO;
import com.junhsue.ksee.entity.MyOrderListEntity;
import com.junhsue.ksee.entity.OrderDetailsEntity;
import com.junhsue.ksee.fragment.dialog.PayDialogFragment;
import com.junhsue.ksee.net.api.OkHttpILoginImpl;
import com.junhsue.ksee.net.callback.BroadIntnetConnectListener;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.error.NetResultCode;
import com.junhsue.ksee.profile.UserProfileService;
import com.junhsue.ksee.utils.CommonUtils;
import com.junhsue.ksee.utils.PopWindowTokenErrorUtils;
import com.junhsue.ksee.utils.StatisticsUtil;
import com.junhsue.ksee.utils.ToastUtil;
import com.junhsue.ksee.view.ActionBar;
import com.junhsue.ksee.view.CircleImageView;

import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 我的订单列表界面
 */
public class MyOrderListActivity extends OrderPayActivity implements View.OnClickListener,MyorderListAdapter.OrderPayCallback,BroadIntnetConnectListener.InternetChanged{

  //订单列表更新广播
  public final static  String BROAD_ACTION_ORDER_LIST_UPDATE="com.junhuse.ksee.acion_order_list_update";

  private ActionBar mAbar;
  private PtrClassicFrameLayout mPtrFram;
  private ListView mLv;
  private CircleImageView mCircleNoData;
  private TextView mTvNoData;
  public Button btn_reloading;
  private View vHead;
  private BroadIntnetConnectListener con;  //网络连接的广播

  private MyorderListAdapter myorderListAdapter;
  private MyOrderListActivity mContext;

  //当前页数0页开始
  private int pageIndex = 0;
  //当前页数的数据条目
  private int pagesize = 10;
  //是否正在预加载的flag；
  private boolean isRefresh = false;
  //是否数据全部加载完毕
  private boolean isFinish = false;
  //数据总数
  private int totlaPage;

  //点击支付时的下标
  private int payIndex;


  @Override
  protected void onReceiveArguments(Bundle bundle) {
  }

  @Override
  protected int setLayoutId() {
    return R.layout.act_my_order_list;
  }

  @Override
  protected void onInitilizeView() {
    mContext = this;
    initView();
  }

  private void initView() {
    mAbar = (ActionBar) findViewById(R.id.ab_my_order_list);
    mAbar.setOnClickListener(mContext);
    mPtrFram = (PtrClassicFrameLayout) findViewById(R.id.ptrClassicFrameLayout_my_order_list);
    mLv = (ListView) findViewById(R.id.lv_my_order_list);
    vHead = View.inflate(mContext,R.layout.item_myanswer_head,null);
    vHead.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));
    mLv.addHeaderView(vHead);
    mCircleNoData = (CircleImageView) mLv.findViewById(R.id.img_answer_nodata);
    mTvNoData = (TextView) mLv.findViewById(R.id.tv_answer_nodata);
    btn_reloading = (Button) mLv.findViewById(R.id.btn_answer_reloading);
    btn_reloading.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        setDataReset();
      }
    });


    mPtrFram.setPtrHandler(mPtrDefaultHandler2);

    myorderListAdapter = new MyorderListAdapter(mContext,this);

    if (!CommonUtils.getIntnetConnect(mContext)){
      setNoNet();
    }else {
      btn_reloading.setVisibility(View.GONE);
      mContext.alertLoadingProgress();
    }

    mLv.setAdapter(myorderListAdapter);

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
            mPtrFram.refreshComplete();
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
      payIndex=position;
      MyOrderListEntity myiorderlist = (MyOrderListEntity) myorderListAdapter.getItem(position);
      Intent intent2PayDetails = new Intent(mContext,OrderDetailsActivity.class);
      Bundle bundle2PayDetails = new Bundle();
      bundle2PayDetails.putString(OrderDetailsActivity.PARAMS_ORDER_NO,myiorderlist.number);
      bundle2PayDetails.putString(OrderDetailsActivity.PARAMS_ORDER_ID,myiorderlist.id);
      bundle2PayDetails.putString(OrderDetailsActivity.PARAMS_GOODS_ID,myiorderlist.good_id);
      bundle2PayDetails.putInt(OrderDetailsActivity.PARAMS_ORDER_STATUS,myiorderlist.order_status_id);
      bundle2PayDetails.putBoolean(OrderPayResultActivity.PARAMS_IS_FROM_ORDER_LIST,true);
      intent2PayDetails.putExtras(bundle2PayDetails);
      startActivity(intent2PayDetails);
    }
  };

  PtrDefaultHandler2 mPtrDefaultHandler2 = new PtrDefaultHandler2() {
    @Override
    public void onLoadMoreBegin(PtrFrameLayout frame) {
      if (!CommonUtils.getIntnetConnect(mContext)){
        setNoNet();
        return;
      }
      if (pageIndex > totlaPage){
        ToastUtil.getInstance(mContext).setContent(getString(R.string.data_load_completed)).setShow();
        isFinish = true;
        mPtrFram.refreshComplete();
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
  private void getData() {
    String token = UserProfileService.getInstance(mContext).getCurrentLoginedUser().token;
    String mPageIndex = String.valueOf(pageIndex);
    String mPagesize = String.valueOf(pagesize);
    mContext.alertLoadingProgress();
    isRefresh = true;
    OkHttpILoginImpl.getInstance().myOrderList(token, mPageIndex, mPagesize, new RequestCallback<MyOrderListDTO>() {
      @Override
      public void onError(int errorCode, String errorMsg) {
        isRefresh = false;
        mPtrFram.refreshComplete();
        mContext.dismissLoadingDialog();
        SystemClock.sleep(50);
        switch (errorCode){
          case NetResultCode.CODE_LOGIN_STATE_ERROR: //登录态错误，重新登录
            PopWindowTokenErrorUtils.getInstance(mContext).showPopupWindow(R.layout.act_my_order_list);
            break;
          case NetResultCode.SERVER_NO_DATA:
            isFinish = true;
            setNoData(View.VISIBLE);
          default:
            break;
        }
      }

      @Override
      public void onSuccess(MyOrderListDTO response) {
        isRefresh = false;
        mPtrFram.refreshComplete();
        mContext.dismissLoadingDialog();
        if (null == response || response.list.size() == 0) {
          if (pageIndex == 0) {
            setNoData(View.VISIBLE);
          }
          return;
        }
        setNoData(View.GONE);
        if (pageIndex == 0){
          myorderListAdapter.cleanList();
        }

        pageIndex++;
        myorderListAdapter.modifyList(response.list);
        mPtrFram.setMode(PtrFrameLayout.Mode.BOTH);
      }
    });
  }

  private void setNoData(int visibility) {
    mCircleNoData.setImageResource(R.drawable.wu_def_order);
    mTvNoData.setText("你还没有任何订单哦");
    mCircleNoData.setVisibility(visibility);
    mTvNoData.setVisibility(visibility);
  }

  /**
   * 无网络的状态
   */
  private void setNoNet() {
    mCircleNoData.setImageResource(R.drawable.common_def_nonet);
    mTvNoData.setText("网络加载出状况了");
    mCircleNoData.setVisibility(View.VISIBLE);
    mTvNoData.setVisibility(View.VISIBLE);
    ToastUtil.getInstance(mContext).setContent(Constants.INTNET_NOT_CONNCATION).setDuration(Toast.LENGTH_SHORT).setShow();

    btn_reloading.setVisibility(View.VISIBLE);
    pageIndex = 0;
    myorderListAdapter.cleanList();
    myorderListAdapter.modifyList(null);
    mPtrFram.refreshComplete();
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
    isFinish  =false;
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.ll_left_layout:
        finish();
        break;
    }
  }

  /**
   * 点击支付的接口回调
   */
  @Override
  public void topay(int position,String orderId, String ali_sign) {
    payIndex=position;
    final String orderIdC=orderId;
    PayDialogFragment payDialogFragment=PayDialogFragment.newInstance();
    payDialogFragment.show(getSupportFragmentManager(),null);
    payDialogFragment.setIPayOnClickListener(new PayDialogFragment.IPayOnClickListener() {

      @Override
      public void onAliPay() {
        pay(orderIdC, OrderDetailsEntity.OrderPayType.ALIPAY);
      }

      @Override
      public void onWechatPay() {
        pay(orderIdC, OrderDetailsEntity.OrderPayType.WECAHT);
      }
    });
  }

  /**
   * 支付成功的接口回调
   */
  @Override
  protected void onPaySuceess() {
    List<MyOrderListEntity> orderList=myorderListAdapter.getList();
    if(null==orderList || orderList.size()<=0) return;
    MyOrderListEntity myOrderListEntity=orderList.get(payIndex);
    //订单编号
    String orderNo=myOrderListEntity.number;
    //订单id
    String orderId=myOrderListEntity.id;
    int businessId=myOrderListEntity.business_id;
    String goodsId=myOrderListEntity.good_id;

    Bundle bundle=new Bundle();
    bundle.putString(OrderDetailsActivity.PARAMS_ORDER_NO,orderNo);
    bundle.putString(OrderDetailsActivity.PARAMS_ORDER_ID,orderId);
    bundle.putInt(OrderDetailsActivity.PARAMS_ORDER_BUSINESS_ID,businessId);
    bundle.putString(OrderDetailsActivity.PARAMS_GOODS_ID,goodsId);
    bundle.putBoolean(OrderPayResultActivity.PARAMS_IS_FROM_ORDER_LIST,true);
    launchScreen(OrderPayResultActivity.class,bundle);
  }



  /** 更新某条订单状态
   * @param orderId  订单id
   * @param status  订单状态
   * @Author  longer modify
   * */
  private  void updateItemOrderStatus(String orderId,int status){
    List<MyOrderListEntity> orderList= myorderListAdapter.getList();

    if(null==orderList ||orderList.size()<=0)
      return;

      for(int i=0;i<orderList.size();i++){
        MyOrderListEntity myOrderListEntity=orderList.get(i);
        if(orderId.equals(myOrderListEntity.id)){
          myOrderListEntity.order_status_id=status;
          myorderListAdapter.notifyDataSetChanged();
          return;
        }
      }

  }


  /** 订单状态更新*/
  BroadcastReceiver registerReceiver=new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      String action =intent.getAction();
      Bundle bundle=intent.getExtras();
      if(BROAD_ACTION_ORDER_LIST_UPDATE.equals(action) && null!=bundle){
        //业务id
        String orderId=bundle.getString(OrderDetailsActivity.PARAMS_ORDER_ID);
        int orderStatus=bundle.getInt(OrderDetailsActivity.PARAMS_ORDER_PAY_STATUS);
        //
        updateItemOrderStatus(orderId,orderStatus);
      }
    }
  };



  @Override
  protected void onStart() {
    super.onStart();
    IntentFilter intentFilter=new IntentFilter();
    intentFilter.addAction(BROAD_ACTION_ORDER_LIST_UPDATE);
    registerReceiver(registerReceiver,intentFilter);
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


  @Override
  public void onDestroy() {
    super.onDestroy();
    pageIndex = 0;
    unregisterReceiver(registerReceiver);
  }


  @Override
  public void onNetChange(boolean netConnection) {
    if (netConnection){
      if (null == myorderListAdapter.getList() || myorderListAdapter.getList().size() == 0) {
        setDataReset();
      }
    }
  }
}
