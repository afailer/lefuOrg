package com.lefuorgn.gov.adapter;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.AppContext;
import com.lefuorgn.R;
import com.lefuorgn.gov.bean.SettingItem;
import java.util.List;

/**
 * Created by liuting on 2017/1/6.
 */

public class SettingAdapter extends BaseQuickAdapter<SettingItem>{

    public SettingAdapter(int layoutResId, List<SettingItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final SettingItem settingItem) {
        TextView view = baseViewHolder.getView(R.id.grid_item);
        Drawable top = AppContext.getInstance().getResources().getDrawable(settingItem.getSettingImg());
        view.setCompoundDrawablesWithIntrinsicBounds(null, top , null, null);
        view.setCompoundDrawablePadding(9);
        view.setText(settingItem.getSettingText());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingItem.click();
            }
        });
    }
}
