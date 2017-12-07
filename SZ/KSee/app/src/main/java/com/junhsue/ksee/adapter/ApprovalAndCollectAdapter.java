package com.junhsue.ksee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.junhsue.ksee.R;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.entity.MsgCenterReceiveReplyEntity;
import com.junhsue.ksee.file.ImageLoaderOptions;
import com.junhsue.ksee.utils.DateUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 点赞和收藏的消息中心的适配器
 * Created by Sugar on 17/11/14.
 */

public class ApprovalAndCollectAdapter<T extends MsgCenterReceiveReplyEntity> extends MyBaseAdapter<MsgCenterReceiveReplyEntity> {

    private Context mContext;
    private LayoutInflater mInflater;
    private int maxSize = 0;

    public ApprovalAndCollectAdapter(Context context) {
        this.mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public void setMaxSize(int size) {
        maxSize = size;
    }

    @Override
    protected View getWrappeView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (viewHolder == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_approval_and_collect, null);
            viewHolder.rlContentLayout = (RelativeLayout) convertView.findViewById(R.id.rl_approval_collect_content_layout);
            viewHolder.tvMessageStatus = (TextView) convertView.findViewById(R.id.tv_message_status);
            viewHolder.ivCivAvatar = (ImageView) convertView.findViewById(R.id.iv_civ_avatar);
            viewHolder.tvUserNickname = (TextView) convertView.findViewById(R.id.tv_msg_user_nickname);
            viewHolder.ivImgStatus = (ImageView) convertView.findViewById(R.id.iv_img_status);
            viewHolder.tvStatusMsg = (TextView) convertView.findViewById(R.id.tv_status_msg);
            viewHolder.tvMsgPublishTime = (TextView) convertView.findViewById(R.id.tv_msg_publish_time);
            viewHolder.tvMsgTitle = (TextView) convertView.findViewById(R.id.tv_msg_title);
            viewHolder.tvMsgContent = (TextView) convertView.findViewById(R.id.tv_msg_content);
            viewHolder.rlHint = (RelativeLayout) convertView.findViewById(R.id.rl_approval_hint);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        MsgCenterReceiveReplyEntity entity = mList.get(position);

        if (position == mList.size() - 1 && maxSize == mList.size() + 1 ) {
            viewHolder.rlHint.setVisibility(View.VISIBLE);
        } else {
            viewHolder.rlHint.setVisibility(View.GONE);
        }


        viewHolder.tvMsgPublishTime.setText(DateUtils.formatCurrentTime(
                entity.initial_time * 1000l, System.currentTimeMillis()));

        if (entity.status == 1) {
            viewHolder.tvMessageStatus.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.tvMessageStatus.setVisibility(View.VISIBLE);
        }

        if (entity.type_id == 16) {//点赞
            if (Integer.parseInt(entity.list.business_id) == 14) {
                ImageLoader.getInstance().displayImage(entity.list.approval_avatar, viewHolder.ivCivAvatar, ImageLoaderOptions.option(R.drawable.pic_default_avatar));
                viewHolder.tvUserNickname.setText(entity.list.approval_nickname);
                viewHolder.tvStatusMsg.setText("赞了您的帖子");
                viewHolder.tvMsgTitle.setText(entity.list.post_content_title);
                viewHolder.tvMsgTitle.setVisibility(View.VISIBLE);
                viewHolder.tvMsgContent.setVisibility(View.GONE);

            } else if (Integer.parseInt(entity.list.business_id) == 15) {
                ImageLoader.getInstance().displayImage(entity.list.approval_avatar, viewHolder.ivCivAvatar, ImageLoaderOptions.option(R.drawable.pic_default_avatar));
                viewHolder.tvUserNickname.setText(entity.list.approval_nickname);
                viewHolder.tvStatusMsg.setText("赞了您的评论");
                viewHolder.tvMsgContent.setText(entity.list.comment_content);
                viewHolder.tvMsgTitle.setVisibility(View.GONE);
                viewHolder.tvMsgContent.setVisibility(View.VISIBLE);
            }
            viewHolder.ivImgStatus.setImageResource(R.drawable.icon_message_approval);

        } else if (entity.type_id == 17) {//收藏

            ImageLoader.getInstance().displayImage(entity.list.favorite_avatar, viewHolder.ivCivAvatar, ImageLoaderOptions.option(R.drawable.pic_default_avatar));
            viewHolder.tvUserNickname.setText(entity.list.favorite_nickname);
            viewHolder.tvStatusMsg.setText("收藏了您的帖子");
            viewHolder.tvMsgTitle.setText(entity.list.post_content_title);
            viewHolder.tvMsgTitle.setVisibility(View.VISIBLE);
            viewHolder.tvMsgContent.setVisibility(View.GONE);
            viewHolder.ivImgStatus.setImageResource(R.drawable.icon_message_collect);
        }


        return convertView;
    }

    private class ViewHolder {
        public ImageView ivCivAvatar;

        public TextView tvMessageStatus;

        public TextView tvUserNickname;

        public ImageView ivImgStatus;

        public TextView tvStatusMsg;

        public TextView tvMsgPublishTime;

        public TextView tvMsgContent;//评论内容

        public TextView tvMsgTitle;//帖子标题

        public RelativeLayout rlContentLayout;
        public RelativeLayout rlHint;
    }
}
