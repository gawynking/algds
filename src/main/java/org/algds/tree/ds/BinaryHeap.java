package org.algds.tree.ds;

/**
 * 二叉堆
 *      结构性：完全二叉树
 *      堆序性：父节点大于任意子结点
 *
 * @param <T>
 */
public class BinaryHeap<T extends Comparable<? super T>> {

    // 1 二叉堆结构定义 **************************************************************************************************
    private static final int DEFAULT_CAPACITY = 10;

    private int currentSize;
    private T[] array;


    public BinaryHeap() {
        this(DEFAULT_CAPACITY);
    }


    public BinaryHeap(int capacity) {
        currentSize = 0;
        array = (T[]) new Comparable[capacity + 1];
    }


    public BinaryHeap(T[] items) {
        currentSize = items.length;
        array = (T[]) new Comparable[(currentSize + 2) * 11 / 10];

        int i = 1;
        for (T item : items)
            array[i++] = item;
        buildHeap();
    }


    // 2 核心方法 *******************************************************************************************************
    // 插入新元素
    public void insert(T x) {
        if (currentSize == array.length - 1)
            enlargeArray(array.length * 2 + 1);

        int hole = ++currentSize;
        for (array[0] = x; x.compareTo(array[hole / 2]) < 0; hole /= 2)
            array[hole] = array[hole / 2];
        array[hole] = x;
    }

    // 删除最小值
    public T deleteMin() {
        if (isEmpty())
            throw new RuntimeException();

        T minItem = findMin();
        array[1] = array[currentSize--];
        percolateDown(1);

        return minItem;
    }

    // 下滤算法
    private void percolateDown(int hole) {
        int child;
        T tmp = array[hole];

        for (; hole * 2 <= currentSize; hole = child) {
            child = hole * 2;
            if (child != currentSize &&
                    array[child + 1].compareTo(array[child]) < 0)
                child++;
            if (array[child].compareTo(tmp) < 0)
                array[hole] = array[child];
            else
                break;
        }
        array[hole] = tmp;
    }


    // 3 其他方法 *******************************************************************************************************
    // 数组扩容
    private void enlargeArray(int newSize) {
        T[] old = array;
        array = (T[]) new Comparable[newSize];
        for (int i = 0; i < old.length; i++)
            array[i] = old[i];
    }

    // 查找最小元素
    public T findMin() {
        if (isEmpty())
            throw new RuntimeException();
        return array[1];
    }

    // 构建二叉堆
    private void buildHeap() {
        for (int i = currentSize / 2; i > 0; i--)
            percolateDown(i);
    }

    // 堆是否为空
    public boolean isEmpty() {
        return currentSize == 0;
    }

    // 使堆为空
    public void makeEmpty() {
        currentSize = 0;
    }


    // 4 单元测试 *******************************************************************************************************
    public static void main(String[] args) {
        int numItems = 100;
        BinaryHeap<Integer> h = new BinaryHeap<Integer>();
        int i = 37;

        for (i = 37; i != 0; i = (i + 37) % numItems) {
            h.insert(i);
            System.out.print(String.format("当前(%s)树"));
            for (Integer item : h.array) {
                System.out.print(item + " ");
            }
        }
        for (i = 1; i < numItems; i++)
            if (h.deleteMin() != i)
                System.out.println("Oops! " + i);
    }

}