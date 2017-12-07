package com.junhsue.ksee.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.junhsue.ksee.BigPictureActivity;
import com.junhsue.ksee.R;
import com.junhsue.ksee.adapter.viewholder.ImageViewHolder;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.entity.PhotoInfo;
import com.junhsue.ksee.entity.PostCommentEntity;
import com.junhsue.ksee.entity.PostDetailEntity;
import com.junhsue.ksee.file.ImageLoaderOptions;
import com.junhsue.ksee.utils.DateUtils;
import com.junhsue.ksee.utils.StringUtils;
import com.junhsue.ksee.view.MultiImageView;
import com.junhsue.ksee.view.Spanny;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sugar on 17/11/8.
 */

public class HomePostNormalAdapter<T extends PostDetailEntity> extends MyBaseAdapter<PostDetailEntity> {

    private Context mContext;
    private LayoutInflater mInflater;
    private HomePostLister homePostLister;
    //
    private ViewHolder viewHolder;


    public HomePostNormalAdapter(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    protected View getWrappeView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_normal_post, null);
            viewHolder.tvCircleName = (TextView) convertView.findViewById(R.id.tv_circle_name);
            viewHolder.tvHomePostTitle = (TextView) convertView.findViewById(R.id.tv_home_post_title);
            viewHolder.ivPostSelectTag = (ImageView) convertView.findViewById(R.id.iv_post_select_tag);
            viewHolder.rlMiddleHorizontal = (RelativeLayout) convertView.findViewById(R.id.rl_middle_horizontal);
            viewHolder.tvNormalDesc = (TextView) convertView.findViewById(R.id.tv_normal_desc);
            viewHolder.ivNoPic = (ImageView) convertView.findViewById(R.id.iv_no_pic);
            viewHolder.rlMiddleVertical = (RelativeLayout) convertView.findViewById(R.id.rl_middle_vertical);
            viewHolder.tvHomePostVDesc = (TextView) convertView.findViewById(R.id.tv_home_post_v_desc);
            viewHolder.tvCreateTime = (TextView) convertView.findViewById(R.id.tv_home_post_create_time);
            viewHolder.tvCommentTotal = (TextView) convertView.findViewById(R.id.tv_home_post_comment_total_size);
            viewHolder.ivApproval = (ImageView) convertView.findViewById(R.id.iv_home_post_approval);
            viewHolder.llBottomApproval = (LinearLayout) convertView.findViewById(R.id.ll_home_bottom_approval_layout);
            viewHolder.tvApprovalCount = (TextView) convertView.findViewById(R.id.tv_home_post_approval_count);
            viewHolder.multiImageView = (MultiImageView) convertView.findViewById(R.id.mli_multiImagView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(null==viewHolder){
            return  convertView;
        }
        final PostDetailEntity postDetailEntity = mList.get(position);

        if(null!=viewHolder.tvCircleName){
            viewHolder.tvCircleName.setText(postDetailEntity.circle_name);
        }

        if (postDetailEntity.is_top) {
//            Spanny spanny = new Spanny();
//            spanny.append(" " + postDetailEntity.title, new ImageSpan(mContext, R.drawable.icon_home_post_selection));
            viewHolder.tvHomePostTitle.setText("       " + postDetailEntity.title);
            viewHolder.ivPostSelectTag.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tvHomePostTitle.setText(postDetailEntity.title);
            viewHolder.ivPostSelectTag.setVisibility(View.GONE);
        }

        //帖子无图正文显示3 行
        if(postDetailEntity.posters.size()==1){
            viewHolder.tvNormalDesc.setMaxLines(4);
        }else{
            viewHolder.tvNormalDesc.setMaxLines(3);
        }

        viewHolder.tvNormalDesc.setText(postDetailEntity.description);

        viewHolder.tvHomePostVDesc.setText(postDetailEntity.description);

        if (postDetailEntity.posters.size() >= 3) {
            viewHolder.rlMiddleVertical.setVisibility(View.VISIBLE);
            viewHolder.rlMiddleHorizontal.setVisibility(View.GONE);
            if (postDetailEntity.posters.size() > 3) {
                List<String> tempUrl = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    tempUrl.add(postDetailEntity.posters.get(i));
                }
                setPosters(viewHolder.multiImageView, tempUrl);
            } else {
                setPosters(viewHolder.multiImageView, postDetailEntity.posters);
            }
        } else {
            viewHolder.rlMiddleVertical.setVisibility(View.GONE);
            viewHolder.rlMiddleHorizontal.setVisibility(View.VISIBLE);
            if (postDetailEntity.posters.size() > 0) {
                ImageLoader.getInstance().displayImage(postDetailEntity.posters.get(0) + Constants.IMAGE_TAILOR_URL, viewHolder.ivNoPic, ImageLoaderOptions.option(R.drawable.img_default_course_system));
                viewHolder.ivNoPic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent2PicDetail = new Intent(mContext, BigPictureActivity.class);
                        intent2PicDetail.putStringArrayListExtra(BigPictureActivity.PICTURE_LIST, postDetailEntity.posters);
                        intent2PicDetail.putExtra(BigPictureActivity.CURRENT_ITEM, 0);
                        mContext.startActivity(intent2PicDetail);
                    }
                });
                viewHolder.ivNoPic.setVisibility(View.VISIBLE);
            } else {
                viewHolder.ivNoPic.setVisibility(View.GONE);
            }
        }

        viewHolder.tvCreateTime.setText(DateUtils.formatCurrentTime(
                postDetailEntity.publish_at * 1000l, System.currentTimeMillis()));


        if (postDetailEntity.is_approval) {
            viewHolder.ivApproval.setImageResource(R.drawable.icon_home_post_approval_light);
            viewHolder.tvApprovalCount.setTextColor(mContext.getResources().getColor(R.color.c_yellow_ffc84a));
        } else {
            viewHolder.ivApproval.setImageResource(R.drawable.icon_home_post_approval_normal);
            viewHolder.tvApprovalCount.setTextColor(mContext.getResources().getColor(R.color.c_gray_aebdcd));
        }
        if (Integer.valueOf(postDetailEntity.approvalcount)==0) {
            viewHolder.tvApprovalCount.setText("赞");
        } else {
            viewHolder.tvApprovalCount.setText(StringUtils.tranNum(postDetailEntity.approvalcount));
        }
        if (Integer.valueOf(postDetailEntity.commentcount) ==0) {
            viewHolder.tvCommentTotal.setText("评论");
        } else {
            viewHolder.tvCommentTotal.setText(StringUtils.tranNum(postDetailEntity.commentcount));
        }

        viewHolder.llBottomApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != homePostLister) {
                    homePostLister.refreshApproval(postDetailEntity, v);
                }
            }
        });
        return convertView;
    }

    private class ViewHolder {

        public TextView tvCircleName;
        public TextView tvHomePostTitle;

        public RelativeLayout rlMiddleHorizontal;
        public TextView tvNormalDesc;
        public ImageView ivNoPic;

        public RelativeLayout rlMiddleVertical;
        public TextView tvHomePostVDesc;

        public TextView tvCommentTotal;
        public TextView tvCreateTime;

        public ImageView ivApproval;
        public TextView tvApprovalCount;

        public MultiImageView multiImageView;
        public LinearLayout llBottomApproval;
        public ImageView ivPostSelectTag;

    }

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

    public interface HomePostLister {
        void refreshApproval(PostDetailEntity postDetailEntity, View v);
    }

    public void setHomePostListener(HomePostLister listener) {
        this.homePostLister = listener;
    }

}
