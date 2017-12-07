package com.junhsue.ksee.fragment.dialog;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.junhsue.ksee.R;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.dto.SaveQNTokenDTO;
import com.junhsue.ksee.entity.ResultEntity;
import com.junhsue.ksee.net.api.OkHttpILoginImpl;
import com.junhsue.ksee.net.api.OkHttpQiniu;
import com.junhsue.ksee.net.api.OkHttpSocialCircleImpl;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.callback.VoiceCallBack;
import com.junhsue.ksee.net.url.RequestUrl;
import com.junhsue.ksee.utils.AudioRecorderUtils;
import com.junhsue.ksee.utils.ToastUtil;
import com.junhsue.ksee.utils.VoicePlayUtil;
import com.junhsue.ksee.view.TimerTextView;
import com.junhsue.ksee.view.VoiceDecibelLengthAnimView;

import java.io.File;

/**
 * 语音对话框
 * Created by Sugar on 17/6/14.
 */

public class VoiceRecordDialogFragment extends BaseDialogFragment {
    private Context mContext;
    private VoiceDecibelLengthAnimView mVoiceLengAnimView;
    private Button btnRecord;
    private AudioRecorderUtils mAudioRecorderUtils;
    //上传到七牛云的token
    private SaveQNTokenDTO saveQNTokenDTO;
    private String questionId;
    //语音播放工具
    private VoicePlayUtil recordVoicePlayUtil;
    private RefreshFromDialogListener fromDialogListener;
    // 播放路径
    private String voiceFilePath;
    private String voiceFileEndName;
    private byte[] voiceFileData;
    private TextView tvVoiceRemind;
    private TextView tvVoiceBack;
    private TextView tvVoiceSubmit;
    private long voiceLength;
    private TimerTextView playTimerTxt;
    private boolean isSend;

