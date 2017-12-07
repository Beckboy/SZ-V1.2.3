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
import android.widget.TextView;

import com.junhsue.ksee.R;
import com.junhsue.ksee.utils.StatisticsUtil;

/**
 * Created by longer on 17/3/24.
 */

public  class PostListTabView extends FrameLayout implements  View.OnClickListener{

    //全部
    private static  final int TAB_INVITE=0;
    //精华
    private static  final int TAB_QUESTION=1;

    private Context mContext;

    private TextView mTxtInvite;
    private TextView mTxtQuestion;

    private LinearLayout mLine;
    //记录当前选择选择的tab
    private int mIndex=0;
    private IAnswerTabClickListener mIAnswerTabClickListener;


    public interface  IAnswerTabClickListener{
        void  onClick(int index);
    }


    public PostListTabView(Context context) {
        super(context);
        this.mContext=context;
        initilizeView(context);
    }



    public PostListTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext=context;
        initilizeView(mContext);
    }


    public PostListTabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext=context;
        initilizeView(mContext);
    }


    public void initilizeView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view =inflater.inflate(R.layout.component_posterlist_tab,this);
        mTxtInvite=(TextView)view.findViewById(R.id.txt_poster_all);
        mTxtQuestion=(TextView)view.findViewById(R.id.txt_poster_best);
        mLine=(LinearLayout)view.findViewById(R.id.ll_line);

        //
        mTxtInvite.setOnClickListener(this);
        mTxtQuestion.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if(id==R.id.txt_poster_all){
            if(mIndex==TAB_INVITE)
                return;
            if(null!=mIAnswerTabClickListener)
                mIAnswerTabClickListener.onClick(TAB_INVITE);
        }

        if(id==R.id.txt_poster_best){
            if(mIndex==TAB_QUESTION)
                return;
            if(null!=mIAnswerTabClickListener)
                mIAnswerTabClickListener.onClick(TAB_QUESTION);
        }

    }

    public void setTabStatus(int index){
        setTabColor(index);
        translateAnimation(index);
    }

    private void translateAnimation(int index) {
        Animation animation = null;
        if (TAB_INVITE == index) {
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0,
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
        Log.i("index",mIndex+"");

    }

    private void setTabColor(int index){
        switch (index){
            case  TAB_INVITE:
                mTxtInvite.setTextColor(getResources().getColor(R.color.c_black_3c4350));
                mTxtQuestion.setTextColor(getResources().getColor(R.color.c_gray_999));
                break;
            case  TAB_QUESTION:
                mTxtQuestion.setTextColor(getResources().getColor(R.color.c_black_3c4350));
                mTxtInvite.setTextColor(getResources().getColor(R.color.c_gray_999));
                break;
        }
    }
    public void setIPostTabClickListener(IAnswerTabClickListener iAnswerTabClickListener) {
        this.mIAnswerTabClickListener = iAnswerTabClickListener;
    }
}
