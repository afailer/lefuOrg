package com.lefuorgn.lefu.multiMedia.widget;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.lefuorgn.util.TLog;

import java.io.IOException;
import java.util.List;


/**
 * 拍照页面
 */

public class PhotographView extends SurfaceView implements SurfaceHolder.Callback {

    private Camera mCamera;
    private Callback mCallback;

    private float mDownX, mDownY;

    /**
     * 聚焦接口回调
     */
    private Camera.AutoFocusCallback mAutoFocusCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            if(mCallback != null) {
                mCallback.endFocus(success);
            }
        }
    };

    public PhotographView(Context context) {
        super(context);
        init();
    }

    public PhotographView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhotographView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        getHolder().addCallback(this);
        setFocusable(true);
        setClickable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // 当Surface被创建之后，开始Camera的预览
        try {
            // 打开拍照功能
            mCamera = Camera.open();
            // 摄像头画面显示在Surface上
            mCamera.setPreviewDisplay(holder);
            mCamera.setDisplayOrientation(90);
        } catch (IOException e) {
            TLog.log("预览失败");
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // 在预览前可以指定Camera的各项参数
        try {
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            // 获取摄像头支持的PictureSize列表
            List<Size> pictureSizeList = parameters.getSupportedPictureSizes();
            // 从列表中选取合适的分辨率
            Size picSize = getProperSize(pictureSizeList, width, height);
            if(picSize == null) {
                picSize = parameters.getPictureSize();
            }
            // 根据选出的PictureSize重新设置大小
            parameters.setPictureSize(picSize.width, picSize.height);
            mCamera.setParameters(parameters);
            mCamera.startPreview();
            focus(mAutoFocusCallback);
        } catch (Exception e){
            TLog.log(e.toString());
        }
    }

    private Size getProperSize(List<Size> sizes, int w, int h) {
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
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mCamera != null) {
            mCamera.stopPreview(); // 停止预览
            mCamera.release(); // 释放相机资源
            mCamera = null;
        }
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
                    if(mCallback != null) {
                        mCallback.startFocus();
                    }
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
     * 添加聚焦监听
     * @param callback 接口回调
     */
    public void addCallback(Callback callback) {
        this.mCallback = callback;
    }

    /**
     * 获取拍照照片
     */
    public void takePicture(Camera.PictureCallback callback) {
        mCamera.takePicture(null, null, callback);
    }

    /**
     * 监听拍照聚焦的状态
     */
    public interface Callback {

        /**
         * 开始聚焦
         */
        void startFocus();

        /**
         * 聚焦结束
         * @param success true: 聚焦成功; false: 聚焦失败
         */
        void endFocus(boolean success);

    }

}