    public static VoiceRecordDialogFragment newInstance(String questionId) {
        VoiceRecordDialogFragment voiceRecordDialogFragment = new VoiceRecordDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.QUESTION_ID, questionId);
        voiceRecordDialogFragment.setArguments(bundle);
        return voiceRecordDialogFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.common_dialog_style);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.component_voice_dialog, null);
        //显示动画
        getDialog().getWindow().setWindowAnimations(R.style.ActionSheetStyle);
        getDialog().setCanceledOnTouchOutside(true);
        questionId = (String) getArguments().get(Constants.QUESTION_ID);
        initView(view);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    /**
     * 初始化数据
     *
     * @param view
     */
    private void initView(View view) {

        mAudioRecorderUtils = new AudioRecorderUtils();
        recordVoicePlayUtil = new VoicePlayUtil();
        btnRecord = (Button) view.findViewById(R.id.btn_record);
        mVoiceLengAnimView = (VoiceDecibelLengthAnimView) view.findViewById(R.id.voice_length);
        playTimerTxt = (TimerTextView) view.findViewById(R.id.tv_voice_length);
        btnRecord = (Button) view.findViewById(R.id.btn_record);
        tvVoiceRemind = (TextView) view.findViewById(R.id.tv_voice_remind);
        tvVoiceBack = (TextView) view.findViewById(R.id.tv_voice_back);
        tvVoiceSubmit = (TextView) view.findViewById(R.id.tv_voice_submit);

        getTokenFromQN();
        //6.0以上需要权限申请
//        requestPermissions();
        setListener();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    /**
     * 设置监听
     */
    public void setListener() {

        //录音回调
        mAudioRecorderUtils.setOnAudioStatusUpdateListener(new AudioRecorderUtils.OnAudioStatusUpdateListener() {

            //录音中....db为声音分贝，time为录音时长
            @Override
            public void onUpdate(double db, long time) {
                voiceLength = time;
                Log.i("voice", "db:" + (int) db / 10);
                if (db / 10 > 4) {
                    mVoiceLengAnimView.setResetTVHeight(((int) db / 10 - 4) * 5);
                }
            }

            //录音结束，filePath为保存路径
            @Override
            public void onStop(String filePath, String fileEndName, byte[] data) {
                voiceFilePath = filePath;
                voiceFileEndName = fileEndName;
                voiceFileData = data;
                //录音结束后,重置播放和录音的UI
                mVoiceLengAnimView.setVisibility(View.GONE);
                playTimerTxt.setVisibility(View.VISIBLE);

            }
        });


        //初始化按钮状态
        btnRecord.setTag("0");
        //Button的touch监听
        btnRecord.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        //记得处理时间过短
                        if ("0".equals(btnRecord.getTag())) {
                            tvVoiceRemind.setText(mContext.getResources().getString(R.string.msg_voice_stop_record));
                            mAudioRecorderUtils.startRecord();
                            mVoiceLengAnimView.startCountdownTimerTask();
                            mVoiceLengAnimView.setVisibility(View.VISIBLE);
                            playTimerTxt.setVisibility(View.GONE);
                            btnRecord.setBackgroundResource(R.drawable.icon_answer_sound_press);
                        } else if ("1".equals(btnRecord.getTag())) {
                            //播放
                            Log.e("===", "===播放");
                            File file = new File(voiceFilePath);
                            if (file.exists()) {
                                recordVoicePlayUtil.startPlaying(voiceFilePath);//启动播放
                                playTimerTxt.startTimerTask();//播放记时
                                //播放结束后,重置语音按钮状态
                                recordVoicePlayUtil.setPlayOnCompleteListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mp) {
                                        Log.e("===", "====播放结束了");
                                        btnRecord.setBackgroundResource(R.drawable.icon_answer_voice_record_play);
                                        btnRecord.setTag("1");
                                    }
                                });


                            }
                        } else if ("2".equals(btnRecord.getTag())) {
                            //暂停
                            Log.e("===", "===暂停");
                            if (recordVoicePlayUtil.getMediaPlayer() != null) {
                                recordVoicePlayUtil.stopPlaying();
                                playTimerTxt.stopTask();//播放暂停后就重置播放时长
                                int timeTemp = dealwithVoiceLength(voiceLength);
                                playTimerTxt.setMaxTimerLength(timeTemp);
                            }
                        }

                        break;

                    case MotionEvent.ACTION_UP:
                        if ("0".equals(btnRecord.getTag())) {
                            if (voiceLength <= AudioRecorderUtils.LIMIT_VOICE_LENGTH) {
                                mAudioRecorderUtils.stopRecord();        //结束录音（保存录音文件）
                                mVoiceLengAnimView.stopTask();
                                mVoiceLengAnimView.setMaxCount(60);
                                tvVoiceRemind.setText(mContext.getResources().getString(R.string.msg_voice_start_record));
                                btnRecord.setBackgroundResource(R.drawable.icon_answer_sound_normal);
                                btnRecord.setTag("0");
                                Toast.makeText(mContext, "录音时间过短", Toast.LENGTH_SHORT).show();
                            } else {
                                mAudioRecorderUtils.stopRecord();    //结束录音（保存录音文件）
                                mVoiceLengAnimView.stopTask();      //停止录音动画

                                int timeTemp = dealwithVoiceLength(voiceLength);
                                playTimerTxt.setMaxTimerLength(timeTemp);
                                mVoiceLengAnimView.setVisibility(View.INVISIBLE);
                                playTimerTxt.setVisibility(View.VISIBLE);
                                tvVoiceRemind.setText("");
                                btnRecord.setBackgroundResource(R.drawable.icon_answer_voice_record_play);
                                btnRecord.setTag("1");
                            }
                        } else if ("1".equals(btnRecord.getTag())) {
                            btnRecord.setBackgroundResource(R.drawable.icon_answer_voice_record_stop);
                            btnRecord.setTag("2");
                        } else if ("2".equals(btnRecord.getTag())) {

//                            int timeTemp=dealwithVoiceLength(voiceLength);
//                            playTimerTxt.setMaxTimerLength(timeTemp);
                            btnRecord.setBackgroundResource(R.drawable.icon_answer_voice_record_play);
                            btnRecord.setTag("1");
                        }

                        break;
                }
                return true;
            }
        });

