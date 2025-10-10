package org.algds.tree.ds;


import java.util.Iterator;
import java.util.Stack;

/**
 * 【朴素】二叉查找树(Binary Search Tree)数据结构定义
 *
 * @param <T>
 */
public class BinarySearchTree<T extends Comparable<? super T>> implements Iterable<T> {

    // 0 单元测试 *******************************************************************************************************

    /**
     * 构建二叉查找树（以5为根节点）
     * 层次结构：
     *   5
     * /   \
     * 3     8
     * / \   / \
     * 1   4 6   9
     * \     \   \
     * 2     7   12
     */
    public static void main(String[] args) {

        // 1 构造树 —————————————————————————————————————————————————————————————————————————————————————————————————————
        BinaryNode<Integer> t1 = new BinaryNode<>(1);
        BinaryNode<Integer> t2 = new BinaryNode<>(2);
        BinaryNode<Integer> t3 = new BinaryNode<>(3);
        BinaryNode<Integer> t4 = new BinaryNode<>(4);
        BinaryNode<Integer> t5 = new BinaryNode<>(5);
        BinaryNode<Integer> t6 = new BinaryNode<>(6);
        BinaryNode<Integer> t7 = new BinaryNode<>(7);
        BinaryNode<Integer> t8 = new BinaryNode<>(8);
        BinaryNode<Integer> t9 = new BinaryNode<>(9);
        BinaryNode<Integer> t12 = new BinaryNode<>(12);

        BinaryNode<Integer> root = t5;  // 根节点

        // 构建左子树
        t5.left = t3;
        t3.left = t1;
        t3.right = t4;
        t1.right = t2;

        // 构建右子树
        t5.right = t8;
        t8.left = t6;
        t8.right = t9;
        t6.right = t7;
        t9.right = t12;

        // 改造二叉查找树
        BinarySearchTree binarySearchTree = new BinarySearchTree(root);

        // 2 执行测试 ———————————————————————————————————————————————————————————————————————————————————————————————————
        // 2.1 toString打印树结构
        System.out.println(root);

        // 2.2 按顺序打印树结构
        binarySearchTree.printTree();

        // 2.3 查找结点8 合 88是否存在
        boolean contains8 = binarySearchTree.contains(8);
        boolean contains88 = binarySearchTree.contains(88);
        System.out.println("8 exists " + contains8);
        System.out.println("88 exists " + contains88);

        // 2.4 查早最小值值最大值
        Integer min = (Integer) binarySearchTree.findMin();
        Integer max = (Integer) binarySearchTree.findMax();
        System.out.println(String.format("当前树 最小值 = %s; 最大值 = %s.", min, max));

        // 2.5 向树中插入元素10
        binarySearchTree.printTree();
        binarySearchTree.insert(10);
        binarySearchTree.printTree();


        // 2.6 删除元素 2 6 和 8
        binarySearchTree.remove(2);
        binarySearchTree.remove(6);
        binarySearchTree.remove(8);
        binarySearchTree.printTree();

        // 2.7 测试迭代器
        System.out.print("中序迭代器测试: ");
        Iterator iterator = binarySearchTree.iterator();
        while (iterator.hasNext()) {
            Integer data = (Integer) iterator.next();
            System.out.print(data + " ");
        }

    }

    // 1 核心数据结构定义 ************************************************************************************************

    /**
     * 内部结点类(链表表示)定义
     *
     * @param <T>
     */
    private static class BinaryNode<T> {
        T data;
        BinaryNode<T> left;
        BinaryNode<T> right;

        BinaryNode(T data) {
            this(data, null, null);
        }

        BinaryNode(T data, BinaryNode<T> left, BinaryNode<T> right) {
            this.data = data;
            this.left = left;
            this.right = right;
        }

        @Override
        public String toString() {
            return "BinaryNode{" + "element=" + this.data + ", left=" + this.left + ", right=" + this.right + '}';
        }
    }

