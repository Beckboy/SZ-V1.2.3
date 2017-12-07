package com.junhsue.ksee.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.junhsue.ksee.BigPictureActivity;
import com.junhsue.ksee.R;
import com.junhsue.ksee.adapter.viewholder.CircleViewHolder;
import com.junhsue.ksee.adapter.viewholder.ImageViewHolder;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.entity.PhotoInfo;
import com.junhsue.ksee.entity.PostDetailEntity;
import com.junhsue.ksee.mvp.presenter.CirclePresenter;
import com.junhsue.ksee.net.url.WebViewUrl;
import com.junhsue.ksee.utils.DateUtils;
import com.junhsue.ksee.utils.GlideCircleTransformUtil;
import com.junhsue.ksee.utils.ShareUtils;
import com.junhsue.ksee.utils.StringUtils;
import com.junhsue.ksee.view.ActionSheet;
import com.junhsue.ksee.view.MultiImageView;
import com.umeng.analytics.social.UMPlatformData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hunter_J on 2017/11/2.
 */

public class CircleDetailBestAdapter extends BaseRecycleViewAdapter implements View.OnClickListener{

  public final static int TYPE_HEAD = 0;
  public final static int TYPE_FOOTER = 0;

  private static final int STATE_IDIE= 0;
  private static final int STATE_ACTIVED= 1;
  private static final int STATE_DEACTIVED= 2;
  private int videoState = STATE_IDIE;
  public static final int HEADVIEW_SIZE = 0;

  int curPlayIndex =-1;
  private long mLastTime = 0;
  private int max_size = 1; //最大数量

  private Context mContext;
  private CirclePresenter presenter;
  private ActionSheet shareActionSheetDialog;

  public void setCirclePresenter(CirclePresenter presenter) {
    this.presenter = presenter;
  }

  public CircleDetailBestAdapter(Context context) {
    this.mContext = context;
  }

