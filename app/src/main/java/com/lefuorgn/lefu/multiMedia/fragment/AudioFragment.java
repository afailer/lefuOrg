package com.lefuorgn.lefu.multiMedia.fragment;

import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

import com.lefuorgn.AppConfig;
import com.lefuorgn.R;
import com.lefuorgn.base.BaseFragment;
import com.lefuorgn.lefu.multiMedia.MultiMediaActivity;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.util.TLog;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 音频录制页面
 */

public class AudioFragment extends BaseFragment {

    private Chronometer mChronometer;
    private TextView mButton;
    // 保存音频的路径文件
    private String mAudioUrl;

    private boolean start; // 是否是未开始状态
    private boolean stop; // 是否是正常停止录制状态
    private MediaRecorder mMediaRecorder;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_multi_media_audio;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mChronometer = (Chronometer) view.findViewById(R.id.c_fragment_multi_media_audio);
        mButton = (TextView) view.findViewById(R.id.btn_fragment_multi_media_audio);
        mButton.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        start = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        if(!stop) {
            // 非正常退出
            stopRecorder();
            mButton.setText("开始录制");
            start = true;
            if(!StringUtils.isEmpty(mAudioUrl)) {
                // 如果已经开始录制, 则删除已经录制的视频
                File file = new File(mAudioUrl);
                if(file.exists()) {
                    file.delete();
                    mAudioUrl = "";
                }
            }
        }
    }

    /**
     * 开始录音
     */
    private void startRecorder() {
        if(mMediaRecorder == null) {
            mMediaRecorder = new MediaRecorder();
        }else {
            mMediaRecorder.reset();
        }
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mAudioUrl = getAudioPath();
        TLog.error("音频保存路径 == " + mAudioUrl);
        mMediaRecorder.setOutputFile(mAudioUrl);
        try {
            mMediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaRecorder.start();
        // 开始计时
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.start();
    }

    /**
     * 创建音频路径
     * @return 音频的存储路径
     */
    private String getAudioPath() {
        // 判断是否存在sd卡
        boolean sdExist = android.os.Environment.MEDIA_MOUNTED
                .equals(android.os.Environment.getExternalStorageState());
        // 生成保存音频的路径
        File fFolder;
        if(sdExist) {
            // SD卡挂载
            TLog.error("SD卡搭载...");
            fFolder = new File(AppConfig.DEFAULT_SAVE_AUDIO_PATH);
        }else {
            // SD卡没有挂载
            TLog.error("SD卡未搭载...");
            String path = getContext().getDir("lefu_audio", Context.MODE_PRIVATE).getAbsolutePath();
            fFolder = new File(path);
        }
        if(!fFolder.exists()) {
            // 文件夹不存在
            if(!fFolder.mkdirs()) {
                // 文件夹创建失败
                return "";
            }
        }
        // 创建图片File
        File fImage = new File(fFolder, UUID.randomUUID().toString() + ".3gp");
        return fImage.getAbsolutePath();
    }

    /**
     * 停止录音
     */
    private void stopRecorder() {
        try {
            if(mMediaRecorder != null) {
                mMediaRecorder.stop();
            }
            if(mChronometer != null) {
                mChronometer.stop();
            }
        }catch (IllegalStateException e) {
            // 没有开始就stop会报错
        }
    }

    @Override
    public void onClick(View v) {
        if(start) {
            mButton.setText("停止录制");
            start = false;
            startRecorder();
        }else {
            stopRecorder();
            start = true;
            stop = true;
            mButton.setText("结束");
            mButton.setOnClickListener(null);
            if(StringUtils.isEmpty(mAudioUrl)) {
                showToast("视频录制失败");
                getActivity().setResult(0);
            }else {
                Intent intent = new Intent();
                intent.putExtra(MultiMediaActivity.RESULT_NAME, mAudioUrl);
                getActivity().setResult(MultiMediaActivity.RESULT_AUDIO, intent);
            }
            getActivity().finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mMediaRecorder != null) {
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
        mChronometer = null;
    }

}
