package com.lefuorgn.widget.tree.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 树形结构中节点
 */

public class Node implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;
    /**
     * 根节点pId为0
     */
    private long pId = 0;

    private long cId; // 政府版本中记录当前机构的真实ID,不是机构的ID都是0

    private String name;

    private int num; // 通讯录用于统计当前目录下员工号码的个数

    private boolean select; // 政府版本中记录当前条目是否被选中

    /**
     * 当前的级别
     */
    private int level;

    /**
     * 是否展开
     */
    private boolean isExpand = false;

    private int icon;

    /**
     * 下一级的子Node
     */
    private List<Node> children = new ArrayList<Node>();

    /**
     * 父Node
     */
    private Node parent;

    public Node() {
    }
    private String extra1;
    private String extra2;
    private int intExtra1;

    public int getIntExtra1() {
        return intExtra1;
    }

    public void setIntExtra1(int intExtra1) {
        this.intExtra1 = intExtra1;
    }

    public String getExtra1() {
        return extra1;
    }

    public void setExtra1(String extra1) {
        this.extra1 = extra1;
    }

    public String getExtra2() {
        return extra2;
    }

    public void setExtra2(String extra2) {
        this.extra2 = extra2;
    }

    public Node(long id, long pId, long cId, String name, int num) {
        super();
        this.id = id;
        this.pId = pId;
        this.cId = cId;
        this.name = name;
        this.setNum(num);
    }

    public Node(long id, long pId, long cId, String name, int num, String extra1, String extra2,int intExtra1) {
        this.id = id;
        this.pId = pId;
        this.cId = cId;
        this.name = name;
        this.num = num;
        this.extra1 = extra1;
        this.extra2 = extra2;
        this.intExtra1=intExtra1;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

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

    public long getcId() {
        return cId;
    }

    public void setcId(long cId) {
        this.cId = cId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    /**
     * 是否为跟节点
     *
     * @return
     */
    public boolean isRoot() {
        return parent == null;
    }

    /**
     * 判断父节点是否展开
     *
     * @return
     */
    public boolean isParentExpand() {
        if (parent == null)
            return false;
        return parent.isExpand();
    }

    /**
     * 是否是叶子界点
     *
     * @return
     */
    public boolean isLeaf() {
        return children.size() == 0;
    }

    /**
     * 获取level
     */
    public int getLevel() {
        return parent == null ? 0 : parent.getLevel() + 1;
    }

    /**
     * 设置展开
     *
     * @param isExpand
     */
    public void setExpand(boolean isExpand) {
        this.isExpand = isExpand;
        if (!isExpand) {

            for (Node node : children) {
                node.setExpand(isExpand);
            }
        }
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public boolean isSelect() {
        return select;
    }

    /**
     * 递归设置当前节点的选择状态
     * @param select 需要设置的状态
     * @param isUpward 是否可以向上递归
     * @param isDown 是否可以向下递归
     */
    public void setSelect(boolean select, boolean isUpward, boolean isDown) {
        this.select = select;
        if(isDown) {
            // 当前递归可以向下走
            for (Node node : children) {
                // 子类只能像上走
                node.setSelect(select, false, true);
            }
        }
        if(isUpward) {
            // 当前递归可以向上走
            if(parent != null) {
                if(select) {
                    boolean flag = true;
                    for (Node node : parent.getChildren()) {
                        if(!node.isSelect()) {
                            // 有节点未被选中, 则父亲不能被选中,并停止循环
                            flag = false;
                            break;
                        }
                    }
                    parent.setSelect(flag, true, false);
                }else {
                    parent.setSelect(select, true, false);
                }
            }
        }
    }

    public Node(long id, long pId, long cId, String name, int num, int level, int intExtra1, String extra1, String extra2, Node parent) {
        this.id = id;
        this.pId = pId;
        this.cId = cId;
        this.name = name;
        this.num = num;
        this.level = level;
        this.intExtra1 = intExtra1;
        this.extra1 = extra1;
        this.extra2 = extra2;
        this.parent = parent;
    }
}
