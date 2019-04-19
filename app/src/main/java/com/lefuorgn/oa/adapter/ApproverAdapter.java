package com.lefuorgn.oa.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.db.model.basic.StaffCache;

import java.util.List;

/**
 * 默认员工信息展示适配器
 */

public class ApproverAdapter extends BaseQuickAdapter<StaffCache> {

    public ApproverAdapter(List<StaffCache> data) {
        super(R.layout.item_fragment_approver, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, StaffCache cache) {
        holder.setText(R.id.tv_item_fragment_approver, cache.getName())
                .setOnClickListener(R.id.tv_item_fragment_approver, new OnItemChildClickListener());
    }
}
