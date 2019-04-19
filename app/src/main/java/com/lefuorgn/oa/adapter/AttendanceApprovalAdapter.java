package com.lefuorgn.oa.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.api.remote.ImageLoader;
import com.lefuorgn.oa.bean.AttendanceApprovalType;

import java.util.List;

/**
 * 审批类型数据适配器
 */

public class AttendanceApprovalAdapter extends BaseQuickAdapter<AttendanceApprovalType> {

    public AttendanceApprovalAdapter(List<AttendanceApprovalType> data) {
        super(R.layout.item_activity_attendance_approval, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, AttendanceApprovalType type) {
        holder.setText(R.id.tv_item_activity_attendance_approval, type.getName());
        ImageView img = holder.getView(R.id.iv_item_activity_attendance_approval);
        ImageLoader.loadOAImage(type.getLogo(), img);
    }
}
