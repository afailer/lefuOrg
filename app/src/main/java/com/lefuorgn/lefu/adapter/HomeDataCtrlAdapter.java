package com.lefuorgn.lefu.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.gov.bean.IconItem;

import java.util.List;

/**
 * Created by liuting on 2017/1/20.
 */

public class HomeDataCtrlAdapter extends BaseQuickAdapter<IconItem>{

    public HomeDataCtrlAdapter(int layoutResId, List<IconItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, IconItem iconItem) {

    }
}
