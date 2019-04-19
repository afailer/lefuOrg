package com.lefuorgn.lefu.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.db.model.basic.OldPeopleFamily;
import com.lefuorgn.db.util.DictionaryManager;

import java.util.List;

/**
 * 老人家属信息适配器
 */

public class FamilyInfoAdapter extends BaseQuickAdapter<OldPeopleFamily> {

    public FamilyInfoAdapter(List<OldPeopleFamily> data) {
        super(R.layout.item_activity_family_info, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, OldPeopleFamily f) {
        holder.setText(R.id.tv_item_activity_family_info_name, f.getRelatives_name())
                .setText(R.id.tv_item_activity_family_info_sex, DictionaryManager.getContent(f.getGender()))
                .setText(R.id.tv_item_activity_family_info_first, f.getFirst_contacts() == 1 ? "是" : "")
                .setText(R.id.tv_item_activity_family_info_relationship, f.getElderly_relationship())
                .setText(R.id.tv_item_activity_family_info_phone, f.getMobile())
                .setText(R.id.tv_item_activity_family_info_tel, f.getTelephone());
    }
}
