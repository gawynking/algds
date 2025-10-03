package org.algds.tree.model;

/**
 * 树的孩子兄弟表示法（用二叉链表实现）
 */
class ChildSiblingTree<T> {

    T data;
    ChildSiblingTree<T> firstChild;   // 第一个孩子下标
    ChildSiblingTree<T> nextSibling;  // 下一个兄弟下标

}
