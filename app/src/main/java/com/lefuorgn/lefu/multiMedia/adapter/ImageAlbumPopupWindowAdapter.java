package com.lefuorgn.lefu.multiMedia.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.api.remote.ImageLoader;
import com.lefuorgn.lefu.multiMedia.bean.ImageFolder;

import java.util.List;

/**
 * 图片文件夹显示信息
 */

public class ImageAlbumPopupWindowAdapter extends BaseQuickAdapter<ImageFolder> {

    public ImageAlbumPopupWindowAdapter(List<ImageFolder> data) {
        super(R.layout.item_pop_window_image_album, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, ImageFolder imageFolder) {
        String des = imageFolder.getAlbumName() + "  " + imageFolder.getCount() + "张";
        holder.setText(R.id.tv_item_pop_window_image_album_des, des)
                .setVisible(R.id.iv_item_pop_window_image_album_choose, imageFolder.isChecked());
        ImageLoader.loadLocalImg(imageFolder.getAlbumPath(),
                R.mipmap.multi_media_image_folder_default,
                (ImageView) holder.getView(R.id.iv_item_pop_window_image_album_img));
    }
}
