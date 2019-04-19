package com.lefuorgn.viewloader.builder;

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
import com.lefuorgn.R;
import com.lefuorgn.lefu.multiMedia.PreviewPictureActivity;
import com.lefuorgn.util.DividerGridItemDecoration;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.viewloader.adapter.ImageViewAdapter;
import com.lefuorgn.viewloader.base.LabelViewBuilder;
import com.lefuorgn.viewloader.bean.Image;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 图片视图构建器
 */

public class ImageViewBuilder extends LabelViewBuilder {

    private RecyclerView mRecyclerView; // 图片展示

    private int mImageWidth; // 图片的宽度

    private boolean mSingle; // true: 单图; false: 多图

    private List<Image> mImageData;
    private ImageViewAdapter mAdapter;
    private OnItemClickListener mListener;

    public ImageViewBuilder(Context context, int paddingWidth, int paddingHeight,
                            float labelSize, int labelColor, float valueSize, int valueColor,
                            float describeSize, int describeColor, int describeBackground) {
        super(context, paddingWidth, paddingHeight, labelSize, labelColor, valueSize, valueColor,
                describeSize, describeColor, describeBackground);
        WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        mImageWidth = (metrics.widthPixels - mPaddingWidth * 2) / 3;
    }

    @Override
    protected void initChildView(ViewGroup parent) {
        mRecyclerView = new RecyclerView(parent.getContext());
        mRecyclerView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        mRecyclerView.setPadding(0, mPaddingHeight, 0, 0);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(Color.WHITE, 4));
        parent.addView(mRecyclerView);
    }

    @Override
    protected void initValue(String value) {
        mImageData = new ArrayList<Image>();
        Image resImage = new Image();
        resImage.setRes(R.mipmap.imag_add);
        resImage.setUri("res");
        resImage.setType(Image.IMAGE_TYPE_RES);
        mImageData.add(resImage);
        if(!StringUtils.isEmpty(value)) {
            String[] values = value.split(",");
            for (String s : values) {
                Image image = new Image();
                image.setUri(s);
                image.setType(Image.IMAGE_TYPE_NET);
                mImageData.add(mImageData.size() - 1, image);
            }
        }
        mAdapter = new ImageViewAdapter(mImageData, mImageWidth);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                if(mImageData.size() - 1 == i) {
                    if(mListener != null) {
                        mListener.onItemClick(view);
                    }
                }else {
                    Image image = mAdapter.getItem(i);
                    Intent intent = new Intent(mContext, PreviewPictureActivity.class);
                    intent.putExtra(PreviewPictureActivity.INTENT_PICTURE_URI, image.getUri());
                    if(image.getType() == Image.IMAGE_TYPE_LOCAL) {
                        intent.putExtra(PreviewPictureActivity.INTENT_PICTURE_TYPE,
                                PreviewPictureActivity.INTENT_PICTURE_TYPE_LOCAL);
                    }else if(image.getType() == Image.IMAGE_TYPE_NET) {
                        intent.putExtra(PreviewPictureActivity.INTENT_PICTURE_TYPE,
                                PreviewPictureActivity.INTENT_PICTURE_TYPE_NET);
                    }else {
                        return;
                    }
                    mContext.startActivity(intent);
                }
            }
        });
        mAdapter.setOnRecyclerViewItemChildClickListener(
                new BaseQuickAdapter.OnRecyclerViewItemChildClickListener() {
                    @Override
                    public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                        // 移除当前条目
                        mAdapter.getData().remove(i);
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    protected void initChildData(JSONObject jsonObject) {
        mSingle = getBoolean(jsonObject, "single");
    }

    /**
     * 添加图片
     * @param uri 图片Uri
     */
    public void addPicture(String uri, boolean local) {
        boolean add = true;
        for (Image image : mImageData) {
            if(image.getUri().equals(uri)) {
                add = false;
                break;
            }
        }
        if(add) {
            Image image = new Image();
            image.setUri(uri);
            image.setType(local ? Image.IMAGE_TYPE_LOCAL : Image.IMAGE_TYPE_NET);
            if(mSingle && mImageData.size() >= 2) {
                // 单一图片, 且存在选择的图片, 移除
                List<Image> list = new ArrayList<Image>();
                for (int i = 0; i < mImageData.size(); i++) {
                    if(i != mImageData.size() - 1) {
                        list.add(mImageData.get(i));
                    }
                }
                mImageData.removeAll(list);
            }
            mImageData.add(mImageData.size() - 1, image);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public String getValue() {
        if(mImageData == null || mImageData.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Image image : mImageData) {
            if(image.getType() != Image.IMAGE_TYPE_RES) {
                sb.append(image.getUri()).append(",");
            }
        }
        return sb.length() > 0 ? sb.substring(0, sb.length() - 1) : "";
    }

    public int getMaxNum() {
        if(mSingle) {
            return 1;
        }else {
            return 9;
        }
    }

    @Override
    protected boolean isHorizontal() {
        return false;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {

        void onItemClick(View view);

    }

}
