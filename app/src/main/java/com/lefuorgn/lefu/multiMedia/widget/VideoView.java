package com.lefuorgn.lefu.multiMedia.widget;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.lefuorgn.AppConfig;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.util.TLog;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;


/**
 * 拍照页面
 */

public class VideoView extends SurfaceView implements SurfaceHolder.Callback, OnErrorListener {


    private Camera mCamera;
    private MediaRecorder mMediaRecorder;
    private Callback mCallback;

    private int mMaxTime;
    private int mCurrentTime;

    private float mDownX, mDownY;
    // 保存视频文件的路径
    private String mImgPath;
    private Timer mTimer; // 计时器

    private boolean stop; // 记录结束录制并返回uri的状态

    /**
     * 聚焦接口回调
     */
    private Camera.AutoFocusCallback mAutoFocusCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            // 不做任何处理
        }
    };

    public VideoView(Context context) {
        super(context);
        init();
    }

    public VideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        getHolder().addCallback(this);
        setFocusable(true);
        setClickable(true);
    }

    /**
     * 开始录制
     */
    public void startRecord() {
        if(mCallback != null) {
            mCallback.startRecord();
        }
        initMediaRecorder(getHolder());
        try {
            // 开启录制并计时
            mMediaRecorder.prepare();
            mMediaRecorder.start();
            startTiming();
        } catch (Exception e) {
            if(mCallback != null) {
                mCallback.error(e);
            }
            e.printStackTrace();
        }
    }

    /**
     * 结束录制
     */
    public void stopRecord() {
        stop = true;
        // 结束计时, 并停止视频录制
        endTime();
        if(mMediaRecorder != null) {
            mMediaRecorder.setOnErrorListener(null);
            mMediaRecorder.setPreviewDisplay(null);
            mMediaRecorder.stop();
            mMediaRecorder.release();
        }
        if(mCallback != null) {
            mCallback.endRecord(mImgPath);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // 当Surface被创建之后，开始Camera的预览
        initCamera(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // 在预览前可以指定Camera的各项参数
        // 重新开始预览
        try {
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            List<Size> sizes = parameters.getSupportedPictureSizes();
            Size size = getOptimalPreviewSize(sizes, width, height);
            parameters.setPictureSize(size.width, size.height);
            mCamera.setParameters(parameters);
            mCamera.startPreview();
        } catch (Exception e){
            TLog.log(e.toString());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // 停止时间计时
        endTime();
        if(!stop) {
            if(mMediaRecorder != null) {
                // 非正常视频录制, 停止视频录制
                try {
                    mMediaRecorder.stop();
                } catch (IllegalStateException e) {
                    // 防止未开始而就调用stop而报错
                }
            }
            if(!StringUtils.isEmpty(mImgPath)) {
                // 如果已经开始录制, 则删除已经录制的视频
                File file = new File(mImgPath);
                if(file.exists()) {
                    file.delete();
                    mImgPath = "";
                }
            }
            if(mCallback != null) {
                // 重置状态
                mCallback.reset();
            }
        }
        // 释放相机
        releaseCameraResource();
    }

    private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.05;
        double targetRatio = (double) w / h;
        if (sizes == null)
            return null;
        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;
        // Try to find an size match aspect ratio and size
        for (Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                continue;
            if (Math.abs(size.height - h) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - h);
            }
        }
        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Size size : sizes) {
                if (Math.abs(size.height - h) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - h);
                }
            }
        }
        return optimalSize;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                break;

            case MotionEvent.ACTION_UP:
                float upX = event.getX();
                float upY = event.getY();
                if(Math.abs(upX - mDownX) < 50 && Math.abs(upY - mDownY) < 50) {
                    focus(mAutoFocusCallback);
                }
                break;
        }
        return true;
    }

    /**
     * 聚焦回调
     * @param callback 接口回调
     * @return true: 聚焦函数执行; false: 聚焦函数执行失败
     */
    private boolean focus(Camera.AutoFocusCallback callback) {
        try {
            mCamera.autoFocus(callback);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 开始计时
     */
    private void startTiming() {
        mCurrentTime = -1;
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mCurrentTime++;
                if(mCurrentTime > mMaxTime) {
                    stopRecord();
                }
                if(mCallback != null) {
                    mCallback.progress(mCurrentTime, mMaxTime);
                }

            }
        }, 0, 1000);
    }

    /**
     * 取消计时
     */
    private void endTime() {
        if(mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    /**
     * 设置视频录制的最大时间
     * @param maxTime 最大时间, 默认是10s
     */
    public void setMaxTime(int maxTime) {
        this.mMaxTime = maxTime;
    }

    /**
     * 添加聚焦监听
     * @param callback 接口回调
     */
    public void addCallback(Callback callback) {
        this.mCallback = callback;
    }

    /**
     * 监听拍照聚焦的状态
     */
    public interface Callback {

        /**
         * 开始录制视频
         */
        void startRecord();

        /**
         * 当前录制进度
         * @param current 当前录制时间
         * @param total 总共录制时间
         */
        void progress(int current, int total);

        /**
         * 录制视频结束
         * @param path 录制视频路径
         */
        void endRecord(String path);

        /**
         * 重置当前状态
         */
        void reset();

        /**
         * 录制失败
         */
        void error(Exception e);

    }

    /**
     * 初始化录像功能
     * @param holder surface创建的SurfaceHolder对象
     */
    private void initCamera(SurfaceHolder holder) {
        // 如果Camera存在则先释放
        try {
            // 打开拍照功能
            mCamera = Camera.open();
            mCamera.setPreviewDisplay(holder);
            // 拍照方向旋转90度
            mCamera.setDisplayOrientation(90);
            mCamera.startPreview();
            // 首次创建的时候, 进行一次聚焦
            mCamera.autoFocus(mAutoFocusCallback);
        } catch (IOException e) {
            TLog.log("预览失败");
        }
    }

    /**
     * 释放拍照资源
     */
    private void releaseCameraResource() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * 初始化MediaRecorder,基于Camera的, 因此它是在创建Camera之后创建
     * @param holder surface创建的SurfaceHolder对象
     */
    private void initMediaRecorder(SurfaceHolder holder) {
        if(mMediaRecorder == null) {
            mMediaRecorder = new MediaRecorder();
            mMediaRecorder.setOnErrorListener(this);
        }else {
            mMediaRecorder.reset();
        }
        // Unlock and set camera to MediaRecorder
        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);
        mMediaRecorder.setPreviewDisplay(holder.getSurface());
        // 从相机采集视频
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        // 从麦克采集音频信息
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        // 设置视频输出的格式和编码(MP4)
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        // 编码格式 after setOutputFormat()
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        // 获取摄像头的配置文件
        if(CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_TIME_LAPSE_480P)) {
            // 存在给定级别下的一个摄像机配置文件
            TLog.error("存在给定级别下的一个摄像机配置文件");
            CamcorderProfile mProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_TIME_LAPSE_480P);
            // 设置录制视频的宽和高
            mMediaRecorder.setVideoSize(mProfile.videoFrameWidth, mProfile.videoFrameHeight);
            if (mProfile.videoBitRate > 2 * 1024 * 1024) {
                mMediaRecorder.setVideoEncodingBitRate(2 * 1024 * 1024);
            } else {
                mMediaRecorder.setVideoEncodingBitRate(mProfile.videoBitRate);
            }
            // 每秒的帧数
            mMediaRecorder.setVideoFrameRate(mProfile.videoFrameRate);
            mMediaRecorder.setAudioEncodingBitRate(44100);
        }else {
            // 不存在给定级别下的一个摄像机配置文件
            TLog.error("不存在给定级别下的一个摄像机配置文件");
            mMediaRecorder.setVideoSize(320, 240);
            mMediaRecorder.setAudioEncodingBitRate(44100);
            mMediaRecorder.setVideoEncodingBitRate(1280 * 720);
            mMediaRecorder.setVideoFrameRate(24);
        }
        // 输出旋转90度，保持竖屏录制
        mMediaRecorder.setOrientationHint(90);
        // 设置输出路径
        mImgPath = getOutputFilePath();
        TLog.error("视频保存路径 == " + mImgPath);
        mMediaRecorder.setOutputFile(mImgPath);
    }

    /**
     * 获取视频保存的文件路径
     * @return 绝对路径
     */
    private String getOutputFilePath() {
        // 判断是否存在sd卡
        boolean sdExist = android.os.Environment.MEDIA_MOUNTED
                .equals(android.os.Environment.getExternalStorageState());
        // 生成保存视频的路径
        File fFolder;
        if(sdExist) {
            // SD卡挂载
            TLog.error("SD卡搭载...");
            fFolder = new File(AppConfig.DEFAULT_SAVE_VIDEO_PATH);
        }else {
            // SD卡没有挂载
            TLog.error("SD卡未搭载...");
            String path = getContext().getDir("lefu_video", Context.MODE_PRIVATE).getAbsolutePath();
            fFolder = new File(path);
        }
        if(!fFolder.exists()) {
            // 文件夹不存在
            if(!fFolder.mkdirs()) {
                TLog.error("文件夹创建失败...");
                // 文件夹创建失败
                return "";
            }
        }
        // 创建图片File
        File fImage = new File(fFolder, UUID.randomUUID().toString() + ".3gp");
        return fImage.getAbsolutePath();
    }

    @Override
    public void onError(MediaRecorder mr, int what, int extra) {
        if(mr != null) {
            mr.reset();
        }
    }
}
