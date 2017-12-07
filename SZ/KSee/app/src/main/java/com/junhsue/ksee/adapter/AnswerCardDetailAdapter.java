package com.junhsue.ksee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.junhsue.ksee.R;
import com.junhsue.ksee.entity.AnswerCardDetailsEntity;

/**
 * Created by hunter_J on 17/4/19.
 */

public class AnswerCardDetailAdapter<T extends AnswerCardDetailsEntity> extends MyBaseAdapter<AnswerCardDetailsEntity>{

  private Context mContext;
  private LayoutInflater mInflater;

  public AnswerCardDetailAdapter(Context context) {
    mContext = context;
    mInflater = LayoutInflater.from(context);
  }


  @Override
  protected View getWrappeView(final int position, View convertView, ViewGroup parent) {

    ViewHolder mHolder = null;
    if (convertView == null){
      convertView = mInflater.inflate(R.layout.item_answer_card_list,null);
      mHolder = new ViewHolder(convertView);
      convertView.setTag(mHolder);
    }else {
      mHolder = (ViewHolder) convertView.getTag();
    }

    AnswerCardDetailsEntity myAnswerCard = mList.get(position);
    if (myAnswerCard != null){

      String title = "",content = "";

      content = myAnswerCard.list.content;
      switch (myAnswerCard.type_id){
        case 5:  //被邀请回答
          title = myAnswerCard.list.nickname+" "+"邀请你回答问题：";
          break;
        case 3:  //提问的问题被回答
          if (myAnswerCard.list.answer_user_list.size() > 3){
            title = myAnswerCard.list.answer_user_list.get(0) + "、" +myAnswerCard.list.answer_user_list.get(1) + "、" +myAnswerCard.list.answer_user_list.get(2) + " ...等";
            title += myAnswerCard.list.answer_user_list.size() + " 人回答了你发布的问题：";
          }else {
            title = "";
            for (int i = 0; i < myAnswerCard.list.answer_user_list.size(); i++){
              if (i == 2 || i == myAnswerCard.list.answer_user_list.size()-1){
                title += myAnswerCard.list.answer_user_list.get(i);
                title += " 回答了你发布的问题：";
                break;
              }
              title += myAnswerCard.list.answer_user_list.get(i) + "、";
            }
          }
          break;
        case 6:  //关注的问题有新的回答
          if (myAnswerCard.list.answer_user_list.size() > 3){
            title = myAnswerCard.list.answer_user_list.get(0) + "、" +myAnswerCard.list.answer_user_list.get(1) + "、" +myAnswerCard.list.answer_user_list.get(2) + " ...等";
            title += myAnswerCard.list.answer_user_list.size() + " 人回答了你关注的问题：";
          }else {
            title = "";
            for (int i = 0; i < myAnswerCard.list.answer_user_list.size(); i++){
              if (i == 2 || i == myAnswerCard.list.answer_user_list.size()-1){
                title += myAnswerCard.list.answer_user_list.get(i);
                title += " 回答了你关注的问题：";
                break;
              }
              title += myAnswerCard.list.answer_user_list.get(i) + "、";
            }
          }
          break;
        case 4:  //我的答案被点赞
          content = myAnswerCard.list.data.get(0).content;
          if (myAnswerCard.list.data.get(0).answer_user_nickname.size() > 3){
            title = myAnswerCard.list.data.get(0).answer_user_nickname.get(0) + "、" +myAnswerCard.list.data.get(0).answer_user_nickname.get(1) + "、" +myAnswerCard.list.data.get(0).answer_user_nickname.get(2) + " ...等";
            title += myAnswerCard.list.data.get(0).answer_user_nickname.size() + " 人点赞了你的回答：";
          }else {
            title = "";
            for (int i = 0; i < myAnswerCard.list.data.get(0).answer_user_nickname.size(); i++){
              if (i == 2 || i == myAnswerCard.list.data.get(0).answer_user_nickname.size()-1){
                title += myAnswerCard.list.data.get(0).answer_user_nickname.get(i);
                title += " 点赞了你的回答：";
                break;
              }
              title += myAnswerCard.list.data.get(0).answer_user_nickname.get(i) + "、";
            }
          }
          break;
      }

      mHolder.tvTitle.setText(title+"");
      mHolder.tvContent.setText(content);
    }
    return convertView;
  }

  private class ViewHolder{
    public TextView tvTitle; //消息标题
    public TextView tvContent; //消息内容

    public ViewHolder(View view) {
      tvTitle = (TextView) view.findViewById(R.id.tv_card_title);
      tvContent = (TextView) view.findViewById(R.id.tv_card_content);
    }
  }

}
