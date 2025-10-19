package org.algds.tree.ds;

/**
 * 二项队列
 *
 * @param <T>
 */
public final class BinomialQueue<T extends Comparable<? super T>> {

    /**
     * 孩子兄弟链表结构
     *
     * @param <T>
     */
    private static class BinNode<T> {

        BinNode(T theElement) {
            this(theElement, null, null);
        }

        BinNode(T theElement, BinNode<T> lt, BinNode<T> nt) {
            element = theElement;
            leftChild = lt;
            nextSibling = nt;
        }

        T element;              // 数据
        BinNode<T> leftChild;   // 第一个儿子
        BinNode<T> nextSibling; // 第一个儿子得下一个兄弟
    }

    private static final int DEFAULT_TREES = 1;

    private int currentSize;
    private BinNode<T>[] theTrees;


    public BinomialQueue() {
        theTrees = new BinNode[DEFAULT_TREES];
        makeEmpty();
    }

    public BinomialQueue(T item) {
        currentSize = 1;
        theTrees = new BinNode[1];
        theTrees[0] = new BinNode<T>(item, null, null);
    }



    /**
     * 合并例程
     *
     * @param rhs
     */
    public void merge(BinomialQueue<T> rhs) {
        if (this == rhs)    // Avoid aliasing problems
            return;

        currentSize += rhs.currentSize;
        if (currentSize > capacity()) {
            int newNumTrees = Math.max(theTrees.length, rhs.theTrees.length) + 1;
            expandTheTrees(newNumTrees);
        }

        // 8种场景
        BinNode<T> carry = null;
        for (int i = 0, j = 1; j <= currentSize; i++, j *= 2) {
            BinNode<T> t1 = theTrees[i];
            BinNode<T> t2 = i < rhs.theTrees.length ? rhs.theTrees[i] : null;

            int whichCase = t1 == null ? 0 : 1;
            whichCase += t2 == null ? 0 : 2;
            whichCase += carry == null ? 0 : 4;

            switch (whichCase) {
                case 0: /* No trees */
                case 1: /* Only this */
                    break;
                case 2: /* Only rhs */
                    theTrees[i] = t2;
                    rhs.theTrees[i] = null;
                    break;
                case 4: /* Only carry */
                    theTrees[i] = carry;
                    carry = null;
                    break;
                case 3: /* this and rhs */
                    carry = combineTrees(t1, t2);
                    theTrees[i] = rhs.theTrees[i] = null;
                    break;
                case 5: /* this and carry */
                    carry = combineTrees(t1, carry);
                    theTrees[i] = null;
                    break;
                case 6: /* rhs and carry */
                    carry = combineTrees(t2, carry);
                    rhs.theTrees[i] = null;
                    break;
                case 7: /* All three */
                    theTrees[i] = carry;
                    carry = combineTrees(t1, t2);
                    rhs.theTrees[i] = null;
                    break;
            }
        }

        for (int k = 0; k < rhs.theTrees.length; k++) {
            rhs.theTrees[k] = null;
        }
        rhs.currentSize = 0;
    }

    /**
     * 用来合并两个独立的树
     *
     * @param t1
     * @param t2
     * @return
     */
    private BinNode<T> combineTrees(BinNode<T> t1, BinNode<T> t2) {
        if (t1.element.compareTo(t2.element) > 0)
            return combineTrees(t2, t1);
        t2.nextSibling = t1.leftChild;
        t1.leftChild = t2;
        return t1;
    }

    /**
     * 对树扩容
     *
     * @param newNumTrees
     */
    private void expandTheTrees(int newNumTrees) {
        BinNode<T>[] old = theTrees;
        int oldNumTrees = theTrees.length;

        theTrees = new BinNode[newNumTrees];
        for (int i = 0; i < Math.min(oldNumTrees, newNumTrees); i++)
            theTrees[i] = old[i];
        for (int i = oldNumTrees; i < newNumTrees; i++)
            theTrees[i] = null;
    }


    /**
     * 插入结点
     *
     * @param x
     */
    public void insert(T x) {
        merge(new BinomialQueue<T>(x));
    }

    /**
     * 查找小小结点
     *
     * @return
     */
    public T findMin() {
        if (isEmpty())
            throw new RuntimeException();
        return theTrees[findMinIndex()].element;
    }

    private int findMinIndex() {
        int i;
        int minIndex;

        for (i = 0; theTrees[i] == null; i++){}

        for (minIndex = i; i < theTrees.length; i++) {
            if (theTrees[i] != null && theTrees[i].element.compareTo(theTrees[minIndex].element) < 0) {
                minIndex = i;
            }
        }

        return minIndex;
    }

    public T deleteMin() {
        if (isEmpty())
            throw new RuntimeException();

        int minIndex = findMinIndex();
        T minItem = theTrees[minIndex].element;

        BinNode<T> deletedTree = theTrees[minIndex].leftChild;

        BinomialQueue<T> deletedQueue = new BinomialQueue<T>();
        deletedQueue.expandTheTrees(minIndex);

        deletedQueue.currentSize = (1 << minIndex) - 1;
        for (int j = minIndex - 1; j >= 0; j--) {
            deletedQueue.theTrees[j] = deletedTree;
            deletedTree = deletedTree.nextSibling;
            deletedQueue.theTrees[j].nextSibling = null;
        }

        theTrees[minIndex] = null;
        currentSize -= deletedQueue.currentSize + 1;

        merge(deletedQueue);

        return minItem;
    }

    public boolean isEmpty() {
        return currentSize == 0;
    }

    public void makeEmpty() {
        currentSize = 0;
        for (int i = 0; i < theTrees.length; i++)
            theTrees[i] = null;
    }

    private int capacity() {
        return (1 << theTrees.length) - 1;
    }



    public static void main(String[] args) {
        int numItems = 10000;
        BinomialQueue<Integer> h = new BinomialQueue<Integer>();
        BinomialQueue<Integer> h1 = new BinomialQueue<Integer>();
        int i = 37;

        System.out.println("Starting check.");

        for (i = 37; i != 0; i = (i + 37) % numItems)
            if (i % 2 == 0)
                h1.insert(i);
            else
                h.insert(i);

        h.merge(h1);
        for (i = 1; i < numItems; i++)
            if (h.deleteMin() != i)
                System.out.println("Oops! " + i);

        System.out.println("Check done.");
    }
}
