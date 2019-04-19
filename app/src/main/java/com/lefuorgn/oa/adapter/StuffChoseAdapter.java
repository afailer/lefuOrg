package com.lefuorgn.oa.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.oa.bean.DeptNode;
import com.lefuorgn.widget.tree.adapter.TreeListViewAdapter;
import com.lefuorgn.widget.tree.bean.Node;
import java.util.List;

/**
 * Created by liuting on 2017/4/6.
 */

public class StuffChoseAdapter extends TreeListViewAdapter<DeptNode> {
    /**
     * @param layoutResId
     * @param mTree
     * @param datas
     * @param defaultExpandLevel 默认展开几级树
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public StuffChoseAdapter(int layoutResId, RecyclerView mTree, List<DeptNode> datas, int defaultExpandLevel) throws IllegalArgumentException, IllegalAccessException {
        super(layoutResId, mTree, datas, defaultExpandLevel);
    }

    @Override
    protected void setConvertView(BaseViewHolder holder,final Node node) {
        final ImageView goneView = holder.getView(R.id.stuff_select);
        goneView.setVisibility(View.GONE);
        holder.getView(R.id.dept_name).setVisibility(View.GONE);
        if(node.getExtra2()!=null && !"".equals(node.getExtra2())){
            holder.setText(R.id.pos_name,node.getExtra2());
        }else{
            holder.getView(R.id.pos_name).setVisibility(View.GONE);
        }
        if(node.getNum()<0){
            holder.getView(R.id.open_dept).setVisibility(View.GONE);
        }else{
            holder.getView(R.id.open_dept).setVisibility(View.VISIBLE);
        }
        holder.setText(R.id.stuff_name,node.getName());
    }
}
