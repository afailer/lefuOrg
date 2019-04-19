package com.lefuorgn.interf;

import android.view.View;

import com.lefuorgn.lefu.base.BaseGridItem;

/**
 * BaseGridActivity中方格点击事件
 */

public interface OnItemChildColumnClickListener<T extends BaseGridItem> {

    /**
     * 点击事件触发方法
     * @param v 当前被点击控件的对象
     * @param item 当前条目对象
     * @param position 当前条目行号
     * @param column 当前条目列值
     */
    void onClick(View v, T item, int position, int column);

}
