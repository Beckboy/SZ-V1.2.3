package com.junhsue.ksee.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.junhsue.ksee.R;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.entity.AnswerEntity;
import com.junhsue.ksee.file.FileUtil;
import com.junhsue.ksee.file.ImageLoaderOptions;
import com.junhsue.ksee.utils.DateUtils;
import com.junhsue.ksee.utils.ScreenWindowUtil;
import com.junhsue.ksee.utils.StringUtils;
import com.junhsue.ksee.utils.VoicePlayUtil;
import com.junhsue.ksee.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.IOException;

/**
 * 回答列表适配器
 * Created by Sugar on 17/3/28.
 */

public class AnswerAdapter<T extends AnswerEntity> extends MyBaseAdapter<AnswerEntity> {

    private Context mContext;
    private LayoutInflater mInflater;
    private OnApprovalClickListener approvalClickListener;

    private VoiceListener voiceListener;

    ViewHolder viewHolder = null;


    public AnswerAdapter(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    protected View getWrappeView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_question_detail_answer, null);
            viewHolder.civAnswerAvatar = (CircleImageView) convertView.findViewById(R.id.civ_answer_avatar);
            viewHolder.tvAnswerNickname = (TextView) convertView.findViewById(R.id.tv_answer_nickname);
            viewHolder.rlPraise = (RelativeLayout) convertView.findViewById(R.id.rl_praise);
            viewHolder.ivPraise = (ImageView) convertView.findViewById(R.id.iv_praise);
            viewHolder.tvTextAnswerContent = (TextView) convertView.findViewById(R.id.tv_text_answer_content);
            viewHolder.tvPraiseNumber = (TextView) convertView.findViewById(R.id.tv_praise_number);
            viewHolder.tvAnswerTime = (TextView) convertView.findViewById(R.id.tv_answer_time);
            viewHolder.voiceLayout = (LinearLayout) convertView.findViewById(R.id.ll_voice_answer_layout);
            viewHolder.rlVoiceAnswer = (RelativeLayout) convertView.findViewById(R.id.rl_voice_answer);
            viewHolder.ivVoiceAnswer = (ImageView) convertView.findViewById(R.id.img_answer_voice);
            viewHolder.tvVoiceLength = (TextView) convertView.findViewById(R.id.tv_voice_length);
            convertView.setTag(viewHolder);
        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }

        final AnswerEntity entity = mList.get(position);

        setData(viewHolder, entity, position);

        return convertView;
    }


    private void setData(final ViewHolder viewHolder, final AnswerEntity entity, final int position) {
        if (entity == null) {
            return;
        }

        if (null!=entity.avatar && !entity.avatar.equals(viewHolder.civAnswerAvatar.getTag())) {
                ImageLoader.getInstance().displayImage(entity.avatar, viewHolder.civAnswerAvatar,
                        ImageLoaderOptions.option(R.drawable.pic_default_avatar));
                viewHolder.civAnswerAvatar.setTag(entity.avatar);
        }else if(null==entity.avatar){
            ImageLoader.getInstance().displayImage("", viewHolder.civAnswerAvatar,
                    ImageLoaderOptions.option(R.drawable.pic_default_avatar));
        }

        viewHolder.tvAnswerNickname.setText(entity.nickname);
        viewHolder.tvTextAnswerContent.setText(entity.content);
        viewHolder.tvVoiceLength.setText(entity.duration + "''");
        viewHolder.tvPraiseNumber.setText("赞同 " + entity.approval_count);


        viewHolder.tvAnswerTime.setText(DateUtils.fromTheCurrentTime(entity.publish_time * 1000l,
                System.currentTimeMillis()));

        if (entity.is_approval) {
            viewHolder.ivPraise.setImageResource(R.drawable.icon_agree_press);
        } else {
            viewHolder.ivPraise.setImageResource(R.drawable.icon_agree_normal);
        }


        if (AnswerEntity.TXT_REPLY_TYPE_VALUE == entity.content_type_id) {// 待优化
            viewHolder.tvTextAnswerContent.setVisibility(View.VISIBLE);
            viewHolder.voiceLayout.setVisibility(View.GONE);
        } else {
            viewHolder.tvTextAnswerContent.setVisibility(View.GONE);
            viewHolder.voiceLayout.setVisibility(View.VISIBLE);
            setVoiceBackgroundLayoutLength(entity, viewHolder.rlVoiceAnswer);
            if (!entity.play_status) {
                viewHolder.ivVoiceAnswer.setImageResource(R.drawable.icon_others_play03);
            }
        }

        viewHolder.rlPraise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (approvalClickListener != null) {
                    approvalClickListener.onApprovalClick(entity, position);
                }
            }
        });

        viewHolder.rlVoiceAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadAndPlayVoice(entity, v, viewHolder.tvVoiceLength, viewHolder.ivVoiceAnswer);

            }
        });


    }

    private void setVoiceBackgroundLayoutLength(AnswerEntity entity, RelativeLayout rlVoiceAnswer) {

        ViewGroup.LayoutParams params = rlVoiceAnswer.getLayoutParams();

        if (0 < entity.duration && entity.duration <= 45) {
            params.width = (int) mContext.getResources().getDimension(R.dimen.dimen_80px) * 4;
        } else if (15 < entity.duration && entity.duration <= 60) {
            params.width = (int) mContext.getResources().getDimension(R.dimen.dimen_80px) * 5;
        }

        rlVoiceAnswer.setLayoutParams(params);
    }


    /**
     * 下载录音播放文件
     *
     * @param entity
     * @param onClickView
     * @param freshView
     * @param animationView
     */
    private void downloadAndPlayVoice(AnswerEntity entity, View onClickView, TextView freshView, ImageView animationView) {

        String dir = Constants.VOICE_SAVE_ROOT;
        String fileName = entity.id + "" + entity.content_type_id + ".amr";
        entity.playFile = dir + fileName;


        if (StringUtils.isBlank(entity.content)) {
            return;
        }
        if (entity.content_type_id != 2) {
            return;
        }

        if (voiceListener != null) {
            voiceListener.onPlay(onClickView, freshView, animationView, entity, dir, fileName);
        }

    }

    private class ViewHolder {

        private CircleImageView civAnswerAvatar;
        private TextView tvAnswerNickname;
        private RelativeLayout rlPraise;
        private ImageView ivPraise;
        private TextView tvTextAnswerContent;
        private TextView tvAnswerTime;
        private TextView tvPraiseNumber;
        private LinearLayout voiceLayout;
        private RelativeLayout rlVoiceAnswer;
        private ImageView ivVoiceAnswer;
        private TextView tvVoiceLength;

    }


    public void setOnApprovalClickListener(OnApprovalClickListener listener) {

        this.approvalClickListener = listener;

    }

    public void setVoiceListener(VoiceListener voiceListener) {
        this.voiceListener = voiceListener;

    }

    public interface OnApprovalClickListener {

        void onApprovalClick(AnswerEntity entity, int position);
    }

    public interface VoiceListener {

        void onPlay(View onClickView, TextView freshView, ImageView animationView, AnswerEntity entity, String fileStr, String fileName);

    }

}
