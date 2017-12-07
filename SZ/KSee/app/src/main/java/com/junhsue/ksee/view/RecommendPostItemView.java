package com.junhsue.ksee.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.junhsue.ksee.R;
import com.junhsue.ksee.entity.PostDetailEntity;
import com.junhsue.ksee.file.ImageLoaderOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 帖子详情的推荐帖子
 * Created by Sugar on 17/11/28.
 */

public class RecommendPostItemView extends FrameLayout {

    private Context mContext;
    private TextView tvRecommendPostTitle;
    private CircleImageView civPostAvatar;
    private TextView tvNickname;

    public RecommendPostItemView(Context context) {
        super(context);
        mContext = context;
        initView(context);
    }

    public RecommendPostItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView(context);
    }

    public RecommendPostItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_recommend_post, this);
        tvRecommendPostTitle = (TextView) view.findViewById(R.id.tv_recommend_post_title);
        civPostAvatar = (CircleImageView) view.findViewById(R.id.civ_recommend_avatar);
        tvNickname = (TextView) view.findViewById(R.id.tv_recommend_post_user_nickname);
    }


    public void setData(PostDetailEntity entity) {

        tvRecommendPostTitle.setText(entity.title);
        ImageLoader.getInstance().displayImage(entity.avatar, civPostAvatar, ImageLoaderOptions.option(R.drawable.pic_default_avatar));
        tvNickname.setText(entity.nickname);

    }

}
