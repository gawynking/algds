package org.algds.tree.ds;

/**
 * 二叉堆
 *      结构性：完全二叉树
 *      堆序性：父节点大于任意子结点
 *
 *  二叉堆由一个泛型数组和一个整形容量变量构成
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
        array = (T[]) new Comparable[capacity + 1]; // 二叉堆从索引1开始，因此构建数组时需要加1更好
    }

    public BinaryHeap(T[] items) {
        // 构建堆结构
        currentSize = items.length;
        array = (T[]) new Comparable[(currentSize + 2) * 11 / 10]; // +2 表示0位和最后一个空位提前预创建; *11/10 表示容量扩展10%

        int i = 1;
        for (T item : items)
            array[i++] = item;
        // 构建堆序
        buildHeap();
    }


    // 2 核心方法 *******************************************************************************************************
    // 插入新元素，上滤算法实现
    public void insert(T x) {
        if (currentSize == array.length - 1)
            enlargeArray(array.length * 2 + 1);

        int hole = ++currentSize;
        for (array[0] = x; x.compareTo(array[hole / 2]) < 0; hole /= 2)
            array[hole] = array[hole / 2];
        array[hole] = x;
    }

    // 删除最小值，下滤算法实现
    public T deleteMin() {
        if (isEmpty())
            throw new RuntimeException();

        T minItem = findMin();
        array[1] = array[currentSize--]; // 删除第一个元素并且将最后一个元素移动到1位置
        percolateDown(1); // 从索引1开始执行下滤算法

        return minItem;
    }

    // 下滤算法
    private void percolateDown(int hole) {
        int child;
        T tmp = array[hole];

        for (; hole * 2 <= currentSize; hole = child) {
            child = hole * 2;
            if (child != currentSize && array[child + 1].compareTo(array[child]) < 0) // 存在两个儿子，取最小儿子索引+1，否则不加1
                child++;
            if (array[child].compareTo(tmp) < 0) // 最小儿子与当前结点比对
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
        // 从最后一个非叶子结点开始执行下滤，当执行到根则构建二叉堆数组完成堆序排列
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

    // 测试用
    public int getCurrentSize(){
        return currentSize;
    }

    public T getIndex(int index){
        if(index > currentSize) throw new RuntimeException();
        return array[index];
    }

    // 4 单元测试 *******************************************************************************************************
    public static void main(String[] args) {
        int numItems = 10;
        BinaryHeap<Integer> h = new BinaryHeap<Integer>();
        int i = 37;

        for (i = 37; i != 0; i = (i + 37) % numItems) {
            h.insert(i);
            System.out.print(String.format("当前(%s)树 => ",i));
            for(int j = 1; j<=h.getCurrentSize(); j++){
                System.out.print(h.getIndex(j)+" ");
            }
            System.out.println("");
        }

        System.out.println("----------------------------------");

        for (i = 1; i < numItems; i++) {
            Integer data = h.deleteMin();
            if (data != i)
                System.out.println(String.format("Index = %s; Data = %s.", i, data));
        }
    }

}