package org.algds.tree.model.binary;

import java.util.Arrays;

/**
 * 顺序存储结构: 二叉树的数组表示（底层用泛型数组）
 *
 */
public class BinaryTreeArray<T> {

    private T[] array;   // 存储树节点的数组
    private int size;    // 实际元素个数

    @SuppressWarnings("unchecked")
    public BinaryTreeArray(int capacity) {
        // 泛型数组需要通过 Object[] 强转
        this.array = (T[]) new Object[capacity];
        this.size = 0;
    }

    public BinaryTreeArray() {
        this(16);
    }

    // 扩容方法
    @SuppressWarnings("unchecked")
    private void ensureCapacity(int minCapacity) {
        if (minCapacity > array.length) {
            int newCapacity = Math.max(array.length * 2, minCapacity);
            array = Arrays.copyOf(array, newCapacity);
        }
    }

    // 设置节点值
    public void set(int index, T value) {
        ensureCapacity(index + 1);
        array[index] = value;
        if (index >= size) {
            size = index + 1;
        }
    }

    // 获取节点值
    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        return array[index];
    }

    // 获取左孩子索引
    public int leftIndex(int index) {
        return 2 * index + 1;
    }

    // 获取右孩子索引
    public int rightIndex(int index) {
        return 2 * index + 2;
    }

    // 获取左孩子值
    public T left(int index) {
        int li = leftIndex(index);
        return get(li);
    }

    // 获取右孩子值
    public T right(int index) {
        int ri = rightIndex(index);
        return get(ri);
    }

    // 获取父节点索引
    public int parentIndex(int index) {
        if (index == 0) return -1; // 根节点无父
        return (index - 1) / 2;
    }

    // 获取父节点值
    public T parent(int index) {
        int pi = parentIndex(index);
        return get(pi);
    }

    // 获取树大小
    public int size() {
        return size;
    }

    // 打印数组内容
    @Override
    public String toString() {
        return Arrays.toString(Arrays.copyOf(array, size));
    }
}
