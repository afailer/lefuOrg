package com.lefuorgn.gov.bean;

import java.io.Serializable;
import com.lefuorgn.widget.tree.annotation.TreeNodeCId;
import com.lefuorgn.widget.tree.annotation.TreeNodeId;
import com.lefuorgn.widget.tree.annotation.TreeNodeLabel;
import com.lefuorgn.widget.tree.annotation.TreeNodePid;

public class GovOrgInfoItem implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@TreeNodeId
	private long id;
	@TreeNodePid
	private long pId = 0; // 根节点pId为0
	@TreeNodeLabel
	private String name;
	@TreeNodeCId
	private long mId; // 当前叶子节点的真实ID
	private boolean select;
	private boolean leafNode; // 当前节点是叶子节点

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getpId() {
		return pId;
	}

	public void setpId(long pId) {
		this.pId = pId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSelect() {
		return select;
	}

	public void setSelect(boolean select) {
		this.select = select;
	}

	public long getmId() {
		return mId;
	}

	public void setmId(long mId) {
		this.mId = mId;
	}

	public boolean isLeafNode() {
		return leafNode;
	}

	public void setLeafNode(boolean leafNode) {
		this.leafNode = leafNode;
	}

	@Override
	public String toString() {
		return "GovOrgInfoItem [id=" + id + ", pId=" + pId + ", name=" + name
				+ ", mId=" + mId + ", select=" + select + ", leafNode="
				+ leafNode + "]";
	}

}
