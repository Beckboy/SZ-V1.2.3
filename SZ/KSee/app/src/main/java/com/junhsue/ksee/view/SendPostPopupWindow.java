package com.junhsue.ksee.view;


import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.junhsue.ksee.R;
import com.junhsue.ksee.utils.DensityUtil;
import com.junhsue.ksee.utils.ScreenWindowUtil;


/**
 * 消息卡片
 * Created by longer on 17/6/19.
 */


public class SendPostPopupWindow extends PopupWindow implements View.OnClickListener {


    //上下文
    private Context mContext;
    //
    private IOnClickListener mIOnClickListener;

    public SendPostPopupWindow(Context context) {
        super(context);
        this.mContext = context;
        initInitilize();
    }


    private void initInitilize() {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //
        View view = layoutInflater.inflate(R.layout.component_home_post, null);
        setContentView(view);
        int width = ScreenWindowUtil.getScreenWidth(mContext);
        int height = (int) (width * 0.6);
        setWidth(width);
        setHeight(height);
        setAttribute();
        //
        view.findViewById(R.id.ll_content).setOnClickListener(this);
    }


    private void setAttribute() {
        setAnimationStyle(R.style.popwin_anim_style);
        // 设置SelectPicPopupWindow弹出窗体可点击
        setFocusable(true);
        setOutsideTouchable(true);

        update();
        //
        ColorDrawable dw = new ColorDrawable(mContext.getResources().getColor(R.color.transent));
        //点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        setBackgroundDrawable(dw);
        //设置弹出动画

    }

    /**
     * 显示视图
     * @param view
     */
    public void showView(View view) {
        showAtLocation(view, Gravity.BOTTOM, 0 , 0);
    }


    public void setIOnClickListener(IOnClickListener mIOnClickListener) {
        this.mIOnClickListener = mIOnClickListener;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case  R.id.ll_content:

                if(null!=mIOnClickListener){
                    mIOnClickListener.onClick();
                }
                break;

        }
    }


    public interface IOnClickListener {

        void onClick();
    }
}
