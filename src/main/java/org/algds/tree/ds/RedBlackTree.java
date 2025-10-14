package org.algds.tree.ds;

/**
 * 红黑树实现 - 自顶向下
 *
 * @param <T>
 */
public class RedBlackTree<T extends Comparable<? super T>> {

    // 1 内部结点定义 ****************************************************************************************************
    private static final int BLACK = 1;
    private static final int RED = 0;

    private static class RedBlackNode<T> {

        RedBlackNode(T theElement) {
            this(theElement, null, null);
        }

        RedBlackNode(T theElement, RedBlackNode<T> lt, RedBlackNode<T> rt) {
            element = theElement;
            left = lt;
            right = rt;
            color = RedBlackTree.BLACK;
        }

        T element;
        RedBlackNode<T> left;
        RedBlackNode<T> right;
        int color;
    }


    // 2 核心结构定义 ****************************************************************************************************

    private RedBlackNode<T> header; // 头结点，header.right 引用红黑树根结点
    private RedBlackNode<T> nullNode; // 空结点，空对象设计模式

    // 自顶向下红黑树不需要叔叔结点，只需要当前结点上游结点引用即可
    private RedBlackNode<T> current; // 当前结点
    private RedBlackNode<T> parent; // 父节点
    private RedBlackNode<T> grand; // 祖父结点
    private RedBlackNode<T> great; // 曾祖父结点


    public RedBlackTree() {
        nullNode = new RedBlackNode<>(null);
        nullNode.left = nullNode;
        nullNode.right = nullNode;

        header = new RedBlackNode<>(null);
        header.left = nullNode;
        header.right = nullNode;
    }

    private int compare(T item, RedBlackNode<T> t) {
        if (t == header)
            return 1;
        else
            return item.compareTo(t.element);
    }


    // 3 核心方法区 *****************************************************************************************************

    /**
     * 向红黑树插入结点
     *
     * @param item
     */
    public void insert(T item) {
        current = parent = grand = header; // 自顶向下插入
        nullNode.element = item; // 保存临时数据，完成下面退出条件

        while (compare(item, current) != 0) { // 退出条件？
            // 向下前进一个深度
            great = grand;
            grand = parent;
            parent = current;
            current = compare(item, current) < 0 ? current.left : current.right;

            // 当遇到两个儿子都是红色结点时 执行旋转+变色动作
            if (current.left.color == RED && current.right.color == RED) { // 当前结点的两个儿子是红色
                handleReorient(item); // 执行调整
            }
        }

        if (current != nullNode) // 这就说明遍历到了最后也没有发现item结点，所以下面可以插入数据了
            return;

        current = new RedBlackNode<>(item, nullNode, nullNode);

        if (compare(item, parent) < 0) {
            parent.left = current;
        } else {
            parent.right = current;
        }

        /**
         * 插入叶子结点是红色，父节点也是红色将引发调整
         */
        handleReorient(item);
    }

    public void remove(T x) {
        throw new UnsupportedOperationException();
    }

    public T findMin() {
        if (isEmpty())
            throw new RuntimeException();

        RedBlackNode<T> itr = header.right;

        while (itr.left != nullNode)
            itr = itr.left;

        return itr.element;
    }

    public T findMax() {
        if (isEmpty())
            throw new RuntimeException();

        RedBlackNode<T> itr = header.right;

        while (itr.right != nullNode)
            itr = itr.right;

        return itr.element;
    }

    public boolean contains(T x) {
        nullNode.element = x;
        current = header.right;

        for (; ; ) {
            if (x.compareTo(current.element) < 0)
                current = current.left;
            else if (x.compareTo(current.element) > 0)
                current = current.right;
            else if (current != nullNode)
                return true;
            else
                return false;
        }
    }

    public boolean isEmpty() {
        return header.right == nullNode;
    }

    public void makeEmpty() {
        header.right = nullNode;
    }

