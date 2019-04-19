package com.lefuorgn.lefu.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.db.model.basic.AllocatingTypeTask;
import com.lefuorgn.widget.CounterView;

import java.util.ArrayList;
import java.util.List;

/**
 * 测量任务数据适配器
 */

public class MeasurementTaskAdapter extends BaseQuickAdapter<AllocatingTypeTask> {

    private List<Integer> list;

    public MeasurementTaskAdapter(List<AllocatingTypeTask> data) {
        super(R.layout.item_activity_measurement_task, data);
        list = new ArrayList<Integer>();
        for (AllocatingTypeTask task : data) {
            list.add(task.getNumber_nursing());
        }
    }

    @Override
    protected void convert(BaseViewHolder holder, final AllocatingTypeTask task) {
        holder.setText(R.id.tv_item_activity_measurement_task_name, task.getContent());
        CounterView view = holder.getView(R.id.cv_item_activity_measurement_task);
        view.setValue(task.getNumber_nursing());
        view.setOnCounterChangeListener(new CounterView.OnCounterChangeListener() {
            @Override
            public void onCounterChange(View view, int amount) {
                task.setNumber_nursing(amount);
            }
        });
    }

    /**
     * 获取旧数据条目的测量条目数
     */
    public List<Integer> getOldData() {
        return list;
    }

}
