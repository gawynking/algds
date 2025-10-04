package org.algds.tree.traversal;

import org.algds.tree.ds.TreeNode;

import java.util.List;
import java.util.Stack;

/**
 * 树的先序遍历，扩展到图属于DFS范畴，适用于二叉树或N叉树
 */
public class PreOrderTraverse {

    /**
     * 递归先序遍历
     */
    public static <T> void preOrderRecursive(TreeNode<T> root) {
        if (root == null) {
            return;
        }

        // 处理当前节点
        print(root);

        // 递归处理所有子节点
        for (TreeNode<?> child : root.getChildren()) {
            preOrderRecursive(child);
        }
    }


    /**
     * 基于栈的先序遍历
     */
    public static <T> void preOrderStack(TreeNode<T> root) {
        if (root == null) {
            return;
        }

        Stack<TreeNode<T>> stack = new Stack<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            TreeNode<T> current = stack.pop();
            print(current);

            // 将子节点逆序压入栈中，保证从左到右遍历
            List<TreeNode<T>> children = current.getChildren();
            for (int i = children.size() - 1; i >= 0; i--) {
                stack.push(children.get(i));
            }
        }
    }


    /**
     * 访问节点方法，节点打印方法
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
        System.out.println("=== 树的孩子表示法 - 先序遍历演示 ===\n");

        // 1 构造树节点
        TreeNode<String> root = new TreeNode<>("A");
        TreeNode<String> B = new TreeNode<>("B");
        TreeNode<String> C = new TreeNode<>("C");
        TreeNode<String> D = new TreeNode<>("D");
        TreeNode<String> E = new TreeNode<>("E");
        TreeNode<String> F = new TreeNode<>("F");
        TreeNode<String> G = new TreeNode<>("G");
        TreeNode<String> H = new TreeNode<>("H");


        // 3 构建树结构
        root.addChildren(B);
        root.addChildren(C);
        root.addChildren(D);

        B.addChildren(E);
        B.addChildren(F);

        D.addChildren(G);
        G.addChildren(H);


        // 1 递归先序遍历
        System.out.println("1. 递归先序遍历结果:");
        preOrderRecursive(root);
        System.out.println("\n");

        // 2 栈先序遍历
        System.out.println("2. 基于栈的先序遍历结果:");
        preOrderStack(root);
        System.out.println("\n");

        System.out.println("=== 演示结束 ===");
    }

}
