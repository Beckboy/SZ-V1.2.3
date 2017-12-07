package com.junhsue.ksee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.TextView;


import com.junhsue.ksee.R;
import com.junhsue.ksee.entity.VideoEntity;
import com.junhsue.ksee.file.ImageLoaderOptions;
import com.junhsue.ksee.utils.ScreenWindowUtil;
import com.nostra13.universalimageloader.core.ImageLoader;


/**
 * 录播视频适配器
 * Created by Sugar on 17/8/11.
 */

public class VideoAdapter<T extends VideoEntity> extends MyBaseAdapter<VideoEntity> {

    private Context mContext;
    private LayoutInflater mInflater;

    public VideoAdapter(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    protected View getWrappeView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_video, null);
            viewHolder.ivPoster = (ImageView) convertView.findViewById(R.id.iv_video_poster);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_video_title);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        ViewGroup.LayoutParams params = viewHolder.ivPoster.getLayoutParams();

        params.height = ScreenWindowUtil.getScreenWidth(mContext) * 14 / 25;
        viewHolder.ivPoster.setLayoutParams(params);


        VideoEntity videoEntity = mList.get(position);


        if (null != videoEntity.poster && !videoEntity.poster.equals(viewHolder.ivPoster.getTag())) {
            ImageLoader.getInstance().displayImage(videoEntity.poster, viewHolder.ivPoster,
                    ImageLoaderOptions.option(R.drawable.img_default_course_suject));
            viewHolder.ivPoster.setTag(videoEntity.poster);
        }

        viewHolder.tvTitle.setText(videoEntity.title);
        return convertView;
    }

    private class ViewHolder {

        public ImageView ivPoster;
        public TextView tvTitle;
    }
}
