package org.algds.tree.ds;

/**
 * 伸展树实现
 *
 * @param <T>
 */
public class SplayTree<T extends Comparable<? super T>> {

    // 0 单元测试 *******************************************************************************************************
    public static void main(String[] args) {
        SplayTree<Integer> t = new SplayTree<Integer>();
        final int NUMS = 40000;
        final int GAP = 307;

        System.out.println("Checking... (no bad output means success)");

        for (int i = GAP; i != 0; i = (i + GAP) % NUMS)
            t.insert(i);
        System.out.println("Inserts complete");

        for (int i = 1; i < NUMS; i += 2)
            t.remove(i);
        System.out.println("Removes complete");

        if (t.findMin() != 2 || t.findMax() != NUMS - 2)
            System.out.println("FindMin or FindMax error!");

        for (int i = 2; i < NUMS; i += 2)
            if (!t.contains(i))
                System.out.println("Error: find fails for " + i);

        for (int i = 1; i < NUMS; i += 2)
            if (t.contains(i))
                System.out.println("Error: Found deleted item " + i);
    }

    // 1 核心定义 *******************************************************************************************************
    // 节点类定义
    private static class BinaryNode<T> {
        BinaryNode(T theElement) {
            this(theElement, null, null);
        }

        BinaryNode(T theElement, BinaryNode<T> lt, BinaryNode<T> rt) {
            element = theElement;
            left = lt;
            right = rt;
        }

        T element;
        BinaryNode<T> left;
        BinaryNode<T> right;
    }


    public SplayTree() {
        nullNode = new BinaryNode<T>(null);
        nullNode.left = nullNode;
        nullNode.right = nullNode;
        root = nullNode;
    }

    private BinaryNode<T> root;
    private BinaryNode<T> nullNode; // 空对象设计模式的经典应用案例
    private BinaryNode<T> newNode = null; // 插入依赖
    private BinaryNode<T> header = new BinaryNode<T>(null); // 伸展依赖


    // 3 核型方法实现 ****************************************************************************************************
    public void makeEmpty() {
        root = nullNode;
    }

    public boolean isEmpty() {
        return root == nullNode;
    }


    public void insert(T x) {
        if (newNode == null)
            newNode = new BinaryNode<T>(null);
        newNode.element = x;

        if (root == nullNode) {
            newNode.left = nullNode;
            newNode.right = nullNode;
            root = newNode;
        } else {
            // 展开树 如果x存在则将x移动到根节点；否则将接近x的节点到根节点
            root = splay(x, root);

            int compareResult = x.compareTo(root.element);

            if (compareResult < 0) { // 1 说明插入的x小于根节点
                newNode.left = root.left;
                newNode.right = root;
                root.left = nullNode;
                root = newNode;
            } else if (compareResult > 0) { // 2 说明插入的x大于根节点
                newNode.right = root.right;
                newNode.left = root;
                root.right = nullNode;
                root = newNode;
            } else { // 3 说明插入x已经存在，直接返回
                return;
            }
        }
        newNode = null;
    }

    public void remove(T x) {
        // 如果不包含就直接返回，contains动作包含splay方法引用，会将x移动到根节点，删除也仅是删除根节点
        if (!contains(x))
            return;

        BinaryNode<T> newTree;

        if (root.left == nullNode)
            newTree = root.right;
        else {
            newTree = root.left;
            newTree = splay(x, newTree); // 这个动作会将小于x的最大值移动的根节点
            newTree.right = root.right;
        }
        root = newTree;
    }

    public T findMin() {
        if (isEmpty())
            throw new RuntimeException();

        BinaryNode<T> ptr = root;

        while (ptr.left != nullNode)
            ptr = ptr.left;

        root = splay(ptr.element, root); // 把最小值移动到根
        return ptr.element;
    }

    public T findMax() {
        if (isEmpty())
            throw new RuntimeException();

        BinaryNode<T> ptr = root;

        while (ptr.right != nullNode)
            ptr = ptr.right;

        root = splay(ptr.element, root); // 把最大值移动到根
        return ptr.element;
    }

    public boolean contains(T x) {
        if (isEmpty())
            return false;
        root = splay(x, root);
        return root.element.compareTo(x) == 0;
    }

