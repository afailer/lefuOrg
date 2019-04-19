package com.lefuorgn.lefu.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.lefu.bean.AllocatingTaskExecuteOption;

import java.util.List;

/**
 * 护理任务详细配单数据适配器
 */

public class OrderTakingNursingAdapter extends BaseQuickAdapter<AllocatingTaskExecuteOption> {

    public OrderTakingNursingAdapter(List<AllocatingTaskExecuteOption> data) {
        super(R.layout.item_activity_order_taking_nursing, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, AllocatingTaskExecuteOption task) {
        holder.setText(R.id.tv_item_activity_order_taking_nursing_name, task.getNursing_item_name())
                .setText(R.id.tv_item_activity_order_taking_nursing_time, task.getPercentage() + " 次");
    }
}
