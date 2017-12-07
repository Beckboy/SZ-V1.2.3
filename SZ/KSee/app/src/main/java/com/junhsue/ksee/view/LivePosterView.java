package com.junhsue.ksee.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.junhsue.ksee.R;
import com.junhsue.ksee.entity.LiveEntity;
import com.junhsue.ksee.file.ImageLoaderOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by hunter_J on 2017/8/11.
 */

public class LivePosterView extends FrameLayout {

    private Context mContext;

    private TextView mTvTitle;
    private LinearLayout mLlTtitle;
    private RelativeLayout mRlRoot;
    private ImageView mImgPoster,mImgIcon;

    public LivePosterView(Context context) {
        super(context);
        this.mContext = context;
        initializeView(context, null);
    }

    public LivePosterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initializeView(context, attrs);
    }


    public LivePosterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initializeView(context, attrs);
    }


    private void initializeView(Context context, AttributeSet attrs) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //
        View view = layoutInflater.inflate(R.layout.component_live_poster, this);

        mRlRoot = (RelativeLayout) view.findViewById(R.id.rl_live_poster);
        mImgPoster = (ImageView) view.findViewById(R.id.img_poster);
        mImgIcon = (ImageView) view.findViewById(R.id.img_icon);
        mLlTtitle = (LinearLayout) view.findViewById(R.id.ll_title);
        mTvTitle = (TextView) view.findViewById(R.id.tv_title);

    }


    /**
     * 设置海报图片
     *
     * @param url
     */
    public void setPosterBackGround(String url) {
        mImgPoster.setVisibility(VISIBLE);
        ImageLoader.getInstance().displayImage(url, (ImageView) mImgPoster,
                ImageLoaderOptions.optionRounded(R.drawable.img_default_course_suject,0));
    }

    /**
     * 设置海报Title
     * @param text
     */
    public void setTitle(String text) {
        mTvTitle.setVisibility(VISIBLE);
        mTvTitle.setText(text);
    }

    /**
     * 设置海报Title
     * @param living 直播态
     */
    public void setLive_Living(int living) {
        if (living != LiveEntity.LiveStatus.LIVING) return;
        mImgIcon.setBackgroundResource(R.drawable.icon_live_tag_ing);
    }

}
