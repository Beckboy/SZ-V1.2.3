package com.junhsue.ksee.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.junhsue.ksee.R;
import com.junhsue.ksee.SendPostActivity;
import com.junhsue.ksee.utils.StatisticsUtil;


/**
 * 主页tab
 * Created by longer on 17/3/24.
 */

public class MainTabView extends FrameLayout implements View.OnClickListener {


    private ImageView mImgKnowledge;//识
    private ImageView mImgRealize;//知
    private ImageView mImgSocialCircle;//社
    private ImageView mImgCollege;//塾
    private ImageView mImgMySpace;//吾

    //记录点击item
    private int mIndex;
    private IMainTabClickListener mIMainTabClickListener;

    private Context mContext;

    /**
     * 首页Tab监听
     */
    public interface IMainTabClickListener {

        void onClick(int index);
    }


    public interface IMainTab {

        public final static int KONWLEDGE = 0;
        public final static int REALIZE = 1;
        public final static int SOCIAL_CIRCLE = 2;
        public final static int COLLEGE = 3;
        public final static int MY_SPACE = 4;

    }

    public MainTabView(Context context) {
        super(context);
        mContext = context;
        initilizeView(context);
    }

    public MainTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initilizeView(context);

    }

    public MainTabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initilizeView(context);

    }


    /***/
    private void initilizeView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.component_main_tab, this);

        mImgKnowledge = (ImageView) view.findViewById(R.id.img_knowledge);
        mImgRealize = (ImageView) view.findViewById(R.id.img_realize);
        mImgSocialCircle = (ImageView) view.findViewById(R.id.img_social_circle);
        mImgCollege = (ImageView) view.findViewById(R.id.img_colleage);
        mImgMySpace = (ImageView) view.findViewById(R.id.img_my_space);

        //
        findViewById(R.id.ll_knowledge).setOnClickListener(this);
        findViewById(R.id.ll_realize).setOnClickListener(this);
        findViewById(R.id.ll_social_circle).setOnClickListener(this);
        findViewById(R.id.ll_college).setOnClickListener(this);
        findViewById(R.id.ll_my_space).setOnClickListener(this);
        //
        setDefaultTab();
        //set background
    }


    private void setDefaultTab() {
        mImgKnowledge.setBackgroundResource(R.drawable.btn_knowledge_press);
        mIndex = IMainTab.KONWLEDGE;
    }


    public void setTabColor(int index) {

        mImgKnowledge.setBackgroundResource(R.drawable.btn_knowledge_normal);
//        mImgRealize.setBackgroundResource(R.drawable.btn_realize_normal);
        mImgRealize.setBackgroundResource(R.drawable.btn_senfposter_normal);
        mImgSocialCircle.setBackgroundResource(R.drawable.btn_social_circle_normal);
        mImgCollege.setBackgroundResource(R.drawable.btn_college_normal);
        mImgMySpace.setBackgroundResource(R.drawable.btn_my_space_normal);

        switch (index) {
            case IMainTab.KONWLEDGE:
                mImgKnowledge.setBackgroundResource(R.drawable.btn_knowledge_press);
                mIndex = IMainTab.KONWLEDGE;
                StatisticsUtil.getInstance(mContext).onCountPage("1.1");

                break;
            case IMainTab.SOCIAL_CIRCLE:
                mImgSocialCircle.setBackgroundResource(R.drawable.btn_social_circle_press);
                mIndex = IMainTab.SOCIAL_CIRCLE;
                StatisticsUtil.getInstance(mContext).onCountPage("1.3");

                break;
            case IMainTab.REALIZE:
//                mImgRealize.setBackgroundResource(R.drawable.btn_realize_press);
                mImgRealize.setBackgroundResource(R.drawable.btn_senfposter_normal);
//                mIndex = IMainTab.REALIZE;
                StatisticsUtil.getInstance(mContext).onCountPage("1.2");

                break;
            case IMainTab.COLLEGE:
                mImgCollege.setBackgroundResource(R.drawable.btn_college_press);
                mIndex = IMainTab.COLLEGE;
                StatisticsUtil.getInstance(mContext).onCountPage("1.4");

                break;
            case IMainTab.MY_SPACE:
                mImgMySpace.setBackgroundResource(R.drawable.btn_my_space_press);
                mIndex = IMainTab.MY_SPACE;
                StatisticsUtil.getInstance(mContext).onCountPage("1.5");

                break;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ll_knowledge:  //识模块
                if (mIndex == IMainTab.KONWLEDGE) {
                    return;
                }
                mIndex = IMainTab.KONWLEDGE;
                break;
            case R.id.ll_social_circle: //社模块
                if (mIndex == IMainTab.SOCIAL_CIRCLE) {
                    return;
                }
                mIndex = IMainTab.SOCIAL_CIRCLE;
                break;
            case R.id.ll_realize:   //fatie
                /**
                 * 知的跳转
                 */
//                if (mIndex == IMainTab.REALIZE) {
//                    return;
//                }
//                mIndex = IMainTab.REALIZE;

                /**
                 * 发帖弹窗
                 */
//                final SendPostPopupWindow sendPostPopupWindow = new SendPostPopupWindow(mContext);
//                sendPostPopupWindow.showView(this);
//                sendPostPopupWindow.setIOnClickListener(new SendPostPopupWindow.IOnClickListener() {
//                    @Override
//                    public void onClick() {
//                        Intent intent2SendPost = new Intent(mContext, SendPostActivity.class);
//                        Bundle bundle2SendPost = new Bundle();
//                        bundle2SendPost.putInt(SendPostActivity.JUMP_BY, SendPostActivity.JUMP_BY_PARENT);
//                        intent2SendPost.putExtras(bundle2SendPost);
//                        mContext.startActivity(intent2SendPost);
//                        sendPostPopupWindow.dismiss();
//                    }
//                });

                /**
                 * 发帖界面的跳转
                 */
                Intent intent2SendPost = new Intent(mContext, SendPostActivity.class);
                Bundle bundle2SendPost = new Bundle();
                bundle2SendPost.putInt(SendPostActivity.JUMP_BY, SendPostActivity.JUMP_BY_PARENT);
                intent2SendPost.putExtras(bundle2SendPost);
                mContext.startActivity(intent2SendPost);

                break;
            case R.id.ll_college: //塾模块
                if (mIndex == IMainTab.COLLEGE) {
                    return;
                }
                mIndex = IMainTab.COLLEGE;
                break;
            case R.id.ll_my_space://吾模块
                if (mIndex == IMainTab.MY_SPACE) {
                    return;
                }
                mIndex = IMainTab.MY_SPACE;
                break;
            default:
                break;
        }
        setTabColor(mIndex);
        if (null != mIMainTabClickListener) {
            mIMainTabClickListener.onClick(mIndex);
        }
    }


    public void setIMainTabClickListener(IMainTabClickListener mainTabClickListener) {
        this.mIMainTabClickListener = mainTabClickListener;
    }
}
