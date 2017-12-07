package com.junhsue.ksee.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.junhsue.ksee.R;
import com.junhsue.ksee.entity.HomeVideoEntity;
import com.junhsue.ksee.file.ImageLoaderOptions;
import com.junhsue.ksee.utils.ScreenWindowUtil;
import com.nostra13.universalimageloader.core.ImageLoader;


/**
 * 录播item
 * Created by longer on 17/8/11.
 */

public class HomeVideoItemView extends FrameLayout {

    //上下文
    private Context mContext;
    //
    private ImageView mImgLiveVideo;
    //
    private TextView mTxTitle;
    //
    private View mLine;
    //播放次数
    private TextView mTxtPlayNumber;
    //播放时长
    private TextView mTxtPlayTime;
    //描述
    private TextView mTxtDesc;

    public HomeVideoItemView(Context context) {
        super(context);
        mContext = context;
        initializeView(context);
    }

    public HomeVideoItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initializeView(context);

    }

    public HomeVideoItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initializeView(context);

    }

    private void initializeView(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //
        View view = layoutInflater.inflate(R.layout.component_home_video_item, this);
        mImgLiveVideo = (ImageView) view.findViewById(R.id.img_live_video);
        mTxtPlayNumber=(TextView)view.findViewById(R.id.txt_content_video_play_number);
        mTxtDesc=(TextView)view.findViewById(R.id.txt_content_video_desc);
        mTxtPlayTime=(TextView)view.findViewById(R.id.txt_content_video_time);
        mTxTitle = (TextView) view.findViewById(R.id.txt_video_title);
        mLine = view.findViewById(R.id.view_line);

    }

    /**
     * 填充标题,图片
     */
    public void setData(HomeVideoEntity homeVideoEntity) {

        ViewGroup.LayoutParams params = mImgLiveVideo.getLayoutParams();
        params.height = ScreenWindowUtil.getScreenWidth(mContext) * 9 / 16;
        mImgLiveVideo.setLayoutParams(params);

        ImageLoader.getInstance().displayImage(homeVideoEntity.poster, mImgLiveVideo,
                ImageLoaderOptions.optionRounded(R.drawable.img_default_course_system, 10));

        mTxtDesc.setText(homeVideoEntity.description);
        mTxTitle.setText(homeVideoEntity.title);
        mTxtPlayNumber.setText(homeVideoEntity.readcount+"播放");
        mTxtPlayTime.setText(homeVideoEntity.duration);
    }

    /**
     * 隐藏底部馅
     */
    public void hindLine() {
        mLine.setVisibility(View.GONE);
    }
}
