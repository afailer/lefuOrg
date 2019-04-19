package com.lefuorgn.lefu.fragment;

import android.content.Intent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.base.BaseContactFragment;
import com.lefuorgn.lefu.activity.ContactDetailsActivity;
import com.lefuorgn.lefu.bean.Staff;
import com.lefuorgn.util.PinyinComparator;
import com.lefuorgn.util.PinyinUtils;

import java.util.Collections;
import java.util.List;

/**
 * 通讯录中全部联系人页面
 */

public class ContactAllFragment extends BaseContactFragment<Staff> {

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_fragment_all_contact;
    }

    @Override
    protected void initData() {
        showWaitDialog();
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Intent intent = new Intent(getActivity(), ContactDetailsActivity.class);
                intent.putExtra("staff", (Staff) mAdapter.getData().get(i));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void convert(BaseViewHolder holder, Staff staff, boolean flag) {
        holder.setText(R.id.tv_item_fragment_contact_name, staff.getStaff_name())
                .setText(R.id.tv_item_fragment_contact_letter, staff.getSortLetters())
                .setText(R.id.tv_item_fragment_contact_dept, staff.getDept_name())
                .setText(R.id.tv_item_fragment_contact_post, staff.getPost_name());
        holder.getView(R.id.tv_item_fragment_contact_letter).setVisibility(flag ? View.VISIBLE : View.GONE);

    }

    /**
     * 设置员工结果
     * @param result 员工数据
     */
    public void setResult(List<Staff> result) {
        hideWaitDialog();
        if(result == null) {
            return;
        }
        // 将名称转换成拼音
        PinyinUtils.convertedToPinyin(result);
        // 排序
        Collections.sort(result, new PinyinComparator<Staff>());
        setData(result);
    }

}
