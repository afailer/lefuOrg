package com.lefuorgn.viewloader.display;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout.LayoutParams;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lefuorgn.lefu.multiMedia.PreviewPictureActivity;
import com.lefuorgn.util.DividerGridItemDecoration;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.viewloader.adapter.ImageViewAdapter;
import com.lefuorgn.viewloader.base.DisplayViewBuilder;
import com.lefuorgn.viewloader.bean.Image;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 展示图片视图构建器基类
 */

public class ImageDisplayBuilder extends DisplayViewBuilder {

    private List<Image> mImageData;
    private ImageViewAdapter mAdapter;

    public ImageDisplayBuilder(Context context, int paddingWidth, int paddingHeight,
                               float labelSize, int labelColor, float valueSize, int valueColor) {
        super(context, paddingWidth, paddingHeight, labelSize, labelColor, valueSize, valueColor);
    }

    @Override
    protected void initChildView(ViewGroup parent) {
        mImageData = new ArrayList<Image>();
        RecyclerView recyclerView = new RecyclerView(parent.getContext());
        recyclerView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        recyclerView.addItemDecoration(new DividerGridItemDecoration(Color.WHITE, 4));
        WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        int width = (metrics.widthPixels - mPaddingWidth * 2) / 3;
        mAdapter = new ImageViewAdapter(mImageData, width, false);
        recyclerView.setAdapter(mAdapter);
        parent.addView(recyclerView, 1);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Image image = mAdapter.getItem(i);
                Intent intent = new Intent(mContext, PreviewPictureActivity.class);
                intent.putExtra(PreviewPictureActivity.INTENT_PICTURE_URI, image.getUri());
                intent.putExtra(PreviewPictureActivity.INTENT_PICTURE_TYPE,
                        PreviewPictureActivity.INTENT_PICTURE_TYPE_NET);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    protected void initChildData(JSONObject jsonObject) {
        if(StringUtils.isEmpty(mValue)) {
            return;
        }
        String[] values = mValue.split(",");
        for (String s : values) {
            Image image = new Image();
            image.setUri(s);
            image.setType(Image.IMAGE_TYPE_NET);
            mImageData.add(image);
        }
        mAdapter.setNewData(mImageData);
    }

    @Override
    protected boolean isHorizontal() {
        return false;
    }
}
