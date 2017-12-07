package com.junhsue.ksee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.junhsue.ksee.R;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.entity.MycollectList;
import com.junhsue.ksee.entity.ResultEntity;
import com.junhsue.ksee.net.api.OkHttpSocialCircleImpl;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.utils.DateUtils;
import com.junhsue.ksee.utils.StatisticsUtil;
import com.junhsue.ksee.utils.ToastUtil;
import com.junhsue.ksee.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by hunter_J on 17/4/19.
 */

public class MycolleageAdapter<T extends MycollectList> extends MyBaseAdapter<MycollectList>  {

  private Context mContext;
  private LayoutInflater mInflater;

  public MycolleageAdapter(Context context) {
    mContext = context;
    mInflater = LayoutInflater.from(context);
  }


  @Override
  protected View getWrappeView(final int position, View convertView, ViewGroup parent) {

    ViewHolder mHolder = null;
    if (convertView == null){
      convertView = mInflater.inflate(R.layout.item_myanswer_mycollect_list,null);
      mHolder = new ViewHolder(convertView);
      convertView.setTag(mHolder);
    }else {
      mHolder = (ViewHolder) convertView.getTag();
    }

    final MycollectList mycollectList = mList.get(position);
    if (mycollectList != null){
      ImageLoader.getInstance().displayImage(mycollectList.avatar,mHolder.circleImageView);
      mHolder.tvName.setText(mycollectList.nickname);
      mHolder.tvTitle.setText(mycollectList.title);
      mHolder.tvContent.setText(mycollectList.content);
      mHolder.tvTopic.setText(mycollectList.fromtopic);
      mHolder.tvAnswer.setText("回答  "+mycollectList.reply);
      mHolder.tvCollect.setText("收藏  "+mycollectList.collect);
      mHolder.tvDate.setText(DateUtils.fromTheCurrentTime(mycollectList.publish_time, System.currentTimeMillis()));

      final ViewHolder finalMHolder = mHolder;
      mHolder.ll.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          StatisticsUtil.getInstance(mContext).onCountActionDot("6.1.1");
          if (finalMHolder.cbColleage.isChecked()){
            finalMHolder.cbColleage.setChecked(false);
          }else {
            finalMHolder.cbColleage.setChecked(true);
          }
        }
      });
      mHolder.cbColleage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
          if (!isChecked){
            senderDeleteFavoriteToServer(mycollectList.content_id+"", mycollectList.business_id+"",position);
          } else {
            senderFavoriteToServer(mycollectList.content_id+"", mycollectList.business_id+"",position);
          }
        }
      });

      mHolder.cbColleage.setChecked(mycollectList.is_favorite);

    }
    return convertView;
  }

  private class ViewHolder{
    public LinearLayout ll;
    public CircleImageView circleImageView;
    public TextView tvName;
    public CheckBox cbColleage;
    public TextView tvTitle;
    public TextView tvContent;
    public TextView tvTopic;
    public TextView tvAnswer;
    public TextView tvCollect;
    public TextView tvDate;

    public ViewHolder(View view) {
      ll = (LinearLayout) view.findViewById(R.id.lll);
      circleImageView = (CircleImageView) view.findViewById(R.id.civ_avatar_mycollect);
      tvName = (TextView) view.findViewById(R.id.tv_name_mycollect);
      cbColleage = (CheckBox) view.findViewById(R.id.icb_collect_mycollect);
      tvTitle = (TextView) view.findViewById(R.id.tv_title_mycollect);
      tvContent = (TextView) view.findViewById(R.id.tv_content_mycollect);
      tvTopic = (TextView) view.findViewById(R.id.tv_topic_mycollect);
      tvAnswer = (TextView) view.findViewById(R.id.tv_answer_mycollect);
      tvCollect = (TextView) view.findViewById(R.id.tv_collect_mycollect);
      tvDate = (TextView) view.findViewById(R.id.tv_date_mycollect);

    }
  }


  /**
   * @param content_id
   * @param business_id
   */
  public void senderFavoriteToServer(String content_id, String business_id, final int position) {
    OkHttpSocialCircleImpl.getInstance().senderFavorite(content_id, business_id, new RequestCallback<ResultEntity>() {

      @Override
      public void onError(int errorCode, String errorMsg) {

      }

      @Override
      public void onSuccess(ResultEntity response) {
        mList.get(position).is_favorite = true;
        mList.get(position).collect = mList.get(position).collect + 1;
        ToastUtil.getInstance(mContext).setContent("收藏成功").setShow();
        notifyDataSetChanged();
      }
    });
  }

  /**
   * @param content_id
   * @param business_id
   */
  public void senderDeleteFavoriteToServer(String content_id, String business_id, final int position) {
    OkHttpSocialCircleImpl.getInstance().senderDeleteFavorite(content_id, business_id, new RequestCallback<ResultEntity>() {

      @Override
      public void onError(int errorCode, String errorMsg) {

      }

      @Override
      public void onSuccess(ResultEntity response) {
        mList.get(position).is_favorite = false;
        mList.get(position).collect = mList.get(position).collect - 1;
        ToastUtil.getInstance(mContext).setContent("取消收藏成功").setShow();
        notifyDataSetChanged();
      }
    });
  }


}
