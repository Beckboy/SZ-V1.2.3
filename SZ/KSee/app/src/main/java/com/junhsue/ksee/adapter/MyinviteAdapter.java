package com.junhsue.ksee.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.junhsue.ksee.R;
import com.junhsue.ksee.entity.MyinviteList;
import com.junhsue.ksee.utils.DateUtils;
import com.junhsue.ksee.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by hunter_J on 17/4/19.
 */

public class MyinviteAdapter<T extends MyinviteList> extends MyBaseAdapter<MyinviteList> {

  private Context mContext;
  private LayoutInflater mInflater;

  public MyinviteAdapter(Context context) {
    mContext = context;
    mInflater = LayoutInflater.from(context);
  }

  @Override
  protected View getWrappeView(int position, View convertView, ViewGroup parent) {

    ViewHolder mHolder = null;
    if (convertView == null){
      convertView = mInflater.inflate(R.layout.item_myanswer_myinvite_list,null);
      mHolder = new ViewHolder(convertView);
      convertView.setTag(mHolder);
    }else {
      mHolder = (ViewHolder) convertView.getTag();
    }

    MyinviteList myinvite = mList.get(position);
    if (myinvite != null){
      ImageLoader.getInstance().displayImage(myinvite.avatar,mHolder.circleImageView);
      String nickname = "<font color=\'#8392A0\'>"+myinvite.nickname+"邀请你回答"+"</font>";
      String content = "<font color=\'#2C4350\'>"+"\""+myinvite.title+"\""+"</font>";
      mHolder.tvContent.setText(Html.fromHtml(nickname+content));
      mHolder.tvDate.setText(DateUtils.fromTheCurrentTime(myinvite.create_at, System.currentTimeMillis()));
    }
    return convertView;
  }

  private class ViewHolder{
    public CircleImageView circleImageView;
    public TextView tvContent;
    public TextView tvDate;

    public ViewHolder(View view) {
      circleImageView = (CircleImageView) view.findViewById(R.id.civ_avatar);
      tvContent = (TextView) view.findViewById(R.id.tv_content);
      tvDate = (TextView) view.findViewById(R.id.tv_date);
    }
  }

}
