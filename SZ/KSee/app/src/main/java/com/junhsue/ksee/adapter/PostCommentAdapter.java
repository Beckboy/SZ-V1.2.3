package com.junhsue.ksee.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.junhsue.ksee.PostCommentListActivity;
import com.junhsue.ksee.R;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.entity.PostCommentEntity;
import com.junhsue.ksee.entity.PostSecondCommentEntity;
import com.junhsue.ksee.file.ImageLoaderOptions;
import com.junhsue.ksee.utils.DateUtils;
import com.junhsue.ksee.utils.StringUtils;
import com.junhsue.ksee.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * 帖子详情的评论列表适配器
 * Created by Sugar on 17/10/23.
 */

public class PostCommentAdapter<T extends PostCommentEntity> extends MyBaseAdapter<PostCommentEntity> {

    private Context mContext;
    private LayoutInflater mInflater;
    private PostListener postListener;
    private int maxSize = 0;

    public PostCommentAdapter(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setMaxSize(int size) {
        maxSize = size;
    }

    @Override
    protected View getWrappeView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_post_comment, null);
            viewHolder.imgTop = (ImageView) convertView.findViewById(R.id.img_top);
            viewHolder.commentAvatar = (CircleImageView) convertView.findViewById(R.id.civ_post_comment_user_avatar);
            viewHolder.tvCommentNickname = (TextView) convertView.findViewById(R.id.tv_post_comment_user_nickname);
            viewHolder.tvCommentPublishTime = (TextView) convertView.findViewById(R.id.tv_post_comment_publish_time);
            viewHolder.tvCommentContent = (TextView) convertView.findViewById(R.id.tv_post_comment_content);
            viewHolder.llCommentSecondMainLayout = (LinearLayout) convertView.findViewById(R.id.ll_comment_second_main_layout);
            viewHolder.llCommentSecond = (LinearLayout) convertView.findViewById(R.id.ll_comment_second_comment);
            viewHolder.tvShowMoreSecondCommentLines = (TextView) convertView.findViewById(R.id.tv_show_more_second_comment_lines);
            viewHolder.rlReply = (RelativeLayout) convertView.findViewById(R.id.rl_post_comment_reply);
            viewHolder.llApproval = (LinearLayout) convertView.findViewById(R.id.ll_post_comment_approval);
            viewHolder.ivPostCommentApproval = (ImageView) convertView.findViewById(R.id.iv_post_comment_approval);
            viewHolder.tvApproval = (TextView) convertView.findViewById(R.id.tv_post_comment_approval_count);
            viewHolder.viewDivider = convertView.findViewById(R.id.v_post_bottom_divider);
            viewHolder.rlHint = (RelativeLayout) convertView.findViewById(R.id.rl_post_hint);
            viewHolder.rlContentLayout = (RelativeLayout) convertView.findViewById(R.id.rl_post_comment_content_layout);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (position == mList.size() - 1 && maxSize == mList.size() + 1 ) {

            viewHolder.rlHint.setVisibility(View.VISIBLE);
        } else {
            viewHolder.rlHint.setVisibility(View.GONE);
        }

        if (position == 0) {
            viewHolder.imgTop.setVisibility(View.VISIBLE);
        } else {
            viewHolder.imgTop.setVisibility(View.GONE);
        }
        final PostCommentEntity postCommentEntity = mList.get(position);
        ImageLoader.getInstance().displayImage(postCommentEntity.avatar, viewHolder.commentAvatar, ImageLoaderOptions.option(R.drawable.pic_default_avatar));
        viewHolder.tvCommentNickname.setText(postCommentEntity.nickname);
        viewHolder.tvCommentPublishTime.setText(DateUtils.formatCurrentTime(
                postCommentEntity.create_at * 1000l, System.currentTimeMillis()));

        viewHolder.tvCommentContent.setText(postCommentEntity.content);

        if (position == 0) {
            viewHolder.viewDivider.setVisibility(View.GONE);
        } else {
            viewHolder.viewDivider.setVisibility(View.VISIBLE);
        }
//        Trace.e("===commadapter==评论列表的id:"+postCommentEntity.id+"approval_count:"+postCommentEntity.approval_count+"is_approval:"+postCommentEntity.is_approval);
        if (postCommentEntity.approval_count > 0) {
            viewHolder.tvApproval.setText(StringUtils.tranNum(postCommentEntity.approval_count + ""));
        } else {
            viewHolder.tvApproval.setText("赞");
        }

