package com.lefuorgn.lefu.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.db.util.SignConfigManager;
import com.lefuorgn.lefu.bean.AllocatingTaskProgress;

import java.util.List;

/**
 * 配单执行进度适配器
 */

public class AllocatingTaskProgressAdapter extends BaseQuickAdapter<AllocatingTaskProgress> {

    public AllocatingTaskProgressAdapter(List<AllocatingTaskProgress> data) {
        super(R.layout.item_activity_allocating_task_progress, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, AllocatingTaskProgress a) {
        float percent = (a.getFinish() * 1.0f / a.getTotal()) * 100;
        holder.setText(R.id.tv_item_activity_allocating_task_progress_type,
                SignConfigManager.getNursingName(a.getNursing_item_id()))
                .setText(R.id.tv_item_activity_allocating_task_progress, String.format("%.2f", percent) + "%")
                .setProgress(R.id.pb_item_activity_allocating_task_progress, a.getFinish(), a.getTotal());
    }
}
