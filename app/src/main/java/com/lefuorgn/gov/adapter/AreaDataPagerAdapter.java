package com.lefuorgn.gov.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lefuorgn.AppContext;
import com.lefuorgn.R;
import com.lefuorgn.gov.bean.AllDataItem;
import com.lefuorgn.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 辖区数据页面适配器
 */

public abstract class AreaDataPagerAdapter extends PagerAdapter{
    private List<AllDataItem> items=new ArrayList<AllDataItem>();
    private AppContext appContext;

    public AreaDataPagerAdapter(List<AllDataItem> items){
        appContext=AppContext.getInstance();
        this.items.addAll(items);
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        RelativeLayout view = (RelativeLayout) View.inflate(appContext, R.layout.all_data, null);
        final TextView value = (TextView) view.findViewById(R.id.gov_alldata_value);
        final TextView valueDetail= (TextView) view.findViewById(R.id.gov_alldata_value_detail);
        final AllDataItem allDataItem = items.get(position);
        value.setText(allDataItem.getValue());

        valueDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                skipChartActivity(allDataItem);
            }
        });
        value.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                skipChartActivity(allDataItem);
            }
        });
        TextView type = (TextView) view.findViewById(R.id.gov_alldata_type);
        type.setText(items.get(position).getType());
        TextView time = (TextView) view.findViewById(R.id.gov_alldata_time);
        time.setText(String.format("更新时间%s", StringUtils
                .getFormatData(items.get(position).getTime(), "yyyy.MM.dd")));
        container.addView(view);
        return view;
    }

    public abstract void skipChartActivity(AllDataItem item);

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0==arg1;
    }
}