    /**
     * 伸展树展开算法 - 核心算法
     *
     * 伸展树展开原理
     *
     * 一 自底向上展开策略
     *
     * 1 单旋转
     *             P                 x
     *            / \               / \
     *           x   C    -->      A   P
     *          / \                   / \
     *         A   B                 B   C
     *
     * 2 一字型(zig-zig)
     *            G               x
     *           / \             / \
     *          P   D           A   P
     *         / \                 / \
     *        x   C      -->      B   G
     *       / \                     / \
     *      A   B                   C   D
     *
     * 3 之字形(zig-zag)
     *           G                    x
     *          / \                  / \
     *         P   D     -->        P   G
     *        / \                  / \  / \
     *       A   x                 A  B C  D
     *          / \
     *         B   C
     *
     *
     * 二 自顶向下展开策略
     *
     * 1 单旋转
     *            x                     y      R
     *     L     / \    R   -->    L    |     /
     *          y   B                   A    x
     *          |                             \
     *          A                              B
     *
     * 2 一字形(zig-zig)
     *            x
     *           / \                        R
     *          y   C                z     /
     *     L   / \     R   -->   L   |    y
     *        z   B                  A     \
     *        |                             x
     *        A                            / \
     *                                    B   C
     *
     * 3 之字形(zig-zag)
     * type-1: 更彻底
     *            x
     *           / \               L            R
     *     L    y   C   R   -->     \     z    /
     *         / \                   y    |   x
     *        A   z                 /     B    \
     *            |                A            C
     *            B
     *
     * type2: 更简单
     *            x
     *           / \                     y
     *     L    y   C   R   -->    L    / \      R
     *         / \                     A   z    /
     *        A   z                        |   x
     *            |                        B    \
     *            B                              C
     *
     *
     * 4 组装
     *           x                      x
     *     L    / \   R   -->         /   \
     *         A   B                 L     R
     *                                \   /
     *                                 A B
     *
     */
    private BinaryNode<T> splay(T x, BinaryNode<T> t) {

        BinaryNode<T> leftTreeMax; // 柞树最大<中间树最小
        BinaryNode<T> rightTreeMin; // 右树最小>中间树最大

        // header分别引用左右树；初始转换左右树都是空树
        header.left = nullNode;
        header.right = nullNode;

        leftTreeMax = header;
        rightTreeMin = header;

        nullNode.element = x;

        // 应用自顶向下展开
        while(true) {
            int compareResult = x.compareTo(t.element);

            if (compareResult < 0) { // 1 展开节点小于根节点，即只能在左子树中
                if (x.compareTo(t.left.element) < 0) // 展开几点小于左子树根节点，即匹配到模式LL(?<-tl<-t)，执行右旋转 t = (?<-tl->t)
                    t = rotateWithLeftChild(t);
                if (t.left == nullNode) // 新根即tl的左子树为空，则说明不存在比x更小的元素，直接退出
                    break;

                // 新树根赋值 rightTreeMin 及其 left
                rightTreeMin.left = t;
                rightTreeMin = t;

                t = t.left; // tl 存在左子树，则向下移动t为t的左子树，继续展开后续节点
            } else if (compareResult > 0) { // 2 展开节点大于根节点，即只能在右子树中
                if (x.compareTo(t.right.element) > 0) // 展开几点大于右子树根节点，即匹配到模式RR(t->tr->?)，执行左旋转 t = (t<-tr->?)
                    t = rotateWithRightChild(t);
                if (t.right == nullNode) // 新根即tr的右子树为空，则说明不存在比x更大的元素，直接退出
                    break;

                // 新树赋值给 leftTreeMax和leftTreeMax的right
                leftTreeMax.right = t;
                leftTreeMax = t;

                t = t.right; // tr 存在右子树，则向下移动t为t的右子树，继续展开后续节点
            } else { // 3 展开节点等于根节点，即展开几点即为根节点，无需其他动作
                break;
            }
        }

        // 组装树返回
        leftTreeMax.right = t.left;
        rightTreeMin.left = t.right;
        // header left 和 header right分别引用 R和L 的根
        t.left = header.right;
        t.right = header.left;

        return t;
    }

    /**
     * 右旋转（处理LL情况）
     *     k2          k1
     *    /           / \
     *   k1    -->   o1  k2
     *  /
     * o1
     */
    private static <T> BinaryNode<T> rotateWithLeftChild(BinaryNode<T> k2) {
        BinaryNode<T> k1 = k2.left;
        k2.left = k1.right;
        k1.right = k2;
        return k1;
    }

    /**
     * 左旋转（处理RR情况）
     * k1                k2
     *  \               / \
     *   k2   -->      k1  o1
     *    \
     *     o1
     */
    private static <T> BinaryNode<T> rotateWithRightChild(BinaryNode<T> k1) {
        BinaryNode<T> k2 = k1.right;
        k1.right = k2.left;
        k2.left = k1;
        return k2;
    }

}