package com.lefuorgn.gov.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.gov.bean.Organization;

import java.util.List;

/**
 * Created by liuting on 2017/1/16.
 */

public class SeekAdapter extends BaseQuickAdapter<Organization>{

    public SeekAdapter(int layoutResId, List<Organization> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Organization organization) {
        baseViewHolder.setText(R.id.name_item_activity_seek_org,organization.getAgency_name());
    }
}
