package com.lefuorgn.oa.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.interf.OnScrollChangedListenerImp;
import com.lefuorgn.lefu.base.BaseGridItem;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.widget.MyHScrollView;

import java.util.List;

/**
 * Created by liuting on 2017/4/24.
 */

public abstract class BaseGridAdapter<T extends BaseGridItem> extends BaseQuickAdapter<T> {
    private int mWidth;
    private int mHeight;
    private ViewGroup.LayoutParams mParams;
    protected LayoutInflater mInflater;
    int mSize;

    public BaseGridAdapter(Context context, int layoutResId, List<T> data, int mSize) {
        this(context, layoutResId, data, mSize, 0, 0);
    }

    public BaseGridAdapter(Context context, int layoutResId, List<T> data, int mSize, int width, int height) {
        super(layoutResId, data);
        this.mContext = context;
        this.mSize = mSize;
        mWidth = width;
        mHeight = height;
        mParams = new ViewGroup.LayoutParams(StringUtils.dip2px(context, 1), ViewGroup.LayoutParams.MATCH_PARENT);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    protected BaseViewHolder createBaseViewHolder(ViewGroup parent, int layoutResId) {
        View view = getItemView(layoutResId, parent);
        MyHScrollView hsvView = (MyHScrollView) view.findViewById(R.id.item_work_horizontal);
        if(hsvView != null) {
            hsvView.AddOnScrollChangedListener(new OnScrollChangedListenerImp(hsvView));
            LinearLayout hsv = (LinearLayout) view.findViewById(R.id.month_work_content);
            for (int i = 0; i < mSize; i++) {
                hsv.addView(getView(hsv));
            }
            return new GridBaseViewHolder(view);
        }else {
            return super.createBaseViewHolder(parent, layoutResId);
        }
    }
    /**
     * 获取空格内的布局
     * @param parent 父控件
     * @return 内容布局控件
     */
    protected View getView(ViewGroup parent) {
        View container = mLayoutInflater.inflate(R.layout.textview, parent, false);
        return container;
    }
    private class GridBaseViewHolder extends BaseViewHolder {
        GridBaseViewHolder(View view) {
            super(view);
        }
    }

}