        if (postCommentEntity.is_approval) {
            viewHolder.ivPostCommentApproval.setImageResource(R.drawable.icon_post_approval_light);
            viewHolder.tvApproval.setTextColor(mContext.getResources().getColor(R.color.c_yellow_ffc84a));
        } else {
            viewHolder.ivPostCommentApproval.setImageResource(R.drawable.icon_post_approval);
            viewHolder.tvApproval.setTextColor(mContext.getResources().getColor(R.color.c_gray_aebdcd));
        }

        viewHolder.llApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (postListener != null) {

                    postListener.refreshApproval(postCommentEntity, v);

                }

            }
        });

        viewHolder.rlReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postListener != null) {

                    postListener.onReplySecond(postCommentEntity, v);

                }

            }
        });

        viewHolder.tvCommentContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postListener != null) {
                    postListener.replyOrRemove(postCommentEntity, v);
                }
            }
        });

        viewHolder.llCommentSecondMainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postListener != null) {
                    postListener.toNext(postCommentEntity, v);
                }
            }
        });

        List<PostSecondCommentEntity> commentEntityList = new ArrayList<>();
        if (postCommentEntity.repply.size() > 3) {
            String moreLines = (postCommentEntity.repply.size() - 3) + "";
            viewHolder.tvShowMoreSecondCommentLines.setVisibility(View.VISIBLE);
            viewHolder.tvShowMoreSecondCommentLines.setText(String.format(mContext.getResources().getString(R.string.msg_second_post_comment_lines), moreLines));

            for (int i = 0; i < 3; i++) {
                commentEntityList.add(postCommentEntity.repply.get(i));
            }

        } else {
            viewHolder.tvShowMoreSecondCommentLines.setVisibility(View.GONE);

            for (int i = 0; i < postCommentEntity.repply.size(); i++) {
                commentEntityList.add(postCommentEntity.repply.get(i));
            }
        }

        if (commentEntityList.size() <= 0) {
            viewHolder.llCommentSecondMainLayout.setVisibility(View.GONE);
        } else {
            viewHolder.llCommentSecondMainLayout.setVisibility(View.VISIBLE);
            viewHolder.llCommentSecond.removeAllViews();
        }
        for (int i = 0; i < commentEntityList.size(); i++) {
            TextView tvSecond = new TextView(mContext);
            tvSecond.setTextColor(mContext.getResources().getColor(R.color.c_black_55626e));
            int padding = (int) mContext.getResources().getDimension(R.dimen.dimen_10px);
            tvSecond.setPadding(padding, padding, padding, 0);
            tvSecond.setLineSpacing(2, 1);
            String secondContent = commentEntityList.get(i).nickname + ":" + commentEntityList.get(i).content;
            tvSecond.setText(secondContent);
            viewHolder.llCommentSecond.addView(tvSecond);
        }
        return convertView;

    }


    private class ViewHolder {

        private ImageView imgTop;
        private CircleImageView commentAvatar;
        private TextView tvCommentNickname;
        private TextView tvCommentPublishTime;
        private TextView tvCommentContent;
        private LinearLayout llCommentSecondMainLayout;
        private LinearLayout llCommentSecond;
        private TextView tvShowMoreSecondCommentLines;
        private RelativeLayout rlReply;
        private LinearLayout llApproval;
        private TextView tvApproval;
        private View viewDivider;
        private ImageView ivPostCommentApproval;
        private RelativeLayout rlContentLayout;
        private RelativeLayout rlHint;

    }


    public interface PostListener {

        void toNext(PostCommentEntity postCommentEntity, View v);

        void replyOrRemove(PostCommentEntity postCommentEntity, View v);

        void refreshApproval(PostCommentEntity postCommentEntity, View v);

        void onReplySecond(PostCommentEntity postCommentEntity, View v);

    }

    public void setPostListener(PostListener listener) {
        this.postListener = listener;
    }
}
