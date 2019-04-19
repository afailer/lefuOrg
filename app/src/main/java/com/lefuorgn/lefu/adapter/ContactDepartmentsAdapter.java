package com.lefuorgn.lefu.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.lefu.activity.ContactDepartmentsDetailsActivity;
import com.lefuorgn.lefu.bean.Department;
import com.lefuorgn.widget.tree.adapter.TreeListViewAdapter;
import com.lefuorgn.widget.tree.bean.Node;

import java.util.List;

/**
 * 部门联系人适配器
 */

public class ContactDepartmentsAdapter extends TreeListViewAdapter<Department> {

    public ContactDepartmentsAdapter(RecyclerView mTree, List<Department> datas)
            throws IllegalArgumentException, IllegalAccessException {
        super(R.layout.item_fragment_contact_departments, mTree, datas, 0);
    }

    @Override
    protected void setConvertView(BaseViewHolder holder, final Node node) {
        if (node.getIcon() == -1) {
            holder.getView(R.id.id_tree_node_icon).setVisibility(View.INVISIBLE);
        } else {
            holder.getView(R.id.id_tree_node_icon).setVisibility(View.VISIBLE);
            ((ImageView) holder.getView(R.id.id_tree_node_icon)).setImageResource(node.getIcon());
        }
        if(!node.isLeaf()) {
            // 非叶子节点
            holder.getView(R.id.id_tree_node_num).setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ContactDepartmentsDetailsActivity.class);
                    intent.putExtra("node", node);
                    mContext.startActivity(intent);
                }
            });
        }else {
            holder.getView(R.id.id_tree_node_num).setClickable(false);
        }
        holder.setText(R.id.id_tree_node_num, node.getNum() + "")
                .setText(R.id.id_tree_node_label, node.getName());

    }
}