    public void printTree() {
        if (isEmpty())
            System.out.println("Empty tree");
        else
            System.out.print("Red Black tree: ");
            printTree(header.right);
    }

    private void printTree(RedBlackNode<T> t) {
        if (t != nullNode) {
            printTree(t.left);
            System.out.print(t.element + " ");
            printTree(t.right);
        }
    }


    // 4 重要方法区(变色 + 旋转) ******************************************************************************************
    private void handleReorient(T item) {
        // 执行变色动作
        current.color = RED;
        current.left.color = BLACK;
        current.right.color = BLACK;

        /**
         * 当前结点和父节点都是红色将会引发调整，调整原理如下所示：
         *
         * 一字型(zig-zig) 带.表示是红色结点
         *            G               P
         *           / \             / \
         *         .P   S           .x .G
         *         / \  |            | / \
         *       .x   B C   -->      A B  S
         *        |                       |
         *        A                       C
         *
         * 之字形(zig-zag)
         *           G                      x
         *          / \                   /   \
         *         .P   S     -->       .P    .G
         *         | \  |               / \   / \
         *         A .x C              A  B1  B2 S
         *           / \                         |
         *          B1  B2                       C
         */
        if (parent.color == RED) { // 当前结点时红色 并且 父节点也是红色（祖父一定是黑色），违反红黑树规则，需要执行调整
            grand.color = RED; // 调整祖父为红色,因为不管是一字型还是之字形旋转后都是变为红色结点
            /**
             * 满足下面两种情况
             *      G       G
             *     /         \
             *    P    或     P
             *     \         /
             *      X       X
             */
            if ((compare(item, grand) < 0) != (compare(item, parent) < 0)) { // 之字型旋转
                parent = rotate(item, grand); // zig-zag 格式需要完成两次旋转，这里执行第一次，传入祖父为了后边建立链
                /** 之字形第一次旋转
                 *             G
                 *            /
                 *           x
                 *          /
                 *         P
                 */
            }
            current = rotate(item, great); // zig-zag 的第二次旋转 || 或者 zig-zig格式的一次旋转
            current.color = BLACK;
        }

        header.right.color = BLACK; // 根节点始终是黑色
    }


    private RedBlackNode<T> rotate(T item, RedBlackNode<T> parent) {
        if (compare(item, parent) < 0)
            return parent.left = compare(item, parent.left) < 0 ?
                    rotateWithLeftChild(parent.left) :  // LL
                    rotateWithRightChild(parent.left);  // LR (parent.left = P)
        else
            return parent.right = compare(item, parent.right) < 0 ?
                    rotateWithLeftChild(parent.right) :  // RL
                    rotateWithRightChild(parent.right);  // RR
    }

    /**
     * 右旋转（处理LL情况）
     *     k2          k1
     *    /           / \
     *   k1    -->   o1  k2
     *  /
     * o1
     */
    private RedBlackNode<T> rotateWithLeftChild(RedBlackNode<T> k2) {
        RedBlackNode<T> k1 = k2.left;
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
    private RedBlackNode<T> rotateWithRightChild(RedBlackNode<T> k1) {
        RedBlackNode<T> k2 = k1.right;
        k1.right = k2.left;
        k2.left = k1;
        return k2;
    }


    // 5 单元测试 *******************************************************************************************************
    public static void main(String[] args) {
        RedBlackTree<Integer> t = new RedBlackTree<>();
        final int NUMS = 50;
        final int GAP = 3;

        System.out.println("Checking... (no more output means success)");

        t.printTree();

        for (int i = GAP; i != 0; i = (i + GAP) % NUMS)
            t.insert(i);

        t.printTree();

        if (t.findMin() != 1 || t.findMax() != NUMS - 1)
            System.out.println("FindMin or FindMax error!");

        for (int i = 1; i < NUMS; i++)
            if (!t.contains(i))
                System.out.println("Find error1!");
    }
}
