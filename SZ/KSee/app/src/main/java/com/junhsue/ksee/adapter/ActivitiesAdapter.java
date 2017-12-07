package com.junhsue.ksee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.junhsue.ksee.R;
import com.junhsue.ksee.entity.ActivityEntity;
import com.junhsue.ksee.file.ImageLoaderOptions;
import com.junhsue.ksee.utils.DateUtils;
import com.junhsue.ksee.utils.ScreenWindowUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 活动
 * Created by Sugar on 17/4/25.
 */

public class ActivitiesAdapter<T extends ActivityEntity> extends MyBaseAdapter<ActivityEntity> {

    private Context mContext;
    private LayoutInflater mInflater;

    private OnApprovalListener listener;

    private ViewHolder viewHolder;

    public ActivitiesAdapter(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    protected View getWrappeView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_activity, null);
            viewHolder.ivContentLink = (ImageView) convertView.findViewById(R.id.iv_content_link);
            viewHolder.tvtitle = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tvActivityType = (TextView) convertView.findViewById(R.id.txt_activities_type);
            viewHolder.mTxtEndDate = (TextView) convertView.findViewById(R.id.txt_end_date);
            viewHolder.mTxtActiviesAddress = (TextView) convertView.findViewById(R.id.txt_address);
            viewHolder.mRLHead = (RelativeLayout) convertView.findViewById(R.id.rl_content_head);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        ViewGroup.LayoutParams params = viewHolder.ivContentLink.getLayoutParams();

        params.height = ScreenWindowUtil.getScreenWidth(mContext) * 9 / 16;
        viewHolder.ivContentLink.setLayoutParams(params);
        // viewHolder.mRLHead.setLayoutParams(params);

        final ActivityEntity entity = mList.get(position);

        if (null != entity.poster && !entity.poster.equals(viewHolder.ivContentLink.getTag())) {
            ImageLoader.getInstance().displayImage(entity.poster, viewHolder.ivContentLink,
                    ImageLoaderOptions.option(R.drawable.img_default_course_suject));
            viewHolder.ivContentLink.setTag(entity.poster);
        }

        viewHolder.tvtitle.setText(entity.title);
        viewHolder.tvActivityType.setText(entity.business_name);
        viewHolder.mTxtActiviesAddress.setText(entity.address);
        String date = String.format(mContext.getString(R.string.msg_activies_dead_time_show),
                DateUtils.getFormatData(entity.signup_deadline * 1000l));
        viewHolder.mTxtEndDate.setText(date);
        String address = String.format(mContext.getString(R.string.msg_activies_address), entity.address);
        viewHolder.mTxtActiviesAddress.setText(address);

        return convertView;
    }


    private class ViewHolder {

        public ImageView ivContentLink;
        public TextView tvtitle;
        public TextView tvActivityType;
        //结束时间
        public TextView mTxtEndDate;
        //活动地点
        public TextView mTxtActiviesAddress;

        private RelativeLayout mRLHead;
    }


    public interface OnApprovalListener {

        void onApprovalClick(ActivityEntity entity, int position);

    }

    public void setOnApprovalListener(OnApprovalListener listener) {

        this.listener = listener;

    }

}
