package com.junhsue.ksee.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.junhsue.ksee.R;
import com.junhsue.ksee.entity.CircleEntity;
import com.junhsue.ksee.file.ImageLoaderOptions;
import com.nostra13.universalimageloader.core.ImageLoader;


/**
 * 圈子适配器
 * Created by longer on 17/10/24.
 */

public class CircleAdapter<T extends CircleEntity> extends MyBaseAdapter<CircleEntity> {


    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ViewHolder mViewHolder;

    //
    private ICircleListener mICircleListener;
    //
    public CircleAdapter(Context context){
        mLayoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext=context;
    }

    @Override
    protected View getWrappeView(final int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            mViewHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.item_recommend_circle, null);
            convertView.setTag(mViewHolder);
            mViewHolder.mImgCircle = (ImageView) convertView.findViewById(R.id.img_circle);
            mViewHolder.mTxtTitle = (TextView) convertView.findViewById(R.id.txt_circle_title);
            mViewHolder.mTxtDesc = (TextView) convertView.findViewById(R.id.txt_circle_desc);
            mViewHolder.mImgFavourite = (ImageView) convertView.findViewById(R.id.img_circle_favourite);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        CircleEntity circleEntity = getList().get(position);
        //
        ImageLoader.getInstance().displayImage(circleEntity.poster, mViewHolder.mImgCircle
                , ImageLoaderOptions.option(R.drawable.img_default_course_system));
        //
        mViewHolder.mTxtTitle.setText(circleEntity.name);
        mViewHolder.mTxtDesc.setText(circleEntity.description);
        if (circleEntity.is_concern) {
            mViewHolder.mImgFavourite.setBackgroundResource(R.drawable.icon_circle_favourite);
        } else {
            mViewHolder.mImgFavourite.setBackgroundResource(R.drawable.icon_circle_unfavourite);
        }

        mViewHolder.mImgFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mICircleListener) {
                    mICircleListener.addFavourite(position);
                }
            }
        });

        return convertView;
    }


    public void setICircleListener(ICircleListener mICircleListener) {
        this.mICircleListener = mICircleListener;
    }


    /**
     * 圈子添加收藏
     */
    public interface ICircleListener{

        //添加收藏
        void  addFavourite(int position);
    }


    private class  ViewHolder{

        private ImageView mImgCircle;
        //标题
        private TextView  mTxtTitle;
        //描述
        private TextView mTxtDesc;
        //关注
        private ImageView mImgFavourite;

    }


}