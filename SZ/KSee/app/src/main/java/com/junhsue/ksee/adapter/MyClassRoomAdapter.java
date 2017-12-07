package com.junhsue.ksee.adapter;

import android.content.Context;
import android.text.Html;
import android.text.Spannable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.utils.EaseSmileUtils;
import com.junhsue.ksee.R;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.entity.ClassRoomListEntity;
import com.junhsue.ksee.file.ImageLoaderOptions;
import com.junhsue.ksee.utils.DateUtils;
import com.nostra13.universalimageloader.core.ImageLoader;


/**
 * Created by hunter_J on 17/4/25.
 */

public class MyClassRoomAdapter<T extends ClassRoomListEntity> extends MyBaseAdapter<T> {

  private Context mContext;
  private LayoutInflater mInflater;

  public MyClassRoomAdapter(Context context) {
    mContext = context;
    mInflater = LayoutInflater.from(context);
  }

  @Override
  protected View getWrappeView(int position, View convertView, ViewGroup parent) {

    ViewHolder mHolder = null;
    if (convertView == null){
      convertView = mInflater.inflate(R.layout.item_classroom_list,null);
      mHolder = new ViewHolder(convertView);
      convertView.setTag(mHolder);
    }else {
      mHolder = (MyClassRoomAdapter.ViewHolder) convertView.getTag();
    }

    ClassRoomListEntity classRoomListEntity = mList.get(position);
    if (classRoomListEntity != null){
      if(!classRoomListEntity.poster.equals(mHolder.imgIcon.getTag())){
        ImageLoader.getInstance().displayImage(classRoomListEntity.poster,mHolder.imgIcon,
            ImageLoaderOptions.option(R.drawable.img_default_course_system));
        mHolder.imgIcon.setTag(classRoomListEntity.poster);
      }
      mHolder.tvTitle.setText(classRoomListEntity.name);
      String msg = classRoomListEntity.hx_message;
      long lastmsg_time = classRoomListEntity.hx_lastmsg_time;
      boolean is_read = classRoomListEntity.is_read;
      if ( msg != null ) {
        String content = null;
        mHolder.tvCont.setText("");
        mHolder.tvCont.setVisibility(View.INVISIBLE);
        if (msg.contains("[有人@我]")){
          mHolder.tvCont.setText("[有人@我]");
          mHolder.tvCont.setVisibility(View.VISIBLE);
          msg = msg.substring(6,msg.length());
          Log.i("tttt","走到@人了");
        }
        Spannable span_msg = EaseSmileUtils.getSmiledText(mContext, msg);
//         设置内容
        mHolder.tvContent.setText(span_msg, TextView.BufferType.SPANNABLE);
        if (content != null) {
          Spannable span_content = EaseSmileUtils.getSmiledText(mContext, content);
          // 设置内容
          mHolder.tvContent.setText(span_content, TextView.BufferType.SPANNABLE);
        }
        if (lastmsg_time != 0) {
          mHolder.tvDate.setText(DateUtils.fromTheCurrentTime(lastmsg_time));
        }else {
          mHolder.tvDate.setText(null);
        }
        setImgUnreadState(mHolder);

        if (is_read){
          mHolder.imgUnread.setVisibility(View.INVISIBLE);
        }else {
          mHolder.imgUnread.setVisibility(View.VISIBLE);
        }
        Log.i("EMSG:",msg+":"+lastmsg_time+":"+is_read);
        return convertView;
      }
      Log.i("EMSG:","无数据："+classRoomListEntity.name);
    }
    return convertView;
  }

  private void setImgUnreadState(ViewHolder mHolder) {
      mHolder.imgUnread.setVisibility(View.INVISIBLE);
  }


  private class ViewHolder{
    public ImageView imgIcon;
    public ImageView imgUnread;
    public TextView tvTitle;
    public TextView tvContent;
    public TextView tvCont;
    public TextView tvDate;

    public ViewHolder(View view) {
      imgIcon = (ImageView) view.findViewById(R.id.icv_class_room_poter);
      imgUnread = (ImageView) view.findViewById(R.id.icv_class_room_unread);
      tvTitle = (TextView) view.findViewById(R.id.tv_classroom_title);
      tvContent = (TextView) view.findViewById(R.id.tv_classroom_last_content);
      tvCont = (TextView) view.findViewById(R.id.tv_classroom_last_cont);
      tvDate = (TextView) view.findViewById(R.id.tv_classroom_last_date);
    }
  }

}
