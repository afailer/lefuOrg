package com.lefuorgn.lefu.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.lefu.bean.OldPeopleInfo;

import java.util.List;

/**
 * 老人信息和住院信息适配器
 */

public class ElderlyInfoAdapter extends BaseQuickAdapter<OldPeopleInfo> {

    public ElderlyInfoAdapter(List<OldPeopleInfo> data) {
        super(R.layout.item_activity_elderly_info, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, OldPeopleInfo info) {
        baseViewHolder.setText(R.id.tv_item_activity_elderly_info_key, info.getKey())
                .setText(R.id.tv_item_activity_elderly_info_value, info.getValue());
    }
}
