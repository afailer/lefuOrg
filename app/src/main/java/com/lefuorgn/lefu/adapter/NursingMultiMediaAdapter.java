package com.lefuorgn.lefu.adapter;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore.Images;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.api.remote.ImageLoader;
import com.lefuorgn.lefu.bean.MultiMedia;

import java.util.List;

/**
 * 多媒体信息展示控
 */

public class NursingMultiMediaAdapter extends BaseQuickAdapter<MultiMedia>{

    public NursingMultiMediaAdapter(List<MultiMedia> data) {
        super(R.layout.dialog_nursing_multi_media, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, MultiMedia s) {
        ImageView img = holder.getView(R.id.iv_dialog_nursing_multi_media);
        holder.setOnClickListener(R.id.iv_dialog_nursing_multi_media_close, new OnItemChildClickListener());
        int type = s.getType();
        if(type == MultiMedia.MULTI_MEDIA_TYPE_PICTURE) {
            // 当前条目为图片
            ImageLoader.loadLocalImg(s.getUri(), img);
        }else if(type == MultiMedia.MULTI_MEDIA_TYPE_VIDEO) {
            // 当前条目为视频
            Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(s.getUri(), Images.Thumbnails.MINI_KIND);
            if(bitmap != null) {
                img.setImageBitmap(bitmap);
            }else {
                img.setImageResource(R.mipmap.multi_media_video_default);
            }
        }else if(type == MultiMedia.MULTI_MEDIA_TYPE_AUDIO) {
            // 当前条目为音频
            img.setImageResource(R.mipmap.multi_media_audio_default);
        }
    }
}
