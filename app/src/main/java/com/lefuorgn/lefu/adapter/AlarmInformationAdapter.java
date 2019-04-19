package com.lefuorgn.lefu.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.lefu.bean.AlarmInformation;

import java.util.List;

/**
 * 告警信息条目数据适配器
 */

public class AlarmInformationAdapter extends BaseQuickAdapter<AlarmInformation> {

    public AlarmInformationAdapter(List<AlarmInformation> data) {
        super(R.layout.item_activity_alarm_information, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, AlarmInformation info) {
        TextView name = holder.getView(R.id.tv_item_activity_alarm_info_name);
        name.setText(info.getName());
        name.setSelected(info.getCls() == null);
        name.setCompoundDrawablesWithIntrinsicBounds(info.getDrawableRes(), 0, 0, 0);
        holder.setText(R.id.tv_item_activity_alarm_info_num, info.getInfoNum() + "")
                .setVisible(R.id.tv_item_activity_alarm_info_num, info.getInfoNum() > 0)
                .setText(R.id.tv_item_activity_alarm_info_skip, info.getSkipInfo());
    }
}
