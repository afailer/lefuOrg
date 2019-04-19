package com.lefuorgn.lefu.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;

import java.util.List;

/**
 * 老人病史信息适配器
 */

public class MedicalHistoryInfoAdapter extends BaseQuickAdapter<String> {

    public MedicalHistoryInfoAdapter(List<String> data) {
        super(R.layout.item_activity_medical_history_info, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, String s) {
        holder.setText(R.id.tv_item_activity_medical_history_info, s);
    }
}
