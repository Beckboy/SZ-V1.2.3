package com.junhsue.ksee.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;
import com.junhsue.ksee.R;
import com.junhsue.ksee.view.CircleImageView;
import com.junhsue.ksee.view.ExpandableTextView;

/**
 * Created by hunter_J on 2017/11/2.
 */

public abstract class CircleViewHolder extends RecyclerView.ViewHolder {

    public final static int TYPE_URL = 1;
    public final static int TYPE_IMAGE = 2;

    public int viewType;

    public CircleImageView mCircleImageView;
    public TextView mTvName;
    public TextView mTvTitle;
    public ImageView mImgBest;
    /** 动态的内容 **/
    public ExpandableTextView mTvContent;
    public TextView mTvDate;
//    public MultiImageView mMultiImageView;
    /** 点赞评论列表 **/
    public TextView mTvShare;
    public TextView mTvHot;
    public TextView mTvChat;
    public TextView mTvCollect;

    public CircleViewHolder(View itemView, int viewType) {
        super(itemView);
        this.viewType = viewType;

        ViewStub viewStub = (ViewStub) itemView.findViewById(R.id.viewStub);

        initSubView(viewType, viewStub);

        mCircleImageView = (CircleImageView) itemView.findViewById(R.id.civ_avatar);
        mTvName = (TextView) itemView.findViewById(R.id.tv_name);
        mTvDate = (TextView) itemView.findViewById(R.id.tv_time);
        mTvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        mImgBest = (ImageView) itemView.findViewById(R.id.img_best);
//        mMultiImageView = (MultiImageView) itemView.findViewById(R.id.multiImagView);
        mTvShare = (TextView) itemView.findViewById(R.id.tv_share);
        mTvHot = (TextView) itemView.findViewById(R.id.tv_hot);
        mTvChat = (TextView) itemView.findViewById(R.id.tv_chat);
        mTvCollect = (TextView) itemView.findViewById(R.id.tv_collect);
        mTvContent = (ExpandableTextView) itemView.findViewById(R.id.expandable_textview_text);
    }

    public abstract void initSubView(int viewType, ViewStub viewStub);

}
