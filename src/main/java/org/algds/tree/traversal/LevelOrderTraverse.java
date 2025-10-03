package org.algds.tree.traversal;

import org.algds.tree.ds.TreeNode;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 树的层序遍历算法
 */
public class LevelOrderTraverse {

    /**
     * 层序遍历 - 简化版本（只按顺序输出，不显示层级）
     */
    public static <T> void levelOrderTraverse(TreeNode<T> root) {
        if (root == null) {
            return;
        }

        Queue<TreeNode<T>> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            TreeNode<T> current = queue.poll();
            print(current);

            // 将当前节点的所有子节点加入队列
            List<TreeNode<T>> children = current.getChildren();
            for (TreeNode<T> child : children) {
                queue.offer(child);
            }
        }

    }


    /**
     * 节点打印方法
     */
    private static <T> void print(TreeNode<T> node) {
        System.out.print(node.getData() + " ");
    }

    /**
     * 单元测试，构建测试树结构：
     *         A
     *        /|\
     *       B C D
     *      /|   |
     *     E F   G
     *           |
     *           H
     */
    public static void main(String[] args) {
        System.out.println("=== 树的孩子表示法 - 层序遍历演示 ===\n");

        // 构造树节点
        TreeNode<String> A = new TreeNode<>("A");
        TreeNode<String> B = new TreeNode<>("B");
        TreeNode<String> C = new TreeNode<>("C");
        TreeNode<String> D = new TreeNode<>("D");
        TreeNode<String> E = new TreeNode<>("E");
        TreeNode<String> F = new TreeNode<>("F");
        TreeNode<String> G = new TreeNode<>("G");
        TreeNode<String> H = new TreeNode<>("H");

        // 构建树结构
        A.addChildren(B);
        A.addChildren(C);
        A.addChildren(D);

        B.addChildren(E);
        B.addChildren(F);

        D.addChildren(G);
        G.addChildren(H);

        // 层序遍历
        System.out.println("1. 层序遍历:");
        levelOrderTraverse(A);
        System.out.println();

        System.out.println("=== 演示结束 ===");
    }
}