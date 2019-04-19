package com.lefuorgn.lefu.multiMedia;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.lefuorgn.R;
import com.lefuorgn.api.remote.ImageLoader;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 图片预览类
 */

public class PreviewPictureActivity extends Activity {

    public static final String INTENT_PICTURE_URI = "intent_picture_uri";
    public static final String INTENT_PICTURE_TYPE = "intent_picture_type";
    public static final int INTENT_PICTURE_TYPE_LOCAL = 1;
    public static final int INTENT_PICTURE_TYPE_NET = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_preview_picture);
        PhotoView iv = (PhotoView) findViewById(R.id.iv_activity_preview_picture);
        String uri = getIntent().getStringExtra(INTENT_PICTURE_URI);
        int local = getIntent().getIntExtra(INTENT_PICTURE_TYPE, INTENT_PICTURE_TYPE_LOCAL);
        if(local == INTENT_PICTURE_TYPE_NET) {
            ImageLoader.loadImg(uri, iv);
        }else {
            ImageLoader.loadLocalImg(uri, iv);
        }
        iv.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float v, float v1) {
                finish();
            }
        });
    }

}
