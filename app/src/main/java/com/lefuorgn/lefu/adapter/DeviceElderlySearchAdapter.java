package com.lefuorgn.lefu.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.lefu.bean.DeviceElderly;

import java.util.ArrayList;

/**
 * 搜索老人内容展示适配器
 */

public class DeviceElderlySearchAdapter extends BaseQuickAdapter<DeviceElderly> {

    public DeviceElderlySearchAdapter() {
        super(R.layout.item_activity_personal_data_search, new ArrayList<DeviceElderly>());
    }

    @Override
    protected void convert(BaseViewHolder holder, DeviceElderly elderly) {
        holder.setText(R.id.tv_item_activity_personal_data_search, elderly.getOlder_name());
    }
}
