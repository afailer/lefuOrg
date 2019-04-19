package com.lefuorgn.gov.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.bean.UseRateItem;

import java.util.List;

/**
 * Created by liuting on 2017/1/3.
 */

public class ChartBedUseRateAdapter extends BaseQuickAdapter<UseRateItem>{
    public ChartBedUseRateAdapter(int layoutResId, List<UseRateItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, UseRateItem item) {
        baseViewHolder.setText(R.id.chart_recycle_time_year,item.getTime().substring(0, 4));
        baseViewHolder.setText(R.id.chart_recycle_time_month,item.getTime().subSequence(item.getTime().length()-3,item.getTime().length()));
        baseViewHolder.setText(R.id.chart_recycle_bed_left,item.getBedLeftNum());
        baseViewHolder.setText(R.id.chart_recycle_bed_num,item.getBedTotalNum());
        baseViewHolder.setText(R.id.chart_recycle_use_rate,item.getBedLiveRate());
    }
}
