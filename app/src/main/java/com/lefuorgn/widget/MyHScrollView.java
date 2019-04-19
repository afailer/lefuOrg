package com.lefuorgn.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.lefuorgn.interf.OnScrollChangedListener;
import com.lefuorgn.lefu.util.ScrollViewObserver;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 具有滑动位置共享的水平ScrollView
 */

public class MyHScrollView extends HorizontalScrollView {

    /**
     * 记录当前滚动事件是否分发
     */
    private boolean distribute = true;
    private List<View> mViews;

    /**
     * 所有的对象共用一份
     */
    private static ScrollViewObserver mScrollViewObserver = new ScrollViewObserver();

    public MyHScrollView(Context context) {
        super(context);
    }

    public MyHScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyHScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if(distribute) {
            mScrollViewObserver.NotifyOnScrollChanged(this, l, t, oldl, oldt);
        }else {
            distribute = true;
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ViewGroup tabsLayout = (ViewGroup) getChildAt(0);
        if (tabsLayout == null || tabsLayout.getMeasuredWidth() >= getMeasuredWidth()) {
            return;
        }
        if(tabsLayout.getChildCount() <= 0) {
            return;
        }
        if(mViews == null) {
            mViews = new ArrayList<View>();
        }else {
            mViews.clear();
        }
        for (int w = 0; w < tabsLayout.getChildCount(); w++) {
            if(w % 2 == 0) {
                mViews.add(tabsLayout.getChildAt(w));
            }
        }
        adjustChildWidthWithParent(mViews, getMeasuredWidth() - tabsLayout.getPaddingLeft()
                        - tabsLayout.getPaddingRight(), widthMeasureSpec,
                heightMeasureSpec);
    }

    /**
     * 适配子View的宽度
     * @param views 所有具有内容的View集合, 不包括分割线
     * @param parentViewWidth 父控件的总宽度
     * @param widthMeasureSpec 父控件宽度
     * @param heightMeasureSpec 父控件的高度
     */
    private void adjustChildWidthWithParent(List<View> views, int parentViewWidth
            , int widthMeasureSpec, int heightMeasureSpec) {
        // 获取平均值
        int averageWidth = parentViewWidth / views.size();
        // 内容控件的个数
        int viewCount = views.size();
        while (true) {
            // 遍历所有的View
            Iterator<View> iterator = views.iterator();
            while (iterator.hasNext()) {
                View view = iterator.next();
                if (view.getMeasuredWidth() > averageWidth) {
                    // 大于平均值的移除
                    parentViewWidth -= view.getMeasuredWidth();
                    viewCount--;
                    iterator.remove();
                }
            }
            // 剩余View继续求平均值
            averageWidth = parentViewWidth / viewCount;
            boolean end = true;
            for (View view : views) {
                if (view.getMeasuredWidth() > averageWidth) {
                    // 存在大于平均值的View, 继续循环,去除大于平均值的View
                    end = false;
                }
            }
            if (end) {
                break;
            }
        }
        for (View view : views) {
            if (view.getMeasuredWidth() < averageWidth) {
                // 小于平均值的View, 重新分配宽度
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view
                        .getLayoutParams();
                layoutParams.width = averageWidth;
                view.setLayoutParams(layoutParams);
                // 再次测量让新宽度生效
                measureChildWithMargins(view, widthMeasureSpec, 0, heightMeasureSpec, 0);
            }
        }
    }

    /**
     * 滚动到指定的位置
     * @param distribute 是否进行数据分发
     * @param x 水平方向的位置
     * @param y 垂直方向的位置
     */
    public void scrollToByFlag(boolean distribute, int x, int y) {
        this.distribute = distribute;
        super.scrollTo(x, y);
    }

    /**
     * 添加滚动监听事件
     * @param listener 指定的事件
     */
    public void AddOnScrollChangedListener(OnScrollChangedListener listener) {
        mScrollViewObserver.AddOnScrollChangedListener(listener);
    }

    /**
     * 清除当前缓存中MyHScrollView对象
     */
    public static void clearViewCache() {
        mScrollViewObserver.clear();
    }

    /**
     * 重置当前位置
     */
    public static void resetLocation() {
        mScrollViewObserver.resetLocation();
    }

}
