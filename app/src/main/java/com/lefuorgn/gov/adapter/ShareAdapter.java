package com.lefuorgn.gov.adapter;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.AppContext;
import com.lefuorgn.R;
import com.lefuorgn.gov.bean.ShareItem;
import com.lefuorgn.util.ShareUtils;

import java.util.List;

/**
 * Created by liuting on 2017/1/11.
 */

public class ShareAdapter extends BaseQuickAdapter<ShareItem>{
    public ShareAdapter(int layoutResId, List<ShareItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder,final ShareItem shareItem) {
        TextView gridItem=baseViewHolder.getView(R.id.grid_item);
        gridItem.setTextSize(16);
        gridItem.setPadding(10,20,10,20);
        Drawable top = AppContext.getInstance().getResources().getDrawable(shareItem.getResIcon());
        gridItem.setCompoundDrawablesWithIntrinsicBounds(null, top , null, null);
        gridItem.setCompoundDrawablePadding(6);
        gridItem.setText(shareItem.getName());
        gridItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtils.shareMessage(shareItem);
            }
        });


    }
}
