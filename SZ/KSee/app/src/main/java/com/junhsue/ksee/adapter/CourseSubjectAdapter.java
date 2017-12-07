package com.junhsue.ksee.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.junhsue.ksee.R;
import com.junhsue.ksee.entity.Course;
import com.junhsue.ksee.file.ImageLoaderOptions;
import com.junhsue.ksee.utils.DateUtils;
import com.junhsue.ksee.utils.NumberFormatUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 主题课课程适配器
 * Created by longer on 17/3/27.
 */

public class CourseSubjectAdapter<T extends Course> extends MyBaseAdapter<T> {

    private static  String TAG="CourseSubjectAdapter";

    private Context mContext;
    private LayoutInflater mInflater;

    public CourseSubjectAdapter(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    protected View getWrappeView(int position, View convertView, ViewGroup parent) {

        ViewHolder mViewHolderChild = null;

        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.item_course_suject_child, null);
            mViewHolderChild = new ViewHolder(convertView);
            mViewHolderChild.mImg=(ImageView) convertView.findViewById(R.id.img);
            convertView.setTag(mViewHolderChild);
        } else {
            mViewHolderChild = (ViewHolder) convertView.getTag();
        }
        Course course = getList().get(position);
        if (null != course) {
            if(!course.poster.equals(mViewHolderChild.mImg.getTag())){
                ImageLoader.getInstance().displayImage(course.poster, mViewHolderChild.mImg,
                        ImageLoaderOptions.optionRounded(R.drawable.img_default_course_suject,10));
                mViewHolderChild.mImg.setTag(course.poster);
            }

            mViewHolderChild.mTxtCourseTitle.setText(course.title);
            mViewHolderChild.mTxtCourseCity.setText(String.format("城市: %s",TextUtils.isEmpty(course.city)?"上海":course.city));
            mViewHolderChild.mTxtCourseTime.setText(String.format("时间: %s",
                    DateUtils.timestampToPatternTime(course.start_time*1000l,"MM月dd日")));
            mViewHolderChild.mTxtPrice.setText(NumberFormatUtils.formatPointTwo(course.amount));

        }
        if(getList().size()-1 ==  position){
            mViewHolderChild.mImgLine.setVisibility(View.GONE);
        }else
            mViewHolderChild.mImgLine.setVisibility(View.VISIBLE);
        return convertView;
    }


    class ViewHolder {
        public ImageView mImg;
        public TextView mTxtCourseTitle;//课程标题
        public TextView mTxtCourseCity;//课程城市
        public TextView mTxtPrice;//课程价格
        public TextView mTxtCourseTime;//课程开始的时间
        public ImageView mImgLine;
        public ViewHolder(View view){
            mImg = (ImageView) view.findViewById(R.id.img);
            mTxtCourseTitle = (TextView) view.findViewById(R.id.txt_title);
            mTxtCourseCity = (TextView) view.findViewById(R.id.txt_city);
            mTxtPrice = (TextView) view.findViewById(R.id.txt_price);
            mImgLine=(ImageView)view.findViewById(R.id.img_line);
            mTxtCourseTime=(TextView)view.findViewById(R.id.txt_time);
        }
    }

}
