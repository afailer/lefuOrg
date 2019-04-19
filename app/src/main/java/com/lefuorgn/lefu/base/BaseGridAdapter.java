package com.lefuorgn.lefu.base;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.db.model.basic.DisplaySignOrNursingItem;
import com.lefuorgn.interf.OnItemChildColumnClickListener;
import com.lefuorgn.interf.OnScrollChangedListenerImp;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.widget.MyHScrollView;

import java.util.List;

/**
 * 网格形数据适配器基类
 */
public abstract class BaseGridAdapter<T extends BaseGridItem> extends BaseQuickAdapter<T> {

    /**
     * 方格点击事件
     */
    protected OnItemChildColumnClickListener mListener;

    private Context mContext;
    private List<DisplaySignOrNursingItem> mItems;
    private int mWidth;
    private int mHeight;
    private ViewGroup.LayoutParams mParams;
    protected LayoutInflater mInflater;


    public BaseGridAdapter(Context context, int layoutResId, List<T> data, List<DisplaySignOrNursingItem> items) {
        this(context, layoutResId, data, items, 0, 0);
        mWidth = mContext.getResources().getDimensionPixelSize(R.dimen.base_grid_width);
        mHeight = mContext.getResources().getDimensionPixelSize(R.dimen.base_grid_height);
    }

    public BaseGridAdapter(Context context, int layoutResId, List<T> data, List<DisplaySignOrNursingItem> items, int width, int height) {
        super(layoutResId, data);
        this.mContext = context;
        this.mItems = items;
        mWidth = width;
        mHeight = height;
        mParams = new ViewGroup.LayoutParams(StringUtils.dip2px(context, 1), ViewGroup.LayoutParams.MATCH_PARENT);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    protected BaseViewHolder createBaseViewHolder(ViewGroup parent, int layoutResId) {
        View view = getItemView(layoutResId, parent);
        MyHScrollView hsvView = (MyHScrollView) view.findViewById(R.id.hsv_item_activity_base_grid);
        if(hsvView != null) {
            hsvView.AddOnScrollChangedListener(new OnScrollChangedListenerImp(hsvView));
            LinearLayout hsv = (LinearLayout) view.findViewById(R.id.ll_item_activity_base_grid);
            for (int i = 0; i < mItems.size(); i++) {
                hsv.addView(getView(hsv, mWidth, mHeight));
                if(i < mItems.size() - 1) {
                    hsv.addView(getLine(), mParams);
                }
            }
            return new MyBaseViewHolder(view);
        }else {
            return super.createBaseViewHolder(parent, layoutResId);
        }

    }

    /**
     * 获取分割线
     * @return 分割线控件
     */
    protected View getLine() {
        View view = new View(mContext);
        view.setBackgroundResource(R.color.recycler_view_item_division_color);
        return view;
    }

    /**
     * 获取空格内的布局
     * @param parent 父控件
     * @return 内容布局控件
     */
    protected View getView(ViewGroup parent, int width, int height) {
        TextView tv = new TextView(parent.getContext());
        tv.setWidth(width);
        tv.setHeight(height);
        tv.setGravity(Gravity.CENTER);
        return tv;
    }

    private class MyBaseViewHolder extends BaseViewHolder {
        MyBaseViewHolder(View view) {
            super(view);
        }
    }

    /**
     * 设置网格点击事件
     * @param l 事件对象
     */
    public void setOnItemChildColumnClickListener(OnItemChildColumnClickListener l) {
        mListener = l;
    }

}
