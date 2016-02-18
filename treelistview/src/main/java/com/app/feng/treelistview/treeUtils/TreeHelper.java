package com.app.feng.treelistview.treeUtils;

import com.app.feng.treelistview.R;
import com.app.feng.treelistview.annotation.NodeID;
import com.app.feng.treelistview.annotation.NodeName;
import com.app.feng.treelistview.annotation.NodeParentID;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 将数据节点（Bean）转化成树节点(Node)
 * Created by feng on 2015/11/11.
 */
public class TreeHelper {
    /**
     * 将用户数据转化成树节点
     * 使用反射+注解得到用户数据中的字段。
     *
     * @param datas 包含用户数据（Bean）的List
     * @param <T>
     * @return
     * @NodeID 该节点的标识
     * @NodeParentID 该节点的父节点标识
     * @NodeName 显示在该节点上的名字
     * @see com.app.feng.treelistview.annotation
     */
    public static <T> List<Node> changeDataToNode(List<T> datas) throws IllegalAccessException {
        List<Node> tempNodes = new ArrayList<>();
        Node node = null;
        for (T data :
                datas) {

            int tempId = -1;
            int tempParentId = -1;
            String tempName = null;

            Class aClass = data.getClass();
            Field[] declaredFields = aClass.getDeclaredFields();
            //设置Node的三个变量，ID，ParentID，Name。
            for (Field field :
                    declaredFields) {
                if (field.getAnnotation(NodeID.class) != null) {
                    field.setAccessible(true);

                    tempId = field.getInt(data);
                }
                if (field.getAnnotation(NodeParentID.class) != null) {
                    field.setAccessible(true);

                    tempParentId = field.getInt(data);
                }
                if (field.getAnnotation(NodeName.class) != null) {
                    field.setAccessible(true);

                    tempName = (String) field.get(data);
                }
            }
            node = new Node(tempId, tempParentId, tempName);
            tempNodes.add(node);
        }
        //设置节点间的属性关系
        for (int i = 0; i < tempNodes.size(); i++) {
            Node n = tempNodes.get(i);
            //对每个节点来说
            for (int j = i + 1; j < tempNodes.size(); j++) {
                //找它下部分节点和它的继承关系
                Node m = tempNodes.get(j);

                if (m.getpId() == n.getId()) {
                    //找道子节点
                    m.setParent(n);
                    n.getChildrens().add(m);
                } else if (m.getId() == n.getpId()) {
                    //找到父节点
                    m.getChildrens().add(n);
                    n.setParent(m);
                }

            }
            //为非叶子节点设置图片
            setIconForNode(n);
        }

        return tempNodes;
    }

    private static void setIconForNode(Node n) {
        if (!n.isLeaf()) {
            if (n.isExpand()) {
                n.setIcon(R.drawable.icon_down_arrow);
            } else {
                n.setIcon(R.drawable.icon_right_arrow);
            }
        } else {
            //不是叶子节点的话，就设置个标志位-1
            n.setIcon(-1);
        }
    }

    /**
     * 创建树节点，对树进行排序
     *
     * @param datas
     * @param defaultExpandLevel 该树形列表默认展开到第几级
     * @param <T>
     * @return
     * @see #changeDataToNode(List)
     */
    public static <T> List<Node> getSortedNodes(List<T> datas, int defaultExpandLevel) throws IllegalAccessException {
        List<Node> result = new ArrayList<>();
        List<Node> nodes = changeDataToNode(datas);

        List<Node> rootNodes = getRootNodes(nodes);

        for (Node root :
                rootNodes) {
            addNode(result, root, defaultExpandLevel, 1);
        }
        return result;
    }

    /**
     * 此方法对于每个传递进来的root节点，都会遍历它的所有孩子节点（深度遍历），以此来生成一颗树
     *
     * @param result
     * @param root
     * @param defaultExpandLevel
     * @param currentLevel
     */
    private static void addNode(List<Node> result, Node root, int defaultExpandLevel, int currentLevel) {

        result.add(root);

        if (defaultExpandLevel >= currentLevel) {
            root.setExpand(true);
        }

        //这个和深度遍历算法差不多，只不过判断左右孩子为空变成了判断叶子节点。
        if (root.isLeaf()) {
            return;
        }

        for (int i = 0; i < root.getChildrens().size(); i++) {
            //每次的调用root都改变了。
            addNode(result, root.getChildrens().get(i), defaultExpandLevel, currentLevel + 1);
        }

    }

    private static List<Node> getRootNodes(List<Node> nodes) {
        List<Node> rootNodes = new ArrayList<>();

        for (Node node :
                nodes) {
            if (node.isRoot()) {
                rootNodes.add(node);
            }
        }

        return rootNodes;
    }

    /**
     * 根据isParentExpand确定要显示出来的节点（当然，根节点是必须显示的）
     *
     * @param nodes
     * @return 返回可见的节点列表
     */

    public static List<Node> filterVisibleNodes(List<Node> nodes) {
        List<Node> result = new ArrayList<>();

        for (Node node :
                nodes) {
            if (node.isRoot() || node.isParentExpand()) {
                setIconForNode(node);
                result.add(node);
            }
        }
        return result;
    }
}
