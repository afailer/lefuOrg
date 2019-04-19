package com.lefuorgn.lefu.multiMedia.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.lefuorgn.AppConfig;
import com.lefuorgn.R;
import com.lefuorgn.base.BaseFragment;
import com.lefuorgn.lefu.multiMedia.MultiMediaActivity;
import com.lefuorgn.lefu.multiMedia.widget.PhotographView;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.util.TLog;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * 拍照页面
 */

public class ImageCaptureFragment extends BaseFragment {

    private PhotographView mPhotographView;
    private ImageView mFocusImageView;

    private SavePictureTask mSavePictureTask;

    private Handler mHandler = new Handler();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_multi_media_image_capture;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mPhotographView = (PhotographView) view.findViewById(R.id.pv_fragment_multi_media_image_capture);
        mFocusImageView = (ImageView) view.findViewById(R.id.iv_fragment_multi_media_image_capture);
        view.findViewById(R.id.btn_fragment_multi_media_image_capture).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        mPhotographView.addCallback(new PhotographView.Callback() {
            @Override
            public void startFocus() {
                // 开始聚焦
                mFocusImageView.setVisibility(View.VISIBLE);
                mFocusImageView.setImageResource(R.drawable.focus_focusing);
                //3秒后隐藏View。在此处设置是由于可能聚焦事件可能不触发。
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mFocusImageView.setVisibility(View.GONE);
                    }
                },3500);
            }

            @Override
            public void endFocus(boolean success) {
                // 聚焦完成
                if(success) {
                    mFocusImageView.setImageResource(R.drawable.focus_focused);
                }else {
                    mFocusImageView.setImageResource(R.drawable.focus_focus_failed);
                }
                // 提示图框1秒后消失
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mFocusImageView.setVisibility(View.GONE);
                    }
                },1000);
            }
        });
    }

    @Override
    public void onClick(View v) {
        // 拍照按钮被点击
        mPhotographView.takePicture(new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                cancelPictureTask();
                mSavePictureTask = (SavePictureTask) new SavePictureTask().execute(data);
            }
        });
    }

    /**
     * 取消图片保存任务
     */
    private void cancelPictureTask() {
        if(mSavePictureTask != null) {
            mSavePictureTask.cancel(true);
            mSavePictureTask = null;
        }
    }

    /**
     * 本地获取数据请求任务类
     */
    private class SavePictureTask extends AsyncTask<byte[], Void, String> {

        @Override
        protected void onPreExecute() {
            showLoadingDialog();
        }

        @Override
        protected String doInBackground(byte[]... params) {
            // 判断是否存在sd卡
            boolean sdExist = android.os.Environment.MEDIA_MOUNTED
                    .equals(android.os.Environment.getExternalStorageState());
            // 生成保存图片的路径
            File fFolder;
            if(sdExist) {
                // SD卡挂载
                TLog.error("SD卡搭载...");
                fFolder = new File(AppConfig.DEFAULT_SAVE_IMAGE_PATH);
            }else {
                // SD卡没有挂载
                TLog.error("SD卡未搭载...");
                String path = getContext().getDir("lefu_img", Context.MODE_PRIVATE).getAbsolutePath();
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
            File fImage = new File(fFolder, UUID.randomUUID().toString() + ".jpg");
            // 字节数组转换成Bitmap对象,并处理
            BitmapFactory.Options options = new BitmapFactory.Options();
            byte[] buffer = params[0];
            Bitmap bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.length, options);
            // 旋转图片
            bitmap = rotateImage(bitmap);
            // 创建输出流
            BufferedOutputStream bos = null;
            try {
                bos = new BufferedOutputStream(new FileOutputStream(fImage));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            } catch (Exception e) {
                // 文件保存失败
                return "";
            } finally {
                if(bos != null) {
                    try {
                        bos.flush();
                        bos.close();
                        bitmap.recycle();
                    } catch (IOException e) {
                        TLog.log(e.toString());
                    }
                }
            }
            TLog.error("图片的大小是: " + fImage.length() / 1024 + "KB");
            return fImage.getAbsolutePath();
        }

        /**
         * 旋转图片90度
         * @param bitmap Bitmap对象
         * @return 旋转后的对象
         */
        private Bitmap rotateImage(Bitmap bitmap) {
            //旋转图片 动作
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            // 创建新的图片
            Bitmap nBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                    bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return nBitmap;
        }

        @Override
        protected void onPostExecute(String s) {
            TLog.log("保存后图片的路径 === " + s);
            if(StringUtils.isEmpty(s)) {
                showToast("图片保存失败!");
            }else {
                // (这里可以添加通知相册更新内容的广播)
                Intent intent = new Intent();
                intent.putExtra(MultiMediaActivity.RESULT_NAME, s);
                getActivity().setResult(MultiMediaActivity.RESULT_IMAGE_CAPTURE, intent);
                getActivity().finish();
            }
            hideLoadingDialog();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelPictureTask();
    }
}
