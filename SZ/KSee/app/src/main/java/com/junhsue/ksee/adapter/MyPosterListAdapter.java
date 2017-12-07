package com.junhsue.ksee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.junhsue.ksee.R;
import com.junhsue.ksee.entity.PostDetailEntity;
import com.junhsue.ksee.net.callback.DeleteItemCallback;
import com.junhsue.ksee.utils.DateUtils;

/**
 * Created by hunter_J on 17/4/19.
 */

public class MyPosterListAdapter<T extends PostDetailEntity> extends MyBaseAdapter<PostDetailEntity>{

  private Context mContext;
  private LayoutInflater mInflater;
  //确认删除接口回调
  private DeleteItemCallback deleteItemCallback;

  //删除按钮的展示和隐藏
  private boolean isShow = false;

  public MyPosterListAdapter(Context context, DeleteItemCallback deleteItemCallback) {
    mContext = context;
    this.deleteItemCallback = deleteItemCallback;
    mInflater = LayoutInflater.from(context);
  }


  @Override
  protected View getWrappeView(final int position, View convertView, ViewGroup parent) {
    ViewHolder mHolder = null;
    if (convertView == null){
      convertView = mInflater.inflate(R.layout.item_my_poster_list,null);
      mHolder = new ViewHolder(convertView);
      convertView.setTag(mHolder);
    }else {
      mHolder = (ViewHolder) convertView.getTag();
    }

    final PostDetailEntity myPoster = mList.get(position);
    if (myPoster != null){
      mHolder.tvTitle.setText(myPoster.title);
      mHolder.tvTime.setText(DateUtils.formatCurrentTime(myPoster.publish_at * 1000l, System.currentTimeMillis()));
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
    public TextView tvTitle; //帖子标题
    public TextView tvTime; //帖子时间

    public ViewHolder(View view) {
      btnDel = (Button) view.findViewById(R.id.btn_post_item_del);
      tvTitle = (TextView) view.findViewById(R.id.tv_poster_item_title);
      tvTime = (TextView) view.findViewById(R.id.tv_poster_item_time);
    }
  }

}
