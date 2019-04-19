package com.lefuorgn.lefu.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.lefu.bean.ElectronicFenceItem;

import java.util.List;

/**
 * 安全区域信息展示适配器
 */

public class ElectronicFenceAdapter extends BaseQuickAdapter<ElectronicFenceItem> {

    public ElectronicFenceAdapter(List<ElectronicFenceItem> data) {
        super(R.layout.item_pop_window_electronic, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, ElectronicFenceItem item) {
        holder.setText(R.id.tv_item_pop_window_electronic, item.getName());
    }
}