  @Override
  public int getItemViewType(int position) {
//        if (position == 0){
//            return TYPE_HEAD;
//        }else
     if (position == (max_size-1)){
        return TYPE_FOOTER;
     }
    int itemType = CircleViewHolder.TYPE_IMAGE;
    return itemType;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    RecyclerView.ViewHolder viewHolder = null;
    if (viewType == TYPE_FOOTER){
      View headView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_all_foot, parent, false);
      viewHolder = new FooterViewHolder(headView);
    }else {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_circle_detail_list_all, parent, false);
      view.setOnClickListener(this);
      if (viewType == CircleViewHolder.TYPE_IMAGE) {
        viewHolder = new ImageViewHolder(view);
      }
    }
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
//        if (getItemViewType(position) == TYPE_HEAD){
//            //HeaderViewHolder holder = (HeaderViewHolder) viewHolder;
//        }else {
    if (getItemViewType(position) == TYPE_FOOTER) {
      FooterViewHolder holder = (FooterViewHolder) viewHolder;
      if (max_size == 1) {
        holder.itemView.setVisibility(View.VISIBLE);
        holder.mImgHint.setBackgroundResource(R.drawable.bg_circle_list_best);
        holder.mTvHint.setText("小钬花儿诚挚邀请你来发帖\n" + "说不定将成为精华帖第一人哦 o^_^o");
      } else {
        holder.mLl.setVisibility(View.GONE);
      }
    } else {
      final int circlePosition = position - HEADVIEW_SIZE;
      CircleViewHolder holder = (CircleViewHolder) viewHolder;
      holder.itemView.setTag(position);
      if (datas == null || datas.size() == 0) return;
      final PostDetailEntity circleItem = (PostDetailEntity) datas.get(circlePosition);
      final String circleId = circleItem.circle_id;
      String name = circleItem.nickname;
      String headImg = circleItem.avatar;
      String title = circleItem.title;
      String content = circleItem.description;
      String approvalcount = circleItem.approvalcount;
      String commentCount = circleItem.commentcount;
      long creatTime = circleItem.publish_at;
      boolean hasAnonymous = circleItem.is_anonymous;
      boolean hasApproval = circleItem.is_approval;
      boolean hasFavort = circleItem.is_favorite;
      boolean hasTop = circleItem.is_top;

      Glide.with(mContext).load(headImg).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.icon_avatar).transform(new GlideCircleTransformUtil(mContext)).into(holder.mCircleImageView);

      if (name != null) {
        if (hasAnonymous)
          holder.mTvName.setText("匿名");
        else
          holder.mTvName.setText(name);
      }
      if (creatTime != 0) {
        holder.mTvDate.setText(DateUtils.formatCurrentTime(circleItem.publish_at * 1000l, System.currentTimeMillis()));
      }
      if (title != null) {
        if (hasTop) {
          holder.mTvTitle.setText("     " + title);
          holder.mImgBest.setVisibility(View.VISIBLE);
        }else {
          holder.mTvTitle.setText(title);
          holder.mImgBest.setVisibility(View.GONE);
        }
      }
      if (content != null) {
        holder.mTvContent.setText(content);
      }
      if (approvalcount != null) {
        if (Integer.valueOf(approvalcount) > 0) {
          holder.mTvHot.setText(StringUtils.tranNum(approvalcount));
        } else {
          holder.mTvHot.setText("赞");
        }

      }
      if (commentCount != null) {
        if (Integer.valueOf(commentCount) > 0) {
          holder.mTvChat.setText(StringUtils.tranNum(commentCount));
        } else {
          holder.mTvChat.setText("评论");
        }
      }

      if (hasApproval) {
        Drawable image = mContext.getResources().getDrawable(R.drawable.icon_post_approval_light);
        image.setBounds(0, 0, image.getMinimumWidth(), image.getMinimumHeight());
        holder.mTvHot.setCompoundDrawables(image, null, null, null);
        holder.mTvHot.setTextColor(mContext.getResources().getColor(R.color.c_yellow_ffc84a));
      } else {
        Drawable image = mContext.getResources().getDrawable(R.drawable.icon_post_approval);
        image.setBounds(0, 0, image.getMinimumWidth(), image.getMinimumHeight());
        holder.mTvHot.setCompoundDrawables(image, null, null, null);
        holder.mTvHot.setTextColor(mContext.getResources().getColor(R.color.c_gray_aebdcd));
      }
      if (hasFavort) {
        Drawable image = mContext.getResources().getDrawable(R.drawable.icon_post_collect_selector);
        image.setBounds(0, 0, image.getMinimumWidth(), image.getMinimumHeight());
        holder.mTvCollect.setCompoundDrawables(image, null, null, null);
        holder.mTvCollect.setText("已收藏");
        holder.mTvCollect.setTextColor(mContext.getResources().getColor(R.color.c_red_fc613c));
      } else {
        Drawable image = mContext.getResources().getDrawable(R.drawable.icon_post_collect_normal);
        image.setBounds(0, 0, image.getMinimumWidth(), image.getMinimumHeight());
        holder.mTvCollect.setCompoundDrawables(image, null, null, null);
        holder.mTvCollect.setText("收藏");
        holder.mTvCollect.setTextColor(mContext.getResources().getColor(R.color.c_gray_aebdcd));
      }
      holder.mTvHot.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if (System.currentTimeMillis() - mLastTime < 700)//防止快速点击操作
            return;
          mLastTime = System.currentTimeMillis();
          if (!circleItem.is_approval) {
            presenter.addApproval(position, circleItem.id);
          } else {
            presenter.deleteApproval(position, circleItem.id);
          }
        }
      });
      holder.mTvCollect.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if (System.currentTimeMillis() - mLastTime < 700)//防止快速点击操作
            return;
          mLastTime = System.currentTimeMillis();
          if (!circleItem.is_favorite) {
            presenter.addFavort(position, circleItem.id);
          } else {
            presenter.deleteFavort(position, circleItem.id);
          }
        }
      });
      holder.mTvShare.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          showShareActionSheetDailog(circleItem);
        }
      });
      switch (holder.viewType) {
        case CircleViewHolder.TYPE_URL:// 处理链接动态的链接内容和和图片
          break;
        case CircleViewHolder.TYPE_IMAGE:// 处理图片
          if (holder instanceof ImageViewHolder) {
            List<PhotoInfo> photos = null;
            if (photos == null) {
              photos = new ArrayList<>();
            } else {
              photos.clear();
            }
            for (String photoUrl : circleItem.posters) {
              PhotoInfo photoInfo = new PhotoInfo();
              photoInfo.url = photoUrl + Constants.IMAGE_TAILOR_URL_NORMAL;
              photos.add(photoInfo);
            }
            if (photos != null && photos.size() > 0) {
              ((ImageViewHolder) holder).multiImageView.setVisibility(View.VISIBLE);
              ((ImageViewHolder) holder).multiImageView.setList(photos);
              ((ImageViewHolder) holder).multiImageView.setmOnItemClickListener(new MultiImageView.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                  //imagesize是作为loading时的图片size
                  Intent intent2PicDetail = new Intent(mContext, BigPictureActivity.class);
                  intent2PicDetail.putStringArrayListExtra(BigPictureActivity.PICTURE_LIST, circleItem.posters);
                  intent2PicDetail.putExtra(BigPictureActivity.CURRENT_ITEM, position);
                  mContext.startActivity(intent2PicDetail);
                }
              });
            } else {
              ((ImageViewHolder) holder).multiImageView.setVisibility(View.GONE);
            }
          }
          break;
        default:
          break;
      }
    }
  }


  /**
   * 分享弹出框
   */
  private void showShareActionSheetDailog(PostDetailEntity postDetailEntity) {
    final String path = "";//如果分享图片获取该图片的本地存储地址
    final String webPage = String.format(WebViewUrl.H5_POST_SHARE, postDetailEntity.id);
    final String title = postDetailEntity.title;
    final String desc = postDetailEntity.description;

    shareActionSheetDialog = new ActionSheet(mContext);
    LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View view = inflater.inflate(R.layout.component_share_dailog, null);
    shareActionSheetDialog.setContentView(view);
    shareActionSheetDialog.show();


    LinearLayout llShareFriend = (LinearLayout) view.findViewById(R.id.ll_share_friend);
    LinearLayout llShareCircle = (LinearLayout) view.findViewById(R.id.ll_share_circle);
    TextView cancelButton = (TextView) view.findViewById(R.id.tv_cancel);

    llShareFriend.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        ShareUtils.getInstance(mContext).sharePage(ShareUtils.SendToPlatformType.TO_FRIEND, webPage, path,
                title, desc, UMPlatformData.UMedia.WEIXIN_FRIENDS);
        if (shareActionSheetDialog != null) {
          shareActionSheetDialog.dismiss();
        }

      }
    });

    llShareCircle.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ShareUtils.getInstance(mContext).sharePage(ShareUtils.SendToPlatformType.TO_CIRCLE, webPage, path,
                title, desc, UMPlatformData.UMedia.WEIXIN_CIRCLE);
        if (shareActionSheetDialog != null) {
          shareActionSheetDialog.dismiss();
        }

      }
    });

    cancelButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (shareActionSheetDialog.isShowing()) {
          shareActionSheetDialog.dismiss();
        }
      }
    });

  }

  @Override
  public int getItemCount() {
    max_size = datas.size() + 1; //有head需要+1
    return max_size;
  }

  @Override
  public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
    super.onViewAttachedToWindow(holder);
  }

  @Override
  public void onClick(View view) {
    if (itemListener != null){
      itemListener.onItemClick((Integer) view.getTag());
    }
  }

  public class HeaderViewHolder extends RecyclerView.ViewHolder{

    public HeaderViewHolder(View itemView) {
      super(itemView);
    }
  }


  public class FooterViewHolder extends RecyclerView.ViewHolder {

    public TextView mTvHint;
    public ImageView mImgHint;
    public LinearLayout mLl;

    public FooterViewHolder(View itemView) {
      super(itemView);
      mTvHint = (TextView) itemView.findViewById(R.id.tv_hint);
      mImgHint = (ImageView) itemView.findViewById(R.id.img_hint);
      mLl = (LinearLayout) itemView.findViewById(R.id.ll_img_bitmap);
    }
  }

}
