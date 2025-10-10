package org.algds.tree.ds;

import java.util.Iterator;
import java.util.Stack;

/**
 * 【平衡】AVL树数据结构定义 - 自平衡二叉查找树
 *
 * @param <T>
 */
public class AVLTree<T extends Comparable<? super T>> implements Iterable<T> {

    // 0 单元测试 *******************************************************************************************************
    public static void main(String[] args) {

        AVLTree<Integer> avlTree = new AVLTree<>();

        // 倒序插入 3 2 1
        avlTree.insert(3);
        avlTree.insert(2);
        avlTree.insert(1);

        avlTree.printTree(); // In-order: 1(0) 2(1) 3(0)

        // 依次插入 4 5 6 7
        avlTree.insert(4);
        avlTree.insert(5);
        avlTree.insert(6);
        avlTree.insert(7);

        avlTree.printTree(); // In-order: 1(0) 2(1) 3(0) 4(2) 5(0) 6(1) 7(0)

        // 倒序插入 16 - 10
        avlTree.insert(16);
        avlTree.insert(15);
        avlTree.insert(14);
        avlTree.insert(13);
        avlTree.insert(12);
        avlTree.insert(11);
        avlTree.insert(10);

        avlTree.printTree(); // In-order: 1(0) 2(1) 3(0) 4(2) 5(0) 6(1) 7(3) 10(0) 11(1) 12(0) 13(2) 14(0) 15(1) 16(0)

        // 依次插入 8 9
        avlTree.insert(8);
        avlTree.insert(9);

        avlTree.printTree(); // In-order: 1(0) 2(1) 3(0) 4(2) 5(0) 6(1) 7(4) 8(0) 9(1) 10(0) 11(2) 12(0) 13(3) 14(0) 15(1) 16(0)

    }


    // 1 核心数据结构定义 ************************************************************************************************

    /**
     * AVL树结点类定义，增加高度信息
     */
    private static class AVLNode<T> {
        T data;
        AVLNode<T> left;
        AVLNode<T> right;
        int height; // 节点高度

        AVLNode(T data) {
            this(data, null, null);
        }

        AVLNode(T data, AVLNode<T> left, AVLNode<T> right) {
            this.data = data;
            this.left = left;
            this.right = right;
            this.height = 0; // 新节点初始高度为0（叶子节点）
        }

        @Override
        public String toString() {
            return "AVLNode{" +
                    "data=" + data +
                    ", left=" + left +
                    ", right=" + right +
                    ", height=" + height +
                    '}';
        }
    }

    private AVLNode<T> root;

    public AVLTree() {
        this.root = null;
    }


    // 2 公共方法区 *****************************************************************************************************
    public void makeEmpty() {
        this.root = null;
    }

    public boolean isEmpty() {
        return this.root == null;
    }

    public boolean contains(T x) {
        return contains(x, root);
    }

    public T findMin() {
        if (isEmpty()) throw new RuntimeException("Tree is empty");
        return findMin(root).data;
    }

    public T findMax() {
        if (isEmpty()) throw new RuntimeException("Tree is empty");
        return findMax(root).data;
    }

    public void insert(T x) {
        root = insert(x, root);
    }

    public void remove(T x) {
        root = remove(x, root);
    }

    public void printTree() {
        if (isEmpty()) {
            System.out.println("Empty tree");
        } else {
            System.out.print("In-order: ");
            printTree(root);
            System.out.println();
        }
    }

    /**
     * 获取树的高度
     */
    public int height() {
        return height(root);
    }

    /**
     * 检查树是否平衡（主要用于测试）
     */
    public boolean isBalanced() {
        return isBalanced(root);
    }

    @Override
    public Iterator<T> iterator() {
        return new InOrderIterator();
    }

    // 3 平衡相关核心方法 ***********************************************************************************************

    /**
     * 获取节点高度（空节点高度为-1）
     */
    private int height(AVLNode<T> t) {
        return t == null ? -1 : t.height;
    }

    /**
     * 更新节点高度
     */
    private void updateHeight(AVLNode<T> t) {
        if (t != null) {
            t.height = Math.max(height(t.left), height(t.right)) + 1;
        }
    }

    /**
     * 获取节点的平衡因子
     */
    private int getBalance(AVLNode<T> t) {
        if (t == null) return 0;
        return height(t.left) - height(t.right);
    }


    /**
     * 右旋转（处理LL情况）
     */
    private AVLNode<T> rotateWithLeftChild(AVLNode<T> k2) {
        AVLNode<T> k1 = k2.left;
        k2.left = k1.right;
        k1.right = k2;

        updateHeight(k2);
        updateHeight(k1);

        return k1;
    }

