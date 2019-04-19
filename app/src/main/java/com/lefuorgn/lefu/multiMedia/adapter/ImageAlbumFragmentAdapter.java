package com.lefuorgn.lefu.multiMedia.adapter;

import android.graphics.Color;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.api.remote.ImageLoader;
import com.lefuorgn.lefu.multiMedia.bean.Image;

import java.util.List;

/**
 * 图片详情页面展示适配器
 */

public class ImageAlbumFragmentAdapter extends BaseQuickAdapter<Image> {

    public ImageAlbumFragmentAdapter(List<Image> data) {
        super(R.layout.item_fragment_image_album, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Image s) {
        int position = holder.getLayoutPosition() - this.getHeaderViewsCount();
        final ImageView iv = holder.getView(R.id.iv_item_fragment_image_album);
        if(position == 0) {
            iv.setColorFilter(Color.TRANSPARENT);
            holder.setVisible(R.id.cb_item_fragment_image_album, false);
            ImageLoader.loadImg(R.mipmap.multi_media_picture_default, iv);
        }else {
            if(s.isSelect()) {
                iv.setColorFilter(Color.parseColor("#77000000"));
            }else {
                iv.setColorFilter(Color.TRANSPARENT);
            }
            holder.setVisible(R.id.cb_item_fragment_image_album, true)
                    .setChecked(R.id.cb_item_fragment_image_album, s.isSelect());
            ImageLoader.loadLocalImg(s.getUrl(), iv);
        }
    }

}
