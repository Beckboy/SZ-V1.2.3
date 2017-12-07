package com.junhsue.ksee;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.junhsue.ksee.adapter.AnswerAdapter;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.dto.InviteDTO;
import com.junhsue.ksee.dto.QuestionDetailAnswerListDTO;
import com.junhsue.ksee.entity.AnswerEntity;
import com.junhsue.ksee.entity.QuestionEntity;
import com.junhsue.ksee.entity.ResultEntity;
import com.junhsue.ksee.file.ImageLoaderOptions;
import com.junhsue.ksee.fragment.MyAnswerColleageFragment;
import com.junhsue.ksee.fragment.dialog.VoiceRecordDialogFragment;
import com.junhsue.ksee.net.api.OKHttpDownloadFileImpl;
import com.junhsue.ksee.net.api.OkHttpILoginImpl;
import com.junhsue.ksee.net.api.OkHttpSocialCircleImpl;
import com.junhsue.ksee.net.callback.FileRequestCallBack;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.url.WebViewUrl;
import com.junhsue.ksee.utils.DateUtils;
import com.junhsue.ksee.utils.MediaPlayUtil;
import com.junhsue.ksee.utils.ShareUtils;
import com.junhsue.ksee.utils.StatisticsUtil;
import com.junhsue.ksee.utils.StringUtils;
import com.junhsue.ksee.utils.ToastUtil;
import com.junhsue.ksee.utils.VoicePlayUtil;
import com.junhsue.ksee.view.ActionBar;
import com.junhsue.ksee.view.ActionSheet;
import com.junhsue.ksee.view.CircleImageView;
import com.junhsue.ksee.view.Spanny;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.social.UMPlatformData;

import java.io.File;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 问答详情页
 * Created by Sugar on 17/3/24 in Junhsue.
 */

public class QuestionDetailActivity extends BaseActivity implements View.OnClickListener, AnswerAdapter.OnApprovalClickListener, AnswerAdapter.VoiceListener {

    private ActionBar actionBar;
    private String questionId;
    private ListView lvQuestionDetail;
    private RelativeLayout blankPage;
    private PtrClassicFrameLayout pcframLayout;
    private Context mContext;

    private CircleImageView civAvatar;
    private TextView tvNickname;
    //机构
    private TextView tvfromD;
    private TextView tvQuestionTitle;
    private TextView tvTopicContent;
    private TextView tvCollectNumber;
    private TextView tvReplyNumber;
    private TextView tvPublishTime;
    private TextView tvFromTopicName;
    private int currentPage = 0;
    private boolean isMaxPage = false;
    private int pageSize = 10;
    //问答实体
    private QuestionEntity questionEntity;

    //分享弹出框
    private ActionSheet shareActionSheetDialog;
    //回答列表适配器
    private AnswerAdapter answerAdapter;
    //语音播放工具
    private VoicePlayUtil voicePlayUtil;
    private AnimationDrawable animationDrawable;


    @Override
    protected void onReceiveArguments(Bundle bundle) {
        questionId = bundle.getString(Constants.QUESTION_ID, "");
    }


    @Override
    protected int setLayoutId() {
        mContext = this;
        return R.layout.act_question_detail;
    }

    @Override
    protected void onInitilizeView() {

        voicePlayUtil = new VoicePlayUtil();

        questionEntity = new QuestionEntity();
        actionBar = (ActionBar) findViewById(R.id.ab_question_detail_title);
        pcframLayout = (PtrClassicFrameLayout) findViewById(R.id.pcfl_questions_detail_layout);
        lvQuestionDetail = (ListView) findViewById(R.id.lv_questions_detail);

        answerAdapter = new AnswerAdapter(mContext);
        addQuestionContentView();

        //加载回答列表空白占位图
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View blankView = inflater.inflate(R.layout.item_question_detail_answer_blank_page, null);
        blankPage = (RelativeLayout) blankView.findViewById(R.id.rl_answer_blank_page);
        lvQuestionDetail.addFooterView(blankView);

        lvQuestionDetail.setAdapter(answerAdapter);
        //弹出loading框
        alertLoadingProgress(true);
        setListener();


    }