    /**
     * 左旋转（处理RR情况）
     */
    private AVLNode<T> rotateWithRightChild(AVLNode<T> k1) {
        AVLNode<T> k2 = k1.right;
        k1.right = k2.left;
        k2.left = k1;

        updateHeight(k1);
        updateHeight(k2);

        return k2;
    }

    /**
     * 左右双旋转（处理LR情况）
     */
    private AVLNode<T> doubleWithLeftChild(AVLNode<T> k3) {
        k3.left = rotateWithRightChild(k3.left);
        return rotateWithLeftChild(k3);
    }

    /**
     * 右左双旋转（处理RL情况）
     */
    private AVLNode<T> doubleWithRightChild(AVLNode<T> k1) {
        k1.right = rotateWithLeftChild(k1.right);
        return rotateWithRightChild(k1);
    }


    /**
     * 平衡节点
     */
    private AVLNode<T> balance(AVLNode<T> t) {
        if (t == null) return null;

        updateHeight(t);
        int balance = getBalance(t);

        // 左子树比右子树高（LL或LR情况）
        if (balance > 1) {
            if (getBalance(t.left) >= 0) {
                // LL情况
                return rotateWithLeftChild(t);
            } else {
                // LR情况
                return doubleWithLeftChild(t);
            }
        }
        // 右子树比左子树高（RR或RL情况）
        else if (balance < -1) {
            if (getBalance(t.right) <= 0) {
                // RR情况
                return rotateWithRightChild(t);
            } else {
                // RL情况
                return doubleWithRightChild(t);
            }
        }

        return t; // 已经平衡
    }

    // 4 核心操作的平衡版本 *********************************************************************************************
    private boolean contains(T x, AVLNode<T> t) {
        if (t == null) return false;

        int compareResult = x.compareTo(t.data);

        if (compareResult < 0) {
            return contains(x, t.left);
        } else if (compareResult > 0) {
            return contains(x, t.right);
        } else {
            return true;
        }
    }

    private AVLNode<T> findMin(AVLNode<T> t) {
        if (t == null) return null;
        while (t.left != null) {
            t = t.left;
        }
        return t;
    }

    private AVLNode<T> findMax(AVLNode<T> t) {
        if (t == null) return null;
        while (t.right != null) {
            t = t.right;
        }
        return t;
    }

    private AVLNode<T> insert(T x, AVLNode<T> t) {
        if (t == null) {
            return new AVLNode<>(x);
        }

        int compareResult = x.compareTo(t.data);

        if (compareResult < 0) {
            t.left = insert(x, t.left);
        } else if (compareResult > 0) {
            t.right = insert(x, t.right);
        } else {
            // 重复元素，不插入
            return t;
        }

        return balance(t); // 插入后平衡
    }

    private AVLNode<T> remove(T x, AVLNode<T> t) {
        if (t == null) return null;

        int compareResult = x.compareTo(t.data);

        if (compareResult < 0) {
            t.left = remove(x, t.left);
        } else if (compareResult > 0) {
            t.right = remove(x, t.right);
        } else if (t.left != null && t.right != null) {
            // 有两个子节点：用右子树的最小值替换当前节点
            t.data = findMin(t.right).data;
            t.right = remove(t.data, t.right);
        } else {
            // 有一个或零个子节点
            t = (t.left != null) ? t.left : t.right;
        }

        return balance(t); // 删除后平衡
    }

    private void printTree(AVLNode<T> t) {
        if (t != null) {
            printTree(t.left);
            System.out.print(t.data + "(" + t.height + ") ");
            printTree(t.right);
        }
    }

    // 5 辅助方法（用于测试）*******************************************************************************************
    private boolean isBalanced(AVLNode<T> t) {
        if (t == null) return true;

        int balance = getBalance(t);
        if (Math.abs(balance) > 1) {
            return false;
        }

        return isBalanced(t.left) && isBalanced(t.right);
    }

    // 6 迭代器实现 ****************************************************************************************************
    private class InOrderIterator implements Iterator<T> {
        private final Stack<AVLNode<T>> stack;

        public InOrderIterator() {
            stack = new Stack<>();
            pushLeft(root);
        }

        private void pushLeft(AVLNode<T> node) {
            while (node != null) {
                stack.push(node);
                node = node.left;
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new RuntimeException("No more elements in tree");
            }

            AVLNode<T> node = stack.pop();
            T result = node.data;

            if (node.right != null) {
                pushLeft(node.right);
            }

            return result;
        }
    }
}