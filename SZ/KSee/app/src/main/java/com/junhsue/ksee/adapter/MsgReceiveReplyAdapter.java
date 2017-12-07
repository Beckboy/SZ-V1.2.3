package com.junhsue.ksee.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.junhsue.ksee.R;
import com.junhsue.ksee.entity.MsgCenterReceiveReplyEntity;
import com.junhsue.ksee.entity.MyinviteList;
import com.junhsue.ksee.utils.DateUtils;
import com.junhsue.ksee.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by hunter_J on 17/11/14.
 */

public class MsgReceiveReplyAdapter<T extends MsgCenterReceiveReplyEntity> extends MyBaseAdapter<MsgCenterReceiveReplyEntity> {

  private Context mContext;
  private LayoutInflater mInflater;
  private int maxSize = 0;

  public MsgReceiveReplyAdapter(Context context) {
    mContext = context;
    mInflater = LayoutInflater.from(context);
  }

  @Override
  public int getCount() {
    return maxSize == 0 ? mList.size() : maxSize;
  }

  public void setMaxSize(int size){
    maxSize = size;
  }

  @Override
  protected View getWrappeView(int position, View convertView, ViewGroup parent) {

    ViewHolder mHolder = null;
    if (convertView == null){
      convertView = mInflater.inflate(R.layout.item_msg_center_reply_list,null);
      mHolder = new ViewHolder(convertView);
      convertView.setTag(mHolder);
    }else {
      mHolder = (ViewHolder) convertView.getTag();
    }

    if (position == mList.size() && maxSize == mList.size()+1){
      mHolder.mLlContent.setVisibility(View.GONE);
      mHolder.mRlHint.setVisibility(View.VISIBLE);
      return convertView;
    }else {
      mHolder.mLlContent.setVisibility(View.VISIBLE);
      mHolder.mRlHint.setVisibility(View.GONE);
    }

    MsgCenterReceiveReplyEntity replyEntity = mList.get(position);
    if (replyEntity != null && replyEntity.list != null ){
      if (replyEntity.list.reply_avatar != null)
        ImageLoader.getInstance().displayImage(replyEntity.list.reply_avatar,mHolder.mCircleImageView);
      if (replyEntity.list.reply_nickname != null)
        mHolder.mTvName.setText(replyEntity.list.reply_nickname);
      if (replyEntity.initial_time != 0)
        mHolder.mTvDate.setText(DateUtils.formatCurrentTime(replyEntity.initial_time, System.currentTimeMillis()));
      if (replyEntity.list.comment_content != null)
        mHolder.mTvMsg.setText(replyEntity.list.comment_content);
      if (replyEntity.list.comment_business_id != null) {
        int bisness_id = Integer.parseInt(replyEntity.list.comment_business_id);
        switch (bisness_id) {
          case 14: //帖子被回复
            mHolder.mTvContent.setText("评论 我的帖子: " + replyEntity.list.post_content_title);
            break;
          case 15: //评论被回复
            mHolder.mTvContent.setText("回复 我的评论: " + replyEntity.list.comment_content_title);
            break;
        }
      }
      switch (replyEntity.status) {
        case 0:
          mHolder.mTvRed.setVisibility(View.VISIBLE);
          break;
        case 1:
          mHolder.mTvRed.setVisibility(View.INVISIBLE);
          break;
      }
    }
    return convertView;
  }

  private class ViewHolder{
    public CircleImageView mCircleImageView;
    public TextView mTvRed;
    public TextView mTvName;
    public TextView mTvDate;
    public TextView mTvMsg;
    public TextView mTvContent;
    public LinearLayout mLlContent;
    public RelativeLayout mRlHint;

    public ViewHolder(View view) {
      mCircleImageView = (CircleImageView) view.findViewById(R.id.civ_avatar);
      mTvRed = (TextView) view.findViewById(R.id.tv_red_circle);
      mTvName = (TextView) view.findViewById(R.id.tv_name);
      mTvDate = (TextView) view.findViewById(R.id.tv_date);
      mTvMsg = (TextView) view.findViewById(R.id.tv_reply);
      mTvContent = (TextView) view.findViewById(R.id.tv_content);
      mLlContent = (LinearLayout) view.findViewById(R.id.ll_data);
      mRlHint = (RelativeLayout) view.findViewById(R.id.rl_hint);
    }
  }

}
