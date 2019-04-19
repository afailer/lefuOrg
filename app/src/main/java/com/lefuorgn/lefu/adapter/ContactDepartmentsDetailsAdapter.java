package com.lefuorgn.lefu.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.lefu.bean.Staff;

import java.util.List;

/**
 * 通讯录部门下员工列表适配器
 */

public class ContactDepartmentsDetailsAdapter extends BaseQuickAdapter<Staff> {

    public ContactDepartmentsDetailsAdapter(List<Staff> data) {
        super(R.layout.item_activity_contact_department_details, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Staff staff) {
        holder.setText(R.id.tv_item_activity_contact_dept_details_name, staff.getStaff_name())
                .setText(R.id.tv_item_activity_contact_dept_details_dept, staff.getDept_name())
                .setText(R.id.tv_item_activity_contact_dept_details_post, staff.getPost_name());
    }
}
