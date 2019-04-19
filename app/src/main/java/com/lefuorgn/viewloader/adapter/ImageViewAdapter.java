package com.lefuorgn.viewloader.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.api.remote.ImageLoader;
import com.lefuorgn.viewloader.bean.Image;

import java.util.List;

/**
 * 图片信息转换适配器
 */

public class ImageViewAdapter extends BaseQuickAdapter<Image> {

    private int mWidth;
    private boolean showDelete; // 是否显示删除按钮

    public ImageViewAdapter(List<Image> data, int width) {
        this(data, width, true);
    }

    public ImageViewAdapter(List<Image> data, int width, boolean showDelete) {
        super(R.layout.builder_label_image, data);
        this.mWidth = width;
        this.showDelete = showDelete;
    }

    @Override
    protected void convert(BaseViewHolder holder, Image image) {
        ImageView imageView = holder.getView(R.id.iv_builder_label_image);
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        params.width = mWidth;
        //noinspection SuspiciousNameCombination
        params.height = mWidth;
        ImageButton imageButton = holder.getView(R.id.btn_builder_label_image);
        if(image.getType() == Image.IMAGE_TYPE_LOCAL) {
            // 本地图片
            ImageLoader.loadLocalImg(image.getUri(), imageView);
            imageButton.setVisibility(View.VISIBLE);
        }else if(image.getType() == Image.IMAGE_TYPE_NET) {
            // 网络图片
            ImageLoader.loadImg(image.getUri(), imageView);
            imageButton.setVisibility(View.VISIBLE);
        }else if(image.getType() == Image.IMAGE_TYPE_RES) {
            // 资源图片
            ImageLoader.loadImg(image.getRes(), imageView);
            imageButton.setVisibility(View.GONE);
        }
        if(showDelete) {
            // 显示
            holder.setOnClickListener(R.id.btn_builder_label_image, new OnItemChildClickListener());
        }else {
            imageButton.setVisibility(View.GONE);
        }
    }
}
