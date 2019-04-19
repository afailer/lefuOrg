package com.lefuorgn.lefu.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.lefu.bean.NursingOldPeople;

import java.util.List;

/**
 * 护理信息中dialog展示老人信息适配器
 */

public class NursingOldPeopleAdapter extends BaseQuickAdapter<NursingOldPeople> {

    public NursingOldPeopleAdapter(List<NursingOldPeople> data) {
        super(R.layout.dialog_nursing_oldpeople, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, NursingOldPeople n) {
        holder.setText(R.id.tv_dialog_nursing_old_people, n.getOld_people_name())
                .setOnClickListener(R.id.iv_dialog_nursing_close, new OnItemChildClickListener());
    }
}
