package com.lefuorgn.lefu.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.lefu.bean.AllocatingTaskExecuteOption;

import java.util.List;

/**
 * 护理任务详细配单数据适配器
 */

public class ExecuteTakingNursingAdapter extends BaseQuickAdapter<AllocatingTaskExecuteOption> {

    public ExecuteTakingNursingAdapter(List<AllocatingTaskExecuteOption> data) {
        super(R.layout.item_activity_execute_taking_nursing, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, AllocatingTaskExecuteOption task) {
        holder.setText(R.id.tv_item_activity_execute_taking_nursing_name, task.getNursing_item_name())
                .setText(R.id.tv_item_activity_execute_taking_nursing_time, getTimeInfo(task));
        holder.getView(R.id.ll_item_activity_execute_taking_nursing)
                .setSelected(task.getComplete() < task.getTotal());
        holder.getView(R.id.tv_item_activity_execute_taking_nursing_time)
                .setSelected(task.getComplete() < task.getTotal());
    }

    private String getTimeInfo(AllocatingTaskExecuteOption task) {
        if(task.getSave_type() == AllocatingTaskExecuteOption.SAVE_TYPE_LOCAL) {
            return task.getComplete() + " 次";
        }else {
            return task.getPercentage() + " 次";
        }
    }

}
