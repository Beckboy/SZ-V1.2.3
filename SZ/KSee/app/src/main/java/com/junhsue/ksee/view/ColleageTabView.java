package com.junhsue.ksee.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.junhsue.ksee.R;
import com.junhsue.ksee.common.IColleageMenu;
import com.junhsue.ksee.common.IHomeMenu;

/**
 * 塾模块自定义view
 * Created by longer on 17/3/22.
 */

public class ColleageTabView extends FrameLayout implements View.OnClickListener {


    private Context mContext;

    //记录点击tab的位置
    private static int mIndex;

    private TextView mTxtcourse, mTxtLive, mTxtActivities, mTxtClassroom;

    public IColleageTabClickListener mIColleageTabClickListener;

    /**
     * 监听点击事件
     */
    public interface IColleageTabClickListener {

        void onClick(int position);
    }

    public ColleageTabView(Context context) {
        super(context);
        this.mContext = context;
        initilizeView(context);
    }

    public ColleageTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initilizeView(context);
    }


    public ColleageTabView(Context context, AttributeSet attrs, Context mContext) {
        super(context, attrs);
        this.mContext = mContext;
        initilizeView(context);
    }


    private void initilizeView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.component_colleage_tab, this);

        mTxtLive = (TextView) view.findViewById(R.id.txt_live);
        mTxtcourse = (TextView) view.findViewById(R.id.txt_course);
        mTxtActivities = (TextView) view.findViewById(R.id.txt_activities);
        mTxtClassroom = (TextView) view.findViewById(R.id.txt_classroom);
        //
        mTxtLive.setOnClickListener(this);
        mTxtcourse.setOnClickListener(this);
        //mTxtActivities.setOnClickListener(this);
        mTxtClassroom.setOnClickListener(this);
        //
        initilizeTxtColor();
        mTxtLive.setTextColor(mContext.getResources().getColor(R.color.c_yellow_cdac8d));

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_live:
                setTabIndexColor(IColleageMenu.LIVE);

                break;
            case R.id.txt_course:
                setTabIndexColor(IColleageMenu.COURSE);
                break;
//            case R.id.txt_activities:
//                mTxtActivities.setTextColor(mContext.getResources().getColor(R.color.c_yellow_cdac8d));
//                mIndex = 2;
//                break;
            case R.id.txt_classroom:
                setTabIndexColor(IColleageMenu.CLASSROOM);
                break;
            default:
                break;
        }

        if (null != mIColleageTabClickListener) {
            mIColleageTabClickListener.onClick(mIndex);
        }
    }


    /**
     * 设置tab颜色
     */
    public void setTabIndexColor(int index){
        initilizeTxtColor();
        switch (index){
            case  IColleageMenu.COURSE:
                mTxtcourse.setTextColor(mContext.getResources().getColor(R.color.c_yellow_cdac8d));
                mIndex = IColleageMenu.COURSE;
                break;
            case IColleageMenu.LIVE:
                mTxtLive.setTextColor(mContext.getResources().getColor(R.color.c_yellow_cdac8d));
                mIndex =IColleageMenu.LIVE;
                break;
            case IColleageMenu.CLASSROOM:
                mTxtClassroom.setTextColor(mContext.getResources().getColor(R.color.c_yellow_cdac8d));
                mIndex = IColleageMenu.CLASSROOM;

                break;
        }
    }
    /**
     * 初试化文字颜色
     *
     * @param
     */
    public void initilizeTxtColor() {
        int colorResourceId = mContext.getResources().getColor(R.color.c_gray_999);
        mTxtcourse.setTextColor(colorResourceId);
        mTxtLive.setTextColor(colorResourceId);
        mTxtActivities.setTextColor(colorResourceId);
        mTxtClassroom.setTextColor(colorResourceId);
    }

    public void setIColleageTabClickListener(IColleageTabClickListener iColleageTabClickListener) {
        this.mIColleageTabClickListener = iColleageTabClickListener;
    }

}
