package com.junhsue.ksee.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v4.widget.TextViewCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.request.target.ImageViewTarget;
import com.junhsue.ksee.R;
import com.junhsue.ksee.common.IMsgIgnoreType;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.dto.MsgAnswerFavouriteDTO;
import com.junhsue.ksee.dto.MsgQuestionReplyDTO;
import com.junhsue.ksee.entity.ClassRoomListEntity;
import com.junhsue.ksee.entity.MsgActivityEntity;
import com.junhsue.ksee.entity.MsgActivityNewEntity;
import com.junhsue.ksee.entity.MsgAnswerFavouriteEntity;
import com.junhsue.ksee.entity.MsgClassRoomEntity;
import com.junhsue.ksee.entity.MsgCourseEntity;
import com.junhsue.ksee.entity.MsgCourseJoinEntity;
import com.junhsue.ksee.entity.MsgCourseListEntity;
import com.junhsue.ksee.entity.MsgEntity;
import com.junhsue.ksee.entity.MsgLiveEntity;
import com.junhsue.ksee.entity.MsgLiveNewEntity;
import com.junhsue.ksee.entity.MsgQuestionFavourteReplyDTO;
import com.junhsue.ksee.entity.MsgQuestionInviteEntity;
import com.junhsue.ksee.entity.MsgSystemUpdate;
import com.junhsue.ksee.file.ImageLoaderOptions;
import com.junhsue.ksee.utils.DataGsonUitls;
import com.junhsue.ksee.utils.DateUtils;
import com.junhsue.ksee.utils.MsgCardLiveTimer;
import com.junhsue.ksee.utils.NumberFormatUtils;
import com.junhsue.ksee.utils.ScreenWindowUtil;
import com.junhsue.ksee.view.MsgPopupWindow;
import com.nostra13.universalimageloader.core.ImageLoader;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.iwgang.countdownview.CountdownView;
import cn.iwgang.countdownview.DynamicConfig;


/**
 * 卡片适配器
 * <p>
 * Created by longer on 17/5/23
 * 填写省市
 */

public class MsgAdapter<T extends MsgEntity> extends MyBaseAdapter<T> {


    private Context mContext;

    private LayoutInflater mLayoutInflater;

    private MsgEntity.IMsgClickListener mIMsgClickListener;


    public MsgAdapter(Context context) {
        this.mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getItemViewType(int position) {
        //
        int pos = (getList().get(position).type_id) - 1;
        return pos;
    }

    @Override
    public int getViewTypeCount() {
        return MsgEntity.MsgType.values().length;
    }

    @Override
    protected View getWrappeView(int position, View convertView, ViewGroup parent) {

        // +1 的原因是因为后台卡片类型从下标 1开始
        int type = getItemViewType(position) + 1;

        final MsgEntity msgEntity = getList().get(position);
        //卡片类型
        //final int cardType=msgEntity.type_id;
        String msg = "" + DataGsonUitls.toJson(msgEntity.list);
        //
        switch (type) {

            case MsgEntity.IMsgType.TASK:

                ViewHolderSystemTask viewHolderSystemTask=null;
                if(null==convertView){
                    convertView=mLayoutInflater.inflate(R.layout.item_msg_task,null);
                    viewHolderSystemTask=new ViewHolderSystemTask();
                    viewHolderSystemTask.mLLMore=(LinearLayout)convertView.findViewById(R.id.ll_more);
                    convertView.setTag(viewHolderSystemTask);
                }else{
                    viewHolderSystemTask=(ViewHolderSystemTask)convertView.getTag();
                }

                final  LinearLayout mLLMore=viewHolderSystemTask.mLLMore;

                viewHolderSystemTask.mLLMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showMsgMenu(MsgEntity.IMsgType.TASK,msgEntity.id, IMsgIgnoreType.TYPE_SYSTEM,mLLMore);

                    }
                });
                break;
            /**
             * 回答被点赞消息卡片*/
            case MsgEntity.IMsgType.ANSWER_FAVOURITE: //回答被点赞

                ViewHolderAnswerFavourite viewHolderAnswerFavourite = null;
                if (null == convertView) {
                    viewHolderAnswerFavourite = new ViewHolderAnswerFavourite();
                    convertView = mLayoutInflater.inflate(R.layout.item_msg_quesiton_reply, null);
                    viewHolderAnswerFavourite.mBtn = (Button) convertView.findViewById(R.id.btn);
                    viewHolderAnswerFavourite.mLLmore = (LinearLayout) convertView.findViewById(R.id.ll_more);
                    viewHolderAnswerFavourite.mTxtTitle = (TextView) convertView.findViewById(R.id.txt_title);
                    viewHolderAnswerFavourite.mTxtContent = (TextView) convertView.findViewById(R.id.txt_desc);
                    convertView.setTag(viewHolderAnswerFavourite);
                } else {
                    viewHolderAnswerFavourite = (ViewHolderAnswerFavourite) convertView.getTag();
                }

                //viewHolderAnswerFavourite.mTxtTitle.setText(mContext.getString(R.string.msg_question_favorite));

                final LinearLayout llMore = viewHolderAnswerFavourite.mLLmore;
                final MsgAnswerFavouriteDTO msgAnswerFavouriteDTO = DataGsonUitls.format(msg,
                        MsgAnswerFavouriteDTO.class);

