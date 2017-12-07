package com.junhsue.ksee.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.junhsue.ksee.OrderPayActivity;
import com.junhsue.ksee.R;
import com.junhsue.ksee.common.MyOrderListOrderState;
import com.junhsue.ksee.entity.GoodsInfo;
import com.junhsue.ksee.entity.MyOrderListEntity;
import com.junhsue.ksee.entity.MyinviteList;
import com.junhsue.ksee.file.ImageLoaderOptions;
import com.junhsue.ksee.net.api.ICourse;
import com.junhsue.ksee.utils.DateUtils;
import com.junhsue.ksee.utils.NumberFormatUtils;
import com.junhsue.ksee.utils.StatisticsUtil;
import com.junhsue.ksee.utils.ToastUtil;
import com.junhsue.ksee.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.w3c.dom.Text;

import java.net.InterfaceAddress;

/**
 * Created by hunter_J on 17/4/19.
 */
public class MyorderListAdapter<T extends MyOrderListEntity> extends MyBaseAdapter<MyOrderListEntity>{

  private Context mContext;
  private LayoutInflater mInflater;
  private OrderPayCallback mOrderPayCallback;

  public MyorderListAdapter(Context context,OrderPayCallback orderPayCallback) {
    mContext = context;
    mOrderPayCallback = orderPayCallback;
    mInflater = LayoutInflater.from(context);
  }

  public interface OrderPayCallback {

    void topay(int position,String orderId, String ali_sign);
  }

  @Override
  protected View getWrappeView(final int position, View convertView, ViewGroup parent) {

    ViewHolder mHolder = null;
    if (convertView == null){
      convertView = mInflater.inflate(R.layout.item_my_order_list,null);
      mHolder = new ViewHolder(convertView);
      convertView.setTag(mHolder);
    }else {
      mHolder = (ViewHolder) convertView.getTag();
    }

    final MyOrderListEntity myorder = mList.get(position);
    if (myorder != null){
      mHolder.tvOrdernum.setText("订单号: "+myorder.number);
      ImageLoader.getInstance().displayImage(myorder.poster,mHolder.imgPoster,
              ImageLoaderOptions.option(R.drawable.img_default_course_suject));
      mHolder.tvTitle.setText(myorder.name);
      //获取业务名称
      mHolder.tvDate.setText(GoodsInfo.GoodsType.getTypeName(myorder.business_id));
      mHolder.tvMoney.setText( NumberFormatUtils.formatPointTwo(myorder.amount));
      mHolder.tvCount.setText("×"+myorder.count+"");
      mHolder.tvAmountFinishPay.setText(NumberFormatUtils.formatPointTwo(myorder.total_amount));

      switch (myorder.order_status_id){
        case 1: // 已支支付
          mHolder.tvResult.setText("交易成功");
          mHolder.tvResult.setTextColor(Color.parseColor("#59B197"));
          mHolder.mRlFinishPay.setVisibility(View.VISIBLE);
          mHolder.mRlWaitPay.setVisibility(View.GONE);
          break;
        case 3: // 支付异常
          mHolder.tvResult.setText("交易异常");
          mHolder.tvResult.setTextColor(Color.RED);
          mHolder.mRlFinishPay.setVisibility(View.VISIBLE);
          mHolder.mRlWaitPay.setVisibility(View.GONE);
          break;
        case 4: // 已关闭
          mHolder.tvResult.setText("交易关闭");
          mHolder.tvResult.setTextColor(Color.parseColor("#C7CDD5"));
          //mHolder.tvAmountFinishPay.setText(myorder.total_amount+"");
          mHolder.mRlFinishPay.setVisibility(View.VISIBLE);
          mHolder.mRlWaitPay.setVisibility(View.GONE);
          break; 
        case 2:
          mHolder.tvResult.setText("等待支付");
          mHolder.tvResult.setTextColor(Color.parseColor("#CDAC8D"));
          mHolder.tvCountPayWait.setText("共"+myorder.count+"份");
          mHolder.mRlFinishPay.setVisibility(View.GONE);
          mHolder.mRlWaitPay.setVisibility(View.VISIBLE);
          mHolder.tvAmountWait.setText(NumberFormatUtils.formatPointTwo(myorder.total_amount));
          mHolder.btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              mOrderPayCallback.topay(position,myorder.id, myorder.ali_sign);
              StatisticsUtil.getInstance(mContext).onCountActionDot("6.2.1");
            }
          });
          break;
      }

    }
    return convertView;
  }

  private class ViewHolder{
    public TextView tvOrdernum; //订单编号
    public TextView tvResult; //交易结果
    public ImageView imgPoster; //订单海报
    public TextView tvTitle; //订单标题
    public TextView tvDate; //订单时间
    public TextView tvMoney; //订单单价
    public TextView tvCount; //订单数量
    public RelativeLayout mRlFinishPay; //交易完成或关闭的栏目
    public TextView tvAmountFinishPay; //交易成功或关闭的交易总金额
    public RelativeLayout mRlWaitPay; //待支付的栏目
    public Button btnPayNow; //立即支付按钮
    public TextView tvAmountWait; //待支付的总金额
    public TextView tvCountPayWait; //待支付的总个数

    public ViewHolder(View view) {
     tvOrdernum = (TextView) view.findViewById(R.id.tv_myorderlist_ordernumber);
     tvResult = (TextView) view.findViewById(R.id.tv_myorderlist_result);imgPoster = (ImageView) view.findViewById(R.id.img_myorderlist);
     tvTitle = (TextView) view.findViewById(R.id.tv_myorderlist_title);
     tvDate = (TextView) view.findViewById(R.id.tv_myorderlist_date);
     tvMoney = (TextView) view.findViewById(R.id.tv_myorderlist_money);
     tvCount = (TextView) view.findViewById(R.id.tv_myorderlist_count);
     mRlFinishPay = (RelativeLayout) view.findViewById(R.id.rl_myorder_list_finish);
     tvAmountFinishPay = (TextView) view.findViewById(R.id.tv_myorderlist_amount);
     mRlWaitPay = (RelativeLayout) view.findViewById(R.id.rl_myorder_list_wait);
     btnPayNow = (Button) view.findViewById(R.id.btn_myorder_list_pay_now);
     tvAmountWait = (TextView) view.findViewById(R.id.tv_myorderlist_waitpay_amount);
     tvCountPayWait = (TextView) view.findViewById(R.id.tv_myorderlist_waitpay_count);
    }
  }

}
