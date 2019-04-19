package com.lefuorgn.lefu.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.lefu.bean.AllocatingTaskExecuteOption;

import java.util.List;

/**
 * 执行配单执行操作页面数据适配器
 */

public class ExecuteAllocatingTaskAdapter extends BaseQuickAdapter<AllocatingTaskExecuteOption> {

    public ExecuteAllocatingTaskAdapter(List<AllocatingTaskExecuteOption> data) {
        super(R.layout.item_activity_excute_allocating_task, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, AllocatingTaskExecuteOption option) {
        holder.setText(R.id.tv_item_activity_execute_allocating_task_type, option.getNursing_item_name())
                .setText(R.id.tv_item_activity_execute_allocating_task_time,
                        String.format("%s", String.valueOf(option.getComplete())))
                .setText(R.id.tv_item_activity_execute_allocating_task_total,
                        "共 " + option.getTotal() + " 次")
                .setOnClickListener(R.id.tv_item_activity_execute_allocating_task_time,
                        new OnItemChildClickListener())
                .setOnClickListener(R.id.tv_item_activity_execute_allocating_task_total,
                        new OnItemChildClickListener());
        TextView tv_time = holder.getView(R.id.tv_item_activity_execute_allocating_task_time);
        TextView tv_total = holder.getView(R.id.tv_item_activity_execute_allocating_task_total);
        if(option.getSave_type() == AllocatingTaskExecuteOption.SAVE_TYPE_SERVICE) {
            // 服务器数据
            tv_time.setSelected(option.getComplete() >= option.getTotal());
            tv_total.setTextColor(
                    Color.parseColor(option.getComplete() >= option.getTotal() ? "#9FAFB6" : "#000000"));
            tv_total.setVisibility(View.VISIBLE);
        }else if (option.getSave_type() == AllocatingTaskExecuteOption.SAVE_TYPE_LOCAL){
            // 本地数据
            tv_time.setSelected(true);
            tv_total.setVisibility(View.INVISIBLE);
        }
    }
}
