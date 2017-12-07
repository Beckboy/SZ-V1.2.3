package com.junhsue.ksee.adapter;

import android.content.Context;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.junhsue.ksee.R;
import com.junhsue.ksee.entity.QuestionEntity;
import com.junhsue.ksee.file.ImageLoaderOptions;
import com.junhsue.ksee.utils.DateUtils;
import com.junhsue.ksee.view.CircleImageView;
import com.junhsue.ksee.view.Spanny;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 问答列表适配器
 * Created by Sugar on 17/3/21 in Junhsue.
 */

public class QuestionAdapter<T extends QuestionEntity> extends MyBaseAdapter<QuestionEntity> {
    private Context mContext;
    private LayoutInflater mInflater;
    private OnCollectListener listener;

    public QuestionAdapter(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    protected View getWrappeView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_social_question, null);
            viewHolder.ivAvatar = (CircleImageView) convertView.findViewById(R.id.civ_avatar);
            viewHolder.tvNickname = (TextView) convertView.findViewById(R.id.tv_nickname);
            viewHolder.rlCollect = (RelativeLayout) convertView.findViewById(R.id.rl_collect);
            viewHolder.ivCollect = (ImageView) convertView.findViewById(R.id.iv_collect);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_topic_content);
            viewHolder.tvFromTopicName = (TextView) convertView.findViewById(R.id.tv_from_topic_name);
            viewHolder.tvReply = (TextView) convertView.findViewById(R.id.tv_reply);
            viewHolder.tvCollect = (TextView) convertView.findViewById(R.id.tv_collection);
            viewHolder.tvPublishTime = (TextView) convertView.findViewById(R.id.tv_publish_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final QuestionEntity entity = mList.get(position);

        if (entity != null) {

            if (!entity.avatar.equals(viewHolder.ivAvatar.getTag())) {

                ImageLoader.getInstance().displayImage(entity.avatar, viewHolder.ivAvatar, ImageLoaderOptions.option(R.drawable.pic_default_avatar));
                viewHolder.ivAvatar.setTag(entity.avatar);

            }

            viewHolder.tvNickname.setText(entity.nickname);

            if (entity.is_hot > 0) {
                Spanny spanny = new Spanny();
                spanny.append(" "+entity.title, new ImageSpan(mContext, R.drawable.icon_she_hot));
                viewHolder.tvTitle.setText(spanny);
            } else {
                viewHolder.tvTitle.setText(entity.title);
            }

            viewHolder.tvContent.setText(entity.content);
            viewHolder.tvReply.setText("回答 " + entity.reply);
            viewHolder.tvCollect.setText("收藏 " + entity.collect);
            viewHolder.tvFromTopicName.setText(entity.fromtopic);
            viewHolder.tvPublishTime.setText(DateUtils.fromTheCurrentTime(
                    entity.publish_time * 1000l, System.currentTimeMillis()));

            if (entity.is_favorite) {
                viewHolder.ivCollect.setImageResource(R.drawable.icon_collected);
            } else {
                viewHolder.ivCollect.setImageResource(R.drawable.icon_collect_normal);
            }

        }


        viewHolder.rlCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listener != null) {

                    listener.onCollectClick(entity, position);
                }
            }
        });

        return convertView;
    }


    private class ViewHolder {

        public CircleImageView ivAvatar;//提问用户头像
        public TextView tvNickname;//提问用户名
        public RelativeLayout rlCollect;//收藏状态的按钮布局
        public ImageView ivCollect;//收藏状态的按钮
        public TextView tvTitle;//问题的提问标题
        public TextView tvContent;//问题的提问内容
        public TextView tvFromTopicName;//来自话题
        public TextView tvReply;//回答量
        public TextView tvCollect;// 收藏量
        public TextView tvPublishTime;//问题的发布时间

    }

    public void setOnCollectListener(OnCollectListener listener) {
        this.listener = listener;
    }

    public interface OnCollectListener {

        void onCollectClick(QuestionEntity entity, int position);

    }
}
