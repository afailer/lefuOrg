package com.lefuorgn.lefu.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.db.model.basic.OldPeople;

import java.util.ArrayList;

/**
 * 搜索老人内容展示适配器
 */

public class PersonalDataSearchAdapter extends BaseQuickAdapter<OldPeople> {

    public PersonalDataSearchAdapter() {
        super(R.layout.item_activity_personal_data_search, new ArrayList<OldPeople>());
    }

    @Override
    protected void convert(BaseViewHolder holder, OldPeople oldPeople) {
        holder.setText(R.id.tv_item_activity_personal_data_search, oldPeople.getElderly_name());
        if(!"".equals(oldPeople.getRoom_no()) && !"".equals(oldPeople.getBed_no())){
            holder.setText(R.id.tv_item_activity_personal_room_bed_no_search,oldPeople.getRoom_no()+"号房 "+oldPeople.getBed_no()+"号床");
        }
    }
}
