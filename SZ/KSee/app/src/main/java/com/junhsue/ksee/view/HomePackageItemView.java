package com.junhsue.ksee.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.junhsue.ksee.R;
import com.junhsue.ksee.file.ImageLoaderOptions;
import com.junhsue.ksee.utils.ScreenWindowUtil;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 方案首页的item
 * Created by Sugar on 17/9/27.
 */

public class HomePackageItemView extends FrameLayout {

    private Context mContext;

    private TextView tvTitle;

    private TextView tvReadCount;

    private TextView tvTag;

    private RoundedImageView rivBg;


    public HomePackageItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initLayoutView(context);
    }

    public HomePackageItemView(Context context) {
        super(context);
        mContext = context;
        initLayoutView(context);
    }

    public HomePackageItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initLayoutView(context);
    }

    private void initLayoutView(Context context) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //
        View contentView = layoutInflater.inflate(R.layout.component_package_item, this);

        tvTitle = (TextView) contentView.findViewById(R.id.tv_home_package_title);
        rivBg = (RoundedImageView) contentView.findViewById(R.id.riv_home_package_bg);
        tvReadCount = (TextView) contentView.findViewById(R.id.tv_home_package_read_count);
        tvTag = (TextView) contentView.findViewById(R.id.tv_home_package_tag);

        //视频图片宽度和高度
        //宽度:(屏宽-26px-26px)/2

        ViewGroup.LayoutParams params = rivBg.getLayoutParams();
        params.width = (int) (ScreenWindowUtil.getScreenWidth(mContext) - mContext.getResources().getDimension(R.dimen.dimen_26px) * 2) / 2;
        //高度:
        params.height = (int) ((ScreenWindowUtil.getScreenWidth(mContext)/2 - mContext.getResources().getDimension(R.dimen.dimen_26px))*114/95 );
        rivBg.setLayoutParams(params);



    }

    /**
     * @param title
     * @param url
     * @param count
     * @param tag
     */
    public void setData(String title, String url, int count, String tag) {
        tvTitle.setText(title);
        tvTag.setText("#" + tag);

        ImageLoader.getInstance().displayImage(url, rivBg, ImageLoaderOptions.option(R.drawable.img_default_course_system));
        setReadCount(tvReadCount, count);

    }

    /**
     * 设置浏览阅读量
     *
     * @param tvReadCount
     * @param readCount
     */
    private void setReadCount(TextView tvReadCount, long readCount) {
        if (readCount >= 1000) {
            tvReadCount.setText((readCount / 1000) + "." + (readCount % 1000) / 100 + "k" + "浏览");
        } else {
            tvReadCount.setText(readCount + "浏览");
        }
    }
}
