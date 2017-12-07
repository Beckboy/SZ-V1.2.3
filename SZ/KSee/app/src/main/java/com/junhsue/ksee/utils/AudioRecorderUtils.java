package com.junhsue.ksee.utils;

import android.media.MediaRecorder;
import android.os.Handler;
import android.util.Log;

import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.file.FileUtil;

import java.io.File;
import java.io.IOException;

/**
 * 录音工具类
 * Created by fan on 2016/6/23.
 */
public class AudioRecorderUtils {

    private final String TAG = "AudioRecorderUtils";
    //文件路径
    private String filePath;
    //文件夹路径
    private String FolderPath;
    //每个文件的后缀尾行拼接名字
    private String fileKeyName;

    private MediaRecorder mMediaRecorder;
    // 振幅基值单位
    private int BASE = 1;
    // 间隔取样时间
    private int SPACE = 100;
    //录音启动时间
    private long startTime;
    //录音结束时间
    private long endTime;
    // 最小录音时长2000毫秒
    public static final int LIMIT_VOICE_LENGTH = 2000;
    // 最大录音时长1000*60*10
    public static final int MAX_LENGTH = 1000 * 60;


    //录音状态监听
    private OnAudioStatusUpdateListener audioStatusUpdateListener;

    /**
     * 文件存储默认sdcard/record
     */
    public AudioRecorderUtils() {

        //默认保存路径为/sdcard/voice_record/下
        this(FileUtil.ROOTPATH + Constants.VOICE_SAVE_FILE);
    }

    public AudioRecorderUtils(String filePath) {

        File path = new File(filePath);
        if (!path.exists())
            path.mkdirs();

        this.FolderPath = filePath;
    }


    /**
     * 开始录音 使用amr格式
     * 录音文件
     *
     * @return
     */
    public void startRecord() {
        // 开始录音
        /* ①Initial：实例化MediaRecorder对象 */
        if (mMediaRecorder == null)
            mMediaRecorder = new MediaRecorder();
        try {
            /* ②setAudioSource/setVedioSource */
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 设置麦克风
            /* ②设置音频文件的编码：AAC/AMR_NB/AMR_MB/Default 声音的（波形）的采样 */
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            fileKeyName = DateUtils.getCurrentTime() + ".amr";
            filePath = FolderPath + fileKeyName;
            /* ③准备 */
            mMediaRecorder.setOutputFile(filePath);
            /*
             * ②设置输出文件的格式：THREE_GPP/MPEG-4/RAW_AMR/Default THREE_GPP(3gp格式
             * ，H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
             */
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mMediaRecorder.setAudioChannels(1);
            mMediaRecorder.setAudioSamplingRate(8000);
//            mMediaRecorder.setAudioEncodingBitRate(16);

            mMediaRecorder.setMaxDuration(MAX_LENGTH);
            mMediaRecorder.prepare();
            /* ④开始 */
            mMediaRecorder.start();
            // AudioRecord audioRecord.
            /* 获取开始时间* */
            startTime = System.currentTimeMillis();
            updateMicStatus();
            Log.e("fan", "startTime" + startTime);
        } catch (IllegalStateException e) {
            Log.i(TAG, "call startAmr(File mRecAudioFile) failed!" + e.getMessage());
        } catch (IOException e) {
            Log.i(TAG, "call startAmr(File mRecAudioFile) failed!" + e.getMessage());
        }
    }

    /**
     * 停止录音
     */
    public long stopRecord() {
        if (mMediaRecorder == null)
            return 0L;

        endTime = System.currentTimeMillis();

        try {

            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;

            byte[] data = FileFormatUtils.getBytes(filePath);//字节数组转换

            if (data != null && audioStatusUpdateListener != null && fileKeyName != null) {
                audioStatusUpdateListener.onStop(filePath, fileKeyName, data);
            }

            filePath = "";  //清空

        } catch (RuntimeException e) {

            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;

            File file = new File(filePath);
            if (file.exists())
                file.delete();

            filePath = "";

        }

        return endTime - startTime;
    }

    /**
     * 取消录音
     */
    public void cancelRecord() {

        try {

            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;

        } catch (RuntimeException e) {

            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }

        File file = new File(filePath);
        if (file.exists())//需优化
            file.delete();

        filePath = "";
        fileKeyName = "";

    }

    private final Handler mHandler = new Handler();

    private Runnable mUpdateMicStatusTimer = new Runnable() {
        public void run() {
            updateMicStatus();
        }
    };


    /**
     * 更新麦克状态
     */
    private void updateMicStatus() {

        if (mMediaRecorder != null) {
            double ratio = (double) mMediaRecorder.getMaxAmplitude() / BASE;
            double db = 0;// 分贝
            if (ratio > 1) {
                db = 20 * Math.log10(ratio);
                if (null != audioStatusUpdateListener) {
                    audioStatusUpdateListener.onUpdate(db, System.currentTimeMillis() - startTime);
                }
            }
            mHandler.postDelayed(mUpdateMicStatusTimer, SPACE);
        }
    }

    public void setOnAudioStatusUpdateListener(OnAudioStatusUpdateListener audioStatusUpdateListener) {
        this.audioStatusUpdateListener = audioStatusUpdateListener;
    }

    /**
     * 录音状态监听接口
     */
    public interface OnAudioStatusUpdateListener {
        /**
         * 录音中...
         *
         * @param db   当前声音分贝
         * @param time 录音时长
         */
        public void onUpdate(double db, long time);

        /**
         * 停止录音
         *
         * @param filePath 保存路径
         */
        public void onStop(String filePath, String fileEndName, byte[] data);
    }

}
