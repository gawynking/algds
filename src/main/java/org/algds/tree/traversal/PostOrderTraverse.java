package org.algds.tree.traversal;

import org.algds.tree.ds.TreeNode;

import java.util.List;
import java.util.Stack;

/**
 * 树的后序遍历
 */
public class PostOrderTraverse {

    /**
     * 递归后序遍历
     */
    public static <T> void postOrderRecursive(TreeNode<T> root) {
        if (root == null) {
            return;
        }

        // 递归处理所有子节点
        for (TreeNode<?> child : root.getChildren()) {
            postOrderRecursive(child);
        }

        // 处理当前节点 - 打印
        print(root);
    }


    /**
     * 基于栈的后序遍历
     */
    public static <T> void postOrderStack(TreeNode<T> root) {
        if (root == null) {
            return;
        }

        Stack<TreeNode<T>> stack = new Stack<>();
        Stack<TreeNode<T>> output = new Stack<>();

        stack.push(root);

        while (!stack.isEmpty()) {
            TreeNode<T> current = stack.pop();
            output.push(current);

            // 将子节点按顺序压入栈中
            List<TreeNode<T>> children = current.getChildren();
            for (TreeNode<T> child : children) {
                stack.push(child);
            }
        }

        // 输出栈中的节点（逆序弹出）
        while (!output.isEmpty()) {
            print(output.pop());
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
        System.out.println("=== 树的孩子表示法 - 后序遍历演示 ===\n");

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

        // 1 递归后序遍历
        System.out.println("1. 递归后序遍历结果:");
        postOrderRecursive(A);
        System.out.println("\n");

        // 2 双栈法后序遍历
        System.out.println("2. 基于双栈的后序遍历结果:");
        postOrderStack(A);
        System.out.println("\n");

        System.out.println("=== 演示结束 ===");
    }
}