package com.junhsue.ksee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.junhsue.ksee.R;
import com.junhsue.ksee.entity.CourseSystem;
import com.junhsue.ksee.file.ImageLoaderOptions;
import com.junhsue.ksee.utils.ScreenWindowUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by longer on 17/3/30.
 */

public class CourseSystemAdapter<T extends CourseSystem> extends MyBaseAdapter<T> {


    public ViewHolder mViewHolder;

    private LayoutInflater mInflater;

    private Context mContext;

    public CourseSystemAdapter(Context context) {
        this.mContext=context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    protected View getWrappeView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            mViewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_course_system, null);
            mViewHolder.mImg = (ImageView) convertView.findViewById(R.id.img);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        CourseSystem courseSystem = mList.get(position);
        if (null != convertView) {
            ViewGroup.LayoutParams params = mViewHolder.mImg.getLayoutParams();
            params.height = ScreenWindowUtil.getScreenWidth(mContext) * 9/16;
            mViewHolder.mImg.setLayoutParams(params);

            if(!courseSystem.poster.equals(mViewHolder.mImg.getTag())){
                ImageLoader.getInstance().displayImage(courseSystem.poster, mViewHolder.mImg,
                        ImageLoaderOptions.option(R.drawable.img_default_course_system));
                mViewHolder.mImg.setTag(courseSystem.poster);
            }
        }

        return convertView;
    }


    class ViewHolder {

        public ImageView mImg;

    }

}
