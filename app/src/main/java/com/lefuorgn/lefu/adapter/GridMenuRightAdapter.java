package com.lefuorgn.lefu.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;

import java.util.List;

/**
 * BaseGridActivity侧拉菜单数据适配器
 */
public class GridMenuRightAdapter extends BaseQuickAdapter<String> {

    public GridMenuRightAdapter(List<String> data) {
        super(R.layout.item_fragment_grid_menu_right, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, String str) {
        baseViewHolder.setText(R.id.tv_item_fragment_grid_menu_right, str);
    }
}
