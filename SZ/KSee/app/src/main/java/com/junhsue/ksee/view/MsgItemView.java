package com.junhsue.ksee.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.junhsue.ksee.R;

/**
 * 消息item自定义view
 * Created by longer on 17/11/14.
 */

public class MsgItemView extends FrameLayout {

    private Context mContext;
    //首页图片
    private ImageView mImgTag;
    //首页标题
    private TextView mTxtTitle;
    //消息数
    private TextView mTxtMsgCount;

    public MsgItemView(Context context) {
        super(context);
        mContext = context;
        initilizeView(context,null);
    }

    public MsgItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initilizeView(context,attrs);
    }


    public MsgItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initilizeView(context,attrs);
    }


    private void initilizeView(Context context,AttributeSet attrs){
       View view= LayoutInflater.from(context).inflate(R.layout.component_msg,this);
       mImgTag=(ImageView)view.findViewById(R.id.img);
       mTxtTitle=(TextView) view.findViewById(R.id.txt_title);
       mTxtMsgCount=(TextView)view.findViewById(R.id.msg_count);

        TypedArray typedArray = this.mContext.obtainStyledAttributes(attrs, R.styleable.attrs_msg);

        if(typedArray.hasValue(R.styleable.attrs_msg_img)){
            int resoureId=typedArray.getResourceId(R.styleable.attrs_msg_img,0);
            mImgTag.setImageResource(resoureId);
        }
        //内容
        if(typedArray.hasValue(R.styleable.attrs_msg_text)){
            String title=typedArray.getString(R.styleable.attrs_msg_text);
            mTxtTitle.setText(title);
        }
        //
        typedArray.recycle();

    }


    /**
     * 设置消息数
     * @param count
     */
    public void setMsgCount(int count){
        if(count==0){
            mTxtMsgCount.setVisibility(GONE);
            return;
        }
        mTxtMsgCount.setVisibility(VISIBLE);
        if(count<99){
           mTxtMsgCount.setText(String.valueOf(count));
        }else if(count>99){
            mTxtMsgCount.setText("99+");
        }

    }
}


