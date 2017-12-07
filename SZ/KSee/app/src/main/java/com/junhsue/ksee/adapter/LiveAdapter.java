package com.junhsue.ksee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.junhsue.ksee.R;
import com.junhsue.ksee.entity.LiveEntity;
import com.junhsue.ksee.file.ImageLoaderOptions;
import com.junhsue.ksee.utils.DateUtils;
import com.junhsue.ksee.utils.StringUtils;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 直播列表
 * Created by Sugar on 17/8/11.
 */

public class LiveAdapter<T extends LiveEntity> extends MyBaseAdapter<LiveEntity> {

    private Context mContext;
    private LayoutInflater mInflater;

    public LiveAdapter(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    protected View getWrappeView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_live_list, null);
            viewHolder.rivPoster = (RoundedImageView) convertView.findViewById(R.id.riv_live_poster);
            viewHolder.rivTag = (RoundedImageView) convertView.findViewById(R.id.riv_live_tag);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_live_list_title);
            viewHolder.tvSpeaker = (TextView) convertView.findViewById(R.id.tv_live_speaker);
            viewHolder.tvStartTime = (TextView) convertView.findViewById(R.id.tv_live_start_time);
            viewHolder.divider=convertView.findViewById(R.id.v_live_divider);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        LiveEntity entity = mList.get(position);

        if (null != entity.poster && !entity.poster.equals(viewHolder.rivPoster.getTag())) {
            ImageLoader.getInstance().displayImage(entity.poster, viewHolder.rivPoster,
                    ImageLoaderOptions.option(R.drawable.img_default_course_suject));
            viewHolder.rivPoster.setTag(entity.poster);
        }

        viewHolder.tvTitle.setText(entity.title);
        viewHolder.tvSpeaker.setText(entity.speaker);
        viewHolder.tvStartTime.setText(DateUtils.timestampToPatternTime(entity.start_time * 1000l, "MM-dd HH:mm"));

        if (StringUtils.isBlank(entity.start_time + "")) {
            viewHolder.tvStartTime.setVisibility(View.GONE);
        } else {
            viewHolder.tvStartTime.setVisibility(View.VISIBLE);
        }
        setImgTag(entity.living_status, viewHolder.rivTag);

        if (position==0){
            viewHolder.divider.setVisibility(View.GONE);
        }else {
            viewHolder.divider.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    private class ViewHolder {

        public RoundedImageView rivPoster;
        public RoundedImageView rivTag;
        public TextView tvTitle;
        public TextView tvSpeaker;
        public TextView tvStartTime;

        public View divider;

    }

    /**
     * 设置直播状态
     * <p>
     * 1.当前系统的时间 小于直播开始时间,说明直播未开始
     * <p>
     * 2.当前时间大于直播结束时间,直播已结束
     * <p>
     * 3.当前时间大于等于直播开始时间&&小于等于结束时间,直播进行中
     * <p>
     * <p>
     * note : 单位为秒
     */
    private void setImgTag(int status, RoundedImageView mImgLiveTag) {
        long currentTime = System.currentTimeMillis() / 1000;
        if (status == LiveEntity.LiveStatus.NO_START) {
            //直播未开始
            ImageLoader.getInstance().displayImage("", mImgLiveTag,
                    ImageLoaderOptions.option(R.drawable.icon_live_notbegin_tag_small));


        } else if (status == LiveEntity.LiveStatus.END) {
            //直播已结束
            ImageLoader.getInstance().displayImage("", mImgLiveTag,
                    ImageLoaderOptions.option(R.drawable.icon_live_end_tag_small));
        } else if (status == LiveEntity.LiveStatus.LIVING) {
            //正在直播
            ImageLoader.getInstance().displayImage("", mImgLiveTag,
                    ImageLoaderOptions.option(R.drawable.icon_live_living_tag_small));

        }
    }
}
