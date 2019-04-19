package com.lefuorgn.gov.adapter;

import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.gov.bean.AllDataItem;
import com.lefuorgn.util.StringUtils;

import java.util.List;

/**
 * 辖区数据信息适配器
 */

public class AreaDataAdapter extends BaseQuickAdapter<AllDataItem>{

    public AreaDataAdapter(int layoutResId, List<AllDataItem> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, AllDataItem allDataItem) {
            RelativeLayout title = baseViewHolder.getView(R.id.all_title);
            if(allDataItem.getCountName().equals("")){
                title.setVisibility(View.GONE);
            }else{
                title.setVisibility(View.VISIBLE);
                baseViewHolder.setText(R.id.all_data_type_name,allDataItem.getCountName());
            }
            baseViewHolder.setText(R.id.gov_all_item_type,allDataItem.getType())
                    .setText(R.id.gov_all_item_value,allDataItem.getValue())
                    .setText(R.id.gov_all_item_time, String.format("更新时间%s", StringUtils
                            .getFormatData(allDataItem.getTime(), "yyyy.MM.dd")))
                    .setImageResource(R.id.gov_all_item_icon, allDataItem.getIcon());
    }
}