    // 根节点引用
    private BinaryNode<T> root;

    /**
     * 这是该数据结构正常使用的方法
     */
    public BinarySearchTree() {
        this(null);
    }

    /**
     * 这个方法是测试用，方便将外部构造好的树直接赋值进来
     *
     * @param root
     */
    @Deprecated
    public BinarySearchTree(BinaryNode<T> root) {
        this.root = root;
    }

    @Override
    public Iterator<T> iterator() {
        return new InOrderIterator();
    }

    private class InOrderIterator implements Iterator<T> {

        private final Stack<BinaryNode<T>> stack;

        public InOrderIterator() {
            stack = new Stack<>();
            // 初始化：将最左路径的所有节点入栈
            pushLeft(root);
        }

        /**
         * 将节点及其所有左子节点入栈
         */
        private void pushLeft(BinaryNode<T> node) {
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

            // 弹出栈顶节点（当前要访问的节点）
            BinaryNode<T> node = stack.pop();
            T result = node.data;

            // 如果该节点有右子树，将右子树的最左路径入栈
            if (node.right != null) {
                pushLeft(node.right);
            }

            return result;
        }
    }


    // 2 公共方法区 *****************************************************************************************************
    public void makeEmpty() {
        this.root = null;
    }

    public boolean isEmpty() {
        return this.root == null;
    }

    public boolean contains(T x) {
        return this.contains(x, root);
    }

    public T findMin() {
        if (isEmpty()) throw new RuntimeException("Tree is empty");
        return this.findMin(root).data;
    }

    public T findMax() {
        if (isEmpty()) throw new RuntimeException("Tree is empty");
        return this.findMax(root).data;
    }

    public void insert(T x) {
        root = this.insert(x, root);
    }

    public void remove(T x) {
        root = this.remove(x, root);
    }

    public void printTree() {
        if (isEmpty()) {
            System.out.println("Empty tree: ");
        } else {
            System.out.print("Tree: ");
            printTree(root);
        }
        System.out.println();
    }

    // 3 内部方法区 *****************************************************************************************************
    private boolean contains(T x, BinaryNode<T> t) {
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

    private BinaryNode<T> findMin(BinaryNode<T> t) {
        if (t == null) {
            return null;
        } else if (t.left == null) {
            return t;
        }
        return findMin(t.left);
    }

    private BinaryNode<T> findMax(BinaryNode<T> t) {
        if (t != null) {
            while (t.right != null) {
                t = t.right;
            }
        }
        return t;
    }

    /**
     * 每一次插入新的属性总是能插入到下一个叶子结点
     *
     * @param x
     * @param t
     * @return
     */
    private BinaryNode<T> insert(T x, BinaryNode<T> t) {
        if (t == null) {
            return new BinaryNode<>(x, null, null);
        }

        int compareResult = x.compareTo(t.data);

        if (compareResult < 0) {
            t.left = insert(x, t.left);
        } else if (compareResult > 0) {
            t.right = insert(x, t.right);
        } else { // 重复结点

        }

        return t;
    }

    /**
     * 删除指定结点
     *
     * @param x
     * @param t
     * @return
     */
    private BinaryNode<T> remove(T x, BinaryNode<T> t) {
        if (t == null) return null;

        int compareResult = x.compareTo(t.data);

        if (compareResult < 0) {
            t.left = remove(x, t.left);
        } else if (compareResult > 0) {
            t.right = remove(x, t.right);
        } else if (t.left != null && t.right != null) {
            t.data = findMin(t.right).data;
            t.right = remove(t.data, t.right);
        } else {
            t = (t.left != null) ? t.left : t.right;
        }

        return t;
    }

    /**
     * 中序遍历顺序打印树数据
     *
     * @param t
     */
    private void printTree(BinaryNode<T> t) {
        if (t != null) {
            printTree(t.left);
            System.out.print(t.data + " ");
            printTree(t.right);
        }
    }

}