//        //播放结束后,重置语音按钮状态
//        recordVoicePlayUtil.setPlayOnCompleteListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                Log.e("===","====播放结束了");
//                btnRecord.setBackgroundResource(R.drawable.icon_answer_voice_record_play);
//                btnRecord.setTag("1");
//            }
//        });

        mVoiceLengAnimView.setRecordVoiceFinishListener(new VoiceDecibelLengthAnimView.RecordVoiceFinishListener() {
            @Override
            public void recordVoiceEnd() {
                mAudioRecorderUtils.stopRecord();
                btnRecord.setBackgroundResource(R.drawable.icon_answer_voice_record_play);
                btnRecord.setTag("1");
            }
        });


        tvVoiceBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //录音取消
                //播放取消
                if (recordVoicePlayUtil.getMediaPlayer() != null) {
                    recordVoicePlayUtil.stopPlaying();
                }
                dismiss();
            }
        });

        tvVoiceSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSend) {
                    ToastUtil.showToast(mContext, "哎呦,戳太快了,已经在发送途中了哦!");
                    return;
                }
                //预处理
                if (voiceLength <= AudioRecorderUtils.LIMIT_VOICE_LENGTH) {
                    Toast.makeText(mContext, "录音时长过短或未录音,不能发送哦!", Toast.LENGTH_LONG).show();
                    return;
                }

                if (recordVoicePlayUtil.isPlaying()) {//如果在播放状态下发送,需要关闭播放再发送。
                    recordVoicePlayUtil.stopPlaying();
                }

                isSend = true;
                uploadToQNServer(voiceFileEndName, voiceFileData);
            }
        });


    }

    /**
     * 时间处理
     *
     * @param voiceLength
     * @return
     */
    private int dealwithVoiceLength(long voiceLength) {

        int tempTime = 0;
        if (voiceLength % 1000 == 0) {
            tempTime = (int) (voiceLength / 1000);
        } else if (voiceLength % 1000 > 0) {
            tempTime = ((int) (voiceLength / 1000) + 1);
        }

        return tempTime;
    }


    /**
     * 上传语音到七牛云服务器
     *
     * @param fileEndName
     * @param data
     */
    private void uploadToQNServer(String fileEndName, byte[] data) {
        //上传语音到七牛云存储
        OkHttpQiniu.getInstance().uploadVoice(mContext, new VoiceCallBack() {
            @Override
            public void uploadSuccess(String key) {

                String url = RequestUrl.BASE_QINIU_API + key;
                senderVoiceAnswer(url);

            }

            @Override
            public void uploadFailed(String info) {

            }
        }, data, fileEndName, saveQNTokenDTO.token);
    }


    /**
     * 获取token
     */
    private void getTokenFromQN() {
        OkHttpILoginImpl.getInstance().GetQNToken(new RequestCallback<SaveQNTokenDTO>() {
            @Override
            public void onError(int errorCode, String errorMsg) {
                Trace.i("vetifyCode send failed info :" + errorMsg);
            }

            @Override
            public void onSuccess(final SaveQNTokenDTO response) {
                Trace.i("发送成功:response:" + response.token);
                saveQNTokenDTO = response;

            }
        });
    }


    /**
     * 语音回答发布
     *
     * @param url 语音在七牛云的存储地址
     */
    public void senderVoiceAnswer(String url) {

        int timeTemp = dealwithVoiceLength(voiceLength);

        OkHttpSocialCircleImpl.getInstance().loadAnswerReplay(questionId, url, 2, timeTemp + "", new RequestCallback<ResultEntity>() {
            @Override
            public void onError(int errorCode, String errorMsg) {
                Toast.makeText(mContext, "发送失败", Toast.LENGTH_SHORT).show();
                isSend = false;
            }

            @Override
            public void onSuccess(ResultEntity response) {
                Toast.makeText(mContext, "发送成功", Toast.LENGTH_SHORT).show();
                fromDialogListener.refresh();
                dismiss();
                isSend = false;

            }
        });
    }

    public interface RefreshFromDialogListener {
        void refresh();

    }

    public void setRefreshFromDialogListener(RefreshFromDialogListener dialogListener) {

        this.fromDialogListener = dialogListener;

    }

}
