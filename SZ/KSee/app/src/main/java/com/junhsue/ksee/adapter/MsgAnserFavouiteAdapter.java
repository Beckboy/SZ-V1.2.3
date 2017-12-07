package com.junhsue.ksee.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.junhsue.ksee.R;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.entity.MsgAnswerFavouriteEntity;

/**
 *
 * 回答点赞列表
 * Created by longer on 17/6/8.
 */

public class MsgAnserFavouiteAdapter<T extends MsgAnswerFavouriteEntity> extends MyBaseAdapter<T> {


    private Context mContext;

    private LayoutInflater mLayoutInflater;

    private ViewHolder mViewHolder;

    public MsgAnserFavouiteAdapter(Context context){
        this.mContext=context;
        mLayoutInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    protected View getWrappeView(int position, View convertView, ViewGroup parent) {

        if(null==convertView){
            mViewHolder=new ViewHolder();
            convertView=mLayoutInflater.inflate(R.layout.item_msg_answer_favouite,null);
            mViewHolder.mTxtAnswerTitle=(TextView)convertView.findViewById(R.id.txt_answer_title);
            mViewHolder.mTxtQuestionTitle=(TextView)convertView.findViewById(R.id._question_title);
            convertView.setTag(mViewHolder);
        }else{
            mViewHolder=(ViewHolder)convertView.getTag();
        }

        MsgAnswerFavouriteEntity  msgAnswerFavouriteEntity= getList().get(position);

        //点赞用户
        String  userStr="";
        int count=msgAnswerFavouriteEntity.answer_user_nickname.size()>2?
                2:msgAnswerFavouriteEntity.answer_user_nickname.size();

        for(int i=0;i<count;i++){
            userStr+=msgAnswerFavouriteEntity.answer_user_nickname.get(i);
            if(count>1 && i!=count-1){
                userStr+="、";
            }
        }

        if(msgAnswerFavouriteEntity.answer_user_nickname.size()>0 &&
                msgAnswerFavouriteEntity.answer_user_nickname.size()<=2){
            userStr+=mContext.getString(R.string.msg_quesion_favorite_part_content);

        }else if(msgAnswerFavouriteEntity.answer_user_nickname.size()>2){
            userStr+=String.format(mContext.getString(R.string.msg_question_favorite__part_content_single),
                    String.valueOf(msgAnswerFavouriteEntity.answer_user_nickname.size()));
        }

        StringBuilder title=new StringBuilder();
        title.append(userStr);
        title.append("\"");

        if(TextUtils.isEmpty(msgAnswerFavouriteEntity.answer_content)){
            title.append("语音");
        }else{
            title.append(msgAnswerFavouriteEntity.answer_content);
        }
        title.append("\"");
        SpannableString spannableString=new SpannableString(title);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#8392A0")),
                0,userStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#3C4350"))
                ,userStr.length(),title.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        mViewHolder.mTxtAnswerTitle.setText(spannableString);

        mViewHolder.mTxtQuestionTitle.setText("问题: "+msgAnswerFavouriteEntity.content);

        return convertView;
    }



    class ViewHolder {

        //回答标题
        private TextView mTxtAnswerTitle;
        //
        private TextView mTxtQuestionTitle;

    }
}
