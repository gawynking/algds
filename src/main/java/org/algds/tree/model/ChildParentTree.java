package org.algds.tree.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 树的孩子双亲表示法
 */
public class ChildParentTree<T> {

    T data;                              // 结点数据
    ChildParentTree<T> parent;           // 父节点下标
    List<ChildParentTree<T>> children;   // 孩子结点下标集合

}
