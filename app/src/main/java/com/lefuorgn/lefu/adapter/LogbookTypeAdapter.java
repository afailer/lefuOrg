package com.lefuorgn.lefu.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.lefu.bean.LogbookType;

import java.util.List;

/**
 * 交班记录条件分类选择信息适配器
 */

public class LogbookTypeAdapter extends BaseQuickAdapter<LogbookType> {

    public LogbookTypeAdapter(List<LogbookType> data) {
        super(R.layout.item_pop_window_logbook, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, LogbookType type) {
        holder.setText(R.id.tv_item_pop_window_logbook, type.getContent());
    }
}
