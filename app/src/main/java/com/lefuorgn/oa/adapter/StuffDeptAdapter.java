package com.lefuorgn.oa.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.oa.activity.SelectStuffActivity;
import com.lefuorgn.oa.bean.DeptNode;
import com.lefuorgn.widget.tree.adapter.TreeListViewAdapter;
import com.lefuorgn.widget.tree.bean.Node;

import java.util.List;

/**
 * Created by liuting on 2017/4/1.
 */

public class StuffDeptAdapter extends TreeListViewAdapter<DeptNode>{
    /**
     * @param layoutResId
     * @param mTree
     * @param datas
     * @param defaultExpandLevel 默认展开几级树
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public StuffDeptAdapter(int layoutResId, RecyclerView mTree, List<DeptNode> datas, int defaultExpandLevel) throws IllegalArgumentException, IllegalAccessException {
        super(layoutResId, mTree, datas, defaultExpandLevel);
    }

    @Override
    protected void setConvertView(BaseViewHolder holder,final Node node) {
        final ImageView selector = holder.getView(R.id.stuff_select);
        //final ImageView selector=holder.getView(R.id.id_treenode_select_gov);
        TextView lable=holder.getView(R.id.stuff_name);
        holder.getView(R.id.tv_item_fragment_contact_letter).setVisibility(View.GONE);
        holder.getView(R.id.dept_name).setVisibility(View.GONE);
        holder.getView(R.id.pos_name).setVisibility(View.GONE);
        selector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                node.setSelect(!node.isSelect(),true,true);
                if(node.isSelect()){
                    selector.setImageResource(R.mipmap.gov_select);
                }else{
                    selector.setImageResource(R.mipmap.gov_normal);
                }
                StuffDeptAdapter.this.notifyDataSetChanged();
            }

        });
        if(node.isSelect()){
            selector.setImageResource(R.mipmap.gov_select);
        }else{
            selector.setImageResource(R.mipmap.gov_normal);
        }
        if(node.isLeaf()){
            holder.getView(R.id.open_dept).setVisibility(View.GONE);
        }else{
            holder.getView(R.id.open_dept).setVisibility(View.VISIBLE);
        }
        lable.setText(node.getName());
    }
    /**
            * 获取当前被选中的机构ID集合
    * @return 返回的格式是 : ""或者"1,2,3"
            */
    public String getCheckedNodes() {
        StringBuilder sb=new StringBuilder();
        for (Node node : getAllNodes()) {
            if(node.isLeaf() && node.isSelect()) {
                sb.append(node.getId()+","+node.getName()+ SelectStuffActivity.DIVIDE_STR);
            }
        }
        return sb.toString();
    }
}
