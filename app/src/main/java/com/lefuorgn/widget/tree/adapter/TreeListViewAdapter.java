package com.lefuorgn.widget.tree.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.AppContext;
import com.lefuorgn.widget.tree.bean.Node;
import com.lefuorgn.widget.tree.util.TreeHelper;

import java.util.ArrayList;
import java.util.List;


public abstract class TreeListViewAdapter<T> extends BaseQuickAdapter<Node> {

	/**
	 * 存储所有的Node
	 */
	protected List<Node> mAllNodes;

	/**
	 * 点击的回调接口
	 */
	private OnTreeNodeClickListener onTreeNodeClickListener;

	public interface OnTreeNodeClickListener {
		void onClick(Node node, View view, int position);
	}

	public void setOnTreeNodeClickListener(
			OnTreeNodeClickListener onTreeNodeClickListener) {
		this.onTreeNodeClickListener = onTreeNodeClickListener;
	}

	/**
	 * 
	 * @param mTree
	 * @param datas
	 * @param defaultExpandLevel
	 *            默认展开几级树
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public TreeListViewAdapter(int layoutResId, RecyclerView mTree, List<T> datas,
							   int defaultExpandLevel) throws IllegalArgumentException,
			IllegalAccessException {
		super(layoutResId, new ArrayList<Node>());
		/**
		 * 对所有的Node进行排序
		 */
		mAllNodes = TreeHelper.getSortedNodes(datas, defaultExpandLevel);

		/**
		 * 过滤出可见的Node
		 */
		mTree.setLayoutManager(new LinearLayoutManager(AppContext.getInstance()));
		mTree.setAdapter(this);

		/**
		 * 设置节点点击时，可以展开以及关闭；并且将ItemClick事件继续往外公布
		 */
        setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Node node = (Node) getData().get(position);
                expandOrCollapse(node);

				if (onTreeNodeClickListener != null) {
					onTreeNodeClickListener.onClick(node, view, position);
				}
            }
        });
        setNewData(TreeHelper.filterVisibleNode(mAllNodes));

	}

	/**
	 * 相应ListView的点击事件 展开或关闭某节点
	 */
	public void expandOrCollapse(Node node) {
        if (!node.isLeaf()) {
            node.setExpand(!node.isExpand());
            setNewData(TreeHelper.filterVisibleNode(mAllNodes));
        }
	}
	/**
	 * 获取所有的节点信息
	 * @return
	 */
	protected List<Node> getAllNodes() {
		if(mAllNodes == null) {
			new ArrayList<Node>();
		}
		return mAllNodes;
	}

	@Override
	protected void convert(BaseViewHolder baseViewHolder, Node node) {
		View convertView = baseViewHolder.getConvertView();
		// 设置内边距
		convertView.setPadding(node.getLevel() * 30, 3, 3, 3);
		setConvertView(baseViewHolder, node);
	}

	protected abstract void setConvertView(BaseViewHolder holder, Node node);

}
