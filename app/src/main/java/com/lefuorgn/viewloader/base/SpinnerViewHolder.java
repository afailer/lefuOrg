package com.lefuorgn.viewloader.base;

import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

/**
 * 控件缓存器
 */

public class SpinnerViewHolder {

    private final SparseArray<View> views = new SparseArray<View>();
    private View convertView;

    public SpinnerViewHolder(View view) {
        convertView = view;
        convertView.setTag(this);
    }

    public SpinnerViewHolder setText(int viewId, CharSequence value) {
        TextView view = (TextView)this.getView(viewId);
        view.setText(value);
        return this;
    }

    public View getView(int viewId) {
        View view = this.views.get(viewId);
        if(view == null) {
            view = this.convertView.findViewById(viewId);
            this.views.put(viewId, view);
        }
        return view;
    }

}
