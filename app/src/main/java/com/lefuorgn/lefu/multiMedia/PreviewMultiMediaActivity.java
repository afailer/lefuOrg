package com.lefuorgn.lefu.multiMedia;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.lefuorgn.AppConfig;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.LefuApi;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.util.TLog;
import com.lefuorgn.util.ToastUtils;

import java.io.File;

/**
 * 多媒体文件预览
 */

public class PreviewMultiMediaActivity extends Activity {

    public static final String MULTI_MEDIA_URI = "multi_media_uri";

    private VideoView mVideoView;
    private RelativeLayout mLoadingView;
    private TextView mProgressView;
    private File resultFile;
    private int rate;
    private int pRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_preview_multi_media);
        // 初始化播放控件
        mVideoView = (VideoView) findViewById(R.id.vv_activity_preview_multi_media);
        mLoadingView = (RelativeLayout) findViewById(R.id.rl_activity_preview_multi_media);
        mProgressView = (TextView) findViewById(R.id.tv_activity_preview_multi_media);
        // 获取要播放内容的地址
        String path = getIntent().getStringExtra(MULTI_MEDIA_URI);
        TLog.error("path == " + path);
        if(StringUtils.isEmpty(path)) {
            ToastUtils.show(this, "无效的地址");
            return;
        }
        // 播放相应的视频
        mVideoView.setMediaController(new MediaController(this));
        if(path.startsWith("http:")) {
            // 网络视频
            String localPath = getLocalPath(path);
            loadNetVideo(path, localPath);
        }else {
            // 本地视频
            mLoadingView.setVisibility(View.GONE);
            mVideoView.setVideoPath(path);
            mVideoView.start();
        }
    }

    /**
     * 加载网络视频
     * @param path 视频地址
     * @param localPath 要保存的地址
     */
    private void loadNetVideo(String path, String localPath) {
        LefuApi.downloadMultiMedia(path, localPath, new RequestCallback<File>() {
            @Override
            public void onSuccess(File result) {
                resultFile = result;
                TLog.error("下载成功");
                mLoadingView.setVisibility(View.GONE);
                mVideoView.setVideoPath(result.getAbsolutePath());
                mVideoView.start();
            }

            @Override
            public void onFailure(ApiHttpException e) {
                TLog.error("下载失败 " + e.toString());
                mLoadingView.setVisibility(View.GONE);
                ToastUtils.show(PreviewMultiMediaActivity.this, e.getMessage());
                finish();
            }

            @Override
            public void onLoading(long count, long current) {
                rate = (int) (current * 1.0f / count * 100);
                if(rate % 2 == 0 && rate != pRate) {
                    // 每下载10%的数据进行更新
                    pRate = rate;
                    // 更新进度
                    mProgressView.setText("正在下载 " + rate + "%");
                }
            }
        });
    }

    /**
     * 获取要保存本地的数据
     * @param path 视频网络地址
     */
    private String getLocalPath(String path) {
        // 判断是否存在sd卡
        boolean sdExist = android.os.Environment.MEDIA_MOUNTED
                .equals(android.os.Environment.getExternalStorageState());
        String name = getSaveFileName(path);
        if(sdExist) {
            // SD卡挂载
            return AppConfig.DEFAULT_SAVE_FILE_PATH + name;
        }else {
            // SD卡没有挂载
            return getDir("download", Context.MODE_PRIVATE).getAbsolutePath() + name;
        }

    }

    /**
     * 获取文件名称
     * @param downloadUrl 要下载文件的url
     * @return 文件名称
     */
    private String getSaveFileName(String downloadUrl) {
        if (downloadUrl == null || StringUtils.isEmpty(downloadUrl)) {
            return "";
        }
        return downloadUrl.substring(downloadUrl.lastIndexOf("/"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mVideoView != null) {
            mVideoView.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mVideoView != null) {
            mVideoView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mVideoView != null) {
            mVideoView.stopPlayback();
        }
        if(resultFile != null && resultFile.exists()) {
            resultFile.delete();
        }
    }
}
