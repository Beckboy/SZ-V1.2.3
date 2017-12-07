package com.junhsue.ksee.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.junhsue.ksee.BigPictureActivity;
import com.junhsue.ksee.R;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.entity.PhotoInfo;
import com.junhsue.ksee.entity.PostDetailEntity;
import com.junhsue.ksee.file.ImageLoaderOptions;
import com.junhsue.ksee.utils.DateUtils;
import com.junhsue.ksee.utils.StringUtils;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sugar on 17/11/8.
 */

public class HomeNormalPostItemView extends FrameLayout {
    private Context mContext;

    private TextView tvCircleName;
    private TextView tvHomePostTitle;

    private RelativeLayout rlMiddleHorizontal;
    private TextView tvNormalDesc;
    private ImageView ivNoPic;

    private RelativeLayout rlMiddleVertical;
    private TextView tvHomePostVDesc;

    private TextView tvCommentTotal;
    private TextView tvCreateTime;

    public ImageView ivApproval;
    public TextView tvApprovalCount;

    private MultiImageView multiImageView;

    public LinearLayout llApprovalLayout;

    public ImageView ivPostSelectTag;

    public HomeNormalPostItemView(Context context) {
        super(context);
        mContext = context;
        initializeView(context);
    }

    public HomeNormalPostItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initializeView(context);
    }

    public HomeNormalPostItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initializeView(context);
    }

    private void initializeView(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //
        View view = layoutInflater.inflate(R.layout.item_normal_post, this);
        tvCircleName = (TextView) view.findViewById(R.id.tv_circle_name);
        tvHomePostTitle = (TextView) view.findViewById(R.id.tv_home_post_title);
        ivPostSelectTag = (ImageView) view.findViewById(R.id.iv_post_select_tag);
        rlMiddleHorizontal = (RelativeLayout) view.findViewById(R.id.rl_middle_horizontal);
        tvNormalDesc = (TextView) view.findViewById(R.id.tv_normal_desc);
        ivNoPic = (ImageView) view.findViewById(R.id.iv_no_pic);
        rlMiddleVertical = (RelativeLayout) view.findViewById(R.id.rl_middle_vertical);
        tvHomePostVDesc = (TextView) view.findViewById(R.id.tv_home_post_v_desc);
        tvCreateTime = (TextView) view.findViewById(R.id.tv_home_post_create_time);
        tvCommentTotal = (TextView) view.findViewById(R.id.tv_home_post_comment_total_size);
        ivApproval = (ImageView) view.findViewById(R.id.iv_home_post_approval);
        tvApprovalCount = (TextView) view.findViewById(R.id.tv_home_post_approval_count);
        multiImageView = (MultiImageView) view.findViewById(R.id.mli_multiImagView);
        llApprovalLayout = (LinearLayout) view.findViewById(R.id.ll_home_bottom_approval_layout);
    }

    public void setData(final PostDetailEntity postDetailEntity) {
        tvCircleName.setText(postDetailEntity.circle_name);
        tvNormalDesc.setText(postDetailEntity.description);
        tvHomePostVDesc.setText(postDetailEntity.description);
   /*     if (postDetailEntity.is_top) {
            Spanny spanny = new Spanny();
            spanny.append(" " + postDetailEntity.title, new ImageSpan(mContext, R.drawable.icon_home_post_selection));
            tvHomePostTitle.setText(spanny);
        } else {
            tvHomePostTitle.setText(postDetailEntity.title);
        }*/

        if (postDetailEntity.is_top) {
//            Spanny spanny = new Spanny();
//            spanny.append(" " + postDetailEntity.title, new ImageSpan(mContext, R.drawable.icon_home_post_selection));
            tvHomePostTitle.setText("       " + postDetailEntity.title);
            ivPostSelectTag.setVisibility(View.VISIBLE);
        } else {
            tvHomePostTitle.setText(postDetailEntity.title);
            ivPostSelectTag.setVisibility(View.GONE);
        }


        if (postDetailEntity.posters.size() >= 3) {
            rlMiddleVertical.setVisibility(View.VISIBLE);
            rlMiddleHorizontal.setVisibility(View.GONE);
            if (postDetailEntity.posters.size() > 3) {
                List<String> tempUrl = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    tempUrl.add(postDetailEntity.posters.get(i));
                }
                setPosters(multiImageView, tempUrl);
            } else {
                setPosters(multiImageView, postDetailEntity.posters);
            }

        } else {
            rlMiddleVertical.setVisibility(View.GONE);
            rlMiddleHorizontal.setVisibility(View.VISIBLE);
            if (postDetailEntity.posters.size() > 0) {
                ImageLoader.getInstance().displayImage(postDetailEntity.posters.get(0) + Constants.IMAGE_TAILOR_URL, ivNoPic, ImageLoaderOptions.option(R.drawable.img_loading_default));
                ivNoPic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent2PicDetail = new Intent(mContext, BigPictureActivity.class);
                        intent2PicDetail.putStringArrayListExtra(BigPictureActivity.PICTURE_LIST, postDetailEntity.posters);
                        intent2PicDetail.putExtra(BigPictureActivity.CURRENT_ITEM, 0);
                        mContext.startActivity(intent2PicDetail);
                    }
                });
                ivNoPic.setVisibility(View.VISIBLE);
            } else {
                ivNoPic.setVisibility(View.GONE);
            }
        }

        tvCreateTime.setText(DateUtils.formatCurrentTime(
                postDetailEntity.publish_at * 1000l, System.currentTimeMillis()));


        if (postDetailEntity.is_approval) {
            ivApproval.setImageResource(R.drawable.icon_home_post_approval_light);
            tvApprovalCount.setTextColor(Color.parseColor("#FFC84A"));
        } else {
            ivApproval.setImageResource(R.drawable.icon_home_post_approval_normal);
            tvApprovalCount.setTextColor(Color.parseColor("#AEBDCD"));
        }
        if (Integer.valueOf(postDetailEntity.approvalcount)== 0) {
            tvApprovalCount.setText("赞");

        } else {
            tvApprovalCount.setText(StringUtils.tranNum(postDetailEntity.approvalcount));

        }
        if (Integer.valueOf(postDetailEntity.commentcount) ==0) {
            tvCommentTotal.setText("评论");
        } else {
            tvCommentTotal.setText(StringUtils.tranNum(postDetailEntity.commentcount));
        }
    }

    /**
     * 设置多图
     *
     * @param multiImageView
     * @param posters
     */
    private void setPosters(MultiImageView multiImageView, final List<String> posters) {
        List<PhotoInfo> photos = null;
        if (photos == null) {
            photos = new ArrayList<>();
        } else {
            photos.clear();
        }
        for (String photoUrl : posters) {
            PhotoInfo photoInfo = new PhotoInfo();
            photoInfo.url = photoUrl + Constants.IMAGE_TAILOR_URL;
            photos.add(photoInfo);
        }
        if (photos != null && photos.size() > 0) {
            multiImageView.setVisibility(View.VISIBLE);
            multiImageView.setList(photos);
            multiImageView.setmOnItemClickListener(new MultiImageView.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    //imagesize是作为loading时的图片size
                    Intent intent2PicDetail = new Intent(mContext, BigPictureActivity.class);
                    intent2PicDetail.putStringArrayListExtra(BigPictureActivity.PICTURE_LIST, (ArrayList<String>) posters);
                    intent2PicDetail.putExtra(BigPictureActivity.CURRENT_ITEM, position);
                    mContext.startActivity(intent2PicDetail);
                }
            });
        } else {
            multiImageView.setVisibility(View.GONE);
        }
    }
}
