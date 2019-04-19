package com.lefuorgn.lefu.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.lefu.bean.Estimate;
import com.lefuorgn.util.StringUtils;

import java.util.List;

/**
 * 评估表历史信息适配器
 */

public class EstimateDetailsAdapter extends BaseQuickAdapter<Estimate> {


    public EstimateDetailsAdapter(List<Estimate> data) {
        super(R.layout.item_fragment_estimate_details, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Estimate e) {
        holder.setText(R.id.tv_item_fragment_estimate_details_score, e.getSum() + "")
                .setText(R.id.tv_item_fragment_estimate_details_name, e.getInspect_user_name())
                .setText(R.id.tv_item_fragment_estimate_details_date,
                        StringUtils.getFormatData(e.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
    }
}
