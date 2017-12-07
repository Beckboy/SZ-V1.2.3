package com.junhsue.ksee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.junhsue.ksee.R;
import com.junhsue.ksee.entity.ActivityCommentEntity;
import com.junhsue.ksee.file.ImageLoaderOptions;
import com.junhsue.ksee.utils.DateUtils;
import com.junhsue.ksee.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by longer on 17/5/4.
 */

public class ActivityCommentAdapter<T extends ActivityCommentEntity> extends MyBaseAdapter<T> {


    public Context mContext;

    private ViewHolder mViewHolder;
    private LayoutInflater mLayoutInflater;

    public ActivityCommentAdapter(Context context) {
        this.mContext = context;
        mLayoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    protected View getWrappeView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            mViewHolder=new ViewHolder();
            convertView=mLayoutInflater.inflate(R.layout.item_activity_comment,null);
            mViewHolder.mCircleImageView=(CircleImageView) convertView.findViewById(R.id.imgAvatar);
            mViewHolder.mTxtUserName=(TextView)convertView.findViewById(R.id.txt_user_name);
            mViewHolder.mTxtUnit=(TextView)convertView.findViewById(R.id.txt_unit);
            mViewHolder.mTxtComment=(TextView)convertView.findViewById(R.id.txt_comment);
            mViewHolder.mTxtTime=(TextView)convertView.findViewById(R.id.txt_comment_time);
            mViewHolder.mViewLine=convertView.findViewById(R.id.view_line);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder=(ViewHolder)convertView.getTag();
        }
        ActivityCommentEntity activityCommentEntity=getList().get(position);
        if(null==activityCommentEntity) return convertView;

        ImageLoader.getInstance().displayImage(activityCommentEntity.avatar,mViewHolder.mCircleImageView,
                ImageLoaderOptions.option(R.drawable.img_default_course_suject));
        mViewHolder.mTxtUserName.setText(activityCommentEntity.nickname);
        mViewHolder.mTxtComment.setText(activityCommentEntity.content);
        mViewHolder.mTxtUnit.setText(activityCommentEntity.position);
        mViewHolder.mTxtTime.setText(DateUtils.fromTheCurrentTime(activityCommentEntity.create_at,
                System.currentTimeMillis()));

        if(position==getList().size()-1){
            mViewHolder.mViewLine.setVisibility(View.INVISIBLE);
        }else{
            mViewHolder.mViewLine.setVisibility(View.VISIBLE);
        }
        return convertView;
    }


    class ViewHolder {

        //头像
        public  CircleImageView mCircleImageView;
        //用户昵称
        public  TextView mTxtUserName;
        //所属机构
        public  TextView mTxtUnit;
        //用户评论
        public  TextView mTxtComment;
        //评论发表的时间
        public  TextView mTxtTime;
        //
        public View mViewLine;

    }
}
