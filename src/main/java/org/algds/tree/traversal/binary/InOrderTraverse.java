package org.algds.tree.traversal.binary;

import org.algds.tree.ds.BinaryTreeNode;
import java.util.Stack;

/**
 * 推广到图属于DFS遍历范畴，仅二叉树遍历支持中序遍历
 */
public class InOrderTraverse {

    /**
     * 递归中序遍历
     */
    public static void inOrderRecursive(BinaryTreeNode<?> root) {
        if (root == null) return;

        // print(root);                     // 前序访问
        inOrderRecursive(root.getLeft());   // 遍历左子树
        print(root);                        // 访问根节点-中序访问
        inOrderRecursive(root.getRight());  // 遍历右子树
        // print(root);                     // 后序访问
    }

    /**
     * 迭代中序遍历（使用栈）
     */
    public static void inOrderIterative(BinaryTreeNode<?> root) {
        if (root == null) return;

        Stack<BinaryTreeNode<?>> stack = new Stack<>();
        BinaryTreeNode<?> current = root;

        while (current != null || !stack.isEmpty()) {
            // 将左子树的所有节点入栈(向左走到尽头)
            while (current != null) {
                stack.push(current);
                current = current.getLeft();
            }

            // 弹出栈顶节点并访问
            current = stack.pop();
            print(current);

            // 转向右子树
            current = current.getRight();
        }
    }


    /**
     * Morris中序遍历
     */
    public static <T> void inorderMorris(BinaryTreeNode<T> root) {
        BinaryTreeNode<T> current = root;

        while (current != null) {
            if (current.getLeft() == null) {
                // 如果没有左子树，直接访问当前节点
                print(current);
                current = current.getRight();
            } else {
                // 找到当前节点的前驱节点（左子树的最右节点）
                BinaryTreeNode<T> predecessor = current.getLeft();
                while (predecessor.getRight() != null && !predecessor.getRight().equals(current)) {
                    predecessor = predecessor.getRight();
                }

                if (predecessor.getRight() == null) {
                    // 建立临时链接，便于回溯
                    predecessor.setRight(current);
                    current = current.getLeft();
                } else {
                    // 恢复树的结构并访问节点
                    predecessor.setRight(null);
                    print(current);
                    current = current.getRight();
                }
            }
        }
    }


    /**
     * 访问节点的方法
     */
    private static void print(BinaryTreeNode<?> node) {
        System.out.print(node + " ");
    }

    /**
     * 单元测试
     *     A
     *    / \
     *   B   C
     *  / \
     * D   E
     */
    public static void main(String[] args) {

        // 字符串类型的二叉树
        System.out.println("\n=== 字符串类型二叉树测试 ===");
        BinaryTreeNode<String> root = new BinaryTreeNode<>("A");
        BinaryTreeNode<String> B = new BinaryTreeNode<>("B");
        BinaryTreeNode<String> C = new BinaryTreeNode<>("C");
        BinaryTreeNode<String> D = new BinaryTreeNode<>("D");
        BinaryTreeNode<String> E = new BinaryTreeNode<>("E");

        root.setLeft(B);
        root.setRight(C);
        B.setLeft(D);
        B.setRight(E);


        System.out.print("递归中序遍历: ");
        inOrderRecursive(root);
        System.out.println("\n");

        System.out.print("迭代中序遍历: ");
        inOrderIterative(root);
        System.out.println("\n");

        System.out.print("Morris中序遍历: ");
        inorderMorris(root);
        System.out.println("\n");
    }
}

