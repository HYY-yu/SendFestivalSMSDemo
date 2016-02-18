package com.app.feng.treelistview.treeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 建立树的节点
 * Created by feng on 2015/11/11.
 */
public class Node {
    //本节点的ID
    private int id;
    //父节点的ID 默认都是根节点
    private int pId = 0;

    private String name;
    //本节点的层级
    private int level;

    //是否展开 默认不展开
    private boolean isExpand = false;

    //展开的ICON
    private int icon;

    //父节点
    private Node parent;
    //子节点集
    private List<Node> childrens = new ArrayList<>();

    /*
    构造
     */
    public Node() {

    }

    public Node(int id, int pId, String name) {
        this();
        this.id = id;
        this.pId = pId;
        this.name = name;
    }
    /*
     * Get Set 集
     */

    public List<Node> getChildrens() {
        return childrens;
    }

    public void setChildrens(List<Node> childrens) {
        this.childrens = childrens;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean isExpand) {
        this.isExpand = isExpand;
        //如果需要收缩当前节点，应该将它的所有子节点也收缩
        if (!isExpand) {
            for (Node node : childrens) {
                node.setExpand(false);
            }
        }
    }

    public int getLevel() {
        //先根据父节点找到当前Node的层级
        level = parent == null ? 0 : parent.getLevel() + 1;
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    /*
     * 其他方法
     */

    /**
     * 是否是根节点
     *
     * @return
     */
    public boolean isRoot() {
        return parent == null;
    }

    /**
     * 判断父节点是否是展开的（用以确定当前节点是否显示）
     *
     * @return 为fasle 可能是根节点，也可能父节点未展开。
     */
    public boolean isParentExpand() {
        if (isRoot()) {
            return false;
        }
        return parent.isExpand();
    }

    /**
     * 判断当前节点是否为叶子节点（无子节点）,用于控制ICON的显示。
     *
     * @return
     */
    public boolean isLeaf() {
        return childrens.size() == 0;
    }
}
