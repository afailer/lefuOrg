package com.lefuorgn.viewloader.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;


/**
 * 下拉菜单数据适配器
 */

public abstract class BaseSpinnerAdapter<T> extends BaseAdapter {

    private List<T> mData;
    /**
     * 显示内容布局
     */
    private int mResource;

    private LayoutInflater mInflater;

    public BaseSpinnerAdapter(Context context, int resource, List<T> data) {
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mResource = resource;
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(mResource, parent, false);
        }
        SpinnerViewHolder viewHolder = (SpinnerViewHolder) convertView.getTag();
        if(viewHolder == null) {
            viewHolder = new SpinnerViewHolder(convertView);
        }
        convert(viewHolder, mData.get(position));
        return convertView;
    }

    protected abstract void convert(SpinnerViewHolder holder, T t);

    public abstract String getSelectText(int position);

    public interface OnSelectDataListener<T> {
        void onSelectData(T t, int position);
    }

}
