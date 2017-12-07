package com.junhsue.ksee.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.junhsue.ksee.R;
import com.junhsue.ksee.entity.MsgCenterReceiveReplyEntity;
import com.junhsue.ksee.utils.DateUtils;

/**
 * 消息通知
 * Created by longer on 17/11/15.
 */

public class MsgNoticeAdapter<T extends MsgCenterReceiveReplyEntity> extends MyBaseAdapter<T> {


    private Context mContext;
    //
    private LayoutInflater mLayoutInflater;
    //
    private ViewHolder mViewHolder;

    public MsgNoticeAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }


    @Override
    protected View getWrappeView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = mLayoutInflater.inflate(R.layout.item_msg_notice, null);
            mViewHolder=new ViewHolder();
            mViewHolder.mTxtTime=(TextView)convertView.findViewById(R.id.txt_time);
            mViewHolder.mTxtContent=(TextView)convertView.findViewById(R.id.txt_content);
            //mViewHolder.mTxtTitle=(TextView)convertView.findViewById(R.id.txt_title);
            convertView.setTag(mViewHolder);
        }else{
            mViewHolder=(ViewHolder)convertView.getTag();
        }

        MsgCenterReceiveReplyEntity msgCenterReceiveReplyEntity=getList().get(position);
        mViewHolder.mTxtTime.setText(DateUtils.timestampToPatternTime(msgCenterReceiveReplyEntity.initial_time*1000l
                ,"yyy-MM-dd HH:mm"));

        StringBuilder stringBuilder=new StringBuilder();
        if(null!=msgCenterReceiveReplyEntity.list ){
            //精选帖子加精
            if(msgCenterReceiveReplyEntity.list.bar_info_type_id==1){
                stringBuilder.append(mContext.getString(R.string.msg_center_noticle_posts_hot
                        ,msgCenterReceiveReplyEntity.list.post_content_title));
            }else {
                //删除帖子文案
                stringBuilder.append(mContext.getString(R.string.msg_center_noticle_posts_delete,
                        msgCenterReceiveReplyEntity.list.post_content_title)+"\n");
                stringBuilder.append(msgCenterReceiveReplyEntity.list.bar_info_type_name);
            }
            if(!TextUtils.isEmpty(msgCenterReceiveReplyEntity.list.infocomment)){
                stringBuilder.append("\n\n"+msgCenterReceiveReplyEntity.list.infocomment);
            }
        }
        mViewHolder.mTxtContent.setText(stringBuilder.toString());
        return convertView;
    }


    private class ViewHolder {

        private TextView mTxtTime;
        private TextView mTxtContent;
    }

}
