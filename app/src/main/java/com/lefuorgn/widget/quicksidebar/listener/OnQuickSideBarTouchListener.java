package com.lefuorgn.widget.quicksidebar.listener;

/**
 * 侧拉滑动监听事件
 */

public interface OnQuickSideBarTouchListener {

    /**
     * 滑动被选中的内容
     * @param letter 内容详情
     * @param position 当前指示内容的位置
     * @param y 当前内容的Y轴位置
     */
    void onLetterChanged(String letter, int position, float y);

    /**
     * touch滑动事件
     */
    void onLetterTouching(boolean touching);

}
