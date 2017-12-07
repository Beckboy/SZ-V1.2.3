package com.junhsue.ksee.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.junhsue.ksee.R;
import com.junhsue.ksee.entity.MyaskList;
import com.junhsue.ksee.entity.MyinviteList;
import com.junhsue.ksee.utils.DateUtils;
import com.junhsue.ksee.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by hunter_J on 17/4/19.
 */

public class MyaskAdapter<T extends MyaskList> extends MyBaseAdapter<MyaskList> {

  private Context mContext;
  private LayoutInflater mInflater;

  public MyaskAdapter(Context context) {
    mContext = context;
    mInflater = LayoutInflater.from(context);
  }

  @Override
  protected View getWrappeView(int position, View convertView, ViewGroup parent) {

    ViewHolder mHolder = null;
    if (convertView == null){
      convertView = mInflater.inflate(R.layout.item_myanswer_myask_list,null);
      mHolder = new ViewHolder(convertView);
      convertView.setTag(mHolder);
    }else {
      mHolder = (ViewHolder) convertView.getTag();
    }

    MyaskList myask = mList.get(position);
    if (myask != null){
      ImageLoader.getInstance().displayImage(myask.avatar,mHolder.circleImageView);
      mHolder.tvName.setText(myask.nickname);
      mHolder.tvTitle.setText(myask.title);
      mHolder.tvContent.setText(myask.content);
      mHolder.tvTopic.setText(myask.fromtopic);
      mHolder.tvAnswer.setText("回答  "+myask.reply);
      mHolder.tvCollect.setText("收藏  "+myask.collect);
      mHolder.tvDate.setText(DateUtils.fromTheCurrentTime(myask.publish_time, System.currentTimeMillis()));
    }
    return convertView;
  }

  private class ViewHolder{
    public CircleImageView circleImageView;
    public TextView tvName;
    public TextView tvTitle;
    public TextView tvContent;
    public TextView tvTopic;
    public TextView tvAnswer;
    public TextView tvCollect;
    public TextView tvDate;

    public ViewHolder(View view) {
      circleImageView = (CircleImageView) view.findViewById(R.id.civ_avatar_myask);
      tvName = (TextView) view.findViewById(R.id.tv_name_myask);
      tvTitle = (TextView) view.findViewById(R.id.tv_title_myask);
      tvContent = (TextView) view.findViewById(R.id.tv_content_myask);
      tvTopic = (TextView) view.findViewById(R.id.tv_topic_myask);
      tvAnswer = (TextView) view.findViewById(R.id.tv_answer_myask);
      tvCollect = (TextView) view.findViewById(R.id.tv_collect_myask);
      tvDate = (TextView) view.findViewById(R.id.tv_date_myask);
    }
  }

}
