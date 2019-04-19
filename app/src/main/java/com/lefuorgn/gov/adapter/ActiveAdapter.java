package com.lefuorgn.gov.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.api.remote.ImageLoader;
import com.lefuorgn.gov.bean.OrgActive;
import com.lefuorgn.util.StringUtils;

import java.util.List;

/**
 * Created by liuting on 2016/12/28.
 */

public class ActiveAdapter extends BaseQuickAdapter<OrgActive>{

    public ActiveAdapter(int layoutResId, List<OrgActive> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final OrgActive orgActive) {
        ImageView img = baseViewHolder.getView(R.id.item_img);
        TextView title = baseViewHolder.getView(R.id.item_title);
        TextView time = baseViewHolder.getView(R.id.item_holdtime);
        baseViewHolder.getView(R.id.item_type).setVisibility(View.GONE);
        ImageLoader.loadImg(orgActive.getPic(),img);
        title.setText(orgActive.getTheme());
        time.setText(StringUtils.getFormatData(orgActive.getHoldTime(), "yyyy年MM月dd日"));
    }
}
