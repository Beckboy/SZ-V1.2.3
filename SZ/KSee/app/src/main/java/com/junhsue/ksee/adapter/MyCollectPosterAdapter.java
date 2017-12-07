package com.junhsue.ksee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.junhsue.ksee.R;
import com.junhsue.ksee.dto.SendPostResultDTO;
import com.junhsue.ksee.entity.PostDetailEntity;
import com.junhsue.ksee.net.api.OKHttpNewSocialCircle;
import com.junhsue.ksee.net.callback.DeleteItemCallback;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.utils.DateUtils;
import com.junhsue.ksee.utils.ToastUtil;
import com.junhsue.ksee.view.CircleImageView;

/**
 * Created by hunter_J on 17/4/19.
 */

public class MyCollectPosterAdapter<T extends PostDetailEntity> extends MyBaseAdapter<PostDetailEntity>{

  private Context mContext;
  private LayoutInflater mInflater;
  //确认删除接口回调
  private DeleteItemCallback deleteItemCallback;

  //删除按钮的展示和隐藏
  private boolean isShow = false;

  public MyCollectPosterAdapter(Context context, DeleteItemCallback deleteItemCallback) {
    mContext = context;
    this.deleteItemCallback = deleteItemCallback;
    mInflater = LayoutInflater.from(context);
  }


  @Override
  protected View getWrappeView(final int position, View convertView, ViewGroup parent) {
    ViewHolder mHolder = null;
    if (convertView == null){
      convertView = mInflater.inflate(R.layout.item_my_collect_poster,null);
      mHolder = new ViewHolder(convertView);
      convertView.setTag(mHolder);
    }else {
      mHolder = (ViewHolder) convertView.getTag();
    }

    final PostDetailEntity myPoster = mList.get(position);
    if (myPoster != null){
      Glide.with(mContext).load(myPoster.avatar).error(R.drawable.icon_avatar).into(mHolder.mCircleImageView);
      if (myPoster.is_anonymous){
        mHolder.tvName.setText("匿名");
      }else {
        mHolder.tvName.setText(myPoster.nickname);
      }
      if (myPoster.title != null) {
        mHolder.tvTitle.setText(myPoster.title);
      }
      if (myPoster.publish_at != 0) {
        mHolder.tvTime.setText(DateUtils.formatCurrentTime(myPoster.publish_at * 1000l, System.currentTimeMillis()));
      }
    }
    if (isShow){
      mHolder.btnDel.setVisibility(View.VISIBLE);
    }else {
      mHolder.btnDel.setVisibility(View.GONE);
    }
    mHolder.btnDel.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        deleteItemCallback.deleteItem(position);
      }
    });
    return convertView;
  }



  public void setDelBtnVisibility(boolean isShow){
    this.isShow = isShow;
    notifyDataSetChanged();
  }

  private class ViewHolder{
    public Button btnDel; //删除按钮
    public CircleImageView mCircleImageView; //头像
    public TextView tvName; //名字
    public TextView tvTitle; //帖子标题
    public TextView tvTime; //帖子时间

    public ViewHolder(View view) {
      btnDel = (Button) view.findViewById(R.id.btn_collect_poster_item_del);
      mCircleImageView = (CircleImageView) view.findViewById(R.id.img_collect_poster_item_avatar);
      tvName = (TextView) view.findViewById(R.id.tv_collect_poster_item_name);
      tvTitle = (TextView) view.findViewById(R.id.tv_collect_poster_item_title);
      tvTime = (TextView) view.findViewById(R.id.tv_collect_poster_item_time);
    }
  }

}
