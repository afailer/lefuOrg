package com.lefuorgn.interf;

import com.lefuorgn.widget.MyHScrollView;

/**
 * MyHScrollView滚动监听分发事件
 */
public interface OnScrollChangedListener {

    /**
     * 分发过来处理的事件
     * @param view 当前分发的MyHScrollView对象
     * @param l 水平移动的距离
     * @param t 垂直移动的距离
     * @param oldl 移动前水平距离
     * @param oldt 移动后水平距离
     */
	void onOtherScrollChanged(MyHScrollView view, int l, int t, int oldl, int oldt);

    /**
     * 获取当前事件绑定的MyHScrollView对象
     * @return 当前绑定的MyHScrollView对象
     */
	MyHScrollView getMyHScrollView();
	
}
