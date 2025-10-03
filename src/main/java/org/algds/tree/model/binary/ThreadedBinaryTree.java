package org.algds.tree.model.binary;

/**
 * 线索二叉树
 *
 * 如何定位节点前驱和后继节点；如果将二叉树转化为线索二叉树
 */
public class ThreadedBinaryTree<T> {
    T data;                      // 数据
    ThreadedBinaryTree<T> left;  // 左指针
    ThreadedBinaryTree<T> right; // 右指针
    int ltag;                    // 标志位：0=孩子，1=线索
    int rtag;                    // 标志位：0=孩子，1=线索
}
