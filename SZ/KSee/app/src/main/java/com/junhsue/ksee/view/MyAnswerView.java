package com.junhsue.ksee.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.junhsue.ksee.R;
import com.junhsue.ksee.utils.StatisticsUtil;

/**
 * Created by longer on 17/3/24.
 */

public  class MyAnswerView extends FrameLayout implements  View.OnClickListener{

    //被邀请
    private static  final int TAB_INVITE=0;
    //我提问的
    private static  final int TAB_QUESTION=1;
    //收藏
    private static  final int TAB_COLLEAGE=2;

    private Context mContext;

    private TextView mTxtInvite;
    private TextView mTxtQuestion;
    private TextView mTxtColleage;

    private LinearLayout mLine;
    //记录当前选择选择的tab
    private int mIndex=0;
    private IAnswerTabClickListener mIAnswerTabClickListener;


    public interface  IAnswerTabClickListener{
        void  onClick(int index);
    }


    public MyAnswerView(Context context) {
        super(context);
        this.mContext=context;
        initilizeView(context);
    }



    public MyAnswerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext=context;
        initilizeView(mContext);
    }


    public MyAnswerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext=context;
        initilizeView(mContext);
    }



    public void initilizeView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view =inflater.inflate(R.layout.component_my_answer_tab,this);
        mTxtInvite=(TextView)view.findViewById(R.id.txt_answer_invite);
        mTxtQuestion=(TextView)view.findViewById(R.id.txt_answer_question);
        mTxtColleage=(TextView)view.findViewById(R.id.txt_answer_colleage);
        mLine=(LinearLayout)view.findViewById(R.id.ll_line);

        //
        mTxtInvite.setOnClickListener(this);
        mTxtQuestion.setOnClickListener(this);
        mTxtColleage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if(id==R.id.txt_answer_invite){
            if(mIndex==TAB_INVITE)
                return;
            if(null!=mIAnswerTabClickListener)
                mIAnswerTabClickListener.onClick(TAB_INVITE);
        }


        if(id==R.id.txt_answer_question){
            if(mIndex==TAB_QUESTION)
                return;
            if(null!=mIAnswerTabClickListener)
                mIAnswerTabClickListener.onClick(TAB_QUESTION);
        }


        if(id==R.id.txt_answer_colleage){
            if(mIndex==TAB_COLLEAGE)
                return;
            if(null!=mIAnswerTabClickListener)
                mIAnswerTabClickListener.onClick(TAB_COLLEAGE);
        }
    }

    public void setTabStatus(int index){
        setTabColor(index);
        translateAnimation(index);
    }

    private void translateAnimation(int index) {
        Animation animation = null;
        if (TAB_INVITE == index) {
            if (mIndex == TAB_QUESTION) {
                animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0,
                    Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
            }else {
                animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 2f, Animation.RELATIVE_TO_SELF, 0,
                    Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
            }
        } else if (TAB_QUESTION == index) {
            if (mIndex == TAB_INVITE) {
                animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1f,
                    Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
            }else {
                animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 2f, Animation.RELATIVE_TO_SELF, 1f,
                    Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
            }
        } else {
            if (mIndex == TAB_INVITE) {
                animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 2f,
                    Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
            }else {
                animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 2f,
                    Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
            }
        }
        animation.setDuration(300);
        animation.setFillAfter(true);
        animation.setFillEnabled(true);
        mLine.startAnimation(animation);
        mIndex = index;
        Log.i("index",mIndex+"");

    }

    private void setTabColor(int index){
        switch (index){
            case  TAB_INVITE:
                mTxtInvite.setTextColor(getResources().getColor(R.color.c_black_3c4350));
                mTxtQuestion.setTextColor(getResources().getColor(R.color.c_gray_999));
                mTxtColleage.setTextColor(getResources().getColor(R.color.c_gray_999));
                StatisticsUtil.getInstance(mContext).onCountPage("1.5.1");
                break;
            case  TAB_QUESTION:
                mTxtQuestion.setTextColor(getResources().getColor(R.color.c_black_3c4350));
                mTxtInvite.setTextColor(getResources().getColor(R.color.c_gray_999));
                mTxtColleage.setTextColor(getResources().getColor(R.color.c_gray_999));
                StatisticsUtil.getInstance(mContext).onCountPage("1.5.2");
                break;
            case  TAB_COLLEAGE:
                mTxtColleage.setTextColor(getResources().getColor(R.color.c_black_3c4350));
                mTxtInvite.setTextColor(getResources().getColor(R.color.c_gray_999));
                mTxtQuestion.setTextColor(getResources().getColor(R.color.c_gray_999));
                StatisticsUtil.getInstance(mContext).onCountPage("1.5.3");
                break;
        }
    }
    public void setIAnswerTabClickListener(IAnswerTabClickListener iAnswerTabClickListener) {
        this.mIAnswerTabClickListener = iAnswerTabClickListener;
    }
}
