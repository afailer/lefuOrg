package com.lefuorgn.viewloader.adapter;

import android.content.Context;
import android.widget.TextView;

import com.lefuorgn.R;
import com.lefuorgn.viewloader.base.BaseSpinnerAdapter;
import com.lefuorgn.viewloader.base.SpinnerViewHolder;
import com.lefuorgn.viewloader.bean.SpinnerData;

import java.util.List;

/**
 * 下拉菜单数据适配器
 */

public class SelectViewAdapter extends BaseSpinnerAdapter<SpinnerData> {

    private float mValueSize;
    private int mValueColor;
    private OnSelectDataListener<SpinnerData> mListener;

    public SelectViewAdapter(Context context, float valueSize, int valueColor, List<SpinnerData> data) {
        super(context, R.layout.builder_label_spinner, data);
        this.mValueSize = valueSize;
        this.mValueColor = valueColor;
    }

    @Override
    public String getSelectText(int position) {
        SpinnerData data = (SpinnerData) getItem(position);
        if(mListener != null) {
            mListener.onSelectData(data, position);
        }
        return data.getLabel();
    }

    @Override
    protected void convert(SpinnerViewHolder holder, SpinnerData data) {
        TextView view = (TextView) holder.getView(R.id.tv_builder_label);
        view.setTextSize(mValueSize);
        view.setTextColor(mValueColor);
        view.setText(data.getLabel());
    }

    public void setOnSelectDataListener(OnSelectDataListener<SpinnerData> listener) {
        mListener = listener;
    }

}
