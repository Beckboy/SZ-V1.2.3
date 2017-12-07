package com.junhsue.ksee.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.junhsue.ksee.BaseActivity;
import com.junhsue.ksee.R;
import com.junhsue.ksee.utils.ScreenWindowUtil;

/**
 * Created by hunter_J on 2017/8/10.
 */

public class RealizeTagsListHeadView extends RelativeLayout {

    private ImageView mImgHead;
    private LinearLayout mImgBack;
    private Context mContext;

    public RealizeTagsListHeadView(Context context) {
        super(context);
        init(context, null);
    }

    public RealizeTagsListHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RealizeTagsListHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }


    private void init(Context context, AttributeSet attrs){
        mContext = context;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //
        View view = layoutInflater.inflate(R.layout.v_realize_tags_list_head, this);
        mImgHead = (ImageView) view.findViewById(R.id.img_head);
        mImgBack = (LinearLayout) view.findViewById(R.id.img_back);

        setHeadLayoutParam(mImgHead);

        setBackClick(mImgBack);
    }

    /**
     * 返回按钮的点击事件
     * @param mImgBack
     */
    private void setBackClick(LinearLayout mImgBack) {
        mImgBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseActivity activity = (BaseActivity) mContext;
                activity.popStack();
            }
        });
    }

    private void setHeadLayoutParam(ImageView mImgBack) {
        int width = ScreenWindowUtil.getScreenWidth(mContext);
        ViewGroup.LayoutParams params = mImgBack.getLayoutParams();
        params.width = width;
        params.height = (int) ((375.0/750.0) * width);
        mImgBack.setLayoutParams(params);
    }


    /**
     * 设置Head图片
     *
     * @param index 标题
     *  展示顺序：校长核心 行政人事 市场招生 教学教务 创业者 政策项目
     */
    public void setBackground(int index) {
        mImgBack.setVisibility(VISIBLE);
        mImgHead.setVisibility(VISIBLE);
        switch (index){
            case 23: //行政人事
                //行政人事
                mImgHead.setBackgroundResource(R.drawable.zhi_img_administrative);
                break;
            case 24: //校长核心
                //校长核心
                mImgHead.setBackgroundResource(R.drawable.zhi_img_principal);
                break;
            case 25: //市场
                //市场招生
                mImgHead.setBackgroundResource(R.drawable.zhi_img_market);
                break;
            case 26: //教学教务
                //教学教务
                mImgHead.setBackgroundResource(R.drawable.zhi_img_teaching);
                break;
            case 28: //政策项目
                //政策项目
                mImgHead.setBackgroundResource(R.drawable.zhi_img_policies);
                break;
            case 31: //创业者
                //创业者
                mImgHead.setBackgroundResource(R.drawable.zhi_img_entrepren);
                break;
            default:
                mImgHead.setBackgroundResource(R.drawable.shi_calendar_def);
                break;
        }
    }



}
