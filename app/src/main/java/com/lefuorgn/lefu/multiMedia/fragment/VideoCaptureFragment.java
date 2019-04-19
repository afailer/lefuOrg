package com.lefuorgn.lefu.multiMedia.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lefuorgn.R;
import com.lefuorgn.base.BaseFragment;
import com.lefuorgn.lefu.multiMedia.MultiMediaActivity;
import com.lefuorgn.lefu.multiMedia.widget.VideoView;
import com.lefuorgn.util.StringUtils;

/**
 * 视频录制页面
 */

public class VideoCaptureFragment extends BaseFragment {

    private static final int MAX_TIME = 7; // 录制视频的最大时间
    private boolean start;
    private TextView mButton;
    private VideoView mVideoView;
    private ProgressBar mProgressBar;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_multi_media_video;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mVideoView = (VideoView) view.findViewById(R.id.vv_fragment_multi_media_video);
        mButton = (TextView) view.findViewById(R.id.btn_fragment_multi_media_video);
        mProgressBar = (ProgressBar) view.findViewById(R.id.pb_fragment_multi_media_video);
        mButton.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        start = true;
        mVideoView.setMaxTime(MAX_TIME);
        mVideoView.addCallback(new VideoView.Callback() {
            @Override
            public void startRecord() {
                mProgressBar.setMax(MAX_TIME);
            }

            @Override
            public void progress(int current, int total) {
                mProgressBar.setProgress(current);
            }

            @Override
            public void endRecord(String path) {
                if(StringUtils.isEmpty(path)) {
                    showToast("视频录制失败");
                    getActivity().setResult(0);
                }else {
                    Intent intent = new Intent();
                    intent.putExtra(MultiMediaActivity.RESULT_NAME, path);
                    getActivity().setResult(MultiMediaActivity.RESULT_VIDEO_CAPTURE, intent);
                }
                getActivity().finish();
            }

            @Override
            public void reset() {
                mProgressBar.setProgress(0);
                mButton.setText("开始录制");
                start = true;
            }

            @Override
            public void error(Exception e) {
                showToast("录制失败......");
                getActivity().finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(start) {
            mVideoView.startRecord();
            mButton.setText("停止录制");
            start = false;
        }else {
            mVideoView.stopRecord();
            mButton.setText("结束");
            mButton.setOnClickListener(null);
        }
    }

}
