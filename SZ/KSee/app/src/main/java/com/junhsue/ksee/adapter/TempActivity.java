package com.junhsue.ksee.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.junhsue.ksee.BaseActivity;
import com.junhsue.ksee.PostDetailActivity;
import com.junhsue.ksee.R;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.dto.PostDetailListDTO;
import com.junhsue.ksee.entity.PostDetailEntity;
import com.junhsue.ksee.file.ImageLoaderOptions;
import com.junhsue.ksee.net.api.OKHttpNewSocialCircle;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.view.CircleImageView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by longer on 17/11/8.
 */

public class TempActivity extends BaseActivity {

    private Context mContext;

    private LinearLayout mLLContentPostSelection;

    //精选帖子
    private ArrayList<PostDetailEntity> mListPostSelection=new ArrayList<PostDetailEntity>();
    //
    private LinearLayout mLLPostSelection;
    @Override
    protected void onReceiveArguments(Bundle bundle) {

    }

    @Override
    protected int setLayoutId() {
        return 0;
    }

    @Override
    protected void onInitilizeView() {

        mLLContentPostSelection=(LinearLayout) findViewById(R.id.ll_content_post_selection);
        mLLPostSelection=(LinearLayout)findViewById(R.id.ll_post_selection);
    }


    /**
     * 获取精选帖子列表
     */
    private void getPostSelection() {
        String page = "0";
        String paseSize = "3";
        String top = "1";
        OKHttpNewSocialCircle.getInstance().getCircleBarList(page, paseSize, top, new RequestCallback<PostDetailListDTO>() {
            @Override
            public void onError(int errorCode, String errorMsg) {

            }

            @Override
            public void onSuccess(PostDetailListDTO response) {

                if(response.list.size()==0){
                    //做相应的处理
                    mLLContentPostSelection.setVisibility(View.GONE);

                }else{
                    mLLContentPostSelection.setVisibility(View.VISIBLE);
                    mListPostSelection.clear();
                    mListPostSelection.addAll(response.list);
                    fillPostSelection(response.list);
                }
            }
        });
    }


    /**
     * 填充精选帖子
     */

    private void fillPostSelection(List<PostDetailEntity> lists) {
        ArrayList<PostDetailEntity> postS=new ArrayList<PostDetailEntity>();
        mLLPostSelection.removeAllViews();
        postS.addAll(lists);

        for(int i=0;i<postS.size();i++){
            PostDetailEntity postDetailEntity=postS.get(i);
            addPostView(postDetailEntity);
        }
    }

    /**
     * 修改点赞状态
     * @param circleId 圈子
     * @param isFavorite
     */
    private void updatePostLikeStatus(String circleId,boolean isFavorite){
            for(int i=0;i<mListPostSelection.size();i++){
                PostDetailEntity postDetailEntity=mListPostSelection.get(i);
                int  count= Integer.parseInt(postDetailEntity.approvalcount);
                if(postDetailEntity.circle_id==circleId){
                    postDetailEntity.is_favorite=isFavorite;
                    postDetailEntity.commentcount=isFavorite?String.valueOf(count+1)
                            :String.valueOf(count-1);
                    break;
                }
            }
    }


    private void addPostView(final PostDetailEntity postDetailEntity) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_home_post_selection, null);
        //裙子
        TextView mTxtTag = (TextView) view.findViewById(R.id.txt_tag);
        //帖子标题
        TextView mTxtPostTitle = (TextView) view.findViewById(R.id.txt_title);
        TextView txtPostDesc = (TextView) view.findViewById(R.id.txt_post_desc);
        RoundedImageView roundedImageView = (RoundedImageView) view.findViewById(R.id.img_post);
        CircleImageView circleImageView = (CircleImageView) view.findViewById(R.id.img_post_user);
        //帖子点赞
        LinearLayout llPostLike = (LinearLayout) view.findViewById(R.id.ll_post_like);
        ImageView imgPostLike = (ImageView) view.findViewById(R.id.img_post_like);
        TextView txtLikeCount = (TextView) view.findViewById(R.id.txt_post_count);
        //评论数
        TextView txtCommandCount = (TextView) view.findViewById(R.id.txt_msg_count);

        mTxtTag.setText(postDetailEntity.circle_name);
        mTxtPostTitle.setText(postDetailEntity.title);
        txtPostDesc.setText(postDetailEntity.description);
        txtLikeCount.setText(postDetailEntity.approvalcount);
        txtCommandCount.setText(postDetailEntity.commentcount);
        String url=postDetailEntity.posters.size()>0?postDetailEntity.posters.get(0):"";
        ImageLoader.getInstance().displayImage(url,roundedImageView,ImageLoaderOptions.option(R.drawable.img_default_course_system));
        //添加视图
        mLLPostSelection.addView(view);
        if (postDetailEntity.is_favorite) {
            txtLikeCount.setTextColor(Color.parseColor("#FFC84A"));
            imgPostLike.setBackgroundResource(R.drawable.icon_home_post_selected);
        } else {
            txtLikeCount.setTextColor(Color.parseColor("#AEBDCD"));
            imgPostLike.setBackgroundResource(R.drawable.icon_home_post_normal);
        }

        ImageLoader.getInstance().displayImage(postDetailEntity.avatar, circleImageView
                , ImageLoaderOptions.option(R.drawable.pic_default_avatar));

        //点赞
        llPostLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(postDetailEntity.is_favorite){
                    //取消点赞
                    updatePostLikeStatus(postDetailEntity.circle_id,false);
                }else{
                    //点赞
                    updatePostLikeStatus(postDetailEntity.circle_id,true);
                }
                fillPostSelection(mListPostSelection);
            }
        });

        //跳转到贴子详情
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,PostDetailActivity.class);
                intent.putExtra(Constants.POST_DETAIL_ID,postDetailEntity.circle_id);
                startActivity(intent);
            }
        });
    }
}
