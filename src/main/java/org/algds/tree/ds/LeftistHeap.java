package org.algds.tree.ds;

/**
 * 左式堆
 *  性质：对于任意结点X，左儿子npl(x.left)>=npl(x.right)
 *
 *  更擅长支持堆的合并，链表实现
 *
 * @param <T>
 */
public class LeftistHeap<T extends Comparable<? super T>> {

    // 内部数据结构定义 **************************************************************************************************
    private static class LeftistNode<T> {

        LeftistNode(T theElement) {
            this(theElement, null, null);
        }

        LeftistNode(T theElement, LeftistNode<T> lt, LeftistNode<T> rt) {
            element = theElement;
            left = lt;
            right = rt;
            npl = 0;
        }

        T element;
        LeftistNode<T> left;
        LeftistNode<T> right;
        int npl;          // 零路径长
    }

    private LeftistNode<T> root;

    public LeftistHeap() {
        root = null;
    }


    /** 核心方法区 ******************************************************************************************************
     * 左氏堆merge是核心方法
     *
     * @param rhs
     */
    public void merge(LeftistHeap<T> rhs) {
        if (this == rhs)
            return;

        root = merge(root, rhs.root);
        rhs.root = null; // 断开链接
    }

    private LeftistNode<T> merge(LeftistNode<T> h1, LeftistNode<T> h2) {
        if (h1 == null)
            return h2;
        if (h2 == null)
            return h1;
        if (h1.element.compareTo(h2.element) < 0)
            return merge1(h1, h2);
        else
            return merge1(h2, h1);
    }

    // 真正的合并操作，递归实现
    private LeftistNode<T> merge1(LeftistNode<T> h1, LeftistNode<T> h2) {
        if (h1.left == null) {
            h1.left = h2;
        } else {
            h1.right = merge(h1.right, h2);
            if (h1.left.npl < h1.right.npl) {
                swapChildren(h1);
            }
            h1.npl = h1.right.npl + 1;
        }
        return h1;
    }

    // 交换根的左右儿子
    private static <T> void swapChildren(LeftistNode<T> t) {
        LeftistNode<T> tmp = t.left;
        t.left = t.right;
        t.right = tmp;
    }


    // 功能方法区 *******************************************************************************************************
    public void insert(T x) {
        root = merge(new LeftistNode<>(x), root);
    }

    public T findMin() {
        if (isEmpty())
            throw new RuntimeException();
        return root.element;
    }

    public T deleteMin() {
        if (isEmpty())
            throw new RuntimeException();

        T minItem = root.element;
        root = merge(root.left, root.right);

        return minItem;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public void makeEmpty() {
        root = null;
    }


    public static void main(String[] args) {
        int numItems = 100;
        LeftistHeap<Integer> h1 = new LeftistHeap<>();
        LeftistHeap<Integer> h2 = new LeftistHeap<>();
        int i = 37;

        for (i = 37; i != 0; i = (i + 37) % numItems)
            if (i % 2 == 0)
                h2.insert(i);
            else
                h1.insert(i);

        h1.merge(h2);
        for (i = 1; i < numItems; i++)
            if (h1.deleteMin() != i)
                System.out.println("Oops! " + i);
    }
}