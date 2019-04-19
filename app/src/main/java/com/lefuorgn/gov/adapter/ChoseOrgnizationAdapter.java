package com.lefuorgn.gov.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.gov.bean.GovOrgInfoItem;
import com.lefuorgn.widget.tree.adapter.TreeListViewAdapter;
import com.lefuorgn.widget.tree.bean.Node;

import java.util.List;

/**
 * Created by liuting on 2017/1/5.
 */

public class ChoseOrgnizationAdapter extends TreeListViewAdapter<GovOrgInfoItem>{
    /**
     * @param layoutResId
     * @param mTree
     * @param datas
     * @param defaultExpandLevel 默认展开几级树
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public ChoseOrgnizationAdapter(int layoutResId, RecyclerView mTree, List<GovOrgInfoItem> datas, int defaultExpandLevel) throws IllegalArgumentException, IllegalAccessException {
        super(layoutResId, mTree, datas, defaultExpandLevel);
    }

    @Override
    protected void setConvertView(BaseViewHolder holder, Node node) {
        if (node.getIcon() == -1) {
            holder.getView(R.id.id_treenode_icon_gov).setVisibility(View.INVISIBLE);
        } else {
            holder.getView(R.id.id_treenode_icon_gov).setVisibility(View.VISIBLE);
            holder.setImageResource(R.id.id_treenode_icon_gov, node.getIcon());
        }
        holder.setText(R.id.id_treenode_label_gov,node.getName());
    }
}
