package com.lefuorgn.lefu.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.lefu.bean.GridLayoutItem;

import java.util.List;

/**
 * 网格数据适配器
 */

public class GridLayoutItemAdapter extends BaseQuickAdapter<GridLayoutItem> {

    public GridLayoutItemAdapter(List<GridLayoutItem> data) {
        super(R.layout.item_grid_layout_item, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, GridLayoutItem item) {
        TextView tv = holder.getView(R.id.tv_item_grid_layout_item);
        tv.setText(item.getName());
        tv.setSelected(!item.isPermission());
        tv.setCompoundDrawablesWithIntrinsicBounds(0, item.getDrawableRes(), 0, 0);
    }
}
