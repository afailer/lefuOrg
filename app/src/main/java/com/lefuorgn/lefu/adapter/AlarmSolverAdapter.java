package com.lefuorgn.lefu.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.lefu.bean.AlarmEntrySolver;
import com.lefuorgn.util.StringUtils;

import java.util.List;

/**
 * 报警信息解决者信息适配器
 */

public class AlarmSolverAdapter extends BaseQuickAdapter<AlarmEntrySolver> {

    public AlarmSolverAdapter(List<AlarmEntrySolver> data) {
        super(R.layout.item_activity_alarm_solver, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, AlarmEntrySolver solver) {
        String details = "操作人: " + solver.getUser_name() + "\n" +
                "操作时间: " + StringUtils.getFormatData(solver.getTime(), "yyyy-MM-dd HH:mm");
        if(!StringUtils.isEmpty(solver.getRemark())) {
            details = details + "\n处理结果: " + solver.getRemark();
        }
        holder.setText(R.id.tv_item_activity_alarm_solver, details);
    }
}
