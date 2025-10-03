package org.algds.tree;

import org.algds.tree.model.binary.BinaryTreeArray;

public class BinaryTreeArrayTest {

    /**
     *        1
     *       / \
     *      2   3
     *     / \
     *    4   5
     * @param args
     */
    public static void main(String[] args) {
        BinaryTreeArray<Integer> tree = new BinaryTreeArray<>();

        // 构造一棵完全二叉树: 1 作为根, 2左 3右
        tree.set(0, 1); // 根
        tree.set(1, 2); // 左
        tree.set(2, 3); // 右
        tree.set(3, 4); // 左的左
        tree.set(4, 5); // 左的右

        System.out.println("根: " + tree.get(0)); // 1
        System.out.println("根的左孩子: " + tree.left(0)); // 2
        System.out.println("根的右孩子: " + tree.right(0)); // 3
        System.out.println("节点 2 的右孩子: " + tree.right(1)); // 5
        System.out.println("节点 5 的父节点: " + tree.parent(4)); // 2
    }


}
