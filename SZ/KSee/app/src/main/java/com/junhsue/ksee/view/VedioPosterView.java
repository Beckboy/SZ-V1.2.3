package com.junhsue.ksee.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.junhsue.ksee.R;
import com.junhsue.ksee.file.ImageLoaderOptions;
import com.junhsue.ksee.utils.ScreenWindowUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by hunter_J on 2017/8/12.
 */

public class VedioPosterView extends FrameLayout {

    //文章标题
    public TextView mTxtTitle;
    //海报背景
    public ImageView mImgPoster;

    private Context mContext;


    public VedioPosterView(Context context) {
        super(context);
        this.mContext = context;
        initializeView(context, null);
    }

    public VedioPosterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initializeView(context, attrs);
    }

    public VedioPosterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initializeView(context, attrs);
    }


    private void initializeView(Context context, AttributeSet attrs) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //
        View view = layoutInflater.inflate(R.layout.component_vedio_poster, this);

        mTxtTitle = (TextView) view.findViewById(R.id.tv_title);
        mImgPoster = (ImageView) view.findViewById(R.id.img_poster);
        //
    }


    /**
     * 设置文本内容
     *
     * @param text
     */
    public void setTitleText(String text) {
        mTxtTitle.setText(text);
    }

    /**
     * 设置海报图片
     *
     * @param url
     */
    public void setPosterBackGround(String url) {
        ImageLoader.getInstance().displayImage(url, (ImageView) mImgPoster,
                        ImageLoaderOptions.optionRounded(R.drawable.img_default_course_suject,20));
    }


}
