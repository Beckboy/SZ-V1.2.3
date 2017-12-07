package com.junhsue.ksee.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.junhsue.ksee.R;
import com.junhsue.ksee.entity.RealizeArticleEntity;
import com.junhsue.ksee.file.ImageLoaderOptions;
import com.junhsue.ksee.utils.DateUtils;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 文章适配器
 * Created by longer on 17/8/3.
 */

public class RealizeArticleAdapter<T extends RealizeArticleEntity> extends MyBaseAdapter<T> {


    private Context mContext;
    //
    private LayoutInflater mLayoutInflater;
    //
    private ViewHolder mViewHolder;

    public RealizeArticleAdapter(Context context) {
        this.mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    protected View getWrappeView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            mViewHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.item_realize_article, null);
            //
            mViewHolder.mImg = (RoundedImageView) convertView.findViewById(R.id.img);
            mViewHolder.mTxtTitle = (TextView) convertView.findViewById(R.id.txt_title);
            mViewHolder.mTxtArticleTag = (TextView) convertView.findViewById(R.id.txt_tag);
            mViewHolder.mTxtReadNumer = (TextView) convertView.findViewById(R.id.txt_read_count);
            mViewHolder.mtxtPublishAt = (TextView) convertView.findViewById(R.id.txt_publish_at);
            mViewHolder.mLLAritcleContentView = (LinearLayout) convertView.findViewById(R.id.ll_article_content_view);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        RealizeArticleEntity realizeArticleEntity = getList().get(position);
        if (position%2!=0){
            mViewHolder.mLLAritcleContentView.setBackgroundColor(mContext.getResources().getColor(R.color.c_gray_f3f6f7));
        }
        //
        mViewHolder.mTxtTitle.setText(realizeArticleEntity.title);
        if (realizeArticleEntity.tags.size() > 0) {
            mViewHolder.mTxtArticleTag.setText("#" + realizeArticleEntity.tags.get(0));
        }
     /*   mViewHolder.mTxtReadNumer.setText(String.format(mContext.getString(R.string.msg_realize_article_read_number),
                String.valueOf(realizeArticleEntity.readcount)));*/

        if (null != realizeArticleEntity.poster &&
                !realizeArticleEntity.poster.equals(mViewHolder.mImg.getTag())) {
            ImageLoader.getInstance().displayImage(realizeArticleEntity.poster, mViewHolder.mImg,
                    ImageLoaderOptions.option(R.drawable.img_default_course_suject));
            mViewHolder.mImg.setTag(realizeArticleEntity.poster);
        } else if (TextUtils.isEmpty(realizeArticleEntity.poster)) {
            ImageLoader.getInstance().displayImage("", mViewHolder.mImg,
                    ImageLoaderOptions.option(R.drawable.img_default_course_suject));
        }
        String date = DateUtils.timestampToPatternTime(Integer.valueOf(realizeArticleEntity.publish_at) * 1000l, "MM-dd");
        mViewHolder.mtxtPublishAt.setText(date);
        setReadCount(mViewHolder.mTxtReadNumer, Integer.valueOf(realizeArticleEntity.readcount));
        return convertView;
    }


    class ViewHolder {

        //文章标题
        private TextView mTxtTitle;
        //图片背景
        private RoundedImageView mImg;
        //阅读数量
        private TextView mTxtReadNumer;
        //文章标签
        private TextView mTxtArticleTag;
        //文章日期
        private TextView mtxtPublishAt;

        private LinearLayout mLLAritcleContentView;
    }


    /**
     * 设置阅读量
     *
     * @param tvReadCount
     * @param readCount
     */
    private void setReadCount(TextView tvReadCount, long readCount) {
        if (readCount >= 1000) {
            tvReadCount.setText((readCount / 1000) + "." + (readCount % 1000) / 100 + "k" + "阅读 ");
        } else {
            tvReadCount.setText(readCount + "阅读 ");
        }
    }

}
