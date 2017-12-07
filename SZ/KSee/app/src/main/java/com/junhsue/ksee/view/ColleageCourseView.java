package com.junhsue.ksee.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.junhsue.ksee.R;

/**
 * Created by longer on 17/3/24.
 */

public  class ColleageCourseView extends FrameLayout implements  View.OnClickListener{



    //主题课
    private static  final int TAB_SUJECT=0;
    //系统课
    private static  final int TAB_SYSTEM=1;

    private Context mContext;

    private TextView mTxtSubjectCourse;
    private TextView mTxtSystemCourse;

    private LinearLayout mLine;
    //记录当前选择选择的tab
    private int mIndex=0;
    private IColleageTabClickListener mIColleageTabClickListener;



    public interface  IColleageTabClickListener{
        void  onClick(int index);
    }


    public ColleageCourseView(Context context) {
        super(context);
        this.mContext=context;
        initilizeView(context);
    }



    public ColleageCourseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext=context;
        initilizeView(mContext);
    }


    public ColleageCourseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext=context;
        initilizeView(mContext);
    }



    public void initilizeView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view =inflater.inflate(R.layout.component_colleage_course_tab,this);
        mTxtSubjectCourse=(TextView)view.findViewById(R.id.txt_course_subject);
        mTxtSystemCourse=(TextView)view.findViewById(R.id.txt_course_system);
        mLine=(LinearLayout)view.findViewById(R.id.ll_line);

        //
        mTxtSubjectCourse.setOnClickListener(this);
        mTxtSystemCourse.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if(id==R.id.txt_course_subject){
            if(mIndex==TAB_SUJECT)
                return;
            if(null!=mIColleageTabClickListener)
                mIColleageTabClickListener.onClick(TAB_SUJECT);
            setTabColor(TAB_SUJECT);
            translateAnimation(TAB_SUJECT);
        }


        if(id==R.id.txt_course_system){
            if(mIndex==TAB_SYSTEM)
                return;

            if(null!=mIColleageTabClickListener)
                mIColleageTabClickListener.onClick(TAB_SYSTEM);
            setTabColor(TAB_SYSTEM);
            translateAnimation(TAB_SYSTEM);

        }
    }

    public void setTabStatus(int index){
        setTabColor(index);
        translateAnimation(index);
    }

    private void translateAnimation(int index) {
        Animation animation = null;
        if (TAB_SUJECT == index) {
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF,0 ,

                    Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        } else {
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1f,

                    Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);

        }
        animation.setDuration(300);
        animation.setFillAfter(true);
        animation.setFillEnabled(true);
        mLine.startAnimation(animation);
        mIndex = index;

    }

    private void setTabColor(int index){
        switch (index){
            case  TAB_SUJECT:
                mTxtSubjectCourse.setTextColor(getResources().getColor(R.color.c_black_3c4350));
                mTxtSystemCourse.setTextColor(getResources().getColor(R.color.c_gray_999));
                break;
            case  TAB_SYSTEM:
                mTxtSystemCourse.setTextColor(getResources().getColor(R.color.c_black_3c4350));
                mTxtSubjectCourse.setTextColor(getResources().getColor(R.color.c_gray_999));
                break;
        }
    }
    public void setIColleageTabClickListener(IColleageTabClickListener iColleageTabClickListener) {
        this.mIColleageTabClickListener = iColleageTabClickListener;
    }
}
