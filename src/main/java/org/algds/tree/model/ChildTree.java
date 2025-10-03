package org.algds.tree.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 树的孩子表示法
 */
public class ChildTree<T> {

    T data;
    List<ChildTree<T>> children = new ArrayList<>();  // 孩子下标集合

}
