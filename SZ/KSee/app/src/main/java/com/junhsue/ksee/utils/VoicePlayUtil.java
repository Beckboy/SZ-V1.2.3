package com.junhsue.ksee.utils;

import android.media.MediaPlayer;
import android.util.Log;

import com.junhsue.ksee.common.Trace;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 播放工具类
 * Created by Sugar on 17/5/24.
 */

public class VoicePlayUtil {

    private MediaPlayer mPlayer = null;

    public VoicePlayUtil() {
        mPlayer = new MediaPlayer();
    }

    /**
     * 播放
     *
     * @param mFileName
     */
    public void startPlaying(String mFileName) {
        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
        }
        try {

            mPlayer.reset();
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Trace.d( "mPlayer,prepare() failed");
        }
    }

    /**
     * 停止播放
     */
    public void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }


    /**
     * 获得播放器
     *
     * @return
     */
    public MediaPlayer getMediaPlayer() {
        if (mPlayer != null) {
            return mPlayer;
        }
        return null;
    }


    /**
     * 暂停播放
     */
    public void pause() {
        if (mPlayer != null) {
            mPlayer.pause();
        }
    }

    /**
     * 停止播放
     */
    public void stop() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
        }
    }

    /**
     * 获得播放当前位置
     *
     * @return
     */
    public int getCurrentPosition() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            return mPlayer.getCurrentPosition();
        } else {
            return 0;
        }
    }

    /**
     * 获得播放时长
     *
     * @return
     */
    public int getDuration() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            return mPlayer.getDuration();
        } else {
            return 0;
        }
    }

    /**
     * 是否处于播放状态
     *
     * @return
     */
    public boolean isPlaying() {
        if (mPlayer != null) {
            return mPlayer.isPlaying();
        } else {
            return false;
        }
    }


    /**
     * 设置播放完成监听
     *
     * @param playOnCompleteListener
     */
    public void setPlayOnCompleteListener(MediaPlayer.OnCompletionListener playOnCompleteListener) {
        if (mPlayer != null) {
            mPlayer.setOnCompletionListener(playOnCompleteListener);
        }
    }

}
