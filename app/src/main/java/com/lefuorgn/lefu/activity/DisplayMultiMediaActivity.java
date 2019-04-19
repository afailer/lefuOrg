package com.lefuorgn.lefu.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lefuorgn.R;
import com.lefuorgn.api.remote.ImageLoader;
import com.lefuorgn.api.remote.common.RemoteUtil;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.lefu.bean.MultiMedia;
import com.lefuorgn.lefu.bean.NursingItemInfo;
import com.lefuorgn.lefu.multiMedia.PreviewMultiMediaActivity;
import com.lefuorgn.util.TLog;

import java.util.ArrayList;
import java.util.List;

/**
 * 多媒体信息展示控件
 */

public class DisplayMultiMediaActivity extends BaseActivity {

    private ViewPager mViewPager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_display_multi_media;
    }

    @Override
    protected void initView() {
        mViewPager = (ViewPager)findViewById(R.id.vp_activity_display_multi_media);
    }

    @Override
    protected void initData() {
        NursingItemInfo info = (NursingItemInfo) getIntent().getSerializableExtra("nursingItemInfo");
        mViewPager.setAdapter(new DisplayMultiMediaAdapter(this, info.getMedia()));
    }

    @Override
    protected boolean hasStatusBar() {
        return false;
    }

    private class DisplayMultiMediaAdapter extends PagerAdapter {

        private List<MultiMedia> mData;
        private List<View> mImgViews;

        DisplayMultiMediaAdapter(Context context, List<MultiMedia> data) {
            TLog.error(data.toString());
            this.mData = data;
            mImgViews = new ArrayList<View>();
            for (int i = 0; i < mData.size(); i++) {
                @SuppressLint("InflateParams")
                View view = LayoutInflater.from(context)
                        .inflate(R.layout.item_activity_display_multi_media, null);
                mImgViews.add(view);
            }
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final MultiMedia media = mData.get(position);
            View view = mImgViews.get(position);
            ImageView imageView = (ImageView) view
                    .findViewById(R.id.iv_item_activity_display_multi_media);
            ViewGroup.LayoutParams params = imageView.getLayoutParams();
            if(media.getType() == MultiMedia.MULTI_MEDIA_TYPE_PICTURE) {
                params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                params.height = LinearLayout.LayoutParams.MATCH_PARENT;
                imageView.setLayoutParams(params);
                ImageLoader.loadImg(media.getUri(), imageView);
            }else if(media.getType() == MultiMedia.MULTI_MEDIA_TYPE_VIDEO) {
                params.width = LinearLayout.LayoutParams.WRAP_CONTENT;
                params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                imageView.setLayoutParams(params);
                imageView.setImageResource(R.mipmap.multi_media_video_default);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(DisplayMultiMediaActivity.this, PreviewMultiMediaActivity.class);
                        intent.putExtra(PreviewMultiMediaActivity.MULTI_MEDIA_URI, RemoteUtil.IMG_URL + media.getUri());
                        startActivity(intent);
                    }
                });
            }else if(media.getType() == MultiMedia.MULTI_MEDIA_TYPE_AUDIO) {
                params.width = LinearLayout.LayoutParams.WRAP_CONTENT;
                params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                imageView.setLayoutParams(params);
                imageView.setImageResource(R.mipmap.multi_media_audio_default);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri = Uri.parse(RemoteUtil.IMG_URL + media.getUri());
                        Intent m_intent = new Intent(Intent.ACTION_VIEW, uri);
                        m_intent.setDataAndType(uri, "audio/mp3");
                        startActivity(m_intent);
                    }
                });
            }
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mImgViews.get(position));
        }
    }

}
