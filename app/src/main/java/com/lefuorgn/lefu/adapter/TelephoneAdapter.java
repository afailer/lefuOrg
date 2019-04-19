package com.lefuorgn.lefu.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.lefu.bean.FamilyTelephone;

import java.util.List;

/**
 * 联系人数据适配器
 */

public class TelephoneAdapter extends BaseQuickAdapter<FamilyTelephone> {

    public TelephoneAdapter(List<FamilyTelephone> data) {
        super(R.layout.item_activity_telephone, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, FamilyTelephone phone) {
        holder.setText(R.id.tv_item_activity_telephone_name, phone.getName())
                .setText(R.id.tv_item_activity_telephone_phone, phone.getTelephone());
    }
}
