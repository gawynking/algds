package org.algds.tree.ds;

import java.util.ArrayList;
import java.util.List;

/**
 * 孩子表示法，算法演示
 */
public class TreeNode<T> {

    private T data;
    private List<TreeNode<T>> children = new ArrayList<>();  // 孩子下标集合

    public TreeNode(T data){
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<TreeNode<T>> getChildren() {
        return children;
    }

    public void addChildren(TreeNode<T> node){
        this.children.add(node);
    }

}
