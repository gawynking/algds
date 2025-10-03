package org.algds.tree.model.binary;

/**
 * 链式存储结构: 二叉树的三叉链表表示
 *
 * @param <T>
 */
public class BinaryTreeWithParent<T> {
    public T value;
    public BinaryTreeWithParent<T> parent;
    public BinaryTreeWithParent<T> left;
    public BinaryTreeWithParent<T> right;
}
