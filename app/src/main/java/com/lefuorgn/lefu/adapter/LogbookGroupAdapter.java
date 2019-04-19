package com.lefuorgn.lefu.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.lefu.bean.LogbookGroup;

import java.util.ArrayList;

/**
 * 交班记录条件分组选择信息适配器
 */

public class LogbookGroupAdapter extends BaseQuickAdapter<LogbookGroup> {

    public LogbookGroupAdapter() {
        super(R.layout.item_pop_window_logbook, new ArrayList<LogbookGroup>());
    }

    @Override
    protected void convert(BaseViewHolder holder, LogbookGroup logbookGroup) {
        holder.setText(R.id.tv_item_pop_window_logbook, logbookGroup.getGroup_name());
    }
}
