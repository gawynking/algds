package org.algds.tree.traversal.binary;

import org.algds.tree.ds.BinaryTreeNode;
import java.util.Stack;

/**
 * 推广到图属于DFS遍历范畴，仅二叉树遍历支持中序遍历
 */
public class InOrderTraverse {

    /**
     * 递归中序遍历
     * 时间复杂度：O(n)，空间复杂度：O(h)，h为树的高度
     */
    public static void inorderRecursive(BinaryTreeNode<?> root) {
        if (root != null) {
            inorderRecursive(root.getLeft());   // 遍历左子树
            print(root);                        // 访问根节点
            inorderRecursive(root.getRight());  // 遍历右子树
        }
    }

    /**
     * 迭代中序遍历（使用栈）
     * 时间复杂度：O(n)，空间复杂度：O(h)，h为树的高度
     */
    public static void inorderIterative(BinaryTreeNode<?> root) {
        if (root == null) return;

        Stack<BinaryTreeNode<?>> stack = new Stack<>();
        BinaryTreeNode<?> current = root;

        while (current != null || !stack.isEmpty()) {
            // 将左子树的所有节点入栈
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
     * 时间复杂度：O(n)，空间复杂度：O(1)
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
        BinaryTreeNode<String> strRoot = new BinaryTreeNode<>("A");
        BinaryTreeNode<String> strB = new BinaryTreeNode<>("B");
        BinaryTreeNode<String> strC = new BinaryTreeNode<>("C");
        BinaryTreeNode<String> strD = new BinaryTreeNode<>("D");
        BinaryTreeNode<String> strE = new BinaryTreeNode<>("E");

        strRoot.setLeft(strB);
        strRoot.setRight(strC);
        strB.setLeft(strD);
        strB.setRight(strE);


        System.out.print("递归中序遍历: ");
        inorderRecursive(strRoot);
        System.out.println();

        System.out.print("迭代中序遍历: ");
        inorderIterative(strRoot);
        System.out.println();

        System.out.print("Morris中序遍历: ");
        inorderMorris(strRoot);
        System.out.println();
    }
}

