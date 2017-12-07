package com.junhsue.ksee.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.junhsue.ksee.R;
import com.junhsue.ksee.file.ImageLoaderOptions;
import com.junhsue.ksee.utils.DateUtils;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 首页百科文章item
 * Created by longer on 17/8/11.
 * mofidy by Sugar on 17/9/8;
 */

public class HomeArticleItemView extends FrameLayout {


    private Context mContext;
    //标题
    private TextView mTxtTitle;
    //
    private RoundedImageView mImg;
    //
    private TextView mTxtRecount;
    //
    private TextView mTxtTag;

    //
    private TextView mTxtPublishAt;

    public HomeArticleItemView(Context context) {
        super(context);
        mContext = context;
        initializeView(context);
    }

    public HomeArticleItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initializeView(context);
    }


    public HomeArticleItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initializeView(context);
    }

    private void initializeView(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //
        View view = layoutInflater.inflate(R.layout.item_realize_article, this);
        mTxtTitle = (TextView) view.findViewById(R.id.txt_title);
        mImg = (RoundedImageView ) view.findViewById(R.id.img);
        mTxtRecount = (TextView) view.findViewById(R.id.txt_read_count);
        mTxtTag = (TextView) view.findViewById(R.id.txt_tag);
        mTxtPublishAt = (TextView) view.findViewById(R.id.txt_publish_at);
    }


    /**
     * 填充内容
     *
     * @param title 标题
     * @param url   图片
     * @param count 大小
     * @param tag   标签
     */
    public void setData(String title, String url, String count, String tag) {
        mTxtTitle.setText(title);
        ImageLoader.getInstance().displayImage(url, mImg, ImageLoaderOptions.optionRounded(R.drawable.img_default_course_system, 10));
        mTxtRecount.setText(String.format(mContext.getString(R.string.msg_realize_article_read_number),
                String.valueOf(count)));
        mTxtTag.setText("#" + tag);

    }

    /**
     * 填充内容
     *
     * @param title       标题
     * @param url         图片
     * @param pulish_time 发布时间
     * @param count       大小
     * @param tag         标签
     */
    public void setData(String title, String url, String pulish_time, String count, String tag) {
        mTxtTitle.setText(title);
        ImageLoader.getInstance().displayImage(url, mImg, ImageLoaderOptions.option(R.drawable.img_default_course_system));

        long readCount = Integer.valueOf(count);

        setReadCount(mTxtRecount, readCount);

        mTxtTag.setText("#" + tag);
        String date = dealWithPushlishTime((Integer.valueOf(pulish_time)*1000l));
        mTxtPublishAt.setText(date);
    }


    private String dealWithPushlishTime(long publish_a) {
        String date = DateUtils.timestampToPatternTime(publish_a, "MM-dd");
        return date;
    }

    /**
     * 设设置阅读量
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
