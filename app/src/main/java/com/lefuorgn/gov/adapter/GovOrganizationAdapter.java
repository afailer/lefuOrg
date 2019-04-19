package com.lefuorgn.gov.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.gov.bean.GovOrgInfoItem;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.widget.tree.adapter.TreeListViewAdapter;
import com.lefuorgn.widget.tree.bean.Node;

import java.util.List;

/**
 * Created by liuting on 2016/12/28.
 */

public class GovOrganizationAdapter extends TreeListViewAdapter<GovOrgInfoItem>{
    /**
     * @param layoutResId
     * @param mTree
     * @param datas
     * @param defaultExpandLevel 默认展开几级树
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public GovOrganizationAdapter(int layoutResId, RecyclerView mTree, List<GovOrgInfoItem> datas, int defaultExpandLevel) throws IllegalArgumentException, IllegalAccessException {
        super(layoutResId, mTree, datas, defaultExpandLevel);
    }

    @Override
    protected void setConvertView(BaseViewHolder holder, final Node node) {
        ImageView icon = holder.getView(R.id.id_treenode_icon_gov);
        final ImageView selector=holder.getView(R.id.id_treenode_select_gov);
        TextView lable=holder.getView(R.id.id_treenode_label_gov);
        if(node.getIcon()==-1){
            icon.setVisibility(View.INVISIBLE);
        }else{
            icon.setVisibility(View.INVISIBLE);
            icon.setImageResource(node.getIcon());
        }
            selector.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    node.setSelect(!node.isSelect(),true,true);
                    if(node.isSelect()){
                        selector.setImageResource(R.mipmap.gov_select);
                    }else{
                        selector.setImageResource(R.mipmap.gov_normal);
                    }
                    GovOrganizationAdapter.this.notifyDataSetChanged();
                }

            });
        selector.setImageResource(R.mipmap.gov_normal);
        if(node.isSelect()){
            selector.setImageResource(R.mipmap.gov_select);
        }else{
            selector.setImageResource(R.mipmap.gov_normal);
        }
        lable.setText(node.getName());
    }
    /**
     * 获取当前被选中的机构ID集合
     * @return 返回的格式是 : ""或者"1,2,3"
     */
    public String getCheckedIds() {
        StringBuilder sb = new StringBuilder();
        for (Node node : getAllNodes()) {
            if(node.isLeaf() && node.isSelect()) {
                sb.append(node.getcId() + ",");
            }
        }
        String str = sb.toString();
        if(!StringUtils.isEmpty(str)) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }
}
