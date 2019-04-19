package com.lefuorgn.lefu.adapter;

import android.graphics.Color;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.lefu.bean.EstimateItem;

import java.util.List;

/**
 * 评估表条目适配器
 */

public class EstimateAdapter extends BaseQuickAdapter<EstimateItem> {

    private boolean permission;

    public EstimateAdapter(List<EstimateItem> data, boolean permission) {
        super(R.layout.item_activity_estimate, data);
        this.permission = permission;
    }

    @Override
    protected void convert(BaseViewHolder holder, EstimateItem e) {
        holder.setText(R.id.tv_item_activity_estimate, e.getTitle())
                .setTextColor(R.id.tv_item_activity_estimate,
                        e.getId() == -1 ? Color.BLACK : permission ? Color.BLACK : Color.GRAY);
    }
}
