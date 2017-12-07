package com.junhsue.ksee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.junhsue.ksee.R;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.entity.PostSecondCommentEntity;
import com.junhsue.ksee.file.ImageLoaderOptions;
import com.junhsue.ksee.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 帖子列表
 * Created by Sugar on 17/10/23.
 */

public class PostCommentListAdapter<T extends PostSecondCommentEntity> extends MyBaseAdapter<PostSecondCommentEntity> {
    private Context mContext;
    private LayoutInflater mInflater;
    private int maxSize = 0;

    public PostCommentListAdapter(Context context) {
        this.mContext = context;
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
            convertView = mInflater.inflate(R.layout.item_post_comment_second, null);
            viewHolder.commentAvatar = (CircleImageView) convertView.findViewById(R.id.civ_post_comment_user_avatar);
            viewHolder.tvPostCommentNickname = (TextView) convertView.findViewById(R.id.tv_post_comment_nickname);
            viewHolder.tvCommentContent = (TextView) convertView.findViewById(R.id.tv_comment_content);
            viewHolder.divider = convertView.findViewById(R.id.v_second_divider);
            viewHolder.rlHint = (RelativeLayout) convertView.findViewById(R.id.rl_post_second_hint);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        PostSecondCommentEntity entity = mList.get(position);
        ImageLoader.getInstance().displayImage(entity.avatar, viewHolder.commentAvatar, ImageLoaderOptions.option(R.drawable.pic_default_avatar));
        viewHolder.tvPostCommentNickname.setText(entity.nickname);
        viewHolder.tvCommentContent.setText(entity.content);
        if (position == mList.size() - 1 && maxSize == mList.size() + 1) {

            viewHolder.rlHint.setVisibility(View.VISIBLE);
        } else {
            viewHolder.rlHint.setVisibility(View.GONE);
        }
        if (position == 0) {
            viewHolder.divider.setVisibility(View.GONE);
        } else {
            viewHolder.divider.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    private class ViewHolder {
        private CircleImageView commentAvatar;
        private TextView tvPostCommentNickname;
        private TextView tvCommentContent;
        private View divider;
        private RelativeLayout rlHint;
    }


}