                if (msgAnswerFavouriteDTO.data.size() > 0) {

                    MsgAnswerFavouriteEntity msgAnswerFavouriteEntity = msgAnswerFavouriteDTO.data.get(0);
                    //点赞用户
                    String userStr = "";
                    int count = msgAnswerFavouriteEntity.answer_user_nickname.size() > 2 ?
                            2 : msgAnswerFavouriteEntity.answer_user_nickname.size();

                    for (int i = 0; i < count; i++) {
                        userStr += msgAnswerFavouriteEntity.answer_user_nickname.get(i);
                        if (count > 1 && i != count - 1) {
                            userStr += "、";
                        }
                    }

                    if (msgAnswerFavouriteEntity.answer_user_nickname.size() > 0 &&
                            msgAnswerFavouriteEntity.answer_user_nickname.size() <= 2) {
                        userStr += mContext.getString(R.string.msg_quesion_favorite_part_content);

                    } else if (msgAnswerFavouriteEntity.answer_user_nickname.size() > 2) {
                        userStr += String.format(mContext.getString(R.string.msg_question_favorite__part_content_single),
                                String.valueOf(msgAnswerFavouriteEntity.answer_user_nickname.size()));
                    }

                    StringBuilder title = new StringBuilder();
                    title.append(userStr);
                    title.append("\"");
                    title.append(msgAnswerFavouriteEntity.content);
                    title.append("\"");
                    SpannableString spannableString = new SpannableString(title);
                    spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#8392A0")),
                            0, userStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#cdac8c"))
                            , userStr.length(), title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    viewHolderAnswerFavourite.mTxtContent.setText(spannableString);
                    viewHolderAnswerFavourite.mBtn.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            if (null != mIMsgClickListener) {
                                mIMsgClickListener.jumpFavoritePage(msgAnswerFavouriteDTO);
                            }
                        }
                    });

                    viewHolderAnswerFavourite.mLLmore.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            showMsgMenu(MsgEntity.IMsgType.ANSWER_FAVOURITE,msgEntity.id, IMsgIgnoreType.TYPE_NORMAL,llMore);
                        }
                    });

                }

                break;

            //提问的问题被回答
            /**
             *
             *一个问题对应一张消息卡片
             *
             *如果没有回答
             * */
            case MsgEntity.IMsgType.QUESITON_REPLY:

                ViewHolderQuestionReply viewHolderQuestionReply = null;

                if (null == convertView) {

                    convertView = mLayoutInflater.inflate(R.layout.item_msg_quesiton_reply, null);
                    viewHolderQuestionReply = new ViewHolderQuestionReply();
                    viewHolderQuestionReply.mLLmore = (LinearLayout) convertView.findViewById(R.id.ll_more);
                    viewHolderQuestionReply.mTxtTitle = (TextView) convertView.findViewById(R.id.txt_title);
                    viewHolderQuestionReply.mTxtDesc = (TextView) convertView.findViewById(R.id.txt_desc);
                    viewHolderQuestionReply.mBtn = (Button) convertView.findViewById(R.id.btn);
                    convertView.setTag(viewHolderQuestionReply);
                } else {
                    viewHolderQuestionReply = (ViewHolderQuestionReply) convertView.getTag();
                }

                final MsgQuestionReplyDTO msgQuestionReplyDTO = DataGsonUitls.format(msg, MsgQuestionReplyDTO.class);
                //获取问题回答者列表
                List<String> msgQuestionReplyEntities = msgQuestionReplyDTO.answer_user_list;
                String questionUser = "";
                //获取所有回答的用户,获取问题内容就取第一个元素
                int count = msgQuestionReplyEntities.size() > 2 ? 2 : msgQuestionReplyEntities.size();
                for (int i = 0; i < count; i++) {
                    String msgQuestionReplyEntity = msgQuestionReplyEntities.get(i);
                    questionUser += msgQuestionReplyEntity;
                    if (count > 1 && i != count - 1) {
                        questionUser += "、";
                    }
                }
                if (msgQuestionReplyEntities.size() > 0 & msgQuestionReplyEntities.size() <= 2) {

                    questionUser += mContext.getString(R.string.msg_question_release_people_single);
                } else if (msgQuestionReplyEntities.size() > 2) {
                    questionUser += String.format(mContext.getString(R.string.msg_question_release),
                            String.valueOf(msgQuestionReplyEntities.size()));
                }
                StringBuilder titleRelease = new StringBuilder();
                titleRelease.append(questionUser);
                titleRelease.append("\"");
                titleRelease.append(msgQuestionReplyDTO.content);
                titleRelease.append("\"");
                SpannableString spannableStringRelease = new SpannableString(titleRelease);
                spannableStringRelease.setSpan(new ForegroundColorSpan(Color.parseColor("#8392A0")),
                        0, questionUser.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                spannableStringRelease.setSpan(new ForegroundColorSpan(Color.parseColor("#cdac8c"))
                        , questionUser.length(), titleRelease.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                viewHolderQuestionReply.mTxtDesc.setText(spannableStringRelease);

                viewHolderQuestionReply.mBtn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (null != mIMsgClickListener)
                            mIMsgClickListener.onClick(msgEntity.type_id, msgQuestionReplyDTO.question_id);
                    }
                });

                final LinearLayout llMoreQuestionReply = viewHolderQuestionReply.mLLmore;


                viewHolderQuestionReply.mLLmore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        showMsgMenu(MsgEntity.IMsgType.QUESITON_REPLY,msgEntity.id,IMsgIgnoreType.TYPE_NORMAL,llMoreQuestionReply);
                    }
                });

                break;

            /**
             * 邀请回答消息卡片
             */
            case MsgEntity.IMsgType.QUESTION_INVITE: //邀请回答

                ViewHolderQuestionReply viewHolderQuestionInvite = null;

                if (null == convertView) {
                    viewHolderQuestionInvite = new ViewHolderQuestionReply();
                    convertView = mLayoutInflater.inflate(R.layout.item_msg_quesiton_reply, null);
                    viewHolderQuestionInvite.mLLmore = (LinearLayout) convertView.findViewById(R.id.ll_more);
                    viewHolderQuestionInvite.mTxtTitle = (TextView) convertView.findViewById(R.id.txt_title);
                    viewHolderQuestionInvite.mTxtDesc = (TextView) convertView.findViewById(R.id.txt_desc);
                    viewHolderQuestionInvite.mBtn = (Button) convertView.findViewById(R.id.btn);
                    convertView.setTag(viewHolderQuestionInvite);
                } else {
                    viewHolderQuestionInvite = (ViewHolderQuestionReply) convertView.getTag();
                }

                final MsgQuestionInviteEntity msgQuestionInviteEntity = DataGsonUitls.format(msg, MsgQuestionInviteEntity.class);

                if (null != msgQuestionInviteEntity) {

//                    viewHolderQuestionInvite.mTxtTitle.setText(String.format(
//                            mContext.getString(R.string.msg_question_invite_title), msgQuestionInviteEntity.nickname));
                    String userStr = msgQuestionInviteEntity.nickname + mContext.getString(R.string.msg_question_reply);
                    StringBuilder title = new StringBuilder();
                    title.append(userStr);
                    title.append("\"");
                    title.append(msgQuestionInviteEntity.content);
                    title.append("\"");
                    SpannableString spannableString = new SpannableString(title);
                    spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#8392A0")),
                            0, userStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#cdac8c"))
                            , userStr.length(), title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    viewHolderQuestionInvite.mTxtDesc.setText(spannableString);

                    viewHolderQuestionInvite.mBtn.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (null != mIMsgClickListener)
                                mIMsgClickListener.onClick(msgEntity.type_id, msgQuestionInviteEntity.question_id);
                        }
                    });
                }

                final LinearLayout llMoreQuestionInvite = viewHolderQuestionInvite.mLLmore;


                viewHolderQuestionInvite.mLLmore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        showMsgMenu(MsgEntity.IMsgType.QUESTION_INVITE,msgEntity.id, IMsgIgnoreType.TYPE_NORMAL,llMoreQuestionInvite);
                    }
                });


                break;

            case MsgEntity.IMsgType.QUESTION_FAVOURTE_REPLY: //关注的问题有新的回答

                ViewHolderQuestionReply viewHolderQuestionFavourteReply = null;

                if (null == convertView) {

                    convertView = mLayoutInflater.inflate(R.layout.item_msg_quesiton_reply, null);
                    viewHolderQuestionFavourteReply = new ViewHolderQuestionReply();
                    viewHolderQuestionFavourteReply.mLLmore = (LinearLayout) convertView.findViewById(R.id.ll_more);
                    viewHolderQuestionFavourteReply.mTxtTitle = (TextView) convertView.findViewById(R.id.txt_title);
                    viewHolderQuestionFavourteReply.mTxtDesc = (TextView) convertView.findViewById(R.id.txt_desc);
                    viewHolderQuestionFavourteReply.mBtn = (Button) convertView.findViewById(R.id.btn);
                    convertView.setTag(viewHolderQuestionFavourteReply);
                } else {
                    viewHolderQuestionFavourteReply = (ViewHolderQuestionReply) convertView.getTag();
                }

                final MsgQuestionFavourteReplyDTO msgQuestionFavourteReplyDTO = DataGsonUitls.format(msg, MsgQuestionFavourteReplyDTO.class);

                if (null != msgQuestionFavourteReplyDTO) {

                    List<String> userListFavourteReply = msgQuestionFavourteReplyDTO.answer_user_list;

                    String questionUserFavourteReply = "";
                    //获取所有回答的用户,获取问题内容就取第一个元素
                    int countFavourteReply = userListFavourteReply.size() > 2 ? 2 : userListFavourteReply.size();
                    for (int i = 0; i < countFavourteReply; i++) {
                        String str = userListFavourteReply.get(i);
                        questionUserFavourteReply += str;
                        if (countFavourteReply > 1 && i != countFavourteReply - 1) {
                            questionUserFavourteReply += "、";
                        }
                    }
                    if (userListFavourteReply.size() > 0 & userListFavourteReply.size() <= 2) {

                        questionUserFavourteReply += mContext.getString(R.string.msg_question_favoutite_repley_people_single);
                    } else if (userListFavourteReply.size() > 2) {
                        questionUserFavourteReply += String.format(mContext.getString(R.string.msg_question_favoutite_reply), String.valueOf(userListFavourteReply.size()));
                    }
                    StringBuilder titleFavourteReply = new StringBuilder();
                    titleFavourteReply.append(questionUserFavourteReply);
                    titleFavourteReply.append("\"");
                    titleFavourteReply.append(msgQuestionFavourteReplyDTO.content);
                    titleFavourteReply.append("\"");
                    SpannableString spannableString = new SpannableString(titleFavourteReply);
                    spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#8392A0")),
                            0, questionUserFavourteReply.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#cdac8c"))
                            , questionUserFavourteReply.length(), titleFavourteReply.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


                    viewHolderQuestionFavourteReply.mTxtDesc.setText(spannableString);

                    viewHolderQuestionFavourteReply.mBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (null != mIMsgClickListener)
                                mIMsgClickListener.onClick(msgEntity.type_id, msgQuestionFavourteReplyDTO.question_id);
                        }
                    });
                }


                final LinearLayout llMoreQuestionFavourteReply = viewHolderQuestionFavourteReply.mLLmore;

                viewHolderQuestionFavourteReply.mLLmore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        showMsgMenu(MsgEntity.IMsgType.QUESTION_FAVOURTE_REPLY,msgEntity.id, IMsgIgnoreType.TYPE_NORMAL,llMoreQuestionFavourteReply);
                    }
                });


                break;

            /*** 新的活动*/
            case MsgEntity.IMsgType.ACTIVITY_NEW:

                ViewHolderActivityNew viewHolderActivityNew = null;
                final int typeIdCurrent = msgEntity.type_id;

                if (null == convertView) {

                    viewHolderActivityNew = new ViewHolderActivityNew();
                    convertView = mLayoutInflater.inflate(R.layout.item_msg_activity_new, null);
                    viewHolderActivityNew.mLLMore = (LinearLayout) convertView.findViewById(R.id.ll_more);
                    viewHolderActivityNew.mImg = (ImageView) convertView.findViewById(R.id.img);
                    viewHolderActivityNew.mTxtTitle = (TextView) convertView.findViewById(R.id.txt_activity_title);
                    viewHolderActivityNew.mTxtActivityDate = (TextView) convertView.findViewById(R.id.txt_activity_date);
                    viewHolderActivityNew.mTxtActivityPrice = (TextView) convertView.findViewById(R.id.txt_activity_price);
                    viewHolderActivityNew.mBtn = (Button) convertView.findViewById(R.id.btn);
                    convertView.setTag(viewHolderActivityNew);
                } else {
                    viewHolderActivityNew = (ViewHolderActivityNew) convertView.getTag();
                }
                //
                final MsgActivityNewEntity msgActivityNewEntity = DataGsonUitls.format(msg, MsgActivityNewEntity.class);

                if (null != msgActivityNewEntity) {
                    viewHolderActivityNew.mTxtTitle.setText(msgActivityNewEntity.title);
                    //
                    viewHolderActivityNew.mTxtActivityDate.setText(
                            String.format(mContext.getString(R.string.msg_msg_activity_date_end),
                                    DateUtils.formatDate(msgActivityNewEntity.signup_deadline * 1000l)));
                    //
                    viewHolderActivityNew.mTxtActivityPrice.setText(NumberFormatUtils.formatPointTwo(msgActivityNewEntity.price));
                    //

                    ViewGroup.LayoutParams params = viewHolderActivityNew.mImg.getLayoutParams();
                    params.height = ScreenWindowUtil.getScreenWidth(mContext) * 9 / 16;
                    viewHolderActivityNew.mImg.setLayoutParams(params);

                    if (!msgActivityNewEntity.poster.equals(viewHolderActivityNew.mImg.getTag())) {
                        ImageLoader.getInstance().displayImage(msgActivityNewEntity.poster, viewHolderActivityNew.mImg,
                                ImageLoaderOptions.option(R.drawable.img_default_course_system));
                        viewHolderActivityNew.mImg.setTag(msgActivityNewEntity.poster);
                    }

                    viewHolderActivityNew.mBtn.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (null != mIMsgClickListener)
                                mIMsgClickListener.onClick(typeIdCurrent, msgActivityNewEntity.activity_id);

                        }
                    });

                    final LinearLayout llMoreActivityNew = viewHolderActivityNew.mLLMore;

                    viewHolderActivityNew.mLLMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            showMsgMenu(MsgEntity.IMsgType.ACTIVITY_NEW,msgEntity.id,IMsgIgnoreType.TYPE_NORMAL, llMoreActivityNew);
                        }
                    });
                }


                break;

            /*** 报名活动提醒*/
            case MsgEntity.IMsgType.ACTIVITY_START:

                ViewHolderActivity viewHolderActivity = null;
                //
                final int typeIdTemp = msgEntity.type_id;
                //
                if (null == convertView) {
                    viewHolderActivity = new ViewHolderActivity();
                    convertView = mLayoutInflater.inflate(R.layout.item_msg_activity, null);
                    viewHolderActivity.mLLMore = (LinearLayout) convertView.findViewById(R.id.ll_more);
                    viewHolderActivity.mTxtTitle = (TextView) convertView.findViewById(R.id.txt_title);
                    viewHolderActivity.mTxtActivityDate = (TextView) convertView.findViewById(R.id.txt_activity_date);
                    viewHolderActivity.mBtn = (Button) convertView.findViewById(R.id.btn);
                    convertView.setTag(viewHolderActivity);
                } else {
                    viewHolderActivity = (ViewHolderActivity) convertView.getTag();
                }
                final MsgActivityEntity msgActivityEntity = DataGsonUitls.format(msg, MsgActivityEntity.class);
                //
                if (null != msgActivityEntity) {

                    viewHolderActivity.mTxtTitle.setText(msgActivityEntity.title);
                    //
                    viewHolderActivity.mTxtActivityDate.setText(String.format(mContext.getString(R.string.msg_msg_activity_date_start),
                            DateUtils.formatDate(msgActivityEntity.start_time * 1000l)));
                    viewHolderActivity.mBtn.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (null != mIMsgClickListener)
                                mIMsgClickListener.onClick(typeIdTemp, msgActivityEntity.activity_id);

                        }
                    });

                    final LinearLayout llMoreActivity = viewHolderActivity.mLLMore;

                    viewHolderActivity.mLLMore.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            showMsgMenu(MsgEntity.IMsgType.ACTIVITY_START,msgEntity.id,IMsgIgnoreType.TYPE_NORMAL, llMoreActivity);
                        }
                    });
                }
                break;

            /*** 教室加入申请*/
            case MsgEntity.IMsgType.CLASSROOM_JOIN:

                ViewHolderClassRoom viewHolderClassRoom = null;

                if (null == convertView) {
                    viewHolderClassRoom = new ViewHolderClassRoom();
                    convertView = mLayoutInflater.inflate(R.layout.item_msg_classroom_join, null);
                    viewHolderClassRoom.mLLMore = (LinearLayout) convertView.findViewById(R.id.ll_more);
                    viewHolderClassRoom.mTxtTitle = (TextView) convertView.findViewById(R.id.txt_title);
                    viewHolderClassRoom.mBtn = (Button) convertView.findViewById(R.id.btn);
                    convertView.setTag(viewHolderClassRoom);
                } else {
                    viewHolderClassRoom = (ViewHolderClassRoom) convertView.getTag();
                }
                final MsgClassRoomEntity msgClassRoomEntity = DataGsonUitls.format(msg, MsgClassRoomEntity.class);

                if (null != msgClassRoomEntity) {
                    viewHolderClassRoom.mTxtTitle.setText(String.format(mContext.getString(R.string.msg_classroom),
                            msgClassRoomEntity.name));

                    viewHolderClassRoom.mBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (null != mIMsgClickListener) {
                                ClassRoomListEntity classRoomListEntity = new ClassRoomListEntity();
                                classRoomListEntity.roomid = msgClassRoomEntity.classroom_id;
                                classRoomListEntity.hx_roomid = msgClassRoomEntity.hx_roomid;
                                classRoomListEntity.name = msgClassRoomEntity.name;

                                mIMsgClickListener.jumpClassRoom(classRoomListEntity);
                            }
                        }
                    });

                    final LinearLayout llMoreClassRoom = viewHolderClassRoom.mLLMore;

                    viewHolderClassRoom.mLLMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            showMsgMenu(MsgEntity.IMsgType.CLASSROOM_JOIN,msgEntity.id,IMsgIgnoreType.TYPE_NORMAL, llMoreClassRoom);
                        }
                    });
                }
                break;

            /*** 新的直播上架*/
            case MsgEntity.IMsgType.LIVE_NEW:

                ViewHolderLiveNew viewHolderLiveNew = null;

                if (null == convertView) {
                    viewHolderLiveNew = new ViewHolderLiveNew();

                    convertView = mLayoutInflater.inflate(R.layout.item_msg_live, null);

                    viewHolderLiveNew.mLLMore = (LinearLayout) convertView.findViewById(R.id.ll_more);
                    viewHolderLiveNew.mImg = (ImageView) convertView.findViewById(R.id.img);
                    viewHolderLiveNew.mTxtLiveTitle = (TextView) convertView.findViewById(R.id.txt_live_title);
                    viewHolderLiveNew.mTxtLiveStartTime = (TextView) convertView.findViewById(R.id.txt_live_start_time);
                    viewHolderLiveNew.mTxtLivePrice = (TextView) convertView.findViewById(R.id.txt_live_price);
                    viewHolderLiveNew.mBtn = (Button) convertView.findViewById(R.id.btn);
                    viewHolderLiveNew.mLLStatusFree = (LinearLayout) convertView.findViewById(R.id.ll_live_status_free);
                    viewHolderLiveNew.mLLStatusNormal = (LinearLayout) convertView.findViewById(R.id.ll_live_status_normal);
                    viewHolderLiveNew.mLLStatusPass = (LinearLayout) convertView.findViewById(R.id.ll_live_status_pass);
                    viewHolderLiveNew.mTxtLiveSpeaker = (TextView) convertView.findViewById(R.id.txt_live_speaker);

                    convertView.setTag(viewHolderLiveNew);
                } else {

                    viewHolderLiveNew = (ViewHolderLiveNew) convertView.getTag();

                }
                final MsgLiveNewEntity msgLiveNewEntity = DataGsonUitls.format(msg, MsgLiveNewEntity.class);
                //
                if (null != msgLiveNewEntity) {
                    viewHolderLiveNew.mTxtLiveTitle.setText(msgLiveNewEntity.title);
                    viewHolderLiveNew.mTxtLiveStartTime.setText(String.format(mContext.getString(R.string.msg_live_start_time),
                            DateUtils.timestampToPatternTime(msgLiveNewEntity.start_time * 1000l, "MM/dd HH:mm")));

                    viewHolderLiveNew.mTxtLivePrice.setText(NumberFormatUtils.formatPointTwo(msgLiveNewEntity.amount));
                    viewHolderLiveNew.mTxtLiveSpeaker.setText(String.format(mContext.getString(R.string.msg_speaker), msgLiveNewEntity.speaker));
                    if (!msgLiveNewEntity.poster.equals(viewHolderLiveNew.mImg.getTag())) {
                        ImageLoader.getInstance().displayImage(msgLiveNewEntity.poster, viewHolderLiveNew.mImg,
                                ImageLoaderOptions.option(R.drawable.img_default_course_system));
                        viewHolderLiveNew.mImg.setTag(msgLiveNewEntity.poster);
                    }

//                viewHolderLiveNew.mBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (null != mIMsgClickListener)
//                            mIMsgClickListener.jumpLiveDetails(msgLiveNewEntity.live_id,msgLiveNewEntity.title);
//
//                    }
//                });
                }
                //免费直播
                if (msgLiveNewEntity.is_free) {

                    viewHolderLiveNew.mLLStatusNormal.setVisibility(View.INVISIBLE);
                    viewHolderLiveNew.mLLStatusPass.setVisibility(View.INVISIBLE);

                    viewHolderLiveNew.mLLStatusFree.setVisibility(View.VISIBLE);

                    //收费直播未购买
                } else if (msgLiveNewEntity.is_free == false && msgLiveNewEntity.is_allowed == false) {

                    viewHolderLiveNew.mLLStatusFree.setVisibility(View.INVISIBLE);
                    viewHolderLiveNew.mLLStatusPass.setVisibility(View.INVISIBLE);

                    viewHolderLiveNew.mLLStatusNormal.setVisibility(View.VISIBLE);

                    //收费直播已购买
                } else if (msgLiveNewEntity.is_free == false && msgLiveNewEntity.is_allowed) {

                    viewHolderLiveNew.mLLStatusNormal.setVisibility(View.INVISIBLE);
                    viewHolderLiveNew.mLLStatusFree.setVisibility(View.INVISIBLE);
                    viewHolderLiveNew.mLLStatusPass.setVisibility(View.VISIBLE);
                }
                final LinearLayout llMoreLiveNew = viewHolderLiveNew.mLLMore;

                viewHolderLiveNew.mLLMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        showMsgMenu(MsgEntity.IMsgType.LIVE_NEW,msgEntity.id,IMsgIgnoreType.TYPE_NORMAL,llMoreLiveNew);
                    }
                });

                break;

            /*** 报名的直播*/
            case MsgEntity.IMsgType.LIVE_START:
                ViewHolderLive viewHolderLive = null;

                if (null == convertView) {
                    viewHolderLive = new ViewHolderLive();

                    convertView = mLayoutInflater.inflate(R.layout.item_msg_live_join, null);
                    viewHolderLive.mLLMore = (LinearLayout) convertView.findViewById(R.id.ll_more);
                    viewHolderLive.mTxtLiveTitle = (TextView) convertView.findViewById(R.id.txt_title);
                    viewHolderLive.mTxtTime = (TextView) convertView.findViewById(R.id.txt_activity_date);
                    viewHolderLive.mBtn = (Button) convertView.findViewById(R.id.btn);
                    viewHolderLive.mCountdownView = (CountdownView) convertView.findViewById(R.id.count_donw_view);
                    convertView.setTag(viewHolderLive);
                } else {
                    viewHolderLive = (ViewHolderLive) convertView.getTag();
                }

                final MsgLiveEntity msgLiveEntity = DataGsonUitls.format(msg, MsgLiveEntity.class);
                MsgCardLiveTimer msgCardLiveTimer = new MsgCardLiveTimer(mContext, viewHolderLive.mCountdownView);
                msgCardLiveTimer.startTime(msgLiveEntity.start_time);
                viewHolderLive.mTxtLiveTitle.setText("《" + msgLiveEntity.title + "》");


                if (null != msgLiveEntity) {

                    viewHolderLive.mBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (null != mIMsgClickListener)
                                mIMsgClickListener.jumpLiveDetails(msgLiveEntity.live_id, msgLiveEntity.title);
                        }
                    });

                    final LinearLayout llMoreLive = viewHolderLive.mLLMore;

                    viewHolderLive.mLLMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            showMsgMenu(MsgEntity.IMsgType.LIVE_START,msgEntity.id,IMsgIgnoreType.TYPE_NORMAL,llMoreLive);
                        }
                    });
                }
                break;

            /**
             * 新建课程列表
             */
            case MsgEntity.IMsgType.COLLEAGE_TABLE:

                ViewHolderCourseList viewHolderCourseList = null;
                if (null == viewHolderCourseList) {
                    viewHolderCourseList = new ViewHolderCourseList();
                    //
                    convertView = mLayoutInflater.inflate(R.layout.item_msg_course_list, null);
                    viewHolderCourseList.mLLMore = (LinearLayout) convertView.findViewById(R.id.ll_more);
                    viewHolderCourseList.mTxtTitle = (TextView) convertView.findViewById(R.id.txt_title);
                    viewHolderCourseList.mLLCourseList = (LinearLayout) convertView.findViewById(R.id.ll_course_list);
                    viewHolderCourseList.mBtn = (Button) convertView.findViewById(R.id.btn);

                } else {
                    viewHolderCourseList = (ViewHolderCourseList) convertView.getTag();
                }

                final MsgCourseListEntity msgCourseListEntity = DataGsonUitls.format(msg, MsgCourseListEntity.class);

                if (null != msgCourseListEntity) {
                    viewHolderCourseList.mTxtTitle.setText(mContext.getString(R.string.msg_course_list));

                    ArrayList<String> data = msgCourseListEntity.data;
                    viewHolderCourseList.mLLCourseList.removeAllViews();
                    //
                    if (data.size() > 0) {
                        for (int i = 0; i < data.size(); i++) {
                            TextView txtView = createCourseList((i + 1) + ". " + data.get(i));
                            viewHolderCourseList.mLLCourseList.addView(txtView);
                        }
                    }
                    viewHolderCourseList.mBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (null != mIMsgClickListener)
                                mIMsgClickListener.onClick(msgEntity.type_id, "0");

                        }
                    });

                    final LinearLayout llMoreCourseList = viewHolderCourseList.mLLMore;

                    viewHolderCourseList.mLLMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            showMsgMenu(MsgEntity.IMsgType.COLLEAGE_TABLE,msgEntity.id,IMsgIgnoreType.TYPE_NORMAL,llMoreCourseList);
                        }
                    });

                }
                break;

            /** *
             * 新的课程提醒
             */
            case MsgEntity.IMsgType.COLLEAGE_NEW:
                ViewHolderCourse viewHolderCourse = null;
                if (null == convertView) {
                    viewHolderCourse = new ViewHolderCourse();
                    convertView = mLayoutInflater.inflate(R.layout.item_msg_course_new, null);
                    viewHolderCourse.mLLMore = (LinearLayout) convertView.findViewById(R.id.ll_more);
                    viewHolderCourse.mTxtTitle = (TextView) convertView.findViewById(R.id.txt_course_title);
                    viewHolderCourse.mTxtCity = (TextView) convertView.findViewById(R.id.txt_city);
                    viewHolderCourse.mTxtPrice = (TextView) convertView.findViewById(R.id.txt_course_price);
                    viewHolderCourse.mImg = (ImageView) convertView.findViewById(R.id.img);
                    //viewHolderCourse.mBtn=(Button)convertView.findViewById(R.id.btn);
                    //
                    convertView.setTag(viewHolderCourse);
                } else {
                    viewHolderCourse = (ViewHolderCourse) convertView.getTag();
                }
                final MsgCourseEntity msgCourseEntity = DataGsonUitls.format(msg, MsgCourseEntity.class);

                if (null != msgCourseEntity) {

                    viewHolderCourse.mTxtTitle.setText(String.format(mContext.getString(R.string.msg_course_new), msgCourseEntity.title));
                    //
                    viewHolderCourse.mTxtCity.setText("城市 :" + msgCourseEntity.city);
                    //价格
                    viewHolderCourse.mTxtPrice.setText(NumberFormatUtils.formatPointTwo(msgCourseEntity.amount));
                    //
                    if (!msgCourseEntity.poster.equals(viewHolderCourse.mImg.getTag())) {
                        ImageLoader.getInstance().displayImage(msgCourseEntity.poster, viewHolderCourse.mImg,
                                ImageLoaderOptions.option(R.drawable.img_default_course_system));
                        viewHolderCourse.mImg.setTag(msgCourseEntity.poster);
                    }
                    final LinearLayout llMoreCourse = viewHolderCourse.mLLMore;

                    viewHolderCourse.mLLMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            showMsgMenu(MsgEntity.IMsgType.COLLEAGE_NEW,msgEntity.id,IMsgIgnoreType.TYPE_NORMAL,llMoreCourse);
                        }
                    });
                }
                break;

            /**
             * 报名的课程提醒*/

            case MsgEntity.IMsgType.COLLEAGE_START:

                ViewHolderCourse viewHolderCourseStart = null;

                if (null == convertView) {
                    convertView = mLayoutInflater.inflate(R.layout.item_msg_course_join, null);
                    viewHolderCourseStart = new ViewHolderCourse();
                    viewHolderCourseStart.mLLMore = (LinearLayout) convertView.findViewById(R.id.ll_more);
                    viewHolderCourseStart.mTxtTitle = (TextView) convertView.findViewById(R.id.txt_title);
                    viewHolderCourseStart.mBtn = (Button) convertView.findViewById(R.id.btn);
                    convertView.setTag(viewHolderCourseStart);
                } else {
                    viewHolderCourseStart = (ViewHolderCourse) convertView.getTag();
                }
                final MsgCourseJoinEntity msgCourseJoinEntity = DataGsonUitls.format(msg, MsgCourseJoinEntity.class);

                if (null != msgCourseJoinEntity) {
                    String day = String.valueOf(msgCourseJoinEntity.days_left <= 0 ? 0 : msgCourseJoinEntity.days_left);
                    viewHolderCourseStart.mTxtTitle.setText(String.format(mContext.getString(R.string.msg_course_start_time),
                            msgCourseJoinEntity.title, day));
                    viewHolderCourseStart.mBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (null != mIMsgClickListener)
                                mIMsgClickListener.jumpCourseDetails(msgCourseJoinEntity.business_id, msgCourseJoinEntity.cour_id);

                        }
                    });

                    final LinearLayout llMoreCourseStart = viewHolderCourseStart.mLLMore;

                    viewHolderCourseStart.mLLMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            showMsgMenu(MsgEntity.IMsgType.COLLEAGE_START,msgEntity.id,IMsgIgnoreType.TYPE_NORMAL,llMoreCourseStart);
                        }
                    });
                }

                break;

            //系统更新
            case MsgEntity.IMsgType.SYSTEM_UPDATE:
                ViewHolderSystemUpdate viewHolderSystemUpdate=null;
                if (null == convertView) {
                    convertView = mLayoutInflater.inflate(R.layout.item_msg_system_update, null);
                    viewHolderSystemUpdate=new ViewHolderSystemUpdate();
                    viewHolderSystemUpdate.mTxtTitle=(TextView) convertView.findViewById(R.id.txt_system_udapte_title);
                    viewHolderSystemUpdate.mTxtContent=(TextView)convertView.findViewById(R.id.txt_system_update_desc);
                    viewHolderSystemUpdate.mLLMore=(LinearLayout)convertView.findViewById(R.id.ll_system_update_more);
                    viewHolderSystemUpdate.mBtn=(Button)convertView.findViewById(R.id.btn_system_update);
                    convertView.setTag(viewHolderSystemUpdate);
                }else{
                    viewHolderSystemUpdate=(ViewHolderSystemUpdate)convertView.getTag();

                }
                final MsgSystemUpdate msgSystemUpdate = DataGsonUitls.format(msg, MsgSystemUpdate.class);
                viewHolderSystemUpdate.mTxtTitle.setText(msgSystemUpdate.title);
                viewHolderSystemUpdate.mTxtContent.setText(msgSystemUpdate.description);


                final LinearLayout llMoreCourseStart = viewHolderSystemUpdate.mLLMore;

                viewHolderSystemUpdate.mLLMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showMsgMenu(MsgEntity.IMsgType.SYSTEM_UPDATE,msgEntity.id,IMsgIgnoreType.TYPE_SYSTEM,llMoreCourseStart);

                    }
                });

                viewHolderSystemUpdate.mBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != mIMsgClickListener)
                            mIMsgClickListener.update(msgSystemUpdate.address);


                    }
                });

                break;
            default:
                convertView = mLayoutInflater.inflate(R.layout.item_msg_empty, null);

                break;
        }

        return convertView;
    }


    /**
     * 创建课程
     */
    private TextView createCourseList(String courseTitle) {

        TextView txtView = new TextView(mContext);
        txtView.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        txtView.setPadding(0, 0, 0, 5);
        txtView.setTextSize(14);
        txtView.setTextColor(mContext.getResources().getColor(R.color.c_gray_999));
        txtView.setText(courseTitle);
        return txtView;
    }

    /**
     * 弹出菜单
     *
     * @param cardType  卡片类型
     * @param msgId 消息id
     */
    private void showMsgMenu(int cardType,String msgId, int ignoreType,View view) {

        final String id = msgId;
        final int  type=ignoreType;
        final int  cardTypeC=cardType;
        final MsgPopupWindow msgPopupWindow = new MsgPopupWindow(mContext);
        msgPopupWindow.showView(view);
        msgPopupWindow.setIOnClickListener(new MsgPopupWindow.IOnClickListener() {
            @Override
            public void onClick() {
                if (null != mIMsgClickListener) {
                    msgPopupWindow.dismiss();
                    mIMsgClickListener.ignoreMsg(cardTypeC,id,type);
                }
            }
        });
    }

    /**
     * 系统任务
     */
    class  ViewHolderSystemTask{

        //系统卡片
        private LinearLayout mLLMore;

    }
    /**
     * 新的课程
     */
    class ViewHolderCourse {

        private LinearLayout mLLMore;
        //
        private ImageView mImg;
        //标题
        private TextView mTxtTitle;
        //城市
        private TextView mTxtCity;
        //价格
        private TextView mTxtPrice;
        private Button mBtn;
    }


    /**
     * 课程列表
     */
    class ViewHolderCourseList {
        private LinearLayout mLLMore;
        //
        private TextView mTxtTitle;
        //
        private LinearLayout mLLCourseList;
        //
        private Button mBtn;
    }


    /**
     * 问题被回答
     */
    class ViewHolderQuestionReply {

        //标题
        private TextView mTxtTitle;
        //内容描述
        private TextView mTxtDesc;
        //
        private LinearLayout mLLmore;
        private Button mBtn;
    }

    /**
     * 回答被点赞
     */

    class ViewHolderAnswerFavourite {
        //标题
        private TextView mTxtTitle;
        //
        private TextView mTxtContent;
        //
        private LinearLayout mLLmore;
        private Button mBtn;
    }

    /**
     * 新的活动
     */
    class ViewHolderActivityNew {

        private LinearLayout mLLMore;
        //
        private ImageView mImg;
        //活动标题
        private TextView mTxtTitle;
        //活动日期
        private TextView mTxtActivityDate;
        //活动价格
        private TextView mTxtActivityPrice;
        //
        private Button mBtn;
    }

    /**
     * 报名的活动
     */
    class ViewHolderActivity {

        private LinearLayout mLLMore;
        //活动标题
        private TextView mTxtTitle;
        //活动开始的日期
        private TextView mTxtActivityDate;
        //
        private Button mBtn;
    }


    class ViewHolderClassRoom {

        private LinearLayout mLLMore;
        //
        private TextView mTxtTitle;
        //
        private Button mBtn;
    }


    class ViewHolderLiveNew {

        private LinearLayout mLLMore;
        //
        private ImageView mImg;
        //直播标题
        private TextView mTxtLiveTitle;
        //直播开始的时间
        private TextView mTxtLiveStartTime;
        //
        private TextView mTxtLivePrice;
        //
        private TextView mTxtLiveSpeaker;
        //
        private Button mBtn;
        //直播免费状态
        private LinearLayout mLLStatusFree;
        //收费直播,未购买
        private LinearLayout mLLStatusNormal;
        //收费直播,已购买
        private LinearLayout mLLStatusPass;
    }


    class ViewHolderLive {

        private LinearLayout mLLMore;
        //直播标题
        private TextView mTxtLiveTitle;
        //直播倒计时时间
        private TextView mTxtTime;
        //直播
        private Button mBtn;

        private CountdownView mCountdownView;
    }

    /**
     * 版本更新
     */
    class ViewHolderSystemUpdate{

        private TextView mTxtTitle;
        //更新内容
        private TextView mTxtContent;
        //
        private LinearLayout mLLMore;
        //
        private Button mBtn;
    }

    public void setIMsgClickListener(MsgEntity.IMsgClickListener mIMsgClickListener) {
        this.mIMsgClickListener = mIMsgClickListener;
    }
}