    @Override
    protected void onResume() {
        StatisticsUtil.getInstance(mContext).onCountPage("1.3.3");//自定义页面统计
        super.onResume();
        //弹出loading框
        alertLoadingProgress(true);
        refreshAll();
    }

    /**
     * 全部布局整体刷新
     */
    private void refreshAll() {
        currentPage = 0;
        loadQuestionDetail(questionId);

    }

    private void setListener() {
        actionBar.setOnClickListener(this);
        answerAdapter.setOnApprovalClickListener(this);
        pcframLayout.setPtrHandler(ptrDefaultHandler2);
        lvQuestionDetail.setOnItemClickListener(answerDetailItemClickListener);
        answerAdapter.setVoiceListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_left_layout:
                finishByResult();
                sendBroadcastToRefreshQuestion();
                finish();
                break;
            case R.id.btn_right_one://分享
                showShareActionSheetDailog();
                break;
            case R.id.btn_right_two://收藏
                if (questionEntity.is_favorite) {
                    senderDeleteFavoriteToServer(questionEntity.id, questionEntity.business_id);
                } else {
                    senderFavoriteToServer(questionEntity.id, questionEntity.business_id);
                }

                sendBroadcastToMyColleageRefresh();
                StatisticsUtil.getInstance(mContext).onCountActionDot("3.4.1");//自定义埋点统计
                break;

        }
    }

    /**
     * 发送广播去我的收藏刷新收藏列表的状态
     */
    private void sendBroadcastToMyColleageRefresh() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(MyAnswerColleageFragment.BROAD_ACTION_COLLEAGE_LIST_ENTITY, questionEntity);
        intent.putExtras(bundle);
        intent.setAction(MyAnswerColleageFragment.BROAD_ACTION_COLLEAGE_LIST_UPDATE);
        sendBroadcast(intent);
    }


    /**
     * 发送广播去社首页刷新问答列表的状态
     */
    private void sendBroadcastToRefreshQuestion() {
        Intent intent = new Intent();
        intent.putExtra(Constants.QUESTION, questionEntity);
        intent.setAction(Constants.ACTION_REFRESH_QUESTION_ELEMENT_STATUS);
        sendBroadcast(intent);
    }

    private void finishByResult() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.QUESTION, questionEntity);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
    }


    /**
     * 问题详情页的问题详情布局
     *
     * @return
     */
    public void addQuestionContentView() {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View questionContentView = inflater.inflate(R.layout.component_question_detail_content, null);
        civAvatar = (CircleImageView) questionContentView.findViewById(R.id.civ_avatar);
        tvNickname = (TextView) questionContentView.findViewById(R.id.tv_nickname);
        tvfromD = (TextView) questionContentView.findViewById(R.id.tv_from_d);//机构
        tvFromTopicName = (TextView) questionContentView.findViewById(R.id.tv_from_topic_name);//来自话题
        tvQuestionTitle = (TextView) questionContentView.findViewById(R.id.tv_title);
        tvTopicContent = (TextView) questionContentView.findViewById(R.id.tv_topic_content);
        tvCollectNumber = (TextView) questionContentView.findViewById(R.id.tv_collection);
        tvReplyNumber = (TextView) questionContentView.findViewById(R.id.tv_reply);
        tvPublishTime = (TextView) questionContentView.findViewById(R.id.tv_publish_time);
        questionContentView.findViewById(R.id.rl_text_answer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentEditActivity.class);
                intent.putExtra(Constants.COMMON_EDIT_TYPE, Constants.ANSWER);
                intent.putExtra(Constants.QUESTION_ID, questionId);
                mContext.startActivity(intent);
                StatisticsUtil.getInstance(mContext).onCountActionDot("3.4.4");//自定义埋点统计
            }
        });
        questionContentView.findViewById(R.id.rl_voice_answer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VoiceRecordDialogFragment voiceRecordDialogFragment = VoiceRecordDialogFragment.newInstance(questionId);
                voiceRecordDialogFragment.setRefreshFromDialogListener(new VoiceRecordDialogFragment.RefreshFromDialogListener() {
                    @Override
                    public void refresh() {
                        refreshAll();
                    }
                });
                voiceRecordDialogFragment.show(getSupportFragmentManager(), "");
                StatisticsUtil.getInstance(mContext).onCountActionDot("3.4.5");//自定义埋点统计
            }
        });
        lvQuestionDetail.addHeaderView(questionContentView);

    }


    /**
     * 加载问题详情数据
     *
     * @param questionId
     */
    private void loadQuestionDetail(final String questionId) {
        if (StringUtils.isBlank(questionId)) {
            return;
        }
        OkHttpSocialCircleImpl.getInstance().loadQuestionDetail(questionId, new RequestCallback<QuestionEntity>() {

            @Override
            public void onError(int errorCode, String errorMsg) {
                Trace.i("load question detail info failed ,info:" + errorMsg);
                loadAnswerListFromServer(questionId, currentPage, pageSize);
            }

            @Override
            public void onSuccess(QuestionEntity response) {
                Trace.i("load question detail info success");
                questionEntity = response;
                loadAnswerListFromServer(questionId, currentPage, pageSize);
            }
        });

    }


    /**
     * 分享弹出框
     */
    private void showShareActionSheetDailog() {
        final String path = "";//如果分享图片获取该图片的本地存储地址
        final String webPage = String.format(WebViewUrl.H5_SHARE_QUESTION, questionEntity.id);
        final String title = questionEntity.title;
        final String desc = questionEntity.content;

        shareActionSheetDialog = new ActionSheet(this);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.component_share_dailog, null);
        shareActionSheetDialog.setContentView(view);
        shareActionSheetDialog.show();


        LinearLayout llShareFriend = (LinearLayout) view.findViewById(R.id.ll_share_friend);
        LinearLayout llShareCircle = (LinearLayout) view.findViewById(R.id.ll_share_circle);
        TextView cancelButton = (TextView) view.findViewById(R.id.tv_cancel);

        llShareFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShareUtils.getInstance(mContext).sharePage(ShareUtils.SendToPlatformType.TO_FRIEND, webPage, path,
                        title, desc, UMPlatformData.UMedia.WEIXIN_FRIENDS);
                if (shareActionSheetDialog != null) {
                    shareActionSheetDialog.dismiss();
                }
                //统计具体问答分享的次数
                StatisticsUtil.getInstance(mContext).statisticsQuestionShareCount(questionId);
                //统计
                StatisticsUtil.getInstance(mContext).onCountActionDot("3.4.3");//自定义埋点统计

            }
        });

        llShareCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtils.getInstance(mContext).sharePage(ShareUtils.SendToPlatformType.TO_CIRCLE, webPage, path,
                        title, desc, UMPlatformData.UMedia.WEIXIN_CIRCLE);
                if (shareActionSheetDialog != null) {
                    shareActionSheetDialog.dismiss();
                }

                //统计具体问答分享的次数
                StatisticsUtil.getInstance(mContext).statisticsQuestionShareCount(questionId);

                StatisticsUtil.getInstance(mContext).onCountActionDot("3.4.2");//自定义埋点统计
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shareActionSheetDialog.isShowing()) {
                    shareActionSheetDialog.dismiss();
                }
            }
        });


    }


    /**
     * 刷新问题布局
     *
     * @param entity
     */
    private void refreshQuestionView(QuestionEntity entity) {

        if (entity == null) {
            return;
        }

        ImageLoader.getInstance().displayImage(entity.avatar, civAvatar, ImageLoaderOptions.option(R.drawable.pic_default_avatar));
        tvfromD.setText(entity.organization);
        tvNickname.setText(entity.nickname);
        tvFromTopicName.setText("来自话题: " + entity.fromtopic);

        if (StringUtils.isNotBlank(entity.title)) {

            if (entity.is_hot > 0) {
                Spanny spanny = new Spanny();
                spanny.append(" " + entity.title, new ImageSpan(mContext, R.drawable.icon_she_hot));
                tvQuestionTitle.setText(spanny);
            } else {
                tvQuestionTitle.setText(entity.title);
            }

        }
        tvTopicContent.setText(entity.content);
        tvCollectNumber.setText("收藏 " + entity.collect);
        tvReplyNumber.setText("回答 " + entity.reply);
        tvPublishTime.setText(DateUtils.fromTheCurrentTime(
                entity.publish_time * 1000l, System.currentTimeMillis()));

        if (entity.is_favorite) {
            actionBar.setRightImgeTwo(R.drawable.icon_collected);
        } else {
            actionBar.setRightImgeTwo(R.drawable.icon_collect_normal);
        }


    }


    AdapterView.OnItemClickListener answerDetailItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            startAnswerActivity(parent, position);

        }
    };


    private void startAnswerActivity(AdapterView<?> parent, int position) {
        if (position == 0) {
            return;
        }
//        Log.e("===", "===position:" + position + "====answerAdapter.getList():" + answerAdapter.getList().size());
        if (position > answerAdapter.getList().size()) {//点击尾部布局不跳转
            return;
        }

        AnswerEntity answerEntity = (AnswerEntity) parent.getItemAtPosition(position);
        answerEntity.questionTitleOfAnswer = tvQuestionTitle.getText().toString().trim();

        if (answerEntity.content_type_id == AnswerEntity.VOICE_REPLY_TYPE_VALUE) {
            return;
        }
        Intent intent = new Intent(mContext, AnswerDetailActivity.class);
        intent.putExtra(Constants.ANSWER, answerEntity);
        startActivityForResult(intent, Constants.APPROVAL_RESULT);
    }

    /**
     * 刷新监听
     */
    PtrDefaultHandler2 ptrDefaultHandler2 = new PtrDefaultHandler2() {
        @Override
        public void onLoadMoreBegin(PtrFrameLayout frame) {
            if (isMaxPage) {
                Toast.makeText(mContext, getString(R.string.data_load_completed), Toast.LENGTH_SHORT).show();
                pcframLayout.refreshComplete();//加载完毕
            } else {
                currentPage++;
                loadAnswerListFromServer(questionId, currentPage, pageSize);
            }
        }

        @Override
        public void onRefreshBegin(PtrFrameLayout frame) {
            currentPage = 0;
            refreshAll();
        }
    };


    /**
     * 加载回答列表
     *
     * @param questionId
     * @param currentPage
     * @param pageSize
     */
    public void loadAnswerListFromServer(String questionId, final int currentPage, int pageSize) {

        if (StringUtils.isBlank(questionId)) {
            return;
        }
        OkHttpSocialCircleImpl.getInstance().loadQuestionDetailAnswerList(questionId, pageSize, currentPage, new RequestCallback<QuestionDetailAnswerListDTO>() {


            @Override
            public void onError(int errorCode, String errorMsg) {
                pcframLayout.refreshComplete();
                refreshQuestionView(questionEntity);
                if (answerAdapter.getList().size() <= 0) {
                    blankPage.setVisibility(View.VISIBLE);
                }
                dismissLoadingDialog();

            }

            @Override
            public void onSuccess(QuestionDetailAnswerListDTO response) {
                QuestionDetailAnswerListDTO questionDetailAnswerListDTO = response;
                refreshQuestionView(questionEntity);
                refreshAnswersView(questionDetailAnswerListDTO);
                dismissLoadingDialog();
            }
        });
    }

    /**
     * 刷新回答列表
     *
     * @param questionDetailAnswerListDTO
     */
    private void refreshAnswersView(QuestionDetailAnswerListDTO questionDetailAnswerListDTO) {

        if (currentPage == 0) {
            answerAdapter.cleanList();
        }
        answerAdapter.modifyList(questionDetailAnswerListDTO.list);
        if (questionDetailAnswerListDTO.list.size() < pageSize) {
            isMaxPage = true;
        }

        if (answerAdapter.getList().size() <= 0) {
            blankPage.setVisibility(View.VISIBLE);
        } else {
            blankPage.setVisibility(View.GONE);
        }
        pcframLayout.refreshComplete();
    }


    @Override
    public void onApprovalClick(AnswerEntity entity, int position) {

        if (entity.is_approval) {
            entity.is_approval = false;
            entity.approval_count = entity.approval_count - 1;
            sendDeleteApprovalToServer(entity);
        } else {
            entity.is_approval = true;
            entity.approval_count = entity.approval_count + 1;
            sendApprovalToServer(entity);
        }

        StatisticsUtil.getInstance(mContext).onCountActionDot("3.5.1");//自定义埋点统计
    }


    /**
     * 收藏
     *
     * @param content_id
     * @param business_id
     */
    public void senderFavoriteToServer(String content_id, String business_id) {
        OkHttpSocialCircleImpl.getInstance().senderFavorite(content_id, business_id, new RequestCallback<ResultEntity>() {

            @Override
            public void onError(int errorCode, String errorMsg) {

            }

            @Override
            public void onSuccess(ResultEntity response) {
                actionBar.setRightImgeTwo(R.drawable.icon_collected);
                questionEntity.is_favorite = true;
                questionEntity.collect = questionEntity.collect + 1;
                tvCollectNumber.setText("收藏 " + questionEntity.collect);
                Toast.makeText(mContext, "收藏成功", Toast.LENGTH_SHORT).show();

            }
        });
    }

    /**
     * 取消收藏
     *
     * @param content_id
     * @param business_id
     */
    public void senderDeleteFavoriteToServer(String content_id, String business_id) {

        OkHttpSocialCircleImpl.getInstance().senderDeleteFavorite(content_id, business_id, new RequestCallback<ResultEntity>() {

            @Override
            public void onError(int errorCode, String errorMsg) {

            }

            @Override
            public void onSuccess(ResultEntity response) {
                actionBar.setRightImgeTwo(R.drawable.icon_collect_normal);
                questionEntity.is_favorite = false;
                questionEntity.collect = questionEntity.collect - 1;
                tvCollectNumber.setText("收藏 " + questionEntity.collect);
                Toast.makeText(mContext, "取消收藏成功", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * 点赞
     */
    public void sendApprovalToServer(final AnswerEntity entity) {

        OkHttpSocialCircleImpl.getInstance().senderApproval(entity.id, entity.business_id, new RequestCallback<ResultEntity>() {

            @Override
            public void onError(int errorCode, String errorMsg) {

            }

            @Override
            public void onSuccess(ResultEntity response) {

                List<AnswerEntity> answerList = answerAdapter.getList();
                for (int i = 0; i < answerList.size(); i++) {
                    if (entity.id.equals(answerList.get(i).id)) {
                        answerList.get(i).is_approval = entity.is_approval;
                        answerList.get(i).approval_count = entity.approval_count;
                    }
                }
                ToastUtil.getInstance(mContext).setContent("点赞成功").setDuration(Toast.LENGTH_SHORT).setShow();
                answerAdapter.notifyDataSetChanged();
            }
        });

    }

    /**
     * 取消点赞
     */
    public void sendDeleteApprovalToServer(final AnswerEntity entity) {

        OkHttpSocialCircleImpl.getInstance().senderDeleteApproval(entity.id, entity.business_id, new RequestCallback<ResultEntity>() {

            @Override
            public void onError(int errorCode, String errorMsg) {

            }

            @Override
            public void onSuccess(ResultEntity response) {

                List<AnswerEntity> answerList = answerAdapter.getList();
                for (int i = 0; i < answerList.size(); i++) {

                    if (entity.id.equals(answerList.get(i).id)) {
                        answerList.get(i).is_approval = entity.is_approval;
                        answerList.get(i).approval_count = entity.approval_count;
                    }
                }
                answerAdapter.notifyDataSetChanged();
                ToastUtil.getInstance(mContext).setContent("取消点赞成功").setDuration(Toast.LENGTH_SHORT).setShow();

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) {
            return;
        }
        Bundle bundle = data.getExtras();
        if (bundle == null) {
            return;
        }
        if (requestCode != Constants.APPROVAL_RESULT) {
            return;
        }
        AnswerEntity entity = (AnswerEntity) bundle.getSerializable(Constants.ANSWER);

        List<AnswerEntity> answerList = answerAdapter.getList();
        for (int i = 0; i < answerList.size(); i++) {

            if (entity.id.equals(answerList.get(i).id)) {
                answerList.get(i).is_approval = entity.is_approval;
                answerList.get(i).approval_count = entity.approval_count;
            }
        }
        answerAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (MediaPlayUtil.getInstance().isPlaying()) {
            voicePlayUtil.stopPlaying();
            animationDrawable.stop();
        }

        if (shareActionSheetDialog != null) {
            shareActionSheetDialog.dismiss();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        voicePlayUtil.stopPlaying();
    }

    @Override
    public void onPlay(View onClickView, TextView freshView, final ImageView animationView, final AnswerEntity entity, String fileStr, String fileName) {

        File file = new File(fileStr, fileName);
        //if  The file exist
        if (file.exists()) {
            voicePlayUtil.startPlaying(file.getAbsolutePath());
            animationView.setImageResource(R.drawable.voice_play_icon);
            animationDrawable = (AnimationDrawable) animationView.getDrawable();
            animationDrawable.start();
            voicePlayUtil.setPlayOnCompleteListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    animationDrawable.stop();
                    animationView.setImageResource(R.drawable.voice_play_icon);
                }
            });
            refreshVoiceAnswerStatus(entity);
            return;
        }


        OKHttpDownloadFileImpl.getInstance().downloadFile(entity.content, fileStr, fileName, new FileRequestCallBack() {

            @Override
            public void onError(int errorCode, String errorMsg) {
            }

            @Override
            public void onSuccess(File file) {
                Trace.i("success");
                String filePath = file.getAbsolutePath();
                voicePlayUtil.startPlaying(filePath);
                animationView.setImageResource(R.drawable.voice_play_icon);
                animationDrawable = (AnimationDrawable) animationView.getDrawable();
                animationDrawable.start();
                voicePlayUtil.setPlayOnCompleteListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        animationDrawable.stop();
                        animationView.setImageResource(R.drawable.voice_play_icon);
                    }
                });
                refreshVoiceAnswerStatus(entity);

            }

            @Override
            public void inProgress(float progress) {
                Trace.i("progress=" + progress);

            }
        });
        StatisticsUtil.getInstance(mContext).onCountActionDot("3.5.2");//自定义埋点统计

    }

    /**
     * 刷新语音按钮的状态
     *
     * @param entity
     */
    private void refreshVoiceAnswerStatus(AnswerEntity entity) {

        for (int i = 0; i < answerAdapter.getList().size(); i++) {

            if (((AnswerEntity) answerAdapter.getList().get(i)).id.equals(entity.id)) {
                ((AnswerEntity) answerAdapter.getList().get(i)).play_status = true;
            } else {
                ((AnswerEntity) answerAdapter.getList().get(i)).play_status = false;
            }

        }
        answerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        //返回前通知上一个页面刷新变动数据
        finishByResult();
        sendBroadcastToRefreshQuestion();
        super.onBackPressed();
    }



}
