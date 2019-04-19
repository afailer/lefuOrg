package com.lefuorgn.lefu.multiMedia;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.lefuorgn.R;
import com.lefuorgn.lefu.multiMedia.fragment.AudioFragment;
import com.lefuorgn.lefu.multiMedia.fragment.ImageAlbumFragment;
import com.lefuorgn.lefu.multiMedia.fragment.ImageCaptureFragment;
import com.lefuorgn.lefu.multiMedia.fragment.VideoCaptureFragment;

/**
 * 多媒体页面
 */

public class MultiMediaActivity extends FragmentActivity {

    /**
     * 当前多媒体类型
     */
    public static final String MULTIMEDIA_TYPE = "multiMedia_type";

    /**
     * 当前多媒体类型
     * 打开相册页面, 包括拍照页面跳转按钮
     */
    public static final String ACTION_IMAGE_ALBUM = "android.multiMedia.action.image.album";
    /**
     * 当前多媒体类型
     * 打开拍照页面
     */
    public static final String ACTION_IMAGE_CAPTURE = "android.multiMedia.action.image.capture";
    /**
     * 当前多媒体类型
     * 打开视频录制页面
     */
    public static final String ACTION_VIDEO_CAPTURE = "android.multiMedia.action.video.capture";
    /**
     * 当前多媒体类型
     * 打开音频录制页面
     */
    public static final String ACTION_AUDIO = "android.multiMedia.action.audio";
    /**
     * 相册内容返回值码
     */
    public static final int RESULT_IMAGE_ALBUM = 0x100;
    /**
     * 照相内容返回值码
     */
    public static final int RESULT_IMAGE_CAPTURE = 0x200;
    /**
     * 视频录制返回值码
     */
    public static final int RESULT_VIDEO_CAPTURE = 0x300;
    /**
     * 音频录制返回值码
     */
    public static final int RESULT_AUDIO = 0x400;
    /**
     * 返回值类型名称
     */
    public static final String RESULT_NAME = "result_name";
    /**
     * 传递的值, 相片选择器使用, 传递的是当前可以使用图片的个数
     */
    public static final String INTENT_ARGUMENTS = "intent_arguments";
    /**
     * 传递的值, 相片选择器使用, 传递的是已使用的图片
     */
    public static final String INTENT_ARGUMENTS_SELECT = "intent_arguments_select";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_multi_media);
        // 获取当前页面类型
        Intent intent = getIntent();
        String type = intent.getStringExtra(MULTIMEDIA_TYPE);
        // 添加指定的页面
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment;
        if(ACTION_IMAGE_ALBUM.equals(type)) {
            // 相册页面
            fragment = new ImageAlbumFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(INTENT_ARGUMENTS, intent.getIntExtra(INTENT_ARGUMENTS, 0));
            bundle.putString(INTENT_ARGUMENTS_SELECT, intent.getStringExtra(INTENT_ARGUMENTS_SELECT));
            fragment.setArguments(bundle);
        }else if(ACTION_IMAGE_CAPTURE.equals(type)) {
            // 拍照页面
            fragment = new ImageCaptureFragment();
        }else if(ACTION_VIDEO_CAPTURE.equals(type)) {
            // 视频录制页面
            fragment = new VideoCaptureFragment();
        }else if(ACTION_AUDIO.equals(type)) {
            // 音频录制页面
            fragment = new AudioFragment();
        }else {
            // 默认是相册页面
            fragment = new ImageAlbumFragment();
        }
        ft.add(R.id.fl_activity_multi_media, fragment);
        ft.commit();
    }
}
