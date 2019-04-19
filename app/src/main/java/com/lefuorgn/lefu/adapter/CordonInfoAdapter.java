package com.lefuorgn.lefu.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.j256.ormlite.dao.ForeignCollection;
import com.lefuorgn.R;
import com.lefuorgn.db.model.basic.SignConfig;
import com.lefuorgn.db.model.basic.SignIntervalPointColor;
import com.lefuorgn.db.util.SignConfigManager;
import com.lefuorgn.util.StringUtils;

import java.util.List;

/**
 * 老人体征数据警戒值适配器
 */

public class CordonInfoAdapter extends BaseQuickAdapter<SignConfig> {

    public CordonInfoAdapter(List<SignConfig> data) {
        super(R.layout.item_activity_cordon_info, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, SignConfig s) {
        ForeignCollection<SignIntervalPointColor> f = s.getfLine();
        double low = 0, high = 0;
        boolean first = true;
        for (SignIntervalPointColor item : f) {
            if("#03CF97".equals(item.getColor())) {
                double value = item.getValue();
                if(first) {
                    first = false;
                    low = value;
                    high = value;
                }else {
                    if(low > value) {
                        low = value;
                    }else if(high < value) {
                        high = value;
                    }
                }
            }
        }
        holder.setText(R.id.tv_item_activity_cordon_info_title,
                        SignConfigManager.getTitle(s.getType()))
                .setText(R.id.tv_item_activity_cordon_info_low,
                        StringUtils.numberFormat((int) s.getAccur(), low))
                .setText(R.id.tv_item_activity_cordon_info_high,
                        StringUtils.numberFormat((int) s.getAccur(), high));
    }
}
