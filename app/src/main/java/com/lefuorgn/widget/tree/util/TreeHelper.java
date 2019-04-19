package com.lefuorgn.widget.tree.util;

import com.lefuorgn.R;
import com.lefuorgn.widget.tree.annotation.TreeNodeCId;
import com.lefuorgn.widget.tree.annotation.TreeNodeExtra1;
import com.lefuorgn.widget.tree.annotation.TreeNodeExtra2;
import com.lefuorgn.widget.tree.annotation.TreeNodeId;
import com.lefuorgn.widget.tree.annotation.TreeNodeIntExtra1;
import com.lefuorgn.widget.tree.annotation.TreeNodeLabel;
import com.lefuorgn.widget.tree.annotation.TreeNodeNum;
import com.lefuorgn.widget.tree.annotation.TreeNodePid;
import com.lefuorgn.widget.tree.bean.Node;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class TreeHelper {
	/**
	 * 传入我们的普通bean，转化为我们排序后的Node
	 * 
	 * @param datas
	 * @param defaultExpandLevel
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static <T> List<Node> getSortedNodes(List<T> datas,
			int defaultExpandLevel) throws IllegalArgumentException,
			IllegalAccessException

	{
		List<Node> result = new ArrayList<Node>();
		// 将用户数据转化为List<Node>
		List<Node> nodes = convetData2Node(datas);
		// 拿到根节点
		List<Node> rootNodes = getRootNodes(nodes);
		// 排序以及设置Node间关系
		for (Node node : rootNodes) {
			addNode(result, node, defaultExpandLevel, 1);
		}
		return result;
	}

	/**
	 * 过滤出所有可见的Node
	 * 
	 * @param nodes
	 * @return
	 */
	public static List<Node> filterVisibleNode(List<Node> nodes) {
		List<Node> result = new ArrayList<Node>();

		for (Node node : nodes) {
			// 如果为跟节点，或者上层目录为展开状态
			if (node.isRoot() || node.isParentExpand()) {
				setNodeIcon(node);
				result.add(node);
			}
		}
		return result;
	}

	/**
	 * 将我们的数据转化为树的节点
	 * 
	 * @param datas
	 * @return
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	private static <T> List<Node> convetData2Node(List<T> datas)
			throws IllegalArgumentException, IllegalAccessException

	{
		List<Node> nodes = new ArrayList<Node>();
		Node node = null;

		for (T t : datas) {
			long id = -1;
			long pId = -1;
			long cId = -1;
			String label = null;
			int num = -1;
			String extra1=null;
			String extra2=null;
			int intExtra1=-1;
			Class<? extends Object> clazz = t.getClass();
			Field[] declaredFields = clazz.getDeclaredFields();
			for (Field f : declaredFields) {
				if (f.getAnnotation(TreeNodeId.class) != null) {
					f.setAccessible(true);
					id = f.getLong(t);
				}
				if(f.getAnnotation(TreeNodeExtra1.class)!=null){
					f.setAccessible(true);
					extra1=(String)f.get(t);
				}
				if(f.getAnnotation(TreeNodeExtra2.class)!=null){
					f.setAccessible(true);
					extra2=(String)f.get(t);
				}
				if(f.getAnnotation(TreeNodeIntExtra1.class)!=null){
					f.setAccessible(true);
					intExtra1=f.getInt(t);
				}
				if (f.getAnnotation(TreeNodePid.class) != null) {
					f.setAccessible(true);
					pId = f.getLong(t);
				}
				if (f.getAnnotation(TreeNodeCId.class) != null) {
					f.setAccessible(true);
					cId = f.getLong(t);
				}
				if (f.getAnnotation(TreeNodeLabel.class) != null) {
					f.setAccessible(true);
					label = (String) f.get(t);
				}
				if (f.getAnnotation(TreeNodeNum.class) != null) {
					f.setAccessible(true);
					num = f.getInt(t);
				}
				if (id != -1 && pId != -1 && cId != -1 && label != null && num != -1) {
					break;
				}
			}
			node = new Node(id, pId, cId, label, num,extra1,extra2,intExtra1);
			nodes.add(node);
		}

		/**
		 * 设置Node间，父子关系;让每两个节点都比较一次，即可设置其中的关系
		 */
		for (int i = 0; i < nodes.size(); i++) {
			Node n = nodes.get(i);
			for (int j = i + 1; j < nodes.size(); j++) {
				Node m = nodes.get(j);
				if (m.getpId() == n.getId()) {
					n.getChildren().add(m);
					m.setParent(n);
				} else if (m.getId() == n.getpId()) {
					m.getChildren().add(n);
					n.setParent(m);
				}
			}
		}

		// 设置图片
		for (Node n : nodes) {
			setNodeIcon(n);
		}
		return nodes;
	}

	private static List<Node> getRootNodes(List<Node> nodes) {
		List<Node> root = new ArrayList<Node>();
		for (Node node : nodes) {
			if (node.isRoot())
				root.add(node);
		}
		return root;
	}

	/**
	 * 把一个节点上的所有的内容都挂上去
	 */
	private static void addNode(List<Node> nodes, Node node,
			int defaultExpandLeval, int currentLevel) {

		nodes.add(node);
		if (defaultExpandLeval >= currentLevel) {
			node.setExpand(true);
		}

		if (node.isLeaf())
			return;
		for (int i = 0; i < node.getChildren().size(); i++) {
			addNode(nodes, node.getChildren().get(i), defaultExpandLeval,
					currentLevel + 1);
		}
	}

	/**
	 * 设置节点的图标
	 * 
	 * @param node
	 */
	private static void setNodeIcon(Node node) {
		if (node.getChildren().size() > 0 && node.isExpand()) {
			node.setIcon(R.mipmap.tree_ex);
		} else if (node.getChildren().size() > 0 && !node.isExpand()) {
			node.setIcon(R.mipmap.tree_ec);
		} else
			node.setIcon(-1);

	}

}
