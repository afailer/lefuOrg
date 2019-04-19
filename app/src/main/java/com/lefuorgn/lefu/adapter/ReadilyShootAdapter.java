package com.lefuorgn.lefu.adapter;

import android.content.Context;
import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.db.model.basic.DisplaySignOrNursingItem;
import com.lefuorgn.lefu.base.BaseGridAdapter;
import com.lefuorgn.lefu.bean.NursingInfo;

import java.util.List;

/**
 * 随手拍信息数据适配器
 */
public class ReadilyShootAdapter extends BaseGridAdapter<NursingInfo> {

    public ReadilyShootAdapter(Context context, List<NursingInfo> data, List<DisplaySignOrNursingItem> items) {
        super(context, R.layout.item_activity_readily_shoot, data, items);
    }

    @Override
    protected void convert(BaseViewHolder holder, final NursingInfo nursingInfo) {
        final int position = holder.getLayoutPosition() - this.getHeaderViewsCount();
        int time = nursingInfo.getNursingItemInfoList().get(0).getNurse_times();
        final View view = holder.getView(R.id.tv_item_activity_readily_shoot_value);
        holder.setText(R.id.tv_item_activity_readily_shoot_name, nursingInfo.getOldPeopleName())
                .setOnClickListener(R.id.tv_item_activity_readily_shoot_name, new OnItemChildClickListener())
                .setText(R.id.tv_item_activity_readily_shoot_value, time == 0 ? "" : time + "")
                .setOnClickListener(R.id.tv_item_activity_readily_shoot_value, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mListener != null) {
                            mListener.onClick(view, nursingInfo, position, 0);
                        }
                    }
                });
    }

}
